/*******************************************************************************
 * Copyright (c) 2021, 2022 Joerg Kubitz
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Joerg Kubitz - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class Test_org_eclipse_swt_widgets_Display {

	@Test
	public void test_isXMouseActive() {
		Display display = new Display();
		try {
//			boolean xMouseActive = display.isXMouseActive();
//			System.out.println("org.eclipse.swt.widgets.Display.isXMouseActive(): " + xMouseActive);

			// Calling above method using reflection method call.
			Method method = Class.forName(Display.class.getName()).getDeclaredMethod("isXMouseActive");
			Boolean xMouseActive = null;
			if (method != null && method.canAccess(display)) {
				xMouseActive = (Boolean) method.invoke(display, (Object[]) null);
			}
			System.out.println("org.eclipse.swt.widgets.Display.isXMouseActive(): " + xMouseActive);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			display.dispose();
		}
	}
}
