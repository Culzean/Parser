package game.EntityMouvementBehavior;

import events.Acceleration;
import game.Entity;

public class Wandering extends EntityMouvementBehavior 
{
	@Override
	public void update(Entity e1, double heartBeat, double dt) 
	{
		//find current acceleration
		double newAcc[] = Acceleration.getAcc(e1.getStartDX(), e1.getStartDY(), e1.acceRate(heartBeat));
		//set and apply current acceleration
		e1.setVelX(newAcc[0] * dt); e1.setVelY(newAcc[1] * dt);
		e1.setPosX((int) (e1.getPosX() + e1.getVelX()));
		e1.setPosY((int) (e1.getPosY() + e1.getVelY()));
		this.setMove(EntityMouvementBehavior.WANDERING);
	}
}
