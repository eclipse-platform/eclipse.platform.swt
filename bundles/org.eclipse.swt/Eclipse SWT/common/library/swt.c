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
 
#include "swt.h"

int IS_JNI_1_2 = 0;

#ifdef JNI_VERSION_1_2
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
	IS_JNI_1_2 = 1;
	return JNI_VERSION_1_2;
}
#endif

void throwOutOfMemory(JNIEnv *env) {
	jclass clazz = (*env)->FindClass(env, "java/lang/OutOfMemoryError");
	if (clazz != NULL) {
		(*env)->ThrowNew(env, clazz, "");
	}
}
