package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.util.*;import org.eclipse.jface.action.*;import org.eclipse.swt.*;import org.eclipse.swt.graphics.*;import org.eclipse.swt.layout.*;import org.eclipse.swt.widgets.*;import org.eclipse.ui.part.*;

/**
 * The view for the paint application.
 * All rendering happens inside the area created by createPartControl().
 * 
 * @see ViewPart
 */
public class PaintView extends ViewPart {
	private Display workbenchDisplay;

	// current active settings
	private ToolSettings toolSettings;

	// paint surface for drawing
	private PaintSurface paintSurface;

	// status information
	private PaintStatus paintStatus;

	// array of paint tools
	private HashMap paintToolMap;
	
	/** Cached UI data **/
	// handle of currently active IAction on the UI
	private IAction activeAction;
	// handle of active foreground color box Canvas widget
	private Canvas activeForegroundColorCanvas;
	// handle of active background color box Canvas widget
	private Canvas activeBackgroundColorCanvas;

	// shared data	
	Color paintColorWhite, paintColorBlack;
	Color[] paintColors;

	/**
	 * Constructs a Paint view.
	 */
	public PaintView() {
	}

	/**
	 * Cleanup
	 */
	public void dispose() {
		if (paintSurface != null) paintSurface.dispose();
		paintSurface = null;
		
		if (paintColorWhite != null) paintColorWhite.dispose();
		paintColorWhite = null;
		if (paintColorBlack != null) paintColorBlack.dispose();
		paintColorBlack = null;
		
		if (paintColors != null) {
			for (int i = 0; i < paintColors.length; ++i) {
				final Color color = paintColors[i];
				if (color != null) color.dispose();
			}
			paintColors = null;
		}
		
		if (toolSettings != null) toolSettings.dispose();
		toolSettings = null;

		super.dispose();
	}
	
	/**
	 * Called when frame obtains focus.
	 * 
	 * @see ViewPart#setFocus
	 */
	public void setFocus()  {
	}

	/**
	 * Creates the example.
	 * 
	 * @see ViewPart#createPartControl
	 */
	public void createPartControl(Composite parent) {
		/*** Initialize shared data ***/
		workbenchDisplay = parent.getDisplay();
		
		paintColorWhite = new Color(workbenchDisplay, 255, 255, 255);
		paintColorBlack = new Color(workbenchDisplay, 0, 0, 0);

		paintColors = new Color[82];
		for (int i = 0; i < 82; i++) {
			paintColors[i] = new Color(workbenchDisplay,
				((i*7)%255),((i*23)%255), ((i*51)%255));
		}

		toolSettings = new ToolSettings();
		toolSettings.commonForegroundColor = paintColorBlack;
		toolSettings.commonBackgroundColor = paintColorWhite;

		/*** Build GUI ***/
		createGUI(parent);

		/*** Set defaults ***/
		setForegroundColor(paintColorBlack);
		setBackgroundColor(paintColorWhite);
	}

	/**
	 * Creates the GUI.
	 */
	private void createGUI(Composite parent) {
		GridLayout gridLayout;
		GridData gridData;

		/*** Create principal GUI elements ***/		
		Composite displayArea = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		displayArea.setLayout(gridLayout);

		// paint canvas
		final Canvas paintCanvas = new Canvas(displayArea, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		paintCanvas.setLayoutData(gridData);
		paintCanvas.setBackground(paintColorWhite);
		
		// color selector frame
		final Composite colorFrame = new Composite(displayArea, SWT.NONE);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		colorFrame.setLayoutData(gridData);

		// status text
		final Text statusText = new Text(displayArea, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		statusText.setLayoutData(gridData);

		/*** Create program elements that depend on GUI ***/
		// paintStatus
		paintStatus = new PaintStatus(statusText);

		// paintSurface
		paintSurface = new PaintSurface(paintCanvas, paintStatus);

		// paintToolMap
		paintToolMap = new HashMap();
		paintToolMap.put("org.eclipse.swt.examples.paint.toolPencil", 
			new PencilTool(toolSettings, paintSurface));
		paintToolMap.put("org.eclipse.swt.examples.paint.toolAirbrush",
			new AirbrushTool(toolSettings, paintSurface));
		paintToolMap.put("org.eclipse.swt.examples.paint.toolLine",
			new LineTool(toolSettings, paintSurface));
		paintToolMap.put("org.eclipse.swt.examples.paint.toolPolyLine",
			new PolyLineTool(toolSettings, paintSurface));
		paintToolMap.put("org.eclipse.swt.examples.paint.toolRectangle",
			new RectangleTool(toolSettings, paintSurface));
		paintToolMap.put("org.eclipse.swt.examples.paint.toolFilledRectangle",
			new FilledRectangleTool(toolSettings, paintSurface));
		paintToolMap.put("org.eclipse.swt.examples.paint.toolEllipse",
			new EllipseTool(toolSettings, paintSurface));
		paintToolMap.put("org.eclipse.swt.examples.paint.toolNull", null);
		
		// activeForegroundColorCanvas, activeBackgroundColorCanvas
		gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		colorFrame.setLayout(gridLayout);

		activeForegroundColorCanvas = new Canvas(colorFrame, SWT.BORDER);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.heightHint = 24;
		gridData.widthHint = 24;
		activeForegroundColorCanvas.setLayoutData(gridData);

		activeBackgroundColorCanvas = new Canvas(colorFrame, SWT.BORDER);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.heightHint = 24;
		gridData.widthHint = 24;
		activeBackgroundColorCanvas.setLayoutData(gridData);

		final Canvas paletteCanvas = new Canvas(colorFrame, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 24;
		paletteCanvas.setLayoutData(gridData);
		paletteCanvas.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event e) {
				Rectangle bounds = paletteCanvas.getClientArea();
				Color color = getColorAt(bounds, e.x, e.y);				
					
				if (e.button == 1) setForegroundColor(color);
				else setBackgroundColor(color);
			}
			private Color getColorAt(Rectangle bounds, int x, int y) {
				if (bounds.height <= 1 && bounds.width <= 1) return paintColorWhite;
				final int row = (y - bounds.y) * 2 / bounds.height;
				final int col = (x - bounds.x) * 41 / bounds.width;
				return paintColors[Math.min(Math.max(row * 41 + col, 0), paintColors.length - 1)];
			}
		});
		Listener refreshListener = new Listener() {
			public void handleEvent(Event e) {
				if (e.gc == null) return;
				Rectangle bounds = paletteCanvas.getClientArea();
				for (int row = 0; row < 2; ++row) {
					for (int col = 0; col < 41; ++col) {
						final int x = bounds.width * col / 41;
						final int y = bounds.height * row / 2;
						final int width = Math.max(bounds.width * (col + 1) / 41 - x, 1);
						final int height = Math.max(bounds.height * (row + 1) / 2 - y, 1);
						e.gc.setBackground(paintColors[row * 41 + col]);
						e.gc.fillRectangle(bounds.x + x, bounds.y + y, width, height);
					}
				}
			}
		};
		paletteCanvas.addListener(SWT.Resize, refreshListener);
		paletteCanvas.addListener(SWT.Paint, refreshListener);
		paletteCanvas.redraw();
	}
		
	/**
	 * Notifies the tool that its settings have changed.
	 */
	private void updateToolSettings() {
		final PaintTool activePaintTool = paintSurface.getPaintTool();
		if (activePaintTool == null) return;
		
		activePaintTool.set(toolSettings);
		activePaintTool.resetSession();
	}

	/**
	 * Sets the tool foreground color.
	 * 
	 * @param color the new color to use
	 */
	public void setForegroundColor(Color color) {
		if (activeForegroundColorCanvas != null)
			activeForegroundColorCanvas.setBackground(color);
		toolSettings.commonForegroundColor = color;
		updateToolSettings();
	}

	/**
	 * Set the tool background color.
	 * 
	 * @param color the new color to use
	 */
	public void setBackgroundColor(Color color) {
		if (activeBackgroundColorCanvas != null)
			activeBackgroundColorCanvas.setBackground(color);
		toolSettings.commonBackgroundColor = color;
		updateToolSettings();
	}

	/**
	 * Selects a tool given its ID.
	 */
	public void setPaintToolByID(String id) {
		final PaintTool paintTool = (PaintTool) paintToolMap.get(id);
		paintSurface.setPaintSession(paintTool);
		updateToolSettings();
	}
	
	/**
	 * Selects a tool given its UI Action.
	 */
	public void setPaintToolByAction(IAction action) {
		if (activeAction != null) activeAction.setChecked(false);
		activeAction = action;
		if (! action.isChecked()) action.setChecked(true);
		
		setPaintToolByID(action.getId());
	}
	
	/**
	 * Returns the Display.
	 * 
	 * @return the display we're using
	 */
	public Display getDisplay() {
		return workbenchDisplay;
	}
}
