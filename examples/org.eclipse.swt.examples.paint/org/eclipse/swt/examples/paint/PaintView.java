package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.eclipse.ui.part.*;

import java.net.*;
import java.util.*;

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

	// map action ids to useful data
	private HashMap /* of String to PaintTool */ paintToolMap;
	private HashMap /* of String to Integer */ paintFillTypeMap;
	
	/** UI data **/
	// handle of currently active tool IAction on the UI
	private IAction activeToolAction;
	// handle of currently active filltype IAction on the UI
	private IAction activeFillTypeAction;

	// handle of active foreground color box Canvas widget
	private Canvas activeForegroundColorCanvas;
	// handle of active background color box Canvas widget
	private Canvas activeBackgroundColorCanvas;
	
	private static final int numPaletteRows = 3;
	private static final int numPaletteCols = 50;

	// shared data	
	private Color paintColorBlack, paintColorWhite; // alias for paintColors[0] and [1]
	private Color[] paintColors;
	private Font paintDefaultFont; // do not free

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
		if (paintColors != null) {
			for (int i = 0; i < paintColors.length; ++i) {
				final Color color = paintColors[i];
				if (color != null) color.dispose();
			}
		}
		paintDefaultFont = null;
		paintColors = null;
		paintSurface = null;
		super.dispose();
	}
	
	/**
	 * Called when we must grab focus.
	 * 
	 * @see org.eclipse.ui.part.ViewPart#setFocus
	 */
	public void setFocus()  {
		paintSurface.setFocus();
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
		
		paintDefaultFont = workbenchDisplay.getSystemFont();

		paintColors = new Color[numPaletteCols * numPaletteRows];
		paintColors[0] = paintColorBlack;
		paintColors[1] = paintColorWhite;
		for (int i = 2; i < paintColors.length; i++) {
			paintColors[i] = new Color(workbenchDisplay,
				((i*7)%255),((i*23)%255), ((i*51)%255));
		}

		toolSettings = new ToolSettings();
		toolSettings.commonForegroundColor = paintColorBlack;
		toolSettings.commonBackgroundColor = paintColorWhite;
		toolSettings.commonFont = paintDefaultFont;

		/*** Add toolbar contributions ***/
		final IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolbarManager = actionBars.getToolBarManager();

		toolbarManager.add(new GroupMarker("group.tools"));
		toolbarManager.appendToGroup("group.tools", new SelectPaintToolAction("tool.Pencil"));
		toolbarManager.appendToGroup("group.tools", new SelectPaintToolAction("tool.Airbrush"));
		toolbarManager.appendToGroup("group.tools", new SelectPaintToolAction("tool.Line"));
		toolbarManager.appendToGroup("group.tools", new SelectPaintToolAction("tool.PolyLine"));
		toolbarManager.appendToGroup("group.tools", new SelectPaintToolAction("tool.Rectangle"));
		toolbarManager.appendToGroup("group.tools", new SelectPaintToolAction("tool.RoundedRectangle"));
		toolbarManager.appendToGroup("group.tools", new SelectPaintToolAction("tool.Ellipse"));
		toolbarManager.appendToGroup("group.tools", new SelectPaintToolAction("tool.Text"));
		toolbarManager.add(new Separator());
		toolbarManager.add(new GroupMarker("group.options"));
		toolbarManager.appendToGroup("group.options", new SelectFillTypeAction("fill.None"));
		toolbarManager.appendToGroup("group.options", new SelectFillTypeAction("fill.Outline"));
		toolbarManager.appendToGroup("group.options", new SelectFillTypeAction("fill.Solid"));
		toolbarManager.add(new Separator());
		toolbarManager.appendToGroup("group.options", new SelectFontAction("options.Font"));
		actionBars.updateActionBars();

		/*** Build GUI ***/
		createGUI(parent);

		/*** Set defaults ***/
		setPaintToolByID("tool.Pencil");
		setFillTypeByID("fill.None");
		setForegroundColor(paintColorBlack);
		setBackgroundColor(paintColorWhite);
	}

	/**
	 * Creates the GUI.
	 */
	private void createGUI(Composite parent) {
		GridLayout gridLayout;
		GridData gridData;

		/*** Create principal GUI layout elements ***/		
		Composite displayArea = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		displayArea.setLayout(gridLayout);

		// Creating these elements here avoids the need to instantiate the GUI elements
		// in strict layout order.  The natural layout ordering is an artifact of using
		// SWT layouts, but unfortunately it is not the same order as that required to
		// instantiate all of the non-GUI application elements to satisfy referential
		// dependencies.  It is possible to reorder the initialization to some extent, but
		// this can be very tedious.
		
		// paint canvas
		final Canvas paintCanvas = new Canvas(displayArea, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL |
			SWT.NO_REDRAW_RESIZE);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		paintCanvas.setLayoutData(gridData);
		paintCanvas.setBackground(paintColorWhite);
		
		// color selector frame
		final Composite colorFrame = new Composite(displayArea, SWT.NONE);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
		colorFrame.setLayoutData(gridData);

		// tool settings frame
		final Composite toolSettingsFrame = new Composite(displayArea, SWT.NONE);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
		toolSettingsFrame.setLayoutData(gridData);

		// status text
		final Text statusText = new Text(displayArea, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
		statusText.setLayoutData(gridData);

		/*** Create the remaining application elements inside the principal GUI layout elements ***/	
		// paintStatus
		paintStatus = new PaintStatus(statusText);

		// paintSurface
		paintSurface = new PaintSurface(paintCanvas, paintStatus);

		// paintToolMap
		paintToolMap = new HashMap();
		paintToolMap.put("tool.Pencil", new PencilTool(toolSettings, paintSurface));
		paintToolMap.put("tool.Airbrush", new AirbrushTool(toolSettings, paintSurface));
		paintToolMap.put("tool.Line", new LineTool(toolSettings, paintSurface));
		paintToolMap.put("tool.PolyLine", new PolyLineTool(toolSettings, paintSurface));
		paintToolMap.put("tool.Rectangle", new RectangleTool(toolSettings, paintSurface));
		paintToolMap.put("tool.RoundedRectangle", new RoundedRectangleTool(toolSettings, paintSurface));
		paintToolMap.put("tool.Ellipse", new EllipseTool(toolSettings, paintSurface));
		paintToolMap.put("tool.Text", new TextTool(toolSettings, paintSurface));
		paintToolMap.put("tool.Null", null);

		// paintFillTypeMap
		paintFillTypeMap = new HashMap();
		paintFillTypeMap.put("fill.None", new Integer(ToolSettings.ftNone));
		paintFillTypeMap.put("fill.Outline", new Integer(ToolSettings.ftOutline));
		paintFillTypeMap.put("fill.Solid", new Integer(ToolSettings.ftSolid));

		// colorFrame		
		gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		colorFrame.setLayout(gridLayout);

		// activeForegroundColorCanvas, activeBackgroundColorCanvas
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

		// paletteCanvas
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
				final int row = (y - bounds.y) * numPaletteRows / bounds.height;
				final int col = (x - bounds.x) * numPaletteCols / bounds.width;
				return paintColors[Math.min(Math.max(row * numPaletteCols + col, 0), paintColors.length - 1)];
			}
		});
		Listener refreshListener = new Listener() {
			public void handleEvent(Event e) {
				if (e.gc == null) return;
				Rectangle bounds = paletteCanvas.getClientArea();
				for (int row = 0; row < numPaletteRows; ++row) {
					for (int col = 0; col < numPaletteCols; ++col) {
						final int x = bounds.width * col / numPaletteCols;
						final int y = bounds.height * row / numPaletteRows;
						final int width = Math.max(bounds.width * (col + 1) / numPaletteCols - x, 1);
						final int height = Math.max(bounds.height * (row + 1) / numPaletteRows - y, 1);
						e.gc.setBackground(paintColors[row * numPaletteCols + col]);
						e.gc.fillRectangle(bounds.x + x, bounds.y + y, width, height);
					}
				}
			}
		};
		paletteCanvas.addListener(SWT.Resize, refreshListener);
		paletteCanvas.addListener(SWT.Paint, refreshListener);
		//paletteCanvas.redraw();
		
		// toolSettingsFrame
		gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		toolSettingsFrame.setLayout(gridLayout);

		Label label = new Label(toolSettingsFrame, SWT.NONE);
		label.setText(PaintPlugin.getResourceString("settings.AirbrushRadius.text"));

		final Scale airbrushRadiusScale = new Scale(toolSettingsFrame, SWT.HORIZONTAL);
		airbrushRadiusScale.setMinimum(5);
		airbrushRadiusScale.setMaximum(50);
		airbrushRadiusScale.setSelection(toolSettings.airbrushRadius);
		airbrushRadiusScale.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));
		airbrushRadiusScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				toolSettings.airbrushRadius = airbrushRadiusScale.getSelection();
				updateToolSettings();
			}
		});

		label = new Label(toolSettingsFrame, SWT.NONE);
		label.setText(PaintPlugin.getResourceString("settings.AirbrushIntensity.text"));

		final Scale airbrushIntensityScale = new Scale(toolSettingsFrame, SWT.HORIZONTAL);
		airbrushIntensityScale.setMinimum(1);
		airbrushIntensityScale.setMaximum(100);
		airbrushIntensityScale.setSelection(toolSettings.airbrushIntensity);
		airbrushIntensityScale.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));
		airbrushIntensityScale.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				toolSettings.airbrushIntensity = airbrushIntensityScale.getSelection();
				updateToolSettings();
			}
		});
	}
		
	/**
	 * Notifies the tool that its settings have changed.
	 */
	private void updateToolSettings() {
		final PaintTool activePaintTool = paintSurface.getPaintTool();
		if (activePaintTool == null) return;
		
		activePaintTool.endSession();
		activePaintTool.set(toolSettings);
		activePaintTool.beginSession();
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
		if (activeToolAction != null) activeToolAction.setChecked(false);
		IAction action = getActionByID(id);
		if (action != null) {
			activeToolAction = action;
			if (! action.isChecked()) action.setChecked(true);
		}
		
		final PaintTool paintTool = (PaintTool) paintToolMap.get(id);
		paintSurface.setPaintSession(paintTool);
		updateToolSettings();
	}
	
	/**
	 * Selects a filltype given its ID.
	 */
	public void setFillTypeByID(String id) {
		if (activeFillTypeAction != null) activeFillTypeAction.setChecked(false);
		IAction action = getActionByID(id);
		if (action != null) {
			activeFillTypeAction = action;
			if (! action.isChecked()) action.setChecked(true);
		}
		
		final Integer fillType = (Integer) paintFillTypeMap.get(id);
		toolSettings.commonFillType = fillType.intValue();
		updateToolSettings();		
	}

	/**
	 * Gets the IAction representing the UI toolbar button with the specified ID.
	 */
	private IAction getActionByID(String id) {
		final IActionBars actionBars = getViewSite().getActionBars();
		IToolBarManager toolbarManager = actionBars.getToolBarManager();
		ActionContributionItem contributionItem = (ActionContributionItem) toolbarManager.find(id);
		if (contributionItem == null) return null;
		return contributionItem.getAction();
	}

	/**
	 * Returns the Display.
	 * 
	 * @return the display we're using
	 */
	public Display getDisplay() {
		return workbenchDisplay;
	}
	
	/**
	 * Action set glue.
	 */
	abstract class PaintAction extends Action {
		public PaintAction(String id) {
			super();
			setId(id);

			try {
				final URL installUrl = PaintPlugin.getDefault().getDescriptor().getInstallURL();
				final URL imageUrl = new URL(installUrl, PaintPlugin.getResourceString(id + ".image"));
				setImageDescriptor(ImageDescriptor.createFromURL(imageUrl));
			} catch (MalformedURLException e) {
				PaintPlugin.logError("", e);	
			}

			setText(PaintPlugin.getResourceString(id + ".label"));
			setToolTipText(PaintPlugin.getResourceString(id + ".tooltip"));
			setDescription(PaintPlugin.getResourceString(id + ".description"));
		}
	}
	class SelectPaintToolAction extends PaintAction {
		public SelectPaintToolAction(String id) { super(id); }
		public int getStyle() { return IAction.AS_CHECK_BOX; }
		public void run() { setPaintToolByID(getId()); }
		
	}
	class SelectFillTypeAction extends PaintAction {
		public SelectFillTypeAction(String id) { super(id); }
		public int getStyle() { return IAction.AS_CHECK_BOX; }
		public void run() { setFillTypeByID(getId()); }
	}
	class SelectFontAction extends PaintAction {
		public SelectFontAction(String id) { super(id); }
		public int getStyle() { return IAction.AS_PUSH_BUTTON; }
		public void run() {
			FontDialog fontDialog = new FontDialog(paintSurface.getShell(), SWT.PRIMARY_MODAL);
			FontData[] fontDatum = toolSettings.commonFont.getFontData();
			if (fontDatum != null && fontDatum.length > 0) {
				fontDialog.setFontData(fontDatum[0]);
			}
			fontDialog.setText(PaintPlugin.getResourceString("options.Font.dialog.title"));

			paintSurface.hideRubberband();
			FontData fontData = fontDialog.open();
			paintSurface.showRubberband();
			if (fontData != null) {
				try {
					Font font = new Font(workbenchDisplay, fontData);
					toolSettings.commonFont = font;
					updateToolSettings();
				} catch (SWTException e) {
				}
			}
		}
	}
}
