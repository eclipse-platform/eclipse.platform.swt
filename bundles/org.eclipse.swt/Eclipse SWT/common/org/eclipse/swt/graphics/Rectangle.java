/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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


import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class represent rectangular areas in an
 * (x, y) coordinate system. The top left corner of the rectangle
 * is specified by its x and y values, and the extent of the
 * rectangle is specified by its width and height.
 * <p>
 * The coordinate space for rectangles and points is considered
 * to have increasing values downward and to the right from its
 * origin making this the normal, computer graphics oriented notion
 * of (x, y) coordinates rather than the strict mathematical one.
 * </p>
 * <p>
 * The hashCode() method in this class uses the values of the public
 * fields to compute the hash value. When storing instances of the
 * class in hashed collections, do not modify these fields after the
 * object has been inserted.
 * </p>
 * <p>
 * Application code does <em>not</em> need to explicitly release the
 * resources managed by each instance when those instances are no longer
 * required, and thus no <code>dispose()</code> method is provided.
 * </p>
 *
 * @see Point
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */

public sealed class Rectangle implements Serializable, Cloneable permits Rectangle.OfFloat {

	/**
	 * the x coordinate of the rectangle
	 */
	public int x;

	/**
	 * the y coordinate of the rectangle
	 */
	public int y;

	/**
	 * the width of the rectangle
	 */
	public int width;

	/**
	 * the height of the rectangle
	 */
	public int height;

	static final long serialVersionUID = 3256439218279428914L;

/**
 * Construct a new instance of this class given the
 * x, y, width and height values.
 *
 * @param x the x coordinate of the origin of the rectangle
 * @param y the y coordinate of the origin of the rectangle
 * @param width the width of the rectangle
 * @param height the height of the rectangle
 */
public Rectangle (int x, int y, int width, int height) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
}

/**
 * Destructively replaces the x, y, width and height values
 * in the receiver with ones which represent the union of the
 * rectangles specified by the receiver and the given rectangle.
 * <p>
 * The union of two rectangles is the smallest single rectangle
 * that completely covers both of the areas covered by the two
 * given rectangles.
 * </p>
 *
 * @param rect the rectangle to merge with the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 */
public void add (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int left = x < rect.x ? x : rect.x;
	int top = y < rect.y ? y : rect.y;
	int lhs = x + width;
	int rhs = rect.x + rect.width;
	int right = lhs > rhs ? lhs : rhs;
	lhs = y + height;
	rhs = rect.y + rect.height;
	int bottom = lhs > rhs ? lhs : rhs;
	x = left;  y = top;  width = right - left;  height = bottom - top;
}

/**
 * Returns <code>true</code> if the point specified by the
 * arguments is inside the area specified by the receiver,
 * and <code>false</code> otherwise.
 *
 * @param x the x coordinate of the point to test for containment
 * @param y the y coordinate of the point to test for containment
 * @return <code>true</code> if the rectangle contains the point and <code>false</code> otherwise
 */
public boolean contains (int x, int y) {
	return (x >= this.x) && (y >= this.y) && x < (this.x + width) && y < (this.y + height);
}

/**
 * Returns <code>true</code> if the given point is inside the
 * area specified by the receiver, and <code>false</code>
 * otherwise.
 *
 * @param pt the point to test for containment
 * @return <code>true</code> if the rectangle contains the point and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 */
public boolean contains (Point pt) {
	if (pt == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return contains(pt.x, pt.y);
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param object the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode()
 */
@Override
public boolean equals(Object object) {
	if (object == null) {
		return false;
	}
	if (object == this) {
		return true;
	}
	if (!(object instanceof Rectangle other)) {
		return false;
	}
	return (other.x == this.x) && (other.y == this.y) && (other.width == this.width) && (other.height == this.height);
}

/**
 * Returns an integer hash code for the receiver. Any two
 * objects that return <code>true</code> when passed to
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals(Object)
 */
@Override
public int hashCode () {
	return x ^ y ^ width ^ height;
}

/**
 * Destructively replaces the x, y, width and height values
 * in the receiver with ones which represent the intersection of the
 * rectangles specified by the receiver and the given rectangle.
 *
 * @param rect the rectangle to intersect with the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 *
 * since 3.0
 */
public void intersect (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (this == rect) return;
	int left = x > rect.x ? x : rect.x;
	int top = y > rect.y ? y : rect.y;
	int lhs = x + width;
	int rhs = rect.x + rect.width;
	int right = lhs < rhs ? lhs : rhs;
	lhs = y + height;
	rhs = rect.y + rect.height;
	int bottom = lhs < rhs ? lhs : rhs;
	x = right < left ? 0 : left;
	y = bottom < top ? 0 : top;
	width = right < left ? 0 : right - left;
	height = bottom < top ? 0 : bottom - top;
}

/**
 * Returns a new rectangle which represents the intersection
 * of the receiver and the given rectangle.
 * <p>
 * The intersection of two rectangles is the rectangle that
 * covers the area which is contained within both rectangles.
 * </p>
 *
 * @param rect the rectangle to intersect with the receiver
 * @return the intersection of the receiver and the argument
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 */
public Rectangle intersection (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (this == rect) return new Rectangle (x, y, width, height);
	int left = x > rect.x ? x : rect.x;
	int top = y > rect.y ? y : rect.y;
	int lhs = x + width;
	int rhs = rect.x + rect.width;
	int right = lhs < rhs ? lhs : rhs;
	lhs = y + height;
	rhs = rect.y + rect.height;
	int bottom = lhs < rhs ? lhs : rhs;
	return new Rectangle (
		right < left ? 0 : left,
		bottom < top ? 0 : top,
		right < left ? 0 : right - left,
		bottom < top ? 0 : bottom - top);
}

/**
 * Returns <code>true</code> if the rectangle described by the
 * arguments intersects with the receiver and <code>false</code>
 * otherwise.
 * <p>
 * Two rectangles intersect if the area of the rectangle
 * representing their intersection is not empty.
 * </p>
 *
 * @param x the x coordinate of the origin of the rectangle
 * @param y the y coordinate of the origin of the rectangle
 * @param width the width of the rectangle
 * @param height the height of the rectangle
 * @return <code>true</code> if the rectangle intersects with the receiver, and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 *
 * @see #intersection(Rectangle)
 * @see #isEmpty()
 *
 * @since 3.0
 */
public boolean intersects (int x, int y, int width, int height) {
	return (x < this.x + this.width) && (y < this.y + this.height) &&
		(x + width > this.x) && (y + height > this.y);
}

/**
 * Returns <code>true</code> if the given rectangle intersects
 * with the receiver and <code>false</code> otherwise.
 * <p>
 * Two rectangles intersect if the area of the rectangle
 * representing their intersection is not empty.
 * </p>
 *
 * @param rect the rectangle to test for intersection
 * @return <code>true</code> if the rectangle intersects with the receiver, and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 *
 * @see #intersection(Rectangle)
 * @see #isEmpty()
 */
public boolean intersects (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return rect == this || intersects (rect.x, rect.y, rect.width, rect.height);
}

/**
 * Returns <code>true</code> if the receiver does not cover any
 * area in the (x, y) coordinate plane, and <code>false</code> if
 * the receiver does cover some area in the plane.
 * <p>
 * A rectangle is considered to <em>cover area</em> in the
 * (x, y) coordinate plane if both its width and height are
 * non-zero.
 * </p>
 *
 * @return <code>true</code> if the receiver is empty, and <code>false</code> otherwise
 */
public boolean isEmpty () {
	return (width <= 0) || (height <= 0);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the rectangle
 */
@Override
public String toString () {
	return "Rectangle {" + x + ", " + y + ", " + width + ", " + height + "}"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
}

/**
 * Returns a new rectangle which represents the union of
 * the receiver and the given rectangle.
 * <p>
 * The union of two rectangles is the smallest single rectangle
 * that completely covers both of the areas covered by the two
 * given rectangles.
 * </p>
 *
 * @param rect the rectangle to perform union with
 * @return the union of the receiver and the argument
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 * </ul>
 *
 * @see #add(Rectangle)
 */
public Rectangle union (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int left = x < rect.x ? x : rect.x;
	int top = y < rect.y ? y : rect.y;
	int lhs = x + width;
	int rhs = rect.x + rect.width;
	int right = lhs > rhs ? lhs : rhs;
	lhs = y + height;
	rhs = rect.y + rect.height;
	int bottom = lhs > rhs ? lhs : rhs;
	return new Rectangle (left, top, right - left, bottom - top);
}

/**
 * Creates a new {@code Rectangle} using the specified top-left point and
 * dimensions.
 * <p>
 * If the provided {@code Point} instance carries additional contextual
 * information, an extended {@code Rectangle} type may be returned to preserve
 * that context. Otherwise, a standard {@code Rectangle} is returned.
 * </p>
 *
 * @param topLeft the top-left corner of the rectangle
 * @param width   the width of the rectangle
 * @param height  the height of the rectangle
 * @return a new {@code Rectangle} instance appropriate for the given point and
 *         dimensions
 * @since 3.131
 */
public static Rectangle of(Point topLeft, int width, int height) {
	if (topLeft instanceof Point.WithMonitor monitorAwareTopLeft) {
		return new Rectangle.WithMonitor(monitorAwareTopLeft.getX(), monitorAwareTopLeft.getY(), width, height, monitorAwareTopLeft.getMonitor());
	}
	return new Rectangle(topLeft.x, topLeft.y, width, height);
}

/**
 * Creates and returns a copy of this {@code Rectangle}.
 * <p>
 * This method performs a shallow copy of the rectangle's fields: {@code x},
 * {@code y}, {@code width}, and {@code height}. It does not copy any
 * subclass-specific fields, so subclasses should override this method if
 * additional fields exist.
 * </p>
 *
 * @return a new {@code Rectangle} instance with the same position and size as
 *         this one
 * @since 3.131
 */
@Override
public Rectangle clone() {
	return new Rectangle(x, y, width, height);
}

/**
 * Instances of this class represent {@link org.eclipse.swt.graphics.Rectangle}
 * objects which supports values of Float type for it's fields
 *
 * @since 3.131
 * @noreference This class is not intended to be referenced by clients
 */
public static sealed class OfFloat extends Rectangle permits Rectangle.WithMonitor {

	private static final long serialVersionUID = -3006999002677468391L;

	private float residualX, residualY, residualWidth, residualHeight;

	public OfFloat(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public OfFloat(float x, float y, float width, float height) {
		super(Math.round(x), Math.round(y), Math.round(width), Math.round(height));
		this.residualX = x - this.x;
		this.residualY = y - this.y;
		this.residualWidth = width - this.width;
		this.residualHeight = height - this.height;
	}

	public float getX() {
		return x + residualX;
	}

	public float getY() {
		return y + residualY;
	}

	public float getWidth() {
		return width + residualWidth;
	}

	public float getHeight() {
		return height + residualHeight;
	}

	public void setX(float x) {
		this.x = Math.round(x);
		this.residualX = x - this.x;
	}

	public void setY(float y) {
		this.y = Math.round(y);
		this.residualY = y - this.y;
	}

	public void setWidth(float width) {
		this.width = Math.round(width);
		this.residualWidth = width - this.width;
	}

	public void setHeight(float height) {
		this.height = Math.round(height);
		this.residualHeight = height - this.height;
	}

}

/**
 * Instances of this class represent {@link org.eclipse.swt.graphics.Rectangle.OfFloat}
 * objects along with the context of the monitor in relation to which they are
 * placed on the display. The monitor awareness makes it easy to scale and
 * translate the rectangles between pixels and points.
 *
 * @since 3.131
 * @noreference This class is not intended to be referenced by clients
 */
public static final class WithMonitor extends Rectangle.OfFloat {

	private static final long serialVersionUID = 5041911840525116925L;

	private final Monitor monitor;

	/**
	 * Constructs a new Rectangle.WithMonitor
	 *
	 * @param x the x coordinate of the top left corner of the rectangle
	 * @param y the y coordinate of the top left corner of the rectangle
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle
	 * @param monitor the monitor with whose context the rectangle is created
	 */
	public WithMonitor(int x, int y, int width, int height, Monitor monitor) {
		super(x, y, width, height);
		this.monitor = monitor;
	}

	private WithMonitor(float x, float y, float width, float height, Monitor monitor) {
		super(x, y, width, height);
		this.monitor = monitor;
	}

	/**
	 * {@return the monitor with whose context the instance is created}
	 */
	public Monitor getMonitor() {
		return monitor;
	}

	@Override
	public Rectangle.WithMonitor clone() {
		return new Rectangle.WithMonitor(getX(), getY(), getWidth(), getHeight(), monitor);
	}

}
}