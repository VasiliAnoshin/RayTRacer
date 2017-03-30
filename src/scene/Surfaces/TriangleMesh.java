package scene.Surfaces;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import math.Point3D;
import math.Ray;
import math.Vec;
import scene.Intersection;

public class TriangleMesh extends Surface{

	private class iTriangle {
		private Point3D m_p0,m_p1,m_p2;
		private Vec m_normal;
		
		public iTriangle(Point3D p0,Point3D p1,Point3D p2) {
			this.m_p0 = p0;
			this.m_p1 = p1;
			this.m_p2 = p2;
			this.m_normal = Vec.crossProd(new Vec(p0,p1), new Vec(p0,p2)).normalized();
		}
		
		public iTriangle(double[] pAr) {
			this(new Point3D(pAr[0],pAr[1],pAr[2]),
				 new Point3D(pAr[3],pAr[4],pAr[5]),
				 new Point3D(pAr[6],pAr[7],pAr[8]));
		}
		
		public Intersection getIntersectionWithRay(Ray ray, Surface srf) {
			if (m_normal.dotProd(ray.m_rayDirection())>=0)
				return null;
			
			double t = m_p0.sub(ray.m_pointOfOrigin()).dotProd(m_normal) / m_normal.dotProd(ray.m_rayDirection());
			Point3D intrPt = Point3D.add(ray.m_pointOfOrigin(), Vec.scale(ray.m_rayDirection(), t));
		
			if (t<0.0d) 
				return null;
			
			Vec v1,v2,v3,n1,n2,n3;
			double d1,d2,d3;
			
			v1 = m_p0.sub(ray.m_pointOfOrigin());
			v2 = m_p1.sub(ray.m_pointOfOrigin());
			v3 = m_p2.sub(ray.m_pointOfOrigin());
			
			n1 = Vec.crossProd(v2, v1);
			n2 = Vec.crossProd(v3, v2);
			n3 = Vec.crossProd(v1, v3);
			
			if ((n1.length()>0.0001) && 
				(n2.length()>0.0001) &&
				(n3.length()>0.0001)) { 
				
				d1 = ray.m_rayDirection().dotProd(n1.normalized());
				d2 = ray.m_rayDirection().dotProd(n2.normalized());
				d3 = ray.m_rayDirection().dotProd(n3.normalized());
				
				if (((d1>=0.0d) && (d2>=0.0d) && (d3>=0.0d)) ||
				    ((d1<=0.0d) && (d2<=0.0d) && (d3<=0.0d))) {
					return new Intersection(intrPt, srf, t , this.m_normal, ray.m_rayDirection());
				} else 
					return null;
			} else 
				return null;
		}
	}
	
	private List<iTriangle> m_triangles;
	
	public TriangleMesh(Map<String, String> attributes) {
		super(attributes);
		m_triangles = new LinkedList<TriangleMesh.iTriangle>();
		
		int i=0;
		double pAr[] = new double[9];
		String i_stTri;
		
		while ((i_stTri = attributes.get("tri" +i))!=null || i<2) {
			if (i_stTri!=null) {
				try {
					Scanner s = new Scanner(i_stTri);
					for (int j=0;j<9;j++)
						pAr[j] = s.nextDouble();
					m_triangles.add(new iTriangle(pAr));
				} catch (Exception e) {
					System.err.println("error with tri" + i + " parameters");
				}
			}
			i++;
		}
		//System.out.println("add " + m_triangles.size() + " triangals to trimesh");
	}
	
	@Override
	public Vec getNormalAtPoint(Point3D point) {
		//need to search for the tri containing the point
		//not needed in that implementation... 
		return null;
	}

	@Override
	public Intersection getIntersectionWithRay(Ray ray) {
		double t,min_t = Double.MAX_VALUE;
		Intersection i_newInersct,i_minInersct = null;
		
		for (iTriangle tri : m_triangles) {
			if ((i_newInersct = tri.getIntersectionWithRay(ray,this)) !=null) {
				t = i_newInersct.getHitDistance();
				if (t>0.0d && t<min_t) {
					min_t = t;
					i_minInersct = i_newInersct;
				}
			}
		}
		return i_minInersct;
	}


}
