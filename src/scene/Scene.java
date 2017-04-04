package scene;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import parser.Element;
import math.Point3D;
import math.Ray;
import math.Vec;
import scene.Light.DirectionalLight;
import scene.Light.Light;
import scene.Light.PointLight;
import scene.Light.spotLight;
import scene.Surfaces.Cylindr;
import scene.Surfaces.Sphere;
import scene.Surfaces.Surface;
import scene.Surfaces.TriangleMesh;

public class Scene {
	//background-col; default = (0,0,0)
	private Point3D m_backgroundColor;
	 // max-recursion-level; Default = 10;
	private int m_maxRecursionLevel;
	// ambient-light. I AL; default = (0,0,0);
	private Point3D m_ambientLight; 
	
	private List<Surface> m_surfaces;
	private List<Light> m_lights;
	
	public Scene (Map<String, String> sceneAttributes, List<Element> sceneElements) {
		String i_stBackgroundColor = sceneAttributes.get("background-col");
		String i_stMaxRecursionLevel = sceneAttributes.get("max-recursion-level");
		String i_stAmbientLight = sceneAttributes.get("ambient-light");
		
		if (i_stBackgroundColor!=null)
			m_backgroundColor = new Point3D(i_stBackgroundColor);
		else 
			m_backgroundColor = new Point3D(0,0,0);
		
		if (i_stAmbientLight!=null)
			m_ambientLight = new Point3D(i_stAmbientLight);
		else 
			m_ambientLight = new Point3D(0,0,0);
		
		if (i_stMaxRecursionLevel!=null) 
			m_maxRecursionLevel = Integer.parseInt(i_stMaxRecursionLevel);
		else 
			m_maxRecursionLevel = 10;
		
		m_surfaces = new LinkedList<Surface>();
		m_lights = new LinkedList<Light>();
		
		for (Element scnElement : sceneElements) {
			boolean wasAdd = true;
			Map<String, String> scnAtt = scnElement.getAttributes();
			if (scnElement.getName().equals("sphere"))
				m_surfaces.add(new Sphere(scnAtt));
			else if (scnElement.getName().equals("cylinder"))
				m_surfaces.add(new Cylindr(scnAtt));
			else if (scnElement.getName().equals("trimesh"))
				m_surfaces.add(new TriangleMesh(scnAtt));
			else if (scnElement.getName().equals("dir-light"))
					m_lights.add(new DirectionalLight(scnAtt));
			else if (scnElement.getName().equals("omni-light"))
				m_lights.add(new PointLight(scnAtt));
			else if (scnElement.getName().equals("spot-light"))
				m_lights.add(new spotLight(scnAtt));
			else {
				wasAdd = false;
				System.out.println("scene element: " + scnElement.getName() + " not supported.");
			}
			if (wasAdd)
				System.out.println("scene was add: " + scnElement.getName());
		}
	}
	
	public List<Light> getLigths() {
		return m_lights;
	}
	
	public Point3D getBackgroundColorPt () {
		return m_backgroundColor;
	}
	
	public Point3D getAmbientLight() {
		return m_ambientLight.clone();
	}
	
	public Intersection getIntersectionWithRay(Ray ray) { 
		double t,min_t = Double.MAX_VALUE;
		Intersection i_newInersct,i_minInersct = null;
		
		for (Surface surface : m_surfaces) {
			if ((i_newInersct = surface.getIntersectionWithRay(ray)) !=null) {
				t = i_newInersct.getHitDistance();
				if (t>0.0d && t<min_t) {
					min_t = t;
					i_minInersct = i_newInersct;
				}
			}
		}
		return i_minInersct;
	}
	
	private boolean lightIsOccluded(Light light,Intersection inersct) {
		//Directional light like a sun
		if (light instanceof DirectionalLight) {
			return false;
		} else { 
			//Build ray starting in origin with direction to our surface 
			Ray rayIsectToLight = new Ray(light.getOrigin(),inersct.getIntersectionPoint().sub(light.getOrigin()));
			//check if intersection point in the surface is the same point as we get when shooting ray from light source.
			return (getIntersectionWithRay(rayIsectToLight).getHitSurface()!=inersct.getHitSurface());
		}
	}
	
	public Point3D getColorAtIntersectPoint(Point3D eyePos,Intersection iSct,int level) {
		if (level == m_maxRecursionLevel)
			return new Point3D(0, 0, 0);
		
		Point3D iSctPt = iSct.getIntersectionPoint();
		Surface iSctSurface = iSct.getHitSurface();
		
		Point3D color = iSctSurface.getEmissionFactors();
		color.add(Point3D.mult(iSctSurface.getAmbientFactors(),m_ambientLight));
		//negate??
		Vec i_vToViewer = new Vec(eyePos,iSctPt).normalized(); 
		
		Vec i_normal = iSct.getNormalOnSurface();

		Vec i_vToLight=null;
		double dotP;
		
		for (Light light : m_lights) {
			if (light instanceof DirectionalLight) {
				i_vToLight = Vec.negate(light.getDirection()).normalized(); 
			} else {
				i_vToLight = new Vec(iSctPt,light.getOrigin()).normalized();
			}
			//Get Specular and Diffuse colors
			if (!lightIsOccluded(light, iSct)) {
				Point3D iL = light.getIntensityAtPoint(iSctPt);
				//check that two vectors are at the right angles to each other != 90degrees.
				if ((dotP = Vec.dotProd(i_normal, i_vToLight))>0.00001d)
					color.add(Point3D.scale(Point3D.mult(iSctSurface.getDiffuseFactors(),iL),dotP));
		    
				if ((dotP = Vec.dotProd(i_vToLight.reflect(i_normal), i_vToViewer))>0.00001d)
					color.add(Point3D.scale(Point3D.mult(iSctSurface.getSpecularFactors(),iL),Math.pow(dotP, iSctSurface.getShininessFactor())));
			}
		}
		
		Ray out_ray = new Ray(iSctPt, iSct.getHitRayDirection().reflect(i_normal));
		Intersection hit = getIntersectionWithRay(out_ray);
		if (hit!=null)
			color.add(Point3D.scale(getColorAtIntersectPoint(eyePos, hit, level+1),iSctSurface.getReflectanceFactor()));
		else
			color.add(Point3D.scale(m_backgroundColor,iSctSurface.getReflectanceFactor()));
			// Here should come a similar part for
			// a Refractive Ray */
		return color; 
	}
}
