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

	static final String COLOR_HOVER = "button.background.hover"; //$NON-NLS-1$
	static final String COLOR_TOGGLE = "button.background.toggle"; //$NON-NLS-1$
	static final String COLOR_SELECTION = "button.background.selection"; //$NON-NLS-1$
	static final String COLOR_OUTLINE = "button.outline"; //$NON-NLS-1$
	static final String COLOR_OUTLINE_DISABLED = "button.outline.disabled"; //$NON-NLS-1$
	static final String COLOR_BOX = "button.box"; //$NON-NLS-1$
	static final String COLOR_BOX_DISABLED = "button.box.disabled"; //$NON-NLS-1$
	static final String COLOR_GRAYED = "button.gray"; //$NON-NLS-1$

	protected final Button button;

	public abstract Point computeDefaultSize();

	private Image disabledImage;
	private boolean pressed;
	private boolean hover;

	public ButtonRenderer(Button button) {
		super(button);
		this.button = button;
	}

	public void invalidateImage() {
		if (disabledImage != null) {
			disabledImage.dispose();
			disabledImage = null;
		}
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
		final Image image = button.getImage();
		if (button.isEnabled()) {
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
