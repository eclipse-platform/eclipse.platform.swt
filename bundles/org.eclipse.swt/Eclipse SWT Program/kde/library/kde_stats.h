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

#ifdef NATIVE_STATS
extern int KDE_nativeFunctionCount;
extern int KDE_nativeFunctionCallCount[];
extern char* KDE_nativeFunctionNames[];
#define KDE_NATIVE_ENTER(env, that, func) KDE_nativeFunctionCallCount[func]++;
#define KDE_NATIVE_EXIT(env, that, func) 
#else
#define KDE_NATIVE_ENTER(env, that, func) 
#define KDE_NATIVE_EXIT(env, that, func) 
#endif

typedef enum {
	_1KApplication_1new_FUNC,
	_1KGlobal_1iconLoader_FUNC,
	_1KIconLoader_1iconPath_FUNC,
	_1KMimeTypeListIterator_1delete_FUNC,
	_1KMimeTypeListIterator_1dereference_FUNC,
	_1KMimeTypeListIterator_1equals_FUNC,
	_1KMimeTypeListIterator_1increment_FUNC,
	_1KMimeTypeList_1begin_FUNC,
	_1KMimeTypeList_1delete_FUNC,
	_1KMimeTypeList_1end_FUNC,
	_1KMimeType_1allMimeTypes_FUNC,
	_1KMimeType_1delete_FUNC,
	_1KMimeType_1icon_FUNC,
	_1KMimeType_1mimeType_FUNC,
	_1KMimeType_1name_FUNC,
	_1KMimeType_1offers_FUNC,
	_1KMimeType_1patterns_FUNC,
	_1KRun_1runURL_FUNC,
	_1KServiceList_1delete_FUNC,
	_1KURL_1delete_FUNC,
	_1KURL_1new_FUNC,
	_1QCString_1data_FUNC,
	_1QCString_1delete_FUNC,
	_1QCString_1new_FUNC,
	_1QStringListIterator_1delete_FUNC,
	_1QStringListIterator_1dereference_FUNC,
	_1QStringListIterator_1equals_FUNC,
	_1QStringListIterator_1increment_FUNC,
	_1QStringList_1begin_FUNC,
	_1QStringList_1delete_FUNC,
	_1QStringList_1end_FUNC,
	_1QString_1delete_FUNC,
	_1QString_1equals_FUNC,
	_1QString_1new_FUNC,
	_1QString_1utf8_FUNC,
	_1XFreePixmap_FUNC,
	_1XpmReadFileToPixmap_FUNC,
	_1free_FUNC,
	_1malloc_FUNC,
	_1sigaction_FUNC,
	_1sigaction_1sizeof_FUNC,
} KDE_FUNCS;
