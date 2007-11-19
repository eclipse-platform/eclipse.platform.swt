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

public class NSGlyphInfo extends NSObject {

public NSGlyphInfo() {
	super();
}

public NSGlyphInfo(int id) {
	super(id);
}

public int characterCollection() {
	return OS.objc_msgSend(this.id, OS.sel_characterCollection);
}

public int characterIdentifier() {
	return OS.objc_msgSend(this.id, OS.sel_characterIdentifier);
}

public static NSGlyphInfo glyphInfoWithCharacterIdentifier(int cid, int characterCollection, NSString theString) {
	int result = OS.objc_msgSend(OS.class_NSGlyphInfo, OS.sel_glyphInfoWithCharacterIdentifier_1collection_1baseString_1, cid, characterCollection, theString != null ? theString.id : 0);
	return result != 0 ? new NSGlyphInfo(result) : null;
}

public static NSGlyphInfo glyphInfoWithGlyph(int glyph, NSFont font, NSString theString) {
	int result = OS.objc_msgSend(OS.class_NSGlyphInfo, OS.sel_glyphInfoWithGlyph_1forFont_1baseString_1, glyph, font != null ? font.id : 0, theString != null ? theString.id : 0);
	return result != 0 ? new NSGlyphInfo(result) : null;
}

public static NSGlyphInfo glyphInfoWithGlyphName(NSString glyphName, NSFont font, NSString theString) {
	int result = OS.objc_msgSend(OS.class_NSGlyphInfo, OS.sel_glyphInfoWithGlyphName_1forFont_1baseString_1, glyphName != null ? glyphName.id : 0, font != null ? font.id : 0, theString != null ? theString.id : 0);
	return result != 0 ? new NSGlyphInfo(result) : null;
}

public NSString glyphName() {
	int result = OS.objc_msgSend(this.id, OS.sel_glyphName);
	return result != 0 ? new NSString(result) : null;
}

}
