package gameEngine;

import android.R;
import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Debug;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class ParserActivity extends Activity 
{
	ParserView gameView = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	//profiling method!
    	//Debug.startMethodTracing("parser", 20000000);
    	DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		gameView = new ParserView(this, dm.widthPixels, dm.heightPixels);
        setContentView(gameView);
    }
    
    public void onDestroy()
    {
    	super.onDestroy();
    	
    	//profiling stop
    	//Debug.stopMethodTracing();
    }
    
    public void onStart()
    {
    	super.onStart();
    }
    
    public void onStop()
    {
    	super.onStop();
    	gameView.stopGame();
    }
    
   /* public void onPause()
    {
    	
    }
    
    public void onResume()
    {
    		
    }*/
}