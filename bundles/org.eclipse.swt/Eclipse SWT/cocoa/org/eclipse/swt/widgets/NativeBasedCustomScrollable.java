/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

abstract class NativeBasedCustomScrollable extends Scrollable {

	protected abstract ControlRenderer getRenderer();

	private Color backgroundColor;
	private Color foregroundColor;

	public NativeBasedCustomScrollable(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	void createHandle() {
		state |= CANVAS;
		boolean scrolled = (style & (SWT.V_SCROLL | SWT.H_SCROLL)) != 0;
		if (!scrolled)
			state |= THEME_BACKGROUND;
		NSRect rect = new NSRect();
		if (scrolled || hasBorder()) {
			NSScrollView scrollWidget = (NSScrollView) new SWTScrollView().alloc();
			scrollWidget.initWithFrame(rect);
			scrollWidget.setDrawsBackground(false);
			if ((style & SWT.H_SCROLL) != 0)
				scrollWidget.setHasHorizontalScroller(true);
			if ((style & SWT.V_SCROLL) != 0)
				scrollWidget.setHasVerticalScroller(true);
			scrollWidget.setBorderType(hasBorder() ? OS.NSBezelBorder : OS.NSNoBorder);
			scrollView = scrollWidget;
		}
		NSView widget = (NSView) new SWTCanvasView().alloc();
		widget.initWithFrame(rect);
//	widget.setFocusRingType(OS.NSFocusRingTypeExterior);
		view = widget;
		if (scrollView != null) {
			NSClipView contentView = scrollView.contentView();
			contentView.setAutoresizesSubviews(true);
			view.setAutoresizingMask(OS.NSViewWidthSizable | OS.NSViewHeightSizable);
		}
	}

	@Override
	public final Color getBackground() {
		return backgroundColor != null ? backgroundColor : getRenderer().getDefaultBackground();
	}

	@Override
	public final void setBackground(Color color) {
		backgroundColor = color;
		super.setBackground(color);
	}

	@Override
	public final Color getForeground() {
		return foregroundColor != null ? foregroundColor : getRenderer().getDefaultForeground();
	}

	@Override
	public final void setForeground(Color color) {
		foregroundColor = color;
		super.setForeground(color);
	}
}
