package org.eclipse.swt.examples.skia;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SnippetGifInterpolationLoader {

	final static boolean useSkia = true;
	
	static Image loadImage(Device device, Class<SnippetGifInterpolationLoader> clazz, String string) {
		try (InputStream stream = clazz.getResourceAsStream(string)) {
			if (stream == null)
				return null;
			return new Image(device, stream);
		} catch (SWTException ex) {
		} catch (IOException ex) {
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		Display display = new Display();
		final var image = SnippetGifInterpolationLoader.loadImage(display, SnippetGifInterpolationLoader.class, "home_nav.gif");
		
		Shell s = new Shell(display);
		s.setLayout(new FillLayout());
		
		int style = useSkia ? (SWT.FILL | SWT.SKIA ) : SWT.FILL;
		
		Canvas c = new Canvas(s, style );
		c.setBackground(display.getSystemColor(SWT.COLOR_RED));
		c.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(image, 0, 0);
				var gc = e.gc;
				var width = c.getBounds().width;
				var height = c.getBounds().height;
				Rectangle bounds = image.getBounds();
				
				String text;
				Point size = gc.stringExtent("OriginalText");
				
				
				float scaleX = 10f;
				float scaleY = 10f;
				var device = e.gc.getDevice();
				
				Transform transform = new Transform(device);
				transform.translate((width - (bounds.width * scaleX + 10) * 4) / 2, 25 + bounds.height + size.y +
							(height - (25 + bounds.height + size.y + bounds.height*scaleY)) / 2);
				transform.scale(scaleX, scaleY);
				
				// --- draw strings ---
				float[] point = new float[2];
				text =  "None"; //$NON-NLS-1$
				size = gc.stringExtent(text);
				point[0] = (scaleX*bounds.width + 5 - size.x)/(2*scaleX);
				point[1] = bounds.height;
				transform.transform(point);
				gc.drawString(text, (int)point[0], (int)point[1], true);

				text =  "Low"; //$NON-NLS-1$
				size = gc.stringExtent(text);
				point[0] = (scaleX*bounds.width + 5 - size.x)/(2*scaleX) + bounds.width;
				point[1] = bounds.height;
				transform.transform(point);
				gc.drawString(text, (int)point[0], (int)point[1], true);

				text = "Default"; //$NON-NLS-1$
				size = gc.stringExtent(text);
				point[0] = (scaleX*bounds.width + 5 - size.x)/(2*scaleX) + 2*bounds.width;
				point[1] = bounds.height;
				transform.transform(point);
				gc.drawString(text, (int)point[0], (int)point[1], true);

				text =  "High"; //$NON-NLS-1$
				size = gc.stringExtent(text);
				point[0] = (scaleX*bounds.width + 5 - size.x)/(2*scaleX) + 3*bounds.width;
				point[1] = bounds.height;
				transform.transform(point);
				gc.drawString(text, (int)point[0], (int)point[1], true);

				gc.setTransform(transform);
				transform.dispose();

				// --- draw images ---

				// no interpolation
				gc.setInterpolation(SWT.NONE);
				gc.drawImage(image, 0, 0);

				// low interpolation
				gc.setInterpolation(SWT.LOW);
				gc.drawImage(image, bounds.width, 0);

				// default interpolation
				gc.setInterpolation(SWT.DEFAULT);
				gc.drawImage(image, 2*bounds.width, 0);

				// high interpolation
				gc.setInterpolation(SWT.HIGH);
				gc.drawImage(image, 3*bounds.width, 0);

			}
		});
		
		
		s.open();
		s.setSize(500, 500);
		
		while(!s.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
		display.dispose();
		
		
		
	}
	
}
