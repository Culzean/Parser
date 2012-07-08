package game.EntityMouvementBehavior;

import events.Vector2D;
import game.Entity;

public class FatCellRoll extends EntityMouvementBehavior{

	Vector2D vRoll = null;
	Vector2D vRollPos = null;
	
	
	static float rotPlus = 0.087f;//5 degrees
	static float rotNeg = -0.087f;
	
	private float rot;
	
	public FatCellRoll(int posRotX, int posRotY, Vector2D vCvsC )
	{
		vRollPos = new Vector2D( posRotX, posRotY );
		vRoll = vCvsC;
		this.setMove(EntityMouvementBehavior.FATCELLROLL);
		setRot();
	}
	
	private void setRot()
	{
		if(vRoll.x > 0 && vRoll.y > 0)
			rot = rotPlus;
		else if(vRoll.x < 0 && vRoll.y < 0)
			rot = rotPlus;
		else if(vRoll.x < 0 && vRoll.y > 0)
			rot = rotNeg;
		else if(vRoll.x > 0 && vRoll.y < 0)
			rot = rotNeg;
	}
	
	public void update(Entity e1, double heartBeat, double dt) {
		
		//rotate vector
		vRoll.x = ( vRoll.x * Math.cos(rot) ) - ( vRoll.y * Math.sin(rot) );
		vRoll.y = ( vRoll.x * Math.sin(rot) ) + ( vRoll.y * Math.cos(rot) );
		
		e1.setPosX( vRollPos.x + vRoll.x );
		e1.setPosY( vRollPos.y + vRoll.y );
	}

}
