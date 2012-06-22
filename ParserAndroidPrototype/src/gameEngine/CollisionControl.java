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

package gameEngine;

import events.Vector2D;
import game.Entity;
import game.Heart;

public class CollisionControl 
{
	public static boolean CircleVsCircle(Entity c1, Entity c2)
	{
		//could also do SAT AABB
		
		//(x-h1)2 + (y-k1)2 = r2 and (x-h2)2 + (y-k2)2 = r2 
		// if sqrt(h2-h1)2 + (k2-k1)2 <= (r1 + r2)
		//then colliding
		int c1_X = (int) c1.getPosX(); int c2_X = (int) c2.getPosX();
		int radTot = c1.getRadius() + c2.getRadius();
		if(Math.abs(c2_X - c1_X) <= radTot)//must be an axis alinged box overlapping to be colliding
		{
			int c2_Y = (int) c2.getPosY();  int c1_Y = (int) c1.getPosY();
		
			int xCom = (c2_X - c1_X) * (c2_X - c1_X);
			int yCom = (c2_Y - c1_Y) * (c2_Y - c1_Y);
			int rCom = (c1.getRadius() + c2.getRadius()) * (c1.getRadius() + c2.getRadius());
		
			if( xCom + yCom <= rCom )
				return true;
			else
				return false;
		}else
				return false;
	}
	public static boolean InvCircleVsCircle(Heart c1, Entity c2)
	{
		//very similar to normla cvc but the radius comparision is inversed
		//the intended use of this means heart is FIRST
		
		int c1_X = (int) c1.getX(); int c2_X = (int) c2.getPosX();
		int radTot = c1.getCurRad() + c2.getRadius();
		if(Math.abs(c2_X - c1_X) <= radTot)//must be an axis alinged box overlapping to be colliding
		{
			int c2_Y = (int) c2.getPosY();  int c1_Y = (int) c1.getY();
		
			int xCom = (c2_X - c1_X) * (c2_X - c1_X);
			int yCom = (c2_Y - c1_Y) * (c2_Y - c1_Y);
			int rCom = (c1.getCurRad() - c2.getRadius()) * (c1.getCurRad() - c2.getRadius());
		
			if( xCom + yCom <= rCom )
				return true;
			else
				return false;
		}else
				return false;
	}
	
	public static boolean CircleVsPoint(Heart c1, int p1x, int p1y)
	{
		
		int c1_X = (int) c1.getX();
		int radTot = c1.getRadius();
		if(Math.abs(p1x - c1_X) <= radTot)//must be an axis alinged box overlapping to be colliding
		{
			int c1_Y = (int) c1.getY();
		
			int xCom = (p1x - c1_X) * (p1x - c1_X);
			int yCom = (p1y - c1_Y) * (p1y - c1_Y);
		
			if( xCom + yCom <= (radTot * radTot) )
				return true;
			else
				return false;
		}else
				return false;
	}
}
