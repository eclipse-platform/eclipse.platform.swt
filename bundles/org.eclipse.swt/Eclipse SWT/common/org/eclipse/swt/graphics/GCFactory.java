/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.swt.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class GCFactory {

	public static GC createGraphicsContext(Control control) {
		GCHandle innerGC = null;
		if (SWT.USE_SKIJA) {
			innerGC = new SkijaGC(control);
		} else
			innerGC = new NativeGC(control);

		GC gc = new GC();
		gc.innerGC = innerGC;
		return gc;
	}

	public static GC createGraphicsContext(GC originalGC) {
		if (!SWT.USE_SKIJA) {
			return originalGC;
		}

		if (!(originalGC.innerGC instanceof NativeGC originalNativeGC)) {
			return originalGC;
		}

		GC gc = new GC();
		gc.innerGC = new SkijaGC(originalNativeGC, null);
		return gc;
	}

}
