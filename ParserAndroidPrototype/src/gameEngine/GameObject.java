package gameEngine;

import game.Entity;
import android.graphics.Canvas;

public interface GameObject {

	
	public boolean Update(double beat, double dt);
	public void Draw(Canvas canvas);
	public void Collide(Entity e1);
}
