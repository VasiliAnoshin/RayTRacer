package scene.Surfaces;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;
import scene.Intersection;

public class Cylindr extends Surface{
	/*
	 * center - (3d coord)
	 * radius - (number)
	 */

	// q – axis(ось) point(3d coord)
	private Point3D m_axisPt; 
	//u – axis direction (3d coord)
	private Vec m_direction; 
	//radius - (number)
	double m_radius;
	
	public Cylindr(Map<String, String> attributes) {
		super(attributes);		
		String i_stAxisPt = attributes.get("q");		
		String i_stDir = attributes.get("u");		
		String i_stRadius = attributes.get("radius");
		
		if (i_stDir==null || i_stAxisPt==null || i_stRadius==null) {
			System.err.print("error adding cylinder:");
			if (i_stAxisPt==null) 
				System.err.println("missing q parameter");
			if (i_stDir==null) 
				System.err.println("missing U parameter");
			if (i_stRadius==null) 
				System.err.println("missing radius parameter");
		} else {
			m_axisPt = new Point3D(i_stAxisPt);
			m_direction = new Vec(i_stDir);
			m_radius = Double.parseDouble(i_stRadius);
		}
	}
	//Find normal on cylinder surface 
	@Override
	public Vec getNormalAtPoint(Point3D point) {
		Vec v = point.sub(m_axisPt);
		return new Vec(Point3D.add(m_axisPt, Vec.scale(m_direction,v.dotProd(m_direction))),point).normalized();
	}

	@Override
	public Intersection getIntersectionWithRay(Ray ray) {
		Point3D q = m_axisPt;
		Vec u = m_direction.normalized();
		Point3D p = ray.m_pointOfOrigin();
		Vec v = ray.m_rayDirection().normalized();
		Vec w = p.sub(q);
		double alpha = Vec.dotProd(u, w) / Vec.dotProd(u, u);
		double beta = Vec.dotProd(u, v) / Vec.dotProd(u, u);
		
		double A = beta*beta*Vec.dotProd(u, u) - 2.0d*beta*Vec.dotProd(u,v) + Vec.dotProd(v,v); 
		double B = 2.0d*alpha*beta*Vec.dotProd(u, u) - 2.0d*beta*Vec.dotProd(u,w) + 2.0d*Vec.dotProd(v,w) - 2.0d*alpha*Vec.dotProd(u,v);
		double C = alpha*alpha*Vec.dotProd(u,u) - 2.0d*alpha*Vec.dotProd(u,w) + Vec.dotProd(w,w) - m_radius*m_radius;
		
		double D = B*B - 4.0d*A*C;
		if (D>0) {
			double t1 = (-1.0d*B + Math.sqrt(D))/(2.0d*A);
			double t2 = (-1.0d*B - Math.sqrt(B*B - 4.0d*A*C))/(2.0d*A);
			
			double t=t1;
			if (((t2<t)&&(t2>=0))||(t<0))
				t = t2;
			if (t>0) {	
				Point3D p1 = Point3D.add(ray.m_pointOfOrigin(), Vec.scale(ray.m_rayDirection(), t));
				Vec iNormal = getNormalAtPoint(p1);
				if (Vec.dotProd(ray.m_rayDirection(),iNormal)<0.0d)
					return (new Intersection(p1,this,t,iNormal,ray.m_rayDirection()));
				else
					return null;
			} else
				return null;
		} else {
			return null;
		}
	
	}
}
