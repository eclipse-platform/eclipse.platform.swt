/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.*;

/**
 * This class is the abstract superclass of all graphics resource objects.
 * Resources created by the application must be disposed.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation. However, it has not been marked
 * final to allow those outside of the SWT development team to implement
 * patched versions of the class in order to get around specific
 * limitations in advance of when those limitations can be addressed
 * by the team.  Any class built using subclassing to access the internals
 * of this class will likely fail to compile or run between releases and
 * may be strongly platform specific. Subclassing should not be attempted
 * without an intimate and detailed understanding of the workings of the
 * hierarchy. No support is provided for user-written classes which are
 * implemented as subclasses of this class.
 * </p>
 *
 * @see #dispose
 * @see #isDisposed
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 *
 * @since 3.1
 */
public abstract class Resource {

	/**
	 * Stack recorded at resource allocation time, wraped as Exception for
	 * convenience
	 *
	 * @since 3.116
	 */
	public static class NonDisposedException extends Exception {
		static final long serialVersionUID = 1;
	}

	/**
	 * Reports not disposed SWT resource
	 *
	 * @since 3.116
	 */
	public interface NonDisposedReporter {
		/**
		 * Note that this is called as part of {@link Object#finalize()}, please
		 * read its docs.
		 *
		 * @param resource        the Resource which wasn't properly disposed
		 * @param allocationStack stack trace that allocated the Resource, or
		 *                        null if {@link #collectAllocationStacks} is
		 *                        false.
		 * @see Resource#trackNonDisposed(boolean, NonDisposedReporter)
		 */
		void onNonDisposedResource(Resource resource, NonDisposedException allocationStack);
	}

	/**
	 * the device where this resource was created
	 */
	Device device;

	/**
	 * Used to report not disposed SWT resources, null by default
	 */
	private static NonDisposedReporter nonDisposedReporter;

	/**
	 * Set to {@code true} if leak detector should ignore not disposed resource
	 */
	boolean nonDisposedIgnore;

	/**
	 * Set to {@code true} if leak detector should create exceptions for not disposed resources
	 */
	private static boolean collectAllocationStacks;

	/**
	 * Recorded at object creation if {@link #trackNonDisposed(boolean)} was
	 * called with {@code true}, used to track resource disposal
	 */
	private NonDisposedException allocationStack;

	static {
		final String property = System.getProperty("org.eclipse.swt.graphics.Resource.reportNonDisposed");
		if (property != null) {
			if (property.equalsIgnoreCase("stacks")) {
				trackNonDisposed(true);
			} else if (property.equalsIgnoreCase("true")) {
				trackNonDisposed(false);
			}
		}
	}

public Resource() {
}

Resource(Device device) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	if (collectAllocationStacks && !(this instanceof Color)) {
		allocationStack = new NonDisposedException();
	}
}

void destroy() {
}

/**
 * Disposes of the operating system resources associated with
 * this resource. Applications must dispose of all resources
 * which they allocate.
 * This method does nothing if the resource is already disposed.
 */
public void dispose() {
	if (device == null) return;
	if (device.isDisposed()) return;
	destroy();
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

/**
 * Returns the allocation stack if {@link #collectAllocationStacks} is enabled.
 * Otherwise, will return {@code null}.
 *
 * @see #trackNonDisposed(boolean)
 * @since 3.116
 */
public NonDisposedException getAllocationStack() {
	return allocationStack;
}

/**
 * Returns the <code>Device</code> where this resource was
 * created.
 *
 * @return <code>Device</code> the device of the receiver
 *
 * @since 3.2
 */
public Device getDevice() {
	Device device = this.device;
	if (device == null || isDisposed ()) SWT.error (SWT.ERROR_GRAPHIC_DISPOSED);
	return device;
}

void init() {
	if (device.tracking) device.new_Object(this);
}

/**
 * Returns <code>true</code> if the resource has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the resource.
 * When a resource has been disposed, it is an error to
 * invoke any other method (except {@link #dispose()}) using the resource.
 *
 * @return <code>true</code> when the resource is disposed and <code>false</code> otherwise
 */
public abstract boolean isDisposed();

@Override
protected void finalize() {
	if (nonDisposedIgnore) return;
	if (nonDisposedReporter == null) return;
	if (isDisposed()) return;

	// Color doesn't really have any resource to be leaked, ignore
	if (this instanceof Color) return;

	nonDisposedReporter.onNonDisposedResource(this, allocationStack);
}

/**
 * Enables detection of Resource objects for which {@link #dispose()} wasn't
 * called, which means a leak of native memory and/or OS resources.
 *
 * @param collectAllocationStacks whether to collect allocation stacks for
 *                                better reports. This is a performance
 *                                tradeoff. Changing this value will only affect
 *                                future objects.
 * @param reporter                object used to report detected errors. Use
 *                                null to disable tracking. Setting a new
 *                                reporter has an immediate effect.
 *
 * @see #getAllocationStack()
 * @since 3.116
 */
public static void trackNonDisposed(boolean collectAllocationStacks, NonDisposedReporter reporter) {
	nonDisposedReporter = reporter;
	Resource.collectAllocationStacks = collectAllocationStacks;
}

/**
 * Enables detection of Resource objects for which {@link #dispose()} wasn't
 * called, which means a leak of native memory and/or OS resources. Uses default
 * reporter that will print to {@link System#err}.
 *
 * @param collectAllocationStacks whether to collect allocation stacks for
 *                                better reports. This is a performance
 *                                tradeoff. Changing this value will only affect
 *                                future objects.
 *
 * @see #getAllocationStack()
 * @since 3.116
 */
public static void trackNonDisposed(boolean collectAllocationStacks) {
	trackNonDisposed(collectAllocationStacks, (object, exception) -> {
		System.err.println("Resource was not properly disposed: " + object);
		if (exception != null)
			exception.printStackTrace();
	});
}

}
