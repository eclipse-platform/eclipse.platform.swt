/* -*- Mode: C++; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

/*
 * This file is derived from the Original Code provided by mozilla.org,
 * whose Initial Developer is Netscape Communications Corporation.  Changes
 * to the original file were made by SWT on September 18, 2013 and are marked
 * with trailing comment "//SWT-20130918".
 */

#ifndef nsIGlobalObject_h__
#define nsIGlobalObject_h__

//#include "nsISupports.h" //SWT-20130918
//#include "nsIScriptObjectPrincipal.h" //SWT-20130918

class JSObject;

#define NS_IGLOBALOBJECT_IID \
{ 0x8503e9a9, 0x530, 0x4b26,  \
{ 0xae, 0x24, 0x18, 0xca, 0x38, 0xe5, 0xed, 0x17 } }

#define nsresult int //SWT-20130918

class nsIGlobalObject /* : public nsIScriptObjectPrincipal */ //SWT-20130918
{
public:
//  NS_DECLARE_STATIC_IID_ACCESSOR(NS_IGLOBALOBJECT_IID) //SWT-20130918

  virtual nsresult QueryInterface(/* const nsIID & uuid, void **result */) = 0; //SWT-20130918
  virtual nsresult AddRef(void) = 0; //SWT-20130918
  virtual nsresult Release(void) = 0; //SWT-20130918

  virtual nsIPrincipal* GetPrincipal() = 0; //SWT-20130918

  virtual JSObject* GetGlobalJSObject() = 0;
};

//NS_DEFINE_STATIC_IID_ACCESSOR(nsIGlobalObject, //SWT-20130918
//                              NS_IGLOBALOBJECT_IID) //SWT-20130918

#endif // nsIGlobalObject_h__
