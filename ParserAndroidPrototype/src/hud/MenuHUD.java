package hud;

import android.graphics.Canvas;
import game.State;

public class MenuHUD extends HUD{

	public MenuHUD(State stateRef) {
		super(stateRef);
	}
	
	public void draw(Canvas dbimage)
	{
		redCell.draw(dbimage);
		whiteCell.draw(dbimage);
		platelet.draw(dbimage);
	}

}
