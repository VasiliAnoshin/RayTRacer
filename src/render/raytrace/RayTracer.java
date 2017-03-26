package render.raytrace;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import parser.SceneDescriptor;
import render.IRenderer;

public class RayTracer implements IRenderer {

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
				
		// TODO Implement this. Read the data stored in sceneDesc to initialize your scene objects
		// (camera, surfaces, lights etc.).
		// Example:
		// Map<String, String> attributes = sceneDesc.getSceneAttributes();
		// if (attributes.containsKey("radius"))
		//	   r = Double.valueOf(attributes.get("radius"));
		// Make sure you follow software design practices, e.g. every surface type handles its own attributes.
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
		// TODO Implement this. This renders pixel (x,y) of the image. This is very similar to the code in the lecture's slides.
		return Color.BLACK;
	}
	
	

}
