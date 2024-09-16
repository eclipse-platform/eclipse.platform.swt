package org.eclipse.swt.graphics;

import java.io.*;
import java.nio.charset.*;

/**
 * Utility class for handling SVG-related operations.
 *
 * @since 3.129
 */
public class SVGUtil {

	/**
	 * Determines whether the given {@link InputStream} contains a SVG file.
	 *
	 * @param data byte array to check.
	 * @return {@code true} if the input stream contains SVG content; {@code false}
	 *         otherwise.
	 * @throws IOException              if an error occurs while reading the stream.
	 * @throws IllegalArgumentException if the input stream is {@code null}.
	 */
	public static boolean isSVGFile(byte[] data) throws IOException {
		String content = new String(data, 0, Math.min(data.length, 512), StandardCharsets.UTF_8);
		return content.contains("<svg");
	}

	/**
	 * Determines whether the given {@link InputStream} contains a SVG file.
	 *
	 * @param inputStream the input stream to check.
	 * @return {@code true} if the input stream contains SVG content; {@code false}
	 *         otherwise.
	 * @throws IOException              if an error occurs while reading the stream.
	 * @throws IllegalArgumentException if the input stream is {@code null}.
	 */
	public static boolean isSVGFile(InputStream inputStream) throws IOException {
		if (inputStream == null) {
			throw new IllegalArgumentException("InputStream cannot be null");
		}
		byte[] data = inputStream.readNBytes(512);
		return isSVGFile(data);
	}
}
