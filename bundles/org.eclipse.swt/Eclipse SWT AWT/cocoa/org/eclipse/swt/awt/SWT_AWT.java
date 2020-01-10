/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
import org.eclipse.swt.graphics.Rectangle;
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

	/**
	 * Key for running pending AWT invokeLater() events.
	 */
	static final String RUN_AWT_INVOKE_LATER_KEY = "org.eclipse.swt.internal.runAWTInvokeLater"; //$NON-NLS-1$

	static final String JDK17_FRAME = "sun.lwawt.macosx.CViewEmbeddedFrame";

	static boolean loaded, swingInitialized;

	static native final Object initFrame (long handle, String className);
	static native final void validateWithBounds (Frame frame, int x, int y, int w, int h);
	static native final void synthesizeWindowActivation (Frame frame, boolean doActivate);

	static synchronized void loadLibrary () {
		if (loaded) return;
		loaded = true;
		Toolkit.getDefaultToolkit();
		Library.loadLibrary("swt-awt");
	}

	static synchronized void initializeSwing() {
		if (swingInitialized) return;
		swingInitialized = true;
		try {
			/* Initialize the default focus traversal policy */
			Class<?> clazz = Class.forName("javax.swing.UIManager");
			Method method = clazz.getMethod("getDefaults");
			if (method != null) method.invoke(clazz);
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
	final long handle = parent.view.id;
	final String className = embeddedFrameClass != null ? embeddedFrameClass : JDK17_FRAME;
	try {
		if (embeddedFrameClass != null) {
			Class.forName(className);
		}
		loadLibrary();
	} catch (ClassNotFoundException cne) {
			SWT.error (SWT.ERROR_NOT_IMPLEMENTED, cne);
	} catch (Throwable e) {
		SWT.error (SWT.ERROR_UNSPECIFIED , e, " [Error while starting AWT]");
	}

	/* NOTE: Swing must not be initialize in an invokeLater() or it hangs */
	initializeSwing();

	final Frame [] result = new Frame [1];
	final Throwable[] exception = new Throwable[1];
	Runnable runnable = new Runnable () {
		boolean run;
		@Override
		public void run() {
			if (run) return;
			run = true;
				Object obj = initFrame(handle, className);
				if (obj == null || !(obj instanceof Frame)) {
					exception [0] = new Throwable("[Error while creating AWT embedded frame]");
					SWT.error (SWT.ERROR_UNSPECIFIED, exception[0]);
					return;
				}
				result[0] = (Frame) obj;
				result[0].addNotify();
		}
	};
	if (EventQueue.isDispatchThread() || parent.getDisplay().getSyncThread() != null) {
		runnable.run();
	} else {
		/* Force AWT to process the invokeLater() right away */
		EventQueue.invokeLater(runnable);
		Display display = parent.getDisplay();
		while (result[0] == null && exception[0] == null) {
			display.setData(RUN_AWT_INVOKE_LATER_KEY, true);
			Boolean invoked = (Boolean)display.getData(RUN_AWT_INVOKE_LATER_KEY);
			if (invoked != null && !invoked.booleanValue()) {
				runnable.run();
			}
		}
	}
	if (exception[0] != null) {
		SWT.error (SWT.ERROR_NOT_IMPLEMENTED, exception[0]);
	}
	final Frame frame = result[0];

	/* NOTE: addNotify() should not be called in the UI thread or we could hang */
	//frame.addNotify();

	parent.setData(EMBEDDED_FRAME_KEY, frame);

	/* Forward the iconify and deiconify events */
	final Listener shellListener = e -> {
		switch (e.type) {
			case SWT.Deiconify:
				EventQueue.invokeLater(() -> frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_DEICONIFIED)));
				break;
			case SWT.Iconify:
				EventQueue.invokeLater(() -> frame.dispatchEvent (new WindowEvent (frame, WindowEvent.WINDOW_ICONIFIED)));
				break;
		}
	};
	Shell shell = parent.getShell ();
	shell.addListener (SWT.Deiconify, shellListener);
	shell.addListener (SWT.Iconify, shellListener);

	/* When display is disposed the frame is disposed in AWT EventQueue.
	 * Force main event loop to run to let the frame finish dispose.
	 */
	final Display display = parent.getDisplay();
	display.addListener(SWT.Dispose, new Listener() {
		@Override
		public void handleEvent(Event event) {
			while (frame.isDisplayable() && !display.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			//Frame finished dispose, the listener can be removed
			if (!display.isDisposed()) {
				display.removeListener(SWT.Dispose, this);
			}
		}
	});

	/*
	 * Generate the appropriate events to activate and deactivate
	 * the embedded frame. This is needed in order to make keyboard
	 * focus work properly for lightweights.
	 */
	Listener listener = new Listener () {
		@Override
		public void handleEvent (Event e) {
			switch (e.type) {
				case SWT.Dispose:
					Shell shell = parent.getShell ();
					shell.removeListener (SWT.Deiconify, shellListener);
					shell.removeListener (SWT.Iconify, shellListener);
					shell.removeListener (SWT.Activate, this);
					shell.removeListener (SWT.Deactivate, this);
					parent.setVisible(false);
					EventQueue.invokeLater(() -> {
						try {
							frame.dispose ();
						} catch (Throwable e1) {}
					});
					break;
				case SWT.Activate:
					if (!parent.isFocusControl()) return;
				case SWT.FocusIn:
					EventQueue.invokeLater(() -> {
						if (frame.isActive()) return;
						try {
							synthesizeWindowActivation (frame, Boolean.TRUE);
						} catch (Throwable e1) {e1.printStackTrace();}
					});
					break;
				case SWT.Deactivate:
				case SWT.FocusOut:
					EventQueue.invokeLater(() -> {
						if (!frame.isActive()) return;
						try {
							synthesizeWindowActivation (frame, Boolean.FALSE);
						} catch (Throwable e1) {e1.printStackTrace();}
					});
					break;
			}
		}
	};

	parent.addListener (SWT.FocusIn, listener);
	parent.addListener(SWT.FocusOut, listener);
	//To allow cross-app activation/deactivation
	shell.addListener (SWT.Activate, listener);
	shell.addListener (SWT.Deactivate, listener);
	parent.addListener (SWT.Dispose, listener);

	display.asyncExec(() -> {
		if (parent.isDisposed()) return;
		final Rectangle clientArea = parent.getClientArea();
		try {
			validateWithBounds(frame, Integer.valueOf(clientArea.x), Integer.valueOf(clientArea.y), Integer.valueOf(clientArea.width), Integer.valueOf(clientArea.height));
		} catch (Throwable e) {e.printStackTrace();}
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
public static Shell new_Shell(final Display display, final Canvas parent) {
	if (display == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	if (parent == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	// Since Java 7, AWT widgets don't have a backing NSView, making embedding impossible
	SWT.error(SWT.ERROR_NOT_IMPLEMENTED, null, "[Embedding SWT in AWT isn't supported on macOS]");
	return null;
}
}
