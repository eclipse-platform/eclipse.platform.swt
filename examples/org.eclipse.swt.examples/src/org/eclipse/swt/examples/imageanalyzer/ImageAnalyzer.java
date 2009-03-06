/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.imageanalyzer;


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.printing.*;
import org.eclipse.swt.custom.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.text.MessageFormat;

public class ImageAnalyzer {
	static ResourceBundle bundle = ResourceBundle.getBundle("examples_images");
	Display display;
	Shell shell;
	Canvas imageCanvas, paletteCanvas;
	Label typeLabel, sizeLabel, depthLabel, transparentPixelLabel,
		timeToLoadLabel, screenSizeLabel, backgroundPixelLabel,
		locationLabel, disposalMethodLabel, delayTimeLabel,
		repeatCountLabel, paletteLabel, dataLabel, statusLabel;
	Combo backgroundCombo, scaleXCombo, scaleYCombo, alphaCombo;
	Button incrementalCheck, transparentCheck, maskCheck, backgroundCheck;
	Button previousButton, nextButton, animateButton;
	StyledText dataText;
	Sash sash;
	Color whiteColor, blackColor, redColor, greenColor, blueColor, canvasBackground;
	Font fixedWidthFont;
	Cursor crossCursor;
	GC imageCanvasGC;
	PrinterData printerData;
	
	int paletteWidth = 140; // recalculated and used as a width hint
	int ix = 0, iy = 0, py = 0; // used to scroll the image and palette
	float xscale = 1, yscale = 1; // used to scale the image
	int alpha = 255; // used to modify the alpha value of the image
	boolean incremental = false; // used to incrementally display an image
	boolean transparent = true; // used to display an image with transparency
	boolean showMask = false; // used to display an icon mask or transparent image mask
	boolean showBackground = false; // used to display the background of an animated image
	boolean animate = false; // used to animate a multi-image file
	Thread animateThread; // draws animated images
	Thread incrementalThread; // draws incremental images
	String lastPath; // used to seed the file dialog
	String currentName; // the current image file or URL name
	String fileName; // the current image file
	ImageLoader loader; // the loader for the current image file
	ImageData[] imageDataArray; // all image data read from the current file
	int imageDataIndex; // the index of the current image data
	ImageData imageData; // the currently-displayed image data
	Image image; // the currently-displayed image
	Vector incrementalEvents; // incremental image events
	long loadTime = 0; // the time it took to load the current image
	
	static final int INDEX_DIGITS = 4;
	static final int ALPHA_CHARS = 5;
	static final int ALPHA_CONSTANT = 0;
	static final int ALPHA_X = 1;
	static final int ALPHA_Y = 2;
	static final String[] OPEN_FILTER_EXTENSIONS = new String[] {
			"*.bmp; *.gif; *.ico; *.jfif; *.jpeg; *.jpg; *.png; *.tif; *.tiff",
			"*.bmp", "*.gif", "*.ico", "*.jpg; *.jpeg; *.jfif", "*.png", "*.tif; *.tiff" };
	static final String[] OPEN_FILTER_NAMES = new String[] {
			bundle.getString("All_images") + " (bmp, gif, ico, jfif, jpeg, jpg, png, tif, tiff)",
			"BMP (*.bmp)", "GIF (*.gif)", "ICO (*.ico)", "JPEG (*.jpg, *.jpeg, *.jfif)",
			"PNG (*.png)", "TIFF (*.tif, *.tiff)" };
	static final String[] SAVE_FILTER_EXTENSIONS = new String[] {
			"*.bmp", "*.bmp", "*.gif", "*.ico", "*.jpg", "*.png", "*.tif", "*.bmp" };
	static final String[] SAVE_FILTER_NAMES = new String[] {
			"Uncompressed BMP (*.bmp)", "RLE Compressed BMP (*.bmp)", "GIF (*.gif)",
			"ICO (*.ico)", "JPEG (*.jpg)", "PNG (*.png)",
			"TIFF (*.tif)", "OS/2 BMP (*.bmp)" };

	class TextPrompter extends Dialog {
		String message = "";
		String result = null;
		Shell dialog;
		Text text;
		public TextPrompter (Shell parent, int style) {
			super (parent, style);
		}
		public TextPrompter (Shell parent) {
			this (parent, SWT.APPLICATION_MODAL);
		}
		public String getMessage () {
			return message;
		}
		public void setMessage (String string) {
			message = string;
		}
		public String open () {
			dialog = new Shell(getParent(), getStyle());
			dialog.setText(getText());
			dialog.setLayout(new GridLayout());
			Label label = new Label(dialog, SWT.NONE);
			label.setText(message);
			label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			text = new Text(dialog, SWT.SINGLE | SWT.BORDER);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.widthHint = 300;
			text.setLayoutData(data);
			Composite buttons = new Composite(dialog, SWT.NONE);
			GridLayout grid = new GridLayout();
			grid.numColumns = 2;
			buttons.setLayout(grid);
			buttons.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
			Button ok = new Button(buttons, SWT.PUSH);
			ok.setText(bundle.getString("OK"));
			data = new GridData();
			data.widthHint = 75;
			ok.setLayoutData(data);
			ok.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					result = text.getText();
					dialog.dispose();
				}
			});
			Button cancel = new Button(buttons, SWT.PUSH);
			cancel.setText(bundle.getString("Cancel"));
			data = new GridData();
			data.widthHint = 75;
			cancel.setLayoutData(data);
			cancel.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					dialog.dispose();
				}
			});
			dialog.setDefaultButton(ok);
			dialog.pack();
			dialog.open();
			while (!dialog.isDisposed()) {
				if (!display.readAndDispatch()) display.sleep();
			}
			return result;
		}
	}

	public static void main(String [] args) {
		Display display = new Display();
		ImageAnalyzer imageAnalyzer = new ImageAnalyzer();
		Shell shell = imageAnalyzer.open(display);
		
		while (!shell.isDisposed())
			if (!display.readAndDispatch()) display.sleep();
		display.dispose();
	}

	public Shell open(Display dpy) {
		// Create a window and set its title.
		this.display = dpy;
		shell = new Shell(display);
		shell.setText(bundle.getString("Image_analyzer"));
		
		// Hook resize and dispose listeners.
		shell.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent event) {
				resizeShell(event);
			}
		});
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				animate = false; // stop any animation in progress
				if (animateThread != null) {
					// wait for the thread to die before disposing the shell.
					while (animateThread.isAlive()) {
						if (!display.readAndDispatch()) display.sleep();
					}
				}
				e.doit = true;
			}
		});
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				// Clean up.
				if (image != null)
					image.dispose();
				whiteColor.dispose();
				blackColor.dispose();
				redColor.dispose();
				greenColor.dispose();
				blueColor.dispose();
				fixedWidthFont.dispose();
				crossCursor.dispose();
			}
		});

		// Create colors and fonts.
		whiteColor = new Color(display, 255, 255, 255);
		blackColor = new Color(display, 0, 0, 0);
		redColor = new Color(display, 255, 0, 0);
		greenColor = new Color(display, 0, 255, 0);
		blueColor = new Color(display, 0, 0, 255);
		fixedWidthFont = new Font(display, "courier", 10, 0);
		crossCursor = new Cursor(display, SWT.CURSOR_CROSS);
		
		// Add a menu bar and widgets.
		createMenuBar();
		createWidgets();
		shell.pack();
		
		// Create a GC for drawing, and hook the listener to dispose it.
		imageCanvasGC = new GC(imageCanvas);
		imageCanvas.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				imageCanvasGC.dispose();
			}
		});
		
		// Open the window
		shell.open();
		return shell;
	}

	void createWidgets() {
		// Add the widgets to the shell in a grid layout.
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.numColumns = 2;
		shell.setLayout(layout);

		// Add a composite to contain some control widgets across the top.
		Composite controls = new Composite(shell, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.marginTop = 5;
		rowLayout.marginBottom = 5;
		rowLayout.spacing = 8;
		controls.setLayout(rowLayout);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		controls.setLayoutData(gridData);
		
		// Combo to change the background.
		Group group = new Group(controls, SWT.NONE);
		group.setLayout(new RowLayout());
		group.setText(bundle.getString("Background"));
		backgroundCombo = new Combo(group, SWT.DROP_DOWN | SWT.READ_ONLY);
		backgroundCombo.setItems(new String[] {
			bundle.getString("None"),
			bundle.getString("White"),
			bundle.getString("Black"),
			bundle.getString("Red"),
			bundle.getString("Green"),
			bundle.getString("Blue")});
		backgroundCombo.select(backgroundCombo.indexOf(bundle.getString("White")));
		backgroundCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				changeBackground();
			}
		});
		
		// Combo to change the x scale.
		String[] values = {
			"0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1",
			"1.1", "1.2", "1.3", "1.4", "1.5", "1.6", "1.7", "1.8", "1.9", "2",
			"3", "4", "5", "6", "7", "8", "9", "10",};
		group = new Group(controls, SWT.NONE);
		group.setLayout(new RowLayout());
		group.setText(bundle.getString("X_scale"));
		scaleXCombo = new Combo(group, SWT.DROP_DOWN);
		for (int i = 0; i < values.length; i++) {
			scaleXCombo.add(values[i]);
		}
		scaleXCombo.select(scaleXCombo.indexOf("1"));
		scaleXCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scaleX();
			}
		});
		
		// Combo to change the y scale.
		group = new Group(controls, SWT.NONE);
		group.setLayout(new RowLayout());
		group.setText(bundle.getString("Y_scale"));
		scaleYCombo = new Combo(group, SWT.DROP_DOWN);
		for (int i = 0; i < values.length; i++) {
			scaleYCombo.add(values[i]);
		}
		scaleYCombo.select(scaleYCombo.indexOf("1"));
		scaleYCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scaleY();
			}
		});
		
		// Combo to change the alpha value.
		group = new Group(controls, SWT.NONE);
		group.setLayout(new RowLayout());
		group.setText(bundle.getString("Alpha_K"));
		alphaCombo = new Combo(group, SWT.DROP_DOWN | SWT.READ_ONLY);
		for (int i = 0; i <= 255; i += 5) {
			alphaCombo.add(String.valueOf(i));
		}
		alphaCombo.select(alphaCombo.indexOf("255"));
		alphaCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				alpha();
			}
		});
		
		// Check box to request incremental display.
		group = new Group(controls, SWT.NONE);
		group.setLayout(new RowLayout());
		group.setText(bundle.getString("Display"));
		incrementalCheck = new Button(group, SWT.CHECK);
		incrementalCheck.setText(bundle.getString("Incremental"));
		incrementalCheck.setSelection(incremental);
		incrementalCheck.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				incremental = ((Button)event.widget).getSelection();
			}
		});

		// Check box to request transparent display.
		transparentCheck = new Button(group, SWT.CHECK);
		transparentCheck.setText(bundle.getString("Transparent"));
		transparentCheck.setSelection(transparent);
		transparentCheck.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				transparent = ((Button)event.widget).getSelection();
				if (image != null) {
					imageCanvas.redraw();
				}
			}
		});

		// Check box to request mask display.
		maskCheck = new Button(group, SWT.CHECK);
		maskCheck.setText(bundle.getString("Mask"));
		maskCheck.setSelection(showMask);
		maskCheck.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				showMask = ((Button)event.widget).getSelection();
				if (image != null) {
					imageCanvas.redraw();
				}
			}
		});

		// Check box to request background display.
		backgroundCheck = new Button(group, SWT.CHECK);
		backgroundCheck.setText(bundle.getString("Background"));
		backgroundCheck.setSelection(showBackground);
		backgroundCheck.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				showBackground = ((Button)event.widget).getSelection();
			}
		});

		// Group the animation buttons.
		group = new Group(controls, SWT.NONE);
		group.setLayout(new RowLayout());
		group.setText(bundle.getString("Animation"));

		// Push button to display the previous image in a multi-image file.
		previousButton = new Button(group, SWT.PUSH);
		previousButton.setText(bundle.getString("Previous"));
		previousButton.setEnabled(false);
		previousButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				previous();
			}
		});

		// Push button to display the next image in a multi-image file.
		nextButton = new Button(group, SWT.PUSH);
		nextButton.setText(bundle.getString("Next"));
		nextButton.setEnabled(false);
		nextButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				next();
			}
		});

		// Push button to toggle animation of a multi-image file.
		animateButton = new Button(group, SWT.PUSH);
		animateButton.setText(bundle.getString("Animate"));
		animateButton.setEnabled(false);
		animateButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				animate();
			}
		});

		// Label to show the image file type.
		typeLabel = new Label(shell, SWT.NONE);
		typeLabel.setText(bundle.getString("Type_initial"));
		typeLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// Canvas to show the image.
		imageCanvas = new Canvas(shell, SWT.V_SCROLL | SWT.H_SCROLL | SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND);
		imageCanvas.setBackground(whiteColor);
		imageCanvas.setCursor(crossCursor);
		gridData = new GridData();
		gridData.verticalSpan = 15;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		imageCanvas.setLayoutData(gridData);
		imageCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				if (image == null) {
					Rectangle bounds = imageCanvas.getBounds();
					event.gc.fillRectangle(0, 0, bounds.width, bounds.height);
				} else {
					paintImage(event);
				}
			}
		});
		imageCanvas.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent event) {
				if (image != null) {
					showColorAt(event.x, event.y);
				}
			}
		});

		// Set up the image canvas scroll bars.
		ScrollBar horizontal = imageCanvas.getHorizontalBar();
		horizontal.setVisible(true);
		horizontal.setMinimum(0);
		horizontal.setEnabled(false);
		horizontal.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scrollHorizontally((ScrollBar)event.widget);
			}
		});
		ScrollBar vertical = imageCanvas.getVerticalBar();
		vertical.setVisible(true);
		vertical.setMinimum(0);
		vertical.setEnabled(false);
		vertical.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scrollVertically((ScrollBar)event.widget);
			}
		});

		// Label to show the image size.
		sizeLabel = new Label(shell, SWT.NONE);
		sizeLabel.setText(bundle.getString("Size_initial"));
		sizeLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// Label to show the image depth.
		depthLabel = new Label(shell, SWT.NONE);
		depthLabel.setText(bundle.getString("Depth_initial"));
		depthLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// Label to show the transparent pixel.
		transparentPixelLabel = new Label(shell, SWT.NONE);
		transparentPixelLabel.setText(bundle.getString("Transparent_pixel_initial"));
		transparentPixelLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// Label to show the time to load.
		timeToLoadLabel = new Label(shell, SWT.NONE);
		timeToLoadLabel.setText(bundle.getString("Time_to_load_initial"));
		timeToLoadLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// Separate the animation fields from the rest of the fields.
		Label separator = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// Label to show the logical screen size for animation.
		screenSizeLabel = new Label(shell, SWT.NONE);
		screenSizeLabel.setText(bundle.getString("Animation_size_initial"));
		screenSizeLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// Label to show the background pixel.
		backgroundPixelLabel = new Label(shell, SWT.NONE);
		backgroundPixelLabel.setText(bundle.getString("Background_pixel_initial"));
		backgroundPixelLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// Label to show the image location (x, y).
		locationLabel = new Label(shell, SWT.NONE);
		locationLabel.setText(bundle.getString("Image_location_initial"));
		locationLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// Label to show the image disposal method.
		disposalMethodLabel = new Label(shell, SWT.NONE);
		disposalMethodLabel.setText(bundle.getString("Disposal_initial"));
		disposalMethodLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// Label to show the image delay time.
		delayTimeLabel = new Label(shell, SWT.NONE);
		delayTimeLabel.setText(bundle.getString("Delay_initial"));
		delayTimeLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// Label to show the background pixel.
		repeatCountLabel = new Label(shell, SWT.NONE);
		repeatCountLabel.setText(bundle.getString("Repeats_initial"));
		repeatCountLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// Separate the animation fields from the palette.
		separator = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// Label to show if the image has a direct or indexed palette.
		paletteLabel = new Label(shell, SWT.NONE);
		paletteLabel.setText(bundle.getString("Palette_initial"));
		paletteLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));

		// Canvas to show the image's palette.
		paletteCanvas = new Canvas(shell, SWT.BORDER | SWT.V_SCROLL | SWT.NO_REDRAW_RESIZE);
		paletteCanvas.setFont(fixedWidthFont);
		paletteCanvas.getVerticalBar().setVisible(true);
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		GC gc = new GC(paletteLabel);
		paletteWidth = gc.stringExtent(bundle.getString("Max_length_string")).x;
		gc.dispose();
		gridData.widthHint = paletteWidth;
		gridData.heightHint = 16 * 11; // show at least 16 colors
		paletteCanvas.setLayoutData(gridData);
		paletteCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				if (image != null)
					paintPalette(event);
			}
		});

		// Set up the palette canvas scroll bar.
		vertical = paletteCanvas.getVerticalBar();
		vertical.setVisible(true);
		vertical.setMinimum(0);
		vertical.setIncrement(10);
		vertical.setEnabled(false);
		vertical.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				scrollPalette((ScrollBar)event.widget);
			}
		});

		// Sash to see more of image or image data.
		sash = new Sash(shell, SWT.HORIZONTAL);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = GridData.FILL;
		sash.setLayoutData(gridData);
		sash.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				if (event.detail != SWT.DRAG) {
					((GridData)paletteCanvas.getLayoutData()).heightHint = SWT.DEFAULT;
					Rectangle paletteCanvasBounds = paletteCanvas.getBounds();
					int minY = paletteCanvasBounds.y + 20;
					Rectangle dataLabelBounds = dataLabel.getBounds();
					int maxY = statusLabel.getBounds().y - dataLabelBounds.height - 20;
					if (event.y > minY && event.y < maxY) {
						Rectangle oldSash = sash.getBounds();
						sash.setBounds(event.x, event.y, event.width, event.height);
						int diff = event.y - oldSash.y;
						Rectangle bounds = imageCanvas.getBounds();
						imageCanvas.setBounds(bounds.x, bounds.y, bounds.width, bounds.height + diff);
						bounds = paletteCanvasBounds;
						paletteCanvas.setBounds(bounds.x, bounds.y, bounds.width, bounds.height + diff);
						bounds = dataLabelBounds;
						dataLabel.setBounds(bounds.x, bounds.y + diff, bounds.width, bounds.height);
						bounds = dataText.getBounds();
						dataText.setBounds(bounds.x, bounds.y + diff, bounds.width, bounds.height - diff);
						//shell.layout(true);
					}
				}
			}
		});

		// Label to show data-specific fields.
		dataLabel = new Label(shell, SWT.NONE);
		dataLabel.setText(bundle.getString("Pixel_data_initial"));
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = GridData.FILL;
		dataLabel.setLayoutData(gridData);
		
		// Text to show a dump of the data.
		dataText = new StyledText(shell, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
		dataText.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		dataText.setFont(fixedWidthFont);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = 128;
		gridData.grabExcessVerticalSpace = true;
		dataText.setLayoutData(gridData);
		dataText.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent event) {
				if (image != null && event.button == 1) {
					showColorForData();
				}
			}
		});
		dataText.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent event) {
				if (image != null) {
					showColorForData();
				}
			}
		});
		
		// Label to show status and cursor location in image.
		statusLabel = new Label(shell, SWT.NONE);
		statusLabel.setText("");
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = GridData.FILL;
		statusLabel.setLayoutData(gridData);
	}
	
	Menu createMenuBar() {
		// Menu bar.
		Menu menuBar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menuBar);
		createFileMenu(menuBar);
		createAlphaMenu(menuBar);
		return menuBar;
	}
	
	void createFileMenu(Menu menuBar) {
		// File menu
		MenuItem item = new MenuItem(menuBar, SWT.CASCADE);
		item.setText(bundle.getString("File"));
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		item.setMenu(fileMenu);

		// File -> Open File...
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(bundle.getString("OpenFile"));
		item.setAccelerator(SWT.MOD1 + 'O');
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuOpenFile();
			}
		});
		
		// File -> Open URL...
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(bundle.getString("OpenURL"));
		item.setAccelerator(SWT.MOD1 + 'U');
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuOpenURL();
			}
		});
		
		// File -> Reopen
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(bundle.getString("Reopen"));
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuReopen();
			}
		});
		
		new MenuItem(fileMenu, SWT.SEPARATOR);

		// File -> Load File... (natively)
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(bundle.getString("LoadFile"));
		item.setAccelerator(SWT.MOD1 + 'L');
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuLoad();
			}
		});
		
		new MenuItem(fileMenu, SWT.SEPARATOR);
		
		// File -> Save
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(bundle.getString("Save"));
		item.setAccelerator(SWT.MOD1 + 'S');
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuSave();
			}
		});
		
		// File -> Save As...
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(bundle.getString("Save_as"));
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuSaveAs();
			}
		});
		
		// File -> Save Mask As...
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(bundle.getString("Save_mask_as"));
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuSaveMaskAs();
			}
		});
		
		new MenuItem(fileMenu, SWT.SEPARATOR);
		
		// File -> Print
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(bundle.getString("Print"));
		item.setAccelerator(SWT.MOD1 + 'P');
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuPrint();
			}
		});
		
		new MenuItem(fileMenu, SWT.SEPARATOR);

		// File -> Exit
		item = new MenuItem(fileMenu, SWT.PUSH);
		item.setText(bundle.getString("Exit"));
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shell.close();
			}
		});
	
	}

	void createAlphaMenu(Menu menuBar) {
		// Alpha menu
		MenuItem item = new MenuItem(menuBar, SWT.CASCADE);
		item.setText(bundle.getString("Alpha"));
		Menu alphaMenu = new Menu(shell, SWT.DROP_DOWN);
		item.setMenu(alphaMenu);

		// Alpha -> K
		item = new MenuItem(alphaMenu, SWT.PUSH);
		item.setText("K");
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuComposeAlpha(ALPHA_CONSTANT);
			}
		});

		// Alpha -> (K + x) % 256
		item = new MenuItem(alphaMenu, SWT.PUSH);
		item.setText("(K + x) % 256");
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuComposeAlpha(ALPHA_X);
			}
		});

		// Alpha -> (K + y) % 256
		item = new MenuItem(alphaMenu, SWT.PUSH);
		item.setText("(K + y) % 256");
		item.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				menuComposeAlpha(ALPHA_Y);
			}
		});
	}

	void menuComposeAlpha(int alpha_op) {
		if (image == null) return;
		animate = false; // stop any animation in progress
		Cursor waitCursor = new Cursor(display, SWT.CURSOR_WAIT);
		shell.setCursor(waitCursor);
		imageCanvas.setCursor(waitCursor);
		try {
			if (alpha_op == ALPHA_CONSTANT) {
				imageData.alpha = alpha;
			} else {
				imageData.alpha = -1;
				switch (alpha_op) {
					case ALPHA_X: 
						for (int y = 0; y < imageData.height; y++) {
						for (int x = 0; x < imageData.width; x++) {
							imageData.setAlpha(x, y, (x + alpha) % 256);
						}
						}
						break;
					case ALPHA_Y: 
						for (int y = 0; y < imageData.height; y++) {
						for (int x = 0; x < imageData.width; x++) {
							imageData.setAlpha(x, y, (y + alpha) % 256);
						}
						}
						break;
					default: break;
				}					
			}			
			displayImage(imageData);
		} finally {
			shell.setCursor(null);
			imageCanvas.setCursor(crossCursor);
			waitCursor.dispose();
		}
	}

	/* Just use Image(device, filename) to load an image file. */
	void menuLoad() {
		animate = false; // stop any animation in progress
		
		// Get the user to choose an image file.
		FileDialog fileChooser = new FileDialog(shell, SWT.OPEN);
		if (lastPath != null)
			fileChooser.setFilterPath(lastPath);
		fileChooser.setFilterExtensions(OPEN_FILTER_EXTENSIONS);
		fileChooser.setFilterNames(OPEN_FILTER_NAMES);
		String filename = fileChooser.open();
		lastPath = fileChooser.getFilterPath();
		if (filename == null)
			return;

		Cursor waitCursor = new Cursor(display, SWT.CURSOR_WAIT);
		shell.setCursor(waitCursor);
		imageCanvas.setCursor(waitCursor);
		try {
			// Read the new image from the chosen file.
			long startTime = System.currentTimeMillis();
			Image newImage = new Image(display, filename);
			loadTime = System.currentTimeMillis() - startTime; // don't include getImageData in load time
			imageData = newImage.getImageData();

			// Cache the filename.
			currentName = filename;
			fileName = filename;
			
			// Fill in array and loader data.
			loader = new ImageLoader();
			imageDataArray = new ImageData[] {imageData};
			loader.data = imageDataArray;
				
			// Display the image.
			imageDataIndex = 0;
			displayImage(imageData);
		} catch (SWTException e) {
			showErrorDialog(bundle.getString("Loading_lc"), filename, e);
		} catch (SWTError e) {
			showErrorDialog(bundle.getString("Loading_lc"), filename, e);
		} catch (OutOfMemoryError e) {
			showErrorDialog(bundle.getString("Loading_lc"), filename, e);
		} finally {
			shell.setCursor(null);
			imageCanvas.setCursor(crossCursor);
			waitCursor.dispose();
		}
	}
	
	void menuOpenFile() {
		animate = false; // stop any animation in progress
		
		// Get the user to choose an image file.
		FileDialog fileChooser = new FileDialog(shell, SWT.OPEN);
		if (lastPath != null)
			fileChooser.setFilterPath(lastPath);
		fileChooser.setFilterExtensions(OPEN_FILTER_EXTENSIONS);
		fileChooser.setFilterNames(OPEN_FILTER_NAMES);
		String filename = fileChooser.open();
		lastPath = fileChooser.getFilterPath();
		if (filename == null)
			return;

		Cursor waitCursor = new Cursor(display, SWT.CURSOR_WAIT);
		shell.setCursor(waitCursor);
		imageCanvas.setCursor(waitCursor);
		ImageLoader oldLoader = loader;
		try {
			loader = new ImageLoader();
			if (incremental) {
				// Prepare to handle incremental events.
				loader.addImageLoaderListener(new ImageLoaderListener() {
					public void imageDataLoaded(ImageLoaderEvent event) {
						incrementalDataLoaded(event);
					}
				});
				incrementalThreadStart();
			}
			// Read the new image(s) from the chosen file.
			long startTime = System.currentTimeMillis();
			imageDataArray = loader.load(filename);
			loadTime = System.currentTimeMillis() - startTime;
			if (imageDataArray.length > 0) {
				// Cache the filename.
				currentName = filename;
				fileName = filename;
				
				// If there are multiple images in the file (typically GIF)
				// then enable the Previous, Next and Animate buttons.
				previousButton.setEnabled(imageDataArray.length > 1);
				nextButton.setEnabled(imageDataArray.length > 1);
				animateButton.setEnabled(imageDataArray.length > 1 && loader.logicalScreenWidth > 0 && loader.logicalScreenHeight > 0);
	
				// Display the first image in the file.
				imageDataIndex = 0;
				displayImage(imageDataArray[imageDataIndex]);
			}
		} catch (SWTException e) {
			showErrorDialog(bundle.getString("Loading_lc"), filename, e);
			loader = oldLoader;
		} catch (SWTError e) {
			showErrorDialog(bundle.getString("Loading_lc"), filename, e);
			loader = oldLoader;
		} catch (OutOfMemoryError e) {
			showErrorDialog(bundle.getString("Loading_lc"), filename, e);
			loader = oldLoader;
		} finally {
			shell.setCursor(null);
			imageCanvas.setCursor(crossCursor);
			waitCursor.dispose();
		}
	}
	
	void menuOpenURL() {
		animate = false; // stop any animation in progress
		
		// Get the user to choose an image URL.
		TextPrompter textPrompter = new TextPrompter(shell, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		textPrompter.setText(bundle.getString("OpenURLDialog"));
		textPrompter.setMessage(bundle.getString("EnterURL"));
		String urlname = textPrompter.open();
		if (urlname == null) return;

		Cursor waitCursor = new Cursor(display, SWT.CURSOR_WAIT);
		shell.setCursor(waitCursor);
		imageCanvas.setCursor(waitCursor);
		ImageLoader oldLoader = loader;
		try {
			URL url = new URL(urlname);
			InputStream stream = url.openStream();
			loader = new ImageLoader();
			if (incremental) {
				// Prepare to handle incremental events.
				loader.addImageLoaderListener(new ImageLoaderListener() {
					public void imageDataLoaded(ImageLoaderEvent event) {
						incrementalDataLoaded(event);
					}
				});
				incrementalThreadStart();
			}
			// Read the new image(s) from the chosen URL.
			long startTime = System.currentTimeMillis();
			imageDataArray = loader.load(stream);
			loadTime = System.currentTimeMillis() - startTime;
			stream.close();
			if (imageDataArray.length > 0) {
				currentName = urlname;
				fileName = null;
				
				// If there are multiple images (typically GIF)
				// then enable the Previous, Next and Animate buttons.
				previousButton.setEnabled(imageDataArray.length > 1);
				nextButton.setEnabled(imageDataArray.length > 1);
				animateButton.setEnabled(imageDataArray.length > 1 && loader.logicalScreenWidth > 0 && loader.logicalScreenHeight > 0);
	
				// Display the first image.
				imageDataIndex = 0;
				displayImage(imageDataArray[imageDataIndex]);
			}
		} catch (Exception e) {
			showErrorDialog(bundle.getString("Loading_lc"), urlname, e);
			loader = oldLoader;
		} catch (OutOfMemoryError e) {
			showErrorDialog(bundle.getString("Loading_lc"), urlname, e);
			loader = oldLoader;
		} finally {
			shell.setCursor(null);
			imageCanvas.setCursor(crossCursor);
			waitCursor.dispose();
		}
	}

	/*
	 * Called to start a thread that draws incremental images
	 * as they are loaded.
	 */
	void incrementalThreadStart() {
		incrementalEvents = new Vector();
		incrementalThread = new Thread("Incremental") {
			public void run() {
				// Draw the first ImageData increment.
				while (incrementalEvents != null) {
					// Synchronize so we don't try to remove when the vector is null.
					synchronized (ImageAnalyzer.this) {
						if (incrementalEvents != null) {
							if (incrementalEvents.size() > 0) {
								ImageLoaderEvent event = (ImageLoaderEvent) incrementalEvents.remove(0);
								if (image != null) image.dispose();
								image = new Image(display, event.imageData);
								imageData = event.imageData;
								imageCanvasGC.drawImage(
									image,
									0,
									0,
									imageData.width,
									imageData.height,
									imageData.x,
									imageData.y,
									imageData.width,
									imageData.height);
							} else {
								yield();
							}
						}
					}
				}
				display.wake();
			}
		};
		incrementalThread.setDaemon(true);
		incrementalThread.start();
	}
	
	/*
	 * Called when incremental image data has been loaded,
	 * for example, for interlaced GIF/PNG or progressive JPEG.
	 */
	void incrementalDataLoaded(ImageLoaderEvent event) {
		// Synchronize so that we do not try to add while
		// the incremental drawing thread is removing.
		synchronized (this) {
			incrementalEvents.addElement(event);
		}
	}
	
	void menuSave() {
		if (image == null) return;
		animate = false; // stop any animation in progress

		// If the image file type is unknown, we can't 'Save',
		// so we have to use 'Save As...'.
		if (imageData.type == SWT.IMAGE_UNDEFINED || fileName == null) {
			menuSaveAs();
			return;
		}

		Cursor waitCursor = new Cursor(display, SWT.CURSOR_WAIT);
		shell.setCursor(waitCursor);
		imageCanvas.setCursor(waitCursor);
		try {
			// Save the current image to the current file.
			loader.data = new ImageData[] {imageData};
			loader.save(fileName, imageData.type);
			
		} catch (SWTException e) {
			showErrorDialog(bundle.getString("Saving_lc"), fileName, e);
		} catch (SWTError e) {
			showErrorDialog(bundle.getString("Saving_lc"), fileName, e);
		} finally {
			shell.setCursor(null);
			imageCanvas.setCursor(crossCursor);
			waitCursor.dispose();
		}
	}

	void menuSaveAs() {
		if (image == null) return;
		animate = false; // stop any animation in progress

		// Get the user to choose a file name and type to save.
		FileDialog fileChooser = new FileDialog(shell, SWT.SAVE);
		fileChooser.setFilterPath(lastPath);
		if (fileName != null) {
			String name = fileName;
			int nameStart = name.lastIndexOf(java.io.File.separatorChar);
			if (nameStart > -1) {
				name = name.substring(nameStart + 1);
			}
			fileChooser.setFileName(name);
		}
		fileChooser.setFilterExtensions(SAVE_FILTER_EXTENSIONS);
		fileChooser.setFilterNames(SAVE_FILTER_NAMES);
		String filename = fileChooser.open();
		lastPath = fileChooser.getFilterPath();
		if (filename == null)
			return;

		// Figure out what file type the user wants saved.
		int filetype = fileChooser.getFilterIndex();
		if (filetype == -1) {
			/* The platform file dialog does not support user-selectable file filters.
			 * Determine the desired type by looking at the file extension. 
			 */
			filetype = determineFileType(filename);
			if (filetype == SWT.IMAGE_UNDEFINED) {
				MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
				box.setMessage(createMsg(bundle.getString("Unknown_extension"), 
					filename.substring(filename.lastIndexOf('.') + 1)));
				box.open();
				return;
			}
		}
		
		if (new java.io.File(filename).exists()) {
			MessageBox box = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
			box.setMessage(createMsg(bundle.getString("Overwrite"), filename));
			if (box.open() == SWT.CANCEL)
				return;
		}
		
		Cursor waitCursor = new Cursor(display, SWT.CURSOR_WAIT);
		shell.setCursor(waitCursor);
		imageCanvas.setCursor(waitCursor);
		try {
			// Save the current image to the specified file.
			boolean multi = false;
			if (loader.data.length > 1) {
				MessageBox box = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
				box.setMessage(createMsg(bundle.getString("Save_all"), new Integer(loader.data.length)));
				int result = box.open();
				if (result == SWT.CANCEL) return;
				if (result == SWT.YES) multi = true;
			}
			/* If the image has transparency but the user has transparency turned off,
			 * turn it off in the saved image. */
			int transparentPixel = imageData.transparentPixel;
			if (!multi && transparentPixel != -1 && !transparent) {
				imageData.transparentPixel = -1;
			}
			
			if (!multi) loader.data = new ImageData[] {imageData};
			loader.save(filename, filetype);
			
			/* Restore the previous transparency setting. */
			if (!multi && transparentPixel != -1 && !transparent) {
				imageData.transparentPixel = transparentPixel;
			}

			// Update the shell title and file type label,
			// and use the new file.
			fileName = filename;
			shell.setText(createMsg(bundle.getString("Analyzer_on"), filename));
			typeLabel.setText(createMsg(bundle.getString("Type_string"), fileTypeString(filetype)));

		} catch (SWTException e) {
			showErrorDialog(bundle.getString("Saving_lc"), filename, e);
		} catch (SWTError e) {
			showErrorDialog(bundle.getString("Saving_lc"), filename, e);
		} finally {
			shell.setCursor(null);
			imageCanvas.setCursor(crossCursor);
			waitCursor.dispose();
		}
	}

	void menuSaveMaskAs() {
		if (image == null || !showMask) return;
		if (imageData.getTransparencyType() == SWT.TRANSPARENCY_NONE) return;
		animate = false; // stop any animation in progress

		// Get the user to choose a file name and type to save.
		FileDialog fileChooser = new FileDialog(shell, SWT.SAVE);
		fileChooser.setFilterPath(lastPath);
		if (fileName != null) fileChooser.setFileName(fileName);
		fileChooser.setFilterExtensions(SAVE_FILTER_EXTENSIONS);
		fileChooser.setFilterNames(SAVE_FILTER_NAMES);
		String filename = fileChooser.open();
		lastPath = fileChooser.getFilterPath();
		if (filename == null)
			return;

		// Figure out what file type the user wants saved.
		int filetype = fileChooser.getFilterIndex();
		if (filetype == -1) {
			/* The platform file dialog does not support user-selectable file filters.
			 * Determine the desired type by looking at the file extension. 
			 */
			filetype = determineFileType(filename);
			if (filetype == SWT.IMAGE_UNDEFINED) {
				MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
				box.setMessage(createMsg(bundle.getString("Unknown_extension"), 
					filename.substring(filename.lastIndexOf('.') + 1)));
				box.open();
				return;
			}
		}
		
		if (new java.io.File(filename).exists()) {
			MessageBox box = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
			box.setMessage(createMsg(bundle.getString("Overwrite"), filename));
			if (box.open() == SWT.CANCEL)
				return;
		}
		
		Cursor waitCursor = new Cursor(display, SWT.CURSOR_WAIT);
		shell.setCursor(waitCursor);
		imageCanvas.setCursor(waitCursor);
		try {
			// Save the mask of the current image to the specified file.
			ImageData maskImageData = imageData.getTransparencyMask();
			loader.data = new ImageData[] {maskImageData};
			loader.save(filename, filetype);
			
		} catch (SWTException e) {
			showErrorDialog(bundle.getString("Saving_lc"), filename, e);
		} catch (SWTError e) {
			showErrorDialog(bundle.getString("Saving_lc"), filename, e);
		} finally {
			shell.setCursor(null);
			imageCanvas.setCursor(crossCursor);
			waitCursor.dispose();
		}
	}

	void menuPrint() {
		if (image == null) return;

		try {
			// Ask the user to specify the printer.
			PrintDialog dialog = new PrintDialog(shell, SWT.NONE);
			if (printerData != null) dialog.setPrinterData(printerData);
			printerData = dialog.open();
			if (printerData == null) return;
			
			Printer printer = new Printer(printerData);
			Point screenDPI = display.getDPI();
			Point printerDPI = printer.getDPI();
			int scaleFactor = printerDPI.x / screenDPI.x;
			Rectangle trim = printer.computeTrim(0, 0, 0, 0);
			if (printer.startJob(currentName)) {
				if (printer.startPage()) {
					GC gc = new GC(printer);
					int transparentPixel = imageData.transparentPixel;
					if (transparentPixel != -1 && !transparent) {
						imageData.transparentPixel = -1;
					}
					Image printerImage = new Image(printer, imageData);
					gc.drawImage(
						printerImage,
						0,
						0,
						imageData.width,
						imageData.height,
						-trim.x,
						-trim.y,
						scaleFactor * imageData.width,
						scaleFactor * imageData.height);
					if (transparentPixel != -1 && !transparent) {
						imageData.transparentPixel = transparentPixel;
					}
					printerImage.dispose();
					gc.dispose();
					printer.endPage();
				}
				printer.endJob();
			}
			printer.dispose();
		} catch (SWTError e) {
			MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
			box.setMessage(bundle.getString("Printing_error") + e.getMessage());
			box.open();
		}
	}

	void menuReopen() {
		if (currentName == null) return;
		animate = false; // stop any animation in progress
		Cursor waitCursor = new Cursor(display, SWT.CURSOR_WAIT);
		shell.setCursor(waitCursor);
		imageCanvas.setCursor(waitCursor);
		try {
			loader = new ImageLoader();
			ImageData[] newImageData;
			if (fileName == null) {
				URL url = new URL(currentName);
				InputStream stream = url.openStream();
				long startTime = System.currentTimeMillis();
				newImageData = loader.load(stream);
				loadTime = System.currentTimeMillis() - startTime;
				stream.close();
			} else {
				long startTime = System.currentTimeMillis();
				newImageData = loader.load(fileName);
				loadTime = System.currentTimeMillis() - startTime;
			}
			imageDataIndex = 0;
			displayImage(newImageData[imageDataIndex]);

		} catch (Exception e) {
			showErrorDialog(bundle.getString("Reloading_lc"), currentName, e);
		} catch (OutOfMemoryError e) {
			showErrorDialog(bundle.getString("Reloading_lc"), currentName, e);
		} finally {	
			shell.setCursor(null);
			imageCanvas.setCursor(crossCursor);
			waitCursor.dispose();
		}
	}
	
	void changeBackground() {
		String background = backgroundCombo.getText();
		if (background.equals(bundle.getString("White"))) {
			imageCanvas.setBackground(whiteColor);
		} else if (background.equals(bundle.getString("Black"))) {
			imageCanvas.setBackground(blackColor);
		} else if (background.equals(bundle.getString("Red"))) {
			imageCanvas.setBackground(redColor);
		} else if (background.equals(bundle.getString("Green"))) {
			imageCanvas.setBackground(greenColor);
		} else if (background.equals(bundle.getString("Blue"))) {
			imageCanvas.setBackground(blueColor);
		} else {
			imageCanvas.setBackground(null);
		}
	}
	
	/*
	 * Called when the ScaleX combo selection changes.
	 */
	void scaleX() {
		try {
			xscale = Float.parseFloat(scaleXCombo.getText());
		} catch (NumberFormatException e) {
			xscale = 1;
			scaleXCombo.select(scaleXCombo.indexOf("1"));
		}
		if (image != null) {
			resizeScrollBars();
			imageCanvas.redraw();
		}
	}
	
	/*
	 * Called when the ScaleY combo selection changes.
	 */
	void scaleY() {
		try {
			yscale = Float.parseFloat(scaleYCombo.getText());
		} catch (NumberFormatException e) {
			yscale = 1;
			scaleYCombo.select(scaleYCombo.indexOf("1"));
		}
		if (image != null) {
			resizeScrollBars();
			imageCanvas.redraw();
		}
	}
	
	/*
	 * Called when the Alpha combo selection changes.
	 */
	void alpha() {
		try {
			alpha = Integer.parseInt(alphaCombo.getText());
		} catch (NumberFormatException e) {
			alphaCombo.select(alphaCombo.indexOf("255"));
			alpha = 255;
		}
	}
	
	/*
	 * Called when the mouse moves in the image canvas.
	 * Show the color of the image at the point under the mouse.
	 */
	void showColorAt(int mx, int my) {
		int x = mx - imageData.x - ix;
		int y = my - imageData.y - iy;
		showColorForPixel(x, y);
	}
	
	/*
	 * Called when a mouse down or key press is detected
	 * in the data text. Show the color of the pixel at
	 * the caret position in the data text.
	 */
	void showColorForData() {
		int delimiterLength = dataText.getLineDelimiter().length();
		int charactersPerLine = 6 + 3 * imageData.bytesPerLine + delimiterLength;
		int position = dataText.getCaretOffset();
		int y = position / charactersPerLine;
		if ((position - y * charactersPerLine) < 6 || ((y + 1) * charactersPerLine - position) <= delimiterLength) {
			statusLabel.setText("");
			return;
		}
		int dataPosition = position - 6 * (y + 1) - delimiterLength * y;
		int byteNumber = dataPosition / 3;
		int where = dataPosition - byteNumber * 3;
		int xByte = byteNumber % imageData.bytesPerLine;
		int x = -1;
		int depth = imageData.depth;
		if (depth == 1) { // 8 pixels per byte (can only show 3 of 8)
			if (where == 0) x = xByte * 8;
			if (where == 1) x = xByte * 8 + 3;
			if (where == 2) x = xByte * 8 + 7;
		}
		if (depth == 2) { // 4 pixels per byte (can only show 3 of 4)
			if (where == 0) x = xByte * 4;
			if (where == 1) x = xByte * 4 + 1;
			if (where == 2) x = xByte * 4 + 3;
		}
		if (depth == 4) { // 2 pixels per byte
			if (where == 0) x = xByte * 2;
			if (where == 1) x = xByte * 2;
			if (where == 2) x = xByte * 2 + 1;
		}
		if (depth == 8) { // 1 byte per pixel
			x = xByte;
		}
		if (depth == 16) { // 2 bytes per pixel
			x = xByte / 2;
		}
		if (depth == 24) { // 3 bytes per pixel
			x = xByte / 3;
		}
		if (depth == 32) { // 4 bytes per pixel
			x = xByte / 4;
		}
		if (x != -1) {
			showColorForPixel(x, y);
		} else {
			statusLabel.setText("");
		}
	}
	
	/*
	 * Set the status label to show color information
	 * for the specified pixel in the image.
	 */
	void showColorForPixel(int x, int y) {
		if (x >= 0 && x < imageData.width && y >= 0 && y < imageData.height) {
			int pixel = imageData.getPixel(x, y);
			RGB rgb = imageData.palette.getRGB(pixel);
			boolean hasAlpha = false;
			int alphaValue = 0;
			if (imageData.alphaData != null && imageData.alphaData.length > 0) {
				hasAlpha = true;
				alphaValue = imageData.getAlpha(x, y);
			}
			String rgbMessageFormat = bundle.getString(hasAlpha ? "RGBA" : "RGB");
			Object[] rgbArgs = {
					Integer.toString(rgb.red),
					Integer.toString(rgb.green),
					Integer.toString(rgb.blue),
					Integer.toString(alphaValue)
			};
			Object[] rgbHexArgs = {
					Integer.toHexString(rgb.red),
					Integer.toHexString(rgb.green),
					Integer.toHexString(rgb.blue),
					Integer.toHexString(alphaValue)
			};
			Object[] args = {
					new Integer(x),
					new Integer(y),
					new Integer(pixel),
					Integer.toHexString(pixel),
					createMsg(rgbMessageFormat, rgbArgs),
					createMsg(rgbMessageFormat, rgbHexArgs),
					(pixel == imageData.transparentPixel) ? bundle.getString("Color_at_transparent") : ""};
			statusLabel.setText(createMsg(bundle.getString("Color_at"), args));
		} else {
			statusLabel.setText("");
		}
	}
	
	/*
	 * Called when the Animate button is pressed.
	 */
	void animate() {
		animate = !animate;
		if (animate && image != null && imageDataArray.length > 1) {
			animateThread = new Thread(bundle.getString("Animation")) {
				public void run() {
					// Pre-animation widget setup.
					preAnimation();
					
					// Animate.
					try {
						animateLoop();
					} catch (final SWTException e) {
						display.syncExec(new Runnable() {
							public void run() {
								showErrorDialog(createMsg(bundle.getString("Creating_image"), 
										    new Integer(imageDataIndex+1)),
										    currentName, e);
							}
						});
					}
					
					// Post animation widget reset.
					postAnimation();
				}
			};
			animateThread.setDaemon(true);
			animateThread.start();
		}
	}
	
	/*
	 * Loop through all of the images in a multi-image file
	 * and display them one after another.
	 */
	void animateLoop() {
		// Create an off-screen image to draw on, and a GC to draw with.
		// Both are disposed after the animation.
		Image offScreenImage = new Image(display, loader.logicalScreenWidth, loader.logicalScreenHeight);
		GC offScreenImageGC = new GC(offScreenImage);
		
		try {
			// Use syncExec to get the background color of the imageCanvas.
			display.syncExec(new Runnable() {
				public void run() {
					canvasBackground = imageCanvas.getBackground();
				}
			});

			// Fill the off-screen image with the background color of the canvas.
			offScreenImageGC.setBackground(canvasBackground);
			offScreenImageGC.fillRectangle(
				0,
				0,
				loader.logicalScreenWidth,
				loader.logicalScreenHeight);
					
			// Draw the current image onto the off-screen image.
			offScreenImageGC.drawImage(
				image,
				0,
				0,
				imageData.width,
				imageData.height,
				imageData.x,
				imageData.y,
				imageData.width,
				imageData.height);

			int repeatCount = loader.repeatCount;
			while (animate && (loader.repeatCount == 0 || repeatCount > 0)) {
				if (imageData.disposalMethod == SWT.DM_FILL_BACKGROUND) {
					// Fill with the background color before drawing.
					Color bgColor = null;
					int backgroundPixel = loader.backgroundPixel;
					if (showBackground && backgroundPixel != -1) {
						// Fill with the background color.
						RGB backgroundRGB = imageData.palette.getRGB(backgroundPixel);
						bgColor = new Color(null, backgroundRGB);
					}
					try {
						offScreenImageGC.setBackground(bgColor != null ? bgColor : canvasBackground);
						offScreenImageGC.fillRectangle(
							imageData.x,
							imageData.y,
							imageData.width,
							imageData.height);
					} finally {
						if (bgColor != null) bgColor.dispose();
					}
				} else if (imageData.disposalMethod == SWT.DM_FILL_PREVIOUS) {
					// Restore the previous image before drawing.
					offScreenImageGC.drawImage(
						image,
						0,
						0,
						imageData.width,
						imageData.height,
						imageData.x,
						imageData.y,
						imageData.width,
						imageData.height);
				}
									
				// Get the next image data.
				imageDataIndex = (imageDataIndex + 1) % imageDataArray.length;
				imageData = imageDataArray[imageDataIndex];
				image.dispose();
				image = new Image(display, imageData);
				
				// Draw the new image data.
				offScreenImageGC.drawImage(
					image,
					0,
					0,
					imageData.width,
					imageData.height,
					imageData.x,
					imageData.y,
					imageData.width,
					imageData.height);
				
				// Draw the off-screen image to the screen.
				imageCanvasGC.drawImage(offScreenImage, 0, 0);
				
				// Sleep for the specified delay time before drawing again.
				try {
					Thread.sleep(visibleDelay(imageData.delayTime * 10));
				} catch (InterruptedException e) {
				}
				
				// If we have just drawn the last image in the set,
				// then decrement the repeat count.
				if (imageDataIndex == imageDataArray.length - 1) repeatCount--;
			}
		} finally {
			offScreenImage.dispose();
			offScreenImageGC.dispose();
		}
	}

	/*
	 * Pre animation setup.
	 */
	void preAnimation() {
		display.syncExec(new Runnable() {
			public void run() {
				// Change the label of the Animate button to 'Stop'.
				animateButton.setText(bundle.getString("Stop"));
				
				// Disable anything we don't want the user
				// to select during the animation.
				previousButton.setEnabled(false);
				nextButton.setEnabled(false);
				backgroundCombo.setEnabled(false);
				scaleXCombo.setEnabled(false);
				scaleYCombo.setEnabled(false);
				alphaCombo.setEnabled(false);
				incrementalCheck.setEnabled(false);
				transparentCheck.setEnabled(false);
				maskCheck.setEnabled(false);
				// leave backgroundCheck enabled
			
				// Reset the scale combos and scrollbars.
				resetScaleCombos();
				resetScrollBars();
			}
		});
	}

	/*
	 * Post animation reset.
	 */
	void postAnimation() {
		display.syncExec(new Runnable() {
			public void run() {
				// Enable anything we disabled before the animation.
				previousButton.setEnabled(true);
				nextButton.setEnabled(true);
				backgroundCombo.setEnabled(true);
				scaleXCombo.setEnabled(true);
				scaleYCombo.setEnabled(true);
				alphaCombo.setEnabled(true);
				incrementalCheck.setEnabled(true);
				transparentCheck.setEnabled(true);
				maskCheck.setEnabled(true);
			
				// Reset the label of the Animate button.
				animateButton.setText(bundle.getString("Animate"));
			
				if (animate) {
					// If animate is still true, we finished the
					// full number of repeats. Leave the image as-is.
					animate = false;
				} else {
					// Redisplay the current image and its palette.
					displayImage(imageDataArray[imageDataIndex]);
				}
			}
		});
	}

	/*
	 * Called when the Previous button is pressed.
	 * Display the previous image in a multi-image file.
	 */
	void previous() {
		if (image != null && imageDataArray.length > 1) {
			if (imageDataIndex == 0) {
				imageDataIndex = imageDataArray.length;
			}
			imageDataIndex = imageDataIndex - 1;
			displayImage(imageDataArray[imageDataIndex]);
		}	
	}

	/*
	 * Called when the Next button is pressed.
	 * Display the next image in a multi-image file.
	 */
	void next() {
		if (image != null && imageDataArray.length > 1) {
			imageDataIndex = (imageDataIndex + 1) % imageDataArray.length;
			displayImage(imageDataArray[imageDataIndex]);
		}	
	}

	void displayImage(ImageData newImageData) {
		resetScaleCombos();
		if (incremental && incrementalThread != null) {
			// Tell the incremental thread to stop drawing.
			synchronized (this) {
				incrementalEvents = null;
			}
			
			// Wait until the incremental thread is done.
			while (incrementalThread.isAlive()) {
				if (!display.readAndDispatch()) display.sleep();
			}
		}
					
		// Dispose of the old image, if there was one.
		if (image != null) image.dispose();

		try {
			// Cache the new image and imageData.
			image = new Image(display, newImageData);
			imageData = newImageData;

		} catch (SWTException e) {
			showErrorDialog(bundle.getString("Creating_from") + " ", currentName, e);
			image = null;
			return;
		}

		// Update the widgets with the new image info.
		String string = createMsg(bundle.getString("Analyzer_on"), currentName);
		shell.setText(string);

		if (imageDataArray.length > 1) {
			string = createMsg(bundle.getString("Type_index"), 
			                   new Object[] {fileTypeString(imageData.type),
			                                 new Integer(imageDataIndex + 1),
			                                 new Integer(imageDataArray.length)});
		} else {
			string = createMsg(bundle.getString("Type_string"), fileTypeString(imageData.type));
		}
		typeLabel.setText(string);

		string = createMsg(bundle.getString("Size_value"), 
					 new Object[] {new Integer(imageData.width),
							   new Integer(imageData.height)});
		sizeLabel.setText(string);

		string = createMsg(bundle.getString("Depth_value"), new Integer(imageData.depth));
		depthLabel.setText(string);

		string = createMsg(bundle.getString("Transparent_pixel_value"), pixelInfo(imageData.transparentPixel));
		transparentPixelLabel.setText(string);

		string = createMsg(bundle.getString("Time_to_load_value"), new Long(loadTime));
		timeToLoadLabel.setText(string);

		string = createMsg(bundle.getString("Animation_size_value"), 
		                      new Object[] {new Integer(loader.logicalScreenWidth),
								new Integer(loader.logicalScreenHeight)});
		screenSizeLabel.setText(string);

		string = createMsg(bundle.getString("Background_pixel_value"), pixelInfo(loader.backgroundPixel));
		backgroundPixelLabel.setText(string);

		string = createMsg(bundle.getString("Image_location_value"), 
		                      new Object[] {new Integer(imageData.x), new Integer(imageData.y)});
		locationLabel.setText(string);

		string = createMsg(bundle.getString("Disposal_value"),
		                      new Object[] {new Integer(imageData.disposalMethod),
							      disposalString(imageData.disposalMethod)});
		disposalMethodLabel.setText(string);

		int delay = imageData.delayTime * 10;
		int delayUsed = visibleDelay(delay);
		if (delay != delayUsed) {
			string = createMsg(bundle.getString("Delay_value"), 
			                   new Object[] {new Integer(delay), new Integer(delayUsed)});
		} else {
			string = createMsg(bundle.getString("Delay_used"), new Integer(delay));
		}
		delayTimeLabel.setText(string);

		if (loader.repeatCount == 0) {
			string = createMsg( bundle.getString("Repeats_forever"), new Integer(loader.repeatCount));
		} else {
			string = createMsg(bundle.getString("Repeats_value"), new Integer(loader.repeatCount));
		}
		repeatCountLabel.setText(string);

		if (imageData.palette.isDirect) {
			string = bundle.getString("Palette_direct");
		} else {
			string = createMsg(bundle.getString("Palette_value"), new Integer(imageData.palette.getRGBs().length));
		}
		paletteLabel.setText(string);

		string = createMsg(
				bundle.getString("Pixel_data_value"),
				new Object[] {
						new Integer(imageData.bytesPerLine),
						new Integer(imageData.scanlinePad),
						depthInfo(imageData.depth),
						(imageData.alphaData != null && imageData.alphaData.length > 0) ?
								bundle.getString("Scroll_for_alpha") : "" });
		dataLabel.setText(string);

		String data = dataHexDump(dataText.getLineDelimiter());
		dataText.setText(data);
		
		// bold the first column all the way down
		int index = 0;
		while((index = data.indexOf(':', index+1)) != -1) {
			int start = index - INDEX_DIGITS;
			int length = INDEX_DIGITS;
			if (Character.isLetter(data.charAt(index-1))) {
				start = index - ALPHA_CHARS;
				length = ALPHA_CHARS;
			}
			dataText.setStyleRange(new StyleRange(start, length, dataText.getForeground(), dataText.getBackground(), SWT.BOLD));
		}

		statusLabel.setText("");

		// Redraw both canvases.
		resetScrollBars();
		paletteCanvas.redraw();
		imageCanvas.redraw();
	}

	void paintImage(PaintEvent event) {
		GC gc = event.gc;
		Image paintImage = image;
		
		/* If the user wants to see the transparent pixel in its actual color,
		 * then temporarily turn off transparency.
		 */
		int transparentPixel = imageData.transparentPixel;
		if (transparentPixel != -1 && !transparent) {
			imageData.transparentPixel = -1;
			paintImage = new Image(display, imageData);
		}
		
		/* Scale the image when drawing, using the user's selected scaling factor. */
		int w = Math.round(imageData.width * xscale);
		int h = Math.round(imageData.height * yscale);
		
		/* If any of the background is visible, fill it with the background color. */
		Rectangle bounds = imageCanvas.getBounds();
		if (imageData.getTransparencyType() != SWT.TRANSPARENCY_NONE) {
			/* If there is any transparency at all, fill the whole background. */
			gc.fillRectangle(0, 0, bounds.width, bounds.height);
		} else {
			/* Otherwise, just fill in the backwards L. */
			if (ix + w < bounds.width) gc.fillRectangle(ix + w, 0, bounds.width - (ix + w), bounds.height);
			if (iy + h < bounds.height) gc.fillRectangle(0, iy + h, ix + w, bounds.height - (iy + h));
		}
		
		/* Draw the image */
		gc.drawImage(
			paintImage,
			0,
			0,
			imageData.width,
			imageData.height,
			ix + imageData.x,
			iy + imageData.y,
			w,
			h);
		
		/* If there is a mask and the user wants to see it, draw it. */
		if (showMask && (imageData.getTransparencyType() != SWT.TRANSPARENCY_NONE)) {
			ImageData maskImageData = imageData.getTransparencyMask();
			Image maskImage = new Image(display, maskImageData);
			gc.drawImage(
				maskImage,
				0,
				0,
				imageData.width,
				imageData.height,
				w + 10 + ix + imageData.x,
				iy + imageData.y,
				w,
				h);
			maskImage.dispose();
		}
		
		/* If transparency was temporarily disabled, restore it. */
		if (transparentPixel != -1 && !transparent) {
			imageData.transparentPixel = transparentPixel;
			paintImage.dispose();
		}
	}

	void paintPalette(PaintEvent event) {
		GC gc = event.gc;
		gc.fillRectangle(paletteCanvas.getClientArea());
		if (imageData.palette.isDirect) {
			// For a direct palette, display the masks.
			int y = py + 10;
			int xTab = 50;
			gc.drawString("rMsk", 10, y, true);
			gc.drawString(toHex4ByteString(imageData.palette.redMask), xTab, y, true);
			gc.drawString("gMsk", 10, y+=12, true);
			gc.drawString(toHex4ByteString(imageData.palette.greenMask), xTab, y, true);
			gc.drawString("bMsk", 10, y+=12, true);
			gc.drawString(toHex4ByteString(imageData.palette.blueMask), xTab, y, true);
			gc.drawString("rShf", 10, y+=12, true);
			gc.drawString(Integer.toString(imageData.palette.redShift), xTab, y, true);
			gc.drawString("gShf", 10, y+=12, true);
			gc.drawString(Integer.toString(imageData.palette.greenShift), xTab, y, true);
			gc.drawString("bShf", 10, y+=12, true);
			gc.drawString(Integer.toString(imageData.palette.blueShift), xTab, y, true);
		} else {
			// For an indexed palette, display the palette colors and indices.
			RGB[] rgbs = imageData.palette.getRGBs();
			if (rgbs != null) {
				int xTab1 = 40, xTab2 = 100;
				for (int i = 0; i < rgbs.length; i++) {
					int y = (i+1) * 10 + py;
					gc.drawString(String.valueOf(i), 10, y, true);
					gc.drawString(toHexByteString(rgbs[i].red) + toHexByteString(rgbs[i].green) + toHexByteString(rgbs[i].blue), xTab1, y, true);
					Color color = new Color(display, rgbs[i]);
					gc.setBackground(color);
					gc.fillRectangle(xTab2, y+2, 10, 10);
					color.dispose();
				}
			}
		}
	}
	
	void resizeShell(ControlEvent event) {
		if (image == null || shell.isDisposed())
			return;
		resizeScrollBars();
	}

	// Reset the scale combos to 1.
	void resetScaleCombos() {
		xscale = 1; yscale = 1;
		scaleXCombo.select(scaleXCombo.indexOf("1"));
		scaleYCombo.select(scaleYCombo.indexOf("1"));
	}
	
	// Reset the scroll bars to 0.
	void resetScrollBars() {
		if (image == null) return;
		ix = 0; iy = 0; py = 0;
		resizeScrollBars();
		imageCanvas.getHorizontalBar().setSelection(0);
		imageCanvas.getVerticalBar().setSelection(0);
		paletteCanvas.getVerticalBar().setSelection(0);
	}
	
	void resizeScrollBars() {
		// Set the max and thumb for the image canvas scroll bars.
		ScrollBar horizontal = imageCanvas.getHorizontalBar();
		ScrollBar vertical = imageCanvas.getVerticalBar();
		Rectangle canvasBounds = imageCanvas.getClientArea();
		int width = Math.round(imageData.width * xscale);
		if (width > canvasBounds.width) {
			// The image is wider than the canvas.
			horizontal.setEnabled(true);
			horizontal.setMaximum(width);
			horizontal.setThumb(canvasBounds.width);
			horizontal.setPageIncrement(canvasBounds.width);
		} else {
			// The canvas is wider than the image.
			horizontal.setEnabled(false);
			if (ix != 0) {
				// Make sure the image is completely visible.
				ix = 0;
				imageCanvas.redraw();
			}
		}
		int height = Math.round(imageData.height * yscale);
		if (height > canvasBounds.height) {
			// The image is taller than the canvas.
			vertical.setEnabled(true);
			vertical.setMaximum(height);
			vertical.setThumb(canvasBounds.height);
			vertical.setPageIncrement(canvasBounds.height);
		} else {
			// The canvas is taller than the image.
			vertical.setEnabled(false);
			if (iy != 0) {
				// Make sure the image is completely visible.
				iy = 0;
				imageCanvas.redraw();
			}
		}

		// Set the max and thumb for the palette canvas scroll bar.
		vertical = paletteCanvas.getVerticalBar();
		if (imageData.palette.isDirect) {
			vertical.setEnabled(false);
		} else { // indexed palette
			canvasBounds = paletteCanvas.getClientArea();
			int paletteHeight = imageData.palette.getRGBs().length * 10 + 20; // 10 pixels each index + 20 for margins.
			vertical.setEnabled(true);
			vertical.setMaximum(paletteHeight);
			vertical.setThumb(canvasBounds.height);
			vertical.setPageIncrement(canvasBounds.height);
		}
	}

	/*
	 * Called when the image canvas' horizontal scrollbar is selected.
	 */
	void scrollHorizontally(ScrollBar scrollBar) {
		if (image == null) return;
		Rectangle canvasBounds = imageCanvas.getClientArea();
		int width = Math.round(imageData.width * xscale);
		int height = Math.round(imageData.height * yscale);
		if (width > canvasBounds.width) {
			// Only scroll if the image is bigger than the canvas.
			int x = -scrollBar.getSelection();
			if (x + width < canvasBounds.width) {
				// Don't scroll past the end of the image.
				x = canvasBounds.width - width;
			}
			imageCanvas.scroll(x, iy, ix, iy, width, height, false);
			ix = x;
		}
	}
	
	/*
	 * Called when the image canvas' vertical scrollbar is selected.
	 */
	void scrollVertically(ScrollBar scrollBar) {
		if (image == null) return;
		Rectangle canvasBounds = imageCanvas.getClientArea();
		int width = Math.round(imageData.width * xscale);
		int height = Math.round(imageData.height * yscale);
		if (height > canvasBounds.height) {
			// Only scroll if the image is bigger than the canvas.
			int y = -scrollBar.getSelection();
			if (y + height < canvasBounds.height) {
				// Don't scroll past the end of the image.
				y = canvasBounds.height - height;
			}
			imageCanvas.scroll(ix, y, ix, iy, width, height, false);
			iy = y;
		}
	}

	/*
	 * Called when the palette canvas' vertical scrollbar is selected.
	 */
	void scrollPalette(ScrollBar scrollBar) {
		if (image == null) return;
		Rectangle canvasBounds = paletteCanvas.getClientArea();
		int paletteHeight = imageData.palette.getRGBs().length * 10 + 20;
		if (paletteHeight > canvasBounds.height) {
			// Only scroll if the palette is bigger than the canvas.
			int y = -scrollBar.getSelection();
			if (y + paletteHeight < canvasBounds.height) {
				// Don't scroll past the end of the palette.
				y = canvasBounds.height - paletteHeight;
			}
			paletteCanvas.scroll(0, y, 0, py, paletteWidth, paletteHeight, false);
			py = y;
		}
	}

	/*
	 * Return a String containing a line-by-line dump of
	 * the data in the current imageData. The lineDelimiter
	 * parameter must be a string of length 1 or 2.
	 */
	String dataHexDump(String lineDelimiter) {
		final int MAX_DUMP = 1024 * 1024;
		if (image == null) return "";
		boolean truncated = false;
		char[] dump = null;
		byte[] alphas = imageData.alphaData;
		try {
			int length = imageData.height * (6 + 3 * imageData.bytesPerLine + lineDelimiter.length());
			if (alphas != null && alphas.length > 0) {
				length += imageData.height * (6 + 3 * imageData.width + lineDelimiter.length()) + 6 + lineDelimiter.length();
			}
			dump = new char[length];
		} catch (OutOfMemoryError e) {
			/* Too much data to dump - truncate. */
			dump = new char[MAX_DUMP];
			truncated = true;
		}
		int index = 0;
		try {
			for (int i = 0; i < imageData.data.length; i++) {
				if (i % imageData.bytesPerLine == 0) {
					int line = i / imageData.bytesPerLine;
					dump[index++] = Character.forDigit(line / 1000 % 10, 10);
					dump[index++] = Character.forDigit(line / 100 % 10, 10);
					dump[index++] = Character.forDigit(line / 10 % 10, 10);
					dump[index++] = Character.forDigit(line % 10, 10);
					dump[index++] = ':';
					dump[index++] = ' ';
				}
				byte b = imageData.data[i];
				dump[index++] = Character.forDigit((b & 0xF0) >> 4, 16);
				dump[index++] = Character.forDigit(b & 0x0F, 16);
				dump[index++] = ' ';
				if ((i + 1) % imageData.bytesPerLine == 0) {
					dump[index++] = lineDelimiter.charAt(0);
					if (lineDelimiter.length() > 1) {
						dump[index++] = lineDelimiter.charAt(1);
					}
				}
			}
			if (alphas != null && alphas.length > 0) {
				dump[index++] = lineDelimiter.charAt(0);
				if (lineDelimiter.length() > 1) {
					dump[index++] = lineDelimiter.charAt(1);
				}
				System.arraycopy(new char[]{'A','l','p','h','a',':'}, 0, dump, index, 6);
				index +=6;
				dump[index++] = lineDelimiter.charAt(0);
				if (lineDelimiter.length() > 1) {
					dump[index++] = lineDelimiter.charAt(1);
				}
				for (int i = 0; i < alphas.length; i++) {
					if (i % imageData.width == 0) {
						int line = i / imageData.width;
						dump[index++] = Character.forDigit(line / 1000 % 10, 10);
						dump[index++] = Character.forDigit(line / 100 % 10, 10);
						dump[index++] = Character.forDigit(line / 10 % 10, 10);
						dump[index++] = Character.forDigit(line % 10, 10);
						dump[index++] = ':';
						dump[index++] = ' ';
					}
					byte b = alphas[i];
					dump[index++] = Character.forDigit((b & 0xF0) >> 4, 16);
					dump[index++] = Character.forDigit(b & 0x0F, 16);
					dump[index++] = ' ';
					if ((i + 1) % imageData.width == 0) {
						dump[index++] = lineDelimiter.charAt(0);
						if (lineDelimiter.length() > 1) {
							dump[index++] = lineDelimiter.charAt(1);
						}
					}
				}
			}
		} catch (IndexOutOfBoundsException e) {}
		String result = "";
		try {
			result = new String(dump);
		} catch (OutOfMemoryError e) {
			/* Too much data to display in the text widget - truncate. */
			result = new String(dump, 0, MAX_DUMP);
			truncated = true;
		}
		if (truncated) result += "\n ...data dump truncated at " + MAX_DUMP + "bytes...";
		return result;
	}
	
	/*
	 * Open an error dialog displaying the specified information.
	 */
	void showErrorDialog(String operation, String filename, Throwable e) {
		MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
		String message = createMsg(bundle.getString("Error"), new String[] {operation, filename});
		String errorMessage = "";
		if (e != null) {
			if (e instanceof SWTException) {
				SWTException swte = (SWTException) e;
				errorMessage = swte.getMessage();
				if (swte.throwable != null) {
					errorMessage += ":\n" + swte.throwable.toString();
				}
			} else if (e instanceof SWTError) {
				SWTError swte = (SWTError) e;
				errorMessage = swte.getMessage();
				if (swte.throwable != null) {
					errorMessage += ":\n" + swte.throwable.toString();
				}
			} else {
				errorMessage = e.toString();
			}
		}
		box.setMessage(message + errorMessage);
		box.open();
	}
	
	/*
	 * Open a dialog asking the user for more information on the type of BMP file to save.
	 */
	int showBMPDialog() {
		final int [] bmpType = new int[1];
		bmpType[0] = SWT.IMAGE_BMP;
		SelectionListener radioSelected = new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				Button radio = (Button) event.widget;
				if (radio.getSelection()) bmpType[0] = ((Integer)radio.getData()).intValue();
			}
		};
		// need to externalize strings
		final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM);

		dialog.setText(bundle.getString("Save_as_type"));
		dialog.setLayout(new GridLayout());
		
		Label label = new Label(dialog, SWT.NONE);
		label.setText(bundle.getString("Save_as_type_label"));
		
		Button radio = new Button(dialog, SWT.RADIO);
		radio.setText(bundle.getString("Save_as_type_no_compress"));
		radio.setSelection(true);
		radio.setData(new Integer(SWT.IMAGE_BMP));
		radio.addSelectionListener(radioSelected);

		radio = new Button(dialog, SWT.RADIO);
		radio.setText(bundle.getString("Save_as_type_rle_compress"));
		radio.setData(new Integer(SWT.IMAGE_BMP_RLE));
		radio.addSelectionListener(radioSelected);
		
		radio = new Button(dialog, SWT.RADIO);
		radio.setText(bundle.getString("Save_as_type_os2"));
		radio.setData(new Integer(SWT.IMAGE_OS2_BMP));
		radio.addSelectionListener(radioSelected);

		label = new Label(dialog, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Button ok = new Button(dialog, SWT.PUSH);
		ok.setText(bundle.getString("OK"));
		GridData data = new GridData();
		data.horizontalAlignment = SWT.CENTER;
		data.widthHint = 75;
		ok.setLayoutData(data);
		ok.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				dialog.close();
			}
		});
		
		dialog.pack();
		dialog.open();
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		return bmpType[0];
	}	
	
	/*
	 * Return a String describing how to analyze the bytes
	 * in the hex dump.
	 */
	static String depthInfo(int depth) {
		Object[] args = {new Integer(depth), ""};
		switch (depth) {
			case 1:
				args[1] = createMsg(bundle.getString("Multi_pixels"), 
				                    new Object[] {new Integer(8), " [01234567]"});
				break;
			case 2:
				args[1] = createMsg(bundle.getString("Multi_pixels"),
				                    new Object[] {new Integer(4), "[00112233]"});
				break;
			case 4:
				args[1] = createMsg(bundle.getString("Multi_pixels"),
				                    new Object[] {new Integer(2), "[00001111]"});
				break;
			case 8:
				args[1] = bundle.getString("One_byte");
				break;
			case 16:
				args[1] = createMsg(bundle.getString("Multi_bytes"), new Integer(2));
				break;
			case 24:
				args[1] = createMsg(bundle.getString("Multi_bytes"), new Integer(3));
				break;
			case 32:
				args[1] = createMsg(bundle.getString("Multi_bytes"), new Integer(4));
				break;
			default:
				args[1] = bundle.getString("Unsupported_lc");
		}
		return createMsg(bundle.getString("Depth_info"), args);
	}
	
	/*
	 * Return the specified number of milliseconds.
	 * If the specified number of milliseconds is too small
	 * to see a visual change, then return a higher number.
	 */
	static int visibleDelay(int ms) {
		if (ms < 20) return ms + 30;
		if (ms < 30) return ms + 10;
		return ms;
	}

	/*
	 * Return the specified byte value as a hex string,
	 * preserving leading 0's.
	 */
	static String toHexByteString(int i) {
		if (i <= 0x0f)
			return "0" + Integer.toHexString(i);
		return Integer.toHexString(i & 0xff);
	}

	/*
	 * Return the specified 4-byte value as a hex string,
	 * preserving leading 0's.
	 * (a bit 'brute force'... should probably use a loop...)
	 */
	static String toHex4ByteString(int i) {
		String hex = Integer.toHexString(i);
		if (hex.length() == 1)
			return "0000000" + hex;
		if (hex.length() == 2)
			return "000000" + hex;
		if (hex.length() == 3)
			return "00000" + hex;
		if (hex.length() == 4)
			return "0000" + hex;
		if (hex.length() == 5)
			return "000" + hex;
		if (hex.length() == 6)
			return "00" + hex;
		if (hex.length() == 7)
			return "0" + hex;
		return hex;
	}
	
	/*
	 * Return a String describing the specified
	 * transparent or background pixel.
	 */
	static String pixelInfo(int pixel) {
		if (pixel == -1) {
			return pixel + " (" + bundle.getString("None_lc") + ")";
		}
		return pixel + " (0x" + Integer.toHexString(pixel) + ")";
	}
	
	/*
	 * Return a String describing the specified disposal method.
	 */
	static String disposalString(int disposalMethod) {
		switch (disposalMethod) {
			case SWT.DM_FILL_NONE: return bundle.getString("None_lc");
			case SWT.DM_FILL_BACKGROUND: return bundle.getString("Background_lc");
			case SWT.DM_FILL_PREVIOUS: return bundle.getString("Previous_lc");
		}
		return bundle.getString("Unspecified_lc");
	}
	
	/*
	 * Return a String describing the specified image file type.
	 */
	String fileTypeString(int filetype) {
		if (filetype == SWT.IMAGE_BMP)
			return "BMP";
		if (filetype == SWT.IMAGE_BMP_RLE)
			return "RLE" + imageData.depth + " BMP";
		if (filetype == SWT.IMAGE_OS2_BMP)
			return "OS/2 BMP";
		if (filetype == SWT.IMAGE_GIF)
			return "GIF";
		if (filetype == SWT.IMAGE_ICO)
			return "ICO";
		if (filetype == SWT.IMAGE_JPEG)
			return "JPEG";
		if (filetype == SWT.IMAGE_PNG)
			return "PNG";
		if (filetype == SWT.IMAGE_TIFF)
			return "TIFF";
		return bundle.getString("Unknown_ac");
	}
	
	/*
	 * Return the specified file's image type, based on its extension.
	 * Note that this is not a very robust way to determine image type,
	 * and it is only to be used in the absence of any better method.
	 */
	int determineFileType(String filename) {
		String ext = filename.substring(filename.lastIndexOf('.') + 1);
		if (ext.equalsIgnoreCase("bmp")) {
			return showBMPDialog();
		}
		if (ext.equalsIgnoreCase("gif"))
			return SWT.IMAGE_GIF;
		if (ext.equalsIgnoreCase("ico"))
			return SWT.IMAGE_ICO;
		if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("jfif"))
			return SWT.IMAGE_JPEG;
		if (ext.equalsIgnoreCase("png"))
			return SWT.IMAGE_PNG;
		if (ext.equalsIgnoreCase("tif") || ext.equalsIgnoreCase("tiff"))
			return SWT.IMAGE_TIFF;
		return SWT.IMAGE_UNDEFINED;
	}
	
	static String createMsg(String msg, Object[] args) {
		MessageFormat formatter = new MessageFormat(msg);
		return formatter.format(args);
	}
	
	static String createMsg(String msg, Object arg) {
		MessageFormat formatter = new MessageFormat(msg);
		return formatter.format(new Object[]{arg});
	}
}
