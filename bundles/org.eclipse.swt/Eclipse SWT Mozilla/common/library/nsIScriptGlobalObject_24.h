/* -*- Mode: C++; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: 2 -*- */
/* vim: set ts=2 sw=2 et tw=80: */
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

/*
 * This file is derived from the Original Code provided by mozilla.org,
 * whose Initial Developer is Netscape Communications Corporation.  Changes
 * to the original file were made by SWT on September 18, 2013 and are marked
 * with trailing comment "//SWT-20130918".
 */
 
#ifndef nsIScriptGlobalObject_h__
#define nsIScriptGlobalObject_h__

//#include "nsISupports.h" //SWT-20130918
//#include "nsEvent.h" //SWT-20130918
#include "nsIGlobalObject_24.h"  //SWT-20130918

class nsIScriptContext;
class nsScriptErrorEvent;
class nsIScriptGlobalObject;
class JSObject;

// A helper function for nsIScriptGlobalObject implementations to use
// when handling a script error.  Generally called by the global when a context
// notifies it of an error via nsIScriptGlobalObject::HandleScriptError.
// Returns true if HandleDOMEvent was actually called, in which case
// aStatus will be filled in with the status.
//bool //SWT-20130918
//NS_HandleScriptError(nsIScriptGlobalObject *aScriptGlobal, //SWT-20130918
//                     nsScriptErrorEvent *aErrorEvent, //SWT-20130918
//                     nsEventStatus *aStatus); //SWT-20130918

#define NS_ISCRIPTGLOBALOBJECT_IID \
{ 0xde24b30a, 0x12c6, 0x4e5f, \
  { 0xa8, 0x5e, 0x90, 0xcd, 0xfb, 0x6c, 0x54, 0x51 } }

#define nsresult int //SWT-20130918

/**
 * The global object which keeps a script context for each supported script
 * language. This often used to store per-window global state.
 * This is a heavyweight interface implemented only by DOM globals, and
 * it might go away some time in the future.
 */

class nsIScriptGlobalObject : public nsIGlobalObject
{
public:
//  NS_DECLARE_STATIC_IID_ACCESSOR(NS_ISCRIPTGLOBALOBJECT_IID) //SWT-20130918

  /**
   * Ensure that the script global object is initialized for working with the
   * specified script language ID.  This will set up the nsIScriptContext
   * and 'script global' for that language, allowing these to be fetched
   * and manipulated.
   * @return NS_OK if successful; error conditions include that the language
   * has not been registered, as well as 'normal' errors, such as
   * out-of-memory
   */
  virtual nsresult EnsureScriptEnvironment() = 0;
  /**
   * Get a script context (WITHOUT added reference) for the specified language.
   */
  virtual nsIScriptContext *GetScriptContext() = 0;

  nsIScriptContext* GetContext() {
    return GetScriptContext();
  }

  /**
   * Called when the global script for a language is finalized, typically as
   * part of its GC process.  By the time this call is made, the
   * nsIScriptContext for the language has probably already been removed.
   * After this call, the passed object is dead - which should generally be the
   * same object the global is using for a global for that language.
   */
  virtual void OnFinalize(JSObject* aObject) = 0;

  /**
   * Called to enable/disable scripts.
   */
  virtual void SetScriptsEnabled(bool aEnabled, bool aFireTimeouts) = 0;

  /**
   * Handle a script error.  Generally called by a script context.
   */
//  virtual nsresult HandleScriptError(nsScriptErrorEvent *aErrorEvent, //SWT-20130918
//                                     nsEventStatus *aEventStatus) { //SWT-20130918
//    NS_ENSURE_STATE(NS_HandleScriptError(this, aErrorEvent, aEventStatus)); //SWT-20130918
//    return NS_OK; //SWT-20130918
//  } //SWT-20130918

  virtual bool IsBlackForCC() { return false; }
};

//NS_DEFINE_STATIC_IID_ACCESSOR(nsIScriptGlobalObject,  //SWT-20130918
//                              NS_ISCRIPTGLOBALOBJECT_IID)  //SWT-20130918

#endif
