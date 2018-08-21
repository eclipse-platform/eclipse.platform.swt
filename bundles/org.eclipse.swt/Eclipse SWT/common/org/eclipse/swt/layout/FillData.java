/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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
package org.eclipse.swt.layout;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

class FillData {

	int defaultWidth = -1, defaultHeight = -1;
	int currentWhint, currentHhint, currentWidth = -1, currentHeight = -1;

Point computeSize (Control control, int wHint, int hHint, boolean flushCache) {
	if (flushCache) flushCache();
	if (wHint == SWT.DEFAULT && hHint == SWT.DEFAULT) {
		if (defaultWidth == -1 || defaultHeight == -1) {
			Point size = control.computeSize (wHint, hHint, flushCache);
			defaultWidth = size.x;
			defaultHeight = size.y;
		}
		return new Point(defaultWidth, defaultHeight);
	}
	if (currentWidth == -1 || currentHeight == -1 || wHint != currentWhint || hHint != currentHhint) {
		Point size = control.computeSize (wHint, hHint, flushCache);
		currentWhint = wHint;
		currentHhint = hHint;
		currentWidth = size.x;
		currentHeight = size.y;
	}
	return new Point(currentWidth, currentHeight);
}
void flushCache () {
	defaultWidth = defaultHeight = -1;
	currentWidth = currentHeight = -1;
}
}
