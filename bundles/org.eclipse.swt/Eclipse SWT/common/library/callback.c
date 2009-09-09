/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
 
/**
 * Callback implementation.
 */
#include "callback.h"
#include <string.h>

/* define this to print out debug statements */
/* #define DEBUG_CALL_PRINTS */

/* --------------- callback globals ----------------- */

static JavaVM *jvm = NULL;
static CALLBACK_DATA callbackData[MAX_CALLBACKS];
static int callbackEnabled = 1;
static int callbackEntryCount = 0;
static int initialized = 0;
static jint JNI_VERSION = 0;

#ifdef DEBUG_CALL_PRINTS
static int counter = 0;
#endif

#ifdef ATOMIC
#include <libkern/OSAtomic.h>
#define ATOMIC_INC(value) OSAtomicIncrement32(&value);
#define ATOMIC_DEC(value) OSAtomicDecrement32(&value);
#else
#define ATOMIC_INC(value) value++;
#define ATOMIC_DEC(value) value--;
#endif

jintLong callback(int index, ...);

#ifdef USE_ASSEMBLER

#if !(defined (_WIN32) || defined (_WIN32_WCE))
#include <sys/mman.h>
#endif

static unsigned char *callbackCode = NULL;
#define CALLBACK_THUNK_SIZE 64

#else

/* --------------- callback functions --------------- */


/* Function name from index and number of arguments */
#define FN(index, args) fn##index##_##args

/**
 * Functions templates
 *
 * NOTE: If the maximum number of arguments changes (MAX_ARGS), the number
 *       of function templates has to change accordingly.
 */

/* Function template with no arguments */
#define FN_0(index) RETURN_TYPE FN(index, 0)() { return RETURN_CAST callback(index); }

/* Function template with 1 argument */
#define FN_1(index) RETURN_TYPE FN(index, 1)(jintLong p1) { return RETURN_CAST callback(index, p1); }

/* Function template with 2 arguments */
#define FN_2(index) RETURN_TYPE FN(index, 2)(jintLong p1, jintLong p2) { return RETURN_CAST callback(index, p1, p2); }

/* Function template with 3 arguments */
#define FN_3(index) RETURN_TYPE FN(index, 3)(jintLong p1, jintLong p2, jintLong p3) { return RETURN_CAST callback(index, p1, p2, p3); }

/* Function template with 4 arguments */
#define FN_4(index) RETURN_TYPE FN(index, 4)(jintLong p1, jintLong p2, jintLong p3, jintLong p4) { return RETURN_CAST callback(index, p1, p2, p3, p4); }

/* Function template with 5 arguments */
#define FN_5(index) RETURN_TYPE FN(index, 5)(jintLong p1, jintLong p2, jintLong p3, jintLong p4, jintLong p5) { return RETURN_CAST callback(index, p1, p2, p3, p4, p5); }

/* Function template with 6 arguments */
#define FN_6(index) RETURN_TYPE FN(index, 6)(jintLong p1, jintLong p2, jintLong p3, jintLong p4, jintLong p5, jintLong p6) { return RETURN_CAST callback(index, p1, p2, p3, p4, p5, p6); }

/* Function template with 7 arguments */
#define FN_7(index) RETURN_TYPE FN(index, 7)(jintLong p1, jintLong p2, jintLong p3, jintLong p4, jintLong p5, jintLong p6, jintLong p7) { return RETURN_CAST callback(index, p1, p2, p3, p4, p5, p6, p7); }

/* Function template with 8 arguments */
#define FN_8(index) RETURN_TYPE FN(index, 8)(jintLong p1, jintLong p2, jintLong p3, jintLong p4, jintLong p5, jintLong p6, jintLong p7, jintLong p8) { return RETURN_CAST callback(index, p1, p2, p3, p4, p5, p6, p7, p8); }

/* Function template with 9 arguments */
#define FN_9(index) RETURN_TYPE FN(index, 9)(jintLong p1, jintLong p2, jintLong p3, jintLong p4, jintLong p5, jintLong p6, jintLong p7, jintLong p8, jintLong p9) { return RETURN_CAST callback(index, p1, p2, p3, p4, p5, p6, p7, p8, p9); }

/* Function template with 10 arguments */
#define FN_10(index) RETURN_TYPE FN(index, 10) (jintLong p1, jintLong p2, jintLong p3, jintLong p4, jintLong p5, jintLong p6, jintLong p7, jintLong p8, jintLong p9, jintLong p10) { return RETURN_CAST callback(index, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10); }

/* Function template with 11 arguments */
#define FN_11(index) RETURN_TYPE FN(index, 11) (jintLong p1, jintLong p2, jintLong p3, jintLong p4, jintLong p5, jintLong p6, jintLong p7, jintLong p8, jintLong p9, jintLong p10, jintLong p11) { return RETURN_CAST callback(index, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11); }

/* Function template with 12 arguments */
#define FN_12(index) RETURN_TYPE FN(index, 12) (jintLong p1, jintLong p2, jintLong p3, jintLong p4, jintLong p5, jintLong p6, jintLong p7, jintLong p8, jintLong p9, jintLong p10, jintLong p11, jintLong p12) { return RETURN_CAST callback(index, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12); }

/**
 * Define all functions with the specified number of arguments.
 *
 * NOTE: If the maximum number of callbacks changes (MAX_CALLBACKS),
 *       this macro has to be updated. 
 */
#if MAX_CALLBACKS == 16
#define FN_BLOCK(args) \
	FN_##args(0) \
	FN_##args(1) \
	FN_##args(2) \
	FN_##args(3) \
	FN_##args(4) \
	FN_##args(5) \
	FN_##args(6) \
	FN_##args(7) \
	FN_##args(8) \
	FN_##args(9) \
	FN_##args(10) \
	FN_##args(11) \
	FN_##args(12) \
	FN_##args(13) \
	FN_##args(14) \
	FN_##args(15)
#elif MAX_CALLBACKS == 128
#define FN_BLOCK(args) \
	FN_##args(0) \
	FN_##args(1) \
	FN_##args(2) \
	FN_##args(3) \
	FN_##args(4) \
	FN_##args(5) \
	FN_##args(6) \
	FN_##args(7) \
	FN_##args(8) \
	FN_##args(9) \
	FN_##args(10) \
	FN_##args(11) \
	FN_##args(12) \
	FN_##args(13) \
	FN_##args(14) \
	FN_##args(15) \
	FN_##args(16) \
	FN_##args(17) \
	FN_##args(18) \
	FN_##args(19) \
	FN_##args(20) \
	FN_##args(21) \
	FN_##args(22) \
	FN_##args(23) \
	FN_##args(24) \
	FN_##args(25) \
	FN_##args(26) \
	FN_##args(27) \
	FN_##args(28) \
	FN_##args(29) \
	FN_##args(30) \
	FN_##args(31) \
	FN_##args(32) \
	FN_##args(33) \
	FN_##args(34) \
	FN_##args(35) \
	FN_##args(36) \
	FN_##args(37) \
	FN_##args(38) \
	FN_##args(39) \
	FN_##args(40) \
	FN_##args(41) \
	FN_##args(42) \
	FN_##args(43) \
	FN_##args(44) \
	FN_##args(45) \
	FN_##args(46) \
	FN_##args(47) \
	FN_##args(48) \
	FN_##args(49) \
	FN_##args(50) \
	FN_##args(51) \
	FN_##args(52) \
	FN_##args(53) \
	FN_##args(54) \
	FN_##args(55) \
	FN_##args(56) \
	FN_##args(57) \
	FN_##args(58) \
	FN_##args(59) \
	FN_##args(60) \
	FN_##args(61) \
	FN_##args(62) \
	FN_##args(63) \
	FN_##args(64) \
	FN_##args(65) \
	FN_##args(66) \
	FN_##args(67) \
	FN_##args(68) \
	FN_##args(69) \
	FN_##args(70) \
	FN_##args(71) \
	FN_##args(72) \
	FN_##args(73) \
	FN_##args(74) \
	FN_##args(75) \
	FN_##args(76) \
	FN_##args(77) \
	FN_##args(78) \
	FN_##args(79) \
	FN_##args(80) \
	FN_##args(81) \
	FN_##args(82) \
	FN_##args(83) \
	FN_##args(84) \
	FN_##args(85) \
	FN_##args(86) \
	FN_##args(87) \
	FN_##args(88) \
	FN_##args(89) \
	FN_##args(90) \
	FN_##args(91) \
	FN_##args(92) \
	FN_##args(93) \
	FN_##args(94) \
	FN_##args(95) \
	FN_##args(96) \
	FN_##args(97) \
	FN_##args(98) \
	FN_##args(99) \
	FN_##args(100) \
	FN_##args(101) \
	FN_##args(102) \
	FN_##args(103) \
	FN_##args(104) \
	FN_##args(105) \
	FN_##args(106) \
	FN_##args(107) \
	FN_##args(108) \
	FN_##args(109) \
	FN_##args(110) \
	FN_##args(111) \
	FN_##args(112) \
	FN_##args(113) \
	FN_##args(114) \
	FN_##args(115) \
	FN_##args(116) \
	FN_##args(117) \
	FN_##args(118) \
	FN_##args(119) \
	FN_##args(120) \
	FN_##args(121) \
	FN_##args(122) \
	FN_##args(123) \
	FN_##args(124) \
	FN_##args(125) \
	FN_##args(126) \
	FN_##args(127)
#else
#error Invalid MAX_CALLBACKS
#endif /* MAX_CALLBACKS == 16 */

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
#if MAX_CALLBACKS == 16
#define FN_A_BLOCK(args) { \
	(jintLong)FN(0, args), \
	(jintLong)FN(1, args), \
	(jintLong)FN(2, args), \
	(jintLong)FN(3, args), \
	(jintLong)FN(4, args), \
	(jintLong)FN(5, args), \
	(jintLong)FN(6, args), \
	(jintLong)FN(7, args), \
	(jintLong)FN(8, args), \
	(jintLong)FN(9, args), \
	(jintLong)FN(10, args), \
	(jintLong)FN(11, args), \
	(jintLong)FN(12, args), \
	(jintLong)FN(13, args), \
	(jintLong)FN(14, args), \
	(jintLong)FN(15, args), \
},
#elif MAX_CALLBACKS == 128
#define FN_A_BLOCK(args) { \
	(jintLong)FN(0, args), \
	(jintLong)FN(1, args), \
	(jintLong)FN(2, args), \
	(jintLong)FN(3, args), \
	(jintLong)FN(4, args), \
	(jintLong)FN(5, args), \
	(jintLong)FN(6, args), \
	(jintLong)FN(7, args), \
	(jintLong)FN(8, args), \
	(jintLong)FN(9, args), \
	(jintLong)FN(10, args), \
	(jintLong)FN(11, args), \
	(jintLong)FN(12, args), \
	(jintLong)FN(13, args), \
	(jintLong)FN(14, args), \
	(jintLong)FN(15, args), \
	(jintLong)FN(16, args), \
	(jintLong)FN(17, args), \
	(jintLong)FN(18, args), \
	(jintLong)FN(19, args), \
	(jintLong)FN(20, args), \
	(jintLong)FN(21, args), \
	(jintLong)FN(22, args), \
	(jintLong)FN(23, args), \
	(jintLong)FN(24, args), \
	(jintLong)FN(25, args), \
	(jintLong)FN(26, args), \
	(jintLong)FN(27, args), \
	(jintLong)FN(28, args), \
	(jintLong)FN(29, args), \
	(jintLong)FN(30, args), \
	(jintLong)FN(31, args), \
	(jintLong)FN(32, args), \
	(jintLong)FN(33, args), \
	(jintLong)FN(34, args), \
	(jintLong)FN(35, args), \
	(jintLong)FN(36, args), \
	(jintLong)FN(37, args), \
	(jintLong)FN(38, args), \
	(jintLong)FN(39, args), \
	(jintLong)FN(40, args), \
	(jintLong)FN(41, args), \
	(jintLong)FN(42, args), \
	(jintLong)FN(43, args), \
	(jintLong)FN(44, args), \
	(jintLong)FN(45, args), \
	(jintLong)FN(46, args), \
	(jintLong)FN(47, args), \
	(jintLong)FN(48, args), \
	(jintLong)FN(49, args), \
	(jintLong)FN(50, args), \
	(jintLong)FN(51, args), \
	(jintLong)FN(52, args), \
	(jintLong)FN(53, args), \
	(jintLong)FN(54, args), \
	(jintLong)FN(55, args), \
	(jintLong)FN(56, args), \
	(jintLong)FN(57, args), \
	(jintLong)FN(58, args), \
	(jintLong)FN(59, args), \
	(jintLong)FN(60, args), \
	(jintLong)FN(61, args), \
	(jintLong)FN(62, args), \
	(jintLong)FN(63, args), \
	(jintLong)FN(64, args), \
	(jintLong)FN(65, args), \
	(jintLong)FN(66, args), \
	(jintLong)FN(67, args), \
	(jintLong)FN(68, args), \
	(jintLong)FN(69, args), \
	(jintLong)FN(70, args), \
	(jintLong)FN(71, args), \
	(jintLong)FN(72, args), \
	(jintLong)FN(73, args), \
	(jintLong)FN(74, args), \
	(jintLong)FN(75, args), \
	(jintLong)FN(76, args), \
	(jintLong)FN(77, args), \
	(jintLong)FN(78, args), \
	(jintLong)FN(79, args), \
	(jintLong)FN(80, args), \
	(jintLong)FN(81, args), \
	(jintLong)FN(82, args), \
	(jintLong)FN(83, args), \
	(jintLong)FN(84, args), \
	(jintLong)FN(85, args), \
	(jintLong)FN(86, args), \
	(jintLong)FN(87, args), \
	(jintLong)FN(88, args), \
	(jintLong)FN(89, args), \
	(jintLong)FN(90, args), \
	(jintLong)FN(91, args), \
	(jintLong)FN(92, args), \
	(jintLong)FN(93, args), \
	(jintLong)FN(94, args), \
	(jintLong)FN(95, args), \
	(jintLong)FN(96, args), \
	(jintLong)FN(97, args), \
	(jintLong)FN(98, args), \
	(jintLong)FN(99, args), \
	(jintLong)FN(100, args), \
	(jintLong)FN(101, args), \
	(jintLong)FN(102, args), \
	(jintLong)FN(103, args), \
	(jintLong)FN(104, args), \
	(jintLong)FN(105, args), \
	(jintLong)FN(106, args), \
	(jintLong)FN(107, args), \
	(jintLong)FN(108, args), \
	(jintLong)FN(109, args), \
	(jintLong)FN(110, args), \
	(jintLong)FN(111, args), \
	(jintLong)FN(112, args), \
	(jintLong)FN(113, args), \
	(jintLong)FN(114, args), \
	(jintLong)FN(115, args), \
	(jintLong)FN(116, args), \
	(jintLong)FN(117, args), \
	(jintLong)FN(118, args), \
	(jintLong)FN(119, args), \
	(jintLong)FN(120, args), \
	(jintLong)FN(121, args), \
	(jintLong)FN(122, args), \
	(jintLong)FN(123, args), \
	(jintLong)FN(124, args), \
	(jintLong)FN(125, args), \
	(jintLong)FN(126, args), \
	(jintLong)FN(127, args), \
},
#else
#error Invalid MAX_CALLBACKS
#endif /* MAX_CALLBACKS == 16 */

jintLong fnx_array[MAX_ARGS+1][MAX_CALLBACKS] = { 
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

#endif /* USE_ASSEMBLER */

/* --------------- callback class calls --------------- */

JNIEXPORT jintLong JNICALL Java_org_eclipse_swt_internal_Callback_bind
  (JNIEnv *env, jclass that, jobject callbackObject, jobject object, jstring method, jstring signature, jint argCount, jboolean isStatic, jboolean isArrayBased, jintLong errorResult)
{
	int i;
	jmethodID mid = NULL;
	jclass javaClass = that;
	const char *methodString = NULL, *sigString = NULL;
	if (jvm == NULL) (*env)->GetJavaVM(env, &jvm);
	if (JNI_VERSION == 0) JNI_VERSION = (*env)->GetVersion(env);
	if (!initialized) {
		memset(&callbackData, 0, sizeof(callbackData));
		initialized = 1;
	}
	if (method) methodString = (const char *) (*env)->GetStringUTFChars(env, method, NULL);
	if (signature) sigString = (const char *) (*env)->GetStringUTFChars(env, signature, NULL);
	if (object && methodString && sigString) {
		if (isStatic) {
			mid = (*env)->GetStaticMethodID(env, object, methodString, sigString);
		} else {
			javaClass = (*env)->GetObjectClass(env, object);    
			mid = (*env)->GetMethodID(env, javaClass, methodString, sigString);
		}
	}
	if (method && methodString) (*env)->ReleaseStringUTFChars(env, method, methodString);
	if (signature && sigString) (*env)->ReleaseStringUTFChars(env, signature, sigString);
	if (mid == 0) goto fail;
	for (i=0; i<MAX_CALLBACKS; i++) {
		if (!callbackData[i].callback) {
			if ((callbackData[i].callback = (*env)->NewGlobalRef(env, callbackObject)) == NULL) goto fail;
			if ((callbackData[i].object = (*env)->NewGlobalRef(env, object)) == NULL) goto fail;
			callbackData[i].isStatic = isStatic;
			callbackData[i].isArrayBased = isArrayBased;
			callbackData[i].argCount = argCount;
			callbackData[i].errorResult = errorResult;
			callbackData[i].methodID = mid;
#ifndef USE_ASSEMBLER
			return (jintLong) fnx_array[argCount][i];
#else
			{
			int j = 0, k;
			unsigned char* code;
#ifdef __APPLE__
			int pad = 0;
#endif
			if (callbackCode == NULL) {
#if defined (_WIN32) || defined (_WIN32_WCE)
				callbackCode = VirtualAlloc(NULL, CALLBACK_THUNK_SIZE * MAX_CALLBACKS, MEM_COMMIT, PAGE_EXECUTE_READWRITE);
				if (callbackCode == NULL) return 0;
#else 
				callbackCode = mmap(NULL, CALLBACK_THUNK_SIZE * MAX_CALLBACKS, PROT_EXEC | PROT_READ | PROT_WRITE, MAP_PRIVATE | MAP_ANON, -1, 0);
				if (callbackCode == MAP_FAILED) return 0;
#endif
			}
			code = (unsigned char *)(callbackCode + (i * CALLBACK_THUNK_SIZE));

			//PUSH EBP - 1 byte
			code[j++] = 0x55;

			//MOV EBP,ESP - 2 bytes
			code[j++] = 0x8b;
			code[j++] = 0xec;

#ifdef __APPLE__
			/* darwin calling conventions require that the stack be aligned on a 16-byte boundary. */
			k = (argCount+3)*sizeof(jintLong);
			pad = ((k + 15) & ~15) - k;
			if (pad > 0) {
				//SUB ESP,pad - 3 bytes
				code[j++] = 0x83;
				code[j++] = 0xec;
				code[j++] = pad;
			}
#endif

			// 3*argCount bytes
			for (k=(argCount + 1) * sizeof(jintLong); k >= sizeof(jintLong)*2; k -= sizeof(jintLong)) {
				//PUSH SS:[EBP+k]
				code[j++] = 0xff;
				code[j++] = 0x75;
				code[j++] = k;
			}

			if (i > 127) {
				//PUSH i - 5 bytes
				code[j++] = 0x68;
				code[j++] = ((i >> 0) & 0xFF);
				code[j++] = ((i >> 8) & 0xFF);
				code[j++] = ((i >> 16) & 0xFF);
				code[j++] = ((i >> 24) & 0xFF);
			} else {
				//PUSH i - 2 bytes
				code[j++] = 0x6a;
				code[j++] = i;
			}

			//MOV EAX callback - 1 + sizeof(jintLong) bytes
			code[j++] = 0xb8;
			((jintLong *)&code[j])[0] = (jintLong)&callback;
			j += sizeof(jintLong);

			//CALL EAX - 2 bytes
			code[j++] = 0xff;
			code[j++] = 0xd0;

			//ADD ESP,(argCount + 1) * sizeof(jintLong) - 3 bytes
			code[j++] = 0x83;
			code[j++] = 0xc4;
#ifdef __APPLE__
			code[j++] = (unsigned char)(pad + ((argCount + 1) * sizeof(jintLong)));
#else
			code[j++] = (unsigned char)((argCount + 1) * sizeof(jintLong));
#endif

			//POP EBP - 1 byte
			code[j++] = 0x5d;

#if defined (_WIN32) || defined (_WIN32_WCE)
			//RETN argCount * sizeof(jintLong) - 3 bytes
			code[j++] = 0xc2;
			code[j++] = (unsigned char)(argCount * sizeof(jintLong));
			code[j++] = 0x00;
#else
			//RETN - 1 byte
			code[j++] = 0xc3;
#endif

			if (j > CALLBACK_THUNK_SIZE) {
				jclass errorClass = (*env)->FindClass(env, "java/lang/Error");
				(*env)->ThrowNew(env, errorClass, "Callback thunk overflow");
			}

			return (jintLong)code;
			}
#endif /* USE_ASSEMBLER */
		}
	}
fail:
    return 0;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_Callback_unbind
  (JNIEnv *env, jclass that, jobject callback)
{
	int i;
    for (i=0; i<MAX_CALLBACKS; i++) {
        if (callbackData[i].callback != NULL && (*env)->IsSameObject(env, callback, callbackData[i].callback)) {
            if (callbackData[i].callback != NULL) (*env)->DeleteGlobalRef(env, callbackData[i].callback);
            if (callbackData[i].object != NULL) (*env)->DeleteGlobalRef(env, callbackData[i].object);
            memset(&callbackData[i], 0, sizeof(CALLBACK_DATA));
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

jintLong callback(int index, ...)
{
	if (!callbackEnabled) return 0;

	{
	JNIEnv *env = NULL;
	jmethodID mid = callbackData[index].methodID;
	jobject object = callbackData[index].object;
	jboolean isStatic = callbackData[index].isStatic;
	jboolean isArrayBased = callbackData[index].isArrayBased;
	jint argCount = callbackData[index].argCount;
	jintLong result = callbackData[index].errorResult;
	int detach = 0;
	va_list vl;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "* callback starting %d\n", counter++);
#endif

#ifdef JNI_VERSION_1_2
	if (IS_JNI_1_2) {
		(*jvm)->GetEnv(jvm, (void **)&env, JNI_VERSION_1_2);
	}
#endif
	
#ifdef JNI_VERSION_1_4
	if (env == NULL) {
		if (JNI_VERSION >= JNI_VERSION_1_4) {
			(*jvm)->AttachCurrentThreadAsDaemon(jvm, (void **)&env, NULL);
		}
	}
#endif
	
	if (env == NULL) {
		(*jvm)->AttachCurrentThread(jvm, (void **)&env, NULL);
		if (IS_JNI_1_2) detach = 1;
	}
	
	/* If the current thread is not attached to the VM, it is not possible to call into the VM */
	if (env == NULL) {
#ifdef DEBUG_CALL_PRINTS
		fprintf(stderr, "* could not get env\n");
#endif
		goto noEnv;
	}

	/* If an exception has occurred in previous callbacks do not call into the VM. */
	if ((*env)->ExceptionOccurred(env)) {
		goto done;
	}

	/* Call into the VM. */
	ATOMIC_INC(callbackEntryCount);
	va_start(vl, index);
	if (isArrayBased) {
		int i;
		jintLongArray argsArray = (*env)->NewIntLongArray(env, argCount);
		if (argsArray != NULL) {
			jintLong *elements = (*env)->GetIntLongArrayElements(env, argsArray, NULL);
			if (elements != NULL) {
				for (i=0; i<argCount; i++) {
					elements[i] = va_arg(vl, jintLong); 
				}
				(*env)->ReleaseIntLongArrayElements(env, argsArray, elements, 0);
				if (isStatic) {
					result = (*env)->CallStaticIntLongMethod(env, object, mid, argsArray);
				} else {
					result = (*env)->CallIntLongMethod(env, object, mid, argsArray);
				}
			}
			/*
			* This function may be called many times before returning to Java,
			* explicitly delete local references to avoid GP's in certain VMs.
			*/
			(*env)->DeleteLocalRef(env, argsArray);
		}
	} else {
		if (isStatic) {
			result = (*env)->CallStaticIntLongMethodV(env, object, mid, vl);
		} else {
			result = (*env)->CallIntLongMethodV(env, object, mid, vl);
		}
	}
	va_end(vl);
	ATOMIC_DEC(callbackEntryCount);
	
done:
	/* If an exception has occurred in Java, return the error result. */
	if ((*env)->ExceptionOccurred(env)) {
#ifdef DEBUG_CALL_PRINTS
		fprintf(stderr, "* java exception occurred\n");
		(*env)->ExceptionDescribe(env);
#endif
		result = callbackData[index].errorResult;
	}

	if (detach) {
		(*jvm)->DetachCurrentThread(jvm);
		env = NULL;
	}

noEnv:

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "* callback exiting %d\n", --counter);
#endif

	return result;
	}
}

/* ------------- callback class calls end --------------- */
