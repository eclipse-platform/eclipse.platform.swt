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
package org.eclipse.swt.internal.cocoa;

public class NSGlyphGenerator extends NSObject {

public NSGlyphGenerator() {
	super();
}

public NSGlyphGenerator(int id) {
	super(id);
}

public void generateGlyphsForGlyphStorage(id  glyphStorage, int nChars, int glyphIndex, int charIndex) {
	OS.objc_msgSend(this.id, OS.sel_generateGlyphsForGlyphStorage_1desiredNumberOfCharacters_1glyphIndex_1characterIndex_1, glyphStorage != null ? glyphStorage.id : 0, nChars, glyphIndex, charIndex);
}

public static id sharedGlyphGenerator() {
	int result = OS.objc_msgSend(OS.class_NSGlyphGenerator, OS.sel_sharedGlyphGenerator);
	return result != 0 ? new id(result) : null;
}

}
