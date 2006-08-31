/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * This class is used for storing data relevant to a background.
 */
public class GraphicsBackground {
	
	private Image bgImage;
	private Image thumbNail;
	private Color bgColor1;
	private Color bgColor2;
	
	public GraphicsBackground() {
		bgImage = null;
		thumbNail = null;
		bgColor1 = null;
		bgColor2 = null;
	}

	public Image getBgImage() {
		return bgImage;
	}

	public void setBgImage(Image bgImage) {
		this.bgImage = bgImage;
	}

	public Color getBgColor1() {
		return bgColor1;
	}

	public void setBgColor1(Color bgColor1) {
		this.bgColor1 = bgColor1;
	}

	public Color getBgColor2() {
		return bgColor2;
	}

	public void setBgColor2(Color bgColor2) {
		this.bgColor2 = bgColor2;
	}

	public Image getThumbNail() {
		return thumbNail;
	}

	public void setThumbNail(Image thumbNail) {
		this.thumbNail = thumbNail;
	}
}
