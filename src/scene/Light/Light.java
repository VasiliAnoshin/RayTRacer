package scene.Light;

import java.util.Map;
import math.Point3D;
import math.Vec;

public abstract class Light {
	//Color of the light : I0 from the slides. default = (1,1,1) - white.
	protected Point3D m_color; 
	
	public Light(Map<String, String> attributes) {
		String i_stColor = attributes.get("color");
		if (i_stColor==null) 
			m_color = new Point3D(1.0d, 1.0d, 1.0d);
		else
			m_color = new Point3D(i_stColor);
	}
	
	public Light() {
		m_color = new Point3D(1.0d, 1.0d, 1.0d);
	}
	//Light Intensity
	public abstract Point3D getIntensityAtPoint(Point3D point);
	// Position can be null , for example for Sun.
	public abstract Point3D getOrigin();
	//Direction , can be null for pointLight for example. 
	public abstract Vec getDirection();  
}
