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
	public static final int ORGAN = 3;
	public static final int CELL = 4;
	public static final int REDCELL = 1;
	public static final int WHITECELL = 0;
	public static final int PLATELET = 2;
	public static final int VIRUS = 5;
	public static final int FATCELL = 6;
	public static final int CELLPOP = 7;
	public static final int OXYGEN = 8;
	public static final int OXYFLARE = 9;
	
	public static final int NUM_ENT_TYPES = 8;
	
	protected float posX, posY;
	protected float velX, velY;
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
	public Entity(float posX, float posY, int radius, int type, Resources viewRes)
	{
		viewResources = viewRes;
		start2d = new Vector2D(0,0);
		colVec = new Vector2D(0,0);
		lapVec = new Vector2D(0,0);
		this.setRadius(radius);
		this.setPosX(posX); 
		this.setPosY(posY);
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
	public void moveTo(float x, float y)
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
	public void setPosX(float posX)	{ this.posX = posX; }
	public void setPosY(float posY)	{ this.posY = posY; }
	public float getPosY() { return posY; }
	public float getPosX() { return posX; }
	public float getVelX() { return velX; }				public void setVelX(float velX) { this.velX = velX; }
	public float getVelY() { return velY; }				public void setVelY(float velY) { this.velY = velY; }
	public int getRadius() 	{ return radius; }				
	public boolean isRemoved() { return remove; }
	public void setRemove(boolean remove) { this.remove = remove; }
	public int getType() { return type; }					public void setType(int type) { this.type = type; }
	public Paint getEntityColor() {	return entityColor;	} 			public void setEntityColor(Paint entityColor) { this.entityColor = entityColor; }
	public int getIntColor() {	return entityColor.getColor();	} 	public void setEntityColor(int entityColor) { this.entityColor.setColor(entityColor); }
	
	public int getWidth() {	return this.getRadius(); }		public void setWidth(int width)	{ this.setRadius(width); }
	public int getHeight() { return this.getRadius(); }		public void setHeight(int height) {	this.setRadius(height);	}
	public int getCenX() { return (int) (posX + this.getRadius()); } public int getCenY() {	return (int) (posY + this.getRadius());	}
	
	//Spawn velocity
	public float getStartDX() { return (float) start2d.x; }		public void setStartDX(float startDX) { this.start2d.x = startDX; }
	public float getStartDY() { return (float) start2d.y; }		public void setStartDY(float startDY) { this.start2d.y = startDY; }
	//same as a 2d vector
	public Vector2D getStart2d()	{ return start2d; }		public void setStart2d(Vector2D newStart)	{ this.start2d = newStart; }
	
	
	public void setRadius(int newRad)
	{
		this.radius = newRad;
	}
	
	//Mouvement behavior
	public EntityMouvementBehavior getMouvementBehavior(){ return mouvementBehavior; }
	public void setMouvementBehavior(EntityMouvementBehavior mouvementBehavior) { this.mouvementBehavior = mouvementBehavior; }
}
