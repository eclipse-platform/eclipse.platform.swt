/*******************************************************************************
 * Copyright (c) 2009, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import com.trolltech.qt.core.QUrl;
import com.trolltech.qt.webkit.QWebView;

import org.eclipse.swt.widgets.Composite;

/**
 *
 */
public class QtWebkit extends WebBrowser {

	private QWebView webView = null;
	private int style;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.browser.WebBrowser#create(org.eclipse.swt.widgets.Composite
	 * , int)
	 */
	@Override
	public void create(Composite parent, int style) {
		this.style = style;
		webView = new QWebView(parent.getQWidget());
		webView.loadProgress.connect(this, "loadProgress(int)"); //$NON-NLS-1$
		webView.loadFinished.connect(this, "loadFinished(boolean)"); //$NON-NLS-1$
		webView.titleChanged.connect(this, "titleChanged(java.lang.String)"); //$NON-NLS-1$

	}

	public void loadProgress(int progress) {
		ProgressEvent progressEvent = new ProgressEvent(browser);
		progressEvent.display = browser.getDisplay();
		progressEvent.widget = browser;
		progressEvent.current = progress;
		for (int i = 0; i < progressListeners.length; i++) {
			progressListeners[i].changed(progressEvent);
		}
	}

	public void loadFinished(boolean OK) {
		ProgressEvent progressEvent = new ProgressEvent(browser);
		progressEvent.display = browser.getDisplay();
		progressEvent.widget = browser;
		for (int i = 0; i < progressListeners.length; i++) {
			progressListeners[i].completed(progressEvent);
		}
	}

	public void titleChanged(String title) {
		TitleEvent newEvent = new TitleEvent(browser);
		newEvent.display = browser.getDisplay();
		newEvent.widget = browser;
		newEvent.title = title;
		for (int i = 0; i < titleListeners.length; i++) {
			titleListeners[i].changed(newEvent);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.browser.WebBrowser#execute(java.lang.String)
	 */
	@Override
	public boolean execute(String script) {
		webView.page().mainFrame().evaluateJavaScript(script);
		webView.repaint(); // TODO seems to help for SWT Snippet161 but is a repaint really necessary ?
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.browser.WebBrowser#back()
	 */
	@Override
	public boolean back() {
		webView.back();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.browser.WebBrowser#forward()
	 */
	@Override
	public boolean forward() {
		webView.forward();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.browser.WebBrowser#getBrowserType()
	 */
	@Override
	public String getBrowserType() {
		// TODO Auto-generated method stub
		return "qtWebkit";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.browser.WebBrowser#getText()
	 */
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.browser.WebBrowser#getUrl()
	 */
	@Override
	public String getUrl() {
		return webView.url().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.browser.WebBrowser#isBackEnabled()
	 */
	@Override
	public boolean isBackEnabled() {
		return webView.history().canGoBack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.browser.WebBrowser#isForwardEnabled()
	 */
	@Override
	public boolean isForwardEnabled() {
		return webView.history().canGoForward();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.browser.WebBrowser#refresh()
	 */
	@Override
	public void refresh() {
		webView.reload();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.browser.WebBrowser#setText(java.lang.String)
	 */
	@Override
	public boolean setText(String html) {
		webView.setHtml(html);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.browser.WebBrowser#setUrl(java.lang.String)
	 */
	@Override
	public boolean setUrl(String url) {
		webView.load(new QUrl(url));
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.browser.WebBrowser#stop()
	 */
	@Override
	public void stop() {
		webView.stop();
	}

}
