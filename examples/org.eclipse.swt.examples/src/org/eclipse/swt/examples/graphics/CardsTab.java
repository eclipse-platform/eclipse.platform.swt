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

import org.eclipse.swt.graphics.*;

/**
 * This tab demonstrates various transformations, such as scaling, rotation, and
 * translation.
 */
public class CardsTab extends AnimatedGraphicsTab {
	
	float movClubX, movClubY, movDiamondX, movDiamondY, movHeart, movSpade;
	float inc_club = 5.0f;
	float inc_diamond = 5.0f;
	float inc_hearts = 5.0f;
	float inc_spade = 5.0f;
	float scale, scaleWidth;
	int rotationAngle = 0;
	float scaleArg = 0;
	float heartScale = 0.5f;
	float spadeScale = 0.333f;
	int clubWidth, diamondWidth, heartWidth, spadeHeight; 
	
	Image ace_club, ace_spade, ace_diamond, ace_hearts;

/**
 * Constructor
 * @param example A GraphicsExample
 */
public CardsTab(GraphicsExample example) {
	super(example);
}

public String getCategory() {
	return GraphicsExample.getResourceString("Transform"); //$NON-NLS-1$
}

public String getText() {
	return GraphicsExample.getResourceString("Cards"); //$NON-NLS-1$
}

public String getDescription() {
	return GraphicsExample.getResourceString("CardsDescription"); //$NON-NLS-1$
}

public void dispose() {
	if (ace_club != null) {
		ace_club.dispose();
		ace_club = null;
		ace_spade.dispose();
		ace_spade = null;
		ace_diamond.dispose();
		ace_diamond = null;
		ace_hearts.dispose();
		ace_hearts = null;
	}
}
public void next(int width, int height) {
	rotationAngle = (rotationAngle+10)%360;

	// scaleVal goes from 0 to 1, then 1 to 0, then starts over
	scaleArg = (float)((scaleArg == 1) ? scaleArg - 0.1 : scaleArg + 0.1);
	scale = (float)Math.cos(scaleArg);
	
	movClubX += inc_club;
	movDiamondX += inc_diamond;
	movHeart += inc_hearts;
	movSpade += inc_spade;
	
	scaleWidth = (float) ((movClubY/height)*0.35 + 0.15);
	movClubY = 2*height/5 * (float)Math.sin(0.01*movClubX - 90) + 2*height/5;
    movDiamondY = 2*height/5 * (float)Math.cos(0.01*movDiamondX) + 2*height/5;
    
    if (movClubX + clubWidth*scaleWidth > width) {
    	movClubX = width - clubWidth*scaleWidth;
        inc_club = -inc_club;
    }
    if (movClubX < 0) {
        movClubX = 0;
        inc_club = -inc_club;
    }
    if (movDiamondX + diamondWidth*scaleWidth > width) {
	    movDiamondX = width - diamondWidth*scaleWidth;
        inc_diamond = -inc_diamond;
    }
    if (movDiamondX < 0) {
	    movDiamondX = 0;
        inc_diamond = -inc_diamond;
    }
    if (movHeart + heartWidth*heartScale > width) {
    	movHeart = width - heartWidth*heartScale;
    	inc_hearts = -inc_hearts;
    }
    if (movHeart < 0) {
    	movHeart = 0;
    	inc_hearts = -inc_hearts;
    }
    if (movSpade + spadeHeight*spadeScale > height) {
    	movSpade = height - spadeHeight*spadeScale;
    	inc_spade = -inc_spade;
    }
    if (movSpade < 0) {
    	movSpade = 0;
    	inc_spade = -inc_spade;
    }
}

/* (non-Javadoc)
 * @see org.eclipse.swt.examples.graphics.GraphicsTab#paint(org.eclipse.swt.graphics.GC, int, int)
 */
public void paint(GC gc, int width, int height) {
	if (!example.checkAdvancedGraphics()) return;
	Device device = gc.getDevice();
	
	if (ace_club == null) {
		ace_club = GraphicsExample.loadImage(device, GraphicsExample.class, "ace_club.jpg");
		ace_spade = GraphicsExample.loadImage(device, GraphicsExample.class, "ace_spade.jpg");
		ace_diamond = GraphicsExample.loadImage(device, GraphicsExample.class, "ace_diamond.jpg");
		ace_hearts = GraphicsExample.loadImage(device, GraphicsExample.class, "ace_hearts.jpg");
	}
	
	clubWidth = ace_club.getBounds().width;
	diamondWidth = ace_diamond.getBounds().width;
	heartWidth = ace_hearts.getBounds().width;
	spadeHeight = ace_spade.getBounds().height;

	Transform transform;
	
	// ace of clubs		
	transform = new Transform(device);
	transform.translate((int)movClubX, (int)movClubY);	
	transform.scale(scaleWidth, scaleWidth);
	
	// rotate on center of image
	Rectangle rect = ace_club.getBounds();
	transform.translate(rect.width/2, rect.height/2);
	transform.rotate(rotationAngle);
	transform.translate(-rect.width/2, -rect.height/2);

	gc.setTransform(transform);
	transform.dispose();
	gc.drawImage(ace_club, 0, 0);
	
	// ace of diamonds
	transform = new Transform(device);
	transform.translate((int)movDiamondX, (int)movDiamondY);
	transform.scale(scaleWidth, scaleWidth);
	gc.setTransform(transform);
	transform.dispose();
	gc.drawImage(ace_diamond, 0, 0);

	// ace of hearts
	transform = new Transform(device);
	transform.translate(movHeart, height/2);
	transform.scale(heartScale, 0.5f*scale);
	gc.setTransform(transform);
	transform.dispose();
	gc.drawImage(ace_hearts, 0, 0);
	
	// ace of spades
	transform = new Transform(device);
	transform.translate(movSpade, movSpade);
	transform.scale(0.5f*scale, spadeScale);
	gc.setTransform(transform);
	transform.dispose();
	gc.drawImage(ace_spade, 0, 0);
}
}

