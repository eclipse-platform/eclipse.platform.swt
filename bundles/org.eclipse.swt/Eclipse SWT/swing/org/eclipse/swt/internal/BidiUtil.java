/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

import java.awt.Container;

import org.eclipse.swt.graphics.GC;

/*
 * This class is supplied so that the StyledText code that supports bidi text (supported
 * for win platforms) is not platform dependent.  Bidi text is not implemented on 
 * emulated platforms.
 */
public class BidiUtil {
  // Keyboard language types
  public static final int KEYBOARD_NON_BIDI = 0;
  public static final int KEYBOARD_BIDI = 1;

  // bidi rendering input flag constants, not used
  // on emulated platforms
  public static final int CLASSIN = 1;
  public static final int LINKBEFORE = 2;
  public static final int LINKAFTER = 4;

  // bidi rendering/ordering constants, not used on 
  // emulated platforms
  public static final int CLASS_HEBREW = 2;
  public static final int CLASS_ARABIC = 2;
  public static final int CLASS_LOCALNUMBER = 4;
  public static final int CLASS_LATINNUMBER = 5;  
  public static final int REORDER = 0;        
  public static final int LIGATE = 0;
  public static final int GLYPHSHAPE = 0;

/*
 * Not implemented.
 */
public static void addLanguageListener(Container handle, Runnable runnable) {
}
/*
 * Not implemented.
 *
 */
public static void drawGlyphs(GC gc, char[] renderBuffer, int[] renderDx, int x, int y) {
}
/*
 * Bidi not supported on emulated platforms.
 *
 */
public static boolean isBidiPlatform() {
  return false;
}
/*
 * Not implemented.
 */
public static boolean isKeyboardBidi() {
  return false;
}
/*
 * Not implemented.
 */
public static int getFontBidiAttributes(GC gc) {
  return 0; 
}
/*
 *  Not implemented.
 *
 */
public static void getOrderInfo(GC gc, String text, int[] order, byte[] classBuffer, int flags, int [] offsets) {
}
/*
 *  Not implemented. Returns null.
 *
 */
public static char[] getRenderInfo(GC gc, String text, int[] order, byte[] classBuffer, int[] dx, int flags, int[] offsets) {
  return null;
}
/*
 * Not implemented. Returns 0.
 */
public static int getKeyboardLanguage() {
  return 0;
}
/*
 * Not implemented.
 */
public static void removeLanguageListener(Container handle) {
}
/*
 * Not implemented.
 */
public static void setKeyboardLanguage(int language) {
}
/*
 * Not implemented.
 */
public static boolean setOrientation(Container handle, int orientation) {
  return false;
}
}
