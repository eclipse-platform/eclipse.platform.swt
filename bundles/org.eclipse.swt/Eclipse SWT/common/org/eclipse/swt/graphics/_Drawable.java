///*******************************************************************************
// * Copyright (c) 2000, 2016 IBM Corporation and others.
// *
// * This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License 2.0
// * which accompanies this distribution, and is available at
// * https://www.eclipse.org/legal/epl-2.0/
// *
// * SPDX-License-Identifier: EPL-2.0
// *
// * Contributors:
// *     IBM Corporation - initial API and implementation
// *******************************************************************************/
//package org.eclipse.swt.graphics;
//
//
///**
// * Implementers of <code>Drawable</code> can have a graphics context (GC)
// * created for them, and then they can be drawn on by sending messages to
// * their associated GC. SWT images, and device objects such as the Display
// * device and the Printer device, are drawables.
// * <p>
// * <b>IMPORTANT:</b> This interface is <em>not</em> part of the SWT
// * public API. It is marked public only so that it can be shared
// * within the packages provided by SWT. It should never be
// * referenced from application code.
// * </p>
// *
// * @see Device
// * @see Image
// * @see GC
// */
//public interface Drawable {
//
///**
// * Invokes platform specific functionality to allocate a new GC handle.
// * <p>
// * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
// * API for <code>Drawable</code>. It is marked public only so that it
// * can be shared within the packages provided by SWT. It is not
// * available on all platforms, and should never be called from
// * application code.
// * </p>
// *
// * @param data the platform specific GC data
// * @return the platform specific GC handle
// *
// * @noreference This method is not intended to be referenced by clients.
// */
//
//long  internal_new_GC (GCData data);
//
///**
// * Invokes platform specific functionality to dispose a GC handle.
// * <p>
// * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
// * API for <code>Drawable</code>. It is marked public only so that it
// * can be shared within the packages provided by SWT. It is not
// * available on all platforms, and should never be called from
// * application code.
// * </p>
// *
// * @param handle the platform specific GC handle
// * @param data the platform specific GC data
// *
// * @noreference This method is not intended to be referenced by clients.
// */
//void internal_dispose_GC (long  handle, GCData data);
//
///**
// * Returns <code>true</code> iff coordinates can be auto-scaled on this
// * drawable and <code>false</code> if not. E.g. a {@link GC} method should not
// * auto-scale the bounds of a figure drawn on a Printer device, but it may have
// * to auto-scale when drawing on a high-DPI Display monitor.
// *
// * @return <code>true</code> if auto-scaling is enabled for this drawable
// *
// * @noreference This method is not intended to be referenced by clients.
// */
//default boolean isAutoScalable () {
//	return true;
//}
//
//}
