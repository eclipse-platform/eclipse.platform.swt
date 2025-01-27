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

	public static GC createGraphicsContext(Control c, int style) {

		GCHandle igc = null;

		if (SWT.USE_SKIJA) {
			igc = new SkijaGC(c, style);
		} else
			igc = new NativeGC(c, style);

		GC gc = new GC();
		gc.innerGC = igc;

		return gc;

	}

	public static GC createGraphicsContext(Control c) {
		return createGraphicsContext(c, SWT.NONE);
	}

}
