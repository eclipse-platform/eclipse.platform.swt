/*******************************************************************************
 * Copyright (c) 2023 Christoph Läubrich and others.
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
package org.eclipse.swt.opengl;

/**
 * The GL class is the main entry point to access the native OpenGL functions.
 * Keep in mind that OpenGL is single threaded only, as SWT, so you best
 * access this only from inside a display thread, but there are no checks or
 * exceptions that enforce this.
 * <p>
 * <b>IMPORTANT:</b> Because of
 * <a href="https://github.com/eclipse-jdt/eclipse.jdt.core/issues/809">a bug in
 * JDT</a> currently all access must be fully qualified e.g.
 * <code>org.eclipse.swt.opengl.GL.<method name></code>. once this bug is fixed,
 * you usually want to use
 * <code>import static org.eclipse.swt.opengl.GL.*</code> instead.
 * </p>
 *
 * @since 3.123
 */
public class GL extends org.eclipse.swt.opengl.panama.glut_h {

}
