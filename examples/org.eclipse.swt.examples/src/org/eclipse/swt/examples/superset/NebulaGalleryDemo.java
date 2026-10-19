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

import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;

/**
 * Nebula Gallery in SWT.VIRTUAL mode (kind NGALLERY), as in the Gallery
 * javadoc snippet: root items are expanded groups of {@link #GROUP} tiles, so
 * scale N = N tiles in ceil(N/GROUP) groups. Scrolling drives the vertical
 * ScrollBar, which is the Gallery's own scroll input.
 */
final class NebulaGalleryDemo extends SupersetDemo {

	private static final String PKG = "org.eclipse.nebula.widgets.gallery.";
	private static final int GROUP = 1_000, TILE = 72;
	private Composite gallery;
	private Method parentOf, indexInGallery, indexInGroup, setCount, setExpanded;
	private boolean stated;

	NebulaGalleryDemo(DemoSpec spec, Counters counters) {
		super(spec, counters);
	}

	@Override
	public Control create(Composite parent) {
		gallery = (Composite) Reflect.make(PKG + "Gallery", parent, SWT.VIRTUAL | SWT.V_SCROLL | SWT.BORDER | SWT.MULTI);
		Reflect.call(gallery, "setGroupRenderer", Reflect.make(PKG + "DefaultGalleryGroupRenderer"));
		Object items = Reflect.make(PKG + "DefaultGalleryItemRenderer");
		Reflect.call(gallery, "setItemRenderer", items);
		parentOf = Reflect.find(PKG + "GalleryItem", "getParentItem");
		Class<?> itemType = parentOf.getDeclaringClass();
		indexInGallery = Reflect.find(PKG + "Gallery", "indexOf", itemType);
		indexInGroup = Reflect.find(PKG + "GalleryItem", "indexOf", itemType);
		setCount = Reflect.find(PKG + "GalleryItem", "setItemCount", int.class);
		setExpanded = Reflect.find(PKG + "GalleryItem", "setExpanded", boolean.class);
		gallery.addListener(SWT.SetData, e -> fill((Item) e.item));
		return control = gallery;
	}

	private void fill(Item item) {
		Object group = Reflect.exec(parentOf, item);
		if (group == null) {
			int g = (Integer) Reflect.exec(indexInGallery, gallery, item);
			item.setText("Group " + g);
			Reflect.exec(setCount, item, Math.min(GROUP, rows - g * GROUP));
			Reflect.exec(setExpanded, item, true);
			return;
		}
		int g = (Integer) Reflect.exec(indexInGallery, gallery, group);
		int i = (Integer) Reflect.exec(indexInGroup, group, item);
		item.setText(text.cell(g * GROUP + i, 0));
		counters.inc(Counters.MATERIALIZE);
	}

	@Override
	public void setData(int n) {
		super.setData(n);
		Reflect.call(gallery, "setItemCount", (n + GROUP - 1) / GROUP);
	}

	@Override
	public void updateData() {
		rows++;
		Reflect.call(gallery, "setItemCount", (rows + GROUP - 1) / GROUP);
		Reflect.call(gallery, "clearAll");
	}

	@Override
	public String applyState() {
		if (rows < 4) return super.applyState();
		Object group = Reflect.call(gallery, "getItem", 0);
		Object[] sel = (Object[]) java.lang.reflect.Array.newInstance(parentOf.getDeclaringClass(), 2);
		sel[0] = Reflect.call(group, "getItem", 1);
		sel[1] = Reflect.call(group, "getItem", 3);
		Reflect.call(gallery, "setSelection", (Object) sel);
		stated = true;
		return "selection+" + super.applyState();
	}

	@Override
	public String captureState() {
		if (!stated) return super.captureState();
		StringBuilder b = new StringBuilder("sel=");
		for (Object it : (Object[]) Reflect.call(gallery, "getSelection")) b.append(((Item) it).getText()).append(',');
		return b.append('|').append(super.captureState()).toString();
	}

	@Override
	public int visibleRows() {
		int cols = Math.max(1, gallery.getClientArea().width / TILE);
		return fit(TILE) * cols;
	}
}
