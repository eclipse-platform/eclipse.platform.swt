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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Page of one catalog concept: the ledger row (owner, status, gap action) and
 * one sub-tab per widget. Widget panes are created on first selection.
 */
final class ConceptPage {

	private final Composite page;

	ConceptPage(Composite parent, ConceptInfo concept, List<DemoSpec> demos, ShowcaseState state) {
		page = new Composite(parent, SWT.NONE);
		page.setLayout(new GridLayout(1, false));
		Label ledger = new Label(page, SWT.WRAP);
		ledger.setText(concept.ledgerLine() + "\nSWT owner: " + concept.swtOwner() + "\nNebula owner: " + concept.nebulaOwner()
				+ "\nlarge content: " + (concept.large() ? "yes" : "no") + " | priority " + concept.priority());
		ledger.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		CTabFolder widgets = new CTabFolder(page, SWT.BOTTOM | SWT.BORDER);
		widgets.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		for (DemoSpec spec : demos) {
			CTabItem item = new CTabItem(widgets, SWT.NONE);
			item.setText(spec.owner() + ": " + spec.simpleName());
			item.setData(spec);
		}
		widgets.addListener(SWT.Selection, e -> open((CTabItem) e.item, concept, state));
		if (widgets.getItemCount() > 0) {
			widgets.setSelection(0);
			open(widgets.getItem(0), concept, state);
		}
	}

	private static void open(CTabItem item, ConceptInfo concept, ShowcaseState state) {
		if (item.getControl() != null) {
			state.visible = (DemoPane) item.getControl();
			state.visible.resume();
			return;
		}
		DemoPane pane = new DemoPane(item.getParent(), (DemoSpec) item.getData(), concept, state.scale);
		item.setControl(pane);
		state.visible = pane;
	}

	Composite page() {
		return page;
	}
}
