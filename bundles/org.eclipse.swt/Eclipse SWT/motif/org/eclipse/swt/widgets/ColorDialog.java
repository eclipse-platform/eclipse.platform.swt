package org.eclipse.swt.widgets;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

/**
 * A color dialog allows the user to select a color
 * from all available colors in the current pallette
 */
public /*final*/ class ColorDialog extends Dialog {
	private static final int DEPTH_4 = 0;				// index for COLOR_SWATCH_EXTENTS
	private static final int DEPTH_8 = 1;				// index for COLOR_SWATCH_EXTENTS
	private static final int COLOR_SWATCH_EXTENTS[] = {40, 10};	// extents of the squares drawn to display a 
																// color out of 4 bit and 8 bit color depth
	private static final int COLOR_SWATCH_BORDER = 1;	// border between each color pad
	
	private Shell shell;								// the dialog shell
	private Canvas colorsCanvas;
	private Label sampleLabel;
	private Canvas sampleCanvas;
	private Label selectionLabel;
	private Canvas selectionCanvas;
	private Button ok;
	private Button cancel;

	private boolean okSelected;							// true if the dialog was hidden 
														// because the ok button was selected
	private RGB dialogResult;													
	private int colorDepth;								// color depth of the display
	private int colorSwatchExtent;						// the size of on square used 
														// to display one color
	private Color colorGrid[][];						// the colors displayed in the dialog
	
public ColorDialog(Shell parent) {
	this(parent, SWT.NULL);
}
public ColorDialog(Shell parent, int style) {
	super(parent, style | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
}

void createChildren() {
	Shell dialog = getDialogShell();
	GridData gridData = new GridData();
	GridLayout layout = new GridLayout();
	final int ButtonHeight = 30;
	final int ColorChooserWidth = COLOR_SWATCH_EXTENTS[DEPTH_8] * 32 - COLOR_SWATCH_BORDER;	// extent of one color field multiplied with number of fields in a row
	final int ColorChooserHeight = COLOR_SWATCH_EXTENTS[DEPTH_8] * 8 - COLOR_SWATCH_BORDER;	// extent of one color field multiplied with number of rows

	layout.numColumns = 3;
	layout.marginWidth = 15;
	layout.marginHeight = 15;
	layout.horizontalSpacing = 10;
	layout.verticalSpacing = 2;
	dialog.setLayout(layout);

	// row one
	colorsCanvas = new Canvas(dialog, SWT.BORDER);	
	gridData.widthHint = ColorChooserWidth;
	gridData.heightHint = ColorChooserHeight;	
	gridData.verticalSpan = 2;
	gridData.horizontalSpan = 2;	
	colorsCanvas.setLayoutData(gridData);

	// create ok and cancel buttons (row two and three)
	createOkCancel();

	// row three - empty row
	Label fillLabel = new Label(dialog, SWT.NULL);
	gridData = new GridData();	
	gridData.heightHint = 5;
	gridData.horizontalSpan = layout.numColumns;
	fillLabel.setLayoutData(gridData);

	// row four - setup group box with sample text and canvas
	createSampleGroup(ColorChooserWidth);
	createSelectionGroup();	
}
void createSampleGroup(int colorChooserWidth) {
	Shell dialog = getDialogShell();
	Group sampleGroup = new Group(dialog, SWT.NULL);
	GridData gridData = new GridData();
	GridLayout layout = new GridLayout();
	int sampleGroupWidth;

	sampleGroup.setText("Sample");
	gridData.horizontalAlignment = GridData.FILL;	
	sampleGroup.setLayoutData(gridData);
	sampleGroup.setLayout(layout);
	sampleGroupWidth = (colorChooserWidth - layout.marginWidth * 4 - layout.horizontalSpacing * 4) / 2;
		
	sampleLabel = new Label(sampleGroup, SWT.CENTER | SWT.BORDER);
	sampleLabel.setAlignment(SWT.CENTER);
	sampleLabel.setText("A Sample Text");
	gridData = new GridData();
	gridData.grabExcessHorizontalSpace = true;
	gridData.widthHint = sampleGroupWidth;
	sampleLabel.setLayoutData(gridData);

	sampleCanvas = new Canvas(sampleGroup, SWT.BORDER);
	gridData = new GridData();
	gridData.grabExcessHorizontalSpace = true;
	gridData.heightHint = 15;
	gridData.widthHint = sampleGroupWidth;
	sampleCanvas.setLayoutData(gridData);
}
void createSelectionGroup() {
	Shell dialog = getDialogShell();
	Group selectionGroup = new Group(dialog, SWT.NULL);
	GridData gridData = new GridData();
	GridLayout layout = new GridLayout();

	selectionGroup.setText("Selection");
	gridData.horizontalAlignment = GridData.FILL;	
	selectionGroup.setLayoutData(gridData);
	selectionGroup.setLayout(layout);
	
	selectionLabel = new Label(selectionGroup, SWT.CENTER | SWT.BORDER);
	selectionLabel.setAlignment(SWT.CENTER);
	selectionLabel.setText("Current Selection");
	gridData = new GridData();
	gridData.grabExcessHorizontalSpace = true;
	gridData.horizontalAlignment = GridData.FILL;
	selectionLabel.setLayoutData(gridData);

	selectionCanvas = new Canvas(selectionGroup, SWT.BORDER);
	gridData = new GridData();
	gridData.grabExcessHorizontalSpace = true;
	gridData.horizontalAlignment = GridData.FILL;
	gridData.heightHint = 15;
	selectionCanvas.setLayoutData(gridData);
}
void disposeColors() {
	for (int row = 0; row < colorGrid.length; row++) {
		for (int column = 0; column < colorGrid[row].length; column++) {
			colorGrid[row][column].dispose();
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (7/6/99 5:07:09 PM)
 */
void drawColor(int xIndex, int yIndex, Color color, GC gc) {
	int colorSwatchExtent = getColorSwatchExtent();
	int colorExtent = colorSwatchExtent - COLOR_SWATCH_BORDER;

	gc.setBackground(color);	
	gc.fillRectangle(
		xIndex * colorSwatchExtent, yIndex * colorSwatchExtent, 
		colorExtent, colorExtent);
}
/**
 * Insert the method's description here.
 * Creation date: (7/6/99 5:07:09 PM)
 */
Canvas getColorCanvas() {
	return colorsCanvas;
}
/**
 * Insert the method's description here.
 * Creation date: (7/6/99 5:07:09 PM)
 */
int getColorDepth() {
	return colorDepth;
}
/**
 * Insert the method's description here.
 * Creation date: (7/6/99 5:07:09 PM)
 */
Color [][] getColorGrid() {
	return colorGrid;
}
/**
 * Insert the method's description here.
 * Creation date: (7/6/99 5:07:09 PM)
 */
int getColorSwatchExtent() {
	return colorSwatchExtent;
}
public RGB getRGB() {
	return dialogResult;
}
/**
 * Insert the method's description here.
 * Creation date: (7/6/99 5:07:09 PM)
 */
Canvas getSampleCanvas() {
	return sampleCanvas;
}
/**
 * Insert the method's description here.
 * Creation date: (7/6/99 5:07:09 PM)
 */
Label getSampleText() {
	return sampleLabel;
}
/**
 * Insert the method's description here.
 * Creation date: (7/6/99 5:07:09 PM)
 */
Canvas getSelectionCanvas() {
	return selectionCanvas;
}
/**
 * Insert the method's description here.
 * Creation date: (7/6/99 5:07:09 PM)
 */
Label getSelectionText() {
	return selectionLabel;
}
void handleEvents(Event event) {
	Color selectionColor;
	
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
		if (event.widget == getOKButton()) {
			setOkSelected(true);
			getDialogShell().setVisible(false);
		}
		else
		if (event.widget == getCancelButton()) {
			setOkSelected(false);		
			getDialogShell().setVisible(false);
		}
	}	
}
/**
 * Insert the method's description here.
 * Creation date: (7/6/99 5:07:09 PM)
 */
void initialize4BitColors() {
	Display display = getDialogShell().getDisplay();
	
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
/**
 * Insert the method's description here.
 * Creation date: (7/6/99 5:07:09 PM)
 */
void initialize8BitColors() {
	Display display = getDialogShell().getDisplay();	
	int numColumns = colorGrid.length;
	int numRows = colorGrid[0].length;
	int iterationStep = 51;
	int row = 0;
	int column = 0;
	int red;
	int green;
	int blue;
	
	for (red = 0; red <= 255; red += iterationStep) {
		for (blue = 0; blue <= 255; blue += iterationStep) {
			if (blue == iterationStep && column < 20) {	// hack to evenly distribute 256 colors on 32 columns
				blue += iterationStep;
			}
			for (green = 0; green <= 255; green += iterationStep) {
				if (row == 2 || row == 5) {				// hack to evenly distribute 256 colors on 8 rows
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
	Display display = getDialogShell().getDisplay();
	Color selectionColor;
	RGB rgb = getRGB();
	
	if (rgb != null) {
		selectionColor = new Color(display, rgb);
		getSelectionCanvas().setBackground(selectionColor);
		getSelectionText().setBackground(selectionColor);
		selectionColor.dispose();
	}
	setColorDepth(display.getDepth());
}
void installListeners() {
	Canvas colorCanvas = getColorCanvas();
	Listener listener = new Listener() {
		public void handleEvent(Event event) {handleEvents(event);}
	};
	
	getOKButton().addListener(SWT.Selection, listener);
	getCancelButton().addListener(SWT.Selection, listener);
	colorCanvas.addListener(SWT.Paint, listener);
	colorCanvas.addListener(SWT.MouseDown, listener);
	colorCanvas.addListener(SWT.MouseMove, listener);
}
/**
 * Insert the method's description here.
 * Creation date: (7/19/99 7:13:21 PM)
 */
void mouseDown(Event event) {
	int swatchExtent = getColorSwatchExtent();
	Color colorGrid[][] = getColorGrid();
	Color color = colorGrid[event.x / swatchExtent][event.y / swatchExtent];

	getSelectionCanvas().setBackground(color);
	getSelectionText().setBackground(color);	
}
/**
 * Insert the method's description here.
 * Creation date: (7/19/99 7:13:21 PM)
 */
void mouseMove(Event event) {
	int swatchExtent = getColorSwatchExtent();
	Color colorGrid[][] = getColorGrid();
	Color color = colorGrid[event.x / swatchExtent][event.y / swatchExtent];

	getSampleCanvas().setBackground(color);
	getSampleText().setBackground(color);
}
public RGB open() {
	Color selectionColor;
	RGB dialogResult = null;
	Shell dialog = new Shell(getParent(), getStyle() | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
	
	setDialogShell(dialog);
	createChildren();
	installListeners();
	openModal();
	if (isOkSelected() == true) {
		selectionColor = getSelectionCanvas().getBackground();
		dialogResult = new RGB(
			selectionColor.getRed(), 
			selectionColor.getGreen(), 
			selectionColor.getBlue());
		setRGB(dialogResult);
	}
	disposeColors();	
	// Fix for 1G5NLY7
	if (dialog.isDisposed() == false) {
		dialog.dispose();
	}	
	return dialogResult;
}
/**
 * Insert the method's description here.
 * Creation date: (7/6/99 5:07:09 PM)
 */
void paint(Event event) {
	Color colorGrid[][] = getColorGrid();
	
	for (int column = 0; column < colorGrid.length; column++) {
		for (int row = 0; row < colorGrid[0].length; row++) {
			drawColor(column, row, colorGrid[column][row], event.gc);			
		}
	}
}
/**
 * Insert the method's description here.
 * Creation date: (7/6/99 5:07:09 PM)
 */
void setColorDepth(int bits) {
	colorDepth = bits;
	if (bits == 4) {
		colorSwatchExtent = COLOR_SWATCH_EXTENTS[DEPTH_4];
		colorGrid = new Color[8][2];
		initialize4BitColors();
	}
	else {
		colorSwatchExtent = COLOR_SWATCH_EXTENTS[DEPTH_8];
		colorGrid = new Color[32][8];
		initialize8BitColors();				
	}
}
/**
 *
 */
public void setRGB(RGB rgb) {
	dialogResult = rgb;
}
/**
 * Create the widgets of the dialog.
 */
void createOkCancel() {
	Shell dialog = getDialogShell();
	GridData gridData;
	
	ok = new Button(dialog, SWT.PUSH);
	ok.setText("OK");
	dialog.setDefaultButton(ok);	
	gridData = new GridData();
	gridData.horizontalAlignment = GridData.FILL;
	gridData.widthHint = 70;
	ok.setLayoutData(gridData);

	cancel = new Button(dialog, SWT.PUSH);
	cancel.setText("Cancel");
	gridData = new GridData();
	gridData.horizontalAlignment = GridData.FILL;
	gridData.verticalAlignment = GridData.BEGINNING;		
	cancel.setLayoutData(gridData);
}
/**
 * Answer the cancel button
 */
Button getCancelButton() {
	return cancel;
}

/**
 * Answer the dialog shell.
 */
Shell getDialogShell() {
	return shell;
}
/**
 * Answer the ok button.
 */
Button getOKButton() {
	return ok;
}


/**
 * Insert the method's description here.
 * Creation date: (08/05/99 12:34:43)
 * @return boolean
 */
boolean isOkSelected() {
	return okSelected;
}
/**
 * Open the receiver and set its size to the size calculated by 
 * the layout manager.
 */
void openDialog() {
	Shell dialog = getDialogShell();
	Point pt;
	Rectangle displayRect;
	int widthLimit;
		
	// Start everything off by setting the shell size to its computed size.
	pt = dialog.computeSize(-1, -1, false);
	
	// Ensure that the width of the shell fits the display.
	displayRect = dialog.getDisplay().getBounds();
	widthLimit = displayRect.width * 7 / 8;
	if (pt.x > widthLimit) {
		pt = dialog.computeSize (widthLimit, -1, false);
	}
	dialog.setBounds (0, 0, pt.x, pt.y);
	dialog.setText(getText());
	// Open the window.
	dialog.open();
}
/**
 * Initialize the widgets of the receiver, open the dialog
 * and block the method until the dialog is closed by the user.
 */
void openModal() {
	Shell dialog = getDialogShell();
	Display display = dialog.getDisplay();

	initializeWidgets();
	setRGB(null);
	openDialog();
	while (dialog.isDisposed() == false && dialog.getVisible() == true) {
		if (display.readAndDispatch() == false) {
			display.sleep();
		}
	}
}


/**
 * Insert the method's description here.
 * Creation date: (08/05/99 12:34:43)
 * @param newOkSelected boolean
 */
void setOkSelected(boolean newOkSelected) {
	okSelected = newOkSelected;
}
/**
 * Set the shell used as the dialog window.
 */
void setDialogShell(Shell shell) {
	this.shell = shell;
}



}
