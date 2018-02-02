/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
//package org.eclipse.swt.tests.gtk.snippets;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Rectangle;
//import org.eclipse.swt.internal.gtk.GTK;
//import org.eclipse.swt.internal.gtk.GtkAllocation;
//import org.eclipse.swt.internal.gtk.OS;
//import org.eclipse.swt.layout.GridData;
//import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Display;
//import org.eclipse.swt.widgets.Menu;
//import org.eclipse.swt.widgets.MenuItem;
//import org.eclipse.swt.widgets.Shell;
//import org.eclipse.swt.widgets.Widget;
//
//
///*
// * Title: Bug 526083 - [GTK3] Menu.getBounds() returns wrong x & y coordinate
// * How to run: launch snippet, right click in the boxes, observe messages printed in the console
// * Bug description: The "getBounds" line has incorrect values
// * Expected results: The "getBounds" line should have the same values as the "correct bounds" line
// * GTK Version(s): GTK3
// */
//public class Bug526083_MenuGetBounds {
//
//	public static void main(String[] args) {
//		final Display display = new Display();
//		Shell shell = new Shell(display);
//		shell.setLayout(new GridLayout(2, false));
//		Composite c1 = new Composite(shell, SWT.BORDER);
//		c1.setLayoutData(new GridData(100, 100));
//		Composite c2 = new Composite(shell, SWT.BORDER);
//		c2.setLayoutData(new GridData(100, 100));
//		final Menu menu = new Menu(shell, SWT.POP_UP);
//		MenuItem item = new MenuItem(menu, SWT.PUSH);
//		item.setText("Popup");
//		menu.addListener(SWT.Show, event -> display.asyncExec(() -> {
//			try {
//				// menu.getBounds call
//				System.err.println("getBounds: " + invoke(menu, "getBounds"));
//				System.err.println("-----------------------------------------");
//				// menu.getBounds implementation
////							long /*int*/ window = gtk_widget_get_window (menu.handle);
//				long /*int*/ window = ((Long)invoke(Widget.class, "gtk_widget_get_window", true, menu, new Object[] { Long.valueOf(menu.handle) })).longValue();
//				int [] origin_x = new int [1], origin_y = new int [1];
//				OS.gdk_window_get_origin (window, origin_x, origin_y);
//				GtkAllocation allocation = new GtkAllocation ();
//				GTK.gtk_widget_get_allocation (menu.handle, allocation);
//				System.err.println(origin_x[0] + " " + origin_y[0] + " " + allocation.x + " " + allocation.y + " " + allocation.width + " " + allocation.height);
//				int x = origin_x [0] + allocation.x;
//				int y = origin_y [0] + allocation.y;
//				int width = allocation.width;
//				int height = allocation.height;
//				Rectangle bounds = new Rectangle (x, y, width, height);
//				System.err.println("allocation: " + allocation.x + " " + allocation.y + " " + allocation.width + " " + allocation.height);
//				System.err.println("bounds: " + bounds);
//				System.err.println("-----------------------------------------");
//				// correct bounds (without allocation)
//				x = origin_x [0];
//				y = origin_y [0];
//				bounds = new Rectangle (x, y, width, height);
//				System.err.println("correct bounds: " + bounds);
//			} catch (Throwable t) {
//				t.printStackTrace();
//			}
//		}));
//		c1.setMenu(menu);
//		c2.setMenu(menu);
//		shell.setMenu(menu);
//		shell.setSize(300, 300);
//		shell.setLocation(100, 100);
//		shell.open();
//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch())
//				display.sleep();
//		}
//		display.dispose();
//	}
//
//	public final static Object invoke(final String clazzName, final String methodName, final Object... args)
//			throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
//		return invoke(clazzName, methodName, false, args);
//	}
//
//	public final static Object invoke(final String clazzName, final String methodName, final Class<?>[] params, final Object... args)
//			throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
//		return invoke(Class.forName(clazzName), methodName, false, null, params, args);
//	}
//
//	public final static Object invoke(final String clazzName, final String methodName, final boolean declaredMethod, final Object... args)
//			throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
//		return invoke(Class.forName(clazzName), methodName, declaredMethod, null, args);
//	}
//
//	public final static Object invoke(final Object obj, final String methodName, final Object... args)
//			throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
//		return invoke(obj.getClass(), methodName, true, obj, args);
//	}
//
//	public final static Object invoke(final Class<?> clazz, final String methodName, final boolean declaredMethod, final Object obj, final Object... args)
//			throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
//		Class<?>[] params = new Class[args.length];
//		for (int i = 0; i < args.length; i++) {
//			Class<?> argClazz = args[i].getClass();
//			try {
//				Field typeField = argClazz.getField("TYPE"); //$NON-NLS-1$
//				params[i] = (Class<?>) typeField.get(null);
//			} catch (NoSuchFieldException e) {
//				params[i] = argClazz;
//			}
//		}
//		return invoke(clazz, methodName, declaredMethod, obj, params, args);
//	}
//
//	public final static Object invoke(final Class<?> clazz, final String methodName, final boolean declaredMethod, final Object obj, final Class<?>[] params, final Object... args)
//			throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
//		Method method;
//		if (declaredMethod) {
//			method = clazz.getDeclaredMethod(methodName, params);
//			method.setAccessible(true);
//		} else {
//			method = clazz.getMethod(methodName, params);
//		}
//		return method.invoke(obj, args);
//	}
//
//}