/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

/*
 * - Size Optimization -
 * You can specify here which structs and SWT JNI calls
 * you want to exclude. This can be useful to create
 * a smaller library, based on your particular requirements.
 * For example, if your application does not use the type ACCEL
 * you can add:
 * #define NO_ACCEL
 * If your application does not require the function Arc,
 * you can add:
 * #define NO_Arc
 * By default, all types and JNI calls relevant to a
 * platform are included.
 *
 */