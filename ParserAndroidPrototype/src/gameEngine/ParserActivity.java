package gameEngine;

import android.R;
import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
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
    	DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		gameView = new ParserView(this, dm.widthPixels, dm.heightPixels);
        setContentView(gameView);
    }
    
    public void onDestroy()
    {
    	super.onDestroy();
    	gameView.stopGame();
    }
    
    public void onStart()
    {
    	super.onStart();
    }
    
    /*public void onStop()
    {
    	
    }
    
    public void onPause()
    {
    	
    }
    
    public void onResume()
    {
    		
    }*/
}