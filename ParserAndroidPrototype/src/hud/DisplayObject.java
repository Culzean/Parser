package hud;

import gameEngine.Sprite;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public abstract class DisplayObject {

	protected int posX, posY;
	protected int width;
	protected int height;
	protected Paint color;
	protected Sprite sprite;
	
	public DisplayObject()
	{
		color = new Paint();
		color.setColor(Color.CYAN);
		color.setAlpha(120);
	}
	
	public DisplayObject(int posX, int posY, int _width, int _height)
	{
		setPosX(posX);
		setPosY(posY);
		setWidth(_width);
		setHeight(_height);
		color = new Paint();
		color.setColor(Color.CYAN);
		color.setAlpha(120);
	}
	
	public void setSprite(Sprite newSprite)
	{
		sprite = newSprite;
		sprite.resize(getWidth(), getHeight());
		sprite.setLocation(getPosX(), getPosY());
	}
	
	public void setPaint(int r, int g, int b, int a)
	{
		this.color = new Paint();//the garbage collection should get the paint created in super
		color.setARGB(r, g, b, a);
	}
	
	protected void draw( Canvas dbimage )
	{
		if(sprite != null)
			sprite.draw(dbimage);
		else
			dbimage.drawRect(this.posX, this.posY, this.posX+this.width, this.posY+this.height, color);
	}
	
	public abstract void update();
	
	public int getPosX() 					{		return posX;	}

	public void setPosX(int positionX) 		{		this.posX = positionX;	}

	public int getPosY() 					{		return posY;	}

	public void setPosY(int positionY) 		{		this.posY = positionY;	}

	public int getWidth()					{		return width;	}

	public void setWidth(int width)			{		this.width = width;	}

	public int getHeight() 					{		return height;	}

	public void setHeight(int height)		{		this.height = height;	}
	
}
