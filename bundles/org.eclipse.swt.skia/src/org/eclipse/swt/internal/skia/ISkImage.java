package org.eclipse.swt.internal.skia;

import io.github.humbleui.skija.FilterTileMode;
import io.github.humbleui.skija.Shader;

public interface ISkImage extends AutoCloseable {

	boolean isClosed();

	int getHeight();

	int getWidth();

	Shader makeShader(FilterTileMode repeat);

	io.github.humbleui.skija.Image getSkijaImage();

	@Override
	void close();
}
