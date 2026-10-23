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
package org.eclipse.swt.internal.canvasext;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;

/**
 * Handles the creation of external canvas extensions based on the style of the
 * canvas and the availability of an extension factory.
 */
public class ExternalCanvasHandler {

	private static boolean FAILED_WITH_ERRORS = false;
	private static boolean ExternalCanvasWasLogged = false;

	// DISABLE the external canvas extension. This is necessary in case the
	// extension causes problems on a platform. In this case, the user can set this
	// property to disable the extension and use the default canvas implementation.
	private final static String DISABLE_EXTERNAL_CANVAS = "org.eclipse.swt.external.canvas:disabled";

	// This is only for test cases in order to check whether the software works with
	// the canvas extension.
	// NEVER USE THIS IN PRODUCTIVE CODE!!
	private final static String FORCE_ENABLE_EXTERNAL_CANVAS = "org.eclipse.swt.external.canvas:forceEnabled";

	// In order to know whether an external canvas was activated.
	private final static String LOG_EXTERNAL_CANVAS_ACTIVATION = "org.eclipse.swt.external.canvas:logActivation";

	private static IExternalCanvasFactory externalFactory = null;
	private static boolean factoryLoaded = false;

	private static void loadFactory() {

		if (factoryLoaded)
			return;

		factoryLoaded = true;
		try {
			// it is possible that the loading of external libraries fails.
			externalFactory = ServiceLoader.load(IExternalCanvasFactory.class).findFirst().orElse(null);
		} catch (Throwable t) {
			FAILED_WITH_ERRORS = true;
			t.printStackTrace(System.err);
		}
	}


	private static boolean isDisabled() {
		var disable = System.getProperty(DISABLE_EXTERNAL_CANVAS);
		if (disable != null) {
			return true;
		}
		return false;
	}

	private static boolean isForcedEnabled() {
		var forceEnable = System.getProperty(FORCE_ENABLE_EXTERNAL_CANVAS);
		if (forceEnable != null) {
			return true;
		}
		return false;
	}

	private static boolean isLogActive() {
		var log = System.getProperty(LOG_EXTERNAL_CANVAS_ACTIVATION);
		if (log != null) {
			return true;
		}
		return false;
	}

	public static boolean isActive(Canvas canvas, int style) {

		if (FAILED_WITH_ERRORS)
			return false;

		if (isDisabled()) {

			if (!ExternalCanvasWasLogged && isLogActive()) {
				System.out.println("External canvas disabled.");
				ExternalCanvasWasLogged = true;
			}

			return false;
		}

		if (canvas instanceof StyledText || canvas instanceof Decorations)
			return false;

		loadFactory();

		if ((style & SWT.SKIA) != 0 && externalFactory != null)
			return true;

		if (externalFactory == null) {
			if (!ExternalCanvasWasLogged && isLogActive()) {
				System.out.println("No external canvas factory found. External canvas disabled.");
				ExternalCanvasWasLogged = true;
			}
			return false;
		}

		if (isForcedEnabled()) {

			if (!ExternalCanvasWasLogged && isLogActive()) {
				System.out.println("Force enabled, external canvas factory found. External canvas will be activated.");
				ExternalCanvasWasLogged = true;
			}

			return true;
		}

		return false;
	}

	public static IExternalCanvasHandler createHandler(Canvas c) {

		if (FAILED_WITH_ERRORS)
			return null;

		if (isDisabled())
			return null;

		loadFactory();

		try {
			// it is possible that the loading of external libraries fails.
			var externalCanvas = externalFactory.createCanvasExtension(c);

			if (!ExternalCanvasWasLogged && isLogActive()) {
				System.out.println("External canvas activated.");
				ExternalCanvasWasLogged = true;
			}

			return externalCanvas;
		} catch (Throwable t) {
			FAILED_WITH_ERRORS = true;
			t.printStackTrace(System.err);
			return null;
		}
	}
}
