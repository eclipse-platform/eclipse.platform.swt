package org.eclipse.swt.custom;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.internal.SWTEventListener;

class StyledTextListener extends TypedListener {
/**
 */
StyledTextListener(SWTEventListener listener) {
	super(listener);
}
/**
 * Process StyledText events by invoking the event's handler.
 */
public void handleEvent(Event e) {
	TextChangedEvent textChangedEvent;
	
	switch (e.type) {
		case StyledText.ExtendedModify:
			ExtendedModifyEvent extendedModifyEvent = new ExtendedModifyEvent((StyledTextEvent) e);
			((ExtendedModifyListener) eventListener).modifyText(extendedModifyEvent);
		break;
		
		case StyledText.LineGetStyle:
			LineStyleEvent lineStyleEvent = new LineStyleEvent((StyledTextEvent) e);
			((LineStyleListener) eventListener).lineGetStyle(lineStyleEvent);
			((StyledTextEvent) e).styles = lineStyleEvent.styles;
		break;
		
		case StyledText.LineGetBackground:
			LineBackgroundEvent lineBgEvent = new LineBackgroundEvent((StyledTextEvent) e);
			((LineBackgroundListener) eventListener).lineGetBackground(lineBgEvent);
			((StyledTextEvent) e).lineBackground = lineBgEvent.lineBackground;
		break;
		
		case StyledText.VerifyKey:
			VerifyEvent verifyEvent = new VerifyEvent(e);
			((VerifyKeyListener) eventListener).verifyKey(verifyEvent);
			e.doit = verifyEvent.doit;
		break;		

		case StyledText.TextChanging:
			TextChangingEvent textChangingEvent = new TextChangingEvent((StyledTextContent) e.data, (StyledTextEvent) e);
			((TextChangeListener) eventListener).textChanging(textChangingEvent);
		break;

		case StyledText.TextChanged:
			textChangedEvent = new TextChangedEvent((StyledTextContent) e.data);
			((TextChangeListener) eventListener).textChanged(textChangedEvent);
		break;

		case StyledText.TextSet:
			textChangedEvent = new TextChangedEvent((StyledTextContent) e.data);
			((TextChangeListener) eventListener).textSet(textChangedEvent);
		break;
	}
}
}


