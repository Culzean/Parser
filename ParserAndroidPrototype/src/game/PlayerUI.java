package game;

import events.PlayerTraj;
import events.Vector2D;
import gameEngine.CollisionControl;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.SensorEvent;
import android.view.MotionEvent;

import java.lang.Math;
import java.util.Iterator;
import java.util.LinkedList;

public class PlayerUI 
{
	
	public final int MEASURE = 20;
	public final float MAX_RADIUS = 300.0f;
	
	private float[] xVals = new float[MEASURE];
	private float[] yVals = new float[MEASURE];
	private float[] zVals = new float[MEASURE];
	
	private LinkedList<PlayerTraj> inputList = new LinkedList<PlayerTraj>();
	final int MAX_INPUTS = 3;
	private int inputIndex = 0;
	private int outputIndex = 0;
	private PlayerTraj[] inputArray = new PlayerTraj[MAX_INPUTS];
	
	private int counter = 0;
	
	private float[] readVar = new float[3];
	
	private int heartX;
	private int heartY;
	private int heartRad;
	private Paint lineColour = new Paint();
	
	private Vector2D start;
	private Vector2D end;
	private boolean drag = false;
	PlayerTraj tempTraj = null;
	
	private GameModel gameModel = null;
	private Heart heartRef = null;
	
	//class will determine which UI system is in use.
	//on update it will read the correct input and deliver a vector to assign directions to new cells
	//Will also read player input for choice of cell
	//
	//This class will provide a new vector that can be assigned to each cell
	//there are several ways to arrive at this value
	//here I will implement angular acceleration from the y axis
	//and scale from the x axis
	//
	//could also be calculated by two x and y scalars. which is simpler
	
	public PlayerUI(GameModel _model, Heart _heartRef)
	{
		gameModel = _model;
		heartRef = _heartRef;
		heartX = heartRef.getX();	heartY = heartRef.getY();	heartRad = heartRef.getRadius();
		lineColour.setColor(Color.argb(180, 50, 50, 50));
		lineColour.setStrokeWidth(4);
		start = new Vector2D(0,0);
		end = new Vector2D(0,0);
		Init();
	}
	
	private void Init()
	{
		for(int i =0; i<MAX_INPUTS; ++i)
			inputArray[i] = new PlayerTraj(heartX, heartY);
		end.x = heartX;		end.y = heartY;
	}
	
	public PlayerTraj update( long dt)
	{
		float y = (float) ((gameModel.getCalibrateX() - getAverageRead(xVals)) * 0.1);
		float x = (float) ((gameModel.getCalibrateY() - getAverageRead(yVals)) * 0.1);
		
		//playerInput.x = MAX_RADIUS* -x;
		//playerInput.y = MAX_RADIUS* -y;
		
		if(inputIndex >= MAX_INPUTS)
			inputIndex = 0;
		
		if(end.x != heartX && end.y != heartY)
		{
			inputArray[inputIndex].setVector((int)(end.x - start.x), (int)(end.y - start.y));
			outputIndex = inputIndex;
			++inputIndex;
			end.x = heartX;	end.y = heartY;
		}
		
		if(gameModel.getPlayerIn() == null)
			{
			//then maybe we need to add a new input
				if(inputArray[outputIndex].isInPlay())
				{
					
					gameModel.setPlayerIn(inputArray[outputIndex]);
					if(--outputIndex < 0)
						outputIndex = inputArray.length-1;
				}
			}
		
		return gameModel.getPlayerIn();
	}
	
	public void setValues(SensorEvent event)
	{
		//record a series of values from the accelerometer
		//Then take the average
		//there is a balance here between smooth values, delay to respond and performance
		xVals[counter] = event.values[0];
		yVals[counter] = event.values[1];
		zVals[counter] = event.values[2];
		if(++counter >= MEASURE)
			counter = 0;
	}
	
	public float getAverageRead(final float[] values)
	{
		float av = 0;
		float max = 0; float min = 0;
		for(int i =0; i<MEASURE; i++)
			{
				av += values[i];
				if(values[i] > max)
					max = values[i];
				else if(values[i] < min)
					min= values[i];
			}
		readVar[1]  = max - min;
		return av / MEASURE;
	}
	
	public void touch(MotionEvent event)
	{
		int e = event.getAction();
		
		switch(e)
		{
		case MotionEvent.ACTION_DOWN:
			if(!drag && CollisionControl.CircleVsPoint(heartRef, (int)event.getX(), (int)event.getY()))
			{
				start.x = (int) event.getX();
				start.y = (int) event.getY();
				drag = !drag;
			}
			break;
		case MotionEvent.ACTION_MOVE:
				
			break;
		
		case MotionEvent.ACTION_UP:
			if(drag)
			{
				drag = false;
				end.x = event.getX();
				end.y = event.getY();
			}
			
			break;
			
			default:
				
			break;
		};
	}
	
	public void draw(Canvas g)
	{
		//Iterator<PlayerTraj> itr = inputList.iterator();
		for(int i = 0; i < MAX_INPUTS ; ++i)
		{
			inputArray[i].draw(g);
		}
		//g.drawLine((int)heartX, (int)heartY, (int)playerInput.x + heartX, (int)(heartY + playerInput.y), lineColour);
	}
}
