/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * This tab presents cubic and quadratic curves that can be drawn. As a
 * demonstration, cubic and quadratic curves are used to spell out "SWT".
 * The user may reposition the cubic and quadratic handles. 
 */
public class CurvesSWTTab extends GraphicsTab {
	/** These rectangles represent the handles on the curves. */
	private Rectangle sRect1, sRect2, wRect1, wRect2, tTopRect1, tTopRect2, 
			tBottomRect1, tBottomRect2;
	
	/** These values represent the positions of the curves. */
	private float sXPos, sYPos, wXPos, wYPos, topTXPos, topTYPos, 
			botTXPos, botTYPos;
	
	/** These values represent the x and y displacement of each handle. */
	private float sDiffX1, sDiffY1, sDiffX2, sDiffY2;
	private float wDiffX1, wDiffY1, wDiffX2, wDiffY2;
	private float tTopDiffX1, tTopDiffY1, tTopDiffX2, tTopDiffY2;
	private float tBotDiffX1, tBotDiffY1, tBotDiffX2, tBotDiffY2;
	
	/** These are flags that indicate whether or not a handle has been moved. */
	private boolean sLeftPtMoved, sRightPtMoved, wPt1Moved, wPt2Moved,
			tTopPt1Moved, tTopPt2Moved, tBotPt1Moved, tBotPt2Moved;
	
	private MouseMoveListener mouseMoveListener;
	private MouseListener mouseListener;
	private Cursor cursor;
	
	/** true if hovering over a handle, false otherwise */
	private boolean hovering = false;
	
	/** true if left mouse button is held down, false otherwise */
	private boolean mouseDown = false;

	
public CurvesSWTTab(GraphicsExample example) {
	super(example);
	sRect1 = new Rectangle(-75, 50, 5, 5);
	sRect2 = new Rectangle(75, 100, 5, 5);
	sDiffX1 = sDiffY1 = 0;
	sDiffX2 = sDiffY2 = 0;
	wRect1 = new Rectangle(80, 300, 5, 5);
	wRect2 = new Rectangle(120, 300, 5, 5);
	wDiffX1 = wDiffY1 = wDiffX2 = wDiffY2 = 0;
	tTopRect1 = new Rectangle(33, -20, 5, 5);
	tTopRect2 = new Rectangle(66, 20, 5, 5);
	tTopDiffX1 = tTopDiffY1 = tTopDiffX2 = tTopDiffY2 = 0;
	tBottomRect1 = new Rectangle(-33, 50, 5, 5);
	tBottomRect2 = new Rectangle(33, 100, 5, 5);
	tBotDiffX1 = tBotDiffY1 = tBotDiffX2 = tBotDiffY2 = 0;
}

public String getCategory() {
	return GraphicsExample.getResourceString("Curves"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("SWT"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("CurvesSWTDescription"); //$NON-NLS-1$
}

public boolean getDoubleBuffered() {
	return true;
}

public void dispose() {
	if (mouseListener != null)
		example.canvas.removeMouseListener(mouseListener);
	
	if (mouseMoveListener != null)
		example.canvas.removeMouseMoveListener(mouseMoveListener);
	
	cursor = null;
}

	/**
	 * This helper method determines whether or not the cursor is positioned
	 * over a handle.
	 * 
	 * @param e
	 *            A MouseEvent
	 * @return true if cursor is positioned over a handle; false otherwise
	 */
private boolean isHovering(MouseEvent e) {
	Rectangle r1 = new Rectangle(sRect1.x + (int)sXPos - 1, sRect1.y + (int)sYPos - 1, sRect1.width+2, sRect1.height+2);
	Rectangle r2 = new Rectangle(sRect2.x + (int)sXPos - 1, sRect2.y + (int)sYPos - 1, sRect2.width+2, sRect2.height+2);
	Rectangle w1 = new Rectangle(wRect1.x + (int)wXPos - 1, wRect1.y + (int)wYPos - 1, wRect1.width+2, wRect1.height+2);
	Rectangle w2 = new Rectangle(wRect2.x + (int)wXPos - 1, wRect2.y + (int)wYPos - 1, wRect2.width+2, wRect2.height+2);
	Rectangle tTop1 = new Rectangle(tTopRect1.x + (int)topTXPos - 1, tTopRect1.y + (int)topTYPos - 1, tTopRect1.width+2, tTopRect1.height+2);
	Rectangle tTop2 = new Rectangle(tTopRect2.x + (int)topTXPos - 1, tTopRect2.y + (int)topTYPos - 1, tTopRect2.width+2, tTopRect2.height+2);
	Rectangle tBot1 = new Rectangle(tBottomRect1.x + (int)botTXPos - 1, tBottomRect1.y + (int)botTYPos - 1, tBottomRect1.width+2, tBottomRect1.height+2);
	Rectangle tBot2 = new Rectangle(tBottomRect2.x + (int)botTXPos - 1, tBottomRect2.y + (int)botTYPos - 1, tBottomRect2.width+2, tBottomRect2.height+2);
	
	return ( r1.contains(e.x, e.y) || r2.contains(e.x, e.y) 
		 || w1.contains(e.x, e.y) || w2.contains(e.x, e.y) 
		 || tTop1.contains(e.x, e.y) || tTop2.contains(e.x, e.y) 
		 || tBot1.contains(e.x, e.y) || tBot2.contains(e.x, e.y) );
}
/** 
 * Creates the widgets used to control the drawing.
 */
public void createControlPanel(Composite parent) {
	if (cursor == null) { 
		cursor = parent.getDisplay().getSystemCursor(SWT.CURSOR_HAND);
	}

	mouseMoveListener = new MouseMoveListener() {
		
		public void mouseMove(MouseEvent e) {
			if (hovering && mouseDown) {
				example.canvas.setCursor(cursor);
			} else if (isHovering(e)) {
				example.canvas.setCursor(cursor);
				hovering = true;
			} else {
				example.canvas.setCursor(null);
				hovering = false;
			}

			if (sLeftPtMoved) {
				sDiffX1 = sDiffX1 + e.x - (int)sXPos - sRect1.x;
				sDiffY1 = sDiffY1 + e.y - (int)sYPos - sRect1.y;
				sRect1.x = e.x - (int)sXPos;
				sRect1.y = e.y - (int)sYPos;
			} else if (sRightPtMoved) {
				sDiffX2 = sDiffX2 + e.x - (int)sXPos - sRect2.x;
				sDiffY2 = sDiffY2 + e.y - (int)sYPos - sRect2.y;
				sRect2.x = e.x - (int)sXPos;
				sRect2.y = e.y - (int)sYPos;
			} else if (wPt1Moved) {
				wDiffX1 = wDiffX1 + e.x - (int)wXPos - wRect1.x;
				wDiffY1 = wDiffY1 + e.y - (int)wYPos - wRect1.y;
				wRect1.x = e.x - (int)wXPos;
				wRect1.y = e.y - (int)wYPos;				
			} else if (wPt2Moved) {
				wDiffX2 = wDiffX2 + e.x - (int)wXPos - wRect2.x;
				wDiffY2 = wDiffY2 + e.y - (int)wYPos - wRect2.y;
				wRect2.x = e.x - (int)wXPos;
				wRect2.y = e.y - (int)wYPos;	
			} else if (tTopPt1Moved) {
				tTopDiffX1 = tTopDiffX1 + e.x - (int)topTXPos - tTopRect1.x;
				tTopDiffY1 = tTopDiffY1 + e.y - (int)topTYPos - tTopRect1.y;
				tTopRect1.x = e.x - (int)topTXPos;
				tTopRect1.y = e.y - (int)topTYPos;	
			} else if (tTopPt2Moved) {
				tTopDiffX2 = tTopDiffX2 + e.x - (int)topTXPos - tTopRect2.x;
				tTopDiffY2 = tTopDiffY2 + e.y - (int)topTYPos - tTopRect2.y;
				tTopRect2.x = e.x - (int)topTXPos;
				tTopRect2.y = e.y - (int)topTYPos;
			} else if (tBotPt1Moved) {
				tBotDiffX1 = tBotDiffX1 + e.x - (int)botTXPos - tBottomRect1.x;
				tBotDiffY1 = tBotDiffY1 + e.y - (int)botTYPos - tBottomRect1.y;
				tBottomRect1.x = e.x - (int)botTXPos;
				tBottomRect1.y = e.y - (int)botTYPos;	
			} else if (tBotPt2Moved) {
				tBotDiffX2 = tBotDiffX2 + e.x - (int)botTXPos - tBottomRect2.x;
				tBotDiffY2 = tBotDiffY2 + e.y - (int)botTYPos - tBottomRect2.y;
				tBottomRect2.x = e.x - (int)botTXPos;
				tBottomRect2.y = e.y - (int)botTYPos;
			}
			example.redraw();			
		}
	};
	
	mouseListener = new MouseListener() {

		public void mouseDoubleClick(MouseEvent e) {}

		/**
		 * Sent when a mouse button is pressed.
		 *
		 * @param e an event containing information about the mouse button press
		 */
		public void mouseDown(MouseEvent e) {  
			Rectangle r1 = new Rectangle(sRect1.x + (int)sXPos - 1, sRect1.y + (int)sYPos - 1, sRect1.width+2, sRect1.height+2);
			Rectangle r2 = new Rectangle(sRect2.x + (int)sXPos - 1, sRect2.y + (int)sYPos - 1, sRect2.width+2, sRect2.height+2);
			Rectangle w1 = new Rectangle(wRect1.x + (int)wXPos - 1, wRect1.y + (int)wYPos - 1, wRect1.width+2, wRect1.height+2);
			Rectangle w2 = new Rectangle(wRect2.x + (int)wXPos - 1, wRect2.y + (int)wYPos - 1, wRect2.width+2, wRect2.height+2);
			Rectangle tTop1 = new Rectangle(tTopRect1.x + (int)topTXPos - 1, tTopRect1.y + (int)topTYPos - 1, tTopRect1.width+2, tTopRect1.height+2);
			Rectangle tTop2 = new Rectangle(tTopRect2.x + (int)topTXPos - 1, tTopRect2.y + (int)topTYPos - 1, tTopRect2.width+2, tTopRect2.height+2);
			Rectangle tBot1 = new Rectangle(tBottomRect1.x + (int)botTXPos - 1, tBottomRect1.y + (int)botTYPos - 1, tBottomRect1.width+2, tBottomRect1.height+2);
			Rectangle tBot2 = new Rectangle(tBottomRect2.x + (int)botTXPos - 1, tBottomRect2.y + (int)botTYPos - 1, tBottomRect2.width+2, tBottomRect2.height+2);

			if (r1.contains(e.x, e.y)) {
				sLeftPtMoved = true;
				mouseDown = true;
			} else if (r2.contains(e.x, e.y)) {
				sRightPtMoved = true;
				mouseDown = true;
			} else if (w1.contains(e.x, e.y)) {
				wPt1Moved = true;
				mouseDown = true;
			} else if (w2.contains(e.x, e.y)) {
				wPt2Moved = true;
				mouseDown = true;
			} else if (tTop1.contains(e.x, e.y)) {
				tTopPt1Moved = true;
				mouseDown = true;
			} else if (tTop2.contains(e.x, e.y)) {
				tTopPt2Moved = true;
				mouseDown = true;
			} else if (tBot1.contains(e.x, e.y)) {
				tBotPt1Moved = true;
				mouseDown = true;
			} else if (tBot2.contains(e.x, e.y)) {
				tBotPt2Moved = true;
				mouseDown = true;
			}
		}

		/**
		 * Sent when a mouse button is released.
		 *
		 * @param e an event containing information about the mouse button release
		 */
		public void mouseUp(MouseEvent e) {
			mouseDown = false;
			if (isHovering(e)) {
				example.canvas.setCursor(cursor);
			} else {
				example.canvas.setCursor(null);
			}
			if (sLeftPtMoved)
				sLeftPtMoved = false;
			if (sRightPtMoved)
				sRightPtMoved = false;
			if (wPt1Moved)
				wPt1Moved = false;
			if (wPt2Moved)
				wPt2Moved = false;
			if (tTopPt1Moved)
				tTopPt1Moved = false;	
			if (tTopPt2Moved)
				tTopPt2Moved = false;	
			if (tBotPt1Moved)
				tBotPt1Moved = false;	
			if (tBotPt2Moved)
				tBotPt2Moved = false;
			
			example.redraw();
		}
	};
	example.canvas.addMouseMoveListener(mouseMoveListener);
	example.canvas.addMouseListener(mouseListener);
}

public void paint(GC gc, int width, int height) {
	if (!example.checkAdvancedGraphics()) return;
	Device device = gc.getDevice();
	
	Font font = new Font(device, getPlatformFont(), 16, SWT.ITALIC);
	gc.setFont(font);
	gc.setLineWidth(2);
	
	Transform transform;
	
	// ----- letter s -----	
	sXPos = 4*width/16;
	sYPos = (height-150)/2;
	
	transform = new Transform(device);
	transform.translate(sXPos, sYPos);
	gc.setTransform(transform);
	transform.dispose();

	gc.setForeground(device.getSystemColor(SWT.COLOR_DARK_BLUE));
	gc.drawString(GraphicsExample.getResourceString("Cubic"), 0, 175, true);
	
	Path path = new Path(device);
	path.cubicTo(-200 + sDiffX1, 50 + sDiffY1, 200 + sDiffX2, 100 + sDiffY2, 0, 150);
	gc.drawPath(path);
	path.dispose();
	
	// draw the spline points
	gc.setTransform(null);
	gc.drawRectangle(sRect1.x + (int)sXPos, sRect1.y + (int)sYPos, sRect1.width, sRect1.height);
	gc.drawRectangle(sRect2.x + (int)sXPos, sRect2.y + (int)sYPos, sRect2.width, sRect2.height);
	
	// ----- letter w -----
	wXPos = 6*width/16;
	wYPos = (height-150)/2;
	
	transform = new Transform(device);
	transform.translate(wXPos, wYPos);
	gc.setTransform(transform);
	transform.dispose();
	
	gc.setForeground(device.getSystemColor(SWT.COLOR_GRAY));
	gc.drawString(GraphicsExample.getResourceString("Quadratic"), 0, -50, true);
	gc.drawString(GraphicsExample.getResourceString("Quadratic"), 110, -50, true);
	
	path = new Path(device);
	path.quadTo(100 + wDiffX1, 300 + wDiffY1, 100, 0);
	path.quadTo(100+wDiffX2, 300+wDiffY2, 200, 0);
	gc.drawPath(path);
	path.dispose();
	
	gc.setTransform(null);	
	gc.drawRectangle(wRect1.x + (int)wXPos, wRect1.y + (int)wYPos, wRect1.width, wRect1.height);
	gc.drawRectangle(wRect2.x + (int)wXPos, wRect2.y + (int)wYPos, wRect2.width, wRect2.height);
	
	
	// ----- top of letter t -----
	topTXPos = 11*width/16;
	topTYPos = (height-150)/2;
	
	transform = new Transform(device);
	transform.translate(topTXPos, topTYPos);
	gc.setTransform(transform);
	transform.dispose();
	
	gc.setForeground(device.getSystemColor(SWT.COLOR_YELLOW));
	gc.drawString(GraphicsExample.getResourceString("Cubic"), 25, -50, true);
	
	path = new Path(device);
	path.cubicTo(33 + tTopDiffX1, -20 + tTopDiffY1, 66 + tTopDiffX2, 20 + tTopDiffY2, 100, 0);
	gc.drawPath(path);
	path.dispose();

	gc.setTransform(null);
	gc.drawRectangle(tTopRect1.x + (int)topTXPos, tTopRect1.y + (int)topTYPos, tTopRect1.width, tTopRect1.height);
	gc.drawRectangle(tTopRect2.x + (int)topTXPos, tTopRect2.y + (int)topTYPos, tTopRect2.width, tTopRect2.height);
	

	// ----- vertical bar of letter t -----
	botTXPos = 12*width/16;
	botTYPos = (height-150)/2;

	transform = new Transform(device);
	transform.translate(botTXPos, botTYPos);
	gc.setTransform(transform);
	transform.dispose();
	
	gc.setForeground(device.getSystemColor(SWT.COLOR_RED));
	gc.drawString(GraphicsExample.getResourceString("Cubic"), 0, 175, true);
	
	path = new Path(device);
	path.cubicTo(-33 + tBotDiffX1, 50 + tBotDiffY1, 33 + tBotDiffX2, 100 + tBotDiffY2, 0, 150);
	gc.drawPath(path);
	path.dispose();
	
	gc.setTransform(null);
	gc.drawRectangle(tBottomRect1.x + (int)botTXPos, tBottomRect1.y + (int)botTYPos, tBottomRect1.width, tBottomRect1.height);
	gc.drawRectangle(tBottomRect2.x + (int)botTXPos, tBottomRect2.y + (int)botTYPos, tBottomRect2.width, tBottomRect2.height);

	font.dispose();
}

/**
 * Returns the name of a valid font for the resident platform.
 */
static String getPlatformFont() {
	if(SWT.getPlatform() == "win32") {
		return "Arial";	
	} else if (SWT.getPlatform() == "motif") {
		return "Helvetica";		
	} else if (SWT.getPlatform() == "gtk") {
		return "Baekmuk Batang";		
	} else if (SWT.getPlatform() == "carbon") {
		return "Arial";
	} else { // photon, etc ...
		return "Verdana";
	}
}
}
