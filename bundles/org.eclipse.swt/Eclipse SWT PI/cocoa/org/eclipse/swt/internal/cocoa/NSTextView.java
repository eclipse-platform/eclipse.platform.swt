/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSTextView extends NSText {

public NSTextView() {
	super();
}

public NSTextView(int /*long*/ id) {
	super(id);
}

public NSTextView(id id) {
	super(id);
}

public NSDictionary markedTextAttributes() {
	int result = OS.objc_msgSend(this.id, OS.sel_markedTextAttributes);
	return result != 0 ? new NSDictionary(result) : null;
}

public void setRichText(boolean flag) {
	OS.objc_msgSend(this.id, OS.sel_setRichText_, flag);
}

public NSTextContainer textContainer() {
	int result = OS.objc_msgSend(this.id, OS.sel_textContainer);
	return result != 0 ? new NSTextContainer(result) : null;
}

public NSTextStorage textStorage() {
	int result = OS.objc_msgSend(this.id, OS.sel_textStorage);
	return result != 0 ? new NSTextStorage(result) : null;
}

}
