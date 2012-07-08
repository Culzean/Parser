package gameEngine;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

public class SoundManager {

	//using a hashmap where the key is sounds/filename.ogg
	
	AssetManager assetManager = null;
	HashMap<String, Integer> soundPoolMap;
	SoundPool mainSndPool = null;
	
	public SoundManager( AssetManager refAM )
	{
		assetManager = refAM;
		mainSndPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		soundPoolMap = new HashMap<String, Integer>();
	}
	
	public int LoadSound(String fname)
	{
		try{
			AssetFileDescriptor descriptor = assetManager.openFd(fname);
			soundPoolMap.put( fname, mainSndPool.load(descriptor, 1) );
		} catch ( IOException ex ){
			//how to print text?
			System.out.println("Could not load a sound file " + ex.getMessage());
		}
		return soundPoolMap.get(fname);
	}
	
	public int GetSoundID(String fname)
	{
		if(soundPoolMap.get(fname) == null)
		{
			return LoadSound(fname);
		}
		else
		return soundPoolMap.get(fname);
	}
	
	public boolean PlaySound( int SoundID, int loop, float PlyRate )
	{
		mainSndPool.play(SoundID, (float)0.99, (float)0.99, 1, loop, PlyRate);
		return true;
	}
	
	public boolean PlaySound( int SoundID, int loop )
	{
		mainSndPool.play(SoundID,(float)0.99, (float)0.99, 0, loop, 1);
		return true;
	}
	
	public boolean PlaySound( int SoundID )
	{
		mainSndPool.play(SoundID,(float)0.99, (float)0.99, 0, 0, 1);
		return true;
	}
	
	public boolean PlaySound( int SoundID, float leftVol, float rightVol,  int loop, float PlyRate )
	{
		mainSndPool.play(SoundID, leftVol, rightVol, 1, loop, PlyRate);
		return true;
	}
	public void ReleaseSounds()
	{
		mainSndPool.release();
	}
}
