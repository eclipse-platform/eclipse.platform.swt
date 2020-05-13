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
package org.eclipse.swt.tests.junit.performance;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.test.performance.Dimension;
import org.eclipse.test.performance.Performance;
import org.eclipse.test.performance.PerformanceMeter;
import org.junit.Before;
import org.junit.Test;

/**
 * Automated Performance Test Suite for class org.eclipse.swt.graphics.Color
 *
 * @see org.eclipse.swt.graphics.Color
 */
public class Test_situational extends SwtPerformanceTestCase {

@Before
public void setUp() {
	display = Display.getDefault();
}

/**
 * Situations:
 *
 * - Widget creation
 * - syncExec/asyncExec performance
 * - Image creation
 * - Drawing operations
 * - String measuring
 * - String drawing
 * - Region operations
 * - Fonts
 * - Image loading
 * - Layouts
 */
@Test
public void test_createComposites() {
	PerformanceMeter meter = createMeter("Create composites");
	int samples;

	Performance.getDefault();

	// Warm up.
	for(samples = 0; samples < 2; samples++) {
		Shell shell = new Shell(display);
		for (int i = 0; i < 100; i++) {
			Composite c = new Composite(shell, SWT.NONE);
			for (int j = 0; j < 10; j++) {
				new Composite(c, SWT.NONE);
			}
		}
		shell.dispose();
		while(display.readAndDispatch()){/*empty*/}
	}

	for(samples = 0; samples < 100; samples++) {
		Shell shell = new Shell(display);
		meter.start();
		for (int i = 0; i < 100; i++) {
			Composite c = new Composite(shell, SWT.NONE);
			for (int j = 0; j < 50; j++) {
				new Composite(c, SWT.NONE);
			}
		}
		meter.stop();
		shell.dispose();
		while(display.readAndDispatch()){/*empty*/}
	}
	disposeMeter(meter);
}

@Test
public void test_createWidgets() {
	PerformanceMeter meter = createMeter("Create composites and widgets");
	int samples;

	Performance performance = Performance.getDefault();
	performance.tagAsGlobalSummary(meter, "Create composites and widgets", Dimension.ELAPSED_PROCESS);

	for(samples = 0; samples < 10; samples++) {
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		meter.start();
		for (int i = 0; i < 50; i++) {
			Composite c = new Composite(shell, SWT.NONE);
			for (int j = 0; j < 10; j++) {
				new Button(c, SWT.PUSH);
				new Label(c, SWT.NONE);
				new Scale(c, SWT.NONE);
				new ProgressBar(c, SWT.NONE);
				new List(c, SWT.NONE);
				new Text(c, SWT.SINGLE);
				new Text(c, SWT.MULTI);
				new Slider(c, SWT.NONE);
				new Tree(c, SWT.NONE);
				new Table(c, SWT.NONE);
				new TabFolder(c, SWT.NONE);
				new Group(c, SWT.BORDER);
				new Composite(c, SWT.NONE);
			}
		}
		meter.stop();
		shell.dispose();
		while(display.readAndDispatch()){/*empty*/}
	}
	disposeMeter(meter);
}

@Test
public void test_layout() {
	PerformanceMeter meter = createMeter("Layout Composites");
	int samples;

	for(samples = 0; samples < 10; samples++) {
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		String curText = "";
		Label changedLabel;
		Composite parent = shell;
		GridData data;

		for(int i = 0; i < 10; i++) {
			Composite c = new Composite(parent, SWT.BORDER);
			data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			data.verticalAlignment = GridData.FILL;
			c.setLayoutData(data);
			c.setLayout(new GridLayout(2, false));

			Composite c1 = new Composite(c, SWT.BORDER);
			data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			data.verticalAlignment = GridData.FILL;
			data.widthHint = data.heightHint = 2;
			c1.setLayoutData(data);

			Composite c2 = new Composite(c, SWT.BORDER);
			data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			data.verticalAlignment = GridData.FILL;
			data.widthHint = data.heightHint = 2;
			c2.setLayoutData(data);

			Composite c3 = new Composite(c, SWT.BORDER);
			data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			data.verticalAlignment = GridData.FILL;
			data.widthHint = data.heightHint = 2;
			c3.setLayoutData(data);

			Composite c4 = new Composite(c, SWT.BORDER);
			data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			data.verticalAlignment = GridData.FILL;
			c4.setLayoutData(data);
			c4.setLayout(new GridLayout());
			parent = c4;
		}
		changedLabel = new Label(parent, SWT.NONE);
		data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		changedLabel.setLayoutData(data);

		shell.open();
		while(display.readAndDispatch()){/*empty*/}
		try { Thread.sleep(2000); } catch (Exception e) {}
		while(display.readAndDispatch()){/*empty*/}
		meter.start();
		for(int numlayouts = 0; numlayouts < 20; numlayouts++) {
			shell.layout(true);
			curText = "!!!" + curText + "!!!\n!";
			changedLabel.setText(curText);
			//while(display.readAndDispatch());
		}
		meter.stop();
		shell.dispose();
		while(display.readAndDispatch()){/*empty*/}
	}
	disposeMeter(meter);
}

@Test
public void test_imageDrawing() {
	PerformanceMeter meter = createMeter("Draw on an image");
	int samples;

	for(samples = 0; samples < 10; samples++) {
		int width = 640;
		int height = 480;
		Image image = new Image(display, width, height);
		Color color1 = new Color(0xff, 0, 0xff);
		Color color2 = new Color(0, 0xff, 0xff);
		int x1 = 0, y1 = height/2, x2 = width/2, y2 = 0;
		meter.start();
		GC gc = new GC(image);
		for(int i = 0; i < 10000; i++) {
			x1 = (x1 + 5) % width; y1 = (y1 + 5) % height; x2 = (x2 + 5) % width; y2 = (y2 + 5) % height;
			gc.setLineStyle(SWT.LINE_SOLID);
			gc.drawLine(x1, y1, x2, y2);
			gc.setForeground((i & 1) == 0 ? color1 : color2);
			gc.setBackground((i & 1) == 0 ? color1 : color2);
			gc.fillRectangle(x1, y1, 200, 200);
			gc.drawRoundRectangle(x2, y2, 200, 200, 50, 50);
			gc.setLineStyle(SWT.LINE_DASHDOT);
			gc.drawLine(x2, y1, x1, y2);
		}
		gc.dispose();
		meter.stop();
		image.dispose();
		color1.dispose();
		color2.dispose();
		while(display.readAndDispatch()){/*empty*/}
	}
	disposeMeter(meter);
}

@Test
public void test_windowDrawing() {
	PerformanceMeter meter = createMeter("Draw on a window");
	Performance performance= Performance.getDefault();
	performance.setComment(meter,
			Performance.EXPLAINS_DEGRADATION_COMMENT,
			"Regression due to a issue on the Releng test machine");

	int samples;

	for(samples = 0; samples < 10; samples++) {
		int width = 640;
		int height = 480;
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		Canvas c = new Canvas(shell, SWT.NONE);
		GridData data = new GridData();
		data.widthHint = width;
		data.heightHint = height;
		c.setLayoutData(data);
		shell.pack();
		shell.open();
		while(display.readAndDispatch()){/*empty*/}
		try { Thread.sleep(2000); } catch (Exception e) {}
		while(display.readAndDispatch()){/*empty*/}
		Color color1 = new Color(0xff, 0, 0xff);
		Color color2 = new Color(0, 0xff, 0xff);
		int x1 = 0, y1 = height/2, x2 = width/2, y2 = 0;
		meter.start();
		GC gc = new GC(c);
		for(int i = 0; i < 6000; i++) {
			x1 = (x1 + 5) % width; y1 = (y1 + 5) % height; x2 = (x2 + 5) % width; y2 = (y2 + 5) % height;
			gc.setLineStyle(SWT.LINE_SOLID);
			gc.drawLine(x1, y1, x2, y2);
			gc.setForeground((i & 1) == 0 ? color1 : color2);
			gc.setBackground((i & 1) == 0 ? color1 : color2);
			gc.fillRectangle(x1, y1, 200, 200);
			gc.drawRoundRectangle(x2, y2, 200, 200, 50, 50);
			gc.setLineStyle(SWT.LINE_DASHDOT);
			gc.drawLine(x2, y1, x1, y2);
		}
		gc.dispose();
		meter.stop();
		shell.dispose();
		color1.dispose();
		color2.dispose();
		while(display.readAndDispatch()){/*empty*/}
	}
	disposeMeter(meter);
}

@Test
public void test_stringDrawing() {
	PerformanceMeter meter = createMeterWithoutSummary("Draw strings using GC.drawText");
	int samples;

	for(samples = 0; samples < 10; samples++) {
		int width = 640;
		int height = 480;
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		Canvas c = new Canvas(shell, SWT.NONE);
		GridData data = new GridData();
		data.widthHint = width;
		data.heightHint = height;
		c.setLayoutData(data);
		shell.pack();
		shell.open();
		while(display.readAndDispatch()){/*empty*/}
		try { Thread.sleep(2000); } catch (Exception e) {}
		while(display.readAndDispatch()){/*empty*/}
		Color color1 = new Color(0xff, 0, 0xff);
		Color color2 = new Color(0, 0xff, 0xff);
		Font font1 = new Font(display, "Helvetica", 20, SWT.NONE);
		Font font2 = new Font(display, "Helvetica", 10, SWT.BOLD);
		String testString = "The quick \tbr&own SWT jum&ped foxily o\nver the lazy dog.";
		int x1 = 0, y1 = height/2, x2 = width/2, y2 = 0;
		meter.start();
		GC gc = new GC(c);
		for(int i = 0; i < 4000; i++) {
			x1 = (x1 + 5) % width; y1 = (y1 + 5) % height; x2 = (x2 + 5) % width; y2 = (y2 + 5) % height;
			gc.setFont((i & 1) == 0 ? font1 : font2);
			gc.setForeground((i & 1) == 0 ? color1 : color2);
			gc.textExtent(testString);
			gc.drawText(testString, x2, y1);
			gc.drawText(testString, x2, y1/2, SWT.DRAW_MNEMONIC | SWT.DRAW_TRANSPARENT);
			gc.drawText(testString, x2, y2, true);
		}
		gc.dispose();
		meter.stop();
		shell.dispose();
		color1.dispose();
		color2.dispose();
		font1.dispose();
		font2.dispose();
		while(display.readAndDispatch()){/*empty*/}
	}
	disposeMeter(meter);
}

@Test
public void test_fastStringDrawing() {
	PerformanceMeter meter = createMeterWithoutSummary("Draw strings using GC.drawString()");
	int samples;

	for(samples = 0; samples < 10; samples++) {
		int width = 640;
		int height = 480;
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		Canvas c = new Canvas(shell, SWT.NONE);
		GridData data = new GridData();
		data.widthHint = width;
		data.heightHint = height;
		c.setLayoutData(data);
		shell.pack();
		shell.open();
		while(display.readAndDispatch()){/*empty*/}
		try { Thread.sleep(2000); } catch (Exception e) {}
		while(display.readAndDispatch()){/*empty*/}
		Color color1 = new Color(0xff, 0, 0xff);
		Color color2 = new Color(0, 0xff, 0xff);
		Font font1 = new Font(display, "Helvetica", 20, SWT.NONE);
		Font font2 = new Font(display, "Helvetica", 10, SWT.BOLD);
		String testString = "The quick brown SWT jumped foxily over the lazy dog.";
		int x1 = 0, y1 = height/2, x2 = width/2, y2 = 0;
		meter.start();
		GC gc = new GC(c);
		for(int i = 0; i < 2000; i++) {
			x1 = (x1 + 5) % width; y1 = (y1 + 5) % height; x2 = (x2 + 5) % width; y2 = (y2 + 5) % height;
			gc.setFont((i & 1) == 0 ? font1 : font2);
			gc.setForeground((i & 1) == 0 ? color1 : color2);
			gc.stringExtent(testString);
			gc.drawString(testString, x1, y2);
			gc.drawString(testString, x1, y1, true);
		}
		gc.dispose();
		meter.stop();
		shell.dispose();
		color1.dispose();
		color2.dispose();
		font1.dispose();
		font2.dispose();
		while(display.readAndDispatch()){/*empty*/}
	}
	disposeMeter(meter);
}

/* custom */
Display display;
}
