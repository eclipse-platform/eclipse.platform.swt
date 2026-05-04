/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal.skia;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageVersion;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.canvasext.FontProperties;
import org.eclipse.swt.internal.canvasext.IDpiScaler;
import org.eclipse.swt.internal.graphics.SkiaTextDrawing;
import org.eclipse.swt.internal.skia.cache.ImageKey;
import org.eclipse.swt.internal.skia.cache.ImageTextKey;
import org.eclipse.swt.internal.skia.cache.SplitsTextCache;
import org.eclipse.swt.widgets.Canvas;

import io.github.humbleui.skija.FontEdging;
import io.github.humbleui.skija.FontHinting;
import io.github.humbleui.skija.FontMgr;
import io.github.humbleui.skija.FontSlant;
import io.github.humbleui.skija.FontStyle;
import io.github.humbleui.skija.Typeface;

public class SkiaResources {
	private static final String NO_IMAGE_CACHE = "org.eclipse.swt.internal.skia.noImageCache";
	private static boolean USE_NO_IMAGE_CACHE = true;

	/** Maximum number of entries kept in the text-image LRU cache. */
	private static final int TEXT_IMAGE_CACHE_MAX = 512;

	/** Maximum number of entries kept in the SWT→Skija image LRU cache. */
	private static final int IMAGE_CACHE_MAX = 256;

	/**
	 * LRU cache that automatically closes the evicted Skija image when the cache
	 * exceeds its capacity. Must only be accessed from the SWT UI thread.
	 */
	private static final class LruImageCache<K> extends LinkedHashMap<K, io.github.humbleui.skija.Image> {
		private static final long serialVersionUID = -4958211475619287841L;
		private final int maxSize;

		LruImageCache(int maxSize) {
			super(maxSize + 1, 0.75f, /* accessOrder= */ true);
			this.maxSize = maxSize;
		}

		@Override
		protected boolean removeEldestEntry(Map.Entry<K, io.github.humbleui.skija.Image> eldest) {
			if (size() > maxSize) {
				// Close the native Skija image before evicting it from the cache.
				final io.github.humbleui.skija.Image image = eldest.getValue();
				if (image != null && !image.isClosed()) {
					image.close();
				}
				return true;
			}
			return false;
		}
	}

	private Color background;
	private Color foreground;
	private final Canvas canvas;
	private Font swtFont;
	private io.github.humbleui.skija.Font skiaFont;

	// cache access only from UI thread, so no need for concurrent map
	private final Map<String, String> fontNameMapping = new HashMap<>();
	private final Set<String> unknownFonts = new HashSet<>();
	private final Map<FontProperties, io.github.humbleui.skija.Font> fontCache = new HashMap<>();
	private final LruImageCache<ImageKey> imageCache = new LruImageCache<>(IMAGE_CACHE_MAX);
	private final LruImageCache<ImageTextKey> textImageCache = new LruImageCache<>(TEXT_IMAGE_CACHE_MAX);
	private final Map<SplitsTextCache, String[]> cachedTextSplits = new HashMap<>();

	// ---------------------------------------------------------------------------------

	private final ISkiaCanvasExtension skiaExtension;

	public SkiaResources(Canvas canvas, ISkiaCanvasExtension skiaExtension) {

		USE_NO_IMAGE_CACHE = System.getProperty(NO_IMAGE_CACHE) == null ? false
				: Boolean.parseBoolean(System.getProperty(NO_IMAGE_CACHE));

		this.canvas = canvas;
		this.skiaExtension = skiaExtension;
		this.canvas.addListener(SWT.Dispose, e -> resetResources());
		this.canvas.addListener(SWT.ZoomChanged, e -> resetResources());
	}

	public void setBackground(Color color) {

		if (color == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.background = color;
	}

	public void setForeground(Color color) {
		if (color == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.foreground = color;
	}

	public Color getForeground() {
		if (foreground != null && !foreground.isDisposed()) {
			return foreground;
		}
		return canvas.getForeground();

	}

	public Color getBackground() {

		if (background != null && !background.isDisposed()) {
			return background;
		}

		return canvas.getBackground();
	}

	public void setFont(Font font) {
		if (font != null) {
			if (font.isDisposed()) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
		} else {
			font = getDefaultFont();
		}
		this.swtFont = font;
		this.skiaFont = getSkijaFont(font);

	}

	private io.github.humbleui.skija.Font getSkijaFont(org.eclipse.swt.graphics.Font font) {
		final FontProperties props = FontProperties.getFontProperties(font);
		var cachedFont = fontCache.get(props);
		if (cachedFont != null && cachedFont.isClosed()) {
			fontCache.remove(props);
			cachedFont = null;
		}

		io.github.humbleui.skija.Font f = null;

		if (cachedFont == null) {
			f = createSkijaFont(props);
			fontCache.put(props, f);
			return f;
		}

		return fontCache.get(props);
	}

	private io.github.humbleui.skija.Font createSkijaFont(FontProperties props) {

		FontSlant slant = FontSlant.UPRIGHT;
		if (props.lfItalic != 0) {
			slant = FontSlant.ITALIC;
		}

		final FontStyle style = new FontStyle(props.lfWeight, 5, slant);
		final io.github.humbleui.skija.Font skijaFont = new io.github.humbleui.skija.Font(
				extractTypeface(props, style));

		if (props.lfWidth != 0) {
			final float stretch = (float) ((props.lfWidth / 10.0) + 0.5);
			skijaFont.setScaleX(stretch);
		}

		int fontSize = (props.lfHeight);
		final DpiScalerUtil util = new DpiScalerUtil(getScaler());
		fontSize = util.getZoomedFontSize(fontSize);

		skijaFont.setSize(fontSize);
		skijaFont.setEdging(FontEdging.SUBPIXEL_ANTI_ALIAS);
		skijaFont.setSubpixel(true);
		skijaFont.setHinting(FontHinting.NORMAL);
		skijaFont.setAutoHintingForced(true);

		return skijaFont;
	}

	private Typeface extractTypeface(FontProperties props, FontStyle style) {

		final FontMgr fm = FontMgr.getDefault();
		var name = props.name;
		name = name.trim();

		if (fontNameMapping.containsKey(name)) {
			return fm.matchFamilyStyle(fontNameMapping.get(name), style);
		}

		if (unknownFonts.contains(name)) {
			return fm.matchFamilyStyle(null, style);
		}

		var bestMatch = findBestFit(name);

		if (bestMatch == null) {
			bestMatch = findBestFit(name.replaceAll("[^\\p{L}\\p{N}]+", " ")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		if (bestMatch != null) {
			fontNameMapping.put(name, bestMatch);
			return fm.matchFamilyStyle(bestMatch, style);
		}

		if (SWT.getPlatform().equals("win32")) { //$NON-NLS-1$
			// arabic fonts are no longer supported on windows. Also windows falls back to
			// arial
			if (name.toLowerCase().startsWith("arabic ") || name.toLowerCase().equals("arabic")) { //$NON-NLS-1$
				fontNameMapping.put(name, "Arial"); //$NON-NLS-1$
				return fm.matchFamilyStyle("Arial", style); //$NON-NLS-1$
			}
		}
		// No matching font found, use system default
		unknownFonts.add(name);
		return fm.matchFamilyStyle(null, style);

	}

	private static String findBestFit(String name) {
		final String[] parts = name.split(" "); //$NON-NLS-1$

		for (int i = 0; i < parts.length; i++) {
			parts[i] = parts[i].trim();
			parts[i] = parts[i].toLowerCase();
		}

		final FontMgr fm = FontMgr.getDefault();
		final var count = fm.getFamiliesCount();

		String bestMatch = null;
		int bestMatchScore = 0;

		for (int i = 0; i < count; i++) {

			final var f = fm.getFamilyName(i);
			final var fLower = f.toLowerCase();

			int score = 0;
			for (final String part : parts) {
				if (fLower.contains(part)) {
					score++;
				}
			}

			if (score > bestMatchScore) {
				bestMatchScore = score;
				bestMatch = f;
			}
		}
		return bestMatch;
	}

	private org.eclipse.swt.graphics.Font getDefaultFont() {
		org.eclipse.swt.graphics.Font originalFont = canvas.getFont();

		if (originalFont == null || originalFont.isDisposed()) {
			originalFont = canvas.getDisplay().getSystemFont();
		}
		return originalFont;
	}

	public io.github.humbleui.skija.Font getSkiaFont() {
		if (skiaFont == null) {
			setFont(null);
		}
		return skiaFont;
	}

	public IDpiScaler getScaler() {
		return skiaExtension.getScaler();
	}

	private void resetResources() {

		if (this.skiaFont != null && !this.skiaFont.isClosed()) {
			this.skiaFont.close();
		}
		skiaFont = null;

		fontCache.values().forEach(f -> {
			if (!f.isClosed()) {
				f.close();
			}
		});
		fontCache.clear();

		imageCache.values().forEach(i -> {
			if (!i.isClosed()) {
				i.close();
			}
		});

		imageCache.clear();

		textImageCache.values().forEach(i -> {
			if (!i.isClosed()) {
				i.close();
			}
		});

		textImageCache.clear();
		cachedTextSplits.clear();

		fontNameMapping.clear();
		unknownFonts.clear();

	}

	public Font getFont() {

		if (swtFont != null && !swtFont.isDisposed()) {
			return swtFont;
		}
		swtFont = getDefaultFont();

		return swtFont;
	}

	public void resetBaseColors() {
		foreground = null;
		background = null;

	}

	public void cacheImage(Image swtImage, int zoom, io.github.humbleui.skija.Image skijaImage) {
		if (!USE_NO_IMAGE_CACHE) {
			final var key = new ImageKey(swtImage, ImageVersion.getVersion(swtImage), zoom);
			final var old = imageCache.get(key);
			if (old != null && !old.isClosed()) {
				old.close();
			}
			this.imageCache.put(key, skijaImage);
		}
	}

	public io.github.humbleui.skija.Image getCachedImage(Image swtImage, int zoom) {
		return this.imageCache.get(new ImageKey(swtImage, ImageVersion.getVersion(swtImage), zoom));
	}

	public void cacheTextImage(String text, FontProperties fontProperties, boolean transparent, int background,
			int foreground, boolean antiAlias, io.github.humbleui.skija.Image skijaImage) {
		if (!USE_NO_IMAGE_CACHE) {
			final var key = new ImageTextKey(text, fontProperties, transparent, background, foreground, antiAlias);
			final var old = textImageCache.get(key);
			if (old != null && !old.isClosed()) {
				old.close();
			}
			this.textImageCache.put(key, skijaImage);
		}
	}

	public io.github.humbleui.skija.Image getTextImage(String text, FontProperties fontProperties, boolean transparent,
			int background, int foreground, boolean antialias) {
		return this.textImageCache.get(new ImageTextKey(text, fontProperties, transparent, background, foreground, antialias));
	}

	private static String[] splitString(String text) {
		return text.split("\r\n|\n|\r"); //$NON-NLS-1$
	}

	public String[] getTextSplits(String inputText, int flags) {

		final boolean replaceAmpersand = (flags & SWT.DRAW_MNEMONIC) != 0;
		final boolean delimiter = (flags & SWT.DRAW_DELIMITER) != 0;
		final boolean tabulatorExpansion = (flags & SWT.DRAW_TAB) != 0;

		String[] splits = null;

		if (SkiaTextDrawing.USE_TEXT_CACHE) {
			splits = cachedTextSplits
					.get(new SplitsTextCache(inputText, replaceAmpersand, delimiter, tabulatorExpansion));
		}

		String workInputText = inputText;


		if (splits == null) {
			if (tabulatorExpansion) {
				workInputText = expandTabs(workInputText, 0);
			} else {
				workInputText = workInputText.replaceAll("\\t", ""); //$NON-NLS-1$ //$NON-NLS-2$
			}

			if (replaceAmpersand) {
				workInputText = replaceMnemonics(workInputText);
			}

			// replace form feed characters with "\u240C" this is the unicode standard sign
			// for form feed.
			// unfortunately skia does not even render these form feed characters...
			workInputText = workInputText.replace("\f", "\u240C"); //$NON-NLS-1$//$NON-NLS-2$

			if (delimiter) {
				splits = splitString(workInputText);
			} else {
				splits = new String[] { removeDelimiter(workInputText) };
			}
		}

		if (SkiaTextDrawing.USE_TEXT_CACHE) {
			cachedTextSplits.put(new SplitsTextCache(inputText, replaceAmpersand, delimiter, tabulatorExpansion),
					splits);
		}
		return splits;
	}

	/**
	 * Expands tab characters (\t) in the text to position-dependent spaces, so that
	 * the next character aligns to the next tab stop (every 8 average character
	 * widths by default). The expansion is based on the current x position and the
	 * average character width of the font.
	 *
	 * @param text   The input text containing tab characters
	 * @param startX The starting x position in pixels (used to calculate tab
	 *               alignment)
	 * @return The text with tabs expanded to spaces, aligned to the next tab stop
	 */
	private String expandTabs(String text, int startX) {
		final StringBuilder result = new StringBuilder();
		int currentX = 0;
		// Measure space width exactly once — it is constant for the current font.
		final int spaceWidth = textExtent(" ").x; //$NON-NLS-1$
		final float avgCharWidthF = getSkiaFont().getMetrics()._avgCharWidth;
		int avgCharWidth = (int) avgCharWidthF;
		if (avgCharWidth <= 0) {
			avgCharWidth = spaceWidth > 0 ? spaceWidth : 1;
		}
		final int tabSpacingPx = 8 * avgCharWidth;
		for (int i = 0; i < text.length(); i++) {
			final char ch = text.charAt(i);
			if (ch == '\t') {
				final int offsetInTab = tabSpacingPx > 0 ? (currentX - startX) % tabSpacingPx : 0;
				final int nextTabX = currentX + (tabSpacingPx - offsetInTab);
				while (currentX < nextTabX) {
					result.append(' ');
					// reuse the already-measured spaceWidth instead of calling textExtent again
					currentX += spaceWidth;
				}
			} else {
				// measureTextWidth avoids creating an intermediate String object per character
				final int charWidth = (int) getSkiaFont().measureTextWidth(String.valueOf(ch));
				result.append(ch);
				currentX += charWidth;
			}
		}
		return result.toString();
	}

	public Point textExtent(String text, int flags) {

		final float height = getSkiaFont().getMetrics().getHeight();
		final float width = getSkiaFont().measureTextWidth(replaceMnemonics(text));
		return new Point((int) width, (int) height);
	}

	public Point textExtent(String string) {
		return textExtent(string, SWT.NONE);
	}

	private static String removeDelimiter(String inputText) {
		return inputText.replaceAll("\r\n|\r|\n", ""); //$NON-NLS-1$//$NON-NLS-2$
	}

	public static String replaceMnemonics(String text) {
		final int mnemonicIndex = text.lastIndexOf('&');
		if (mnemonicIndex != -1) {
			text = text.replaceAll("&", ""); //$NON-NLS-1$ //$NON-NLS-2$
			// TODO Underline the mnemonic key
			// it seems this also does not work in windows with a simple snippet.
		}
		return text;
	}
}