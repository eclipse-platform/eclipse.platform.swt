/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * SWT KDE natives implementation.
 */ 

/* #define PRINT_FAILED_RCODES */
#define NDEBUG 

#include <stdio.h>
#include <signal.h>
#include "swt.h"

#include <kapp.h>
#include <kiconloader.h>
#include <kmimetype.h>
#include <krun.h>
#include <qstring.h>

extern "C" {

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KApplication_1new
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KApplication_1new
  (JNIEnv *env, jclass that, int appName)
{
	int myArgc = 1;
	char* myArgv[2] = { "SWT", 0 };  // KApplication requires a NULL terminated list
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KApplication_1new\n");
#endif
	QCString qcString = *((QCString*) appName);
	
	// NOTE: When a KDE application is initialized, it installs its own
	// SIGSEGV signal handler so that it can pop up a dialogue box and
	// display an error message should SIGSEGV occur. After the dialogue
	// box is closed, it terminates the program. The Hursley Java VM (on Linux)
	// happens to catch SIGSEGV signals so that it can throw a null pointer
	// exception. Thus when KDE is initialized, the Java try ... catch 
	// mechanism for null pointers does not work. Eclipse code relies upon
	// this try ... catch feature.
	//
	// The solution is to obtain the Java VM's signal handler before initializing
	// KDE and to reinstall that handler after the initialization. The method
	// sigaction() must be used instead of signal() because it returns more
	// information on how to handle the signal.
	
	// Obtain the current signal handling logic for SIGSEGV.
	struct sigaction prev;
	sigaction( SIGSEGV, NULL, &prev );
	
	// Initialize KDE, which installs its own signal handler.
	KApplication* app = new KApplication(myArgc, myArgv, qcString, 0, 0);
	
	// Replace the Java VM signal handler.
	sigaction( SIGSEGV, &prev, NULL );
	
	return (jint) app;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KGlobal_1iconLoader
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KGlobal_1iconLoader
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KGlobal_1iconLoader\n");
#endif
	return (jint) KGlobal::iconLoader();
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KIconLoader_1iconPath
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KIconLoader_1iconPath
  (JNIEnv *env, jclass that, jint kloader, jint iconQString, jint iconType, jint canReturnNull)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KIconLoader_1iconPath\n");
#endif
	KIconLoader* loader = (KIconLoader*) kloader;
	QString iconName = *((QString*) iconQString);
	QString iconPath = loader->iconPath(iconName, iconType, canReturnNull);
	if (iconPath == 0) return 0;
	QString* answer = new QString();
	*answer = iconPath;
	return (jint) answer;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KMimeType_1mimeType
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KMimeType_1mimeType
  (JNIEnv *env, jclass that, jint mimeTypeName)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KMimeType_1mimeType\n");
#endif
  	KSharedPtr<KMimeType>* mimeType = new KSharedPtr<KMimeType>();
  	QString qMimeType = *((QString*) mimeTypeName);
  	*mimeType = KMimeType::mimeType( qMimeType );
  	return (jint) mimeType;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KMimeType_1icon
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KMimeType_1icon
  (JNIEnv *env, jclass that, jint mimeTypePtr, jint unused1, jint unused2)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KMimeType_1icon\n");
#endif
	KSharedPtr<KMimeType> mimeType = *((KSharedPtr<KMimeType>*) mimeTypePtr);
	QString* answer = new QString();
	*answer = mimeType->icon((const QString&) NULL, 0);
	return (jint) answer;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KMimeType_1name
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KMimeType_1name
  (JNIEnv *env, jclass that, jint mimeTypePtr)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KMimeType_1name\n");
#endif
	KSharedPtr<KMimeType> mimeType = *((KSharedPtr<KMimeType>*) mimeTypePtr);
	QString* name = new QString();
	*name = mimeType->name(); 
	return (jint) name;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KMimeType_1patterns
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KMimeType_1patterns
  (JNIEnv *env, jclass that, jint mimeTypePtr)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KMimeType_1patterns\n");
#endif
	KSharedPtr<KMimeType> mimeType = *((KSharedPtr<KMimeType>*) mimeTypePtr);
	QStringList* patternList = new QStringList();
	*patternList = mimeType->patterns(); 
	return (jint) patternList;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KMimeType_1offers
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KMimeType_1offers
  (JNIEnv *env, jclass that, jint mimeTypeName)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KMimeType_1offers\n");
#endif
	QString qMimeType = *((QString*) mimeTypeName);
	KService::List* serviceList = new KService::List();
	*serviceList = KMimeType::offers( qMimeType ); 
	return (jint) serviceList;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KMimeType_1allMimeTypes
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KMimeType_1allMimeTypes
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KMimeType_1allMimeTypes\n");
#endif
 	KMimeType::List* mimeTypeList = new KMimeType::List();
	*mimeTypeList = KMimeType::allMimeTypes();
  	return (jint) mimeTypeList;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KMimeTypeList_1begin
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KMimeTypeList_1begin
  (JNIEnv *env, jclass that, jint mimeTypeList)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KMimeTypeList_1begin\n");
#endif
	KMimeType::List *list= (KMimeType::List*) mimeTypeList;
	QValueListIterator<KMimeType::Ptr>* iterator = new QValueListIterator<KMimeType::Ptr>();
  	*iterator = list->begin();
	return (jint) iterator;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KMimeTypeList_1delete
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_KDE_KMimeTypeList_1delete
  (JNIEnv *env, jclass that, jint mimeTypeList)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KMimeTypeList_1delete\n");
#endif
	delete (KMimeType::List*) mimeTypeList;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KMimeTypeList_1end
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KMimeTypeList_1end
  (JNIEnv *env, jclass that, jint mimeTypeList)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KMimeTypeList_1end\n");
#endif
	KMimeType::List *list = (KMimeType::List*) mimeTypeList;
	QValueListIterator<KMimeType::Ptr>* iterator = new QValueListIterator<KMimeType::Ptr>();
  	*iterator = list->end();
	return (jint) iterator;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KMimeTypeListIterator_1delete
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_KDE_KMimeTypeListIterator_1delete
  (JNIEnv *env, jclass that, jint iterator)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KMimeTypeListIterator_1delete\n");
#endif
	delete (QValueListIterator<KMimeType::Ptr>*) iterator;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KMimeTypeListIterator_1dereference
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KMimeTypeListIterator_1dereference
  (JNIEnv *env, jclass that, jint iterator)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KMimeTypeListIterator_1dereference\n");
#endif
	KSharedPtr<KMimeType>* mimeType = new KSharedPtr<KMimeType>();
	*mimeType = *(*((QValueListIterator<KMimeType::Ptr>*) iterator));
	return (jint) mimeType;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KMimeTypeListIterator_1equals
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KMimeTypeListIterator_1equals
  (JNIEnv *env, jclass that, jint iterator, jint iterator2)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KMimeTypeListIterator_1equals\n");
#endif
	return *((QValueListIterator<KMimeType::Ptr>*) iterator) == 
		   *((QValueListIterator<KMimeType::Ptr>*) iterator2);
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KMimeTypeListIterator_1increment
 * Signature: (I)I
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_KDE_KMimeTypeListIterator_1increment
  (JNIEnv *env, jclass that, jint iterator)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KMimeTypeListIterator_1increment\n");
#endif
	++(*((QValueListIterator<KMimeType::Ptr>*) iterator));
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QStringList_1begin
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_QStringList_1begin
  (JNIEnv *env, jclass that, jint qstringList)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QStringList_1begin\n");
#endif
	QStringList *list= (QStringList*) qstringList;
	QValueListIterator<QString>* iterator = new QValueListIterator<QString>();
  	*iterator = list->begin();
	return (jint) iterator;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QStringList_1delete
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_KDE_QStringList_1delete
  (JNIEnv *env, jclass that, jint qstringList)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QStringList_1delete\n");
#endif
	delete (QStringList*) qstringList;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QStringList_1end
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_QStringList_1end
  (JNIEnv *env, jclass that, jint qstringList)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QStringList_1end\n");
#endif
	QStringList *list = (QStringList*) qstringList;
	QValueListIterator<QString>* iterator = new QValueListIterator<QString>();
  	*iterator = list->end();
	return (jint) iterator;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QStringListIterator_1delete
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_KDE_QStringListIterator_1delete
  (JNIEnv *env, jclass that, jint iterator)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QStringListIterator_1delete\n");
#endif
	delete (QValueListIterator<QString>*) iterator;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QStringListIterator_1dereference
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_QStringListIterator_1dereference
  (JNIEnv *env, jclass that, jint iterator)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QStringListIterator_1dereference\n");
#endif
	QString* qstring = new QString();
	*qstring = *(*((QValueListIterator<QString>*) iterator));
	return (jint) qstring;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QStringListIterator_1equals
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_QStringListIterator_1equals
  (JNIEnv *env, jclass that, jint iterator, jint iterator2)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QStringListIterator_1equals\n");
#endif
	return *((QValueListIterator<QString>*) iterator) == 
		   *((QValueListIterator<QString>*) iterator2);
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QStringListIterator_1increment
 * Signature: (I)I
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_KDE_QStringListIterator_1increment
  (JNIEnv *env, jclass that, jint iterator)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QStringListIterator_1increment\n");
#endif
	++(*((QValueListIterator<QString>*) iterator));
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KURL_1new
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KURL_1new
  (JNIEnv *env, jclass that, jint qURLString)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KURL_1new\n");
#endif
	QString urlString = *((QString*) qURLString);
	return (jint) new KURL(urlString);
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KURL_1delete
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_KDE_KURL_1delete
  (JNIEnv *env, jclass that, jint url)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KURL_1delete\n");
#endif
	delete (KURL*) url;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KRun_1runURL
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KRun_1runURL
  (JNIEnv *env, jclass that, jint kurl, jint mimeTypeName)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KRun_1runURL\n");
#endif
	KURL url = *((KURL*) kurl);
  	QString qMimeType = *((QString*) mimeTypeName);
	return (jint) KRun::runURL( url, qMimeType );
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KServiceList_1delete
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_KDE_KServiceList_1delete
  (JNIEnv *env, jclass that, jint serviceList)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KServiceList_1delete\n");
#endif
	delete (KService::List*) serviceList;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QCString_1data
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_QCString_1data
  (JNIEnv *env, jclass that, jint qcString)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QCString_1data\n");
#endif
	return (jint) ((QCString*) qcString)->data();
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QCString_1delete
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_KDE_QCString_1delete
  (JNIEnv *env, jclass that, jint qcString)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QCString_1delete\n");
#endif
	delete (QCString*) qcString;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QCString_1new
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_QCString_1new
  (JNIEnv *env, jclass that, jbyteArray text)
{
	jbyte *text1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QCString_1new\n");
#endif
	if (text) text1 = env->GetByteArrayElements(text, NULL);
	QCString* qcString = new QCString((char*) text1);
  	if (text) env->ReleaseByteArrayElements(text, text1, 0);	
	return (jint) qcString;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QString_1delete
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_KDE_QString_1delete
  (JNIEnv *env, jclass that, jint qString)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QString_1delete\n");
#endif
	delete (QString*) qString;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QString_1equals
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_QString_1equals
  (JNIEnv *env, jclass that, jint qString, jint qString2)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QString_1equals\n");
#endif
	return *((QString*) qString) == *((QString*) qString2);
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QString_1new
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_QString_1new
  (JNIEnv *env, jclass that, jbyteArray text)
{
	jbyte *text1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QString_1new\n");
#endif
	if (text) text1 = env->GetByteArrayElements(text, NULL);
	QString* qString = new QString((char*) text1);
  	if (text) env->ReleaseByteArrayElements(text, text1, 0);	
	return (jint) qString;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QString_1utf8
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_QString_1utf8
  (JNIEnv *env, jclass that, jint qString)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QString_1utf8\n");
#endif
	QString string = *((QString*) qString);
	QCString* qcString = new QCString();
	*qcString = string.utf8();
	return (jint) qcString;
}


} /* extern "C" */
