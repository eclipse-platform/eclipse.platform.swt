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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This class extends the GraphicsTab class to create animated graphics.
 * */
public abstract class AnimatedGraphicsTab extends GraphicsTab {

	ToolBar toolBar;
	ToolItem playItem, pauseItem;
	Spinner timerSpinner;		// to input the speed of the animation 
	private boolean animate;	// flag that indicates whether or not to animate the graphic

	public AnimatedGraphicsTab(GraphicsExample example) {
		super(example);
		animate = true;
	}

	/**
	 * Sets the layout of the composite to RowLayout and creates the toolbar.
	 * 
	 * @see org.eclipse.swt.examples.graphics.GraphicsTab#createControlPanel(org.eclipse.swt.widgets.Composite)
	 */
	public void createControlPanel(Composite parent) {
		
		// setup layout
		RowLayout layout = new RowLayout();
		layout.wrap = true;
		layout.spacing = 8;
		parent.setLayout(layout);
		
		createToolBar(parent);
	}
	
	/**
	 * Creates the toolbar controls: play, pause and animation timer.
	 * 
	 * @param parent A composite
	 */
	void createToolBar(final Composite parent) {
		final Display display = parent.getDisplay();
				
		toolBar = new ToolBar(parent, SWT.FLAT);
		Listener toolBarListener = new Listener() {
			public void handleEvent(Event event) {
				switch (event.type) {
					case SWT.Selection: {
						if (event.widget == playItem) {
							animate = true;
							playItem.setEnabled(!animate);
							pauseItem.setEnabled(animate);
						} else if (event.widget == pauseItem) {
							animate = false;
							playItem.setEnabled(!animate);
							pauseItem.setEnabled(animate);
						}
					}
					break;
				}
			}
		};
		
		// play tool item
		playItem = new ToolItem(toolBar, SWT.PUSH);
		playItem.setText(GraphicsExample.getResourceString("Play")); //$NON-NLS-1$
		playItem.setImage(example.loadImage(display, "play.gif")); //$NON-NLS-1$
		playItem.addListener(SWT.Selection, toolBarListener);
		
		// pause tool item
		pauseItem = new ToolItem(toolBar, SWT.PUSH);
		pauseItem.setText(GraphicsExample.getResourceString("Pause")); //$NON-NLS-1$
		pauseItem.setImage(example.loadImage(display, "pause.gif")); //$NON-NLS-1$
		pauseItem.addListener(SWT.Selection, toolBarListener);
		
		// timer spinner
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		comp.setLayout(gridLayout);

		Label label = new Label(comp, SWT.CENTER);
		label.setText(GraphicsExample.getResourceString("Animation")); //$NON-NLS-1$
		timerSpinner = new Spinner(comp, SWT.BORDER | SWT.WRAP);
		timerSpinner.setMaximum(1000);
		
		playItem.setEnabled(false);
		animate = true;

		timerSpinner.setSelection(getInitialAnimationTime());
	}
	
	/**
	 *  Answer whether the receiver's drawing should be double bufferer.
	 */
	public boolean getDoubleBuffered() {
		return true;
	}
	
	/**
	 * Gets the initial animation time to be used by the tab. Animation time:
	 * number of milliseconds between the current drawing and the next (the time
	 * interval between calls to the next method). Should be overridden to
	 * return a value that is more appropriate for the tab.
	 */
	public int getInitialAnimationTime() {
		return 30;
	}
	
	/**
	 * Gets the animation time that is selected in the spinner. Animation time:
	 * number of milliseconds between the current drawing and the next (the time
	 * interval between calls to the next method). Should be overridden to
	 * return a value that is more appropriate for the tab.
	 */
	public int getAnimationTime() {
		return timerSpinner.getSelection();
	}
	
	/**
	 * Returns the true if the tab is currently animated; false otherwise.
	 */
	public boolean getAnimation() {
		return animate;
	}
	
	/**
	 * Causes the animation to stop or start.
	 * 
	 * @param flag
	 *            true starts the animation; false stops the animation.
	 */
	public void setAnimation(boolean flag) {
		animate = flag;
		playItem.setEnabled(!flag);
		pauseItem.setEnabled(flag);
	}

	/**
	 * Advance the animation.
	 */
	public abstract void next(int width, int height);
	
}
