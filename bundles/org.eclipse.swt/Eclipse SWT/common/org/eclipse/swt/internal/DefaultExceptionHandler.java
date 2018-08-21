/*******************************************************************************
 * Copyright (c) 2016 Google Inc and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Stefan Xenos (Google) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal;

import java.util.function.*;

public class DefaultExceptionHandler {

public static final Consumer<RuntimeException> RUNTIME_EXCEPTION_HANDLER = exception -> {
	throw exception;
};

public static final Consumer<Error> RUNTIME_ERROR_HANDLER = error -> {
	throw error;
};

}
