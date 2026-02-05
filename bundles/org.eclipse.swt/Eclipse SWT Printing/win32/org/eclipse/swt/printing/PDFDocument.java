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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class are used to create PDF documents.
 * Applications create a GC on a PDFDocument using <code>new GC(pdfDocument)</code>
 * and then draw on the GC using the usual graphics calls.
 * <p>
 * A <code>PDFDocument</code> object may be constructed by providing
 * a filename and the page dimensions. After drawing is complete,
 * the document must be disposed to finalize the PDF file.
 * </p><p>
 * Application code must explicitly invoke the <code>PDFDocument.dispose()</code>
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 * <p>
 * <b>Note:</b> On Windows, this class uses the built-in "Microsoft Print to PDF"
 * printer which is available on Windows 10 and later.
 * </p>
 * <p>
 * The following example demonstrates how to use PDFDocument:
 * </p>
 * <pre>
 *    PDFDocument pdf = new PDFDocument("output.pdf", 612, 792); // Letter size in points
 *    GC gc = new GC(pdf);
 *    gc.drawText("Hello, PDF!", 100, 100);
 *    gc.dispose();
 *    pdf.dispose();
 * </pre>
 *
 * @see GC
 * @since 3.133
 *
 * @noreference This class is provisional API and subject to change. It is being made available to gather early feedback. The API or behavior may change in future releases as the implementation evolves based on user feedback.
 */
public final class PDFDocument extends Device {
	long handle;
	boolean isGCCreated = false;
	boolean jobStarted = false;
	boolean pageStarted = false;
	String filename;

	/**
	 * Preferred width of the page in points (1/72 inch)
	 */
	double preferredWidthInPoints;

	/**
	 * Preferred height of the page in points (1/72 inch)
	 */
	double preferredHeightInPoints;

	/**
	 * Actual width of the page in points (1/72 inch)
	 */
	double actualWidthInPoints;

	/**
	 * Actual height of the page in points (1/72 inch)
	 */
	double actualHeightInPoints;

	/** The name of the Microsoft Print to PDF printer */
	private static final String PDF_PRINTER_NAME = "Microsoft Print to PDF";

	/** Points per inch - the standard PDF coordinate system uses 72 points per inch */
	private static final double POINTS_PER_INCH = 72.0;

	/**
	 * Internal data class to pass PDF document parameters through
	 * the Device constructor.
	 */
	static class PDFDocumentData extends DeviceData {
		String filename;
		double widthInPoints;
		double heightInPoints;
	}

	/** Helper class to represent a paper size with orientation */
	private static class PaperSize {
		int paperSizeConstant;
		int orientation;
		double widthInPoints;
		double heightInPoints;

		PaperSize(int paperSize, int orientation, double width, double height) {
			this.paperSizeConstant = paperSize;
			this.orientation = orientation;
			this.widthInPoints = width;
			this.heightInPoints = height;
		}
	}

	/**
	 * Finds the best matching standard paper size for the given dimensions in points.
	 * Tries both portrait and landscape orientations and selects the one that
	 * minimizes wasted space while ensuring the content fits.
	 * The returned paper size will always be >= the requested size.
	 */
	private static PaperSize findBestPaperSize(double widthInPoints, double heightInPoints) {
		// Common paper sizes (width x height in points, portrait orientation)
		// 1 inch = 72 points
		int[][] standardSizes = {
			{OS.DMPAPER_LETTER, 612, 792},       // 8.5 x 11 inches
			{OS.DMPAPER_LEGAL, 612, 1008},       // 8.5 x 14 inches
			{OS.DMPAPER_A4, 595, 842},           // 8.27 x 11.69 inches (210 x 297 mm)
			{OS.DMPAPER_TABLOID, 792, 1224},     // 11 x 17 inches
			{OS.DMPAPER_A3, 842, 1191},          // 11.69 x 16.54 inches (297 x 420 mm)
			{OS.DMPAPER_EXECUTIVE, 522, 756},    // 7.25 x 10.5 inches
			{OS.DMPAPER_A5, 420, 595},           // 5.83 x 8.27 inches (148 x 210 mm)
		};

		PaperSize bestMatch = null;
		double minWaste = Double.MAX_VALUE;

		for (int[] size : standardSizes) {
			double paperWidth = size[1];
			double paperHeight = size[2];

			// Try portrait orientation
			if (widthInPoints <= paperWidth && heightInPoints <= paperHeight) {
				double waste = (paperWidth * paperHeight) - (widthInPoints * heightInPoints);
				if (waste < minWaste) {
					minWaste = waste;
					bestMatch = new PaperSize(size[0], OS.DMORIENT_PORTRAIT, paperWidth, paperHeight);
				}
			}

			// Try landscape orientation (swap width and height)
			if (widthInPoints <= paperHeight && heightInPoints <= paperWidth) {
				double waste = (paperHeight * paperWidth) - (widthInPoints * heightInPoints);
				if (waste < minWaste) {
					minWaste = waste;
					bestMatch = new PaperSize(size[0], OS.DMORIENT_LANDSCAPE, paperHeight, paperWidth);
				}
			}
		}

		// Error if requested size exceeds the largest available standard paper size
		if (bestMatch == null) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, " [Requested page size exceeds maximum supported size]");
		}

		return bestMatch;
	}

	/**
	 * Constructs a new PDFDocument with the specified filename and preferred page dimensions.
	 * <p>
	 * The dimensions specify the preferred page size in points (1/72 inch). On Windows,
	 * the Microsoft Print to PDF driver only supports standard paper sizes, so the actual
	 * page size may be larger than requested. Use {@link #getBounds()} to query the actual
	 * page dimensions after construction.
	 * </p>
	 * <p>
	 * You must dispose the PDFDocument when it is no longer required.
	 * </p>
	 *
	 * @param filename the path to the PDF file to create
	 * @param widthInPoints the preferred width of each page in points (1/72 inch)
	 * @param heightInPoints the preferred height of each page in points (1/72 inch)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_NULL_ARGUMENT - if filename is null</li>
	 *    <li>ERROR_INVALID_ARGUMENT - if width or height is not positive</li>
	 * </ul>
	 * @exception SWTError <ul>
	 *    <li>ERROR_NO_HANDLES - if the PDF printer is not available</li>
	 * </ul>
	 *
	 * @see #dispose()
	 * @see #getBounds()
	 */
	public PDFDocument(String filename, double widthInPoints, double heightInPoints) {
		super(checkData(filename, widthInPoints, heightInPoints));
	}

	/**
	 * Validates and prepares the data for construction.
	 */
	static PDFDocumentData checkData(String filename, double widthInPoints, double heightInPoints) {
		if (filename == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (widthInPoints <= 0 || heightInPoints <= 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		PDFDocumentData data = new PDFDocumentData();
		data.filename = filename;
		data.widthInPoints = widthInPoints;
		data.heightInPoints = heightInPoints;
		return data;
	}

	/**
	 * Creates the PDF device in the operating system.
	 * This method is called before <code>init</code>.
	 *
	 * @param data the DeviceData which describes the receiver
	 */
	@Override
	protected void create(DeviceData data) {
		PDFDocumentData pdfData = (PDFDocumentData) data;
		this.filename = pdfData.filename;
		this.preferredWidthInPoints = pdfData.widthInPoints;
		this.preferredHeightInPoints = pdfData.heightInPoints;

		// Find the best matching standard paper size for the requested dimensions
		PaperSize bestMatch = findBestPaperSize(preferredWidthInPoints, preferredHeightInPoints);
		this.actualWidthInPoints = bestMatch.widthInPoints;
		this.actualHeightInPoints = bestMatch.heightInPoints;

		// Create printer DC for "Microsoft Print to PDF"
		TCHAR driver = new TCHAR(0, "WINSPOOL", true);
		TCHAR deviceName = new TCHAR(0, PDF_PRINTER_NAME, true);

		// Get printer settings
		long[] hPrinter = new long[1];
		if (OS.OpenPrinter(deviceName, hPrinter, 0)) {
			int dwNeeded = OS.DocumentProperties(0, hPrinter[0], deviceName, 0, 0, 0);
			if (dwNeeded >= 0) {
				long hHeap = OS.GetProcessHeap();
				long lpInitData = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, dwNeeded);
				if (lpInitData != 0) {
					int rc = OS.DocumentProperties(0, hPrinter[0], deviceName, lpInitData, 0, OS.DM_OUT_BUFFER);
					if (rc == OS.IDOK) {
						DEVMODE devmode = new DEVMODE();
						OS.MoveMemory(devmode, lpInitData, DEVMODE.sizeof);
						devmode.dmPaperSize = (short) bestMatch.paperSizeConstant;
						devmode.dmOrientation = (short) bestMatch.orientation;
						devmode.dmFields = OS.DM_PAPERSIZE | OS.DM_ORIENTATION;
						OS.MoveMemory(lpInitData, devmode, DEVMODE.sizeof);
						handle = OS.CreateDC(driver, deviceName, 0, lpInitData);
					}
					OS.HeapFree(hHeap, 0, lpInitData);
				}
			}
			OS.ClosePrinter(hPrinter[0]);
		}

		if (handle == 0) {
			SWT.error(SWT.ERROR_NO_HANDLES, null, " [Failed to create device context for '" + PDF_PRINTER_NAME + "'. Ensure the printer is installed and enabled.]");
		}
	}

	/**
	 * Ensures the print job has been started
	 */
	private void ensureJobStarted() {
		if (!jobStarted) {
			DOCINFO di = new DOCINFO();
			di.cbSize = DOCINFO.sizeof;
			long hHeap = OS.GetProcessHeap();

			// Set output filename
			TCHAR buffer = new TCHAR(0, filename, true);
			int byteCount = buffer.length() * TCHAR.sizeof;
			long lpszOutput = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
			OS.MoveMemory(lpszOutput, buffer, byteCount);
			di.lpszOutput = lpszOutput;

			// Set document name
			TCHAR docName = new TCHAR(0, "SWT PDF Document", true);
			int docByteCount = docName.length() * TCHAR.sizeof;
			long lpszDocName = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, docByteCount);
			OS.MoveMemory(lpszDocName, docName, docByteCount);
			di.lpszDocName = lpszDocName;

			int rc = OS.StartDoc(handle, di);

			OS.HeapFree(hHeap, 0, lpszOutput);
			OS.HeapFree(hHeap, 0, lpszDocName);

			if (rc <= 0) {
				int lastError = OS.GetLastError();
				SWT.error(SWT.ERROR_NO_HANDLES, null, " [StartDoc failed for '" + PDF_PRINTER_NAME + "' (rc=" + rc + ", lastError=" + lastError + ")]");
			}
			jobStarted = true;
		}
	}

	/**
	 * Ensures the current page has been started
	 */
	private void ensurePageStarted() {
		ensureJobStarted();
		if (!pageStarted) {
			int rc = OS.StartPage(handle);
			if (rc <= 0) {
				int lastError = OS.GetLastError();
				SWT.error(SWT.ERROR_NO_HANDLES, null, " [StartPage failed (rc=" + rc + ", lastError=" + lastError + ")]");
			}
			pageStarted = true;
		}
	}

	/**
	 * Starts a new page in the PDF document.
	 * <p>
	 * This method should be called after completing the content of one page
	 * and before starting to draw on the next page. The new page will have
	 * the same dimensions as the initial page.
	 * </p>
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public void newPage() {
		checkDevice();
		if (pageStarted) {
			OS.EndPage(handle);
			pageStarted = false;
		}
		ensurePageStarted();
	}

	/**
	 * Starts a new page in the PDF document with the specified dimensions.
	 * <p>
	 * This method should be called after completing the content of one page
	 * and before starting to draw on the next page.
	 * </p>
	 * <p>
	 * <b>Note:</b> On Windows, changing page dimensions after the document
	 * has been started may not be fully supported by all printer drivers.
	 * </p>
	 *
	 * @param widthInPoints the preferred width of the new page in points (1/72 inch)
	 * @param heightInPoints the preferred height of the new page in points (1/72 inch)
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if width or height is not positive</li>
	 * </ul>
	 * @exception SWTException <ul>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public void newPage(double widthInPoints, double heightInPoints) {
		checkDevice();
		if (widthInPoints <= 0 || heightInPoints <= 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

		this.preferredWidthInPoints = widthInPoints;
		this.preferredHeightInPoints = heightInPoints;
		// Note: actual page size may be larger due to standard paper size constraints
		PaperSize bestMatch = findBestPaperSize(widthInPoints, heightInPoints);
		this.actualWidthInPoints = bestMatch.widthInPoints;
		this.actualHeightInPoints = bestMatch.heightInPoints;
		newPage();
	}

	/**
	 * Returns the actual width of the current page in points.
	 * <p>
	 * On Windows, this may be larger than the preferred width specified
	 * in the constructor due to standard paper size constraints.
	 * </p>
	 *
	 * @return the actual width in points (1/72 inch)
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public double getWidth() {
		checkDevice();
		return actualWidthInPoints;
	}

	/**
	 * Returns the actual height of the current page in points.
	 * <p>
	 * On Windows, this may be larger than the preferred height specified
	 * in the constructor due to standard paper size constraints.
	 * </p>
	 *
	 * @return the actual height in points (1/72 inch)
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	public double getHeight() {
		checkDevice();
		return actualHeightInPoints;
	}

	/**
	 * Returns the DPI (dots per inch) of the PDF document.
	 * Since the coordinate system is scaled to work in points (1/72 inch),
	 * this always returns 72 DPI, consistent with GTK and Cocoa implementations.
	 *
	 * @return a point whose x coordinate is the horizontal DPI and whose y coordinate is the vertical DPI
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	@Override
	public Point getDPI() {
		checkDevice();
		return new Point(72, 72);
	}

	/**
	 * Returns a rectangle describing the receiver's size and location.
	 * The rectangle dimensions are in points (1/72 inch).
	 * <p>
	 * On Windows, this returns the actual page size which may be larger
	 * than the preferred size specified in the constructor.
	 * </p>
	 *
	 * @return the bounding rectangle
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	@Override
	public Rectangle getBounds() {
		checkDevice();
		return new Rectangle(0, 0, (int) actualWidthInPoints, (int) actualHeightInPoints);
	}

	/**
	 * Returns a rectangle which describes the area of the
	 * receiver which is capable of displaying data.
	 * <p>
	 * On Windows, the printable area may be smaller than the page bounds
	 * due to printer margins.
	 * </p>
	 *
	 * @return the client area
	 *
	 * @exception SWTException <ul>
	 *    <li>ERROR_DEVICE_DISPOSED - if the receiver has been disposed</li>
	 * </ul>
	 */
	@Override
	public Rectangle getClientArea() {
		checkDevice();
		// Get the printable area from the device capabilities
		// Need actual printer DPI for conversion (not the user-facing 72 DPI)
		int printerDpiX = OS.GetDeviceCaps(handle, OS.LOGPIXELSX);
		int printerDpiY = OS.GetDeviceCaps(handle, OS.LOGPIXELSY);
		int printableWidth = OS.GetDeviceCaps(handle, OS.HORZRES);
		int printableHeight = OS.GetDeviceCaps(handle, OS.VERTRES);
		int offsetX = OS.GetDeviceCaps(handle, OS.PHYSICALOFFSETX);
		int offsetY = OS.GetDeviceCaps(handle, OS.PHYSICALOFFSETY);

		// Convert from device units to points
		double scaleX = POINTS_PER_INCH / printerDpiX;
		double scaleY = POINTS_PER_INCH / printerDpiY;

		int x = (int) (offsetX * scaleX);
		int y = (int) (offsetY * scaleY);
		int width = (int) (printableWidth * scaleX);
		int height = (int) (printableHeight * scaleY);

		return new Rectangle(x, y, width, height);
	}

	/**
	 * Invokes platform specific functionality to allocate a new GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	 * API for <code>PDFDocument</code>. It is marked public only so that it
	 * can be shared within the packages provided by SWT. It is not
	 * available on all platforms, and should never be called from
	 * application code.
	 * </p>
	 *
	 * @param data the platform specific GC data
	 * @return the platform specific GC handle
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	@Override
	public long internal_new_GC(GCData data) {
		checkDevice();
		if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES, null, " [PDF document handle is not valid]");
		if (data != null) {
			if (isGCCreated) SWT.error(SWT.ERROR_INVALID_ARGUMENT);

			ensurePageStarted();

			int mask = SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
			if ((data.style & mask) != 0) {
				data.layout = (data.style & SWT.RIGHT_TO_LEFT) != 0 ? OS.LAYOUT_RTL : 0;
			} else {
				data.style |= SWT.LEFT_TO_RIGHT;
			}
			data.device = this;
			data.nativeZoom = 100;
			data.font = getSystemFont();

			// Set up coordinate system scaling to work in points
			// The printer has its own DPI, so we scale to make 1 user unit = 1 point
			int printerDpiX = OS.GetDeviceCaps(handle, OS.LOGPIXELSX);
			int printerDpiY = OS.GetDeviceCaps(handle, OS.LOGPIXELSY);

			// Scale factor: printer_dpi / POINTS_PER_INCH (since we want 1 unit = 1 point = 1/72 inch)
			float scaleX = (float)(printerDpiX / POINTS_PER_INCH);
			float scaleY = (float)(printerDpiY / POINTS_PER_INCH);

			OS.SetGraphicsMode(handle, OS.GM_ADVANCED);
			float[] transform = new float[] {scaleX, 0, 0, scaleY, 0, 0};
			OS.SetWorldTransform(handle, transform);

			isGCCreated = true;
		}
		return handle;
	}

	/**
	 * Invokes platform specific functionality to dispose a GC handle.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
	 * API for <code>PDFDocument</code>. It is marked public only so that it
	 * can be shared within the packages provided by SWT. It is not
	 * available on all platforms, and should never be called from
	 * application code.
	 * </p>
	 *
	 * @param hDC the platform specific GC handle
	 * @param data the platform specific GC data
	 *
	 * @noreference This method is not intended to be referenced by clients.
	 */
	@Override
	public void internal_dispose_GC(long hDC, GCData data) {
		if (data != null) isGCCreated = false;
	}

	/**
	 * @noreference This method is not intended to be referenced by clients.
	 */
	@Override
	public boolean isAutoScalable() {
		return false;
	}

	/**
	 * Destroys the PDF document handle.
	 * This method is called internally by the dispose
	 * mechanism of the <code>Device</code> class.
	 */
	@Override
	protected void destroy() {
		if (handle != 0) {
			if (pageStarted) {
				OS.EndPage(handle);
			}
			if (jobStarted) {
				OS.EndDoc(handle);
			}
			OS.DeleteDC(handle);
			handle = 0;
		}
	}
}
