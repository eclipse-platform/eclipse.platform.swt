package org.eclipse.swt.internal.skia;

import io.github.humbleui.types.IRect;

/**
 * Interface to abstract Skija's Surface for SWT integration.
 * This allows decoupling from the direct Skija dependency in client code.
 */
public interface ISkSurface extends AutoCloseable {
	/**
	 * Returns the width of the surface in pixels.
	 */
	int getWidth();

	/**
	 * Returns the height of the surface in pixels.
	 */
	int getHeight();

	/**
	 * Returns true if the surface is closed/disposed.
	 */
	boolean isClosed();

	/**
	 * Returns the ISkCanvas for drawing operations.
	 */
	ISkCanvas getCanvas();

	/**
	 * Creates an image snapshot of the current surface contents.
	 * The return type should be an abstraction (e.g., ISkImage) but for now Object.
	 */
	ISkImage makeImageSnapshot();

	ISkImage makeImageSnapshot(IRect rect);

	/**
	 * Creates a new surface with the given width and height.
	 * The return type should be ISkSurface.
	 */
	ISkSurface makeSurface(int width, int height);

	/**
	 * Closes/releases the surface and its resources.
	 */
	@Override
	void close();
}