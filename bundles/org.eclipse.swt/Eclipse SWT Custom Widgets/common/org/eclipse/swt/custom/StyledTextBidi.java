package org.eclipse.swt.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.internal.BidiUtil;
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.widgets.Control;
import java.io.*;
import java.util.Enumeration;
import java.util.Vector;

/*
 */
class StyledTextBidi {
	GC gc;
	int tabWidth;
	int[] renderPositions;
	int[] order;
	int[] dx;
	byte[] classBuffer;
	byte[] glyphBuffer;
	
	class DirectionRun {
		int logicalStart;
		int logicalEnd;
		
		DirectionRun(int logicalStart, int logicalEnd) {
			this.logicalStart = logicalStart;
			this.logicalEnd = logicalEnd;
		}		
		int getVisualStart() {
			int visualStart = order[logicalStart];
			int visualEnd = order[logicalEnd];
			
			if (visualEnd < visualStart) {
				visualStart = visualEnd;
			}
			return visualStart;
		}
		int getVisualEnd() {
			int visualStart = order[logicalStart];
			int visualEnd = order[logicalEnd];
			
			if (visualEnd < visualStart) {
				visualEnd = visualStart;
			}
			return visualEnd;
		}
		int getRenderStartX() {
			return renderPositions[getVisualStart()];
		}
		int getRenderStopX() {
			int visualEnd = getVisualEnd();
			
			return renderPositions[visualEnd] + dx[visualEnd];
		}
		public String toString() {
			StringBuffer buf = new StringBuffer();
			buf.append("vStart,Stop:" + getVisualStart()  + "," + getVisualEnd() + " lStart,Stop:" + logicalStart  + "," + logicalEnd + " renderStart,Stop: " + getRenderStartX() + "," + getRenderStopX());
			return buf.toString();
		}
	}
	
public StyledTextBidi(GC gc, int tabWidth, String text, int[] boldRanges, Font boldFont, int [] offsets) {
	int length = text.length();
		
	setGC(gc);
	setTabWidth(tabWidth);
	renderPositions = new int[length];
	order = new int[length];
	dx = new int[length];
	classBuffer = new byte[length];
	if (text.length() == 0) {
		glyphBuffer = new byte[0];
	}
	else {
		glyphBuffer = BidiUtil.getRenderInfo(gc, text, order, classBuffer, dx, 0, offsets);
		if (boldRanges != null) {
			Font normalFont = gc.getFont();
			gc.setFont(boldFont);
			for (int i = 0; i < boldRanges.length; i += 2) {
				int rangeStart = boldRanges[i];
				int rangeLength = boldRanges[i + 1];
				prepareBoldText(text, rangeStart, rangeLength);
			}
			gc.setFont(normalFont);
		}
		adjustTabStops(text);
		calculateRenderPositions();
	}
}
public static void addLanguageListener (int hwnd, Runnable runnable) {
	BidiUtil.addLanguageListener(hwnd, runnable);
}
void adjustTabStops(String text) {
	int tabIndex = text.indexOf('\t', 0);
	int logicalIndex = 0;
	int x = StyledText.XINSET;
				
	while (tabIndex != -1) {
		for (; logicalIndex < tabIndex; logicalIndex++) {
			x += dx[order[logicalIndex]];
		}
		int tabStop = x + tabWidth;
		tabStop -= tabStop % tabWidth;
		dx[order[tabIndex]] = tabStop - x;
		tabIndex = text.indexOf('\t', tabIndex + 1);
	}
}
void calculateRenderPositions() {
	renderPositions = new int[dx.length];	
	renderPositions[0] = StyledText.XINSET;
	for (int i = 0; i < dx.length - 1; i++) {
		renderPositions[i + 1] = renderPositions[i] + dx[i];
	}
}
/** 
 * 
 * @param logicalStart start offset in the logical text
 * @param length number of logical characters to render
 * @param xOffset x location of the line start
 * @param yOffset y location of the line start
 */
public int drawBidiText(int logicalStart, int length, int xOffset, int yOffset) {
	Enumeration directionRuns = getDirectionRuns(logicalStart, length).elements();
	int endOffset = logicalStart + length;
	int stopX;

	if (endOffset > getTextLength()) {
		return StyledText.XINSET;
	}
	while (directionRuns.hasMoreElements()) {
		DirectionRun run = (DirectionRun) directionRuns.nextElement();
		int visualStart = run.getVisualStart();
		int visualEnd = run.getVisualEnd();
		int x = xOffset + run.getRenderStartX();
		drawGlyphs(visualStart, visualEnd - visualStart + 1, x, yOffset);				
	}		
	// between R2L and L2R direction segment?
	if (endOffset < order.length && isRightToLeft(endOffset) == false && isRightToLeft(endOffset - 1)) {
		// continue drawing at start of L2R segment
		stopX = renderPositions[endOffset];
	}
	else
	if (isRightToLeft(endOffset - 1)) {
		// we rendered a R2L segment
		stopX = renderPositions[order[endOffset - 1]];
	}
	else {
		int visualEnd = order[endOffset - 1];
		// we rendered a L2R segment
		stopX = renderPositions[visualEnd] + dx[visualEnd];
	}
	return stopX;
}
/**
 * 
 */
void drawGlyphs(int visualStart, int length, int x, int y) {
	byte[] renderBuffer = new byte[length * 2];
	int[] renderDx = new int[length];

	if (length == 0) {
		return;
	}	
	System.arraycopy(glyphBuffer, visualStart * 2, renderBuffer, 0, length * 2);
	// copy the distance values for the desired rendering range
	System.arraycopy(dx, visualStart, renderDx, 0, length);	
	BidiUtil.drawGlyphs(gc, renderBuffer, renderDx, x, y);
}
public boolean equals(Object object) {
	StyledTextBidi test;
	if (object == this) return true;
	if (object instanceof StyledTextBidi) test = (StyledTextBidi) object;
	else return false;

	int[] intArray1 = test.order;
	int[] intArray2 = this.order;
	if (intArray1.length != intArray2.length) return false;
	for (int i=0; i<intArray1.length; i++) {
		if (intArray1[i] != intArray2[i]) return false;
	}
	intArray1 = test.dx;
	intArray2 = this.dx;
	if (intArray1.length != intArray2.length) return false;
	for (int i=0; i<intArray1.length; i++) {
		if (intArray1[i] != intArray2[i]) return false;
	}
	intArray1 = test.renderPositions;
	intArray2 = this.renderPositions;
	if (intArray1.length != intArray2.length) return false;
	for (int i=0; i<intArray1.length; i++) {
		if (intArray1[i] != intArray2[i]) return false;
	}
	byte[] byteArray1 = test.classBuffer;
	byte[] byteArray2 = this.classBuffer;
	if (byteArray1.length != byteArray2.length) return false;
	for (int i=0; i<byteArray1.length; i++) {
		if (byteArray1[i] != byteArray2[i]) return false;
	}
	byteArray1 = test.glyphBuffer;
	byteArray2 = this.glyphBuffer;
	if (byteArray1.length != byteArray2.length) return false;
	for (int i=0; i<byteArray1.length; i++) {
		if (byteArray1[i] != byteArray2[i]) return false;
	}
	return true;
}
public void fillBackground(int logicalStart, int length, int xOffset, int yOffset, int height) {
	Enumeration directionRuns = getDirectionRuns(logicalStart, length).elements();

	if (logicalStart + length > getTextLength()) {
		return;
	}
	while (directionRuns.hasMoreElements()) {
		DirectionRun run = (DirectionRun) directionRuns.nextElement();
		int visualStart = run.getVisualStart();
		int visualEnd = run.getVisualEnd();
		int startX = run.getRenderStartX();
		gc.fillRectangle(xOffset + startX, yOffset, run.getRenderStopX() - startX, height);	
	}				
}
public int[] getCaretOffsetAndDirectionAtX(int x) {
	int lineLength = getTextLength();
	int low = -1;
	int high = lineLength;
	int offset;

	if (lineLength == 0) {
		return new int[] {0,0};
	}
	
	int eol = renderPositions[renderPositions.length - 1] + dx[dx.length - 1];
	if (x > eol) {
		return new int[] {lineLength,ST.COLUMN_NEXT};
	}
	
	// get the index visually clicked character
	int visualIndex = 0;
	int visualX = renderPositions[visualIndex];
	while ((x > visualX) && (visualIndex < renderPositions.length)) {
		visualIndex++;
		if (visualIndex < renderPositions.length) visualX = renderPositions[visualIndex];
	}
	visualIndex = Math.max(0,visualIndex-1);
	// figure out if the character was clicked on the right or left
	int halfway = renderPositions[visualIndex] + (dx[visualIndex] / 2);
	boolean visualLeft = (x <= halfway);
	int direction;
	offset = getLogicalOffset(visualIndex);

	// handle visual beginning
	if (visualIndex == 0) {
		if (isRightToLeft(offset)) {
			if (visualLeft) {
				offset = getLigatureOffset(offset);
				offset = offset + 1;
				direction = ST.COLUMN_NEXT;
			}
			else {
				direction = ST.COLUMN_PREVIOUS;
			}
		}
		else {
			if (visualLeft) {
				direction = ST.COLUMN_PREVIOUS;
			}
			else {
				offset = offset + 1;
				direction = ST.COLUMN_NEXT;
			}
		}			
		return new int[] {offset, direction};
	}	

	// handle visual end
	if (visualIndex == renderPositions.length - 1) {
		if (isRightToLeft(offset)) {
			if (visualLeft) {
				offset = getLigatureOffset(offset);
				offset = offset + 1;
				direction = ST.COLUMN_NEXT;
			}
			else {
				offset = offset;
				direction = ST.COLUMN_PREVIOUS;
			}
		}
		else {
			if (visualLeft) {
				offset = offset;
				direction = ST.COLUMN_PREVIOUS;
			}
			else {
				offset = offset + 1;
				direction = ST.COLUMN_NEXT;
			}
		}
		return new int[] {offset, direction};
	}	

	if (isRightToLeft(offset)) {
		if (visualLeft) {
			offset = getLigatureOffset(offset);
			offset = offset + 1;
			direction = ST.COLUMN_NEXT;
		}
		else {
			offset = offset;
			direction = ST.COLUMN_PREVIOUS;
		}
	}
	else {
		if (visualLeft) {
			offset = offset;
			direction = ST.COLUMN_PREVIOUS;
		}
		else {
			offset = offset + 1;
			direction = ST.COLUMN_NEXT;
		}
	}
	return new int[] {offset, direction};		
}
public int getCaretPosition(int logicalOffset) {
	int caretX;

	if (getTextLength() == 0) {
		return StyledText.XINSET;
	}
	// at or past end of line?
	if (logicalOffset >= order.length) {
		logicalOffset = Math.min(logicalOffset, order.length - 1);
		int visualOffset = order[logicalOffset];
		if (isRightToLeft(logicalOffset)) {
			caretX = renderPositions[visualOffset];
		}
		else {
			caretX = renderPositions[visualOffset] + dx[visualOffset];
		}
	}
	else
	if (logicalOffset > 0 && isRightToLeft(logicalOffset) != isRightToLeft(logicalOffset - 1)) {
		// logically first character of different direction segment?
		// go behind last character of previous direction segment
		int visualOffset = order[logicalOffset - 1];
		if (isRightToLeft(logicalOffset - 1)) {
			caretX = renderPositions[visualOffset];
		}
		else {
			caretX = renderPositions[visualOffset] + dx[visualOffset];
		}
	}
	else
	// at R2L character or beginning of line?
	if (isRightToLeft(logicalOffset) || logicalOffset == 0) {
		int visualOffset = order[logicalOffset];


		if (isRightToLeft(logicalOffset)) {
			caretX = renderPositions[visualOffset] + dx[visualOffset];
		}
		else {
			caretX = renderPositions[visualOffset];
		}
	}	
	else {
		// L2R segment
		caretX = renderPositions[order[logicalOffset]];
	}		
	return caretX;
}
public int getCaretPosition(int logicalOffset, int direction) {
	// moving to character at logicalOffset
	if (getTextLength() == 0) {
		return StyledText.XINSET;
	}
	int caretX;
	// at or past end of line?
	if (logicalOffset >= order.length) {
		logicalOffset = Math.min(logicalOffset, order.length - 1);
		int visualOffset = order[logicalOffset];
		if (isRightToLeft(logicalOffset)) {
			caretX = renderPositions[visualOffset];
		}
		else {
			caretX = renderPositions[visualOffset] + dx[visualOffset];
		}
	}
	else
	// at beginning of line?
	if (logicalOffset == 0) {
		int visualOffset = order[logicalOffset];
		if (isRightToLeft(logicalOffset)) {
			caretX = renderPositions[visualOffset] + dx[visualOffset];
		}
		else {
			caretX = renderPositions[visualOffset];
		}
	}
	else
	if (direction == ST.COLUMN_NEXT &&
		isRightToLeft(logicalOffset) != isRightToLeft(logicalOffset - 1)) {
		int visualOffset = order[logicalOffset-1];
		// moving between segments
		if (isRightToLeft(logicalOffset - 1)) {
			// moving from RtoL to LtoR
			caretX = renderPositions[visualOffset];
		}
		else {
			// moving from LtoR to RtoL
			caretX = renderPositions[visualOffset] + dx[visualOffset];
		}
	}
	else
	if (direction == ST.COLUMN_PREVIOUS &&
		isRightToLeft(logicalOffset) != isRightToLeft(logicalOffset - 1)) {
		int visualOffset = order[logicalOffset];
		// moving between segments
		if (isRightToLeft(logicalOffset - 1)) {
			// moving from LtoR to RtoL
			caretX = renderPositions[visualOffset];
		}
		else {
			// moving from RtoL to LtoR
			caretX = renderPositions[visualOffset] + dx[visualOffset];
		}
	}
	else
	if (isRightToLeft(logicalOffset)) {
		int visualOffset = order[logicalOffset];
		caretX = renderPositions[visualOffset] + dx[visualOffset];
	}
	else {
		caretX = renderPositions[order[logicalOffset]];
	}
	return caretX;
}

Vector getDirectionRuns(int logicalStart, int length) {
	Vector directionRuns = new Vector();
	int logicalEnd = logicalStart + length - 1;
	int segmentLogicalStart = logicalStart;
	int segmentLogicalEnd = segmentLogicalStart;
	 
	if (logicalEnd < getTextLength()) {
		while (segmentLogicalEnd <= logicalEnd) {
			int segType = classBuffer[segmentLogicalStart];
			// Search for the end of the direction segment. Each segment needs to 
			// be rendered separately. 
			// E.g., 11211 (1=R2L, 2=L2R), rendering from logical index 0 to 5 
			// would be visual 1 to 4 and would thus miss visual 0. Rendering the 
			// segments separately would render from visual 1 to 0, then 2, then 
			// 4 to 3.
			while (segmentLogicalEnd < logicalEnd && segType == classBuffer[segmentLogicalEnd + 1]) {
				segmentLogicalEnd++;
			}
			directionRuns.addElement(new DirectionRun(segmentLogicalStart, segmentLogicalEnd));
			segmentLogicalStart = ++segmentLogicalEnd;
		}
	}
	return directionRuns;
}
/**
 *  Answer SWT.LEFT or SWT.RIGHT.
 */
public static int getKeyboardLanguageDirection() {
	int language = BidiUtil.getKeyboardLanguage();
	if (language == BidiUtil.KEYBOARD_HEBREW) {
		return SWT.RIGHT;
	}
	if (language == BidiUtil.KEYBOARD_ARABIC) {
		return SWT.RIGHT;
	}
	return SWT.LEFT;
}
int getLigatureOffset(int offset) {
	// should call BidiText to see if font has ligatures...
	int newOffset = offset;
	int i = offset + 1;
	while (i<order.length && (order[i] == order[offset])) {
		newOffset = i;
		i++;
	}
	return newOffset;
}
int getLogicalOffset(int visualOffset) {
	int logicalOffset = 0;
	
	while (logicalOffset < order.length && order[logicalOffset] != visualOffset) {
		logicalOffset++;
	}
	return logicalOffset;
}
public int getOffsetAtX(int x) {
	int lineLength = getTextLength();
	int low = -1;
	int high = lineLength;

	if (lineLength == 0) {
		return 0;
	}
	if (x > renderPositions[renderPositions.length - 1] + dx[dx.length - 1]) {
		return lineLength;
	}
	while (high - low > 1) {
		int offset = (high + low) / 2;
		int visualX = renderPositions[offset];


		if (x <= visualX + dx[offset]) {
			high = offset;			
		}
		else 
		if (high == lineLength && high - offset == 1) {
			// requested x location is past end of line
			high = -1;
		}
		else {
			low = offset;
		}
	}
	return getLogicalOffset(high);
}
int[] getRenderIndexesFor(int start, int length) {
	int[] positions = new int[length];
	int end = start + length;
	
	for (int i = start; i < end; i++) {
		positions[i-start] = order[i];
	}		
	return positions;
}
int getTextLength() {
	return order.length;
}
public int getTextWidth() {
	int width = 0;
	
	if (getTextLength() > 0) {
		width = renderPositions[renderPositions.length - 1] + dx[dx.length - 1];
	}
	return width;
}
/**
 * 
 */
public boolean isRightToLeft(int logicalIndex) {
	// for rendering, caret positioning, consider numbers as LtoR
	boolean isRightToLeft = false;
	
	if (logicalIndex < classBuffer.length) {
		isRightToLeft = (classBuffer[logicalIndex] == BidiUtil.CLASS_ARABIC) || 
					    (classBuffer[logicalIndex] == BidiUtil.CLASS_HEBREW);
	}
	return isRightToLeft;
}
/**
 * 
 */
public boolean isRightToLeftInput(int logicalIndex) {
	// for keyboard positioning, consider numbers as RtoL
	boolean isRightToLeft = false;
	
	if (logicalIndex < classBuffer.length) {
		isRightToLeft = (classBuffer[logicalIndex] == BidiUtil.CLASS_ARABIC) || 
					    (classBuffer[logicalIndex] == BidiUtil.CLASS_HEBREW) ||
					    (classBuffer[logicalIndex] == BidiUtil.CLASS_LOCALNUMBER);
	}
	return isRightToLeft;
}/**
 *
 */
void prepareBoldText(String textline, int logicalStart, int length) {
	int byteCount = length;
	int flags = 0;
	String text = textline.substring(logicalStart, logicalStart + length);

	// figure out what is before and after the substring
	// so that the proper character shaping will occur
	if (logicalStart != 0  
		&& !Character.isWhitespace(textline.charAt(logicalStart - 1)) 
		&& isRightToLeft(logicalStart - 1)) {
		// if the start of the substring is not the beginning of the 
		// text line, check to see what is before the string
		flags |= BidiUtil.LINKBEFORE;
	}
	if ((logicalStart + byteCount) != dx.length 
		&& !Character.isWhitespace(textline.charAt(logicalStart + byteCount)) 
		&& isRightToLeft(logicalStart + byteCount)) {
		// if the end of the substring is not the end of the text line,
		// check to see what is after the substring
		flags |= BidiUtil.LINKAFTER;
	}		
	// set classification values for the substring
	flags |= BidiUtil.CLASSIN;
	byte[] classArray = new byte[byteCount];
	int[] renderIndexes = getRenderIndexesFor(logicalStart, byteCount);
	for (int i = 0; i < byteCount; i++) {
		classArray[i] = classBuffer[renderIndexes[i]];
	}
	int[] dxArray = new int[byteCount];
	int[] orderArray = new int[byteCount];	
	BidiUtil.getRenderInfo(gc, text, orderArray, classArray, dxArray, flags);
	// update the existing dx array with the new dx values based on the bold font
	for (int i = 0; i < dxArray.length; i++) {
		int dxValue = dxArray[orderArray[i]];
		int visualIndex = renderIndexes[i];
		dx[visualIndex] = dxValue;
	}
}
public void redrawRange(Control parent, int logicalStart, int length, int xOffset, int yOffset, int height) {
	Enumeration directionRuns = getDirectionRuns(logicalStart, length).elements();

	if (logicalStart + length > getTextLength()) {
		return;
	}
	while (directionRuns.hasMoreElements()) {
		DirectionRun run = (DirectionRun) directionRuns.nextElement();
		int visualStart = run.getVisualStart();
		int visualEnd = run.getVisualEnd();
		int startX = run.getRenderStartX();

		parent.redraw(xOffset + startX, yOffset, run.getRenderStopX() - startX, height, true);
	}				
}
public static void removeLanguageListener (int hwnd) {
	BidiUtil.removeLanguageListener(hwnd);
}
void setGC(GC gc) {
	this.gc = gc;
}
/**
 * 
 */
public void setKeyboardLanguage(int logicalIndex) {
	int language = BidiUtil.KEYBOARD_LATIN;
	
	if (logicalIndex >= classBuffer.length) {
		return;
	}
	if (isRightToLeftInput(logicalIndex)) {
		String codePage = System.getProperty("file.encoding").toUpperCase();
		if ("CP1255".equals(codePage)) {
			language = BidiUtil.KEYBOARD_HEBREW;
		}
		else
		if ("CP1256".equals(codePage)) {		
			language = BidiUtil.KEYBOARD_ARABIC;
		}
	}
	BidiUtil.setKeyboardLanguage(language);
}
void setTabWidth(int tabWidth) {
	this.tabWidth = tabWidth;
}
/**
 * Should change output to conform with other SWT toString output (i.e., Class {value1, value2})
 */
public String toString() {
	StringBuffer buf = new StringBuffer();
	buf.append("\n");
	buf.append("Render Positions: ");
	buf.append("\n");
	for (int i=0; i<renderPositions.length; i++) {
		buf.append(renderPositions[i]);
		buf.append(" ");
	}
	buf.append("\n");
	buf.append("Order: ");
	buf.append("\n");
	for (int i=0; i<order.length; i++) {
		buf.append(order[i]);
		buf.append(" ");
	}
	buf.append("\n");
	buf.append("DX: ");
	buf.append("\n");
	for (int i=0; i<dx.length; i++) {
		buf.append(dx[i]);
		buf.append(" ");
	}
	buf.append("\n");
	buf.append("Class: ");
	buf.append("\n");
	for (int i=0; i<classBuffer.length; i++) {
		buf.append(classBuffer[i]);
		buf.append(" ");
	}
	buf.append("\n");
	buf.append("Glyph Buffer: ");
	buf.append("\n");
	for (int i=0; i<glyphBuffer.length; i++) {
		buf.append(glyphBuffer[i]);
		buf.append(" ");
	}
/*	buf.append("\n");
	buf.append("Glyphs: ");
	buf.append("\n");
	buf.append(getGlyphs());
	buf.append("\n");
*/
	return buf.toString();
}
}