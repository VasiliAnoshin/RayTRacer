package scene;

import java.util.Map;

import math.*;

public class Camera {
	//p0
	private Point3D m_eye; 
	// towards
	private Vec m_direction;  
	//private Point3D m_lookAt; //look-at
	//up-direction, camera.up vector
	private Vec m_upDirection; 
	 //screen-dist, vector.dist 
	private double m_screenDist;
	//screen-width, camera (frustum).width, default = 2.0
	private double m_screenWidth; 
	// m_direction X m_upDirection
	private Vec m_rightDirection; 
	
	private int m_halfCanvasHeight; 
	private int m_halfCanvasWidth;
	private double m_pixelRatio;
	private Point3D m_imageCenter;
	
	/**
	 * Attributes + Imageresolution( 𝑅↓𝑥 × 𝑅↓𝑦 )
	 * @param camAttributes
	 * @param canvasWidth
	 * @param canvasHeight
	 */	  	 
	public Camera (Map<String, String> camAttributes, int canvasWidth, int canvasHeight) {
		String i_stEye = camAttributes.get("eye");
		String i_stDirection = camAttributes.get("direction");
		String i_stLookAt = camAttributes.get("look-at");
		String i_stUp = camAttributes.get("up-direction");
		String i_stScrDist = camAttributes.get("screen-dist");
		String i_stScrWidth = camAttributes.get("screen-width");
		
		if ( (i_stEye==null) || (i_stUp==null) || (i_stScrDist==null) || ((i_stDirection==null) && (i_stLookAt==null)) ) {
			System.err.println("can't init camera:");
			if (i_stEye==null) System.err.println("missing attribute - eye.");
			if (i_stUp==null) System.err.println("missing attribute - up-direction.");
			if (i_stScrDist==null) System.err.println("missing attribute - screen-dist.");
			if ((i_stDirection==null) && (i_stLookAt==null)) System.err.print("missing attribute - direction or look-at.");	
		} else {
			m_eye = new Point3D(i_stEye);
			
			if (i_stDirection!=null)
				m_direction = (new Vec(i_stDirection)).normalized();
			else
				m_direction = new Vec(m_eye,new Point3D(i_stLookAt)).normalized();			
			m_upDirection =  new Vec(i_stUp).normalized();
			//What if the user provide us not perpendicular , not normalized  𝑉↓𝑡𝑜 , 𝑉↓𝑢𝑝 ? 
			m_rightDirection = Vec.crossProd(m_direction, m_upDirection).normalized();
			m_upDirection = Vec.crossProd(m_rightDirection, m_direction);
			//Plane width(𝑤)
			if (i_stScrWidth==null) 
				m_screenWidth = 2.0d;
			else
				m_screenWidth = Double.parseDouble(i_stScrWidth);
			//Distance to plane (𝑑) 
			m_screenDist = Double.parseDouble(i_stScrDist);
			
			//Only the screen width is specified. The screen height needs to be deduced according to
			//the aspect-ratio of the canvas.
			m_halfCanvasHeight = canvasHeight/2;
			m_halfCanvasWidth = canvasWidth/2;
			m_imageCenter = Point3D.add(m_eye,Vec.scale(m_direction,m_screenDist)); 
			m_pixelRatio = m_screenWidth / canvasWidth;
		}
	}
	//get eye position
	public Point3D getEyePos() {
		return m_eye.clone();
	}
	
	public Ray constructRayThroughPixel(double x, double y) {		
		// pCenter = p0 + d*vTo
		// Ratio (pixel width): 𝑅=𝑤/𝑅_𝑥 
		// The central pixel is ⌊𝑅_𝑥/2⌋,⌊𝑅_𝑦/2⌋
		// 𝑃=𝑃_𝑐+(𝑥−⌊𝑅_𝑥/2⌋)𝑅 𝑉_𝑟𝑖𝑔ℎ𝑡−(𝑦−⌊𝑅_𝑦/2⌋)𝑅 𝑉 ̃_𝑢𝑝
		// 𝑉=𝑃−𝑃_0
		
		Vec i_right = Vec.scale(m_rightDirection,(x - m_halfCanvasWidth) * m_pixelRatio); 
		//Vec i_up = Vec.scale(Vec.negate(m_upDirection),(y - m_halfCanvasHeight) * m_pixelRatio);
		Vec i_up = Vec.scale(Vec.negate(m_upDirection),(y - m_halfCanvasHeight) * m_pixelRatio);
		Vec i_rayVec =  Point3D.add(m_imageCenter,Vec.add(i_right,i_up)).sub(m_eye).normalized();
		//Vector is a direction and a magnitude (for example, 10 m north-east). 
		//A ray is just a direction and a starting point, and it has infinite length.
		return new Ray(m_eye,i_rayVec);
	}
}
