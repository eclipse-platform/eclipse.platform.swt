/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
package org.eclipse.swt.custom;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

class StyledTextEvent extends Event {
	// used by LineStyleEvent
	int[] ranges;
	StyleRange[] styles;
	int alignment;
	int indent;
	int verticalIndent;
	int wrapIndent;
	boolean justify;
	Bullet bullet;
	int bulletIndex;
	int[] tabStops;
	// used by LineBackgroundEvent
	Color lineBackground;
	// used by TextChangedEvent
	int replaceCharCount;
	int newCharCount;
	int replaceLineCount;
	int newLineCount;
	// used by PaintObjectEvent
	int x;
	int y;
	int ascent;
	int descent;
	StyleRange style;

StyledTextEvent (StyledTextContent content) {
	super();
	data = content;
}
}


