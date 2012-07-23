package game.EntityMouvementBehavior;

import events.Acceleration;
import game.Entity;

public class OxyFloat extends EntityMouvementBehavior{

	private float max_velY;
	private float accY;
	
	public OxyFloat( int moveType)
	{
		this.setMove(moveType);
		max_velY = - Acceleration.slow;
		accY = max_velY;
	}
	
	public void update(Entity e1, double heartBeat, double dt) {
		
		//this always moves up
		e1.setVelY((float) (accY * dt));		//e1.setVelX(accX* dt);
		accY = (float) (accY - dt * 0.005);
		
		e1.setVelX( (float) (0.9 * Math.sin(Math.toRadians( getAngle())) + 0.5) );
		
		if( accY < max_velY)
			accY = 0;
		
		e1.setPosX((int)(e1.getPosX() + e1.getVelX()));

		e1.setPosY((int)(e1.getPosY() + e1.getVelY()));
		
		setAngle((float) (getAngle() + (0.8 * dt)));
		
	}

}
