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

#include "swt.h"
#include "kde_structs.h"
#include "kde_stats.h"

extern "C" {

#define KDE_NATIVE(func) Java_org_eclipse_swt_internal_kde_KDE_##func

#ifndef NO_KApplication_1new
JNIEXPORT jint JNICALL KDE_NATIVE(KApplication_1new)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3, jboolean arg4)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, KApplication_1new_FUNC);
	rc = (jint)new KApplication(arg0, (char **)arg1, *(QCString *)arg2, (bool)arg3, (bool)arg4);
	KDE_NATIVE_EXIT(env, that, KApplication_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_KGlobal_1iconLoader
JNIEXPORT jint JNICALL KDE_NATIVE(KGlobal_1iconLoader)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, KGlobal_1iconLoader_FUNC);
	rc = (jint)KGlobal::iconLoader();
	KDE_NATIVE_EXIT(env, that, KGlobal_1iconLoader_FUNC);
	return rc;
}
#endif

#ifndef NO_KIconLoader_1iconPath
JNIEXPORT jint JNICALL KDE_NATIVE(KIconLoader_1iconPath)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, KIconLoader_1iconPath_FUNC);
	QString temp = ((KIconLoader *)arg0)->iconPath(*(QString *)arg1, arg2, (bool)arg3);
	{
		QString* copy = new QString();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, KIconLoader_1iconPath_FUNC);
	return rc;
}
#endif

#ifndef NO_KMimeTypeListIterator_1delete
JNIEXPORT void JNICALL KDE_NATIVE(KMimeTypeListIterator_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, KMimeTypeListIterator_1delete_FUNC);
	delete (QValueListIterator<KMimeType::Ptr> *)arg0;
	KDE_NATIVE_EXIT(env, that, KMimeTypeListIterator_1delete_FUNC);
}
#endif

#ifndef NO_KMimeTypeListIterator_1dereference
JNIEXPORT jint JNICALL KDE_NATIVE(KMimeTypeListIterator_1dereference)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, KMimeTypeListIterator_1dereference_FUNC);
	KSharedPtr<KMimeType> temp = KMimeTypeListIterator_dereference(arg0);
	{
		KSharedPtr<KMimeType>* copy = new KSharedPtr<KMimeType>();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, KMimeTypeListIterator_1dereference_FUNC);
	return rc;
}
#endif

#ifndef NO_KMimeTypeListIterator_1equals
JNIEXPORT jboolean JNICALL KDE_NATIVE(KMimeTypeListIterator_1equals)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	KDE_NATIVE_ENTER(env, that, KMimeTypeListIterator_1equals_FUNC);
	rc = (jboolean)KMimeTypeListIterator_equals(arg0, arg1);
	KDE_NATIVE_EXIT(env, that, KMimeTypeListIterator_1equals_FUNC);
	return rc;
}
#endif

#ifndef NO_KMimeTypeListIterator_1increment
JNIEXPORT void JNICALL KDE_NATIVE(KMimeTypeListIterator_1increment)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, KMimeTypeListIterator_1increment_FUNC);
	KMimeTypeListIterator_increment(arg0);
	KDE_NATIVE_EXIT(env, that, KMimeTypeListIterator_1increment_FUNC);
}
#endif

#ifndef NO_KMimeTypeList_1begin
JNIEXPORT jint JNICALL KDE_NATIVE(KMimeTypeList_1begin)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, KMimeTypeList_1begin_FUNC);
	QValueListIterator<KMimeType::Ptr> temp = ((KMimeType::List *)arg0)->begin();
	{
		QValueListIterator<KMimeType::Ptr>* copy = new QValueListIterator<KMimeType::Ptr>();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, KMimeTypeList_1begin_FUNC);
	return rc;
}
#endif

#ifndef NO_KMimeTypeList_1delete
JNIEXPORT void JNICALL KDE_NATIVE(KMimeTypeList_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, KMimeTypeList_1delete_FUNC);
	delete (KMimeType::List *)arg0;
	KDE_NATIVE_EXIT(env, that, KMimeTypeList_1delete_FUNC);
}
#endif

#ifndef NO_KMimeTypeList_1end
JNIEXPORT jint JNICALL KDE_NATIVE(KMimeTypeList_1end)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, KMimeTypeList_1end_FUNC);
	QValueListIterator<KMimeType::Ptr> temp = ((KMimeType::List *)arg0)->end();
	{
		QValueListIterator<KMimeType::Ptr>* copy = new QValueListIterator<KMimeType::Ptr>();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, KMimeTypeList_1end_FUNC);
	return rc;
}
#endif

#ifndef NO_KMimeType_1allMimeTypes
JNIEXPORT jint JNICALL KDE_NATIVE(KMimeType_1allMimeTypes)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, KMimeType_1allMimeTypes_FUNC);
	KMimeType::List temp = KMimeType::allMimeTypes();
	{
		KMimeType::List* copy = new KMimeType::List();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, KMimeType_1allMimeTypes_FUNC);
	return rc;
}
#endif

#ifndef NO_KMimeType_1delete
JNIEXPORT void JNICALL KDE_NATIVE(KMimeType_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, KMimeType_1delete_FUNC);
	delete (KSharedPtr<KMimeType> *)arg0;
	KDE_NATIVE_EXIT(env, that, KMimeType_1delete_FUNC);
}
#endif

#ifndef NO_KMimeType_1icon
JNIEXPORT jint JNICALL KDE_NATIVE(KMimeType_1icon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, KMimeType_1icon_FUNC);
	QString temp = (*(KSharedPtr<KMimeType> *)arg0)->icon((const QString&)arg1, (bool)arg2);
	{
		QString* copy = new QString();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, KMimeType_1icon_FUNC);
	return rc;
}
#endif

#ifndef NO_KMimeType_1mimeType
JNIEXPORT jint JNICALL KDE_NATIVE(KMimeType_1mimeType)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, KMimeType_1mimeType_FUNC);
	KSharedPtr<KMimeType> temp = KMimeType::mimeType(*(QString *)arg0);
	{
		KSharedPtr<KMimeType>* copy = new KSharedPtr<KMimeType>();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, KMimeType_1mimeType_FUNC);
	return rc;
}
#endif

#ifndef NO_KMimeType_1name
JNIEXPORT jint JNICALL KDE_NATIVE(KMimeType_1name)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, KMimeType_1name_FUNC);
	QString temp = (*(KSharedPtr<KMimeType> *)arg0)->name();
	{
		QString* copy = new QString();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, KMimeType_1name_FUNC);
	return rc;
}
#endif

#ifndef NO_KMimeType_1offers
JNIEXPORT jint JNICALL KDE_NATIVE(KMimeType_1offers)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, KMimeType_1offers_FUNC);
	KService::List temp = KMimeType::offers(*(QString *)arg0);
	{
		KService::List* copy = new KService::List();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, KMimeType_1offers_FUNC);
	return rc;
}
#endif

#ifndef NO_KMimeType_1patterns
JNIEXPORT jint JNICALL KDE_NATIVE(KMimeType_1patterns)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, KMimeType_1patterns_FUNC);
	QStringList temp = (*(KSharedPtr<KMimeType> *)arg0)->patterns();
	{
		QStringList* copy = new QStringList();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, KMimeType_1patterns_FUNC);
	return rc;
}
#endif

#ifndef NO_KRun_1runURL
JNIEXPORT jint JNICALL KDE_NATIVE(KRun_1runURL)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, KRun_1runURL_FUNC);
	rc = (jint)KRun::runURL(*(KURL *)arg0, *(QString *)arg1);
	KDE_NATIVE_EXIT(env, that, KRun_1runURL_FUNC);
	return rc;
}
#endif

#ifndef NO_KServiceList_1delete
JNIEXPORT void JNICALL KDE_NATIVE(KServiceList_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, KServiceList_1delete_FUNC);
	delete (KService::List*)arg0;
	KDE_NATIVE_EXIT(env, that, KServiceList_1delete_FUNC);
}
#endif

#ifndef NO_KURL_1delete
JNIEXPORT void JNICALL KDE_NATIVE(KURL_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, KURL_1delete_FUNC);
	delete (KURL*)arg0;
	KDE_NATIVE_EXIT(env, that, KURL_1delete_FUNC);
}
#endif

#ifndef NO_KURL_1new
JNIEXPORT jint JNICALL KDE_NATIVE(KURL_1new)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, KURL_1new_FUNC);
	rc = (jint)new KURL(*(QString *)arg0);
	KDE_NATIVE_EXIT(env, that, KURL_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_QCString_1data
JNIEXPORT jint JNICALL KDE_NATIVE(QCString_1data)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, QCString_1data_FUNC);
	rc = (jint)((QCString *)arg0)->data();
	KDE_NATIVE_EXIT(env, that, QCString_1data_FUNC);
	return rc;
}
#endif

#ifndef NO_QCString_1delete
JNIEXPORT void JNICALL KDE_NATIVE(QCString_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, QCString_1delete_FUNC);
	delete (QCString *)arg0;
	KDE_NATIVE_EXIT(env, that, QCString_1delete_FUNC);
}
#endif

#ifndef NO_QCString_1new
JNIEXPORT jint JNICALL KDE_NATIVE(QCString_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, QCString_1new_FUNC);
	if (arg0) if ((lparg0 = env->GetByteArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)new QCString((char *)lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	KDE_NATIVE_EXIT(env, that, QCString_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_QStringListIterator_1delete
JNIEXPORT void JNICALL KDE_NATIVE(QStringListIterator_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, QStringListIterator_1delete_FUNC);
	delete (QValueListIterator<QString> *)arg0;
	KDE_NATIVE_EXIT(env, that, QStringListIterator_1delete_FUNC);
}
#endif

#ifndef NO_QStringListIterator_1dereference
JNIEXPORT jint JNICALL KDE_NATIVE(QStringListIterator_1dereference)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, QStringListIterator_1dereference_FUNC);
	QString temp = QStringListIterator_dereference(arg0);
	{
		QString* copy = new QString();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, QStringListIterator_1dereference_FUNC);
	return rc;
}
#endif

#ifndef NO_QStringListIterator_1equals
JNIEXPORT jboolean JNICALL KDE_NATIVE(QStringListIterator_1equals)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	KDE_NATIVE_ENTER(env, that, QStringListIterator_1equals_FUNC);
	rc = (jboolean)QStringListIterator_equals(arg0, arg1);
	KDE_NATIVE_EXIT(env, that, QStringListIterator_1equals_FUNC);
	return rc;
}
#endif

#ifndef NO_QStringListIterator_1increment
JNIEXPORT void JNICALL KDE_NATIVE(QStringListIterator_1increment)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, QStringListIterator_1increment_FUNC);
	QStringListIterator_increment(arg0);
	KDE_NATIVE_EXIT(env, that, QStringListIterator_1increment_FUNC);
}
#endif

#ifndef NO_QStringList_1begin
JNIEXPORT jint JNICALL KDE_NATIVE(QStringList_1begin)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, QStringList_1begin_FUNC);
	QValueListIterator<QString> temp = ((QStringList *)arg0)->begin();
	{
		QValueListIterator<QString>* copy = new QValueListIterator<QString>();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, QStringList_1begin_FUNC);
	return rc;
}
#endif

#ifndef NO_QStringList_1delete
JNIEXPORT void JNICALL KDE_NATIVE(QStringList_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, QStringList_1delete_FUNC);
	delete (QStringList*)arg0;
	KDE_NATIVE_EXIT(env, that, QStringList_1delete_FUNC);
}
#endif

#ifndef NO_QStringList_1end
JNIEXPORT jint JNICALL KDE_NATIVE(QStringList_1end)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, QStringList_1end_FUNC);
	QValueListIterator<QString> temp = ((QStringList *)arg0)->end();
	{
		QValueListIterator<QString>* copy = new QValueListIterator<QString>();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, QStringList_1end_FUNC);
	return rc;
}
#endif

#ifndef NO_QString_1delete
JNIEXPORT void JNICALL KDE_NATIVE(QString_1delete)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, QString_1delete_FUNC);
	delete (QString *)arg0;
	KDE_NATIVE_EXIT(env, that, QString_1delete_FUNC);
}
#endif

#ifndef NO_QString_1equals
JNIEXPORT jboolean JNICALL KDE_NATIVE(QString_1equals)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	KDE_NATIVE_ENTER(env, that, QString_1equals_FUNC);
	rc = (jboolean)QString_equals(arg0, arg1);
	KDE_NATIVE_EXIT(env, that, QString_1equals_FUNC);
	return rc;
}
#endif

#ifndef NO_QString_1new
JNIEXPORT jint JNICALL KDE_NATIVE(QString_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, QString_1new_FUNC);
	if (arg0) if ((lparg0 = env->GetByteArrayElements(arg0, NULL)) == NULL) goto fail;
	rc = (jint)new QString((char *)lparg0);
fail:
	if (arg0 && lparg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	KDE_NATIVE_EXIT(env, that, QString_1new_FUNC);
	return rc;
}
#endif

#ifndef NO_QString_1utf8
JNIEXPORT jint JNICALL KDE_NATIVE(QString_1utf8)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, QString_1utf8_FUNC);
	QCString temp = ((QString *)arg0)->utf8();
	{
		QCString* copy = new QCString();
		*copy = temp;
		rc = (jint)copy;
	}
	KDE_NATIVE_EXIT(env, that, QString_1utf8_FUNC);
	return rc;
}
#endif

#ifndef NO_XFreePixmap
JNIEXPORT void JNICALL KDE_NATIVE(XFreePixmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	KDE_NATIVE_ENTER(env, that, XFreePixmap_FUNC);
	XFreePixmap((Display *)arg0, (Pixmap)arg1);
	KDE_NATIVE_EXIT(env, that, XFreePixmap_FUNC);
}
#endif

#ifndef NO_XpmReadFileToPixmap
JNIEXPORT jint JNICALL KDE_NATIVE(XpmReadFileToPixmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3, jintArray arg4, jint arg5)
{
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, XpmReadFileToPixmap_FUNC);
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
	KDE_NATIVE_EXIT(env, that, XpmReadFileToPixmap_FUNC);
	return rc;
}
#endif

#ifndef NO_free
JNIEXPORT void JNICALL KDE_NATIVE(free)
	(JNIEnv *env, jclass that, jint arg0)
{
	KDE_NATIVE_ENTER(env, that, free_FUNC);
	free((void *)arg0);
	KDE_NATIVE_EXIT(env, that, free_FUNC);
}
#endif

#ifndef NO_malloc
JNIEXPORT jint JNICALL KDE_NATIVE(malloc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, malloc_FUNC);
	rc = (jint)malloc(arg0);
	KDE_NATIVE_EXIT(env, that, malloc_FUNC);
	return rc;
}
#endif

#ifndef NO_sigaction
JNIEXPORT jint JNICALL KDE_NATIVE(sigaction)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, sigaction_FUNC);
	if (arg1) if ((lparg1 = env->GetByteArrayElements(arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = env->GetByteArrayElements(arg2, NULL)) == NULL) goto fail;
	rc = (jint)sigaction(arg0, (struct sigaction *)lparg1, (struct sigaction *)lparg2);
fail:
	if (arg2 && lparg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	if (arg1 && lparg1) env->ReleaseByteArrayElements(arg1, lparg1, 0);
	KDE_NATIVE_EXIT(env, that, sigaction_FUNC);
	return rc;
}
#endif

#ifndef NO_sigaction_1sizeof
JNIEXPORT jint JNICALL KDE_NATIVE(sigaction_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	KDE_NATIVE_ENTER(env, that, sigaction_1sizeof_FUNC);
	rc = (jint)sigaction_sizeof();
	KDE_NATIVE_EXIT(env, that, sigaction_1sizeof_FUNC);
	return rc;
}
#endif

}
