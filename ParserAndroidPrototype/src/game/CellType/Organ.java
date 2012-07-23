package game.CellType;

import events.Vector2D;
import game.Entity;
import game.GameModel;
import game.EntityMouvementBehavior.EntityMouvementBehavior;
import gameEngine.GameObject;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Organ extends Cell implements GameObject{

	private int drawRad;
	private String name;
	private Paint textInk;
	private int oxyCount;
	private final int MAX_NUTRIENTS = 9;
	
	//will there be more than one way to trigger this bool?
	private boolean bustFat = false;
	private int DEF_COL[];
	private float popCount;
	private final float DEF_POP = 36; //3.6 seconds
	private final float MAX_POP = 120;
	
	public Organ(int startX, int startY, int rad, Resources viewRes,
			GameModel refModel, int color[], String iname) {
		super(startX, startY, 0, 0, rad, viewRes, refModel);
		drawRad = (int) (rad * 1.1);
		this.entityColor = new Paint();
		entityColor.setColor(Color.rgb(color[0], color[1], color[2]));
		name = iname;
		textInk = new Paint(Color.MAGENTA);
		textInk.setTextSize(16);
		this.setType(Entity.ORGAN);
		oxyCount = 0;
		popCount = DEF_POP;
		DEF_COL = color;
	}

	public void Draw(Canvas canvas)
	{
		canvas.drawCircle((int)this.getPosX(), (int)this.getPosY(), drawRad, entityColor);
		canvas.drawText(name, getPosX() - 8, getPosY() - 8, textInk);

	}
	
	public boolean Update(double beat, double dt)
	{
		if(bustFat)
			popCount -= (dt * 0.01) ;
		if(popCount < 0)
			{
				Exercise();
				popCount = DEF_POP;
				bustFat = false;
			}
		return false;
	}
	
	public void Collide(Entity e1)
	{
		if(e1.getType() == Entity.OXYGEN)
		{
			if(e1.getMouvementBehavior().getMove() == EntityMouvementBehavior.WANDERING)
			{
				bustFat = true;
				if(++oxyCount > MAX_NUTRIENTS)
					oxyCount = MAX_NUTRIENTS;
				entityColor.setColor(Color.rgb(DEF_COL[0], (int)(DEF_COL[1] * (1 / oxyCount) ),
						(int)(DEF_COL[2] * oxyCount * 0.2) ));
			}
			
		}
		else if(e1.getType() == Entity.FATCELL)
		{
			if(Math.abs(posX - e1.getPosX()) > Math.abs(posY - e1.getPosY()))
			{
				if(this.posX > e1.getPosX())
				{
					e1.setPosX(e1.getPosX() - 1);
				}
				else
				{
					e1.setPosX(e1.getPosX() + 1);
				}
			}
			else
			{
				if(this.posY > e1.getPosY())
				{
					e1.setPosY(e1.getPosY() - 1);
				}
				else
				{
					e1.setPosY(e1.getPosY() + 1);
				}
			}
		}
		else if(e1.getType() == Entity.PLATELET)
		{
			popCount *= 1.2;
			if(popCount > this.MAX_POP)
				popCount = this.MAX_POP;
		}
	}
	
	private void Exercise()
	{
		int popRad = (int) ( (this.getRadius() * 0.4) + ( getRadius() * 0.8 * oxyCount ));
		model.orderCell().order(CELLPOP, popRad, (int)posX, (int)posY);
		entityColor.setColor(Color.rgb(DEF_COL[0],DEF_COL[1],DEF_COL[2]) ); 
		oxyCount = 0;
	}
	

	

}
