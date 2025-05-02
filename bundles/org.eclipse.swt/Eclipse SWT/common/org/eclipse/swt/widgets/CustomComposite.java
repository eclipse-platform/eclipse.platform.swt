package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

public abstract class CustomComposite extends Composite {

	protected abstract ControlRenderer getRenderer();

	protected Color background;
	protected Color foreground;

	protected CustomComposite(Composite parent, int style) {
		super(parent, style);
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
}
