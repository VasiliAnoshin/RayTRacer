package scene.Light;

import java.util.Map;
import math.Point3D;
import math.Vec;

public class DirectionalLight extends Light{
	// direction in which the light shines at , this type of Light remind SUN 
	Vec m_direction; 
	
	public DirectionalLight(Map<String, String> attributes) {
		super(attributes);
		
		String i_stDir = attributes.get("direction");
		if (i_stDir == null) 
			i_stDir = attributes.get("dir");
		if (i_stDir == null)
			System.err.println("Directional light missint direction attribute");
		else 
			m_direction = new Vec(i_stDir).normalized();
	}
	
	@Override
	public Point3D getIntensityAtPoint(Point3D point) {
		return super.m_color;
	}

	@Override
	public Point3D getOrigin() {
		return null;
	}

	@Override
	public Vec getDirection() {
		return m_direction.clone();
	}
}
