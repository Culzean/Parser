package game.EntityMouvementBehavior;

import events.Vector2D;
import game.Entity;

public class FatCellMovement extends EntityMouvementBehavior{

	private Vector2D vMovement;
	static final float VEL_CONST = 2.0f;
	
	public FatCellMovement(int heartX, int heartY, int spawnX, int spawnY)
	{
		int x = -1 * (heartX - spawnX);
		int y = -1 * (heartY - spawnY);
		vMovement = new Vector2D(x,y);
		vMovement.normalize();
		vMovement.x *= VEL_CONST;
		vMovement.y *= VEL_CONST;
		this.setMove(EntityMouvementBehavior.FATCELLMOVEMENT);
	}
	
	public FatCellMovement(int heartX, int heartY, int spawnX, int spawnY, int obX, int obY)
	{
		int x = (heartX - spawnX);
		int y = (heartY - spawnY);
		vMovement = new Vector2D(x,y);
		vMovement.normalize();
		vMovement.x *= VEL_CONST;
		vMovement.y *= VEL_CONST;
		this.setMove(EntityMouvementBehavior.FATTHROW);
	}

	public void update(Entity e1, double heartBeat, double dt) {
		
		e1.setPosX((float) (e1.getPosX() + vMovement.x));
		e1.setPosY((float) (e1.getPosY() + vMovement.y));
		
	}

}
