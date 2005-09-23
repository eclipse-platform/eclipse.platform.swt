/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
#include <windows.h>

GLYPHMETRICSFLOAT *getGLYPHMETRICSFLOATFields(JNIEnv *env, jobject lpObject, GLYPHMETRICSFLOAT *lpStruct);
void setGLYPHMETRICSFLOATFields(JNIEnv *env, jobject lpObject, GLYPHMETRICSFLOAT *lpStruct);

LAYERPLANEDESCRIPTOR *getLAYERPLANEDESCRIPTORFields(JNIEnv *env, jobject lpObject, LAYERPLANEDESCRIPTOR *lpStruct);
void setLAYERPLANEDESCRIPTORFields(JNIEnv *env, jobject lpObject, LAYERPLANEDESCRIPTOR *lpStruct);

POINTFLOAT *getPOINTFLOATFields(JNIEnv *env, jobject lpObject, POINTFLOAT *lpStruct);
void setPOINTFLOATFields(JNIEnv *env, jobject lpObject, POINTFLOAT *lpStruct);

PIXELFORMATDESCRIPTOR *getPIXELFORMATDESCRIPTORFields(JNIEnv *env, jobject lpObject, PIXELFORMATDESCRIPTOR *lpStruct);
void setPIXELFORMATDESCRIPTORFields(JNIEnv *env, jobject lpObject, PIXELFORMATDESCRIPTOR *lpStruct);

