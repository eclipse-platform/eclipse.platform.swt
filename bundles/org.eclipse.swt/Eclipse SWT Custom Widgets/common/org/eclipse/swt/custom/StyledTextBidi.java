package org.eclipse.swt.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.internal.BidiUtil;
import org.eclipse.swt.internal.Compatibility;
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.widgets.Control;
import java.io.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * This class provides API for StyledText to implement bidirectional text
 * functions.
 * Objects of this class are created for a single line of text.
 */
class StyledTextBidi {
	private GC gc;
	private int tabWidth;			// tab width in number of spaces. used to calculate tab stops
	private int[] renderPositions;	// x position where characters of the line get rendered at.
									// in visual order
	private int[] order;			// reordering indices in logical order. iV=order[iL]. iV=visual index, iL=logical index.
									// if no character in a line needs reordering all iV and iL are the same.
	private int[] dx;				// distance between character cells. in visual order. renderPositions[iV + 1] = renderPositions[iV] + dx[iV]
	private byte[] classBuffer;		// the character types in logical order. see BidiUtil for the possible types.
	private byte[] glyphBuffer;		// the glyphs in visual order as they will be rendered on screen.

	/** 
	 * This class describes a text segment of a single direction, either 
	 * left-to-right (L2R) or right-to-left (R2L). 
	 * Objects of this class are used by StyledTextBidi rendering methods 
	 * to render logically contiguous text segments that may be visually 
	 * discontiguous if they consist of different directions.
	 */
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

/**
 * Constructs an instance of this class for a line of text. The text 
 * is reordered to reflect right-to-left (R2L) text segments.
 * <p>
 * 
 * @param gc the GC to use for rendering and measuring of this line.
 * @param tabWidth tab width in number of spaces. used to calculate 
 * 	tab stops.
 * @param text line that bidi data should be calculated for
 * @param boldRanges bold text segments in the line. specified as 
 * 	i=bold start,i+1=bold length
 * @param boldFont font that bold text will be rendered in. needed for 
 * 	proper measuring of bold text segments.
 * @param offset text segments that should be measured and reordered 
 * 	separately. May be needed to preserve the order of separate R2L 
 * 	segments to each other.
 */
public StyledTextBidi(GC gc, int tabWidth, String text, int[] boldRanges, Font boldFont, int[] offsets) {
	int length = text.length();
		
	this.gc = gc;
	this.tabWidth = tabWidth;
	renderPositions = new int[length];
	order = new int[length];
	dx = new int[length];
	classBuffer = new byte[length];
	if (length == 0) {
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
		calculateTabStops(text);
		calculateRenderPositions();
	}
}
/**
 * Adds a listener that should be called when the user changes the 
 * keyboard layout for the specified window.
 * <p>
 * 
 * @param control Control to add the keyboard language listener for.
 * 	Each window has its own keyboard language setting.
 * @param runnable the listener that should be called when the user 
 * 	changes the keyboard layout.
 */
static void addLanguageListener(Control control, Runnable runnable) {
	BidiUtil.addLanguageListener(control.handle, runnable);
}
/**
 * Answers the direction of the active keyboard language, either 
 * left to right or right to left.
 * This determines the direction of the caret and can be changed 
 * by the user.
 * <p>
 * 
 * @return the direction of the active keyboard language. SWT.LEFT 
 * 	or SWT.RIGHT.
 */
static int getKeyboardLanguageDirection() {
	int language = BidiUtil.getKeyboardLanguage();
	if (language == BidiUtil.KEYBOARD_HEBREW) {
		return SWT.RIGHT;
	}
	if (language == BidiUtil.KEYBOARD_ARABIC) {
		return SWT.RIGHT;
	}
	return SWT.LEFT;
}
/**
 * Removes the keyboard language listener for the specified window.
 * <p>
 * 
 * @param control window to remove the keyboard language listener from.
 */
static void removeLanguageListener(Control control) {
	BidiUtil.removeLanguageListener(control.handle);
}
/**
 * Calculates render positions using the glyph distances.
 */
private void calculateRenderPositions() {
	renderPositions = new int[dx.length];	
	renderPositions[0] = StyledText.XINSET;
	for (int i = 0; i < dx.length - 1; i++) {
		renderPositions[i + 1] = renderPositions[i] + dx[i];
	}
}
/**
 * Calculates tab stops by adjusting the glyph distances.
 * <p>
 * 
 * @param text the original line text (not reordered) containing 
 * 	tab characters.
 */
private void calculateTabStops(String text) {
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
/** 
 * Renders the specified text segment.
 * The rendered text may be visually discontiguous if the text segment 
 * is bidirectional.
 * <p>
 * 
 * @param logicalStart start offset in the logical text
 * @param length number of logical characters to render
 * @param xOffset x location of the line start
 * @param yOffset y location of the line start
 */
int drawBidiText(int logicalStart, int length, int xOffset, int yOffset) {
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
 * Renders a segment of glyphs. Glyphs are visual objects so the
 * start and length are visual as well.
 * <p>
 * 
 * @param visualStart start offset of the glyphs to render relative to the 
 * 	line start.
 * @param length number of glyphs to render
 * @param x x location to render at
 * @param y y location to render at
 */
private void drawGlyphs(int visualStart, int length, int x, int y) {
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
/**
 * 
 */
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
/** 
 * Fills a rectangle spanning the given logical range.
 * The rectangle may be visually discontiguous if the text segment 
 * is bidirectional.
 * <p>
 * 
 * @param logicalStart logcial start offset of the rectangle
 * @param length number of logical characters the rectangle should span
 * @param xOffset x location of the line start
 * @param yOffset y location of the line start
 * @param height height of the rectangle
 */
void fillBackground(int logicalStart, int length, int xOffset, int yOffset, int height) {
	Enumeration directionRuns = getDirectionRuns(logicalStart, length).elements();

	if (logicalStart + length > getTextLength()) {
		return;
	}
	while (directionRuns.hasMoreElements()) {
		DirectionRun run = (DirectionRun) directionRuns.nextElement();
		int startX = run.getRenderStartX();
		gc.fillRectangle(xOffset + startX, yOffset, run.getRenderStopX() - startX, height);	
	}				
}
/**
 * Returns the offset and direction that will position the caret, depending 
 * on the direction in front of or behind the character at the specified x 
 * location in the line.
 * <p>
 * 
 * @param x the x location of the character in the line.
 * @return array containing the caret offset and direction for the x location.
 * 	index 0: offset relative to the start of the line
 * 	index 1: direction, either ST.COLUMN_NEXT or ST.COLUMN_PREVIOUS.
 *	The direction is used to control the caret position at direction 
 * 	boundaries. The semantics are like using keyboard cursor navigation. 
 * 	Example: RRRLLL
 * 	Pressing cursor left (COLUMN_PREVIOUS) in the L2R segment places the cursor 
 * 	in front of the first character of the L2R segment. Pressing cursor right 
 * 	(COLUMN_NEXT) in a R2L segment places the cursor behind the last character 
 * 	of the R2L segment. However, both are the same logical offset.
 */
int[] getCaretOffsetAndDirectionAtX(int x) {
	int lineLength = getTextLength();
	int offset;
	int direction;
	
	if (lineLength == 0) {
		return new int[] {0, 0};
	}		
	int eol = renderPositions[renderPositions.length - 1] + dx[dx.length - 1];
	if (x >= eol) {
		return new int[] {lineLength, ST.COLUMN_NEXT};
	}
	// get the visual offset of the clicked character
	int visualOffset = getVisualOffsetAtX(x);
	// figure out if the character was clicked on the right or left
	int halfway = renderPositions[visualOffset] + dx[visualOffset] / 2;
	boolean visualLeft = (x <= halfway);
	offset = getLogicalOffset(visualOffset);

	if (isRightToLeft(offset)) {
		if (visualLeft) {
			offset = getLigatureEndOffset(offset);
			offset++;
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
			offset++;
			direction = ST.COLUMN_NEXT;
		}
	}
	return new int[] {offset, direction};		
}
/**
 * Returns the caret position at the specified offset in the line.
 * <p>
 * @param logicalOffset offset of the character in the line
 * @return the caret position at the specified offset in the line.
 */
int getCaretPosition(int logicalOffset) {
	return getCaretPosition(logicalOffset, ST.COLUMN_NEXT);
}
/**
 * Returns the caret position at the specified offset in the line.
 * The direction determines the caret position at direction boundaries.
 * If the logical offset is between a R2L and a L2R segment pressing 
 * cursor left in the L2R segment places the cursor in front of the 
 * first character of the L2R segment. Pressing cursor right in the 
 * R2L segment places the cursor behind the last character of the R2L 
 * segment. However, both are the same logical offset.
 * <p>
 * 
 * @param logicalOffset offset of the character in the line
 * @param direction direction the caret moved to the specified location.
 * 	 either ST.COLUMN_NEXT (right cursor key) or ST.COLUMN_PREVIOUS (left cursor key) .
 * @return the caret position at the specified offset in the line, 
 * 	taking the direction into account as described above.
 */
int getCaretPosition(int logicalOffset, int direction) {
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
	// consider local numbers as R2L in determining direction boundaries.
	// fixes 1GK9API.
	if (direction == ST.COLUMN_NEXT &&
		isRightToLeftInput(logicalOffset) != isRightToLeftInput(logicalOffset - 1)) {
		int visualOffset = order[logicalOffset-1];
		// moving between segments.
		// do not consider local numbers as R2L here, to determine position,
		// because local numbers are navigated L2R and we want the caret to 
		// be to the right of the number. see 1GK9API
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
	// consider local numbers as R2L in determining direction boundaries.
	// fixes 1GK9API.
	if (direction == ST.COLUMN_PREVIOUS &&
		isRightToLeftInput(logicalOffset) != isRightToLeftInput(logicalOffset - 1)) {
		int visualOffset = order[logicalOffset];
		// moving between segments.
		// consider local numbers as R2L here, to determine position, because
		// we want to stay in L2R segment and place the cursor to the left of
		// first L2R character. see 1GK9API
		if (isRightToLeftInput(logicalOffset - 1)) {
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
/**
 * Returns the direction segments that are in the specified text
 * range. The text range may be visually discontiguous if the 
 * text is bidirectional. Each returned direction run has a single 
 * direction and is thus contiguous ensuring proper rendering.
 * <p>
 * 
 * @param logicalStart offset of the logcial start of the first 
 * 	direction segment
 * @param length length of the text included in the direction 
 * 	segments
 * @return the direction segments that are in the specified 
 * text range. Each segment has a single direction and is thus 
 * contiguous.
 */
private Vector getDirectionRuns(int logicalStart, int length) {
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
 * Returns the offset of the last character comprising a ligature.
 * <p>
 * 
 * @return the offset of the last character comprising a ligature.
 */
int getLigatureEndOffset(int offset) {
	if (!isLigated(gc)) return offset;
	int newOffset = offset;
	int i = offset + 1;
	while (i<order.length && (order[i] == order[offset])) {
		newOffset = i;
		i++;
	}
	return newOffset;
}
/**
 * Returns the offset of the first character comprising a ligature.
 * <p>
 * 
 * @return the offset of the first character comprising a ligature.
 */
int getLigatureStartOffset(int offset) {
	if (!isLigated(gc)) return offset;
	int newOffset = offset;
	int i = offset - 1;
	while (i>=0 && (order[i] == order[offset])) {
		newOffset = i;
		i--;
	}
	return newOffset;
}
/**
 * Returns the logical offset of the character at the specified 
 * visual offset.
 * <p>
 * 
 * @param visualOffset the visual offset
 * @return the logical offset of the character at <code>visualOffset</code>.
 */
private int getLogicalOffset(int visualOffset) {
	int logicalOffset = 0;
	
	while (logicalOffset < order.length && order[logicalOffset] != visualOffset) {
		logicalOffset++;
	}
	return logicalOffset;
}
/**
 * Returns the offset of the character at the specified x location.
 * <p>
 * 
 * @param x the location of the character
 * @return the logical offset of the character at the specified x 
 * 	location.
 */
int getOffsetAtX(int x) {
	int visualOffset;

	if (getTextLength() == 0) {
		return 0;
	}
	if (x >= renderPositions[renderPositions.length - 1] + dx[dx.length - 1]) {
		// Return when x is past the end of the line. Fixes 1GLADBK.
		return -1;
	}
	visualOffset = getVisualOffsetAtX(x);
	return getLogicalOffset(visualOffset);
}
/**
 * Returns the visual offset of the character at the specified x 
 * location.
 * <p>
 * 
 * @param x the location of the character
 * @return the visual offset of the character at the specified x 
 * 	location.
 */
private int getVisualOffsetAtX(int x) {
	int lineLength = getTextLength();
	int low = -1;
	int high = lineLength;

	while (high - low > 1) {
		int offset = (high + low) / 2;
		int visualX = renderPositions[offset];

		// visualX + dx is the start of the next character. Restrict right/high
		// search boundary only if x is before next character. Fixes 1GL4ZVE.
		if (x < visualX + dx[offset]) {
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
	return high;
}
/**
 * Returns the reordering indices that map between logical and 
 * visual index of characters in the specified range.
 * <p>
 * 
 * @param start start offset of the reordering indices
 * @param length number of reordering indices to return
 * @return the reordering indices that map between logical and 
 * 	visual index of characters in the specified range. Relative 
 * 	to the start of the range.
 */
private int[] getRenderIndexesFor(int start, int length) {
	int[] positions = new int[length];
	int end = start + length;
	
	for (int i = start; i < end; i++) {
		positions[i-start] = order[i];
	}		
	return positions;
}
/**
 * Returns the number of characters in the line.
 * <p>
 * 
 * @return the number of characters in the line.
 */
private int getTextLength() {
	return order.length;
}
/**
 * Returns the width in pixels of the line.
 * <p>
 * 
 * @return the width in pixels of the line.
 */
int getTextWidth() {
	int width = 0;
	
	if (getTextLength() > 0) {
		width = renderPositions[renderPositions.length - 1] + dx[dx.length - 1];
	}
	return width;
}
/**
 * Returns whether the font set in the specified gc contains 
 * ligatured glyphs.
 * <p>
 * 
 * @param gc the GC that should be tested for ligatures.
 * @return 
 * 	true=the font set in the specified gc contains ligatured glyphs. 
 * 	false=the font set in the specified gc doesn't contain ligatured 
 * 	glyphs. 
 */
static boolean isLigated(GC gc) {
	// should call BidiUtil
	return true;
}
/**
 * Returns the direction of the character at the specified index.
 * Used for rendering and caret positioning where local numbers (e.g., 
 * national Arabic, or Hindi, numbers) are considered left-to-right.
 * <p>
 * 
 * @param logicalIndex the index of the character
 * @return 
 * 	true=the character at the specified index is in a right-to-left
 * 	codepage (e.g., Hebrew, Arabic).
 * 	false=the character at the specified index is in a left-to-right/latin
 * 	codepage.
 */
boolean isRightToLeft(int logicalIndex) {
	boolean isRightToLeft = false;
	
	if (logicalIndex < classBuffer.length) {
		isRightToLeft = (classBuffer[logicalIndex] == BidiUtil.CLASS_ARABIC) || 
					    (classBuffer[logicalIndex] == BidiUtil.CLASS_HEBREW);
	}
	return isRightToLeft;
}
/**
 * Returns the direction of the character at the specified index.
 * Used for setting the keyboard language where local numbers (e.g., 
 * national Arabic, or Hindi, numbers) are considered right-to-left.
 * <p>
 * 
 * @param logicalIndex the index of the character
 * @return 
 * 	true=the character at the specified index is in a right-to-left
 * 	codepage (e.g., Hebrew, Arabic).
 * 	false=the character at the specified index is in a left-to-right/latin
 * 	codepage.
 */
boolean isRightToLeftInput(int logicalIndex) {
	boolean isRightToLeft = false;
	
	if (logicalIndex < classBuffer.length) {
		isRightToLeft = (classBuffer[logicalIndex] == BidiUtil.CLASS_ARABIC) || 
					    (classBuffer[logicalIndex] == BidiUtil.CLASS_HEBREW) ||
					    (classBuffer[logicalIndex] == BidiUtil.CLASS_LOCALNUMBER);
	}
	return isRightToLeft;
}
/**
 * Reorder and calculate render positions for the specified sub-line 
 * of text. The results will be merged with the data for the rest of 
 * the line .
 * <p>
 * 
 * @param textline the entire line of text that this object represents.
 * @param logicalStart the start offset of the first character to 
 * 	reorder.
 * @param length the number of characters to reorder
 */
private void prepareBoldText(String textline, int logicalStart, int length) {
	int byteCount = length;
	int flags = 0;
	String text = textline.substring(logicalStart, logicalStart + length);

	// figure out what is before and after the substring
	// so that the proper character shaping will occur
	if (logicalStart != 0  
		&& !Compatibility.isWhitespace(textline.charAt(logicalStart - 1)) 
		&& isRightToLeft(logicalStart - 1)) {
		// if the start of the substring is not the beginning of the 
		// text line, check to see what is before the string
		flags |= BidiUtil.LINKBEFORE;
	}
	if ((logicalStart + byteCount) != dx.length 
		&& !Compatibility.isWhitespace(textline.charAt(logicalStart + byteCount)) 
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
/** 
 * Redraws a rectangle spanning the given logical range.
 * The rectangle may be visually discontiguous if the text segment 
 * is bidirectional.
 * <p>
 * 
 * @param parent window that should be invalidated
 * @param logicalStart logcial start offset of the rectangle
 * @param length number of logical characters the rectangle should span
 * @param xOffset x location of the line start
 * @param yOffset y location of the line start
 * @param height height of the invalidated rectangle
 */
void redrawRange(Control parent, int logicalStart, int length, int xOffset, int yOffset, int height) {
	Enumeration directionRuns = getDirectionRuns(logicalStart, length).elements();

	if (logicalStart + length > getTextLength()) {
		return;
	}
	while (directionRuns.hasMoreElements()) {
		DirectionRun run = (DirectionRun) directionRuns.nextElement();
		int startX = run.getRenderStartX();

		parent.redraw(xOffset + startX, yOffset, run.getRenderStopX() - startX, height, true);
	}				
}
/**
 * Sets the keyboard language to match the codepage of the character 
 * at the specified offset.
 * Only distinguishes between left-to-right and right-to-left
 * characters and sets the keyboard language to one of Latin, Hebrew 
 * and Arabic.
 * <p>
 * 
 * @param logicalIndex logical offset of the character to use for 
 * 	determining the new keyboard language.
 */
void setKeyboardLanguage(int logicalIndex) {
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
/**
 * Returns a string representation of the receiver.
 * Should change output to conform with other SWT toString output (i.e., Class {value1, value2})
 * <p>
 * 
 * @return a string representation of the receiver for 
 * 	debugging purposes.
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