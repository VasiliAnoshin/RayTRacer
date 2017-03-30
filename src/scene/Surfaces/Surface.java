package scene.Surfaces;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;
import scene.Intersection;

public abstract class Surface {
	/*
	 * (material)-diffuse - (rgb) the diffuse part of a flat material (K D ) default = (0.7, 0.7, 0.7)
	 * (material)-specular - (rgb) the specular part of the material (K S ) default = (1, 1, 1)
	 * (material)-ambient - (rgb) the ambient part of the material (K A ) default = (0.1, 0.1, 0.1)
	 * (material)- emission - (rgb) the emission part of the material (I E ) default = (0, 0, 0)
	 * (material)-shininess - (number) the power of the (V ∙ R ) in the formula (n). default = 100
	 * reflectance - (number) the reflectance coefficient of the material. K S from slides. default = 0 (no reflectance).
	 */
	// mtl-diffuse (K D ) default = (0.7, 0.7, 0.7)
	protected Point3D m_mtlDiffuse;  
	// mtl-specular (K S ) default = (1, 1, 1)
	protected Point3D m_mtlSpecular; 
	// mtl-ambient (K A ) default = (0.1, 0.1, 0.1)
	protected Point3D m_mtlAmbient;  
	// mtl-emission (I E ) default = (0, 0, 0)
	protected Point3D m_mtlEmission; 
	// mtl-shininess n, (V ∙ R )^(n) power, default = 100
	protected int m_mtlShininess;
	//reflectance (K S), default = 0 (no reflectance).
	protected double m_mtlReflectance; 
	
	
	//definitions for the 
	public Surface(Map<String, String> attributes) {
		String i_stMtlDiffuse = attributes.get("mtl-diffuse");
		String i_stMtlSpecular = attributes.get("mtl-specular");
		String i_stMtlAmbient = attributes.get("mtl-ambient");
		String i_stMtlEmission = attributes.get("mtl-emission");
		String i_stMtlShininess = attributes.get("mtl-shininess");
		String i_stMtlReflectance = attributes.get("reflectance");
		
		if (i_stMtlDiffuse!=null) 
			m_mtlDiffuse = new Point3D(i_stMtlDiffuse);
		else
			m_mtlDiffuse = new Point3D(0.7d,0.7d,0.7d);
		
		if (i_stMtlSpecular!=null) 
			m_mtlSpecular = new Point3D(i_stMtlSpecular);
		else
			m_mtlSpecular = new Point3D(1,1,1);
		
		if (i_stMtlAmbient!=null) 
			m_mtlAmbient = new Point3D(i_stMtlAmbient);
		else
			m_mtlAmbient = new Point3D(0.1d,0.1d,0.1d);
		
		if (i_stMtlEmission!=null) 
			m_mtlEmission = new Point3D(i_stMtlEmission);
		else
			m_mtlEmission = new Point3D(0,0,0);
		
		if (i_stMtlShininess!=null) 
			m_mtlShininess = Integer.parseInt(i_stMtlShininess);
		else
			m_mtlShininess = 100;
		
		if (i_stMtlReflectance!=null)
			m_mtlReflectance = Double.parseDouble(i_stMtlReflectance);
		else 
			m_mtlReflectance = 0;
	}
	
	//public abstract double getLenghtOfHitByRay(Ray ray);
	public abstract Vec getNormalAtPoint(Point3D point);
	public abstract Intersection getIntersectionWithRay(Ray ray);
	
	public Point3D getDiffuseFactors() {
		return m_mtlDiffuse.clone();
	}
	public Point3D getSpecularFactors() {
		return m_mtlSpecular.clone();
	}
	public Point3D getAmbientFactors() {
		return m_mtlAmbient.clone();
	}
	public Point3D getEmissionFactors() {
		return m_mtlEmission.clone();
	}
	public int getShininessFactor() {
		return m_mtlShininess;
	}
	public double getReflectanceFactor() {
		return m_mtlReflectance;
	}
}
