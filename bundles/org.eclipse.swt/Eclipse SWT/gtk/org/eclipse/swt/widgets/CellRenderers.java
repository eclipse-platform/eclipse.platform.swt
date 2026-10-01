/*******************************************************************************
 * Copyright (c) 2026 vogella GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Lars Vogel - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.internal.gtk.*;

/**
 * Cell renderer lookups shared by the GtkTreeView based Table, Tree and List.
 */
final class CellRenderers {

private CellRenderers () {
}

static long getPixbufRenderer (long column) {
	long list = GTK.gtk_cell_layout_get_cells(column);
	if (list == 0) return 0;
	long originalList = list;
	long pixbufRenderer = 0;
	while (list != 0) {
		long renderer = OS.g_list_data (list);
		if (GTK.GTK_IS_CELL_RENDERER_PIXBUF (renderer)) {
			pixbufRenderer = renderer;
			break;
		}
		list = OS.g_list_next (list);
	}
	OS.g_list_free (originalList);
	return pixbufRenderer;
}

static long getTextRenderer (long column) {
	long list = GTK.gtk_cell_layout_get_cells(column);
	if (list == 0) return 0;
	long originalList = list;
	long textRenderer = 0;
	while (list != 0) {
		long renderer = OS.g_list_data (list);
		if (GTK.GTK_IS_CELL_RENDERER_TEXT (renderer)) {
			textRenderer = renderer;
			break;
		}
		list = OS.g_list_next (list);
	}
	OS.g_list_free (originalList);
	return textRenderer;
}
}
