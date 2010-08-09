/*******************************************************************************
 * Copyright (c) 2009, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.qt;

import com.trolltech.qt.QtPropertyResetter;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QScrollArea;

public class StylableScrollArea extends QScrollArea {
	private QColor gradientStart = new QColor(0, 0, 255);
	private QColor gradientEnd = new QColor(0, 255, 0);
	private QColor text = new QColor(0, 0, 0);
	private QColor border = new QColor(255, 255, 255);

	public QColor getGradientStart() {
		return gradientStart;
	}

	public void setGradientStart(QColor gradientStart) {
		this.gradientStart = gradientStart;
	}

	@QtPropertyResetter(name = "gradientStart")
	public void resetGradientStart() {
		gradientStart = new QColor(0, 0, 255);
	}

	public QColor getGradientEnd() {
		return gradientEnd;
	}

	public void setGradientEnd(QColor gradientEnd) {
		this.gradientEnd = gradientEnd;
	}

	public QColor getText() {
		return text;
	}

	public void setText(QColor text) {
		this.text = text;
	}

	public QColor getBorder() {
		return border;
	}

	public void setBorder(QColor border) {
		this.border = border;
	}

}