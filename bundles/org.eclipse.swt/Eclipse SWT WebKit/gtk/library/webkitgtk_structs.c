/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "webkitgtk_structs.h"

#ifndef NO_JSClassDefinition
typedef struct JSClassDefinition_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID version, attributes, className, parentClass, staticValues, staticFunctions, initialize, finalize, hasProperty, getProperty, setProperty, deleteProperty, getPropertyNames, callAsFunction, callAsConstructor, hasInstance, convertToType;
} JSClassDefinition_FID_CACHE;

JSClassDefinition_FID_CACHE JSClassDefinitionFc;

void cacheJSClassDefinitionFields(JNIEnv *env, jobject lpObject)
{
	if (JSClassDefinitionFc.cached) return;
	JSClassDefinitionFc.clazz = (*env)->GetObjectClass(env, lpObject);
	JSClassDefinitionFc.version = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "version", "I");
	JSClassDefinitionFc.attributes = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "attributes", "I");
	JSClassDefinitionFc.className = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "className", I_J);
	JSClassDefinitionFc.parentClass = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "parentClass", I_J);
	JSClassDefinitionFc.staticValues = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "staticValues", I_J);
	JSClassDefinitionFc.staticFunctions = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "staticFunctions", I_J);
	JSClassDefinitionFc.initialize = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "initialize", I_J);
	JSClassDefinitionFc.finalize = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "finalize", I_J);
	JSClassDefinitionFc.hasProperty = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "hasProperty", I_J);
	JSClassDefinitionFc.getProperty = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "getProperty", I_J);
	JSClassDefinitionFc.setProperty = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "setProperty", I_J);
	JSClassDefinitionFc.deleteProperty = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "deleteProperty", I_J);
	JSClassDefinitionFc.getPropertyNames = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "getPropertyNames", I_J);
	JSClassDefinitionFc.callAsFunction = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "callAsFunction", I_J);
	JSClassDefinitionFc.callAsConstructor = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "callAsConstructor", I_J);
	JSClassDefinitionFc.hasInstance = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "hasInstance", I_J);
	JSClassDefinitionFc.convertToType = (*env)->GetFieldID(env, JSClassDefinitionFc.clazz, "convertToType", I_J);
	JSClassDefinitionFc.cached = 1;
}

JSClassDefinition *getJSClassDefinitionFields(JNIEnv *env, jobject lpObject, JSClassDefinition *lpStruct)
{
	if (!JSClassDefinitionFc.cached) cacheJSClassDefinitionFields(env, lpObject);
	lpStruct->version = (*env)->GetIntField(env, lpObject, JSClassDefinitionFc.version);
	lpStruct->attributes = (JSClassAttributes)(*env)->GetIntField(env, lpObject, JSClassDefinitionFc.attributes);
	lpStruct->className = (const char*)(*env)->GetIntLongField(env, lpObject, JSClassDefinitionFc.className);
	lpStruct->parentClass = (JSClassRef)(*env)->GetIntLongField(env, lpObject, JSClassDefinitionFc.parentClass);
	lpStruct->staticValues = (const JSStaticValue*)(*env)->GetIntLongField(env, lpObject, JSClassDefinitionFc.staticValues);
	lpStruct->staticFunctions = (const JSStaticFunction*)(*env)->GetIntLongField(env, lpObject, JSClassDefinitionFc.staticFunctions);
	lpStruct->initialize = (JSObjectInitializeCallback)(*env)->GetIntLongField(env, lpObject, JSClassDefinitionFc.initialize);
	lpStruct->finalize = (JSObjectFinalizeCallback)(*env)->GetIntLongField(env, lpObject, JSClassDefinitionFc.finalize);
	lpStruct->hasProperty = (JSObjectHasPropertyCallback)(*env)->GetIntLongField(env, lpObject, JSClassDefinitionFc.hasProperty);
	lpStruct->getProperty = (JSObjectGetPropertyCallback)(*env)->GetIntLongField(env, lpObject, JSClassDefinitionFc.getProperty);
	lpStruct->setProperty = (JSObjectSetPropertyCallback)(*env)->GetIntLongField(env, lpObject, JSClassDefinitionFc.setProperty);
	lpStruct->deleteProperty = (JSObjectDeletePropertyCallback)(*env)->GetIntLongField(env, lpObject, JSClassDefinitionFc.deleteProperty);
	lpStruct->getPropertyNames = (JSObjectGetPropertyNamesCallback)(*env)->GetIntLongField(env, lpObject, JSClassDefinitionFc.getPropertyNames);
	lpStruct->callAsFunction = (JSObjectCallAsFunctionCallback)(*env)->GetIntLongField(env, lpObject, JSClassDefinitionFc.callAsFunction);
	lpStruct->callAsConstructor = (JSObjectCallAsConstructorCallback)(*env)->GetIntLongField(env, lpObject, JSClassDefinitionFc.callAsConstructor);
	lpStruct->hasInstance = (JSObjectHasInstanceCallback)(*env)->GetIntLongField(env, lpObject, JSClassDefinitionFc.hasInstance);
	lpStruct->convertToType = (JSObjectConvertToTypeCallback)(*env)->GetIntLongField(env, lpObject, JSClassDefinitionFc.convertToType);
	return lpStruct;
}

void setJSClassDefinitionFields(JNIEnv *env, jobject lpObject, JSClassDefinition *lpStruct)
{
	if (!JSClassDefinitionFc.cached) cacheJSClassDefinitionFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, JSClassDefinitionFc.version, (jint)lpStruct->version);
	(*env)->SetIntField(env, lpObject, JSClassDefinitionFc.attributes, (jint)lpStruct->attributes);
	(*env)->SetIntLongField(env, lpObject, JSClassDefinitionFc.className, (jintLong)lpStruct->className);
	(*env)->SetIntLongField(env, lpObject, JSClassDefinitionFc.parentClass, (jintLong)lpStruct->parentClass);
	(*env)->SetIntLongField(env, lpObject, JSClassDefinitionFc.staticValues, (jintLong)lpStruct->staticValues);
	(*env)->SetIntLongField(env, lpObject, JSClassDefinitionFc.staticFunctions, (jintLong)lpStruct->staticFunctions);
	(*env)->SetIntLongField(env, lpObject, JSClassDefinitionFc.initialize, (jintLong)lpStruct->initialize);
	(*env)->SetIntLongField(env, lpObject, JSClassDefinitionFc.finalize, (jintLong)lpStruct->finalize);
	(*env)->SetIntLongField(env, lpObject, JSClassDefinitionFc.hasProperty, (jintLong)lpStruct->hasProperty);
	(*env)->SetIntLongField(env, lpObject, JSClassDefinitionFc.getProperty, (jintLong)lpStruct->getProperty);
	(*env)->SetIntLongField(env, lpObject, JSClassDefinitionFc.setProperty, (jintLong)lpStruct->setProperty);
	(*env)->SetIntLongField(env, lpObject, JSClassDefinitionFc.deleteProperty, (jintLong)lpStruct->deleteProperty);
	(*env)->SetIntLongField(env, lpObject, JSClassDefinitionFc.getPropertyNames, (jintLong)lpStruct->getPropertyNames);
	(*env)->SetIntLongField(env, lpObject, JSClassDefinitionFc.callAsFunction, (jintLong)lpStruct->callAsFunction);
	(*env)->SetIntLongField(env, lpObject, JSClassDefinitionFc.callAsConstructor, (jintLong)lpStruct->callAsConstructor);
	(*env)->SetIntLongField(env, lpObject, JSClassDefinitionFc.hasInstance, (jintLong)lpStruct->hasInstance);
	(*env)->SetIntLongField(env, lpObject, JSClassDefinitionFc.convertToType, (jintLong)lpStruct->convertToType);
}
#endif

