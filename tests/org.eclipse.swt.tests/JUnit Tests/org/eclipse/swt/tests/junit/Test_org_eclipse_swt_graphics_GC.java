/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import junit.framework.*;
import junit.textui.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

/**
 * Automated Test Suite for class org.eclipse.swt.graphics.GC
 *
 * @see org.eclipse.swt.graphics.GC
 */
public class Test_org_eclipse_swt_graphics_GC extends SwtTestCase {

public Test_org_eclipse_swt_graphics_GC(String name) {
	super(name);
}

public static void main(String[] args) {
	TestRunner.run(suite());
}

protected void setUp() {
	display = new Display();
	shell = new Shell(display);
	shell.setBounds(0,30,240,290);
	shell.open();
	gc = new GC(shell);
}

protected void tearDown() {
	gc.dispose();
	shell.dispose();
	display.dispose();
}

public void test_ConstructorLorg_eclipse_swt_graphics_Drawable() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_graphics_Drawable not written");
}

public void test_ConstructorLorg_eclipse_swt_graphics_DrawableI() {
	warnUnimpl("Test test_ConstructorLorg_eclipse_swt_graphics_DrawableI not written");
}

public void test_copyAreaIIIIII() {
	warnUnimpl("Test test_copyAreaIIIIII not written");
}

public void test_copyAreaLorg_eclipse_swt_graphics_ImageII() {
	warnUnimpl("Test test_copyAreaLorg_eclipse_swt_graphics_ImageII not written");
}

public void test_dispose() {
	warnUnimpl("Test test_dispose not written");
}

public void test_drawArcIIIIII() {
	warnUnimpl("Test test_drawArcIIIIII not written");
}

public void test_drawFocusIIII() {
	warnUnimpl("Test test_drawFocusIIII not written");
}

public void test_drawImageLorg_eclipse_swt_graphics_ImageII() {
		Color c1 = new Color(display, 255, 0, 0);
		Color c2 = new Color(display, 0, 0, 0);
		Color c3 = new Color(display, 255, 255, 0);
		
		PaletteData paletteData = new PaletteData(new RGB[] {c1.getRGB(), c2.getRGB(), c3.getRGB()});
		ImageData data = new ImageData(30,30, 8, paletteData);
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				if (x > y) data.setPixel(x, y, paletteData.getPixel(c1.getRGB()));
				else if (x < y) data.setPixel(x, y, paletteData.getPixel(c2.getRGB()));
				else data.setPixel(x, y, paletteData.getPixel(c3.getRGB()));
			}
		}
		Image image = new Image(display, data);
		data = image.getImageData();
		data.transparentPixel = paletteData.getPixel(c1.getRGB());
		Image imageTransparent = new Image(display, data);
		data.transparentPixel = -1;
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				data.setAlpha(x, y, 127);
			}
		}		
		Image imageAlpha = new Image(display, data);
								
		gc.drawImage(image, 100, 100);
		gc.drawImage(imageTransparent, 130, 100);
		gc.drawImage(imageAlpha, 160, 100);
		try {
			gc.drawImage(null, 100, 100);
			fail("No exception thrown"); //should never get here
		}
		catch (IllegalArgumentException e) {
		}	
}

public void test_drawImageLorg_eclipse_swt_graphics_ImageIIIIIIII() {
		Color c1 = new Color(display, 255, 0, 0);
		Color c2 = new Color(display, 0, 0, 0);
		Color c3 = new Color(display, 255, 255, 0);
		
		PaletteData paletteData = new PaletteData(new RGB[] {c1.getRGB(), c2.getRGB(), c3.getRGB()});
		ImageData data = new ImageData(30,30, 8, paletteData);
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				if (x > y) data.setPixel(x, y, paletteData.getPixel(c1.getRGB()));
				else if (x < y) data.setPixel(x, y, paletteData.getPixel(c2.getRGB()));
				else data.setPixel(x, y, paletteData.getPixel(c3.getRGB()));
			}
		}
		Image image = new Image(display, data);
		data = image.getImageData();
		data.transparentPixel = paletteData.getPixel(c1.getRGB());
		Image imageTransparent = new Image(display, data);
		data.transparentPixel = -1;
		for (int y = 0; y < data.height; y++) {
			for (int x = 0; x < data.width; x++) {
				data.setAlpha(x, y, 127);
			}
		}		
		Image imageAlpha = new Image(display, data);
								
		gc.drawImage(image, 10, 5, 20, 15, 100, 120, 50, 60);
		gc.drawImage(imageTransparent, 10, 5, 20, 15, 100, 120, 10, 10);
		gc.drawImage(imageAlpha, 10, 5, 20, 15, 100, 120, 20, 15);
		try {
			gc.drawImage(null, 10, 5, 20, 15, 100, 120, 50, 60);
			fail("No exception thrown"); //should never get here
		}
		catch (IllegalArgumentException e) {
		}
}

public void test_drawLineIIII() {
}

public void test_drawOvalIIII() {
}

public void test_drawPolygon$I() {
}

public void test_drawPolyline$I() {
	warnUnimpl("Test test_drawPolyline$I not written");
}

public void test_drawRectangleIIII() {
	warnUnimpl("Test test_drawRectangleIIII not written");
}

public void test_drawRectangleLorg_eclipse_swt_graphics_Rectangle() {
	warnUnimpl("Test test_drawRectangleLorg_eclipse_swt_graphics_Rectangle not written");
}

public void test_drawRoundRectangleIIIIII() {
	warnUnimpl("Test test_drawRoundRectangleIIIIII not written");
}

public void test_drawStringLjava_lang_StringII() {
	warnUnimpl("Test test_drawStringLjava_lang_StringII not written");
}

public void test_drawStringLjava_lang_StringIIZ() {
	warnUnimpl("Test test_drawStringLjava_lang_StringIIZ not written");
}

public void test_drawTextLjava_lang_StringII() {
	warnUnimpl("Test test_drawTextLjava_lang_StringII not written");
}

public void test_drawTextLjava_lang_StringIII() {
	warnUnimpl("Test test_drawTextLjava_lang_StringIII not written");
}

public void test_drawTextLjava_lang_StringIIZ() {
	warnUnimpl("Test test_drawTextLjava_lang_StringIIZ not written");
}

public void test_equalsLjava_lang_Object() {
	warnUnimpl("Test test_equalsLjava_lang_Object not written");
}

public void test_fillArcIIIIII() {
	warnUnimpl("Test test_fillArcIIIIII not written");
}

public void test_fillGradientRectangleIIIIZ() {
	warnUnimpl("Test test_fillGradientRectangleIIIIZ not written");
}

public void test_fillOvalIIII() {
	warnUnimpl("Test test_fillOvalIIII not written");
}

public void test_fillPolygon$I() {
	warnUnimpl("Test test_fillPolygon$I not written");
}

public void test_fillRectangleIIII() {
	warnUnimpl("Test test_fillRectangleIIII not written");
}

public void test_fillRectangleLorg_eclipse_swt_graphics_Rectangle() {
	warnUnimpl("Test test_fillRectangleLorg_eclipse_swt_graphics_Rectangle not written");
}

public void test_fillRoundRectangleIIIIII() {
	warnUnimpl("Test test_fillRoundRectangleIIIIII not written");
}

public void test_getAdvanceWidthC() {
	warnUnimpl("Test test_getAdvanceWidthC not written");
}

public void test_getBackground() {
	warnUnimpl("Test test_getBackground not written");
}

public void test_getCharWidthC() {
	warnUnimpl("Test test_getCharWidthC not written");
}

public void test_getClipping() {
	warnUnimpl("Test test_getClipping not written");
}

public void test_getClippingLorg_eclipse_swt_graphics_Region() {
	warnUnimpl("Test test_getClippingLorg_eclipse_swt_graphics_Region not written");
}

public void test_getFont() {
	warnUnimpl("Test test_getFont not written");
}

public void test_getFontMetrics() {
	warnUnimpl("Test test_getFontMetrics not written");
}

public void test_getForeground() {
	warnUnimpl("Test test_getForeground not written");
}

public void test_getLineStyle() {
	warnUnimpl("Test test_getLineStyle not written");
}

public void test_getLineWidth() {
	warnUnimpl("Test test_getLineWidth not written");
}

public void test_getStyle() {
	warnUnimpl("Test test_getStyle not written");
}

public void test_getXORMode() {
	warnUnimpl("Test test_getXORMode not written");
}

public void test_hashCode() {
	warnUnimpl("Test test_hashCode not written");
}

public void test_isClipped() {
	warnUnimpl("Test test_isClipped not written");
}

public void test_isDisposed() {
	warnUnimpl("Test test_isDisposed not written");
}

public void test_setBackgroundLorg_eclipse_swt_graphics_Color() {
	warnUnimpl("Test test_setBackgroundLorg_eclipse_swt_graphics_Color not written");
}

public void test_setClippingIIII() {
	warnUnimpl("Test test_setClippingIIII not written");
}

public void test_setClippingLorg_eclipse_swt_graphics_Rectangle() {
	warnUnimpl("Test test_setClippingLorg_eclipse_swt_graphics_Rectangle not written");
}

public void test_setClippingLorg_eclipse_swt_graphics_Region() {
	warnUnimpl("Test test_setClippingLorg_eclipse_swt_graphics_Region not written");
}

public void test_setFontLorg_eclipse_swt_graphics_Font() {
	warnUnimpl("Test test_setFontLorg_eclipse_swt_graphics_Font not written");
}

public void test_setForegroundLorg_eclipse_swt_graphics_Color() {
	warnUnimpl("Test test_setForegroundLorg_eclipse_swt_graphics_Color not written");
}

public void test_setLineStyleI() {
	warnUnimpl("Test test_setLineStyleI not written");
}

public void test_setLineWidthI() {
	warnUnimpl("Test test_setLineWidthI not written");
}

public void test_setXORModeZ() {
	warnUnimpl("Test test_setXORModeZ not written");
}

public void test_stringExtentLjava_lang_String() {
	warnUnimpl("Test test_stringExtentLjava_lang_String not written");
}

public void test_textExtentLjava_lang_String() {
	warnUnimpl("Test test_textExtentLjava_lang_String not written");
}

public void test_textExtentLjava_lang_StringI() {
	warnUnimpl("Test test_textExtentLjava_lang_StringI not written");
}

public void test_toString() {
	warnUnimpl("Test test_toString not written");
}

public void test_win32_newILorg_eclipse_swt_graphics_GCData() {
	warnUnimpl("Test test_win32_newILorg_eclipse_swt_graphics_GCData not written");
}

public void test_win32_newLorg_eclipse_swt_graphics_DrawableLorg_eclipse_swt_graphics_GCData() {
	// do not test - Windows only
}

public static Test suite() {
	TestSuite suite = new TestSuite();
	java.util.Vector methodNames = methodNames();
	java.util.Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_GC((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_Drawable");
	methodNames.addElement("test_ConstructorLorg_eclipse_swt_graphics_DrawableI");
	methodNames.addElement("test_copyAreaIIIIII");
	methodNames.addElement("test_copyAreaLorg_eclipse_swt_graphics_ImageII");
	methodNames.addElement("test_dispose");
	methodNames.addElement("test_drawArcIIIIII");
	methodNames.addElement("test_drawFocusIIII");
	methodNames.addElement("test_drawImageLorg_eclipse_swt_graphics_ImageII");
	methodNames.addElement("test_drawImageLorg_eclipse_swt_graphics_ImageIIIIIIII");
	methodNames.addElement("test_drawLineIIII");
	methodNames.addElement("test_drawOvalIIII");
	methodNames.addElement("test_drawPolygon$I");
	methodNames.addElement("test_drawPolyline$I");
	methodNames.addElement("test_drawRectangleIIII");
	methodNames.addElement("test_drawRectangleLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_drawRoundRectangleIIIIII");
	methodNames.addElement("test_drawStringLjava_lang_StringII");
	methodNames.addElement("test_drawStringLjava_lang_StringIIZ");
	methodNames.addElement("test_drawTextLjava_lang_StringII");
	methodNames.addElement("test_drawTextLjava_lang_StringIII");
	methodNames.addElement("test_drawTextLjava_lang_StringIIZ");
	methodNames.addElement("test_equalsLjava_lang_Object");
	methodNames.addElement("test_fillArcIIIIII");
	methodNames.addElement("test_fillGradientRectangleIIIIZ");
	methodNames.addElement("test_fillOvalIIII");
	methodNames.addElement("test_fillPolygon$I");
	methodNames.addElement("test_fillRectangleIIII");
	methodNames.addElement("test_fillRectangleLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_fillRoundRectangleIIIIII");
	methodNames.addElement("test_getAdvanceWidthC");
	methodNames.addElement("test_getBackground");
	methodNames.addElement("test_getCharWidthC");
	methodNames.addElement("test_getClipping");
	methodNames.addElement("test_getClippingLorg_eclipse_swt_graphics_Region");
	methodNames.addElement("test_getFont");
	methodNames.addElement("test_getFontMetrics");
	methodNames.addElement("test_getForeground");
	methodNames.addElement("test_getLineStyle");
	methodNames.addElement("test_getLineWidth");
	methodNames.addElement("test_getStyle");
	methodNames.addElement("test_getXORMode");
	methodNames.addElement("test_hashCode");
	methodNames.addElement("test_isClipped");
	methodNames.addElement("test_isDisposed");
	methodNames.addElement("test_setBackgroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setClippingIIII");
	methodNames.addElement("test_setClippingLorg_eclipse_swt_graphics_Rectangle");
	methodNames.addElement("test_setClippingLorg_eclipse_swt_graphics_Region");
	methodNames.addElement("test_setFontLorg_eclipse_swt_graphics_Font");
	methodNames.addElement("test_setForegroundLorg_eclipse_swt_graphics_Color");
	methodNames.addElement("test_setLineStyleI");
	methodNames.addElement("test_setLineWidthI");
	methodNames.addElement("test_setXORModeZ");
	methodNames.addElement("test_stringExtentLjava_lang_String");
	methodNames.addElement("test_textExtentLjava_lang_String");
	methodNames.addElement("test_textExtentLjava_lang_StringI");
	methodNames.addElement("test_toString");
	methodNames.addElement("test_win32_newILorg_eclipse_swt_graphics_GCData");
	methodNames.addElement("test_win32_newLorg_eclipse_swt_graphics_DrawableLorg_eclipse_swt_graphics_GCData");
	return methodNames;
}
protected void runTest() throws Throwable {
	if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_Drawable")) test_ConstructorLorg_eclipse_swt_graphics_Drawable();
	else if (getName().equals("test_ConstructorLorg_eclipse_swt_graphics_DrawableI")) test_ConstructorLorg_eclipse_swt_graphics_DrawableI();
	else if (getName().equals("test_copyAreaIIIIII")) test_copyAreaIIIIII();
	else if (getName().equals("test_copyAreaLorg_eclipse_swt_graphics_ImageII")) test_copyAreaLorg_eclipse_swt_graphics_ImageII();
	else if (getName().equals("test_dispose")) test_dispose();
	else if (getName().equals("test_drawArcIIIIII")) test_drawArcIIIIII();
	else if (getName().equals("test_drawFocusIIII")) test_drawFocusIIII();
	else if (getName().equals("test_drawImageLorg_eclipse_swt_graphics_ImageII")) test_drawImageLorg_eclipse_swt_graphics_ImageII();
	else if (getName().equals("test_drawImageLorg_eclipse_swt_graphics_ImageIIIIIIII")) test_drawImageLorg_eclipse_swt_graphics_ImageIIIIIIII();
	else if (getName().equals("test_drawLineIIII")) test_drawLineIIII();
	else if (getName().equals("test_drawOvalIIII")) test_drawOvalIIII();
	else if (getName().equals("test_drawPolygon$I")) test_drawPolygon$I();
	else if (getName().equals("test_drawPolyline$I")) test_drawPolyline$I();
	else if (getName().equals("test_drawRectangleIIII")) test_drawRectangleIIII();
	else if (getName().equals("test_drawRectangleLorg_eclipse_swt_graphics_Rectangle")) test_drawRectangleLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_drawRoundRectangleIIIIII")) test_drawRoundRectangleIIIIII();
	else if (getName().equals("test_drawStringLjava_lang_StringII")) test_drawStringLjava_lang_StringII();
	else if (getName().equals("test_drawStringLjava_lang_StringIIZ")) test_drawStringLjava_lang_StringIIZ();
	else if (getName().equals("test_drawTextLjava_lang_StringII")) test_drawTextLjava_lang_StringII();
	else if (getName().equals("test_drawTextLjava_lang_StringIII")) test_drawTextLjava_lang_StringIII();
	else if (getName().equals("test_drawTextLjava_lang_StringIIZ")) test_drawTextLjava_lang_StringIIZ();
	else if (getName().equals("test_equalsLjava_lang_Object")) test_equalsLjava_lang_Object();
	else if (getName().equals("test_fillArcIIIIII")) test_fillArcIIIIII();
	else if (getName().equals("test_fillGradientRectangleIIIIZ")) test_fillGradientRectangleIIIIZ();
	else if (getName().equals("test_fillOvalIIII")) test_fillOvalIIII();
	else if (getName().equals("test_fillPolygon$I")) test_fillPolygon$I();
	else if (getName().equals("test_fillRectangleIIII")) test_fillRectangleIIII();
	else if (getName().equals("test_fillRectangleLorg_eclipse_swt_graphics_Rectangle")) test_fillRectangleLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_fillRoundRectangleIIIIII")) test_fillRoundRectangleIIIIII();
	else if (getName().equals("test_getAdvanceWidthC")) test_getAdvanceWidthC();
	else if (getName().equals("test_getBackground")) test_getBackground();
	else if (getName().equals("test_getCharWidthC")) test_getCharWidthC();
	else if (getName().equals("test_getClipping")) test_getClipping();
	else if (getName().equals("test_getClippingLorg_eclipse_swt_graphics_Region")) test_getClippingLorg_eclipse_swt_graphics_Region();
	else if (getName().equals("test_getFont")) test_getFont();
	else if (getName().equals("test_getFontMetrics")) test_getFontMetrics();
	else if (getName().equals("test_getForeground")) test_getForeground();
	else if (getName().equals("test_getLineStyle")) test_getLineStyle();
	else if (getName().equals("test_getLineWidth")) test_getLineWidth();
	else if (getName().equals("test_getStyle")) test_getStyle();
	else if (getName().equals("test_getXORMode")) test_getXORMode();
	else if (getName().equals("test_hashCode")) test_hashCode();
	else if (getName().equals("test_isClipped")) test_isClipped();
	else if (getName().equals("test_isDisposed")) test_isDisposed();
	else if (getName().equals("test_setBackgroundLorg_eclipse_swt_graphics_Color")) test_setBackgroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setClippingIIII")) test_setClippingIIII();
	else if (getName().equals("test_setClippingLorg_eclipse_swt_graphics_Rectangle")) test_setClippingLorg_eclipse_swt_graphics_Rectangle();
	else if (getName().equals("test_setClippingLorg_eclipse_swt_graphics_Region")) test_setClippingLorg_eclipse_swt_graphics_Region();
	else if (getName().equals("test_setFontLorg_eclipse_swt_graphics_Font")) test_setFontLorg_eclipse_swt_graphics_Font();
	else if (getName().equals("test_setForegroundLorg_eclipse_swt_graphics_Color")) test_setForegroundLorg_eclipse_swt_graphics_Color();
	else if (getName().equals("test_setLineStyleI")) test_setLineStyleI();
	else if (getName().equals("test_setLineWidthI")) test_setLineWidthI();
	else if (getName().equals("test_setXORModeZ")) test_setXORModeZ();
	else if (getName().equals("test_stringExtentLjava_lang_String")) test_stringExtentLjava_lang_String();
	else if (getName().equals("test_textExtentLjava_lang_String")) test_textExtentLjava_lang_String();
	else if (getName().equals("test_textExtentLjava_lang_StringI")) test_textExtentLjava_lang_StringI();
	else if (getName().equals("test_toString")) test_toString();
	else if (getName().equals("test_win32_newILorg_eclipse_swt_graphics_GCData")) test_win32_newILorg_eclipse_swt_graphics_GCData();
	else if (getName().equals("test_win32_newLorg_eclipse_swt_graphics_DrawableLorg_eclipse_swt_graphics_GCData")) test_win32_newLorg_eclipse_swt_graphics_DrawableLorg_eclipse_swt_graphics_GCData();
}

/* custom */
Display display;
Shell shell;
GC gc;
}
