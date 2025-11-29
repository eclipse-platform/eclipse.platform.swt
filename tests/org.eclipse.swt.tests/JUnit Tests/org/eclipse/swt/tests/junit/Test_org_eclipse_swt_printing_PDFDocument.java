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
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.DataFormatException;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.printing.PDFDocument;
import org.eclipse.swt.widgets.Display;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Automated Test Suite for class org.eclipse.swt.printing.PDFDocument
 *
 * @see org.eclipse.swt.printing.PDFDocument
 */
public class Test_org_eclipse_swt_printing_PDFDocument {

	Display display;
	PDFDocument pdfDocument;
	GC gc;
	File tempFile;

	@TempDir
	Path tempDir;

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
	}

	@AfterEach
	public void tearDown() {
		if (gc != null && !gc.isDisposed()) {
			gc.dispose();
		}
		if (pdfDocument != null && !pdfDocument.isDisposed()) {
			pdfDocument.dispose();
		}
		if (tempFile != null && tempFile.exists()) {
			tempFile.delete();
		}
	}

	@Test
	public void test_createPDFDocumentWithHelloWorld() throws IOException, DataFormatException {
		// Create a temporary file for the PDF
		tempFile = Files.createTempFile(tempDir, "test", ".pdf").toFile();
		String filename = tempFile.getAbsolutePath();

		// Create PDF document with standard letter size (612 x 792 points)
		pdfDocument = new PDFDocument(filename, 612, 792);
		assertNotNull(pdfDocument, "PDFDocument should be created");

		// Create a GC on the PDF document
		gc = new GC(pdfDocument);
		assertNotNull(gc, "GC should be created on PDFDocument");

		// Draw "hello world" text
		gc.drawText("hello world", 100, 100);

		// Dispose of resources to finalize the PDF
		gc.dispose();
		gc = null;
		pdfDocument.dispose();
		pdfDocument = null;

		// Verify the PDF file was created and is not empty
		assertTrue(tempFile.exists(), "PDF file should exist");
		assertTrue(tempFile.length() > 0, "PDF file should not be empty");

		// Verify PDF magic bytes and content
		byte[] fileContent = Files.readAllBytes(tempFile.toPath());
		assertTrue(fileContent.length >= 5, "PDF file should have at least 5 bytes for header");

		// Check for PDF magic bytes: %PDF-
		String headerString = new String(fileContent, 0, Math.min(5, fileContent.length));
		assertTrue(headerString.startsWith("%PDF-"),
			"PDF file should start with %PDF- magic bytes, but got: " + headerString);
	}

}
