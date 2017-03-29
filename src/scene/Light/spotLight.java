package scene.Light;

import java.util.Map;

import math.Point3D;
import math.Vec;

public class spotLight extends Light{
	//position of the light
	Point3D m_pos; 
	//direction
	Vec m_direction;
	 //attenuation - (3 numbers- k c ,k l , k q ) the Default = (1,0,0) - no attenuation, only constant factor.	
	double m_kc,m_kl,m_kq; 	
	
	public spotLight(Map<String, String> attributes) {
		super(attributes);
		
		String i_stPos = attributes.get("pos");
		if (i_stPos==null) 
			System.err.println("spot light missint pos attribute");
		else 
			m_pos = new Point3D(i_stPos);
		
		String i_stDir = attributes.get("direction");
		if (i_stDir==null) 
			i_stDir = attributes.get("dir");
		if (i_stDir==null)
			System.err.println("spot light missint direction attribute");
		else 
			m_direction = new Vec(i_stDir).normalized();
		
		m_kc = 1.0d;
		m_kl = 0.0d;
		m_kq = 0.0d;
		
		String i_stAttenuation = attributes.get("kc");
		if (i_stAttenuation!=null)
			m_kc = Double.parseDouble(i_stAttenuation);
	    i_stAttenuation = attributes.get("kl");
		if (i_stAttenuation!=null)
			m_kl = Double.parseDouble(i_stAttenuation);
		i_stAttenuation = attributes.get("kq");
		if (i_stAttenuation!=null)
			m_kq = Double.parseDouble(i_stAttenuation);
	}
	
	public Point3D getIntensityAtPoint(Point3D point) {
		Vec L = new Vec(m_pos,point);
		
		if (L.dotProd(m_direction)>0.0d) {
			double dist = L.length();
			L.normalize();
			return (Point3D.scale(super.m_color,1.0d/( (m_kc + m_kl * dist + m_kq * dist * dist) * (1.0d/L.dotProd(m_direction)))));
		} else {
			return (new Point3D(0,0,0));
		}
			
	}
	
	@Override
	public Point3D getOrigin() {
		return m_pos.clone();
	}

	@Override
	public Vec getDirection() {
		return m_direction.clone();
	}
}