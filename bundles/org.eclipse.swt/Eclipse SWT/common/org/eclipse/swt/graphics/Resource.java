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

import java.util.function.*;

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
	 * Used to track not disposed SWT resource. A separate class allows
	 * not to have the {@link #finalize} when tracking is disabled, avoiding
	 * possible performance issues in GC.
	 */
	private static class ResourceTracker {
		/**
		 * Resource that is tracked here
		 */
		private Resource resource;

		/**
		 * Recorded at Resource creation if {@link #setNonDisposeHandler} was
		 * enabled, used to track resource disposal
		 */
		private Error allocationStack;

		/**
		 * Allows to ignore specific Resources even if they are not disposed
		 * properly, used for example for Fonts that SWT doesn't own.
		 */
		boolean ignoreMe;

		ResourceTracker(Resource resource, Error allocationStack) {
			this.resource = resource;
			this.allocationStack = allocationStack;
		}

		@Override
		protected void finalize() {
			if (ignoreMe) return;
			if (nonDisposedReporter == null) return;

			// If the Resource is GC'ed before it was disposed, this is a leak.
			if (!resource.isDisposed())
				nonDisposedReporter.accept(allocationStack);
		}
	}

	/**
	 * the device where this resource was created
	 */
	Device device;

	/**
	 * Used to report not disposed SWT resources, null by default
	 */
	private static Consumer<Error> nonDisposedReporter;

	/**
	 * Used to track not disposed SWT resource
	 */
	private ResourceTracker tracker;

	static {
		boolean trackingEnabled = Boolean.getBoolean("org.eclipse.swt.graphics.Resource.reportNonDisposed"); //$NON-NLS-1$
		if (trackingEnabled) {
			setNonDisposeHandler(exception -> {
				if (exception != null) {
					exception.printStackTrace();
				} else {
					System.err.println("SWT Resource was not properly disposed"); //$NON-NLS-1$
				}
			});
		}
	}

public Resource() {
	initNonDisposeTracking();
}

Resource(Device device) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	initNonDisposeTracking();
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

void ignoreNonDisposed() {
	if (tracker != null) {
		tracker.ignoreMe = true;
	}
}

void init() {
	if (device.tracking) device.new_Object(this);
}

void initNonDisposeTracking() {
	// Color doesn't really have any resource to be leaked, ignore.
	if (this instanceof Color) return;

	// Avoid performance costs of having '.finalize()' when not tracking.
	if (nonDisposedReporter == null) return;

	// Capture a stack trace to help investigating the leak
	Error error = new Error("SWT Resource was not properly disposed"); //$NON-NLS-1$

	// Allocate a helper class with '.finalize()' in it, it will do the actual
	// work of detecting and reporting errors. This works because Resource
	// holds the only reference to 'ResourceTracker' and therefore the tracker
	// is only GC'ed when Resource itself is ready to be GC'ed.
	tracker = new ResourceTracker(this, error);
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

/**
 * Enables detection of Resource objects for which {@link #dispose()} wasn't
 * called, which means a leak of native memory and/or OS resources.
 *
 * WARNING: the reporter will be called from a different thread. Do not block
 * it and do not throw any exceptions. It's best to queue the errors for some
 * other worker to process.
 *
 * @param reporter                object used to report detected errors. Use
 *                                null to disable tracking. Setting a new
 *                                reporter has an immediate effect.
 *
 * @since 3.116
 */
public static void setNonDisposeHandler(Consumer<Error> reporter) {
	nonDisposedReporter = reporter;
}

}
