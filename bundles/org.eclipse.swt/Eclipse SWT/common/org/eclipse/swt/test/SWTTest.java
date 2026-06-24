/*******************************************************************************
 * Copyright (c) 2025 Christoph Läubrich and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Christoph Läubrich - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.test;

import java.lang.reflect.*;

import org.eclipse.pde.api.tools.annotations.*;
import org.eclipse.swt.widgets.*;
import org.junit.jupiter.api.extension.*;

/**
 * The {@link SWTTest} is an extension that can be using in JUnit Platform
 * Runners as an extension to execute the test code in the default display
 * thread.
 * <p>
 * <b>Usage: </b>You would never use this in any client code but when writing a
 * test for the JUnit Platform like in this example:
 * </p>
 * <p>
 * <pre>
 * &#64;ExtendWith(SWTTest.class)
 * public class MyTestThaRequireUI {
 *
 * }
 * </pre>
 * </p>
 */
@NoInstantiate
public final class SWTTest implements InvocationInterceptor {

	@Override
	public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
			ExtensionContext extensionContext) throws Throwable {

		if (Display.getCurrent() == null) {
			invocation.proceed();
		} else {
			Throwable[] throwable = new Throwable[1];
			Display.getDefault().syncExec(() -> {
				try {
					invocation.proceed();
				} catch (Throwable t) {
					throwable[0] = t;
				}

			});
			Throwable t = throwable[0];
			if (t != null) {
				throw t;
			}
		}
	}
}
