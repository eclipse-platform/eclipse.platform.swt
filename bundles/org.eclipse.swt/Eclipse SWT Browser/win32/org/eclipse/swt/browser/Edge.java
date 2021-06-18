/*******************************************************************************
 * Copyright (c) 2020, 2021 Nikita Nemkin and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Nikita Nemkin <nikita@nemkin.ru> - initial implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import java.net.*;
import java.nio.charset.*;
import java.time.*;
import java.util.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;

class Edge extends WebBrowser {
	static {
		Library.loadLibrary("WebView2Loader");
	}

	// WebView2Loader.dll compatible version. This is NOT the minimal required version.
	static final String SDK_TARGET_VERSION = "89.0.721.0";

	// Display.getData() keys
	static final String APPLOCAL_DIR_KEY = "org.eclipse.swt.internal.win32.appLocalDir";

	// System.getProperty() keys
	static final String BROWSER_DIR_PROP = "org.eclipse.swt.browser.EdgeDir";
	static final String BROWSER_ARGS_PROP = "org.eclipse.swt.browser.EdgeArgs";
	static final String DATA_DIR_PROP = "org.eclipse.swt.browser.EdgeDataDir";
	static final String LANGUAGE_PROP = "org.eclipse.swt.browser.EdgeLanguage";
	static final String VERSIONT_PROP = "org.eclipse.swt.browser.EdgeVersion";

	static String DataDir;
	static ICoreWebView2Environment Environment;
	static ArrayList<Edge> Instances = new ArrayList<>();

	ICoreWebView2 webView;
	ICoreWebView2_2 webView_2;
	ICoreWebView2Controller controller;
	ICoreWebView2Settings settings;
	ICoreWebView2Environment2 environment2;

	static boolean inCallback;
	boolean inNewWindow;
	HashMap<Long, LocationEvent> navigations = new HashMap<>();

	static {
		NativeClearSessions = () -> {
			ICoreWebView2CookieManager manager = getCookieManager();
			if (manager == null) return;

			long[] ppv = new long[1];
			int hr = callAndWait(ppv, completion -> manager.GetCookies(null, completion));
			if (hr != COM.S_OK) error(SWT.ERROR_NO_HANDLES, hr);
			ICoreWebView2CookieList cookieList = new ICoreWebView2CookieList(ppv[0]);

			int[] count = new int[1], isSession = new int[1];
			cookieList.get_Count(count);
			for (int i = 0; i < count[0]; i++) {
				hr = cookieList.GetValueAtIndex(i, ppv);
				if (hr != COM.S_OK) error(SWT.ERROR_NO_HANDLES, hr);
				ICoreWebView2Cookie cookie = new ICoreWebView2Cookie(ppv[0]);
				cookie.get_IsSession(isSession);
				if (isSession[0] != 0) {
					manager.DeleteCookie(cookie);
				}
				cookie.Release();
			}
			cookieList.Release();
			manager.Release();

			// Bug in WebView2. DeleteCookie is asynchronous. Wait a short while for it to take effect.
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		};

		NativeGetCookie = () -> {
			ICoreWebView2CookieManager manager = getCookieManager();
			if (manager == null) return;

			char[] uri = stringToWstr(CookieUrl);
			long[] ppv = new long[1];
			int hr = callAndWait(ppv, completion -> manager.GetCookies(uri, completion));
			if (hr != COM.S_OK) error(SWT.ERROR_NO_HANDLES, hr);
			ICoreWebView2CookieList cookieList = new ICoreWebView2CookieList(ppv[0]);

			int[] count = new int[1];
			cookieList.get_Count(count);
			for (int i = 0; i < count[0]; i++) {
				hr = cookieList.GetValueAtIndex(i, ppv);
				if (hr != COM.S_OK) error(SWT.ERROR_NO_HANDLES, hr);
				ICoreWebView2Cookie cookie = new ICoreWebView2Cookie(ppv[0]);
				cookie.get_Name(ppv);
				String name = wstrToString(ppv[0], true);
				if (CookieName.equals(name)) {
					cookie.get_Value(ppv);
					CookieValue = wstrToString(ppv[0], true);
				}
				cookie.Release();
				if (CookieValue != null) {
					break;
				}
			}
			cookieList.Release();
			manager.Release();
		};

		NativeSetCookie = () -> {
			HttpCookie parser = HttpCookie.parse(CookieValue).get(0);
			URL origin;
			try {
				origin = new URL(CookieUrl);
			} catch (MalformedURLException e) {
				return;
			}
			if (parser.getDomain() == null) {
				parser.setDomain(origin.getHost());
			}
			if (parser.getPath() == null) {
				parser.setPath(origin.getPath());
			}

			ICoreWebView2CookieManager manager = getCookieManager();
			if (manager == null) return;

			char[] name = stringToWstr(parser.getName());
			char[] value = stringToWstr(parser.getValue());
			char[] domain = stringToWstr(parser.getDomain());
			char[] path = stringToWstr(parser.getPath());
			long[] ppv = new long[1];
			int hr = manager.CreateCookie(name, value, domain, path, ppv);
			if (hr != COM.S_OK) {
				manager.Release();
				return;
			}
			ICoreWebView2Cookie cookie = new ICoreWebView2Cookie(ppv[0]);

			if (parser.getMaxAge() != -1) {
				cookie.put_Expires(Instant.now().getEpochSecond() + parser.getMaxAge());
			}
			cookie.put_IsSecure(parser.getSecure());
			cookie.put_IsHttpOnly(parser.isHttpOnly());
			hr = manager.AddOrUpdateCookie(cookie);
			cookie.Release();
			manager.Release();

			CookieResult = hr >= COM.S_OK;
		};
	}

static String wstrToString(long psz, boolean free) {
	if (psz == 0) return "";
	int len = OS.wcslen(psz);
	char[] data = new char[len];
	OS.MoveMemory(data, psz, len * Character.BYTES);
	if (free) OS.CoTaskMemFree(psz);
	return String.valueOf(data);
}

static String bstrToString(long bstr) {
	if (bstr == 0) return "";
	int len = COM.SysStringLen(bstr);
	char[] data = new char[len];
	OS.MoveMemory(data, bstr, len * Character.BYTES);
	return String.valueOf(data);
}

static char[] stringToWstr(String s) {
	return (s != null) ? (s + "\0").toCharArray() : null;
}

static void error(int code, int hr) {
	SWT.error(code, null, String.format(" [0x%08x]", hr));
}

static IUnknown newCallback(ICoreWebView2SwtCallback handler) {
	long punk = COM.CreateSwtWebView2Callback((arg0, arg1) -> {
		inCallback = true;
		try {
			return handler.Invoke(arg0, arg1);
		} finally {
			inCallback = false;
		}
	});
	if (punk == 0) error(SWT.ERROR_NO_HANDLES, COM.E_OUTOFMEMORY);
	return new IUnknown(punk);
}

IUnknown newHostObject(ICoreWebView2SwtHost handler) {
	long pdisp = COM.CreateSwtWebView2Host(handler);
	if (pdisp == 0) error(SWT.ERROR_NO_HANDLES, COM.E_OUTOFMEMORY);
	return new IUnknown(pdisp);
}

static int callAndWait(long[] ppv, ToIntFunction<IUnknown> callable) {
	int[] phr = {COM.S_OK};
	IUnknown completion = newCallback((result, pv) -> {
		phr[0] = (int)result;
		if ((int)result == COM.S_OK) {
			ppv[0] = pv;
			new IUnknown(pv).AddRef();
		}
		return COM.S_OK;
	});
	ppv[0] = 0;
	phr[0] = callable.applyAsInt(completion);
	completion.Release();
	Display display = Display.getCurrent();
	while (phr[0] == COM.S_OK && ppv[0] == 0) {
		if (!display.readAndDispatch()) display.sleep();
	}
	return phr[0];
}

static int callAndWait(String[] pstr, ToIntFunction<IUnknown> callable) {
	int[] phr = new int[1];
	IUnknown completion = newCallback((result, pszJson) -> {
		phr[0] = (int)result;
		if ((int)result == COM.S_OK) {
			pstr[0] = wstrToString(pszJson, false);
		}
		return COM.S_OK;
	});
	pstr[0] = null;
	phr[0] = callable.applyAsInt(completion);
	completion.Release();
	Display display = Display.getCurrent();
	while (phr[0] == COM.S_OK && pstr[0] == null) {
		if (!display.readAndDispatch()) display.sleep();
	}
	return phr[0];
}

static ICoreWebView2CookieManager getCookieManager() {
	if (Instances.isEmpty()) {
		SWT.error(SWT.ERROR_NOT_IMPLEMENTED, null, " [WebView2: cookie access requires a Browser instance]");
	}
	Edge instance = Instances.get(0);
	if (instance.webView_2 == null) {
		SWT.error(SWT.ERROR_NOT_IMPLEMENTED, null, " [WebView2 version 88+ is required to access cookies]");
	}

	long[] ppv = new long[1];
	int hr = instance.webView_2.get_CookieManager(ppv);
	if (hr != COM.S_OK) error(SWT.ERROR_NO_HANDLES, hr);
	return new ICoreWebView2CookieManager(ppv[0]);
}

void checkDeadlock() {
	// Feature in WebView2. All event handlers, completion handlers
	// and JavaScript callbacks are serialized. An event handler waiting
	// for a completion of another handler will deadlock. Detect this
	// situation and throw an exception instead.
	if (inCallback || inNewWindow) {
		SWT.error(SWT.ERROR_FAILED_EVALUATE, null, " [WebView2: deadlock detected]");
	}
}

ICoreWebView2Environment createEnvironment() {
	if (Environment != null) return Environment;
	Display display = Display.getCurrent();

	// Gather customization properties
	String browserDir = System.getProperty(BROWSER_DIR_PROP);
	String dataDir = System.getProperty(DATA_DIR_PROP);
	String browserArgs = System.getProperty(BROWSER_ARGS_PROP);
	String language = System.getProperty(LANGUAGE_PROP);
	if (dataDir == null) {
		// WebView2 will append "\\EBWebView"
		dataDir = (String)display.getData(APPLOCAL_DIR_KEY);
	}

	// Initialize options
	long pOpts = COM.CreateSwtWebView2Options();
	if (pOpts == 0) error(SWT.ERROR_NO_HANDLES, COM.E_OUTOFMEMORY);
	ICoreWebView2EnvironmentOptions options = new ICoreWebView2EnvironmentOptions(pOpts);
	char[] pVersion = stringToWstr(SDK_TARGET_VERSION);
	options.put_TargetCompatibleBrowserVersion(pVersion);
	if (browserArgs != null) {
		char[] pBrowserArgs = stringToWstr(browserArgs);
		options.put_AdditionalBrowserArguments(pBrowserArgs);
	}
	if (language != null) {
		char[] pLanguage = stringToWstr(language);
		options.put_Language(pLanguage);
	}

	// Create the environment
	char[] pBrowserDir = stringToWstr(browserDir);
	char[] pDataDir = stringToWstr(dataDir);
	long[] ppv = new long[1];
	int hr = callAndWait(ppv, completion -> COM.CreateCoreWebView2EnvironmentWithOptions(
					pBrowserDir, pDataDir, options.getAddress(), completion.getAddress()));
	options.Release();
	if (hr == OS.HRESULT_FROM_WIN32(OS.ERROR_FILE_NOT_FOUND)) {
		SWT.error(SWT.ERROR_NOT_IMPLEMENTED, null, " [WebView2 runtime not found]");
	}
	if (hr != COM.S_OK) error(SWT.ERROR_NO_HANDLES, hr);
	Environment = new ICoreWebView2Environment(ppv[0]);
	DataDir = dataDir;

	// Save Edge version for reporting
	long[] ppVersion = new long[1];
	Environment.get_BrowserVersionString(ppVersion);
	String version = wstrToString(ppVersion[0], true);
	System.setProperty(VERSIONT_PROP, version);

	// Destroy the environment on app exit.
	display.disposeExec(() -> {
		Environment.Release();
		Environment = null;
	});
	return Environment;
}

@Override
public void create(Composite parent, int style) {
	checkDeadlock();
	ICoreWebView2Environment environment = createEnvironment();

	long[] ppv = new long[1];
	int hr = environment.QueryInterface(COM.IID_ICoreWebView2Environment2, ppv);
	if (hr == COM.S_OK) environment2 = new ICoreWebView2Environment2(ppv[0]);

	hr = callAndWait(ppv, completion -> environment.CreateCoreWebView2Controller(browser.handle, completion));
	if (hr != COM.S_OK) error(SWT.ERROR_NO_HANDLES, hr);
	controller = new ICoreWebView2Controller(ppv[0]);

	controller.get_CoreWebView2(ppv);
	webView = new ICoreWebView2(ppv[0]);
	webView.get_Settings(ppv);
	settings = new ICoreWebView2Settings(ppv[0]);
	hr = webView.QueryInterface(COM.IID_ICoreWebView2_2, ppv);
	if (hr == COM.S_OK) webView_2 = new ICoreWebView2_2(ppv[0]);

	long[] token = new long[1];
	IUnknown handler;
	handler = newCallback(this::handleCloseRequested);
	webView.add_WindowCloseRequested(handler, token);
	handler.Release();
	handler = newCallback(this::handleNavigationStarting);
	webView.add_NavigationStarting(handler, token);
	handler.Release();
	handler = newCallback(this::handleFrameNavigationStarting);
	webView.add_FrameNavigationStarting(handler, token);
	handler.Release();
	handler = newCallback(this::handleNavigationCompleted);
	webView.add_NavigationCompleted(handler, token);
	handler.Release();
	handler = newCallback(this::handleFrameNavigationCompleted);
	webView.add_FrameNavigationCompleted(handler, token);
	handler.Release();
	handler = newCallback(this::handleDocumentTitleChanged);
	webView.add_DocumentTitleChanged(handler, token);
	handler.Release();
	handler = newCallback(this::handleNewWindowRequested);
	webView.add_NewWindowRequested(handler, token);
	handler.Release();
	handler = newCallback(this::handleSourceChanged);
	webView.add_SourceChanged(handler, token);
	handler.Release();
	handler = newCallback(this::handleMoveFocusRequested);
	controller.add_MoveFocusRequested(handler, token);
	handler.Release();
	if (webView_2 != null) {
		handler = newCallback(this::handleDOMContentLoaded);
		webView_2.add_DOMContentLoaded(handler, token);
		handler.Release();
	}

	IUnknown hostDisp = newHostObject(this::handleCallJava);
	long[] hostObj = { COM.VT_DISPATCH, hostDisp.getAddress(), 0 }; // VARIANT
	webView.AddHostObjectToScript("swt\0".toCharArray(), hostObj);
	hostDisp.Release();

	browser.addListener(SWT.Dispose, this::browserDispose);
	browser.addListener(SWT.FocusIn, this::browserFocusIn);
	browser.addListener(SWT.Resize, this::browserResize);
	browser.addListener(SWT.Move, this::browserMove);

	Instances.add(this);
}

void browserDispose(Event event) {
	Instances.remove(this);

	if (webView_2 != null) webView_2.Release();
	if (environment2 != null) environment2.Release();
	settings.Release();
	webView.Release();
	webView_2 = null;
	environment2 = null;
	settings = null;
	webView = null;

	// Bug in WebView2. Closing the controller from an event handler results
	// in a crash. The fix is to delay the closure with asyncExec.
	if (inCallback) {
		ICoreWebView2Controller controller1 = controller;
		controller.put_IsVisible(false);
		browser.getDisplay().asyncExec(() -> {
			controller1.Close();
			controller1.Release();
		});
	} else {
		controller.Close();
		controller.Release();
	}
	controller = null;
}

void browserFocusIn(Event event) {
	// TODO: directional traversals
	controller.MoveFocus(COM.COREWEBVIEW2_MOVE_FOCUS_REASON_PROGRAMMATIC);
}

void browserMove(Event event) {
	controller.NotifyParentWindowPositionChanged();
}

void browserResize(Event event) {
	RECT rect = new RECT();
	OS.GetClientRect(browser.handle, rect);
	controller.put_Bounds(rect);
	controller.put_IsVisible(true);
}

@Override
public Object evaluate(String script) throws SWTException {
	checkDeadlock();

	// Feature in WebView2. ExecuteScript works regardless of IsScriptEnabled setting.
	// Disallow programmatic execution manually.
	if (!jsEnabled) return null;

	String script2 = "(function() {try { " + script + " } catch (e) { return '" + ERROR_ID + "' + e.message; } })();\0";
	String[] pJson = new String[1];
	int hr = callAndWait(pJson, completion -> webView.ExecuteScript(script2.toCharArray(), completion));
	if (hr != COM.S_OK) error(SWT.ERROR_FAILED_EVALUATE, hr);

	Object data = JSON.parse(pJson[0]);
	if (data instanceof String && ((String) data).startsWith(ERROR_ID)) {
		String errorMessage = ((String) data).substring(ERROR_ID.length());
		throw new SWTException (SWT.ERROR_FAILED_EVALUATE, errorMessage);
	}
	return data;
}

@Override
public boolean execute(String script) {
	// Feature in WebView2. ExecuteScript works regardless of IsScriptEnabled setting.
	// Disallow programmatic execution manually.
	if (!jsEnabled) return false;

	IUnknown completion = newCallback((long result, long json) -> COM.S_OK);
	int hr = webView.ExecuteScript(stringToWstr(script), completion);
	completion.Release();
	return hr == COM.S_OK;
}

@Override
public String getBrowserType() {
	return "edge";
}

@Override
String getJavaCallDeclaration() {
	return "if (!window.callJava) { window.callJava = function(index, token, args) {\n"
			+"return JSON.parse(window.chrome.webview.hostObjects.sync.swt.CallJava(index, token, JSON.stringify(args)));\n"
			+"}};\n";
}

@Override
public String getText() {
	return (String)evaluate("return document.documentElement.outerHTML;");
}

@Override
public String getUrl() {
	long ppsz[] = new long[1];
	webView.get_Source(ppsz);
	return wstrToString(ppsz[0], true);
}

int handleCloseRequested(long pView, long pArgs) {
	browser.getDisplay().asyncExec(() -> {
		if (browser.isDisposed()) return;
		WindowEvent event = new WindowEvent(browser);
		event.display = browser.getDisplay();
		event.widget = browser;
		for (CloseWindowListener listener : closeWindowListeners) {
			listener.close(event);
			if (browser.isDisposed()) return;
		}
		browser.dispose();
	});
	return COM.S_OK;
}

int handleDocumentTitleChanged(long pView, long pArgs) {
	long[] ppsz = new long[1];
	webView.get_DocumentTitle(ppsz);
	String title = wstrToString(ppsz[0], true);
	browser.getDisplay().asyncExec(() -> {
		if (browser.isDisposed()) return;
		TitleEvent event = new TitleEvent(browser);
		event.display = browser.getDisplay();
		event.widget = browser;
		event.title = title;
		for (TitleListener listener : titleListeners) {
			listener.changed(event);
			if (browser.isDisposed()) return;
		}
	});
	return COM.S_OK;
}

long handleCallJava(int index, long bstrToken, long bstrArgsJson) {
	Object result = null;
	String token = bstrToString(bstrToken);
	BrowserFunction function = functions.get(index);
	if (function != null && token.equals (function.token)) {
		try {
			String argsJson = bstrToString(bstrArgsJson);
			Object args = (Object[]) JSON.parse(argsJson.toCharArray());
			result = function.function ((Object[]) args);
		} catch (Throwable e) {
			result = WebBrowser.CreateErrorString(e.getLocalizedMessage());
		}
	}
	String json = JSON.stringify(result);
	return COM.SysAllocStringLen(json.toCharArray(), json.length());
}

int handleFrameNavigationStarting(long pView, long pArgs) {
	return handleNavigationStarting(pView, pArgs, false);
}

int handleNavigationStarting(long pView, long pArgs) {
	return handleNavigationStarting(pView, pArgs, true);
}

int handleNavigationStarting(long pView, long pArgs, boolean top) {
	ICoreWebView2NavigationStartingEventArgs args = new ICoreWebView2NavigationStartingEventArgs(pArgs);
	long[] ppszUrl = new long[1];
	int hr = args.get_Uri(ppszUrl);
	if (hr != COM.S_OK) return hr;
	String url = wstrToString(ppszUrl[0], true);
	long[] pNavId = new long[1];
	args.get_NavigationId(pNavId);
	LocationEvent event = new LocationEvent(browser);
	event.display = browser.getDisplay();
	event.widget = browser;
	event.location = url;
	event.top = top;
	event.doit = true;
	for (LocationListener listener : locationListeners) {
		listener.changing(event);
		if (browser.isDisposed()) return COM.S_OK;
	}
	if (event.doit) {
		// Save location and top for all events that use navigationId.
		navigations.put(pNavId[0], event);
		jsEnabled = jsEnabledOnNextPage;
		settings.put_IsScriptEnabled(jsEnabled);
		// Register browser functions in the new document.
		if (!functions.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (BrowserFunction function : functions.values()) {
				sb.append(function.functionString);
			}
			execute(sb.toString());
		}
	} else {
		args.put_Cancel(true);
	}
	return COM.S_OK;
}

int handleSourceChanged(long pView, long pArgs) {
	// Feature in WebView2. Navigations to data URIs set the Source
	// to an empty string. Navigations with NavigateToString set the Source
	// to about:blank. Initial Source is about:blank. If Source value
	// is the same between navigations, SourceChanged isn't fired.
	// TODO: emit missing location changed events
	long[] ppsz = new long[1];
	int hr = webView.get_Source(ppsz);
	if (hr != COM.S_OK) return hr;
	String url = wstrToString(ppsz[0], true);
	browser.getDisplay().asyncExec(() -> {
		if (browser.isDisposed()) return;
		LocationEvent event = new LocationEvent(browser);
		event.display = browser.getDisplay();
		event.widget = browser;
		event.location = url;
		event.top = true;
		for (LocationListener listener : locationListeners) {
			listener.changed(event);
			if (browser.isDisposed()) return;
		}
	});
	return COM.S_OK;
}

void sendProgressCompleted() {
	browser.getDisplay().asyncExec(() -> {
		if (browser.isDisposed()) return;
		ProgressEvent event = new ProgressEvent(browser);
		event.display = browser.getDisplay();
		event.widget = browser;
		for (ProgressListener listener : progressListeners) {
			listener.completed(event);
			if (browser.isDisposed()) return;
		}
	});
}

int handleDOMContentLoaded(long pView, long pArgs) {
	ICoreWebView2DOMContentLoadedEventArgs args = new ICoreWebView2DOMContentLoadedEventArgs(pArgs);
	long[] pNavId = new long[1];
	args.get_NavigationId(pNavId);
	LocationEvent startEvent = navigations.get(pNavId[0]);
	if (startEvent != null && startEvent.top) {
		sendProgressCompleted();
	}
	return COM.S_OK;
}

int handleNavigationCompleted(long pView, long pArgs) {
	return handleNavigationCompleted(pView, pArgs, true);
}

int handleFrameNavigationCompleted(long pView, long pArgs) {
	return handleNavigationCompleted(pView, pArgs, false);
}

int handleNavigationCompleted(long pView, long pArgs, boolean top) {
	ICoreWebView2NavigationCompletedEventArgs args = new ICoreWebView2NavigationCompletedEventArgs(pArgs);
	long[] pNavId = new long[1];
	args.get_NavigationId(pNavId);
	LocationEvent startEvent = navigations.remove(pNavId[0]);
	if (webView_2 == null && startEvent != null && startEvent.top) {
		// If DOMContentLoaded isn't available, fire
		// ProgressListener.completed from here.
		sendProgressCompleted();
	}
	return COM.S_OK;
}

void updateWindowFeatures(ICoreWebView2NewWindowRequestedEventArgs args, WindowEvent event) {
	long[] ppv = new long[1];
	int hr = args.get_WindowFeatures(ppv);
	if (hr != COM.S_OK) return;
	ICoreWebView2WindowFeatures features = new ICoreWebView2WindowFeatures(ppv[0]);

	int[] px = new int[1], py = new int[1];
	features.get_HasPosition(px);
	if (px[0] != 0) {
		features.get_Left(px);
		features.get_Top(py);
		event.location = new Point(px[0], py[0]);
	}
	features.get_HasSize(px);
	if (px[0] != 0) {
		features.get_Width(px);
		features.get_Height(py);
		event.size = new Point(px[0], py[0]);
	}
	// event.addressBar = ???
	features.get_ShouldDisplayMenuBar(px);
	event.menuBar = px[0] != 0;
	features.get_ShouldDisplayStatus(px);
	event.statusBar = px[0] != 0;
	features.get_ShouldDisplayToolbar(px);
	event.toolBar = px[0] != 0;
}

int handleNewWindowRequested(long pView, long pArgs) {
	ICoreWebView2NewWindowRequestedEventArgs args = new ICoreWebView2NewWindowRequestedEventArgs(pArgs);
	args.AddRef();
	long[] ppv = new long[1];
	args.GetDeferral(ppv);
	ICoreWebView2Deferral deferral = new ICoreWebView2Deferral(ppv[0]);
	inNewWindow = true;
	browser.getDisplay().asyncExec(() -> {
		try {
			if (browser.isDisposed()) return;
			WindowEvent openEvent = new WindowEvent(browser);
			openEvent.display = browser.getDisplay();
			openEvent.widget = browser;
			openEvent.required = false;
			for (OpenWindowListener openListener : openWindowListeners) {
				openListener.open(openEvent);
				if (browser.isDisposed()) return;
			}
			if (openEvent.browser != null && !openEvent.browser.isDisposed()) {
				WebBrowser other = openEvent.browser.webBrowser;
				args.put_Handled(true);
				if (other instanceof Edge) {
					args.put_NewWindow(((Edge)other).webView.getAddress());

					// Send show event to the other browser.
					WindowEvent showEvent = new WindowEvent (other.browser);
					showEvent.display = browser.getDisplay();
					showEvent.widget = other.browser;
					updateWindowFeatures(args, showEvent);
					for (VisibilityWindowListener showListener : other.visibilityWindowListeners) {
						showListener.show(showEvent);
						if (other.browser.isDisposed()) return;
					}
				}
			} else if (openEvent.required) {
				args.put_Handled(true);
			}
		} finally {
			deferral.Complete();
			deferral.Release();
			args.Release();
			inNewWindow = false;
		}
	});
	return COM.S_OK;
}

int handleMoveFocusRequested(long pView, long pArgs) {
	ICoreWebView2MoveFocusRequestedEventArgs args = new ICoreWebView2MoveFocusRequestedEventArgs(pArgs);
	int[] pReason = new int[1];
	args.get_Reason(pReason);
	args.put_Handled(true);
	switch (pReason[0]) {
	case COM.COREWEBVIEW2_MOVE_FOCUS_REASON_NEXT:
		browser.traverse(SWT.TRAVERSE_TAB_NEXT);
		break;
	case COM.COREWEBVIEW2_MOVE_FOCUS_REASON_PREVIOUS:
		browser.traverse(SWT.TRAVERSE_TAB_PREVIOUS);
		break;
	}
	return COM.S_OK;
}

@Override
public boolean isBackEnabled() {
	int[] pval = new int[1];
	webView.get_CanGoBack(pval);
	return pval[0] != 0;
}

@Override
public boolean isForwardEnabled() {
	int[] pval = new int[1];
	webView.get_CanGoForward(pval);
	return pval[0] != 0;
}

@Override
public boolean back() {
	// Feature in WebView2. GoBack returns S_OK even when CanGoBack is FALSE.
	return isBackEnabled() && webView.GoBack() == COM.S_OK;
}

@Override
public boolean forward() {
	// Feature in WebView2. GoForward returns S_OK even when CanGoForward is FALSE.
	return isForwardEnabled() && webView.GoForward() == COM.S_OK;
}

@Override
public void refresh() {
	webView.Reload();
}

@Override
public void stop() {
	webView.Stop();
}

@Override
public boolean setText(String html, boolean trusted) {
	char[] data = new char[html.length() + 1];
	html.getChars(0, html.length(), data, 0);
	return webView.NavigateToString(data) == COM.S_OK;
}

@Override
public boolean setUrl(String url, String postData, String[] headers) {
	// Feature in WebView2. Partial URLs like "www.example.com" are not accepted.
	// Prepend the protocol if it's missing.
	if (!url.matches("[a-z][a-z0-9+.-]*:.*")) {
		url = "http://" + url;
	}
	int hr;
	char[] pszUrl = stringToWstr(url);
	if (postData != null || headers != null) {
		if (environment2 == null || webView_2 == null) {
			SWT.error(SWT.ERROR_NOT_IMPLEMENTED, null, " [WebView2 version 88+ is required to set postData and headers]");
		}
		long[] ppRequest = new long[1];
		char[] pszMethod = null;
		char[] pszHeaders = null;
		IStream stream = null;
		if (postData != null) {
			pszMethod = "POST\0".toCharArray();
			byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);
			long pStream = COM.SHCreateMemStream(postDataBytes, postData.length());
			if (pStream == 0) error(SWT.ERROR_NO_HANDLES, COM.E_OUTOFMEMORY);
			stream = new IStream(pStream);
		} else {
			pszMethod = "GET\0".toCharArray();
		}
		if (headers != null) {
			String hblock = String.join("\r\n", Arrays.asList(headers));
			pszHeaders = new char[hblock.length() + 1];
			hblock.getChars(0, hblock.length(), pszHeaders, 0);
		}
		hr = environment2.CreateWebResourceRequest(pszUrl, pszMethod, stream, pszHeaders, ppRequest);
		if (stream != null) stream.Release();
		if (hr != COM.S_OK) error(SWT.ERROR_NO_HANDLES, hr);
		IUnknown request = new IUnknown(ppRequest[0]);
		hr = webView_2.NavigateWithWebResourceRequest(request);
		request.Release();
	} else {
		hr = webView.Navigate(pszUrl);
	}
	return hr == COM.S_OK;
}

}
