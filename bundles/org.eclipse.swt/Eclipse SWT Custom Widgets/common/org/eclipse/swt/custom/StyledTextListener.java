package org.eclipse.swt.custom;
/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp 2000, 2001
 */

/* Imports */
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import java.util.*;

class StyledTextListener extends TypedListener {
/**
 */
StyledTextListener(EventListener listener) {
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


		case StyledText.TextReplaced:
			textChangedEvent = new TextChangedEvent((StyledTextContent)e.data, (StyledTextEvent) e);
			((TextChangedListener) eventListener).textReplaced(textChangedEvent);
		break;

		case StyledText.TextSet:
			textChangedEvent = new TextChangedEvent((StyledTextContent)e.data, (StyledTextEvent) e);
			((TextChangedListener) eventListener).textSet(textChangedEvent);
		break;
	}
}
}


