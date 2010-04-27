/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.accessibility;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.accessibility.*;

/**
 * This example shows how to use an AccessibleActionListener
 * to provide information pertaining to actions to an AT.
 */
public class AccessibleActionExample {
	static int MARGIN = 20;
	static ResourceBundle resourceBundle = ResourceBundle.getBundle("examples_accessibility"); //$NON-NLS-1$
	static String getResourceString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!"; //$NON-NLS-1$ //$NON-NLS-2$
		}			
	}
	final static String buttonText = "Action Button";

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setText("Accessible Action Example");
		
		Button button = new Button(shell, SWT.PUSH);
		button.setText("Button");
		
		final Canvas customButton = new Canvas(shell, SWT.NONE) {
			public Point computeSize(int wHint, int hHint, boolean changed) {
				GC gc = new GC(this);
				Point point = gc.stringExtent(buttonText);
				gc.dispose();
				point.x += MARGIN;
				point.y += MARGIN;
				return point;
			}
		};
		customButton.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle clientArea = customButton.getClientArea();
				Point stringExtent = e.gc.stringExtent(buttonText);
				int x = clientArea.x + (clientArea.width - stringExtent.x) / 2;
				int y = clientArea.y + (clientArea.height - stringExtent.y) / 2;
				e.gc.drawString(buttonText, x, y);
			}
		});
		customButton.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				int actionIndex = (e.button == 1) ? 0 : 1;
				customButtonAction(actionIndex);
			}
		});
		customButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int modifierKeys = e.stateMask & SWT.MODIFIER_MASK;
				if (modifierKeys == SWT.CTRL || modifierKeys == 0) {
					if (e.character == '1') customButtonAction(0);
					else if (e.character == '2') customButtonAction(1);
				}
			}
		});

		Accessible accessible = customButton.getAccessible();
		accessible.addAccessibleListener(new AccessibleAdapter() {
			public void getName(AccessibleEvent e) {
				e.result = buttonText;
			}
			public void getKeyboardShortcut(AccessibleEvent e) {
				e.result = "CTRL+1"; // default action is 'action 1'
			}
		});
		accessible.addAccessibleControlListener(new AccessibleControlAdapter() {
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_PUSHBUTTON;
			}
		});
		accessible.addAccessibleActionListener(new AccessibleActionAdapter() {
			public void getActionCount(AccessibleActionEvent e) {
				e.count = 2;
			}
			public void getName(AccessibleActionEvent e) {
				if (0 <= e.index && e.index <= 1) {
					if (e.localized) {
						e.result = AccessibleActionExample.getResourceString("action" + e.index);
					} else {
						e.result = "Action" + e.index; //$NON-NLS-1$
					}
				}
			}
			public void getDescription(AccessibleActionEvent e) {
				if (0 <= e.index && e.index <= 1) {
					e.result = AccessibleActionExample.getResourceString("action" + e.index + "description");
				}
			}
			public void doAction(AccessibleActionEvent e) {
				if (0 <= e.index && e.index <= 1) {
					customButtonAction(e.index);
					e.result = ACC.OK;
				}
			}
			public void getKeyBinding(AccessibleActionEvent e) {
				switch (e.index) { 
					case 0: e.result = "1;CTRL+1"; break;
					case 1: e.result = "2;CTRL+2"; break;
					default: e.result = null;
				}
			}
		});

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
	
	/**
	 * Perform the specified action.
	 * @param action - must be 0 (for action 1) or 1 (for action 2)
	 */
	static void customButtonAction(int action) {
		System.out.println("action " + action + " occurred");
	}
}
