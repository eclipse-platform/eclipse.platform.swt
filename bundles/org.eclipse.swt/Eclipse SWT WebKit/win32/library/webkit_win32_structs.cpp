/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others. All rights reserved.
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
#include "webkit_win32_structs.h"

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
	JSClassDefinitionFc.clazz = env->GetObjectClass(lpObject);
	JSClassDefinitionFc.version = env->GetFieldID(JSClassDefinitionFc.clazz, "version", "I");
	JSClassDefinitionFc.attributes = env->GetFieldID(JSClassDefinitionFc.clazz, "attributes", "I");
	JSClassDefinitionFc.className = env->GetFieldID(JSClassDefinitionFc.clazz, "className", I_J);
	JSClassDefinitionFc.parentClass = env->GetFieldID(JSClassDefinitionFc.clazz, "parentClass", I_J);
	JSClassDefinitionFc.staticValues = env->GetFieldID(JSClassDefinitionFc.clazz, "staticValues", I_J);
	JSClassDefinitionFc.staticFunctions = env->GetFieldID(JSClassDefinitionFc.clazz, "staticFunctions", I_J);
	JSClassDefinitionFc.initialize = env->GetFieldID(JSClassDefinitionFc.clazz, "initialize", I_J);
	JSClassDefinitionFc.finalize = env->GetFieldID(JSClassDefinitionFc.clazz, "finalize", I_J);
	JSClassDefinitionFc.hasProperty = env->GetFieldID(JSClassDefinitionFc.clazz, "hasProperty", I_J);
	JSClassDefinitionFc.getProperty = env->GetFieldID(JSClassDefinitionFc.clazz, "getProperty", I_J);
	JSClassDefinitionFc.setProperty = env->GetFieldID(JSClassDefinitionFc.clazz, "setProperty", I_J);
	JSClassDefinitionFc.deleteProperty = env->GetFieldID(JSClassDefinitionFc.clazz, "deleteProperty", I_J);
	JSClassDefinitionFc.getPropertyNames = env->GetFieldID(JSClassDefinitionFc.clazz, "getPropertyNames", I_J);
	JSClassDefinitionFc.callAsFunction = env->GetFieldID(JSClassDefinitionFc.clazz, "callAsFunction", I_J);
	JSClassDefinitionFc.callAsConstructor = env->GetFieldID(JSClassDefinitionFc.clazz, "callAsConstructor", I_J);
	JSClassDefinitionFc.hasInstance = env->GetFieldID(JSClassDefinitionFc.clazz, "hasInstance", I_J);
	JSClassDefinitionFc.convertToType = env->GetFieldID(JSClassDefinitionFc.clazz, "convertToType", I_J);
	JSClassDefinitionFc.cached = 1;
}

JSClassDefinition *getJSClassDefinitionFields(JNIEnv *env, jobject lpObject, JSClassDefinition *lpStruct)
{
	if (!JSClassDefinitionFc.cached) cacheJSClassDefinitionFields(env, lpObject);
	lpStruct->version = env->GetIntField(lpObject, JSClassDefinitionFc.version);
	lpStruct->attributes = (JSClassAttributes)env->GetIntField(lpObject, JSClassDefinitionFc.attributes);
	lpStruct->className = (const char*)env->GetIntLongField(lpObject, JSClassDefinitionFc.className);
	lpStruct->parentClass = (JSClassRef)env->GetIntLongField(lpObject, JSClassDefinitionFc.parentClass);
	lpStruct->staticValues = (const JSStaticValue*)env->GetIntLongField(lpObject, JSClassDefinitionFc.staticValues);
	lpStruct->staticFunctions = (const JSStaticFunction*)env->GetIntLongField(lpObject, JSClassDefinitionFc.staticFunctions);
	lpStruct->initialize = (JSObjectInitializeCallback)env->GetIntLongField(lpObject, JSClassDefinitionFc.initialize);
	lpStruct->finalize = (JSObjectFinalizeCallback)env->GetIntLongField(lpObject, JSClassDefinitionFc.finalize);
	lpStruct->hasProperty = (JSObjectHasPropertyCallback)env->GetIntLongField(lpObject, JSClassDefinitionFc.hasProperty);
	lpStruct->getProperty = (JSObjectGetPropertyCallback)env->GetIntLongField(lpObject, JSClassDefinitionFc.getProperty);
	lpStruct->setProperty = (JSObjectSetPropertyCallback)env->GetIntLongField(lpObject, JSClassDefinitionFc.setProperty);
	lpStruct->deleteProperty = (JSObjectDeletePropertyCallback)env->GetIntLongField(lpObject, JSClassDefinitionFc.deleteProperty);
	lpStruct->getPropertyNames = (JSObjectGetPropertyNamesCallback)env->GetIntLongField(lpObject, JSClassDefinitionFc.getPropertyNames);
	lpStruct->callAsFunction = (JSObjectCallAsFunctionCallback)env->GetIntLongField(lpObject, JSClassDefinitionFc.callAsFunction);
	lpStruct->callAsConstructor = (JSObjectCallAsConstructorCallback)env->GetIntLongField(lpObject, JSClassDefinitionFc.callAsConstructor);
	lpStruct->hasInstance = (JSObjectHasInstanceCallback)env->GetIntLongField(lpObject, JSClassDefinitionFc.hasInstance);
	lpStruct->convertToType = (JSObjectConvertToTypeCallback)env->GetIntLongField(lpObject, JSClassDefinitionFc.convertToType);
	return lpStruct;
}

void setJSClassDefinitionFields(JNIEnv *env, jobject lpObject, JSClassDefinition *lpStruct)
{
	if (!JSClassDefinitionFc.cached) cacheJSClassDefinitionFields(env, lpObject);
	env->SetIntField(lpObject, JSClassDefinitionFc.version, (jint)lpStruct->version);
	env->SetIntField(lpObject, JSClassDefinitionFc.attributes, (jint)lpStruct->attributes);
	env->SetIntLongField(lpObject, JSClassDefinitionFc.className, (jintLong)lpStruct->className);
	env->SetIntLongField(lpObject, JSClassDefinitionFc.parentClass, (jintLong)lpStruct->parentClass);
	env->SetIntLongField(lpObject, JSClassDefinitionFc.staticValues, (jintLong)lpStruct->staticValues);
	env->SetIntLongField(lpObject, JSClassDefinitionFc.staticFunctions, (jintLong)lpStruct->staticFunctions);
	env->SetIntLongField(lpObject, JSClassDefinitionFc.initialize, (jintLong)lpStruct->initialize);
	env->SetIntLongField(lpObject, JSClassDefinitionFc.finalize, (jintLong)lpStruct->finalize);
	env->SetIntLongField(lpObject, JSClassDefinitionFc.hasProperty, (jintLong)lpStruct->hasProperty);
	env->SetIntLongField(lpObject, JSClassDefinitionFc.getProperty, (jintLong)lpStruct->getProperty);
	env->SetIntLongField(lpObject, JSClassDefinitionFc.setProperty, (jintLong)lpStruct->setProperty);
	env->SetIntLongField(lpObject, JSClassDefinitionFc.deleteProperty, (jintLong)lpStruct->deleteProperty);
	env->SetIntLongField(lpObject, JSClassDefinitionFc.getPropertyNames, (jintLong)lpStruct->getPropertyNames);
	env->SetIntLongField(lpObject, JSClassDefinitionFc.callAsFunction, (jintLong)lpStruct->callAsFunction);
	env->SetIntLongField(lpObject, JSClassDefinitionFc.callAsConstructor, (jintLong)lpStruct->callAsConstructor);
	env->SetIntLongField(lpObject, JSClassDefinitionFc.hasInstance, (jintLong)lpStruct->hasInstance);
	env->SetIntLongField(lpObject, JSClassDefinitionFc.convertToType, (jintLong)lpStruct->convertToType);
}
#endif

