////////////////////////////////////////////////////
//
//2 dimensional Vector class
//based on C++ class by Daniel Livingstone
//translated by Daniel Waine
//
////////////////////////
package events;

import java.lang.Math;

public class Vector2D {
	
	public double x,y;
	
	public Vector2D(double ix, double iy)
	{x = ix; y = iy;}
	
	public Vector2D() 	{ x = 0; y = 0; }
	
	//form negation
	public void negateThis()
	{
		this.x = (0 - this.x);	this.y = (0 - this.y);
	}
	
	//assign one vector to this vector
	public Vector2D setTo(final Vector2D newValues){
		this.x = newValues.x;
		this.y = newValues.y;
		return this;
	}
	//add one Vecrtor to the vector you have already
	public Vector2D addVector(final Vector2D addVector)
	{
		this.x += addVector.x;		this.y += addVector.y;
		return this;
	}
	//subtract one vector from the vector you have
	public Vector2D minusVector(final Vector2D subVector)
	{
		this.x -= subVector.x;		this.y -= subVector.y;
		return this;
	}
	//time your vector by a scaling number. Can be less than zero. No zeroes
	public Vector2D scaleVector(final float s)
	{
		this.x *= s;	this.y *= s;
		return this;
	}
	//scale your vector down
	public Vector2D divideVector(final float s)
	{
		this.x /= s;	this.y /= s;
		return this;
	}
	
	
	//add this vector and another without changing values of either
	public Vector2D add2Vectors(final Vector2D otherVector)
	{
		return new Vector2D( (this.x + otherVector.x), (this.y + otherVector.y));
	}
	//get vector of subtraction of this vector from arguement vector. No modifying
	public Vector2D subtract2Vectors(final Vector2D otherVector)
	{
		return new Vector2D( (this.x - otherVector.x), (this.y - otherVector.y));
	}
	//get result of a scale without modifying this vector
	public Vector2D getVectorScale(final float s)
	{
		return new Vector2D( (this.x * s) , (this.y * s) );
	}
	//get result of a divide without modifying this vector
	public Vector2D getVectorDivide(final float s)
	{
		if(s == 0)
			return null;
		else
			return new Vector2D( (this.x / s) , (this.y / s) );
	}
	
	
	////////////////////////////////////////////////
	//
	//Key vector sums!
	//get dot product of two vectors
	public double findDot( final Vector2D v )
	{
		return ( this.x * v.x + this.y * v.y );
	}
	//get length of this vector
	public final double getLength()
	{
		return ( Math.sqrt((x * x) + (y * y)));
	}
	//return a unit vector with this vector's direction
	public final Vector2D getUnit()
	{
		double temp = getLength();
		return new Vector2D(this.x / temp, this.y / temp);
	}
	//set this vector to be a unit vector
	public final void normalize()
	{
		double temp = this.getLength();
		this.x /= temp;		this.y /= temp;
	}
}
