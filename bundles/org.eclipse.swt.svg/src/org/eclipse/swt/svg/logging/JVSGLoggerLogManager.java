/*******************************************************************************
 * Copyright (c) 2026 Eclipse contributors and others.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *******************************************************************************/
package org.eclipse.swt.svg.logging;

import java.util.function.Supplier;

import com.github.weisj.jsvg.logging.LogManager;
import com.github.weisj.jsvg.logging.Logger;

public class JVSGLoggerLogManager implements LogManager {

	private static final Logger.Level LEVEL = Logger.Level.valueOf(System.getProperty("org.eclipse.swt.svg.logging", "ERROR"));

	@Override
	public Logger createLogger(String name) {
		return new Logger() {
			@Override
			public void log(Logger.Level level, String message, Throwable e) {
				if (level.compareTo(LEVEL) <= 0) {
					return;
				}
				doLog(level, message);
				e.printStackTrace();
			}

			@Override
			public void log(Logger.Level level, Supplier<String> messageSupplier) {
				if (level.compareTo(LEVEL) < 0) {
					return;
				}
				doLog(level, messageSupplier.get());
			}

			@Override
			public void log(Logger.Level level, String message) {
				if (level.compareTo(LEVEL) < 0) {
					return;
				}
				doLog(level, message);
			}

			private void doLog(Logger.Level level, String message) {
				System.err.format("JSVGRasterizer: %s: %s: %s", level, name, message).println();
			}
		};
	}
}
