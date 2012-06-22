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

package events;

	
	public enum Acceleration
	{
		//main acceleration to the boundary
		//and secondary acceleration to the left and right this target
		FAST (5, 5),
		NORMAL (3,3),
		DRIFT (1,1),
		SLOW (-2,-2),
		VERYSLOW (-4,-4);
		
		
		private final double dBound, dSecd;
		Acceleration(double boundary, double secondary){
			dBound = boundary; dSecd = secondary;
		}
		
		public double getdBound() {	return dBound;	}
		public double getdSecd() {	return dSecd; }
		
		public static double[] getAcc(double startdX, double startdY, Acceleration a)
		{//will return the correct current acceleration on one axis
			//given the start direction and current heart rate
			//one of these values is absolute. The other the sign matters
			double newAcc[] = {0,0};
			Acceleration rate = a;
			double unit = Math.sqrt((startdX * startdX) + (startdY * startdY));
			if(Math.abs(startdX) > Math.abs(startdY))
			{
				newAcc[0] = rate.getdBound() * (startdX / unit);
				newAcc[1] = rate.getdSecd() * (startdY / unit);
				
			}else{
				
				newAcc[0] = rate.getdSecd() * (startdX / unit);
				newAcc[1] = rate.getdBound() * (startdY / unit);
				
			}

			//System.out.println("The acceleration is : " + newAcc[0] + "   "+ newAcc[1]);
			return newAcc;
		}
		

	}
