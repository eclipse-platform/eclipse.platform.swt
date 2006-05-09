/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
 
/* Special sizeof's */
#define fd_set_sizeof() sizeof(fd_set)
#define localeconv_decimal_point() localeconv()->decimal_point

/* Libraries for dynamic loaded functions */
#define XRenderQueryExtension_LIB "libXrender.so"
#define XRenderQueryVersion_LIB "libXrender.so"
#define XRenderFindStandardFormat_LIB "libXrender.so"
#define XRenderFindVisualFormat_LIB "libXrender.so"
#define XRenderComposite_LIB "libXrender.so"
#define XRenderCreatePicture_LIB "libXrender.so"
#define XRenderFreePicture_LIB "libXrender.so"
#define XRenderSetPictureClipRectangles_LIB "libXrender.so"
#define XRenderSetPictureClipRegion_LIB "libXrender.so"
#define XRenderSetPictureTransform_LIB "libXrender.so"

