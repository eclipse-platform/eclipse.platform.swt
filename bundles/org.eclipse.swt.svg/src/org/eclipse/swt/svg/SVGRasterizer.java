package org.eclipse.swt.svg;

import static java.awt.RenderingHints.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import org.eclipse.swt.graphics.ISVGRasterizer;
import org.eclipse.swt.graphics.SVGRasterizerRegistry;
import org.eclipse.swt.graphics.SVGUtil;

import com.github.weisj.jsvg.*;
import com.github.weisj.jsvg.geometry.size.*;
import com.github.weisj.jsvg.parser.*;

/**
 * A rasterizer implementation for converting SVG data into rasterized images.
 * This class implements the {@code ISVGRasterizer} interface.
 * 
 * @since 1.0.0
 */
public class SVGRasterizer implements ISVGRasterizer {
	
	private SVGLoader svgLoader;

	/**
	 * Initializes the SVG rasterizer by registering an instance of this rasterizer
	 * with the {@link SVGRasterizerRegistry}.
	 */
	public static void intializeSVGRasterizer() {
		SVGRasterizerRegistry.register(new SVGRasterizer());
	}

	private final static Map<Key, Object> RENDERING_HINTS = Map.of(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON, //
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
	public BufferedImage rasterizeSVG(byte[] bytes, float scalingFactor) throws IOException {
		if(svgLoader == null) {
			svgLoader = new SVGLoader();
		}
		SVGDocument svgDocument = null;
		if (SVGUtil.isSVGFile(bytes)) {
			try (InputStream stream = new ByteArrayInputStream(bytes)) {
				svgDocument = svgLoader.load(stream, null, LoaderContext.createDefault());
			}
			if (svgDocument != null) {
				FloatSize size = svgDocument.size();
				double originalWidth = size.getWidth();
				double originalHeight = size.getHeight();
				int scaledWidth = (int) Math.round(originalWidth * scalingFactor);
				int scaledHeight = (int) Math.round(originalHeight * scalingFactor);
				BufferedImage image = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = image.createGraphics();
				g.setRenderingHints(RENDERING_HINTS);
				g.scale(scalingFactor, scalingFactor);
				svgDocument.render(null, g);
				g.dispose();
				return image;
			}
		}
		return null;
	}
}
