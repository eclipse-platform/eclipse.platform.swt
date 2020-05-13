/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.eclipse.swt.examples.paint;


import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class PaintExample {
	private static ResourceBundle resourceBundle =
		ResourceBundle.getBundle("examples_paint");
	private Composite mainComposite;
	private Canvas activeForegroundColorCanvas;
	private Canvas activeBackgroundColorCanvas;
	private Color paintColorBlack, paintColorWhite; // alias for paintColors[0] and [1]
	private Color[] paintColors;
	private Font paintDefaultFont; // do not free
	private static final int numPaletteRows = 3;
	private static final int numPaletteCols = 50;
	private ToolSettings toolSettings; // current active settings
	private PaintSurface paintSurface; // paint surface for drawing

	static final int Pencil_tool = 0;
	static final int Airbrush_tool = 1;
	static final int Line_tool = 2;
	static final int PolyLine_tool = 3;
	static final int Rectangle_tool = 4;
	static final int RoundedRectangle_tool = 5;
	static final int Ellipse_tool = 6;
	static final int Text_tool = 7;
	static final int None_fill = 8;
	static final int Outline_fill = 9;
	static final int Solid_fill = 10;
	static final int Solid_linestyle = 11;
	static final int Dash_linestyle = 12;
	static final int Dot_linestyle = 13;
	static final int DashDot_linestyle = 14;
	static final int Font_options = 15;

	static final int Default_tool = Pencil_tool;
	static final int Default_fill = None_fill;
	static final int Default_linestyle = Solid_linestyle;

	public static final Tool[] tools = {
		new Tool(Pencil_tool, "Pencil", "tool", SWT.RADIO),
		new Tool(Airbrush_tool, "Airbrush", "tool", SWT.RADIO),
		new Tool(Line_tool, "Line", "tool", SWT.RADIO),
		new Tool(PolyLine_tool, "PolyLine", "tool", SWT.RADIO),
		new Tool(Rectangle_tool, "Rectangle", "tool", SWT.RADIO),
		new Tool(RoundedRectangle_tool, "RoundedRectangle", "tool", SWT.RADIO),
		new Tool(Ellipse_tool, "Ellipse", "tool", SWT.RADIO),
		new Tool(Text_tool, "Text", "tool", SWT.RADIO),
		new Tool(None_fill, "None", "fill", SWT.RADIO, Integer.valueOf(ToolSettings.ftNone)),
		new Tool(Outline_fill, "Outline", "fill", SWT.RADIO, Integer.valueOf(ToolSettings.ftOutline)),
		new Tool(Solid_fill, "Solid", "fill", SWT.RADIO, Integer.valueOf(ToolSettings.ftSolid)),
		new Tool(Solid_linestyle, "Solid", "linestyle", SWT.RADIO, Integer.valueOf(SWT.LINE_SOLID)),
		new Tool(Dash_linestyle, "Dash", "linestyle", SWT.RADIO, Integer.valueOf(SWT.LINE_DASH)),
		new Tool(Dot_linestyle, "Dot", "linestyle", SWT.RADIO, Integer.valueOf(SWT.LINE_DOT)),
		new Tool(DashDot_linestyle, "DashDot", "linestyle", SWT.RADIO, Integer.valueOf(SWT.LINE_DASHDOT)),
		new Tool(Font_options, "Font", "options", SWT.PUSH)
	};

	/**
	 * Creates an instance of a PaintExample embedded inside
	 * the supplied parent Composite.
	 *
	 * @param parent the container of the example
	 */
	public PaintExample(Composite parent) {
		mainComposite = parent;
		initResources();
		initActions();
		init();
	}

	/**
	 * Invokes as a standalone program.
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText(getResourceString("window.title"));
		shell.setLayout(new GridLayout());
		PaintExample instance = new PaintExample(shell);
		instance.createToolBar(shell);
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		instance.createGUI(composite);
		instance.setDefaults();
		setShellSize(display, shell);
		shell.open();
		while (! shell.isDisposed()) {
			if (! display.readAndDispatch()) display.sleep();
		}
		instance.dispose();
	}

	/**
	 * Creates the toolbar.
	 * Note: Only called by standalone.
	 */
	private void createToolBar(Composite parent) {
		ToolBar toolbar = new ToolBar (parent, SWT.NONE);
		String group = null;
		for (int i = 0; i < tools.length; i++) {
			Tool tool = tools[i];
			if (group != null && !tool.group.equals(group)) {
				new ToolItem (toolbar, SWT.SEPARATOR);
			}
			group = tool.group;
			ToolItem item = addToolItem(toolbar, tool);
			if (i == Default_tool || i == Default_fill || i == Default_linestyle) item.setSelection(true);
		}
	}

	/**
	 * Adds a tool item to the toolbar.
	 * Note: Only called by standalone.
	 */
	private ToolItem addToolItem(final ToolBar toolbar, final Tool tool) {
		final String id = tool.group + '.' + tool.name;
		ToolItem item = new ToolItem (toolbar, tool.type);
		item.setText (getResourceString(id + ".label"));
		item.setToolTipText(getResourceString(id + ".tooltip"));
		item.setImage(tool.image);
		item.addSelectionListener(widgetSelectedAdapter(e -> tool.action.run()));
		final int childID = toolbar.indexOf(item);
		toolbar.getAccessible().addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getName(org.eclipse.swt.accessibility.AccessibleEvent e) {
				if (e.childID == childID) {
					e.result = getResourceString(id + ".description");
				}
			}
		});
		return item;
	}

	/**
	 * Sets the default tool item states.
	 */
	public void setDefaults() {
		setPaintTool(Default_tool);
		setFillType(Default_fill);
		setLineStyle(Default_linestyle);
		setForegroundColor(paintColorBlack);
		setBackgroundColor(paintColorWhite);
	}

	/**
	 * Creates the GUI.
	 */
	public void createGUI(Composite parent) {
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
			SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND);
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
		// paintSurface
		paintSurface = new PaintSurface(paintCanvas, statusText, paintColorWhite);

		// finish initializing the tool data
		tools[Pencil_tool].data = new PencilTool(toolSettings, paintSurface);
		tools[Airbrush_tool].data = new AirbrushTool(toolSettings, paintSurface);
		tools[Line_tool].data = new LineTool(toolSettings, paintSurface);
		tools[PolyLine_tool].data = new PolyLineTool(toolSettings, paintSurface);
		tools[Rectangle_tool].data = new RectangleTool(toolSettings, paintSurface);
		tools[RoundedRectangle_tool].data = new RoundedRectangleTool(toolSettings, paintSurface);
		tools[Ellipse_tool].data = new EllipseTool(toolSettings, paintSurface);
		tools[Text_tool].data = new TextTool(toolSettings, paintSurface);

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
		final Canvas paletteCanvas = new Canvas(colorFrame, SWT.BORDER | SWT.NO_BACKGROUND);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 24;
		paletteCanvas.setLayoutData(gridData);
		paletteCanvas.addListener(SWT.MouseDown, new Listener() {
			@Override
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
		Listener refreshListener = e -> {
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
		label.setText(getResourceString("settings.AirbrushRadius.text"));

		final Scale airbrushRadiusScale = new Scale(toolSettingsFrame, SWT.HORIZONTAL);
		airbrushRadiusScale.setMinimum(5);
		airbrushRadiusScale.setMaximum(50);
		airbrushRadiusScale.setSelection(toolSettings.airbrushRadius);
		airbrushRadiusScale.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));
		airbrushRadiusScale.addSelectionListener(widgetSelectedAdapter(e -> {
			toolSettings.airbrushRadius = airbrushRadiusScale.getSelection();
			updateToolSettings();
		}));

		label = new Label(toolSettingsFrame, SWT.NONE);
		label.setText(getResourceString("settings.AirbrushIntensity.text"));

		final Scale airbrushIntensityScale = new Scale(toolSettingsFrame, SWT.HORIZONTAL);
		airbrushIntensityScale.setMinimum(1);
		airbrushIntensityScale.setMaximum(100);
		airbrushIntensityScale.setSelection(toolSettings.airbrushIntensity);
		airbrushIntensityScale.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));
		airbrushIntensityScale.addSelectionListener(widgetSelectedAdapter(e -> {
			toolSettings.airbrushIntensity = airbrushIntensityScale.getSelection();
			updateToolSettings();
		}));
	}

	/**
	 * Disposes of all resources associated with a particular
	 * instance of the PaintExample.
	 */
	public void dispose() {
		if (paintSurface != null) paintSurface.dispose();
		if (paintColors != null) {
			for (final Color color : paintColors) {
				if (color != null) color.dispose();
			}
		}
		paintDefaultFont = null;
		paintColors = null;
		paintSurface = null;
		freeResources();
	}

	/**
	 * Frees the resource bundle resources.
	 */
	public void freeResources() {
		for (Tool tool : tools) {
			final Image image = tool.image;
			if (image != null) image.dispose();
			tool.image = null;
		}
	}

	/**
	 * Returns the Display.
	 *
	 * @return the display we're using
	 */
	public Display getDisplay() {
		return mainComposite.getDisplay();
	}

	/**
	 * Gets a string from the resource bundle.
	 * We don't want to crash because of a missing String.
	 * Returns the key if not found.
	 */
	public static String getResourceString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}
	}

	/**
	 * Gets a string from the resource bundle and binds it
	 * with the given arguments. If the key is not found,
	 * return the key.
	 */
	public static String getResourceString(String key, Object[] args) {
		try {
			return MessageFormat.format(getResourceString(key), args);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}
	}

	/**
	 * Initialize colors, fonts, and tool settings.
	 */
	private void init() {
		Display display = mainComposite.getDisplay();

		paintColorWhite = new Color(255, 255, 255);
		paintColorBlack = new Color(0, 0, 0);

		paintDefaultFont = display.getSystemFont();

		paintColors = new Color[numPaletteCols * numPaletteRows];
		paintColors[0] = paintColorBlack;
		paintColors[1] = paintColorWhite;
		for (int i = 2; i < paintColors.length; i++) {
			paintColors[i] = new Color(((i*7)%255),
				((i*23)%255),((i*51)%255));
		}

		toolSettings = new ToolSettings();
		toolSettings.commonForegroundColor = paintColorBlack;
		toolSettings.commonBackgroundColor = paintColorWhite;
		toolSettings.commonFont = paintDefaultFont;
	}

	/**
	 * Sets the action field of the tools
	 */
	private void initActions() {
		for (Tool tool2 : tools) {
			final Tool tool = tool2;
			String group = tool.group;
			if (group.equals("tool")) {
				tool.action = () -> setPaintTool(tool.id);
			} else if (group.equals("fill")) {
				tool.action = () -> setFillType(tool.id);
			} else if (group.equals("linestyle")) {
				tool.action = () -> setLineStyle(tool.id);
			} else if (group.equals("options")) {
				tool.action = () -> {
					FontDialog fontDialog = new FontDialog(paintSurface.getShell(), SWT.PRIMARY_MODAL);
					FontData[] fontDatum = toolSettings.commonFont.getFontData();
					if (fontDatum != null && fontDatum.length > 0) {
						fontDialog.setFontList(fontDatum);
					}
					fontDialog.setText(getResourceString("options.Font.dialog.title"));

					paintSurface.hideRubberband();
					FontData fontData = fontDialog.open();
					paintSurface.showRubberband();
					if (fontData != null) {
						try {
							Font font = new Font(mainComposite.getDisplay(), fontData);
							toolSettings.commonFont = font;
							updateToolSettings();
						} catch (SWTException ex) {
						}
					}
				};
			}
		}
	}

	/**
	 * Loads the image resources.
	 */
	public void initResources() {
		final Class<PaintExample> clazz = PaintExample.class;
		if (resourceBundle != null) {
			try {
				for (Tool tool2 : tools) {
					Tool tool = tool2;
					String id = tool.group + '.' + tool.name;
					try (InputStream sourceStream = clazz.getResourceAsStream(getResourceString(id + ".image"))) {
						ImageData source = new ImageData(sourceStream);
						ImageData mask = source.getTransparencyMask();
						tool.image = new Image(null, source, mask);
					}
				}
				return;
			} catch (Throwable t) {
			}
		}
		String error = (resourceBundle != null) ?
			getResourceString("error.CouldNotLoadResources") :
			"Unable to load resources";
		freeResources();
		throw new RuntimeException(error);
	}

	/**
	 * Grabs input focus.
	 */
	public void setFocus() {
		mainComposite.setFocus();
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
	public void setPaintTool(int id) {
		PaintTool paintTool = (PaintTool) tools[id].data;
		paintSurface.setPaintSession(paintTool);
		updateToolSettings();
	}

	/**
	 * Selects a filltype given its ID.
	 */
	public void setFillType(int id) {
		Integer fillType = (Integer) tools[id].data;
		toolSettings.commonFillType = fillType.intValue();
		updateToolSettings();
	}

	/**
	 * Selects line type given its ID.
	 */
	public void setLineStyle(int id) {
		Integer lineType = (Integer) tools[id].data;
		toolSettings.commonLineStyle = lineType.intValue();
		updateToolSettings();
	}

	/**
	 * Sets the size of the shell to it's "packed" size,
	 * unless that makes it bigger than the display,
	 * in which case set it to 9/10 of display size.
	 */
	private static void setShellSize (Display display, Shell shell) {
		Rectangle bounds = display.getBounds();
		Point size = shell.computeSize (SWT.DEFAULT, SWT.DEFAULT);
		if (size.x > bounds.width) size.x = bounds.width * 9 / 10;
		if (size.y > bounds.height) size.y = bounds.height * 9 / 10;
		shell.setSize (size);
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
}

