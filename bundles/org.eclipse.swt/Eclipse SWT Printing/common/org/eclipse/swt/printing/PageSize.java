/*******************************************************************************
 * Copyright (c) 2025 Eclipse Platform Contributors and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eclipse Platform Contributors - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.printing;

/**
 * A {@code PageSize} represents the dimensions of a page in points
 * (1 point = 1/72 inch), as used in PDF documents and printing.
 * <p>
 * This record provides predefined constants for common paper sizes
 * that are supported across all platforms (Windows, GTK, macOS).
 * Using these constants avoids confusion between points and pixels
 * and ensures consistent page dimensions.
 * </p>
 * <p>
 * Example usage:
 * </p>
 * <pre>
 *    PDFDocument pdf = new PDFDocument("output.pdf", PageSize.A4);
 *    GC gc = new GC(pdf);
 *    gc.drawText("Hello, PDF!", 100, 100);
 *    gc.dispose();
 *    pdf.dispose();
 * </pre>
 *
 * @param width the width of the page in points (1/72 inch)
 * @param height the height of the page in points (1/72 inch)
 *
 * @since 3.133
 *
 * @noreference This class is provisional API and subject to change. It is being made available to gather early feedback. The API or behavior may change in future releases as the implementation evolves based on user feedback.
 */
public record PageSize(double width, double height) {

	/** US Letter size: 8.5 x 11 inches (612 x 792 points) */
	public static final PageSize LETTER = new PageSize(612, 792);

	/** US Legal size: 8.5 x 14 inches (612 x 1008 points) */
	public static final PageSize LEGAL = new PageSize(612, 1008);

	/** ISO A3 size: 297 x 420 mm (842 x 1191 points) */
	public static final PageSize A3 = new PageSize(842, 1191);

	/** ISO A4 size: 210 x 297 mm (595 x 842 points) */
	public static final PageSize A4 = new PageSize(595, 842);

	/** ISO A5 size: 148 x 210 mm (420 x 595 points) */
	public static final PageSize A5 = new PageSize(420, 595);

	/** US Executive size: 7.25 x 10.5 inches (522 x 756 points) */
	public static final PageSize EXECUTIVE = new PageSize(522, 756);

	/** US Tabloid size: 11 x 17 inches (792 x 1224 points) */
	public static final PageSize TABLOID = new PageSize(792, 1224);
}
