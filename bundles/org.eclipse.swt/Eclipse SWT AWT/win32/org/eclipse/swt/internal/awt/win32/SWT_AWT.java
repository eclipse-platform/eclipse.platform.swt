/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.awt.win32;

 
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

/* Win32, SUN AWT */
import sun.awt.windows.WEmbeddedFrame;
//import sun.awt.DrawingSurface;
//import sun.awt.windows.WDrawingSurfaceInfo;

/* SWT Imports */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.graphics.Rectangle;

/* AWT Imports */
import java.awt.EventQueue;
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
	/*
	 * Some JREs have implemented the WEmbeddedFrame constructor to take an integer
	 * value for the HWND parameter and other JREs take a long for the HWND parameter.
	 * To handle this binary incompatability, we use reflection to perform the equivalent of
	 * the following line of code:
	 * 
	 * final WEmbeddedFrame frame = new WEmbeddedFrame(handle);
	 */
	Constructor constructor = null;
	try {
		constructor = WEmbeddedFrame.class.getConstructor (new Class [] {int.class});
	} catch (Exception e1) {
		try {
			constructor = WEmbeddedFrame.class.getConstructor (new Class [] {long.class});
		} catch (Exception e2) {
			SWT.error (SWT.ERROR_NOT_IMPLEMENTED, e2);
		}
	}
	 WEmbeddedFrame value = null;
	try {
		value = (WEmbeddedFrame) constructor.newInstance (new Object [] {new Integer (handle)});
	} catch (Exception e) {
		SWT.error (SWT.ERROR_NOT_IMPLEMENTED, e);
	}
	final WEmbeddedFrame frame = value;
	
	Panel panel = new Panel ();
	frame.add (panel);
	parent.addListener (SWT.Activate, new Listener () {
		public void handleEvent (Event e) {
			/* Needed to fix focus for lightweights in JDK 1.3.1 */
			EventQueue.invokeLater(new Runnable () {
				public void run () {
					frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_ACTIVATED));
					frame.dispatchEvent (new FocusEvent (frame, FocusEvent.FOCUS_GAINED));
				}
			});
		}
	});
	parent.addListener (SWT.Deactivate, new Listener () {
		public void handleEvent (Event e) {
			/* Needed to fix focus for lightweights in JDK 1.3.1 */
			EventQueue.invokeLater(new Runnable () {
				public void run () {
					frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_DEACTIVATED));
					frame.dispatchEvent (new FocusEvent (frame, FocusEvent.FOCUS_LOST));
				}
			});
		}
	});
	parent.getShell ().addListener (SWT.Move, new Listener () {
		public void handleEvent (Event e) {
			EventQueue.invokeLater(new Runnable () {
				public void run () {
					frame.dispatchEvent (new ComponentEvent (frame, ComponentEvent.COMPONENT_MOVED));
				}
			});
		}
	});
	parent.addListener (SWT.Resize, new Listener () {
		public void handleEvent (Event e) {
			final Rectangle rect = parent.getClientArea ();
			EventQueue.invokeLater(new Runnable () {
				public void run () {
					frame.setSize (rect.width, rect.height);
					frame.validate ();
				}
			});
		}
	});
	parent.addListener (SWT.Dispose, new Listener () {
		public void handleEvent (Event event) {
			parent.setVisible(false);
			frame.dispose ();
		}
	});
	return panel;
}

public static Shell new_Shell (Display display, final Canvas parent) {
	/*
	* As of JDK 1.4, the DrawingSurface and WDrawingSurfaceInfo no longer exist
	* so that code that references these classes no longer compiles.  The fix is
	* to use refection to invoke equivalent code that is commented below.  There
	* is no fix at this time for the missing WDrawingSurfaceInfo functionality.
	*/
//	DrawingSurface ds = (DrawingSurface)parent.getPeer();
//	WDrawingSurfaceInfo wds = (WDrawingSurfaceInfo)ds.getDrawingSurfaceInfo();
//	wds.lock ();
//	int handle = (int) wds.getHWnd ();
//	wds.unlock ();
	Integer hwnd = null;
	try {
		Object ds = parent.getPeer ();
		Class drawingSurfaceClass = Class.forName ("sun.awt.DrawingSurface");  //$NON-NLS-1$
		Method method = drawingSurfaceClass.getDeclaredMethod ("getDrawingSurfaceInfo", null); //$NON-NLS-1$
		Object wds = method.invoke (ds, null);
		Class wDrawingSurfaceClass = Class.forName ("sun.awt.windows.WDrawingSurfaceInfo"); //$NON-NLS-1$
		method = wDrawingSurfaceClass.getMethod ("lock", null); //$NON-NLS-1$
		method.invoke (wds, null);
		method = wDrawingSurfaceClass.getMethod ("getHWnd", null); //$NON-NLS-1$
		hwnd = (Integer) method.invoke (wds, null);
		method = wDrawingSurfaceClass.getMethod ("unlock", null); //$NON-NLS-1$
		method.invoke (wds, null);
	} catch (Exception e) {
		SWT.error (SWT.ERROR_NOT_IMPLEMENTED, e);
	}
	int handle = hwnd.intValue();

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
