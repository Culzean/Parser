package game.EntityMouvementBehavior;

import game.Entity;

public class Harmonic extends EntityMouvementBehavior 
{
	public Harmonic()
	{
		angle = 0;
		this.setMove(EntityMouvementBehavior.HARMONIC);
	}
	
	@Override
	public void update(Entity e1, double heartBeat) 
	{
		e1.setVelX(Math.cos(Math.toRadians(2 * angle)));
		e1.setVelY(Math.sin(Math.toRadians(angle)));
		
		e1.setPosX((int)(e1.getPosX() + e1.getVelX()));
		e1.setPosY((int)(e1.getPosY() + e1.getVelY()));
		
		angle += 10;
	}
}
