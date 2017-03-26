package render;

import render.raytrace.RayTracer;

/**
 * Returns the default renderer
 */
public class RendererFactory {

	/**
	 * Instantiates a new renderer and returns it
	 * 
	 * @return
	 */
	public static IRenderer newInstance() {
		return new RayTracer();
	}
}