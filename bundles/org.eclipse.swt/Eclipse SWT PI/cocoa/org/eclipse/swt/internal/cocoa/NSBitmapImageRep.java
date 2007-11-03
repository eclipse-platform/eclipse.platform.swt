package org.eclipse.swt.internal.cocoa;

public class NSBitmapImageRep extends NSImageRep {

public NSBitmapImageRep() {
	super(0);
}

public NSBitmapImageRep(int id) {
	super(id);
}

public int bitmapData() {
	return OS.objc_msgSend(id, OS.sel_bitmapData);
}

public int get_class() {
	return OS.class_NSBitmapImageRep;
}

public NSBitmapImageRep initWithBitmapData(int[] planes, int pixelsWide, int pixelsHigh, int bitsPerSample, int samplesPerPixel, boolean hasAlpha, boolean isPlanar, NSString colorSpaceName, int bitmapFormat, int bytesPerRow, int bitsPerPixel) {
	int id = OS.objc_msgSend(this.id, OS.sel_initWithBitmapDataPlanes_1pixelsWide_1pixelsHigh_1bitsPerSample_1samplesPerPixel_1hasAlpha_1isPlanar_1colorSpaceName_1bitmapFormat_1bytesPerRow_1bitsPerPixel_1, 
			planes, pixelsWide, pixelsHigh, bitsPerSample, samplesPerPixel, hasAlpha ? 1 : 0, isPlanar ? 1 : 0, colorSpaceName.id, bitmapFormat, bytesPerRow, bitsPerPixel);
	return id != 0 ? this : null;
}

}
