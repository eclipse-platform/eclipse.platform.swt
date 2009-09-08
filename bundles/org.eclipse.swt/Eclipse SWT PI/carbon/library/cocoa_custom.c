/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "cocoa_structs.h"
#include "cocoa_stats.h"

#include <dlfcn.h>

#define Cocoa_NATIVE(func) Java_org_eclipse_swt_internal_cocoa_Cocoa_##func

extern id objc_msgSend(id, SEL, ...);

#ifndef NO_HIJavaViewCreateWithCocoaView
JNIEXPORT jint JNICALL Cocoa_NATIVE(HIJavaViewCreateWithCocoaView)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1)
{
	jint *lparg0=NULL;
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, HIJavaViewCreateWithCocoaView_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	/*
	rc = (jint)HIJavaViewCreateWithCocoaView(lparg0, arg1);
	*/
	{
		static int initialized = 0;
		static void *fp = NULL;
		if (!initialized) {
			void* handle = dlopen("/System/Library/Frameworks/JavaVM.framework/Libraries/libframeembedding.jnilib", RTLD_LAZY);
			if (handle) fp = dlsym(handle, "HIJavaViewCreateWithCocoaView");
			initialized = 1;
		}
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jint *, jint))fp)(lparg0, arg1);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	Cocoa_NATIVE_EXIT(env, that, HIJavaViewCreateWithCocoaView_FUNC);
	return rc;
}
#endif

@interface NSStatusItemImageView : NSImageView
{
	int user_data;
	int (*proc) (int sender, int user_data, int selector, void * arg0);
}
@end

@implementation NSStatusItemImageView

- (id)initWithProc:(id)prc frame:(NSRect)rect user_data:(int)data
{
    [super initWithFrame: rect];
    proc= (void *) prc;
    user_data = data;
    return self;
}

- (void)mouseDown:(NSEvent *)event
{
	proc((int)self, user_data, 0, event);
}

- (void)mouseUp:(NSEvent *)event
{
	proc((int)self, user_data, 1, event);
}

- (void)rightMouseDown:(NSEvent *)event
{
	proc((int)self, user_data, 2, event);
}

- (void)drawRect:(NSRect) rect
{
	proc((int)self, user_data, 3, &rect);
	[super drawRect: rect];
}
@end

@interface WebKitDelegate : NSObject
{
	int user_data;
	int (*proc) (int sender, int user_data, int selector, int arg0, int arg1, int arg2, int arg3);
}
@end

@implementation WebKitDelegate

- (id)initWithProc:(id)prc user_data:(int)data
{
    [super init];
    proc= (void *) prc;
    user_data = data;
    return self;
}

/* WebFrameLoadDelegate */

- (void)webView:(WebView *)sender didFailProvisionalLoadWithError:(NSError *)error forFrame:(WebFrame *)frame
{
	proc((int)sender, user_data, 1, (int)error, (int)frame, 0, 0);
}

- (void)webView:(WebView *)sender didFinishLoadForFrame:(WebFrame *)frame
{
	proc((int)sender, user_data, 2, (int)frame, 0, 0, 0);
}

- (void)webView:(WebView *)sender didReceiveTitle:(NSString *)title forFrame:(WebFrame *)frame
{
	proc((int)sender, user_data, 3, (int)title, (int)frame, 0, 0);
}

- (void)webView:(WebView *)sender didStartProvisionalLoadForFrame:(WebFrame *)frame
{
	proc((int)sender, user_data, 4, (int)frame, 0, 0, 0);
}

- (void)webView:(WebView *)sender didCommitLoadForFrame:(WebFrame *)frame
{
	proc((int)sender, user_data, 10, (int)frame, 0, 0, 0);
}

- (void)webView:(WebView *)sender didChangeLocationWithinPageForFrame:(WebFrame *)frame
{
	proc((int)sender, user_data, 31, (int)frame, 0, 0, 0);
}

- (void)webView:(WebView *)sender windowScriptObjectAvailable:(WebScriptObject *)windowScriptObject
{
	proc((int)sender, user_data, 33, (int)windowScriptObject, 0, 0, 0);
}

/* WebResourceLoadDelegate */

- (void)webView:(WebView *)sender resource:(id)identifier didFinishLoadingFromDataSource:(WebDataSource *)dataSource
{
	proc((int)sender, user_data, 5, (int)identifier, (int)dataSource, 0, 0);
}

- (void)webView:(WebView *)sender resource:(id)identifier didFailLoadingWithError:(NSError *)error fromDataSource:(WebDataSource *)dataSource
{
	proc((int)sender, user_data, 6, (int)identifier, (int)error, (int)dataSource, 0);
}

- (id)webView:(WebView *)sender identifierForInitialRequest:(NSURLRequest *)request fromDataSource:(WebDataSource *)dataSource
{
    return (id) proc((int)sender, user_data, 7, (int)request, (int)dataSource, 0, 0);    
}

- (NSURLRequest *)webView:(WebView *)sender resource:(id)identifier willSendRequest:(NSURLRequest *)request redirectResponse:(NSURLResponse *)redirectResponse fromDataSource:(WebDataSource *)dataSource
{
	return (NSURLRequest *) proc((int)sender, user_data, 8, (int)identifier, (int)request, (int)redirectResponse, (int)dataSource);
}

- (void)webView:(WebView *)sender resource:(id)identifier didReceiveAuthenticationChallenge:(NSURLAuthenticationChallenge *)challenge fromDataSource:(WebDataSource *)dataSource
{
	proc((int)sender, user_data, 35, (int)identifier, (int)challenge, (int)dataSource, 0);
}

/* handleNotification */

- (void)handleNotification:(NSNotification *)notification
{
	proc((int)[notification object], user_data, 9, (int)notification, 0, 0, 0);
}

/* UIDelegate */

- (WebView *)webView:(WebView *)sender createWebViewWithRequest:(NSURLRequest *)request
{
	return (WebView *) proc((int)sender, user_data, 11, (int)request, 0, 0, 0);
}

- (void)webViewShow:(WebView *)sender
{
	proc((int)sender, user_data, 12, 0, 0, 0, 0);
}

- (void)webView:(WebView *)sender setFrame:(NSRect)frame
{
	proc((int)sender, user_data, 13, (int)&frame, 0, 0, 0);
}

- (void)webViewClose:(WebView *)sender
{
	proc((int)sender, user_data, 14, 0, 0, 0, 0);
}

- (NSArray *)webView:(WebView *)sender contextMenuItemsForElement:(NSDictionary *)element defaultMenuItems:(NSArray *)defaultMenuItems
{
	return (NSArray *)proc((int)sender, user_data, 15, (int)element, (int)defaultMenuItems, 0, 0);
}

- (void)webView:(WebView *)sender setStatusBarVisible:(BOOL)visible
{
	proc((int)sender, user_data, 16, (int)visible, 0, 0, 0);
}

- (void)webView:(WebView *)sender setResizable:(BOOL)resizable
{
	proc((int)sender, user_data, 17, (int)resizable, 0, 0, 0);
}

- (void)webView:(WebView *)sender setToolbarsVisible:(BOOL)visible
{
	proc((int)sender, user_data, 18, (int)visible, 0, 0, 0);
}

- (void)webView:(WebView *)sender setStatusText:(NSString *)text
{
	proc((int)sender, user_data, 23, (int)text, 0, 0, 0);
}

- (void)webViewFocus:(WebView *)sender
{
	proc((int)sender, user_data, 24, 0, 0, 0, 0);
}

- (void)webViewUnfocus:(WebView *)sender
{
	proc((int)sender, user_data, 25, 0, 0, 0, 0);
}

- (BOOL)webView:(WebView *)sender runBeforeUnloadConfirmPanelWithMessage:(NSString *)message initiatedByFrame:(WebFrame *)frame
{
	return proc((int)sender, user_data, 36, (int)message, (int)frame, 0, 0);
}

- (void)webView:(WebView *)sender runJavaScriptAlertPanelWithMessage:(NSString *)message
{
	proc((int)sender, user_data, 26, (int)message, 0, 0, 0);
}

- (BOOL)webView:(WebView *)sender runJavaScriptConfirmPanelWithMessage:(NSString *)message
{
	return (BOOL) proc((int)sender, user_data, 27, (int)message, 0, 0, 0);
}

- (void)webView:(WebView *)sender runOpenPanelForFileButtonWithResultListener:(id<WebOpenPanelResultListener>)resultListener
{
	proc((int)sender, user_data, 28, (int)resultListener, 0, 0, 0);
}

- (void)webView:(WebView *)sender mouseDidMoveOverElement:(NSDictionary *)elementInformation modifierFlags:(unsigned int)modifierFlags
{
	proc((int)sender, user_data, 30, (int)elementInformation, (int)modifierFlags, 0, 0);
}

/* WebPolicyDelegate */
- (void)webView:(WebView *)sender decidePolicyForMIMEType:(NSString *)type request:(NSURLRequest *)request frame:(WebFrame*)frame decisionListener:(id<WebPolicyDecisionListener>)listener
{
	proc((int)sender, user_data, 19, (int)type, (int)request, (int)frame, (int)listener);
}

- (void)webView:(WebView *)sender decidePolicyForNavigationAction:(NSDictionary *)actionInformation request:(NSURLRequest *)request frame:(WebFrame *)frame decisionListener:(id<WebPolicyDecisionListener>)listener
{
	proc((int)sender, user_data, 20, (int)actionInformation, (int)request, (int)frame, (int)listener);
}


- (void)webView:(WebView *)sender decidePolicyForNewWindowAction:(NSDictionary *)actionInformation request:(NSURLRequest *)request newFrameName:(NSString *)frameName decisionListener:(id<WebPolicyDecisionListener>)listener
{
	proc((int)sender, user_data, 21, (int)actionInformation, (int)request, (int)frameName, (int)listener);
}


- (void)webView:(WebView *)sender unableToImplementPolicyWithError:(NSError *)error frame:(WebFrame *)frame
{
	proc((int)sender, user_data, 22, (int)error, (int)frame, 0, 0);
}

/* WebDownload */

- (void)download:(NSURLDownload *)download decideDestinationWithSuggestedFilename:(NSString *)filename
{
	proc((int)download, user_data, 29, (int)download, (int)filename, 0, 0);
}

/* DOMEventListener */

- (void)handleEvent:(DOMEvent *)evt
{
	proc((int)evt, user_data, 32, (int)evt, 0, 0, 0);
}

/* WebScripting */

+ (BOOL)isSelectorExcludedFromWebScript:(SEL)aSelector
{
	return aSelector != @selector(callJava:index:arg:);
}

+ (NSString *)webScriptNameForSelector:(SEL)aSelector
{
	if (aSelector == @selector(callJava:index:arg:)) {
		return @"callJava";
	}
	return 0;
}

/* external */

- (id)callJava:(NSObject *)arg index:(NSObject *)index arg:(NSObject *)arg0
{
	return (id)proc(0, user_data, 34, (int)arg, (int)index, (int)arg0, 0);
}

@end

