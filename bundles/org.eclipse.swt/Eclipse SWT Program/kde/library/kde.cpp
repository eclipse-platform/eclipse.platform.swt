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
#include "kde_structs.h"
#include "kde_stats.h"

extern "C" {

#define KDE_NATIVE(func) Java_org_eclipse_swt_internal_kde_KDE_##func

#ifndef NO__1KApplication_1new
JNIEXPORT jint JNICALL KDE_NATIVE(_1KApplication_1new)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3, jboolean arg4)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1KApplication_1new_FUNC);
	rc = (jint)new KApplication(arg0, (char **)arg1, *(QCString *)arg2, (bool)arg3, (bool)arg4);
	KDE_NATIVE_EXIT(env, that, _1KApplication_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1KGlobal_1iconLoader
JNIEXPORT jint JNICALL KDE_NATIVE(_1KGlobal_1iconLoader)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1KGlobal_1iconLoader_FUNC);
	rc = (jint)KGlobal::iconLoader();
	KDE_NATIVE_EXIT(env, that, _1KGlobal_1iconLoader_FUNC);
	return rc;
}
#endif

#ifndef NO__1KIconLoader_1iconPath
JNIEXPORT jint JNICALL KDE_NATIVE(_1KIconLoader_1iconPath)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1KIconLoader_1iconPath_FUNC);
	QString temp = ((KIconLoader *)arg0)->iconPath(*(QString *)arg1, arg2, (bool)arg3);
	{
		QString* copy = new QString();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, _1KIconLoader_1iconPath_FUNC);
	return rc;
}
#endif

#ifndef NO__1KMimeTypeListIterator_1delete
JNIEXPORT void JNICALL KDE_NATIVE(_1KMimeTypeListIterator_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, _1KMimeTypeListIterator_1delete_FUNC);
	delete (QValueListIterator<KMimeType::Ptr> *)arg0;
	KDE_NATIVE_EXIT(env, that, _1KMimeTypeListIterator_1delete_FUNC);
}
#endif

#ifndef NO__1KMimeTypeListIterator_1dereference
JNIEXPORT jint JNICALL KDE_NATIVE(_1KMimeTypeListIterator_1dereference)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1KMimeTypeListIterator_1dereference_FUNC);
	KSharedPtr<KMimeType> temp = KMimeTypeListIterator_dereference(arg0);
	{
		KSharedPtr<KMimeType>* copy = new KSharedPtr<KMimeType>();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, _1KMimeTypeListIterator_1dereference_FUNC);
	return rc;
}
#endif

#ifndef NO__1KMimeTypeListIterator_1equals
JNIEXPORT jboolean JNICALL KDE_NATIVE(_1KMimeTypeListIterator_1equals)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	KDE_NATIVE_ENTER(env, that, _1KMimeTypeListIterator_1equals_FUNC);
	rc = (jboolean)KMimeTypeListIterator_equals(arg0, arg1);
	KDE_NATIVE_EXIT(env, that, _1KMimeTypeListIterator_1equals_FUNC);
	return rc;
}
#endif

#ifndef NO__1KMimeTypeListIterator_1increment
JNIEXPORT void JNICALL KDE_NATIVE(_1KMimeTypeListIterator_1increment)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, _1KMimeTypeListIterator_1increment_FUNC);
	KMimeTypeListIterator_increment(arg0);
	KDE_NATIVE_EXIT(env, that, _1KMimeTypeListIterator_1increment_FUNC);
}
#endif

#ifndef NO__1KMimeTypeList_1begin
JNIEXPORT jint JNICALL KDE_NATIVE(_1KMimeTypeList_1begin)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1KMimeTypeList_1begin_FUNC);
	QValueListIterator<KMimeType::Ptr> temp = ((KMimeType::List *)arg0)->begin();
	{
		QValueListIterator<KMimeType::Ptr>* copy = new QValueListIterator<KMimeType::Ptr>();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, _1KMimeTypeList_1begin_FUNC);
	return rc;
}
#endif

#ifndef NO__1KMimeTypeList_1delete
JNIEXPORT void JNICALL KDE_NATIVE(_1KMimeTypeList_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, _1KMimeTypeList_1delete_FUNC);
	delete (KMimeType::List *)arg0;
	KDE_NATIVE_EXIT(env, that, _1KMimeTypeList_1delete_FUNC);
}
#endif

#ifndef NO__1KMimeTypeList_1end
JNIEXPORT jint JNICALL KDE_NATIVE(_1KMimeTypeList_1end)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1KMimeTypeList_1end_FUNC);
	QValueListIterator<KMimeType::Ptr> temp = ((KMimeType::List *)arg0)->end();
	{
		QValueListIterator<KMimeType::Ptr>* copy = new QValueListIterator<KMimeType::Ptr>();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, _1KMimeTypeList_1end_FUNC);
	return rc;
}
#endif

#ifndef NO__1KMimeType_1allMimeTypes
JNIEXPORT jint JNICALL KDE_NATIVE(_1KMimeType_1allMimeTypes)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1KMimeType_1allMimeTypes_FUNC);
	KMimeType::List temp = KMimeType::allMimeTypes();
	{
		KMimeType::List* copy = new KMimeType::List();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, _1KMimeType_1allMimeTypes_FUNC);
	return rc;
}
#endif

#ifndef NO__1KMimeType_1delete
JNIEXPORT void JNICALL KDE_NATIVE(_1KMimeType_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, _1KMimeType_1delete_FUNC);
	delete (KSharedPtr<KMimeType> *)arg0;
	KDE_NATIVE_EXIT(env, that, _1KMimeType_1delete_FUNC);
}
#endif

#ifndef NO__1KMimeType_1icon
JNIEXPORT jint JNICALL KDE_NATIVE(_1KMimeType_1icon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1KMimeType_1icon_FUNC);
	QString temp = (*(KSharedPtr<KMimeType> *)arg0)->icon((const QString&)arg1, (bool)arg2);
	{
		QString* copy = new QString();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, _1KMimeType_1icon_FUNC);
	return rc;
}
#endif

#ifndef NO__1KMimeType_1mimeType
JNIEXPORT jint JNICALL KDE_NATIVE(_1KMimeType_1mimeType)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1KMimeType_1mimeType_FUNC);
	KSharedPtr<KMimeType> temp = KMimeType::mimeType(*(QString *)arg0);
	{
		KSharedPtr<KMimeType>* copy = new KSharedPtr<KMimeType>();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, _1KMimeType_1mimeType_FUNC);
	return rc;
}
#endif

#ifndef NO__1KMimeType_1name
JNIEXPORT jint JNICALL KDE_NATIVE(_1KMimeType_1name)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1KMimeType_1name_FUNC);
	QString temp = (*(KSharedPtr<KMimeType> *)arg0)->name();
	{
		QString* copy = new QString();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, _1KMimeType_1name_FUNC);
	return rc;
}
#endif

#ifndef NO__1KMimeType_1offers
JNIEXPORT jint JNICALL KDE_NATIVE(_1KMimeType_1offers)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1KMimeType_1offers_FUNC);
	KService::List temp = KMimeType::offers(*(QString *)arg0);
	{
		KService::List* copy = new KService::List();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, _1KMimeType_1offers_FUNC);
	return rc;
}
#endif

#ifndef NO__1KMimeType_1patterns
JNIEXPORT jint JNICALL KDE_NATIVE(_1KMimeType_1patterns)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1KMimeType_1patterns_FUNC);
	QStringList temp = (*(KSharedPtr<KMimeType> *)arg0)->patterns();
	{
		QStringList* copy = new QStringList();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, _1KMimeType_1patterns_FUNC);
	return rc;
}
#endif

#ifndef NO__1KRun_1runURL
JNIEXPORT jint JNICALL KDE_NATIVE(_1KRun_1runURL)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1KRun_1runURL_FUNC);
	rc = (jint)KRun::runURL(*(KURL *)arg0, *(QString *)arg1);
	KDE_NATIVE_EXIT(env, that, _1KRun_1runURL_FUNC);
	return rc;
}
#endif

#ifndef NO__1KServiceList_1delete
JNIEXPORT void JNICALL KDE_NATIVE(_1KServiceList_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, _1KServiceList_1delete_FUNC);
	delete (KService::List*)arg0;
	KDE_NATIVE_EXIT(env, that, _1KServiceList_1delete_FUNC);
}
#endif

#ifndef NO__1KURL_1delete
JNIEXPORT void JNICALL KDE_NATIVE(_1KURL_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, _1KURL_1delete_FUNC);
	delete (KURL*)arg0;
	KDE_NATIVE_EXIT(env, that, _1KURL_1delete_FUNC);
}
#endif

#ifndef NO__1KURL_1new
JNIEXPORT jint JNICALL KDE_NATIVE(_1KURL_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1KURL_1new_FUNC);
	rc = (jint)new KURL(*(QString *)arg0);
	KDE_NATIVE_EXIT(env, that, _1KURL_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1QCString_1data
JNIEXPORT jint JNICALL KDE_NATIVE(_1QCString_1data)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1QCString_1data_FUNC);
	rc = (jint)((QCString *)arg0)->data();
	KDE_NATIVE_EXIT(env, that, _1QCString_1data_FUNC);
	return rc;
}
#endif

#ifndef NO__1QCString_1delete
JNIEXPORT void JNICALL KDE_NATIVE(_1QCString_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, _1QCString_1delete_FUNC);
	delete (QCString *)arg0;
	KDE_NATIVE_EXIT(env, that, _1QCString_1delete_FUNC);
}
#endif

#ifndef NO__1QCString_1new
JNIEXPORT jint JNICALL KDE_NATIVE(_1QCString_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1QCString_1new_FUNC);
	if (arg0) if ((lparg0 = env->GetByteArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)new QCString((char *)lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	KDE_NATIVE_EXIT(env, that, _1QCString_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1QStringListIterator_1delete
JNIEXPORT void JNICALL KDE_NATIVE(_1QStringListIterator_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, _1QStringListIterator_1delete_FUNC);
	delete (QValueListIterator<QString> *)arg0;
	KDE_NATIVE_EXIT(env, that, _1QStringListIterator_1delete_FUNC);
}
#endif

#ifndef NO__1QStringListIterator_1dereference
JNIEXPORT jint JNICALL KDE_NATIVE(_1QStringListIterator_1dereference)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1QStringListIterator_1dereference_FUNC);
	QString temp = QStringListIterator_dereference(arg0);
	{
		QString* copy = new QString();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, _1QStringListIterator_1dereference_FUNC);
	return rc;
}
#endif

#ifndef NO__1QStringListIterator_1equals
JNIEXPORT jboolean JNICALL KDE_NATIVE(_1QStringListIterator_1equals)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	KDE_NATIVE_ENTER(env, that, _1QStringListIterator_1equals_FUNC);
	rc = (jboolean)QStringListIterator_equals(arg0, arg1);
	KDE_NATIVE_EXIT(env, that, _1QStringListIterator_1equals_FUNC);
	return rc;
}
#endif

#ifndef NO__1QStringListIterator_1increment
JNIEXPORT void JNICALL KDE_NATIVE(_1QStringListIterator_1increment)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, _1QStringListIterator_1increment_FUNC);
	QStringListIterator_increment(arg0);
	KDE_NATIVE_EXIT(env, that, _1QStringListIterator_1increment_FUNC);
}
#endif

#ifndef NO__1QStringList_1begin
JNIEXPORT jint JNICALL KDE_NATIVE(_1QStringList_1begin)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1QStringList_1begin_FUNC);
	QValueListIterator<QString> temp = ((QStringList *)arg0)->begin();
	{
		QValueListIterator<QString>* copy = new QValueListIterator<QString>();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, _1QStringList_1begin_FUNC);
	return rc;
}
#endif

#ifndef NO__1QStringList_1delete
JNIEXPORT void JNICALL KDE_NATIVE(_1QStringList_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, _1QStringList_1delete_FUNC);
	delete (QStringList*)arg0;
	KDE_NATIVE_EXIT(env, that, _1QStringList_1delete_FUNC);
}
#endif

#ifndef NO__1QStringList_1end
JNIEXPORT jint JNICALL KDE_NATIVE(_1QStringList_1end)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1QStringList_1end_FUNC);
	QValueListIterator<QString> temp = ((QStringList *)arg0)->end();
	{
		QValueListIterator<QString>* copy = new QValueListIterator<QString>();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, _1QStringList_1end_FUNC);
	return rc;
}
#endif

#ifndef NO__1QString_1delete
JNIEXPORT void JNICALL KDE_NATIVE(_1QString_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, _1QString_1delete_FUNC);
	delete (QString *)arg0;
	KDE_NATIVE_EXIT(env, that, _1QString_1delete_FUNC);
}
#endif

#ifndef NO__1QString_1equals
JNIEXPORT jboolean JNICALL KDE_NATIVE(_1QString_1equals)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	KDE_NATIVE_ENTER(env, that, _1QString_1equals_FUNC);
	rc = (jboolean)QString_equals(arg0, arg1);
	KDE_NATIVE_EXIT(env, that, _1QString_1equals_FUNC);
	return rc;
}
#endif

#ifndef NO__1QString_1new
JNIEXPORT jint JNICALL KDE_NATIVE(_1QString_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1QString_1new_FUNC);
	if (arg0) if ((lparg0 = env->GetByteArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)new QString((char *)lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	KDE_NATIVE_EXIT(env, that, _1QString_1new_FUNC);
	return rc;
}
#endif

#ifndef NO__1QString_1utf8
JNIEXPORT jint JNICALL KDE_NATIVE(_1QString_1utf8)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1QString_1utf8_FUNC);
	QCString temp = ((QString *)arg0)->utf8();
	{
		QCString* copy = new QCString();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, _1QString_1utf8_FUNC);
	return rc;
}
#endif

#ifndef NO__1XFreePixmap
JNIEXPORT void JNICALL KDE_NATIVE(_1XFreePixmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	KDE_NATIVE_ENTER(env, that, _1XFreePixmap_FUNC);
	XFreePixmap((Display *)arg0, (Pixmap)arg1);
	KDE_NATIVE_EXIT(env, that, _1XFreePixmap_FUNC);
}
#endif

#ifndef NO__1XpmReadFileToPixmap
JNIEXPORT jint JNICALL KDE_NATIVE(_1XpmReadFileToPixmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3, jintArray arg4, jint arg5)
{
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1XpmReadFileToPixmap_FUNC);
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetIntArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = env->GetIntArrayElements(arg4, NULL)) == NULL) goto fail;
/*
	rc = (jint)XpmReadFileToPixmap((Display *)arg0, (Window)arg1, (char *)lparg2, (Pixmap *)lparg3, (Pixmap *)lparg4, (XpmAttributes *)arg5);
*/
	{
		static int initialized = 0;
		static void *handle = NULL;
		typedef jint (*FPTR)(Display *, Window, char *, Pixmap *, Pixmap *, XpmAttributes *);
		static FPTR fptr;
		rc = 0;
		if (!initialized) {
			if (!handle) handle = dlopen(XpmReadFileToPixmap_LIB, RTLD_LAZY);
			if (handle) fptr = (FPTR)dlsym(handle, "XpmReadFileToPixmap");
			initialized = 1;
		}
		if (fptr) {
			rc = (jint)(*fptr)((Display *)arg0, (Window)arg1, (char *)lparg2, (Pixmap *)lparg3, (Pixmap *)lparg4, (XpmAttributes *)arg5);
		}
	}
fail:
	if (arg4 && lparg4) env->ReleaseIntArrayElements(arg4, lparg4, 0);
	if (arg3 && lparg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	KDE_NATIVE_EXIT(env, that, _1XpmReadFileToPixmap_FUNC);
	return rc;
}
#endif

#ifndef NO__1free
JNIEXPORT void JNICALL KDE_NATIVE(_1free)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, _1free_FUNC);
	free((void *)arg0);
	KDE_NATIVE_EXIT(env, that, _1free_FUNC);
}
#endif

#ifndef NO__1malloc
JNIEXPORT jint JNICALL KDE_NATIVE(_1malloc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1malloc_FUNC);
	rc = (jint)malloc(arg0);
	KDE_NATIVE_EXIT(env, that, _1malloc_FUNC);
	return rc;
}
#endif

#ifndef NO__1sigaction
JNIEXPORT jint JNICALL KDE_NATIVE(_1sigaction)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1sigaction_FUNC);
	if (arg1) if ((lparg1 = env->GetByteArrayElements(arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)sigaction(arg0, (struct sigaction *)lparg1, (struct sigaction *)lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	if (arg1 && lparg1) env->ReleaseByteArrayElements(arg1, lparg1, 0);
	KDE_NATIVE_EXIT(env, that, _1sigaction_FUNC);
	return rc;
}
#endif

#ifndef NO__1sigaction_1sizeof
JNIEXPORT jint JNICALL KDE_NATIVE(_1sigaction_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, _1sigaction_1sizeof_FUNC);
	rc = (jint)sigaction_sizeof();
	KDE_NATIVE_EXIT(env, that, _1sigaction_1sizeof_FUNC);
	return rc;
}
#endif

}
