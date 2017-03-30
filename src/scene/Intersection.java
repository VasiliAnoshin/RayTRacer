package scene;

import math.Point3D;
import math.Vec;
import scene.Surfaces.Surface;

public class Intersection {
	private Surface m_surface;
	private double m_min_t;
	private Vec m_normalOnSurfaceAtInsct;
	private Point3D m_pointOnSurface;
	private Vec m_hitRayDirection;
	
	public Intersection(Point3D point, Surface surface, double min_t, Vec surfaceNormal, Vec hitRayDir) {
		this.m_surface = surface;
		this.m_min_t = min_t;
		this.m_normalOnSurfaceAtInsct = surfaceNormal.clone();
		this.m_pointOnSurface = point.clone();
		this.m_hitRayDirection = hitRayDir.clone().normalized();
	}
	
	public Surface getHitSurface() {
		return m_surface;
	}
	
	public Point3D getIntersectionPoint() {
		return m_pointOnSurface;
	}
	
	public double getHitDistance() {
		return m_min_t;
	}
	
	public Vec getNormalOnSurface() {
		return m_normalOnSurfaceAtInsct;
	}
	
	public Vec getHitRayDirection() {
		return m_hitRayDirection;
	}
}
