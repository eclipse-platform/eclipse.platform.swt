/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;

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
    case StyledText.LineGetBackground:
      LineBackgroundEvent lineBgEvent = new LineBackgroundEvent((StyledTextEvent) e);
      ((LineBackgroundListener) eventListener).lineGetBackground(lineBgEvent);
      ((StyledTextEvent) e).lineBackground = lineBgEvent.lineBackground;
      break;    
    case StyledText.LineGetSegments:
      BidiSegmentEvent segmentEvent = new BidiSegmentEvent((StyledTextEvent) e);
      ((BidiSegmentListener) eventListener).lineGetSegments(segmentEvent);
      ((StyledTextEvent) e).segments = segmentEvent.segments;
      break;    
    case StyledText.LineGetStyle:
      LineStyleEvent lineStyleEvent = new LineStyleEvent((StyledTextEvent) e);
      ((LineStyleListener) eventListener).lineGetStyle(lineStyleEvent);
      ((StyledTextEvent) e).ranges = lineStyleEvent.ranges;
      ((StyledTextEvent) e).styles = lineStyleEvent.styles;
      ((StyledTextEvent) e).alignment = lineStyleEvent.alignment;
      ((StyledTextEvent) e).indent = lineStyleEvent.indent;
      ((StyledTextEvent) e).justify = lineStyleEvent.justify;
      ((StyledTextEvent) e).bullet = lineStyleEvent.bullet;
      ((StyledTextEvent) e).bulletIndex = lineStyleEvent.bulletIndex;
      break;
    case StyledText.PaintObject:
      PaintObjectEvent paintObjectEvent = new PaintObjectEvent((StyledTextEvent) e);
      ((PaintObjectListener) eventListener).paintObject(paintObjectEvent);
      break;      
    case StyledText.VerifyKey:
      VerifyEvent verifyEvent = new VerifyEvent(e);
      ((VerifyKeyListener) eventListener).verifyKey(verifyEvent);
      e.doit = verifyEvent.doit;
      break;
    case StyledText.TextChanged:
      textChangedEvent = new TextChangedEvent((StyledTextContent) e.data);
      ((TextChangeListener) eventListener).textChanged(textChangedEvent);
      break;
    case StyledText.TextChanging:
      TextChangingEvent textChangingEvent = new TextChangingEvent((StyledTextContent) e.data, (StyledTextEvent) e);
      ((TextChangeListener) eventListener).textChanging(textChangingEvent);
      break;
    case StyledText.TextSet:
      textChangedEvent = new TextChangedEvent((StyledTextContent) e.data);
      ((TextChangeListener) eventListener).textSet(textChangedEvent);
      break;
  }
}
}


