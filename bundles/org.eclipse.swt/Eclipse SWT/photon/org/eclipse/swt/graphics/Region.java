package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;

public final class Region {

	/**
	 * the OS resource for the region
	 * (Warning: This field is platform dependent)
	 */
	public int handle;
	
	static int EMPTY_REGION = -1;

public Region () {
	handle = EMPTY_REGION;
}
Region(int handle) {
	this.handle = handle;
}


public void add (Rectangle rect) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (rect.width < 0 || rect.height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (handle == 0) return;
	int tile_ptr = OS.PhGetTile();
	PhTile_t tile = new PhTile_t();
	tile.rect_ul_x = (short)rect.x;
	tile.rect_ul_y = (short)rect.y;
	tile.rect_lr_x = (short)(rect.x + rect.width - 1);
	tile.rect_lr_y = (short)(rect.y + rect.height - 1);
	OS.memmove(tile_ptr, tile, PhTile_t.sizeof);
	if (handle == EMPTY_REGION) handle = tile_ptr;
	else handle = OS.PhAddMergeTiles (handle, tile_ptr, null);
}

public void add (Region region) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (region == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (region.isDisposed()) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (handle == 0) return;
	if (region.handle == EMPTY_REGION) return;
	int copy = OS.PhCopyTiles(region.handle);
	if (handle == EMPTY_REGION) handle = copy;
	else handle = OS.PhAddMergeTiles (handle, copy, null);
}

public boolean contains (int x, int y) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (handle == 0 || handle == EMPTY_REGION) return false;
	int tile_ptr = OS.PhGetTile();
	PhTile_t tile = new PhTile_t();
	tile.rect_ul_x = tile.rect_lr_x = (short)x;
	tile.rect_ul_y = tile.rect_lr_y = (short)y;
	OS.memmove(tile_ptr, tile, PhTile_t.sizeof);
	int intersection = OS.PhIntersectTilings (tile_ptr, handle, null);
	boolean result = intersection != 0;
	OS.PhFreeTiles(tile_ptr);
	OS.PhFreeTiles(intersection);
	return result;
}

public boolean contains (Point pt) {
	if (pt == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return contains(pt.x, pt.y);
}

public void dispose () {
	if (handle == 0) return;
	if (handle != EMPTY_REGION) OS.PhFreeTiles (handle);
	handle = 0;
}

public boolean equals (Object object) {
	if (this == object) return true;
	if (!(object instanceof Region)) return false;
	Region region = (Region)object;
	return handle == region.handle;
}

public Rectangle getBounds() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (handle == 0 || handle == EMPTY_REGION) return new Rectangle(0, 0, 0, 0);
	PhTile_t tile = new PhTile_t();
	int temp_tile;
	int rect_ptr = OS.malloc(PhRect_t.sizeof);
	OS.memmove(rect_ptr, handle, PhRect_t.sizeof);
	OS.memmove(tile, handle, PhTile_t.sizeof);
	while ((temp_tile = tile.next) != 0) {
		OS.PhRectUnion (rect_ptr, temp_tile);
		OS.memmove(tile, temp_tile, PhTile_t.sizeof);
	}
	PhRect_t rect = new PhRect_t();
	OS.memmove(rect, rect_ptr, PhRect_t.sizeof);
	OS.free(rect_ptr);
	int width = rect.lr_x - rect.ul_x + 1;
	int height = rect.lr_y - rect.ul_y + 1;
	return new Rectangle(rect.ul_x, rect.ul_y, width, height);
}

public int hashCode () {
	return handle;
}

public boolean intersects (int x, int y, int width, int height) {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	if (handle == 0 || handle == EMPTY_REGION) return false;
	int tile_ptr = OS.PhGetTile();
	PhTile_t tile = new PhTile_t();
	tile.rect_ul_x = (short)x;
	tile.rect_ul_y = (short)y;
	tile.rect_lr_x = (short)(x + width - 1);
	tile.rect_lr_y = (short)(y + height - 1);
	OS.memmove(tile_ptr, tile, PhTile_t.sizeof);
	int intersection = OS.PhIntersectTilings (tile_ptr, handle, null);
	boolean result = intersection != 0;
	OS.PhFreeTiles(tile_ptr);
	OS.PhFreeTiles(intersection);
	return result;
}

public boolean intersects (Rectangle rect) {
	if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	return intersects(rect.x, rect.y, rect.width, rect.height);
}

public boolean isDisposed() {
	return handle == 0;
}

public boolean isEmpty () {
	return getBounds().isEmpty();
	
}

public static Region photon_new(int handle) {
	return new Region(handle);
}

public String toString () {
	if (isDisposed()) return "Region {*DISPOSED*}";
	return "Region {" + handle + "}";
}
}
