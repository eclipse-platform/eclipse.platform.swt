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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Headless implementation of Button for SWT.
 */
public class Button extends Control {
	String text = "";
	Image image;
	boolean selected;
	boolean grayed;
	int alignment = SWT.CENTER;

public Button(Composite parent, int style) {
	super(parent, checkStyle(style));
}

static int checkStyle(int style) {
	style = checkBits(style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO, SWT.TOGGLE, 0);
	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
		return checkBits(style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
	}
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		return checkBits(style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
	}
	if ((style & SWT.ARROW) != 0) {
		style |= SWT.NO_FOCUS;
		return checkBits(style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0, 0);
	}
	return style;
}

public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection, typedListener);
	addListener(SWT.DefaultSelection, typedListener);
}

public int getAlignment() {
	checkWidget();
	if ((style & SWT.ARROW) != 0) {
		if ((style & SWT.UP) != 0) return SWT.UP;
		if ((style & SWT.DOWN) != 0) return SWT.DOWN;
		if ((style & SWT.LEFT) != 0) return SWT.LEFT;
		if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
		return SWT.UP;
	}
	return alignment;
}

public boolean getGrayed() {
	checkWidget();
	if ((style & SWT.CHECK) == 0) return false;
	return grayed;
}

public Image getImage() {
	checkWidget();
	return image;
}

public boolean getSelection() {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
	return selected;
}

public String getText() {
	checkWidget();
	return text;
}

public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error(SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection, listener);
}

public void setAlignment(int alignment) {
	checkWidget();
	if ((style & SWT.ARROW) != 0) {
		if ((style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) == 0) return;
		style &= ~(SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		style |= alignment & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		return;
	}
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	this.alignment = alignment;
}

public void setGrayed(boolean grayed) {
	checkWidget();
	if ((style & SWT.CHECK) == 0) return;
	this.grayed = grayed;
}

public void setImage(Image image) {
	checkWidget();
	if ((style & SWT.ARROW) != 0) return;
	this.image = image;
}

public void setSelection(boolean selected) {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return;
	this.selected = selected;
}

public void setText(String string) {
	checkWidget();
	if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.ARROW) != 0) return;
	text = string;
}

@Override
String getNameText() {
	return getText();
}

}
