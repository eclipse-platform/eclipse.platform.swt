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
 * Callback implementation.
 */
#include "swt.h"
#include "callback.h"
#include <string.h>

/* --------------- callback globals ----------------- */

static JavaVM *jvm = NULL;
static jfieldID objectID = NULL;
static jfieldID addressID = NULL;
static jfieldID methodID = NULL;
static jfieldID signatureID = NULL;
static jfieldID isStaticID = NULL;
static jfieldID argCountID = NULL;
static jfieldID isArrayBasedID = NULL;
static CALLBACK_DATA callbackData[MAX_CALLBACKS];
static int callbackIDsCached = 0;
static int callbackEnabled = 1;
static int callbackEntryCount = 0;
static int initialized = 0;

#ifdef DEBUG_CALL_PRINTS
static int counter = 0;
#endif

/* --------------- callback functions --------------- */

int callback(int index, ...);

/* Function name from index and number of arguments */
#define FN(index, args) fn##index##_##args

/**
 * Functions templates
 *
 * NOTE: If the maximum number of arguments changes (MAX_ARGS), the number
 *       of function templates has to change accordinglly.
 */

/* Function template with no arguments */
#define FN_0(index) RETURN_TYPE FN(index, 0)() { return RETURN_CAST callback(index); }

/* Function template with 1 argument */
#define FN_1(index) RETURN_TYPE FN(index, 1)(int p1) { return RETURN_CAST callback(index, p1); }

/* Function template with 2 arguments */
#define FN_2(index) RETURN_TYPE FN(index, 2)(int p1, int p2) { return RETURN_CAST callback(index, p1, p2); }

/* Function template with 3 arguments */
#define FN_3(index) RETURN_TYPE FN(index, 3)(int p1, int p2, int p3) { return RETURN_CAST callback(index, p1, p2, p3); }

/* Function template with 4 arguments */
#define FN_4(index) RETURN_TYPE FN(index, 4)(int p1, int p2, int p3, int p4) { return RETURN_CAST callback(index, p1, p2, p3, p4); }

/* Function template with 5 arguments */
#define FN_5(index) RETURN_TYPE FN(index, 5)(int p1, int p2, int p3, int p4, int p5) { return RETURN_CAST callback(index, p1, p2, p3, p4, p5); }

/* Function template with 6 arguments */
#define FN_6(index) RETURN_TYPE FN(index, 6)(int p1, int p2, int p3, int p4, int p5, int p6) { return RETURN_CAST callback(index, p1, p2, p3, p4, p5, p6); }

/* Function template with 7 arguments */
#define FN_7(index) RETURN_TYPE FN(index, 7)(int p1, int p2, int p3, int p4, int p5, int p6, int p7) { return RETURN_CAST callback(index, p1, p2, p3, p4, p5, p6, p7); }

/* Function template with 8 arguments */
#define FN_8(index) RETURN_TYPE FN(index, 8)(int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8) { return RETURN_CAST callback(index, p1, p2, p3, p4, p5, p6, p7, p8); }

/* Function template with 9 arguments */
#define FN_9(index) RETURN_TYPE FN(index, 9)(int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8, int p9) { return RETURN_CAST callback(index, p1, p2, p3, p4, p5, p6, p7, p8, p9); }

/* Function template with 10 arguments */
#define FN_10(index) RETURN_TYPE FN(index, 10) (int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8, int p9, int p10) { return RETURN_CAST callback(index, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10); }

/* Function template with 11 arguments */
#define FN_11(index) RETURN_TYPE FN(index, 11) (int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8, int p9, int p10, int p11) { return RETURN_CAST callback(index, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11); }

/* Function template with 12 arguments */
#define FN_12(index) RETURN_TYPE FN(index, 12) (int p1, int p2, int p3, int p4, int p5, int p6, int p7, int p8, int p9, int p10, int p11, int p12) { return RETURN_CAST callback(index, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12); }

/**
 * Define all functions with the specified number of arguments.
 *
 * NOTE: If the maximum number of callbacks changes (MAX_CALLBACKS),
 *       this macro has to be updated. 
 */
#ifdef REDUCED_CALLBACKS
#define FN_BLOCK(args) FN_##args(0) FN_##args(1) FN_##args(2) FN_##args(3) FN_##args(4) FN_##args(5) FN_##args(6) FN_##args(7) FN_##args(8) FN_##args(9) FN_##args(10) FN_##args(11) FN_##args(12) FN_##args(13) FN_##args(14) FN_##args(15)
#else
#define FN_BLOCK(args) FN_##args(0) FN_##args(1) FN_##args(2) FN_##args(3) FN_##args(4) FN_##args(5) FN_##args(6) FN_##args(7) FN_##args(8) FN_##args(9) FN_##args(10) FN_##args(11) FN_##args(12) FN_##args(13) FN_##args(14) FN_##args(15) FN_##args(16) FN_##args(17) FN_##args(18) FN_##args(19) FN_##args(20) FN_##args(21) FN_##args(22) FN_##args(23) FN_##args(24) FN_##args(25) FN_##args(26) FN_##args(27) FN_##args(28) FN_##args(29) FN_##args(30) FN_##args(31) FN_##args(32) FN_##args(33) FN_##args(34) FN_##args(35) FN_##args(36) FN_##args(37) FN_##args(38) FN_##args(39) FN_##args(40) FN_##args(41) FN_##args(42) FN_##args(43) FN_##args(44) FN_##args(45) FN_##args(46) FN_##args(47) FN_##args(48) FN_##args(49) FN_##args(50) FN_##args(51) FN_##args(52) FN_##args(53) FN_##args(54) FN_##args(55) FN_##args(56) FN_##args(57) FN_##args(58) FN_##args(59) FN_##args(60) FN_##args(61) FN_##args(62) FN_##args(63) FN_##args(64) FN_##args(65) FN_##args(66) FN_##args(67) FN_##args(68) FN_##args(69) FN_##args(70) FN_##args(71) FN_##args(72) FN_##args(73) FN_##args(74) FN_##args(75) FN_##args(76) FN_##args(77) FN_##args(78) FN_##args(79) FN_##args(80) FN_##args(81) FN_##args(82) FN_##args(83) FN_##args(84) FN_##args(85) FN_##args(86) FN_##args(87) FN_##args(88) FN_##args(89) FN_##args(90) FN_##args(91) FN_##args(92) FN_##args(93) FN_##args(94) FN_##args(95) FN_##args(96) FN_##args(97) FN_##args(98) FN_##args(99) FN_##args(100) FN_##args(101) FN_##args(102) FN_##args(103) FN_##args(104) FN_##args(105) FN_##args(106) FN_##args(107) FN_##args(108) FN_##args(109) FN_##args(110) FN_##args(111) FN_##args(112) FN_##args(113) FN_##args(114) FN_##args(115) FN_##args(116) FN_##args(117) FN_##args(118) FN_##args(119) FN_##args(120) FN_##args(121) FN_##args(122) FN_##args(123) FN_##args(124) FN_##args(125) FN_##args(126) FN_##args(127) 
#endif /* REDUCED_CALLBACKS */

/**
 * Define all callback functions.
 *
 * NOTE: If the maximum number of arguments changes (MAX_ARGS), the following
 *       has to change accordinglly.
 */
FN_BLOCK(0)
FN_BLOCK(1)
FN_BLOCK(2)
FN_BLOCK(3)
FN_BLOCK(4)
FN_BLOCK(5)
FN_BLOCK(6)
FN_BLOCK(7)
FN_BLOCK(8)
FN_BLOCK(9)
FN_BLOCK(10)
FN_BLOCK(11)
FN_BLOCK(12)

/**
 * Initialize the function pointers for the callback routines.
 *
 * NOTE: If MAX_ARGS or MAX_CALLBACKS changes, the following has to be updated.
 */
#ifdef REDUCED_CALLBACKS
#define FN_A_BLOCK(args) {(int *)FN(0, args),(int *)FN(1, args),(int *)FN(2, args),(int *)FN(3, args),(int *)FN(4, args),(int *)FN(5, args),(int *)FN(6, args),(int *)FN(7, args),(int *)FN(8, args),(int *)FN(9, args),(int *)FN(10, args),(int *)FN(11, args),(int *)FN(12, args),(int *)FN(13, args),(int *)FN(14, args),(int *)FN(15, args)},
#else
#define FN_A_BLOCK(args) {(int *)FN(0, args),(int *)FN(1, args),(int *)FN(2, args),(int *)FN(3, args),(int *)FN(4, args),(int *)FN(5, args),(int *)FN(6, args),(int *)FN(7, args),(int *)FN(8, args),(int *)FN(9, args),(int *)FN(10, args),(int *)FN(11, args),(int *)FN(12, args),(int *)FN(13, args),(int *)FN(14, args),(int *)FN(15, args),(int *)FN(16, args),(int *)FN(17, args),(int *)FN(18, args),(int *)FN(19, args),(int *)FN(20, args),(int *)FN(21, args),(int *)FN(22, args),(int *)FN(23, args),(int *)FN(24, args),(int *)FN(25, args),(int *)FN(26, args),(int *)FN(27, args),(int *)FN(28, args),(int *)FN(29, args),(int *)FN(30, args),(int *)FN(31, args),(int *)FN(32, args),(int *)FN(33, args),(int *)FN(34, args),(int *)FN(35, args),(int *)FN(36, args),(int *)FN(37, args),(int *)FN(38, args),(int *)FN(39, args),(int *)FN(40, args),(int *)FN(41, args),(int *)FN(42, args),(int *)FN(43, args),(int *)FN(44, args),(int *)FN(45, args),(int *)FN(46, args),(int *)FN(47, args),(int *)FN(48, args),(int *)FN(49, args),(int *)FN(50, args),(int *)FN(51, args),(int *)FN(52, args),(int *)FN(53, args),(int *)FN(54, args),(int *)FN(55, args),(int *)FN(56, args),(int *)FN(57, args),(int *)FN(58, args),(int *)FN(59, args),(int *)FN(60, args),(int *)FN(61, args),(int *)FN(62, args),(int *)FN(63, args),(int *)FN(64, args),(int *)FN(65, args),(int *)FN(66, args),(int *)FN(67, args),(int *)FN(68, args),(int *)FN(69, args),(int *)FN(70, args),(int *)FN(71, args),(int *)FN(72, args),(int *)FN(73, args),(int *)FN(74, args),(int *)FN(75, args),(int *)FN(76, args),(int *)FN(77, args),(int *)FN(78, args),(int *)FN(79, args),(int *)FN(80, args),(int *)FN(81, args),(int *)FN(82, args),(int *)FN(83, args),(int *)FN(84, args),(int *)FN(85, args),(int *)FN(86, args),(int *)FN(87, args),(int *)FN(88, args),(int *)FN(89, args),(int *)FN(90, args),(int *)FN(91, args),(int *)FN(92, args),(int *)FN(93, args),(int *)FN(94, args),(int *)FN(95, args),(int *)FN(96, args),(int *)FN(97, args),(int *)FN(98, args),(int *)FN(99, args),(int *)FN(100, args),(int *)FN(101, args),(int *)FN(102, args),(int *)FN(103, args),(int *)FN(104, args),(int *)FN(105, args),(int *)FN(106, args),(int *)FN(107, args),(int *)FN(108, args),(int *)FN(109, args),(int *)FN(110, args),(int *)FN(111, args),(int *)FN(112, args),(int *)FN(113, args),(int *)FN(114, args),(int *)FN(115, args),(int *)FN(116, args),(int *)FN(117, args),(int *)FN(118, args),(int *)FN(119, args),(int *)FN(120, args),(int *)FN(121, args),(int *)FN(122, args),(int *)FN(123, args),(int *)FN(124, args),(int *)FN(125, args),(int *)FN(126, args),(int *)FN(127, args)},
#endif /* REDUCED_CALLBACKS */

int * fnx_array[MAX_ARGS+1][MAX_CALLBACKS] = { 
	FN_A_BLOCK(0)    
	FN_A_BLOCK(1)    
	FN_A_BLOCK(2)    
	FN_A_BLOCK(3)    
	FN_A_BLOCK(4)    
	FN_A_BLOCK(5)    
	FN_A_BLOCK(6)    
	FN_A_BLOCK(7)    
	FN_A_BLOCK(8)    
	FN_A_BLOCK(9)    
	FN_A_BLOCK(10)    
	FN_A_BLOCK(11)    
	FN_A_BLOCK(12)    
};


/* --------------- callback class calls --------------- */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_Callback_bind
  (JNIEnv *env, jclass that, jobject lpCallback)
{
	int i;
	jclass javaClass;
   	jobject javaObject, javaMethod, javaSignature;
   	jboolean isStatic, isArrayBased;
	jint argCount;
	jmethodID mid;
	const char *methodString, *sigString;
	
	if (jvm == NULL) (*env)->GetJavaVM(env, &jvm);

	/*
	* The methodID and fieldID are computed once and will be valid
	* unless the class is unloaded.
	*/
    javaClass = that;
    if (!callbackIDsCached) {
        objectID = (*env)->GetFieldID(env, javaClass, "object", "Ljava/lang/Object;");
        addressID = (*env)->GetFieldID(env, javaClass, "address", "I");
        methodID = (*env)->GetFieldID(env, javaClass, "method", "Ljava/lang/String;");
        signatureID = (*env)->GetFieldID(env, javaClass, "signature", "Ljava/lang/String;");
        isStaticID = (*env)->GetFieldID(env, javaClass, "isStatic", "Z");
        argCountID = (*env)->GetFieldID(env, javaClass, "argCount", "I");
        isArrayBasedID = (*env)->GetFieldID(env, javaClass, "isArrayBased", "Z");
        callbackIDsCached = 1;
    }
    javaObject = (*env)->GetObjectField(env, lpCallback, objectID);
    javaMethod = (*env)->GetObjectField(env, lpCallback, methodID);
    javaSignature = (*env)->GetObjectField(env, lpCallback, signatureID);
    isStatic = (*env)->GetBooleanField(env, lpCallback, isStaticID);
    argCount = (*env)->GetIntField(env, lpCallback, argCountID);
    isArrayBased = (*env)->GetBooleanField(env, lpCallback, isArrayBasedID);
    methodString = (const char *) (*env)->GetStringUTFChars(env, javaMethod, NULL);
    sigString = (const char *) (*env)->GetStringUTFChars(env, javaSignature, NULL);
    if (isStatic) {
        mid = (*env)->GetStaticMethodID(env, javaObject, methodString, sigString);
    } else {
        javaClass = (*env)->GetObjectClass(env, javaObject);    
        mid = (*env)->GetMethodID(env, javaClass, methodString, sigString);
    }
    (*env)->ReleaseStringUTFChars(env, javaMethod, methodString);
    (*env)->ReleaseStringUTFChars(env, javaSignature, sigString);
    if (initialized==0) {
        memset((void *)&callbackData, 0, sizeof(callbackData));
        initialized = 1;
    }
    for (i=0; i<MAX_CALLBACKS; i++) {
        if (!callbackData[i].callin) {
            callbackData[i].callin = (*env)->NewGlobalRef(env, lpCallback);
            callbackData[i].methodID = mid;
            return (jint) fnx_array[argCount][i];
        }
    }
    fprintf(stderr, "bind fail, no free callback slot ******* \n");
    return 0; /* this means there was no free callback slot */
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_Callback_unbind
  (JNIEnv *env, jclass that, jobject lpCallback)
{
	int i, address, argCount;
	if (!callbackIDsCached) return;

    address = (*env)->GetIntField(env, lpCallback, addressID);
    argCount = (*env)->GetIntField(env, lpCallback, argCountID);
    
    for (i=0; i<MAX_CALLBACKS; i++) {        
        if ((int)fnx_array[argCount][i] == address) {
            (*env)->DeleteGlobalRef(env, callbackData[i].callin);
            callbackData[i].callin = 0;      
            callbackData[i].methodID = 0;
        }
    }
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_Callback_getEnabled
  (JNIEnv *env, jclass that)
{
	return (jboolean)callbackEnabled;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_Callback_getEntryCount
  (JNIEnv *env, jclass that)
{
	return (jint)callbackEntryCount;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_Callback_setEnabled
  (JNIEnv *env, jclass that, jboolean enable)
{
	callbackEnabled = enable;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_Callback_reset
  (JNIEnv *env, jclass that)
{
    memset((void *)&callbackData, 0, sizeof(callbackData));
}

JNIEXPORT jstring JNICALL Java_org_eclipse_swt_internal_Callback_getPlatform
  (JNIEnv *env, jclass that)
{
	return (*env)->NewStringUTF(env, PLATFORM);
}

int callback(int index, ...)
{
	if (!callbackEnabled) return 0;

	{
	jobject callback = callbackData[index].callin;
	jmethodID mid = callbackData[index].methodID;
	JNIEnv *env;
	jobject javaObject;
	jboolean isStatic, isArrayBased;
	int result = 0;
	va_list vl;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "* callback starting %d\n", counter++);
#endif

	(*jvm)->GetEnv(jvm, (void **)&env, JNI_VERSION_1_1);

	/* An exception has already occurred. Allow the stack to unwind so that
	the exception will be thrown in Java */
	if ((*env)->ExceptionOccurred(env)) {
#ifdef DEBUG_CALL_PRINTS
		fprintf(stderr, "************ java exception occurred\n");
#endif
		return 0;
	}

	javaObject = (*env)->GetObjectField(env,callback, objectID);
	isStatic = ((*env)->GetBooleanField(env,callback, isStaticID)) != 0;
	isArrayBased = ((*env)->GetBooleanField(env,callback, isArrayBasedID)) != 0;

	callbackEntryCount++;
	va_start(vl, index);
	if (isArrayBased) {
		int i;
		jint argCount = (*env)->GetIntField(env, callback, argCountID);
		jintArray javaArray = (*env)->NewIntArray(env, argCount);
		jint *elements = (*env)->GetIntArrayElements(env, javaArray, NULL);
		for (i=0; i<argCount; i++) {
			elements[i] = va_arg(vl, jint); 
		}
		(*env)->ReleaseIntArrayElements(env, javaArray, elements, 0);
		if (isStatic) {
			result = (*env)->CallStaticIntMethod(env, javaObject, mid, javaArray);
		} else {
			result = (*env)->CallIntMethod(env, javaObject, mid, javaArray);
		}
		(*env)->DeleteLocalRef(env, javaArray);
	} else {
		if (isStatic) {
			result = (*env)->CallStaticIntMethodV(env, javaObject, mid, vl);
		} else {
			result = (*env)->CallIntMethodV(env, javaObject, mid, vl);
		}
	}
	va_end(vl);
	callbackEntryCount--;
	
	/*
	* This function may be called many times before we return to Java.
	* We have to explicitly delete local references to avoid GP's
	* in the JDK and IBM Hursley VM.
	*/
	(*env)->DeleteLocalRef(env, javaObject);

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "* callback exiting %d\n", --counter);
#endif

	return result;
	}
}

/* ------------- callback class calls end --------------- */
