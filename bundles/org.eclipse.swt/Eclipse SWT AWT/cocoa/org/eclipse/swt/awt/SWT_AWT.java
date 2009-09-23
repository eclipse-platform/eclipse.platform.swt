/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Scott Kovatch - interface to apple.awt.CHIViewEmbeddedFrame
 *******************************************************************************/
package org.eclipse.swt.awt;

import java.awt.*;
import java.awt.Canvas;
import java.awt.event.*;
import java.lang.reflect.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

/**
 * This class provides a bridge between SWT and AWT, so that it
 * is possible to embed AWT components in SWT and vice versa.
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#awt">Swing/AWT snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.0
 */
public class SWT_AWT {
	
	/**
	 * The name of the embedded Frame class. The default class name
	 * for the platform will be used if <code>null</code>. 
	 */
	public static String embeddedFrameClass;
	
	/**
	 * Key for looking up the embedded frame for a Composite using
	 * getData(). 
	 */
	static String EMBEDDED_FRAME_KEY = "org.eclipse.swt.awt.SWT_AWT.embeddedFrame";
	
	static {
		System.setProperty("apple.awt.usingSWT", "true");
	}
	
	static boolean loaded, swingInitialized;
	
	static native final int /*long*/ getAWTHandle (Canvas canvas);
	
	static synchronized void loadLibrary () {
		if (loaded) return;
		loaded = true;
		Toolkit.getDefaultToolkit();
		/*
		 * Note that the jawt library is loaded explicitly
		 * because it cannot be found by the library loader.
		 * All exceptions are caught because the library may
		 * have been loaded already.
		 */
		try {
			System.loadLibrary("jawt");
		} catch (Throwable e) {}
		Library.loadLibrary("swt-awt");
	}
	
	static synchronized void initializeSwing() {
		if (swingInitialized) return;
		swingInitialized = true;
		try {
			/* Initialize the default focus traversal policy */
			Class[] emptyClass = new Class[0];
			Object[] emptyObject = new Object[0];
			Class clazz = Class.forName("javax.swing.UIManager");
			Method method = clazz.getMethod("getDefaults", emptyClass);
			if (method != null) method.invoke(clazz, emptyObject);
		} catch (Throwable e) {}
	}
	
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
	public static Frame getFrame(Composite parent) {
		if (parent == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if ((parent.getStyle() & SWT.EMBEDDED) == 0) return null;
		return (Frame) parent.getData(EMBEDDED_FRAME_KEY);
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
	public static Frame new_Frame(final Composite parent) {
		if (parent == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if ((parent.getStyle() & SWT.EMBEDDED) == 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		
		final int /*long*/ handle = parent.view.id;
		
		Class clazz = null;
		try {
			String className = embeddedFrameClass != null ? embeddedFrameClass : "apple.awt.CEmbeddedFrame";
			if (embeddedFrameClass == null) {
				clazz = Class.forName(className, true, ClassLoader.getSystemClassLoader());
			} else {
				clazz = Class.forName(className);
			}
		} catch (ClassNotFoundException cne) {
			SWT.error (SWT.ERROR_NOT_IMPLEMENTED, cne);		
		} catch (Throwable e) {
			SWT.error (SWT.ERROR_UNSPECIFIED , e, " [Error while starting AWT]");		
		}
		
		Object value = null;
		Constructor constructor = null;
		try {
			constructor = clazz.getConstructor (new Class [] {long.class});
			value = constructor.newInstance (new Object [] {new Long(handle)});
		} catch (Throwable e) {
			SWT.error(SWT.ERROR_NOT_IMPLEMENTED, e);
		}
		final Frame frame = (Frame) value;
		parent.setData(EMBEDDED_FRAME_KEY, frame);
		
		Listener listener = new Listener() {
			public void handleEvent(Event e) {
				switch (e.type) {
					case SWT.Dispose: {
						parent.setVisible(false);
						EventQueue.invokeLater(new Runnable () {
							public void run () {
								frame.dispose ();
							}
						});
						break;
					}
				}
			}
		};
		
		parent.addListener(SWT.Dispose, listener);
		
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
	public static Shell new_Shell(final Display display, final Canvas parent) {
		if (display == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		if (parent == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
		int /*long*/ handle = 0;
		
		try {
			loadLibrary ();
			handle = getAWTHandle (parent);
		} catch (Throwable e) {
			SWT.error (SWT.ERROR_NOT_IMPLEMENTED, e);
		}
		if (handle == 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT, null, " [peer not created]");
		
		final Shell shell = Shell.cocoa_new (display, handle);
		final ComponentListener listener = new ComponentAdapter () {
			public void componentResized (ComponentEvent e) {
				display.asyncExec (new Runnable () {
					public void run () {
						if (shell.isDisposed()) return;
						Dimension dim = parent.getSize ();
						shell.setSize (dim.width, dim.height);
					}
				});
			}
		};
		parent.addComponentListener(listener);
		shell.addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event event) {
				parent.removeComponentListener(listener);
			}
		});
		shell.setVisible (true);
		return shell;
	}
}