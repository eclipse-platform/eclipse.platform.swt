/*******************************************************************************
 * Copyright (c) 2025 Vector Informatik GmbH and others.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Michael Bangas (Vector Informatik GmbH) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.svg;

import static java.awt.RenderingHints.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.swt.graphics.SVGRasterizer;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.github.weisj.jsvg.*;
import com.github.weisj.jsvg.geometry.size.*;
import com.github.weisj.jsvg.parser.*;

/**
 * A rasterizer implementation for converting SVG data into rasterized images.
 * This class implements the {@code ISVGRasterizer} interface.
 * 
 * @since 1.0.0
 */
public class JSVGRasterizer implements SVGRasterizer {
	
	private SVGLoader svgLoader;

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
	public ImageData rasterizeSVG(InputStream stream, float scalingFactor) throws IOException {
		if (stream == null) {
	        throw new IllegalArgumentException("InputStream cannot be null");
	    }
		if(svgLoader == null) {
			svgLoader = new SVGLoader();
		}
		return rasterize(stream, scalingFactor);
	}
	
	@Override
	public ImageData rasterizeDisabledSVG(InputStream stream, float scalingFactor) throws IOException {
		if(svgLoader == null) {
			svgLoader = new SVGLoader();
		}
		InputStream disabledStream = applyDisabledLook(stream);
		return rasterize(disabledStream, scalingFactor);
	}
	
	@Override
	public ImageData rasterizeGraySVG(InputStream stream, float scalingFactor) throws IOException {
		if(svgLoader == null) {
			svgLoader = new SVGLoader();
		}
		InputStream disabledStream = applyGrayLook(stream);
		return rasterize(disabledStream, scalingFactor);
	}
	
	private ImageData rasterize(InputStream stream, float scalingFactor) throws IOException {
		SVGDocument svgDocument = null;
		stream.mark(Integer.MAX_VALUE);
		InputStream nonClosingStream = new FilterInputStream(stream) {
		        @Override
		        public void close() throws IOException {
		            // Do nothing to prevent closing the underlying stream
		        }
		    };
        svgDocument = svgLoader.load(nonClosingStream, null, LoaderContext.createDefault());
        stream.reset();
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
			return convertToSWT(image);
		}
		return null;
	}
	
	private static InputStream applyDisabledLook(InputStream svgInputStream) throws IOException {
		Document svgDocument = parseSVG(svgInputStream);
		addDisabledFilter(svgDocument);
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			writeSVG(svgDocument, outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		}
	}
	
	private static InputStream applyGrayLook(InputStream svgInputStream) throws IOException {
		Document svgDocument = parseSVG(svgInputStream);
		addGrayFilter(svgDocument);
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			writeSVG(svgDocument, outputStream);
			return new ByteArrayInputStream(outputStream.toByteArray());
		}
	}

	private static Document parseSVG(InputStream inputStream) throws IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			return builder.parse(inputStream);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			throw new IOException(e.getMessage());
		}
	}

	private static void addDisabledFilter(Document document) {
		addFilter(document, 0.64f, 0.4f);
	}
	
	private static void addGrayFilter(Document document) {
		addFilter(document, 0.64f, 0.1f);
	}

	private static void addFilter(Document document, float slope, float intercept) {
		Element defs = (Element) document.getElementsByTagName("defs").item(0);
		if (defs == null) {
			defs = document.createElement("defs");
			document.getDocumentElement().appendChild(defs);
		}

		Element filter = document.createElement("filter");
		filter.setAttribute("id", "customizedLook");

		Element colorMatrix = document.createElement("feColorMatrix");
		colorMatrix.setAttribute("type", "saturate");
		colorMatrix.setAttribute("values", "0");
		filter.appendChild(colorMatrix);

		Element componentTransfer = document.createElement("feComponentTransfer");
		for (String channel : new String[] { "R", "G", "B" }) {
			Element func = document.createElement("feFunc" + channel);
			func.setAttribute("type", "linear");
			func.setAttribute("slope", Float.toString(slope));
			func.setAttribute("intercept", Float.toString(intercept));
			componentTransfer.appendChild(func);
		}
		filter.appendChild(componentTransfer);
		defs.appendChild(filter);
		document.getDocumentElement().setAttribute("filter", "url(#customizedLook)");
	}

	private static void writeSVG(Document document, OutputStream outputStream) throws IOException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.transform(new DOMSource(document), new StreamResult(outputStream));
		} catch (TransformerException e) {
			throw new IOException(e.getMessage());
		}
	}
	
	private ImageData convertToSWT(BufferedImage bufferedImage) {
	    if (bufferedImage.getColorModel() instanceof DirectColorModel) {
	        DirectColorModel colorModel = (DirectColorModel)bufferedImage.getColorModel();
	        PaletteData palette = new PaletteData(
	                colorModel.getRedMask(),
	                colorModel.getGreenMask(),
	                colorModel.getBlueMask());
	        ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(),
	                colorModel.getPixelSize(), palette);
	        for (int y = 0; y < data.height; y++) {
	            for (int x = 0; x < data.width; x++) {
	                int rgb = bufferedImage.getRGB(x, y);
	                int pixel = palette.getPixel(new RGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF));
	                data.setPixel(x, y, pixel);
	                if (colorModel.hasAlpha()) {
	                    data.setAlpha(x, y, (rgb >> 24) & 0xFF);
	                }
	            }
	        }
	        return data;
	    }
	    else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
	        IndexColorModel colorModel = (IndexColorModel)bufferedImage.getColorModel();
	        int size = colorModel.getMapSize();
	        byte[] reds = new byte[size];
	        byte[] greens = new byte[size];
	        byte[] blues = new byte[size];
	        colorModel.getReds(reds);
	        colorModel.getGreens(greens);
	        colorModel.getBlues(blues);
	        RGB[] rgbs = new RGB[size];
	        for (int i = 0; i < rgbs.length; i++) {
	            rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
	        }
	        PaletteData palette = new PaletteData(rgbs);
	        ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(),
	                colorModel.getPixelSize(), palette);
	        data.transparentPixel = colorModel.getTransparentPixel();
	        WritableRaster raster = bufferedImage.getRaster();
	        int[] pixelArray = new int[1];
	        for (int y = 0; y < data.height; y++) {
	            for (int x = 0; x < data.width; x++) {
	                raster.getPixel(x, y, pixelArray);
	                data.setPixel(x, y, pixelArray[0]);
	            }
	        }
	        return data;
	    }
	    else if (bufferedImage.getColorModel() instanceof ComponentColorModel) {
	        ComponentColorModel colorModel = (ComponentColorModel)bufferedImage.getColorModel();
	        //ASSUMES: 3 BYTE BGR IMAGE TYPE
	        PaletteData palette = new PaletteData(0x0000FF, 0x00FF00,0xFF0000);
	        ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(),
	                colorModel.getPixelSize(), palette);
	        //This is valid because we are using a 3-byte Data model with no transparent pixels
	        data.transparentPixel = -1;
	        WritableRaster raster = bufferedImage.getRaster();
	        int[] pixelArray = new int[3];
	        for (int y = 0; y < data.height; y++) {
	            for (int x = 0; x < data.width; x++) {
	                raster.getPixel(x, y, pixelArray);
	                int pixel = palette.getPixel(new RGB(pixelArray[0], pixelArray[1], pixelArray[2]));
	                data.setPixel(x, y, pixel);
	            }
	        }
	        return data;
	    }
	    return null;
	}

	public boolean isSVGFile(InputStream stream) throws IOException {
		if (stream == null) {
	        throw new IllegalArgumentException("InputStream cannot be null");
	    }
		stream.mark(Integer.MAX_VALUE);
		try {
		    int firstByte = stream.read();
		    return firstByte == '<';
		} finally {
			stream.reset();
		}
	}
}
