/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *    Paul Pazderski - Bug 547634: PathToPIDL
 *******************************************************************************/

#include "swt.h"
#include <Shlobj.h>

#define COM_NATIVE(func) Java_org_eclipse_swt_internal_ole_win32_COM_##func

extern "C" {

class CFileSysBindData : public IFileSystemBindData
{
public:
	CFileSysBindData() : refCount(1)
	{
		ZeroMemory(&findData, sizeof(findData));
	}

	IFACEMETHODIMP QueryInterface(REFIID riid, void **ppv)
	{
		*ppv = nullptr;
		HRESULT hr = E_NOINTERFACE;
		if (riid == IID_IUnknown || riid == IID_IFileSystemBindData) {
			*ppv = static_cast<IFileSystemBindData*>(this);
			AddRef();
			hr = S_OK;
		}
		return hr;
	}

	IFACEMETHODIMP_(ULONG) AddRef()
	{
		return InterlockedIncrement(&refCount);
	}

	IFACEMETHODIMP_(ULONG) Release()
	{
		long rc = InterlockedDecrement(&refCount);
		if (!rc)
			delete this;
		return rc;
	}

	IFACEMETHODIMP SetFindData(const WIN32_FIND_DATAW *pfd)
	{
		findData = *pfd;
		return S_OK;
	}

	IFACEMETHODIMP GetFindData(WIN32_FIND_DATAW *pfd)
	{
		*pfd = findData;
		return S_OK;
	}

private:
	long refCount;
	WIN32_FIND_DATAW findData;
};

/*
 * An extended version of SHParseDisplayName which use bind context to support
 * creation of simple PIDLs in case the normal PIDL creation failed.
 * (most likley due to non existing file/directory)
 */
HRESULT PathToPIDL(PCWSTR pszName, PIDLIST_ABSOLUTE *ppidl)
{
	if (!ppidl) return E_FAIL;
	*ppidl = nullptr;
	
	SFGAOF sfgao = 0;
	HRESULT hr = SHParseDisplayName(pszName, nullptr, ppidl, sfgao, &sfgao);
	if (hr == S_OK) return hr;

	IFileSystemBindData *pfsbd = new CFileSysBindData();
	if (!pfsbd) return E_OUTOFMEMORY;

	WIN32_FIND_DATAW data = {};
	pfsbd->SetFindData(&data);

	IBindCtx* pbc;

	hr = CreateBindCtx(0, &pbc);
	if (hr == S_OK)
	{
		BIND_OPTS bo = { sizeof(bo), 0, STGM_CREATE, 0 };
		hr = pbc->SetBindOptions(&bo);
		if (hr == S_OK)
		{
			hr = pbc->RegisterObjectParam(STR_FILE_SYS_BIND_DATA, pfsbd);
			if (hr == S_OK)
			{
				sfgao = 0;
				hr = SHParseDisplayName(pszName, pbc, ppidl, sfgao, &sfgao);
			}
		}
		pbc->Release();
	}
	pfsbd->Release();
	return hr;
}

// Stand-in for all WebView2 *EventHandler and *CompletedHandler interfaces.
interface ISwtWebView2Callback : IUnknown {
	virtual HRESULT Invoke(UINT_PTR arg0, UINT_PTR arg1) = 0;
};

class SwtWebView2Callback : public ISwtWebView2Callback {
	ULONG refCount;
	jobject object;
	jmethodID methodID;

	SwtWebView2Callback(jobject object, jmethodID methodID)
		: refCount(1), object(object), methodID(methodID) {}

	~SwtWebView2Callback() {
		JNIEnv *env = nullptr;
		if (JVM->GetEnv((void **)&env, JNI_VERSION_1_2) != JNI_OK) {
			return;
		}
		env->DeleteGlobalRef(object);
	}

public:
	static ISwtWebView2Callback *Create(JNIEnv *env, jobject callback) {
		jclass cls = env->GetObjectClass(callback);
		jmethodID methodID = env->GetMethodID(cls, "Invoke", "(JJ)I");
		jobject object = env->NewGlobalRef(callback);
		if (object == nullptr) return nullptr;
		return new SwtWebView2Callback(object, methodID);
	}

	// IUnknown

	HRESULT QueryInterface(REFIID riid, void **ppv) override {
		*ppv = nullptr;
		if (riid == IID_IUnknown) {
			*ppv = this;
			AddRef();
			return S_OK;
		}
		return E_NOINTERFACE;
	}

	ULONG AddRef() override {
		return ++refCount;
	}

	ULONG Release() override {
		long rc = --refCount;
		if (!rc) delete this;
		return rc;
	}

	// ISwtWebView2Callback

	HRESULT Invoke(UINT_PTR arg0, UINT_PTR arg1) override {
		JNIEnv *env = nullptr;
		if (JVM->GetEnv((void **)&env, JNI_VERSION_1_2) != JNI_OK) {
			return E_FAIL;
		}
		if (env->ExceptionCheck()) {
			return E_FAIL;
		}
		HRESULT result = (HRESULT)env->CallIntMethod(
				object, methodID, (jlong)arg0, (jlong)arg1);
		if (env->ExceptionCheck()) {
			return E_FAIL;
		}
		return result;
	}
};

// Callback interface exposed to WebView2 via AddHostObjectToScript.
interface ISwtWebView2Host : IDispatch {
	virtual BSTR CallJava(int index, BSTR token, BSTR args) = 0;
};

class SwtWebView2Host : public ISwtWebView2Host {
	ULONG refCount;
	jobject object;
	jmethodID methodID;
	ITypeInfo *pTypeInfo;

	SwtWebView2Host(jobject object, jmethodID methodID, ITypeInfo *pTypeInfo)
		: refCount(1), object(object), methodID(methodID), pTypeInfo(pTypeInfo) {}

	~SwtWebView2Host() {
		pTypeInfo->Release();
		JNIEnv *env = nullptr;
		if (JVM->GetEnv((void **)&env, JNI_VERSION_1_2) != JNI_OK) {
			return;
		}
		env->DeleteGlobalRef(object);
	}

public:
	static IDispatch* Create(JNIEnv *env, jobject host) {
		jclass cls = env->GetObjectClass(host);
		jmethodID methodID = env->GetMethodID(cls, "CallJava", "(IJJ)J");
		jobject object = env->NewGlobalRef(host);
		ITypeInfo *pTypeInfo = nullptr;
		if (object == nullptr) goto error;

		// NB: CreateDispTypeInfo doesn't support parameters
		static PARAMDATA params[] = {
			{L"index", VT_I4}, {L"token", VT_BSTR}, {L"args", VT_BSTR}
		};
		static METHODDATA methods[] = {
			{L"CallJava", params, 1, 7, CC_STDCALL, ARRAYSIZE(params), DISPATCH_METHOD, VT_BSTR},
		};
		static INTERFACEDATA iface = {methods, 1};
		HRESULT hr = CreateDispTypeInfo(&iface, LOCALE_NEUTRAL, &pTypeInfo);
		if (hr != S_OK) goto error;

		return new SwtWebView2Host(object, methodID, pTypeInfo);

	error:
		if (object != nullptr) env->DeleteGlobalRef(object);
		if (pTypeInfo != nullptr) pTypeInfo->Release();
		return nullptr;
	}

	// IUnknown

	HRESULT QueryInterface(REFIID riid, void **ppv) override {
		*ppv = nullptr;
		if (riid == IID_IUnknown || riid == IID_IDispatch) {
			*ppv = this;
			AddRef();
			return S_OK;
		}
		return E_NOINTERFACE;
	}

	ULONG AddRef() override {
		return ++refCount;
	}

	ULONG Release() override {
		long rc = --refCount;
		if (!rc) delete this;
		return rc;
	}

	// IDispatch

	HRESULT GetTypeInfoCount(UINT *pctinfo) override {
		*pctinfo = 1;
		return S_OK;
	}

	HRESULT GetTypeInfo(UINT iTInfo, LCID lcid, ITypeInfo **ppTInfo) override {
    	/* Suppress warnings about unreferenced parameters */
    	(void)lcid;

		*ppTInfo = nullptr;
		if (iTInfo != 0) {
			return DISP_E_BADINDEX;
		}
		pTypeInfo->AddRef();
		*ppTInfo = pTypeInfo;
		return S_OK;
	}

	HRESULT GetIDsOfNames(REFIID riid, LPOLESTR *rgszNames, UINT cNames,
			LCID lcid, DISPID *rgDispId) override {
    	/* Suppress warnings about unreferenced parameters */
    	(void)lcid;
    	(void)riid;

		return pTypeInfo->GetIDsOfNames(rgszNames, cNames, rgDispId);
	}

	HRESULT Invoke(DISPID dispIdMember, REFIID riid, LCID lcid,
			WORD wFlags, DISPPARAMS *pDispParams, VARIANT *pVarResult,
			EXCEPINFO *pExcepInfo, UINT *puArgErr) override {
    	/* Suppress warnings about unreferenced parameters */
    	(void)lcid;
    	(void)riid;

		return pTypeInfo->Invoke(this, dispIdMember, wFlags, pDispParams,
				pVarResult, pExcepInfo, puArgErr);
	}

	// ISwtWebView2Host

	BSTR CallJava(int index, BSTR token, BSTR args) override {
		JNIEnv *env = nullptr;
		if (JVM->GetEnv((void **)&env, JNI_VERSION_1_2) != JNI_OK) {
			return NULL;
		}
		if (env->ExceptionCheck()) {
			return NULL;
		}
		BSTR result = (BSTR)env->CallLongMethod(
				object, methodID, index, (jlong)token, (jlong)args);
		if (env->ExceptionCheck()) {
			return NULL;
		}
		return result;
	}
};

static const IID IID_ICoreWebView2EnvironmentOptions =
	{0x2fde08a8, 0x1e9a, 0x4766, {0x8c, 0x05, 0x95, 0xa9, 0xce, 0xb9, 0xd1, 0xc5}};

interface ICoreWebView2EnvironmentOptions : IUnknown {
	virtual HRESULT get_AdditionalBrowserArguments(LPWSTR *value) = 0;
	virtual HRESULT put_AdditionalBrowserArguments(LPCWSTR value) = 0;
	virtual HRESULT get_Language(LPWSTR *value) = 0;
	virtual HRESULT put_Language(LPCWSTR value) = 0;
	virtual HRESULT get_TargetCompatibleBrowserVersion(LPWSTR *value) = 0;
	virtual HRESULT put_TargetCompatibleBrowserVersion(LPCWSTR value) = 0;
	virtual HRESULT get_AllowSingleSignOnUsingOSPrimaryAccount(BOOL *allow) = 0;
	virtual HRESULT put_AllowSingleSignOnUsingOSPrimaryAccount(BOOL allow) = 0;
};

class SwtWebView2Options : public ICoreWebView2EnvironmentOptions {
	ULONG refCount = 1;
	LPWSTR args = nullptr;
	LPWSTR language = nullptr;
	LPWSTR version = nullptr;
	BOOL allowSSO = FALSE;

	HRESULT CopyString(LPCWSTR pszSrc, LPWSTR *ppszDest) {
		if (pszSrc) {
			size_t cbSize = (wcslen(pszSrc) + 1) * sizeof(WCHAR);
			*ppszDest = (LPWSTR)CoTaskMemAlloc(cbSize);
			if (!*ppszDest) return E_OUTOFMEMORY;
			memcpy(*ppszDest, pszSrc, cbSize);
		} else {
			*ppszDest = nullptr;
		}
		return S_OK;
	}

	~SwtWebView2Options() {
		CoTaskMemFree(args);
		CoTaskMemFree(language);
		CoTaskMemFree(version);
	}

public:
	static IUnknown *Create() {
		return new SwtWebView2Options();
	}

	// IUnknown

	HRESULT QueryInterface(REFIID riid, void **ppv) override {
		*ppv = nullptr;
		if (riid == IID_IUnknown || riid == IID_ICoreWebView2EnvironmentOptions) {
			*ppv = this;
			AddRef();
			return S_OK;
		}
		return E_NOINTERFACE;
	}

	ULONG AddRef() override {
		return ++refCount;
	}

	ULONG Release() override {
		long rc = --refCount;
		if (!rc) delete this;
		return rc;
	}

	// ICoreWebView2EnvironmentOptions

	HRESULT get_AdditionalBrowserArguments(LPWSTR* value) override {
		return CopyString(args, value);
	}

	HRESULT put_AdditionalBrowserArguments(LPCWSTR value) override {
		CoTaskMemFree(args);
		return CopyString(value, &args);
	}

	HRESULT get_Language(LPWSTR* value) override {
		return CopyString(language, value);
	}

	HRESULT put_Language(LPCWSTR value) override {
		CoTaskMemFree(language);
		return CopyString(value, &language);
	}

	HRESULT get_TargetCompatibleBrowserVersion(LPWSTR* value) override {
		return CopyString(version, value);
	}

	HRESULT put_TargetCompatibleBrowserVersion(LPCWSTR value) override {
		CoTaskMemFree(version);
		return CopyString(value, &version);
	}

	HRESULT get_AllowSingleSignOnUsingOSPrimaryAccount(BOOL* allow) override {
		*allow = allowSSO;
		return S_OK;
	}

	HRESULT put_AllowSingleSignOnUsingOSPrimaryAccount(BOOL allow) override {
		allowSSO = allow;
		return S_OK;
	}
};

JNIEXPORT jlong JNICALL COM_NATIVE(CreateSwtWebView2Callback)
	(JNIEnv *env, jclass that, jobject callback)
{
	/* Suppress warnings about unreferenced parameters */
	(void)that;

	return (jlong)SwtWebView2Callback::Create(env, callback);
}

JNIEXPORT jlong JNICALL COM_NATIVE(CreateSwtWebView2Host)
	(JNIEnv *env, jclass that, jobject host)
{
	/* Suppress warnings about unreferenced parameters */
	(void)that;

	return (jlong)SwtWebView2Host::Create(env, host);
}

JNIEXPORT jlong JNICALL COM_NATIVE(CreateSwtWebView2Options)
	(JNIEnv *env, jclass that, jobject host)
{
	/* Suppress warnings about unreferenced parameters */
	(void)env;
	(void)that;
	(void)host;

	return (jlong)SwtWebView2Options::Create();
}

} // extern "C"
