package render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import math.Point3D;
import math.Ray;
import parser.SceneDescriptor;
import scene.Camera;
import scene.Intersection;
import scene.Scene;

public class RayTracer implements IRenderer {
	//background-tex; default = null;
	private BufferedImage m_backgroundImage;  
	private Camera m_camrea;
	private Scene m_scene;
	private int m_superSampWidth;
	private int width, height;
	
	/**
	 * Inits the renderer with scene description and sets the target canvas to
	 * size (width X height). After init renderLine may be called
	 * 
	 * @param sceneDesc
	 *            Description data structure of the scene
	 * @param width
	 *            Width of the canvas
	 * @param height
	 *            Height of the canvas
	 * @param path
	 *            File path to the location of the scene. Should be used as a
	 *            basis to load external resources (e.g. background image)
	 */
	@Override
	public void init(SceneDescriptor sceneDesc, int width, int height, File path) {
		this.width = width;
		this.height = height;
		
		m_camrea = new Camera(sceneDesc.getCameraAttributes(),width,height);
		m_scene = new Scene(sceneDesc.getSceneAttributes(),sceneDesc.getObjects());
		
		String i_stSuperSampl = sceneDesc.getSceneAttributes().get("super-samp-width");
		String i_stBkgImgFile = sceneDesc.getSceneAttributes().get("background-tex");
		
		if (i_stBkgImgFile!=null){
			setBackgroundBufferedImg(path.getParent() + File.separator + i_stBkgImgFile);
		}	
		if (i_stSuperSampl!=null){
			m_superSampWidth = Integer.parseInt(i_stSuperSampl);
		}
		else{
			m_superSampWidth = 1;
		}
	}
	//if backgroundImage is Exist							
	private void setBackgroundBufferedImg(String stFullPath) {
		File i_backgroundImageFile = new File(stFullPath);
		if (!i_backgroundImageFile.exists()) 
			System.err.println("file not found: " + i_backgroundImageFile.toString());
		else {
			try {
				m_backgroundImage = ImageIO.read(i_backgroundImageFile);
				BufferedImage nImg = new BufferedImage(this.width, this.height, m_backgroundImage.getType());
				Graphics g = nImg.createGraphics();
				g.drawImage(m_backgroundImage, 0, 0, this.width, this.height, null);
				g.dispose();
				m_backgroundImage = nImg;
			} catch (IOException e) {
				System.err.println("error reading file: " + i_backgroundImageFile.toString());
			} 
		}
	}
	/**
	 * Renders the given line to the given canvas. Canvas is of the exact size
	 * given to init. This method must be called only after init.
	 * 
	 * @param canvas
	 *            BufferedImage containing the partial image
	 * @param line
	 *            The line of the image that should be rendered.
	 */
	@Override
	public void renderLine(BufferedImage canvas, int y) {
		for (int x = 0; x < width; ++x) {
			canvas.setRGB(x, y, traceRay(x, y).getRGB());
		}
	}
	
	@Override
	public Color traceRay(int x, int y) {
		Point3D colorPt = new Point3D(0,0,0);
		try {
			double d = 1.0d/m_superSampWidth; 
			double dDiv2 = d / 2.0d;
			for (int x1=0;x1<m_superSampWidth;x1++) {
				for (int y1=0;y1<m_superSampWidth;y1++) {
					Ray ray = m_camrea.constructRayThroughPixel(x+dDiv2 + x1*d, y+dDiv2+y1*d);
					Intersection scnIntersection = m_scene.getIntersectionWithRay(ray);
					if (scnIntersection!=null) {
						colorPt.add(m_scene.getColorAtIntersectPoint(m_camrea.getEyePos(),scnIntersection,0));
					} else {
						if (m_backgroundImage!=null) {
							Color c = new Color(m_backgroundImage.getRGB(x, y));
							colorPt.add(new Point3D(c.getRed()/255.0d,c.getGreen()/255.0d,c.getBlue()/255.0d));
						} else {
							colorPt.add(m_scene.getBackgroundColorPt());
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("error at (" + x + "," + y + ")" + e.getMessage());
			return Color.BLACK;
		}
		return Point3D.scale(colorPt, 1.0d/(m_superSampWidth*m_superSampWidth)).toColor();
	
	}
	
	

}
