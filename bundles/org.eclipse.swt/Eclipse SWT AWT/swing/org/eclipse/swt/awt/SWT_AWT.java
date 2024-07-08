/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.awt;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/* SWT Imports */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.internal.Library;
import org.eclipse.swt.internal.swing.CComposite;
import org.eclipse.swt.internal.swing.Utils;

/* AWT Imports */
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Canvas;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.FocusEvent;

/**
 * This class provides a bridge between SWT and AWT, so that it
 * is possible to embed AWT components in SWT and vice versa.
 * 
 * @since 3.0
 */
public class SWT_AWT {

  /**
   * Key for looking up the embedded frame for a Composite using
   * getData(). 
   */
  static String EMBEDDED_FRAME_KEY = "org.eclipse.swt.awt.SWT_AWT.embeddedFrame";

/**
 * Returns a <code>java.awt.Frame</code> which is the embedded frame
 * associated with the specified composite.
 * 
 * @param parent the parent <code>Composite</code> of the <code>java.awt.Frame</code>
 * @return a <code>java.awt.Frame</code> the embedded frame or <code>null</code>.
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * 
 * @since 3.2
 */
public static Frame getFrame (Composite parent) {
  if (parent == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
  if ((parent.getStyle () & SWT.EMBEDDED) == 0) return null;
  return (Frame)parent.getData(EMBEDDED_FRAME_KEY);
}

/**
 * Creates a new <code>java.awt.Frame</code>. This frame is the root for
 * the AWT components that will be embedded within the composite. In order
 * for the embedding to succeed, the composite must have been created
 * with the SWT.EMBEDDED style.
 * <p>
 * IMPORTANT: As of JDK1.5, the embedded frame does not receive mouse events.
 * When a lightweight component is added as a child of the embedded frame,
 * the cursor does not change. In order to work around both these problems, it is
 * strongly recommended that a heavyweight component such as <code>java.awt.Panel</code>
 * be added to the frame as the root of all components.
 * </p>
 * 
 * @param parent the parent <code>Composite</code> of the new <code>java.awt.Frame</code>
 * @return a <code>java.awt.Frame</code> to be the parent of the embedded AWT components
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parent Composite does not have the SWT.EMBEDDED style</li> 
 * </ul>
 * 
 * @since 3.0
 */
public static Frame new_Frame (final Composite parent) {
	if (parent == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if ((parent.getStyle () & SWT.EMBEDDED) == 0) {
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}
	final Container container = ((CComposite)parent.handle).getClientArea();
	final Frame frame = new Frame() {
    public void setLayout(LayoutManager mgr) {
      if(container != null) {
        container.setLayout(mgr);
      }
    }
    protected void addImpl(Component comp, Object constraints, int index) {
      container.add(comp, constraints, index);
    }
  };
  container.setLayout(new BorderLayout(0, 0));
  parent.setData(EMBEDDED_FRAME_KEY, frame);  
//	frame.addNotify();
	
	/*
	* Generate the appropriate events to activate and deactivate
	* the embedded frame. This is needed in order to make keyboard
	* focus work properly for lightweights.
	*/
	Listener listener = new Listener () {
		public void handleEvent (Event e) {
			switch (e.type) {
				case SWT.Dispose:
					parent.setVisible(false);
					EventQueue.invokeLater(new Runnable () {
						public void run () {
							frame.dispose ();
						}
					});
					break;
				case SWT.Activate:
					EventQueue.invokeLater(new Runnable () {
						public void run () {
							if (Library.JAVA_VERSION < Library.JAVA_VERSION(1, 4, 0)) {
								frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_ACTIVATED));
								frame.dispatchEvent (new FocusEvent (frame, FocusEvent.FOCUS_GAINED));
							} else {
								frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_ACTIVATED));
								frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_GAINED_FOCUS));
							}
						}
					});
					break;
				case SWT.Deactivate:
					EventQueue.invokeLater(new Runnable () {
						public void run () {
							if (Library.JAVA_VERSION < Library.JAVA_VERSION(1, 4, 0)) {
								frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_DEACTIVATED));
								frame.dispatchEvent (new FocusEvent (frame, FocusEvent.FOCUS_LOST));
							} else {
								frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_LOST_FOCUS));
								frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_DEACTIVATED));
							}
						}
					});
					break;
			}
		}
	};
	parent.addListener (SWT.Activate, listener);
	parent.addListener (SWT.Deactivate, listener);
	parent.addListener (SWT.Dispose, listener);
	
	parent.getDisplay().asyncExec(new Runnable() {
		public void run () {
			if (parent.isDisposed()) return;
			final Rectangle clientArea = parent.getClientArea();
			EventQueue.invokeLater(new Runnable () {
				public void run () {
					frame.setSize (clientArea.width, clientArea.height);
					frame.validate ();
				}
			});
		}
	});
	return frame;
}

/**
 * Creates a new <code>Shell</code>. This Shell is the root for
 * the SWT widgets that will be embedded within the AWT canvas. 
 * 
 * @param display the display for the new Shell
 * @param parent the parent <code>java.awt.Canvas</code> of the new Shell
 * @return a <code>Shell</code> to be the parent of the embedded SWT widgets
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the display is null</li>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the parent's peer is not created</li>
 * </ul>
 * 
 * @since 3.0
 */
public static Shell new_Shell (final Display display, final Canvas parent) {
	if (display == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (parent == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
  Utils.notImplemented(); return null;
//  int handle = 0;
//	try {
//		loadLibrary ();
//		handle = getAWTHandle (parent);
//	} catch (Throwable e) {
//		SWT.error (SWT.ERROR_NOT_IMPLEMENTED, e);
//	}
//  if (handle == 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT, null, " [peer not created]");
//
//	final Shell shell = Shell.win32_new (display, handle);
//	parent.addComponentListener(new ComponentAdapter () {
//		public void componentResized (ComponentEvent e) {
//			display.syncExec (new Runnable () {
//				public void run () {
//					Dimension dim = parent.getSize ();
//					shell.setSize (dim.width, dim.height);
//				}
//			});
//		}
//	});
//	shell.setVisible (true);
//	return shell;
}
}
