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
/*
 * Copyright (c) Daniel Waine 2012. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * implimentation of the Mersenne Twister pseudorandom number generator
 * originally developed by Takuji Nishimura and Makoto Matsumoto
 * 
 * 
 */
import java.util.Date;

public class RandomNumGen {
	
	public final static int CMATH_N = 624;
	public final static int CMATH_M = 397;
	private static final int CMATH_MATRIX_A = 		0x9908b0df;			//constant vector a
	public final static int CMATH_UPPER_MASK = 		0x80000000;			//most significant w-r bits
	public final static int CMATH_LOWER_MASK = 		0x7FFFFFFF;			//least significant r bits
	
	
	public final static int CMATH_TEMPERING_MASK_B = 		0x9D2C5680;
	public final static int CMATH_TEMPERING_MASK_C =		0xEFC60000;
	
	int rseed;
	Long[] mt = new Long[CMATH_N];			//array for the state vector
	private int mti;		//mti==N+1 means mt[N] is not initialized

	
	public RandomNumGen(){
		rseed = 1;
		mti = CMATH_N+1;
	}
	
	public int Random(int n)
	{
		long y;
		final long mag01[] = {0x0 , CMATH_MATRIX_A };
		
		if(n==0)
			return 0;
		
		if(mti >= CMATH_N)	//twist it
		{
			int kk;
			
			if(mti == CMATH_N+1)
				setRandomSeed(6877);		//set default seed
			
			for(kk=0; kk<CMATH_N - CMATH_M ;kk++)
			{
				y = (mt[kk] & CMATH_UPPER_MASK) | (mt[kk+1] & CMATH_LOWER_MASK);
				mt[kk] = mt[kk+CMATH_M] ^ (y >> 1) ^ mag01[ (int) (y & 0x01) ];
			}
			for(;kk<CMATH_N-1 ; kk++)
			{
				y = (mt[kk] & CMATH_UPPER_MASK ) | (mt[kk+1] & CMATH_LOWER_MASK);
				mt[kk] = mt[kk+(CMATH_M - CMATH_N)] ^ (y >> 1) ^ mag01[(int) (y & 0x01)];
			}
			y = (mt[CMATH_N -1] & CMATH_UPPER_MASK) | (mt[0] & CMATH_LOWER_MASK);
			mt[CMATH_N - 1] = mt[CMATH_M -1] ^ (y >> 1) ^ mag01[(int) (y & 0x01)];
			
			mti = 0;
		}
		y = 0;
		y = mt[mti++];
		y ^= (y >> 11);
		y ^= (y << 7) & CMATH_TEMPERING_MASK_B;
		y ^= (y << 15) & CMATH_TEMPERING_MASK_C;
		y ^= (y >> 18);
		
		return (int) (y%n);
	}
	
	public void setRandomSeed(int n)
	{
		//knuth 1981 the Art of Computer Programming
		//vol. 2 2nd ed
		mt[0] = (long) (n & 0xFFFFFFFF);
		for(mti = 1; mti<CMATH_N ; mti++)
			mt[mti] = (69069 * mt[mti-1]) & 0xFFFFFFFF;
		
		rseed = n;
	}
	
	public int getRandomSeed()
	{
		return rseed;
	}
	
	public void Randomize()
	{
		setRandomSeed((int) new Date().getTime());
		//System.out.println(mt.length);
	}

}
