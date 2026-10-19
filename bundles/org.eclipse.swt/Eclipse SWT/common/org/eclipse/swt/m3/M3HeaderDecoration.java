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
 *     Hitesh Soliwal (Synexia) - adapted from TV_5.0 treeviewer ColumnFilterUI.updateColumnImage
 *         (docs/m3/sort-filter/PROVENANCE.tsv)
 *******************************************************************************/
package org.eclipse.swt.m3;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/*
 * The header marks of a Table or Tree shared by the column sort and the column filter: per
 * column a direction, a priority number (0 = none) and a filtered flag, shown as one generated
 * column image. The column's own image is saved in the column data and restored when the column
 * has no mark. The image is computed as ImageData (triangle, 3x5 digit bitmap, funnel) in the
 * control's foreground colour, at 100% and 200%, so it needs no font and its pixels are exact.
 */
final class M3HeaderDecoration {

	static final int WIDTH = 20, HEIGHT = 10;
	private static final String KEY = "org.eclipse.swt.m3.M3HeaderDecoration"; //$NON-NLS-1$
	private static final String ORIGINAL = "org.eclipse.swt.m3.M3HeaderDecoration.original"; //$NON-NLS-1$
	private static final Object NO_IMAGE = new Object ();
	/* 3x5 digits 0 .. 9, then '+', one row per 3 bits (top row first) */
	private static final short [] DIGITS = {
		0x7B6F, 0x2C97, 0x73E7, 0x73CF, 0x5BC9, 0x79CF, 0x79EF, 0x7249, 0x7BEF, 0x7BCF, 0x05D0,
	};

	private final Composite control;
	private int [] directions = new int [0], priorities = new int [0];
	private boolean [] filtered = new boolean [0];
	private final Map<Integer, Image> images = new HashMap<> ();

	private M3HeaderDecoration (Composite control) {
		this.control = control;
		control.addListener (SWT.Dispose, event -> dispose ());
	}

	static M3HeaderDecoration of (Composite control) {
		Object decoration = control.getData (KEY);
		if (decoration instanceof M3HeaderDecoration) return (M3HeaderDecoration) decoration;
		M3HeaderDecoration created = new M3HeaderDecoration (control);
		control.setData (KEY, created);
		return created;
	}

	void setSort (int column, int direction, int priority) {
		grow (column);
		directions [column] = direction;
		priorities [column] = priority;
		update (column);
	}

	void setFiltered (int column, boolean value) {
		grow (column);
		filtered [column] = value;
		update (column);
	}

	int code (int column) {
		if (column >= directions.length) return 0;
		int direction = directions [column] == SWT.UP ? 1 : directions [column] == SWT.DOWN ? 2 : 0;
		return direction | Math.min (priorities [column], 10) << 2 | (filtered [column] ? 1 << 6 : 0);
	}

	private void grow (int column) {
		if (column < directions.length) return;
		directions = Arrays.copyOf (directions, column + 1);
		priorities = Arrays.copyOf (priorities, column + 1);
		filtered = Arrays.copyOf (filtered, column + 1);
	}

	private void update (int column) {
		Item item = M3Columns.column (control, column);
		Object original = item.getData (ORIGINAL);
		int code = code (column);
		if (code == 0) {
			if (original == null) return;
			item.setData (ORIGINAL, null);
			item.setImage (original == NO_IMAGE ? null : (Image) original);
			return;
		}
		if (original == null) item.setData (ORIGINAL, item.getImage () == null ? NO_IMAGE : item.getImage ());
		item.setImage (image (code));
	}

	private Image image (int code) {
		Image image = images.get (code);
		if (image == null) {
			RGB rgb = control.getForeground ().getRGB ();
			image = new Image (control.getDisplay (), (ImageDataProvider) zoom -> zoom == 100 || zoom == 200 ? imageData (code, rgb, zoom / 100) : null);
			images.put (code, image);
		}
		return image;
	}

	/* the pixels of a mark: direction (code & 3), number ((code >> 2) & 15), filtered (code & 64) */
	static ImageData imageData (int code, RGB color, int scale) {
		ImageData data = new ImageData (WIDTH * scale, HEIGHT * scale, 8, new PaletteData (new RGB (255, 255, 255), color));
		data.transparentPixel = 0;
		int direction = code & 3, number = code >> 2 & 15;
		for (int y = 0; y < 4 && direction != 0; y++) {
			int row = direction == 1 ? 3 + y : 6 - y;
			for (int x = 3 - y; x <= 3 + y; x++) dot (data, x, row, scale);
		}
		if (number != 0) {
			int glyph = DIGITS [number == 10 ? 10 : number];
			for (int bit = 0; bit < 15; bit++) {
				if ((glyph >> (14 - bit) & 1) != 0) dot (data, 8 + bit % 3, 2 + bit / 3, scale);
			}
		}
		if ((code & 64) != 0) {
			for (int y = 0; y < 3; y++) {
				for (int x = 13 + y; x <= 19 - y; x++) dot (data, x, 1 + y, scale);
			}
			for (int y = 4; y < 9; y++) dot (data, 16, y, scale);
		}
		return data;
	}

	private static void dot (ImageData data, int x, int y, int scale) {
		for (int dy = 0; dy < scale; dy++) {
			for (int dx = 0; dx < scale; dx++) data.setPixel (x * scale + dx, y * scale + dy, 1);
		}
	}

	private void dispose () {
		for (Image image : images.values ()) image.dispose ();
		images.clear ();
	}
}
