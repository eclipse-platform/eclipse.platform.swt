package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public abstract class ProgressBarRenderer extends ControlRenderer {

	protected ProgressBarRenderer(Control control) {
		super(control);
	}

	public abstract Point computeDefaultSize();

}
