/*
 * (c) Copyright IBM Corp., 2000, 2001
 * All Rights Reserved.
 */

/**
 * SWT KDE natives implementation.
 */ 

/* #define PRINT_FAILED_RCODES */
#define NDEBUG 

#include <stdio.h>
#include "jni.h"

#include <kapp.h>
#include <kservice.h>
#include <kmimetype.h>
#include <kuserprofile.h>
#include <kurl.h>
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
	int myArgc = 0;
	char* myArgv[1];
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KApplication_1new\n");
#endif
	QCString qcString = *((QCString*) appName);
	KApplication* app = new KApplication(myArgc, myArgv, qcString);
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
  (JNIEnv *env, jclass that, jint receiver, jint iconQString, jint iconType, jint canReturnNull)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KIconLoader_1iconPath\n");
#endif
	KIconLoader* loader = (KIconLoader*) receiver;
	QString iconName = *((QString*) iconQString);
	QString iconPath = loader->iconPath(iconName, iconType, canReturnNull);
	if (iconPath == 0) return 0;
	QString* answer = new QString();
	*answer = iconPath;
	return (jint) answer;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KMimeType_1findByURL
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KMimeType_1findByURL
  (JNIEnv *env, jclass that, jint kurl)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KMimeType_1findByURL\n");
#endif
	KURL url = *((KURL*) kurl);
  	KSharedPtr<KMimeType>* mimeType = new KSharedPtr<KMimeType>();
  	*mimeType = KMimeType::findByURL(url);
  	return (jint) mimeType;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KMimeType_1icon
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KMimeType_1icon
  (JNIEnv *env, jclass that, jint receiver, jint unused1, jint unused2)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KMimeType_1icon\n");
#endif
	KSharedPtr<KMimeType> mimeType = *((KSharedPtr<KMimeType>*) receiver);
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
  (JNIEnv *env, jclass that, jint receiver)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KMimeType_1name\n");
#endif
	KSharedPtr<KMimeType> mimeType = *((KSharedPtr<KMimeType>*) receiver);
	QString* answer = new QString();
	*answer = mimeType->name(); 
	return (jint) answer;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KService_1allServices
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KService_1allServices
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KService_1allServices\n");
#endif
	KService::List* pointer = new KService::List();
	*pointer = KService::allServices();
	return (jint) pointer;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KService_1exec
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KService_1exec
  (JNIEnv *env, jclass that, jint receiver)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KService_1exec\n");
#endif
	KSharedPtr<KService> service = *((KSharedPtr<KService>*) receiver);
	QString* answer = new QString();
	*answer = service->exec();
	return (jint) answer;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KService_1icon
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KService_1icon
  (JNIEnv *env, jclass that, jint receiver)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KService_1icon\n");
#endif
	KSharedPtr<KService> service = *((KSharedPtr<KService>*) receiver);
	QString* answer = new QString();
	*answer = service->icon();
	return (jint) answer;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KService_1name
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KService_1name
  (JNIEnv *env, jclass that, jint receiver)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KService_1name\n");
#endif
	KSharedPtr<KService> service = *((KSharedPtr<KService>*) receiver);
	QString* answer = new QString();
	*answer = service->name();
	return (jint) answer;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KService_1serviceByName
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KService_1serviceByName
  (JNIEnv *env, jclass that, jint serviceName)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KService_1serviceByName\n");
#endif
	QString* name = (QString*) serviceName;
	KSharedPtr<KService> service = KService::serviceByName(*name);
	if (service == 0) return 0;
	KSharedPtr<KService>* pointer = new KSharedPtr<KService>();
	*pointer = service;
	return (jint) pointer;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KService_1type
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KService_1type
  (JNIEnv *env, jclass that, jint receiver)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KService_1type\n");
#endif
	KSharedPtr<KService> service = *((KSharedPtr<KService>*) receiver);
	QString* answer = new QString();
	*answer = service->type();
	return (jint) answer;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KServiceTypeProfile_1preferredService
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KServiceTypeProfile_1preferredService
  (JNIEnv *env, jclass that, jint mimeTypeQString, jint needApp)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KServiceTypeProfile_1preferredService\n");
#endif
	QString mimeTypeName = *((QString*) mimeTypeQString);
	KSharedPtr<KService> service = KServiceTypeProfile::preferredService(mimeTypeName, needApp);
	if (service == 0) return 0;
	KSharedPtr<KService>* pointer = new KSharedPtr<KService>();
	*pointer = service;
	return (jint) pointer;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KURL_1delete
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_KDE_KURL_1delete
  (JNIEnv *env, jclass that, jint receiver)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KURL_1delete\n");
#endif
	delete (KURL*) receiver;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KURL_1new
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KURL_1new
  (JNIEnv *env, jclass that, jint qString)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KURL_1new\n");
#endif
	QString urlString = *((QString*) qString);
	return (jint) new KURL(urlString);
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KServiceList_1begin
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KServiceList_1begin
  (JNIEnv *env, jclass that, jint receiver)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KServiceList_1begin\n");
#endif
	KService::List *list= (KService::List*) receiver;
	QValueListConstIterator<KService::Ptr>* beginning = new QValueListConstIterator<KService::Ptr>();
  	*beginning = list->begin();
	return (jint) beginning;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KServiceList_1delete
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_KDE_KServiceList_1delete
  (JNIEnv *env, jclass that, jint receiver)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KServiceList_1delete\n");
#endif
	delete (KService::List*) receiver;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KServiceList_1end
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KServiceList_1end
  (JNIEnv *env, jclass that, jint receiver)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KServiceList_1end\n");
#endif
	KService::List *list = (KService::List*) receiver;
	QValueListConstIterator<KService::Ptr>* end = new QValueListConstIterator<KService::Ptr>();
  	*end = list->end();
	return (jint) end;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QCString_1data
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_QCString_1data
  (JNIEnv *env, jclass that, jint receiver)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QCString_1data\n");
#endif
	return (jint) ((QCString*) receiver)->data();
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QCString_1delete
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_KDE_QCString_1delete
  (JNIEnv *env, jclass that, jint receiver)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QCString_1delete\n");
#endif
	delete (QCString*) receiver;
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
  (JNIEnv *env, jclass that, jint receiver)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QString_1delete\n");
#endif
	delete (QString*) receiver;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    QString_1equals
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_QString_1equals
  (JNIEnv *env, jclass that, jint receiver, jint object)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QString_1equals\n");
#endif
	return *((QString*) receiver) == *((QString*) object);
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
  (JNIEnv *env, jclass that, jint receiver)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "QString_1utf8\n");
#endif
	QString string = *((QString*) receiver);
	QCString* qcString = new QCString();
	*qcString = string.utf8();
	return (jint) qcString;
}


/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KServiceListIterator_1delete
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_KDE_KServiceListIterator_1delete
  (JNIEnv *env, jclass that, jint receiver)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KServiceListIterator_1delete\n");
#endif
	delete (QValueListIterator<KService::Ptr>*) receiver;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KServiceListIterator_1dereference
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KServiceListIterator_1dereference
  (JNIEnv *env, jclass that, jint receiver)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KServiceListIterator_1dereference\n");
#endif
	KSharedPtr<KService>* service = new KSharedPtr<KService>();
	*service = *(*((QValueListIterator<KService::Ptr>*) receiver));
	return (jint) service;
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KServiceListIterator_1increment
 * Signature: (I)I
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_KDE_KServiceListIterator_1increment
  (JNIEnv *env, jclass that, jint receiver)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KServiceListIterator_1increment\n");
#endif
	++(*((QValueListIterator<KService::Ptr>*) receiver));
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KServiceListIterator_1new
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KServiceListIterator_1new
  (JNIEnv *env, jclass that, jint listBeginning)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KServiceListIterator_1new\n");
#endif
	const QValueListIterator<KService::Ptr> *iterator =
		(const QValueListIterator<KService::Ptr> *) listBeginning;

	return (jint) new QValueListIterator<KService::Ptr>(*iterator);
}

/*
 * Class:     org_eclipse_swt_internal_motif_KDE
 * Method:    KServiceListIterator_1equals
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_KDE_KServiceListIterator_1equals
  (JNIEnv *env, jclass that, jint receiver, jint object)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "KServiceListIterator_1equals\n");
#endif
	return *((QValueListIterator<KService::Ptr>*) receiver) == 
		*((QValueListIterator<KService::Ptr>*) object);
}


} /* extern "C" */
