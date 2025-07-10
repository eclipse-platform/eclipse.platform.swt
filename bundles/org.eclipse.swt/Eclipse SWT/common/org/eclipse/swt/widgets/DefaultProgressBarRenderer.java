package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;

public class DefaultProgressBarRenderer extends ProgressBarRenderer {

	protected DefaultProgressBarRenderer(ProgressBar progressBar) {
		super(progressBar);
	}

	@Override
	public void paint(GC gc, int width, int height) {
	}


	@Override
	public Point computeDefaultSize() {

		return new Point(30, 30);

	}

}
