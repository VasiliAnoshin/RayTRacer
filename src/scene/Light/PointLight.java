package scene.Light;

import java.util.Map;

import math.Point3D;
import math.Vec;

public class PointLight extends Light{
	//position specific in the scene , this type of Light remind LAMP.	
	//TODO DELETE ? The same point defined in the upper class. 
	Point3D m_pos; 
	//Attenuation (Ослабление) - (3 numbers- kc ,kl , kq ) the Default = (1,0,0) - no attenuation, only constant factor.
	//Attenuation depends on distance of the Lamp from the object. 
	//Can be computed by polynomial function.		
	double m_kc,m_kl,m_kq; 	 
	
	public PointLight(Map<String, String> attributes) {
		super(attributes);
		
		String i_stPos = attributes.get("pos");
		if (i_stPos==null) 
			System.err.println("point light missint pos attribute");
		else 
			m_pos = new Point3D(i_stPos);
		
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

	@Override
	public Point3D getIntensityAtPoint(Point3D point) {
		double dist = new Vec(point,m_pos).length();
		return (Point3D.scale(super.m_color,1.0d/(m_kc + m_kl * dist + m_kq * dist * dist)));
	}

	@Override
	public Point3D getOrigin() {
		return m_pos.clone();
	}

	@Override
	public Vec getDirection() {
		return null;
	}
}
