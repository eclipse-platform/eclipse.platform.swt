/*******************************************************************************
* Copyright (c) 2000, 2004 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Common Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/cpl-v10.html
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

#ifdef NATIVE_STATS
int KDE_nativeFunctionCallCount[];
char* KDE_nativeFunctionNames[];
#define KDE_NATIVE_ENTER(env, that, func) KDE_nativeFunctionCallCount[func]++;
#define KDE_NATIVE_EXIT(env, that, func) 
#else
#define KDE_NATIVE_ENTER(env, that, func) 
#define KDE_NATIVE_EXIT(env, that, func) 
#endif

typedef enum {
	KApplication_1new_FUNC,
	KGlobal_1iconLoader_FUNC,
	KIconLoader_1iconPath_FUNC,
	KMimeTypeListIterator_1delete_FUNC,
	KMimeTypeListIterator_1dereference_FUNC,
	KMimeTypeListIterator_1equals_FUNC,
	KMimeTypeListIterator_1increment_FUNC,
	KMimeTypeList_1begin_FUNC,
	KMimeTypeList_1delete_FUNC,
	KMimeTypeList_1end_FUNC,
	KMimeType_1allMimeTypes_FUNC,
	KMimeType_1delete_FUNC,
	KMimeType_1icon_FUNC,
	KMimeType_1mimeType_FUNC,
	KMimeType_1name_FUNC,
	KMimeType_1offers_FUNC,
	KMimeType_1patterns_FUNC,
	KRun_1runURL_FUNC,
	KServiceList_1delete_FUNC,
	KURL_1delete_FUNC,
	KURL_1new_FUNC,
	QCString_1data_FUNC,
	QCString_1delete_FUNC,
	QCString_1new_FUNC,
	QStringListIterator_1delete_FUNC,
	QStringListIterator_1dereference_FUNC,
	QStringListIterator_1equals_FUNC,
	QStringListIterator_1increment_FUNC,
	QStringList_1begin_FUNC,
	QStringList_1delete_FUNC,
	QStringList_1end_FUNC,
	QString_1delete_FUNC,
	QString_1equals_FUNC,
	QString_1new_FUNC,
	QString_1utf8_FUNC,
	free_FUNC,
	malloc_FUNC,
} KDE_FUNCS;
