package org.eclipse.swt.graphics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Data;
import io.github.humbleui.skija.FontMgr;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Surface;
import io.github.humbleui.skija.paragraph.Alignment;
import io.github.humbleui.skija.paragraph.FontCollection;
import io.github.humbleui.skija.paragraph.Paragraph;
import io.github.humbleui.skija.paragraph.ParagraphBuilder;
import io.github.humbleui.skija.paragraph.ParagraphStyle;
import io.github.humbleui.skija.paragraph.TextStyle;

public class SkijaExample {

	public static Surface surface;

	public static void main(String[] args) {
		int width = 800;
		int height = 600;

		surface = Surface.makeRasterN32Premul(width, height);
		Canvas canvas = surface.getCanvas();

		canvas.clear(0xFFFFFFFF); // White background

		String text = "This is a sample text for selection.";
		drawParagraphWithSelection(canvas, width, text, 5, 7, 0xFFFF0000); // Red
																			// color
																			// for
																			// selection

		// Save or display the surface as needed
	}

	private static void drawParagraphWithSelection(Canvas canvas, int width,
			String text, int startSelection, int endSelection,
			int selectionColor) {
		// Initial ParagraphStyle and TextStyle setup
		ParagraphStyle paragraphStyle = new ParagraphStyle();
		TextStyle textStyle = new TextStyle()
				.setFontFamilies(new String[]{"Arial"}).setFontSize(30)
				.setColor(0xFF000000); // Black color

		ParagraphStyle style = new ParagraphStyle();
		style.setAlignment(Alignment.LEFT);

		FontMgr fontMgr = FontMgr.getDefault();

		FontCollection fc = new FontCollection();
		fc.setDefaultFontManager(fontMgr);

		ParagraphBuilder builder = new ParagraphBuilder(style, fc);

		// Add text
		builder.pushStyle(textStyle);
		builder.addText(text);
		builder.popStyle();

		// Build the paragraph
		Paragraph paragraph = builder.build();

		// Layout the paragraph
		paragraph.layout(width);

		// Draw the paragraph initially
		paragraph.paint(canvas, 0, 0);

		createSnapshot();

		canvas.clear(0xFFFFFFFF); // White background

		// Update foreground color
		Paint paint = new Paint().setColor(selectionColor);
		paragraph.updateForegroundPaint(startSelection, endSelection,
				new Paint().setColor(0x00000000));
		paragraph.updateForegroundPaint(startSelection, endSelection, paint);
		paragraph.updateBackgroundPaint(startSelection, endSelection,
				new Paint().setColor(0xFFFF00FF));

		paint.close();

		// Repaint the paragraph to reflect the new foreground style
		// Layout the paragraph
		paragraph.layout(width);
		paragraph.paint(canvas, 0, 0);

		createSnapshot();
	}

	private static void createSnapshot() {

		io.github.humbleui.skija.Image im = surface.makeImageSnapshot();
		Data encodeToData = im.encodeToData();

		File fi = new File(Math.random() + "word-wrap-example.png");

		FileOutputStream fis;
		try {
			fis = new FileOutputStream(fi);
			fis.write(encodeToData.getBytes());
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		im.close();

	}

}