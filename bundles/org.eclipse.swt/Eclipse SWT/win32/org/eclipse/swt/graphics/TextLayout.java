package org.eclipse.swt.graphics;

import java.io.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;

import io.github.humbleui.skija.*;
import io.github.humbleui.skija.paragraph.*;
import io.github.humbleui.types.*;

/**
 * This is the Skija TextLayout. The performance at scrolling for 1000s of lines in styled text is
 * insufficient.
 *
 * For this the fastCalculationMode works, but it also has bugs, 
 * because the font size calculation from SWT to Skija does not yet work properly.
 *
 */
public final class TextLayout extends Resource {

	// TODO: problem with using the right font sizes.
	// The innerGC has not the configuration data from the styled text like font
	// size etc.
	// the size conversion from SWT Font to Skija Font is not working.
	private static boolean fastCalculationMode = false;

	final static Set<TextLayout> textLayouts;

	static {
		textLayouts = new HashSet<>();
	}

	private io.github.humbleui.skija.Font font;
	private String text;
	int lineSpacingInPoints, ascent, descent, indent, wrapIndent,
			verticalIndentInPoints;
	boolean justify;
	int alignment;
	int[] tabs;
	int[] segments;
	char[] segmentsChars;
	int wrapWidth;
	int orientation;
	private double defaultTabWidth;
	private FontMetrics fixedLineMetrics;
	private double fixedLineMetricsDy;

	int[] lineOffsets;
	Rectangle[] lineBounds;

	// the following Callbacks are never freed
	static Callback textLayoutCallback2;
	static final byte[] SWT_OBJECT = {'S', 'W', 'T', '_', 'O', 'B', 'J', 'E',
			'C', 'T', '\0'};

	static final int TAB_COUNT = 32;
	static final int UNDERLINE_THICK = 1 << 16;
	int[] invalidOffsets;
	private StyleItem[] styles;
	int stylesCount;
	private Paragraph paragraph;
	private byte[] imageBytes;

	private int selectionStart;
	private int selectionEnd;
	private Color selectionForeground;
	private Color selectionBackground;
	private int textDirection;
	private Font swtFont;

	int nativeZoom = DPIUtil.getNativeDeviceZoom();
	private static Surface surface;
	private static GC innerGC;

	static final char LTR_MARK = '\u200E', RTL_MARK = '\u200F';

	static Color linkForeground;

	static class StyleItem {
		TextStyle style;
		int start, length;
		boolean lineBreak, softBreak, tab;

		@Override
		public String toString() {
			return "StyleItem {" + start + ", " + length + ", " + style + "}";
		}
	}

	/**
	 * Constructs a new instance of this class on the given device.
	 * <p>
	 * You must dispose the text layout when it is no longer required.
	 * </p>
	 *
	 * @param device
	 *            the device on which to allocate the text layout
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if device is null and there is
	 *                no current device</li>
	 *                </ul>
	 *
	 * @see #dispose()
	 */
	public TextLayout(Device device) {
		super(device);
		wrapWidth = ascent = descent = -1;
		alignment = SWT.LEFT;
		orientation = SWT.LEFT_TO_RIGHT;
		text = "";

		styles = new StyleItem[2];
		styles[0] = new StyleItem();
		styles[1] = new StyleItem();
		stylesCount = 2;

		init();
	}

	@Override
	void init() {

		synchronized (textLayouts) {
			if (surface == null) {
				Rectangle b = device.getBounds();

				surface = Surface.makeRaster(
						ImageInfo.makeN32Premul(b.width, b.height), 0,
						new SurfaceProps(PixelGeometry.RGB_H));

				// DPI Analysis
				Point dpi = device.getDPI();

			}

			if (innerGC == null) {
				innerGC = new GC(device);
				setFont(innerGC.getFont());
			}

			textLayouts.add(this);

		}

		super.init();

	}

	void checkLayout() {
		if (isDisposed())
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	}

	// not used...
	float[] computePolyline(int left, int top, int right, int bottom) {
		int height = bottom - top; // can be any number
		int width = 2 * height; // must be even
		int peaks = Compatibility.ceil(right - left, width);
		if (peaks == 0 && right - left > 2) {
			peaks = 1;
		}
		int length = ((2 * peaks) + 1) * 2;
		if (length < 0)
			return new float[0];

		float[] coordinates = new float[length];
		for (int i = 0; i < peaks; i++) {
			int index = 4 * i;
			coordinates[index] = left + (width * i);
			coordinates[index + 1] = bottom;
			coordinates[index + 2] = coordinates[index] + width / 2;
			coordinates[index + 3] = top;
		}
		coordinates[length - 2] = left + (width * peaks);
		coordinates[length - 1] = bottom;
		return coordinates;
	}

	// heuristic that doesn't work properly. This needs improvement drastically.
	private float getFontSize() {

		if (this.font != null)
			return (float) (this.font.getSize() * 1.4) + 2;

		if (ascent != -1 && descent != -1) {
			return (float) ((Math.abs(ascent) + Math.abs(descent)) / 2.0 * 1.5);
		}

		Font f = getFont();

		if (f == null)
			f = device.getSystemFont();

		FontData fd = f.getFontData()[0];

		return (float) (fd.getHeightF() * 2.2);

	}

	void computeRuns(GC gc) {

		computeRuns(gc, this.selectionStart, this.selectionEnd,
				this.selectionForeground, this.selectionBackground, 0);
	}

	void computeRuns(GC gc, int selectionStart, int selectionEnd,
			Color selectionForeground, Color selectionBackground, int flags) {

		if (selectionEnd < selectionStart) {
			selectionEnd = selectionStart;
		}

		// --------------------------------------------------------------------------------------------
		// In Skija there are the following methods in Paragraph:
		//
		// void updateTextAlign(Alignment alignment); void updateFontSize(int
		// from, int
		// to, float size); void updateForegroundPaint(int from, int to, Paint
		// paint);
		// void updateBackgroundPaint(int from, int to, Paint paint);
		//
		// These can be used for selections. In order to increase the
		// performance.
		//
		// But is seems this feature is not yet fully supported in skia or
		// skija. This
		// must be further checked.
		//
		// ---------------------------------------------------------------------------------------------

		if (paragraph != null && this.selectionStart == selectionStart
				&& this.selectionEnd == selectionEnd
				&& Objects.equals(this.selectionForeground, selectionForeground)
				&& Objects.equals(this.selectionBackground,
						selectionBackground))
			return;

		freeRuns();
		Canvas canvas = surface.getCanvas();
		canvas.clear(0x00000000);

		paragraph = createParagraph(selectionStart, selectionEnd,
				selectionForeground, selectionBackground);

		this.selectionStart = selectionStart;
		this.selectionEnd = selectionEnd;
		this.selectionForeground = selectionForeground;
		this.selectionBackground = selectionBackground;

		float lineWidth = getWidth();

		if (lineWidth < 0.00001) {
			lineWidth = Float.MAX_VALUE;
		}

		paragraph.layout(lineWidth);
		paragraph.paint(surface.getCanvas(), 0, 0);


		LineMetrics[] lineMetrics = paragraph.getLineMetrics();
		{
			// for the case of an empty string, we have to improvise a rectangle
			// with the right height.
			if (!"".equals(text)) {
				int count = (int) paragraph.getLineNumber();
				lineOffsets = new int[count + 1];
				lineOffsets[0] = 0;
				lineBounds = new Rectangle[count];
				int startx = 0;
				int starty = 0;

				for (int k = 0; k < count; k++) {
					var m = lineMetrics[k];

					m.getLeft();
					m.getRight();
					m.getHeight();
					m.getWidth();

					lineOffsets[k + 1] = (int) m.getEndIncludingNewline();

					if (k != 0) {
						starty += lineBounds[k - 1].height;
					}

					lineBounds[k] = new Rectangle(startx, starty,
							(int) m.getWidth(), (int) m.getHeight());
				}
			} else {

				lineOffsets = new int[2];
				lineOffsets[0] = 0;
				lineOffsets[1] = 0;
				lineBounds = new Rectangle[1];

				Font f = getFont();

				FontMetrics fm = innerGC.getFontMetrics();

				// TODO dummy calculation for the line height. This seems to
				// work, no idea whether it is right.
				int he = Math.abs(fm.getAscent()) + Math.abs(fm.getDescent())
						+ fm.getLeading();

				lineBounds[0] = new Rectangle(0, 0, 0, he);

			}
		}

		Rectangle b = getBounds();

		io.github.humbleui.skija.Image im = surface
				.makeImageSnapshot(new IRect(0, 0, b.width, b.height));

		if (im == null) {
			imageBytes = null;
			return;
		}

		imageBytes = EncoderPNG.encode(im).getBytes();

	}

	@Override
	void destroy() {
		freeRuns();
		font = null;
		text = null;
		styles = null;
		segments = null;
		segmentsChars = null;
		imageBytes = null;

		synchronized (textLayouts) {
			textLayouts.remove(this);

			if (textLayouts.isEmpty()) {
				surface.close();
				surface = null;

				innerGC.dispose();
				innerGC = null;

			}

		}

		if (paragraph != null) {
			if (!paragraph.isClosed())
				paragraph.close();

			paragraph = null;
		}

		super.destroy();

	}

	/**
	 * Draws the receiver's text using the specified GC at the specified point.
	 *
	 * @param gc
	 *            the GC to draw
	 * @param x
	 *            the x coordinate of the top left corner of the rectangular
	 *            area where the text is to be drawn
	 * @param y
	 *            the y coordinate of the top left corner of the rectangular
	 *            area where the text is to be drawn
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
	 *                </ul>
	 */

	public void draw(GC gc, int x, int y) {
		draw(gc, x, y, -1, -1, null, null);
	}

	/**
	 * Draws the receiver's text using the specified GC at the specified point.
	 *
	 * @param gc
	 *            the GC to draw
	 * @param x
	 *            the x coordinate of the top left corner of the rectangular
	 *            area where the text is to be drawn
	 * @param y
	 *            the y coordinate of the top left corner of the rectangular
	 *            area where the text is to be drawn
	 * @param selectionStart
	 *            the offset where the selections starts, or -1 indicating no
	 *            selection
	 * @param selectionEnd
	 *            the offset where the selections ends, or -1 indicating no
	 *            selection
	 * @param selectionForeground
	 *            selection foreground, or NULL to use the system default color
	 * @param selectionBackground
	 *            selection background, or NULL to use the system default color
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
	 *                </ul>
	 */

	public void draw(GC gc, int x, int y, int selectionStart, int selectionEnd,
			Color selectionForeground, Color selectionBackground) {
		draw(gc, x, y, selectionStart, selectionEnd, selectionForeground,
				selectionBackground, 0);
	}

	/**
	 * Draws the receiver's text using the specified GC at the specified point.
	 * <p>
	 * The parameter <code>flags</code> can include one of
	 * <code>SWT.DELIMITER_SELECTION</code> or <code>SWT.FULL_SELECTION</code>
	 * to specify the selection behavior on all lines except for the last line,
	 * and can also include <code>SWT.LAST_LINE_SELECTION</code> to extend the
	 * specified selection behavior to the last line.
	 * </p>
	 *
	 * @param gc
	 *            the GC to draw
	 * @param x
	 *            the x coordinate of the top left corner of the rectangular
	 *            area where the text is to be drawn
	 * @param y
	 *            the y coordinate of the top left corner of the rectangular
	 *            area where the text is to be drawn
	 * @param selectionStart
	 *            the offset where the selections starts, or -1 indicating no
	 *            selection
	 * @param selectionEnd
	 *            the offset where the selections ends, or -1 indicating no
	 *            selection
	 * @param selectionForeground
	 *            selection foreground, or NULL to use the system default color
	 * @param selectionBackground
	 *            selection background, or NULL to use the system default color
	 * @param flags
	 *            drawing options
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
	 *                </ul>
	 *
	 * @since 3.3
	 */

	public void draw(GC gc, int x, int y, int selectionStart, int selectionEnd,
			Color selectionForeground, Color selectionBackground, int flags) {
		checkLayout();

		if (gc == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (gc.isDisposed())
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		if (selectionForeground != null && selectionForeground.isDisposed())
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		if (selectionBackground != null && selectionBackground.isDisposed())
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);

		setFont(gc.getFont());

		y += getVerticalIndent();

		if (!gc.getFont().equals(getFont())) {
			setFont(gc.getFont());
			freeRuns();
		}

		computeRuns(gc, selectionStart, selectionEnd, selectionForeground,
				selectionBackground, flags);
		int length = translateOffset(text.length());
		if (length == 0 && flags == 0)
			return;

		if (imageBytes != null) {
			org.eclipse.swt.graphics.Image transferImage = new org.eclipse.swt.graphics.Image(
					gc.getDevice(), new ByteArrayInputStream(imageBytes));

			gc.drawImage(transferImage, x, y);

			transferImage.dispose();
		}

	}

	// for (int i = 0; i < lines.length; i++) {
	//
	// Rectangle r = lineBounds[i];
	// gc.drawRectangle(
	// new Rectangle(x + r.x, y + r.y, r.width, r.height));
	// gc.fillRectangle(
	// new Rectangle(x + r.x, y + r.y, r.width, r.height));
	// gc.drawText(text, x, y);
	//
	// }

	// gc.handle.saveGraphicsState();
	// NSPoint pt = new NSPoint();
	// pt.x = x;
	// pt.y = y;
	// NSRange range = new NSRange();
	// long numberOfGlyphs = layoutManager.numberOfGlyphs();
	// if (numberOfGlyphs > 0) {
	// range.location = 0;
	// range.length = numberOfGlyphs;
	// layoutManager.drawBackgroundForGlyphRange(range, pt);
	// }
	// boolean hasSelection = selectionStart <= selectionEnd
	// && selectionStart != -1 && selectionEnd != -1;
	// if (hasSelection || ((flags & SWT.LAST_LINE_SELECTION) != 0
	// && (flags & (SWT.FULL_SELECTION
	// | SWT.DELIMITER_SELECTION)) != 0)) {
	// if (selectionBackground == null)
	// selectionBackground = device
	// .getSystemColor(SWT.COLOR_LIST_SELECTION);
	// NSColor selectionColor = NSColor.colorWithDeviceRed(
	// selectionBackground.handle[0],
	// selectionBackground.handle[1],
	// selectionBackground.handle[2],
	// selectionBackground.handle[3]);
	// NSBezierPath path = NSBezierPath.bezierPath();
	// NSRect rect = new NSRect();
	// if (hasSelection) {
	// range.location = translateOffset(selectionStart);
	// range.length = translateOffset(
	// selectionEnd - selectionStart + 1);
	// long[] rectCount = new long[1];
	// long pArray = layoutManager.rectArrayForCharacterRange(
	// range, range, textContainer, rectCount);
	// for (int k = 0; k < rectCount[0]; k++, pArray += NSRect.sizeof) {
	// OS.memmove(rect, pArray, NSRect.sizeof);
	// fixRect(rect);
	// rect.x += pt.x;
	// rect.y += pt.y;
	// if (fixedLineMetrics != null)
	// rect.height = fixedLineMetrics.height;
	// rect.height = Math.max(rect.height, ascent + descent);
	// if ((flags & (SWT.FULL_SELECTION
	// | SWT.DELIMITER_SELECTION)) != 0
	// && (/* hasSelection || */ (flags
	// & SWT.LAST_LINE_SELECTION) != 0)) {
	// rect.height += spacing;
	// }
	// path.appendBezierPathWithRect(rect);
	// }
	// }
	// // TODO draw full selection for wrapped text and delimiter
	// // selection for hard breaks
	// if ((flags
	// & (SWT.FULL_SELECTION | SWT.DELIMITER_SELECTION)) != 0
	// && (/* hasSelection || */ (flags
	// & SWT.LAST_LINE_SELECTION) != 0)) {
	// NSRect bounds = lineBounds[lineBounds.length - 1];
	// rect.x = pt.x + bounds.x + bounds.width;
	// rect.y = y + bounds.y;
	// rect.width = (flags & SWT.FULL_SELECTION) != 0
	// ? 0x7fffffff
	// : (bounds.height + spacing) / 3;
	// rect.height = Math.max(bounds.height + spacing,
	// ascent + descent);
	// path.appendBezierPathWithRect(rect);
	// }
	// selectionColor.setFill();
	// path.fill();
	// }
	// if (numberOfGlyphs > 0) {
	// range.location = 0;
	// range.length = numberOfGlyphs;
	// double[] fg = gc.data.foreground;
	// boolean defaultFg = fg[0] == 0 && fg[1] == 0 && fg[2] == 0
	// && fg[3] == 1 && gc.data.alpha == 255;
	// if (!defaultFg) {
	// for (int i = 0; i < stylesCount - 1; i++) {
	// StyleItem run = styles[i];
	// if (run.style != null && run.style.foreground != null)
	// continue;
	// if (run.style != null && run.style.underline
	// && run.style.underlineStyle == SWT.UNDERLINE_LINK)
	// continue;
	// range.location = length != 0
	// ? translateOffset(run.start)
	// : 0;
	// range.length = translateOffset(styles[i + 1].start)
	// - range.location;
	// layoutManager.addTemporaryAttribute(
	// OS.NSForegroundColorAttributeName, gc.data.fg,
	// range);
	// }
	// }
	// NSPoint ptGlyphs = new NSPoint();
	// ptGlyphs.x = pt.x;
	// ptGlyphs.y = pt.y;
	// if (fixedLineMetrics != null)
	// ptGlyphs.y += fixedLineMetricsDy;
	// range.location = 0;
	// range.length = numberOfGlyphs;
	// layoutManager.drawGlyphsForGlyphRange(range, ptGlyphs);
	// if (!defaultFg) {
	// range.location = 0;
	// range.length = length;
	// layoutManager.removeTemporaryAttribute(
	// OS.NSForegroundColorAttributeName, range);
	// }
	// NSPoint point = new NSPoint();
	// for (int j = 0; j < stylesCount; j++) {
	// StyleItem run = styles[j];
	// TextStyle style = run.style;
	// if (style == null)
	// continue;
	// boolean drawUnderline = style.underline
	// && !isUnderlineSupported(style);
	// drawUnderline = drawUnderline && (j + 1 == stylesCount
	// || !style.isAdherentUnderline(styles[j + 1].style));
	// boolean drawBorder = style.borderStyle != SWT.NONE;
	// drawBorder = drawBorder && (j + 1 == stylesCount
	// || !style.isAdherentBorder(styles[j + 1].style));
	// if (!drawUnderline && !drawBorder)
	// continue;
	// int end = j + 1 < stylesCount
	// ? translateOffset(styles[j + 1].start - 1)
	// : length;
	// for (int i = 0; i < lineOffsets.length - 1; i++) {
	// int lineStart = untranslateOffset(lineOffsets[i]);
	// int lineEnd = untranslateOffset(lineOffsets[i + 1] - 1);
	// if (drawUnderline) {
	// int start = run.start;
	// for (int k = j; k > 0 && style.isAdherentUnderline(
	// styles[k - 1].style); k--) {
	// start = styles[k - 1].start;
	// }
	// start = translateOffset(start);
	// if (!(start > lineEnd || end < lineStart)) {
	// range.location = Math.max(lineStart, start);
	// range.length = Math.min(lineEnd, end) + 1
	// - range.location;
	// if (range.length > 0) {
	// long[] rectCount = new long[1];
	// long pArray = layoutManager
	// .rectArrayForCharacterRange(range,
	// range, textContainer,
	// rectCount);
	// NSRect rect = new NSRect();
	// gc.handle.saveGraphicsState();
	// double baseline = layoutManager.typesetter()
	// .baselineOffsetInLayoutManager(
	// layoutManager, lineStart);
	// double[] color = null;
	// if (style.underlineColor != null)
	// color = style.underlineColor.handle;
	// if (color == null
	// && style.foreground != null)
	// color = style.foreground.handle;
	// if (color != null) {
	// NSColor.colorWithDeviceRed(color[0],
	// color[1], color[2], color[3])
	// .setStroke();
	// }
	// for (int k = 0; k < rectCount[0]; k++, pArray += NSRect.sizeof) {
	// OS.memmove(rect, pArray, NSRect.sizeof);
	// fixRect(rect);
	// double underlineX = pt.x + rect.x;
	// double underlineY = pt.y + rect.y
	// + rect.height - baseline + 1;
	// NSBezierPath path = NSBezierPath
	// .bezierPath();
	// switch (style.underlineStyle) {
	// case SWT.UNDERLINE_ERROR : {
	// path.setLineWidth(2f);
	// path.setLineCapStyle(
	// OS.NSRoundLineCapStyle);
	// path.setLineJoinStyle(
	// OS.NSRoundLineJoinStyle);
	// path.setLineDash(
	// new double[]{1, 3f}, 2,
	// 0);
	// point.x = underlineX;
	// point.y = underlineY + 0.5f;
	// path.moveToPoint(point);
	// point.x = underlineX
	// + rect.width;
	// point.y = underlineY + 0.5f;
	// path.lineToPoint(point);
	// break;
	// }
	// case SWT.UNDERLINE_SQUIGGLE : {
	// gc.handle.setShouldAntialias(
	// false);
	// path.setLineWidth(1.0f);
	// path.setLineCapStyle(
	// OS.NSButtLineCapStyle);
	// path.setLineJoinStyle(
	// OS.NSMiterLineJoinStyle);
	// double lineBottom = pt.y
	// + rect.y + rect.height;
	// float squigglyThickness = 1;
	// float squigglyHeight = 2
	// * squigglyThickness;
	// double squigglyY = Math.min(
	// underlineY
	// - squigglyHeight
	// / 2,
	// lineBottom
	// - squigglyHeight
	// - 1);
	// float[] points = computePolyline(
	// (int) underlineX,
	// (int) squigglyY,
	// (int) (underlineX
	// + rect.width),
	// (int) (squigglyY
	// + squigglyHeight));
	// point.x = points[0] + 0.5f;
	// point.y = points[1] + 0.5f;
	// path.moveToPoint(point);
	// for (int p = 2; p < points.length; p += 2) {
	// point.x = points[p] + 0.5f;
	// point.y = points[p + 1]
	// + 0.5f;
	// path.lineToPoint(point);
	// }
	// break;
	// }
	// }
	// path.stroke();
	// }
	// gc.handle.restoreGraphicsState();
	// }
	// }
	// }
	// if (drawBorder) {
	// int start = run.start;
	// for (int k = j; k > 0 && style.isAdherentBorder(
	// styles[k - 1].style); k--) {
	// start = styles[k - 1].start;
	// }
	// start = translateOffset(start);
	// if (!(start > lineEnd || end < lineStart)) {
	// range.location = Math.max(lineStart, start);
	// range.length = Math.min(lineEnd, end) + 1
	// - range.location;
	// if (range.length > 0) {
	// long[] rectCount = new long[1];
	// long pArray = layoutManager
	// .rectArrayForCharacterRange(range,
	// range, textContainer,
	// rectCount);
	// NSRect rect = new NSRect();
	// gc.handle.saveGraphicsState();
	// double[] color = null;
	// if (style.borderColor != null)
	// color = style.borderColor.handle;
	// if (color == null
	// && style.foreground != null)
	// color = style.foreground.handle;
	// if (color != null) {
	// NSColor.colorWithDeviceRed(color[0],
	// color[1], color[2], color[3])
	// .setStroke();
	// }
	// int width = 1;
	// float[] dashes = null;
	// switch (style.borderStyle) {
	// case SWT.BORDER_SOLID :
	// break;
	// case SWT.BORDER_DASH :
	// dashes = width != 0
	// ? GC.LINE_DASH
	// : GC.LINE_DASH_ZERO;
	// break;
	// case SWT.BORDER_DOT :
	// dashes = width != 0
	// ? GC.LINE_DOT
	// : GC.LINE_DOT_ZERO;
	// break;
	// }
	// double[] lengths = null;
	// if (dashes != null) {
	// lengths = new double[dashes.length];
	// for (int k = 0; k < lengths.length; k++) {
	// lengths[k] = width == 0
	// ? dashes[k]
	// : dashes[k] * width;
	// }
	// }
	// for (int k = 0; k < rectCount[0]; k++, pArray += NSRect.sizeof) {
	// OS.memmove(rect, pArray, NSRect.sizeof);
	// fixRect(rect);
	// rect.x += pt.x + 0.5f;
	// rect.y += pt.y + 0.5f;
	// rect.width -= 0.5f;
	// rect.height -= 0.5f;
	// NSBezierPath path = NSBezierPath
	// .bezierPath();
	// path.setLineDash(lengths,
	// lengths != null
	// ? lengths.length
	// : 0,
	// 0);
	// path.appendBezierPathWithRect(rect);
	// path.stroke();
	// }
	// gc.handle.restoreGraphicsState();
	// }
	// }
	// }
	// }
	// }
	// }
	// gc.handle.restoreGraphicsState();
	// } finally {
	// gc.uncheckGC(pool);
	// }

	// void fixRect(NSRect rect) {
	// double right = -1;
	// for (int j = 0; j < lineBounds.length; j++) {
	// NSRect line = lineBounds[j];
	// if (rect.y <= line.y && line.y <= rect.y + rect.height) {
	// right = Math.max(right, line.x + line.width);
	// }
	// }
	// if (right != -1 && rect.x + rect.width > right) {
	// rect.width = right - rect.x;
	// }
	// }

	private synchronized Paragraph createParagraph(int selectionStart,
			int selectionEnd, Color selectionForeground,
			Color selectionBackground) {

		Paragraph paragraph = null;

		// placeholder for tab
		var tabPlaceholder = new PlaceholderStyle(40, // Width
				getFontSize(), // Height
				PlaceholderAlignment.MIDDLE, BaselineMode.ALPHABETIC, 0); // Offset

		if (selectionForeground == null)
			selectionForeground = device
					.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
		if (selectionBackground == null)
			selectionBackground = device
					.getSystemColor(SWT.COLOR_LIST_BACKGROUND);

		boolean hasSelection = selectionStart != -1 || selectionEnd != -1;

		ParagraphStyle style = new ParagraphStyle();
		style.setAlignment(Alignment.LEFT);

		FontMgr fontMgr = FontMgr.getDefault();

		FontCollection fc = new FontCollection();
		fc.setDefaultFontManager(fontMgr);
		Font f = getFont();

		if (f == null)
			f = device.getSystemFont();

		FontData fd = f.getFontData()[0];

		io.github.humbleui.skija.paragraph.TextStyle normal = new io.github.humbleui.skija.paragraph.TextStyle()
				.setFontSize(getFontSize())
				.setFontFamilies(new String[]{fd.getName()})
				.setColor(0xFF000000);

		io.github.humbleui.skija.paragraph.TextStyle selectionStyle = new io.github.humbleui.skija.paragraph.TextStyle()
				.setFontSize(getFontSize())
				.setFontFamilies(new String[]{fd.getName()})
				.setForeground(new Paint().setColor(SkijaGC
						.convertSWTColorToSkijaColor(selectionForeground)))
				.setBackground(new Paint().setColor(SkijaGC
						.convertSWTColorToSkijaColor(selectionBackground)));

		// io.github.humbleui.skija.paragraph.TextStyle selectionStyle = new
		// io.github.humbleui.skija.paragraph.TextStyle()
		// .setFontSize(getFontSize())
		// .setFontFamilies(new String[]{fd.getName()})
		// .setColor(0xFFFF0000);

		int offset = 0;

		Canvas canvas = surface.getCanvas();
		canvas.clear(0x00000000);

		try (ParagraphBuilder paragraphBuilder = new ParagraphBuilder(style,
				fc)) {

			// paragraphBuilder.addPlaceholder(new
			// PlaceholderStyle(selectionStart,
			// selectionEnd, null, null, offset));

			// classical spaces ' ' will be ignored in the paragraph, this means
			// '\u00A0' is necessary.
			String str = text.replace(" ", "\u00A0");
			boolean textIsStyled = false;

			for (int j = 0; j < stylesCount; j++) {
				var si = styles[j];

				StyleItem next = null;

				if (styles.length > j + 1)
					next = styles[j + 1];

				int nextStyleStart = str.length();
				if (next != null) {
					nextStyleStart = Math.min(str.length(),
							styles[j + 1].start);
				}

				if (si.start > nextStyleStart)
					continue;

				String s = str.substring(si.start, nextStyleStart);
				if (s != "") {
					if (hasSelection) {

						var ts = convertToTextStyle(si, fd);

						for (int i = si.start; i < nextStyleStart; i++) {

							if (selectionStart <= i && i <= selectionEnd) {

								paragraphBuilder.pushStyle(selectionStyle);

								addText(paragraphBuilder, tabPlaceholder,
										str.substring(i,
												Math.min(nextStyleStart,
														selectionEnd + 1)));

								paragraphBuilder.popStyle();
								i = Math.min(nextStyleStart, selectionEnd + 1)
										- 1;

							} else {
								if (i > selectionStart) {
									paragraphBuilder.pushStyle(ts);

									addText(paragraphBuilder, tabPlaceholder,
											str.substring(i, nextStyleStart));

									paragraphBuilder.popStyle();
									i = nextStyleStart - 1;
								} else {
									paragraphBuilder.pushStyle(ts);

									addText(paragraphBuilder, tabPlaceholder,
											str.substring(i,
													Math.min(nextStyleStart,
															selectionStart)));

									paragraphBuilder.popStyle();
									i = Math.min(nextStyleStart, selectionStart)
											- 1;
								}

							}

						}

						ts.close();

					} else {

						var ts = convertToTextStyle(si, fd);
						paragraphBuilder.pushStyle(ts);

						addText(paragraphBuilder, tabPlaceholder, s);

						paragraphBuilder.popStyle();

						ts.close();
					}
				}

				textIsStyled = true;

			}

			if (!textIsStyled) {
				paragraphBuilder.pushStyle(normal);
				paragraphBuilder.addText(str);
				paragraphBuilder.popStyle();

			}

			paragraph = paragraphBuilder.build();
		} catch (Exception e) {
			e.printStackTrace();
		}

		selectionStyle.close();
		normal.close();

		return paragraph;
	}

	private void addText(ParagraphBuilder paragraphBuilder,
			PlaceholderStyle tabPlaceholder, String substring) {

		int tabIndex = -1;
		String s = substring;
		while ((tabIndex = s.indexOf("\t")) != -1) {

			if (tabIndex == 0) {
				paragraphBuilder.addPlaceholder(tabPlaceholder);
				s = s.substring(1);
			} else {
				paragraphBuilder.addText(s.substring(0, tabIndex));
				s = s.substring(tabIndex);
			}

		}

		paragraphBuilder.addText(s);

	}

	private io.github.humbleui.skija.paragraph.TextStyle convertToTextStyle(
			StyleItem si, FontData fd) {

		TextStyle ts = si.style;

		// int foreground = SkijaGC.convertSWTColorToSkijaColor(ts.foreground);
		// int background = SkijaGC.convertSWTColorToSkijaColor(ts.background);

		if (ts == null) {

			int foreground = SkijaGC.convertSWTColorToSkijaColor(
					device.getSystemColor(SWT.COLOR_BLACK));

			Paint foreP = new Paint().setColor(foreground);

			return new io.github.humbleui.skija.paragraph.TextStyle()
					.setFontSize(getFontSize())
					.setFontFamilies(new String[]{fd.getName()})
					.setForeground(foreP);

		}

		int foreground = SkijaGC
				.convertSWTColorToSkijaColor(ts.foreground != null
						? ts.foreground
						: device.getSystemColor(SWT.COLOR_BLACK));
		Paint foreP = new Paint().setColor(foreground);

		Paint backP = null;

		if (ts.background != null) {
			int background = SkijaGC.convertSWTColorToSkijaColor(ts.background);
			backP = new Paint().setColor(background);

		}

		FontStyle fs = FontStyle.NORMAL;

		float fontSize = getFontSize();
		if (ts.font != null && ts.font.getFontData() != null
				&& ts.font.getFontData().length >= 1) {
			fd = ts.font.getFontData()[0];
			// fontSize = (float) ((fd.getHeightF() * 1.4) + 2);

			fs = ((fd.getStyle() & SWT.NORMAL) != 0) ? FontStyle.NORMAL : null;

			if (fs == null) {
				fs = (fd.getStyle() & SWT.BOLD) != 0 ? FontStyle.BOLD : null;
			}

			if ((fd.getStyle() & SWT.ITALIC) != 0) {

				if (fs == null) {
					fs = FontStyle.ITALIC;

				}

				if (fs == FontStyle.BOLD) {
					fs = FontStyle.BOLD_ITALIC;
				}

			}

			if (fs == null)
				fs = FontStyle.NORMAL;

		}

		// boolean underline = ts.underline;
		// boolean underline = ts.underline;
		// boolean overline = false;
		// boolean strikethrough = ts.strikeout;
		//
		// // TODO can we use multiple decoratoins at one TextStyle??
		// Color underlineCol = ts.underlineColor;
		// // Color underlineCol = getDevice().getSystemColor(SWT.COLOR_RED);
		// Color strikethroughCol = ts.strikeoutColor;
		//
		// DecorationStyle ds = new DecorationStyle(underline, overline,
		// strikethrough, false,
		// SkijaGC.convertSWTColorToSkijaColor(underlineCol),
		// DecorationLineStyle.SOLID, 1);

		io.github.humbleui.skija.paragraph.TextStyle textSty = new io.github.humbleui.skija.paragraph.TextStyle()
				.setFontStyle(fs).setFontSize(fontSize)
				.setFontFamilies(new String[]{fd.getName()})
				.setForeground(foreP) //
				.setBackground(backP);

		if (backP != null)
			textSty = textSty.setBackground(backP);

		return textSty;

	}

	void freeRuns() {
		lineBounds = null;
		lineOffsets = null;

		if (paragraph != null) {
			if (paragraph != null && !paragraph.isClosed())
				paragraph.close();

			paragraph = null;
		}

		selectionStart = -1;
		selectionEnd = -1;
		selectionForeground = null;
		selectionBackground = null;

		imageBytes = null;

		// for (int i = 0; i < stylesCount - 1; i++) {
		// StyleItem run = styles[i];
		// if (run.cell != null) {
		// OS.object_setInstanceVariable(run.cell.id, SWT_OBJECT, 0);
		// run.cell.release();
		// run.cell = null;
		// OS.DeleteGlobalRef(run.jniRef);
		// run.jniRef = 0;
		// }
		// }
	}

	/**
	 * Returns the receiver's horizontal text alignment, which will be one of
	 * <code>SWT.LEFT</code>, <code>SWT.CENTER</code> or <code>SWT.RIGHT</code>.
	 *
	 * @return the alignment used to positioned text horizontally
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public int getAlignment() {
		checkLayout();
		return alignment;
	}

	/**
	 * Returns the ascent of the receiver.
	 *
	 * @return the ascent
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #getDescent()
	 * @see #setDescent(int)
	 * @see #setAscent(int)
	 * @see #getLineMetrics(int)
	 */

	public int getAscent() {
		checkLayout();
		return ascent;
	}

	/**
	 * Returns the bounds of the receiver. The width returned is either the
	 * width of the longest line or the width set using
	 * {@link TextLayout#setWidth(int)}. To obtain the text bounds of a line use
	 * {@link TextLayout#getLineBounds(int)}.
	 *
	 * @return the bounds of the receiver
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #setWidth(int)
	 * @see #getLineBounds(int)
	 */

	public Rectangle getBounds() {
		checkLayout();

		if (lineBounds == null && fastCalculationMode) {

			FontMetrics fm = innerGC.getFontMetrics();

			int height = Math.abs(fm.getAscent()) + Math.abs(fm.getDescent())
					+ Math.abs(fm.getLeading());

			double avgWidth = fm.getAverageCharacterWidth();

			String[] splits = text.split(System.lineSeparator());

			if (splits.length == 0)
				return new Rectangle(0, 0, 0, height);

			int linesCount = splits.length;

			int width = 0;
			int maxChars = 0;

			int wrapWidth = getWidth();
			double charsPerLine = 0.0;

			if (wrapWidth != -1) {
				charsPerLine = ((double) wrapWidth) / avgWidth;
			}

			int countedLines = 0;
			for (String s : splits) {
				int strgLength = s.length();
				maxChars = Math.max(maxChars, strgLength);

				if (wrapWidth != -1) {

					if (maxChars <= charsPerLine)
						countedLines++;
					else {
						double linesOfSplit = strgLength / charsPerLine;
						int linesOfSplitInt = ((int) linesOfSplit) + 1;
						countedLines += linesOfSplitInt;
					}
				}
			}

			if (wrapWidth == -1) {
				return new Rectangle(0, 0, (int) (maxChars * avgWidth) + 20,
						height * linesCount);
			} else {

				return new Rectangle(0, 0, wrapWidth, height * countedLines);

			}

		}

		computeRuns(null);

		int wrapWidth = getWidth();

		int width = 0;
		int height = 0;

		Point initial = new Point(0, 0);

		for (Rectangle r : lineBounds) {

			if (initial == null)
				initial = new Point(r.x, r.y);

			width = Math.max(width, r.width);

			height += r.height;

		}

		int lineWidth = getWidth();

		if (width < lineWidth)
			width = lineWidth;

		return new Rectangle(initial.x, initial.y, width, height);
	}

	/**
	 * Returns the bounds for the specified range of characters. The bounds is
	 * the smallest rectangle that encompasses all characters in the range. The
	 * start and end offsets are inclusive and will be clamped if out of range.
	 *
	 * @param start
	 *            the start offset
	 * @param end
	 *            the end offset
	 * @return the bounds of the character range
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public Rectangle getBounds(int start, int end) {
		checkLayout();

		computeRuns(null);

		if (end < 0) {
			return new Rectangle(0, 0, 0, 0);
		}

		Point startTop = getLocation(start, false);
		Point endTop = getLocation(end, false);

		// TODO: this is only an approximation...

		Rectangle r = new Rectangle(startTop.x, startTop.y,
				endTop.x - startTop.x, endTop.y - startTop.y + 20);

		return r;
	}

	/**
	 * Returns the descent of the receiver.
	 *
	 * @return the descent
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #getAscent()
	 * @see #setAscent(int)
	 * @see #setDescent(int)
	 * @see #getLineMetrics(int)
	 */

	public int getDescent() {
		checkLayout();
		return descent;
	}

	/**
	 * Returns the default font currently being used by the receiver to draw and
	 * measure text.
	 *
	 * @return the receiver's font
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public Font getFont() {
		checkLayout();

		if (swtFont != null)
			return swtFont;

		return innerGC.getFont();
	}

	/**
	 * Returns the receiver's indent.
	 *
	 * @return the receiver's indent
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @since 3.2
	 */

	public int getIndent() {
		checkLayout();
		return indent;
	}

	/**
	 * Returns the receiver's justification.
	 *
	 * @return the receiver's justification
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @since 3.2
	 */
	public boolean getJustify() {
		checkLayout();
		return justify;
	}

	/**
	 * Returns the embedding level for the specified character offset. The
	 * embedding level is usually used to determine the directionality of a
	 * character in bidirectional text.
	 *
	 * @param offset
	 *            the character offset
	 * @return the embedding level
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the character offset is
	 *                out of range</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public int getLevel(int offset) {
		checkLayout();

		return 1;

		// NSAutoreleasePool pool = null;
		// if (!NSThread.isMainThread())
		// pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
		// try {
		// computeRuns();
		// int length = text.length();
		// if (!(0 <= offset && offset <= length))
		// SWT.error(SWT.ERROR_INVALID_RANGE);
		// offset = translateOffset(offset);
		// long glyphOffset = layoutManager
		// .glyphIndexForCharacterAtIndex(offset);
		// NSRange range = new NSRange();
		// range.location = glyphOffset;
		// range.length = 1;
		// byte[] bidiLevels = new byte[1];
		// layoutManager.getGlyphsInRange(range, 0, 0, 0, 0, bidiLevels);
		// return bidiLevels[0];
		// } finally {
		// if (pool != null)
		// pool.release();
		// }
	}

	/**
	 * Returns the line offsets. Each value in the array is the offset for the
	 * first character in a line except for the last value, which contains the
	 * length of the text.
	 *
	 * @return the line offsets
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public int[] getLineOffsets() {
		checkLayout();
		computeRuns(null);
		int[] offsets = new int[lineOffsets.length];
		for (int i = 0; i < offsets.length; i++) {
			offsets[i] = untranslateOffset(lineOffsets[i]);
		}

		return offsets;
	}

	/**
	 * Returns the index of the line that contains the specified character
	 * offset.
	 *
	 * @param offset
	 *            the character offset
	 * @return the line index
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the character offset is
	 *                out of range</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public int getLineIndex(int offset) {
		checkLayout();

		if (offset == 0 || offset <= 0)
			return 0;

		computeRuns(null);
		int length = text.length();

		if (!(0 <= offset && offset <= length))
			SWT.error(SWT.ERROR_INVALID_RANGE);
		offset = translateOffset(offset);
		for (int line = 0; line < lineOffsets.length - 1; line++) {
			if (lineOffsets[line + 1] > offset) {
				return line;
			}
		}

		return lineBounds.length - 1;
	}

	/**
	 * Returns the bounds of the line for the specified line index.
	 *
	 * @param lineIndex
	 *            the line index
	 * @return the line bounds
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the line index is out of
	 *                range</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public Rectangle getLineBounds(int lineIndex) {

		checkLayout();

		if (lineBounds == null && lineIndex == 0 && fastCalculationMode) {
			FontMetrics fm = innerGC.getFontMetrics();

			return new Rectangle(0, 0, 0, Math.abs(fm.getAscent())
					+ Math.abs(fm.getDescent()) + Math.abs(fm.getLeading()));

		}
		computeRuns(null);
		return lineBounds[lineIndex];

		// NSAutoreleasePool pool = null;
		// if (!NSThread.isMainThread())
		// pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
		// try {
		// computeRuns();
		// if (!(0 <= lineIndex && lineIndex < lineBounds.length))
		// SWT.error(SWT.ERROR_INVALID_RANGE);
		// NSRect rect = lineBounds[lineIndex];
		// int height = Math.max((int) Math.ceil(rect.height),
		// ascent + descent);
		// return new Rectangle((int) rect.x, (int) rect.y,
		// (int) Math.ceil(rect.width), height);
		// } finally {
		// if (pool != null)
		// pool.release();
		// }
	}

	/**
	 * Returns the receiver's line count. This includes lines caused by
	 * wrapping.
	 *
	 * @return the line count
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public int getLineCount() {
		checkLayout();

		computeRuns(null);

		if (lineOffsets == null || lineOffsets.length == 0)
			return 0;

		return lineOffsets.length - 1;

		// NSAutoreleasePool pool = null;
		// if (!NSThread.isMainThread())
		// pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
		// try {
		// computeRuns();
		// return lineOffsets.length - 1;
		// } finally {
		// if (pool != null)
		// pool.release();
		// }
	}

	/**
	 * Returns the font metrics for the specified line index.
	 *
	 * @param lineIndex
	 *            the line index
	 * @return the font metrics
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the line index is out of
	 *                range</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public FontMetrics getLineMetrics(int lineIndex) {
		checkLayout();

		// if (font == null)
		// setFont(null);
		// if (true) {
		// IFontMetrics fm = new SkijaFontMetrics(font.getMetrics());
		// return fm;
		// }
		//
		// // TODO a GC just for getting the FontMetrics is wrong. This should
		// be
		// // done differently.
		FontMetrics fm = innerGC.getFontMetrics();

		return fm;

		// NSAutoreleasePool pool = null;
		// if (!NSThread.isMainThread())
		// pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
		// try {
		// computeRuns();
		// int lineCount = getLineCount();
		// if (!(0 <= lineIndex && lineIndex < lineCount))
		// SWT.error(SWT.ERROR_INVALID_RANGE);
		// if (fixedLineMetrics != null)
		// return fixedLineMetrics.makeCopy();
		// int length = text.length();
		// if (length == 0) {
		// Font font = this.font != null ? this.font : device.systemFont;
		// int ascent = (int) layoutManager
		// .defaultBaselineOffsetForFont(font.handle);
		// int descent = (int) layoutManager
		// .defaultLineHeightForFont(font.handle) - ascent;
		// ascent = Math.max(ascent, this.ascent);
		// descent = Math.max(descent, this.descent);
		// return FontMetrics.cocoa_new(ascent, descent, 0.0, 0,
		// ascent + descent);
		// }
		// Rectangle rect = getLineBounds(lineIndex);
		// int baseline = (int) layoutManager.typesetter()
		// .baselineOffsetInLayoutManager(layoutManager,
		// getLineOffsets()[lineIndex]);
		// return FontMetrics.cocoa_new(rect.height - baseline, baseline, 0.0,
		// 0, rect.height);
		// } finally {
		// if (pool != null)
		// pool.release();
		// }
	}

	Color getLinkForeground() {
		if (linkForeground == null) {

			linkForeground = device.getSystemColor(SWT.COLOR_LINK_FOREGROUND);

			// /*
			// * Color used is same as SWT.COLOR_LINK_FOREGROUND computed in
			// * Display.getWidgetColorRGB()
			// */
			// NSTextView textView = (NSTextView) new NSTextView().alloc();
			// textView.init();
			// NSDictionary dict = textView.linkTextAttributes();
			// linkForeground = new NSColor(
			// dict.valueForKey(OS.NSForegroundColorAttributeName));
			// textView.release();
		}
		return linkForeground;
	}

	/**
	 * Returns the location for the specified character offset. The
	 * <code>trailing</code> argument indicates whether the offset corresponds
	 * to the leading or trailing edge of the cluster.
	 *
	 * @param offset
	 *            the character offset
	 * @param trailing
	 *            the trailing flag
	 * @return the location of the character offset
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #getOffset(Point, int[])
	 * @see #getOffset(int, int, int[])
	 */

	public Point getLocation(int offset, boolean trailing) {
		checkLayout();
		computeRuns(null);
		int length = text.length();
		if (!(0 <= offset && offset <= length)) {
			SWT.error(SWT.ERROR_INVALID_RANGE);
		}

		if (offset < 0) {
			offset = 0;
		}

		if (offset == text.length()) {
			TextBox[] tbs = paragraph.getRectsForRange(offset - 1, offset,
					RectHeightMode.TIGHT, RectWidthMode.TIGHT);

			if (offset == 0) {
				return new Point(0, 0);
			}

			TextBox tb = tbs[0];

			int x = (int) tb.getRect().getRight();
			int y = (int) tb.getRect().getTop();

			return new Point(x, y);

		}

		TextBox[] tbs = paragraph.getRectsForRange(offset, offset + 1,
				RectHeightMode.TIGHT, RectWidthMode.TIGHT);

		if (tbs == null || tbs.length != 1)
			throw new IllegalStateException("Unexpected. getLocation( " + offset
					+ " " + trailing + "  " + Arrays.toString(tbs));

		TextBox tb = tbs[0];

		int x = 0;
		if (trailing)
			x = (int) tb.getRect().getRight();
		else {
			x = (int) tb.getRect().getLeft();
		}
		int y = (int) tb.getRect().getTop();

		return new Point(x, y);

	}

	/**
	 * Returns the next offset for the specified offset and movement type. The
	 * movement is one of <code>SWT.MOVEMENT_CHAR</code>,
	 * <code>SWT.MOVEMENT_CLUSTER</code>, <code>SWT.MOVEMENT_WORD</code>,
	 * <code>SWT.MOVEMENT_WORD_END</code> or
	 * <code>SWT.MOVEMENT_WORD_START</code>.
	 *
	 * @param offset
	 *            the start offset
	 * @param movement
	 *            the movement type
	 * @return the next offset
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the offset is out of
	 *                range</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #getPreviousOffset(int, int)
	 */

	public int getNextOffset(int offset, int movement) {

		int r;
		if (text.length() <= offset)
			r = 0;

		else
			r = offset + 1;

		return r;
		// NSAutoreleasePool pool = null;
		// if (!NSThread.isMainThread())
		// pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
		// try {
		// return _getOffset(offset, movement, true);
		// } finally {
		// if (pool != null)
		// pool.release();
		// }
	}

	int _getOffset(int offset, int movement, boolean forward) {
		// checkLayout();
		// computeRuns();
		// int length = text.length();
		// if (!(0 <= offset && offset <= length))
		// SWT.error(SWT.ERROR_INVALID_RANGE);
		// if (forward && offset == length)
		// return length;
		// if (!forward && offset == 0)
		// return 0;
		// int step = forward ? 1 : -1;
		// if ((movement & SWT.MOVEMENT_CHAR) != 0)
		// return offset + step;
		// switch (movement) {
		// case SWT.MOVEMENT_CLUSTER :
		// // TODO cluster
		// offset += step;
		// if (0 <= offset && offset < length) {
		// char ch = text.charAt(offset);
		// if (0xDC00 <= ch && ch <= 0xDFFF) {
		// if (offset > 0) {
		// ch = text.charAt(offset - 1);
		// if (0xD800 <= ch && ch <= 0xDBFF) {
		// offset += step;
		// }
		// }
		// }
		// }
		// break;
		// case SWT.MOVEMENT_WORD : {
		// offset = translateOffset(offset);
		// offset = (int) textStorage.nextWordFromIndex(offset, forward);
		// return untranslateOffset(offset);
		// }
		// case SWT.MOVEMENT_WORD_END : {
		// offset = translateOffset(offset);
		// if (forward) {
		// offset = (int) textStorage.nextWordFromIndex(offset, true);
		// } else {
		// length = translateOffset(length);
		// int result = 0;
		// while (result < length) {
		// int wordEnd = (int) textStorage
		// .nextWordFromIndex(result, true);
		// if (wordEnd >= offset) {
		// offset = result;
		// break;
		// }
		// result = wordEnd;
		// }
		// }
		// return untranslateOffset(offset);
		// }
		// case SWT.MOVEMENT_WORD_START : {
		// offset = translateOffset(offset);
		// if (forward) {
		// int result = translateOffset(length);
		// while (result > 0) {
		// int wordStart = (int) textStorage
		// .nextWordFromIndex(result, false);
		// if (wordStart <= offset) {
		// offset = result;
		// break;
		// }
		// result = wordStart;
		// }
		// } else {
		// offset = (int) textStorage.nextWordFromIndex(offset, false);
		// }
		// return untranslateOffset(offset);
		// }
		// }
		return offset;
	}

	/**
	 * Returns the character offset for the specified point. For a typical
	 * character, the trailing argument will be filled in to indicate whether
	 * the point is closer to the leading edge (0) or the trailing edge (1).
	 * When the point is over a cluster composed of multiple characters, the
	 * trailing argument will be filled with the position of the character in
	 * the cluster that is closest to the point.
	 *
	 * @param point
	 *            the point
	 * @param trailing
	 *            the trailing buffer
	 * @return the character offset
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the trailing length is
	 *                less than <code>1</code></li>
	 *                <li>ERROR_NULL_ARGUMENT - if the point is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #getLocation(int, boolean)
	 */
	public int getOffset(Point point, int[] trailing) {
		checkLayout();
		if (point == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		return getOffset(point.x, point.y, trailing);
	}

	/**
	 * Returns the character offset for the specified point. For a typical
	 * character, the trailing argument will be filled in to indicate whether
	 * the point is closer to the leading edge (0) or the trailing edge (1).
	 * When the point is over a cluster composed of multiple characters, the
	 * trailing argument will be filled with the position of the character in
	 * the cluster that is closest to the point.
	 *
	 * @param x
	 *            the x coordinate of the point
	 * @param y
	 *            the y coordinate of the point
	 * @param trailing
	 *            the trailing buffer
	 * @return the character offset
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the trailing length is
	 *                less than <code>1</code></li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #getLocation(int, boolean)
	 */

	public int getOffset(int x, int y, int[] trailing) {
		checkLayout();
		computeRuns(null);

		if (trailing != null && trailing.length < 1)
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		int length = text.length();
		if (length == 0) {
			return 0;
		}

		Rectangle line = null;

		// TODO use binary search.
		for (int i = 0; i < lineBounds.length; i++) {

			if (lineBounds[i].contains(x, y)) {
				line = lineBounds[i];
				break;
			}

		}

		if (line == null) {
			if (x <= lineBounds[0].x && y <= lineBounds[0].y) {
				return 0;
			} else {
				return text.length();
			}
		}

		int position = 0;

		PositionWithAffinity affinity = paragraph
				.getGlyphPositionAtCoordinate((float) x, (float) y);

		if (affinity.getAffinity() == Affinity.UPSTREAM) {

			position = affinity.getPosition() - 1;
			if (trailing != null)
				trailing[0] = 1;
		} else
			position = affinity.getPosition();

		return position;

		// System.out.println("Rectangle: " + line + " index: " + lineIndex);
		//
		// if (line == null) {
		//
		// if (x <= lineBounds[0].x && y <= lineBounds[0].y) {
		// System.out.println("Result: " + 0);
		// return 0;
		// } else {
		// System.out.println("Result: " + text.length());
		// return text.length();
		// }
		//
		// }
		//
		// FontMetrics fm = innerGC.getFontMetrics();
		//
		// double avgw = fm.getAverageCharacterWidth();
		//
		// int min = x / ((int) avgw + 2);
		//
		// int max = x / ((int) (avgw - 2));
		//
		// String s = lines[lineIndex];
		//
		// // TODO use binary search
		//
		// int minPosition = min;
		// int maxPosotion = max; // TODO use trailing...
		// for (int i = min; i < Math.min(max, s.length()); i++) {
		// Point te = innerGC.textExtent(s.substring(0, i));
		// if (te.x < x) {
		// minPosition = i;
		// if (te.x > x) {
		// maxPosotion = i;
		// break;
		// }
		// }
		//
		// }
		//
		// System.out.println("Result: " + minPosition);
		// return minPosition;

		// NSPoint pt = new NSPoint();
		// pt.x = x;
		// pt.y = y - getVerticalIndent();
		// double[] partialFraction = new double[1];
		// long glyphIndex = layoutManager.glyphIndexForPoint(pt,
		// textContainer, partialFraction);
		// long charOffset = layoutManager
		// .characterIndexForGlyphAtIndex(glyphIndex);
		// if (textStorage.string().characterAtIndex(charOffset) == '\n')
		// charOffset--;
		// int offset = (int) charOffset;
		// offset = Math.min(untranslateOffset(offset), length - 1);
		// if (trailing != null) {
		// trailing[0] = Math.round((float) partialFraction[0]);
		// if (partialFraction[0] >= 0.5) {
		// char ch = text.charAt(offset);
		// if (0xD800 <= ch && ch <= 0xDBFF) {
		// if (offset + 1 < length) {
		// ch = text.charAt(offset + 1);
		// if (0xDC00 <= ch && ch <= 0xDFFF) {
		// trailing[0]++;
		// }
		// }
		// }
		// }
		// }
		// return offset;
		// } finally {
		// if (pool != null)
		// pool.release();
		// }
	}

	/**
	 * Returns the orientation of the receiver.
	 *
	 * @return the orientation style
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public int getOrientation() {
		checkLayout();
		return orientation;
	}

	/**
	 * Returns the previous offset for the specified offset and movement type.
	 * The movement is one of <code>SWT.MOVEMENT_CHAR</code>,
	 * <code>SWT.MOVEMENT_CLUSTER</code> or <code>SWT.MOVEMENT_WORD</code>,
	 * <code>SWT.MOVEMENT_WORD_END</code> or
	 * <code>SWT.MOVEMENT_WORD_START</code>.
	 *
	 * @param offset
	 *            the start offset
	 * @param movement
	 *            the movement type
	 * @return the previous offset
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the offset is out of
	 *                range</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #getNextOffset(int, int)
	 */

	public int getPreviousOffset(int offset, int movement) {

		computeRuns(null);

		int lineIndex = getLineIndex(offset);
		int lineOffset = lineOffsets[lineIndex];

		int r = -1;

		if (lineOffset < offset)
			r = offset - 1;
		else
			r = 0;

		return r;

		// NSAutoreleasePool pool = null;
		// if (!NSThread.isMainThread())
		// pool = (NSAutoreleasePool) new NSAutoreleasePool().alloc().init();
		// try {
		// return _getOffset(offset, movement, false);
		// } finally {
		// if (pool != null)
		// pool.release();
		// }
	}

	/**
	 * Gets the ranges of text that are associated with a
	 * <code>TextStyle</code>.
	 *
	 * @return the ranges, an array of offsets representing the start and end of
	 *         each text style.
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #getStyles()
	 *
	 * @since 3.2
	 */

	public int[] getRanges() {
		checkLayout();

		int[] result = new int[styles.length * 2];
		int count = 0;
		for (int i = 0; i < styles.length - 1; i++) {
			if (styles[i].style != null) {
				result[count++] = styles[i].start;
				result[count++] = styles[i + 1].start - 1;
			}
		}
		if (count != result.length) {
			int[] newResult = new int[count];
			System.arraycopy(result, 0, newResult, 0, count);
			result = newResult;
		}

		return result;
	}

	/**
	 * Returns the text segments offsets of the receiver.
	 *
	 * @return the text segments offsets
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public int[] getSegments() {
		checkLayout();

		return segments;
	}

	/**
	 * Returns the segments characters of the receiver.
	 *
	 * @return the segments characters
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @since 3.6
	 */
	public char[] getSegmentsChars() {
		checkLayout();

		return segmentsChars;
	}

	String getSegmentsText() {
		int length = text.length();
		if (length == 0)
			return text;
		if (segments == null)
			return text;
		int nSegments = segments.length;
		if (nSegments == 0)
			return text;
		if (segmentsChars == null) {
			if (nSegments == 1)
				return text;
			if (nSegments == 2) {
				if (segments[0] == 0 && segments[1] == length)
					return text;
			}
		}
		char[] oldChars = new char[length];
		text.getChars(0, length, oldChars, 0);
		char[] newChars = new char[length + nSegments];
		int charCount = 0, segmentCount = 0;
		char defaultSeparator = orientation == SWT.RIGHT_TO_LEFT
				? RTL_MARK
				: LTR_MARK;
		while (charCount < length) {
			if (segmentCount < nSegments
					&& charCount == segments[segmentCount]) {
				char separator = segmentsChars != null
						&& segmentsChars.length > segmentCount
								? segmentsChars[segmentCount]
								: defaultSeparator;
				newChars[charCount + segmentCount++] = separator;
			} else {
				newChars[charCount + segmentCount] = oldChars[charCount++];
			}
		}
		while (segmentCount < nSegments) {
			segments[segmentCount] = charCount;
			char separator = segmentsChars != null
					&& segmentsChars.length > segmentCount
							? segmentsChars[segmentCount]
							: defaultSeparator;
			newChars[charCount + segmentCount++] = separator;
		}
		return new String(newChars, 0, newChars.length);
	}

	/**
	 * Returns the line spacing of the receiver.
	 *
	 * @return the line spacing
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public int getSpacing() {
		checkLayout();

		return lineSpacingInPoints;
	}

	/**
	 * Returns the vertical indent of the receiver.
	 *
	 * @return the vertical indent
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * @since 3.109
	 */

	public int getVerticalIndent() {
		checkLayout();
		return verticalIndentInPoints;
	}

	/**
	 * Gets the style of the receiver at the specified character offset.
	 *
	 * @param offset
	 *            the text offset
	 * @return the style or <code>null</code> if not set
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the character offset is
	 *                out of range</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public TextStyle getStyle(int offset) {
		checkLayout();
		int length = text.length();
		if (!(0 <= offset && offset < length))
			SWT.error(SWT.ERROR_INVALID_RANGE);
		for (int i = 1; i < stylesCount; i++) {
			if (styles[i].start > offset) {

				TextStyle ts = styles[i - 1].style;
				return ts;
			}
		}
		return null;
	}

	/**
	 * Gets all styles of the receiver.
	 *
	 * @return the styles
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #getRanges()
	 *
	 * @since 3.2
	 */

	public TextStyle[] getStyles() {
		checkLayout();
		TextStyle[] result = new TextStyle[stylesCount];
		int count = 0;
		for (int i = 0; i < stylesCount; i++) {
			if (styles[i].style != null) {
				result[count++] = styles[i].style;
			}
		}
		if (count != result.length) {
			TextStyle[] newResult = new TextStyle[count];
			System.arraycopy(result, 0, newResult, 0, count);
			result = newResult;
		}
		return result;
	}

	/**
	 * Returns the tab list of the receiver.
	 *
	 * @return the tab list
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public int[] getTabs() {
		checkLayout();
		return tabs;
	}

	/**
	 * Gets the receiver's text, which will be an empty string if it has never
	 * been set.
	 *
	 * @return the receiver's text
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public String getText() {
		checkLayout();
		return text;
	}

	/**
	 * Returns the text direction of the receiver.
	 *
	 * @return the text direction value
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * @since 3.103
	 */
	public int getTextDirection() {
		checkLayout();
		return orientation;
	}

	/**
	 * Returns the width of the receiver.
	 *
	 * @return the width
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public int getWidth() {
		checkLayout();
		return wrapWidth;
	}

	/**
	 * Returns the receiver's wrap indent.
	 *
	 * @return the receiver's wrap indent
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @since 3.6
	 */
	public int getWrapIndent() {
		checkLayout();
		return wrapIndent;
	}

	void initClasses() {
		// String className = "SWTTextAttachmentCell";
		// if (OS.objc_lookUpClass(className) != 0)
		// return;
		//
		// textLayoutCallback2 = new Callback(getClass(), "textLayoutProc", 2);
		// long proc2 = textLayoutCallback2.getAddress();
		// long cellBaselineOffsetProc = OS.CALLBACK_cellBaselineOffset(proc2);
		// long cellSizeProc = OS.CALLBACK_NSTextAttachmentCell_cellSize(proc2);
		//
		// byte[] types = {'*', '\0'};
		// int size = C.PTR_SIZEOF, align = C.PTR_SIZEOF == 4 ? 2 : 3;
		// long cls = OS.objc_allocateClassPair(OS.class_NSCell, className, 0);
		// OS.class_addIvar(cls, SWT_OBJECT, size, (byte) align, types);
		// OS.class_addProtocol(cls, OS.protocol_NSTextAttachmentCell);
		// OS.class_addMethod(cls, OS.sel_cellSize, cellSizeProc, "@:");
		// OS.class_addMethod(cls, OS.sel_cellBaselineOffset,
		// cellBaselineOffsetProc, "@:");
		// if (OS.VERSION >= OS.VERSION(10, 11, 0)) {
		// long attachmentProc = OS
		// .CALLBACK_NSTextAttachmentCell_attachment(proc2);
		// OS.class_addMethod(cls, OS.sel_attachment, attachmentProc, "@:");
		// }
		// OS.objc_registerClassPair(cls);
	}

	/**
	 * Returns <code>true</code> if the text layout has been disposed, and
	 * <code>false</code> otherwise.
	 * <p>
	 * This method gets the dispose state for the text layout. When a text
	 * layout has been disposed, it is an error to invoke any other method
	 * (except {@link #dispose()}) using the text layout.
	 * </p>
	 *
	 * @return <code>true</code> when the text layout is disposed and
	 *         <code>false</code> otherwise
	 */

	@Override
	public boolean isDisposed() {
		return device == null;
	}

	/*
	 * Returns true if the underline style is supported natively
	 */
	boolean isUnderlineSupported(TextStyle style) {
		if (style != null && style.underline) {
			int uStyle = style.underlineStyle;
			return uStyle == SWT.UNDERLINE_SINGLE
					|| uStyle == SWT.UNDERLINE_DOUBLE
					|| uStyle == SWT.UNDERLINE_LINK
					|| uStyle == UNDERLINE_THICK;
		}
		return false;
	}

	/**
	 * Sets the text alignment for the receiver. The alignment controls how a
	 * line of text is positioned horizontally. The argument should be one of
	 * <code>SWT.LEFT</code>, <code>SWT.RIGHT</code> or <code>SWT.CENTER</code>.
	 * <p>
	 * The default alignment is <code>SWT.LEFT</code>. Note that the receiver's
	 * width must be set in order to use <code>SWT.RIGHT</code> or
	 * <code>SWT.CENTER</code> alignment.
	 * </p>
	 *
	 * @param alignment
	 *            the new alignment
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #setWidth(int)
	 */

	public void setAlignment(int alignment) {
		checkLayout();
		int mask = SWT.LEFT | SWT.CENTER | SWT.RIGHT;
		alignment &= mask;
		if (alignment == 0)
			return;
		if ((alignment & SWT.LEFT) != 0)
			alignment = SWT.LEFT;
		if ((alignment & SWT.RIGHT) != 0)
			alignment = SWT.RIGHT;
		if (this.alignment == alignment)
			return;

		freeRuns();
		this.alignment = alignment;
	}

	/**
	 * Sets the ascent of the receiver. The ascent is distance in points from
	 * the baseline to the top of the line and it is applied to all lines. The
	 * default value is <code>-1</code> which means that the ascent is
	 * calculated from the line fonts.
	 *
	 * @param ascent
	 *            the new ascent
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the ascent is less than
	 *                <code>-1</code></li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #setDescent(int)
	 * @see #getLineMetrics(int)
	 */

	public void setAscent(int ascent) {
		checkLayout();

		if (ascent < -1)
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		if (this.ascent == ascent)
			return;
		freeRuns();
		this.ascent = ascent;
	}

	/**
	 * Sets the descent of the receiver. The descent is distance in points from
	 * the baseline to the bottom of the line and it is applied to all lines.
	 * The default value is <code>-1</code> which means that the descent is
	 * calculated from the line fonts.
	 *
	 * @param descent
	 *            the new descent
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the descent is less than
	 *                <code>-1</code></li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #setAscent(int)
	 * @see #getLineMetrics(int)
	 */

	public void setDescent(int descent) {
		checkLayout();
		if (descent < -1)
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		if (this.descent == descent)
			return;
		freeRuns();
		this.descent = descent;
	}

	/**
	 * Forces line heights in receiver to obey provided value. This is useful
	 * with texts that contain glyphs from different scripts, such as mixing
	 * latin glyphs with hieroglyphs or emojis.
	 * <p>
	 * Text lines with different metrics will be forced to fit. This means
	 * painting text in such a way that its baseline is where specified by given
	 * 'metrics'. This can sometimes introduce small visual artifacs, such as
	 * taller lines overpainting or being clipped by content above and below.
	 * </p>
	 * The possible ways to set FontMetrics include:
	 * <ul>
	 * <li>Obtaining 'FontMetrics' via {@link GC#getFontMetrics}. Note that this
	 * will only obtain metrics for currently selected font and will not account
	 * for font fallbacks (for example, with a latin font selected, painting
	 * hieroglyphs usually involves a fallback font).</li>
	 * <li>Obtaining 'FontMetrics' via a temporary 'TextLayout'. This would
	 * involve setting a desired text sample to 'TextLayout', then measuring it
	 * with {@link TextLayout#getLineMetrics(int)}. This approach will also take
	 * fallback fonts into account.</li>
	 * </ul>
	 *
	 * NOTE: Does not currently support (as in, undefined behavior) multi-line
	 * layouts, including those caused by word wrapping. StyledText uses one
	 * TextLayout per line and is only affected by word wrap restriction.
	 *
	 * @since 3.125
	 */

	public void setFixedLineMetrics(FontMetrics metrics) {
		if (metrics == null) {
			fixedLineMetrics = null;
			return;
		}

		fixedLineMetrics = metrics;
		freeRuns();
	}

	/**
	 * Sets the default font which will be used by the receiver to draw and
	 * measure text. If the argument is null, then a default font appropriate
	 * for the platform will be used instead. Note that a text style can
	 * override the default font.
	 *
	 * @param font
	 *            the new font for the receiver, or null to indicate a default
	 *            font
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the font has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public void setFont(Font font) {
		checkLayout();

		if (Objects.equals(this.swtFont, font))
			return;

		this.swtFont = font;

		freeRuns();

		if (true)
			return;

		if (font == null)
			font = device.getSystemFont();

		if (font != null && font.isDisposed())
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);

		innerGC.setFont(font);
		FontData fontData = font.getFontData()[0];
		FontStyle style = FontStyle.NORMAL;
		boolean isBold = (fontData.getStyle() & SWT.BOLD) != 0;
		boolean isItalic = (fontData.getStyle() & SWT.ITALIC) != 0;
		if (isBold && isItalic) {
			style = FontStyle.BOLD_ITALIC;
		} else if (isBold) {
			style = FontStyle.BOLD;
		} else if (isItalic) {
			style = FontStyle.ITALIC;
		}
		this.font = new io.github.humbleui.skija.Font(
				Typeface.makeFromName(fontData.getName(), style));
		int fontSize = DPIUtil.autoScaleUp(fontData.getHeight());
		if (SWT.getPlatform().equals("win32")) {
			fontSize *= this.font.getSize()
					/ innerGC.getDevice().getSystemFont().getFontData()[0]
							.getHeight();
		}
		this.font.setSize(fontSize);
		this.font.setEdging(FontEdging.SUBPIXEL_ANTI_ALIAS);
		this.font.setSubpixel(true);

		freeRuns();
	}

	/**
	 * Sets the indent of the receiver. This indent is applied to the first line
	 * of each paragraph.
	 *
	 * @param indent
	 *            new indent
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #setWrapIndent(int)
	 *
	 * @since 3.2
	 */

	public void setIndent(int indent) {
		checkLayout();
		if (indent < 0)
			return;
		if (this.indent == indent)
			return;

		this.indent = indent;
		freeRuns();
	}

	/**
	 * Sets the wrap indent of the receiver. This indent is applied to all lines
	 * in the paragraph except the first line.
	 *
	 * @param wrapIndent
	 *            new wrap indent
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #setIndent(int)
	 *
	 * @since 3.6
	 */

	public void setWrapIndent(int wrapIndent) {
		checkLayout();
		if (wrapIndent < 0)
			return;
		if (this.wrapIndent == wrapIndent)
			return;

		this.wrapIndent = wrapIndent;
		freeRuns();
	}

	/**
	 * Sets the justification of the receiver. Note that the receiver's width
	 * must be set in order to use justification.
	 *
	 * @param justify
	 *            new justify
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @since 3.2
	 */

	public void setJustify(boolean justify) {
		checkLayout();
		if (justify == this.justify)
			return;

		this.justify = justify;
		freeRuns();
	}

	/**
	 * Sets the orientation of the receiver, which must be one of
	 * <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
	 *
	 * @param orientation
	 *            new orientation style
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public void setOrientation(int orientation) {
		checkLayout();
		int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		orientation &= mask;
		if (orientation == 0)
			return;
		if ((orientation & SWT.LEFT_TO_RIGHT) != 0)
			orientation = SWT.LEFT_TO_RIGHT;
		if (this.orientation == orientation)
			return;
		this.orientation = orientation;
		freeRuns();
	}

	/**
	 * Sets the offsets of the receiver's text segments. Text segments are used
	 * to override the default behavior of the bidirectional algorithm.
	 * Bidirectional reordering can happen within a text segment but not between
	 * two adjacent segments.
	 * <p>
	 * Each text segment is determined by two consecutive offsets in the
	 * <code>segments</code> arrays. The first element of the array should
	 * always be zero and the last one should always be equals to length of the
	 * text.
	 * </p>
	 * <p>
	 * When segments characters are set, the segments are the offsets where the
	 * characters are inserted in the text.
	 * </p>
	 *
	 * @param segments
	 *            the text segments offset
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #setSegmentsChars(char[])
	 */

	public void setSegments(int[] segments) {
		checkLayout();
		if (this.segments == null && segments == null)
			return;
		if (this.segments != null && segments != null) {
			if (this.segments.length == segments.length) {
				int i;
				for (i = 0; i < segments.length; i++) {
					if (this.segments[i] != segments[i])
						break;
				}
				if (i == segments.length)
					return;
			}
		}
		this.segments = segments;
		freeRuns();
	}

	/**
	 * Sets the characters to be used in the segments boundaries. The segments
	 * are set by calling <code>setSegments(int[])</code>. The application can
	 * use this API to insert Unicode Control Characters in the text to control
	 * the display of the text and bidi reordering. The characters are not
	 * accessible by any other API in <code>TextLayout</code>.
	 *
	 * @param segmentsChars
	 *            the segments characters
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #setSegments(int[])
	 *
	 * @since 3.6
	 */

	public void setSegmentsChars(char[] segmentsChars) {
		checkLayout();
		if (this.segmentsChars == null && segmentsChars == null)
			return;
		if (this.segmentsChars != null && segmentsChars != null) {
			if (this.segmentsChars.length == segmentsChars.length) {
				int i;
				for (i = 0; i < segmentsChars.length; i++) {
					if (this.segmentsChars[i] != segmentsChars[i])
						break;
				}
				if (i == segmentsChars.length)
					return;
			}
		}
		this.segmentsChars = segmentsChars;
		freeRuns();
	}

	/**
	 * Sets the line spacing of the receiver. The line spacing is the space left
	 * between lines.
	 *
	 * @param spacing
	 *            the new line spacing
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the spacing is
	 *                negative</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public void setSpacing(int spacing) {
		checkLayout();

		if (spacing < 0)
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		if (this.lineSpacingInPoints == spacing)
			return;
		this.lineSpacingInPoints = spacing;
		freeRuns();
	}

	/**
	 * Sets the vertical indent of the receiver. The vertical indent is the
	 * space left before the first line.
	 *
	 * @param verticalIndent
	 *            the new vertical indent
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the vertical indent is
	 *                negative</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * @since 3.109
	 */

	public void setVerticalIndent(int verticalIndent) {
		checkLayout();
		if (verticalIndent < 0)
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		if (this.verticalIndentInPoints == verticalIndent)
			return;
		this.verticalIndentInPoints = verticalIndent;
		freeRuns();
	}

	/**
	 * Sets the style of the receiver for the specified range. Styles previously
	 * set for that range will be overwritten. The start and end offsets are
	 * inclusive and will be clamped if out of range.
	 *
	 * @param style
	 *            the style
	 * @param start
	 *            the start offset
	 * @param end
	 *            the end offset
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public void setStyle(TextStyle style, int start, int end) {

		checkLayout();
		int length = text.length();
		if (length == 0)
			return;
		if (start > end)
			return;
		start = Math.min(Math.max(0, start), length - 1);
		end = Math.min(Math.max(0, end), length - 1);
		int low = -1;
		int high = stylesCount;
		while (high - low > 1) {
			int index = (high + low) / 2;
			if (styles[index + 1].start > start) {
				high = index;
			} else {
				low = index;
			}
		}
		if (0 <= high && high < stylesCount) {
			StyleItem item = styles[high];
			if (item.start == start && styles[high + 1].start - 1 == end) {
				if (style == null) {
					if (item.style == null)
						return;
				} else {
					if (style.equals(item.style))
						return;
				}
			}
		}
		freeRuns();
		int modifyStart = high;
		int modifyEnd = modifyStart;
		while (modifyEnd < stylesCount) {
			if (styles[modifyEnd + 1].start > end)
				break;
			modifyEnd++;
		}
		if (modifyStart == modifyEnd) {
			int styleStart = styles[modifyStart].start;
			int styleEnd = styles[modifyEnd + 1].start - 1;
			if (styleStart == start && styleEnd == end) {
				styles[modifyStart].style = style;
				return;
			}
			if (styleStart != start && styleEnd != end) {
				int newLength = stylesCount + 2;
				if (newLength > styles.length) {
					int newSize = Math.min(newLength + 1024,
							Math.max(64, newLength * 2));
					StyleItem[] newStyles = new StyleItem[newSize];
					System.arraycopy(styles, 0, newStyles, 0, stylesCount);
					styles = newStyles;
				}
				System.arraycopy(styles, modifyEnd + 1, styles, modifyEnd + 3,
						stylesCount - modifyEnd - 1);
				StyleItem item = new StyleItem();
				item.start = start;
				item.style = style;
				styles[modifyStart + 1] = item;
				item = new StyleItem();
				item.start = end + 1;
				item.style = styles[modifyStart].style;
				styles[modifyStart + 2] = item;
				stylesCount = newLength;
				return;
			}
		}
		if (start == styles[modifyStart].start)
			modifyStart--;
		if (end == styles[modifyEnd + 1].start - 1)
			modifyEnd++;
		int newLength = stylesCount + 1 - (modifyEnd - modifyStart - 1);
		if (newLength > styles.length) {
			int newSize = Math.min(newLength + 1024,
					Math.max(64, newLength * 2));
			StyleItem[] newStyles = new StyleItem[newSize];
			System.arraycopy(styles, 0, newStyles, 0, stylesCount);
			styles = newStyles;
		}
		System.arraycopy(styles, modifyEnd, styles, modifyStart + 2,
				stylesCount - modifyEnd);
		StyleItem item = new StyleItem();
		item.start = start;
		item.style = style;
		styles[modifyStart + 1] = item;
		styles[modifyStart + 2].start = end + 1;
		stylesCount = newLength;
	}

	/**
	 * Sets the receiver's tab list. Each value in the tab list specifies the
	 * space in points from the origin of the text layout to the respective tab
	 * stop. The last tab stop width is repeated continuously.
	 *
	 * @param tabs
	 *            the new tab list
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public void setTabs(int[] tabs) {
		checkLayout();

		if (this.tabs == null && tabs == null)
			return;
		if (Arrays.equals(this.tabs, tabs))
			return;
		this.tabs = tabs;
		freeRuns();
	}

	/**
	 * Sets the receiver's text.
	 * <p>
	 * Note: Setting the text also clears all the styles. This method returns
	 * without doing anything if the new text is the same as the current text.
	 * </p>
	 *
	 * @param text
	 *            the new text
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the text is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */

	public void setText(String text) {
		checkLayout();

		if (text == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (text.equals(this.text))
			return;

		freeRuns();
		this.text = text;
		styles = new StyleItem[2];
		styles[0] = new StyleItem();
		styles[1] = new StyleItem();
		styles[1].start = text.length();
		stylesCount = 2;
	}

	/**
	 * Sets the text direction of the receiver, which must be one of
	 * <code>SWT.LEFT_TO_RIGHT</code>, <code>SWT.RIGHT_TO_LEFT</code> or
	 * <code>SWT.AUTO_TEXT_DIRECTION</code>.
	 *
	 * <p>
	 * <b>Warning</b>: This API is currently only implemented on Windows. It
	 * doesn't set the base text direction on GTK and Cocoa.
	 * </p>
	 *
	 * @param textDirection
	 *            the new text direction
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * @since 3.103
	 */

	public void setTextDirection(int textDirection) {
		checkLayout();

		if (this.textDirection == textDirection)
			return;

		this.textDirection = textDirection;
		freeRuns();
	}

	/**
	 * Sets the line width of the receiver, which determines how text should be
	 * wrapped and aligned. The default value is <code>-1</code> which means
	 * wrapping is disabled.
	 *
	 * @param width
	 *            the new width
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the width is
	 *                <code>0</code> or less than <code>-1</code></li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @see #setAlignment(int)
	 */

	public void setWidth(int width) {
		checkLayout();
		if (width < -1 || width == 0)
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		if (this.wrapWidth == width)
			return;

		freeRuns();

		this.wrapWidth = width;

	}

	/**
	 * Returns a string containing a concise, human-readable description of the
	 * receiver.
	 *
	 * @return a string representation of the receiver
	 */

	@Override
	public String toString() {
		if (isDisposed())
			return "TextLayout {*DISPOSED*}";
		return "TextLayout {" + text + "}";
	}

	/*
	 * Translate a client offset to an internal offset
	 */
	int translateOffset(int offset) {
		int length = text.length();
		if (length == 0)
			return offset;
		if (segments == null)
			return offset;
		int nSegments = segments.length;
		if (nSegments == 0)
			return offset;
		if (segmentsChars == null) {
			if (nSegments == 1)
				return offset;
			if (nSegments == 2) {
				if (segments[0] == 0 && segments[1] == length)
					return offset;
			}
		}
		for (int i = 0; i < nSegments && offset - i >= segments[i]; i++) {
			offset++;
		}
		return offset;
	}

	/*
	 * Translate an internal offset to a client offset
	 */
	int untranslateOffset(int offset) {
		int length = text.length();
		if (length == 0)
			return offset;
		if (segments == null)
			return offset;
		int nSegments = segments.length;
		if (nSegments == 0)
			return offset;
		if (segmentsChars == null) {
			if (nSegments == 1)
				return offset;
			if (nSegments == 2) {
				if (segments[0] == 0 && segments[1] == length)
					return offset;
			}
		}
		for (int i = 0; i < nSegments && offset > segments[i]; i++) {
			offset--;
		}
		return offset;
	}

	/**
	 * Sets Default Tab Width in terms if number of space characters.
	 *
	 * @param tabLength
	 *            in number of characters
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the tabLength is less than
	 *                <code>0</code></li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 *
	 *              DO NOT USE This might be removed in 4.8
	 * @since 3.107
	 */

	public void setDefaultTabWidth(int tabLength) {

	}

	public int getLineIndent(int lineIndex) {
		return 0;
	}

}
