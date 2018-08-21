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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class Bug537025_SendKeyEvent {

	protected static final String SHELL_TITLE = "Keyboard testing shell";
	private Text text;
	private final Display display;
	private final Shell shell;

	public Bug537025_SendKeyEvent() {
		display = Display.getDefault();
		shell = new Shell(display, SWT.CLOSE);
		createShell();

		shell.addListener(SWT.CLOSE, arg0 -> {
			System.out.println("Main Shell handling Close event, about to dipose the main Display");
			display.dispose();
		});

		addClearButton();
		addTestButtons();
		addLeftButton();
		addNoModButton();

		while (!display.isDisposed()) {
			try {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Test finished");
	}

	public void addNoModButton() {
		Button noMod = new Button(shell, SWT.PUSH);
		noMod.setText("Press 'a' with no Shift");
		noMod.setBounds(10, 150, 150, 30);
		noMod.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				testNoMod();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				testNoMod();

			}

		});
	}

	public void addLeftButton() {
		Button left = new Button(shell, SWT.PUSH);
		left.setText("To the Left");
		left.setBounds(10, 120, 150, 30);
		left.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				testTypeLeft();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetDefaultSelected(e);
			}

		});
	}

	public void addClearButton() {
		Button spawn = new Button(shell, SWT.PUSH);
		spawn.setText("Clear text");
		spawn.setBounds(10, 30, 150, 30);
		spawn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				text.setText("");
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				widgetDefaultSelected(arg0);
			}

		});
	}

	public void addTestButtons() {
		Button spawn = new Button(shell, SWT.PUSH);
		spawn.setText("Test type one by one");
		spawn.setBounds(10, 60, 150, 30);
		spawn.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				testTypeOneByOne();
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				widgetDefaultSelected(arg0);
			}
		});

		Button spawn2 = new Button(shell, SWT.PUSH);
		spawn2.setText("Test type with mask");
		spawn2.setBounds(10, 90, 150, 30);
		spawn2.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				testTypeWithMask();
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				widgetDefaultSelected(arg0);
			}
		});
	}

	public void createShell() {
		shell.setText(SHELL_TITLE);
		shell.setLayout(new RowLayout(SWT.VERTICAL));
		shell.setSize(500, 500);
		shell.open();
		shell.setFocus();
		text = new Text(shell, SWT.NONE);
		text.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		shell.layout();
	}

	public void testTypeOneByOne() {
		text.setFocus();
		Event shiftDown = keyDown(SWT.SHIFT);
		Event shiftUp = keyUp(SWT.SHIFT);
		Event a = keyDown('a');
		Event aUp = keyUp('a');

		postEvent(shiftDown);
		postEvent(a);
		postEvent(aUp);
		postEvent(shiftUp);
	}

	public void testTypeLeft() {
		text.setFocus();
		Event leftDown = keyDown(SWT.ARROW_LEFT);
		Event leftUp = keyUp(SWT.ARROW_LEFT);
		postEvent(leftDown);
		postEvent(leftUp);
	}

	public void testNoMod() {
		text.setFocus();
		Event aDown = keyDown('a');
		Event aUp = keyUp('a');
		postEvent(aDown);
		postEvent(aUp);
	}

	public void testTypeWithMask() {
		text.setFocus();
		Event a = keyDown('a');
		a.stateMask = SWT.SHIFT;
		Event aUp = keyUp('a');
		postEvent(a);
		postEvent(aUp);
	}

	public Event keyDown(final int key) {
		return keyEvent(key, SWT.KeyDown, getFocusControl());
	}

	public Event keyUp(final int key) {
		return keyEvent(key, SWT.KeyUp, getFocusControl());
	}

	public void postEvent(Event e) {
		org.eclipse.swt.widgets.Display.getDefault().post(e);
	}

	public Control getFocusControl() {
		return org.eclipse.swt.widgets.Display.getDefault().getFocusControl();
	}

	public Event keyEvent(int key, int type, Widget w) {
		Event e = new Event();
		e.keyCode = key;
		e.character = (char) key;
		e.type = type;
		e.widget = w;
		return e;
	}

	public static void main(String[] args) {
		new Bug537025_SendKeyEvent();
		System.exit(0);
	}

}