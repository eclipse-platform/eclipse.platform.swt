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

public class SWT_AWT {

public static Panel new_Panel (final Composite parent) {
	int handle = parent.handle;
	final WEmbeddedFrame frame = new WEmbeddedFrame (handle);
	Panel panel = new Panel ();
	frame.add (panel);
	parent.addListener (SWT.Resize, new Listener () {
		public void handleEvent (Event e) {
			Rectangle rect = parent.getClientArea ();
			frame.setSize (rect.width, rect.height);
			frame.validate ();
		}
	});
	return panel;
}

public static Shell new_Shell (Display display, final Canvas parent) {
	DrawingSurface ds = (DrawingSurface)parent.getPeer();
	WDrawingSurfaceInfo wds = (WDrawingSurfaceInfo)ds.getDrawingSurfaceInfo();
	wds.lock ();
	int handle = wds.getHWnd ();
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
