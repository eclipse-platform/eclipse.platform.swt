/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class Bug521506_FakeClick {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Snippet"); //$NON-NLS-1$
		shell.setLocation(100, 100);
		shell.setLayout(new GridLayout());

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Test Button"); //$NON-NLS-1$
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.err.println("TEST BUTTON CLICKED"); //$NON-NLS-1$
			}
		});

		// create shell menu bar
		final Menu menuBar = new Menu(shell, SWT.BAR);

		MenuItem menuItem = new MenuItem(menuBar, SWT.CASCADE);
		menuItem.setText("Menu"); //$NON-NLS-1$

		final Menu menu = new Menu(shell, SWT.DROP_DOWN);
		menuItem.setMenu(menu);

		MenuItem clickItem = new MenuItem(menu, SWT.PUSH);
		clickItem.setText("Item"); //$NON-NLS-1$
		clickItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.err.println("ITEM CLICKED"); //$NON-NLS-1$
			}
		});

		shell.setMenuBar(menuBar);

		new Thread(() -> {
			try {
				Thread.sleep(1000);

				// click on button
				display.syncExec(() -> {
					Rectangle bounds = button.getBounds();
					final Event mouseMove = new Event();
					mouseMove.type = SWT.MouseMove;
					mouseMove.x = button.toDisplay(bounds.x + bounds.width / 2, 0).x;
					mouseMove.y = button.toDisplay(0, bounds.y + bounds.height / 2).y;

					display.post(mouseMove);
				});
				Thread.sleep(100);
				display.syncExec(() -> {
					final Event mouseDown = new Event();
					mouseDown.type = SWT.MouseDown;
					mouseDown.button = 1;
					mouseDown.widget = button;
					display.post(mouseDown);
				});
				Thread.sleep(10);
				display.syncExec(() -> {
					final Event mouseUp = new Event();
					mouseUp.type = SWT.MouseUp;
					mouseUp.button = 1;
					mouseUp.widget = button;
					display.post(mouseUp);
				});

				Thread.sleep(1000);

				// open menu
				display.syncExec(() -> {
					Rectangle bounds = getBounds(menuItem);
					final Event mouseMove = new Event();
					mouseMove.type = SWT.MouseMove;
					mouseMove.x = bounds.x + bounds.width / 2;
					mouseMove.y = bounds.y + bounds.height / 2;

					display.post(mouseMove);
				});
				Thread.sleep(100);
				display.syncExec(() -> {
					final Event mouseDown = new Event();
					mouseDown.type = SWT.MouseDown;
					mouseDown.button = 1;
					mouseDown.widget = menuItem;
					display.post(mouseDown);
				});
				Thread.sleep(10);
				display.syncExec(() -> {
					final Event mouseUp = new Event();
					mouseUp.type = SWT.MouseUp;
					mouseUp.button = 1;
					mouseUp.widget = menuItem;
					display.post(mouseUp);
				});
				Thread.sleep(100);
				// click on item
				display.syncExec(() -> {
					Rectangle bounds = getBounds(clickItem);
					final Event mouseMove = new Event();
					mouseMove.type = SWT.MouseMove;
					mouseMove.x = bounds.x + bounds.width / 2;
					mouseMove.y = bounds.y + bounds.height / 2;

					display.post(mouseMove);
				});
				Thread.sleep(200);
				display.syncExec(() -> {
					final Event mouseDown = new Event();
					mouseDown.type = SWT.MouseDown;
					mouseDown.button = 1;
					mouseDown.widget = clickItem;
					display.post(mouseDown);
				});
				Thread.sleep(10);
				display.syncExec(() -> {
					final Event mouseUp = new Event();
					mouseUp.type = SWT.MouseUp;
					mouseUp.button = 1;
					mouseUp.widget = clickItem;
					display.post(mouseUp);
				});

				Thread.sleep(1000);

//				display.syncExec(() -> shell.dispose());
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}).start();

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	final static Rectangle getBounds(final MenuItem menuItem) {
		Rectangle itemRect = getBounds((Object) menuItem);
		Rectangle menuRect = getBounds(menuItem.getParent());
		if ((menuItem.getParent().getStyle() & SWT.RIGHT_TO_LEFT) != 0) {
			itemRect.x = menuRect.x + menuRect.width - itemRect.width - itemRect.x;
		} else {
			itemRect.x += menuRect.x;
		}
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=38436#c143
		itemRect.y += menuRect.y;
		return itemRect;
	}


	private final static Rectangle getBounds(final Object object) {
		try {
			return (Rectangle) invoke(object, "getBounds"); //$NON-NLS-1$
		} catch (Throwable th) {
			th.printStackTrace();
			NoSuchMethodError error = new NoSuchMethodError(th.getMessage());
			error.initCause(th);
			throw error;
		}
	}

	private final static Object invoke(final Object obj, final String methodName, final Object... args) throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		return invoke(obj.getClass(), methodName, true, obj, args);
	}

	private final static Object invoke(final Class<?> clazz, final String methodName, final boolean declaredMethod, final Object obj, final Object... args) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<?>[] params = new Class[args.length];
		for (int i = 0; i < args.length; i++) {
			Class<?> argClazz = args[i].getClass();
			try {
				Field typeField = argClazz.getField("TYPE"); //$NON-NLS-1$
				params[i] = (Class<?>) typeField.get(null);
			} catch (NoSuchFieldException e) {
				params[i] = argClazz;
			}
		}
		return invoke(clazz, methodName, declaredMethod, obj, params, args);
	}

	private final static Object invoke(final Class<?> clazz, final String methodName, final boolean declaredMethod, final Object obj, final Class<?>[] params, final Object... args)
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Method method;
		if (declaredMethod) {
			method = clazz.getDeclaredMethod(methodName, params);
			method.setAccessible(true);
		} else {
			method = clazz.getMethod(methodName, params);
		}
		return method.invoke(obj, args);
	}


}
