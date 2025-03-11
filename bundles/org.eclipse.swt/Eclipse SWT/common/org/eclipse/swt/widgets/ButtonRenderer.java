/*******************************************************************************
 * Copyright (c) 2025 Syntevo GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Thomas Singer (Syntevo) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public abstract class ButtonRenderer extends ControlRenderer {

	private final Button button;

	private Image disabledImage;
	private boolean pressed;
	private boolean hover;

	public ButtonRenderer(Button button) {
		super(button);
		this.button = button;
	}

	protected final String getText() {
		return button.getText();
	}

	protected final Image getImage() {
		return button.getImage();
	}

	public void invalidateImage() {
		if (disabledImage != null) {
			disabledImage.dispose();
			disabledImage = null;
		}
	}

	protected final boolean isSelected() {
		return button.getSelection();
	}

	protected final boolean isGrayed() {
		return button.getGrayed();
	}

	protected final boolean isHover() {
		return hover;
	}

	public final void setHover(boolean hover) {
		this.hover = hover;
	}

	protected final boolean isPressed() {
		return pressed;
	}

	public final void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	protected final void drawImage(GC gc, int x, int y) {
		final Image image = getImage();
		if (isEnabled()) {
			gc.drawImage(image, x, y);
		}
		else {
			if (disabledImage == null) {
				disabledImage = new Image(button.getDisplay(), image,
						SWT.IMAGE_DISABLE);
			}
			gc.drawImage(disabledImage, x, y);
		}
	}
}
