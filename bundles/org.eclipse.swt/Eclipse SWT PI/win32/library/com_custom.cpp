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
#include "com_structs.h"
#include "com_stats.h"
#include <Shlobj.h>

#define COM_NATIVE(func) Java_org_eclipse_swt_internal_ole_win32_COM_##func

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
extern "C" HRESULT PathToPIDL(PCWSTR pszName, PIDLIST_ABSOLUTE *ppidl)
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
