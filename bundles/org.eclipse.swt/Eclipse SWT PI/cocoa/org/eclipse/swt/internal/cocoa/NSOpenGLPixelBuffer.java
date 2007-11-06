package org.eclipse.swt.internal.cocoa;

public class NSOpenGLPixelBuffer extends NSObject {

public NSOpenGLPixelBuffer() {
	super();
}

public NSOpenGLPixelBuffer(int id) {
	super(id);
}

public id initWithTextureTarget(int target, int format, int maxLevel, int pixelsWide, int pixelsHigh) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithTextureTarget_1textureInternalFormat_1textureMaxMipMapLevel_1pixelsWide_1pixelsHigh_1, target, format, maxLevel, pixelsWide, pixelsHigh);
	return result != 0 ? new id(result) : null;
}

public int pixelsHigh() {
	return OS.objc_msgSend(this.id, OS.sel_pixelsHigh);
}

public int pixelsWide() {
	return OS.objc_msgSend(this.id, OS.sel_pixelsWide);
}

public int textureInternalFormat() {
	return OS.objc_msgSend(this.id, OS.sel_textureInternalFormat);
}

public int textureMaxMipMapLevel() {
	return OS.objc_msgSend(this.id, OS.sel_textureMaxMipMapLevel);
}

public int textureTarget() {
	return OS.objc_msgSend(this.id, OS.sel_textureTarget);
}

}
