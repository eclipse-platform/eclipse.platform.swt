/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Michael Bangas (Vector Informatik GmbH) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.svg;

import static java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_COLOR_RENDERING;
import static java.awt.RenderingHints.KEY_DITHERING;
import static java.awt.RenderingHints.KEY_FRACTIONALMETRICS;
import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.KEY_RENDERING;
import static java.awt.RenderingHints.KEY_STROKE_CONTROL;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY;
import static java.awt.RenderingHints.VALUE_DITHER_DISABLE;
import static java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC;
import static java.awt.RenderingHints.VALUE_RENDER_QUALITY;
import static java.awt.RenderingHints.VALUE_STROKE_PURE;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;

import java.awt.Graphics2D;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.internal.image.SVGRasterizer;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.geometry.size.FloatSize;
import com.github.weisj.jsvg.parser.LoaderContext;
import com.github.weisj.jsvg.parser.SVGLoader;

/**
 * A rasterizer implementation for converting SVG data into rasterized images.
 * This class uses the third party library JSVG for the raterization of SVG
 * images.
 */
public class JSVGRasterizer implements SVGRasterizer {

	private static final SVGLoader SVG_LOADER = new SVGLoader();

	private final static Map<Key, Object> RENDERING_HINTS = Map.of( //
			KEY_ANTIALIASING, VALUE_ANTIALIAS_ON, //
			KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY, //
			KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_QUALITY, //
			KEY_DITHERING, VALUE_DITHER_DISABLE, //
			KEY_FRACTIONALMETRICS, VALUE_FRACTIONALMETRICS_ON, //
			KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC, //
			KEY_RENDERING, VALUE_RENDER_QUALITY, //
			KEY_STROKE_CONTROL, VALUE_STROKE_PURE, //
			KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON //
	);

	@Override
	public ImageData rasterizeSVG(InputStream inputStream, int zoom) throws IOException {
		SVGDocument svgDocument = loadAndValidateSVG(inputStream);
		BufferedImage rasterizedImage = renderSVG(svgDocument, zoom);
		return convertToSWTImageData(rasterizedImage);
	}
	
	@Override
	public ImageData rasterizeSVG(InputStream inputStream, int width, int height) throws IOException {
		SVGDocument svgDocument = loadAndValidateSVG(inputStream);
		BufferedImage rasterizedImage = renderSVG(svgDocument, width, height);
		return convertToSWTImageData(rasterizedImage);
	}
	
	private SVGDocument loadAndValidateSVG(InputStream inputStream) throws IOException {
		SVGDocument svgDocument = SVG_LOADER.load(inputStream, null, LoaderContext.createDefault());
		if (svgDocument == null) {
			SWT.error(SWT.ERROR_INVALID_IMAGE);
		}
		return svgDocument;
	}

	private BufferedImage renderSVG(SVGDocument svgDocument, int zoom) {
		FloatSize sourceImageSize = svgDocument.size();
		float scalingFactor = zoom / 100.0f;
		int targetImageWidth = calculateTargetWidth(scalingFactor, sourceImageSize);
		int targetImageHeight = calculateTargetHeight(scalingFactor, sourceImageSize);
		return renderSVG(svgDocument, targetImageWidth, targetImageHeight);
	}
	
	private BufferedImage renderSVG(SVGDocument svgDocument, int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		float widthScalingFactor = width / svgDocument.size().width;
		float heightScalingFactor = height / svgDocument.size().height;
		Graphics2D g = configureRenderingOptions(widthScalingFactor, heightScalingFactor, image);
		svgDocument.render(null, g);
		g.dispose();
		return image;
	}

	private int calculateTargetWidth(float scalingFactor, FloatSize sourceImageSize) {
		double sourceImageWidth = sourceImageSize.getWidth();
		return (int) Math.round(sourceImageWidth * scalingFactor);
	}

	private int calculateTargetHeight(float scalingFactor, FloatSize sourceImageSize) {
		double sourceImageHeight = sourceImageSize.getHeight();
		return (int) Math.round(sourceImageHeight * scalingFactor);
	}

	private Graphics2D configureRenderingOptions(float widthScalingFactor, float heightScalingFactor, BufferedImage image) {
		Graphics2D g = image.createGraphics();
		g.setRenderingHints(RENDERING_HINTS);
		g.scale(widthScalingFactor, heightScalingFactor);
		return g;
	}

	private ImageData convertToSWTImageData(BufferedImage rasterizedImage) {
		int width = rasterizedImage.getWidth();
		int height = rasterizedImage.getHeight();
		int[] pixels = ((DataBufferInt) rasterizedImage.getRaster().getDataBuffer()).getData();
		PaletteData paletteData = new PaletteData(0xFF0000, 0x00FF00, 0x0000FF);
		ImageData imageData = new ImageData(width, height, 24, paletteData);
		int index = 0;
		for (int y = 0; y < imageData.height; y++) {
			for (int x = 0; x < imageData.width; x++) {
				int alpha = (pixels[index] >> 24) & 0xFF;
				imageData.setAlpha(x, y, alpha);
				imageData.setPixel(x, y, pixels[index++] & 0x00FFFFFF);
			}
		}
		return imageData;
	}
}
