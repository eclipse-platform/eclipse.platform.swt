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

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This dialog is used for prompting the user to select two colors for the
 * creation of a gradient.
 */
public class GradientDialog extends Dialog {

	Canvas canvas;

	Button colorButton1, colorButton2;		// color buttons

	Button okButton, cancelButton;

	Menu menu1, menu2;
	
	Color color1, color2;	// first and second color used in gradient
	int returnVal; 			// value to be returned by open(), set to SWT.OK 
							// if the ok button has been pressed		
	ArrayList resources;
			
	public GradientDialog(Shell parent) {		
		this (parent, SWT.PRIMARY_MODAL);
	}
	
	public GradientDialog(Shell parent, int style) {
		super(parent, style);
		color1 = color2 = null;
		returnVal = SWT.CANCEL;
		resources = new ArrayList();
	}
	
	/**
	 * Sets up the dialog and opens it.
	 * */
	public int open() {
		final Shell dialog = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE | getStyle());
		dialog.setText(GraphicsExample.getResourceString("Gradient")); //$NON-NLS-1$
				
		GridLayout gridLayout = new GridLayout();
	    gridLayout.numColumns = 2;
	    gridLayout.marginHeight = 10;
	    gridLayout.marginWidth = 10;
	    dialog.setLayout(gridLayout);
	    
	    // create the controls in the dialog
	    createDialogControls(dialog);
		
		dialog.addListener(SWT.Close, new Listener() {
			public void handleEvent(Event event) {
				for (int i = 0; i < resources.size(); i++) {
					Object obj = resources.get(i);
					if (obj != null && obj instanceof Resource) {
						((Resource) obj).dispose();
					}
				}
				dialog.dispose();
			}
		});	

		dialog.setDefaultButton (okButton);
		dialog.pack ();
		Rectangle rect = getParent().getMonitor().getBounds();
		Rectangle bounds = dialog.getBounds();
		dialog.setLocation(rect.x + (rect.width - bounds.width) / 2, rect.y + (rect.height - bounds.height) / 2);
		dialog.setMinimumSize(bounds.width, bounds.height);
		
		dialog.open ();
		
		Display display = getParent().getDisplay();
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		
		if (menu1 != null) {
			menu1.dispose();
			menu1 = null;
		}
		
		if (menu2 != null) {
			menu2.dispose();
			menu2 = null;
		}

		return returnVal;
	}
	
	/**
	 * Creates the controls of the dialog.
	 * */
	public void createDialogControls(final Shell parent) {
		final Display display = Display.getCurrent();
		
		// message
		Label message = new Label(parent, SWT.NONE); 
		message.setText(GraphicsExample.getResourceString("GradientDlgMsg"));
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.horizontalSpan = 2;
		message.setLayoutData(gridData);
		
		// default colors are white and black
		if (color1 == null || color2 == null) {
			color1 = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
			color2 = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		}			

		// canvas
		canvas = new Canvas(parent, SWT.NONE);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 200;
		gridData.heightHint = 100;
		canvas.setLayoutData(gridData);
		canvas.addListener (SWT.Paint, new Listener () {
			public void handleEvent (Event e) {
				Image preview = null;
				Point size = canvas.getSize();
				preview = GraphicsExample.createImage(display, color1, color2, size.x, size.y);
				if (preview != null) {
					e.gc.drawImage (preview, 0, 0);
				}
				preview.dispose();
			}
		});
		
		// composite used for both color buttons
		Composite colorButtonComp = new Composite(parent, SWT.NONE);
		
		// layout buttons
		RowLayout layout = new RowLayout();;
		layout.type = SWT.VERTICAL;
		layout.pack = false;
		colorButtonComp.setLayout(layout);

		// position composite
		gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
	    colorButtonComp.setLayoutData(gridData);
	    
		ColorMenu colorMenu = new ColorMenu();
		
		// color controls: first color
		colorButton1 = new Button(colorButtonComp, SWT.PUSH);
		colorButton1.setText(GraphicsExample.getResourceString("GradientDlgButton1"));
		Image img1 = GraphicsExample.createImage(Display.getCurrent(), color1);
		colorButton1.setImage(img1);
		resources.add(img1);
		menu1 = colorMenu.createMenu(parent.getParent(), new ColorListener() {
			public void setColor(GraphicsBackground gb) {
				color1 = gb.getBgColor1();
				colorButton1.setImage(gb.getBgImage());
				if (canvas != null) canvas.redraw();
			}
		});
		colorButton1.addListener(SWT.Selection, new Listener() { 
			public void handleEvent(Event event) {
				final Button button = (Button) event.widget;
				final Composite parent = button.getParent(); 
				Rectangle bounds = button.getBounds();
				Point point = parent.toDisplay(new Point(bounds.x, bounds.y));
				menu1.setLocation(point.x, point.y + bounds.height);
				menu1.setVisible(true);
			}
		});
		
		// color controls: second color 
		colorButton2 = new Button(colorButtonComp, SWT.PUSH);
		colorButton2.setText(GraphicsExample.getResourceString("GradientDlgButton2"));
		Image img2 = GraphicsExample.createImage(Display.getCurrent(), color2);
		colorButton2.setImage(img2);
		resources.add(img2);
		menu2 = colorMenu.createMenu(parent.getParent(), new ColorListener() {
			public void setColor(GraphicsBackground gb) {
				color2 = gb.getBgColor1();
				colorButton2.setImage(gb.getBgImage());
				if (canvas != null) canvas.redraw();
			}
		});
		colorButton2.addListener(SWT.Selection, new Listener() { 
			public void handleEvent(Event event) {
				final Button button = (Button) event.widget;
				final Composite parent = button.getParent(); 
				Rectangle bounds = button.getBounds();
				Point point = parent.toDisplay(new Point(bounds.x, bounds.y));
				menu2.setLocation(point.x, point.y + bounds.height);
				menu2.setVisible(true);
			}
		});
		
		// composite used for ok and cancel buttons
		Composite okCancelComp = new Composite(parent, SWT.NONE);

		// layout buttons
		RowLayout rowLayout = new RowLayout();
		rowLayout.pack = false;
		rowLayout.marginTop = 5;
		okCancelComp.setLayout(rowLayout);

		// position composite
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
	    gridData.horizontalSpan = 2;
	    okCancelComp.setLayoutData(gridData);
		
	    // OK button
		okButton = new Button (okCancelComp, SWT.PUSH);
		okButton.setText("&OK");
		okButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				returnVal = SWT.OK;
				parent.close();
			}
		});
		
		// cancel button
		cancelButton = new Button (okCancelComp, SWT.PUSH);
		cancelButton.setText("&Cancel");
		cancelButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				parent.close();
			}
		});
	}
	
	/**
	 * Returns the first color selected by the user.
	 * */
	public Color getFirstColor() {
		return color1;		
	}
	
	/**
	 * Sets the first color.
	 * @param color
	 */
	public void setFirstColor(Color color) {
		this.color1 = color;
	}
	
	/**
	 * Returns the second color selected by the user.
	 * */
	public Color getSecondColor() {
		return color2;
	}

	/**
	 * Sets the second color.
	 * @param color
	 */
	public void setSecondColor(Color color) {
		this.color2 = color;
	}
}
