package math;

public class Ray {

	// point of origin
	 private Point3D p; 
	// ray direction
	 private Vec v; 
	
	/**
	 * constructs a new ray
	 * @param p - point of origin
	 * @param v - ray direction
	 */
	
	public Ray(Point3D p, Vec v) {
		this.p = p;
		this.v = v.normalized();
	}
	
	public Point3D m_pointOfOrigin(){
		return this.p;
	}
	
	public Vec m_rayDirection(){
		return this.v;
	}
}
