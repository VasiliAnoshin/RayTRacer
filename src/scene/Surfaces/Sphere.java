package scene.Surfaces;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;
import scene.Intersection;

public class Sphere extends Surface {

	/*
	 * center - (3d coord)
	 * radius - (number)
	 */
	Point3D m_center;
	double m_radius;
	
	public Sphere(Map<String, String> attributes) {
		super(attributes);
		
		String i_stCenter = attributes.get("center");
		String i_stRadius = attributes.get("radius");
		
		if (i_stCenter==null || i_stRadius==null) {
			System.err.print("error adding sphere:");
			if (i_stCenter==null) 
				System.err.println("missing center attribute");
			if (i_stRadius==null) 
				System.err.println("missing radius attribute");
		} else {
			m_center = new Point3D(i_stCenter);
			m_radius = Double.parseDouble(i_stRadius);
		}
		//System.out.println("add sphere at: " + m_center.toString());
	}

	@Override
	public Vec getNormalAtPoint(Point3D point) {
		return new Vec(m_center,point).normalized();
	}

	@Override
	public Intersection getIntersectionWithRay(Ray ray) {
		double i_radiusSqr = m_radius * m_radius;
		
		Vec i_L = m_center.sub(ray.m_pointOfOrigin());
		double i_tm = Vec.dotProd(i_L, ray.m_rayDirection());
		double i_dSqr = i_L.lengthSquared() - (i_tm * i_tm);
		
		if (i_dSqr<i_radiusSqr) {
			double i_th = Math.sqrt(i_radiusSqr - i_dSqr);
			double t = i_tm - i_th;
			if (t<=0) {
				t = i_tm + i_th;
				if (t>0) {
					Point3D p = Point3D.add(ray.m_pointOfOrigin(), Vec.scale(ray.m_rayDirection(), t));
					if (Vec.dotProd(ray.m_rayDirection(),getNormalAtPoint(p))<0.0d)
						return (new Intersection(p,this,t,getNormalAtPoint(p),ray.m_rayDirection()));
					else
						return null;
				}
				else
					return null;
			} else {
				Point3D p = Point3D.add(ray.m_pointOfOrigin(), Vec.scale(ray.m_rayDirection(), t));
				if (Vec.dotProd(ray.m_rayDirection(),getNormalAtPoint(p))<0.0d)
					return (new Intersection(p,this,t,getNormalAtPoint(p),ray.m_rayDirection()));
				else
					return null;
			}
		} else {
			return null;
		}
	}

}
