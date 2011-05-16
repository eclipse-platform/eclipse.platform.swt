/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

/**
 * Instances of this class allow the user to select a color
 * from a predefined set of available colors.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ColorDialog extends Dialog {
	private static final int COLORSWATCH_SIZE_DEPTH4 = 40;
	private static final int COLORSWATCH_SIZE_DEPTH8 = 15;
	private static final int COLORSWATCH_SIZE_DEPTH16 = 10; 
	private static final int COLORSWATCH_BORDER = 1;	// border between each color pad

	private Shell shell;								// the dialog shell
	private Canvas colorsCanvas;
	private Label sampleLabel, selectionLabel;
	private Canvas sampleCanvas, selectionCanvas;
	private Button okButton, cancelButton;
	private int colorChooserWidth, colorChooserHeight;

	private boolean okSelected;
	private RGB rgb;
	private int colorSwatchExtent;						// the size of each color square
	private Color colorGrid[][];						// the colors displayed in the dialog
	
/**
 * Constructs a new instance of this class given only its parent.
 *
 * @param parent a composite control which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ColorDialog(Shell parent) {
	this (parent, SWT.APPLICATION_MODAL);
}
/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ColorDialog(Shell parent, int style) {
	super (parent, checkStyle (parent, style));
	checkSubclass ();
}
void createChildren() {
	Shell dialog = shell;
	GridLayout layout = new GridLayout (2, false);
	dialog.setLayout(layout);
	
	colorChooserWidth = colorSwatchExtent * colorGrid.length - 1;
	colorChooserHeight = colorSwatchExtent * colorGrid[0].length - 1;
	colorsCanvas = new Canvas(dialog, SWT.BORDER);
	GridData data = new GridData ();
	data.widthHint = colorChooserWidth;
	data.heightHint = colorChooserHeight;
	colorsCanvas.setLayoutData(data);

	Composite buttonsGroup = new Composite (dialog, SWT.NONE);
	buttonsGroup.setLayout(new GridLayout());
	buttonsGroup.setLayoutData(new GridData(GridData.BEGINNING));
	createOkCancel(buttonsGroup);

	Composite bottomGroup = new Composite (dialog,SWT.NONE);
	layout = new GridLayout(2, true);
	layout.marginHeight = 0;
	layout.marginWidth = 0;
	bottomGroup.setLayout(layout);
	bottomGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

	createSampleGroup(bottomGroup);
	createSelectionGroup(bottomGroup);
}
void createOkCancel(Composite parent) {
	okButton = new Button(parent, SWT.PUSH);
	okButton.setText(SWT.getMessage("SWT_OK"));
	shell.setDefaultButton(okButton);
	GridData data = new GridData(GridData.FILL_HORIZONTAL);
	okButton.setLayoutData(data);

	cancelButton = new Button(parent, SWT.PUSH);
	cancelButton.setText(SWT.getMessage("SWT_Cancel"));
	data = new GridData(GridData.FILL_HORIZONTAL);
	cancelButton.setLayoutData(data);
}
void createSampleGroup(Composite parent) {
	Group sampleGroup = new Group(parent, SWT.NULL);
	GridData data = new GridData(GridData.FILL_BOTH);
	data.grabExcessHorizontalSpace = true;
	sampleGroup.setLayout(new GridLayout());
	sampleGroup.setLayoutData(data);
	sampleGroup.setText(SWT.getMessage("SWT_Sample"));

	sampleLabel = new Label(sampleGroup, SWT.CENTER | SWT.BORDER);
	sampleLabel.setAlignment(SWT.CENTER);
	sampleLabel.setText(SWT.getMessage("SWT_A_Sample_Text"));
	data = new GridData(GridData.FILL_HORIZONTAL);
	sampleLabel.setLayoutData(data);

	sampleCanvas = new Canvas(sampleGroup, SWT.BORDER);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.heightHint = 15;
	sampleCanvas.setLayoutData(data);
}
void createSelectionGroup(Composite parent) {
	Group selectionGroup = new Group(parent, SWT.NULL);
	GridData data = new GridData(GridData.FILL_BOTH);
	data.grabExcessHorizontalSpace = true;
	selectionGroup.setLayout(new GridLayout());
	selectionGroup.setLayoutData(data);
	selectionGroup.setText(SWT.getMessage("SWT_Selection"));

	selectionLabel = new Label(selectionGroup, SWT.CENTER | SWT.BORDER);
	selectionLabel.setAlignment(SWT.CENTER);
	selectionLabel.setText(SWT.getMessage("SWT_Current_Selection"));
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.grabExcessHorizontalSpace = true;
	selectionLabel.setLayoutData(data);

	selectionCanvas = new Canvas(selectionGroup, SWT.BORDER);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.grabExcessHorizontalSpace = true;
	data.heightHint = 15;
	selectionCanvas.setLayoutData(data);
}
void disposeColors() {
	for (int row = 0; row < colorGrid.length; row++) {
		for (int column = 0; column < colorGrid[row].length; column++) {
			colorGrid[row][column].dispose();
		}
	}
}
void drawColor(int xIndex, int yIndex, Color color, GC gc) {
	int colorExtent = colorSwatchExtent - COLORSWATCH_BORDER;
	gc.setBackground(color);	
	gc.fillRectangle(
		xIndex * colorSwatchExtent,
		yIndex * colorSwatchExtent, 
		colorExtent, colorExtent);
}
/**
 * Returns the currently selected color in the receiver.
 *
 * @return the RGB value for the selected color, may be null
 *
 * @see PaletteData#getRGBs
 */
public RGB getRGB() {
	return rgb;
}
void handleEvents(Event event) {
	if (event.type == SWT.Paint) {
		paint(event);
	}
	else
	if (event.type == SWT.MouseDown) {
		mouseDown(event);
	}	
	else
	if (event.type == SWT.MouseMove) {
		mouseMove(event);
	}
	else
	if (event.type == SWT.Selection) {
		if (event.widget == okButton) {
			okSelected = true;
			shell.setVisible(false);
		}
		else
		if (event.widget == cancelButton) {
			okSelected = false;
			shell.setVisible(false);
		}
	}	
}
void initialize4BitColors() {
	Display display = shell.display;
	
	colorGrid[0][0] = new Color(display, 0, 0, 0);
	colorGrid[0][1] = new Color(display, 255, 255, 255);
	colorGrid[1][0] = new Color(display, 128, 128, 128);
	colorGrid[1][1] = new Color(display, 192, 192, 192);
	
	colorGrid[2][0] = new Color(display, 0, 0, 128);
	colorGrid[2][1] = new Color(display, 0, 0, 255);
	colorGrid[3][0] = new Color(display, 0, 128, 128);
	colorGrid[3][1] = new Color(display, 0, 255, 255);
	
	colorGrid[4][0] = new Color(display, 0, 128, 0);
	colorGrid[4][1] = new Color(display, 0, 255, 0);
	colorGrid[5][0] = new Color(display, 128, 128, 0);
	colorGrid[5][1] = new Color(display, 255, 255, 0);

	colorGrid[6][0] = new Color(display, 128, 0, 0);
	colorGrid[6][1] = new Color(display, 255, 0, 0);
	colorGrid[7][0] = new Color(display, 128, 0, 128);
	colorGrid[7][1] = new Color(display, 255, 0, 255);
}
void initialize8BitColors() {
	Display display = shell.display;
	int numRows = colorGrid[0].length;
	int iterationStep = 64;
	int row = 0, column = 0;
	int red, green, blue;
	// run the loops from 0 to 256 inclusive since this is easiest for the step
	// size, then adjust the 256 case to the proper 255 value when needed
	for (red = 0; red <= 256; red += iterationStep) {
		for (blue = 0; blue <= 256; blue += iterationStep) {
			for (green = 0; green <= 256; green += iterationStep) {
				if (row == numRows) {
					row = 0;
					column++;
				}
				if (red == 256) red = 255;
				if (blue == 256) blue = 255;
				if (green == 256) green = 255;				
				colorGrid[column][row++] = new Color(display, red, green, blue);
			}
		}
	}
}
void initialize16BitColors() {
	Display display = shell.display;
	int numRows = colorGrid[0].length;
	int iterationStep = 51;
	int row = 0, column = 0;
	int red, green, blue;

	for (red = 0; red <= 255; red += iterationStep) {
		for (blue = 0; blue <= 255; blue += iterationStep) {
			if (blue == iterationStep && column < 20) {		// evenly distribute 256 colors on 32 columns
				blue += iterationStep;
			}
			for (green = 0; green <= 255; green += iterationStep) {
				if (row == 2 || row == 5) {					// evenly distribute 256 colors on 8 rows
					colorGrid[column][row++] = new Color(display, red, green - iterationStep / 2, blue);
				}
				if (row == numRows) {
					row = 0;
					column++;
				}
				colorGrid[column][row++] = new Color(display, red, green, blue);
			}
		}
	}
}
void initializeWidgets() {
	Display display = shell.display;
	if (rgb != null) {
		Color selectionColor = new Color(display, rgb);
		selectionCanvas.setBackground(selectionColor);
		selectionLabel.setBackground(selectionColor);
		selectionColor.dispose();
	}
}
void installListeners() {
	Listener listener = new Listener() {
		public void handleEvent(Event event) {handleEvents(event);}
	};
	okButton.addListener(SWT.Selection, listener);
	cancelButton.addListener(SWT.Selection, listener);
	colorsCanvas.addListener(SWT.Paint, listener);
	colorsCanvas.addListener(SWT.MouseDown, listener);
	colorsCanvas.addListener(SWT.MouseMove, listener);
}
void mouseDown(Event event) {
	int swatchExtent = colorSwatchExtent;
	Color color = colorGrid[event.x / swatchExtent][event.y / swatchExtent];
	selectionCanvas.setBackground(color);
	selectionLabel.setBackground(color);	
}
void mouseMove(Event event) {
	int swatchExtent = colorSwatchExtent;
	// adjust for events received from moving over the Canvas' border
	if (!(0 <= event.x && event.x <= colorChooserWidth)) return;
	if (!(0 <= event.y && event.y <= colorChooserHeight)) return;
	int xgrid = Math.min(colorGrid.length - 1, event.x / swatchExtent);
	int ygrid = Math.min(colorGrid[0].length - 1, event.y / swatchExtent);
	Color color = colorGrid[xgrid][ygrid];
	sampleCanvas.setBackground(color);
	sampleLabel.setBackground(color);
}
/**
 * Makes the receiver visible and brings it to the front
 * of the display.
 *
 * @return the selected color, or null if the dialog was
 *         cancelled, no color was selected, or an error
 *         occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public RGB open() {
	shell = new Shell(parent, getStyle() | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
	Display display = shell.display;
	setColorDepth(display.getDepth());
	createChildren();
	installListeners();
	openModal();
	if (okSelected) {
		Color selectionColor = selectionCanvas.getBackground();
		rgb = new RGB(
			selectionColor.getRed(), 
			selectionColor.getGreen(), 
			selectionColor.getBlue());
	}
	disposeColors();	
	if (shell.isDisposed() == false) {
		shell.dispose();
	}	
	if (!okSelected) return null;
	return rgb;
}
/**
 * Open the receiver and set its size to the size calculated by 
 * the layout manager.
 */
void openDialog() {
	Shell dialog = shell;
		
	// Start everything off by setting the shell size to its computed size.
	Point pt = dialog.computeSize(-1, -1, false);
	
	// Ensure that the width of the shell fits the display.
	Display display = dialog.display;
	Rectangle displayRect = display.getBounds();
	int widthLimit = displayRect.width * 7 / 8;
	int heightLimit = displayRect.height * 7 / 8;
	if (pt.x > widthLimit) {
		pt = dialog.computeSize (widthLimit, -1, false);
	}

	/*
	 * If the parent is visible then center this dialog on it,
	 * otherwise center this dialog on the parent's monitor
	 */
	Rectangle parentBounds = null;
	if (parent.isVisible ()) {
		parentBounds = getParent ().getBounds ();
	} else {
		parentBounds = parent.getMonitor ().getBounds ();
	}
	int originX = (parentBounds.width - pt.x) / 2 + parentBounds.x;
	originX = Math.max (originX, 0);
	originX = Math.min (originX, widthLimit - pt.x);
	int originY = (parentBounds.height - pt.y) / 2 + parentBounds.y;
	originY = Math.max (originY, 0);
	originY = Math.min (originY, heightLimit - pt.y);
	dialog.setBounds (originX, originY, pt.x, pt.y);

	String title = getText ();
	if (title.length () == 0) title = SWT.getMessage ("SWT_ColorDialog_Title");
	shell.setText(title);
	
	dialog.open();
}
void openModal() {
	Display display = shell.display;
	initializeWidgets();
	openDialog();
	while (shell.isDisposed() == false && shell.getVisible() == true) {
		if (display.readAndDispatch() == false) {
			display.sleep();
		}
	}
}
void paint(Event event) {
	for (int column = 0; column < colorGrid.length; column++) {
		for (int row = 0; row < colorGrid[0].length; row++) {
			drawColor(column, row, colorGrid[column][row], event.gc);			
		}
	}
}
void setColorDepth(int bits) {
	if (bits == 4) {
		colorSwatchExtent = COLORSWATCH_SIZE_DEPTH4;
		colorGrid = new Color[8][2];
		initialize4BitColors();
		return;
	}
	if (bits == 8) {
		colorSwatchExtent = COLORSWATCH_SIZE_DEPTH8;
		colorGrid = new Color[25][5];
		initialize8BitColors();
		return;				
	}
	// default case: 16, 24 or 32 bits
	colorSwatchExtent = COLORSWATCH_SIZE_DEPTH16;
	colorGrid = new Color[32][8];
	initialize16BitColors();
}
/**
 * Sets the receiver's selected color to be the argument.
 *
 * @param rgb the new RGB value for the selected color, may be
 *        null to let the platform select a default when
 *        open() is called
 * @see PaletteData#getRGBs
 */
public void setRGB(RGB rgb) {
	this.rgb = rgb;
}
}
