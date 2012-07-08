package game.EntityMouvementBehavior;

import events.Acceleration;
import events.Vector2D;
import game.Entity;

public class Harmonic extends EntityMouvementBehavior 
{
	
	public Harmonic( Vector2D iV, float iAngle )
	{
		this.startDir = iV;
		setAngle(iAngle);
		this.setMove(EntityMouvementBehavior.HARMONIC);
	}
	
	@Override
	public void update(Entity e1, double heartBeat, double dt) 
	{
		e1.setVelX( Math.cos(Math.toRadians( getAngle())) * startDir.x +
				( startDir.x * dt  * Acceleration.ROLL.getdBound() ) );
		e1.setVelY( Math.sin(Math.toRadians(getAngle())) * startDir.y + 
				( startDir.y * dt * Acceleration.ROLL.getdBound() ) );
		
		e1.setPosX((int)(e1.getPosX() + e1.getVelX()));

		e1.setPosY((int)(e1.getPosY() + e1.getVelY()));

		
		setAngle((float) (getAngle() + (0.6 * dt)));
	}
}
