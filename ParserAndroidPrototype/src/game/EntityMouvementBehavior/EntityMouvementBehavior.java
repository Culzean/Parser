package game.EntityMouvementBehavior;

import events.Vector2D;
import game.Entity;

public abstract class EntityMouvementBehavior 
{
	private float angle;
	protected int move;
	protected Vector2D startDir;
	
	//Defining all movement types here
		public static final int FATCELLMOVEMENT = 1;
		public static final int FATCELLROLL = 2;
		public static final int HARMONIC = 3;
		public static final int WANDERING = 4;
	
	public int getMove()					{	return move;	};
	public void setMove(int newVal)			{	move = newVal;	};
	
	public abstract void update(Entity e1, double heartBeat, double dt);
	
	public Vector2D getStartDir()				{		return startDir;			}
	public void setStartDir(Vector2D startDir) 	{	this.startDir = startDir;		}
	public float getAngle() 					{		return angle;				}
	public void setAngle(float angle)			{		this.angle = angle;			}
}