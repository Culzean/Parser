package gameEngine;

import game.GameModel;
import game.GameState;
import game.MenuState;
import game.State;
import android.R;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

public class ParserView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener, 
													View.OnTouchListener, View.OnLongClickListener
{
	//Window resolution
	public static int windowWidth;
	public static int windowHeight;
	
	//Thread
	private ParserThread parserThread;
	
	private String textView;
	private StringBuilder builder = new StringBuilder();
	//Gamestate
	private State gameState;
	private GameModel gameModel;
	
	private int fps = 0;
	private long eventTime = 0;
	
	AssetManager assetManager;
	
	//Useful colors
	public static final Paint textColor = new Paint();
	
    public ParserView(Context context, int wWidth, int wHeight) 
    {
        super(context);
        getHolder().addCallback(this);
        windowWidth = wWidth;
        windowHeight = wHeight;
        parserThread = new ParserThread(this);
        
        gameState = new MenuState(wWidth, wHeight, this, gameModel);
        
        textView = new String();
      //  ((LinearLayout) ).addView(textView);
        
        SensorManager manager 
        = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        if(manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() == 0){
        	textView = ("There's trouble reading the accelerometer");
        }else	{
        	//create accelerometer and load into manager
        	//one more error check
        	Sensor accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
        	if(!manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME))
        		textView = ("Could not register the accelerometer on this device");
        }
        
        assetManager = context.getAssets();
        
        //Color
        textColor.setColor(Color.WHITE);
        
        setLongClickable(true);
    }
    
    public void startGame(ParserView viewRef)
    {
    	gameModel = new GameModel(getResources(), assetManager);
    	gameModel.calibrateX(((MenuState) gameState).calibrateX());
    	gameModel.calibrateY(((MenuState) gameState).calibrateY());
    	gameModel.calibrateZ(((MenuState) gameState).calibrateZ());
    	State newState = new GameState(windowWidth, windowHeight, viewRef, gameModel);
    	changeState(newState);
    }
    
	public void changeState(State newState)
	{
		gameState = newState;
	}
 
    public void doDraw(long elapsed, Canvas canvas) 
    {
    	//Clearing the screen first
    	canvas.drawColor(Color.BLACK);

    	gameState.update(elapsed);
    	gameState.gameRender(elapsed, canvas);
    	
    	//Draw the fps
    	canvas.drawText("FPS: " + fps, 10, 10, textColor);
    	canvas.drawText("Event: " + eventTime, 10, 40, textColor);
    }
    
    public void slowUpdate( long secondDelta ,int frCount )
    {
    	fps = Math.round( ((frCount * 1000f) / secondDelta) );
    	gameState.slowUpdate( secondDelta );
    }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{ 
		windowWidth = width;
        windowHeight = height;
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) 
	{
		if (!parserThread.isAlive()) 
		{
			parserThread = new ParserThread(this);
			parserThread.setRunning(true);
			parserThread.start();
	    }
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) 
	{
		if (parserThread.isAlive())
			parserThread.setRunning(false);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		if(event.getAction() == MotionEvent.ACTION_UP)
			eventTime = event.getDownTime();
		gameState.onTouch(event);
	    return super.onTouchEvent(event);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	public void stopGame()
	{
		if(null != gameModel)
			gameModel.ShutDown();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		/*builder.setLength(0);
		builder.append("x: ");
		builder.append(event.values[0]);
		builder.append(", y: ");
		builder.append(event.values[1]);
		builder.append(", z: ");
		builder.append(event.values[2]);
	
		textView = (builder.toString());
		gameState.onSensorChanged(event);
		
		textView = (builder.toString());*/
		gameState.onSensorChanged(event);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}
}
