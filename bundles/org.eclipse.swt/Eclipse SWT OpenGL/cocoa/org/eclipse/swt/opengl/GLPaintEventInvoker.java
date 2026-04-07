package org.eclipse.swt.opengl;

import java.util.function.*;

import org.eclipse.swt.widgets.*;

/**
 * @noreference This class is not intended to be referenced by clients.
 */
public abstract class GLPaintEventInvoker {

	public GLPaintEventInvoker(Canvas canvas, GLData data) {
	}

	public void redrawTriggered() {
	}

	public abstract void doPaint(Consumer<Event> paintEventSender);

	public Object paint(Consumer<Event> consumer, long wParam, long lParam) {
		return null;
	}

	public void setCurrent() {

	}

}
