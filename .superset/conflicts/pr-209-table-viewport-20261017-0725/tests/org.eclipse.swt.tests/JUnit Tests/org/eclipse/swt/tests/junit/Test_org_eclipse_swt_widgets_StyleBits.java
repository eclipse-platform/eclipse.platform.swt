/*******************************************************************************
 * Copyright (c) 2000, 2026 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Random;
import org.eclipse.swt.widgets.Widget;
import org.junit.jupiter.api.Test;

/** Runs against the actual platform Widget entry point; no Display is required. */
public class Test_org_eclipse_swt_widgets_StyleBits {
	private static Method entry () throws Exception {
		Method method = Widget.class.getDeclaredMethod ("checkBits", int.class, int.class, int.class,
				int.class, int.class, int.class, int.class);
		method.setAccessible (true);
		return method;
	}

	// Original GTK/Win32/Cocoa policy retained independently as the behavioral oracle.
	private static int original (int style, int int0, int int1, int int2, int int3, int int4, int int5) {
		int mask = int0 | int1 | int2 | int3 | int4 | int5;
        if ((style & mask) == 0) {
            style |= int0;
        }
        if ((style & int0) != 0) {
            style = (style & ~mask) | int0;
        }
        if ((style & int1) != 0) {
            style = (style & ~mask) | int1;
        }
        if ((style & int2) != 0) {
            style = (style & ~mask) | int2;
        }
        if ((style & int3) != 0) {
            style = (style & ~mask) | int3;
        }
        if ((style & int4) != 0) {
            style = (style & ~mask) | int4;
        }
        if ((style & int5) != 0) {
            style = (style & ~mask) | int5;
        }
		return style;
	}

	@Test
	public void exhaustiveTwoBitMasksAtLowMiddleAndSignPositions () throws Exception {
		Method method = entry ();
		Object [] values = new Object [7];
		for (int shift : new int [] {0, 15, 30}) {
			for (int encoded = 0; encoded < (1 << 14); encoded++) {
                for (int i = 0; i < 7; i++) {
                    values [i] = ((encoded >>> (i * 2)) & 3) << shift;
                }
				assertEquals (original ((int) values [0], (int) values [1], (int) values [2],
						(int) values [3], (int) values [4], (int) values [5], (int) values [6]),
						(int) method.invoke (null, values));
			}
		}
	}

	@Test
	public void randomFullWidthMasksPreserveOriginalAndUnrelatedBits () throws Exception {
		Method method = entry ();
		Random random = new Random (0x5357545354594c45L);
		Object [] values = new Object [7];
		for (int sample = 0; sample < 65536; sample++) {
            for (int i = 0; i < 7; i++) {
                values [i] = random.nextInt();
            }
			int style = (int) values [0];
			int mask = (int) values [1] | (int) values [2] | (int) values [3]
					| (int) values [4] | (int) values [5] | (int) values [6];
			int actual = (int) method.invoke (null, values);
			assertEquals (original (style, (int) values [1], (int) values [2], (int) values [3],
					(int) values [4], (int) values [5], (int) values [6]), actual);
			assertEquals (style & ~mask, actual & ~mask);
		}
	}

	@Test
	public void compatibilityEntryAndSharedOwnerStayPackagePrivate () throws Exception {
		assertEquals (Modifier.STATIC, entry ().getModifiers ());
		Class<?> shared = Class.forName ("org.eclipse.swt.widgets.StyleBits");
		assertFalse (Modifier.isPublic (shared.getModifiers ()));
		assertTrue (Modifier.isFinal (shared.getModifiers ()));
		assertEquals (0, shared.getDeclaredFields ().length);
	}
}
