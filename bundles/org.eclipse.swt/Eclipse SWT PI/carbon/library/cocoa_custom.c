/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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

#define Cocoa_NATIVE(func) Java_org_eclipse_swt_internal_cocoa_Cocoa_##func

extern id objc_msgSend(id, SEL, ...);

#ifndef NO_objc_1msgSend__IIF
JNIEXPORT jint JNICALL Cocoa_NATIVE(objc_1msgSend__IIF)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jfloat arg2)
{
	jint rc = 0;
	Cocoa_NATIVE_ENTER(env, that, objc_1msgSend__IIF_FUNC);	
	rc = ((jint (*) (id, SEL, float))objc_msgSend)((id)arg0, (SEL)arg1, arg2);
	Cocoa_NATIVE_EXIT(env, that, objc_1msgSend__IIF_FUNC);
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

- (void)getLocation:(NSPoint *)pt
{
	NSRect rect = [self frame];
	NSRect windowRect = [[self window] frame];
	pt->x += rect.size.width / 2;
	pt->y += rect.size.height;
	*pt = [self convertPoint: *pt toView: 0];
	pt->x += windowRect.origin.x;
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

@end

