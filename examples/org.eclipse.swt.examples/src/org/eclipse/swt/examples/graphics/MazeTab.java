/*******************************************************************************
 * Copyright (c) 2006, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

/**
 * This tab shows three circles, each following a different path in a maze.
 * Only one of the three circles follows the correct path.
 */
public class MazeTab extends AnimatedGraphicsTab {

	int nextIndex, nextIndex2, nextIndex3;
	int xcoord, ycoord, xcoord2, ycoord2, xcoord3, ycoord3;
	List<Integer> nextCoord, nextCoord2, nextCoord3;
	boolean isDone, isDone2, isDone3;
	Image image;

/**
 * Constructor
 * @param example A GraphicsExample
 */
public MazeTab(GraphicsExample example) {
	super(example);

	// correct path
	nextCoord = new ArrayList<>();
	nextCoord.addAll(moveDown(20, -50, 20, 110, 10));
	nextCoord.addAll(moveRight(30, 110, 130, 110, 10));
	nextCoord.addAll(moveUp(135, 100, 135, 15, 10));
	nextCoord.addAll(moveRight(140, 15, 210, 15, 10));
	nextCoord.addAll(moveDown(210, 25, 210, 75, 10));
	nextCoord.addAll(moveRight(220, 75, 320, 75, 10));
	nextCoord.addAll(moveUp(320, 65, 320, 55, 10));
	nextCoord.addAll(moveRight(330, 50, 475, 50, 10));
	nextCoord.addAll(moveDown(475, 60, 475, 225, 10));
	nextCoord.addAll(moveLeft(465, 225, 200, 225, 10));
	nextCoord.addAll(moveUp(200, 215, 200, 180, 10));
	nextCoord.addAll(moveLeft(190, 180, 120, 180, 10));
	nextCoord.addAll(moveDown(120, 190, 120, 320, 10));
	nextCoord.addAll(moveRight(130, 320, 475, 320, 10));
	nextCoord.addAll(moveDown(475, 330, 475, 435, 10));
	nextCoord.addAll(moveLeft(465, 435, 20, 435, 10));
	nextCoord.addAll(moveDown(20, 445, 20, 495, 10));

	nextIndex = 0;
	xcoord = nextCoord.get(nextIndex).intValue();
	ycoord = nextCoord.get(nextIndex+1).intValue();

	// wrong path 1
	nextCoord2 = new ArrayList<>();
	nextCoord2.addAll(moveDown(20, -25, 20, 110, 10));
	nextCoord2.addAll(moveRight(30, 110, 130, 110, 10));
	nextCoord2.addAll(moveUp(135, 100, 135, 15, 10));
	nextCoord2.addAll(moveRight(140, 15, 520, 15, 10));
	nextCoord2.addAll(moveDown(525, 15, 525, 480, 10));
	nextCoord2.addAll(moveLeft(515, 480, 70, 480, 10));

	nextIndex2 = 0;
	xcoord2 = nextCoord2.get(nextIndex2).intValue();
	ycoord2 = nextCoord2.get(nextIndex2+1).intValue();

	// wrong path 2
	nextCoord3 = new ArrayList<>();
	nextCoord3.addAll(moveDown(20, 0, 20, 110, 10));
	nextCoord3.addAll(moveRight(30, 110, 130, 110, 10));
	nextCoord3.addAll(moveUp(135, 100, 135, 15, 10));
	nextCoord3.addAll(moveRight(140, 15, 210, 15, 10));
	nextCoord3.addAll(moveDown(210, 25, 210, 75, 10));
	nextCoord3.addAll(moveRight(220, 75, 320, 75, 10));
	nextCoord3.addAll(moveUp(320, 65, 320, 55, 10));
	nextCoord3.addAll(moveRight(330, 50, 475, 50, 10));
	nextCoord3.addAll(moveDown(475, 60, 475, 225, 10));
	nextCoord3.addAll(moveLeft(465, 225, 425, 225, 10));
	nextCoord3.addAll(moveUp(420, 225, 420, 150, 10));
	nextCoord3.addAll(moveLeft(420, 145, 70, 145, 10));
	nextCoord3.addAll(moveDown(70, 150, 70, 320, 10));

	nextIndex3 = 0;
	xcoord3 = nextCoord3.get(nextIndex3).intValue();
	ycoord3 = nextCoord3.get(nextIndex3+1).intValue();

	isDone = isDone2 = isDone3 = false;
}

@Override
public String getCategory() {
	return GraphicsExample.getResourceString("Misc"); //$NON-NLS-1$
}

@Override
public String getText() {
	return GraphicsExample.getResourceString("Maze"); //$NON-NLS-1$
}

@Override
public String getDescription() {
	return GraphicsExample.getResourceString("MazeDescription"); //$NON-NLS-1$
}

@Override
public int getInitialAnimationTime() {
	return 50;
}

@Override
public void dispose() {
	if (image != null) {
		image.dispose();
		image = null;
	}
}

@Override
public void createControlPanel(Composite parent) {
	super.createControlPanel(parent);

	// add selection listener to reset nextNumber after
	// the sequence has completed
	playItem.addListener(SWT.Selection, event -> {
		if (isDone){
			nextIndex = nextIndex2 = nextIndex3 = 0;
			isDone = isDone2 = isDone3 = false;
		}
	});
}

@Override
public void next(int width, int height) {

	if (nextIndex+2 < nextCoord.size()) {
		nextIndex = (nextIndex+2)%nextCoord.size();
		xcoord = nextCoord.get(nextIndex).intValue();
		ycoord = nextCoord.get(nextIndex+1).intValue();
	} else {
		// stop animation
		setAnimation(false);
		isDone = true;
	}

	if (nextIndex2+2 < nextCoord2.size()) {
		nextIndex2 = (nextIndex2+2)%nextCoord2.size();
		xcoord2 = nextCoord2.get(nextIndex2).intValue();
		ycoord2 = nextCoord2.get(nextIndex2+1).intValue();
	} else {
		isDone2 = true;
	}

	if (nextIndex3+2 < nextCoord3.size()) {
		nextIndex3 = (nextIndex3+2)%nextCoord3.size();
		xcoord3 = nextCoord3.get(nextIndex3).intValue();
		ycoord3 = nextCoord3.get(nextIndex3+1).intValue();
	} else {
		isDone3 = true;
	}
}

@Override
public void paint(GC gc, int width, int height) {
	Device device = gc.getDevice();

	if (image == null) {
		image = example.loadImage(device, "maze.bmp");
	}
	// draw maze
	Rectangle bounds = image.getBounds();
	int x = (width - bounds.width) / 2;
	int y = (height - bounds.height) / 2;
	gc.drawImage(image, x, y);

	// draw correct oval
	gc.setBackground(device.getSystemColor(SWT.COLOR_RED));
	gc.fillOval(x + xcoord, y + ycoord, 16, 16);
	gc.drawOval(x + xcoord, y + ycoord, 15, 15);

	// draw wrong oval 1
	gc.setBackground(device.getSystemColor(SWT.COLOR_BLUE));
	gc.fillOval(x + xcoord2, y + ycoord2, 16, 16);
	gc.drawOval(x + xcoord2, y + ycoord2, 15, 15);

	// draw wrong oval 2
	gc.setBackground(device.getSystemColor(SWT.COLOR_GREEN));
	gc.fillOval(x + xcoord3, y + ycoord3, 16, 16);
	gc.drawOval(x + xcoord3, y + ycoord3, 15, 15);

	if (isDone2) {
		Image helpImg = example.loadImage(device, "help.gif");
		gc.drawImage(helpImg, x + xcoord2 + 16, y + ycoord2 - 16);
		helpImg.dispose();
	}

	if (isDone3) {
		Image helpImg = example.loadImage(device, "help.gif");
		gc.drawImage(helpImg, x + xcoord3 + 16, y + ycoord3 - 16);
		helpImg.dispose();
	}
}

/**
 * Returns a list of coordinates moving in a "left-moving" fashion from the start
 * point to the end point inclusively.
 *
 * @param x1
 *            X component of the start point
 * @param y1
 *            Y component of the start point
 * @param x2
 *            X component of the end point
 * @param y2
 *            Y component of the end point
 * @param stepsize
 *            The number of pixels that separate each coordinate
 */
private List<Integer> moveLeft(int x1, int y1, int x2, int y2, int stepsize) {
	List<Integer> coords = new ArrayList<>();
	coords.add(Integer.valueOf(x1));
	coords.add(Integer.valueOf(y1));
	while(x1 - stepsize > x2) {
		x1 = x1 - stepsize;
		coords.add(Integer.valueOf(x1));
		coords.add(Integer.valueOf(y1));
	}
	coords.add(Integer.valueOf(x2));
	coords.add(Integer.valueOf(y2));
	return coords;
}

/**
 * Returns a list of coordinates moving in a "right-moving" fashion from the start
 * point to the end point inclusively.
 *
 * @param x1
 *            X component of the start point
 * @param y1
 *            Y component of the start point
 * @param x2
 *            X component of the end point
 * @param y2
 *            Y component of the end point
 * @param stepsize
 *            The number of pixels that separate each coordinate
 */
private List<Integer> moveRight(int x1, int y1, int x2, int y2, int stepsize) {
	List<Integer> coords = new ArrayList<>();
	coords.add(Integer.valueOf(x1));
	coords.add(Integer.valueOf(y1));
	while(x1 + stepsize < x2) {
		x1 = x1 + stepsize;
		coords.add(Integer.valueOf(x1));
		coords.add(Integer.valueOf(y1));
	}
	coords.add(Integer.valueOf(x2));
	coords.add(Integer.valueOf(y2));
	return coords;
}

/**
 * Returns a list of coordinates moving in an upward fashion from the start
 * point to the end point inclusively.
 *
 * @param x1
 *            X component of the start point
 * @param y1
 *            Y component of the start point
 * @param x2
 *            X component of the end point
 * @param y2
 *            Y component of the end point
 * @param stepsize
 *            The number of pixels that separate each coordinate
 */
private List<Integer> moveUp(int x1, int y1, int x2, int y2, int stepsize) {
	List<Integer> coords = new ArrayList<>();
	coords.add(Integer.valueOf(x1));
	coords.add(Integer.valueOf(y1));
	while(y1 - stepsize > y2) {
		y1 = y1 - stepsize;
		coords.add(Integer.valueOf(x1));
		coords.add(Integer.valueOf(y1));
	}
	coords.add(Integer.valueOf(x2));
	coords.add(Integer.valueOf(y2));
	return coords;
}

/**
 * Returns a list of coordinates moving in a downward fashion from the start
 * point to the end point inclusively.
 *
 * @param x1
 *            X component of the start point
 * @param y1
 *            Y component of the start point
 * @param x2
 *            X component of the end point
 * @param y2
 *            Y component of the end point
 * @param stepsize
 *            The number of pixels that separate each coordinate
 */
private List<Integer> moveDown(int x1, int y1, int x2, int y2, int stepsize) {
	List<Integer> coords = new ArrayList<>();
	coords.add(Integer.valueOf(x1));
	coords.add(Integer.valueOf(y1));
	while(y1 + stepsize < y2) {
		y1 = y1 + stepsize;
		coords.add(Integer.valueOf(x1));
		coords.add(Integer.valueOf(y1));
	}
	coords.add(Integer.valueOf(x2));
	coords.add(Integer.valueOf(y2));
	return coords;
}

}
