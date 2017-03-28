package scene.Light;

import java.util.Map;
import math.Point3D;
import math.Vec;

public abstract class Light {
	protected Point3D m_color; // I 0 from the slides. default = (1,1,1) - white.
	
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
	
	public abstract Point3D getIntensityAtPoint(Point3D point);
	public abstract Point3D getOrigin(); // can be null
	public abstract Vec getDirection(); // 
}
