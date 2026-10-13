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
import org.eclipse.swt.widgets.Composite;

/**
 * Tab folder of one catalog concept: one tab per widget, each tab's control
 * being the widget itself, created on first selection. The concept's ledger
 * row is the folder's tooltip and is shown in the showcase info line.
 */
final class ConceptPage {

	private final CTabFolder widgets;

	ConceptPage(Composite parent, ConceptInfo concept, List<DemoSpec> demos, ShowcaseState state) {
		widgets = new CTabFolder(parent, SWT.BOTTOM | SWT.BORDER);
		widgets.setToolTipText(concept.ledgerLine() + "\nSWT owner: " + concept.swtOwner() + "\nNebula owner: "
				+ concept.nebulaOwner() + "\nlarge content: " + (concept.large() ? "yes" : "no"));
		for (DemoSpec spec : demos) {
			CTabItem item = new CTabItem(widgets, SWT.NONE);
			item.setText(spec.owner() + ": " + spec.simpleName());
			item.setData(new DemoPane(spec, concept));
		}
		widgets.addListener(SWT.Selection, e -> open((CTabItem) e.item, state));
		if (widgets.getItemCount() > 0) {
			widgets.setSelection(0);
			open(widgets.getItem(0), state);
		}
	}

	private static void open(CTabItem item, ShowcaseState state) {
		DemoPane pane = (DemoPane) item.getData();
		if (item.getControl() == null) item.setControl(pane.create(item.getParent(), state.scale));
		state.show(pane);
	}

	CTabFolder page() {
		return widgets;
	}
}
