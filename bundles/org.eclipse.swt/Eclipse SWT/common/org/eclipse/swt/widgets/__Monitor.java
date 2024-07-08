/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
package org.eclipse.swt.widgets;
//
//import org.eclipse.swt.graphics.*;
//
///**
// * Instances of this class are descriptions of monitors.
// *
// * @see Display
// * @see <a href="http://www.eclipse.org/swt/snippets/#monitor">Monitor snippets</a>
// * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
// *
// * @since 3.0
// */
//public final class Monitor {
//	long handle;
//	int x, y, width, height;
//	int clientX, clientY, clientWidth, clientHeight;
//	int zoom = 100;
//
///**
// * Prevents uninitialized instances from being created outside the package.
// */
//Monitor () {
//}
//
///**
// * Compares the argument to the receiver, and returns true
// * if they represent the <em>same</em> object using a class
// * specific comparison.
// *
// * @param object the object to compare with this object
// * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
// *
// * @see #hashCode()
// */
//@Override
//public boolean equals (Object object) {
//	if (object == this) return true;
//	if (!(object instanceof Monitor monitor)) return false;
//	return handle == monitor.handle;
//}
//
///**
// * Returns a rectangle describing the receiver's size and location
// * relative to its device. Note that on multi-monitor systems the
// * origin can be negative.
// *
// * @return the receiver's bounding rectangle
// */
//public Rectangle getBounds () {
//	return new Rectangle (x, y, width, height);
//}
//
///**
// * Returns a rectangle which describes the area of the
// * receiver which is capable of displaying data.
// *
// * @return the client area
// */
//public Rectangle getClientArea () {
//	return new Rectangle (clientX, clientY, clientWidth, clientHeight);
//}
//
///**
// * Returns the zoom value for the monitor
// *
// * @return monitor's zoom value
// *
// * @since 3.107
// */
//public int getZoom () {
//	return zoom;
//}
//
//void setBounds (Rectangle rect) {
//	x = rect.x;
//	y = rect.y;
//	width = rect.width;
//	height = rect.height;
//}
//
//void setClientArea (Rectangle rect) {
//	clientX = rect.x;
//	clientY = rect.y;
//	clientWidth = rect.width;
//	clientHeight = rect.height;
//}
//
///**
// * Returns an integer hash code for the receiver. Any two
// * objects that return <code>true</code> when passed to
// * <code>equals</code> must return the same value for this
// * method.
// *
// * @return the receiver's hash
// *
// * @see #equals(Object)
// */
//@Override
//public int hashCode () {
//	return (int)handle;
//}
//
//}