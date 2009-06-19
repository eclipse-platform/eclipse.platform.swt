/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;


import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;

class StyledTextDragAndDropEffect extends DragAndDropEffect {
	StyledText text;
	
StyledTextDragAndDropEffect(StyledText control) {
	text = control;
}
void showDropTargetEffect(int effect, int x, int y) {
	if ((effect & DND.FEEDBACK_SELECT) != 0) {
		Point pt = text.getDisplay().map(null, text, x, y);
		int oldOffset = text.getCaretOffset();
		try {
			text.setFocus();
			int offset = text.getOffsetAtLocation(pt);
			if (offset != oldOffset) {
				text.setCaretOffset(offset);
			}
		} catch (IllegalArgumentException ex) {
			int maxOffset = text.getCharCount();
			Point maxLocation = text.getLocationAtOffset(maxOffset);
			int offset = -1;
			if (pt.y >= maxLocation.y) {
				if (pt.x >= maxLocation.x) {
					offset = maxOffset;
				} else {
					offset = text.getOffsetAtLocation(new Point(pt.x, maxLocation.y));
				}
			} else {
				offset = maxOffset;
			}
			if (offset != -1 && offset != oldOffset) {
				text.setCaretOffset(offset);
			}
		}
	}
}
}
