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

/**
 * Demo factory: maps the {@code kind} column of superset-demos.tsv to the
 * class that knows the widget's construction and data recipe.
 */
public final class Demos {

	private Demos() {
	}

	/** @return the demo for {@code spec}, or null for non-runnable kinds */
	public static SupersetDemo create(DemoSpec spec, Counters counters) {
		return switch (spec.kind()) {
			case "VTABLE", "VTABLE_CURSOR", "COLUMNS" -> new TableDemo(spec, counters);
			case "VTREE", "VTREE_CURSOR" -> new TreeDemo(spec, counters);
			case "LIST", "COMBO" -> new ItemsDemo(spec, counters);
			case "STYLEDTEXT" -> new StyledTextDemo(spec, counters);
			case "CANVAS", "SCROLLED" -> new CanvasDemo(spec, counters);
			case "TABS" -> new TabsDemo(spec, counters);
			case "STACK" -> new StackDemo(spec, counters);
			case "SLIDER", "SCROLLED_CHILDREN" -> new ScrollDemos(spec, counters);
			case "EXPAND", "TOOLBAR", "COOLBAR", "MENU", "CARET", "TOOLTIP", "SHELL", "DND", "BROWSER", "GLCANVAS" ->
				new SmallSwtDemo(spec, counters);
			case "GENERIC", "SUPPORT" -> new GenericDemo(spec, counters);
			case "NGRID", "NGRIDCOLS" -> new NebulaGridDemo(spec, counters);
			case "NGALLERY" -> new NebulaGalleryDemo(spec, counters);
			case "NCTREE", "NTABLECOMBO", "NCTREECOMBO", "NDUALLIST", "NPROPTABLE" -> new NebulaItemsDemo(spec, counters);
			case "NCOMPTABLE" -> new NebulaCompositeTableDemo(spec, counters);
			case "NXYGRAPH", "NINTENSITY", "NFIGURE" -> new NebulaFigureDemo(spec, counters);
			case "NOSC", "NGANTT", "NPRINT" -> new NebulaStreamDemo(spec, counters);
			default -> null;
		};
	}

	/** @return the matrix result for a kind the runner does not drive */
	public static String staticResult(DemoSpec spec) {
		return switch (spec.kind()) {
			case "PLACEHOLDER" -> "NOT_IMPLEMENTED";
			default -> "NOT_RUN";
		};
	}

	/** @return why a kind is not driven by the runner */
	public static String staticReason(DemoSpec spec) {
		return switch (spec.kind()) {
			case "PLACEHOLDER" -> "no SWT or Nebula implementation; ledger row names the owner";
			case "NOT_APPLICABLE" -> "not a widget concept (catalog NOT_APPLICABLE)";
			case "DIALOG" -> "modal native dialog: opened only by a user click in the showcase";
			case "MODAL" -> "modal tracking loop: opened only by a user click in the showcase";
			case "SIDE_EFFECT" -> "desktop side effect (tray/taskbar/toast): opened only by a user click in the showcase";
			case "ABSTRACT" -> "abstract base class: exercised by every other demo";
			default -> "not driven";
		};
	}
}
