package org.eclipse.swt.tests.junit;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import junit.framework.*;
import junit.textui.*;
import java.util.*;


public class Test_org_eclipse_swt_graphics_TextLayout extends SwtTestCase {
	Display display;
	
public Test_org_eclipse_swt_graphics_TextLayout(String name) {
	super(name);
}
public static void main(String[] args) {
	TestRunner.run(suite());
}
protected void setUp() {
	super.setUp();
	display = Display.getDefault();
}

/*Test suite start*/
public void test_getLevel() {
	TextLayout layout = new TextLayout(display);
	String text = "abc55\u05e9\u05e066";
	layout.setText(text);
	assertEquals(0, layout.getLevel(0));
	assertEquals(0, layout.getLevel(2));
//	assertEquals(0, layout.getLevel(4)); //bug in windows (uniscribe)
	assertEquals(1, layout.getLevel(5));
	assertEquals(1, layout.getLevel(6));	
	assertEquals(2, layout.getLevel(7));
	assertEquals(0, layout.getLevel(9));
	try {
		layout.getLevel(-1);
		fail("invalid range expection expected");
	} catch (Exception e) {} 
	try {
		layout.getLevel(text.length() + 1);
		fail("invalid range expection expected");
	} catch (Exception e) {} 
	layout.dispose();
}
public void test_getSegments() {
	TextLayout layout = new TextLayout(display);
	layout.setText("AB");
	String[] messages = {"no segments", "segments", "segments (duplicate at 0)", "segments (duplicate at 1)", "segments (duplicate at 2)"};
	int[][] segments = {null, {0,1,2}, {0,0,1,2}, {0,1,1,2}, {0,1,2,2}};
	for (int i = 0; i < segments.length; i++) {
		String m = messages[i];
		layout.setSegments(segments[i]);
		assertEquals(m, 1, layout.getNextOffset(0, SWT.MOVEMENT_CLUSTER));
		assertEquals(m, 2, layout.getNextOffset(1, SWT.MOVEMENT_CLUSTER));
		assertEquals(m, 2, layout.getNextOffset(2, SWT.MOVEMENT_CLUSTER));
		assertEquals(m, 1, layout.getPreviousOffset(2, SWT.MOVEMENT_CLUSTER));
		assertEquals(m, 0, layout.getPreviousOffset(1, SWT.MOVEMENT_CLUSTER));
		assertEquals(m, 0, layout.getPreviousOffset(0, SWT.MOVEMENT_CLUSTER));
	}
	
	// Bug 241482 comment 74
	layout.setText("word word word");
	layout.setSegments(new int[] {0, 5, 10});
	int offset = 0;
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(5, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(10, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(14, offset);

	layout.setWidth(layout.getBounds().width / 2);
	layout.setAscent(20);
	layout.setDescent(6);
	offset = 0;
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(5, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(10, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(14, offset);
	layout.dispose();
	layout = new TextLayout(display);


	//Bug 241482 comment 64
	layout.setText("\nAB");
	layout.setSegments(new int[] {0, 1, 3});
	int[] expected = new int[] {0, 1, 3};
	int[] offsets = layout.getLineOffsets();
	for (int i = 0; i < offsets.length; i++) {
		assertEquals(" i = " + i, expected[i], offsets[i]);    
	}
	layout.dispose();
	layout = new TextLayout(display);
	
	//Bug 241482 comment  80
	layout.setText("MNMNMN");
	layout.setSegments(new int[] {0,1,6});
	layout.getBounds();
	assertEquals(layout.getText().length() - 1, layout.getOffset(layout.getBounds().width, 0, null));
	layout.setAscent(9);
	assertEquals(layout.getText().length() - 1, layout.getOffset(layout.getBounds().width, 0, null));
	layout.dispose();
	layout = new TextLayout(display);
	
	//Lina's bug (bug 241482, comment 37)
	boolean doit = false; //known to be broken
	if (doit) {
		int length = layout.getText().length();
		layout.setSegments(new int[] { length});
		int[] trailing = new int [1];
		int width = layout.getBounds().width + 20;
		assertEquals("hit test to the left", 0, layout.getOffset(0, 0, trailing));
		assertEquals("hit test to the left (trailing)", 0, trailing[0]);
		assertEquals("hit test to the right", length - 1, layout.getOffset(width, 0, trailing));
		assertEquals("hit test to the right (trailing)", 1, trailing[0]);
		layout.setSegmentsChars(new char[] { '*' });
		assertEquals("hit test to the left", 0, layout.getOffset(0, 0, trailing));
		assertEquals("hit test to the left (trailing)", 0, trailing[0]);
		assertEquals("hit test to the right", length - 1, layout.getOffset(width, 0, trailing));
		assertEquals("hit test to the right (trailing)", 1, trailing[0]);
	}

    /* wrong: internal testing */
//	String text = "AB";
//	int textLength = text.length();
//	layout.setText(text);
//	messages = new String [] { 
//			"no segments", 
//			"segments",
//			"segments (duplicate at 0)", 
//			"segments (duplicate at 1)",
//			"segments (duplicate at 2)" };
//	segments = new int [][] { 
//			null, 
//			{ 0, 1, 2 }, 
//			{ 0, 0, 1, 2 }, 
//			{ 0, 1, 1, 2 },
//			{ 0, 1, 2, 2 } };
//	int[][] translatedOffsets = { 
//			{ 0, 1, 2 }, 
//			{ 1, 3, 5 }, 
//			{ 2, 4, 6 },
//			{ 1, 4, 6 }, 
//			{ 1, 3, 6 } };
//	int[][] untranslatedOffsets = { 
//			{ 0, 1, 2 }, 
//			{ 0, 0, 1, 1, 2, 2 },
//			{ 0, 0, 0, 1, 1, 2, 2 }, 
//			{ 0, 0, 1, 1, 1, 2, 2 },
//			{ 0, 0, 1, 1, 2, 2, 2 } };
//	for (int i = 0; i < segments.length; i++) {
//		layout.setSegments(segments[i]);
//		layout.getBounds();
//		for (int j = 0; j <= textLength; j++) {
//			assertEquals(messages[i] + " j = " + j,	translatedOffsets[i][j], layout.translateOffset(j));
//		}
//		for (int j = 0, n = layout.getSegments() == null ? 0 : textLength + layout.getSegments().length; j < n; j++) {
//			assertEquals(messages[i] + " j = " + j,	untranslatedOffsets[i][j], layout.untranslateOffset(j));
//		}
//	}
	layout.dispose();
}

public void test_getSegmentsChars() {
	TextLayout layout = new TextLayout(display);
	String text = "ab\u05d0\u05d1.\u05d2cd";
	int textLength = text.length();
	layout.setText(text);

	String[] messages = {
			"no segments", 
			"Embedding RTL dir test", 
			"Embedding LTR dir test",
			"LRO test", 
			"RLO test", 
			"Traditional segments", 
			"Traditional segments invalid"};
	int[][] segments = {
			null, 
			{0, 0, 4, 4, 5, 5, 8, 8}, 
			{0, 0, 4, 4, 5, 5, 8, 8},
			{0, textLength}, 
			{0, textLength}, 
			{0, 4, 8}, 
			{1}};
	char[][] chars = {
			null, 
			{'\u202a', '\u202b', '\u202c', '\u200e', '\u200e', '\u202b', '\u202c', '\u202c'},
			{'\u202b', '\u202a', '\u202c', '\u200f', '\u200f', '\u202a', '\u202c', '\u202c'},
			{0x202d, 0x202c}, 
			{0x202e, 0x202c},
			null, 
			null};
	int[][] levels = {
			{0, 0, 1, 1, 1, 1, 0, 0}, 
			{4, 4, 3, 3, 2, 3, 4, 4}, 
			{2, 2, 3, 3, 1, 3, 2, 2},
			{2, 2, 2, 2, 2, 2, 2, 2}, //Fails on cocoa, where it returns {0, 0, 0, 0, 0, 0, 0, 0}
			{1, 1, 1, 1, 1, 1, 1, 1}, 
			{0, 0, 1, 1, 0, 1, 0, 0},
			{0, 0, 1, 1, 1, 1, 0, 0}};
	int[] offsets = {0, textLength};
	for (int i = 0; i < segments.length; i++) {
		layout.setSegments(segments[i]);
		layout.setSegmentsChars(chars[i]);
		assertEquals("Test line offsets" + ": group: " + i, offsets, layout.getLineOffsets());
		for (int j = 0; j < textLength; j++) {
			assertEquals(messages[i] + ": group: " + i + ", index: " + j, levels[i][j], layout.getLevel(j));
		}
	}
	layout.dispose();
}

public void test_getLineOffsets() {
	TextLayout layout = new TextLayout(display);
	String text = "0123456\n890123\n";
	layout.setText(text);
	int[] offsets = layout.getLineOffsets();
	int[] expected = new int [] {0, 8, 15, 15};
	for (int i = 0; i < offsets.length; i++) {
		assertEquals(expected[i], offsets[i]);	
	}
	layout.setText("");
	offsets = layout.getLineOffsets();
	expected = new int [] {0, 0};
	for (int i = 0; i < offsets.length; i++) {
		assertEquals(expected[i], offsets[i]);	
	}
	layout.setText("\n");
	offsets = layout.getLineOffsets();
	expected = new int [] {0, 1, 1};
	for (int i = 0; i < offsets.length; i++) {
		assertEquals(expected[i], offsets[i]);	
	}
	layout.setText("WMWM");
	int width = layout.getBounds().width;
	layout.setWidth(width / 4 + 1);
	offsets = layout.getLineOffsets();
	expected = new int [] {0, 1, 2, 3, 4};
	for (int i = 0; i < offsets.length; i++) {
		assertEquals(expected[i], offsets[i]);	
	}
	layout.setWidth(width / 2 + 1);
	offsets = layout.getLineOffsets();
	expected = new int [] {0, 2, 4};
	for (int i = 0; i < offsets.length; i++) {
		assertEquals(expected[i], offsets[i]);	
	}
    
	layout.dispose();
}
public void test_getLineIndex() {
	TextLayout layout = new TextLayout(display);
	String text = "0123456\n890123\n";
	layout.setText(text);
	assertEquals(0, layout.getLineIndex(0));
	assertEquals(0, layout.getLineIndex(5));
	assertEquals(0, layout.getLineIndex(7));
	assertEquals(1, layout.getLineIndex(8));
	assertEquals(1, layout.getLineIndex(12));
	assertEquals(1, layout.getLineIndex(14));
	assertEquals(2, layout.getLineIndex(15));
	try {
		layout.getLineIndex(-1);
		fail("invalid range expection expected");
	} catch (Exception e) {} 
	try {
		layout.getLineIndex(text.length() + 1);
		fail("invalid range expection expected");
	} catch (Exception e) {} 
	layout.dispose();
}
public void test_getLineBounds() {
	TextLayout layout = new TextLayout(display);
	String text = "0123456\n890123\n";
	layout.setText(text);
	Rectangle rect0  = layout.getLineBounds(0);
	Rectangle rect1  = layout.getLineBounds(1);
	Rectangle rect2  = layout.getLineBounds(2);	
	
	assertTrue(rect0.width > rect1.width);
	assertEquals(0, rect2.width);
	assertEquals(0, rect0.x);
	assertEquals(0, rect1.x);
	assertEquals(0, rect2.x);
	Rectangle bounds = layout.getBounds();
	assertEquals(bounds.width, rect0.width);
	layout.setWidth(bounds.width);
	layout.setAlignment(SWT.CENTER);
	assertEquals(bounds, layout.getBounds());
	layout.setWidth(bounds.width + 100);
	Rectangle newRect1  = layout.getLineBounds(0);
	assertEquals(50, newRect1.x);
	layout.setAlignment(SWT.RIGHT);
	newRect1  = layout.getLineBounds(0);
	assertEquals(100, newRect1.x);
	assertEquals(bounds.height, rect0.height + rect1.height + rect2.height);
	
	try {
		layout.getLineBounds(-1);
		fail("invalid range expection expected");
	} catch (Exception e) {} 
	try {
		layout.getLineBounds(3);
		fail("invalid range expection expected");
	} catch (Exception e) {} 
	layout.dispose();
}
public void test_setStyle() {
	TextLayout layout;
	TextStyle s1, s2, s3, s4;
	s1 = new TextStyle (null, null, null);
	s2 = new TextStyle (null, null, null);
	s3 = new TextStyle (null, null, null);
	s4 = new TextStyle (null, null, null);
	
	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 2, 3);
	assertEquals(null, layout.getStyle(0));
	assertEquals(null, layout.getStyle(1));
	assertEquals(s1, layout.getStyle(2));
	assertEquals(s1, layout.getStyle(3));
	assertEquals(null, layout.getStyle(4));
	assertEquals(null, layout.getStyle(5));	
	layout.dispose();
	
	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 1);
	layout.setStyle (s2, 2, 3);
	layout.setStyle (s3, 4, 5);
	assertEquals(s1, layout.getStyle(0));
	assertEquals(s1, layout.getStyle(1));
	assertEquals(s2, layout.getStyle(2));
	assertEquals(s2, layout.getStyle(3));
	assertEquals(s3, layout.getStyle(4));
	assertEquals(s3, layout.getStyle(5));	
	layout.dispose();	
	
	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 1);
	layout.setStyle (s2, 2, 3);
	layout.setStyle (s3, 4, 5);
	layout.setStyle (s4, 1, 2);
	assertEquals(s1, layout.getStyle(0));
	assertEquals(s4, layout.getStyle(1));
	assertEquals(s4, layout.getStyle(2));
	assertEquals(s2, layout.getStyle(3));
	assertEquals(s3, layout.getStyle(4));
	assertEquals(s3, layout.getStyle(5));	
	layout.dispose();	
	
	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 1);
	layout.setStyle (s2, 2, 3);
	layout.setStyle (s3, 4, 5);
	layout.setStyle (s4, 3, 4);
	assertEquals(s1, layout.getStyle(0));
	assertEquals(s1, layout.getStyle(1));
	assertEquals(s2, layout.getStyle(2));
	assertEquals(s4, layout.getStyle(3));
	assertEquals(s4, layout.getStyle(4));
	assertEquals(s3, layout.getStyle(5));	
	layout.dispose();	
	
	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 1);
	layout.setStyle (s2, 2, 3);
	layout.setStyle (s3, 4, 5);
	layout.setStyle (s4, 1, 4);	
	assertEquals(s1, layout.getStyle(0));
	assertEquals(s4, layout.getStyle(1));
	assertEquals(s4, layout.getStyle(2));
	assertEquals(s4, layout.getStyle(3));
	assertEquals(s4, layout.getStyle(4));
	assertEquals(s3, layout.getStyle(5));	
	layout.dispose();	

	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 0);
	layout.setStyle (s2, 1, 4);
	layout.setStyle (s3, 5, 5);
	layout.setStyle (s4, 2, 3);
	assertEquals(s1, layout.getStyle(0));
	assertEquals(s2, layout.getStyle(1));
	assertEquals(s4, layout.getStyle(2));
	assertEquals(s4, layout.getStyle(3));
	assertEquals(s2, layout.getStyle(4));
	assertEquals(s3, layout.getStyle(5));
	layout.dispose();	
	
	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 1);
	layout.setStyle (s2, 2, 3);
	layout.setStyle (s3, 4, 5);
	layout.setStyle (s4, 0, 3);
	assertEquals(s4, layout.getStyle(0));
	assertEquals(s4, layout.getStyle(1));
	assertEquals(s4, layout.getStyle(2));
	assertEquals(s4, layout.getStyle(3));
	assertEquals(s3, layout.getStyle(4));
	assertEquals(s3, layout.getStyle(5));	
	layout.dispose();	
	
	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 0);
	layout.setStyle (s2, 1, 4);
	layout.setStyle (s3, 5, 5);
	layout.setStyle (s4, 2, 4);
	assertEquals(s1, layout.getStyle(0));
	assertEquals(s2, layout.getStyle(1));
	assertEquals(s4, layout.getStyle(2));
	assertEquals(s4, layout.getStyle(3));
	assertEquals(s4, layout.getStyle(4));
	assertEquals(s3, layout.getStyle(5));
	layout.dispose();	
	
	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 0);
	layout.setStyle (s2, 1, 4);
	layout.setStyle (s3, 5, 5);
	layout.setStyle (s4, 1, 3);
	assertEquals(s1, layout.getStyle(0));
	assertEquals(s4, layout.getStyle(1));
	assertEquals(s4, layout.getStyle(2));
	assertEquals(s4, layout.getStyle(3));
	assertEquals(s2, layout.getStyle(4));
	assertEquals(s3, layout.getStyle(5));
	layout.dispose();	
	
	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 1);
	layout.setStyle (s2, 2, 3);
	layout.setStyle (s3, 4, 5);
	layout.setStyle (null, 0, 5);
	assertEquals(null, layout.getStyle(0));
	assertEquals(null, layout.getStyle(1));
	assertEquals(null, layout.getStyle(2));
	assertEquals(null, layout.getStyle(3));
	assertEquals(null, layout.getStyle(4));
	assertEquals(null, layout.getStyle(5));	
	layout.dispose();		
	
	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 0, 1);
	layout.setStyle (s2, 2, 3);
	layout.setStyle (s3, 4, 5);
	layout.setStyle (s4, 0, 5);
	assertEquals(s4, layout.getStyle(0));
	assertEquals(s4, layout.getStyle(1));
	assertEquals(s4, layout.getStyle(2));
	assertEquals(s4, layout.getStyle(3));
	assertEquals(s4, layout.getStyle(4));
	assertEquals(s4, layout.getStyle(5));	
	layout.dispose();	
	
	layout = new TextLayout (display);
	layout.setText("aabbcc");
	layout.setStyle (s1, 2, 2);
	layout.setStyle (s2, 2, 3);
	layout.setStyle (null, 3, 3);
	assertEquals(null, layout.getStyle(0));
	assertEquals(null, layout.getStyle(1));
	assertEquals(s2, layout.getStyle(2));
	assertEquals(null, layout.getStyle(3));
	assertEquals(null, layout.getStyle(4));
	assertEquals(null, layout.getStyle(5));	
	layout.dispose();		
}
public void test_getAlignment() {
	TextLayout layout = new TextLayout(display);
	layout.setAlignment(SWT.LEFT);
	assertEquals(SWT.LEFT, layout.getAlignment());
	layout.setAlignment(SWT.CENTER);
	assertEquals(SWT.CENTER, layout.getAlignment());
	layout.setAlignment(SWT.RIGHT);
	assertEquals(SWT.RIGHT, layout.getAlignment());
	
	layout.setAlignment(SWT.PUSH);
	assertEquals(SWT.RIGHT, layout.getAlignment());

	layout.setAlignment(SWT.CENTER| SWT.PUSH);
	assertEquals(SWT.CENTER, layout.getAlignment());
	
	layout.setAlignment(SWT.CENTER| SWT.LEFT | SWT.RIGHT);
	assertEquals(SWT.LEFT, layout.getAlignment());

	layout.dispose();
}
public void test_getOrientation() {
	TextLayout layout = new TextLayout(display);
	layout.setOrientation(SWT.LEFT_TO_RIGHT);
	assertEquals(SWT.LEFT_TO_RIGHT, layout.getOrientation());
	layout.setOrientation(SWT.RIGHT_TO_LEFT);
	assertEquals(SWT.RIGHT_TO_LEFT, layout.getOrientation());
	
	layout.setOrientation(SWT.PUSH);
	assertEquals(SWT.RIGHT_TO_LEFT, layout.getOrientation());

	layout.setOrientation(SWT.LEFT_TO_RIGHT | SWT.PUSH);
	assertEquals(SWT.LEFT_TO_RIGHT, layout.getOrientation());
	
	layout.setOrientation(SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
	assertEquals(SWT.LEFT_TO_RIGHT, layout.getOrientation());

	layout.dispose();
}
public void test_getText() {
	TextLayout layout = new TextLayout(display);
	String text = "Test";
	layout.setText(text);
	assertEquals(text, layout.getText());
	layout.dispose();
}
public void test_getLocation() {
	TextLayout layout = new TextLayout(display);
	String text = "AB\u05E9\u05E0";
	layout.setText(text);
	assertEquals(0, layout.getLocation(0, false).x);
	assertEquals(layout.getLocation(0, true).x, layout.getLocation(1, false).x);
	assertEquals(layout.getLocation(2, false).x, layout.getLineBounds(0).width);
	assertEquals(layout.getLocation(2, true).x, layout.getLocation(3, false).x);
	assertEquals(layout.getLocation(3, true).x, layout.getLocation(1, true).x);	

	assertEquals(layout.getLocation(4, false).x, layout.getLineBounds(0).width);
	assertEquals(layout.getLocation(4, true).x, layout.getLineBounds(0).width);
	layout.dispose();
}
public void test_getNextOffset() {
	TextLayout layout = new TextLayout(display);
	String text;
	int offset;
	
	text = "word word word";
	layout.setText(text);
	offset = 0;
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_END);
	assertEquals(3, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_END);
	assertEquals(8, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_END);
	assertEquals(13, offset);
	
	text = "AB \u05E9\u05E0 CD\nHello";
	layout.setText(text);
	offset = 0;
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(3, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(6, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(8, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(6, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(3, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_WORD_START);
	assertEquals(0, offset);
	offset = 7;
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);	
	assertEquals(8, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);	
	assertEquals(9, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_WORD_START);	
	assertEquals(text.length(), offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_WORD_START);	
	assertEquals(9, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_WORD_START);	
	assertEquals(8, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_CLUSTER);	
	assertEquals(9, offset);
	offset = layout.getNextOffset(offset, SWT.MOVEMENT_CLUSTER);	
	assertEquals(10, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_CLUSTER);	
	assertEquals(9, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_CLUSTER);	
	assertEquals(8, offset);
	offset = layout.getPreviousOffset(offset, SWT.MOVEMENT_CLUSTER);	
	assertEquals(7, offset);
	for (int i = 0; i < text.length(); i++) {			
		assertEquals(i+1, layout.getNextOffset(i, SWT.MOVEMENT_CLUSTER));
	}
	for (int i = text.length(); i > 0; i--) {			
		assertEquals(i-1, layout.getPreviousOffset(i, SWT.MOVEMENT_CLUSTER));
	}
	layout.dispose();
}

public void test_getNextOffset2() {
	//Text thai cluster separate so it can be excluded 
	//for gtk, testing machine (rhel4) is too old to 
	//support thai.
	
    TextLayout layout = new TextLayout(display);
    layout.setText("A\u0E19\u0E49\u0E33B");
    String[] messages = {"no segments", "segments", "segments (duplicate at 0)", "segments (duplicate at 1)", "segments (duplicate at 2)",        
    					"segments (duplicate at 3)", "segments (duplicate at 4)", "segments (duplicate at 5)"};
//    int[][] segments = {null, {0, 1, 2, 3, 4, 5}, {0, 0, 1, 2, 3, 4, 5}, {0, 1, 1, 2, 3, 4, 5}, {0, 1, 2, 2, 3, 4, 5}, {0, 1, 2, 3, 3, 4, 5},
//            			{0, 1, 2, 3, 4, 4, 5}, {0, 1, 2, 3, 4, 5, 5}};
    
    int[][] segments = {null};

    for (int i = 0; i < segments.length; i++) {
        String m = messages[i];
        layout.setSegments(segments[i]);
        assertEquals(m, 1, layout.getNextOffset(0, SWT.MOVEMENT_CLUSTER));
        assertEquals(m, 4, layout.getNextOffset(1, SWT.MOVEMENT_CLUSTER));
        assertEquals(m, 5, layout.getNextOffset(4, SWT.MOVEMENT_CLUSTER));
        assertEquals(m, 4, layout.getPreviousOffset(5, SWT.MOVEMENT_CLUSTER));
        assertEquals(m, 1, layout.getPreviousOffset(4, SWT.MOVEMENT_CLUSTER));
        assertEquals(m, 0, layout.getPreviousOffset(1, SWT.MOVEMENT_CLUSTER));
    }
    layout.dispose();
}
public void test_getLineSpacing() {
	TextLayout layout = new TextLayout(display);
	layout.setSpacing(10);
	assertEquals(10, layout.getSpacing());
	layout.setSpacing(Short.MAX_VALUE);
	assertEquals(Short.MAX_VALUE, layout.getSpacing());
	layout.setSpacing(50);
	assertEquals(50, layout.getSpacing());
	try {
		layout.setSpacing(-1);
		fail("invalid range expection expected");
	} catch (Exception e) {} 	
	assertEquals(50, layout.getSpacing());	
	layout.setSpacing(0);
	assertEquals(0, layout.getSpacing());
	layout.dispose();
}
public void test_getOffset() {
	TextLayout layout = new TextLayout(display);
	String text = "AB \u05E9\u05E0 CD\nHello";
	layout.setText(text);	
	int[] trailing = new int[1];
	
	assertEquals(0, layout.getOffset(0, 0, trailing));
	assertEquals(0, trailing[0]);
	Point point = layout.getLocation(0, true);
	assertEquals(1, layout.getOffset(point.x + 1, 0, trailing));	
	assertEquals(0, trailing[0]);	
	point = layout.getLocation(1, false);
	assertEquals(0, layout.getOffset(point.x - 1, 0, trailing));
	assertEquals(1, trailing[0]);
	point = layout.getLocation(2, true);
	assertEquals(4, layout.getOffset(point.x + 1, 0, trailing));	
	assertEquals(1, trailing[0]);
	point = layout.getLocation(4, true);
	assertEquals(2, layout.getOffset(point.x - 1, 0, trailing));	
	assertEquals(1, trailing[0]);	
	point = layout.getLocation(4, false);
	assertEquals(3, layout.getOffset(point.x + 1, 0, trailing));	
	assertEquals(1, trailing[0]);
	
	Rectangle bounds = layout.getBounds();
	layout.setWidth(bounds.width + 100);
	layout.setAlignment(SWT.CENTER);

	assertEquals(0, layout.getOffset(0, 0, trailing));
	assertEquals(0, trailing[0]);
	point = layout.getLocation(0, true);
	assertEquals(1, layout.getOffset(point.x + 1, 0, trailing));	
	assertEquals(0, trailing[0]);	
	point = layout.getLocation(1, false);
	assertEquals(0, layout.getOffset(point.x - 1, 0, trailing));
	assertEquals(1, trailing[0]);
	point = layout.getLocation(2, true);
	assertEquals(4, layout.getOffset(point.x + 1, 0, trailing));	
	assertEquals(1, trailing[0]);
	point = layout.getLocation(4, true);
	assertEquals(2, layout.getOffset(point.x - 1, 0, trailing));	
	assertEquals(1, trailing[0]);	
	point = layout.getLocation(4, false);
	assertEquals(3, layout.getOffset(point.x + 1, 0, trailing));	
	assertEquals(1, trailing[0]);

	layout.setAlignment(SWT.RIGHT);

	assertEquals(0, layout.getOffset(0, 0, trailing));
	assertEquals(0, trailing[0]);
	point = layout.getLocation(0, true);
	assertEquals(1, layout.getOffset(point.x + 1, 0, trailing));	
	assertEquals(0, trailing[0]);	
	point = layout.getLocation(1, false);
	assertEquals(0, layout.getOffset(point.x - 1, 0, trailing));
	assertEquals(1, trailing[0]);
	point = layout.getLocation(2, true);
	assertEquals(4, layout.getOffset(point.x + 1, 0, trailing));	
	assertEquals(1, trailing[0]);
	point = layout.getLocation(4, true);
	assertEquals(2, layout.getOffset(point.x - 1, 0, trailing));	
	assertEquals(1, trailing[0]);	
	point = layout.getLocation(4, false);
	assertEquals(3, layout.getOffset(point.x + 1, 0, trailing));	
	assertEquals(1, trailing[0]);
	
	text = "Text";
	layout.setText(text);
	int width = layout.getBounds().width;
	layout.setAlignment(SWT.LEFT);
	assertEquals(0, layout.getOffset(1, 0, null));
	assertEquals(text.length()-1, layout.getOffset(width-1, 0, null));
	layout.setWidth(width + 100 );
	layout.setAlignment(SWT.CENTER);
	assertEquals(0, layout.getOffset(1+50, 0, null));
	assertEquals(text.length()-1, layout.getOffset(width-1+50, 0, null));
	layout.setAlignment(SWT.RIGHT);
	assertEquals(0, layout.getOffset(1+100, 0, null));
	assertEquals(text.length()-1, layout.getOffset(width-1+100, 0, null));
	
	layout.dispose();
}
public void test_getTabs() {
	TextLayout layout = new TextLayout(display);
	assertEquals(null, layout.getTabs());
	int[] tabs = new int[] {8, 18, 28, 38, 50, 80};
	layout.setTabs(tabs);
	int[] result = layout.getTabs();
	assertEquals(tabs.length, result.length);
	for (int i = 0; i < result.length; i++) {		
		assertEquals("pos: " + i, tabs[i], result[i]);
	}
	layout.setTabs(null);
	assertEquals(null, layout.getTabs());
	layout.dispose();
}
/*Test suite end*/
public static Test suite() {
	TestSuite suite = new TestSuite();
	Vector methodNames = methodNames();
	Enumeration e = methodNames.elements();
	while (e.hasMoreElements()) {
		suite.addTest(new Test_org_eclipse_swt_graphics_TextLayout((String)e.nextElement()));
	}
	return suite;
}
public static java.util.Vector methodNames() {
	java.util.Vector methodNames = new java.util.Vector();
	methodNames.addElement("test_getLevel");
	methodNames.addElement("test_getLineOffsets");
	methodNames.addElement("test_getLineIndex");
	methodNames.addElement("test_getLineBounds");
	methodNames.addElement("test_getAlignment");
	methodNames.addElement("test_getOrientation");
	methodNames.addElement("test_getText");
	methodNames.addElement("test_getLineSpacing");
	methodNames.addElement("test_getLocation");
	methodNames.addElement("test_getNextOffset");
	methodNames.addElement("test_getNextOffset2");
	methodNames.addElement("test_getOffset");
	methodNames.addElement("test_getTabs");
	methodNames.addElement("test_getSegments");
	methodNames.addElement("test_getSegmentsChars");
	methodNames.addElement("test_setStyle");
	return methodNames;
}
protected void runTest() throws Throwable {
	String name = getName();
	if (name.equals("test_getLevel")) test_getLevel();
	else if (name.equals("test_getLineOffsets")) test_getLineOffsets();	
	else if (name.equals("test_getLineIndex")) test_getLineIndex();
	else if (name.equals("test_getLineBounds")) test_getLineBounds();
	else if (name.equals("test_getAlignment")) test_getAlignment();
	else if (name.equals("test_getOrientation")) test_getOrientation();
	else if (name.equals("test_getText")) test_getText();
	else if (name.equals("test_getLineSpacing")) test_getLineSpacing();
	else if (name.equals("test_getLocation")) test_getLocation();
	else if (name.equals("test_getNextOffset")) test_getNextOffset();
	else if (name.equals("test_getNextOffset2")) test_getNextOffset2();
	else if (name.equals("test_getOffset")) test_getOffset();	
	else if (name.equals("test_getTabs")) test_getTabs();	
	else if (name.equals("test_getSegments")) test_getSegments();
	else if (name.equals("test_getSegmentsChars")) test_getSegmentsChars();	
	else if (name.equals("test_setStyle")) test_setStyle();	
}
}
