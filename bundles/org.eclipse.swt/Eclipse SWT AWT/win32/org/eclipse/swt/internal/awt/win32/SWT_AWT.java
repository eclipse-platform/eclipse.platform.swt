package org.eclipse.swt.internal.awt.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

/* Win32, SUN AWT */
import sun.awt.DrawingSurface;
import sun.awt.windows.WDrawingSurfaceInfo;
import sun.awt.windows.WEmbeddedFrame;

/* SWT Imports */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.graphics.Rectangle;

/* AWT Imports */
import java.awt.Canvas;
import java.awt.Panel;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.FocusEvent;

public class SWT_AWT {

public static Panel new_Panel (final Composite parent) {
	int handle = parent.handle;
	final WEmbeddedFrame frame = new WEmbeddedFrame (handle);
	Panel panel = new Panel ();
	frame.add (panel);
	parent.addListener (SWT.Activate, new Listener () {
		public void handleEvent (Event e) {
			frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_ACTIVATED));
			frame.dispatchEvent (new FocusEvent (frame, FocusEvent.FOCUS_GAINED));
		}
	});
	parent.addListener (SWT.Deactivate, new Listener () {
		public void handleEvent (Event e) {
			frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_DEACTIVATED));
			frame.dispatchEvent (new FocusEvent (frame, FocusEvent.FOCUS_LOST));
		}
	});
	parent.addListener (SWT.Resize, new Listener () {
		public void handleEvent (Event e) {
			final Rectangle rect = parent.getClientArea ();
			frame.getToolkit ().getSystemEventQueue ().invokeLater(new Runnable () {
				public void run () {
					frame.setSize (rect.width, rect.height);
					frame.validate ();
				}
			});
		}
	});
	parent.addListener (SWT.Dispose, new Listener () {
		public void handleEvent (Event e) {
			frame.dispose ();
		}
	});
	return panel;
}

public static Shell new_Shell (Display display, final Canvas parent) {
	DrawingSurface ds = (DrawingSurface)parent.getPeer();
	WDrawingSurfaceInfo wds = (WDrawingSurfaceInfo)ds.getDrawingSurfaceInfo();
	wds.lock ();
	int handle = (int) wds.getHWnd ();
	wds.unlock ();
	final Shell shell = Shell.win32_new (display, handle);
	final Display newDisplay = shell.getDisplay ();
	parent.addComponentListener(new ComponentAdapter () {
		public void componentResized (ComponentEvent e) {
			newDisplay.syncExec (new Runnable () {
				public void run () {
					Dimension dim = parent.getSize ();
					shell.setSize (dim.width, dim.height);
				}
			});
		}
	});
	shell.setVisible (true);
	return shell;
}
}
