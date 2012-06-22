package game.EntityMouvementBehavior;

import game.Entity;

public abstract class EntityMouvementBehavior 
{
	protected int angle;
	protected int move;
	
	//Defining all movement types here
		public static final int FATCELLMOVEMENT = 1;
		public static final int FATCELLROLL = 2;
		public static final int HARMONIC = 3;
		public static final int WANDERING = 4;
	
	public int getMove()					{	return move;	};
	public void setMove(int newVal)			{	move = newVal;	};
	
	public abstract void update(Entity e1, double heartBeat);
}