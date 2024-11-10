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

import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.nio.file.Path;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
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
	static final String SDK_TARGET_VERSION = "99.0.1150.38";

	// Display.getData() keys
	static final String APPLOCAL_DIR_KEY = "org.eclipse.swt.internal.win32.appLocalDir";
	static final String EDGE_USER_DATA_FOLDER = "org.eclipse.swt.internal.win32.Edge.userDataFolder";
	static final String EDGE_USE_DARK_PREFERED_COLOR_SCHEME = "org.eclipse.swt.internal.win32.Edge.useDarkPreferedColorScheme"; //$NON-NLS-1$

	// System.getProperty() keys
	static final String BROWSER_DIR_PROP = "org.eclipse.swt.browser.EdgeDir";
	static final String BROWSER_ARGS_PROP = "org.eclipse.swt.browser.EdgeArgs";
	static final String DATA_DIR_PROP = "org.eclipse.swt.browser.EdgeDataDir";
	static final String LANGUAGE_PROP = "org.eclipse.swt.browser.EdgeLanguage";
	static final String VERSIONT_PROP = "org.eclipse.swt.browser.EdgeVersion";
	/**
	 * The URI_FOR_CUSTOM_TEXT_PAGE is the path to a temporary html file which is used
	 * by Edge browser to navigate to for setting html content in the
	 * DOM of the browser to enable it to load local resources.
	 */
	private static final URI URI_FOR_CUSTOM_TEXT_PAGE = setupAndGetLocationForCustomTextPage();
	private static final String ABOUT_BLANK = "about:blank";

	private record WebViewEnvironment(ICoreWebView2Environment environment, ArrayList<Edge> instances) {
		public WebViewEnvironment(ICoreWebView2Environment environment) {
			this (environment, new ArrayList<>());
		}
	}

	private static Map<Display, WebViewEnvironment> webViewEnvironments = new HashMap<>();
	ICoreWebView2Controller controller;
	ICoreWebView2Settings settings;
	ICoreWebView2Profile profile;
	ICoreWebView2Environment2 environment2;
	private final WebViewProvider webViewProvider = new WebViewProvider();

	WebViewEnvironment containingEnvironment;

	static boolean inCallback;
	boolean inNewWindow;
	HashMap<Long, LocationEvent> navigations = new HashMap<>();
	private boolean ignoreFocus;
	private String lastCustomText;

	private static record CursorPosition(Point location, boolean isInsideBrowser) {};
	private CursorPosition previousCursorPosition = new CursorPosition(new Point(0, 0), false);

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

private static URI setupAndGetLocationForCustomTextPage() {
	URI absolutePath;
	try {
		Path tempFile = Files.createTempFile("base", ".html");
		absolutePath = tempFile.toUri();
		tempFile.toFile().deleteOnExit();
	} catch (IOException e) {
		absolutePath = URI.create(ABOUT_BLANK);
	}
	return absolutePath;
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
	// "completion" callback may be called asynchronously,
	// so keep processing next OS message that may call it
	while (phr[0] == COM.S_OK && ppv[0] == 0) {
		processNextOSMessage();
	}
	completion.Release();
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
	// "completion" callback may be called asynchronously,
	// so keep processing next OS message that may call it
	while (phr[0] == COM.S_OK && pstr[0] == null) {
		processNextOSMessage();
	}
	completion.Release();
	return phr[0];
}

class WebViewProvider {

	private CompletableFuture<ICoreWebView2> webViewFuture = new CompletableFuture<>();
	private CompletableFuture<ICoreWebView2_2> webView_2Future = new CompletableFuture<>();
	private CompletableFuture<ICoreWebView2_10> webView_10Future = new CompletableFuture<>();
	private CompletableFuture<ICoreWebView2_11> webView_11Future = new CompletableFuture<>();
	private CompletableFuture<ICoreWebView2_12> webView_12Future = new CompletableFuture<>();
	private CompletableFuture<ICoreWebView2_13> webView_13Future = new CompletableFuture<>();

	private CompletableFuture<Void> lastWebViewTask = webViewFuture.thenRun(() -> {});

	ICoreWebView2 initializeWebView(ICoreWebView2Controller controller) {
		long[] ppv = new long[1];
		controller.get_CoreWebView2(ppv);
		final ICoreWebView2 webView = new ICoreWebView2(ppv[0]);
		initializeWebView_2(webView);
		initializeWebView_10(webView);
		initializeWebView_11(webView);
		initializeWebView_12(webView);
		initializeWebView_13(webView);
		webViewFuture.complete(webView);
		return webView;
	}

	private void initializeWebView_2(ICoreWebView2 webView) {
		long[] ppv = new long[1];
		int hr = webView.QueryInterface(COM.IID_ICoreWebView2_2, ppv);
		if (hr == COM.S_OK) {
			webView_2Future.complete(new ICoreWebView2_2(ppv[0]));
		} else {
			webView_2Future.cancel(true);
		}
	}

	private void initializeWebView_10(ICoreWebView2 webView) {
		long[] ppv = new long[1];
		int hr = webView.QueryInterface(COM.IID_ICoreWebView2_10, ppv);
		if (hr == COM.S_OK) {
			webView_10Future.complete(new ICoreWebView2_10(ppv[0]));
		} else {
			webView_10Future.cancel(true);
		}
	}

	private void initializeWebView_11(ICoreWebView2 webView) {
		long[] ppv = new long[1];
		int hr = webView.QueryInterface(COM.IID_ICoreWebView2_11, ppv);
		if (hr == COM.S_OK) {
			webView_11Future.complete(new ICoreWebView2_11(ppv[0]));
		} else {
			webView_11Future.cancel(true);
		}
	}

	private void initializeWebView_12(ICoreWebView2 webView) {
		long[] ppv = new long[1];
		int hr = webView.QueryInterface(COM.IID_ICoreWebView2_12, ppv);
		if (hr == COM.S_OK) {
			webView_12Future.complete(new ICoreWebView2_12(ppv[0]));
		} else {
			webView_12Future.cancel(true);
		}
	}

	private void initializeWebView_13(ICoreWebView2 webView) {
		long[] ppv = new long[1];
		int hr = webView.QueryInterface(COM.IID_ICoreWebView2_13, ppv);
		if (hr == COM.S_OK) {
			webView_13Future.complete(new ICoreWebView2_13(ppv[0]));
		} else {
			webView_13Future.cancel(true);
		}
	}

	ICoreWebView2 getWebView(boolean waitForPendingWebviewTasksToFinish) {
		if(waitForPendingWebviewTasksToFinish) {
			waitForFutureToFinish(lastWebViewTask);
		}
		return webViewFuture.join();
	}

	ICoreWebView2_2 getWebView_2(boolean waitForPendingWebviewTasksToFinish) {
		if(waitForPendingWebviewTasksToFinish) {
			waitForFutureToFinish(lastWebViewTask);
		}
		return webView_2Future.join();
	}

	boolean isWebView_2Available() {
		waitForFutureToFinish(webView_2Future);
		return !webView_2Future.isCancelled();
	}

	ICoreWebView2_10 getWebView_10(boolean waitForPendingWebviewTasksToFinish) {
		if(waitForPendingWebviewTasksToFinish) {
			waitForFutureToFinish(lastWebViewTask);
		}
		return webView_10Future.join();
	}

	boolean isWebView_10Available() {
		waitForFutureToFinish(webView_10Future);
		return !webView_10Future.isCancelled();
	}

	ICoreWebView2_11 getWebView_11(boolean waitForPendingWebviewTasksToFinish) {
		if(waitForPendingWebviewTasksToFinish) {
			waitForFutureToFinish(lastWebViewTask);
		}
		return webView_11Future.join();
	}

	boolean isWebView_11Available() {
		waitForFutureToFinish(webView_11Future);
		return !webView_11Future.isCancelled();
	}

	ICoreWebView2_12 getWebView_12(boolean waitForPendingWebviewTasksToFinish) {
		if(waitForPendingWebviewTasksToFinish) {
			waitForFutureToFinish(lastWebViewTask);
		}
		return webView_12Future.join();
	}

	boolean isWebView_12Available() {
		waitForFutureToFinish(webView_12Future);
		return !webView_12Future.isCancelled();
	}

	ICoreWebView2_13 getWebView_13(boolean waitForPendingWebviewTasksToFinish) {
		if(waitForPendingWebviewTasksToFinish) {
			waitForFutureToFinish(lastWebViewTask);
		}
		return webView_13Future.join();
	}

	boolean isWebView_13Available() {
		waitForFutureToFinish(webView_13Future);
		return !webView_13Future.isCancelled();
	}

	/*
	 * Schedule a given runnable in a queue to execute when the webView is free and
	 * has finished all the pending tasks queued before it.
	 */
	void scheduleWebViewTask(Runnable action) {
		lastWebViewTask = lastWebViewTask.thenRun(() -> {
			action.run();
		});
	}

	private <T> void waitForFutureToFinish(CompletableFuture<T> future) {
		while(!future.isDone()) {
			processNextOSMessage();
		}
	}

}

/**
 * Processes a single OS message using {@link Display#readAndDispatch()}. This
 * is required for processing the OS events during browser initialization, since
 * Edge browser initialization happens asynchronously.
 * <p>
 * {@link Display#readAndDispatch()} also processes events scheduled for
 * asynchronous execution via {@link Display#asyncExec(Runnable)}. This may
 * include events such as the disposal of the browser's parent composite, which
 * leads to a failure in browser initialization if processed in between the OS
 * events for initialization. Thus, this method does not implement an ordinary
 * readAndDispatch loop, but waits for an OS event to be processed.
 */
private static void processNextOSMessage() {
	Display display = Display.getCurrent();
	MSG msg = new MSG();
	while (!OS.PeekMessage (msg, 0, 0, 0, OS.PM_NOREMOVE)) {
		display.sleep();
	}
	display.readAndDispatch();
}

static ICoreWebView2CookieManager getCookieManager() {
	WebViewEnvironment environmentWrapper = webViewEnvironments.get(Display.getCurrent());
	if (environmentWrapper == null) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT, null, " [WebView2: environment not initialized for current display]");
	}
	if (environmentWrapper.instances().isEmpty()) {
		SWT.error(SWT.ERROR_NOT_IMPLEMENTED, null, " [WebView2: cookie access requires a Browser instance]");
	}
	Edge instance = environmentWrapper.instances().get(0);
	if (!instance.webViewProvider.isWebView_2Available()) {
		SWT.error(SWT.ERROR_NOT_IMPLEMENTED, null, " [WebView2 version 88+ is required to access cookies]");
	}

	long[] ppv = new long[1];
	int hr = instance.webViewProvider.getWebView_2(true).get_CookieManager(ppv);
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

WebViewEnvironment createEnvironment() {
	Display display = Display.getCurrent();
	WebViewEnvironment existingEnvironment = webViewEnvironments.get(display);
	if (existingEnvironment != null) return existingEnvironment;

	// Gather customization properties
	String browserDir = System.getProperty(BROWSER_DIR_PROP);
	String browserArgs = System.getProperty(BROWSER_ARGS_PROP);
	String language = System.getProperty(LANGUAGE_PROP);
	String dataDir = getDataDir(display);

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
	ICoreWebView2Environment environment = new ICoreWebView2Environment(ppv[0]);
	WebViewEnvironment environmentWrapper = new WebViewEnvironment(environment);

	// Save Edge version for reporting
	long[] ppVersion = new long[1];
	environment.get_BrowserVersionString(ppVersion);
	String version = wstrToString(ppVersion[0], true);
	System.setProperty(VERSIONT_PROP, version);

	// Destroy the environment on app exit.
	display.disposeExec(() -> {
		for (Edge instance : environmentWrapper.instances()) {
			instance.browserDispose(null);
		}
		environment.Release();
		webViewEnvironments.remove(display);
	});

	webViewEnvironments.put(display, environmentWrapper);
	return environmentWrapper;
}

private String getDataDir(Display display) {
	String dataDir = System.getProperty(DATA_DIR_PROP);
	if (dataDir == null) {
		dataDir = (String) display.getData(EDGE_USER_DATA_FOLDER);
	}
	if (dataDir == null) {
		// WebView2 will append "\\EBWebView"
		dataDir = (String)display.getData(APPLOCAL_DIR_KEY);
	}
	return dataDir;
}

@Override
public void create(Composite parent, int style) {
	containingEnvironment = createEnvironment();
	containingEnvironment.instances().add(this);
	long[] ppv = new long[1];
	int hr = containingEnvironment.environment().QueryInterface(COM.IID_ICoreWebView2Environment2, ppv);
	if (hr == COM.S_OK) environment2 = new ICoreWebView2Environment2(ppv[0]);
	// The webview calls are queued to be executed when it is done executing the current task.
	IUnknown setupBrowserCallback = newCallback((result, pv) -> {
		if ((int)result == COM.S_OK) {
			new IUnknown(pv).AddRef();
		}
		setupBrowser((int)result, pv);
		return COM.S_OK;
	});
	containingEnvironment.environment().CreateCoreWebView2Controller(browser.handle, setupBrowserCallback);
}

void setupBrowser(int hr, long pv) {
	if(browser.isDisposed()) {
		browserDispose(new Event());
		return;
	}
	switch (hr) {
	case COM.S_OK:
		break;
	case COM.E_WRONG_THREAD:
		containingEnvironment.instances().remove(this);
		error(SWT.ERROR_THREAD_INVALID_ACCESS, hr);
		break;
	default:
		containingEnvironment.instances().remove(this);
		error(SWT.ERROR_NO_HANDLES, hr);
	}
	long[] ppv = new long[] {pv};
	controller = new ICoreWebView2Controller(ppv[0]);
	final ICoreWebView2 webView = webViewProvider.initializeWebView(controller);
	webView.get_Settings(ppv);
	settings = new ICoreWebView2Settings(ppv[0]);

	if (webViewProvider.isWebView_12Available()) {
		// Align with other Browser implementations:
		// Disable internal status bar on the bottom left of the Browser control
		// Send out StatusTextEvents via handleStatusBarTextChanged for SWT consumers
		settings.put_IsStatusBarEnabled(false);
	}

	if (webViewProvider.isWebView_13Available()) {
		webViewProvider.getWebView_13(false).get_Profile(ppv);
		profile = new ICoreWebView2Profile(ppv[0]);

		// Dark theme inherited from application theme
		if (Boolean.TRUE.equals(browser.getDisplay().getData(EDGE_USE_DARK_PREFERED_COLOR_SCHEME))) {
			profile.put_PreferredColorScheme(/* COREWEBVIEW2_PREFERRED_COLOR_SCHEME_DARK */ 2);
		} else {
			profile.put_PreferredColorScheme(/* COREWEBVIEW2_PREFERRED_COLOR_SCHEME_LIGHT */ 1);
		}
	}

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
	handler = newCallback(this::handleGotFocus);
	controller.add_GotFocus(handler, token);
	handler.Release();
	handler = newCallback(this::handleAcceleratorKeyPressed);
	controller.add_AcceleratorKeyPressed(handler, token);
	handler.Release();
	if (webViewProvider.isWebView_2Available()) {
		handler = newCallback(this::handleDOMContentLoaded);
		webViewProvider.getWebView_2(false).add_DOMContentLoaded(handler, token);
		handler.Release();
	}
	if (webViewProvider.isWebView_10Available()) {
		handler = newCallback(this::handleBasicAuthenticationRequested);
		webViewProvider.getWebView_10(false).add_BasicAuthenticationRequested(handler, token);
		handler.Release();
	}
	if (webViewProvider.isWebView_11Available()) {
		handler = newCallback(this::handleContextMenuRequested);
		webViewProvider.getWebView_11(false).add_ContextMenuRequested(handler, token);
		handler.Release();
	}
	if (webViewProvider.isWebView_12Available()) {
		handler = newCallback(this::handleStatusBarTextChanged);
		webViewProvider.getWebView_12(false).add_StatusBarTextChanged(handler, token);
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
	scheduleMouseMovementHandling();

	// Sometimes when the shell of the browser is opened before the browser is
	// initialized, nothing is drawn on the shell. We need browserResize to force
	// the shell to draw itself again.
	browserResize(new Event());
}

void browserDispose(Event event) {
	containingEnvironment.instances.remove(this);
	webViewProvider.scheduleWebViewTask(() -> {
		webViewProvider.getWebView(false).Release();
		if (environment2 != null) environment2.Release();
		if (settings != null) settings.Release();
		if (webViewProvider.isWebView_2Available()) webViewProvider.getWebView_2(false).Release();
		if (webViewProvider.isWebView_11Available()) webViewProvider.getWebView_11(false).Release();
		if (webViewProvider.isWebView_12Available()) webViewProvider.getWebView_12(false).Release();
		if(controller != null) {
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
		environment2 = null;
		settings = null;
	});
}

void browserFocusIn(Event event) {
	if (ignoreFocus) return;
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

private void scheduleMouseMovementHandling() {
	browser.getDisplay().timerExec(100, () -> {
		if (browser.isDisposed()) {
			return;
		}
		if (browser.isVisible() && hasDisplayFocus()) {
			handleMouseMovement();
		}
		scheduleMouseMovementHandling();
	});
}

private void handleMouseMovement() {
	final Point currentCursorLocation = browser.getDisplay().getCursorLocation();
	Point cursorLocationInControlCoordinate = browser.toControl(currentCursorLocation);
	boolean isCursorInsideBrowser = browser.getBounds().contains(cursorLocationInControlCoordinate);
	boolean hasCursorLocationChanged = !currentCursorLocation.equals(previousCursorPosition.location);

	boolean mousePassedBrowserBorder = previousCursorPosition.isInsideBrowser != isCursorInsideBrowser;
	boolean mouseMovedInsideBrowser = isCursorInsideBrowser && hasCursorLocationChanged;
	if (mousePassedBrowserBorder) {
		if (isCursorInsideBrowser) {
			sendMouseEvent(cursorLocationInControlCoordinate, SWT.MouseEnter);
		} else {
			sendMouseEvent(cursorLocationInControlCoordinate, SWT.MouseExit);
		}
	} else if (mouseMovedInsideBrowser) {
		sendMouseEvent(cursorLocationInControlCoordinate, SWT.MouseMove);
	}
	previousCursorPosition = new CursorPosition(currentCursorLocation, isCursorInsideBrowser);
}

private void sendMouseEvent(Point cursorLocationInControlCoordinate, int mouseEvent) {
	Event newEvent = new Event();
	newEvent.widget = browser;
	Point position = cursorLocationInControlCoordinate;
	newEvent.x = position.x;
	newEvent.y = position.y;
	newEvent.type = mouseEvent;
	browser.notifyListeners(newEvent.type, newEvent);
}

private boolean hasDisplayFocus() {
	return browser.getDisplay().getFocusControl() != null;
}

@Override
public Object evaluate(String script) throws SWTException {
	// Feature in WebView2. ExecuteScript works regardless of IsScriptEnabled setting.
	// Disallow programmatic execution manually.
	if (!jsEnabled) return null;

	String script2 = "(function() {try { " + script + " } catch (e) { return '" + ERROR_ID + "' + e.message; } })();\0";
	String[] pJson = new String[1];
	int hr = callAndWait(pJson, completion -> webViewProvider.getWebView(true).ExecuteScript(script2.toCharArray(), completion));
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
	int hr = webViewProvider.getWebView(true).ExecuteScript(stringToWstr(script), completion);
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
	webViewProvider.getWebView(true).get_Source(ppsz);
	return getExposedUrl(wstrToString(ppsz[0], true));
}

private String getExposedUrl(String url) {
	return isLocationForCustomText(url) ? ABOUT_BLANK : url;
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
	webViewProvider.getWebView(false).get_DocumentTitle(ppsz);
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
			Object args = JSON.parse(argsJson.toCharArray());
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
	String url = getExposedUrl(wstrToString(ppszUrl[0], true));
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
	// Save location and top for all events that use navigationId.
	// will be eventually cleared again in handleNavigationCompleted().
	navigations.put(pNavId[0], event);
	if (event.doit) {
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
	// Since we do not use NavigateToString (but always use a dummy file)
	// we always see a proper (file:// URI).
	// The main location events are fired from
	// handleNavigationStarting() / handleNavigationCompleted()
	// to get consistent/symmetric
	// LocationListener.changing() -> LocationListener.changed() behavior.
	// For #fragment navigation within a page, no NavigationStarted/NavigationCompleted
	// events are send from WebView2.
	// Instead, SourceChanged is fired.
	// We therefore also handle this event specifically for in-same-document scenario.
	// Since SourceChanged cannot be blocked, we only send out
	// LocationListener#changed() events, no changing() events.
	int[] isNewDocument = new int[1];
	ICoreWebView2SourceChangedEventArgs args = new ICoreWebView2SourceChangedEventArgs(pArgs);
	args.get_IsNewDocument(isNewDocument);
	if (isNewDocument[0] == 0) {
		// #fragment navigation inside the same document
		long[] ppsz = new long[1];
		int hr = webViewProvider.getWebView(true).get_Source(ppsz);
		if (hr != COM.S_OK) return hr;
		String url = wstrToString(ppsz[0], true);
		int fragmentIndex = url.indexOf('#');
		String urlWithoutFragment = fragmentIndex == -1 ? url : url.substring(0,fragmentIndex);
		String location;
		if (isLocationForCustomText(urlWithoutFragment)) {
			if (fragmentIndex != -1) {
				location = ABOUT_BLANK.toString() + url.substring(fragmentIndex);
			} else {
				location = ABOUT_BLANK.toString();
			}
		} else {
			location = url;
		}
		browser.getDisplay().asyncExec(() -> {
			if (browser.isDisposed()) return;
			LocationEvent event = new LocationEvent(browser);
			event.display = browser.getDisplay();
			event.widget = browser;
			event.location = location;
			event.top = true;
			for (LocationListener listener : locationListeners) {
				listener.changed(event);
				if (browser.isDisposed()) return;
			}
		});
	}
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
		if (lastCustomText != null && getUrl().equals(ABOUT_BLANK)) {
			IUnknown postExecute = newCallback((long result, long json) -> {
				sendProgressCompleted();
				return COM.S_OK;
			});
			webViewProvider.getWebView(true).ExecuteScript(
					stringToWstr("document.open(); document.write('" + escapeForSingleQuotedJSString(lastCustomText) + "'); document.close();"),
					postExecute);
			postExecute.Release();
			this.lastCustomText = null;
		} else {
			sendProgressCompleted();
		}
	}
	return COM.S_OK;
}

private static String escapeForSingleQuotedJSString(String str) {
	return str.replace("\\", "\\\\") //
			.replace("'", "\\'") //
			.replace("\r", "\\r") //
			.replace("\n", "\\n");
}

int handleBasicAuthenticationRequested(long pView, long pArgs) {
	ICoreWebView2BasicAuthenticationRequestedEventArgs args = new ICoreWebView2BasicAuthenticationRequestedEventArgs(pArgs);

	long[] ppv = new long[1];

	args.get_Uri(ppv);
	String uri = wstrToString(ppv[0], true);

	for (AuthenticationListener authenticationListener : this.authenticationListeners) {
		AuthenticationEvent event = new AuthenticationEvent (browser);
		event.location = uri;
		authenticationListener.authenticate (event);
		if (!event.doit) {
			args.put_Cancel(true);
			return COM.S_OK;
		}
		if (event.user != null && event.password != null) {
			args.get_Response(ppv);
			ICoreWebView2BasicAuthenticationResponse response = new ICoreWebView2BasicAuthenticationResponse(ppv[0]);
			response.put_UserName(stringToWstr(event.user));
			response.put_Password(stringToWstr(event.password));
			return COM.S_OK;
		}
	}

	return COM.S_OK;
}

int handleContextMenuRequested(long pView, long pArgs) {
	ICoreWebView2ContextMenuRequestedEventArgs args = new ICoreWebView2ContextMenuRequestedEventArgs(pArgs);

	long[] locationPointer = new long[1];
	args.get_Location(locationPointer);
	POINT win32Point = new POINT();
	OS.MoveMemory(win32Point, locationPointer, POINT.sizeof);

	// From WebView2 we receive widget-relative win32 POINTs.
	// The Event we create here will be mapped to a
	// MenuDetectEvent used with SWT.MenuDetect eventually, which
	// uses display-relative DISPLAY coordinates.
	// Thefore, we
	// - first, explicitly scale up the the win32 POINT values from edge
	//   to PIXEL coordinates with the real native zoom value
	//   independent from the swt.autoScale property:
	Point pt = new Point( //
			DPIUtil.scaleUp(win32Point.x, DPIUtil.getNativeDeviceZoom()), //
			DPIUtil.scaleUp(win32Point.y, DPIUtil.getNativeDeviceZoom()));
	// - then, scale back down from PIXEL to DISPLAY coordinates, taking
	//   swt.autoScale property into account
	//   which is also later considered in Menu#setLocation()
	pt = new Point( //
			DPIUtil.scaleDown(pt.x, DPIUtil.getZoomForAutoscaleProperty(browser.getShell().nativeZoom)), //
			DPIUtil.scaleDown(pt.y, DPIUtil.getZoomForAutoscaleProperty(browser.getShell().nativeZoom)));
	// - finally, translate the POINT from widget-relative
	//   to DISPLAY-relative coordinates
	pt = browser.toDisplay(pt.x, pt.y);
	Event event = new Event();
	event.x = pt.x;
	event.y = pt.y;
	browser.notifyListeners(SWT.MenuDetect, event);
	if (!event.doit) {
		// Suppress context menu
		args.put_Handled(true);
	} else {
		Menu menu = browser.getMenu();
		if (menu != null && !menu.isDisposed()) {
			args.put_Handled(true);
			if (pt.x != event.x || pt.y != event.y) {
				menu.setLocation(event.x, event.y);
			}
			menu.setVisible(true);
		}
	}
	return COM.S_OK;
}

int handleStatusBarTextChanged(long pView, long pArgs) {
	long ppsz[] = new long[1];
	webViewProvider.getWebView_12(true).get_StatusBarText(ppsz);
	String text = wstrToString(ppsz[0], true);
	StatusTextEvent statusTextEvent = new StatusTextEvent(browser);
	statusTextEvent.display = browser.getDisplay();
	statusTextEvent.widget = browser;
	statusTextEvent.text = text;
	for (StatusTextListener statusTextListener : statusTextListeners) {
		statusTextListener.changed(statusTextEvent);
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
	if (startEvent == null) {
		// handleNavigationStarted() always stores the event, so this should never happen
		return COM.S_OK;
	}
	if (!webViewProvider.isWebView_2Available() && startEvent.top) {
		// If DOMContentLoaded (part of ICoreWebView2_2 interface) isn't available, fire
		// ProgressListener.completed from here.
		sendProgressCompleted();
	}
	int[] pIsSuccess = new int[1];
	args.get_IsSuccess(pIsSuccess);
	if (pIsSuccess[0] != 0) {
		browser.getDisplay().asyncExec(() -> {
			if (browser.isDisposed()) return;
			LocationEvent event = new LocationEvent(browser);
			event.display = browser.getDisplay();
			event.widget = browser;
			event.location = startEvent.location;
			event.top = startEvent.top;
			for (LocationListener listener : locationListeners) {
				listener.changed(event);
				if (browser.isDisposed()) return;
			}
		});
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
			openEvent.required = true;
			for (OpenWindowListener openListener : openWindowListeners) {
				openListener.open(openEvent);
				if (browser.isDisposed()) return;
			}
			if (openEvent.browser != null && !openEvent.browser.isDisposed()) {
				WebBrowser other = openEvent.browser.webBrowser;
				args.put_Handled(true);
				if (other instanceof Edge otherEdge) {
					args.put_NewWindow(otherEdge.webViewProvider.getWebView(true).getAddress());

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

int handleGotFocus(long pView, long pArg) {
	// browser.forceFocus() does not result in
	// Shell#setActiveControl(Control)
	// being called and therefore no SWT.FocusIn event being dispatched,
	// causing active part, etc. not to be updated correctly.
	// The solution is to explicitly send a WM_SETFOCUS
	// to the browser, and, while doing so, ignoring any recursive
	// calls in #browserFocusIn(Event).
	ignoreFocus = true;
	OS.SendMessage (browser.handle, OS.WM_SETFOCUS, 0, 0);
	ignoreFocus = false;
	return COM.S_OK;
}

/**
 * Events are not fired for all keyboard shortcuts.
 * <ul>
 * <li>Events for ctrl, alt modifiers are sent, but not for shift. So we use
 * GetKeyState() to read modifier keys consistently and don't send out any
 * events for the modifier-only events themselves.
 * <li>We are missing some other keys (e.g. VK_TAB or VK_RETURN, unless modified
 * by ctrl or alt).
 * </ul>
 * This is a best-effort implementation oriented towards
 * {@link IE#handleDOMEvent(org.eclipse.swt.ole.win32.OleEvent)}.
 *
 * @see <a href=
 *      "https://learn.microsoft.com/en-us/microsoft-edge/webview2/reference/win32/icorewebview2controller">https://learn.microsoft.com/en-us/microsoft-edge/webview2/reference/win32/icorewebview2controller</a>
 */
int handleAcceleratorKeyPressed(long pView, long pArgs) {
	ICoreWebView2AcceleratorKeyPressedEventArgs args = new ICoreWebView2AcceleratorKeyPressedEventArgs(pArgs);
	int[] virtualKey = new int[1];
	args.get_VirtualKey(virtualKey);
	int[] lparam = new int[1];
	args.get_KeyEventLParam(lparam);
	long flags = Integer.toUnsignedLong(lparam[0]) >> 16;
	boolean isDown = (flags & 0x8000) == 0;

	if (virtualKey[0] == OS.VK_SHIFT || virtualKey[0] == OS.VK_CONTROL || virtualKey[0] == OS.VK_MENU) {
		return COM.S_OK;
	}

	Event keyEvent = new Event ();
	keyEvent.widget = browser;
	keyEvent.keyCode = translateKey(virtualKey[0]);
	if (OS.GetKeyState (OS.VK_MENU) < 0) keyEvent.stateMask |= SWT.ALT;
	if (OS.GetKeyState (OS.VK_SHIFT) < 0) keyEvent.stateMask |= SWT.SHIFT;
	if (OS.GetKeyState (OS.VK_CONTROL) < 0) keyEvent.stateMask |= SWT.CONTROL;

	if (isDown) {
		keyEvent.type = SWT.KeyDown;
		// Ignore repeated events while key is pressed
		boolean isRepeat = (flags & 0x4000) != 0;
		if (isRepeat) {
			return COM.S_OK;
		}

		if (!sendKeyEvent(keyEvent)) {
			args.put_Handled(true);
		}
	} else {
		keyEvent.type = SWT.KeyUp;
		browser.notifyListeners (keyEvent.type, keyEvent);
		if (!keyEvent.doit) {
			args.put_Handled(true);
		}
	}

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
	webViewProvider.getWebView(true).get_CanGoBack(pval);
	return pval[0] != 0;
}

@Override
public boolean isForwardEnabled() {
	int[] pval = new int[1];
	webViewProvider.getWebView(true).get_CanGoForward(pval);
	return pval[0] != 0;
}

@Override
public boolean back() {
	// Feature in WebView2. GoBack returns S_OK even when CanGoBack is FALSE.
	return isBackEnabled() && webViewProvider.getWebView(true).GoBack() == COM.S_OK;
}

@Override
public boolean forward() {
	// Feature in WebView2. GoForward returns S_OK even when CanGoForward is FALSE.
	return isForwardEnabled() && webViewProvider.getWebView(true).GoForward() == COM.S_OK;
}

@Override
public void refresh() {
	webViewProvider.scheduleWebViewTask(() -> webViewProvider.getWebView(false).Reload());
}

@Override
public void stop() {
	webViewProvider.scheduleWebViewTask(() -> webViewProvider.getWebView(false).Stop());
}

private boolean isLocationForCustomText(String location) {
		try {
			return URI_FOR_CUSTOM_TEXT_PAGE.equals(new URI(location));
		} catch (URISyntaxException e) {
			return false;
		}
}

@Override
public boolean setText(String html, boolean trusted) {
	return setWebpageData(URI_FOR_CUSTOM_TEXT_PAGE.toASCIIString(), null, null, html);
}

private boolean setWebpageData(String url, String postData, String[] headers, String html) {
	// Feature in WebView2. Partial URLs like "www.example.com" are not accepted.
	// Prepend the protocol if it's missing.
	if (!url.matches("[a-z][a-z0-9+.-]*:.*")) {
		url = "http://" + url;
	}
	int hr;
	char[] pszUrl = stringToWstr(url);
	if(isLocationForCustomText(url)) {
		this.lastCustomText = html;
	}
	if (postData != null || headers != null) {
		if (environment2 == null || !webViewProvider.isWebView_2Available()) {
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
		webViewProvider.scheduleWebViewTask(() -> {
			webViewProvider.getWebView_2(false).NavigateWithWebResourceRequest(request);
			request.Release();
		});;
	} else {
		webViewProvider.scheduleWebViewTask(() -> webViewProvider.getWebView(false).Navigate(pszUrl));;
	}
	return true;
}

@Override
public boolean setUrl(String url, String postData, String[] headers) {
	return setWebpageData(url, postData, headers, null);
}

}
