package org.eclipse.swt.tests.doubles;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.skia.DpiScalerUtil;
import org.eclipse.swt.internal.skia.ISkSurface;
import org.eclipse.swt.internal.skia.ISkiaCanvasExtension;
import org.eclipse.swt.internal.skia.ISkiaResources;

public class CanvasExtensionDouble implements ISkiaCanvasExtension {

	public ISkSurface surface;
	public ISkiaResources resources;
	public DpiScalerUtil scaler;
	public Drawable drawable;
	public Rectangle clientArea;
	public Device device;
	public Rectangle bounds;

	@Override
	public ISkSurface getSurface() {
		return surface;
	}

	@Override
	public ISkiaResources getResources() {
		return resources;
	}

	@Override
	public ISkSurface createSupportSurface(int width, int height) {
		return surface.makeSurface(width, height);
	}

	@Override
	public DpiScalerUtil getScaler() {
		return scaler;
	}

	@Override
	public Drawable getDrawable() {
		return drawable;
	}

	@Override
	public Rectangle getClientArea() {
		return clientArea;
	}

	@Override
	public Device getDevice() {
		return device;
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public void redraw(int srcX, int srcY, int width, int height, boolean b) {
		// TODO Auto-generated method stub

	}

}