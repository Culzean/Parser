package hud;

import game.test.R;
import gameEngine.Sprite;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Button extends DisplayObject
{
	private Sprite largeImg;
	private boolean selected = false;
	
	public Button(int posX, int posY, int _width, int _height)
	{
		super(posX, posY, _width, _height);
	}
	
	public void setLarge(Sprite large, int largeWidth)
	{
		largeImg = large;
		largeImg.resize(largeWidth, largeWidth);
		largeImg.setLocation(getPosX() - (int)(largeWidth * 0.14), getPosY() - (int)(largeWidth * 0.5));
	}
	
	public void draw(Canvas dbimage)
	{
		if(selected)
			largeImg.draw(dbimage);
		else
			sprite.draw(dbimage);
	}
	
	
	
	public boolean clicked(int clickX, int clickY)
	{
		//First see if these test are true ( means there is no collision)
		if(clickX > getPosX() + getWidth()) return false; // click too much on the right
		else if(clickX < getPosX()) return false; // click too much on the left
		else if(clickY < getPosY()) return false; // click over the button
		else if(clickY > getPosY() + getHeight()) return false; // click under the button
		//Else there is a collision
		else return true;
	}

	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	public void setSelected( boolean val ){
		selected = val;
	}

	
}
