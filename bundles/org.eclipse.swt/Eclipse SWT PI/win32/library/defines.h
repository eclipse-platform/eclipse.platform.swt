/*
 * Copyright (c) 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

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