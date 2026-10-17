/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.superset;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

/**
 * "Show all of them": selects every category, every concept and every widget
 * tab of the showcase in turn, the way a user clicking through would, prints
 * the shell (Control.print) into {@code <dir>/tour/<concept>__<Class>.png} and
 * writes {@code <dir>/SHOWCASE_TOUR.tsv} with what each tab showed. Each
 * concept page is disposed after its visit, so the tour stays small.
 */
final class ShowcaseTour {

	private static final int TOUR_WIDTH = 800;
	private final Shell shell;
	private final File dir;
	private PrintWriter out;
	private int shown;

	ShowcaseTour(Shell shell, File dir) {
		this.shell = shell;
		this.dir = dir;
	}

	void run(TabFolder categories) throws IOException {
		new File(dir, "tour").mkdirs();
		try (PrintWriter w = new PrintWriter(new File(dir, "SHOWCASE_TOUR.tsv"), StandardCharsets.UTF_8)) {
			out = w;
			w.println("category\tconcept\towner\twidget_class\tkind\tshown_as\tscreenshot");
			for (int c = 0; c < categories.getItemCount(); c++) {
				categories.setSelection(c);
				drain();
				concepts(categories.getItem(c).getText(), (CTabFolder) categories.getItem(c).getControl());
			}
		}
	}

	private void concepts(String category, CTabFolder folder) {
		for (CTabItem concept : folder.getItems()) {
			select(folder, concept);
			if (concept.getControl() instanceof CTabFolder widgets) {
				for (CTabItem widget : widgets.getItems()) visit(category.replaceAll(" \\(\\d+\\)$", ""), (ConceptInfo) concept.getData(), widgets, widget);
			}
			Control page = concept.getControl();
			concept.setControl(null);
			if (page != null) page.dispose();
			drain();
		}
	}

	private void visit(String category, ConceptInfo concept, CTabFolder widgets, CTabItem widget) {
		String row;
		try {
			select(widgets, widget);
			Control c = widget.getControl();
			String as = c instanceof Label l && l.getText().startsWith("Cannot create") ? "ERROR: " + l.getText().replace('\n', ' ')
					: c instanceof Label ? "PLACEHOLDER" : c instanceof org.eclipse.swt.widgets.Button b && b.getText().startsWith("Open ") ? "LAUNCHER" : "WIDGET";
			String name = (concept.concept() + "__" + widget.getText().replaceAll("[^A-Za-z0-9._-]", "_")).replaceAll("_+", "_") + ".png";
			String saved = Snapshot.save(shell, new File(dir, "tour"), name, TOUR_WIDTH);
			row = as.replace('\t', ' ') + "\t" + ("-".equals(saved) ? "-" : "tour/" + saved);
			shown++;
		} catch (RuntimeException | LinkageError e) {
			row = "ERROR: " + String.valueOf(e).replace('\t', ' ').replace('\n', ' ') + "\t-";
		}
		DemoSpec spec = ((DemoPane) widget.getData()).spec();
		out.println(category + "\t" + concept.concept() + "\t" + spec.owner() + "\t" + spec.widgetClass() + "\t" + spec.kind() + "\t" + row);
		out.flush();
	}

	private void select(CTabFolder folder, CTabItem item) {
		folder.setSelection(item);
		Event e = new Event();
		e.item = item;
		folder.notifyListeners(SWT.Selection, e);
		folder.layout(true, true);
		drain();
	}

	private void drain() {
		Display d = shell.getDisplay();
		while (d.readAndDispatch()) {
			// let the selected tab create and paint
		}
	}

	int shown() {
		return shown;
	}
}
