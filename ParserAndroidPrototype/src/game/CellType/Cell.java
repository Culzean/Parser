////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
//
//Game Engine and game by Daniel Waine and Raphael Chappuis 
//
//for Mobile and Web Games Development
//
//Banner IDs B00226804 - B00218930
//
////////////////////////////////////////////////////////////////////////

package game.CellType;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import events.Vector2D;
import game.Entity;
import game.GameModel;
import game.EntityMouvementBehavior.Harmonic;
import game.test.R;
import gameEngine.GameObject;
import gameEngine.ParserView;
import gameEngine.Sprite;

public class Cell extends Entity implements GameObject
{
	protected int radius;
	private double ACCELERATION;
	protected GameModel model;
	
	public Cell(int startX, int startY, float startDX, float startDY, int rad, Resources viewRes, GameModel refModel)
	{
		super(startX, startY, rad, Entity.CELL, viewRes); // add constant for the type!!!!!
		model = refModel;
		this.setStartDX((float) startDX); this.setStartDY((float) startDY);
		this.setVelX(this.getStartDX()); this.setVelY(this.getStartDY());
		ACCELERATION = 0.0000002;
		//By default cells are red
		this.setEntityColor(new Paint(Color.RED));
	}
	
	public Cell(int startX, int startY, Vector2D istart2d, int rad, Resources viewRes, GameModel refModel)
	{
		super(startX, startY, rad, Entity.CELL, viewRes); // add constant for the type!!!!!
		model = refModel;
		this.setStart2d(istart2d);
		this.setVelX(this.getStartDX()); this.setVelY(this.getStartDY());
		ACCELERATION = 0.0000002;
		//By default cells are red
		this.setEntityColor(new Paint(Color.RED));
	}
	public boolean Update(double beat, double dt)
	{
		//update parent (entity)
		super.Update(beat, dt);
		
		//Update the entity mouvement if exist
		if(getMouvementBehavior() != null)
			getMouvementBehavior().update(this, beat, dt);
		
		//test on edges
		if( this.getPosX() > (ParserView.windowWidth + radius) ||
			this.getPosX() < (0 - radius) ||
			this.getPosY() > (ParserView.windowHeight + radius) ||
			this.getPosY() < (0 - radius) )
			{
				OutOfBounds();
			}
		
		if(lapVec.x > 0 || lapVec.y > 0)
		{
			this.ResolveCol();
			lapVec.x = 0; lapVec.y = 0;
		}
		
		return isRemoved();
	}
	
	public void infect( Vector2D iDir, float iAngle )
	{
		if(this.getType() != Entity.VIRUS)
		{
			getEntityColor().setColor(Color.GREEN);
			this.setMouvementBehavior(new Harmonic(iDir, iAngle));
			this.setType(Entity.VIRUS);
			Bitmap bitmapSprite = BitmapFactory.decodeResource(viewResources, R.drawable.virus);
			sprite = new Sprite(bitmapSprite, 1, 1, 1, 1);
			sprite.resize(24, 24);
			model.infect();
		}
	}
	
	////////////////////////////////////////////////////////
	///getters and setters
	/////////////////////////////////////////////////
	public double getAccel() { return ACCELERATION;	} 	public void setAccel(double sPEED) { ACCELERATION = sPEED; }

	@Override
	public void ResolveCol() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void OutOfBounds() {
		// TODO Auto-generated method stub
		this.setRemove(true);
	}

	public void Draw(Canvas canvas) {
		super.Draw(canvas);
		
	}
}
