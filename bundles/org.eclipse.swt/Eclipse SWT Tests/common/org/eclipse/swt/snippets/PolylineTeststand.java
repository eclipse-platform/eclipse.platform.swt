/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
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
package org.eclipse.swt.snippets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Drawing;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class PolylineTeststand {
	record NamedMethod(String name, BiConsumer<GC, int[]> consumer) {
	}

	record NamedColor(Color color, String name) {
	}

	record NamedBoolean(Boolean value, String name) {
	}

	record NamedInteger(Integer value, String name) {
	}


	private final NamedMethod POLYLINE = new NamedMethod("Polyline", (gc, values) -> gc.drawPolyline(values));
	private final NamedMethod POLYGON = new NamedMethod("Polygon", (gc, values) -> gc.drawPolygon(values));
	private final NamedMethod POLYFILL = new NamedMethod("Polygon (fill)", (gc, values) -> gc.fillPolygon(values));
	private final List<NamedMethod> methodValues = List.of(POLYLINE, POLYGON, POLYFILL);

	private final NamedColor NO_COLOR = new NamedColor(null, "None");
	private final NamedColor GRAY = new NamedColor(new Color(50, 50, 50), "Black");
	private final NamedColor RED = new NamedColor(new Color(255, 0, 0), "Red");
	private final NamedColor GREEN = new NamedColor(new Color(0, 255, 0), "Green");
	private final NamedColor BLUE = new NamedColor(new Color(0, 0, 255), "Blue");
	private final List<NamedColor> colorValues = List.of(NO_COLOR, GRAY, RED, GREEN, BLUE);

	private final NamedInteger ANTIALIAS_NONE = new NamedInteger(null, "None");
	private final NamedInteger ANTIALIAS_DEFAULT = new NamedInteger(SWT.DEFAULT, "Default");
	private final NamedInteger ANTIALIAS_OFF = new NamedInteger(SWT.OFF, "OFF");
	private final NamedInteger ANTIALIAS_ON = new NamedInteger(SWT.ON, "ON");
	private final List<NamedInteger> antialiasValues = List.of(ANTIALIAS_NONE, ANTIALIAS_DEFAULT, ANTIALIAS_OFF,
			ANTIALIAS_ON);

	private final NamedInteger CAP_NONE = new NamedInteger(null, "None");
	private final NamedInteger CAP_FLAT = new NamedInteger(SWT.CAP_FLAT, "FLAT");
	private final NamedInteger CAP_ROUND = new NamedInteger(SWT.CAP_ROUND, "ROUND");
	private final NamedInteger CAP_SQUARE = new NamedInteger(SWT.CAP_SQUARE, "SQUARE");
	private final List<NamedInteger> lineCapValues = List.of(CAP_NONE, CAP_FLAT, CAP_ROUND, CAP_SQUARE);

	private final NamedInteger JOIN_NONE = new NamedInteger(null, "None");
	private final NamedInteger JOIN_MITER = new NamedInteger(SWT.JOIN_MITER, "MITER");
	private final NamedInteger JOIN_ROUND = new NamedInteger(SWT.JOIN_ROUND, "ROUND");
	private final NamedInteger JOIN_BEVEL = new NamedInteger(SWT.JOIN_BEVEL, "JOIN_BEVEL");
	private final List<NamedInteger> lineJoinValues = List.of(JOIN_NONE, JOIN_MITER, JOIN_ROUND, JOIN_BEVEL);

	private final NamedInteger STYLE_NONE = new NamedInteger(null, "None");
	private final NamedInteger STYLE_SOLIT = new NamedInteger(SWT.LINE_SOLID, "SOLID");
	private final NamedInteger STYLE_DASH = new NamedInteger(SWT.LINE_DASH, "DASH");
	private final NamedInteger STYLE_DOT = new NamedInteger(SWT.LINE_DOT, "DOT");
	private final NamedInteger STYLE_DASHDOT = new NamedInteger(SWT.LINE_DASHDOT, "DASHDOT");
	private final NamedInteger STYLE_DASHDOTDOT = new NamedInteger(SWT.LINE_DASHDOTDOT, "DASHDOTDOT");
	private final List<NamedInteger> lineStyleValues = List.of(STYLE_NONE, STYLE_SOLIT, STYLE_DASH, STYLE_DOT,
			STYLE_DASHDOT, STYLE_DASHDOTDOT);

	private final NamedBoolean ADVANCED_NONE = new NamedBoolean(null, "None");
	private final NamedBoolean ADVANCED_FALSE = new NamedBoolean(Boolean.FALSE, "false");
	private final NamedBoolean ADVANCED_TRUE = new NamedBoolean(Boolean.TRUE, "true");
	private final List<NamedBoolean> advancedValues = List.of(ADVANCED_NONE, ADVANCED_FALSE, ADVANCED_TRUE);


	private static Group drawBox;

	private NamedMethod method = POLYLINE;
	private NamedColor backgroundColor = BLUE;
	private NamedColor forgroundColor = GRAY;
	private int width;
	private NamedInteger antialias = ANTIALIAS_NONE;
	private NamedInteger lineCab = CAP_NONE;
	private NamedInteger lineJoin = JOIN_NONE;
	private NamedInteger lineStyle = STYLE_NONE;
	private NamedBoolean advanced = ADVANCED_NONE;

	private List<Point> points = new ArrayList<>();

	public static void main(String[] args) {
		new PolylineTeststand().start();
	}

	private void start() {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Polyline teststand");
		shell.setLayout(new FillLayout());

		Composite rootComposite = new Composite(shell, SWT.NONE);
		GridLayout rootLayout = new GridLayout(2, false);
		rootComposite.setLayout(rootLayout);

		createControlBox(rootComposite);
		createDrawBox(rootComposite);

		shell.setSize(1200, 800);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private void createControlBox(Composite parent) {
		Group controlGroup = new Group(parent, SWT.NONE);
		controlGroup.setLayout(new GridLayout());
		controlGroup.setText("Controles");
		GridData layoutData = new GridData();
		layoutData.grabExcessVerticalSpace = true;
		layoutData.verticalAlignment = SWT.FILL;
		controlGroup.setLayoutData(layoutData);

		Button clearButton = new Button(controlGroup, SWT.PUSH);
		clearButton.setText("Clear");
		clearButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		clearButton.addListener(SWT.Selection, e -> {
			points.clear();
			drawBox.redraw();
		});

		createGcGroup(controlGroup);
		createMethodGroup(controlGroup);
		createWidthControl(controlGroup);
		createColorControl(controlGroup, "Background", colorValues, backgroundColor, namedColor -> backgroundColor = namedColor);
		createColorControl(controlGroup, "Forground", colorValues, forgroundColor, namedColor -> forgroundColor = namedColor);
		createNamedIntegerControl(controlGroup, "Antialise", antialiasValues, antialias,
				value -> antialias = value);
		createNamedIntegerControl(controlGroup, "Line Cap", lineCapValues, CAP_NONE, value -> lineCab = value);
		createNamedIntegerControl(controlGroup, "Line Join", lineJoinValues, JOIN_NONE, value -> lineJoin = value);
		createNamedIntegerControl(controlGroup, "Line Style", lineStyleValues, STYLE_NONE, value -> lineStyle = value);
		createAdvancedControl(controlGroup);
	}

	private void createGcGroup(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		group.setLayout(new FillLayout());
		group.setText("Used GC");

		ToolBar toolbar = new ToolBar(group, SWT.NONE);

		ToolItem nativeItem = new ToolItem(toolbar, SWT.RADIO);
		nativeItem.setText("Native");
		nativeItem.addListener(SWT.Selection, e -> {
			SWT.USE_M3 = false;
			drawBox.redraw();
		});
		ToolItem m3Item = new ToolItem(toolbar, SWT.RADIO);
		m3Item.setText("Skija");
		m3Item.addListener(SWT.Selection, e -> {
			SWT.USE_M3 = true;
			drawBox.redraw();
		});

		SWT.USE_M3 = true;
		m3Item.setSelection(true);
	}

	private void createMethodGroup(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		group.setLayout(new FillLayout());
		group.setText("Method to test");

		ToolBar toolbar = new ToolBar(group, SWT.NONE);

		for (NamedMethod namedMethod : methodValues) {
			ToolItem item = new ToolItem(toolbar, SWT.RADIO);
			item.setText(namedMethod.name);
			item.addListener(SWT.Selection, e -> {
				method = namedMethod;
				drawBox.redraw();
			});
		}
		toolbar.getItem(0).setSelection(true);

	}

	private void createColorControl(Composite parent, String name, List<NamedColor> namedColors,
			NamedColor defColor, Consumer<NamedColor> consumer) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		group.setLayout(new FillLayout());
		group.setText(name + " Color");

		ToolBar toolbar = new ToolBar(group, SWT.NONE);

		for (NamedColor namedColor : namedColors) {
			ToolItem item = new ToolItem(toolbar, SWT.RADIO);
			item.setText(namedColor.name);
			Color color = namedColor.color;
			item.setBackground(color);
			if (color != null) {
				item.setForeground(new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue()));
			}
			if (namedColor.equals(defColor)) {
				item.setSelection(true);
				consumer.accept(namedColor);
			}
			item.addListener(SWT.Selection, e -> {
				consumer.accept(namedColor);
				drawBox.redraw();
			});
		}
	}

	private void createAdvancedControl(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		group.setLayout(new FillLayout());
		group.setText("Advanced");

		ToolBar toolbar = new ToolBar(group, SWT.NONE);

		for (NamedBoolean namedBoolean : advancedValues) {
			ToolItem item = new ToolItem(toolbar, SWT.RADIO);
			if (namedBoolean.equals(advanced)) {
				item.setSelection(true);
			}
			item.setText(namedBoolean.name);
			item.addListener(SWT.Selection, e -> {
				advanced = namedBoolean;
				drawBox.redraw();
			});
		}
		toolbar.getItem(0).setSelection(true);
	}

	private void createNamedIntegerControl(Composite parent, String name, List<NamedInteger> values,
			NamedInteger defaultValue, Consumer<NamedInteger> consumer) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		group.setLayout(new FillLayout());
		group.setText(name);

		consumer.accept(defaultValue);
		ToolBar toolbar = new ToolBar(group, SWT.NONE);

		for (NamedInteger namedValue : values) {
			ToolItem item = new ToolItem(toolbar, SWT.RADIO);
			if (namedValue.equals(defaultValue)) {
				item.setSelection(true);
			}
			item.setText(namedValue.name);
			item.addListener(SWT.Selection, e -> {
				consumer.accept(namedValue);
				drawBox.redraw();
			});
		}
		toolbar.getItem(0).setSelection(true);
	}

	private void createWidthControl(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		group.setLayout(new FillLayout());
		group.setText("Line width");

		Scale scale = new Scale(group, SWT.NONE);
		scale.setMaximum(-1);
		scale.setMaximum(20);
		scale.setIncrement(1);
		scale.setPageIncrement(1);
		scale.addListener(SWT.Selection, e -> {
			width = scale.getSelection();
			if (width == -1) {
				group.setText("Line width: " + "(none)");
			} else {
				group.setText("Line width: " + width);
			}
			drawBox.redraw();
		});
	}

	private void createDrawBox(Composite parent) {
		drawBox = new Group(parent, SWT.NONE);
		drawBox.setText("Draw Area");
		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		layoutData.horizontalAlignment = SWT.FILL;
		layoutData.verticalAlignment = SWT.FILL;
		drawBox.setLayoutData(layoutData);
		drawBox.addListener(SWT.Paint, e -> {
			Drawing.drawWithGC(drawBox, e.gc, this::onPaint);
		});
		drawBox.addListener(SWT.MouseUp, e -> {
			points.add(new Point(e.x, e.y));
			drawBox.redraw();
		});
	}

	private void onPaint(GC gc) {
		int[] asIntArray = points.stream().flatMapToInt(p -> IntStream.of(p.x, p.y)).toArray();

		if (backgroundColor.color != null) {
			gc.setBackground(backgroundColor.color);
		}
		if (forgroundColor.color != null) {
			gc.setForeground(forgroundColor.color);
		}
		if (advanced.value != null) {
			gc.setAdvanced(advanced.value);
		}
		if (antialias.value != null) {
			gc.setAntialias(antialias.value);
		}
		if (width != -1) {
			gc.setLineWidth(width);
		}
		if (lineCab.value != null) {
			gc.setLineCap(lineCab.value);
		}
		if (lineJoin.value != null) {
			gc.setLineJoin(lineJoin.value);
		}
		if (lineStyle.value != null) {
			gc.setLineStyle(lineStyle.value);
		}

		method.consumer.accept(gc, asIntArray);
	}
}