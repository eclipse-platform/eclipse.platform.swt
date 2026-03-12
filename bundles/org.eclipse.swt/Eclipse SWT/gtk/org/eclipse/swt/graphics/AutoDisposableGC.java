package org.eclipse.swt.graphics;

/**
 * An AutoCloseable implementation of GC which calls dispose in the close
 * method. Can be created via {@link GC#create(Drawable)}.
 *
 * @since 3.133
 */
public final class AutoDisposableGC extends GC implements AutoCloseable {

	AutoDisposableGC(Drawable drawable, int style) {
		super(drawable, style);
	}
	@Override
	public void close() {
		dispose();
	}
}