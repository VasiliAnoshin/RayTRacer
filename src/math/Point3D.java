package math;

import java.awt.Color;
import java.util.Scanner;

public class Point3D{
	public double x, y, z;


	public Point3D(String v) throws IllegalArgumentException {
		Scanner s = new Scanner(v);
		try {
			this.x = s.nextDouble();
			this.y = s.nextDouble();
			this.z = s.nextDouble();
			s.close();
		} catch (Exception e) {
			throw new IllegalArgumentException("Point3D can't be constructed with: " + v);
		}
	}
	
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point3D clone() {
		return new Point3D(this.x, this.y, this.z);
	}
	
	public void add(Vec v) {
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
	}
	
	public void sub(Vec v) {
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
	}
	
	
	public void mult(Point3D p) {
		this.x *= p.x;
		this.y *= p.y;
		this.z *= p.z;
	}
	
	public void scale(double d) {
		this.x *= d;
		this.y *= d;
		this.z *= d;
	}
	
	public void add(Point3D p) {
		this.x += p.x;
		this.y += p.y;
		this.z += p.z;
	}
	
	public static Point3D mult(Point3D p1,Point3D p2) {
		return new Point3D(p1.x * p2.x, p1.y * p2.y,p1.z * p2.z);
	}
	
	public static Point3D add(Point3D p,Vec v) {
		return new Point3D(p.x + v.x, p.y + v.y,p.z + v.z);
	}
	
	public static Point3D scale(Point3D p1,double d) {
		return new Point3D(p1.x * d, p1.y * d,p1.z * d);
	}
	
	public Vec sub(Point3D p) {
		return new Vec(this.x - p.x, this.y - p.y, this.z - p.z);
	}
	
	public String toString() {
		return String.format("(%.2f,%.2f,%.2f)",this.x,this.y,this.z);
	}
	
	public Color toColor() {
		int r = (int) (this.x * 255.0d);
		int g = (int) (this.y * 255.0d);
		int b = (int) (this.z * 255.0d);
		if (r>255) 
			r=255;
		else if (r<0)
			r=0;
		if (g>255) 
			g=255;
		else if (g<0)
			g=0;
		if (b>255) 
			b=255;
		else if (b<0)
			b=0;
		return new Color(0xFF<<24 | ((r & 0xFF)<<16) | ((g&0xFF)<<8) | (b&0xFF));
	}
}