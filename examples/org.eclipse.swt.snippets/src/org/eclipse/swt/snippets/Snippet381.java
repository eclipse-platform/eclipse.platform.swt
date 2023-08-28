/*******************************************************************************
 * Copyright (c) 2023 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *******************************************************************************/
package org.eclipse.swt.snippets;

import java.util.*;
import java.util.List;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This snippet demonstrates the effects of different transformations applied to
 * a graphics canvas (GC) on a pattern of drawn lines. It is particularly useful
 * for comprehension and validation of an offset calculation bug in the GC,
 * particularly concerning rotations of 45°.
 *
 * @see <a href="https://github.com/eclipse-platform/eclipse.platform.swt/issues/788">Issue 788</a>
 * @see <a href="https://github.com/eclipse-platform/eclipse.platform.swt/pull/787">PR 787</a>
 */
public class Snippet381 {
	private static final List<Composite> paintComposites = new ArrayList<>();
	private static final PaintConfiguration paintConfiguration = new PaintConfiguration();

	private static class PaintConfiguration {
		Point anchorPoint = new Point(50, 50);
		float lineWidth = 0;
		float horizontalScaling = 1f;
		float verticalScaling = 1f;
		boolean drawHorizontalLine = true;
		boolean drawVerticalLine = true;
		boolean drawDiagonalLine = true;
		boolean useAntiAliasing = false;
		int rotation = 0;
	}

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Snippet 381");
		shell.setLayout(new GridLayout(1, false));

		Composite paintComposite = new Composite(shell, SWT.NONE);
		paintComposite.setLayoutData(new GridData(400, 200));
		createPaintComposites(paintComposite);

		Composite controlsComposite = new Composite(shell, SWT.NONE);
		controlsComposite.setLayoutData(new GridData(SWT.FILL, SWT.None, true, false));
		createTransformationControls(controlsComposite);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void createPaintComposites(final Composite paintComposite) {
		paintComposite.setLayout(new GridLayout(2, true));

		Label unrotatedLabel = new Label(paintComposite, SWT.NONE);
		unrotatedLabel.setText("Without rotation:");
		unrotatedLabel.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		Label rotatedLabel = new Label(paintComposite, SWT.NONE);
		rotatedLabel.setText("With rotation:");
		rotatedLabel.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));

		Composite paintCompositeUnrotated = new Composite(paintComposite, SWT.BORDER);
		paintCompositeUnrotated.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		paintCompositeUnrotated.addListener(SWT.Paint, event -> {
			drawConfiguredLinePatternAndReferencePoint(event.gc, false);
		});
		paintComposites.add(paintCompositeUnrotated);

		Composite paintCompositeRotated = new Composite(paintComposite, SWT.BORDER);
		paintCompositeRotated.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		paintCompositeRotated.addListener(SWT.Paint, event -> {
			drawConfiguredLinePatternAndReferencePoint(event.gc, true);
		});
		paintComposites.add(paintCompositeRotated);
	}

	private static void createTransformationControls(Composite controlsComposite) {
		controlsComposite.setLayout(new GridLayout(1, false));
		Composite optionsComposite = new Composite(controlsComposite, SWT.BORDER);
		optionsComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		optionsComposite.setLayout(new GridLayout(2, true));
		createBooleanValueControl(optionsComposite, "Draw horizontal line", paintConfiguration.drawHorizontalLine,
				newValue -> paintConfiguration.drawHorizontalLine = newValue);
		createBooleanValueControl(optionsComposite, "Draw vertical line", paintConfiguration.drawVerticalLine,
				newValue -> paintConfiguration.drawVerticalLine = newValue);
		createBooleanValueControl(optionsComposite, "Draw diagonal line", paintConfiguration.drawDiagonalLine,
				newValue -> paintConfiguration.drawDiagonalLine = newValue);
		createBooleanValueControl(optionsComposite, "Use anti aliasing", paintConfiguration.useAntiAliasing,
				newValue -> paintConfiguration.useAntiAliasing = newValue);
		Composite valuesComposite = new Composite(controlsComposite, SWT.NONE);
		valuesComposite.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		valuesComposite.setLayout(new GridLayout(2, false));
		createFloatValueControl(valuesComposite, "Line Width", paintConfiguration.lineWidth,
				newValue -> paintConfiguration.lineWidth = newValue);
		createFloatValueControl(valuesComposite, "Horizontal Scaling", paintConfiguration.horizontalScaling,
				newValue -> paintConfiguration.horizontalScaling = newValue);
		createFloatValueControl(valuesComposite, "Vertical Scaling", paintConfiguration.verticalScaling,
				newValue -> paintConfiguration.verticalScaling = newValue);
		createIntValueControl(valuesComposite, "Rotation", paintConfiguration.rotation,
				newValue -> paintConfiguration.rotation = newValue);
	}

	private static void createBooleanValueControl(Composite container, String valueName, boolean initialValue,
			Consumer<Boolean> valueUpdater) {
		Button valueControl = new Button(container, SWT.CHECK);
		valueControl.setText(valueName);
		valueControl.setSelection(initialValue);
		valueControl.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		valueControl.addListener(SWT.Selection, e -> {
			try {
				valueUpdater.accept(valueControl.getSelection());
				paintComposites.forEach(Composite::redraw);
			} catch (Exception ex) {
			}
		});
	}

	private static void createFloatValueControl(Composite container, String valueName, float initialValue,
			Consumer<Float> valueUpdater) {
		Text valueControl = createNumberValueControl(container, valueName, initialValue + "");
		valueControl.addListener(SWT.CHANGED, e -> {
			try {
				float newValue = Float.parseFloat(valueControl.getText().trim());
				valueUpdater.accept(newValue);
				paintComposites.forEach(Composite::redraw);
			} catch (Exception ex) {
			}
		});
	}

	private static void createIntValueControl(Composite container, String valueName, int initialValue,
			Consumer<Integer> valueUpdater) {
		Text valueControl = createNumberValueControl(container, valueName, initialValue + "");
		valueControl.addListener(SWT.CHANGED, e -> {
			try {
				int newValue = Integer.parseInt(valueControl.getText().trim());
				valueUpdater.accept(newValue);
				paintComposites.forEach(Composite::redraw);
			} catch (Exception ex) {
			}
		});
	}

	private static Text createNumberValueControl(Composite container, String valueName, String initialValue) {
		Label label = new Label(container, SWT.NONE);
		label.setText(valueName + ":");
		Text valueControl = new Text(container, SWT.BORDER);
		valueControl.setText(initialValue);
		valueControl.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		return valueControl;
	}

	private static void drawConfiguredLinePatternAndReferencePoint(GC gc, boolean applyRotation) {
		drawLinePattern(gc, applyRotation);
		drawAnchorPoint(gc);
	}

	private static void drawLinePattern(GC gc, boolean applyRotation) {
		if (paintConfiguration.useAntiAliasing) {
			gc.setAntialias(SWT.ON);
		} else {
			gc.setAntialias(SWT.OFF);
		}
		gc.setLineAttributes(new LineAttributes(paintConfiguration.lineWidth));
		Transform transform = new Transform(Display.getCurrent());
		transform.translate(paintConfiguration.anchorPoint.x, paintConfiguration.anchorPoint.y);
		transform.scale(paintConfiguration.horizontalScaling, paintConfiguration.verticalScaling);
		if (applyRotation) {
			transform.rotate(paintConfiguration.rotation);
		}
		gc.setTransform(transform);
		int offsetX = paintConfiguration.anchorPoint.x - 50;
		int offsetY = paintConfiguration.anchorPoint.y - 50;
		if (paintConfiguration.drawHorizontalLine) {
			gc.drawLine(offsetX, offsetY, offsetX + 30, offsetX + 0);
		}
		if (paintConfiguration.drawDiagonalLine) {
			gc.drawLine(offsetX, offsetY, offsetX + 30, offsetX + 30);
		}
		if (paintConfiguration.drawVerticalLine) {
			gc.drawLine(offsetX, offsetY, offsetX, offsetY + 30);
		}
	}

	private static void drawAnchorPoint(GC gc) {
		gc.setTransform(new Transform(Display.getCurrent()));
		gc.setForeground(new Color(255, 0, 0));
		gc.drawPoint(paintConfiguration.anchorPoint.x, paintConfiguration.anchorPoint.y);
	}

}
