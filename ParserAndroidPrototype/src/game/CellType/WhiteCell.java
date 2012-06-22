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
import android.graphics.Color;
import events.Vector2D;
import game.Entity;
import game.GameModel;
import game.EntityMouvementBehavior.Wandering;
import game.test.R;
import gameEngine.Sprite;

public class WhiteCell extends Cell 
{
	public static final int MAX_APPLICATION = 2;
	int virusRemoved;
	
	public WhiteCell (int startX, int startY, Vector2D istart2d, Resources viewRes, GameModel refModel)
	{
		super(startX, startY, istart2d, 16, viewRes, refModel);
		getEntityColor().setColor(Color.WHITE);
		this.setType(Entity.WHITECELL);
		this.setMouvementBehavior(new Wandering());
		virusRemoved = 0;
		Bitmap bitmapSprite = BitmapFactory.decodeResource(viewRes, R.drawable.white_cells);
		sprite = new Sprite(bitmapSprite, 1, 1, 1, 1);
	}

	public boolean update(double beat)
	{		
		if(virusRemoved >= MAX_APPLICATION || super.update(beat))
			this.setRemove(true);
		
		return isRemoved();
	}
	
	public void Collide(Entity e1)
	{
		super.Collide(e1);
		
		if(e1.getType() == Entity.VIRUS)
		{
			model.disinfect();
			if(virusRemoved < MAX_APPLICATION){
				e1.setRemove(true);
				virusRemoved += 1;
			}
		}
	}
}
