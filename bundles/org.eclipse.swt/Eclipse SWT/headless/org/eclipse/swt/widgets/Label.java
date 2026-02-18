/*******************************************************************************
 * Copyright (c) 2025 IBM Corporation and others.
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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Headless implementation of Label for SWT.
 */
public class Label extends Control {
	String text = "";
	Image image;
	int alignment = SWT.LEFT;

public Label(Composite parent, int style) {
	super(parent, checkStyle(style));
}

static int checkStyle(int style) {
	style |= SWT.NO_FOCUS;
	if ((style & SWT.SEPARATOR) != 0) {
		style = checkBits(style, SWT.VERTICAL, SWT.HORIZONTAL, 0, 0, 0, 0);
		return checkBits(style, SWT.SHADOW_OUT, SWT.SHADOW_IN, SWT.SHADOW_NONE, 0, 0, 0);
	}
	return checkBits(style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
}

public int getAlignment() {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return 0;
	return alignment;
}

public Image getImage() {
	checkWidget();
	return image;
}

public String getText() {
	checkWidget();
	return text;
}

public void setAlignment(int alignment) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	this.alignment = alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
}

public void setImage(Image image) {
	checkWidget();
	if ((style & SWT.SEPARATOR) != 0) return;
	this.image = image;
	this.text = "";
}

public void setText(String string) {
	checkWidget();
	if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.SEPARATOR) != 0) return;
	text = string;
	this.image = null;
}

@Override
String getNameText() {
	return getText();
}

}
