package org.eclipse.swt.tests.doubles;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.internal.skia.ISkCanvas;
import org.eclipse.swt.internal.skia.ISkImage;
import org.eclipse.swt.internal.skia.ISkSurface;

import io.github.humbleui.types.IRect;

public class SkSurfaceDouble implements ISkSurface {

	List<MethodCall> calls ;
	
	String name;
	private int childIndex;
	private int snapshotIndex;

	int width;
	int height;
	boolean closed;
	SkCanvasDouble canvas;

	List<ISkImage> imageSnapshots = new ArrayList<>();

	List<ISkSurface> subSurfaces = new ArrayList<>();

	public SkSurfaceDouble(int width, int height, String string, List<MethodCall> calls) {
		this.width = width;
		this.height = height;
		this.name = string;
		
		if(calls == null) {
			this.calls = new ArrayList<>();
		} else {
			this.calls = calls;
		}
		
		canvas = new SkCanvasDouble(100, 100, name+"-canvas", this.calls);
		
	}

	public SkSurfaceDouble(List<MethodCall> calls) {
		this(100, 100, "surface", calls);
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public boolean isClosed() {
		return closed;
	}

	@Override
	public ISkCanvas getCanvas() {
		return canvas;
	}

	@Override
	public void close() {
		closed = true;
	}

	@Override
	public ISkImage makeImageSnapshot() {

		var doub = new ISkImageDouble();

		doub.closed = false;
		doub.width = width;
		doub.height = height;

		doub.name = name + "-snapshot-" + snapshotIndex++;

		imageSnapshots.add(doub);

		return doub;
	}

	@Override
	public ISkImage makeImageSnapshot(IRect rect) {

		var doub = new ISkImageDouble();

		doub.closed = false;
		doub.width = rect.getWidth();
		doub.height = rect.getHeight();
		doub.name = name + "-snapshot-" + snapshotIndex++;

		imageSnapshots.add(doub);

		return doub;
	}

	@Override
	public ISkSurface makeSurface(int width, int height) {
		SkSurfaceDouble surface = new SkSurfaceDouble(width, height, name + "-child-" + childIndex++, calls);
		subSurfaces.add(surface);
		return surface;
	}

}