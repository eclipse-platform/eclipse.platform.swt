/*******************************************************************************
 * Copyright (c) 2003, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import java.io.*;
import java.net.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.ole.win32.*;
import org.eclipse.swt.widgets.*;

class IE extends WebBrowser {

	OleFrame frame;
	WebSite site;
	OleAutomation auto;
	OleListener domListener;
	OleAutomation[] documents = new OleAutomation[0];

	boolean back, forward, delaySetText, ignoreDispose, ignoreTraverse, performingInitialNavigate;
	boolean installFunctionsOnDocumentComplete, untrustedText, isRefresh, isAboutBlank;
	Point location;
	Point size;
	boolean addressBar = true, menuBar = true, statusBar = true, toolBar = true;
	long globalDispatch;
	String html, lastNavigateURL, uncRedirect;
	Object[] pendingText, pendingUrl;
	int style, lastKeyCode, lastCharCode;
	int lastMouseMoveX, lastMouseMoveY;

	static boolean Initialized;
	static int IEVersion, PDFCount;
	static String ProgId = "Shell.Explorer";	//$NON-NLS-1$

	static final int BeforeNavigate2 = 0xfa;
	static final int CommandStateChange = 0x69;
	static final int DocumentComplete = 0x103;
	static final int DownloadComplete = 0x68;
	static final int NavigateComplete2 = 0xfc;
	static final int NewWindow2 = 0xfb;
	static final int OnMenuBar = 0x100;
	static final int OnStatusBar = 0x101;
	static final int OnToolBar = 0xff;
	static final int OnVisible = 0xfe;
	static final int ProgressChange = 0x6c;
	static final int RegisterAsBrowser = 0x228;
	static final int StatusTextChange = 0x66;
	static final int TitleChange = 0x71;
	static final int WindowClosing = 0x107;
	static final int WindowSetHeight = 0x10b;
	static final int WindowSetLeft = 0x108;
	static final int WindowSetResizable = 0x106;
	static final int WindowSetTop = 0x109;
	static final int WindowSetWidth = 0x10a;
	static final int NavigateError = 0x10f;

	static final short CSC_NAVIGATEFORWARD = 1;
	static final short CSC_NAVIGATEBACK = 2;
	static final int INET_E_DEFAULT_ACTION = 0x800C0011;
	static final int INET_E_RESOURCE_NOT_FOUND = 0x800C0005;
	static final int READYSTATE_COMPLETE = 4;
	static final int URLPOLICY_ALLOW = 0x00;
	static final int URLPOLICY_DISALLOW = 0x03;
	static final int URLPOLICY_JAVA_PROHIBIT = 0x0;
	static final int URLPOLICY_JAVA_LOW = 0x00030000;
	static final int URLZONE_LOCAL_MACHINE = 0;
	static final int URLZONE_INTRANET = 1;
	static final int URLACTION_ACTIVEX_MIN = 0x00001200;
	static final int URLACTION_ACTIVEX_MAX = 0x000013ff;
	static final int URLACTION_ACTIVEX_RUN = 0x00001200;
	static final int URLACTION_FEATURE_ZONE_ELEVATION = 0x00002101;
	static final int URLACTION_JAVA_MIN = 0x00001C00;
	static final int URLACTION_JAVA_MAX = 0x00001Cff;
	static final int URLACTION_SCRIPT_RUN = 0x00001400;

	static final int DISPID_AMBIENT_DLCONTROL = -5512;
	static final int DLCTL_DLIMAGES = 0x00000010;
	static final int DLCTL_VIDEOS = 0x00000020;
	static final int DLCTL_BGSOUNDS = 0x00000040;
	static final int DLCTL_NO_SCRIPTS = 0x00000080;
	static final int DLCTL_NO_JAVA = 0x00000100;
	static final int DLCTL_NO_RUNACTIVEXCTLS = 0x00000200;
	static final int DLCTL_NO_DLACTIVEXCTLS = 0x00000400;
	static final int DLCTL_DOWNLOADONLY = 0x00000800;
	static final int DLCTL_NO_FRAMEDOWNLOAD = 0x00001000;
	static final int DLCTL_RESYNCHRONIZE = 0x00002000;
	static final int DLCTL_PRAGMA_NO_CACHE = 0x00004000;
	static final int DLCTL_FORCEOFFLINE = 0x10000000;
	static final int DLCTL_NO_CLIENTPULL = 0x20000000;
	static final int DLCTL_SILENT = 0x40000000;
	static final int DOCHOSTUIFLAG_THEME = 0x00040000;
	static final int DOCHOSTUIFLAG_NO3DBORDER  = 0x0000004;
	static final int DOCHOSTUIFLAG_NO3DOUTERBORDER = 0x00200000;
	static final int DOCHOSTUIFLAG_ENABLE_REDIRECT_NOTIFICATION = 0x04000000;
	static final int DOCHOSTUIFLAG_DPI_AWARE = 0x40000000;

	static final String ABOUT_BLANK = "about:blank"; //$NON-NLS-1$
	static final String CLSID_SHELLEXPLORER1 = "{EAB22AC3-30C1-11CF-A7EB-0000C05BAE0B}"; //$NON-NLS-1$
	static final int DEFAULT_IE_VERSION = 9999;
	static final String EXTENSION_PDF = ".pdf";	//$NON-NLS-1$
	static final String HTML_DOCUMENT = "HTML Document";	//$NON-NLS-1$
	static final int MAX_PDF = 20;
	static final char SEPARATOR_OS = File.separatorChar;
	static final String PROPERTY_IEVERSION = "org.eclipse.swt.browser.IEVersion"; //$NON-NLS-1$
	static final String VALUE_DEFAULT = "default"; //$NON-NLS-1$

	static final String EVENT_DOUBLECLICK = "dblclick"; //$NON-NLS-1$
	static final String EVENT_DRAGEND = "dragend";	//$NON-NLS-1$
	static final String EVENT_DRAGSTART = "dragstart";	//$NON-NLS-1$
	static final String EVENT_KEYDOWN = "keydown";	//$NON-NLS-1$
	static final String EVENT_KEYPRESS = "keypress";	//$NON-NLS-1$
	static final String EVENT_KEYUP = "keyup";	//$NON-NLS-1$
	static final String EVENT_MOUSEMOVE = "mousemove";	//$NON-NLS-1$
	static final String EVENT_MOUSEWHEEL = "mousewheel";	//$NON-NLS-1$
	static final String EVENT_MOUSEUP = "mouseup";	//$NON-NLS-1$
	static final String EVENT_MOUSEDOWN = "mousedown";	//$NON-NLS-1$
	static final String EVENT_MOUSEOUT = "mouseout";	//$NON-NLS-1$
	static final String EVENT_MOUSEOVER = "mouseover";	//$NON-NLS-1$
	static final String PROTOCOL_FILE = "file://"; //$NON-NLS-1$
	static final String PROPERTY_ALTKEY = "altKey"; //$NON-NLS-1$
	static final String PROPERTY_BUTTON = "button"; //$NON-NLS-1$
	static final String PROPERTY_CTRLKEY = "ctrlKey"; //$NON-NLS-1$
	static final String PROPERTY_DOCUMENT = "Document"; //$NON-NLS-1$
	static final String PROPERTY_FROMELEMENT = "fromElement"; //$NON-NLS-1$
	static final String PROPERTY_KEYCODE = "keyCode"; //$NON-NLS-1$
	static final String PROPERTY_REPEAT = "repeat"; //$NON-NLS-1$
	static final String PROPERTY_RETURNVALUE = "returnValue"; //$NON-NLS-1$
	static final String PROPERTY_SCREENX = "screenX"; //$NON-NLS-1$
	static final String PROPERTY_SCREENY = "screenY"; //$NON-NLS-1$
	static final String PROPERTY_SHIFTKEY = "shiftKey"; //$NON-NLS-1$
	static final String PROPERTY_TOELEMENT = "toElement"; //$NON-NLS-1$
	static final String PROPERTY_TYPE = "type"; //$NON-NLS-1$
	static final String PROPERTY_WHEELDELTA = "wheelDelta"; //$NON-NLS-1$

	static {
		NativeClearSessions = () -> {
			OS.InternetSetOption (0, OS.INTERNET_OPTION_END_BROWSER_SESSION, 0, 0);
		};

		NativeGetCookie = () -> {
			TCHAR url = new TCHAR (0, CookieUrl, true);
			TCHAR cookieData = new TCHAR (0, 8192);
			int[] size = new int[] {cookieData.length ()};
			if (!OS.InternetGetCookie (url, null, cookieData, size)) {
				/* original cookieData size was not large enough */
				size[0] /= TCHAR.sizeof;
				cookieData = new TCHAR (0, size[0]);
				if (!OS.InternetGetCookie (url, null, cookieData, size)) return;
			}
			String allCookies = cookieData.toString (0, size[0]);
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

		NativeSetCookie = () -> {
			TCHAR url = new TCHAR (0, CookieUrl, true);
			TCHAR value = new TCHAR (0, CookieValue, true);
			CookieResult = OS.InternetSetCookie (url, null, value);
		};

		/*
		* The installed version of IE can be determined by looking at registry entry
		* HKEY_LOCAL_MACHINE\Software\Microsoft\Internet Explorer\svcVersion, or
		* HKEY_LOCAL_MACHINE\Software\Microsoft\Internet Explorer\Version (for
		* IE releases prior to IE10).  Check this value in order to determine
		* version-specific features that can be enabled.
		*/
		TCHAR key = new TCHAR (0, "Software\\Microsoft\\Internet Explorer", true);	//$NON-NLS-1$
		long [] phkResult = new long [1];
		if (OS.RegOpenKeyEx (OS.HKEY_LOCAL_MACHINE, key, 0, OS.KEY_READ, phkResult) == 0) {
			int [] lpcbData = new int [1];
			TCHAR buffer = new TCHAR (0, "svcVersion", true); //$NON-NLS-1$
			int result = OS.RegQueryValueEx (phkResult [0], buffer, 0, null, (TCHAR) null, lpcbData);
			if (result != 0) {
				buffer = new TCHAR (0, "Version", true); //$NON-NLS-1$
				result = OS.RegQueryValueEx (phkResult [0], buffer, 0, null, (TCHAR) null, lpcbData);
			}
			if (result == 0) {
				TCHAR lpData = new TCHAR (0, lpcbData [0] / TCHAR.sizeof);
				result = OS.RegQueryValueEx (phkResult [0], buffer, 0, null, lpData, lpcbData);
				if (result == 0) {
					String versionString = lpData.toString (0, lpData.strlen ());
					int index = versionString.indexOf ("."); //$NON-NLS-1$
					if (index != -1) {
						String majorString = versionString.substring (0, index);
						try {
							IEVersion = Integer.parseInt (majorString);
						} catch (NumberFormatException e) {
							/* just continue, version-specific features will not be enabled */
						}
					}
				}
			}
			OS.RegCloseKey (phkResult [0]);
		}

		/*
		* Registry entry HKEY_CLASSES_ROOT\Shell.Explorer\CLSID indicates which version of
		* Shell.Explorer to use by default.  We usually want to use this value because it
		* typically points at the newest one that is available.  However it is possible for
		* this registry entry to be changed by another application to point at some other
		* Shell.Explorer version.
		*
		* The Browser depends on the Shell.Explorer version being at least Shell.Explorer.2.
		* If it is detected in the registry to be Shell.Explorer.1 then change the progId that
		* will be embedded to explicitly specify Shell.Explorer.2.
		*/
		key = new TCHAR (0, "Shell.Explorer\\CLSID", true);	//$NON-NLS-1$
		phkResult = new long [1];
		if (OS.RegOpenKeyEx (OS.HKEY_CLASSES_ROOT, key, 0, OS.KEY_READ, phkResult) == 0) {
			int [] lpcbData = new int [1];
			int result = OS.RegQueryValueEx (phkResult [0], null, 0, null, (TCHAR) null, lpcbData);
			if (result == 0) {
				TCHAR lpData = new TCHAR (0, lpcbData [0] / TCHAR.sizeof);
				result = OS.RegQueryValueEx (phkResult [0], null, 0, null, lpData, lpcbData);
				if (result == 0) {
					String clsid = lpData.toString (0, lpData.strlen ());
					if (clsid.equals (CLSID_SHELLEXPLORER1)) {
						/* Shell.Explorer.1 is the default, ensure that Shell.Explorer.2 is available */
						key = new TCHAR (0, "Shell.Explorer.2", true);	//$NON-NLS-1$
						long [] phkResult2 = new long [1];
						if (OS.RegOpenKeyEx (OS.HKEY_CLASSES_ROOT, key, 0, OS.KEY_READ, phkResult2) == 0) {
							/* specify that Shell.Explorer.2 is to be used */
							OS.RegCloseKey (phkResult2 [0]);
							ProgId = "Shell.Explorer.2";	//$NON-NLS-1$
						}
					}
				}
			}
			OS.RegCloseKey (phkResult [0]);
		}

		if (NativePendingCookies != null) {
			SetPendingCookies (NativePendingCookies);
		}
		NativePendingCookies = null;
	}

@Override
public void create(Composite parent, int style) {
	this.style = style;
	frame = new OleFrame(browser, SWT.NONE);

	try {
		site = new WebSite(frame, SWT.NONE, ProgId);
	} catch (SWTException e) {
		browser.dispose();
		SWT.error(SWT.ERROR_NO_HANDLES);
	}

	if (!Initialized) {
		Initialized = true;
		int version = 0;
		String versionProperty = System.getProperty(PROPERTY_IEVERSION);
		if (versionProperty != null) {
			if (versionProperty.equalsIgnoreCase(VALUE_DEFAULT)) {
				version = -1;
			} else {
				try {
					version = Integer.parseInt(versionProperty);
				} catch (NumberFormatException e) {
					/*
					 * An invalid value was specified for the IEVersion java property.  Ignore it
					 * and continue with the usual steps for determining the version to specify.
					 */
				}
			}
		}
		if (version == 0) {
			if (IEVersion != 0) {
				/*
				 * By default in Embedded IE the docuemntMode is Quirks(5)
				 * mode unless !DOCTYPE directives is defined in the HTML.
				 * As per MSDN IE8 and onwards, there is a way we could hint
				 * embedded IE to use current documentMode via appropriate
				 * version value in the registry. Refer bug 342145.
				 *
				 * Complete list of IE emulation modes is listed on MSDN:
				 * http://msdn.microsoft
				 * .com/en-us/library/ie/ee330730%28v=vs
				 * .85%29.aspx#browser_emulation
				 */
				if (IEVersion >= 10) {
					version = IEVersion * 1000 + 1;
				}
				else if (IEVersion >= 8) {
					version = IEVersion * 1111;
				}
				else {
					version = IEVersion * 1000;
				}
			} else {
				version = DEFAULT_IE_VERSION;
			}
		}

		if (version != -1) {
			long[] key = new long[1];
			final TCHAR subkey = new TCHAR(0, "Software\\Microsoft\\Internet Explorer\\Main\\FeatureControl\\FEATURE_BROWSER_EMULATION", true);	//$NON-NLS-1$
			if (OS.RegCreateKeyEx(OS.HKEY_CURRENT_USER, subkey, 0, null, OS.REG_OPTION_VOLATILE, OS.KEY_WRITE | OS.KEY_QUERY_VALUE, 0, key, null) == 0) {
				TCHAR lpszFile = new TCHAR(0, OS.MAX_PATH);
				OS.GetModuleFileName(0, lpszFile, lpszFile.length());
				String path = lpszFile.toString(0, lpszFile.strlen());
				int index = path.lastIndexOf(SEPARATOR_OS);
				String executable = index != -1 ? path.substring(index + 1) : path;
				final TCHAR lpValueName = new TCHAR(0, executable, true);
				/*
				 * Program name & IE version entry is added to the Windows
				 * registry and same gets deleted during the dispose cycle.
				 * There is a possibility if the SWT application crashes or
				 * is exited forcefully, which leaves the registry entry
				 * as-is and hence if entry exists, updating it next time
				 * the SWT application creates embedded IE, refer bug 440300
				 */
				int result = OS.RegQueryValueEx(key[0], lpValueName, 0, null, (int[])null, null);
				if (result == 0 || result == OS.ERROR_FILE_NOT_FOUND) {
					if (OS.RegSetValueEx(key[0], lpValueName, 0, OS.REG_DWORD, new int[] {version}, 4) == 0) {
						parent.getDisplay().addListener(SWT.Dispose, event -> {
							long[] key1 = new long[1];
							if (OS.RegOpenKeyEx(OS.HKEY_CURRENT_USER, subkey, 0, OS.KEY_WRITE, key1) == 0) {
								OS.RegDeleteValue(key1[0], lpValueName);
							}
						});
					}
				}
				OS.RegCloseKey(key[0]);
			}
		}
	}

	site.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
	auto = new OleAutomation(site);

	domListener = e -> handleDOMEvent(e);

	Listener listener = e -> {
		switch (e.type) {
			case SWT.Dispose: {
				/* make this handler run after other dispose listeners */
				if (ignoreDispose) {
					ignoreDispose = false;
					break;
				}
				ignoreDispose = true;
				browser.notifyListeners (e.type, e);
				e.type = SWT.NONE;

				/* invoke onbeforeunload handlers */
				if (!browser.isClosing) {
					LocationListener[] oldLocationListeners = locationListeners;
					locationListeners = new LocationListener[0];
					site.ignoreAllMessages = true;
					execute ("window.location.href='about:blank'"); //$NON-NLS-1$
					site.ignoreAllMessages = false;
					locationListeners = oldLocationListeners;
				}

				/*
				* It is possible for the Browser's OLE frame to have been disposed
				* by a Dispose listener that was invoked by notifyListeners above,
				* so check for this before unhooking its DOM listeners.
				*/
				if (!frame.isDisposed ()) unhookDOMListeners(documents);

				for (OleAutomation document : documents) {
					document.dispose();
				}
				documents = null;

				Iterator<BrowserFunction> elements = functions.values().iterator ();
				while (elements.hasNext ()) {
					elements.next ().dispose (false);
				}
				functions = null;

				lastNavigateURL = uncRedirect = null;
				domListener = null;
				if (auto != null) auto.dispose();
				auto = null;
				break;
			}
			case SWT.Resize: {
				frame.setBounds(browser.getClientArea());
				break;
			}
			case SWT.MouseWheel: {
				/* MouseWheel events come from the DOM */
				e.doit = false;
				break;
			}
			case SWT.FocusIn: {
				site.setFocus();
				break;
			}
			case SWT.Traverse: {
				/*
				 * Tabbing out of the browser can fail as a result of the WebSite
				 * control embedded within the Browser.  The workaround is to
				 * listen for traversals and re-perform the traversal on the
				 * appropriate control.
				 */
				if (e.detail == SWT.TRAVERSE_TAB_PREVIOUS && e.widget instanceof WebSite) {
					/* otherwise will traverse to the Browser control */
					browser.traverse(SWT.TRAVERSE_TAB_PREVIOUS, e);
					e.doit = false;
				}
				/*
				 * Return traversals can sometimes come through TranslateAccelerator,
				 * depending on where focus is within the Browser.  Traversal
				 * events should always be triggered by a key event from the DOM,
				 * so if a Traversal from TranslateAccelerator is detected
				 * (e.doit == true) then stop its propagation.
				 */
				if (e.detail == SWT.TRAVERSE_RETURN && e.doit && e.widget instanceof Browser) {
					e.type = SWT.None;
					e.doit = false;
				}
				break;
			}
		}
	};
	browser.addListener(SWT.Dispose, listener);
	browser.addListener(SWT.FocusIn, listener);
	browser.addListener(SWT.Resize, listener);
	browser.addListener(SWT.Traverse, listener);
	site.addListener(SWT.MouseWheel, listener);
	site.addListener(SWT.Traverse, listener);

	OleListener oleListener = event -> {
		/* callbacks are asynchronous, auto could be disposed */
		if (auto != null) {
			switch (event.type) {
				case BeforeNavigate2: {

					/* don't send client events if the initial navigate to about:blank has not completed */
					if (performingInitialNavigate) break;

					Variant varResult1 = event.arguments[1];
					String url1 = varResult1.getString();

					if (uncRedirect != null) {
						/*
						* Silently allow the navigate to proceed if the url is the first segment of a
						* UNC path being navigated to (initiated by the NavigateError listener to show
						* a name/password prompter), or if the url is the full UNC path (initiated by
						* the NavigateComplete listener to redirect from the UNC's first segment to its
						* full path).
						*/
						if (uncRedirect.equals(url1) || (uncRedirect.startsWith(url1) && uncRedirect.indexOf('\\', 2) == url1.length())) {
							Variant cancel1 = event.arguments[6];
							if (cancel1 != null) {
								long pCancel1 = cancel1.getByRef();
								OS.MoveMemory(pCancel1, new short[] {OS.VARIANT_FALSE}, 2);
							}
							setAboutBlank(false);
							break;
						} else {
							/*
							* This navigate does not correspond to the previously-initiated
							* UNC navigation so clear this state since it's no longer valid.
							*/
							uncRedirect = null;
						}
					}

					/*
					* Feature in IE.  For navigations on the local machine, BeforeNavigate2's url
					* field contains a string representation of the file path in a non-URL format.
					* In order to be consistent with the other Browser implementations, this
					* case is detected and the string is changed to be a proper url string.
					*/
					if (url1.indexOf(":/") == -1 && url1.indexOf(":\\") != -1) { //$NON-NLS-1$ //$NON-NLS-2$
						TCHAR filePath1 = new TCHAR(0, url1, true);
						TCHAR urlResult1 = new TCHAR(0, OS.INTERNET_MAX_URL_LENGTH);
						int[] size1 = new int[] {urlResult1.length()};
						if (OS.UrlCreateFromPath(filePath1, urlResult1, size1, 0) == COM.S_OK) {
							url1 = urlResult1.toString(0, size1[0]);
						} else {
							url1 = PROTOCOL_FILE + url1.replace('\\', '/');
						}
					}

					/* Disallow local file system accesses if the browser content is untrusted */
					if (url1.startsWith(PROTOCOL_FILE) && _getUrl().startsWith(ABOUT_BLANK) && untrustedText) {
						Variant cancel2 = event.arguments[6];
						if (cancel2 != null) {
							long pCancel2 = cancel2.getByRef();
							OS.MoveMemory(pCancel2, new short[] {OS.VARIANT_TRUE}, 2);
						}
						break;
					}

					LocationEvent newEvent1 = new LocationEvent(browser);
					newEvent1.display = browser.getDisplay();
					newEvent1.widget = browser;
					newEvent1.location = url1;
					newEvent1.doit = true;
					for (LocationListener locationListener : locationListeners) {
						locationListener.changing(newEvent1);
					}
					boolean doit1 = newEvent1.doit && !browser.isDisposed();
					Variant cancel3 = event.arguments[6];
					if (cancel3 != null) {
						long pCancel3 = cancel3.getByRef();
						OS.MoveMemory(pCancel3, new short[] {doit1 ? OS.VARIANT_FALSE : OS.VARIANT_TRUE}, 2);
					}
					if (doit1) {
						varResult1 = event.arguments[0];
						IDispatch dispatch1 = varResult1.getDispatch();
						Variant variant1 = new Variant(auto); /* does not need to be disposed */
						IDispatch top1 = variant1.getDispatch();
						if (top1.getAddress() == dispatch1.getAddress()) {
							setAboutBlank(url1.startsWith(ABOUT_BLANK));
						}
					}
					break;
				}
				case CommandStateChange: {
					boolean enabled = false;
					Variant varResult2 = event.arguments[0];
					int command = varResult2.getInt();
					varResult2 = event.arguments[1];
					enabled = varResult2.getBoolean();
					switch (command) {
						case CSC_NAVIGATEBACK : back = enabled; break;
						case CSC_NAVIGATEFORWARD : forward = enabled; break;
					}
					break;
				}
				case DocumentComplete: {
					if (performingInitialNavigate) {
						/* this event marks the completion of the initial navigate to about:blank */
						performingInitialNavigate = false;

						/* if browser content has been provided by the client then set it now */
						if (pendingText != null) {
							setText((String)pendingText[0], ((Boolean)pendingText[1]).booleanValue());
						} else if (pendingUrl != null) {
							setUrl((String)pendingUrl[0], (String)pendingUrl[1], (String[])pendingUrl[2]);
						}
						pendingText = pendingUrl = null;
						break;
					}

					Variant varResult3 = event.arguments[0];
					IDispatch dispatch2 = varResult3.getDispatch();

					varResult3 = event.arguments[1];
					String url2 = varResult3.getString();
					/*
					* Feature in IE.  For navigations on the local machine, DocumentComplete's url
					* field contains a string representation of the file path in a non-URL format.
					* In order to be consistent with the other Browser implementations, this
					* case is detected and the string is changed to be a proper url string.
					*/
					if (url2.indexOf(":/") == -1 && url2.indexOf(":\\") != -1) { //$NON-NLS-1$ //$NON-NLS-2$
						TCHAR filePath2 = new TCHAR(0, url2, true);
						TCHAR urlResult2 = new TCHAR(0, OS.INTERNET_MAX_URL_LENGTH);
						int[] size2 = new int[] {urlResult2.length()};
						if (OS.UrlCreateFromPath(filePath2, urlResult2, size2, 0) == COM.S_OK) {
							url2 = urlResult2.toString(0, size2[0]);
						} else {
							url2 = PROTOCOL_FILE + url2.replace('\\', '/');
						}
					}
					if (html != null && url2.equals(ABOUT_BLANK)) {
						if (delaySetText) {
							delaySetText = false;
							browser.getDisplay().asyncExec(() -> {
								if (browser.isDisposed() || html == null) return;
								setHTML(html);
								html = null;
							});
						} else {
							setHTML(html);
							html = null;
						}
					} else {
						Variant variant2 = new Variant(auto); /* does not need to be disposed */
						IDispatch top2 = variant2.getDispatch();
						LocationEvent locationEvent = new LocationEvent(browser);
						locationEvent.display = browser.getDisplay();
						locationEvent.widget = browser;
						locationEvent.location = url2;
						locationEvent.top = top2.getAddress() == dispatch2.getAddress();
						for (LocationListener locationListener : locationListeners) {
							locationListener.changed(locationEvent);
						}
						if (browser.isDisposed()) return;

						/*
						* With the IBM 64-bit JVM an unexpected document complete event occurs before
						* the native browser's DOM has been built. Filter this premature event based
						* on the browser's ready state.
						*/
						int[] rgdispid1 = auto.getIDsOfNames(new String[] { "ReadyState" }); //$NON-NLS-1$
						Variant pVarResult1 = auto.getProperty(rgdispid1[0]);
						if (pVarResult1 != null) {
							int readyState = pVarResult1.getInt();
							pVarResult1.dispose ();
							if (readyState != READYSTATE_COMPLETE) {
								break;
							}
						}

						/*
						 * Note.  The completion of the page loading is detected as
						 * described in the MSDN article "Determine when a page is
						 * done loading in WebBrowser Control".
						 */
						if (globalDispatch != 0 && dispatch2.getAddress() == globalDispatch) {
							/* final document complete */
							globalDispatch = 0;

							/* re-install registered functions iff needed */
							IE ie = (IE)browser.webBrowser;
							if (ie.installFunctionsOnDocumentComplete) {
								ie.installFunctionsOnDocumentComplete = false;
								Iterator<BrowserFunction> elements1 = functions.values().iterator ();
								while (elements1.hasNext ()) {
									BrowserFunction function1 = elements1.next ();
									execute (function1.functionString);
								}
							}

							ProgressEvent progressEvent1 = new ProgressEvent(browser);
							progressEvent1.display = browser.getDisplay();
							progressEvent1.widget = browser;
							for (ProgressListener progressListener : progressListeners) {
								progressListener.completed(progressEvent1);
							}
						}
					}
					break;
				}
				case DownloadComplete: {
					/*
					* IE feature.  Some events that swt relies on are not sent when
					* a page is refreshed (as opposed to being navigated to).  The
					* workaround is to use DownloadComplete as an opportunity to
					* do this work.
					*/

					Iterator<BrowserFunction> elements2 = functions.values().iterator ();
					while (elements2.hasNext ()) {
						BrowserFunction function2 = elements2.next ();
						execute (function2.functionString);
					}

					if (!isRefresh) break;
					isRefresh = false;

					/*
					* DocumentComplete is not received for refreshes, but clients may rely
					* on this event for tasks like hooking javascript listeners, so send the
					* event here.
					*/
					ProgressEvent progressEvent2 = new ProgressEvent(browser);
					progressEvent2.display = browser.getDisplay();
					progressEvent2.widget = browser;
					for (ProgressListener progressListener : progressListeners) {
						progressListener.completed(progressEvent2);
					}

					break;
				}
				case NavigateComplete2: {
					jsEnabled = jsEnabledOnNextPage;

					Variant varResult4 = event.arguments[1];
					String url3 = varResult4.getString();
					if (!performingInitialNavigate) {
						varResult4 = event.arguments[0];
						IDispatch dispatch3 = varResult4.getDispatch();
						Variant variant3 = new Variant(auto); /* does not need to be disposed */
						IDispatch top3 = variant3.getDispatch();
						if (top3.getAddress() == dispatch3.getAddress()) {
							setAboutBlank(url3.startsWith(ABOUT_BLANK));
							lastNavigateURL = url3;
						}
					}

					/*
					* Bug in Acrobat Reader.  Opening > MAX_PDF PDF files causes Acrobat to not
					* clean up its shells properly when the container Browser is disposed.
					* This results in Eclipse crashing at shutdown time because the leftover
					* shells have invalid references to unloaded Acrobat libraries.  The
					* workaround is to not unload the Acrobat libraries if > MAX_PDF PDF
					* files have been opened.
					*/
					boolean isPDF = false;
					String path = null;
					try {
						path = new URL(url3).getPath();
					} catch (MalformedURLException e) {
					}
					if (path != null) {
						int extensionIndex = path.lastIndexOf('.');
						if (extensionIndex != -1) {
							String extension = path.substring(extensionIndex);
							if (extension.equalsIgnoreCase(EXTENSION_PDF)) {
								isPDF = true;
								PDFCount++;
								if (PDFCount > MAX_PDF) {
									COM.FreeUnusedLibraries = false;
								}
							}
						}
					}

					if (uncRedirect != null) {
						if (uncRedirect.equals(url3)) {
							/* full UNC path has been successfully navigated */
							uncRedirect = null;
							break;
						}
						if (uncRedirect.startsWith(url3)) {
							/*
							* UNC first segment has been successfully navigated,
							* now redirect to the full UNC path.
							*/
							navigate(uncRedirect, null, null, true);
							break;
						}
						uncRedirect = null;
					}

					varResult4 = event.arguments[0];
					IDispatch dispatch4 = varResult4.getDispatch();
					if (globalDispatch == 0) globalDispatch = dispatch4.getAddress();

					OleAutomation webBrowser = varResult4.getAutomation();
					Variant variant4 = new Variant(auto); /* does not need to be disposed */
					IDispatch top4 = variant4.getDispatch();
					boolean isTop = top4.getAddress() == dispatch4.getAddress();
					if (isTop) {
						/* unhook DOM listeners and unref the last document(s) */
						unhookDOMListeners(documents);
						for (OleAutomation document : documents) {
							document.dispose();
						}
						documents = new OleAutomation[0];

						/* re-install registered functions */
						Iterator<BrowserFunction> elements3 = functions.values().iterator ();
						while (elements3.hasNext ()) {
							BrowserFunction function3 = elements3.next ();
							execute (function3.functionString);
						}
					}
					if (!isPDF) {
						hookDOMListeners(webBrowser, isTop);
					}
					webBrowser.dispose();
					break;
				}
				case NavigateError: {
					if (uncRedirect != null) {
						/*
						* This is the second error attempting to reach this UNC path, so
						* it does not exist.  Don't override the default error handling.
						*/
						uncRedirect = null;
						break;
					}
					Variant varResult5 = event.arguments[1];
					final String url4 = varResult5.getString();
					if (url4.startsWith("\\\\")) { //$NON-NLS-1$
						varResult5 = event.arguments[3];
						int statusCode = varResult5.getInt();
						if (statusCode == INET_E_RESOURCE_NOT_FOUND) {
							int index = url4.indexOf('\\', 2);
							if (index != -1) {
								final String host = url4.substring(0, index);
								Variant cancel4 = event.arguments[4];
								if (cancel4 != null) {
									long pCancel4 = cancel4.getByRef();
									OS.MoveMemory(pCancel4, new short[] {OS.VARIANT_TRUE}, 2);
								}
								browser.getDisplay().asyncExec(() -> {
									if (browser.isDisposed()) return;
									/*
									* Feature of IE.  When a UNC path ends with a '\' character IE
									* drops this character when providing the path as an argument
									* to some IE listeners.  Remove this character here too in
									* order to match these other listener argument values.
									*/
									if (url4.endsWith("\\")) { //$NON-NLS-1$
										uncRedirect = url4.substring(0, url4.length() - 1);
									} else {
										uncRedirect = url4;
									}
									navigate(host, null, null, true);
								});
							}
						}
					}
					break;
				}
				case NewWindow2: {
					Variant cancel5 = event.arguments[1];
					long pCancel5 = cancel5.getByRef();
					WindowEvent newEvent2 = new WindowEvent(browser);
					newEvent2.display = browser.getDisplay();
					newEvent2.widget = browser;
					newEvent2.required = false;
					for (OpenWindowListener openWindowListener : openWindowListeners) {
						openWindowListener.open(newEvent2);
					}
					IE browser = null;
					if (newEvent2.browser != null && newEvent2.browser.webBrowser instanceof IE) {
						browser = (IE)newEvent2.browser.webBrowser;
					}
					boolean doit2 = browser != null && !browser.browser.isDisposed();
					if (doit2) {
						/*
						* When a Browser is opened in a new window, BrowserFunctions that are
						* installed in it in the NavigateComplete2 callback are not retained
						* through the loading of the page.  The workaround is to re-install
						* the functions when DocumentComplete is received.
						*/
						browser.installFunctionsOnDocumentComplete = true;

						Variant variant5 = new Variant(browser.auto); /* does not need to be disposed */
						IDispatch iDispatch = variant5.getDispatch();
						Variant ppDisp = event.arguments[0];
						long byref = ppDisp.getByRef();
						if (byref != 0) OS.MoveMemory(byref, new long[] {iDispatch.getAddress()}, C.PTR_SIZEOF);
					}
					if (newEvent2.required) {
						OS.MoveMemory(pCancel5, new short[]{doit2 ? OS.VARIANT_FALSE : OS.VARIANT_TRUE}, 2);
					}
					break;
				}
				case OnMenuBar: {
					Variant arg01 = event.arguments[0];
					menuBar = arg01.getBoolean();
					break;
				}
				case OnStatusBar: {
					Variant arg02 = event.arguments[0];
					statusBar = arg02.getBoolean();
					break;
				}
				case OnToolBar: {
					Variant arg03 = event.arguments[0];
					toolBar = arg03.getBoolean();
					/*
					* Feature in Internet Explorer.  OnToolBar FALSE is emitted
					* when both tool bar, address bar and menu bar must not be visible.
					* OnToolBar TRUE is emitted when either of tool bar, address bar
					* or menu bar is visible.
					*/
					if (!toolBar) {
						addressBar = false;
						menuBar = false;
					}
					break;
				}
				case OnVisible: {
					Variant arg11 = event.arguments[0];
					boolean visible = arg11.getBoolean();
					WindowEvent newEvent3 = new WindowEvent(browser);
					newEvent3.display = browser.getDisplay();
					newEvent3.widget = browser;
					if (visible) {
						if (addressBar) {
							/*
							* Bug in Internet Explorer.  There is no distinct notification for
							* the address bar.  If neither address, menu or tool bars are visible,
							* OnToolBar FALSE is emitted. For some reason, querying the value of
							* AddressBar in this case returns true even though it should not be
							* set visible.  The workaround is to only query the value of AddressBar
							* when OnToolBar FALSE has not been emitted.
							*/
							int[] rgdispid2 = auto.getIDsOfNames(new String[] { "AddressBar" }); //$NON-NLS-1$
							Variant pVarResult2 = auto.getProperty(rgdispid2[0]);
							if (pVarResult2 != null) {
								if (pVarResult2.getType () == OLE.VT_BOOL) {
									addressBar = pVarResult2.getBoolean ();
								}
								pVarResult2.dispose ();
							}
						}
						newEvent3.addressBar = addressBar;
						newEvent3.menuBar = menuBar;
						newEvent3.statusBar = statusBar;
						newEvent3.toolBar = toolBar;
						newEvent3.location = location;
						newEvent3.size = size;
						for (VisibilityWindowListener visibilityWindowListener : visibilityWindowListeners) {
							visibilityWindowListener.show(newEvent3);
						}
						location = null;
						size = null;
					} else {
						for (VisibilityWindowListener visibilityWindowListener : visibilityWindowListeners) {
							visibilityWindowListener.hide(newEvent3);
						}
					}
					break;
				}
				case ProgressChange: {
					/* don't send client events if the initial navigate to about:blank has not completed */
					if (performingInitialNavigate) break;

					Variant arg12 = event.arguments[0];
					int nProgress = arg12.getType() != OLE.VT_I4 ? 0 : arg12.getInt(); // may be -1
					Variant arg2 = event.arguments[1];
					int nProgressMax = arg2.getType() != OLE.VT_I4 ? 0 : arg2.getInt();
					ProgressEvent newEvent4 = new ProgressEvent(browser);
					newEvent4.display = browser.getDisplay();
					newEvent4.widget = browser;
					newEvent4.current = nProgress;
					newEvent4.total = nProgressMax;
					if (nProgress != -1) {
						for (ProgressListener progressListener : progressListeners) {
							progressListener.changed(newEvent4);
						}
					}
					break;
				}
				case StatusTextChange: {
					/* don't send client events if the initial navigate to about:blank has not completed */
					if (performingInitialNavigate) break;

					Variant arg13 = event.arguments[0];
					if (arg13.getType() == OLE.VT_BSTR) {
						String text = arg13.getString();
						StatusTextEvent newEvent5 = new StatusTextEvent(browser);
						newEvent5.display = browser.getDisplay();
						newEvent5.widget = browser;
						newEvent5.text = text;
						for (StatusTextListener statusTextListener : statusTextListeners) {
							statusTextListener.changed(newEvent5);
						}
					}
					break;
				}
				case TitleChange: {
					/* don't send client events if the initial navigate to about:blank has not completed */
					if (performingInitialNavigate) break;

					Variant arg14 = event.arguments[0];
					if (arg14.getType() == OLE.VT_BSTR) {
						String title = arg14.getString();
						TitleEvent newEvent6 = new TitleEvent(browser);
						newEvent6.display = browser.getDisplay();
						newEvent6.widget = browser;
						newEvent6.title = title;
						for (TitleListener titleListener : titleListeners) {
							titleListener.changed(newEvent6);
						}
					}
					break;
				}
				case WindowClosing: {
					/*
					* Disposing the Browser directly from this callback will crash if the
					* Browser has a text field with an active caret.  As a workaround fire
					* the Close event and dispose the Browser in an async block.
					*/
					browser.getDisplay().asyncExec(() -> {
						if (browser.isDisposed()) return;
						WindowEvent newEvent = new WindowEvent(browser);
						newEvent.display = browser.getDisplay();
						newEvent.widget = browser;
						for (CloseWindowListener closeWindowListener : closeWindowListeners) {
							closeWindowListener.close(newEvent);
						}
						browser.dispose();
					});
					Variant cancel6 = event.arguments[1];
					long pCancel6 = cancel6.getByRef();
					Variant arg15 = event.arguments[0];
					boolean isChildWindow = arg15.getBoolean();
					OS.MoveMemory(pCancel6, new short[]{isChildWindow ? OS.VARIANT_FALSE : OS.VARIANT_TRUE}, 2);
					break;
				}
				case WindowSetHeight: {
					if (size == null) size = new Point(0, 0);
					Variant arg16 = event.arguments[0];
					size.y = arg16.getInt();
					break;
				}
				case WindowSetLeft: {
					if (location == null) location = new Point(0, 0);
					Variant arg17 = event.arguments[0];
					location.x = arg17.getInt();
					break;
				}
				case WindowSetTop: {
					if (location == null) location = new Point(0, 0);
					Variant arg18 = event.arguments[0];
					location.y = arg18.getInt();
					break;
				}
				case WindowSetWidth: {
					if (size == null) size = new Point(0, 0);
					Variant arg19 = event.arguments[0];
					size.x = arg19.getInt();
					break;
				}
			}
		}
	};
	site.addEventListener(BeforeNavigate2, oleListener);
	site.addEventListener(CommandStateChange, oleListener);
	site.addEventListener(DocumentComplete, oleListener);
	site.addEventListener(DownloadComplete, oleListener);
	site.addEventListener(NavigateComplete2, oleListener);
	site.addEventListener(NavigateError, oleListener);
	site.addEventListener(NewWindow2, oleListener);
	site.addEventListener(OnMenuBar, oleListener);
	site.addEventListener(OnStatusBar, oleListener);
	site.addEventListener(OnToolBar, oleListener);
	site.addEventListener(OnVisible, oleListener);
	site.addEventListener(ProgressChange, oleListener);
	site.addEventListener(StatusTextChange, oleListener);
	site.addEventListener(TitleChange, oleListener);
	site.addEventListener(WindowClosing, oleListener);
	site.addEventListener(WindowSetHeight, oleListener);
	site.addEventListener(WindowSetLeft, oleListener);
	site.addEventListener(WindowSetTop, oleListener);
	site.addEventListener(WindowSetWidth, oleListener);

	Variant variant = new Variant(true);
	auto.setProperty(RegisterAsBrowser, variant);
	variant.dispose();

	variant = new Variant(false);
	int[] rgdispid = auto.getIDsOfNames(new String[] {"RegisterAsDropTarget"}); //$NON-NLS-1$
	if (rgdispid != null) auto.setProperty(rgdispid[0], variant);
	variant.dispose();
}

@Override
public boolean back() {
	if (!back) return false;
	int[] rgdispid = auto.getIDsOfNames(new String[] { "GoBack" }); //$NON-NLS-1$
	Variant pVarResult = auto.invoke(rgdispid[0]);
	return pVarResult != null && pVarResult.getType() == OLE.VT_EMPTY;
}

@Override
public boolean close() {
	boolean result = true;
	int[] rgdispid = auto.getIDsOfNames(new String[] {PROPERTY_DOCUMENT});
	int dispIdMember = rgdispid[0];
	Variant pVarResult = auto.getProperty(dispIdMember);
	if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY) {
		if (pVarResult != null) pVarResult.dispose();
	} else {
		OleAutomation document = pVarResult.getAutomation();
		pVarResult.dispose();
		rgdispid = document.getIDsOfNames(new String[]{"parentWindow"}); //$NON-NLS-1$
		/* rgdispid != null implies HTML content */
		if (rgdispid != null) {
			dispIdMember = rgdispid[0];
			pVarResult = document.getProperty(dispIdMember);
			if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY) {
				if (pVarResult != null) pVarResult.dispose();
			} else {
				OleAutomation window = pVarResult.getAutomation();
				pVarResult.dispose();
				rgdispid = window.getIDsOfNames(new String[]{"location"}); //$NON-NLS-1$
				dispIdMember = rgdispid[0];
				pVarResult = window.getProperty(dispIdMember);
				if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY) {
					if (pVarResult != null) pVarResult.dispose();
				} else {
					OleAutomation location = pVarResult.getAutomation();
					pVarResult.dispose();
					LocationListener[] oldListeners = locationListeners;
					locationListeners = new LocationListener[0];
					rgdispid = location.getIDsOfNames(new String[]{"replace"}); //$NON-NLS-1$
					dispIdMember = rgdispid[0];
					Variant[] args = new Variant[] {new Variant("about:blank")}; //$NON-NLS-1$
					pVarResult = location.invoke(dispIdMember, args);
					if (pVarResult == null) {
						/* cancelled by user */
						result = false;
					} else {
						pVarResult.dispose();
					}
					args[0].dispose();
					locationListeners = oldListeners;
					location.dispose();
				}
				window.dispose();
			}
		}
		document.dispose();
	}
	return result;
}

static Variant createSafeArray(String string) {
	/* Create a pointer and copy the data into it */
	byte[] bytes = string.getBytes();
	int length = bytes.length;
	long pvData = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, length);
	C.memmove(pvData, bytes, length);
	int cElements1 = length;

	/* Create a SAFEARRAY in memory */
	long pSafeArray = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, SAFEARRAY.sizeof);
	SAFEARRAY safeArray = new SAFEARRAY();
	safeArray.cDims = 1;
	safeArray.fFeatures = OS.FADF_FIXEDSIZE;
	safeArray.cbElements = 1;
	safeArray.pvData = pvData;
	SAFEARRAYBOUND safeArrayBound = new SAFEARRAYBOUND();
	safeArray.rgsabound = safeArrayBound;
	safeArrayBound.cElements = cElements1;
	OS.MoveMemory (pSafeArray, safeArray, SAFEARRAY.sizeof);

	/* Return a Variant that holds the SAFEARRAY */
	long pVariant = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, Variant.sizeof);
	short vt = (short)(OLE.VT_ARRAY | OLE.VT_UI1);
	OS.MoveMemory(pVariant, new short[] {vt}, 2);
	OS.MoveMemory(pVariant + 8, new long[] {pSafeArray}, C.PTR_SIZEOF);
	return new Variant(pVariant, (short)(OLE.VT_BYREF | OLE.VT_VARIANT));
}

@Override
public boolean execute(String script) {
	/*
	 * Issue with IE: If the browser has not shown any content yet then
	 * first navigate to about:blank to work around bug 465822, then execute
	 * the requested script.
	 */
	if (!performingInitialNavigate && _getUrl().length() == 0) {
		performingInitialNavigate = true;
		navigate (ABOUT_BLANK, null, null, true);
	}

	/* get IHTMLDocument2 */
	int[] rgdispid = auto.getIDsOfNames(new String[] {PROPERTY_DOCUMENT});
	int dispIdMember = rgdispid[0];
	Variant pVarResult = auto.getProperty(dispIdMember);
	if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY) {
		if (pVarResult != null) pVarResult.dispose ();
		return false;
	}
	OleAutomation document = pVarResult.getAutomation();
	pVarResult.dispose();

	/* get IHTMLWindow2 */
	rgdispid = document.getIDsOfNames(new String[]{"parentWindow"}); //$NON-NLS-1$
	if (rgdispid == null) {
		/* implies that browser's content is not a IHTMLDocument2 (eg.- acrobat reader) */
		document.dispose();
		return false;
	}
	dispIdMember = rgdispid[0];
	pVarResult = document.getProperty(dispIdMember);
	if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY) {
		if (pVarResult != null) pVarResult.dispose ();
		document.dispose();
		return false;
	}
	OleAutomation ihtmlWindow2 = pVarResult.getAutomation();
	pVarResult.dispose();
	document.dispose();

	rgdispid = ihtmlWindow2.getIDsOfNames(new String[] { "execScript", "code" }); //$NON-NLS-1$  //$NON-NLS-2$
	if (rgdispid == null) {
		ihtmlWindow2.dispose();
		return false;
	}
	Variant[] rgvarg = new Variant[1];
	rgvarg[0] = new Variant(script);
	int[] rgdispidNamedArgs = new int[1];
	rgdispidNamedArgs[0] = rgdispid[1];
	pVarResult = ihtmlWindow2.invoke(rgdispid[0], rgvarg, rgdispidNamedArgs);
	rgvarg[0].dispose();
	ihtmlWindow2.dispose();
	if (pVarResult == null) return false;
	pVarResult.dispose();
	return true;
}

@Override
public boolean forward() {
	if (!forward) return false;
	int[] rgdispid = auto.getIDsOfNames(new String[] { "GoForward" }); //$NON-NLS-1$
	Variant pVarResult = auto.invoke(rgdispid[0]);
	return pVarResult != null && pVarResult.getType() == OLE.VT_EMPTY;
}

@Override
public String getBrowserType () {
	return "ie"; //$NON-NLS-1$
}

@Override
String getDeleteFunctionString (String functionName) {
	return "window." + functionName + "=undefined"; //$NON-NLS-1$ //$NON-NLS-2$
}

@Override
public String getText() {
	/* get the document object */
	int[] rgdispid = auto.getIDsOfNames(new String[] {PROPERTY_DOCUMENT});
	Variant pVarResult = auto.getProperty(rgdispid[0]);
	if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY) {
		if (pVarResult != null) pVarResult.dispose ();
		return ""; //$NON-NLS-1$
	}
	OleAutomation document = pVarResult.getAutomation();
	pVarResult.dispose();

	/* get the html object */
	rgdispid = document.getIDsOfNames(new String[] {"documentElement"}); //$NON-NLS-1$
	if (rgdispid == null) {
		/* implies that the browser is displaying non-HTML content */
		document.dispose();
		return ""; //$NON-NLS-1$
	}
	pVarResult = document.getProperty(rgdispid[0]);
	document.dispose();
	if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY || pVarResult.getType() == COM.VT_NULL) {
		if (pVarResult != null) pVarResult.dispose ();
		return ""; //$NON-NLS-1$
	}
	OleAutomation element = pVarResult.getAutomation();
	pVarResult.dispose();

	/* get its outerHTML property */
	rgdispid = element.getIDsOfNames(new String[] {"outerHTML"}); //$NON-NLS-1$
	pVarResult = element.getProperty(rgdispid[0]);
	element.dispose();
	if (pVarResult == null || pVarResult.getType() == COM.VT_EMPTY) {
		if (pVarResult != null) pVarResult.dispose ();
		return ""; //$NON-NLS-1$
	}
	String result = pVarResult.getString();
	pVarResult.dispose();

	return result;
}

@Override
public String getUrl() {
	/*
	 * If the url is "" then return ABOUT_BLANK in order to be consistent
	 * with the other Browser implementations which auto-navigate to ABOUT_BLANK
	 * when opened.
	 */
	String result = _getUrl();
	return result.length() != 0 ? result : ABOUT_BLANK;
}

String _getUrl() {
	int[] rgdispid = auto.getIDsOfNames(new String[] { "LocationURL" }); //$NON-NLS-1$
	Variant pVarResult = auto.getProperty(rgdispid[0]);
	if (pVarResult == null || pVarResult.getType() != OLE.VT_BSTR) return ""; //$NON-NLS-1$
	String result = pVarResult.getString();
	pVarResult.dispose();
	return result;
}

@Override
public boolean isBackEnabled() {
	return back;
}

@Override
public boolean isForwardEnabled() {
	return forward;
}

@Override
public boolean isFocusControl () {
	return site.isFocusControl() || frame.isFocusControl();
}

boolean navigate(String url, String postData, String headers[], boolean silent) {
	int count = 1;
	if (postData != null) count++;
	if (headers != null) count++;
	Variant[] rgvarg = new Variant[count];
	int[] rgdispidNamedArgs = new int[count];
	int[] rgdispid = auto.getIDsOfNames(new String[] { "Navigate", "URL", "PostData", "Headers" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	int index = 0;
	rgvarg[index] = new Variant(url);
	rgdispidNamedArgs[index++] = rgdispid[1];
	if (postData != null) {
		rgvarg[index] = createSafeArray(postData);
		rgdispidNamedArgs[index++] = rgdispid[2];
	}
	if (headers != null) {
		StringBuilder buffer = new StringBuilder();
		for (String current : headers) {
			if (current != null) {
				int sep = current.indexOf(':');
				if (sep != -1) {
					String key = current.substring(0, sep).trim();
					String value = current.substring(sep + 1).trim();
					if (key.length() > 0 && value.length() > 0) {
						buffer.append(key);
						buffer.append(':');
						buffer.append(value);
						buffer.append("\r\n");
					}
				}
			}
		}
		rgvarg[index] = new Variant(buffer.toString());
		rgdispidNamedArgs[index++] = rgdispid[3];
	}
	boolean oldValue = false;
	if (silent && IEVersion >= 7) {
		int hResult = OS.CoInternetIsFeatureEnabled(OS.FEATURE_DISABLE_NAVIGATION_SOUNDS, OS.GET_FEATURE_FROM_PROCESS);
		oldValue = hResult == COM.S_OK;
		OS.CoInternetSetFeatureEnabled(OS.FEATURE_DISABLE_NAVIGATION_SOUNDS, OS.SET_FEATURE_ON_PROCESS, true);
	}
	Variant pVarResult = auto.invoke(rgdispid[0], rgvarg, rgdispidNamedArgs);
	if (silent && IEVersion >= 7) {
		OS.CoInternetSetFeatureEnabled(OS.FEATURE_DISABLE_NAVIGATION_SOUNDS, OS.SET_FEATURE_ON_PROCESS, oldValue);
	}
	for (int i = 0; i < count; i++) {
		rgvarg[i].dispose();
	}
	if (pVarResult == null) return false;
	boolean result = pVarResult.getType() == OLE.VT_EMPTY;
	pVarResult.dispose();
	return result;
}

@Override
public void refresh() {
	uncRedirect = null;

	/*
	* Bug in Acrobat Reader.  Opening > MAX_PDF PDF files causes Acrobat to not
	* clean up its shells properly when the container Browser is disposed.
	* This results in Eclipse crashing at shutdown time because the leftover
	* shells have invalid references to unloaded Acrobat libraries.  The
	* workaround is to not unload the Acrobat libraries if > MAX_PDF PDF
	* files have been opened.
	*/
	String url = _getUrl();
	int extensionIndex = url.lastIndexOf('.');
	if (extensionIndex != -1) {
		String extension = url.substring(extensionIndex);
		if (extension.equalsIgnoreCase (EXTENSION_PDF)) {
			PDFCount++;
			if (PDFCount > MAX_PDF) {
				COM.FreeUnusedLibraries = false;
			}
		}
	}

	isRefresh = true;
	int[] rgdispid = auto.getIDsOfNames(new String[] { "Refresh" }); //$NON-NLS-1$
	auto.invoke(rgdispid[0]);
}

void setHTML (String string) {
	int charCount = string.length();
	char[] chars = new char[charCount];
	string.getChars(0, charCount, chars, 0);
	int byteCount = OS.WideCharToMultiByte(OS.CP_UTF8, 0, chars, charCount, null, 0, null, null);
	/*
	* Internet Explorer appears to treat the data loaded with
	* nsIPersistStreamInit.Load as if it were encoded using the default
	* local charset.  There does not seem to be an API to set the
	* desired charset explicitly in this case.  The fix is to
	* prepend the UTF-8 Byte Order Mark signature to the data.
	*/
	byte[] UTF8BOM = {(byte)0xEF, (byte)0xBB, (byte)0xBF};
	long hGlobal = OS.GlobalAlloc(OS.GMEM_FIXED | OS.GMEM_ZEROINIT, UTF8BOM.length + byteCount);
	if (hGlobal != 0) {
		OS.MoveMemory(hGlobal, UTF8BOM, UTF8BOM.length);
		OS.WideCharToMultiByte(OS.CP_UTF8, 0, chars, charCount, hGlobal + UTF8BOM.length, byteCount, null, null);
		long [] ppstm = new long [1];
		/*
		* CreateStreamOnHGlobal is called with the flag fDeleteOnRelease.
		* If the call succeeds the buffer hGlobal is freed automatically
		* when the IStream object is released. If the call fails, free the
		* buffer hGlobal.
		*/
		if (OS.CreateStreamOnHGlobal(hGlobal, true, ppstm) == OS.S_OK) {
			int[] rgdispid = auto.getIDsOfNames(new String[] {PROPERTY_DOCUMENT});
			Variant pVarResult = auto.getProperty(rgdispid[0]);
			IDispatch dispatchDocument = pVarResult.getDispatch();
			long [] ppvObject = new long [1];
			int result = dispatchDocument.QueryInterface(COM.IIDIPersistStreamInit, ppvObject);
			if (result == OS.S_OK) {
				IPersistStreamInit persistStreamInit = new IPersistStreamInit(ppvObject[0]);
				if (persistStreamInit.InitNew() == OS.S_OK) {
					persistStreamInit.Load(ppstm[0]);
				}
				persistStreamInit.Release();
			}
			pVarResult.dispose();
			IUnknown stream = new IUnknown(ppstm[0]);
			stream.Release();
		} else {
			OS.GlobalFree(hGlobal);
		}
	}
}

private void setAboutBlank(boolean value) {
	isAboutBlank = value;
	updateForceTrusted();
}

private void setUntrustedText(boolean value) {
	untrustedText = value;
	updateForceTrusted();
}

private void updateForceTrusted() {
	site.isForceTrusted = isAboutBlank && !untrustedText;
}

@Override
public boolean setText(final String html, boolean trusted) {
	/*
	* If the browser is navigating to about:blank in response to its first
	* setUrl() invocation then delay setting this text content until the
	* navigate has completed.  about:blank will be re-navigated to in order
	* to ensure that all expected client events are sent.
	*/
	if (performingInitialNavigate) {
		pendingText = new Object[] {html, trusted};
		pendingUrl = null;
		return true;
	}

	/*
	* If the html field is non-null then the about:blank page is already being
	* loaded from a previous setText() invocation, so no Stop or Navigate is
	* required.  Just set the html that is to be shown.
	*/
	boolean blankLoading = this.html != null;
	this.html = html;
	setUntrustedText(!trusted);
	if (blankLoading) return true;

	/*
	* Navigate to the blank page and insert the given html when
	* receiving the next DocumentComplete notification.  See the
	* MSDN article "Loading HTML content from a Stream".
	*
	* Note.  Stop any pending request.  This is required to avoid displaying a
	* blank page as a result of consecutive calls to setUrl and/or setText.
	* The previous request would otherwise render the new html content and
	* reset the html field before the browser actually navigates to the blank
	* page as requested below.
	*
	* Feature in Internet Explorer.  Stopping pending requests when no request
	* is pending causes a default page 'Action cancelled' to be displayed.  The
	* workaround is to not invoke 'stop' when no request has been set since
	* that instance was created.
	*/

	/*
	* Stopping the loading of a page causes DocumentComplete events from previous
	* requests to be received before the DocumentComplete for this page.  In such
	* cases we must be sure to not set the html into the browser too soon, since
	* doing so could result in its page being cleared out by a subsequent
	* DocumentComplete.  The Browser's ReadyState can be used to determine whether
	* these extra events will be received or not.
	*/
	if (_getUrl().length() != 0) {
		int[] rgdispid = auto.getIDsOfNames(new String[] { "ReadyState" }); //$NON-NLS-1$
		Variant pVarResult = auto.getProperty(rgdispid[0]);
		if (pVarResult == null) return false;
		delaySetText = pVarResult.getInt() != READYSTATE_COMPLETE;
		pVarResult.dispose();
		rgdispid = auto.getIDsOfNames(new String[] { "Stop" }); //$NON-NLS-1$
		auto.invoke(rgdispid[0]);
	}

	int[] rgdispid = auto.getIDsOfNames(new String[] { "Navigate", "URL" }); //$NON-NLS-1$ //$NON-NLS-2$
	Variant[] rgvarg = new Variant[1];
	rgvarg[0] = new Variant(ABOUT_BLANK);
	int[] rgdispidNamedArgs = new int[1];
	rgdispidNamedArgs[0] = rgdispid[1];
	boolean oldValue = false;
	if (IEVersion >= 7) {
		int hResult = OS.CoInternetIsFeatureEnabled(OS.FEATURE_DISABLE_NAVIGATION_SOUNDS, OS.GET_FEATURE_FROM_PROCESS);
		oldValue = hResult == COM.S_OK;
		OS.CoInternetSetFeatureEnabled(OS.FEATURE_DISABLE_NAVIGATION_SOUNDS, OS.SET_FEATURE_ON_PROCESS, true);
	}
	Variant pVarResult = auto.invoke(rgdispid[0], rgvarg, rgdispidNamedArgs);
	if (IEVersion >= 7) {
		OS.CoInternetSetFeatureEnabled(OS.FEATURE_DISABLE_NAVIGATION_SOUNDS, OS.SET_FEATURE_ON_PROCESS, oldValue);
	}
	rgvarg[0].dispose();
	if (pVarResult == null) return false;
	boolean result = pVarResult.getType() == OLE.VT_EMPTY;
	pVarResult.dispose();
	return result;
}

@Override
public boolean setUrl(String url, String postData, String headers[]) {
	html = uncRedirect = null;

	/*
	* If the browser has not shown any content yet then first navigate to
	* about:blank to work around IE bug http://support.microsoft.com/kb/320153,
	* then navigate to the requested url once about:blank has loaded.
	*/
	if (_getUrl().length() == 0 && !ABOUT_BLANK.equalsIgnoreCase(url)) {
		pendingText = null;
		pendingUrl = new Object[] {url, postData, headers};
		performingInitialNavigate = true;
		navigate (ABOUT_BLANK, null, null, true);
		return true;
	}

	/*
	* Bug in Internet Explorer.  For some reason, Navigating to an xml document before
	* a previous Navigate has completed will leave the Browser in a bad state if the
	* Navigate to the xml document does not complete.  This bad state causes a GP when
	* the parent window is eventually disposed.  The workaround is to issue a Stop before
	* navigating to any xml document.
	*/
	if (url.endsWith(".xml")) {	//$NON-NLS-1$
		int[] rgdispid = auto.getIDsOfNames(new String[] { "Stop" }); //$NON-NLS-1$
		auto.invoke(rgdispid[0]);
	}
	return navigate(url, postData, headers, false);
}

@Override
public void stop() {
	/*
	* If the browser has not completed its initial navigate to about:blank
	* then do not issue Stop here, as this will display the IE error page.
	* Just clear the pending url and text fields so that any pending content
	* will not be set into the browser when the about:blank navigate completes.
	*/
	if (performingInitialNavigate) {
		pendingText = pendingUrl = null;
		return;
	}

	/*
	* Feature of IE.  Invoking Stop in IE before any content has been shown
	* displays a Navigation Cancelled error page.  The workaround is to not
	* invoke Stop if no content has been shown yet.
	*/
	if (_getUrl().length() == 0) return;

	/*
	* Ensure that isAboutBlank is set accurately since Stop can be issued at
	* any stage in the page load cycle.
	*/
	setAboutBlank(getUrl().startsWith(ABOUT_BLANK));

	uncRedirect = null;
	int[] rgdispid = auto.getIDsOfNames(new String[] { "Stop" }); //$NON-NLS-1$
	auto.invoke(rgdispid[0]);
}

@Override
boolean translateMnemonics () {
	return false;
}

void handleDOMEvent (OleEvent e) {
	if (e.arguments == null || e.arguments.length == 0) return; /* for IE5 */

	Variant arg = e.arguments[0];
	OleAutomation event = arg.getAutomation();
	int[] rgdispid = event.getIDsOfNames(new String[]{ PROPERTY_TYPE });
	int dispIdMember = rgdispid[0];
	Variant pVarResult = event.getProperty(dispIdMember);
	String eventType = pVarResult.getString();
	pVarResult.dispose();

	if (eventType.equals(EVENT_KEYDOWN)) {
		rgdispid = event.getIDsOfNames(new String[] { PROPERTY_KEYCODE });
		dispIdMember = rgdispid[0];
		pVarResult = event.getProperty(dispIdMember);
		lastKeyCode = translateKey (pVarResult.getInt());
		pVarResult.dispose();

		rgdispid = event.getIDsOfNames (new String[] {PROPERTY_RETURNVALUE});
		pVarResult = event.getProperty (rgdispid[0]);
		boolean consume = pVarResult != null && pVarResult.getType () == OLE.VT_BOOL && !pVarResult.getBoolean ();
		pVarResult.dispose ();

		MSG msg = new MSG ();
		int flags = OS.PM_NOYIELD | (consume ? OS.PM_REMOVE : OS.PM_NOREMOVE);
		if (OS.PeekMessage (msg, frame.handle, OS.WM_CHAR, OS.WM_CHAR, flags)) {
			/* a keypress will be received for this key so don't send KeyDown here */
			event.dispose();
			return;
		}

		if (consume) {
			/*
			 * an event should not be sent if another listener has vetoed the
			 * KeyDown (this is for non-character cases like arrow keys, etc.)
			 */
			event.dispose();
			return;
		}

		/* if this is a repeating key then an event should not be fired for it */
		rgdispid = event.getIDsOfNames(new String[] { PROPERTY_REPEAT });
		dispIdMember = rgdispid[0];
		pVarResult = event.getProperty(dispIdMember);
		boolean repeating = pVarResult.getBoolean();
		pVarResult.dispose();
		if (repeating) {
			event.dispose();
			return;
		}

		int mask = 0;
		rgdispid = event.getIDsOfNames(new String[] { PROPERTY_ALTKEY });
		dispIdMember = rgdispid[0];
		pVarResult = event.getProperty(dispIdMember);
		if (pVarResult.getBoolean()) mask |= SWT.ALT;
		pVarResult.dispose();

		rgdispid = event.getIDsOfNames(new String[] { PROPERTY_CTRLKEY });
		dispIdMember = rgdispid[0];
		pVarResult = event.getProperty(dispIdMember);
		if (pVarResult.getBoolean()) mask |= SWT.CTRL;
		pVarResult.dispose();

		rgdispid = event.getIDsOfNames(new String[] { PROPERTY_SHIFTKEY });
		dispIdMember = rgdispid[0];
		pVarResult = event.getProperty(dispIdMember);
		if (pVarResult.getBoolean()) mask |= SWT.SHIFT;
		pVarResult.dispose();

		Event keyEvent = new Event ();
		keyEvent.widget = browser;
		keyEvent.type = SWT.KeyDown;
		keyEvent.keyCode = lastKeyCode;
		keyEvent.stateMask = mask;
		keyEvent.stateMask &= ~lastKeyCode;		/* remove current keydown if it's a state key */
		/*
		* keypress events are not received for Backspace, Enter, Delete and
		* Tab, so KeyDown events are sent for them here.  Set the KeyDown
		* event's character field and IE's lastCharCode field for these keys
		* so that the Browser's key events are consistent with other controls.
		*/
		switch (lastKeyCode) {
			case SWT.BS: lastCharCode = keyEvent.character = SWT.BS; break;
			case SWT.CR: lastCharCode = keyEvent.character = SWT.CR; break;
			case SWT.DEL: lastCharCode = keyEvent.character = SWT.DEL; break;
			case SWT.TAB: lastCharCode = keyEvent.character = SWT.TAB; break;
		}

		if (!sendKeyEvent(keyEvent)) {
			rgdispid = event.getIDsOfNames(new String[] { PROPERTY_RETURNVALUE });
			dispIdMember = rgdispid[0];
			Variant pVarFalse = new Variant(false);
			event.setProperty(dispIdMember, pVarFalse);
			pVarFalse.dispose();
		}

		/*
		* Pressing F5 refreshes the current page.  If this is about to happen
		* then set isRefresh to true so that received IE events will be treated
		* accordingly.
		*/
		if (lastKeyCode == SWT.F5) isRefresh = true;

		event.dispose();
		return;
	}

	if (eventType.equals(EVENT_KEYPRESS)) {
		int mask = 0;
		rgdispid = event.getIDsOfNames(new String[] { PROPERTY_CTRLKEY });
		dispIdMember = rgdispid[0];
		pVarResult = event.getProperty(dispIdMember);
		if (pVarResult.getBoolean()) mask |= SWT.CTRL;
		pVarResult.dispose();

		rgdispid = event.getIDsOfNames(new String[] { PROPERTY_SHIFTKEY });
		dispIdMember = rgdispid[0];
		pVarResult = event.getProperty(dispIdMember);
		if (pVarResult.getBoolean()) mask |= SWT.SHIFT;
		pVarResult.dispose();

		rgdispid = event.getIDsOfNames(new String[] { PROPERTY_ALTKEY });
		dispIdMember = rgdispid[0];
		pVarResult = event.getProperty(dispIdMember);
		if (pVarResult.getBoolean()) mask |= SWT.ALT;
		pVarResult.dispose();

		/* in the keypress event the keyCode actually corresponds to the character code */
		rgdispid = event.getIDsOfNames(new String[] { PROPERTY_KEYCODE });
		dispIdMember = rgdispid[0];
		pVarResult = event.getProperty(dispIdMember);
		lastCharCode = pVarResult.getInt();
		pVarResult.dispose();

		/*
		* WebSite.TranslateAccelerator() explicitly does not translate OS.VK_RETURN
		* keys, so the PeekMessage check in the keydown handler always allows a
		* KeyDown to be sent for this key.  However, keydown and keypress events are
		* both sometimes received for OS.VK_RETURN, depending on the page's focus
		* control.  To handle this, do not send a KeyDown for CR or LF here since
		* one is always sent for it from the keydown handler.
		*/
		if (lastCharCode == SWT.CR || lastCharCode == SWT.LF) {
			event.dispose();
			return;
		}

		Event keyEvent = new Event ();
		keyEvent.widget = browser;
		keyEvent.type = SWT.KeyDown;
		keyEvent.keyCode = lastKeyCode;
		keyEvent.character = (char)lastCharCode;
		keyEvent.stateMask = mask;
		if (!sendKeyEvent(keyEvent)) {
			rgdispid = event.getIDsOfNames(new String[] { PROPERTY_RETURNVALUE });
			dispIdMember = rgdispid[0];
			Variant pVarFalse = new Variant(false);
			event.setProperty(dispIdMember, pVarFalse);
			pVarFalse.dispose();
		}

		event.dispose();
		return;
	}

	if (eventType.equals(EVENT_KEYUP)) {
		rgdispid = event.getIDsOfNames(new String[] { PROPERTY_KEYCODE });
		dispIdMember = rgdispid[0];
		pVarResult = event.getProperty(dispIdMember);
		int keyCode = translateKey (pVarResult.getInt());
		pVarResult.dispose();

		/*
		* if a key code could not be determined for this key then it's a
		* key for which key events are not sent (eg.- the Windows key)
		*/
		if (keyCode == 0) {
			lastKeyCode = lastCharCode = 0;
			event.dispose();
			return;
		}

		if (keyCode != lastKeyCode) {
			/* keyup does not correspond to the last keydown */
			lastKeyCode = keyCode;
			lastCharCode = 0;
		}

		int mask = 0;
		rgdispid = event.getIDsOfNames(new String[] { PROPERTY_CTRLKEY });
		dispIdMember = rgdispid[0];
		pVarResult = event.getProperty(dispIdMember);
		if (pVarResult.getBoolean()) mask |= SWT.CTRL;
		pVarResult.dispose();

		rgdispid = event.getIDsOfNames(new String[] { PROPERTY_ALTKEY });
		dispIdMember = rgdispid[0];
		pVarResult = event.getProperty(dispIdMember);
		if (pVarResult.getBoolean()) mask |= SWT.ALT;
		pVarResult.dispose();

		rgdispid = event.getIDsOfNames(new String[] { PROPERTY_SHIFTKEY });
		dispIdMember = rgdispid[0];
		pVarResult = event.getProperty(dispIdMember);
		if (pVarResult.getBoolean()) mask |= SWT.SHIFT;
		pVarResult.dispose();

		Event keyEvent = new Event ();
		keyEvent.widget = browser;
		keyEvent.type = SWT.KeyUp;
		keyEvent.keyCode = lastKeyCode;
		keyEvent.character = (char)lastCharCode;
		keyEvent.stateMask = mask;
		switch (lastKeyCode) {
			case SWT.SHIFT:
			case SWT.CONTROL:
			case SWT.ALT:
			case SWT.COMMAND: {
				keyEvent.stateMask |= lastKeyCode;
			}
		}
		browser.notifyListeners (keyEvent.type, keyEvent);
		if (!keyEvent.doit) {
			rgdispid = event.getIDsOfNames(new String[] { PROPERTY_RETURNVALUE });
			dispIdMember = rgdispid[0];
			Variant pVarFalse = new Variant(false);
			event.setProperty(dispIdMember, pVarFalse);
			pVarFalse.dispose();
		}

		lastKeyCode = lastCharCode = 0;
		event.dispose();
		return;
	}

	/*
	 * Feature in IE. MouseOver/MouseOut events are fired any time the mouse enters
	 * or exits any element within the Browser.  To ensure that SWT events are only
	 * fired for mouse movements into or out of the Browser, do not fire an event if
	 * the element being exited (on MouseOver) or entered (on MouseExit) is within
	 * the Browser.
	 */
	if (eventType.equals(EVENT_MOUSEOVER)) {
		rgdispid = event.getIDsOfNames(new String[] { PROPERTY_FROMELEMENT });
		dispIdMember = rgdispid[0];
		pVarResult = event.getProperty(dispIdMember);
		boolean isInternal = pVarResult.getType() != COM.VT_EMPTY;
		pVarResult.dispose();
		if (isInternal) {
			event.dispose();
			return;
		}
	}
	if (eventType.equals(EVENT_MOUSEOUT)) {
		rgdispid = event.getIDsOfNames(new String[] { PROPERTY_TOELEMENT });
		dispIdMember = rgdispid[0];
		pVarResult = event.getProperty(dispIdMember);
		boolean isInternal = pVarResult.getType() != COM.VT_EMPTY;
		pVarResult.dispose();
		if (isInternal) {
			event.dispose();
			return;
		}
	}

	int mask = 0;
	Event newEvent = new Event();
	newEvent.widget = browser;

	/*
	 * The position of mouse events is received in screen-relative coordinates
	 * in order to handle pages with frames, since frames express their event
	 * coordinates relative to themselves rather than relative to their top-
	 * level page.  Convert screen-relative coordinates to be browser-relative.
	 */
	rgdispid = event.getIDsOfNames(new String[] { PROPERTY_SCREENX });
	dispIdMember = rgdispid[0];
	pVarResult = event.getProperty(dispIdMember);
	int screenX = pVarResult.getInt();
	pVarResult.dispose();

	rgdispid = event.getIDsOfNames(new String[] { PROPERTY_SCREENY });
	dispIdMember = rgdispid[0];
	pVarResult = event.getProperty(dispIdMember);
	int screenY = pVarResult.getInt();
	pVarResult.dispose();

	Point position = DPIUtil.autoScaleDown(new Point(screenX, screenY)); // To Points
	position = browser.getDisplay().map(null, browser, position);
	newEvent.x = position.x; newEvent.y = position.y;

	rgdispid = event.getIDsOfNames(new String[] { PROPERTY_CTRLKEY });
	dispIdMember = rgdispid[0];
	pVarResult = event.getProperty(dispIdMember);
	if (pVarResult.getBoolean()) mask |= SWT.CTRL;
	pVarResult.dispose();

	rgdispid = event.getIDsOfNames(new String[] { PROPERTY_ALTKEY });
	dispIdMember = rgdispid[0];
	pVarResult = event.getProperty(dispIdMember);
	if (pVarResult.getBoolean()) mask |= SWT.ALT;
	pVarResult.dispose();

	rgdispid = event.getIDsOfNames(new String[] { PROPERTY_SHIFTKEY });
	dispIdMember = rgdispid[0];
	pVarResult = event.getProperty(dispIdMember);
	if (pVarResult.getBoolean()) mask |= SWT.SHIFT;
	pVarResult.dispose();

	newEvent.stateMask = mask;

	rgdispid = event.getIDsOfNames(new String[] { PROPERTY_BUTTON });
	dispIdMember = rgdispid[0];
	pVarResult = event.getProperty(dispIdMember);
	int button = pVarResult.getInt();
	pVarResult.dispose();
	switch (button) {
		case 1: button = 1; break;
		case 2: button = 3; break;
		case 4: button = 2; break;
	}

	if (eventType.equals(EVENT_MOUSEDOWN)) {
		newEvent.type = SWT.MouseDown;
		newEvent.button = button;
		newEvent.count = 1;
	} else if (eventType.equals(EVENT_MOUSEUP) || eventType.equals(EVENT_DRAGEND)) {
		newEvent.type = SWT.MouseUp;
		newEvent.button = button != 0 ? button : 1;	/* button assumed to be 1 for dragends */
		newEvent.count = 1;
		switch (newEvent.button) {
			case 1: newEvent.stateMask |= SWT.BUTTON1; break;
			case 2: newEvent.stateMask |= SWT.BUTTON2; break;
			case 3: newEvent.stateMask |= SWT.BUTTON3; break;
			case 4: newEvent.stateMask |= SWT.BUTTON4; break;
			case 5: newEvent.stateMask |= SWT.BUTTON5; break;
		}
	} else if (eventType.equals(EVENT_MOUSEWHEEL)) {
		newEvent.type = SWT.MouseWheel;
		rgdispid = event.getIDsOfNames(new String[] { PROPERTY_WHEELDELTA });
		dispIdMember = rgdispid[0];
		pVarResult = event.getProperty(dispIdMember);
		newEvent.count = pVarResult.getInt () / 120 * 3;
		pVarResult.dispose();
	} else if (eventType.equals(EVENT_MOUSEMOVE)) {
		/*
		* Feature in IE.  Spurious and redundant mousemove events are often received.  The workaround
		* is to not fire MouseMove events whose x and y values match the last MouseMove.
		*/
		if (newEvent.x == lastMouseMoveX && newEvent.y == lastMouseMoveY) {
			event.dispose();
			return;
		}
		newEvent.type = SWT.MouseMove;
		lastMouseMoveX = newEvent.x; lastMouseMoveY = newEvent.y;
	} else if (eventType.equals(EVENT_MOUSEOVER)) {
		newEvent.type = SWT.MouseEnter;
	} else if (eventType.equals(EVENT_MOUSEOUT)) {
		newEvent.type = SWT.MouseExit;
	} else if (eventType.equals(EVENT_DRAGSTART)) {
		newEvent.type = SWT.DragDetect;
		newEvent.button = 1;	/* button assumed to be 1 for dragstarts */
		newEvent.stateMask |= SWT.BUTTON1;
	}

	event.dispose();
	browser.notifyListeners(newEvent.type, newEvent);

	if (eventType.equals(EVENT_DOUBLECLICK)) {
		newEvent = new Event ();
		newEvent.widget = browser;
		newEvent.type = SWT.MouseDoubleClick;
		newEvent.x = position.x; newEvent.y = position.y;
		newEvent.stateMask = mask;
		newEvent.type = SWT.MouseDoubleClick;
		newEvent.button = 1; /* dblclick only comes for button 1 and does not set the button property */
		newEvent.count = 2;
		browser.notifyListeners (newEvent.type, newEvent);
	}
}

void hookDOMListeners(OleAutomation webBrowser, final boolean isTop) {
	int[] rgdispid = webBrowser.getIDsOfNames(new String[] { PROPERTY_DOCUMENT });
	int dispIdMember = rgdispid[0];
	Variant pVarResult = webBrowser.getProperty(dispIdMember);
	if (pVarResult == null) return;
	if (pVarResult.getType() == COM.VT_EMPTY) {
		pVarResult.dispose();
		return;
	}
	final OleAutomation document = pVarResult.getAutomation();
	pVarResult.dispose();

	/*
	 * In some cases, such as setting the Browser's content from a string,
	 * NavigateComplete2 is received multiple times for a top-level document.
	 * For cases like this, any previously-hooked DOM listeners must be
	 * removed from the document before hooking the new set of listeners,
	 * otherwise multiple sets of events will be received.
	 */
	unhookDOMListeners (new OleAutomation[] {document});

	site.addEventListener(document, COM.IIDIHTMLDocumentEvents2, COM.DISPID_HTMLDOCUMENTEVENTS_ONKEYDOWN, domListener);
	site.addEventListener(document, COM.IIDIHTMLDocumentEvents2, COM.DISPID_HTMLDOCUMENTEVENTS_ONKEYPRESS, domListener);
	site.addEventListener(document, COM.IIDIHTMLDocumentEvents2, COM.DISPID_HTMLDOCUMENTEVENTS_ONKEYUP, domListener);
	site.addEventListener(document, COM.IIDIHTMLDocumentEvents2, COM.DISPID_HTMLDOCUMENTEVENTS_ONMOUSEDOWN, domListener);
	site.addEventListener(document, COM.IIDIHTMLDocumentEvents2, COM.DISPID_HTMLDOCUMENTEVENTS_ONMOUSEUP, domListener);
	site.addEventListener(document, COM.IIDIHTMLDocumentEvents2, COM.DISPID_HTMLDOCUMENTEVENTS_ONMOUSEWHEEL, domListener);
	site.addEventListener(document, COM.IIDIHTMLDocumentEvents2, COM.DISPID_HTMLDOCUMENTEVENTS_ONDBLCLICK, domListener);
	site.addEventListener(document, COM.IIDIHTMLDocumentEvents2, COM.DISPID_HTMLDOCUMENTEVENTS_ONMOUSEMOVE, domListener);
	site.addEventListener(document, COM.IIDIHTMLDocumentEvents2, COM.DISPID_HTMLDOCUMENTEVENTS_ONDRAGSTART, domListener);
	site.addEventListener(document, COM.IIDIHTMLDocumentEvents2, COM.DISPID_HTMLDOCUMENTEVENTS_ONDRAGEND, domListener);
	/* ensure that enter/exit are only fired once, by the top-level document */
	if (isTop) {
		site.addEventListener(document, COM.IIDIHTMLDocumentEvents2, COM.DISPID_HTMLDOCUMENTEVENTS_ONMOUSEOVER, domListener);
		site.addEventListener(document, COM.IIDIHTMLDocumentEvents2, COM.DISPID_HTMLDOCUMENTEVENTS_ONMOUSEOUT, domListener);
	}

	OleAutomation[] newDocuments = new OleAutomation[documents.length + 1];
	System.arraycopy(documents, 0, newDocuments, 0, documents.length);
	newDocuments[documents.length] = document;
	documents = newDocuments;
}

void unhookDOMListeners(OleAutomation[] documents) {
	char[] buffer = (COM.IIDIHTMLDocumentEvents2 + '\0').toCharArray();
	GUID guid = new GUID();
	if (COM.IIDFromString(buffer, guid) == COM.S_OK) {
		for (OleAutomation document : documents) {
			site.removeEventListener(document, guid, COM.DISPID_HTMLDOCUMENTEVENTS_ONKEYDOWN, domListener);
			site.removeEventListener(document, guid, COM.DISPID_HTMLDOCUMENTEVENTS_ONKEYPRESS, domListener);
			site.removeEventListener(document, guid, COM.DISPID_HTMLDOCUMENTEVENTS_ONKEYUP, domListener);
			site.removeEventListener(document, guid, COM.DISPID_HTMLDOCUMENTEVENTS_ONMOUSEDOWN, domListener);
			site.removeEventListener(document, guid, COM.DISPID_HTMLDOCUMENTEVENTS_ONMOUSEUP, domListener);
			site.removeEventListener(document, guid, COM.DISPID_HTMLDOCUMENTEVENTS_ONMOUSEWHEEL, domListener);
			site.removeEventListener(document, guid, COM.DISPID_HTMLDOCUMENTEVENTS_ONDBLCLICK, domListener);
			site.removeEventListener(document, guid, COM.DISPID_HTMLDOCUMENTEVENTS_ONMOUSEMOVE, domListener);
			site.removeEventListener(document, guid, COM.DISPID_HTMLDOCUMENTEVENTS_ONDRAGSTART, domListener);
			site.removeEventListener(document, guid, COM.DISPID_HTMLDOCUMENTEVENTS_ONDRAGEND, domListener);
			site.removeEventListener(document, guid, COM.DISPID_HTMLDOCUMENTEVENTS_ONMOUSEOVER, domListener);
			site.removeEventListener(document, guid, COM.DISPID_HTMLDOCUMENTEVENTS_ONMOUSEOUT, domListener);
		}
	}
}
}
