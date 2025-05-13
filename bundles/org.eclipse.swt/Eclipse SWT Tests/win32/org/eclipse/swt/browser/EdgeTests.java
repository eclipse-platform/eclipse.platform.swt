package org.eclipse.swt.browser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.*;
import java.util.*;

import org.eclipse.swt.internal.*;
import org.junit.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;

@ExtendWith(PlatformSpecificExecutionExtension.class)
class EdgeTests {

	private String originalTempDir;

	@Before
	public void setup() throws Exception {
		originalTempDir = System.getProperty("java.io.tmpdir");
	}

	@After
	public void tearDown() throws Exception {
		setTempDirAndInitializeEdgeLocationForCustomTextPage(originalTempDir);
	}

	private static void setTempDirAndInitializeEdgeLocationForCustomTextPage(String dir) {
		System.setProperty("java.io.tmpdir", dir);
		Edge.setupLocationForCustomTextPage();
	}

	/**
	 * https://github.com/eclipse-platform/eclipse.platform.swt/issues/1912
	 */
	@Test
	public void handlingOfTempDirWithSpacesAndUnicodeCharacters() throws Exception {
		setTempDirAndInitializeEdgeLocationForCustomTextPage(
				Files.createTempDirectory("spaces and únîcòde").toString());

		// URI_FOR_CUSTOM_TEXT_PAGE must already be encoded internally
		// for correct handling of spaces and unicode characters
		assertTrue(Edge.URI_FOR_CUSTOM_TEXT_PAGE.toString().contains("spaces%20and%20%C3%BAn%C3%AEc%C3%B2de"));
		assertTrue(Edge.isLocationForCustomText(Edge.URI_FOR_CUSTOM_TEXT_PAGE.toASCIIString()));
	}

	/**
	 * https://github.com/eclipse-platform/eclipse.platform.swt/issues/2061
	 */
	@Test
	public void handlingOfUpperLowerCase() throws Exception {
		setTempDirAndInitializeEdgeLocationForCustomTextPage(Files.createTempDirectory("FoObAr").toString());

		String lowerCaseUri = Edge.URI_FOR_CUSTOM_TEXT_PAGE.toASCIIString().toLowerCase(Locale.ROOT);
		String upperCaseUri = "file"
				+ Edge.URI_FOR_CUSTOM_TEXT_PAGE.toASCIIString().toUpperCase(Locale.ROOT).substring(4);

		assertTrue(Edge.isLocationForCustomText(lowerCaseUri));
		assertTrue(Edge.isLocationForCustomText(upperCaseUri));
	}

	@Test
	public void handlingOfNonFileURIs() throws Exception {
		setTempDirAndInitializeEdgeLocationForCustomTextPage(Files.createTempDirectory("any").toString());

		String httpsUri = "https://eclipse.org";

		assertFalse(Edge.isLocationForCustomText(httpsUri));
	}

	@Test
	public void handlingOfInvalidFileURIs() throws Exception {
		setTempDirAndInitializeEdgeLocationForCustomTextPage(Files.createTempDirectory("any").toString());

		String brokenFileUri = "file://--";

		assertFalse(Edge.isLocationForCustomText(brokenFileUri));
	}

}
