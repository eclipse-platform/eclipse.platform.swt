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

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 *
 */ 
class StyledTextEvent extends Event {
  // used by LineStyleEvent
  int[] ranges;
  StyleRange[] styles;
  int alignment;
  int indent;
  boolean justify;
  Bullet bullet;
  int bulletIndex;
  // used by LineBackgroundEvent
  Color lineBackground;
  // used by BidiSegmentEvent
  int[] segments; 
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
  GC gc;
  StyleRange style;

StyledTextEvent (StyledTextContent content) {
  super();
  data = content; 
}
}


