package gameEngine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Sprite { 
    private Bitmap spriteSheet=null;
    private Bitmap scaledSprite;
    private int xPos;
    private int yPos;
    private int dx;
    private int dy;
    private int numCols;
    private Rect rectangle;
    private int fps;
    private int noOfFrames;
    private int currentFrame;
    private long frameTimer;
    private int spriteHeight;
    private int spriteWidth;
    private Paint spriteAlpha;
    
    public Sprite(Bitmap bmp, int nCols, int nRows, int nFrames, int FPS) {
    	spriteSheet = bmp;
    	scaledSprite = bmp;
    	numCols = nCols;
    	spriteWidth = spriteSheet.getWidth() / nCols;
    	spriteHeight = spriteSheet.getHeight() / nRows;
        rectangle = new Rect(0,0, spriteWidth, spriteHeight);
        noOfFrames = nFrames;
        fps = 1000/FPS;
        frameTimer =0;
        currentFrame =0;
        xPos = 0;
        yPos = 0;
        dx = dy = 0;
        spriteAlpha = new Paint();
        spriteAlpha.setAlpha(255);
    }

    private void updateRect(int frameNumber){
    	int col = frameNumber % numCols;
    	int row = frameNumber / numCols;
    	rectangle.left = col * scaledSprite.getWidth();
    	rectangle.right = rectangle.left + scaledSprite.getWidth();
        rectangle.top = row * scaledSprite.getHeight();
        rectangle.bottom = rectangle.top+scaledSprite.getHeight();
    }
    
    public void update(){
    	xPos += dx;
    	yPos += dy;
    	currentFrame += 1;
    	if(currentFrame >= noOfFrames){
    		currentFrame = 0;
    	}
    	updateRect(currentFrame);
    }
    
    public void update(int dx, int dy){
    	xPos += dx;
    	yPos += dy;
    	update();
    }
    
    public void update(long gameTime) {
        if(gameTime > frameTimer + fps ) {
        	update();
        }
    }
    public void draw(Canvas canvas) {
        Rect dest;
        dest = new Rect(xPos, yPos, xPos + scaledSprite.getWidth(), yPos + scaledSprite.getHeight());
        canvas.drawBitmap(scaledSprite, rectangle, dest, spriteAlpha);
    }
    public void setLocation(int x, int y){
    	xPos = x;
    	yPos = y;
    }
    public int getX(){ return xPos;}
    public int getY(){ return yPos;}
    
    public void setVelocity(int dx, int dy){
    	this.dx = dx;
    	this.dy = dy;
    }
    public int getDx(){ return dx;}
    public int getDy(){ return dy;}
    
    public int getWidth() { return scaledSprite.getWidth(); }
    public int getHeight() { return scaledSprite.getHeight(); }
    
    public void moveTo(int x, int y){
    	xPos = x;
    	yPos = y;
    }
    public void moveBy(int dx, int dy){
    	xPos += dx;
    	yPos += dy;
    }
    
    public void resize(int newWidth, int newHeight)
    {
    	scaledSprite = Bitmap.createScaledBitmap(spriteSheet, newWidth, newHeight, true);
    	updateRect(currentFrame);
    }
    public void resetSize()
    {
    	scaledSprite = spriteSheet;
    }
    public void setAlpha(int alpha)
    {
    	spriteAlpha.setAlpha(alpha);
    }
}
