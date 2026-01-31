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
 * Headless implementation of Item for SWT.
 */
public abstract class Item extends Widget {
	String text = "";
	Image image;

public Item(Widget parent, int style) {
	super(parent, style);
}

public Image getImage() {
	checkWidget();
	return image;
}

public String getText() {
	checkWidget();
	return text;
}

public void setImage(Image image) {
	checkWidget();
	this.image = image;
}

public void setText(String string) {
	checkWidget();
	if (string == null) error(SWT.ERROR_NULL_ARGUMENT);
	text = string;
}

@Override
String getNameText() {
	return getText();
}

}
