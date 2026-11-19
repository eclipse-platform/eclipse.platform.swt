package org.eclipse.swt.tests.doubles;

import java.util.Objects;

public class ISkImageDouble implements org.eclipse.swt.internal.skia.ISkImage {

	public boolean closed = false;
	public int height;
	public int width;

	public String name;

	@Override
	public boolean isClosed() {
		return closed;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public io.github.humbleui.skija.Shader makeShader(io.github.humbleui.skija.FilterTileMode repeat) {
		return null;
	}

	@Override
	public io.github.humbleui.skija.Image getSkijaImage() {
		return null;
	}

	@Override
	public void close() {
		closed = true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(closed, height, name, width);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ISkImageDouble other = (ISkImageDouble) obj;
		return closed == other.closed && height == other.height && Objects.equals(name, other.name)
				&& width == other.width;
	}

	@Override
	public String toString() {
		return "ISkImageDouble [closed=" + closed + ", height=" + height + ", width=" + width + ", name=" + name + "]";
	}

}
