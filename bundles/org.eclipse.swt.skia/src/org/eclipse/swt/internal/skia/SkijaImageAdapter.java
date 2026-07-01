package org.eclipse.swt.internal.skia;

import io.github.humbleui.skija.FilterTileMode;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.Shader;

public class SkijaImageAdapter implements ISkImage {

	public SkijaImageAdapter(Image skijaImage) {
		this.skijaImage = skijaImage;
	}

	private final Image skijaImage;

	@Override
	public boolean isClosed() {
		return skijaImage.isClosed();
	}

	@Override
	public int getHeight() {
		return skijaImage.getHeight();
	}

	@Override
	public void close() {
		skijaImage.close();
	}

	@Override
	public int getWidth() {
		return skijaImage.getWidth();
	}

	@Override
	public Image getSkijaImage() {
		return skijaImage;
	}

	@Override
	public Shader makeShader(FilterTileMode repeat) {
		return skijaImage.makeShader(repeat);
	}

}
