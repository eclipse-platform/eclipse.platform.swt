/*******************************************************************************
 * Copyright (c) 2003, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Neil Rashbrook <neil@parkwaycc.co.uk> - Bug 429739
 *     Matthew Painter <matthew.painter@import.io>
 *******************************************************************************/
package org.eclipse.swt.browser;

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.mozilla.*;
import org.eclipse.swt.internal.mozilla.init.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class Mozilla extends WebBrowser {
	long /*int*/ embedHandle;
	nsIWebBrowser webBrowser;
	Object webBrowserObject;
	MozillaDelegate delegate;

	/* Interfaces for this Mozilla embedding notification */
	XPCOMObject supports;
	XPCOMObject weakReference;
	XPCOMObject webProgressListener;
	XPCOMObject webProgressListener_24;
	XPCOMObject	webBrowserChrome;
	XPCOMObject webBrowserChromeFocus;
	XPCOMObject embeddingSiteWindow;
	XPCOMObject embeddingSiteWindow_24;
	XPCOMObject interfaceRequestor;
	XPCOMObject supportsWeakReference;
	XPCOMObject contextMenuListener;
	XPCOMObject uriContentListener;
	XPCOMObject tooltipListener;
	XPCOMObject domEventListener;
	XPCOMObject badCertListener;

	int chromeFlags = nsIWebBrowserChrome.CHROME_DEFAULT;
	int registerFunctionsOnState = 0;
	int refCount, lastKeyCode, lastCharCode, authCount;
	long /*int*/ request, badCertRequest;
	Point location, size;
	boolean visible, isActive, isChild, ignoreDispose, isRetrievingBadCert, isViewingErrorPage, ignoreAllMessages, untrustedText;
	boolean updateLastNavigateUrl;
	Shell tip = null;
	Listener listener;
	List<LONG> unhookedDOMWindows = new ArrayList<> ();
	String lastNavigateURL;
	byte[] htmlBytes;

	static nsIAppShell AppShell;
	static AppFileLocProvider LocationProvider;
	static WindowCreator2 WindowCreator;
	static int BrowserCount, NextJSFunctionIndex = 1;
	static Map<Integer, BrowserFunction> AllFunctions = new HashMap<> ();
	static Listener DisplayListener;
	static boolean Initialized, IsXULRunner, PerformedVersionCheck, XPCOMWasGlued, XPCOMInitWasGlued;
	static boolean IsGettingSiteWindow;
	static String MozillaPath;
	static String oldProxyHostFTP, oldProxyHostHTTP, oldProxyHostSSL;
	static int oldProxyPortFTP = -1, oldProxyPortHTTP = -1, oldProxyPortSSL = -1, oldProxyType = -1;
	static byte[] jsLibPathBytes;
	static byte[] pathBytes_NSFree;

	/* XULRunner detect constants */
	static final String GCC3 = "-gcc3"; //$NON-NLS-1$
	static final String GRERANGE_LOWER = "1.8.1.2"; //$NON-NLS-1$
	static final String GRERANGE_LOWER_FALLBACK = "1.8"; //$NON-NLS-1$
	static final boolean LowerRangeInclusive = true;
	static final String GRERANGE_UPPER = "1.9.*"; //$NON-NLS-1$
	static final boolean UpperRangeInclusive = true;
	static final String PROPERTY_ABI = "abi"; //$NON-NLS-1$

	static final int MAX_PORT = 65535;
	static final String DEFAULTVALUE_STRING = "default"; //$NON-NLS-1$
	static final char SEPARATOR_OS = System.getProperty ("file.separator").charAt (0); //$NON-NLS-1$
	static final String ABOUT_BLANK = "about:blank"; //$NON-NLS-1$
	static final String DISPOSE_LISTENER_HOOKED = "org.eclipse.swt.browser.Mozilla.disposeListenerHooked"; //$NON-NLS-1$
	static final String HEADER_CONTENTLENGTH = "content-length"; //$NON-NLS-1
	static final String HEADER_CONTENTTYPE = "content-type"; //$NON-NLS-1
	static final String MIMETYPE_FORMURLENCODED = "application/x-www-form-urlencoded"; //$NON-NLS-1$
	static final String MOZILLA_FIVE_HOME = "MOZILLA_FIVE_HOME"; //$NON-NLS-1$
	static final String PREFIX_JAVASCRIPT = "javascript:"; //$NON-NLS-1$
	static final String PREFERENCE_CHARSET = "intl.charset.default"; //$NON-NLS-1$
	static final String PREFERENCE_DISABLEOPENDURINGLOAD = "dom.disable_open_during_load"; //$NON-NLS-1$
	static final String PREFERENCE_DISABLEOPENWINDOWSTATUSHIDE = "dom.disable_window_open_feature.status"; //$NON-NLS-1$
	static final String PREFERENCE_DISABLEWINDOWSTATUSCHANGE = "dom.disable_window_status_change"; //$NON-NLS-1$
	static final String PREFERENCE_JAVASCRIPTENABLED = "javascript.enabled"; //$NON-NLS-1$
	static final String PREFERENCE_LANGUAGES = "intl.accept_languages"; //$NON-NLS-1$
	static final String PREFERENCE_PROXYHOST_FTP = "network.proxy.ftp"; //$NON-NLS-1$
	static final String PREFERENCE_PROXYPORT_FTP = "network.proxy.ftp_port"; //$NON-NLS-1$
	static final String PREFERENCE_PROXYHOST_HTTP = "network.proxy.http"; //$NON-NLS-1$
	static final String PREFERENCE_PROXYPORT_HTTP = "network.proxy.http_port"; //$NON-NLS-1$
	static final String PREFERENCE_PROXYHOST_SSL = "network.proxy.ssl"; //$NON-NLS-1$
	static final String PREFERENCE_PROXYPORT_SSL = "network.proxy.ssl_port"; //$NON-NLS-1$
	static final String PREFERENCE_PROXYTYPE = "network.proxy.type"; //$NON-NLS-1$
	static final String PROFILE_AFTER_CHANGE = "profile-after-change"; //$NON-NLS-1$
	static final String PROFILE_BEFORE_CHANGE = "profile-before-change"; //$NON-NLS-1$
	static final String PROFILE_DIR = SEPARATOR_OS + "eclipse" + SEPARATOR_OS; //$NON-NLS-1$
	static final String PROFILE_DO_CHANGE = "profile-do-change"; //$NON-NLS-1$
	static final String PROPERTY_PROXYPORT = "network.proxy_port"; //$NON-NLS-1$
	static final String PROPERTY_PROXYHOST = "network.proxy_host"; //$NON-NLS-1$
	static final String SEPARATOR_LOCALE = "-"; //$NON-NLS-1$
	static final String SHUTDOWN_PERSIST = "shutdown-persist"; //$NON-NLS-1$
	static final String STARTUP = "startup"; //$NON-NLS-1$
	static final String TOKENIZER_LOCALE = ","; //$NON-NLS-1$
	static final String TRUE = "true"; //$NON-NLS-1$
	static final String URI_FILEROOT = "file:///"; //$NON-NLS-1$
	static final String MOZILLA_PROFILE_PATH = "org.eclipse.swt.browser.MOZ_PROFILE_PATH"; //$NON-NLS-1$
	static final String XULRUNNER_PATH = "org.eclipse.swt.browser.XULRunnerPath"; //$NON-NLS-1$

	// TEMPORARY CODE
	static final String FACTORIES_REGISTERED = "org.eclipse.swt.browser.MozillaFactoriesRegistered"; //$NON-NLS-1$
	static final String GRE_INITIALIZED = "org.eclipse.swt.browser.XULRunnerInitialized"; //$NON-NLS-1$

	static {
		DisplayListener = new Listener () {
			@Override
			public void handleEvent (Event event) {
				if (BrowserCount > 0) return; /* another display is still active */

				long /*int*/[] result = new long /*int*/[1];
				int rc = XPCOM.NS_GetServiceManager (result);
				if (rc != XPCOM.NS_OK) error (rc);
				if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

				nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
				result[0] = 0;
				byte[] buffer = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_OBSERVER_CONTRACTID, true);
				rc = serviceManager.GetServiceByContractID (buffer, IIDStore.GetIID (nsIObserverService.class), result);
				if (rc != XPCOM.NS_OK) error (rc);
				if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

				nsIObserverService observerService = new nsIObserverService (result[0]);
				result[0] = 0;
				buffer = MozillaDelegate.wcsToMbcs (null, PROFILE_BEFORE_CHANGE, true);
				int length = SHUTDOWN_PERSIST.length ();
				char[] chars = new char [length + 1];
				SHUTDOWN_PERSIST.getChars (0, length, chars, 0);
				rc = observerService.NotifyObservers (0, buffer, chars);
				if (rc != XPCOM.NS_OK) error (rc);
				observerService.Release ();

				if (LocationProvider != null) {
					String prefsLocation = LocationProvider.profilePath + AppFileLocProvider.PREFERENCES_FILE;
					nsEmbedString pathString = new nsEmbedString (prefsLocation);
					rc = XPCOM.NS_NewLocalFile (pathString.getAddress (), 1, result);
					if (rc != XPCOM.NS_OK) Mozilla.error (rc);
					if (result[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NULL_POINTER);
					pathString.dispose ();

					nsILocalFile localFile = new nsILocalFile (result [0]);
					result[0] = 0;
					rc = localFile.QueryInterface (IIDStore.GetIID (nsIFile.class), result);
					if (rc != XPCOM.NS_OK) Mozilla.error (rc);
					if (result[0] == 0) Mozilla.error (XPCOM.NS_ERROR_NO_INTERFACE);
					localFile.Release ();

					nsIFile prefFile = new nsIFile (result[0]);
					result[0] = 0;

					buffer = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_PREFSERVICE_CONTRACTID, true);
					rc = serviceManager.GetServiceByContractID (buffer, IIDStore.GetIID (nsIPrefService.class), result);
					if (rc != XPCOM.NS_OK) error (rc);
					if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

					nsIPrefService prefService = new nsIPrefService (result[0]);
					result[0] = 0;
					revertProxySettings (prefService);
					rc = prefService.SavePrefFile (prefFile.getAddress ());
					prefService.Release ();

					prefFile.Release ();
				}
				serviceManager.Release ();

				if (XPCOMWasGlued) {
					/*
					* The following is intentionally commented because it causes subsequent
					* browser instantiations within the process to fail.  Mozilla does not
					* support being unloaded and then re-initialized in a process, see
					* http://www.mail-archive.com/dev-embedding@lists.mozilla.org/msg01732.html .
					*/

//					int size = XPCOM.nsDynamicFunctionLoad_sizeof ();
//					/* alloc memory for two structs, the second is empty to signify the end of the list */
//					long /*int*/ ptr = C.malloc (size * 2);
//					C.memset (ptr, 0, size * 2);
//					nsDynamicFunctionLoad functionLoad = new nsDynamicFunctionLoad ();
//					byte[] bytes = MozillaDelegate.wcsToMbcs (null, "XRE_TermEmbedding", true); //$NON-NLS-1$
//					functionLoad.functionName = C.malloc (bytes.length);
//					C.memmove (functionLoad.functionName, bytes, bytes.length);
//					functionLoad.function = C.malloc (C.PTR_SIZEOF);
//					C.memmove (functionLoad.function, new long /*int*/[] {0} , C.PTR_SIZEOF);
//					XPCOM.memmove (ptr, functionLoad, XPCOM.nsDynamicFunctionLoad_sizeof ());
//					XPCOM.XPCOMGlueLoadXULFunctions (ptr);
//					C.memmove (result, functionLoad.function, C.PTR_SIZEOF);
//					long /*int*/ functionPtr = result[0];
//					result[0] = 0;
//					C.free (functionLoad.function);
//					C.free (functionLoad.functionName);
//					C.free (ptr);
//					XPCOM.Call (functionPtr);

//					XPCOM.XPCOMGlueShutdown ();
					XPCOMWasGlued = false;
				}
				if (XPCOMInitWasGlued) {
					XPCOMInit.XPCOMGlueShutdown ();
					XPCOMInitWasGlued = false;
				}
				Initialized = PerformedVersionCheck = false;
			}

			void revertProxySettings (nsIPrefService prefService) {
				/* the proxy settings are typically not set, so check for this first */
				boolean hostSet = oldProxyHostFTP != null || oldProxyHostHTTP != null || oldProxyHostSSL != null;
				if (!hostSet && oldProxyPortFTP == -1 && oldProxyPortHTTP == -1 && oldProxyPortSSL == -1 && oldProxyType == -1) return;

				long /*int*/[] result = new long /*int*/[1];
				byte[] buffer = new byte[1];
				int rc = prefService.GetBranch (buffer, result);	/* empty buffer denotes root preference level */
				if (rc != XPCOM.NS_OK) error (rc);
				if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

				nsIPrefBranch prefBranch = new nsIPrefBranch (result[0]);
				result[0] = 0;

				if (hostSet) {
					rc = XPCOM.NS_GetComponentManager (result);
					if (rc != XPCOM.NS_OK) error (rc);
					if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

					nsIComponentManager componentManager = new nsIComponentManager (result[0]);
					result[0] = 0;

					byte[] contractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_PREFLOCALIZEDSTRING_CONTRACTID, true);
					rc = componentManager.CreateInstanceByContractID (contractID, 0, IIDStore.GetIID (nsIPrefLocalizedString.class), result);
					if (rc != XPCOM.NS_OK) error (rc);
					if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

					nsIPrefLocalizedString localizedString = new nsIPrefLocalizedString (result[0]);
					result[0] = 0;

					if (oldProxyHostFTP != null) {
						buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_PROXYHOST_FTP, true);
						if (oldProxyHostFTP.equals (DEFAULTVALUE_STRING)) {
							rc = prefBranch.ClearUserPref (buffer);
							if (rc != XPCOM.NS_OK) error (rc);
						} else {
							int length = oldProxyHostFTP.length ();
							char[] charBuffer = new char[length];
							oldProxyHostFTP.getChars (0, length, charBuffer, 0);
							rc = localizedString.SetDataWithLength (length, charBuffer);
							if (rc != XPCOM.NS_OK) error (rc);
							rc = prefBranch.SetComplexValue (buffer, IIDStore.GetIID (nsIPrefLocalizedString.class), localizedString.getAddress ());
							if (rc != XPCOM.NS_OK) error (rc);
						}
					}

					if (oldProxyHostHTTP != null) {
						buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_PROXYHOST_HTTP, true);
						if (oldProxyHostHTTP.equals (DEFAULTVALUE_STRING)) {
							rc = prefBranch.ClearUserPref (buffer);
							if (rc != XPCOM.NS_OK) error (rc);
						} else {
							int length = oldProxyHostHTTP.length ();
							char[] charBuffer = new char[length];
							oldProxyHostHTTP.getChars (0, length, charBuffer, 0);
							rc = localizedString.SetDataWithLength (length, charBuffer);
							if (rc != XPCOM.NS_OK) error (rc);
							rc = prefBranch.SetComplexValue (buffer, IIDStore.GetIID (nsIPrefLocalizedString.class), localizedString.getAddress ());
							if (rc != XPCOM.NS_OK) error (rc);
						}
					}

					if (oldProxyHostSSL != null) {
						buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_PROXYHOST_SSL, true);
						if (oldProxyHostSSL.equals (DEFAULTVALUE_STRING)) {
							rc = prefBranch.ClearUserPref (buffer);
							if (rc != XPCOM.NS_OK) error (rc);
						} else {
							int length = oldProxyHostSSL.length ();
							char[] charBuffer = new char[length];
							oldProxyHostSSL.getChars (0, length, charBuffer, 0);
							rc = localizedString.SetDataWithLength (length, charBuffer);
							if (rc != XPCOM.NS_OK) error (rc);
							rc = prefBranch.SetComplexValue (buffer, IIDStore.GetIID (nsIPrefLocalizedString.class), localizedString.getAddress ());
							if (rc != XPCOM.NS_OK) error (rc);
						}
					}

					localizedString.Release ();
					componentManager.Release ();
				}

				if (oldProxyPortFTP != -1) {
					buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_PROXYPORT_FTP, true);
					rc = prefBranch.SetIntPref (buffer, oldProxyPortFTP);
					if (rc != XPCOM.NS_OK) error (rc);
				}
				if (oldProxyPortHTTP != -1) {
					buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_PROXYPORT_HTTP, true);
					rc = prefBranch.SetIntPref (buffer, oldProxyPortHTTP);
					if (rc != XPCOM.NS_OK) error (rc);
				}
				if (oldProxyPortSSL != -1) {
					buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_PROXYPORT_SSL, true);
					rc = prefBranch.SetIntPref (buffer, oldProxyPortSSL);
					if (rc != XPCOM.NS_OK) error (rc);
				}
				if (oldProxyType != -1) {
					buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_PROXYTYPE, true);
					rc = prefBranch.SetIntPref (buffer, oldProxyType);
					if (rc != XPCOM.NS_OK) error (rc);
				}

				prefBranch.Release ();
			}
		};

		MozillaClearSessions = () -> {
			if (!Initialized) return;
			long /*int*/[] result = new long /*int*/[1];
			int rc = XPCOM.NS_GetServiceManager (result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
			nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
			result[0] = 0;
			byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_COOKIEMANAGER_CONTRACTID, true);
			rc = serviceManager.GetServiceByContractID (aContractID, IIDStore.GetIID (nsICookieManager.class), result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
			serviceManager.Release ();

			nsICookieManager manager = new nsICookieManager (result[0]);
			result[0] = 0;
			rc = manager.GetEnumerator (result);
			if (rc != XPCOM.NS_OK) error (rc);

			nsISimpleEnumerator enumerator = new nsISimpleEnumerator (result[0]);
			int[] moreElements = new int[1]; /* PRBool */
			rc = enumerator.HasMoreElements (moreElements);
			if (rc != XPCOM.NS_OK) error (rc);
			while (moreElements[0] != 0) {
				result[0] = 0;
				rc = enumerator.GetNext (result);
				if (rc != XPCOM.NS_OK) error (rc);
				nsICookie cookie = new nsICookie (result[0]);
				long[] expires = new long[1];
				rc = cookie.GetExpires (expires);
				if (expires[0] == 0) {
					/* indicates a session cookie */
					long /*int*/ domain = XPCOM.nsEmbedCString_new ();
					long /*int*/ name = XPCOM.nsEmbedCString_new ();
					long /*int*/ path = XPCOM.nsEmbedCString_new ();
					cookie.GetHost (domain);
					cookie.GetName (name);
					cookie.GetPath (path);
					rc = manager.Remove (domain, name, path, 0);
					XPCOM.nsEmbedCString_delete (domain);
					XPCOM.nsEmbedCString_delete (name);
					XPCOM.nsEmbedCString_delete (path);
					if (rc != XPCOM.NS_OK) error (rc);
				}
				cookie.Release ();
				rc = enumerator.HasMoreElements (moreElements);
				if (rc != XPCOM.NS_OK) error (rc);
			}
			enumerator.Release ();
			manager.Release ();
		};

		MozillaGetCookie = () -> {
			if (!Initialized) return;

			long /*int*/[] result = new long /*int*/[1];
			int rc = XPCOM.NS_GetServiceManager (result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

			nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
			result[0] = 0;
			rc = serviceManager.GetService (XPCOM.NS_IOSERVICE_CID, IIDStore.GetIID (nsIIOService.class), result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

			nsIIOService ioService = new nsIIOService (result[0]);
			result[0] = 0;
			byte[] bytes = MozillaDelegate.wcsToMbcs (null, CookieUrl, false);
			long /*int*/ aSpec = XPCOM.nsEmbedCString_new (bytes, bytes.length);
			rc = ioService.NewURI (aSpec, null, 0, result);
			XPCOM.nsEmbedCString_delete (aSpec);
			ioService.Release ();
			if (rc != XPCOM.NS_OK) {
				serviceManager.Release ();
				return;
			}
			if (result[0] == 0) error (XPCOM.NS_ERROR_NULL_POINTER);

			nsIURI aURI = new nsIURI (result[0]);
			result[0] = 0;
			byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_COOKIESERVICE_CONTRACTID, true);
			rc = serviceManager.GetServiceByContractID (aContractID, IIDStore.GetIID (nsICookieService.class), result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

			nsICookieService cookieService = new nsICookieService (result[0]);
			result[0] = 0;
			rc = cookieService.GetCookieString (aURI.getAddress(), 0, result);
			cookieService.Release ();
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) {
				aURI.Release ();
				serviceManager.Release ();
				return;
			}
			long /*int*/ cookieString = result[0];
			result[0] = 0;
			aURI.Release ();
			serviceManager.Release ();

			int length = C.strlen (cookieString);
			bytes = new byte[length];
			XPCOM.memmove (bytes, cookieString, length);

			/*
			 * NS_Free was introduced in mozilla 1.8, prior to this the standard free() call
			 * was to be used.  Try to free the cookie string with NS_Free first, and if it fails
			 * then assume that an older mozilla is being used, and use C's free() instead.
			 */
			if (pathBytes_NSFree == null) {
				String mozillaPath = getMozillaPath ();
				mozillaPath += MozillaDelegate.getLibraryName (mozillaPath) + '\0';
				pathBytes_NSFree = mozillaPath.getBytes (StandardCharsets.UTF_8); //$NON-NLS-1$
			}
			if (!XPCOM.NS_Free (pathBytes_NSFree, cookieString)) {
				C.free (cookieString);
			}

			String allCookies = new String (MozillaDelegate.mbcsToWcs (null, bytes));
			StringTokenizer tokenizer = new StringTokenizer (allCookies, ";"); //$NON-NLS-1$
			while (tokenizer.hasMoreTokens ()) {
				String cookie = tokenizer.nextToken ();
				int index = cookie.indexOf ('=');
				if (index != -1) {
					String name = cookie.substring (0, index).trim ();
					if (name.equals (CookieName)) {
						CookieValue = cookie.substring (index + 1).trim ();
						return;
					}
				}
			}
		};

		MozillaSetCookie = () -> {
			if (!Initialized) return;

			long /*int*/[] result = new long /*int*/[1];
			int rc = XPCOM.NS_GetServiceManager (result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

			nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
			result[0] = 0;
			rc = serviceManager.GetService (XPCOM.NS_IOSERVICE_CID, IIDStore.GetIID (nsIIOService.class), result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

			nsIIOService ioService = new nsIIOService (result[0]);
			result[0] = 0;
			byte[] bytes = MozillaDelegate.wcsToMbcs (null, CookieUrl, false);
			long /*int*/ aSpec = XPCOM.nsEmbedCString_new (bytes, bytes.length);
			rc = ioService.NewURI (aSpec, null, 0, result);
			XPCOM.nsEmbedCString_delete (aSpec);
			ioService.Release ();
			if (rc != XPCOM.NS_OK) {
				serviceManager.Release ();
				return;
			}
			if (result[0] == 0) error (XPCOM.NS_ERROR_NULL_POINTER);

			nsIURI aURI = new nsIURI(result[0]);
			result[0] = 0;
			byte[] aCookie = MozillaDelegate.wcsToMbcs (null, CookieValue, true);
			byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_COOKIESERVICE_CONTRACTID, true);
			rc = serviceManager.GetServiceByContractID (aContractID, IIDStore.GetIID (nsICookieService.class), result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

			nsICookieService cookieService = new nsICookieService (result[0]);
			result[0] = 0;
			rc = cookieService.SetCookieString (aURI.getAddress(), 0, aCookie, 0);
			cookieService.Release ();
			aURI.Release ();
			serviceManager.Release ();
			CookieResult = rc == 0;
		};
	}

static String Arch () {
	String osArch = System.getProperty("os.arch"); //$NON-NLS-1$
	if (osArch.equals ("i386") || osArch.equals ("i686")) return "x86"; //$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
	if (osArch.equals ("amd64")) return "x86_64"; //$NON-NLS-1$ $NON-NLS-2$
	if (osArch.equals ("IA64N")) return "ia64_32"; //$NON-NLS-1$ $NON-NLS-2$
	if (osArch.equals ("IA64W")) return "ia64"; //$NON-NLS-1$ $NON-NLS-2$
	return osArch;
}

static String OS() {
	String osName = System.getProperty("os.name"); //$NON-NLS-1$
	if (osName.equals ("Linux")) return "linux"; //$NON-NLS-1$ $NON-NLS-2$
	if (osName.equals ("AIX")) return "aix"; //$NON-NLS-1$ $NON-NLS-2$
	if (osName.equals ("Solaris") || osName.equals ("SunOS")) return "solaris"; //$NON-NLS-1$ $NON-NLS-2$ $NON-NLS-3$
	if (osName.equals ("HP-UX")) return "hpux"; //$NON-NLS-1$ $NON-NLS-2$
	if (osName.equals ("Mac OS X")) return "macosx"; //$NON-NLS-1$ $NON-NLS-2$
	if (osName.startsWith ("Win")) return "win32"; //$NON-NLS-1$ $NON-NLS-2$
	return osName;
}

static void LoadLibraries () {
	boolean initLoaded = false;

	if (Boolean.getBoolean (GRE_INITIALIZED)) {
		/*
		 * Another browser has already initialized xulrunner in this process,
		 * so just bind to it instead of trying to initialize a new one.
		 */
		Initialized = true;
	}

	MozillaPath = System.getProperty (XULRUNNER_PATH);
	/*
	* Browser clients that ship XULRunner in a plug-in must have an opportunity
	* to set the org.eclipse.swt.browser.XULRunnerPath system property to point
	* at their XULRunner before the first Mozilla-based Browser is created.  To
	* facilitate this, reflection is used to reference non-existent class
	* org.eclipse.swt.browser.XULRunnerInitializer the first time a Mozilla-
	* based Browser is created.   A client wishing to use this hook can do so
	* by creating a fragment of org.eclipse.swt that implements this class and
	* sets the system property in its static initializer.
	*/
	if (MozillaPath == null) {
		try {
			Class.forName ("org.eclipse.swt.browser.XULRunnerInitializer"); //$NON-NLS-1$
			MozillaPath = System.getProperty (XULRUNNER_PATH);
		} catch (ClassNotFoundException e) {
			/* no fragment is providing this class, which is the typical case */
		}
	}

	if (MozillaPath == null) {
		try {
			String libName = MozillaDelegate.getSWTInitLibraryName ();
			Library.loadLibrary (libName);
			initLoaded = true;
		} catch (UnsatisfiedLinkError e) {
			/*
			* If this library failed to load then do not attempt to detect a
			* xulrunner to use.  The Browser may still be usable if MOZILLA_FIVE_HOME
			* points at a GRE.
			*/
		}
	} else {
		/* ensure that client-supplied path is using correct separators */
		if (SEPARATOR_OS == '/') {
			MozillaPath = MozillaPath.replace ('\\', SEPARATOR_OS);
		} else {
			MozillaPath = MozillaPath.replace ('/', SEPARATOR_OS);
		}

		MozillaPath += SEPARATOR_OS;
		MozillaPath += MozillaDelegate.getLibraryName (MozillaPath);
		IsXULRunner = true;
	}

	if (initLoaded) {
		/* attempt to discover a XULRunner to use as the GRE */
		MozillaPath = InitDiscoverXULRunner ();
		IsXULRunner = MozillaPath.length () > 0;

		/*
		 * Test whether the detected XULRunner can be used as the GRE before loading swt's
		 * XULRunner library.  If it cannot be used then fall back to attempting to use
		 * the GRE pointed to by MOZILLA_FIVE_HOME.
		 *
		 * One case where this will fail is attempting to use a 64-bit xulrunner while swt
		 * is running in 32-bit mode, or vice versa.
		 */
		if (IsXULRunner) {
			byte[] bytes = MozillaDelegate.wcsToMbcs (null, MozillaPath, true);
			int rc = XPCOMInit.XPCOMGlueStartup (bytes);
			if (rc != XPCOM.NS_OK) {
				MozillaPath = MozillaPath.substring (0, MozillaPath.lastIndexOf (SEPARATOR_OS));
				if (Device.DEBUG) System.out.println ("cannot use detected XULRunner: " + MozillaPath); //$NON-NLS-1$

				/* attempt to XPCOMGlueStartup the GRE pointed at by MOZILLA_FIVE_HOME */
				long /*int*/ ptr = C.getenv (MozillaDelegate.wcsToMbcs (null, MOZILLA_FIVE_HOME, true));
				if (ptr == 0) {
					IsXULRunner = false;
				} else {
					int length = C.strlen (ptr);
					bytes = new byte[length];
					C.memmove (bytes, ptr, length);
					MozillaPath = new String (MozillaDelegate.mbcsToWcs (null, bytes));
					/*
					 * Attempting to XPCOMGlueStartup a mozilla-based GRE != xulrunner can
					 * crash, so don't attempt unless the GRE appears to be xulrunner.
					 */
					if (MozillaPath.indexOf ("xulrunner") == -1) { //$NON-NLS-1$
						IsXULRunner = false;
					} else {
						MozillaPath += SEPARATOR_OS;
						MozillaPath += MozillaDelegate.getLibraryName (MozillaPath);
						bytes = MozillaDelegate.wcsToMbcs (null, MozillaPath, true);
						rc = XPCOMInit.XPCOMGlueStartup (bytes);
						if (rc == XPCOM.NS_OK) {
							/* ensure that client-supplied path is using correct separators */
							if (SEPARATOR_OS == '/') {
								MozillaPath = MozillaPath.replace ('\\', SEPARATOR_OS);
							} else {
								MozillaPath = MozillaPath.replace ('/', SEPARATOR_OS);
							}
						} else {
							IsXULRunner = false;
							MozillaPath = MozillaPath.substring (0, MozillaPath.lastIndexOf (SEPARATOR_OS));
							if (Device.DEBUG) System.out.println ("failed to start as XULRunner: " + MozillaPath); //$NON-NLS-1$
						}
					}
				}
			}
			if (IsXULRunner) {
				XPCOMInitWasGlued = true;
			}
		}
	}
}

@Override
public void create (Composite parent, int style) {
	delegate = new MozillaDelegate (browser);
	final Display display = parent.getDisplay ();

	long /*int*/[] result = new long /*int*/[1];
	if (!Initialized) {
		LoadLibraries ();

		if (IsXULRunner) {
			/* load swt's xulrunner library and invoke XPCOMGlueStartup to load xulrunner's dependencies */
			MozillaPath = initXULRunner (MozillaPath);
		} else {
			/*
			* If style SWT.MOZILLA was specified then this initialization has already
			* failed, because SWT.MOZILLA-style Browsers must utilize XULRunner.
			*/
			if ((style & SWT.MOZILLA) != 0) {
				browser.dispose ();
				String errorString = (MozillaPath != null && MozillaPath.length () > 0) ?
					" [Failed to use detected XULRunner: " + MozillaPath + "]" :
					" [Could not detect registered XULRunner to use]";	//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				SWT.error (SWT.ERROR_NO_HANDLES, null, errorString);
			}

			/* load swt's mozilla library */
			MozillaPath = initMozilla (MozillaPath);
		}

		if (!Initialized) {
			/* create LocationProvider, which tells mozilla where to find things on the file system */
			String profilePath = System.getProperty (MOZILLA_PROFILE_PATH);
			if (profilePath != null && profilePath.length() > 0) {
				/* ensure that client-supplied path is using correct separators */
				if (SEPARATOR_OS == '/') {
					profilePath = profilePath.replace ('\\', SEPARATOR_OS);
				} else {
					profilePath = profilePath.replace ('/', SEPARATOR_OS);
				}
			} else {
				profilePath = MozillaDelegate.getProfilePath ();
			}
			String cacheParentPath = MozillaDelegate.getCacheParentPath ();
			LocationProvider = new AppFileLocProvider (MozillaPath, profilePath, cacheParentPath, IsXULRunner);
			LocationProvider.AddRef ();

			/* write swt.xpt to the file system if needed */
			initExternal (LocationProvider.profilePath);

			/* invoke appropriate Init function (based on mozilla version) */
			initXPCOM (MozillaPath, IsXULRunner);
		}

		/* attempt to initialize JavaXPCOM in the detected XULRunner */
		if (IsXULRunner) initJavaXPCOM (MozillaPath);

		/* get the nsIComponentManager and nsIServiceManager, used throughout initialization */
		int rc = XPCOM.NS_GetComponentManager (result);
		if (rc != XPCOM.NS_OK) {
			browser.dispose ();
			error (rc);
		}
		if (result[0] == 0) {
			browser.dispose ();
			error (XPCOM.NS_NOINTERFACE);
		}
		nsIComponentManager componentManager = new nsIComponentManager (result[0]);
		result[0] = 0;

		rc = XPCOM.NS_GetServiceManager (result);
		if (rc != XPCOM.NS_OK) {
			browser.dispose ();
			error (rc);
		}
		if (result[0] == 0) {
			browser.dispose ();
			error (XPCOM.NS_NOINTERFACE);
		}
		nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
		result[0] = 0;

		/* init the event handler if needed */
		initSpinup (componentManager);

		/*
		 * Check for the property indicating that factories have already been registered,
		 * in which case this browser should not overwrite them with its own.
		 */
		boolean factoriesRegistered = Boolean.getBoolean (FACTORIES_REGISTERED);

		/* init our WindowCreator, which mozilla uses for the creation of child browsers in external Shells */
		if (!factoriesRegistered) {
			initWindowCreator (serviceManager);
		}

		/* notify mozilla that the profile directory has been changed from its default value */
		initProfile (serviceManager, IsXULRunner);

		/* init preference values that give desired mozilla behaviours */
		initPreferences (serviceManager, componentManager);

		/* init our various factories that mozilla can invoke as needed */
		if (!factoriesRegistered) {
			initFactories (serviceManager, componentManager, IsXULRunner);
		}

		serviceManager.Release ();
		componentManager.Release ();

		/* add cookies that were set by a client before the first Mozilla instance was created */
		if (MozillaPendingCookies != null) {
			SetPendingCookies (MozillaPendingCookies);
		}
		MozillaPendingCookies = null;

		Initialized = true;
	}

	BrowserCount++;

	if (display.getData (DISPOSE_LISTENER_HOOKED) == null) {
		display.setData (DISPOSE_LISTENER_HOOKED, DISPOSE_LISTENER_HOOKED);
		display.addListener (SWT.Dispose, DisplayListener);
	}

	/* get the nsIComponentManager, used throughout initialization */
	int rc = XPCOM.NS_GetComponentManager (result);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	if (result[0] == 0) {
		browser.dispose ();
		error (XPCOM.NS_NOINTERFACE);
	}
	nsIComponentManager componentManager = new nsIComponentManager (result[0]);
	result[0] = 0;

	/* create the nsIWebBrowser instance */
	rc = componentManager.CreateInstance (MozillaVersion.CheckVersion(MozillaVersion.VERSION_XR31) ? XPCOM.NS_IWEBBROWSER_31_CID : XPCOM.NS_IWEBBROWSER_CID, 0, IIDStore.GetIID (nsIWebBrowser.class), result);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	if (result[0] == 0) {
		browser.dispose ();
		error (XPCOM.NS_NOINTERFACE);
	}
	webBrowser = new nsIWebBrowser (result[0]);
	result[0] = 0;

	/* create the instance-based callback interfaces */
	createCOMInterfaces ();
	AddRef ();

	/* init the nsIWebBrowser's container and base windows */
	initWebBrowserWindows ();

	if (!PerformedVersionCheck) {
		/* The Mozilla version is currently either known to be 24.x or 10.x, or is assumed to be 1.9.2.x */
		PerformedVersionCheck = true;

		rc = componentManager.QueryInterface (IIDStore.GetIID (nsIComponentRegistrar.class), result);
		if (rc != XPCOM.NS_OK) {
			browser.dispose ();
			error (rc);
		}
		if (result[0] == 0) {
			browser.dispose ();
			error (XPCOM.NS_NOINTERFACE);
		}
		nsIComponentRegistrar componentRegistrar = new nsIComponentRegistrar (result[0]);
		result[0] = 0;

		/*
		* Check for the property indicating that factories have already been registered,
		* in which case this browser should not overwrite them with its own.
		*/
		boolean factoriesRegistered = Boolean.getBoolean (FACTORIES_REGISTERED);

		/*
		* If the version is earlier than 10.x then query for the IID of nsIDocShell from
		* various mozilla releases to determine more precisely which version is being used.
		*/
		if (!MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR10)) {
			rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIInterfaceRequestor.class), result);
			if (rc != XPCOM.NS_OK) {
				browser.dispose ();
				error (XPCOM.NS_ERROR_FAILURE);
			}
			if (result[0] == 0) {
				browser.dispose ();
				error (XPCOM.NS_ERROR_NO_INTERFACE);
			}
			nsIInterfaceRequestor interfaceRequestor = new nsIInterfaceRequestor (result[0]);
			result[0] = 0;

			for (int i = MozillaVersion.VERSION_XR1_9_2; MozillaVersion.VERSION_BASE <= i; i--) {
				nsID iid = IIDStore.GetIID (nsIDocShell.class, i, true);
				if (iid != null) {
					result[0] = 0;
					rc = interfaceRequestor.GetInterface (iid, result);
					if (rc == XPCOM.NS_OK && result[0] != 0) {
						new nsISupports (result[0]).Release ();
						MozillaVersion.SetCurrentVersion (i);
						break;
					}
				}
			}
			interfaceRequestor.Release ();
			result[0] = 0;
		}

		/*
		* A Download factory for contract "Transfer" must be registered iff the GRE's version is 1.8.x.
		*   Check for the availability of the 1.8 implementation of nsIDocShell to determine if the
		*   GRE's version is 1.8.x.
		* If the GRE version is < 1.8 then the previously-registered Download factory for contract
		*   "Download" will be used.
		* If the GRE version is >= 1.9 then no Download factory is registered because this
		*   functionality is provided by the GRE.
		*/
		if (MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR1_8, true)) {
			if (!factoriesRegistered) {
				DownloadFactory_1_8 downloadFactory_1_8 = new DownloadFactory_1_8 ();
				downloadFactory_1_8.AddRef ();
				byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_TRANSFER_CONTRACTID, true);
				byte[] aClassName = MozillaDelegate.wcsToMbcs (null, "swtTransfer", true); //$NON-NLS-1$
				rc = componentRegistrar.RegisterFactory (XPCOM.NS_DOWNLOAD_CID, aClassName, aContractID, downloadFactory_1_8.getAddress ());
				if (rc != XPCOM.NS_OK) {
					browser.dispose ();
					error (rc);
				}
				downloadFactory_1_8.Release ();
			}
		}
		componentRegistrar.Release ();

		if (!factoriesRegistered) {
			HelperAppLauncherDialogFactory dialogFactory = new HelperAppLauncherDialogFactory ();
			dialogFactory.AddRef ();
			byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_HELPERAPPLAUNCHERDIALOG_CONTRACTID, true);
			byte[] aClassName = MozillaDelegate.wcsToMbcs (null, "swtHelperAppLauncherDialog", true); //$NON-NLS-1$
			rc = componentRegistrar.RegisterFactory (XPCOM.NS_HELPERAPPLAUNCHERDIALOG_CID, aClassName, aContractID, dialogFactory.getAddress ());
			if (rc != XPCOM.NS_OK) {
				browser.dispose ();
				error (rc);
			}
			dialogFactory.Release ();
		}

		System.setProperty (FACTORIES_REGISTERED, TRUE);
	}
	componentManager.Release ();

	/*
	 * Bug in XULRunner 1.9.  On win32, Mozilla does not clear its background before content has
	 * been set into it.  As a result, embedders appear broken if they do not immediately display
	 * a URL or text.  The Mozilla bug for this is https://bugzilla.mozilla.org/show_bug.cgi?id=453523.
	 *
	 * The workaround is to subclass the Mozilla window and clear it whenever WM_ERASEBKGND is received.
	 * This subclass should be removed once content has been set into the browser.
	 */
	if (MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR1_9)) {
		delegate.addWindowSubclass ();
	}

	/* add listeners for progress and content */
	rc = webBrowser.AddWebBrowserListener (weakReference.getAddress (), IIDStore.GetIID (nsIWebProgressListener.class));
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	rc = webBrowser.SetParentURIContentListener (uriContentListener.getAddress ());
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}

	delegate.init ();

	listener = event -> {
		switch (event.type) {
			case SWT.Dispose: {
				/* make this handler run after other dispose listeners */
				if (ignoreDispose) {
					ignoreDispose = false;
					break;
				}
				ignoreDispose = true;
				browser.notifyListeners (event.type, event);
				event.type = SWT.NONE;
				onDispose (event.display);
				break;
			}
			case SWT.Resize: onResize (); break;
			case SWT.FocusIn: {
				Activate ();

				/* if tabbing onto a page for the first time then full-Browser focus ring should be shown */

				long /*int*/[] result1 = new long /*int*/[1];
				int rc1 = XPCOM.NS_GetServiceManager (result1);
				if (rc1 != XPCOM.NS_OK) error (rc1);
				if (result1[0] == 0) error (XPCOM.NS_NOINTERFACE);
				nsIServiceManager serviceManager = new nsIServiceManager (result1[0]);
				result1[0] = 0;
				byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_FOCUSMANAGER_CONTRACTID, true);
				rc1 = serviceManager.GetServiceByContractID (aContractID, IIDStore.GetIID (nsIFocusManager.class), result1);
				serviceManager.Release ();

				if (rc1 == XPCOM.NS_OK && result1[0] != 0) {
					nsIFocusManager focusManager = new nsIFocusManager (result1[0]);
					result1[0] = 0;
					rc1 = focusManager.GetFocusedElement (result1);
					if (rc1 == XPCOM.NS_OK) {
						if (result1[0] != 0) {
							new nsISupports (result1[0]).Release ();
							result1[0] = 0;
						} else {
							/* show full browser focus ring */
							rc1 = webBrowser.GetContentDOMWindow (result1);
							if (rc1 == XPCOM.NS_OK && result1[0] != 0) {
								nsIDOMWindow domWindow = new nsIDOMWindow (result1[0]);
								result1[0] = 0;
								rc1 = domWindow.GetDocument (result1);
								domWindow.Release ();
								if (rc1 == XPCOM.NS_OK && result1[0] != 0) {
									nsIDOMDocument domDocument = new nsIDOMDocument (result1[0]);
									result1[0] = 0;
									rc1 = domDocument.GetDocumentElement (result1);
									domDocument.Release ();
									if (rc1 == XPCOM.NS_OK && result1[0] != 0) {
										rc1 = focusManager.SetFocus (result1[0], nsIFocusManager.FLAG_BYKEY);
										new nsISupports(result1[0]).Release ();
										result1[0] = 0;
									}
								}
							}
						}
					}
					focusManager.Release ();
				}
				break;
			}
			case SWT.Activate: Activate (); break;
			case SWT.Deactivate: {
				Display display1 = event.display;
				if (Mozilla.this.browser == display1.getFocusControl ()) Deactivate ();
				break;
			}
			case SWT.Show: {
				/*
				* Feature in GTK Mozilla.  Mozilla does not show up when
				* its container (a GTK fixed handle) is made visible
				* after having been hidden.  The workaround is to reset
				* its size after the container has been made visible.
				*/
				Display display2 = event.display;
				display2.asyncExec(() -> {
					if (browser.isDisposed ()) return;
					onResize ();
				});
				break;
			}
		}
	};
	int[] folderEvents = new int[] {
		SWT.Dispose,
		SWT.Resize,
		SWT.FocusIn,
		SWT.Activate,
		SWT.Deactivate,
		SWT.Show,
		SWT.KeyDown,		/* needed to make browser traversable */
	};
	for (int i = 0; i < folderEvents.length; i++) {
		browser.addListener (folderEvents[i], listener);
	}
}

@Override
public boolean back () {
	htmlBytes = null;

	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIWebNavigation.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

	nsIWebNavigation webNavigation = new nsIWebNavigation (result[0]);
	rc = webNavigation.GoBack ();
	webNavigation.Release ();
	return rc == XPCOM.NS_OK;
}

@Override
public boolean close () {
	final boolean[] result = new boolean[] {false};
	LocationListener[] oldListeners = locationListeners;
	locationListeners = new LocationListener[] {
		new LocationAdapter () {
			@Override
			public void changing (LocationEvent event) {
				/* implies that the user did not veto the page unload */
				result[0] = true;
			}
		}
	};
	execute ("window.location.replace('about:blank');"); //$NON-NLS-1$
	locationListeners = oldListeners;
	return result[0];
}

void createCOMInterfaces () {
	// Create each of the interfaces that this object implements
	supports = new XPCOMObject (new int[] {2, 0, 0}) {
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
	};

	weakReference = new XPCOMObject (new int[] {2, 0, 0, 2}) {
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		@Override
		public long /*int*/ method3 (long /*int*/[] args) {return QueryReferent (args[0], args[1]);}
	};

	webProgressListener = new XPCOMObject (new int[] {2, 0, 0, 4, 6, 3, 4, 3}) {
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		@Override
		public long /*int*/ method3 (long /*int*/[] args) {return OnStateChange (args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3]);}
		@Override
		public long /*int*/ method4 (long /*int*/[] args) {return OnProgressChange (args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3], (int)/*64*/args[4], (int)/*64*/args[5]);}
		@Override
		public long /*int*/ method5 (long /*int*/[] args) {return OnLocationChange (args[0], args[1], args[2]);}
		@Override
		public long /*int*/ method6 (long /*int*/[] args) {return OnStatusChange (args[0], args[1], (int)/*64*/args[2], args[3]);}
		@Override
		public long /*int*/ method7 (long /*int*/[] args) {return OnSecurityChange (args[0], args[1], (int)/*64*/args[2]);}
	};

	webProgressListener_24 = new XPCOMObject (new int[] {2, 0, 0, 4, 6, 4, 4, 3}) {
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		@Override
		public long /*int*/ method3 (long /*int*/[] args) {return OnStateChange (args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3]);}
		@Override
		public long /*int*/ method4 (long /*int*/[] args) {return OnProgressChange (args[0], args[1], (int)/*64*/args[2], (int)/*64*/args[3], (int)/*64*/args[4], (int)/*64*/args[5]);}
		@Override
		public long /*int*/ method5 (long /*int*/[] args) {return OnLocationChange (args[0], args[1], args[2], (int)/*64*/args[3]);}
		@Override
		public long /*int*/ method6 (long /*int*/[] args) {return OnStatusChange (args[0], args[1], (int)/*64*/args[2], args[3]);}
		@Override
		public long /*int*/ method7 (long /*int*/[] args) {return OnSecurityChange (args[0], args[1], (int)/*64*/args[2]);}
	};

	webBrowserChrome = new XPCOMObject (new int[] {2, 0, 0, 2, 1, 1, 1, 1, 0, 2, 0, 1, 1}) {
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {
			return Release ();
		}
		@Override
		public long /*int*/ method3 (long /*int*/[] args) {return SetStatus ((int)/*64*/args[0], args[1]);}
		@Override
		public long /*int*/ method4 (long /*int*/[] args) {return GetWebBrowser (args[0]);}
		@Override
		public long /*int*/ method5 (long /*int*/[] args) {return SetWebBrowser (args[0]);}
		@Override
		public long /*int*/ method6 (long /*int*/[] args) {return GetChromeFlags (args[0]);}
		@Override
		public long /*int*/ method7 (long /*int*/[] args) {return SetChromeFlags ((int)/*64*/args[0]);}
		@Override
		public long /*int*/ method8 (long /*int*/[] args) {return DestroyBrowserWindow ();}
		@Override
		public long /*int*/ method9 (long /*int*/[] args) {return SizeBrowserTo ((int)/*64*/args[0], (int)/*64*/args[1]);}
		@Override
		public long /*int*/ method10 (long /*int*/[] args) {return ShowAsModal ();}
		@Override
		public long /*int*/ method11 (long /*int*/[] args) {return IsWindowModal (args[0]);}
		@Override
		public long /*int*/ method12 (long /*int*/[] args) {return ExitModalEventLoop ((int)/*64*/args[0]);}
	};

	webBrowserChromeFocus = new XPCOMObject (new int[] {2, 0, 0, 0, 0}) {
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		@Override
		public long /*int*/ method3 (long /*int*/[] args) {return FocusNextElement ();}
		@Override
		public long /*int*/ method4 (long /*int*/[] args) {return FocusPrevElement ();}
	};

	embeddingSiteWindow = new XPCOMObject (new int[] {2, 0, 0, 5, 5, 0, 1, 1, 1, 1, 1}) {
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		@Override
		public long /*int*/ method3 (long /*int*/[] args) {return SetDimensions ((int)/*64*/args[0], (int)/*64*/args[1], (int)/*64*/args[2], (int)/*64*/args[3], (int)/*64*/args[4]);}
		@Override
		public long /*int*/ method4 (long /*int*/[] args) {return GetDimensions ((int)/*64*/args[0], args[1], args[2], args[3], args[4]);}
		@Override
		public long /*int*/ method5 (long /*int*/[] args) {return SetFocus ();}
		@Override
		public long /*int*/ method6 (long /*int*/[] args) {return GetVisibility (args[0]);}
		@Override
		public long /*int*/ method7 (long /*int*/[] args) {return SetVisibility ((int)/*64*/args[0]);}
		@Override
		public long /*int*/ method8 (long /*int*/[] args) {return GetTitle (args[0]);}
		@Override
		public long /*int*/ method9 (long /*int*/[] args) {return SetTitle (args[0]);}
		@Override
		public long /*int*/ method10 (long /*int*/[] args) {return GetSiteWindow (args[0]);}
	};

	embeddingSiteWindow_24 = new XPCOMObject (new int[] {2, 0, 0, 5, 5, 0, 1, 1, 1, 1, 1, 0}) {
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		@Override
		public long /*int*/ method3 (long /*int*/[] args) {return SetDimensions ((int)/*64*/args[0], (int)/*64*/args[1], (int)/*64*/args[2], (int)/*64*/args[3], (int)/*64*/args[4]);}
		@Override
		public long /*int*/ method4 (long /*int*/[] args) {return GetDimensions ((int)/*64*/args[0], args[1], args[2], args[3], args[4]);}
		@Override
		public long /*int*/ method5 (long /*int*/[] args) {return SetFocus ();}
		@Override
		public long /*int*/ method6 (long /*int*/[] args) {return GetVisibility (args[0]);}
		@Override
		public long /*int*/ method7 (long /*int*/[] args) {return SetVisibility ((int)/*64*/args[0]);}
		@Override
		public long /*int*/ method8 (long /*int*/[] args) {return GetTitle (args[0]);}
		@Override
		public long /*int*/ method9 (long /*int*/[] args) {return SetTitle (args[0]);}
		@Override
		public long /*int*/ method10 (long /*int*/[] args) {return GetSiteWindow (args[0]);}
		@Override
		public long /*int*/ method11 (long /*int*/[] args) {return Blur ();}
	};

	interfaceRequestor = new XPCOMObject (new int[] {2, 0, 0, 2} ){
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		@Override
		public long /*int*/ method3 (long /*int*/[] args) {return GetInterface (args[0], args[1]);}
	};

	supportsWeakReference = new XPCOMObject (new int[] {2, 0, 0, 1}) {
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		@Override
		public long /*int*/ method3 (long /*int*/[] args) {return GetWeakReference (args[0]);}
	};

	contextMenuListener = new XPCOMObject (new int[] {2, 0, 0, 3}) {
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		@Override
		public long /*int*/ method3 (long /*int*/[] args) {return OnShowContextMenu ((int)/*64*/args[0], args[1], args[2]);}
	};

	uriContentListener = new XPCOMObject (new int[] {2, 0, 0, 2, 5, 3, 4, 1, 1, 1, 1}) {
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		@Override
		public long /*int*/ method3 (long /*int*/[] args) {return OnStartURIOpen (args[0], args[1]);}
		@Override
		public long /*int*/ method4 (long /*int*/[] args) {return DoContent (args[0], (int)/*64*/args[1], args[2], args[3], args[4]);}
		@Override
		public long /*int*/ method5 (long /*int*/[] args) {return IsPreferred (args[0], args[1], args[2]);}
		@Override
		public long /*int*/ method6 (long /*int*/[] args) {return CanHandleContent (args[0], (int)/*64*/args[1], args[2], args[3]);}
		@Override
		public long /*int*/ method7 (long /*int*/[] args) {return GetLoadCookie (args[0]);}
		@Override
		public long /*int*/ method8 (long /*int*/[] args) {return SetLoadCookie (args[0]);}
		@Override
		public long /*int*/ method9 (long /*int*/[] args) {return GetParentContentListener (args[0]);}
		@Override
		public long /*int*/ method10 (long /*int*/[] args) {return SetParentContentListener (args[0]);}
	};

	tooltipListener = new XPCOMObject (new int[] {2, 0, 0, 3, 0}) {
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		@Override
		public long /*int*/ method3 (long /*int*/[] args) {return OnShowTooltip ((int)/*64*/args[0], (int)/*64*/args[1], args[2]);}
		@Override
		public long /*int*/ method4 (long /*int*/[] args) {return OnHideTooltip ();}
	};

	domEventListener = new XPCOMObject (new int[] {2, 0, 0, 1}) {
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		@Override
		public long /*int*/ method3 (long /*int*/[] args) {return HandleEvent (args[0]);}
	};

	badCertListener = new XPCOMObject (new int[] {2, 0, 0, 4}) {
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		@Override
		public long /*int*/ method3 (long /*int*/[] args) {return NotifyCertProblem (args[0], args[1], args[2], args[3]);}
	};
}

@Override
void deregisterFunction (BrowserFunction function) {
	super.deregisterFunction (function);
	AllFunctions.remove (function.index);
}

void disposeCOMInterfaces () {
	if (supports != null) {
		supports.dispose ();
		supports = null;
	}
	if (weakReference != null) {
		weakReference.dispose ();
		weakReference = null;
	}
	if (webProgressListener != null) {
		webProgressListener.dispose ();
		webProgressListener = null;
	}
	if (webProgressListener_24 != null) {
		webProgressListener_24.dispose ();
		webProgressListener_24 = null;
	}
	if (webBrowserChrome != null) {
		webBrowserChrome.dispose ();
		webBrowserChrome = null;
	}
	if (webBrowserChromeFocus != null) {
		webBrowserChromeFocus.dispose ();
		webBrowserChromeFocus = null;
	}
	if (embeddingSiteWindow != null) {
		embeddingSiteWindow.dispose ();
		embeddingSiteWindow = null;
	}
	if (embeddingSiteWindow_24 != null) {
		embeddingSiteWindow_24.dispose ();
		embeddingSiteWindow_24 = null;
	}
	if (interfaceRequestor != null) {
		interfaceRequestor.dispose ();
		interfaceRequestor = null;
	}
	if (supportsWeakReference != null) {
		supportsWeakReference.dispose ();
		supportsWeakReference = null;
	}
	if (contextMenuListener != null) {
		contextMenuListener.dispose ();
		contextMenuListener = null;
	}
	if (uriContentListener != null) {
		uriContentListener.dispose ();
		uriContentListener = null;
	}
	if (tooltipListener != null) {
		tooltipListener.dispose ();
		tooltipListener = null;
	}
	if (domEventListener != null) {
		domEventListener.dispose ();
		domEventListener = null;
	}
	if (badCertListener != null) {
		badCertListener.dispose ();
		badCertListener = null;
	}
}

@Override
public Object evaluate (String script, boolean trusted) throws SWTException {
	if (!MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR24, false)) {
		return super.evaluate(script);
	}
	if (trusted) {
		return evaluateAsChrome(script);
	} else {
		return evaluateInWindow(script);
	}
}

/**
 * evaluate in Chrome security context
*/
Object evaluateAsChrome (String script) {
	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.QueryInterface(IIDStore.GetIID (nsIInterfaceRequestor.class), result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);
	nsIInterfaceRequestor interfaceRequestor = new nsIInterfaceRequestor (result[0]);
	result[0] = 0;
	rc = XPCOM.NS_GetServiceManager (result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);

	nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
	result[0] = 0;
	rc = interfaceRequestor.GetInterface (IIDStore.GetIID (nsIDOMWindow.class), result);
	interfaceRequestor.Release ();
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);

	nsIDOMWindow window = new nsIDOMWindow (result[0]);
	result[0] = 0;
	byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.EXECUTE_CONTRACTID, true);
	rc = serviceManager.GetServiceByContractID (aContractID, IIDStore.GetIID (Execute.class), result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);

	Execute execute = new Execute (result[0]);
	result[0] = 0;
	nsEmbedString data = new nsEmbedString ("(function(){" + script + "}())");
	execute.EvalAsChrome(window, data, result);
	data.dispose ();
	execute.Release ();
	if (result[0] == 0) return null;

	nsIVariant variant = new nsIVariant (result[0]);
	Object retval = External.convertToJava( variant);
	variant.Release ();
	return retval;
}

/**
 * evaluate in Normal security context
*/
Object evaluateInWindow (String script) throws SWTException {
	delegate.removeWindowSubclass ();

	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.QueryInterface(IIDStore.GetIID (nsIInterfaceRequestor.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
	nsIInterfaceRequestor interfaceRequestor = new nsIInterfaceRequestor (result[0]);
	result[0] = 0;
	rc = XPCOM.NS_GetServiceManager (result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

	nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
	result[0] = 0;
	rc = interfaceRequestor.GetInterface (IIDStore.GetIID (nsIDOMWindow.class), result);
	interfaceRequestor.Release ();
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

	nsIDOMWindow window = new nsIDOMWindow (result[0]);
	result[0] = 0;
	byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.EXECUTE_CONTRACTID, true);
	rc = serviceManager.GetServiceByContractID (aContractID, IIDStore.GetIID (Execute.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

	Execute execute = new Execute (result[0]);
	result[0] = 0;
	nsEmbedString data = new nsEmbedString ("(function(){" + script + "}())");
	execute.EvalInWindow (window, data, result);
	data.dispose ();
	execute.Release ();
	if (result[0] == 0) return null;

	nsIVariant variant = new nsIVariant (result[0]);
	Object retval = External.convertToJava (variant);
	variant.Release ();
	return retval;
}

@Override
public boolean execute (String script) {
	/*
	* This could be the first content that is set into the browser, so
	* ensure that the custom subclass that works around Mozilla bug
	* https://bugzilla.mozilla.org/show_bug.cgi?id=453523 is removed.
	*/
	delegate.removeWindowSubclass ();

	long /*int*/[] result = new long /*int*/[1];
	/*
	* As of mozilla 1.9 executing javascript via the javascript: protocol no
	* longer happens synchronously.  As a result, the result of executing JS
	* is not returned to the java side when expected by the client.  The
	* workaround for version 24 is to use a javascript-implemented component
	* which then executes synchronously.
	*/
	if (MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR24)) {
		result[0] = 0;
		int rc = webBrowser.QueryInterface(IIDStore.GetIID (nsIInterfaceRequestor.class), result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
		nsIInterfaceRequestor interfaceRequestor = new nsIInterfaceRequestor (result[0]);
		result[0] = 0;
		rc = XPCOM.NS_GetServiceManager (result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

		nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
		result[0] = 0;
		rc = interfaceRequestor.GetInterface (IIDStore.GetIID (nsIDOMWindow.class), result);
		interfaceRequestor.Release ();
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

		nsIDOMWindow window = new nsIDOMWindow (result[0]);
		result[0] = 0;
		byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.EXECUTE_CONTRACTID, true);
		rc = serviceManager.GetServiceByContractID (aContractID, IIDStore.GetIID (Execute.class), result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

		Execute execute = new Execute (result[0]);
		result[0] = 0;
		nsEmbedString data = new nsEmbedString (script);
		rc = execute.EvalInWindow (window, data, result);
		if (result[0] != 0)
			new nsIVariant (result[0]).Release ();
		data.dispose ();
		execute.Release ();
		return rc == XPCOM.NS_OK;
	}

	/*
	* As of mozilla 1.9 executing javascript via the javascript: protocol no
	* longer happens synchronously.  As a result, the result of executing JS
	* is not returned to the java side when expected by the client.  The
	* workaround is to invoke the javascript handler directly via C++, which is
	* exposed as of mozilla 1.9.
	*/
	if (MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR1_9)) {
		int rc = XPCOM.NS_GetServiceManager (result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

		nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
		result[0] = 0;
		byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_SCRIPTSECURITYMANAGER_CONTRACTID, true);
		rc = serviceManager.GetServiceByContractID (aContractID, IIDStore.GetIID (nsIScriptSecurityManager.class), result);

		if (rc == XPCOM.NS_OK && result[0] != 0) {
			nsIScriptSecurityManager securityManager = new nsIScriptSecurityManager (result[0]);
			result[0] = 0;
			rc = securityManager.GetSystemPrincipal (result);
			securityManager.Release ();

			if (rc == XPCOM.NS_OK && result[0] != 0) {
				nsIPrincipal principal = new nsIPrincipal (result[0]);
				result[0] = 0;
				rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIInterfaceRequestor.class), result);
				if (rc != XPCOM.NS_OK) error (rc);
				if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

				nsIInterfaceRequestor interfaceRequestor = new nsIInterfaceRequestor (result[0]);
				result[0] = 0;
				rc = interfaceRequestor.GetInterface (IIDStore.GetIID (nsIScriptGlobalObject.class), result);
				interfaceRequestor.Release ();

				if (rc == XPCOM.NS_OK && result[0] != 0) {
					long /*int*/ scriptGlobalObject = result[0];
					result[0] = 0;
					rc = (int/*64*/)XPCOM.nsIScriptGlobalObject_EnsureScriptEnvironment (scriptGlobalObject, 2); /* nsIProgrammingLanguage.JAVASCRIPT */
					if (rc != XPCOM.NS_OK) {
						new nsISupports (scriptGlobalObject).Release ();
					} else {
						long /*int*/ scriptContext = XPCOM.nsIScriptGlobalObject_GetScriptContext (scriptGlobalObject, 2); /* nsIProgrammingLanguage.JAVASCRIPT */

						if (scriptContext != 0) {
							/* ensure that the received nsIScriptContext implements the expected interface */
							nsISupports supports = new nsISupports (scriptContext);
							rc = supports.QueryInterface (IIDStore.GetIID (nsIScriptContext.class), result);
							if (rc == XPCOM.NS_OK && result[0] != 0) {
								new nsISupports (result[0]).Release ();
								result[0] = 0;

								long /*int*/ jsContext = XPCOM.nsIScriptContext_GetNativeContext (scriptContext);
								if (jsContext != 0) {
									int length = script.length ();
									char[] scriptChars = new char[length];
									script.getChars(0, length, scriptChars, 0);
									byte[] urlbytes = MozillaDelegate.wcsToMbcs (null, getUrl (), true);
									rc = principal.GetJSPrincipals (jsContext, result);
									long /*int*/ principals = 0;
									if (rc == XPCOM.NS_OK && result[0] != 0) {
										principals = result[0];
										result[0] = 0;
									}
									byte[] jsLibPath = getJSLibPathBytes ();
									long /*int*/ globalJSObject = XPCOM.JS_GetGlobalObject (jsLibPath, jsContext);
									if (globalJSObject != 0) {
										aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_CONTEXTSTACK_CONTRACTID, true);
										rc = serviceManager.GetServiceByContractID (aContractID, IIDStore.GetIID (nsIJSContextStack.class), result);
										if (rc == XPCOM.NS_OK && result[0] != 0) {
											nsIJSContextStack stack = new nsIJSContextStack (result[0]);
											result[0] = 0;
											rc = stack.Push (jsContext);
											if (rc != XPCOM.NS_OK) {
												stack.Release ();
											} else {
												boolean success;
												if (MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR1_9, true)) {
													success = XPCOM.JS_EvaluateUCScriptForPrincipals (jsLibPath, jsContext, globalJSObject, principals, scriptChars, length, urlbytes, 0, result) != 0;
												} else {
													success = XPCOM.JS_EvaluateUCScriptForPrincipals191 (jsLibPath, jsContext, globalJSObject, principals, scriptChars, length, urlbytes, 0, 0) != 0;
												}
												result[0] = 0;
												rc = stack.Pop (result);
												stack.Release ();
												// should principals be Release()d too?
												new nsISupports (scriptGlobalObject).Release ();
												principal.Release ();
												serviceManager.Release ();
												return success;
											}
										}
									}
								}
							}
						}
					}
					new nsISupports (scriptGlobalObject).Release ();
				}
				principal.Release ();
			}
		}
		serviceManager.Release ();
	}

	/* fall back to the pre-1.9 approach */

	String url = PREFIX_JAVASCRIPT + script + ";void(0);";	//$NON-NLS-1$
	int rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIWebNavigation.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

	nsIWebNavigation webNavigation = new nsIWebNavigation (result[0]);
	char[] arg = url.toCharArray ();
	char[] c = new char[arg.length+1];
	System.arraycopy (arg, 0, c, 0, arg.length);
	rc = webNavigation.LoadURI (c, nsIWebNavigation.LOAD_FLAGS_NONE, 0, 0, 0);
	webNavigation.Release ();
	return rc == XPCOM.NS_OK;
}

static Browser findBrowser (long /*int*/ handle) {
	return MozillaDelegate.findBrowser (handle);
}

static Browser getBrowser (long /*int*/ aDOMWindow) {
	long /*int*/[] result = new long /*int*/[1];
	int rc = XPCOM.NS_GetServiceManager (result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);

	nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
	result[0] = 0;
	byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_WINDOWWATCHER_CONTRACTID, true);
	rc = serviceManager.GetServiceByContractID (aContractID, IIDStore.GetIID (nsIWindowWatcher.class), result);
	if (rc != XPCOM.NS_OK) Mozilla.error(rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);
	serviceManager.Release ();

	nsIWindowWatcher windowWatcher = new nsIWindowWatcher (result[0]);
	result[0] = 0;
	/* the chrome will only be answered for the top-level nsIDOMWindow */
	nsIDOMWindow window = new nsIDOMWindow (aDOMWindow);
	rc = window.GetTop (result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);
	long /*int*/ topDOMWindow = result[0];
	result[0] = 0;
	rc = windowWatcher.GetChromeForWindow (topDOMWindow, result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	new nsISupports (topDOMWindow).Release ();
	windowWatcher.Release ();
	if (result[0] == 0) return null;	/* the parent chrome is disconnected */

	nsIWebBrowserChrome webBrowserChrome = new nsIWebBrowserChrome (result[0]);
	result[0] = 0;
	rc = webBrowserChrome.QueryInterface (IIDStore.GetIID (nsIEmbeddingSiteWindow.class), result);
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);
	webBrowserChrome.Release ();

	nsIEmbeddingSiteWindow embeddingSiteWindow = new nsIEmbeddingSiteWindow (result[0]);
	result[0] = 0;
	IsGettingSiteWindow = true;
	rc = embeddingSiteWindow.GetSiteWindow (result);
	IsGettingSiteWindow = false;
	if (rc != XPCOM.NS_OK) Mozilla.error (rc);
	if (result[0] == 0) Mozilla.error (XPCOM.NS_NOINTERFACE);
	embeddingSiteWindow.Release ();

	return findBrowser (result[0]);
}

@Override
public boolean forward () {
	htmlBytes = null;

	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIWebNavigation.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

	nsIWebNavigation webNavigation = new nsIWebNavigation (result[0]);
	rc = webNavigation.GoForward ();
	webNavigation.Release ();

	return rc == XPCOM.NS_OK;
}

@Override
public String getBrowserType () {
	return "mozilla"; //$NON-NLS-1$
}

static byte[] getJSLibPathBytes () {
	if (jsLibPathBytes == null) {
		String[] names = MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR10) ? MozillaDelegate.getJSLibraryNames () : new String[] {MozillaDelegate.getJSLibraryName_Pre10 ()};
		for (int i = 0; i < names.length; i++) {
			File file = new File (getMozillaPath (), names[i]);
			if (file.exists ()) {
				String pathString = file.getAbsolutePath () + '\0';
				jsLibPathBytes = pathString.getBytes (StandardCharsets.UTF_8); //$NON-NLS-1$
				break;
			}
		}
	}
	return jsLibPathBytes;
}

static String getMozillaPath () {
	if (LocationProvider != null) return LocationProvider.mozillaPath;
	if (!Initialized) return "";

	long /*int*/[] result = new long /*int*/[1];
	int rc = XPCOM.NS_GetServiceManager (result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

	nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
	result[0] = 0;
	byte[] buffer = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_DIRECTORYSERVICE_CONTRACTID, true);
	rc = serviceManager.GetServiceByContractID (buffer, XPCOM.NS_IDIRECTORYSERVICE_IID, result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
	serviceManager.Release();

	nsISupports directoryService = new nsISupports (result[0]);
	result[0] = 0;
	rc = directoryService.QueryInterface (IIDStore.GetIID (nsIProperties.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
	directoryService.Release ();

	nsIProperties properties = new nsIProperties (result[0]);
	result[0] = 0;
	buffer = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_GRE_DIR, true);
	rc = properties.Get (buffer, IIDStore.GetIID (nsIFile.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
	properties.Release ();

	nsIFile mozillaDir = new nsIFile (result[0]);
	result[0] = 0;
	nsEmbedString path = new nsEmbedString ();
	rc = mozillaDir.GetPath (path.getAddress());
	if (rc != XPCOM.NS_OK) error (rc);
	String mozillaPath = path.toString ();
	path.dispose ();
	mozillaDir.Release ();

	return mozillaPath + SEPARATOR_OS;
}

@Override
int getNextFunctionIndex () {
	return NextJSFunctionIndex++;
}

@Override
public String getText () {
	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.GetContentDOMWindow (result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

	nsIDOMWindow window = new nsIDOMWindow (result[0]);
	result[0] = 0;
	rc = window.GetDocument (result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
	window.Release ();

	long /*int*/ document = result[0];
	result[0] = 0;
	rc = XPCOM.NS_GetComponentManager (result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

	nsIComponentManager componentManager = new nsIComponentManager (result[0]);
	result[0] = 0;
	byte[] contractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_DOMSERIALIZER_CONTRACTID, true);
	String text = "";

	rc = componentManager.CreateInstanceByContractID (contractID, 0, IIDStore.GetIID (nsIDOMSerializer.class), result);
	if (rc == XPCOM.NS_OK && result[0] != 0) {
		nsIDOMSerializer serializer = new nsIDOMSerializer (result[0]);
		result[0] = 0;
		if (MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR1_8)) {
			nsEmbedString string = new nsEmbedString ();
			rc = serializer.SerializeToString (document, string.getAddress());
			if (rc == XPCOM.NS_OK) {
				text = string.toString();
			}
			string.dispose ();
		} else {
			rc = serializer.SerializeToString (document, result);
			if (rc == XPCOM.NS_OK) {
				int length = XPCOM.strlen_PRUnichar (result[0]);
				char[] chars = new char[length];
				XPCOM.memmove (chars, result[0], length * 2);
				text = new String (chars);
			}
		}
		serializer.Release ();
	}

	componentManager.Release ();
	new nsISupports (document).Release ();
	return text;
}

@Override
public String getUrl () {
	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIWebNavigation.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

	nsIWebNavigation webNavigation = new nsIWebNavigation (result[0]);
	long /*int*/[] aCurrentURI = new long /*int*/[1];
	rc = webNavigation.GetCurrentURI (aCurrentURI);
	if (rc != XPCOM.NS_OK) error (rc);
	webNavigation.Release ();

	byte[] dest = null;
	if (aCurrentURI[0] != 0) {
		nsIURI uri = new nsIURI (aCurrentURI[0]);
		long /*int*/ aSpec = XPCOM.nsEmbedCString_new ();
		rc = uri.GetSpec (aSpec);
		if (rc != XPCOM.NS_OK) error (rc);
		int length = XPCOM.nsEmbedCString_Length (aSpec);
		long /*int*/ buffer = XPCOM.nsEmbedCString_get (aSpec);
		dest = new byte[length];
		XPCOM.memmove (dest, buffer, length);
		XPCOM.nsEmbedCString_delete (aSpec);
		uri.Release ();
	}
	if (dest == null) return ""; //$NON-NLS-1$

	String location = new String (dest);
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (location.equals (URI_FILEROOT)) {
		location = ABOUT_BLANK;
	} else {
		int length = URI_FILEROOT.length ();
		if (location.startsWith (URI_FILEROOT) && location.charAt (length) == '#') {
			location = ABOUT_BLANK + location.substring (length);
		}
	}
	return location;
}

@Override
public Object getWebBrowser () {
	if ((browser.getStyle () & SWT.MOZILLA) == 0) return null;
	if (webBrowserObject != null) return webBrowserObject;

	try {
		Class<?> clazz = Class.forName ("org.mozilla.xpcom.Mozilla"); //$NON-NLS-1$
		Method method = clazz.getMethod ("getInstance", new Class[0]); //$NON-NLS-1$
		Object mozilla = method.invoke (null, new Object[0]);
		method = clazz.getMethod ("wrapXPCOMObject", new Class[] {Long.TYPE, String.class}); //$NON-NLS-1$
		webBrowserObject = method.invoke (
			mozilla,
			new Object[] {
				new Long (webBrowser.getAddress ()),
				IIDStore.GetIID (nsIWebBrowser.class).toString ()});
		/*
		 * The following AddRef() is needed to offset the automatic Release() that
		 * will be performed by JavaXPCOM when webBrowserObject is finalized.
		 */
		webBrowser.AddRef ();
		return webBrowserObject;
	} catch (ClassNotFoundException e) {
		webBrowserObject = webBrowser;
		return webBrowserObject;
	} catch (NoSuchMethodException e) {
	} catch (IllegalArgumentException e) {
	} catch (IllegalAccessException e) {
	} catch (InvocationTargetException e) {
	}
	return null;
}

/**
 * This method attempts to discover XULRunner and if found returns path of
 * the mozilla library, else return empty string.
 *
 * @return string Mozilla path.
 */
static String InitDiscoverXULRunner () {
	/*
	* Up to three XULRunner detection attempts will be made:
	*
	* 1. A XULRunner with 1.8.1.2 <= version < 2.0, and with "abi" property matching
	* the current runtime.  Note that XULRunner registrations began including abi
	* info as of version 1.9.x, so older versions than this will not be returned.
	* 2. A XULRunner with 1.8.1.2 <= version < 2.0.  XULRunner 1.8.1.2 is the oldest
	* release that enables the Browser to expose its JavaXPCOM interfaces to clients.
	* 3. A XULRunner with 1.8.0.1 <= version < 2.0.
	*/

	GREVersionRange range = null;
	try {
		range = new GREVersionRange ();
	} catch (NoClassDefFoundError e) {
		/* Failed to discover XULRunner, return empty string. */
		return "";
	} catch (UnsatisfiedLinkError e) {
		/* Failed to discover XULRunner, return empty string. */
		return "";
	}
	byte[] bytes = MozillaDelegate.wcsToMbcs (null, GRERANGE_LOWER, true);
	long /*int*/ lower = C.malloc (bytes.length);
	C.memmove (lower, bytes, bytes.length);
	range.lower = lower;
	range.lowerInclusive = LowerRangeInclusive;

	bytes = MozillaDelegate.wcsToMbcs (null, GRERANGE_UPPER, true);
	long /*int*/ upper = C.malloc (bytes.length);
	C.memmove (upper, bytes, bytes.length);
	range.upper = upper;
	range.upperInclusive = UpperRangeInclusive;

	GREProperty property = new GREProperty ();
	bytes = MozillaDelegate.wcsToMbcs (null, PROPERTY_ABI, true);
	long /*int*/ name = C.malloc (bytes.length);
	C.memmove (name, bytes, bytes.length);
	property.property = name;
	bytes = MozillaDelegate.wcsToMbcs (null, Arch () + GCC3, true);
	long /*int*/ value = C.malloc (bytes.length);
	C.memmove (value, bytes, bytes.length);
	property.value = value;

	int length = XPCOMInit.PATH_MAX;
	long /*int*/ greBuffer = C.malloc (length);
	int rc = XPCOMInit.GRE_GetGREPathWithProperties (range, 1, property, 1, greBuffer, length);

	if (rc != XPCOM.NS_OK) {
		/* Fall back to attempt #2 */
		rc = XPCOMInit.GRE_GetGREPathWithProperties (range, 1, property, 0, greBuffer, length); /* note: propertiesLength is 0 */
		if (rc != XPCOM.NS_OK) {
			/* Fall back to attempt #3 */
			C.free (lower);
			bytes = MozillaDelegate.wcsToMbcs (null, GRERANGE_LOWER_FALLBACK, true);
			lower = C.malloc (bytes.length);
			C.memmove (lower, bytes, bytes.length);
			range.lower = lower;
			rc = XPCOMInit.GRE_GetGREPathWithProperties (range, 1, property, 0, greBuffer, length); /* note: propertiesLength is 0 */
		}
	}
	C.free (value);
	C.free (name);
	C.free (lower);
	C.free (upper);

	String result = null;
	if (rc == XPCOM.NS_OK) {
		/* indicates that a XULRunner was found */
		length = C.strlen (greBuffer);
		bytes = new byte[length];
		C.memmove (bytes, greBuffer, length);
		result = new String (MozillaDelegate.mbcsToWcs (null, bytes));
	} else {
		result = ""; //$NON-NLS-1$
	}
	C.free (greBuffer);
	return result;
}

void initExternal (String profilePath) {
	File componentsDir = new File (profilePath, AppFileLocProvider.COMPONENTS_DIR);
	java.io.InputStream is = Library.class.getResourceAsStream ("/swt.xpt"); //$NON-NLS-1$
	if (is != null) {
		if (!componentsDir.exists ()) {
			componentsDir.mkdirs ();
		}
		int read;
		byte [] buffer = new byte [4096];
		File file = new File (componentsDir, "swt.xpt"); //$NON-NLS-1$
		try {
			FileOutputStream os = new FileOutputStream (file);
			while ((read = is.read (buffer)) != -1) {
				os.write(buffer, 0, read);
			}
			os.close ();
			is.close ();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}
	is = Library.class.getResourceAsStream ("/swt.js"); //$NON-NLS-1$
	if (is != null) {
		if (!componentsDir.exists ()) {
			componentsDir.mkdirs ();
		}
		int read;
		byte [] buffer = new byte [4096];
		File file = new File (componentsDir, "swt.js"); //$NON-NLS-1$
		try {
			FileOutputStream os = new FileOutputStream (file);
			while ((read = is.read (buffer)) != -1) {
				os.write(buffer, 0, read);
			}
			os.close ();
			is.close ();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}
	is = Library.class.getResourceAsStream ("/chrome.manifest"); //$NON-NLS-1$
	if (is != null) {
		int read;
		byte [] buffer = new byte [4096];
		File file = new File (profilePath, "chrome.manifest"); //$NON-NLS-1$
		try {
			FileOutputStream os = new FileOutputStream (file);
			while ((read = is.read (buffer)) != -1) {
				os.write(buffer, 0, read);
			}
			os.close ();
			is.close ();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}
}

void initFactories (nsIServiceManager serviceManager, nsIComponentManager componentManager, boolean isXULRunner) {
	long /*int*/[] result = new long /*int*/[1];

	int rc = componentManager.QueryInterface (IIDStore.GetIID (nsIComponentRegistrar.class), result);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	if (result[0] == 0) {
		browser.dispose ();
		error (XPCOM.NS_NOINTERFACE);
	}

	nsIComponentRegistrar componentRegistrar = new nsIComponentRegistrar (result[0]);
	result[0] = 0;
	if (!MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR24)) {
		componentRegistrar.AutoRegister (0);	 /* detect the External component */
	}

	PromptService2Factory factory = new PromptService2Factory ();
	factory.AddRef ();
	byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_PROMPTSERVICE_CONTRACTID, true);
	byte[] aClassName = MozillaDelegate.wcsToMbcs (null, "swtPromptService", true); //$NON-NLS-1$
	rc = componentRegistrar.RegisterFactory (XPCOM.NS_PROMPTSERVICE_CID, aClassName, aContractID, factory.getAddress ());
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	if (MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR10)) { /* > 10.x */
		aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_PROMPTER_CONTRACTID, true);
		aClassName = MozillaDelegate.wcsToMbcs (null, "swtPrompter", true); //$NON-NLS-1$
		rc = componentRegistrar.RegisterFactory (XPCOM.NS_PROMPTER_CID, aClassName, aContractID, factory.getAddress ());
		if (rc != XPCOM.NS_OK) {
			browser.dispose ();
			error (rc);
		}
		aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_AUTHPROMPTER_CONTRACTID, true);
		aClassName = MozillaDelegate.wcsToMbcs (null, "swtAuthPrompter", true); //$NON-NLS-1$
		rc = componentRegistrar.RegisterFactory (XPCOM.NS_AUTHPROMPTER_CID, aClassName, aContractID, factory.getAddress ());
		if (rc != XPCOM.NS_OK) {
			browser.dispose ();
			error (rc);
		}
	}
	factory.Release();

	ExternalFactory externalFactory = new ExternalFactory ();
	externalFactory.AddRef ();
	aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.EXTERNAL_CONTRACTID, true);
	aClassName = MozillaDelegate.wcsToMbcs (null, "External", true); //$NON-NLS-1$
	rc = componentRegistrar.RegisterFactory (XPCOM.EXTERNAL_CID, aClassName, aContractID, externalFactory.getAddress ());
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	externalFactory.Release ();

	rc = serviceManager.GetService (XPCOM.NS_CATEGORYMANAGER_CID, IIDStore.GetIID (nsICategoryManager.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

	nsICategoryManager categoryManager = new nsICategoryManager (result[0]);
	result[0] = 0;
	byte[] entry = MozillaDelegate.wcsToMbcs (null, "external", true); //$NON-NLS-1$

	/* register for mozilla versions <= 3.6.x */
	byte[] category = MozillaDelegate.wcsToMbcs (null, "JavaScript global property", true); //$NON-NLS-1$
	rc = categoryManager.AddCategoryEntry (category, entry, aContractID, 0, 1, result);
	result[0] = 0;

	/* register for mozilla versions >= 3.6.x */
	category = MozillaDelegate.wcsToMbcs (null, "JavaScript-global-property", true); //$NON-NLS-1$
	rc = categoryManager.AddCategoryEntry (category, entry, aContractID, 0, 1, result);
	result[0] = 0;

	categoryManager.Release ();

	if (!MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR10)) { /* 1.4.x - 3.6.x */
		DownloadFactory downloadFactory = new DownloadFactory ();
		downloadFactory.AddRef ();
		aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_DOWNLOAD_CONTRACTID, true);
		aClassName = MozillaDelegate.wcsToMbcs (null, "swtDownload", true); //$NON-NLS-1$
		rc = componentRegistrar.RegisterFactory (XPCOM.NS_DOWNLOAD_CID, aClassName, aContractID, downloadFactory.getAddress ());
		if (rc != XPCOM.NS_OK) {
			browser.dispose ();
			error (rc);
		}
		downloadFactory.Release ();
	}

	if (!MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR24)) { /* 1.4.x - 10.x */
		FilePickerFactory pickerFactory = new FilePickerFactory ();
		pickerFactory.AddRef ();
		aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_FILEPICKER_CONTRACTID, true);
		aClassName = MozillaDelegate.wcsToMbcs (null, "swtFilePicker", true); //$NON-NLS-1$
		rc = componentRegistrar.RegisterFactory (XPCOM.NS_FILEPICKER_CID, aClassName, aContractID, pickerFactory.getAddress ());
		/* a failure here is fine, it likely indicates that a default implementation is provided */
		pickerFactory.Release ();
	}

	componentRegistrar.Release ();
}

void initJavaXPCOM (String mozillaPath) {
	try {
		Class<?> clazz = Class.forName ("org.mozilla.xpcom.Mozilla"); //$NON-NLS-1$
		Method method = clazz.getMethod ("getInstance", new Class[0]); //$NON-NLS-1$
		Object mozilla = method.invoke (null, new Object[0]);
		method = clazz.getMethod ("getComponentManager", new Class[0]); //$NON-NLS-1$
		try {
			method.invoke (mozilla, new Object[0]);
		} catch (InvocationTargetException e) {
			/* indicates that JavaXPCOM has not been initialized yet */
			Class<?> fileClass = Class.forName ("java.io.File"); //$NON-NLS-1$
			method = clazz.getMethod ("initialize", new Class[] {fileClass}); //$NON-NLS-1$
			Constructor<?> constructor = fileClass.getDeclaredConstructor (new Class[] {String.class});
			Object argument = constructor.newInstance (new Object[] {mozillaPath});
			method.invoke (mozilla, new Object[] {argument});
		}
	} catch (ClassNotFoundException e) {
		/* JavaXPCOM is not on the classpath */
	} catch (NoSuchMethodException e) {
		/* the JavaXPCOM on the classpath does not implement initialize() */
	} catch (IllegalArgumentException e) {
	} catch (IllegalAccessException e) {
	} catch (InvocationTargetException e) {
	} catch (InstantiationException e) {
	}
}

String initMozilla (String mozillaPath) {
	/* attempt to use the GRE pointed at by MOZILLA_FIVE_HOME */
	long /*int*/ ptr = C.getenv (MozillaDelegate.wcsToMbcs (null, MOZILLA_FIVE_HOME, true));
	if (ptr != 0) {
		int length = C.strlen (ptr);
		byte[] buffer = new byte[length];
		C.memmove (buffer, ptr, length);
		mozillaPath = new String (MozillaDelegate.mbcsToWcs (null, buffer));

		/* ensure that client-supplied path is using correct separators */
		if (SEPARATOR_OS == '/') {
			mozillaPath = mozillaPath.replace ('\\', SEPARATOR_OS);
		} else {
			mozillaPath = mozillaPath.replace ('/', SEPARATOR_OS);
		}
	} else {
		browser.dispose ();
		SWT.error (SWT.ERROR_NO_HANDLES, null, " [Unknown Mozilla path (MOZILLA_FIVE_HOME not set)]"); //$NON-NLS-1$
	}
	if (Device.DEBUG) System.out.println ("Mozilla path: " + mozillaPath); //$NON-NLS-1$

	try {
		Library.loadLibrary ("swt-mozilla"); //$NON-NLS-1$
	} catch (UnsatisfiedLinkError e) {
		browser.dispose ();
		SWT.error (SWT.ERROR_NO_HANDLES, e, " [MOZILLA_FIVE_HOME='" + mozillaPath + "']"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	return mozillaPath;
}

void initXPCOM (String mozillaPath, boolean isXULRunner) {
	long /*int*/[] result = new long /*int*/[1];

	nsEmbedString pathString = new nsEmbedString (mozillaPath);
	int rc = XPCOM.NS_NewLocalFile (pathString.getAddress (), 1, result);
	pathString.dispose ();
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	if (result[0] == 0) {
		browser.dispose ();
		error (XPCOM.NS_ERROR_NULL_POINTER);
	}

	nsIFile localFile = new nsIFile (result[0]);
	result[0] = 0;
	if (isXULRunner) {
		int size = XPCOM.nsDynamicFunctionLoad_sizeof ();
		/* alloc memory for two structs, the second is empty to signify the end of the list */
		long /*int*/ ptr = C.malloc (size * 2);
		C.memset (ptr, 0, size * 2);
		nsDynamicFunctionLoad functionLoad = new nsDynamicFunctionLoad ();

		/*
		 * Attempt to load the XRE_InitEmbedding2 function first, which is present in
		 * mozilla versions > 3.x.
		 */
		byte[] bytes = MozillaDelegate.wcsToMbcs (null, "XRE_InitEmbedding2", true); //$NON-NLS-1$
		functionLoad.functionName = C.malloc (bytes.length);
		C.memmove (functionLoad.functionName, bytes, bytes.length);
		functionLoad.function = C.malloc (C.PTR_SIZEOF);
		C.memmove (functionLoad.function, new long /*int*/[] {0} , C.PTR_SIZEOF);
		XPCOM.memmove (ptr, functionLoad, XPCOM.nsDynamicFunctionLoad_sizeof ());
		rc = XPCOM.XPCOMGlueLoadXULFunctions (ptr);
		if (rc == XPCOM.NS_OK) { /* > 3.x */
			result[0] = 0;
			rc = localFile.QueryInterface (IIDStore.GetIID (nsIFile.class, MozillaVersion.VERSION_XR31, true), result);
			if (rc == XPCOM.NS_OK) { /* 31.x */
				MozillaVersion.SetCurrentVersion (MozillaVersion.VERSION_XR31);
			} else {
				rc = localFile.QueryInterface (IIDStore.GetIID (nsIFile.class, MozillaVersion.VERSION_XR24, true), result);
				if (rc == XPCOM.NS_OK) { /* 24.x */
					MozillaVersion.SetCurrentVersion (MozillaVersion.VERSION_XR24);
				} else { /* 10.x */
					rc = localFile.QueryInterface (IIDStore.GetIID (nsILocalFile.class, MozillaVersion.VERSION_XR10), result);
					if (rc != XPCOM.NS_OK) {
						/* appears to be an unsupported version */
						browser.dispose ();
						error (rc);
					}
					MozillaVersion.SetCurrentVersion (MozillaVersion.VERSION_XR10);
				}
			}
			if (result[0] != 0) new nsISupports (result[0]).Release();
			result[0] = 0;
		} else {
			/*
			 * XRE_InitEmbedding2 was not found, so fall back to XRE_InitEmbedding, which is
			 * present in older mozilla versions.
			 */
			C.free (functionLoad.functionName);
			bytes = MozillaDelegate.wcsToMbcs (null, "XRE_InitEmbedding", true); //$NON-NLS-1$
			functionLoad.functionName = C.malloc (bytes.length);
			C.memmove (functionLoad.functionName, bytes, bytes.length);
			XPCOM.memmove (ptr, functionLoad, XPCOM.nsDynamicFunctionLoad_sizeof ());
			rc = XPCOM.XPCOMGlueLoadXULFunctions (ptr);
			/*
			 * For now guess that the version is 1.9.2.x (aka 3.6.x), will determine
			 * this more precisely once a browser instance has been created.
			 */
			MozillaVersion.SetCurrentVersion (MozillaVersion.VERSION_XR1_9_2);
		}

		C.memmove (result, functionLoad.function, C.PTR_SIZEOF);
		long /*int*/ functionPtr = result[0];
		result[0] = 0;
		C.free (functionLoad.function);
		C.free (functionLoad.functionName);
		C.free (ptr);
		if (functionPtr == 0) {
			browser.dispose ();
			error (XPCOM.NS_ERROR_NULL_POINTER);
		}

		MozillaDelegate.loadAdditionalLibraries (mozillaPath, true);

		if (MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR10)) {
			rc = XPCOM.Call (functionPtr, localFile.getAddress (), localFile.getAddress (), LocationProvider.getAddress ());
		} else {
			rc = XPCOM.Call (functionPtr, localFile.getAddress (), localFile.getAddress (), LocationProvider.getAddress (), 0, 0);
		}
		if (rc == XPCOM.NS_OK) {
			System.setProperty (XULRUNNER_PATH, mozillaPath);
		}
	} else {
		/*
		 * For now guess that the version is equivalent to Firefox 3.6.x (aka XULRunner 1.9.2.x),
		 * will determine this more precisely once a browser instance has been created.
		 */
		MozillaVersion.SetCurrentVersion (MozillaVersion.VERSION_XR1_9_2);
		rc = XPCOM.NS_InitXPCOM2 (0, localFile.getAddress(), LocationProvider.getAddress ());
	}
	localFile.Release ();
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		SWT.error (SWT.ERROR_NO_HANDLES, null, " [MOZILLA_FIVE_HOME may not point at an embeddable GRE] [NS_InitEmbedding " + mozillaPath + " error " + rc + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	System.setProperty (GRE_INITIALIZED, TRUE);
}

void initPreferences (nsIServiceManager serviceManager, nsIComponentManager componentManager) {
	long /*int*/[] result = new long /*int*/[1];

	/*
	 * As a result of using a common profile the user cannot change their locale
	 * and charset.  The fix for this is to set mozilla's locale and charset
	 * preference values according to the user's current locale and charset.
	 */
	byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_PREFSERVICE_CONTRACTID, true);
	int rc = serviceManager.GetServiceByContractID (aContractID, IIDStore.GetIID (nsIPrefService.class), result);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	if (result[0] == 0) {
		browser.dispose ();
		error (XPCOM.NS_NOINTERFACE);
	}

	nsIPrefService prefService = new nsIPrefService (result[0]);
	result[0] = 0;
	byte[] buffer = new byte[1];
	rc = prefService.GetBranch (buffer, result);	/* empty buffer denotes root preference level */
	prefService.Release ();
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	if (result[0] == 0) {
		browser.dispose ();
		error (XPCOM.NS_NOINTERFACE);
	}

	nsIPrefBranch prefBranch = new nsIPrefBranch (result[0]);
	result[0] = 0;

	/* get Mozilla's current locale preference value */
	String prefLocales = null;
	nsIPrefLocalizedString localizedString = null;
	buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_LANGUAGES, true);
	rc = prefBranch.GetComplexValue (buffer, IIDStore.GetIID (nsIPrefLocalizedString.class), result);
	/*
	 * Feature of Debian.  For some reason attempting to query for the current locale
	 * preference fails on Debian.  The workaround for this is to assume a value of
	 * "en-us,en" since this is typically the default value when mozilla is used without
	 * a profile.
	 */
	if (rc != XPCOM.NS_OK) {
		prefLocales = "en-us,en" + TOKENIZER_LOCALE;	//$NON-NLS-1$
	} else {
		if (result[0] == 0) {
			browser.dispose ();
			error (XPCOM.NS_NOINTERFACE);
		}
		localizedString = new nsIPrefLocalizedString (result[0]);
		result[0] = 0;
		rc = localizedString.ToString (result);
		if (rc != XPCOM.NS_OK) {
			browser.dispose ();
			error (rc);
		}
		if (result[0] == 0) {
			browser.dispose ();
			error (XPCOM.NS_NOINTERFACE);
		}
		int length = XPCOM.strlen_PRUnichar (result[0]);
		char[] dest = new char[length];
		XPCOM.memmove (dest, result[0], length * 2);
		prefLocales = new String (dest) + TOKENIZER_LOCALE;
	}
	result[0] = 0;

	/*
	 * construct the new locale preference value by prepending the
	 * user's current locale and language to the original value
	 */
	Locale locale = Locale.getDefault ();
	String language = locale.getLanguage ();
	String country = locale.getCountry ();
	StringBuffer stringBuffer = new StringBuffer (language);
	stringBuffer.append (SEPARATOR_LOCALE);
	stringBuffer.append (country.toLowerCase ());
	stringBuffer.append (TOKENIZER_LOCALE);
	stringBuffer.append (language);
	stringBuffer.append (TOKENIZER_LOCALE);
	String newLocales = stringBuffer.toString ();

	int start, end = -1;
	do {
		start = end + 1;
		end = prefLocales.indexOf (TOKENIZER_LOCALE, start);
		String token;
		if (end == -1) {
			token = prefLocales.substring (start);
		} else {
			token = prefLocales.substring (start, end);
		}
		if (token.length () > 0) {
			token = (token + TOKENIZER_LOCALE).trim ();
			/* ensure that duplicate locale values are not added */
			if (newLocales.indexOf (token) == -1) {
				stringBuffer.append (token);
			}
		}
	} while (end != -1);
	newLocales = stringBuffer.toString ();
	if (!newLocales.equals (prefLocales)) {
		/* write the new locale value */
		newLocales = newLocales.substring (0, newLocales.length () - TOKENIZER_LOCALE.length ()); /* remove trailing tokenizer */
		int length = newLocales.length ();
		char[] charBuffer = new char[length + 1];
		newLocales.getChars (0, length, charBuffer, 0);
		if (localizedString == null) {
			byte[] contractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_PREFLOCALIZEDSTRING_CONTRACTID, true);
			rc = componentManager.CreateInstanceByContractID (contractID, 0, IIDStore.GetIID (nsIPrefLocalizedString.class), result);
			if (rc != XPCOM.NS_OK) {
				browser.dispose ();
				error (rc);
			}
			if (result[0] == 0) {
				browser.dispose ();
				error (XPCOM.NS_NOINTERFACE);
			}
			localizedString = new nsIPrefLocalizedString (result[0]);
			result[0] = 0;
		}
		localizedString.SetDataWithLength (length, charBuffer);
		rc = prefBranch.SetComplexValue (buffer, IIDStore.GetIID (nsIPrefLocalizedString.class), localizedString.getAddress());
	}
	if (localizedString != null) {
		localizedString.Release ();
		localizedString = null;
	}

	/* get Mozilla's current charset preference value */
	String prefCharset = null;
	buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_CHARSET, true);
	rc = prefBranch.GetComplexValue (buffer, IIDStore.GetIID (nsIPrefLocalizedString.class), result);
	/*
	 * Feature of Debian.  For some reason attempting to query for the current charset
	 * preference fails on Debian.  The workaround for this is to assume a value of
	 * "ISO-8859-1" since this is typically the default value when mozilla is used
	 * without a profile.
	 */
	if (rc != XPCOM.NS_OK) {
		prefCharset = "ISO-8859-1";	//$NON-NLS-1$
	} else {
		if (result[0] == 0) {
			browser.dispose ();
			error (XPCOM.NS_NOINTERFACE);
		}
		localizedString = new nsIPrefLocalizedString (result[0]);
		result[0] = 0;
		rc = localizedString.ToString (result);
		if (rc != XPCOM.NS_OK) {
			browser.dispose ();
			error (rc);
		}
		if (result[0] == 0) {
			browser.dispose ();
			error (XPCOM.NS_NOINTERFACE);
		}
		int length = XPCOM.strlen_PRUnichar (result[0]);
		char[] dest = new char[length];
		XPCOM.memmove (dest, result[0], length * 2);
		prefCharset = new String (dest);
	}
	result[0] = 0;

	String newCharset = System.getProperty ("file.encoding");	// $NON-NLS-1$
	if (!newCharset.equals (prefCharset)) {
		/* write the new charset value */
		int length = newCharset.length ();
		char[] charBuffer = new char[length + 1];
		newCharset.getChars (0, length, charBuffer, 0);
		if (localizedString == null) {
			byte[] contractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_PREFLOCALIZEDSTRING_CONTRACTID, true);
			rc = componentManager.CreateInstanceByContractID (contractID, 0, IIDStore.GetIID (nsIPrefLocalizedString.class), result);
			if (rc != XPCOM.NS_OK) {
				browser.dispose ();
				error (rc);
			}
			if (result[0] == 0) {
				browser.dispose ();
				error (XPCOM.NS_NOINTERFACE);
			}
			localizedString = new nsIPrefLocalizedString (result[0]);
			result[0] = 0;
		}
		localizedString.SetDataWithLength (length, charBuffer);
		rc = prefBranch.SetComplexValue (buffer, IIDStore.GetIID (nsIPrefLocalizedString.class), localizedString.getAddress ());
	}
	if (localizedString != null) localizedString.Release ();

	/*
	* Check for proxy values set as documented java properties and update mozilla's
	* preferences with these values if needed.
	*/
	String proxyHost = System.getProperty (PROPERTY_PROXYHOST);
	int port = -1;
	int propertyValue = Integer.getInteger (PROPERTY_PROXYPORT, -1).intValue ();
	if (0 <= propertyValue && propertyValue <= MAX_PORT) port = propertyValue;

	if (proxyHost != null) {
		byte[] contractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_PREFLOCALIZEDSTRING_CONTRACTID, true);
		rc = componentManager.CreateInstanceByContractID (contractID, 0, IIDStore.GetIID (nsIPrefLocalizedString.class), result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

		localizedString = new nsIPrefLocalizedString (result[0]);
		result[0] = 0;

		int length = proxyHost.length ();
		char[] charBuffer = new char[length];
		proxyHost.getChars (0, length, charBuffer, 0);
		rc = localizedString.SetDataWithLength (length, charBuffer);
		if (rc != XPCOM.NS_OK) error (rc);

		buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_PROXYHOST_FTP, true);
		rc = prefBranch.GetComplexValue (buffer, IIDStore.GetIID (nsIPrefLocalizedString.class), result);
		if (rc == XPCOM.NS_OK && result[0] != 0) {
			nsIPrefLocalizedString value = new nsIPrefLocalizedString (result[0]);
			result[0] = 0;
			rc = value.ToString (result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_ERROR_NULL_POINTER);
			length = XPCOM.strlen_PRUnichar (result[0]);
			char[] dest = new char[length];
			XPCOM.memmove (dest, result[0], length * 2);
			oldProxyHostFTP = new String (dest);
		} else {
			/* value is default */
			oldProxyHostFTP = DEFAULTVALUE_STRING;
		}
		result[0] = 0;
		rc = prefBranch.SetComplexValue (buffer, IIDStore.GetIID (nsIPrefLocalizedString.class), localizedString.getAddress ());
		if (rc != XPCOM.NS_OK) error (rc);

		buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_PROXYHOST_HTTP, true);
		rc = prefBranch.GetComplexValue (buffer, IIDStore.GetIID (nsIPrefLocalizedString.class), result);
		if (rc == XPCOM.NS_OK && result[0] != 0) {
			nsIPrefLocalizedString value = new nsIPrefLocalizedString (result[0]);
			result[0] = 0;
			rc = value.ToString (result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_ERROR_NULL_POINTER);
			length = XPCOM.strlen_PRUnichar (result[0]);
			char[] dest = new char[length];
			XPCOM.memmove (dest, result[0], length * 2);
			oldProxyHostHTTP = new String (dest);
		} else {
			/* value is default */
			oldProxyHostHTTP = DEFAULTVALUE_STRING;
		}
		result[0] = 0;
		rc = prefBranch.SetComplexValue (buffer, IIDStore.GetIID (nsIPrefLocalizedString.class), localizedString.getAddress ());
		if (rc != XPCOM.NS_OK) error (rc);

		buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_PROXYHOST_SSL, true);
		rc = prefBranch.GetComplexValue (buffer, IIDStore.GetIID (nsIPrefLocalizedString.class), result);
		if (rc == XPCOM.NS_OK && result[0] != 0) {
			nsIPrefLocalizedString value = new nsIPrefLocalizedString (result[0]);
			result[0] = 0;
			rc = value.ToString (result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_ERROR_NULL_POINTER);
			length = XPCOM.strlen_PRUnichar (result[0]);
			char[] dest = new char[length];
			XPCOM.memmove (dest, result[0], length * 2);
			oldProxyHostSSL = new String (dest);
		} else {
			/* value is default */
			oldProxyHostSSL = DEFAULTVALUE_STRING;
		}
		result[0] = 0;
		rc = prefBranch.SetComplexValue (buffer, IIDStore.GetIID (nsIPrefLocalizedString.class), localizedString.getAddress ());
		if (rc != XPCOM.NS_OK) error (rc);

		localizedString.Release ();
	}

	int[] intResult = new int[1]; /* C long*/
	if (port != -1) {
		buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_PROXYPORT_FTP, true);
		rc = prefBranch.GetIntPref (buffer, intResult);
		if (rc != XPCOM.NS_OK) error (rc);
		oldProxyPortFTP = intResult[0];
		intResult[0] = 0;
		rc = prefBranch.SetIntPref (buffer, port);
		if (rc != XPCOM.NS_OK) error (rc);

		buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_PROXYPORT_HTTP, true);
		rc = prefBranch.GetIntPref (buffer, intResult);
		if (rc != XPCOM.NS_OK) error (rc);
		oldProxyPortHTTP = intResult[0];
		intResult[0] = 0;
		rc = prefBranch.SetIntPref (buffer, port);
		if (rc != XPCOM.NS_OK) error (rc);

		buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_PROXYPORT_SSL, true);
		rc = prefBranch.GetIntPref (buffer, intResult);
		if (rc != XPCOM.NS_OK) error (rc);
		oldProxyPortSSL = intResult[0];
		intResult[0] = 0;
		rc = prefBranch.SetIntPref (buffer, port);
		if (rc != XPCOM.NS_OK) error (rc);
	}

	if (proxyHost != null || port != -1) {
		buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_PROXYTYPE, true);
		rc = prefBranch.GetIntPref (buffer, intResult);
		if (rc != XPCOM.NS_OK) error (rc);
		oldProxyType = intResult[0];
		intResult[0] = 0;
		rc = prefBranch.SetIntPref (buffer, 1);
		if (rc != XPCOM.NS_OK) error (rc);
	}

	/*
	* Ensure that windows that are shown during page loads are not blocked.  Firefox may
	* try to block these by default since such windows are often unwelcome, but this
	* assumption should not be made in the Browser's context.  Since the Browser client
	* is responsible for creating the new Browser and Shell in an OpenWindowListener,
	* they should decide whether the new window is unwelcome or not and act accordingly.
	*/
	buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_DISABLEOPENDURINGLOAD, true);
	rc = prefBranch.SetBoolPref (buffer, 0);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}

	/* Ensure that the status text can be set through means like javascript */
	buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_DISABLEWINDOWSTATUSCHANGE, true);
	rc = prefBranch.SetBoolPref (buffer, 0);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}

	/* Ensure that the status line can be hidden when opening a window from javascript */
	buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_DISABLEOPENWINDOWSTATUSHIDE, true);
	rc = prefBranch.SetBoolPref (buffer, 0);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}

	/* Ensure that javascript execution is enabled since this is the Browser's default behaviour */
	buffer = MozillaDelegate.wcsToMbcs (null, PREFERENCE_JAVASCRIPTENABLED, true);
	rc = prefBranch.SetBoolPref (buffer, 1);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}

	prefBranch.Release ();
}

void initProfile (nsIServiceManager serviceManager, boolean isXULRunner) {
	long /*int*/[] result = new long /*int*/[1];

	byte[] buffer = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_OBSERVER_CONTRACTID, true);
	int rc = serviceManager.GetServiceByContractID (buffer, IIDStore.GetIID (nsIObserverService.class), result);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	if (result[0] == 0) {
		browser.dispose ();
		error (XPCOM.NS_NOINTERFACE);
	}

	nsIObserverService observerService = new nsIObserverService (result[0]);
	result[0] = 0;
	buffer = MozillaDelegate.wcsToMbcs (null, PROFILE_DO_CHANGE, true);
	int length = STARTUP.length ();
	char[] chars = new char [length + 1];
	STARTUP.getChars (0, length, chars, 0);
	rc = observerService.NotifyObservers (0, buffer, chars);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	buffer = MozillaDelegate.wcsToMbcs (null, PROFILE_AFTER_CHANGE, true);
	rc = observerService.NotifyObservers (0, buffer, chars);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	observerService.Release ();

	if (isXULRunner) {
		int size = XPCOM.nsDynamicFunctionLoad_sizeof ();
		/* alloc memory for two structs, the second is empty to signify the end of the list */
		long /*int*/ ptr = C.malloc (size * 2);
		C.memset (ptr, 0, size * 2);
		nsDynamicFunctionLoad functionLoad = new nsDynamicFunctionLoad ();
		byte[] bytes = MozillaDelegate.wcsToMbcs (null, "XRE_NotifyProfile", true); //$NON-NLS-1$
		functionLoad.functionName = C.malloc (bytes.length);
		C.memmove (functionLoad.functionName, bytes, bytes.length);
		functionLoad.function = C.malloc (C.PTR_SIZEOF);
		C.memmove (functionLoad.function, new long /*int*/[] {0} , C.PTR_SIZEOF);
		XPCOM.memmove (ptr, functionLoad, XPCOM.nsDynamicFunctionLoad_sizeof ());
		XPCOM.XPCOMGlueLoadXULFunctions (ptr);
		C.memmove (result, functionLoad.function, C.PTR_SIZEOF);
		long /*int*/ functionPtr = result[0];
		result[0] = 0;
		C.free (functionLoad.function);
		C.free (functionLoad.functionName);
		C.free (ptr);
		/* functionPtr == 0 for xulrunner < 1.9 */
		if (functionPtr != 0) {
			rc = XPCOM.Call (functionPtr);
			if (rc != XPCOM.NS_OK) {
				browser.dispose ();
				error (rc);
			}
		}
	}
}

void initSpinup (nsIComponentManager componentManager) {
	if (MozillaDelegate.needsSpinup ()) {
		long /*int*/[] result = new long /*int*/[1];

		/* nsIAppShell is discontinued as of xulrunner 1.9, so do not fail if it is not found */
		int rc = componentManager.CreateInstance (XPCOM.NS_APPSHELL_CID, 0, IIDStore.GetIID (nsIAppShell.class, MozillaVersion.VERSION_BASE), result);
		if (rc != XPCOM.NS_ERROR_NO_INTERFACE) {
			if (rc != XPCOM.NS_OK) {
				browser.dispose ();
				error (rc);
			}
			if (result[0] == 0) {
				browser.dispose ();
				error (XPCOM.NS_NOINTERFACE);
			}

			AppShell = new nsIAppShell (result[0]);
			result[0] = 0;
			rc = AppShell.Create (0, null);
			if (rc != XPCOM.NS_OK) {
				browser.dispose ();
				error (rc);
			}
			rc = AppShell.Spinup ();
			if (rc != XPCOM.NS_OK) {
				browser.dispose ();
				error (rc);
			}
		}
	}
}

void initWebBrowserWindows () {
	int rc = webBrowser.SetContainerWindow (webBrowserChrome.getAddress());
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}

	long /*int*/[] result = new long /*int*/[1];
	rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIBaseWindow.class), result);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	if (result[0] == 0) {
		browser.dispose ();
		error (XPCOM.NS_ERROR_NO_INTERFACE);
	}

	nsIBaseWindow baseWindow = new nsIBaseWindow (result[0]);
	result[0] = 0;
	Rectangle rect = browser.getClientArea ();
	if (rect.isEmpty ()) {
		rect.width = 1;
		rect.height = 1;
	}

	embedHandle = delegate.getHandle ();

	rc = baseWindow.InitWindow (embedHandle, 0, 0, 0, rect.width, rect.height);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (XPCOM.NS_ERROR_FAILURE);
	}
	rc = delegate.createBaseWindow (baseWindow);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (XPCOM.NS_ERROR_FAILURE);
	}
	rc = baseWindow.SetVisibility (1);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (XPCOM.NS_ERROR_FAILURE);
	}
	baseWindow.Release ();
}

void initWindowCreator (nsIServiceManager serviceManager) {
	WindowCreator = new WindowCreator2 ();
	WindowCreator.AddRef ();

	long /*int*/[] result = new long /*int*/[1];
	byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_WINDOWWATCHER_CONTRACTID, true);
	int rc = serviceManager.GetServiceByContractID (aContractID, IIDStore.GetIID (nsIWindowWatcher.class), result);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	if (result[0] == 0) {
		browser.dispose ();
		error (XPCOM.NS_NOINTERFACE);
	}

	nsIWindowWatcher windowWatcher = new nsIWindowWatcher (result[0]);
	result[0] = 0;
	rc = windowWatcher.SetWindowCreator (WindowCreator.getAddress());
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	windowWatcher.Release ();
}

String initXULRunner (String mozillaPath) {
	if (Device.DEBUG) System.out.println ("XULRunner path: " + mozillaPath); //$NON-NLS-1$
	try {
		Library.loadLibrary ("swt-xulrunner"); //$NON-NLS-1$
	} catch (UnsatisfiedLinkError e) {
		SWT.error (SWT.ERROR_NO_HANDLES, e);
	}

	/*
	* Remove the trailing xpcom lib name from mozillaPath because the
	* Mozilla.initialize and NS_InitXPCOM2 invocations require a directory name only.
	*/
	String mozillaDirPath = mozillaPath.substring (0, mozillaPath.lastIndexOf (SEPARATOR_OS));
	MozillaDelegate.loadAdditionalLibraries (mozillaDirPath, false);
	byte[] path = MozillaDelegate.wcsToMbcs (null, mozillaPath, true);
	int rc = XPCOM.XPCOMGlueStartup (path);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	XPCOMWasGlued = true;

	return mozillaDirPath;
}

@Override
public boolean isBackEnabled () {
	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIWebNavigation.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

	nsIWebNavigation webNavigation = new nsIWebNavigation (result[0]);
	int[] aCanGoBack = new int[1]; /* PRBool */
	rc = webNavigation.GetCanGoBack (aCanGoBack);
	webNavigation.Release ();
	return aCanGoBack[0] != 0;
}

@Override
public boolean isForwardEnabled () {
	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIWebNavigation.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

	nsIWebNavigation webNavigation = new nsIWebNavigation (result[0]);
	int[] aCanGoForward = new int[1]; /* PRBool */
	rc = webNavigation.GetCanGoForward (aCanGoForward);
	webNavigation.Release ();
	return aCanGoForward[0] != 0;
}

static String error (int code) {
	throw new SWTError ("XPCOM error 0x" + Integer.toHexString(code)); //$NON-NLS-1$
}

void onDispose (Display display) {
	/* invoke onbeforeunload handlers */
	if (!browser.isClosing && !browser.isDisposed()) {
		LocationListener[] oldLocationListeners = locationListeners;
		locationListeners = new LocationListener[0];
		ignoreAllMessages = true;
		execute ("window.location.replace('about:blank');"); //$NON-NLS-1$
		ignoreAllMessages = false;
		locationListeners = oldLocationListeners;
	}

	if (badCertRequest != 0) {
		new nsISupports (badCertRequest).Release ();
	}

	int rc = webBrowser.RemoveWebBrowserListener (weakReference.getAddress (), IIDStore.GetIID (nsIWebProgressListener.class));
	if (rc != XPCOM.NS_OK) error (rc);

	rc = webBrowser.SetParentURIContentListener (0);
	if (rc != XPCOM.NS_OK) error (rc);

	rc = webBrowser.SetContainerWindow (0);
	if (rc != XPCOM.NS_OK) error (rc);

	unhookDOMListeners ();

	long /*int*/[] result = new long /*int*/[1];
	rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIBaseWindow.class), result);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

	nsIBaseWindow baseWindow = new nsIBaseWindow (result[0]);
	rc = baseWindow.Destroy ();
	if (rc != XPCOM.NS_OK) error (rc);
	baseWindow.Release ();

	Release ();
	webBrowser.Release ();
	webBrowser = null;
	webBrowserObject = null;
	lastNavigateURL = null;
	htmlBytes = null;
	listener = null;

	if (tip != null && !tip.isDisposed ()) tip.dispose ();
	tip = null;
	location = size = null;

	for (LONG ptrObject : unhookedDOMWindows) {
		new nsISupports (ptrObject.value).Release ();
	}
	unhookedDOMWindows = null;

	Iterator<BrowserFunction> elements = functions.values().iterator ();
	while (elements.hasNext ()) {
		BrowserFunction function = elements.next ();
		AllFunctions.remove (new Integer (function.index));
		function.dispose (false);
	}
	functions = null;

	delegate.onDispose (embedHandle);
	delegate = null;

	embedHandle = 0;
	BrowserCount--;
}

void Activate () {
	isActive = true;
	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIWebBrowserFocus.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

	nsIWebBrowserFocus webBrowserFocus = new nsIWebBrowserFocus (result[0]);
	rc = webBrowserFocus.Activate ();
	if (rc != XPCOM.NS_OK) error (rc);
	webBrowserFocus.Release ();
}

void Deactivate () {
	isActive = false;
	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIWebBrowserFocus.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

	nsIWebBrowserFocus webBrowserFocus = new nsIWebBrowserFocus (result[0]);
	rc = webBrowserFocus.Deactivate ();
	if (rc != XPCOM.NS_OK) error (rc);
	webBrowserFocus.Release ();
}

void navigate (long /*int*/ requestHandle) {
	nsIRequest request = new nsIRequest (requestHandle);

	/* get the request post data, if any */
	long /*int*/[] result = new long /*int*/[1];
	byte[] postData = null;
	final List<String> headers = new ArrayList<> ();
	int rc = request.QueryInterface (IIDStore.GetIID (nsIUploadChannel.class), result);
	if (rc == XPCOM.NS_OK && result[0] != 0) {
		nsIUploadChannel uploadChannel = new nsIUploadChannel (result[0]);
		result[0] = 0;
		rc = uploadChannel.GetUploadStream (result);
		if (rc == XPCOM.NS_OK && result[0] != 0) {
			nsIInputStream inputStream = new nsIInputStream (result[0]);
			result[0] = 0;
			rc = inputStream.QueryInterface (IIDStore.GetIID (nsISeekableStream.class), result);
			if (rc == XPCOM.NS_OK && result[0] != 0) {
				nsISeekableStream seekableStream = new nsISeekableStream (result[0]);
				result[0] = 0;
				long[] initialOffset = new long[1];
				rc = seekableStream.Tell (initialOffset);
				if (rc == XPCOM.NS_OK) {
					rc = seekableStream.Seek (nsISeekableStream.NS_SEEK_SET, 0);
					if (rc == XPCOM.NS_OK) {
						long available;
						if (MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR24)) {
							long[] _retval = new long[1];
							rc = inputStream.Available (_retval);
							available = _retval[0];
						} else {
							int[] _retval = new int[1];
							rc = inputStream.Available (_retval);
							available = _retval[0];
						}
						if (rc == XPCOM.NS_OK) {
							int length = (int)available;
							byte[] bytes = new byte[length];
							int[] retVal = new int[1];
							rc = inputStream.Read (bytes, length, retVal);
							if (rc == XPCOM.NS_OK) {
								int start = 0;
								for (int i = 0; i < length; i++) {
									if (bytes[i] == 13) {
										byte[] current = new byte[i - start];
										System.arraycopy (bytes, start, current, 0, i - start);
										String string = new String (current).trim ();
										if (string.length () != 0) {
											headers.add (string);
										} else {
											start = i + 2; /* skip \r\n */
											postData = new byte[length - start];
											System.arraycopy (bytes, start, postData, 0, length - start);
											break;
										}
										start = i;
									}
								}
							}
						}
					}
					seekableStream.Seek (nsISeekableStream.NS_SEEK_SET, initialOffset[0]);
				}
				seekableStream.Release ();
			}
			inputStream.Release ();
		}
		uploadChannel.Release ();
	}

	/* get the request headers */
	XPCOMObject visitor = new XPCOMObject (new int[] {2, 0, 0, 2}) {
		int refCount = 0;
		@Override
		public long /*int*/ method0 (long /*int*/[] args) {
			/* QueryInterface */
			long /*int*/ riid = args[0];
			long /*int*/ ppvObject = args[1];
			if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
			nsID guid = new nsID ();
			XPCOM.memmove (guid, riid, nsID.sizeof);
			if (guid.Equals (XPCOM.NS_ISUPPORTS_IID) || guid.Equals (XPCOM.NS_IHTTPHEADERVISITOR_IID) || guid.Equals (XPCOM.NS_IHTTPHEADERVISITOR_10_IID)) {
				XPCOM.memmove (ppvObject, new long /*int*/[] {getAddress ()}, C.PTR_SIZEOF);
				refCount++;
				return XPCOM.NS_OK;
			}
			XPCOM.memmove (ppvObject, new long /*int*/[] {0}, C.PTR_SIZEOF);
			return XPCOM.NS_ERROR_NO_INTERFACE;
		}
		@Override
		public long /*int*/ method1 (long /*int*/[] args) {
			/* AddRef */
			return ++refCount;
		}
		@Override
		public long /*int*/ method2 (long /*int*/[] args) {
			/* Release */
			if (--refCount == 0) dispose ();
			return refCount;
		}
		@Override
		public long /*int*/ method3 (long /*int*/[] args) {
			/* VisitHeader */
			long /*int*/ aHeader = args[0];
			long /*int*/ aValue = args[1];

			int length = XPCOM.nsEmbedCString_Length (aHeader);
			long /*int*/ buffer = XPCOM.nsEmbedCString_get (aHeader);
			byte[] dest = new byte[length];
			XPCOM.memmove (dest, buffer, length);
			String header = new String (dest);

			length = XPCOM.nsEmbedCString_Length (aValue);
			buffer = XPCOM.nsEmbedCString_get (aValue);
			dest = new byte[length];
			XPCOM.memmove (dest, buffer, length);
			String value = new String (dest);

			headers.add(header + ':' + value);
			return XPCOM.NS_OK;
		}
	};

	new nsISupports (visitor.getAddress ()).AddRef ();
	rc = request.QueryInterface (IIDStore.GetIID (nsIHttpChannel.class), result);
	if (rc == XPCOM.NS_OK && result[0] != 0) {
		nsIHttpChannel httpChannel = new nsIHttpChannel (result[0]);
		httpChannel.VisitRequestHeaders (visitor.getAddress ());
		httpChannel.Release ();
	}
	result[0] = 0;
	new nsISupports (visitor.getAddress ()).Release ();

	String[] headersArray = null;
	int size = headers.size ();
	if (size > 0) {
		headersArray = new String[size];
		headers.toArray(headersArray);
	}

	/* a request's name often (but not always) is its url */
	String url = lastNavigateURL;
	long /*int*/ name = XPCOM.nsEmbedCString_new ();
	rc = request.GetName (name);
	if (rc == XPCOM.NS_OK) {
		int length = XPCOM.nsEmbedCString_Length (name);
		long /*int*/ buffer = XPCOM.nsEmbedCString_get (name);
		byte[] bytes = new byte[length];
		XPCOM.memmove (bytes, buffer, length);
		String value = new String (bytes);
		if (value.indexOf (":/") != -1) url = value;	//$NON-NLS-1$
	}
	XPCOM.nsEmbedCString_delete (name);

	setUrl (url, postData, headersArray);
}

void onResize () {
	Rectangle rect = DPIUtil.autoScaleUp (browser.getClientArea ()); // To Pixels
	int width = Math.max (1, rect.width);
	int height = Math.max (1, rect.height);

	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIBaseWindow.class), result);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

	Point size = delegate.getNativeSize (width, height);
	delegate.setSize (embedHandle, size.x, size.y);
	nsIBaseWindow baseWindow = new nsIBaseWindow (result[0]);
	rc = baseWindow.SetPositionAndSize (0, 0, size.x, size.y, 1);
	if (rc != XPCOM.NS_OK) error (rc);
	baseWindow.Release ();
}

@Override
public void refresh () {
	htmlBytes = null;

	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIWebNavigation.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

	nsIWebNavigation webNavigation = new nsIWebNavigation (result[0]);
	rc = webNavigation.Reload (nsIWebNavigation.LOAD_FLAGS_NONE);
	webNavigation.Release ();

	/*
	* The following error conditions do not indicate unrecoverable problems:
	*
	* - NS_ERROR_INVALID_POINTER: happens when Reload is called immediately
	* after calling LoadURI.
	* - NS_ERROR_FILE_NOT_FOUND: happens when attempting to reload a file that
	* no longer exists.
	* - NS_BINDING_ABORTED: happens when the user aborts the load (eg.- chooses
	* to not resubmit a page with form data).
	*/
	switch (rc) {
		case XPCOM.NS_OK:
		case XPCOM.NS_ERROR_INVALID_POINTER:
		case XPCOM.NS_ERROR_FILE_NOT_FOUND:
		case XPCOM.NS_BINDING_ABORTED: {
			return;
		}
	}
	error (rc);
}

@Override
void registerFunction (BrowserFunction function) {
	super.registerFunction (function);
	AllFunctions.put (function.index, function);
}

boolean sendChangingEvent (String url) {
	isViewingErrorPage = url.indexOf ("netError.xhtml") != -1; //$NON-NLS-1$

	boolean doit = true;
	if (request == 0) {
		/*
		 * listeners should not be notified of internal transitions like "javascript:..."
		 * because this is an implementation side-effect, not a true navigate
		 */
		if (!url.startsWith (PREFIX_JAVASCRIPT)) {
			if (locationListeners.length > 0) {
				LocationEvent event = new LocationEvent (browser);
				event.display = browser.getDisplay();
				event.widget = browser;
				event.location = url;
				/*
				 * If the URI indicates that the page is being rendered from memory
				 * (via setText()) then set it to about:blank to be consistent with IE.
				 */
				if (event.location.equals (URI_FILEROOT)) {
					event.location = ABOUT_BLANK;
				} else {
					int length = URI_FILEROOT.length ();
					if (event.location.startsWith (URI_FILEROOT) && event.location.charAt (length) == '#') {
						event.location = ABOUT_BLANK + event.location.substring (length);
					}
				}
				event.doit = doit;
				for (int i = 0; i < locationListeners.length; i++) {
					locationListeners[i].changing (event);
				}
				doit = event.doit && !browser.isDisposed();
			}

			if (doit) {
				if (jsEnabled != jsEnabledOnNextPage) {
					jsEnabled = jsEnabledOnNextPage;
					long /*int*/[] result = new long /*int*/[1];
					int rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIWebBrowserSetup.class), result);
					if (rc != XPCOM.NS_OK) error (rc);
					if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

					nsIWebBrowserSetup setup = new nsIWebBrowserSetup (result[0]);
					result[0] = 0;
					rc = setup.SetProperty (nsIWebBrowserSetup.SETUP_ALLOW_JAVASCRIPT, jsEnabled ? 1 : 0);
					if (rc != XPCOM.NS_OK) error (rc);
					setup.Release ();
				}
				if (!isViewingErrorPage) lastNavigateURL = url;
			}
		}
	}
	return doit;
}

@Override
public boolean setText (String html, boolean trusted) {
	/*
	*  Feature in Mozilla.  The focus memory of Mozilla must be
	*  properly managed through the nsIWebBrowserFocus interface.
	*  In particular, nsIWebBrowserFocus.deactivate must be called
	*  when the focus moves from the browser (or one of its children
	*  managed by Mozilla to another widget.  We currently do not
	*  get notified when a widget takes focus away from the Browser.
	*  As a result, deactivate is not properly called. This causes
	*  Mozilla to retake focus the next time a document is loaded.
	*  This breaks the case where the HTML loaded in the Browser
	*  varies while the user enters characters in a text widget. The text
	*  widget loses focus every time new content is loaded.
	*  The current workaround is to call deactivate everytime if
	*  the browser currently does not have focus. A better workaround
	*  would be to have a way to call deactivate when the Browser
	*  or one of its children loses focus.
	*/
	if (browser != browser.getDisplay ().getFocusControl ()) Deactivate ();

	/* convert the String containing HTML to an array of bytes with UTF-8 data */
	byte[] data = html.getBytes (StandardCharsets.UTF_8); //$NON-NLS-1$

	/*
	 * This could be the first content that is set into the browser, so
	 * ensure that the custom subclass that works around Mozilla bug
	 * https://bugzilla.mozilla.org/show_bug.cgi?id=453523 is removed.
	 */
	delegate.removeWindowSubclass ();

	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIWebBrowserStream.class), result);
	if (rc == XPCOM.NS_OK && result[0] != 0) {
		/*
		* Setting mozilla's content through nsIWebBrowserStream does not cause a page
		* load to occur, so the events that usually accompany a page change are not
		* fired.  To make this behave as expected, navigate to about:blank first and
		* then set the html content once the page has loaded.
		*/
		new nsISupports (result[0]).Release ();
		result[0] = 0;

		/*
		* If htmlBytes is not null then the about:blank page is already being loaded,
		* so no Navigate is required.  Just set the html that is to be shown.
		*/
		boolean blankLoading = htmlBytes != null;
		htmlBytes = data;
		untrustedText = !trusted;
		if (blankLoading) return true;

		/* navigate to about:blank */
		rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIWebNavigation.class), result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);
		nsIWebNavigation webNavigation = new nsIWebNavigation (result[0]);
		result[0] = 0;
		char[] uri = new char[ABOUT_BLANK.length () + 1];
		ABOUT_BLANK.getChars (0, ABOUT_BLANK.length (), uri, 0);
		rc = webNavigation.LoadURI (uri, nsIWebNavigation.LOAD_FLAGS_NONE, 0, 0, 0);
		if (rc != XPCOM.NS_OK) error (rc);
		webNavigation.Release ();
	} else {
		byte[] contentCharsetBuffer = MozillaDelegate.wcsToMbcs (null, "UTF-8", false);	//$NON-NLS-1$
		long /*int*/ aContentCharset = XPCOM.nsEmbedCString_new (contentCharsetBuffer, contentCharsetBuffer.length);
		byte[] contentTypeBuffer = MozillaDelegate.wcsToMbcs (null, "text/html", false); // $NON-NLS-1$
		long /*int*/ aContentType = XPCOM.nsEmbedCString_new (contentTypeBuffer, contentTypeBuffer.length);

		rc = XPCOM.NS_GetServiceManager (result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

		nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
		result[0] = 0;
		rc = serviceManager.GetService (XPCOM.NS_IOSERVICE_CID, IIDStore.GetIID (nsIIOService.class), result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

		nsIIOService ioService = new nsIIOService (result[0]);
		result[0] = 0;
		byte[] aString;
		if (trusted) {
			aString = MozillaDelegate.wcsToMbcs (null, URI_FILEROOT, false);
		} else {
			aString = MozillaDelegate.wcsToMbcs (null, ABOUT_BLANK, false);
		}
		long /*int*/ aSpec = XPCOM.nsEmbedCString_new (aString, aString.length);
		rc = ioService.NewURI (aSpec, null, 0, result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
		XPCOM.nsEmbedCString_delete (aSpec);
		ioService.Release ();

		nsIURI uri = new nsIURI (result[0]);
		result[0] = 0;

		rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIInterfaceRequestor.class), result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);
		nsIInterfaceRequestor interfaceRequestor = new nsIInterfaceRequestor (result[0]);
		result[0] = 0;

		/*
		* Feature in Mozilla. LoadStream invokes the nsIInputStream argument
		* through a different thread.  The callback mechanism must attach
		* a non java thread to the JVM otherwise the nsIInputStream Read and
		* Close methods never get called.
		*/
		InputStream inputStream = new InputStream (data);
		inputStream.AddRef ();

		rc = interfaceRequestor.GetInterface (IIDStore.GetIID (nsIDocShell.class, MozillaVersion.VERSION_BASE), result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);
		nsIDocShell docShell = new nsIDocShell (result[0]);
		result[0] = 0;
		rc = docShell.LoadStream (inputStream.getAddress (), uri.getAddress (), aContentType,  aContentCharset, 0);
		docShell.Release ();

		inputStream.Release ();
		interfaceRequestor.Release ();
		uri.Release ();
		XPCOM.nsEmbedCString_delete (aContentType);
		XPCOM.nsEmbedCString_delete (aContentCharset);
	}
	return true;
}

@Override
public boolean setUrl (String url, String postData, String[] headers) {
	byte[] postDataBytes = null;
	if (postData != null) {
		postDataBytes = MozillaDelegate.wcsToMbcs (null, postData, false);
	}
	return setUrl (url, postDataBytes, headers);
}

boolean setUrl (String url, byte[] postData, String[] headers) {
	htmlBytes = null;

	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIWebNavigation.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

	/*
	 * This could be the first content that is set into the browser, so
	 * ensure that the custom subclass that works around Mozilla bug
	 * https://bugzilla.mozilla.org/show_bug.cgi?id=453523 is removed.
	 */
	delegate.removeWindowSubclass ();

	nsIWebNavigation webNavigation = new nsIWebNavigation (result[0]);
	result[0] = 0;
	char[] uri = new char[url.length () + 1];
	url.getChars (0, url.length (), uri, 0);

	nsIMIMEInputStream postDataStream = null;
	InputStream dataStream = null;
	if (postData != null) {
		rc = XPCOM.NS_GetComponentManager (result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
		nsIComponentManager componentManager = new nsIComponentManager (result[0]);
		result[0] = 0;
		byte[] contractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_MIMEINPUTSTREAM_CONTRACTID, true);
		rc = componentManager.CreateInstanceByContractID (contractID, 0, IIDStore.GetIID (nsIMIMEInputStream.class), result);
		componentManager.Release();

		if (rc == XPCOM.NS_OK && result[0] != 0) { /* nsIMIMEInputStream is not in mozilla 1.4 */
			dataStream = new InputStream (postData);
			dataStream.AddRef ();
			postDataStream = new nsIMIMEInputStream (result[0]);
			rc = postDataStream.SetData (dataStream.getAddress ());
			if (rc != XPCOM.NS_OK) error (rc);

			boolean foundLength = false;
			boolean foundType = false;
			if (headers != null) {
				for (int i = 0; i < headers.length; i++) {
					int index = headers[i].indexOf (':');
					if (index != -1) {
						String name = headers[i].substring (0, index).trim ().toLowerCase ();
						if (name.equals (HEADER_CONTENTLENGTH)) {
							foundLength = true;
						} else if (name.equals (HEADER_CONTENTTYPE)) {
							foundType = true;
						}
					}
				}
			}
			rc = postDataStream.SetAddContentLength (foundLength ? 0 : 1);
			if (rc != XPCOM.NS_OK) error (rc);
			if (!foundType) {
				byte[] name = MozillaDelegate.wcsToMbcs (null, HEADER_CONTENTTYPE, true);
				byte[] value = MozillaDelegate.wcsToMbcs (null, MIMETYPE_FORMURLENCODED, true);
				rc = postDataStream.AddHeader (name, value);
				if (rc != XPCOM.NS_OK) error (rc);
			}
		}
		result[0] = 0;
	}

	InputStream headersStream = null;
    if (headers != null) {
		StringBuffer buffer = new StringBuffer ();
		for (int i = 0; i < headers.length; i++) {
			String current = headers[i];
			if (current != null) {
				int sep = current.indexOf (':');
				if (sep != -1) {
					String key = current.substring (0, sep).trim ();
					String value = current.substring (sep + 1).trim ();
					if (key.length () > 0 && value.length () > 0) {
						buffer.append (key);
						buffer.append (':');
						buffer.append (value);
						buffer.append ("\r\n");
					}
				}
			}
		}
		byte[] bytes = MozillaDelegate.wcsToMbcs (null, buffer.toString (), true);
		headersStream = new InputStream (bytes);
		headersStream.AddRef ();
    }

	rc = webNavigation.LoadURI (
		uri,
		nsIWebNavigation.LOAD_FLAGS_NONE,
		0,
		postDataStream == null ? 0 : postDataStream.getAddress (),
		headersStream == null ? 0 : headersStream.getAddress ());
	if (dataStream != null) dataStream.Release ();
	if (headersStream != null) headersStream.Release ();
	webNavigation.Release ();
	return rc == XPCOM.NS_OK;
}

@Override
public void stop () {
	htmlBytes = null;

	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIWebNavigation.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

	nsIWebNavigation webNavigation = new nsIWebNavigation (result[0]);
	rc = webNavigation.Stop (nsIWebNavigation.STOP_ALL);
	if (rc != XPCOM.NS_OK) error (rc);
	webNavigation.Release ();
}

void hookDOMListeners (nsIDOMEventTarget target, boolean isTop) {
	nsEmbedString string = new nsEmbedString (XPCOM.DOMEVENT_FOCUS);
	target.AddEventListener (string.getAddress (), domEventListener.getAddress (), 0, 1, 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_UNLOAD);
	target.AddEventListener (string.getAddress (), domEventListener.getAddress (), 0, 1, 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_MOUSEDOWN);
	target.AddEventListener (string.getAddress (), domEventListener.getAddress (), 0, 1, 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_MOUSEUP);
	target.AddEventListener (string.getAddress (), domEventListener.getAddress (), 0, 1, 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_MOUSEMOVE);
	target.AddEventListener (string.getAddress (), domEventListener.getAddress (), 0, 1, 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_MOUSEWHEEL);
	target.AddEventListener (string.getAddress (), domEventListener.getAddress (), 0, 1, 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_MOUSEDRAG);
	target.AddEventListener (string.getAddress (), domEventListener.getAddress (), 0, 1, 0);
	string.dispose ();

	/*
	* Only hook mouseover and mouseout if the target is a top-level frame, so that mouse moves
	* between frames will not generate events.
	*/
	if (isTop && delegate.hookEnterExit ()) {
		string = new nsEmbedString (XPCOM.DOMEVENT_MOUSEOVER);
		target.AddEventListener (string.getAddress (), domEventListener.getAddress (), 0, 1, 0);
		string.dispose ();
		string = new nsEmbedString (XPCOM.DOMEVENT_MOUSEOUT);
		target.AddEventListener (string.getAddress (), domEventListener.getAddress (), 0, 1, 0);
		string.dispose ();
	}

	string = new nsEmbedString (XPCOM.DOMEVENT_KEYDOWN);
	target.AddEventListener (string.getAddress (), domEventListener.getAddress (), 0, 1, 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_KEYPRESS);
	target.AddEventListener (string.getAddress (), domEventListener.getAddress (), 0, 1, 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_KEYUP);
	target.AddEventListener (string.getAddress (), domEventListener.getAddress (), 0, 1, 0);
	string.dispose ();
}

void unhookDOMListeners () {
	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.GetContentDOMWindow (result);
	if (rc != XPCOM.NS_OK || result[0] == 0) return;

	nsIDOMWindow window = new nsIDOMWindow (result[0]);
	result[0] = 0;
	rc = window.QueryInterface (IIDStore.GetIID (nsIDOMEventTarget.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);
	nsIDOMEventTarget target = new nsIDOMEventTarget (result[0]);
	result[0] = 0;
	unhookDOMListeners (target);
	target.Release ();

	/* Listeners must be unhooked in pages contained in frames */
	rc = window.GetFrames (result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);
	nsIDOMWindowCollection frames = new nsIDOMWindowCollection (result[0]);
	result[0] = 0;
	int[] frameCount = new int[1];
	rc = frames.GetLength (frameCount); /* PRUint32 */
	if (rc != XPCOM.NS_OK) error (rc);
	int count = frameCount[0];

	if (count > 0) {
		for (int i = 0; i < count; i++) {
			rc = frames.Item (i, result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

			nsIDOMWindow frame = new nsIDOMWindow (result[0]);
			result[0] = 0;
			rc = frame.QueryInterface (IIDStore.GetIID (nsIDOMEventTarget.class), result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

			target = new nsIDOMEventTarget (result[0]);
			result[0] = 0;
			unhookDOMListeners (target);
			target.Release ();
			frame.Release ();
		}
	}
	frames.Release ();
	window.Release ();
}

void unhookDOMListeners (nsIDOMEventTarget target) {
	nsEmbedString string = new nsEmbedString (XPCOM.DOMEVENT_FOCUS);
	target.RemoveEventListener (string.getAddress (), domEventListener.getAddress (), 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_UNLOAD);
	target.RemoveEventListener (string.getAddress (), domEventListener.getAddress (), 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_MOUSEDOWN);
	target.RemoveEventListener (string.getAddress (), domEventListener.getAddress (), 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_MOUSEUP);
	target.RemoveEventListener (string.getAddress (), domEventListener.getAddress (), 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_MOUSEMOVE);
	target.RemoveEventListener (string.getAddress (), domEventListener.getAddress (), 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_MOUSEWHEEL);
	target.RemoveEventListener (string.getAddress (), domEventListener.getAddress (), 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_MOUSEDRAG);
	target.RemoveEventListener (string.getAddress (), domEventListener.getAddress (), 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_MOUSEOVER);
	target.RemoveEventListener (string.getAddress (), domEventListener.getAddress (), 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_MOUSEOUT);
	target.RemoveEventListener (string.getAddress (), domEventListener.getAddress (), 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_KEYDOWN);
	target.RemoveEventListener (string.getAddress (), domEventListener.getAddress (), 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_KEYPRESS);
	target.RemoveEventListener (string.getAddress (), domEventListener.getAddress (), 0);
	string.dispose ();
	string = new nsEmbedString (XPCOM.DOMEVENT_KEYUP);
	target.RemoveEventListener (string.getAddress (), domEventListener.getAddress (), 0);
	string.dispose ();
}

/* nsISupports */

int QueryInterface (long /*int*/ riid, long /*int*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;

	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);

	if (guid.Equals (XPCOM.NS_ISUPPORTS_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {supports.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (XPCOM.NS_IWEAKREFERENCE_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {weakReference.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (IIDStore.GetIID (nsIWebProgressListener.class, MozillaVersion.VERSION_XR24, true))) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {webProgressListener_24.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (IIDStore.GetIID (nsIWebProgressListener.class, MozillaVersion.VERSION_XR10))) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {webProgressListener.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (IIDStore.GetIID (nsIWebBrowserChrome.class))) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {webBrowserChrome.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (XPCOM.NS_IWEBBROWSERCHROMEFOCUS_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {webBrowserChromeFocus.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (IIDStore.GetIID (nsIEmbeddingSiteWindow.class, MozillaVersion.VERSION_XR24, true))) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {embeddingSiteWindow_24.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (IIDStore.GetIID (nsIEmbeddingSiteWindow.class, MozillaVersion.VERSION_XR10))) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {embeddingSiteWindow.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (IIDStore.GetIID (nsIInterfaceRequestor.class))) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {interfaceRequestor.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (XPCOM.NS_ISUPPORTSWEAKREFERENCE_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {supportsWeakReference.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (XPCOM.NS_ICONTEXTMENULISTENER_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {contextMenuListener.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (XPCOM.NS_IURICONTENTLISTENER_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {uriContentListener.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (XPCOM.NS_ITOOLTIPLISTENER_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {tooltipListener.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (XPCOM.NS_IBADCERTLISTENER2_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {badCertListener.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	XPCOM.memmove (ppvObject, new long /*int*/[] {0}, C.PTR_SIZEOF);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}

int AddRef () {
	refCount++;
	return refCount;
}

int Release () {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces ();
	return refCount;
}

/* nsIWeakReference */

int QueryReferent (long /*int*/ riid, long /*int*/ ppvObject) {
	return QueryInterface (riid, ppvObject);
}

/* nsIInterfaceRequestor */

int GetInterface (long /*int*/ riid, long /*int*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);
	if (guid.Equals (IIDStore.GetIID (nsIDOMWindow.class))) {
		long /*int*/[] aContentDOMWindow = new long /*int*/[1];
		int rc = webBrowser.GetContentDOMWindow (aContentDOMWindow);
		if (rc != XPCOM.NS_OK) error (rc);
		if (aContentDOMWindow[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);
		XPCOM.memmove (ppvObject, aContentDOMWindow, C.PTR_SIZEOF);
		return rc;
	}
	return QueryInterface (riid, ppvObject);
}

int GetWeakReference (long /*int*/ ppvObject) {
	XPCOM.memmove (ppvObject, new long /*int*/[] {weakReference.getAddress ()}, C.PTR_SIZEOF);
	AddRef ();
	return XPCOM.NS_OK;
}

/* nsIWebProgressListener */

int OnStateChange (long /*int*/ aWebProgress, long /*int*/ aRequest, int aStateFlags, int aStatus) {
	if (registerFunctionsOnState != 0 && ((aStateFlags & registerFunctionsOnState) == registerFunctionsOnState)) {
		registerFunctionsOnState = 0;
		Iterator<BrowserFunction> elements = functions.values().iterator ();
		while (elements.hasNext ()) {
			BrowserFunction function = elements.next ();
			if (!function.isEvaluate) {
				execute (function.functionString);
			}
		}
	}

	/*
	* Feature of Mozilla.  When a redirect occurs, no STATE_IS_DOCUMENT state
	* transitions or OnStartURIOpen() callbacks are received for the new
	* location.  As a result, lastNavigateURL is left with the value of the old
	* url (that was redirected from), and the client does not get an opportunity
	* to veto the redirect.  The only opportunity to work around these omissions
	* is in the subsequent STATE_START | STATE_IS_REQUEST state transition.
	* When this comes, if the request's name appears to be a url then take it to
	* be the new site (update lastNavigateURL), and send a "changing" LocationEvent
	* to allow the redirect to be vetoed.
	*
	* Note that updateLastNavigateUrl is not reset to false here so that in
	* typical contexts where a redirect occurs without an accompanying invalid
	* certificate, the updated site will be retrieved from the channel (this
	* is more proper) on the next STATE_TRANSFERRING | STATE_IS_DOCUMENT transition.
	*/
	if (updateLastNavigateUrl && aStateFlags == (nsIWebProgressListener.STATE_IS_REQUEST | nsIWebProgressListener.STATE_START)) {
		nsIRequest request = new nsIRequest (aRequest);
		long /*int*/ name = XPCOM.nsEmbedCString_new ();
		int rc = request.GetName (name);
		if (rc == XPCOM.NS_OK) {
			int length = XPCOM.nsEmbedCString_Length (name);
			long /*int*/ buffer = XPCOM.nsEmbedCString_get (name);
			byte[] bytes = new byte[length];
			XPCOM.memmove (bytes, buffer, length);
			String value = new String (bytes);
			if (value.indexOf (":/") != -1) { //$NON-NLS-1$
				boolean doit = sendChangingEvent (value);
				if (doit) {
					lastNavigateURL = value;
				} else {
					stop ();
				}
			}
		}
		XPCOM.nsEmbedCString_delete (name);
	}

	if ((aStateFlags & nsIWebProgressListener.STATE_IS_DOCUMENT) == 0) return XPCOM.NS_OK;
	if ((aStateFlags & nsIWebProgressListener.STATE_START) != 0) {
		long /*int*/[] result = new long /*int*/[1];

		/*
		* When navigating to a site that is known to have a bad certificate, request notification
		* callbacks on the channel so that our nsIBadCertListener2 will be invoked.
		*/
		if (isRetrievingBadCert) {
			nsIRequest request = new nsIRequest (aRequest);
			int rc = request.QueryInterface (IIDStore.GetIID (nsIChannel.class), result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

			nsIChannel channel = new nsIChannel (result[0]);
			result[0] = 0;
			rc = channel.SetNotificationCallbacks (interfaceRequestor.getAddress ());
			if (rc != XPCOM.NS_OK) error (rc);
			channel.Release ();
			return XPCOM.NS_OK;
		}

		if (request == 0) request = aRequest;
		registerFunctionsOnState = nsIWebProgressListener.STATE_IS_REQUEST | nsIWebProgressListener.STATE_START;
		/*
		 * Add the page's nsIDOMWindow to the collection of windows that will
		 * have DOM listeners added to them later on in the page loading
		 * process.  These listeners cannot be added yet because the
		 * nsIDOMWindow is not ready to take them at this stage.
		 */
		nsIWebProgress progress = new nsIWebProgress (aWebProgress);
		int rc = progress.GetDOMWindow (result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
		unhookedDOMWindows.add (new LONG (result[0]));
	} else if ((aStateFlags & nsIWebProgressListener.STATE_REDIRECTING) != 0) {
		if (request == aRequest) request = 0;
		registerFunctionsOnState = nsIWebProgressListener.STATE_TRANSFERRING;
		updateLastNavigateUrl = true;
	} else if ((aStateFlags & nsIWebProgressListener.STATE_STOP) != 0) {
		if (isRetrievingBadCert) {
			return XPCOM.NS_OK;
		}

		/*
		* If a site with a bad certificate is being encountered for the first time
		* then store the request for future reference, set the isRetrievingBadCert
		* flag and re-navigate to the site so that notification callbacks can be
		* hooked on it to get its certificate info.
		*/
		switch (aStatus) {
			case XPCOM.SSL_ERROR_BAD_CERT_DOMAIN:
			case XPCOM.SEC_ERROR_CA_CERT_INVALID:
			case XPCOM.SEC_ERROR_EXPIRED_CERTIFICATE:
			case XPCOM.SEC_ERROR_EXPIRED_ISSUER_CERTIFICATE:
			case XPCOM.SEC_ERROR_INADEQUATE_KEY_USAGE:
			case XPCOM.SEC_ERROR_UNKNOWN_ISSUER:
			case XPCOM.SEC_ERROR_UNTRUSTED_CERT:
			case XPCOM.SEC_ERROR_UNTRUSTED_ISSUER: {
				new nsISupports (aRequest).AddRef ();
				if (badCertRequest != 0) {
					new nsISupports (badCertRequest).Release ();
				}
				badCertRequest = aRequest;
				isRetrievingBadCert = true;
				navigate (aRequest);
				return XPCOM.NS_OK;
			}
		}

		/*
		* If this page's nsIDOMWindow handle is still in unhookedDOMWindows then
		* add its DOM listeners now.  It's possible for this to happen since
		* there is no guarantee that a STATE_TRANSFERRING state change will be
		* received for every window in a page, which is when these listeners
		* are typically added.
		*/

		long /*int*/[] result = new long /*int*/[1];
		int rc;
		LONG ptrObject = new LONG (result[0]);
		result[0] = 0;
		int index = unhookedDOMWindows.indexOf (ptrObject);
		if (index != -1) {
			long /*int*/[] window = new long /*int*/[1];
			nsIWebProgress progress = new nsIWebProgress (aWebProgress);
			rc = progress.GetDOMWindow (window);
			if (rc != XPCOM.NS_OK) error (rc);
			if (window[0] == 0) error (XPCOM.NS_NOINTERFACE);

			rc = webBrowser.GetContentDOMWindow (result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);
			boolean isTop = result[0] == window[0];
			new nsISupports (result[0]).Release ();
			result[0] = 0;

			nsIDOMWindow domWindow = new nsIDOMWindow (window[0]);
			rc = domWindow.QueryInterface (IIDStore.GetIID (nsIDOMEventTarget.class), result);
			domWindow.Release();
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

			nsIDOMEventTarget target = new nsIDOMEventTarget (result[0]);
			hookDOMListeners (target, isTop);
			target.Release ();
			result[0] = 0;

			/*
			* Remove and unreference the nsIDOMWindow from the collection of windows
			* that are waiting to have DOM listeners hooked on them.
			*/
			unhookedDOMWindows.remove (ptrObject);
			new nsISupports (ptrObject.value).Release ();
		}

		boolean deferCompleted = false;
		/*
		 * If htmlBytes is not null then there is html from a previous setText() call
		 * waiting to be set into the about:blank page once it has completed loading.
		 */
		if (htmlBytes != null) {
			nsIRequest req = new nsIRequest (aRequest);
			long /*int*/ name = XPCOM.nsEmbedCString_new ();
			rc = req.GetName (name);
			if (rc != XPCOM.NS_OK) error (rc);
			int length = XPCOM.nsEmbedCString_Length (name);
			long /*int*/ buffer = XPCOM.nsEmbedCString_get (name);
			byte[] dest = new byte[length];
			XPCOM.memmove (dest, buffer, length);
			String url = new String (dest);
			XPCOM.nsEmbedCString_delete (name);

			if (url.startsWith (ABOUT_BLANK)) {
				/*
				 * Setting mozilla's content with nsIWebBrowserStream invalidates the
				 * DOM listeners that were hooked on it (about:blank), so remove them and
				 * add new ones after the content has been set.
				 */
				unhookDOMListeners ();

				rc = XPCOM.NS_GetServiceManager (result);
				if (rc != XPCOM.NS_OK) error (rc);
				if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

				nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
				result[0] = 0;
				rc = serviceManager.GetService (XPCOM.NS_IOSERVICE_CID, IIDStore.GetIID (nsIIOService.class), result);
				if (rc != XPCOM.NS_OK) error (rc);
				if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
				serviceManager.Release ();

				nsIIOService ioService = new nsIIOService (result[0]);
				result[0] = 0;
				byte[] aString;
				if (untrustedText) {
					aString = MozillaDelegate.wcsToMbcs (null, ABOUT_BLANK, false);
				} else {
					aString = MozillaDelegate.wcsToMbcs (null, URI_FILEROOT, false);
				}
				long /*int*/ aSpec = XPCOM.nsEmbedCString_new (aString, aString.length);
				rc = ioService.NewURI (aSpec, null, 0, result);
				if (rc != XPCOM.NS_OK) error (rc);
				if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
				XPCOM.nsEmbedCString_delete (aSpec);
				ioService.Release ();

				nsIURI uri = new nsIURI (result[0]);
				result[0] = 0;

				rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIWebBrowserStream.class), result);
				if (rc != XPCOM.NS_OK) error (rc);
				if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

				nsIWebBrowserStream stream = new nsIWebBrowserStream (result[0]);
				result[0] = 0;

				byte[] contentTypeBuffer = MozillaDelegate.wcsToMbcs (null, "text/html", false); // $NON-NLS-1$
				long /*int*/ aContentType = XPCOM.nsEmbedCString_new (contentTypeBuffer, contentTypeBuffer.length);

				rc = stream.OpenStream (uri.getAddress (), aContentType);
				if (rc != XPCOM.NS_OK) error (rc);

				/*
				* For Mozilla < 1.9.2, when content is being set via nsIWebBrowserStream, this
				* is the only place where registered functions can be re-installed such that
				* they will be invokable at load time by JS contained in the text.
				*/
				Iterator<BrowserFunction> elements = functions.values().iterator ();
				while (elements.hasNext ()) {
					BrowserFunction function = elements.next ();
					if (!function.isEvaluate) {
						execute (function.functionString);
					}
				}
				/*
				* For Mozilla >= 1.9.2, when content is being set via nsIWebBrowserStream,
				* registered functions must be re-installed in the subsequent Start Request
				* in order to be invokable at load time by JS contained in the text.
				*/
				registerFunctionsOnState = nsIWebProgressListener.STATE_IS_REQUEST | nsIWebProgressListener.STATE_START;

				long /*int*/ ptr = C.malloc (htmlBytes.length);
				XPCOM.memmove (ptr, htmlBytes, htmlBytes.length);
				int pageSize = 8192;
				int pageCount = htmlBytes.length / pageSize + 1;
				long /*int*/ current = ptr;
				for (int i = 0; i < pageCount; i++) {
					length = i == pageCount - 1 ? htmlBytes.length % pageSize : pageSize;
					if (length > 0) {
						rc = stream.AppendToStream (current, length);
						if (rc != XPCOM.NS_OK) error (rc);
					}
					current += pageSize;
				}
				rc = stream.CloseStream ();
				if (rc != XPCOM.NS_OK) error (rc);
				C.free (ptr);
				XPCOM.nsEmbedCString_delete (aContentType);
				stream.Release ();
				uri.Release ();
				htmlBytes = null;
				/*
				* Browser content that is set via nsIWebBrowserStream is not parsed immediately.
				* Since clients depend on the Completed event to know when the browser's content
				* is available, delay the sending of this event so that the stream content will
				* be parsed first.
				*/
				deferCompleted = true;

				long /*int*/[] window = new long /*int*/[1];
				nsIWebProgress progress = new nsIWebProgress (aWebProgress);
				rc = progress.GetDOMWindow (window);
				if (rc != XPCOM.NS_OK) error (rc);
				if (window[0] == 0) error (XPCOM.NS_NOINTERFACE);

				rc = webBrowser.GetContentDOMWindow (result);
				if (rc != XPCOM.NS_OK) error (rc);
				if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);
				boolean isTop = result[0] == window[0];
				new nsISupports (result[0]).Release ();
				result[0] = 0;

				nsIDOMWindow domWindow = new nsIDOMWindow (window[0]);
				rc = domWindow.QueryInterface (IIDStore.GetIID (nsIDOMEventTarget.class), result);
				domWindow.Release();
				if (rc != XPCOM.NS_OK) error (rc);
				if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

				nsIDOMEventTarget target = new nsIDOMEventTarget (result[0]);
				hookDOMListeners (target, isTop);
				target.Release ();
				result[0] = 0;
			} else {
				registerFunctionsOnState = 0;
			}
		}

		/*
		* Feature in Mozilla.  When a request is redirected (STATE_REDIRECTING),
		* it never reaches the state STATE_STOP and it is replaced with a new request.
		* The new request is received when it is in the state STATE_STOP.
		* To handle this case,  the variable request is set to 0 when the corresponding
		* request is redirected. The following request received with the state STATE_STOP
		* - the new request resulting from the redirection - is used to send
		* the ProgressListener.completed event.
		*/
		if (request == aRequest || request == 0) {
			request = 0;
			StatusTextEvent event = new StatusTextEvent (browser);
			event.display = browser.getDisplay ();
			event.widget = browser;
			event.text = ""; //$NON-NLS-1$
			for (int i = 0; i < statusTextListeners.length; i++) {
				statusTextListeners[i].changed (event);
			}

			final Display display = browser.getDisplay ();
			final ProgressEvent event2 = new ProgressEvent (browser);
			event2.display = display;
			event2.widget = browser;
			Runnable runnable = () -> {
				if (browser.isDisposed ()) return;
				for (int i = 0; i < progressListeners.length; i++) {
					progressListeners[i].completed (event2);
				}
			};
			if (deferCompleted) {
				display.asyncExec (runnable);
			} else {
				display.syncExec (runnable);
			}
		}
	} else if ((aStateFlags & nsIWebProgressListener.STATE_TRANSFERRING) != 0) {
		if (MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR10)) {
			registerFunctionsOnState = nsIWebProgressListener.STATE_IS_REQUEST | nsIWebProgressListener.STATE_STOP;
		}

		if (updateLastNavigateUrl) {
			updateLastNavigateUrl = false;
			nsIRequest request = new nsIRequest (aRequest);

			long /*int*/[] result = new long /*int*/[1];
			int rc = request.QueryInterface (IIDStore.GetIID (nsIChannel.class), result);
			if (rc == XPCOM.NS_OK && result[0] != 0) {
				nsIChannel channel = new nsIChannel (result[0]);
				result[0] = 0;
				rc = channel.GetURI (result);
				if (rc != XPCOM.NS_OK) error (rc);
				if (result[0] == 0) error (XPCOM.NS_ERROR_NULL_POINTER);
				channel.Release ();

				nsIURI uri = new nsIURI (result[0]);
				result[0] = 0;
				long /*int*/ aSpec = XPCOM.nsEmbedCString_new ();
				rc = uri.GetSpec (aSpec);
				if (rc != XPCOM.NS_OK) error (rc);
				int length = XPCOM.nsEmbedCString_Length (aSpec);
				long /*int*/ buffer = XPCOM.nsEmbedCString_get (aSpec);
				byte[] bytes = new byte[length];
				XPCOM.memmove (bytes, buffer, length);
				lastNavigateURL = new String (bytes);
				XPCOM.nsEmbedCString_delete (aSpec);
				uri.Release ();
			}
		}

		/*
		* Hook DOM listeners to the page's nsIDOMWindow here because this is
		* the earliest opportunity to do so.
		*/
		long /*int*/[] result = new long /*int*/[1];
		nsIWebProgress progress = new nsIWebProgress (aWebProgress);
		int rc = progress.GetDOMWindow (result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
		nsIDOMWindow domWindow = new nsIDOMWindow (result[0]);

		LONG ptrObject = new LONG (result[0]);
		result[0] = 0;
		int index = unhookedDOMWindows.indexOf (ptrObject);
		if (index != -1) {
			rc = webBrowser.GetContentDOMWindow (result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);
			boolean isTop = result[0] == domWindow.getAddress ();
			new nsISupports (result[0]).Release ();
			result[0] = 0;

			rc = domWindow.QueryInterface (IIDStore.GetIID (nsIDOMEventTarget.class), result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

			nsIDOMEventTarget target = new nsIDOMEventTarget (result[0]);
			hookDOMListeners (target, isTop);
			target.Release ();
			result[0] = 0;

			/*
			* Remove and unreference the nsIDOMWindow from the collection of windows
			* that are waiting to have DOM listeners hooked on them.
			*/
			unhookedDOMWindows.remove (ptrObject);
			new nsISupports (ptrObject.value).Release ();
		}
		domWindow.Release ();
	}
	return XPCOM.NS_OK;
}

int OnProgressChange (long /*int*/ aWebProgress, long /*int*/ aRequest, int aCurSelfProgress, int aMaxSelfProgress, int aCurTotalProgress, int aMaxTotalProgress) {
	if (progressListeners.length == 0) return XPCOM.NS_OK;
	ProgressEvent event = new ProgressEvent (browser);
	event.display = browser.getDisplay ();
	event.widget = browser;
	event.current = aCurTotalProgress;
	event.total = aMaxTotalProgress;
	for (int i = 0; i < progressListeners.length; i++) {
		progressListeners[i].changed (event);
	}
	return XPCOM.NS_OK;
}

int OnLocationChange (long /*int*/ aWebProgress, long /*int*/ aRequest, long /*int*/ aLocation) {
	return OnLocationChange(aWebProgress, aRequest, aLocation, 0);
}

int OnLocationChange (long /*int*/ aWebProgress, long /*int*/ aRequest, long /*int*/ aLocation, int aFlags) {
	/*
	* Feature in Mozilla.  When a page is loaded via setText before a previous
	* setText page load has completed, the expected OnStateChange STATE_STOP for the
	* original setText never arrives because it gets replaced by the OnStateChange
	* STATE_STOP for the new request.  This results in the request field never being
	* cleared because the original request's OnStateChange STATE_STOP is still expected
	* (but never arrives).  To handle this case, the request field is updated to the new
	* overriding request since its OnStateChange STATE_STOP will be received next.
	*/
	if (request != 0 && request != aRequest) request = aRequest;

	if (locationListeners.length == 0) return XPCOM.NS_OK;

	nsIWebProgress webProgress = new nsIWebProgress (aWebProgress);
	long /*int*/[] aDOMWindow = new long /*int*/[1];
	int rc = webProgress.GetDOMWindow (aDOMWindow);
	if (rc != XPCOM.NS_OK) error (rc);
	if (aDOMWindow[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

	long /*int*/[] aTop = new long /*int*/[1];
	nsIDOMWindow domWindow = new nsIDOMWindow (aDOMWindow[0]);
	rc = domWindow.GetTop (aTop);
	domWindow.Release ();
	if (rc != XPCOM.NS_OK) error (rc);
	if (aTop[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);
	nsIDOMWindow topWindow = new nsIDOMWindow (aTop[0]);
	topWindow.Release ();

	nsIURI location = new nsIURI (aLocation);
	long /*int*/ aSpec = XPCOM.nsEmbedCString_new ();
	location.GetSpec (aSpec);
	int length = XPCOM.nsEmbedCString_Length (aSpec);
	long /*int*/ buffer = XPCOM.nsEmbedCString_get (aSpec);
	byte[] dest = new byte[length];
	XPCOM.memmove (dest, buffer, length);
	XPCOM.nsEmbedCString_delete (aSpec);
	String url = new String (dest);

	/*
	 * As of Mozilla 1.8, the first time that a page is displayed, regardless of
	 * whether it's via Browser.setURL() or Browser.setText(), the GRE navigates
	 * to about:blank and fires the corresponding navigation events.  Do not send
	 * this event on to the user since it is not expected.
	 */
	if (MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR1_8) && aRequest == 0 && url.startsWith (ABOUT_BLANK)) return XPCOM.NS_OK;

	LocationEvent event = new LocationEvent (browser);
	event.display = browser.getDisplay ();
	event.widget = browser;
	event.location = url;
	/*
	 * If the URI indicates that the page is being rendered from memory
	 * (via setText()) then set it to about:blank to be consistent with IE.
	 */
	if (event.location.equals (URI_FILEROOT)) {
		event.location = ABOUT_BLANK;
	} else {
		length = URI_FILEROOT.length ();
		if (event.location.startsWith (URI_FILEROOT) && event.location.charAt (length) == '#') {
			event.location = ABOUT_BLANK + event.location.substring (length);
		}
	}
	event.top = aTop[0] == aDOMWindow[0];
	for (int i = 0; i < locationListeners.length; i++) {
		locationListeners[i].changed (event);
	}
	return XPCOM.NS_OK;
}

int OnStatusChange (long /*int*/ aWebProgress, long /*int*/ aRequest, int aStatus, long /*int*/ aMessage) {
	if (statusTextListeners.length == 0) return XPCOM.NS_OK;
	StatusTextEvent event = new StatusTextEvent (browser);
	event.display = browser.getDisplay ();
	event.widget = browser;
	int length = XPCOM.strlen_PRUnichar (aMessage);
	char[] dest = new char[length];
	XPCOM.memmove (dest, aMessage, length * 2);
	event.text = new String (dest);
	for (int i = 0; i < statusTextListeners.length; i++) {
		statusTextListeners[i].changed (event);
	}
	return XPCOM.NS_OK;
}

int OnSecurityChange (long /*int*/ aWebProgress, long /*int*/ aRequest, int state) {
	return XPCOM.NS_OK;
}

/* nsIWebBrowserChrome */

int SetStatus (int statusType, long /*int*/ status) {
	if (statusTextListeners.length == 0) return XPCOM.NS_OK;
	StatusTextEvent event = new StatusTextEvent (browser);
	event.display = browser.getDisplay ();
	event.widget = browser;
	int length = XPCOM.strlen_PRUnichar (status);
	char[] dest = new char[length];
	XPCOM.memmove (dest, status, length * 2);
	String string = new String (dest);
	event.text = string;
	for (int i = 0; i < statusTextListeners.length; i++) {
		statusTextListeners[i].changed (event);
	}
	return XPCOM.NS_OK;
}

int GetWebBrowser (long /*int*/ aWebBrowser) {
	long /*int*/[] ret = new long /*int*/[1];
	if (webBrowser != null) {
		webBrowser.AddRef ();
		ret[0] = webBrowser.getAddress ();
	}
	XPCOM.memmove (aWebBrowser, ret, C.PTR_SIZEOF);
	return XPCOM.NS_OK;
}

int SetWebBrowser (long /*int*/ aWebBrowser) {
	if (webBrowser != null) webBrowser.Release ();
	webBrowser = aWebBrowser != 0 ? new nsIWebBrowser (aWebBrowser) : null;
	return XPCOM.NS_OK;
}

int GetChromeFlags (long /*int*/ aChromeFlags) {
	int[] ret = new int[1];
	ret[0] = chromeFlags;
	XPCOM.memmove (aChromeFlags, ret, 4); /* PRUint32 */
	return XPCOM.NS_OK;
}

int SetChromeFlags (int aChromeFlags) {
	chromeFlags = aChromeFlags;
	return XPCOM.NS_OK;
}

int DestroyBrowserWindow () {
	WindowEvent newEvent = new WindowEvent (browser);
	newEvent.display = browser.getDisplay ();
	newEvent.widget = browser;
	for (int i = 0; i < closeWindowListeners.length; i++) {
		closeWindowListeners[i].close (newEvent);
	}
	/*
	* Note on Mozilla.  The DestroyBrowserWindow notification cannot be cancelled.
	* The browser widget cannot be used after this notification has been received.
	* The application is advised to close the window hosting the browser widget.
	* The browser widget must be disposed in all cases.
	*/
	browser.dispose ();
	return XPCOM.NS_OK;
}

int SizeBrowserTo (int aCX, int aCY) {
	size = new Point (aCX, aCY);
	boolean isChrome = (chromeFlags & nsIWebBrowserChrome.CHROME_OPENAS_CHROME) != 0;
	if (isChrome) {
		Shell shell = browser.getShell ();
		shell.setSize (shell.computeSize (size.x, size.y));
	}
	return XPCOM.NS_OK;
}

int ShowAsModal () {
	long /*int*/[] result = new long /*int*/[1];
	int rc = XPCOM.NS_GetServiceManager (result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

	nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
	result[0] = 0;
	byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_CONTEXTSTACK_CONTRACTID, true);
	rc = serviceManager.GetServiceByContractID (aContractID, IIDStore.GetIID (nsIJSContextStack.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
	serviceManager.Release ();

	nsIJSContextStack stack = new nsIJSContextStack (result[0]);
	result[0] = 0;
	rc = stack.Push (0);
	if (rc != XPCOM.NS_OK) error (rc);

	Shell shell = browser.getShell ();
	Display display = browser.getDisplay ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}

	rc = stack.Pop (result);
	if (rc != XPCOM.NS_OK) error (rc);
	stack.Release ();
	return XPCOM.NS_OK;
}

int IsWindowModal (long /*int*/ retval) {
	boolean result = (chromeFlags & nsIWebBrowserChrome.CHROME_MODAL) != 0;
	XPCOM.memmove (retval, new boolean[] {result});
	return XPCOM.NS_OK;
}

int ExitModalEventLoop (int aStatus) {
	return XPCOM.NS_OK;
}

/* nsIEmbeddingSiteWindow */

int SetDimensions (int flags, int x, int y, int cx, int cy) {
	boolean isChrome = (chromeFlags & nsIWebBrowserChrome.CHROME_OPENAS_CHROME) != 0;
	if ((flags & nsIEmbeddingSiteWindow.DIM_FLAGS_POSITION) != 0) {
		location = new Point (x, y);
		if (isChrome) {
			browser.getShell ().setLocation (x, y);
		}
	}
	if ((flags & nsIEmbeddingSiteWindow.DIM_FLAGS_SIZE_INNER) != 0) {
		size = new Point (cx, cy);
		if (isChrome) {
			browser.setSize (cx, cy);
		}
	}
	if ((flags & nsIEmbeddingSiteWindow.DIM_FLAGS_SIZE_OUTER) != 0) {
		if (isChrome) {
			browser.getShell ().setSize (cx, cy);
		}
	}
	return XPCOM.NS_OK;
}

int GetDimensions (int flags, long /*int*/ x, long /*int*/ y, long /*int*/ cx, long /*int*/ cy) {
	if ((flags & nsIEmbeddingSiteWindow.DIM_FLAGS_POSITION) != 0) {
		Point location = browser.getShell ().getLocation ();
		if (x != 0) C.memmove (x, new int[] {location.x}, 4); /* PRInt32 */
		if (y != 0) C.memmove (y, new int[] {location.y}, 4); /* PRInt32 */
	}
	if ((flags & nsIEmbeddingSiteWindow.DIM_FLAGS_SIZE_INNER) != 0) {
		Point size = browser.getSize ();
		if (cx != 0) C.memmove (cx, new int[] {size.x}, 4); /* PRInt32 */
		if (cy != 0) C.memmove (cy, new int[] {size.y}, 4); /* PRInt32 */
	}
	if ((flags & nsIEmbeddingSiteWindow.DIM_FLAGS_SIZE_OUTER) != 0) {
		Point size = browser.getShell().getSize ();
		if (cx != 0) C.memmove (cx, new int[] {size.x}, 4); /* PRInt32 */
		if (cy != 0) C.memmove (cy, new int[] {size.y}, 4); /* PRInt32 */
	}
	return XPCOM.NS_OK;
}

int SetFocus () {
	long /*int*/[] result = new long /*int*/[1];
	int rc = webBrowser.QueryInterface (IIDStore.GetIID (nsIBaseWindow.class), result);
	if (rc != XPCOM.NS_OK) {
		browser.dispose ();
		error (rc);
	}
	if (result[0] == 0) error (XPCOM.NS_ERROR_NO_INTERFACE);

	nsIBaseWindow baseWindow = new nsIBaseWindow (result[0]);
	rc = baseWindow.SetFocus ();
	if (rc != XPCOM.NS_OK) error (rc);
	baseWindow.Release ();

	/*
	* Note. Mozilla notifies here that one of the children took
	* focus. This could or should be used to fire an SWT.FOCUS_IN
	* event on Browser focus listeners.
	*/
	return XPCOM.NS_OK;
}

int GetVisibility (long /*int*/ aVisibility) {
	boolean visible = browser.isVisible () && !browser.getShell ().getMinimized ();
	XPCOM.memmove (aVisibility, new boolean[] {visible});
	return XPCOM.NS_OK;
}

int SetVisibility (int aVisibility) {
	if (isChild) {
		WindowEvent event = new WindowEvent (browser);
		event.display = browser.getDisplay ();
		event.widget = browser;
		if (aVisibility != 0) {
			/*
			* Bug in Mozilla.  When the JavaScript window.open is executed, Mozilla
			* fires multiple SetVisibility 1 notifications.  The workaround is
			* to ignore subsequent notifications.
			*/
			if (!visible) {
				visible = true;
				event.location = location;
				event.size = size;
				event.addressBar = (chromeFlags & nsIWebBrowserChrome.CHROME_LOCATIONBAR) != 0;
				/* Feature of OSX.  The menu bar is always displayed. */
				boolean isOSX = Platform.PLATFORM.equals ("cocoa");
				event.menuBar = isOSX || (chromeFlags & nsIWebBrowserChrome.CHROME_MENUBAR) != 0;
				event.statusBar = (chromeFlags & nsIWebBrowserChrome.CHROME_STATUSBAR) != 0;
				event.toolBar = (chromeFlags & nsIWebBrowserChrome.CHROME_TOOLBAR) != 0;
				for (int i = 0; i < visibilityWindowListeners.length; i++) {
					visibilityWindowListeners[i].show (event);
				}
				location = null;
				size = null;
			}
		} else {
			visible = false;
			for (int i = 0; i < visibilityWindowListeners.length; i++) {
				visibilityWindowListeners[i].hide (event);
			}
		}
	}
	return XPCOM.NS_OK;
}

int GetTitle (long /*int*/ aTitle) {
	return XPCOM.NS_OK;
}

int SetTitle (long /*int*/ aTitle) {
	if (titleListeners.length == 0) return XPCOM.NS_OK;
	TitleEvent event = new TitleEvent (browser);
	event.display = browser.getDisplay ();
	event.widget = browser;
	/*
	* To be consistent with other platforms the title event should
	* contain the page's url if the page does not contain a <title>
	* tag.
	*/
	int length = XPCOM.strlen_PRUnichar (aTitle);
	if (length > 0) {
		char[] dest = new char[length];
		XPCOM.memmove (dest, aTitle, length * 2);
		event.title = new String (dest);
	} else {
		event.title = getUrl ();
	}
	for (int i = 0; i < titleListeners.length; i++) {
		titleListeners[i].changed (event);
	}
	return XPCOM.NS_OK;
}

int GetSiteWindow (long /*int*/ aSiteWindow) {
	/*
	* This is expected to be an HWND on Windows, a GtkWidget* on GTK, and
	* a WindowPtr on OS X.  This callback is invoked on Windows when the
	* print dialog is to be shown.  If no handle is returned then no print
	* dialog is shown with XULRunner versions < 4.
	*/

	long /*int*/ siteWindow = delegate.getSiteWindow ();
	XPCOM.memmove (aSiteWindow, new long /*int*/[] {siteWindow}, C.PTR_SIZEOF);
	return XPCOM.NS_OK;
}

int Blur () {
	return XPCOM.NS_OK;
}

/* nsIWebBrowserChromeFocus */

int FocusNextElement () {
	/*
	* Bug in Mozilla embedding API.  Mozilla takes back the focus after sending
	* this event.  This prevents tabbing out of Mozilla. This behaviour can be reproduced
	* with the Mozilla application TestGtkEmbed.  The workaround is to
	* send the traversal notification after this callback returns.
	*/
	browser.getDisplay ().asyncExec (() -> {
		if (browser.isDisposed ()) return;
		browser.traverse (SWT.TRAVERSE_TAB_NEXT);
	});
	return XPCOM.NS_OK;
}

int FocusPrevElement () {
	/*
	* Bug in Mozilla embedding API.  Mozilla takes back the focus after sending
	* this event.  This prevents tabbing out of Mozilla. This behaviour can be reproduced
	* with the Mozilla application TestGtkEmbed.  The workaround is to
	* send the traversal notification after this callback returns.
	*/
	browser.getDisplay ().asyncExec (() -> {
		if (browser.isDisposed ()) return;
		browser.traverse (SWT.TRAVERSE_TAB_PREVIOUS);
	});
	return XPCOM.NS_OK;
}

/* nsIContextMenuListener */

int OnShowContextMenu (int aContextFlags, long /*int*/ aEvent, long /*int*/ aNode) {
	nsIDOMEvent domEvent = new nsIDOMEvent (aEvent);
	long /*int*/[] result = new long /*int*/[1];
	int rc = domEvent.QueryInterface (IIDStore.GetIID (nsIDOMMouseEvent.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

	int[] aScreenX = new int[1], aScreenY = new int[1];
	nsIDOMMouseEvent domMouseEvent = new nsIDOMMouseEvent (result[0]);
	rc = domMouseEvent.GetScreenX (aScreenX);
	if (rc != XPCOM.NS_OK) error (rc);
	rc = domMouseEvent.GetScreenY (aScreenY);
	if (rc != XPCOM.NS_OK) error (rc);
	domMouseEvent.Release ();

	Event event = new Event ();
	aScreenX[0] = DPIUtil.autoScaleDown(aScreenX[0]); // To Points
	aScreenY[0] = DPIUtil.autoScaleDown(aScreenY[0]); // To Points
	event.x = aScreenX[0];
	event.y = aScreenY[0];
	browser.notifyListeners (SWT.MenuDetect, event);
	if (!event.doit || browser.isDisposed ()) return XPCOM.NS_OK;
	Menu menu = browser.getMenu ();
	if (menu != null && !menu.isDisposed ()) {
		if (aScreenX[0] != event.x || aScreenY[0] != event.y) {
			menu.setLocation (event.x, event.y);
		}
		menu.setVisible (true);
	}
	return XPCOM.NS_OK;
}

/* nsIURIContentListener */

int OnStartURIOpen (long /*int*/ aURI, long /*int*/ retval) {
	if (isRetrievingBadCert) return XPCOM.NS_OK;
	authCount = 0;

	nsIURI location = new nsIURI (aURI);
	long /*int*/ aSpec = XPCOM.nsEmbedCString_new ();
	location.GetSpec (aSpec);
	int length = XPCOM.nsEmbedCString_Length (aSpec);
	long /*int*/ buffer = XPCOM.nsEmbedCString_get (aSpec);
	buffer = XPCOM.nsEmbedCString_get (aSpec);
	byte[] dest = new byte[length];
	XPCOM.memmove (dest, buffer, length);
	XPCOM.nsEmbedCString_delete (aSpec);
	String value = new String (dest);

	/*
	* Navigating to "...aboutCertError.xhtml", or to "javascript:showSecuritySection()" when
	* the page "netError.xhtml" is showing, indicates that the last attempted page view had
	* an invalid certificate.  When this happens, veto the current navigate and re-navigate
	* to the page with the bad certificate so that NotifyCertProblem will be invoked.
	*/
	if (value.indexOf ("aboutCertError.xhtml") != -1 || (isViewingErrorPage && value.indexOf ("javascript:showSecuritySection") != -1)) { //$NON-NLS-1$ //$NON-NLS-2$
		XPCOM.memmove (retval, new boolean[] {true});
		isRetrievingBadCert = true;
		setUrl (lastNavigateURL, (byte[])null, null);
		return XPCOM.NS_OK;
	}
	boolean doit = sendChangingEvent (value);
	XPCOM.memmove (retval, new boolean[] {!doit});
	return XPCOM.NS_OK;
}

int DoContent (long /*int*/ aContentType, int aIsContentPreferred, long /*int*/ aRequest, long /*int*/ aContentHandler, long /*int*/ retval) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int IsPreferred (long /*int*/ aContentType, long /*int*/ aDesiredContentType, long /*int*/ retval) {
	boolean preferred = false;
	int size = XPCOM.strlen (aContentType);
	if (size > 0) {
		byte[] typeBytes = new byte[size + 1];
		XPCOM.memmove (typeBytes, aContentType, size);
		String contentType = new String (typeBytes, 0, size);

		/* do not attempt to handle known problematic content types */
		if (!contentType.equals (XPCOM.CONTENT_MAYBETEXT) && !contentType.equals (XPCOM.CONTENT_MULTIPART)) {
			/* determine whether browser can handle the content type */
			long /*int*/[] result = new long /*int*/[1];
			int rc = XPCOM.NS_GetServiceManager (result);
			if (rc != XPCOM.NS_OK) error (rc);
			if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
			nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
			result[0] = 0;

			/* First try to use the nsIWebNavigationInfo if it's available (>= mozilla 1.8) */
			byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_WEBNAVIGATIONINFO_CONTRACTID, true);
			rc = serviceManager.GetServiceByContractID (aContractID, IIDStore.GetIID (nsIWebNavigationInfo.class), result);
			if (rc == XPCOM.NS_OK) {
				byte[] bytes = MozillaDelegate.wcsToMbcs (null, contentType, false);
				long /*int*/ typePtr = XPCOM.nsEmbedCString_new (bytes, bytes.length);
				nsIWebNavigationInfo info = new nsIWebNavigationInfo (result[0]);
				result[0] = 0;
				int[] isSupportedResult = new int[1]; /* PRUint32 */
				rc = info.IsTypeSupported (typePtr, 0, isSupportedResult);
				if (rc != XPCOM.NS_OK) error (rc);
				info.Release ();
				XPCOM.nsEmbedCString_delete (typePtr);
				preferred = isSupportedResult[0] != 0;
			} else {
				/* nsIWebNavigationInfo is not available, so do the type lookup */
				result[0] = 0;
				rc = serviceManager.GetService (XPCOM.NS_CATEGORYMANAGER_CID, IIDStore.GetIID (nsICategoryManager.class), result);
				if (rc != XPCOM.NS_OK) error (rc);
				if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

				nsICategoryManager categoryManager = new nsICategoryManager (result[0]);
				result[0] = 0;
				byte[] categoryBytes = MozillaDelegate.wcsToMbcs (null, "Gecko-Content-Viewers", true);	//$NON-NLS-1$
				rc = categoryManager.GetCategoryEntry (categoryBytes, typeBytes, result);
				categoryManager.Release ();
				/* if no viewer for the content type is registered then rc == XPCOM.NS_ERROR_NOT_AVAILABLE */
				preferred = rc == XPCOM.NS_OK;
			}
			serviceManager.Release ();
		}
	}

	XPCOM.memmove(retval, new boolean[] {preferred});
	if (preferred) {
		XPCOM.memmove (aDesiredContentType, new long /*int*/[] {0}, C.PTR_SIZEOF);
	}
	return XPCOM.NS_OK;
}

int CanHandleContent (long /*int*/ aContentType, int aIsContentPreferred, long /*int*/ aDesiredContentType, long /*int*/ retval) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetLoadCookie (long /*int*/ aLoadCookie) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetLoadCookie (long /*int*/ aLoadCookie) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetParentContentListener (long /*int*/ aParentContentListener) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetParentContentListener (long /*int*/ aParentContentListener) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

/* nsITooltipListener */

int OnShowTooltip (int aXCoords, int aYCoords, long /*int*/ aTipText) {
	int length = XPCOM.strlen_PRUnichar (aTipText);
	char[] dest = new char[length];
	XPCOM.memmove (dest, aTipText, length * 2);
	String text = new String (dest);
	if (tip != null && !tip.isDisposed ()) tip.dispose ();
	Display display = browser.getDisplay ();
	Shell parent = browser.getShell ();
	tip = new Shell (parent, SWT.ON_TOP);
	tip.setLayout (new FillLayout());
	Label label = new Label (tip, SWT.CENTER);
	label.setForeground (display.getSystemColor (SWT.COLOR_INFO_FOREGROUND));
	label.setBackground (display.getSystemColor (SWT.COLOR_INFO_BACKGROUND));
	label.setText (text);
	/*
	* Bug in Mozilla embedded API.  Tooltip coordinates are wrong for
	* elements inside an inline frame (IFrame tag).  The workaround is
	* to position the tooltip based on the mouse cursor location.
	*/
	Point point = display.getCursorLocation ();
	/* Assuming cursor is 21x21 because this is the size of
	 * the arrow cursor on Windows
	 */
	point.y += 21;
	tip.setLocation (point);
	tip.pack ();
	tip.setVisible (true);
	return XPCOM.NS_OK;
}

int OnHideTooltip () {
	if (tip != null && !tip.isDisposed ()) tip.dispose ();
	tip = null;
	return XPCOM.NS_OK;
}

int HandleEvent (long /*int*/ event) {
	nsIDOMEvent domEvent = new nsIDOMEvent (event);
	nsEmbedString type = new nsEmbedString ();
	int rc = domEvent.GetType (type.getAddress());
	if (rc != XPCOM.NS_OK) error (rc);
	String typeString = type.toString();
	type.dispose();

	if (XPCOM.DOMEVENT_UNLOAD.equals (typeString)) {
		long /*int*/[] result = new long /*int*/[1];
		rc = domEvent.GetCurrentTarget (result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

		nsIDOMEventTarget target = new nsIDOMEventTarget (result[0]);
		unhookDOMListeners (target);
		target.Release ();
		return XPCOM.NS_OK;
	}

	if (XPCOM.DOMEVENT_FOCUS.equals (typeString)) {
		delegate.handleFocus ();
		return XPCOM.NS_OK;
	}

	if (XPCOM.DOMEVENT_KEYDOWN.equals (typeString)) {
		long /*int*/[] result = new long /*int*/[1];
		rc = domEvent.QueryInterface (IIDStore.GetIID (nsIDOMKeyEvent.class), result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
		nsIDOMKeyEvent domKeyEvent = new nsIDOMKeyEvent (result[0]);
		result[0] = 0;

		int[] aKeyCode = new int[1]; /* PRUint32 */
		rc = domKeyEvent.GetKeyCode (aKeyCode);
		if (rc != XPCOM.NS_OK) error (rc);
		int keyCode = translateKey (aKeyCode[0]);

		/*
		* if keyCode == lastKeyCode then either a repeating key like Shift
		* is being held or a key for which key events are not sent has been
		* pressed.  In both of these cases a KeyDown should not be sent.
		*/
		if (keyCode != lastKeyCode) {
			lastKeyCode = keyCode;
			switch (keyCode) {
				case SWT.SHIFT:
				case SWT.CONTROL:
				case SWT.ALT:
				case SWT.CAPS_LOCK:
				case SWT.NUM_LOCK:
				case SWT.SCROLL_LOCK:
				case SWT.COMMAND: {
					/* keypress events will not be received for these keys, so send KeyDowns for them now */
					int[] aAltKey = new int[1], aCtrlKey = new int[1], aShiftKey = new int[1], aMetaKey = new int[1]; /* PRBool */
					rc = domKeyEvent.GetAltKey (aAltKey);
					if (rc != XPCOM.NS_OK) error (rc);
					rc = domKeyEvent.GetCtrlKey (aCtrlKey);
					if (rc != XPCOM.NS_OK) error (rc);
					rc = domKeyEvent.GetShiftKey (aShiftKey);
					if (rc != XPCOM.NS_OK) error (rc);
					rc = domKeyEvent.GetMetaKey (aMetaKey);
					if (rc != XPCOM.NS_OK) error (rc);

					Event keyEvent = new Event ();
					keyEvent.widget = browser;
					keyEvent.type = SWT.KeyDown;
					keyEvent.keyCode = keyCode;
					keyEvent.stateMask = (aAltKey[0] != 0 ? SWT.ALT : 0) | (aCtrlKey[0] != 0 ? SWT.CTRL : 0) | (aShiftKey[0] != 0 ? SWT.SHIFT : 0) | (aMetaKey[0] != 0 ? SWT.COMMAND : 0);
					keyEvent.stateMask &= ~keyCode;		/* remove current keydown if it's a state key */
					browser.notifyListeners (keyEvent.type, keyEvent);
					if (!keyEvent.doit || browser.isDisposed ()) {
						domEvent.PreventDefault ();
					}
					break;
				}
				default: {
					/*
					* If the keydown has Meta (but not Meta+Ctrl) as a modifier then send a KeyDown event for it here
					* because a corresponding keypress event will not be received for it from the DOM.  If the keydown
					* does not have Meta as a modifier, or has Meta+Ctrl as a modifier, then then do nothing here
					* because its KeyDown event will be sent from the keypress listener.
					*/
					int[] aMetaKey = new int[1]; /* PRBool */
					rc = domKeyEvent.GetMetaKey (aMetaKey);
					if (rc != XPCOM.NS_OK) error (rc);
					if (aMetaKey[0] != 0) {
						int[] aCtrlKey = new int[1]; /* PRBool */
						rc = domKeyEvent.GetCtrlKey (aCtrlKey);
						if (rc != XPCOM.NS_OK) error (rc);
						if (aCtrlKey[0] == 0) {
							int[] aAltKey = new int[1], aShiftKey = new int[1]; /* PRBool */
							rc = domKeyEvent.GetAltKey (aAltKey);
							if (rc != XPCOM.NS_OK) error (rc);
							rc = domKeyEvent.GetShiftKey (aShiftKey);
							if (rc != XPCOM.NS_OK) error (rc);

							Event keyEvent = new Event ();
							keyEvent.widget = browser;
							keyEvent.type = SWT.KeyDown;
							keyEvent.keyCode = lastKeyCode;
							keyEvent.stateMask = (aAltKey[0] != 0 ? SWT.ALT : 0) | (aCtrlKey[0] != 0? SWT.CTRL : 0) | (aShiftKey[0] != 0? SWT.SHIFT : 0) | (aMetaKey[0] != 0? SWT.COMMAND : 0);
							browser.notifyListeners (keyEvent.type, keyEvent);
							if (!keyEvent.doit || browser.isDisposed ()) {
								domEvent.PreventDefault ();
							}
						}
					}
				}
			}
		}

		domKeyEvent.Release ();
		return XPCOM.NS_OK;
	}

	if (XPCOM.DOMEVENT_KEYPRESS.equals (typeString)) {
		/*
		* if keydown could not determine a keycode for this key then it's a
		* key for which key events are not sent (eg.- the Windows key)
		*/
		if (lastKeyCode == 0) return XPCOM.NS_OK;

		/*
		* On linux only, unexpected keypress events are received for some
		* modifier keys.  The workaround is to ignore these events since
		* KeyDown events are sent for these keys in the keydown listener.
		*/
		switch (lastKeyCode) {
			case SWT.CAPS_LOCK:
			case SWT.NUM_LOCK:
			case SWT.SCROLL_LOCK: return XPCOM.NS_OK;
		}

		long /*int*/[] result = new long /*int*/[1];
		rc = domEvent.QueryInterface (IIDStore.GetIID (nsIDOMKeyEvent.class), result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
		nsIDOMKeyEvent domKeyEvent = new nsIDOMKeyEvent (result[0]);
		result[0] = 0;

		int[] aAltKey = new int[1], aCtrlKey = new int[1], aShiftKey = new int[1], aMetaKey = new int[1]; /* PRBool */
		rc = domKeyEvent.GetAltKey (aAltKey);
		if (rc != XPCOM.NS_OK) error (rc);
		rc = domKeyEvent.GetCtrlKey (aCtrlKey);
		if (rc != XPCOM.NS_OK) error (rc);
		rc = domKeyEvent.GetShiftKey (aShiftKey);
		if (rc != XPCOM.NS_OK) error (rc);
		rc = domKeyEvent.GetMetaKey (aMetaKey);
		if (rc != XPCOM.NS_OK) error (rc);
		int[] aCharCode = new int[1]; /* PRUint32 */
		rc = domKeyEvent.GetCharCode (aCharCode);
		if (rc != XPCOM.NS_OK) error (rc);
		domKeyEvent.Release ();
		lastCharCode = aCharCode[0];
		if (lastCharCode == 0) {
			switch (lastKeyCode) {
				case SWT.TAB: lastCharCode = SWT.TAB; break;
				case SWT.CR: lastCharCode = SWT.CR; break;
				case SWT.BS: lastCharCode = SWT.BS; break;
				case SWT.ESC: lastCharCode = SWT.ESC; break;
				case SWT.DEL: lastCharCode = SWT.DEL; break;
			}
		}
		if (aCtrlKey[0] != 0 && (0 <= lastCharCode && lastCharCode <= 0x7F)) {
			if ('a'  <= lastCharCode && lastCharCode <= 'z') lastCharCode -= 'a' - 'A';
			if (64 <= lastCharCode && lastCharCode <= 95) lastCharCode -= 64;
		}

		Event keyEvent = new Event ();
		keyEvent.widget = browser;
		keyEvent.type = SWT.KeyDown;
		keyEvent.keyCode = lastKeyCode;
		keyEvent.character = (char)lastCharCode;
		keyEvent.stateMask = (aAltKey[0] != 0 ? SWT.ALT : 0) | (aCtrlKey[0] != 0 ? SWT.CTRL : 0) | (aShiftKey[0] != 0 ? SWT.SHIFT : 0) | (aMetaKey[0] != 0 ? SWT.COMMAND : 0);
		boolean doit = true;
		if (delegate.sendTraverse ()) {
			doit = sendKeyEvent (keyEvent);
		} else {
			browser.notifyListeners (keyEvent.type, keyEvent);
			doit = keyEvent.doit;
		}
		if (!doit || browser.isDisposed ()) {
			domEvent.PreventDefault ();
		}
		return XPCOM.NS_OK;
	}

	if (XPCOM.DOMEVENT_KEYUP.equals (typeString)) {
		long /*int*/[] result = new long /*int*/[1];
		rc = domEvent.QueryInterface (IIDStore.GetIID (nsIDOMKeyEvent.class), result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
		nsIDOMKeyEvent domKeyEvent = new nsIDOMKeyEvent (result[0]);
		result[0] = 0;

		int[] aKeyCode = new int[1]; /* PRUint32 */
		rc = domKeyEvent.GetKeyCode (aKeyCode);
		if (rc != XPCOM.NS_OK) error (rc);
		int keyCode = translateKey (aKeyCode[0]);
		if (keyCode == 0) {
			/* indicates a key for which key events are not sent */
			domKeyEvent.Release ();
			return XPCOM.NS_OK;
		}
		if (keyCode != lastKeyCode) {
			/* keyup does not correspond to the last keydown */
			lastKeyCode = keyCode;
			lastCharCode = 0;
		}

		int[] aAltKey = new int[1], aCtrlKey = new int[1], aShiftKey = new int[1], aMetaKey = new int[1]; /* PRBool */
		rc = domKeyEvent.GetAltKey (aAltKey);
		if (rc != XPCOM.NS_OK) error (rc);
		rc = domKeyEvent.GetCtrlKey (aCtrlKey);
		if (rc != XPCOM.NS_OK) error (rc);
		rc = domKeyEvent.GetShiftKey (aShiftKey);
		if (rc != XPCOM.NS_OK) error (rc);
		rc = domKeyEvent.GetMetaKey (aMetaKey);
		if (rc != XPCOM.NS_OK) error (rc);
		domKeyEvent.Release ();

		Event keyEvent = new Event ();
		keyEvent.widget = browser;
		keyEvent.type = SWT.KeyUp;
		keyEvent.keyCode = lastKeyCode;
		keyEvent.character = (char)lastCharCode;
		keyEvent.stateMask = (aAltKey[0] != 0 ? SWT.ALT : 0) | (aCtrlKey[0] != 0 ? SWT.CTRL : 0) | (aShiftKey[0] != 0 ? SWT.SHIFT : 0) | (aMetaKey[0] != 0 ? SWT.COMMAND : 0);
		switch (lastKeyCode) {
			case SWT.SHIFT:
			case SWT.CONTROL:
			case SWT.ALT:
			case SWT.COMMAND: {
				keyEvent.stateMask |= lastKeyCode;
			}
		}
		browser.notifyListeners (keyEvent.type, keyEvent);
		if (!keyEvent.doit || browser.isDisposed ()) {
			domEvent.PreventDefault ();
		}
		lastKeyCode = lastCharCode = 0;
		return XPCOM.NS_OK;
	}

	/* mouse event */

	long /*int*/[] result = new long /*int*/[1];
	rc = domEvent.QueryInterface (IIDStore.GetIID (nsIDOMMouseEvent.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);
	nsIDOMMouseEvent domMouseEvent = new nsIDOMMouseEvent (result[0]);
	result[0] = 0;

	/*
	 * MouseOver and MouseOut events are fired any time the mouse enters or exits
	 * any element within the Browser.  To ensure that SWT events are only
	 * fired for mouse movements into or out of the Browser, do not fire an
	 * event if the element being exited (on MouseOver) or entered (on MouseExit)
	 * is within the Browser.
	 */
	if (XPCOM.DOMEVENT_MOUSEOVER.equals (typeString) || XPCOM.DOMEVENT_MOUSEOUT.equals (typeString)) {
		rc = domMouseEvent.GetRelatedTarget (result);
		if (rc != XPCOM.NS_OK) error (rc);
		if (result[0] != 0) {
			new nsISupports (result[0]).Release ();
			domMouseEvent.Release ();
			return XPCOM.NS_OK;
		}
	}

	int[] aScreenX = new int[1], aScreenY = new int[1]; /* PRInt32 */

	/*
	 * The position of mouse events is received in screen-relative coordinates
	 * in order to handle pages with frames, since frames express their event
	 * coordinates relative to themselves rather than relative to their top-
	 * level page.  Convert screen-relative coordinates to be browser-relative.
	 */
	rc = domMouseEvent.GetScreenX (aScreenX);
	if (rc != XPCOM.NS_OK) error (rc);
	rc = domMouseEvent.GetScreenY (aScreenY);
	if (rc != XPCOM.NS_OK) error (rc);
	Point position = DPIUtil.autoScaleDown(new Point (aScreenX[0], aScreenY[0]));// To Points
	position = browser.getDisplay ().map (null, browser, position);

	int[] aDetail = new int[1]; /* PRInt32 */
	rc = domMouseEvent.GetDetail (aDetail);
	if (rc != XPCOM.NS_OK) error (rc);
	short[] aButton = new short[1]; /* PRUint16 */
	rc = domMouseEvent.GetButton (aButton);
	if (rc != XPCOM.NS_OK) error (rc);
	int[] aAltKey = new int[1], aCtrlKey = new int[1], aShiftKey = new int[1], aMetaKey = new int[1]; /* PRBool */
	rc = domMouseEvent.GetAltKey (aAltKey);
	if (rc != XPCOM.NS_OK) error (rc);
	rc = domMouseEvent.GetCtrlKey (aCtrlKey);
	if (rc != XPCOM.NS_OK) error (rc);
	rc = domMouseEvent.GetShiftKey (aShiftKey);
	if (rc != XPCOM.NS_OK) error (rc);
	rc = domMouseEvent.GetMetaKey (aMetaKey);
	if (rc != XPCOM.NS_OK) error (rc);
	domMouseEvent.Release ();

	Event mouseEvent = new Event ();
	mouseEvent.widget = browser;
	mouseEvent.x = position.x; mouseEvent.y = position.y;
	mouseEvent.stateMask = (aAltKey[0] != 0 ? SWT.ALT : 0) | (aCtrlKey[0] != 0 ? SWT.CTRL : 0) | (aShiftKey[0] != 0 ? SWT.SHIFT : 0) | (aMetaKey[0] != 0 ? SWT.COMMAND : 0);

	if (XPCOM.DOMEVENT_MOUSEDOWN.equals (typeString)) {
		delegate.handleMouseDown ();
		mouseEvent.type = SWT.MouseDown;
		mouseEvent.button = aButton[0] + 1;
		mouseEvent.count = aDetail[0];
	} else if (XPCOM.DOMEVENT_MOUSEUP.equals (typeString)) {
		/*
		 * Bug on OSX.  For some reason multiple mouseup events come from the DOM
		 * when button 3 is released on OSX.  The first of these events has a count
		 * detail and the others do not.  The workaround is to not fire received
		 * button 3 mouseup events that do not have a count since mouse events
		 * without a click count are not valid.
		 */
		int button = aButton[0] + 1;
		int count = aDetail[0];
		if (count == 0 && button == 3) return XPCOM.NS_OK;
		mouseEvent.type = SWT.MouseUp;
		mouseEvent.button = button;
		mouseEvent.count = count;
	} else if (XPCOM.DOMEVENT_MOUSEMOVE.equals (typeString)) {
		mouseEvent.type = SWT.MouseMove;
	} else if (XPCOM.DOMEVENT_MOUSEWHEEL.equals (typeString)) {
		mouseEvent.type = SWT.MouseWheel;
		mouseEvent.count = -aDetail[0];
	} else if (XPCOM.DOMEVENT_MOUSEOVER.equals (typeString)) {
		mouseEvent.type = SWT.MouseEnter;
	} else if (XPCOM.DOMEVENT_MOUSEOUT.equals (typeString)) {
		mouseEvent.type = SWT.MouseExit;
	} else if (XPCOM.DOMEVENT_MOUSEDRAG.equals (typeString)) {
		mouseEvent.type = SWT.DragDetect;
		mouseEvent.button = aButton[0] + 1;
		switch (mouseEvent.button) {
			case 1: mouseEvent.stateMask |= SWT.BUTTON1; break;
			case 2: mouseEvent.stateMask |= SWT.BUTTON2; break;
			case 3: mouseEvent.stateMask |= SWT.BUTTON3; break;
			case 4: mouseEvent.stateMask |= SWT.BUTTON4; break;
			case 5: mouseEvent.stateMask |= SWT.BUTTON5; break;
		}
	}

	browser.notifyListeners (mouseEvent.type, mouseEvent);
	if (browser.isDisposed ()) return XPCOM.NS_OK;
	if (aDetail[0] == 2 && XPCOM.DOMEVENT_MOUSEDOWN.equals (typeString)) {
		mouseEvent = new Event ();
		mouseEvent.widget = browser;
		mouseEvent.x = position.x; mouseEvent.y = position.y;
		mouseEvent.stateMask = (aAltKey[0] != 0 ? SWT.ALT : 0) | (aCtrlKey[0] != 0 ? SWT.CTRL : 0) | (aShiftKey[0] != 0 ? SWT.SHIFT : 0) | (aMetaKey[0] != 0 ? SWT.COMMAND : 0);
		mouseEvent.type = SWT.MouseDoubleClick;
		mouseEvent.button = aButton[0] + 1;
		mouseEvent.count = aDetail[0];
		browser.notifyListeners (mouseEvent.type, mouseEvent);
	}
	return XPCOM.NS_OK;
}

/* nsIBadCertListener2 */

int NotifyCertProblem (long /*int*/ socketInfo, long /*int*/ status, long /*int*/ targetSite, long /*int*/ _suppressError) {
	/* determine the host name and port */
	int length = XPCOM.nsEmbedCString_Length (targetSite);
	long /*int*/ buffer = XPCOM.nsEmbedCString_get (targetSite);
	byte[] dest = new byte[length];
	XPCOM.memmove (dest, buffer, length);
	final String urlPort = new String (dest);
	int index = urlPort.indexOf (':');
	final String host = urlPort.substring (0,index);
	final int port = Integer.valueOf (urlPort.substring (index + 1)).intValue ();

	/* create text descriptions of the certificate problem(s) */

	long /*int*/[] result = new long /*int*/[1];
	nsISupports supports = new nsISupports (status);
	int rc = supports.QueryInterface (IIDStore.GetIID (nsISSLStatus.class), result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_NOINTERFACE);

	nsISSLStatus sslStatus = new nsISSLStatus (result[0]);
	result[0] = 0;
	rc = sslStatus.GetServerCert (result);
	if (rc != XPCOM.NS_OK) error (rc);
	if (result[0] == 0) error (XPCOM.NS_ERROR_NULL_POINTER);

	final nsIX509Cert cert = new nsIX509Cert (result[0]);
	result[0] = 0;
	String[] problems = new String[3];
	int problemCount = 0, flags = 0;
	int[] intResult = new int[1];

	rc = sslStatus.GetIsDomainMismatch (intResult);
	if (intResult[0] != 0) {
		nsEmbedString ptr = new nsEmbedString ();
		rc = cert.GetCommonName (ptr.getAddress());
		if (rc != XPCOM.NS_OK) SWT.error (rc);
		String name = ptr.toString ();
		problems[problemCount++] = Compatibility.getMessage ("SWT_InvalidCert_InvalidName", new String[] {name}); //$NON-NLS-1$
		flags |= nsICertOverrideService.ERROR_MISMATCH;
		ptr.dispose ();
	}
	intResult[0] = 0;

	rc = sslStatus.GetIsNotValidAtThisTime (intResult);
	if (intResult[0] != 0) {
		rc = cert.GetValidity (result);
		if (rc != XPCOM.NS_OK) SWT.error (rc);
		if (result[0] == 0) error (XPCOM.NS_ERROR_NULL_POINTER);

		nsIX509CertValidity validity = new nsIX509CertValidity(result[0]);
		result[0] = 0;

		nsEmbedString ptr = new nsEmbedString ();
		rc = validity.GetNotBeforeGMT (ptr.getAddress());
		if (rc != XPCOM.NS_OK) SWT.error (rc);
		String notBefore = ptr.toString ();
		ptr.dispose ();

		ptr = new nsEmbedString ();
		rc = validity.GetNotAfterGMT (ptr.getAddress());
		if (rc != XPCOM.NS_OK) SWT.error (rc);
		String notAfter = ptr.toString ();
		ptr.dispose ();

		String range = notBefore + " - " + notAfter; //$NON-NLS-1$
		problems[problemCount++] = Compatibility.getMessage ("SWT_InvalidCert_NotValid", new String[] {range}); //$NON-NLS-1$
		flags |= nsICertOverrideService.ERROR_TIME;
		validity.Release ();
	}
	intResult[0] = 0;

	rc = sslStatus.GetIsUntrusted (intResult);
	if (intResult[0] != 0) {
		nsEmbedString ptr = new nsEmbedString ();
		rc = cert.GetIssuerCommonName (ptr.getAddress());
		if (rc != XPCOM.NS_OK) SWT.error (rc);
		String name = ptr.toString ();
		problems[problemCount++] = Compatibility.getMessage ("SWT_InvalidCert_NotTrusted", new String[] {name}); //$NON-NLS-1$
		flags |= nsICertOverrideService.ERROR_UNTRUSTED;
		ptr.dispose ();
	}
	intResult[0] = 0;
	sslStatus.Release ();

	/*
	* The invalid certificate dialog must be shown asynchronously because
	* NotifyCertProblem implementations cannot block.
	*/
	final int finalFlags = flags;
	final String[] finalProblems = new String[problemCount];
	System.arraycopy (problems, 0, finalProblems, 0, problemCount);
	final String url = lastNavigateURL;
	browser.getDisplay().asyncExec(() -> {
		if (browser.isDisposed ()) return;
		if (url.equals (lastNavigateURL)) {
			String message = Compatibility.getMessage ("SWT_InvalidCert_Message", new String[] {urlPort}); //$NON-NLS-1$
			if (new PromptDialog (browser.getShell ()).invalidCert (browser, message, finalProblems, cert)) {
				long /*int*/[] result1 = new long /*int*/[1];
				int rc1 = XPCOM.NS_GetServiceManager (result1);
				if (rc1 != XPCOM.NS_OK) error (rc1);
				if (result1[0] == 0) error (XPCOM.NS_NOINTERFACE);

				nsIServiceManager serviceManager = new nsIServiceManager (result1[0]);
				result1[0] = 0;
				byte[] aContractID = MozillaDelegate.wcsToMbcs (null, XPCOM.NS_CERTOVERRIDE_CONTRACTID, true);
				rc1 = serviceManager.GetServiceByContractID (aContractID, IIDStore.GetIID (nsICertOverrideService.class), result1);
				if (rc1 != XPCOM.NS_OK) error (rc1);
				if (result1[0] == 0) error (XPCOM.NS_NOINTERFACE);
				serviceManager.Release ();

				nsICertOverrideService overrideService = new nsICertOverrideService (result1[0]);
				result1[0] = 0;
				byte[] hostBytes = MozillaDelegate.wcsToMbcs (null, host, false);
				long /*int*/ hostString = XPCOM.nsEmbedCString_new (hostBytes, hostBytes.length);
				rc1 = overrideService.RememberValidityOverride (hostString, port, cert.getAddress (), finalFlags, 1);
				navigate (badCertRequest);
				XPCOM.nsEmbedCString_delete (hostString);
				overrideService.Release ();
			}
			isRetrievingBadCert = false;
		}
		cert.Release ();
		new nsISupports (badCertRequest).Release ();
		badCertRequest = 0;
	});

	XPCOM.memmove (_suppressError, new boolean[] {true});
	return XPCOM.NS_OK;
}

}
