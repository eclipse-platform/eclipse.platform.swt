package org.eclipse.swt.graphics;

/**
 * A registry for managing the instance of an {@link ISVGRasterizer} implementation.
 * This allows for the registration and retrieval of a single rasterizer instance.
 *
 * @since 3.129
 */
public class SVGRasterizerRegistry {

	/**
     * The instance of the registered {@link ISVGRasterizer}.
     */
	private static ISVGRasterizer rasterizer;

	 /**
     * Registers the provided implementation of {@link ISVGRasterizer}.
     * If a rasterizer has already been registered, subsequent calls to this method
     * will have no effect.
     *
     * @param implementation the {@link ISVGRasterizer} implementation to register.
     */
	public static void register(ISVGRasterizer implementation) {
		if (rasterizer == null) {
			rasterizer = implementation;
		}
	}

	/**
     * Retrieves the currently registered {@link ISVGRasterizer} implementation.
     *
     * @return the registered {@link ISVGRasterizer}, or {@code null} if no implementation
     *         has been registered.
     */
	public static ISVGRasterizer getRasterizer() {
		return rasterizer;
	}
}