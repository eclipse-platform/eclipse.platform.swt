/*******************************************************************************
 * Copyright (c) 2026 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;

/**
 * Owns the normal, hot and disabled image lists of a tool bar as a single unit.
 * The three lists are always created, filled, cleared and released together, so
 * they are of equal image size and the index returned when adding an item's
 * images addresses that item in all three of them.
 * <p>
 * Synchronizing the image lists with the native tool bar is up to the owning
 * tool bar, which retrieves the handles to set via
 * {@link #getImageListHandle(int)} and its hot and disabled counterparts.
 */
class ToolBarImageLists {
	private final Display display;

	private final ImageList imageList, disabledImageList, hotImageList;

	private ToolBarImageLists(Display display, ImageList imageList, ImageList hotImageList,
			ImageList disabledImageList) {
		this.display = display;
		this.imageList = imageList;
		this.hotImageList = hotImageList;
		this.disabledImageList = disabledImageList;
	}

	static ToolBarImageLists create(Display display, int style, int width, int height, int zoom) {
		ImageList imageList = display.getImageListToolBar(style, width, height, zoom);
		ImageList hotImageList = display.getImageListToolBarHot(style, width, height, zoom);
		ImageList disabledImageList = display.getImageListToolBarDisabled(style, width, height, zoom);
		return new ToolBarImageLists(display, imageList, hotImageList, disabledImageList);
	}

	void clear(int index) {
		imageList.put(index, null);
		hotImageList.put(index, null);
		disabledImageList.put(index, null);
	}

	void release() {
		display.releaseToolImageList(imageList);
		display.releaseToolHotImageList(hotImageList);
		display.releaseToolDisabledImageList(disabledImageList);
	}

	int add(Image image, Image hotImage, Image disabledImage) {
		int index = imageList.add(image);
		// Use the slot index from the normal image list as authoritative source
		// for the image ordering and reuse it for the hot and disabled lists
		// instead of letting each of them scan for its own free slot, so all
		// three stay index-aligned.
		hotImageList.put(index, hotImage);
		disabledImageList.put(index, disabledImage);
		return index;
	}

	void put(int index, Image image, Image hotImage, Image disabledImage) {
		imageList.put(index, image);
		hotImageList.put(index, hotImage);
		disabledImageList.put(index, disabledImage);
	}

	int moveFrom(ToolBarImageLists source, int index) {
		Image image = source.imageList.get(index);
		Image hotImage = source.hotImageList.get(index);
		Image disabledImage = source.disabledImageList.get(index);
		source.clear(index);
		return add(image, hotImage, disabledImage);
	}

	long getImageListHandle(int zoom) {
		return imageList.getHandle(zoom);
	}

	long getHotImageListHandle(int zoom) {
		return hotImageList.getHandle(zoom);
	}

	long getDisabledImageListHandle(int zoom) {
		return disabledImageList.getHandle(zoom);
	}

	Point getImageSize() {
		return imageList.getImageSize();
	}

}
