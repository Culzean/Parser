package gameEngine;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class ParserThread extends Thread 
{
    private ParserView parserView;
    private SurfaceHolder surfaceHolder;
    private boolean mRun = false;
    
    //Used to calculate the FPS
    private long startingTime;
    private long timeElapsed;
    private long secondCounter;
    private long currentTime;
    private int frCount;
 
    public ParserThread(ParserView pView) 
    {
    	parserView = pView;
        surfaceHolder = parserView.getHolder();
    }
 
    public void setRunning(boolean run) { mRun = run; } 
    
    @Override
    public void run() 
    {
        Canvas canvas = null;
        //Starting time
        startingTime = System.nanoTime();
        secondCounter = System.nanoTime();
        while (mRun) 
        {
        	startingTime = System.nanoTime();
	        canvas = surfaceHolder.lockCanvas();
	        if (canvas != null) 
	        {
	        	currentTime = System.nanoTime();
	        	parserView.doDraw(timeElapsed, canvas);
	            ++frCount;
	            if( (currentTime - secondCounter) > 1000000000 )
	            	{
	            		parserView.slowUpdate( ( currentTime - secondCounter ) ,frCount );
	            		frCount = 0;
	            		secondCounter = currentTime;
	            	}
	            timeElapsed = currentTime - startingTime;
	            surfaceHolder.unlockCanvasAndPost(canvas);
	        }
	        
        }
    }
}
