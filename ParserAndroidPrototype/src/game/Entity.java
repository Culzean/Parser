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

package game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import events.Acceleration;
import events.Vector2D;
import game.EntityMouvementBehavior.EntityMouvementBehavior;
import game.test.R;
import gameEngine.GameObject;
import gameEngine.Sprite;

public abstract class Entity
{
	//Defining all entity type here
	public static final int ORGAN = 0;
	public static final int CELL = 1;
	public static final int REDCELL = 2;
	public static final int WHITECELL = 3;
	public static final int PLATELET = 4;
	public static final int VIRUS = 5;
	public static final int FATCELL = 6;
	public static final int CELLPOP = 7;
	
	public static final int NUM_ENT_TYPES = 8;
	
	protected double posX, posY;
	protected double velX, velY;
	protected int radius, radScale;
	protected boolean remove;
	protected int type;
	protected Paint entityColor;
	protected EntityMouvementBehavior mouvementBehavior;
	protected Vector2D start2d;
	protected Vector2D colVec,lapVec;

	protected Sprite sprite;
	protected Resources viewResources;
	
	//Constructor
	public Entity(double posX, double posY, int radius, int type, Resources viewRes)
	{
		viewResources = viewRes;
		start2d = new Vector2D(0,0);
		colVec = new Vector2D(0,0);
		lapVec = new Vector2D(0,0);
		this.setPosX(posX); 
		this.setPosY(posY);
		this.setRadius(radius);
		this.setType(type);
		this.setRadScale(1);
		//By default the entity use the color WHITE
		this.setEntityColor(new Paint(Color.WHITE));
		this.mouvementBehavior = null;
	}
	
	//By default an entity do nothing
	public boolean Update(double beat, double dt) { 
		if(sprite != null)
			sprite.update();
		return isRemoved(); 
	}
	//Draw the circle at the entity's position
	public void Draw(Canvas canvas)
	{
		if(!isRemoved())
		{
			if(sprite != null)
			{
				sprite.setLocation((int) (posX-sprite.getWidth()*0.5), (int) (posY-sprite.getHeight()*0.5)); //Have to center the sprite on the circle
				sprite.draw(canvas);
			}
			else
				canvas.drawCircle((int)this.getPosX(), (int)this.getPosY(), getRadius(), entityColor);
		}
	}
	
	//Teleport the entity to the position given in parameter
	public void moveTo(double x, double y)
	{
		setPosX((int) x); setPosY((int) y);
	}
	
	//Default behavior push both entity away from each other
	public void Collide(Entity e1)
	{
		if(Math.abs(posX - e1.posX) > Math.abs(posY - e1.posY))
		{
			if(this.posX > e1.getPosX())
			{
				e1.setPosX(e1.getPosX() - 1);
				this.posX = this.posX + 1;
			}
			else
			{
				e1.setPosX(e1.getPosX() + 1);
				this.posX = this.posX - 1;
			}
		}
		else
		{
			if(this.posY > e1.getPosY())
			{
				e1.setPosY(e1.getPosY() - 1);
				this.posY = this.posY + 1;
			}
			else
			{
				e1.setPosY(e1.getPosY() + 1);
				this.posY = this.posY - 1;
			}
		}
	}
	
	public abstract void ResolveCol();
	protected abstract void OutOfBounds();
	
	public Acceleration acceRate(double beat)
	{
		//need to look up somewhere what sort of acceleration we want
		if(beat > 1)
			return Acceleration.FAST;
		else if(beat < -1)
			return Acceleration.SLOW;
		else
			return Acceleration.DRIFT;
	}
	
	//Getters/Setters
	public int getRadScale() { return radScale;	}		public void setRadScale(int radScale) {	this.radScale = radScale; }
	public double getPosX() { return posX; }				public void setPosX(double posX) { this.posX = posX; }
	public double getPosY() { return posY; }				public void setPosY(double posY) { this.posY = posY; }
	public double getVelX() { return velX; }				public void setVelX(double velX) { this.velX = velX; }
	public double getVelY() { return velY; }				public void setVelY(double velY) { this.velY = velY; }
	public int getRadius() 	{ return radius; }				public void setRadius(int newRad) { int diff = radius - newRad;	this.setPosX(this.getPosX() + diff); this.setPosY(this.getPosY() + diff); this.radius = newRad; }
	public boolean isRemoved() { return remove; }
	public void setRemove(boolean remove) { this.remove = remove; }
	public int getType() { return type; }					public void setType(int type) { this.type = type; }
	public Paint getEntityColor() {	return entityColor;	} 	public void setEntityColor(Paint entityColor) { this.entityColor = entityColor; }
	
	public int getWidth() {	return this.getRadius(); }		public void setWidth(int width)	{ this.setRadius(width); }
	public int getHeight() { return this.getRadius(); }		public void setHeight(int height) {	this.setRadius(height);	}
	public int getCenX() { return (int) (posX + this.getRadius()); } public int getCenY() {	return (int) (posY + this.getRadius());	}
	
	//Spawn velocity
	public double getStartDX() { return start2d.x; }		public void setStartDX(double startDX) { this.start2d.x = startDX; }
	public double getStartDY() { return start2d.y; }		public void setStartDY(double startDY) { this.start2d.y = startDY; }
	//same as a 2d vector
	public Vector2D getStart2d()	{ return start2d; }		public void setStart2d(Vector2D newStart)	{ this.start2d = newStart; }
	
	
	//Mouvement behavior
	public EntityMouvementBehavior getMouvementBehavior(){ return mouvementBehavior; }
	public void setMouvementBehavior(EntityMouvementBehavior mouvementBehavior) { this.mouvementBehavior = mouvementBehavior; }
}
