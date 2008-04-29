/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.graphics.*;

public class IME extends Widget {
	Canvas parent;
	int caretOffset;
	int startOffset;
	int commitCount;
	String text;
	int [] ranges;
	TextStyle [] styles;
	
/**
 * Prevents uninitialized instances from being created outside the package.
 */
IME () {
}

/**
 * 
 * @see SWT
 */
public IME (Canvas parent, int style) {
	super (parent, style);
	this.parent = parent;
	createWidget ();
}

void createWidget () {
	text = "";
	startOffset = -1;
	if (parent.getIME () == null) {
		parent.setIME (this);
	}
}

public int getCaretOffset () {
	checkWidget ();
	return startOffset + caretOffset;
}

public int getCommitCount () {
	checkWidget ();
	return commitCount;
}

public int getCompositionOffset () {
	checkWidget ();
	return startOffset;
}

public int [] getRanges () {
	checkWidget ();
	return ranges != null ? ranges : new int [0];
}

public TextStyle [] getStyles () {
	checkWidget ();
	return styles != null ? styles : new TextStyle [0];
}

public String getText () {
	checkWidget ();
	return text;
}

public boolean getWideCaret() {
	return false; 
}

void releaseParent () {
	super.releaseParent ();
	if (this == parent.getIME ()) parent.setIME (null);
}

void releaseWidget () {
	super.releaseWidget ();
	parent = null;
	text = null;
	styles = null;
	ranges = null;
}

public void setCompositionOffset (int offset) {
	checkWidget ();
	if (offset < 0) return;
	if (startOffset != -1) {
		startOffset = offset;
	}
}

}
