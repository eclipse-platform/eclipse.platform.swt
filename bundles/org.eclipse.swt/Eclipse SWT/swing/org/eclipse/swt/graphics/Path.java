/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;

import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;

/**
 * Instances of this class represent paths through the two-dimensional
 * coordinate system. Paths do not have to be continuous, and can be
 * described using lines, rectangles, arcs, cubic or quadratic bezier curves,
 * glyphs, or other paths.
 * <p>
 * Application code must explicitly invoke the <code>Path.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * 
 * @since 3.1
 */
public class Path extends Resource {
	
  /**
   * the OS resource for the Path
   * (Warning: This field is platform dependent)
   * <p>
   * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
   * public API. It is marked public only so that it can be shared
   * within the packages provided by SWT. It is not available on all
   * platforms and should never be accessed from application code.
   * </p>
   */
	public GeneralPath handle;
	
/**
 * Constructs a new empty Path.
 * 
 * @param device the device on which to allocate the path
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the device is null and there is no current device</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle for the path could not be obtained/li>
 * </ul>
 * 
 * @see #dispose()
 */
public Path (Device device) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	handle = new GeneralPath();
	if (handle == null) SWT.error(SWT.ERROR_NO_HANDLES);
	if (device.tracking) device.new_Object(this);
}

/**
 * Adds to the receiver a circular or elliptical arc that lies within
 * the specified rectangular area.
 * <p>
 * The resulting arc begins at <code>startAngle</code> and extends  
 * for <code>arcAngle</code> degrees.
 * Angles are interpreted such that 0 degrees is at the 3 o'clock
 * position. A positive value indicates a counter-clockwise rotation
 * while a negative value indicates a clockwise rotation.
 * </p><p>
 * The center of the arc is the center of the rectangle whose origin 
 * is (<code>x</code>, <code>y</code>) and whose size is specified by the 
 * <code>width</code> and <code>height</code> arguments. 
 * </p><p>
 * The resulting arc covers an area <code>width + 1</code> pixels wide
 * by <code>height + 1</code> pixels tall.
 * </p>
 *
 * @param x the x coordinate of the upper-left corner of the arc
 * @param y the y coordinate of the upper-left corner of the arc
 * @param width the width of the arc
 * @param height the height of the arc
 * @param startAngle the beginning angle
 * @param arcAngle the angular extent of the arc, relative to the start angle
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void addArc(float x, float y, float width, float height, float startAngle, float arcAngle) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (width < 0) {
		x = x + width;
		width = -width;
	}
	if (height < 0) {
		y = y + height;
		height = -height;
	}
	if (width == 0 || height == 0 || arcAngle == 0) return;
	handle.append(new Arc2D.Float(x, y, width, height, startAngle,
				arcAngle, Arc2D.OPEN), true);
}

/**
 * Adds to the receiver the path described by the parameter.
 *
 * @param path the path to add to the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void addPath(Path path) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (path.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	//TODO - expose connect?
	handle.append(path.handle, true);
}

/**
 * Adds to the receiver the rectangle specified by x, y, width and height.
 *
 * @param x the x coordinate of the rectangle to add
 * @param y the y coordinate of the rectangle to add
 * @param width the width of the rectangle to add
 * @param height the height of the rectangle to add
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void addRectangle(float x, float y, float width, float height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	handle.append(new Rectangle2D.Float(x, y, width, height), true);
}

/**
 * Adds to the receiver the pattern of glyphs generated by drawing
 * the given string using the given font starting at the point (x, y).
 *
 * @param string the text to use
 * @param x the x coordinate of the starting point
 * @param y the y coordinate of the starting point
 * @param font the font to use
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the font is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the font has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void addString(String string, float x, float y, org.eclipse.swt.graphics.Font font) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	FontRenderContext frc = new FontRenderContext(null, true, true);
	GlyphVector glyph = font.handle.createGlyphVector(frc, string);
	LineMetrics lm = font.handle.getLineMetrics(string, frc);
	handle.append(glyph.getOutline(x, y + lm.getAscent()), true);
}

/**
 * Closes the current sub path by adding to the receiver a line
 * from the current point of the path back to the starting point
 * of the sub path.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void close() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	handle.closePath();
}

/**
 * Returns <code>true</code> if the specified point is contained by
 * the receiver and false otherwise.
 * <p>
 * If outline is <code>true</code>, the point (x, y) checked for containment in
 * the receiver's outline. If outline is <code>false</code>, the point is
 * checked to see if it is contained within the bounds of the (closed) area
 * covered by the receiver.
 *
 * @param x the x coordinate of the point to test for containment
 * @param y the y coordinate of the point to test for containment
 * @param gc the GC to use when testing for containment
 * @param outline controls whether to check the outline or contained area of the path
 * @return <code>true</code> if the path contains the point and <code>false</code> otherwise
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the gc has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public boolean contains(float x, float y, GC gc, boolean outline) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (gc == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (gc.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	//TODO - should use GC transformation
	// TODO support outline
	return handle.contains(x, y);
}

/**
 * Adds to the receiver a cubic bezier curve based on the parameters.
 *
 * @param cx1 the x coordinate of the first control point of the spline
 * @param cy1 the y coordinate of the first control of the spline
 * @param cx2 the x coordinate of the second control of the spline
 * @param cy2 the y coordinate of the second control of the spline
 * @param x the x coordinate of the end point of the spline
 * @param y the y coordinate of the end point of the spline
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void cubicTo(float cx1, float cy1, float cx2, float cy2, float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	handle.curveTo(cx1, cy1, cx2, cy2, x, y);
}

/**
 * Disposes of the operating system resources associated with
 * the Path. Applications must dispose of all Paths that
 * they allocate.
 */
public void dispose() {
	if (handle == null) return;
	if (device.isDisposed()) return;
	handle = null;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

/**
 * Replaces the first four elements in the parameter with values that
 * describe the smallest rectangle that will completely contain the
 * receiver (i.e. the bounding box).
 *
 * @param bounds the array to hold the result
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter is too small to hold the bounding box</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void getBounds(float[] bounds) {
  if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  if (bounds == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
  if (bounds.length < 4) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
  Rectangle2D boundingBox = handle.getBounds2D();
  bounds[0] = (float)boundingBox.getX();
  bounds[1] = (float)boundingBox.getY();
  bounds[2] = (float)boundingBox.getWidth();
  bounds[3] = (float)boundingBox.getHeight();
}

/**
 * Replaces the first two elements in the parameter with values that
 * describe the current point of the path.
 *
 * @param point the array to hold the result
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parameter is too small to hold the end point</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void getCurrentPoint(float[] point) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (point == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (point.length < 2) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	Point2D p = handle.getCurrentPoint();
	point[0] = (float) p.getX();
	point[1] = (float) p.getY();
}

/**
 * Returns a device independent representation of the receiver.
 * 
 * @return the PathData for the receiver
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 * 
 * @see PathData
 */
public PathData getPathData() {
  if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
  List typeList = new ArrayList();
  List pointList = new ArrayList();
  float[] values = new float[6];
  for(PathIterator pathIterator = handle.getPathIterator(null); !pathIterator.isDone(); pathIterator.next()) {
    switch(pathIterator.currentSegment(values)) {
      case PathIterator.SEG_MOVETO:
        typeList.add(new Byte((byte)SWT.PATH_MOVE_TO));
        break;
      case PathIterator.SEG_LINETO:
        typeList.add(new Byte((byte)SWT.PATH_LINE_TO));
        pointList.add(new Float(values[0]));
        pointList.add(new Float(values[1]));
        break;
      case PathIterator.SEG_QUADTO:
        typeList.add(new Byte((byte)SWT.PATH_QUAD_TO));
        pointList.add(new Float(values[0]));
        pointList.add(new Float(values[1]));
        pointList.add(new Float(values[2]));
        pointList.add(new Float(values[3]));
        break;
      case PathIterator.SEG_CUBICTO:
        typeList.add(new Byte((byte)SWT.PATH_CUBIC_TO));
        pointList.add(new Float(values[0]));
        pointList.add(new Float(values[1]));
        pointList.add(new Float(values[2]));
        pointList.add(new Float(values[3]));
        pointList.add(new Float(values[4]));
        pointList.add(new Float(values[5]));
        break;
      case PathIterator.SEG_CLOSE:
        typeList.add(new Byte((byte)SWT.PATH_CLOSE));
        break;
    }
  }
  byte[] types = new byte[typeList.size()];
  for(int i=typeList.size()-1; i>=0; i--) {
    types[i] = ((Byte)typeList.get(i)).byteValue();
  }
  float[] points = new float[pointList.size()];
  for(int i=pointList.size()-1; i>=0; i--) {
    points[i] = ((Float)typeList.get(i)).floatValue();
  }
  PathData result = new PathData();
  result.types = types;
  result.points = points;
  return result;
//  int count = Gdip.GraphicsPath_GetPointCount(handle);
//  byte[] gdipTypes = new byte[count];
//  float[] points = new float[count * 2];
//  Gdip.GraphicsPath_GetPathTypes(handle, gdipTypes, count);
//  Gdip.GraphicsPath_GetPathPoints(handle, points, count);
//  byte[] types = new byte[count * 2];
//  int index = 0, typesIndex = 0;
//  while (index < count) {
//    byte type = gdipTypes[index];
//    boolean close = false;
//    switch (type & Gdip.PathPointTypePathTypeMask) {
//      case Gdip.PathPointTypeStart:
//        types[typesIndex++] = SWT.PATH_MOVE_TO;
//        close = (type & Gdip.PathPointTypeCloseSubpath) != 0;
//        index += 1;
//        break;
//      case Gdip.PathPointTypeLine:
//        types[typesIndex++] = SWT.PATH_LINE_TO;
//        close = (type & Gdip.PathPointTypeCloseSubpath) != 0;
//        index += 1;
//        break;
//      case Gdip.PathPointTypeBezier:
//        types[typesIndex++] = SWT.PATH_CUBIC_TO;
//        close = (gdipTypes[index + 2] & Gdip.PathPointTypeCloseSubpath) != 0;
//        index += 3;
//        break;
//      default:
//        index++;
//    }
//    if (close) {
//      types[typesIndex++] = SWT.PATH_CLOSE;
//    }
//  }
//  if (typesIndex != types.length) {
//    byte[] newTypes = new byte[typesIndex];
//    System.arraycopy(types, 0, newTypes, 0, typesIndex);
//    types = newTypes;
//  }
//  PathData result = new PathData();
//  result.types = types;
//  result.points = points;
//  return result;
}

/**
 * Adds to the receiver a line from the current point to
 * the point specified by (x, y).
 *
 * @param x the x coordinate of the end of the line to add
 * @param y the y coordinate of the end of the line to add
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void lineTo(float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	handle.lineTo(x, y);
}

/**
 * Returns <code>true</code> if the Path has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the Path.
 * When a Path has been disposed, it is an error to
 * invoke any other method using the Path.
 *
 * @return <code>true</code> when the Path is disposed, and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == null;
}

/**
 * Sets the current point of the receiver to the point
 * specified by (x, y). Note that this starts a new
 * sub path.
 *
 * @param x the x coordinate of the new end point
 * @param y the y coordinate of the new end point
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void moveTo(float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	handle.moveTo(x, y);
}

/**
 * Adds to the receiver a quadratic curve based on the parameters.
 *
 * @param cx the x coordinate of the control point of the spline
 * @param cy the y coordinate of the control point of the spline
 * @param x the x coordinate of the end point of the spline
 * @param y the y coordinate of the end point of the spline
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public void quadTo(float cx, float cy, float x, float y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	handle.quadTo(cx, cy, x, y);
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString() {
	if (isDisposed()) return "Path {*DISPOSED*}";
	return "Path {" + handle + "}";
}

}
