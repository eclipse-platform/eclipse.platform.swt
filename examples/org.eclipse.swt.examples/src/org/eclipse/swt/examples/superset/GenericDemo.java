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
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Small-content widget created by name (kind GENERIC): the first public
 * {@code (Composite, int)} or {@code (Composite)} constructor, then
 * {@code setText} when the widget has one. Objects that are not controls but
 * expose {@code getControl()} (FormattedText, TreeMapper...) are shown through
 * it. Kind SUPPORT covers Nebula support classes with a fixed recipe.
 */
final class GenericDemo extends SupersetDemo {

	GenericDemo(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	@Override
	public Control create(Composite parent) {
		Object widget = "SUPPORT".equals(spec.kind()) ? support(parent) : construct(parent);
		control = widget instanceof Control c ? c : (Control) Reflect.call(widget, "getControl");
		if (Reflect.has(widget, "setText", "")) Reflect.call(widget, "setText", "Sample " + spec.simpleName());
		return control;
	}

	private Object construct(Composite parent) {
		if (Reflect.type(spec.widgetClass()) == null) throw new IllegalStateException("class absent: " + spec.widgetClass());
		try {
			return Reflect.make(spec.widgetClass(), parent, SWT.BORDER);
		} catch (IllegalStateException e) {
			if (!e.getMessage().startsWith("no public constructor")) throw e;
			return Reflect.make(spec.widgetClass(), parent);
		}
	}

	private Object support(Composite parent) {
		return switch (spec.simpleName()) {
			case "PromptSupport" -> prompt(parent);
			case "FontAwesome" -> fontAwesome(parent);
			case "TransitionManager" -> transitions(parent);
			case "DarkPanel" -> darkPanel(parent);
			case "TreeMapper" -> treeMapper(parent);
			default -> throw new UnsupportedOperationException("harness: no recipe for " + spec.simpleName());
		};
	}

	private static Control prompt(Composite parent) {
		Text t = new Text(parent, SWT.BORDER);
		Reflect.callStatic("org.eclipse.nebula.widgets.opal.promptsupport.PromptSupport", "setPrompt", "Type here (PromptSupport)", t);
		return t;
	}

	private static Control fontAwesome(Composite parent) {
		Label l = new Label(parent, SWT.NONE);
		l.setFont((Font) Reflect.callStatic("org.eclipse.nebula.widgets.fontawesome.FontAwesome", "getFont", 24));
		l.setText("   ");
		return l;
	}

	private static Control transitions(Composite parent) {
		CTabFolder folder = new CTabFolder(parent, SWT.BORDER);
		for (int i = 0; i < 3; i++) {
			CTabItem item = new CTabItem(folder, SWT.NONE);
			item.setText("Page " + i);
			item.setControl(new Label(folder, SWT.NONE));
		}
		folder.setSelection(0);
		Reflect.make("org.eclipse.nebula.effects.stw.TransitionManager", folder);
		return folder;
	}

	private static Control darkPanel(Composite parent) {
		Label l = new Label(parent, SWT.WRAP);
		l.setText("DarkPanel(Shell): overlay created, not shown (it dims the whole shell)");
		Reflect.make("org.eclipse.nebula.widgets.opal.panels.DarkPanel", parent.getShell());
		return l;
	}

	private static Object treeMapper(Composite parent) {
		Object support = Reflect.nullProxy("org.eclipse.nebula.widgets.treemapper.ISemanticTreeMapperSupport");
		var display = parent.getDisplay();
		Object ui = Reflect.make("org.eclipse.nebula.widgets.treemapper.TreeMapperUIConfigProvider",
				display.getSystemColor(SWT.COLOR_BLUE), 1, display.getSystemColor(SWT.COLOR_RED), 2);
		return Reflect.make("org.eclipse.nebula.widgets.treemapper.TreeMapper", parent, support, ui);
	}
}
