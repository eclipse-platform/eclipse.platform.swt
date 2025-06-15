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
 *     Syntevo GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;

/**
 * Temporary API.
 */
public abstract class CustomControl extends NativeBasedCustomControl {

	protected abstract Point computeDefaultSize();

	protected abstract ControlRenderer getRenderer();

	private int x;
	private int y;
	private int width;
	private int height;
	protected Color background;
	protected Color foreground;

	protected CustomControl(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public Point getSize() {
		return new Point(width, height);
	}

	@Override
	public void setSize(int width, int height) {
		checkWidget();
		if (width == this.width && height == this.height) {
			return;
		}

		this.width = width;
		this.height = height;
		super.setSize(this.width, this.height);
		redraw();
	}

	@Override
	public void setSize(Point size) {
		checkWidget ();
		if (size == null) error (SWT.ERROR_NULL_ARGUMENT);

		setSize(size.x, size.y);
	}

	@Override
	public Point getLocation() {
		checkWidget();
		return new Point(x, y);
	}

	@Override
	public void setLocation(int x, int y) {
		checkWidget();
		if (x == this.x && y == this.y) {
			return;
		}

		this.x = x;
		this.y = y;
		super.setLocation(x, y);
		redraw();
	}

	@Override
	public void setLocation(Point location) {
		if (location == null) error (SWT.ERROR_NULL_ARGUMENT);
		setLocation(location.x, location.y);
	}

	@Override
	public Rectangle getBounds() {
		checkWidget();
		return new Rectangle(x, y, width, height);
	}

	@Override
	public void setBounds(Rectangle rect) {
		if (rect == null) error (SWT.ERROR_NULL_ARGUMENT);
		checkWidget();
		if (rect.x == this.x && rect.y == this.y && rect.width == this.width && rect.height == this.height) {
			return;
		}

		this.x = rect.x;
		this.y = rect.y;
		this.width = rect.width;
		this.height = rect.height;
		super.setBounds(rect);
		redraw();
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		setBounds(new Rectangle(x, y, width, height));
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (enabled == getEnabled()) {
			return;
		}
		super.setEnabled(enabled);
		if (parent.isEnabled()) {
			redraw();
		}
	}

	@Override
	public final Color getBackground() {
		return background != null ? background : getRenderer().getDefaultBackground();
	}

	@Override
	public void setBackground(Color color) {
		if (color != null && color.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		this.background = color;
		super.setBackground(color);
	}

	@Override
	public final Color getForeground() {
		return foreground != null ? foreground : getRenderer().getDefaultForeground();
	}

	@Override
	public void setForeground(Color color) {
		if (color != null && color.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
		this.foreground = color;
		super.setForeground(color);
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();

		Point defaultSize = computeDefaultSize();
		int width = wHint == SWT.DEFAULT ? defaultSize.x : wHint;
		int height = hHint == SWT.DEFAULT ? defaultSize.y : hHint;
		return new Point(width, height);
	}
}
