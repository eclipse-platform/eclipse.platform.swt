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
 * This tab presents cubic and quadratic curves that can be drawn.
 * The user may reposition the cubic and quadratic handles. 
 */
public class CurvesTab extends GraphicsTab {
	/** These rectangles represent the handles on the curves. */
	private Rectangle quadHndl, quadEndHndl, cubHndl1, cubHndl2, cubEndHndl;
	
	/** These values represent the positions of the curves. */
	private float quadXPos, quadYPos, cubXPos, cubYPos;
	
	/** These values represent the x and y displacement of each handle. */
	private float quadDiffX, quadDiffY, quadEndDiffX, quadEndDiffY;
	private float cubDiffX1, cubDiffY1, cubDiffX2, cubDiffY2, cubEndDiffX, cubEndDiffY;
	
	/** These are flags that indicate whether or not a handle has been moved. */
	private boolean quadPtMoved, quadEndPtMoved, cubPt1Moved, cubPt2Moved, cubEndPtMoved;

	private MouseMoveListener mouseMoveListener;
	private MouseListener mouseListener;
	private Cursor cursor;

	/** true if hovering over a handle, false otherwise */
	private boolean hovering = false;
	
	/** true if left mouse button is held down, false otherwise */
	private boolean mouseDown = false;

	
public CurvesTab(GraphicsExample example) {
	super(example);
	quadHndl = new Rectangle(200, 150, 5, 5);
	quadEndHndl = new Rectangle(400, 0, 5, 5);
	quadDiffX = quadDiffY = quadEndDiffX = quadEndDiffY = 0;
	cubHndl1 = new Rectangle(133, -60, 5, 5);
	cubHndl2 = new Rectangle(266, 60, 5, 5);
	cubDiffX1 = cubDiffY1 = cubDiffX2 = cubDiffY2 = 0;
	cubEndHndl = new Rectangle(400, 0, 5, 5);
	cubEndDiffX = cubEndDiffY = 0;
}

public String getCategory() {
	return GraphicsExample.getResourceString("Curves"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("Curves"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("CurvesDescription"); //$NON-NLS-1$
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
	Rectangle quad = new Rectangle(quadHndl.x + (int)quadXPos - 1, quadHndl.y + (int)quadYPos - 1, quadHndl.width+2, quadHndl.height+2);
	Rectangle quadEnd = new Rectangle(quadEndHndl.x + (int)quadXPos - 1, quadEndHndl.y + (int)quadYPos - 1, quadEndHndl.width+2, quadEndHndl.height+2);
	Rectangle cub1 = new Rectangle(cubHndl1.x + (int)cubXPos - 1, cubHndl1.y + (int)cubYPos - 1, cubHndl1.width+2, cubHndl1.height+2);
	Rectangle cub2 = new Rectangle(cubHndl2.x + (int)cubXPos - 1, cubHndl2.y + (int)cubYPos - 1, cubHndl2.width+2, cubHndl2.height+2);
	Rectangle cubEnd = new Rectangle(cubEndHndl.x + (int)cubXPos - 1, cubEndHndl.y + (int)cubYPos - 1, cubEndHndl.width+2, cubEndHndl.height+2);

	return ( quad.contains(e.x, e.y) || quadEnd.contains(e.x, e.y) 
		 || cub1.contains(e.x, e.y) || cub2.contains(e.x, e.y) 
		 || cubEnd.contains(e.x, e.y));
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

			if (quadPtMoved) {
				quadDiffX = quadDiffX + e.x - (int)quadXPos - quadHndl.x;
				quadDiffY = quadDiffY + e.y - (int)quadYPos - quadHndl.y;
				quadHndl.x = e.x - (int)quadXPos;
				quadHndl.y = e.y - (int)quadYPos;				
			} else if (quadEndPtMoved) {
				quadEndDiffX = quadEndDiffX + e.x - (int)quadXPos - quadEndHndl.x;
				quadEndDiffY = quadEndDiffY + e.y - (int)quadYPos - quadEndHndl.y;
				quadEndHndl.x = e.x - (int)quadXPos;
				quadEndHndl.y = e.y - (int)quadYPos;	
			} else if (cubPt1Moved) {
				cubDiffX1 = cubDiffX1 + e.x - (int)cubXPos - cubHndl1.x;
				cubDiffY1 = cubDiffY1 + e.y - (int)cubYPos - cubHndl1.y;
				cubHndl1.x = e.x - (int)cubXPos;
				cubHndl1.y = e.y - (int)cubYPos;	
			} else if (cubPt2Moved) {
				cubDiffX2 = cubDiffX2 + e.x - (int)cubXPos - cubHndl2.x;
				cubDiffY2 = cubDiffY2 + e.y - (int)cubYPos - cubHndl2.y;
				cubHndl2.x = e.x - (int)cubXPos;
				cubHndl2.y = e.y - (int)cubYPos;
			} else if (cubEndPtMoved) {
				cubEndDiffX = cubEndDiffX + e.x - (int)cubXPos - cubEndHndl.x;
				cubEndDiffY = cubEndDiffY + e.y - (int)cubYPos - cubEndHndl.y;
				cubEndHndl.x = e.x - (int)cubXPos;
				cubEndHndl.y = e.y - (int)cubYPos;
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
			Rectangle quad = new Rectangle(quadHndl.x + (int)quadXPos - 1, quadHndl.y + (int)quadYPos - 1, quadHndl.width+2, quadHndl.height+2);
			Rectangle quadEnd = new Rectangle(quadEndHndl.x + (int)quadXPos - 1, quadEndHndl.y + (int)quadYPos - 1, quadEndHndl.width+2, quadEndHndl.height+2);
			Rectangle cub1 = new Rectangle(cubHndl1.x + (int)cubXPos - 1, cubHndl1.y + (int)cubYPos - 1, cubHndl1.width+2, cubHndl1.height+2);
			Rectangle cub2 = new Rectangle(cubHndl2.x + (int)cubXPos - 1, cubHndl2.y + (int)cubYPos - 1, cubHndl2.width+2, cubHndl2.height+2);
			Rectangle cubEnd = new Rectangle(cubEndHndl.x + (int)cubXPos - 1, cubEndHndl.y + (int)cubYPos - 1, cubEndHndl.width+2, cubEndHndl.height+2);
			
			if (quad.contains(e.x, e.y)) {
				quadPtMoved = true;
				mouseDown = true;
			} else if (quadEnd.contains(e.x, e.y)) {
				quadEndPtMoved = true;
				mouseDown = true;
			} else if (cub1.contains(e.x, e.y)) {
				cubPt1Moved = true;
				mouseDown = true;
			} else if (cub2.contains(e.x, e.y)) {
				cubPt2Moved = true;
				mouseDown = true;
			} else if (cubEnd.contains(e.x, e.y)) {
				cubEndPtMoved = true;
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

			if (quadPtMoved)
				quadPtMoved = false;
			if (quadEndPtMoved)
				quadEndPtMoved = false;
			if (cubPt1Moved)
				cubPt1Moved = false;	
			if (cubPt2Moved)
				cubPt2Moved = false;
			if (cubEndPtMoved)
				cubEndPtMoved = false;
			
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
	gc.setLineWidth(5);
	
	Transform transform;

	// ----- cubic curve -----
	cubXPos = width/5;
	cubYPos = height/3;
	
	transform = new Transform(device);
	transform.translate(cubXPos, cubYPos);
	gc.setTransform(transform);
	transform.dispose();
	
	gc.setForeground(device.getSystemColor(SWT.COLOR_RED));
	gc.drawString(GraphicsExample.getResourceString("Cubic"), 25, -70, true);
	
	Path path = new Path(device);
	path.cubicTo(133 + cubDiffX1, -60 + cubDiffY1, 266 + cubDiffX2, 60 + cubDiffY2, 400 + cubEndDiffX, 0 + cubEndDiffY);
	gc.drawPath(path);
	path.dispose();

	gc.setTransform(null);
	gc.setForeground(device.getSystemColor(SWT.COLOR_DARK_BLUE));
	gc.drawRectangle(cubHndl1.x + (int)cubXPos, cubHndl1.y + (int)cubYPos, cubHndl1.width, cubHndl1.height);
	gc.drawRectangle(cubHndl2.x + (int)cubXPos, cubHndl2.y + (int)cubYPos, cubHndl2.width, cubHndl2.height);
	gc.drawRectangle(cubEndHndl.x + (int)cubXPos, cubEndHndl.y + (int)cubYPos, cubEndHndl.width, cubEndHndl.height);
	
	// ----- quadratic curve -----
	quadXPos = width/5;
	quadYPos = 2*height/3;
	
	transform = new Transform(device);
	transform.translate(quadXPos, quadYPos);
	gc.setTransform(transform);
	transform.dispose();
	
	gc.setForeground(device.getSystemColor(SWT.COLOR_GREEN));
	gc.drawString(GraphicsExample.getResourceString("Quadratic"), 0, -50, true);
	
	path = new Path(device);
	path.quadTo(200 + quadDiffX, 150 + quadDiffY, 400 + quadEndDiffX, 0 + quadEndDiffY);
	gc.drawPath(path);
	path.dispose();
	
	gc.setTransform(null);
	gc.setForeground(device.getSystemColor(SWT.COLOR_GRAY));
	gc.drawRectangle(quadHndl.x + (int)quadXPos, quadHndl.y + (int)quadYPos, quadHndl.width, quadHndl.height);
	gc.drawRectangle(quadEndHndl.x + (int)quadXPos, quadEndHndl.y + (int)quadYPos, quadEndHndl.width, quadEndHndl.height);

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

