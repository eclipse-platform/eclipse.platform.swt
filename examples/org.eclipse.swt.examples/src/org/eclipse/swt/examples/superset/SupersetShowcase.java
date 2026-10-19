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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * Superset Viewport Showcase: every concept of the SWT ∪ Nebula ∪ Qt ∪ React
 * catalog, grouped by category, one tab per concept and one sub-tab per widget
 * (SWT core/custom class or Nebula family). Built like ControlExample: a
 * TabFolder of pages, but every page is created on first selection, so a
 * hidden concept does no work, and each widget tab's control is the widget
 * itself (no wrapper composite, no ScrolledComposite around it). Nebula demos need the Nebula jars on the
 * classpath (see superset-matrix/README.md); without them their tabs say so.
 */
public class SupersetShowcase {

	private final SupersetCatalog catalog = new SupersetCatalog();
	private final ShowcaseState state = new ShowcaseState();
	private TabFolder categories;

	public SupersetShowcase(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		toolbar(parent);
		state.info = new Label(parent, SWT.NONE);
		state.info.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		categories = new TabFolder(parent, SWT.TOP);
		categories.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		for (String category : SupersetCatalog.CATEGORIES) {
			TabItem item = new TabItem(categories, SWT.NONE);
			List<ConceptInfo> concepts = catalog.concepts(category);
			item.setText(category + " (" + concepts.size() + ")");
			item.setControl(conceptFolder(categories, concepts));
		}
		state.strip = new Label(parent, SWT.NONE);
		state.strip.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
	}

	private void toolbar(Composite parent) {
		Composite bar = new Composite(parent, SWT.NONE);
		bar.setLayout(new RowLayout(SWT.HORIZONTAL));
		new Label(bar, SWT.NONE).setText("Scale:");
		Combo scale = new Combo(bar, SWT.READ_ONLY);
		for (int s : DemoSpec.SCALES) scale.add(DemoSpec.scaleLabel(s));
		scale.select(0);
		scale.addListener(SWT.Selection, e -> state.scale = DemoSpec.SCALES[scale.getSelectionIndex()]);
		Button apply = new Button(bar, SWT.PUSH);
		apply.setText("Load scale into visible widget");
		apply.addListener(SWT.Selection, e -> state.applyScale());
		Button sweep = new Button(bar, SWT.PUSH);
		sweep.setText("Sweep 250 frames (end and back)");
		sweep.addListener(SWT.Selection, e -> state.sweep());
		long runnable = catalog.demos().stream().filter(DemoSpec::runnable).count();
		new Label(bar, SWT.NONE).setText(catalog.conceptCount() + " concepts | " + catalog.demos().size() + " widget tabs | "
				+ runnable + " driven by the matrix runner | SWT " + SWT.getVersion() + " " + SWT.getPlatform());
	}

	private Composite conceptFolder(Composite parent, List<ConceptInfo> concepts) {
		CTabFolder folder = new CTabFolder(parent, SWT.TOP | SWT.BORDER);
		for (ConceptInfo concept : concepts) {
			CTabItem item = new CTabItem(folder, SWT.NONE);
			item.setText(concept.concept());
			item.setData(concept);
		}
		folder.addListener(SWT.Selection, e -> open(folder, (CTabItem) e.item));
		if (folder.getItemCount() > 0) {
			folder.setSelection(0);
			open(folder, folder.getItem(0));
		}
		return folder;
	}

	private void open(CTabFolder folder, CTabItem item) {
		if (item.getControl() != null) return;
		ConceptInfo concept = (ConceptInfo) item.getData();
		item.setControl(new ConceptPage(folder, concept, catalog.demos(concept.concept()), state).page());
		item.setToolTipText(concept.ledgerLine());
	}

	/**
	 * Opens the showcase. With {@code --tour <dir>} it visits every category,
	 * concept and widget tab, prints the shell into {@code <dir>/tour/} and
	 * writes {@code <dir>/SHOWCASE_TOUR.tsv}, then exits.
	 */
	public static void main(String[] args) throws java.io.IOException {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SWT Superset Viewport Showcase");
		SupersetShowcase showcase = new SupersetShowcase(shell);
		shell.setSize(1280, 880);
		shell.open();
		if (args.length > 1 && "--tour".equals(args[0])) {
			ShowcaseTour tour = new ShowcaseTour(shell, new java.io.File(args[1]));
			tour.run(showcase.categories);
			System.out.println("tour: " + tour.shown() + " widget tabs shown");
			display.dispose();
			return;
		}
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
