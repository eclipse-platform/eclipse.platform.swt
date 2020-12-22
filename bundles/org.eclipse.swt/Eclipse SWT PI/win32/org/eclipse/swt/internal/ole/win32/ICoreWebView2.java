/*******************************************************************************
 * Copyright (c) 2020 Nikita Nemkin and others.
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
package org.eclipse.swt.internal.ole.win32;

public class ICoreWebView2 extends IUnknown {

public ICoreWebView2(long address) {
	super(address);
}

public int get_Settings(long[] settings) {
	return COM.VtblCall(3, address, settings);
}

public int get_Source(long[] uri) {
	return COM.VtblCall(4, address, uri);
}

public int Navigate(char[] uri) {
	return COM.VtblCall(5, address, uri);
}

public int NavigateToString(char[] htmlContent) {
	return COM.VtblCall(6, address, htmlContent);
}

public int add_NavigationStarting(IUnknown eventHandler, long[] token) {
	return COM.VtblCall(7, address, eventHandler.address, token);
}

public int add_ContentLoading(IUnknown eventHandler, long[] token) {
	return COM.VtblCall(9, address, eventHandler.address, token);
}

public int add_SourceChanged(IUnknown eventHandler, long[] token) {
	return COM.VtblCall(11, address, eventHandler.address, token);
}

public int add_HistoryChanged(IUnknown eventHandler, long[] token) {
	return COM.VtblCall(13, address, eventHandler.address, token);
}

public int add_NavigationCompleted(IUnknown eventHandler, long[] token) {
	return COM.VtblCall(15, address, eventHandler.address, token);
}

public int add_FrameNavigationStarting(IUnknown eventHandler, long[] token) {
	return COM.VtblCall(17, address, eventHandler.address, token);
}

public int add_FrameNavigationCompleted(IUnknown eventHandler, long[] token) {
	return COM.VtblCall(19, address, eventHandler.address, token);
}

public int AddScriptToExecuteOnDocumentCreated(char[] javaScript, long handler) {
	return COM.VtblCall(27, address, javaScript, handler);
}

public int ExecuteScript(char[] javaScript, IUnknown handler) {
	return COM.VtblCall(29, address, javaScript, handler.address);
}

public int Reload() {
	return COM.VtblCall(31, address);
}

public int PostWebMessageAsJson(char[] webMessageAsJson) {
	return COM.VtblCall(32, address, webMessageAsJson);
}

public int add_WebMessageReceived(long handler, long[] token) {
	return COM.VtblCall(34, address, handler, token);
}

public int get_CanGoBack(int[] canGoBack) {
	return COM.VtblCall(38, address, canGoBack);
}

public int get_CanGoForward(int[] canGoForward) {
	return COM.VtblCall(39, address, canGoForward);
}

public int GoBack() {
	return COM.VtblCall(40, address);
}

public int GoForward() {
	return COM.VtblCall(41, address);
}

public int Stop() {
	return COM.VtblCall(43, address);
}

public int add_NewWindowRequested(IUnknown eventHandler, long[] token) {
	return COM.VtblCall(44, address, eventHandler.address, token);
}

public int add_DocumentTitleChanged(IUnknown eventHandler, long[] token) {
	return COM.VtblCall(46, address, eventHandler.address, token);
}

public int get_DocumentTitle(long[] title) {
	return COM.VtblCall(48, address, title);
}

public int AddHostObjectToScript(char[] name, long[] object) {
	return COM.VtblCall(49, address, name, object);
}

public int add_ContainsFullScreenElementChanged(long eventHandler, long[] token) {
	return COM.VtblCall(52, address, eventHandler, token);
}

public int get_ContainsFullScreenElement(int[] containsFullScreenElement) {
	return COM.VtblCall(54, address, containsFullScreenElement);
}

public int add_WindowCloseRequested(IUnknown eventHandler, long[] token) {
	return COM.VtblCall(59, address, eventHandler.address, token);
}

}
