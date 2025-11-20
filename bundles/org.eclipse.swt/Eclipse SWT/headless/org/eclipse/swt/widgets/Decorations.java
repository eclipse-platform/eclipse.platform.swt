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
 * Headless implementation of Decorations for SWT.
 */
public class Decorations extends Canvas {
	String text = "";
	Image image;
	Image[] images = new Image[0];
	Menu menuBar;
	Button defaultButton, saveDefault;

public Decorations() {
	// No-op
}

public Decorations(Display display, int style) {
	super(display, style);
}

public Decorations(Composite parent, int style) {
	super(parent, style);
}

public Button getDefaultButton() {
	checkWidget();
	return defaultButton;
}

public Image getImage() {
	checkWidget();
	return image;
}

public Image[] getImages() {
	checkWidget();
	if (images == null) return new Image[0];
	Image[] result = new Image[images.length];
	System.arraycopy(images, 0, result, 0, images.length);
	return result;
}

public Menu getMenuBar() {
	checkWidget();
	return menuBar;
}

public String getText() {
	checkWidget();
	return text;
}

public void setDefaultButton(Button button) {
	checkWidget();
	if (button != null) {
		if (button.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if (button.getShell() != this) error(SWT.ERROR_INVALID_PARENT);
		if ((button.style & SWT.PUSH) == 0) error(SWT.ERROR_INVALID_ARGUMENT);
	}
	defaultButton = button;
}

public void setImage(Image image) {
	checkWidget();
	this.image = image;
}

public void setImages(Image[] images) {
	checkWidget();
	if (images != null) {
		this.images = new Image[images.length];
		System.arraycopy(images, 0, this.images, 0, images.length);
	} else {
		this.images = new Image[0];
	}
}

public void setMenuBar(Menu menu) {
	checkWidget();
	if (menuBar == menu) return;
	if (menu != null) {
		if ((menu.style & SWT.BAR) == 0) error(SWT.ERROR_MENU_NOT_BAR);
		if (menu.parent != this) error(SWT.ERROR_INVALID_PARENT);
	}
	menuBar = menu;
}

public void setText(String string) {
	checkWidget();
	if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
	text = string;
}

}
