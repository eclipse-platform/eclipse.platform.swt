/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "com_structs.h"
#include "com_stats.h"

#define COM_NATIVE(func) Java_org_eclipse_swt_internal_ole_win32_COM_##func

#ifndef NO_get_1accChild_1CALLBACK
static jintLong get_accChild_CALLBACK;
static HRESULT CALLBACK get_accChild(void* ppVTable, VARIANT varChildID, IDispatch** ppdispChild)
{
	return ((HRESULT (CALLBACK *)(void*, VARIANT*, IDispatch**))get_accChild_CALLBACK)(ppVTable, &varChildID, ppdispChild);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(get_1accChild_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	get_accChild_CALLBACK = func;
	return (jintLong)get_accChild;
}
#endif

#ifndef NO_get_1accName_1CALLBACK
static jintLong get_accName_CALLBACK;
static HRESULT CALLBACK get_accName(void* ppVTable, VARIANT varID, BSTR* pszName)
{
	return ((HRESULT (CALLBACK *)(void*, VARIANT*, BSTR*))get_accName_CALLBACK)(ppVTable, &varID, pszName);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(get_1accName_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	get_accName_CALLBACK = func;
	return (jintLong)get_accName;
}
#endif

#ifndef NO_get_1accValue_1CALLBACK
static jintLong get_accValue_CALLBACK;
static HRESULT CALLBACK get_accValue(void* ppVTable, VARIANT varID, BSTR* pszValue)
{
	return ((HRESULT (CALLBACK *)(void*, VARIANT*, BSTR*))get_accValue_CALLBACK)(ppVTable, &varID, pszValue);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(get_1accValue_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	get_accValue_CALLBACK = func;
	return (jintLong)get_accValue;
}
#endif

#ifndef NO_get_1accDescription_1CALLBACK
static jintLong get_accDescription_CALLBACK;
static HRESULT CALLBACK get_accDescription( void* ppVTable, VARIANT varID, BSTR* pszDescription)
{
	return ((HRESULT (CALLBACK *)(void*, VARIANT*, BSTR*))get_accDescription_CALLBACK)(ppVTable, &varID, pszDescription);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(get_1accDescription_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	get_accDescription_CALLBACK = func;
	return (jintLong)get_accDescription;
}
#endif

#ifndef NO_get_1accRole_1CALLBACK
static jintLong get_accRole_CALLBACK;
static HRESULT CALLBACK get_accRole(void* ppVTable, VARIANT varID, VARIANT* pvarRole)
{
	return ((HRESULT (CALLBACK *)(void*, VARIANT*, VARIANT*))get_accRole_CALLBACK)(ppVTable, &varID, pvarRole);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(get_1accRole_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	get_accRole_CALLBACK = func;
	return (jintLong)get_accRole;
}
#endif

#ifndef NO_get_1accState_1CALLBACK
static jintLong get_accState_CALLBACK;
static HRESULT CALLBACK get_accState(void* ppVTable, VARIANT varID, VARIANT* pvarState)
{
	return ((HRESULT (CALLBACK *)(void*, VARIANT*, VARIANT*))get_accState_CALLBACK)(ppVTable, &varID, pvarState);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(get_1accState_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	get_accState_CALLBACK = func;
	return (jintLong)get_accState;
}
#endif

#ifndef NO_get_1accHelp_1CALLBACK
static jintLong get_accHelp_CALLBACK;
static HRESULT CALLBACK get_accHelp(void* ppVTable, VARIANT varID, BSTR* pszHelp)
{
	return ((HRESULT (CALLBACK *)(void*, VARIANT*, BSTR*))get_accHelp_CALLBACK)(ppVTable, &varID, pszHelp);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(get_1accHelp_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	get_accHelp_CALLBACK = func;
	return (jintLong)get_accHelp;
}
#endif

#ifndef NO_get_1accHelpTopic_1CALLBACK
static jintLong get_accHelpTopic_CALLBACK;
static HRESULT CALLBACK get_accHelpTopic(void* ppVTable, BSTR* pszHelpFile, VARIANT varChild, long* pidTopic)
{
	return ((HRESULT (CALLBACK *)(void*, BSTR*, VARIANT*, long*))get_accHelpTopic_CALLBACK)(ppVTable, pszHelpFile, &varChild, pidTopic);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(get_1accHelpTopic_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	get_accHelpTopic_CALLBACK = func;
	return (jintLong)get_accHelpTopic;
}
#endif

#ifndef NO_get_1accKeyboardShortcut_1CALLBACK
static jintLong get_accKeyboardShortcut_CALLBACK;
static HRESULT CALLBACK get_accKeyboardShortcut(void* ppVTable, VARIANT varID, BSTR* pszKeyboardShortcut)
{
	return ((HRESULT (CALLBACK *)(void*, VARIANT*, BSTR*))get_accKeyboardShortcut_CALLBACK)(ppVTable, &varID, pszKeyboardShortcut);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(get_1accKeyboardShortcut_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	get_accKeyboardShortcut_CALLBACK = func;
	return (jintLong)get_accKeyboardShortcut;
}
#endif

#ifndef NO_get_1accDefaultAction_1CALLBACK
static jintLong get_accDefaultAction_CALLBACK;
static HRESULT CALLBACK get_accDefaultAction(void* ppVTable, VARIANT varID, BSTR* pszDefaultAction)
{
	return ((HRESULT (CALLBACK *)(void*, VARIANT*, BSTR*))get_accDefaultAction_CALLBACK)(ppVTable, &varID, pszDefaultAction);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(get_1accDefaultAction_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	get_accDefaultAction_CALLBACK = func;
	return (jintLong)get_accDefaultAction;
}
#endif

#ifndef NO_accSelect_1CALLBACK
static jintLong accSelect_CALLBACK;
static HRESULT CALLBACK accSelect(void* ppVTable, long flagsSelect, VARIANT varID)
{
	return ((HRESULT (CALLBACK *)(void*, long, VARIANT*))accSelect_CALLBACK)(ppVTable, flagsSelect, &varID);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(accSelect_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	accSelect_CALLBACK = func;
	return (jintLong)accSelect;
}
#endif

#ifndef NO_accLocation_1CALLBACK
static jintLong accLocation_CALLBACK;
static HRESULT CALLBACK accLocation(void* ppVTable, long* pxLeft, long* pyTop, long* pcxWidth, long* pcyHeight, VARIANT varID) 
{
	return ((HRESULT (CALLBACK *)(void*, long*, long*, long*, long*, VARIANT*))accLocation_CALLBACK)(ppVTable, pxLeft, pyTop, pcxWidth, pcyHeight, &varID);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(accLocation_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	accLocation_CALLBACK = func;
	return (jintLong)accLocation;
}
#endif

#ifndef NO_accNavigate_1CALLBACK
static jintLong accNavigate_CALLBACK;
static HRESULT CALLBACK accNavigate(void* ppVTable, long navDir, VARIANT varStart, VARIANT* pvarEnd)
{
	return ((HRESULT (CALLBACK *)(void*, long, VARIANT*, VARIANT*))accNavigate_CALLBACK)(ppVTable, navDir, &varStart, pvarEnd);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(accNavigate_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	accNavigate_CALLBACK = func;
	return (jintLong)accNavigate;
}
#endif

#ifndef NO_accDoDefaultAction_1CALLBACK
static jintLong accDoDefaultAction_CALLBACK;
static HRESULT CALLBACK accDoDefaultAction(void* ppVTable, VARIANT varID)
{
	return ((HRESULT (CALLBACK *)(void*, VARIANT*))accDoDefaultAction_CALLBACK)(ppVTable, &varID);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(accDoDefaultAction_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	accDoDefaultAction_CALLBACK = func;
	return (jintLong)accDoDefaultAction;
}
#endif

#ifndef NO_put_1accName_1CALLBACK
static jintLong put_accName_CALLBACK;
static HRESULT CALLBACK put_accName(void* ppVTable, VARIANT varID, BSTR* pszName)
{
	return ((HRESULT (CALLBACK *)(void*, VARIANT*, BSTR*))put_accName_CALLBACK)(ppVTable, &varID, pszName);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(put_1accName_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	put_accName_CALLBACK = func;
	return (jintLong)put_accName;
}
#endif

#ifndef NO_put_1accValue_1CALLBACK
static jintLong put_accValue_CALLBACK;
static HRESULT CALLBACK put_accValue(void* ppVTable, VARIANT varID, BSTR* pszValue)
{
	return ((HRESULT (CALLBACK *)(void*, VARIANT*, BSTR*))put_accValue_CALLBACK)(ppVTable, &varID, pszValue);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(put_1accValue_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	put_accValue_CALLBACK = func;
	return (jintLong)put_accValue;
}
#endif

#ifndef NO_CALLBACK_1setCurrentValue
static jintLong CALLBACK_setCurrentValue;
static HRESULT CALLBACK setCurrentValue(void* ppVTable, VARIANT arg0) {
	return ((HRESULT (CALLBACK *)(void*, VARIANT*))CALLBACK_setCurrentValue)(ppVTable, &arg0);
}
JNIEXPORT jintLong JNICALL COM_NATIVE(CALLBACK_1setCurrentValue)
	(JNIEnv *env, jclass that, jintLong func)
{
	CALLBACK_setCurrentValue = func;
	return (jintLong)setCurrentValue;
}
#endif

#ifndef NO_VtblCall_1PPPPVARIANT
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall_1PPPPVARIANT)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jintLong arg6)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall_1PPPPVARIANT_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jintLong, jintLong, jintLong, jintLong, VARIANT))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, *(VARIANT *)arg6);
	COM_NATIVE_EXIT(env, that, VtblCall_1PPPPVARIANT_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall_1IVARIANT
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall_1IVARIANT)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintLong arg3)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall_1IVARIANT_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, VARIANT))(*(jintLong **)arg1)[arg0])(arg1, arg2, *(VARIANT *)arg3);
	COM_NATIVE_EXIT(env, that, VtblCall_1IVARIANT_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall_1PVARIANTP
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall_1PVARIANTP)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall_1PVARIANTP_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jintLong, VARIANT, jintLong))(*(jintLong **)arg1)[arg0])(arg1, arg2, *(VARIANT *)arg3, arg4);
	COM_NATIVE_EXIT(env, that, VtblCall_1PVARIANTP_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall_1IVARIANTP
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall_1IVARIANTP)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintLong arg3, jintLong arg4)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall_1IVARIANTP_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, VARIANT, jintLong))(*(jintLong **)arg1)[arg0])(arg1, arg2, *(VARIANT *)arg3, arg4);
	COM_NATIVE_EXIT(env, that, VtblCall_1IVARIANTP_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall_1VARIANT
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall_1VARIANT)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintLong arg2)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall_1VARIANT_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, VARIANT))(*(jintLong **)arg1)[arg0])(arg1, *(VARIANT *)arg2);
	COM_NATIVE_EXIT(env, that, VtblCall_1VARIANT_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall_1VARIANTI
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall_1VARIANTP)
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintLong arg2, jintLong arg3)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall_1VARIANTP_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, VARIANT, jintLong))(*(jintLong **)arg1)[arg0])(arg1, *(VARIANT *)arg2, arg3);
	COM_NATIVE_EXIT(env, that, VtblCall_1VARIANTP_FUNC);
	return rc;
}
#endif
