/*******************************************************************************
 * Copyright (c) 2026 Hitesh Soliwal and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.examples.superset;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.IME;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.ToolTip;

/**
 * Small-content SWT widgets that need a specific construction recipe (items,
 * a host canvas, a child shell, DND transfer, GLData...). One switch over the
 * demo kind keeps every recipe next to the others, as ControlExample's Tabs do.
 */
final class SmallSwtDemo extends SupersetDemo {

	private ToolTip tip;
	private Shell child;

	SmallSwtDemo(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	@Override
	public Control create(Composite parent) {
		control = switch (spec.kind()) {
			case "EXPAND" -> expandBar(parent);
			case "TOOLBAR" -> toolBar(parent, SWT.FLAT | SWT.WRAP);
			case "COOLBAR" -> coolBar(parent);
			case "MENU" -> menuHost(parent);
			case "CARET" -> caretCanvas(parent);
			case "DND" -> dndLabel(parent);
			case "BROWSER" -> browser(parent);
			case "GLCANVAS" -> glCanvas(parent);
			default -> label(parent, spec.simpleName() + " (" + spec.kind() + ")");
		};
		return control;
	}

	private static Label label(Composite parent, String text) {
		Label l = new Label(parent, SWT.WRAP);
		l.setText(text);
		return l;
	}

	private static Control expandBar(Composite parent) {
		ExpandBar bar = new ExpandBar(parent, SWT.V_SCROLL);
		for (int i = 0; i < 3; i++) {
			ExpandItem item = new ExpandItem(bar, SWT.NONE);
			item.setText("Section " + i);
			Label body = label(bar, "Body of section " + i);
			item.setControl(body);
			item.setHeight(body.computeSize(SWT.DEFAULT, SWT.DEFAULT).y + 24);
			item.setExpanded(i == 0);
		}
		return bar;
	}

	private static ToolBar toolBar(Composite parent, int style) {
		ToolBar bar = new ToolBar(parent, style);
		for (int i = 0; i < 12; i++) new ToolItem(bar, i % 4 == 3 ? SWT.SEPARATOR : SWT.PUSH).setText("Tool" + i);
		return bar;
	}

	private static Control coolBar(Composite parent) {
		CoolBar cool = new CoolBar(parent, SWT.FLAT);
		for (int i = 0; i < 2; i++) {
			CoolItem item = new CoolItem(cool, SWT.NONE);
			ToolBar bar = toolBar(cool, SWT.FLAT);
			item.setControl(bar);
			item.setSize(item.computeSize(bar.computeSize(SWT.DEFAULT, SWT.DEFAULT).x, bar.computeSize(SWT.DEFAULT, SWT.DEFAULT).y));
		}
		return cool;
	}

	private static Control menuHost(Composite parent) {
		Label host = label(parent, "Right-click: pop-up Menu with 10 MenuItems (not opened by the runner: TrackPopupMenu is modal)");
		Menu menu = new Menu(host);
		for (int i = 0; i < 10; i++) new MenuItem(menu, i == 4 ? SWT.SEPARATOR : SWT.PUSH).setText("Item " + i);
		host.setMenu(menu);
		return host;
	}

	private static Control caretCanvas(Composite parent) {
		Canvas canvas = new Canvas(parent, SWT.BORDER);
		Caret caret = new Caret(canvas, SWT.NONE);
		caret.setBounds(10, 10, 2, 18);
		new IME(canvas, SWT.NONE);
		canvas.addPaintListener(e -> e.gc.drawString("Caret + IME host canvas", 16, 10));
		return canvas;
	}

	private static Control dndLabel(Composite parent) {
		Label l = label(parent, "Drag from / drop onto this label (DragSource + DropTarget, TextTransfer)");
		Transfer[] types = {TextTransfer.getInstance()};
		new DragSource(l, DND.DROP_COPY).setTransfer(types);
		new DropTarget(l, DND.DROP_COPY).setTransfer(types);
		return l;
	}

	private static Control browser(Composite parent) {
		Browser b = new Browser(parent, SWT.NONE);
		b.setText("<html><body><h3>Browser</h3><p>SWT Browser, default engine.</p></body></html>");
		return b;
	}

	private static Control glCanvas(Composite parent) {
		GLData data = new GLData();
		data.doubleBuffer = true;
		return new GLCanvas(parent, SWT.NONE, data);
	}

	@Override
	public void setData(int n) {
		super.setData(n);
		if ("TOOLTIP".equals(spec.kind())) showTip();
		if ("SHELL".equals(spec.kind())) openChild();
	}

	private void showTip() {
		if (tip == null) tip = new ToolTip(control.getShell(), SWT.NONE);
		tip.setText("ToolTip");
		tip.setMessage("SWT ToolTip shown by the superset showcase");
		tip.setLocation(control.toDisplay(20, 20));
		tip.setVisible(true);
	}

	private void openChild() {
		if (child != null && !child.isDisposed()) return;
		child = new Shell(control.getShell(), SWT.DIALOG_TRIM);
		child.setText("Child Shell");
		child.setLayout(new FillLayout());
		label(child, "Shell / Decorations: a non-modal child shell");
		child.setBounds(control.toDisplay(40, 40).x, control.toDisplay(40, 40).y, 260, 120);
		child.open();
	}

	@Override
	public void updateData() {
		if (tip != null) tip.setVisible(false);
		if (child != null && !child.isDisposed()) child.dispose();
	}
}
