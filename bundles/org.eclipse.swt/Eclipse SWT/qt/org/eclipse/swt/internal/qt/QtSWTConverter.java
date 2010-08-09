/*******************************************************************************
 * Copyright (c) 2009, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.qt;

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QRegion;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;

/**
 * @author J�rgen Becker
 * 
 */
public class QtSWTConverter {

	private QtSWTConverter() {
		// utility class
	}

	public static Rectangle convert(QRect rect) {
		if (rect == null) {
			return null;
		}
		return new Rectangle(rect.x(), rect.y(), rect.width(), rect.height());
	}

	public static QRect convert(Rectangle rect) {
		if (rect == null) {
			return null;
		}
		return new QRect(rect.x, rect.y, rect.width, rect.height);
	}

	public static Point convert(QSize size) {
		if (size == null) {
			return null;
		}
		return new Point(size.width(), size.height());
	}

	public static Point convert(QPoint point) {
		if (point == null) {
			return null;
		}
		return new Point(point.x(), point.y());
	}

	public static Point convertPosition(QRect rect) {
		if (rect == null) {
			return null;
		}
		return new Point(rect.x(), rect.y());
	}

	public static Point convertSize(QRect rect) {
		if (rect == null) {
			return null;
		}
		return new Point(rect.width(), rect.height());
	}

	public static QPoint convert(Point point) {
		if (point == null) {
			return null;
		}
		return new QPoint(point.x, point.y);
	}

	public static QColor convert(Color color) {
		if (color == null) {
			return null;
		}
		return new QColor(color.getRed(), color.getGreen(), color.getBlue());
	}

	public static Color convert(QColor color) {
		if (color == null) {
			return null;
		}
		return new Color(null, color.red(), color.green(), color.blue());
	}

	public static QRegion convert(Region region) {
		if (region == null) {
			return null;
		}
		return new QRegion(convert(region.getBounds()));
	}

	public static Rectangle convert(QRegion region) {
		if (region == null) {
			return null;
		}
		return convert(region.boundingRect());
	}
}
