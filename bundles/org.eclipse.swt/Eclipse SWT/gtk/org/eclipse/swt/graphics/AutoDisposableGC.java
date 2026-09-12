package org.eclipse.swt.graphics;

/**
 * An {@link AutoCloseable} wrapper around a {@link GC} that disposes the GC in
 * its {@link #close()} method. Instances are created via
 * {@link GC#create(Drawable)} and are intended for use in a try-with-resources
 * statement:
 *
 * <pre>
 *    try (AutoDisposableGC autoGC = GC.create(drawable)) {
 *        GC gc = autoGC.gc();
 *        // draw using gc
 *    }
 * </pre>
 *
 * @since 3.135
 */
public final class AutoDisposableGC implements AutoCloseable {

	private final GC gc;

	AutoDisposableGC(GC gc) {
		this.gc = gc;
	}

	/**
	 * @return the wrapped GC
	 */
	public GC gc() {
		return gc;
	}

	@Override
	public void close() {
		gc.dispose();
	}
}
