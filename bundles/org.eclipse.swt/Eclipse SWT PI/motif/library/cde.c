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
 * SWT CDE natives implementation.
 */ 

#include "swt.h"

#include <stdio.h>
#include <assert.h>
#include <Dt/Dts.h>
#include <Dt/Action.h>

/*
 * Class:     org_eclipse_swt_internal_motif_CDE
 * Method:    DtAppInitialize
 * Signature: (III[B[B)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_CDE_DtAppInitialize
  (JNIEnv *env, jclass that, jint appContext, jint display, jint topWidget,
   jbyteArray appName, jbyteArray appClass)
{
    jbyte*   appName1  = NULL;
    jbyte*   appClass1 = NULL;
    jboolean status;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "DtAppInitialize\n");
#endif

    if (appName) appName1 = (*env)->GetByteArrayElements(env, appName, NULL); 
    if (appClass) appClass1 = (*env)->GetByteArrayElements(env, appClass, NULL); 

    status = (jint) DtAppInitialize( (XtAppContext) appContext, 
        (Display*) display, (Widget) topWidget, appName1, appClass1 );

    if (appClass) (*env)->ReleaseByteArrayElements(env, appClass, appClass1, 0);
    if (appName) (*env)->ReleaseByteArrayElements(env, appName, appName1, 0);
    return status;
}

/*
 * Class:     org_eclipse_swt_internal_motif_CDE
 * Method:    DtDbLoad
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_CDE_DtDbLoad
  (JNIEnv *env, jclass that )
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "DtDbLoad\n");
#endif

    DtDbLoad();
}

/*
 * Class:     org_eclipse_swt_internal_motif_CDE
 * Method:    DtDtsDataTypeNames
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_CDE_DtDtsDataTypeNames
  (JNIEnv *env, jclass that)
{
    jint dataTypeList;
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "DtDtsDataTypeNames\n");
#endif

    dataTypeList = (jint) DtDtsDataTypeNames();
    return dataTypeList;
}

/*
 * Class:     org_eclipse_swt_internal_motif_CDE
 * Method:    DtDtsFileToDataType
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_CDE_DtDtsFileToDataType
  (JNIEnv *env, jclass that, jbyteArray fileName)
{
    jbyte* fileName1 = NULL;
    jint   dataType;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "DtDtsFileToDataType\n");
#endif

    if (fileName) fileName1 = (*env)->GetByteArrayElements(env, fileName, NULL); 
    dataType = (jint) DtDtsFileToDataType( (char*) fileName1 );
    if (fileName) (*env)->ReleaseByteArrayElements(env, fileName, fileName1, 0);
    return dataType;
}

/*
 * Class:     org_eclipse_swt_internal_motif_CDE
 * Method:    DtDtsDataTypeIsAction
 * Signature: ([B)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_CDE_DtDtsDataTypeIsAction
  (JNIEnv *env, jclass that, jbyteArray dataType)
{
    jbyte*   dataType1 = NULL;
    jboolean isAction;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "DtDtsDataTypeIsAction\n");
#endif

    if (dataType) dataType1 = (*env)->GetByteArrayElements(env, dataType, NULL); 
    isAction = (jboolean) DtDtsDataTypeIsAction( (char*) dataType1 );
    if (dataType) (*env)->ReleaseByteArrayElements(env, dataType, dataType1, 0);
    return isAction;
}

/*
 * Class:     org_eclipse_swt_internal_motif_CDE
 * Method:    DtDtsDataTypeToAttributeValue
 * Signature: ([B[B[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_CDE_DtDtsDataTypeToAttributeValue
  (JNIEnv *env, jclass that, jbyteArray dataType, jbyteArray attrName, jbyteArray optName)
{
    jbyte*   dataType1 = NULL;
    jbyte*   attrName1 = NULL;
    jbyte*   optName1  = NULL;
    jint     attrValue;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "DtDtsDataTypeToAttributeValue\n");
#endif

    if (dataType) dataType1 = (*env)->GetByteArrayElements(env, dataType, NULL); 
    if (attrName) attrName1 = (*env)->GetByteArrayElements(env, attrName, NULL); 
    if (optName)  optName1  = (*env)->GetByteArrayElements(env, optName, NULL); 

    attrValue = (jint) DtDtsDataTypeToAttributeValue( (char*) dataType1, 
        (char*) attrName1, (char*) optName1 );

    if (optName)  (*env)->ReleaseByteArrayElements(env, optName,  optName1, 0);
    if (attrName) (*env)->ReleaseByteArrayElements(env, attrName, attrName1, 0);
    if (dataType) (*env)->ReleaseByteArrayElements(env, dataType, dataType1, 0);
    return attrValue;
}

/*
 * Class:     org_eclipse_swt_internal_motif_CDE
 * Method:    DtDtsFreeDataType
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_CDE_DtDtsFreeDataType
  (JNIEnv *env, jclass that, jint dataType)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "DtDtsFreeDataType\n");
#endif

    DtDtsFreeDataType( (char*) dataType );
}

/*
 * Class:     org_eclipse_swt_internal_motif_CDE
 * Method:    DtDtsFreeDataTypeNames
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_CDE_DtDtsFreeDataTypeNames
  (JNIEnv *env, jclass that, jint dataTypeList)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "DtDtsFreeDataTypeNames\n");
#endif

    DtDtsFreeDataTypeNames( (char**) dataTypeList );
}

/*
 * Class:     org_eclipse_swt_internal_motif_CDE
 * Method:    DtDtsFreeAttributeValue
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_CDE_DtDtsFreeAttributeValue
  (JNIEnv *env, jclass that, jint attrValue)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "DtDtsFreeAttributeValue\n");
#endif

    DtDtsFreeAttributeValue( (char*) attrValue );
}

/*
 * Class:     org_eclipse_swt_internal_motif_CDE
 * Method:    DtActionInvoke
 * Signature: (I[B[BI[B[B[BIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_CDE_DtActionInvoke
  (JNIEnv *env, jclass that, jint topWidget, jbyteArray action, 
   jbyteArray fileName, jint argCount, jbyteArray termOpts, jbyteArray execHost, 
   jbyteArray contextDir, jint useIndicator, jint callback, jint clientData)
{
    jbyte*   action1   = NULL;
    jbyte*   fileName1 = NULL;
    jbyte*   termOpts1 = NULL;
    jbyte*   execHost1 = NULL;
    jbyte*   contextDir1 = NULL;
    jint     actionID;
    DtActionArg  arg;     /* Action arguments should really be Java objects. */

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "DtActionInvoke\n");
#endif

    if (action)   action1   = (*env)->GetByteArrayElements(env, action, NULL); 
    if (fileName) fileName1 = (*env)->GetByteArrayElements(env, fileName, NULL); 
    if (termOpts) termOpts1 = (*env)->GetByteArrayElements(env, termOpts, NULL); 
    if (execHost) execHost1 = (*env)->GetByteArrayElements(env, execHost, NULL); 
    if (contextDir) contextDir1 = (*env)->GetByteArrayElements(env, contextDir, NULL); 

    /* TBD: DtActionArg should be a Java class and the third argument
     * (fileName) should really be an array of DtActionArg.
     *
     * Create the action argument given the file name. This is the only type
     * of action supported by the Program module of SWT. The argCount is 
     * ignored because only one file can be passed this way.
     */
    arg.argClass = DtACTION_FILE;
    arg.u.file.name = (char*) fileName1;

/*
	printf("Invoking action: %d %s %s 1 %X %X %X %X %X %X\n", topWidget, action1, fileName1,
	        termOpts1, execHost1, contextDir1, useIndicator, callback, clientData );
*/
    actionID = (jint) DtActionInvoke( (Widget) topWidget, (char*) action1, 
        &arg, 1, (char*) termOpts1,(char*)  execHost1, (char*) contextDir1,
        useIndicator, (DtActionCallbackProc) callback, (XtPointer) clientData );

    if (contextDir) (*env)->ReleaseByteArrayElements(env, contextDir, contextDir1, 0);
    if (execHost) (*env)->ReleaseByteArrayElements(env, execHost, execHost1, 0);
    if (termOpts) (*env)->ReleaseByteArrayElements(env, termOpts, termOpts1, 0);
    if (fileName) (*env)->ReleaseByteArrayElements(env, fileName, fileName1, 0);
    if (action)   (*env)->ReleaseByteArrayElements(env, action,   action1, 0);

    return actionID;
}

/* Utility methods */


/*
 * Class:     org_eclipse_swt_internal_motif_CDE
 * Method:    listElementAt
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_CDE_listElementAt
  (JNIEnv *env, jclass that, jint nameList, jint index)
{
	char** nameList1 = (char**) nameList;
    return (jint) nameList1[ index ];
}
