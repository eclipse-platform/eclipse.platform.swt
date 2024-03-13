/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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

import org.eclipse.swt.events.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;

class StyledTextListener extends TypedListener {
StyledTextListener(SWTEventListener listener) {
	super(listener);
}
/**
 * Process StyledText events by invoking the event's handler.
 *
 * @param e the event to handle
 */
@Override
public void handleEvent(Event e) {

	switch (e.type) {
		case ST.ExtendedModify:
			ExtendedModifyEvent extendedModifyEvent = new ExtendedModifyEvent((StyledTextEvent) e);
			((ExtendedModifyListener) eventListener).modifyText(extendedModifyEvent);
			break;
		case ST.LineGetBackground:
			LineBackgroundEvent lineBgEvent = new LineBackgroundEvent((StyledTextEvent) e);
			((LineBackgroundListener) eventListener).lineGetBackground(lineBgEvent);
			((StyledTextEvent) e).lineBackground = lineBgEvent.lineBackground;
			break;
		case ST.LineGetSegments:
			BidiSegmentEvent segmentEvent = new BidiSegmentEvent((StyledTextEvent) e);
			((BidiSegmentListener) eventListener).lineGetSegments(segmentEvent);
			((StyledTextEvent) e).segments = segmentEvent.segments;
			((StyledTextEvent) e).segmentsChars = segmentEvent.segmentsChars;
			break;
		case ST.LineGetStyle:
			LineStyleEvent lineStyleEvent = new LineStyleEvent((StyledTextEvent) e);
			((LineStyleListener) eventListener).lineGetStyle(lineStyleEvent);
			((StyledTextEvent) e).ranges = lineStyleEvent.ranges;
			((StyledTextEvent) e).styles = lineStyleEvent.styles;
			((StyledTextEvent) e).alignment = lineStyleEvent.alignment;
			((StyledTextEvent) e).indent = lineStyleEvent.indent;
			((StyledTextEvent) e).verticalIndent = lineStyleEvent.verticalIndent;
			((StyledTextEvent) e).wrapIndent = lineStyleEvent.wrapIndent;
			((StyledTextEvent) e).justify = lineStyleEvent.justify;
			((StyledTextEvent) e).bullet = lineStyleEvent.bullet;
			((StyledTextEvent) e).bulletIndex = lineStyleEvent.bulletIndex;
			((StyledTextEvent) e).tabStops = lineStyleEvent.tabStops;
			break;
		case ST.PaintObject:
			PaintObjectEvent paintObjectEvent = new PaintObjectEvent((StyledTextEvent) e);
			((PaintObjectListener) eventListener).paintObject(paintObjectEvent);
			break;
		case ST.VerifyKey:
			VerifyEvent verifyEvent = new VerifyEvent(e);
			((VerifyKeyListener) eventListener).verifyKey(verifyEvent);
			e.doit = verifyEvent.doit;
			break;
		case ST.TextChanged: {
			TextChangedEvent textChangedEvent = new TextChangedEvent((StyledTextContent) e.data);
			((TextChangeListener) eventListener).textChanged(textChangedEvent);
			break;
		}
		case ST.TextChanging:
			TextChangingEvent textChangingEvent = new TextChangingEvent((StyledTextContent) e.data, (StyledTextEvent) e);
			((TextChangeListener) eventListener).textChanging(textChangingEvent);
			break;
		case ST.TextSet: {
			TextChangedEvent textChangedEvent = new TextChangedEvent((StyledTextContent) e.data);
			((TextChangeListener) eventListener).textSet(textChangedEvent);
			break;
		}
		case ST.WordNext: {
			MovementEvent wordBoundaryEvent = new MovementEvent((StyledTextEvent) e);
			((MovementListener) eventListener).getNextOffset(wordBoundaryEvent);
			((StyledTextEvent) e).end = wordBoundaryEvent.newOffset;
			break;
		}
		case ST.WordPrevious: {
			MovementEvent wordBoundaryEvent = new MovementEvent((StyledTextEvent) e);
			((MovementListener) eventListener).getPreviousOffset(wordBoundaryEvent);
			((StyledTextEvent) e).end = wordBoundaryEvent.newOffset;
			break;
		}
		case ST.CaretMoved: {
			CaretEvent caretEvent = new CaretEvent((StyledTextEvent) e);
			((CaretListener) eventListener).caretMoved(caretEvent);
			((StyledTextEvent) e).end = caretEvent.caretOffset;
			break;
		}
	}
}
}


