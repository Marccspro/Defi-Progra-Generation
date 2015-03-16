package fr.veridiangames.utils;

public class Vector3f
{
	public float x, y, z;
	
	public Vector3f()
	{
		this(0, 0, 0);
	}
	
	public Vector3f(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float length()
	{
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	
	public Vector3f normalize()
    {
        x /= length();
        y /= length();
        z /= length();

        return this;
    }
	
	public Vector3f add(Vector3f vec)
	{
		x += vec.x;
		y += vec.y;
		z += vec.z;
		
		return this;
	}
	
	public Vector3f sub(Vector3f vec)
	{
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
		
		return this;
	}
	
	public Vector3f mul(Vector3f vec)
	{
		x *= vec.x;
		y *= vec.y;
		z *= vec.z;
		
		return this;
	}
	
	public Vector3f div(Vector3f vec)
	{
		x /= vec.x;
		y /= vec.y;
		z /= vec.z;
		
		return this;
	}
	
	public Vector3f add(float v)
	{
		x += v;
		y += v;
		z += v;
		
		return this;
	}
	
	public Vector3f sub(float v)
	{
		x -= v;
		y -= v;
		z -= v;
		
		return this;
	}
	
	public Vector3f mul(float v)
	{
		x *= v;
		y *= v;
		z *= v;
		
		return this;
	}
	
	public Vector3f div(float v)
	{
		x /= v;
		y /= v;
		z /= v;
		
		return this;
	}
	
    public Vector3f addY(float v)
    {
        y += v;
        return this;
    }

    public Vector3f subY(float v)
    {
        y -= v;
        return this;
    }
	
    public Vector3f addZ(float v)
    {
        z += v;
        return this;
    }

    public Vector3f subZ(float v)
    {
        z -= v;
        return this;
    }
    
    public String toString()
    {
        return x + " " + y + " " + z;
    }
}
