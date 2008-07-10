/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSObject extends id {

public NSObject() {
	super();
}

public NSObject(int id) {
	super(id);
}

public void URL_resourceDataDidBecomeAvailable_(NSURL sender, NSData newBytes) {
	OS.objc_msgSend(this.id, OS.sel_URL_1resourceDataDidBecomeAvailable_1, sender != null ? sender.id : 0, newBytes != null ? newBytes.id : 0);
}

public void URL_resourceDidFailLoadingWithReason_(NSURL sender, NSString reason) {
	OS.objc_msgSend(this.id, OS.sel_URL_1resourceDidFailLoadingWithReason_1, sender != null ? sender.id : 0, reason != null ? reason.id : 0);
}

public void URLHandle_resourceDataDidBecomeAvailable_(NSURLHandle sender, NSData newBytes) {
	OS.objc_msgSend(this.id, OS.sel_URLHandle_1resourceDataDidBecomeAvailable_1, sender != null ? sender.id : 0, newBytes != null ? newBytes.id : 0);
}

public void URLHandle_resourceDidFailLoadingWithReason_(NSURLHandle sender, NSString reason) {
	OS.objc_msgSend(this.id, OS.sel_URLHandle_1resourceDidFailLoadingWithReason_1, sender != null ? sender.id : 0, reason != null ? reason.id : 0);
}

public void URLHandleResourceDidBeginLoading(NSURLHandle sender) {
	OS.objc_msgSend(this.id, OS.sel_URLHandleResourceDidBeginLoading_1, sender != null ? sender.id : 0);
}

public void URLHandleResourceDidCancelLoading(NSURLHandle sender) {
	OS.objc_msgSend(this.id, OS.sel_URLHandleResourceDidCancelLoading_1, sender != null ? sender.id : 0);
}

public void URLHandleResourceDidFinishLoading(NSURLHandle sender) {
	OS.objc_msgSend(this.id, OS.sel_URLHandleResourceDidFinishLoading_1, sender != null ? sender.id : 0);
}

public void URLProtocol_cachedResponseIsValid_(NSURLProtocol protocol, NSCachedURLResponse cachedResponse) {
	OS.objc_msgSend(this.id, OS.sel_URLProtocol_1cachedResponseIsValid_1, protocol != null ? protocol.id : 0, cachedResponse != null ? cachedResponse.id : 0);
}

public void URLProtocol_didCancelAuthenticationChallenge_(NSURLProtocol protocol, NSURLAuthenticationChallenge challenge) {
	OS.objc_msgSend(this.id, OS.sel_URLProtocol_1didCancelAuthenticationChallenge_1, protocol != null ? protocol.id : 0, challenge != null ? challenge.id : 0);
}

public void URLProtocol_didFailWithError_(NSURLProtocol protocol, NSError error) {
	OS.objc_msgSend(this.id, OS.sel_URLProtocol_1didFailWithError_1, protocol != null ? protocol.id : 0, error != null ? error.id : 0);
}

public void URLProtocol_didLoadData_(NSURLProtocol protocol, NSData data) {
	OS.objc_msgSend(this.id, OS.sel_URLProtocol_1didLoadData_1, protocol != null ? protocol.id : 0, data != null ? data.id : 0);
}

public void URLProtocol_didReceiveAuthenticationChallenge_(NSURLProtocol protocol, NSURLAuthenticationChallenge challenge) {
	OS.objc_msgSend(this.id, OS.sel_URLProtocol_1didReceiveAuthenticationChallenge_1, protocol != null ? protocol.id : 0, challenge != null ? challenge.id : 0);
}

public void URLProtocol_didReceiveResponse_cacheStoragePolicy_(NSURLProtocol protocol, NSURLResponse response, int policy) {
	OS.objc_msgSend(this.id, OS.sel_URLProtocol_1didReceiveResponse_1cacheStoragePolicy_1, protocol != null ? protocol.id : 0, response != null ? response.id : 0, policy);
}

public void URLProtocol_wasRedirectedToRequest_redirectResponse_(NSURLProtocol protocol, NSURLRequest request, NSURLResponse redirectResponse) {
	OS.objc_msgSend(this.id, OS.sel_URLProtocol_1wasRedirectedToRequest_1redirectResponse_1, protocol != null ? protocol.id : 0, request != null ? request.id : 0, redirectResponse != null ? redirectResponse.id : 0);
}

public void URLProtocolDidFinishLoading(NSURLProtocol protocol) {
	OS.objc_msgSend(this.id, OS.sel_URLProtocolDidFinishLoading_1, protocol != null ? protocol.id : 0);
}

public void URLResourceDidCancelLoading(NSURL sender) {
	OS.objc_msgSend(this.id, OS.sel_URLResourceDidCancelLoading_1, sender != null ? sender.id : 0);
}

public void URLResourceDidFinishLoading(NSURL sender) {
	OS.objc_msgSend(this.id, OS.sel_URLResourceDidFinishLoading_1, sender != null ? sender.id : 0);
}

public static boolean accessInstanceVariablesDirectly() {
	return OS.objc_msgSend(OS.class_NSObject, OS.sel_accessInstanceVariablesDirectly) != 0;
}

public void addObserver(NSObject observer, NSString keyPath, int options, int context) {
	OS.objc_msgSend(this.id, OS.sel_addObserver_1forKeyPath_1options_1context_1, observer != null ? observer.id : 0, keyPath != null ? keyPath.id : 0, options, context);
}

public NSObject alloc() {
	int result = OS.objc_msgSend(get_class(), OS.sel_alloc);
	this.id = result;
	return result != 0 ? this : null;
}

public int get_class() {
	String name = getClass().getName();
	int index = name.lastIndexOf('.');
	if (index != -1) name = name.substring(index + 1);
	return OS.objc_getClass(name);
}

public static id allocWithZone(int zone) {
	int result = OS.objc_msgSend(OS.class_NSObject, OS.sel_allocWithZone_1, zone);
	return result != 0 ? new id(result) : null;
}

public void archiver_didEncodeObject_(NSKeyedArchiver archiver, id object) {
	OS.objc_msgSend(this.id, OS.sel_archiver_1didEncodeObject_1, archiver != null ? archiver.id : 0, object != null ? object.id : 0);
}

public id archiver_willEncodeObject_(NSKeyedArchiver archiver, id object) {
	int result = OS.objc_msgSend(this.id, OS.sel_archiver_1willEncodeObject_1, archiver != null ? archiver.id : 0, object != null ? object.id : 0);
	return result != 0 ? new id(result) : null;
}

public void archiver_willReplaceObject_withObject_(NSKeyedArchiver archiver, id object, id newObject) {
	OS.objc_msgSend(this.id, OS.sel_archiver_1willReplaceObject_1withObject_1, archiver != null ? archiver.id : 0, object != null ? object.id : 0, newObject != null ? newObject.id : 0);
}

public void archiverDidFinish(NSKeyedArchiver archiver) {
	OS.objc_msgSend(this.id, OS.sel_archiverDidFinish_1, archiver != null ? archiver.id : 0);
}

public void archiverWillFinish(NSKeyedArchiver archiver) {
	OS.objc_msgSend(this.id, OS.sel_archiverWillFinish_1, archiver != null ? archiver.id : 0);
}

public boolean attemptRecoveryFromError_optionIndex_(NSError error, int recoveryOptionIndex) {
	return OS.objc_msgSend(this.id, OS.sel_attemptRecoveryFromError_1optionIndex_1, error != null ? error.id : 0, recoveryOptionIndex) != 0;
}

public void attemptRecoveryFromError_optionIndex_delegate_didRecoverSelector_contextInfo_(NSError error, int recoveryOptionIndex, id delegate, int didRecoverSelector, int contextInfo) {
	OS.objc_msgSend(this.id, OS.sel_attemptRecoveryFromError_1optionIndex_1delegate_1didRecoverSelector_1contextInfo_1, error != null ? error.id : 0, recoveryOptionIndex, delegate != null ? delegate.id : 0, didRecoverSelector, contextInfo);
}

public NSArray attributeKeys() {
	int result = OS.objc_msgSend(this.id, OS.sel_attributeKeys);
	return result != 0 ? new NSArray(result) : null;
}

public boolean authenticateComponents(NSArray components, NSData signature) {
	return OS.objc_msgSend(this.id, OS.sel_authenticateComponents_1withData_1, components != null ? components.id : 0, signature != null ? signature.id : 0) != 0;
}

public NSData authenticationDataForComponents(NSArray components) {
	int result = OS.objc_msgSend(this.id, OS.sel_authenticationDataForComponents_1, components != null ? components.id : 0);
	return result != 0 ? new NSData(result) : null;
}

public static boolean automaticallyNotifiesObserversForKey(NSString key) {
	return OS.objc_msgSend(OS.class_NSObject, OS.sel_automaticallyNotifiesObserversForKey_1, key != null ? key.id : 0) != 0;
}

public id autorelease() {
	int result = OS.objc_msgSend(this.id, OS.sel_autorelease);
	return result != 0 ? new id(result) : null;
}

public id awakeAfterUsingCoder(NSCoder aDecoder) {
	int result = OS.objc_msgSend(this.id, OS.sel_awakeAfterUsingCoder_1, aDecoder != null ? aDecoder.id : 0);
	return result != 0 ? new id(result) : null;
}

public void cancelAuthenticationChallenge(NSURLAuthenticationChallenge challenge) {
	OS.objc_msgSend(this.id, OS.sel_cancelAuthenticationChallenge_1, challenge != null ? challenge.id : 0);
}

public boolean conformsToProtocol (int protocol) {
	return OS.objc_msgSend (id, OS.sel_conformsToProtocol_1, protocol) != 0;
}

public static void static_cancelPreviousPerformRequestsWithTarget_(id aTarget) {
	OS.objc_msgSend(OS.class_NSObject, OS.sel_cancelPreviousPerformRequestsWithTarget_1, aTarget != null ? aTarget.id : 0);
}

public static void static_cancelPreviousPerformRequestsWithTarget_selector_object_(id aTarget, int aSelector, id anArgument) {
	OS.objc_msgSend(OS.class_NSObject, OS.sel_cancelPreviousPerformRequestsWithTarget_1selector_1object_1, aTarget != null ? aTarget.id : 0, aSelector, anArgument != null ? anArgument.id : 0);
}

public static int static_class() {
	return OS.objc_msgSend(OS.class_NSObject, OS.sel_class);
}

//public int class() {
//	return OS.objc_msgSend(this.id, OS.sel_class);
//}

public int classCode() {
	return OS.objc_msgSend(this.id, OS.sel_classCode);
}

public NSClassDescription classDescription() {
	int result = OS.objc_msgSend(this.id, OS.sel_classDescription);
	return result != 0 ? new NSClassDescription(result) : null;
}

public static NSArray classFallbacksForKeyedArchiver() {
	int result = OS.objc_msgSend(OS.class_NSObject, OS.sel_classFallbacksForKeyedArchiver);
	return result != 0 ? new NSArray(result) : null;
}

public int classForArchiver() {
	return OS.objc_msgSend(this.id, OS.sel_classForArchiver);
}

public int classForCoder() {
	return OS.objc_msgSend(this.id, OS.sel_classForCoder);
}

public int classForKeyedArchiver() {
	return OS.objc_msgSend(this.id, OS.sel_classForKeyedArchiver);
}

public static int classForKeyedUnarchiver() {
	return OS.objc_msgSend(OS.class_NSObject, OS.sel_classForKeyedUnarchiver);
}

public int classForPortCoder() {
	return OS.objc_msgSend(this.id, OS.sel_classForPortCoder);
}

public NSString className() {
	int result = OS.objc_msgSend(this.id, OS.sel_className);
	return result != 0 ? new NSString(result) : null;
}

public id coerceValue(id value, NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_coerceValue_1forKey_1, value != null ? value.id : 0, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

//public boolean conformsToProtocol_(Protocol aProtocol) {
//	return OS.objc_msgSend(this.id, OS.sel_conformsToProtocol_1, aProtocol != null ? aProtocol.id : 0) != 0;
//}
//
//public static boolean static_conformsToProtocol_(Protocol protocol) {
//	return OS.objc_msgSend(OS.class_NSObject, OS.sel_conformsToProtocol_1, protocol != null ? protocol.id : 0) != 0;
//}

public void connection_didCancelAuthenticationChallenge_(NSURLConnection connection, NSURLAuthenticationChallenge challenge) {
	OS.objc_msgSend(this.id, OS.sel_connection_1didCancelAuthenticationChallenge_1, connection != null ? connection.id : 0, challenge != null ? challenge.id : 0);
}

public void connection_didFailWithError_(NSURLConnection connection, NSError error) {
	OS.objc_msgSend(this.id, OS.sel_connection_1didFailWithError_1, connection != null ? connection.id : 0, error != null ? error.id : 0);
}

public void connection_didReceiveAuthenticationChallenge_(NSURLConnection connection, NSURLAuthenticationChallenge challenge) {
	OS.objc_msgSend(this.id, OS.sel_connection_1didReceiveAuthenticationChallenge_1, connection != null ? connection.id : 0, challenge != null ? challenge.id : 0);
}

public void connection_didReceiveData_(NSURLConnection connection, NSData data) {
	OS.objc_msgSend(this.id, OS.sel_connection_1didReceiveData_1, connection != null ? connection.id : 0, data != null ? data.id : 0);
}

public void connection_didReceiveResponse_(NSURLConnection connection, NSURLResponse response) {
	OS.objc_msgSend(this.id, OS.sel_connection_1didReceiveResponse_1, connection != null ? connection.id : 0, response != null ? response.id : 0);
}

public boolean connection_handleRequest_(NSConnection connection, NSDistantObjectRequest doreq) {
	return OS.objc_msgSend(this.id, OS.sel_connection_1handleRequest_1, connection != null ? connection.id : 0, doreq != null ? doreq.id : 0) != 0;
}

public boolean connection_shouldMakeNewConnection_(NSConnection ancestor, NSConnection conn) {
	return OS.objc_msgSend(this.id, OS.sel_connection_1shouldMakeNewConnection_1, ancestor != null ? ancestor.id : 0, conn != null ? conn.id : 0) != 0;
}

public NSCachedURLResponse connection_willCacheResponse_(NSURLConnection connection, NSCachedURLResponse cachedResponse) {
	int result = OS.objc_msgSend(this.id, OS.sel_connection_1willCacheResponse_1, connection != null ? connection.id : 0, cachedResponse != null ? cachedResponse.id : 0);
	return result != 0 ? new NSCachedURLResponse(result) : null;
}

public NSURLRequest connection_willSendRequest_redirectResponse_(NSURLConnection connection, NSURLRequest request, NSURLResponse response) {
	int result = OS.objc_msgSend(this.id, OS.sel_connection_1willSendRequest_1redirectResponse_1, connection != null ? connection.id : 0, request != null ? request.id : 0, response != null ? response.id : 0);
	return result != 0 ? new NSURLRequest(result) : null;
}

public void connectionDidFinishLoading(NSURLConnection connection) {
	OS.objc_msgSend(this.id, OS.sel_connectionDidFinishLoading_1, connection != null ? connection.id : 0);
}

public void continueWithoutCredentialForAuthenticationChallenge(NSURLAuthenticationChallenge challenge) {
	OS.objc_msgSend(this.id, OS.sel_continueWithoutCredentialForAuthenticationChallenge_1, challenge != null ? challenge.id : 0);
}

public id copy() {
	int result = OS.objc_msgSend(this.id, OS.sel_copy);
	return result != 0 ? new id(result) : null;
}

public id copyScriptingValue(id value, NSString key, NSDictionary properties) {
	int result = OS.objc_msgSend(this.id, OS.sel_copyScriptingValue_1forKey_1withProperties_1, value != null ? value.id : 0, key != null ? key.id : 0, properties != null ? properties.id : 0);
	return result != 0 ? new id(result) : null;
}

public id copyWithZone_(int zone) {
	int result = OS.objc_msgSend(this.id, OS.sel_copyWithZone_1, zone);
	return result != 0 ? new id(result) : null;
}

public static id static_copyWithZone_(int zone) {
	int result = OS.objc_msgSend(OS.class_NSObject, OS.sel_copyWithZone_1, zone);
	return result != 0 ? new id(result) : null;
}

public int countByEnumeratingWithState(int state, int stackbuf, int len) {
	return OS.objc_msgSend(this.id, OS.sel_countByEnumeratingWithState_1objects_1count_1, state, stackbuf, len);
}

public id createConversationForConnection(NSConnection conn) {
	int result = OS.objc_msgSend(this.id, OS.sel_createConversationForConnection_1, conn != null ? conn.id : 0);
	return result != 0 ? new id(result) : null;
}

public void dealloc() {
	OS.objc_msgSend(this.id, OS.sel_dealloc);
}

public NSString description() {
	int result = OS.objc_msgSend(this.id, OS.sel_description);
	return result != 0 ? new NSString(result) : null;
}

public static NSString static_description() {
	int result = OS.objc_msgSend(OS.class_NSObject, OS.sel_description);
	return result != 0 ? new NSString(result) : null;
}

public NSDictionary dictionaryWithValuesForKeys(NSArray keys) {
	int result = OS.objc_msgSend(this.id, OS.sel_dictionaryWithValuesForKeys_1, keys != null ? keys.id : 0);
	return result != 0 ? new NSDictionary(result) : null;
}

public void didChange(int changeKind, NSIndexSet indexes, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_didChange_1valuesAtIndexes_1forKey_1, changeKind, indexes != null ? indexes.id : 0, key != null ? key.id : 0);
}

public void didChangeValueForKey_(NSString key) {
	OS.objc_msgSend(this.id, OS.sel_didChangeValueForKey_1, key != null ? key.id : 0);
}

public void didChangeValueForKey_withSetMutation_usingObjects_(NSString key, int mutationKind, NSSet objects) {
	OS.objc_msgSend(this.id, OS.sel_didChangeValueForKey_1withSetMutation_1usingObjects_1, key != null ? key.id : 0, mutationKind, objects != null ? objects.id : 0);
}

public boolean doesContain(id object) {
	return OS.objc_msgSend(this.id, OS.sel_doesContain_1, object != null ? object.id : 0) != 0;
}

public void doesNotRecognizeSelector(int aSelector) {
	OS.objc_msgSend(this.id, OS.sel_doesNotRecognizeSelector_1, aSelector);
}

public void download_decideDestinationWithSuggestedFilename_(NSURLDownload download, NSString filename) {
	OS.objc_msgSend(this.id, OS.sel_download_1decideDestinationWithSuggestedFilename_1, download != null ? download.id : 0, filename != null ? filename.id : 0);
}

public void download_didCancelAuthenticationChallenge_(NSURLDownload download, NSURLAuthenticationChallenge challenge) {
	OS.objc_msgSend(this.id, OS.sel_download_1didCancelAuthenticationChallenge_1, download != null ? download.id : 0, challenge != null ? challenge.id : 0);
}

public void download_didCreateDestination_(NSURLDownload download, NSString path) {
	OS.objc_msgSend(this.id, OS.sel_download_1didCreateDestination_1, download != null ? download.id : 0, path != null ? path.id : 0);
}

public void download_didFailWithError_(NSURLDownload download, NSError error) {
	OS.objc_msgSend(this.id, OS.sel_download_1didFailWithError_1, download != null ? download.id : 0, error != null ? error.id : 0);
}

public void download_didReceiveAuthenticationChallenge_(NSURLDownload download, NSURLAuthenticationChallenge challenge) {
	OS.objc_msgSend(this.id, OS.sel_download_1didReceiveAuthenticationChallenge_1, download != null ? download.id : 0, challenge != null ? challenge.id : 0);
}

public void download_didReceiveDataOfLength_(NSURLDownload download, int length) {
	OS.objc_msgSend(this.id, OS.sel_download_1didReceiveDataOfLength_1, download != null ? download.id : 0, length);
}

public void download_didReceiveResponse_(NSURLDownload download, NSURLResponse response) {
	OS.objc_msgSend(this.id, OS.sel_download_1didReceiveResponse_1, download != null ? download.id : 0, response != null ? response.id : 0);
}

public boolean download_shouldDecodeSourceDataOfMIMEType_(NSURLDownload download, NSString encodingType) {
	return OS.objc_msgSend(this.id, OS.sel_download_1shouldDecodeSourceDataOfMIMEType_1, download != null ? download.id : 0, encodingType != null ? encodingType.id : 0) != 0;
}

public void download_willResumeWithResponse_fromByte_(NSURLDownload download, NSURLResponse response, long startingByte) {
	OS.objc_msgSend(this.id, OS.sel_download_1willResumeWithResponse_1fromByte_1, download != null ? download.id : 0, response != null ? response.id : 0, startingByte);
}

public NSURLRequest download_willSendRequest_redirectResponse_(NSURLDownload download, NSURLRequest request, NSURLResponse redirectResponse) {
	int result = OS.objc_msgSend(this.id, OS.sel_download_1willSendRequest_1redirectResponse_1, download != null ? download.id : 0, request != null ? request.id : 0, redirectResponse != null ? redirectResponse.id : 0);
	return result != 0 ? new NSURLRequest(result) : null;
}

public void downloadDidBegin(NSURLDownload download) {
	OS.objc_msgSend(this.id, OS.sel_downloadDidBegin_1, download != null ? download.id : 0);
}

public void downloadDidFinish(NSURLDownload download) {
	OS.objc_msgSend(this.id, OS.sel_downloadDidFinish_1, download != null ? download.id : 0);
}

public void encodeWithCoder(NSCoder aCoder) {
	OS.objc_msgSend(this.id, OS.sel_encodeWithCoder_1, aCoder != null ? aCoder.id : 0);
}

public NSDecimalNumber exceptionDuringOperation(int operation, int error, NSDecimalNumber leftOperand, NSDecimalNumber rightOperand) {
	int result = OS.objc_msgSend(this.id, OS.sel_exceptionDuringOperation_1error_1leftOperand_1rightOperand_1, operation, error, leftOperand != null ? leftOperand.id : 0, rightOperand != null ? rightOperand.id : 0);
	return result != 0 ? new NSDecimalNumber(result) : null;
}

public boolean fileManager_shouldCopyItemAtPath_toPath_(NSFileManager fileManager, NSString srcPath, NSString dstPath) {
	return OS.objc_msgSend(this.id, OS.sel_fileManager_1shouldCopyItemAtPath_1toPath_1, fileManager != null ? fileManager.id : 0, srcPath != null ? srcPath.id : 0, dstPath != null ? dstPath.id : 0) != 0;
}

public boolean fileManager_shouldLinkItemAtPath_toPath_(NSFileManager fileManager, NSString srcPath, NSString dstPath) {
	return OS.objc_msgSend(this.id, OS.sel_fileManager_1shouldLinkItemAtPath_1toPath_1, fileManager != null ? fileManager.id : 0, srcPath != null ? srcPath.id : 0, dstPath != null ? dstPath.id : 0) != 0;
}

public boolean fileManager_shouldMoveItemAtPath_toPath_(NSFileManager fileManager, NSString srcPath, NSString dstPath) {
	return OS.objc_msgSend(this.id, OS.sel_fileManager_1shouldMoveItemAtPath_1toPath_1, fileManager != null ? fileManager.id : 0, srcPath != null ? srcPath.id : 0, dstPath != null ? dstPath.id : 0) != 0;
}

public boolean fileManager_shouldProceedAfterError_(NSFileManager fm, NSDictionary errorInfo) {
	return OS.objc_msgSend(this.id, OS.sel_fileManager_1shouldProceedAfterError_1, fm != null ? fm.id : 0, errorInfo != null ? errorInfo.id : 0) != 0;
}

public boolean fileManager_shouldProceedAfterError_copyingItemAtPath_toPath_(NSFileManager fileManager, NSError error, NSString srcPath, NSString dstPath) {
	return OS.objc_msgSend(this.id, OS.sel_fileManager_1shouldProceedAfterError_1copyingItemAtPath_1toPath_1, fileManager != null ? fileManager.id : 0, error != null ? error.id : 0, srcPath != null ? srcPath.id : 0, dstPath != null ? dstPath.id : 0) != 0;
}

public boolean fileManager_shouldProceedAfterError_linkingItemAtPath_toPath_(NSFileManager fileManager, NSError error, NSString srcPath, NSString dstPath) {
	return OS.objc_msgSend(this.id, OS.sel_fileManager_1shouldProceedAfterError_1linkingItemAtPath_1toPath_1, fileManager != null ? fileManager.id : 0, error != null ? error.id : 0, srcPath != null ? srcPath.id : 0, dstPath != null ? dstPath.id : 0) != 0;
}

public boolean fileManager_shouldProceedAfterError_movingItemAtPath_toPath_(NSFileManager fileManager, NSError error, NSString srcPath, NSString dstPath) {
	return OS.objc_msgSend(this.id, OS.sel_fileManager_1shouldProceedAfterError_1movingItemAtPath_1toPath_1, fileManager != null ? fileManager.id : 0, error != null ? error.id : 0, srcPath != null ? srcPath.id : 0, dstPath != null ? dstPath.id : 0) != 0;
}

public boolean fileManager_shouldProceedAfterError_removingItemAtPath_(NSFileManager fileManager, NSError error, NSString path) {
	return OS.objc_msgSend(this.id, OS.sel_fileManager_1shouldProceedAfterError_1removingItemAtPath_1, fileManager != null ? fileManager.id : 0, error != null ? error.id : 0, path != null ? path.id : 0) != 0;
}

public boolean fileManager_shouldRemoveItemAtPath_(NSFileManager fileManager, NSString path) {
	return OS.objc_msgSend(this.id, OS.sel_fileManager_1shouldRemoveItemAtPath_1, fileManager != null ? fileManager.id : 0, path != null ? path.id : 0) != 0;
}

public void fileManager_willProcessPath_(NSFileManager fm, NSString path) {
	OS.objc_msgSend(this.id, OS.sel_fileManager_1willProcessPath_1, fm != null ? fm.id : 0, path != null ? path.id : 0);
}

public void finalizeXX() {
	OS.objc_msgSend(this.id, OS.sel_finalize);
}

public void forwardInvocation(NSInvocation anInvocation) {
	OS.objc_msgSend(this.id, OS.sel_forwardInvocation_1, anInvocation != null ? anInvocation.id : 0);
}

public void handleMachMessage(int msg) {
	OS.objc_msgSend(this.id, OS.sel_handleMachMessage_1, msg);
}

public void handlePortMessage(NSPortMessage message) {
	OS.objc_msgSend(this.id, OS.sel_handlePortMessage_1, message != null ? message.id : 0);
}

public id handleQueryWithUnboundKey(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_handleQueryWithUnboundKey_1, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public void handleTakeValue(id value, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_handleTakeValue_1forUnboundKey_1, value != null ? value.id : 0, key != null ? key.id : 0);
}

public int hash() {
	return OS.objc_msgSend(this.id, OS.sel_hash);
}

public NSArray indicesOfObjectsByEvaluatingObjectSpecifier(NSScriptObjectSpecifier specifier) {
	int result = OS.objc_msgSend(this.id, OS.sel_indicesOfObjectsByEvaluatingObjectSpecifier_1, specifier != null ? specifier.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSObject init() {
	int result = OS.objc_msgSend(this.id, OS.sel_init);
	return result != 0 ? this : null;
}

public NSObject initWithCoder(NSCoder aDecoder) {
	int result = OS.objc_msgSend(this.id, OS.sel_initWithCoder_1, aDecoder != null ? aDecoder.id : 0);
	return result != 0 ? this : null;
}

public static void initialize() {
	OS.objc_msgSend(OS.class_NSObject, OS.sel_initialize);
}

public void insertValue_atIndex_inPropertyWithKey_(id value, int index, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_insertValue_1atIndex_1inPropertyWithKey_1, value != null ? value.id : 0, index, key != null ? key.id : 0);
}

public void insertValue_inPropertyWithKey_(id value, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_insertValue_1inPropertyWithKey_1, value != null ? value.id : 0, key != null ? key.id : 0);
}

public static int instanceMethodForSelector(int aSelector) {
	return OS.objc_msgSend(OS.class_NSObject, OS.sel_instanceMethodForSelector_1, aSelector);
}

public static NSMethodSignature instanceMethodSignatureForSelector(int aSelector) {
	int result = OS.objc_msgSend(OS.class_NSObject, OS.sel_instanceMethodSignatureForSelector_1, aSelector);
	return result != 0 ? new NSMethodSignature(result) : null;
}

public static boolean instancesRespondToSelector(int aSelector) {
	return OS.objc_msgSend(OS.class_NSObject, OS.sel_instancesRespondToSelector_1, aSelector) != 0;
}

public NSString inverseForRelationshipKey(NSString relationshipKey) {
	int result = OS.objc_msgSend(this.id, OS.sel_inverseForRelationshipKey_1, relationshipKey != null ? relationshipKey.id : 0);
	return result != 0 ? new NSString(result) : null;
}

public boolean isCaseInsensitiveLike(NSString object) {
	return OS.objc_msgSend(this.id, OS.sel_isCaseInsensitiveLike_1, object != null ? object.id : 0) != 0;
}

public boolean isEqual(id object) {
	return OS.objc_msgSend(this.id, OS.sel_isEqual_1, object != null ? object.id : 0) != 0;
}

public boolean isEqualTo(id object) {
	return OS.objc_msgSend(this.id, OS.sel_isEqualTo_1, object != null ? object.id : 0) != 0;
}

public boolean isGreaterThan(id object) {
	return OS.objc_msgSend(this.id, OS.sel_isGreaterThan_1, object != null ? object.id : 0) != 0;
}

public boolean isGreaterThanOrEqualTo(id object) {
	return OS.objc_msgSend(this.id, OS.sel_isGreaterThanOrEqualTo_1, object != null ? object.id : 0) != 0;
}

public boolean isKindOfClass(int aClass) {
	return OS.objc_msgSend(this.id, OS.sel_isKindOfClass_1, aClass) != 0;
}

public boolean isLessThan(id object) {
	return OS.objc_msgSend(this.id, OS.sel_isLessThan_1, object != null ? object.id : 0) != 0;
}

public boolean isLessThanOrEqualTo(id object) {
	return OS.objc_msgSend(this.id, OS.sel_isLessThanOrEqualTo_1, object != null ? object.id : 0) != 0;
}

public boolean isLike(NSString object) {
	return OS.objc_msgSend(this.id, OS.sel_isLike_1, object != null ? object.id : 0) != 0;
}

public boolean isMemberOfClass(int aClass) {
	return OS.objc_msgSend(this.id, OS.sel_isMemberOfClass_1, aClass) != 0;
}

public boolean isNotEqualTo(id object) {
	return OS.objc_msgSend(this.id, OS.sel_isNotEqualTo_1, object != null ? object.id : 0) != 0;
}

public boolean isProxy() {
	return OS.objc_msgSend(this.id, OS.sel_isProxy) != 0;
}

public static boolean isSubclassOfClass(int aClass) {
	return OS.objc_msgSend(OS.class_NSObject, OS.sel_isSubclassOfClass_1, aClass) != 0;
}

public static NSSet keyPathsForValuesAffectingValueForKey(NSString key) {
	int result = OS.objc_msgSend(OS.class_NSObject, OS.sel_keyPathsForValuesAffectingValueForKey_1, key != null ? key.id : 0);
	return result != 0 ? new NSSet(result) : null;
}

public static void load() {
	OS.objc_msgSend(OS.class_NSObject, OS.sel_load);
}

public void lock() {
	OS.objc_msgSend(this.id, OS.sel_lock);
}

public boolean makeNewConnection(NSConnection conn, NSConnection ancestor) {
	return OS.objc_msgSend(this.id, OS.sel_makeNewConnection_1sender_1, conn != null ? conn.id : 0, ancestor != null ? ancestor.id : 0) != 0;
}

public id metadataQuery_replacementObjectForResultObject_(NSMetadataQuery query, NSMetadataItem result) {
	int r = OS.objc_msgSend(this.id, OS.sel_metadataQuery_1replacementObjectForResultObject_1, query != null ? query.id : 0, result != null ? result.id : 0);
	return r != 0 ? new id(r) : null;
}

public id metadataQuery_replacementValueForAttribute_value_(NSMetadataQuery query, NSString attrName, id attrValue) {
	int result = OS.objc_msgSend(this.id, OS.sel_metadataQuery_1replacementValueForAttribute_1value_1, query != null ? query.id : 0, attrName != null ? attrName.id : 0, attrValue != null ? attrValue.id : 0);
	return result != 0 ? new id(result) : null;
}

public int methodForSelector(int aSelector) {
	return OS.objc_msgSend(this.id, OS.sel_methodForSelector_1, aSelector);
}

public NSMethodSignature methodSignatureForSelector(int aSelector) {
	int result = OS.objc_msgSend(this.id, OS.sel_methodSignatureForSelector_1, aSelector);
	return result != 0 ? new NSMethodSignature(result) : null;
}

public NSMutableArray mutableArrayValueForKey(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_mutableArrayValueForKey_1, key != null ? key.id : 0);
	return result != 0 ? new NSMutableArray(result) : null;
}

public NSMutableArray mutableArrayValueForKeyPath(NSString keyPath) {
	int result = OS.objc_msgSend(this.id, OS.sel_mutableArrayValueForKeyPath_1, keyPath != null ? keyPath.id : 0);
	return result != 0 ? new NSMutableArray(result) : null;
}

public id mutableCopy() {
	int result = OS.objc_msgSend(this.id, OS.sel_mutableCopy);
	return result != 0 ? new id(result) : null;
}

public id mutableCopyWithZone_(int zone) {
	int result = OS.objc_msgSend(this.id, OS.sel_mutableCopyWithZone_1, zone);
	return result != 0 ? new id(result) : null;
}

public static id static_mutableCopyWithZone_(int zone) {
	int result = OS.objc_msgSend(OS.class_NSObject, OS.sel_mutableCopyWithZone_1, zone);
	return result != 0 ? new id(result) : null;
}

public NSMutableSet mutableSetValueForKey(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_mutableSetValueForKey_1, key != null ? key.id : 0);
	return result != 0 ? new NSMutableSet(result) : null;
}

public NSMutableSet mutableSetValueForKeyPath(NSString keyPath) {
	int result = OS.objc_msgSend(this.id, OS.sel_mutableSetValueForKeyPath_1, keyPath != null ? keyPath.id : 0);
	return result != 0 ? new NSMutableSet(result) : null;
}

public void netService_didNotPublish_(NSNetService sender, NSDictionary errorDict) {
	OS.objc_msgSend(this.id, OS.sel_netService_1didNotPublish_1, sender != null ? sender.id : 0, errorDict != null ? errorDict.id : 0);
}

public void netService_didNotResolve_(NSNetService sender, NSDictionary errorDict) {
	OS.objc_msgSend(this.id, OS.sel_netService_1didNotResolve_1, sender != null ? sender.id : 0, errorDict != null ? errorDict.id : 0);
}

public void netService_didUpdateTXTRecordData_(NSNetService sender, NSData data) {
	OS.objc_msgSend(this.id, OS.sel_netService_1didUpdateTXTRecordData_1, sender != null ? sender.id : 0, data != null ? data.id : 0);
}

public void netServiceBrowser_didFindDomain_moreComing_(NSNetServiceBrowser aNetServiceBrowser, NSString domainString, boolean moreComing) {
	OS.objc_msgSend(this.id, OS.sel_netServiceBrowser_1didFindDomain_1moreComing_1, aNetServiceBrowser != null ? aNetServiceBrowser.id : 0, domainString != null ? domainString.id : 0, moreComing);
}

public void netServiceBrowser_didFindService_moreComing_(NSNetServiceBrowser aNetServiceBrowser, NSNetService aNetService, boolean moreComing) {
	OS.objc_msgSend(this.id, OS.sel_netServiceBrowser_1didFindService_1moreComing_1, aNetServiceBrowser != null ? aNetServiceBrowser.id : 0, aNetService != null ? aNetService.id : 0, moreComing);
}

public void netServiceBrowser_didNotSearch_(NSNetServiceBrowser aNetServiceBrowser, NSDictionary errorDict) {
	OS.objc_msgSend(this.id, OS.sel_netServiceBrowser_1didNotSearch_1, aNetServiceBrowser != null ? aNetServiceBrowser.id : 0, errorDict != null ? errorDict.id : 0);
}

public void netServiceBrowser_didRemoveDomain_moreComing_(NSNetServiceBrowser aNetServiceBrowser, NSString domainString, boolean moreComing) {
	OS.objc_msgSend(this.id, OS.sel_netServiceBrowser_1didRemoveDomain_1moreComing_1, aNetServiceBrowser != null ? aNetServiceBrowser.id : 0, domainString != null ? domainString.id : 0, moreComing);
}

public void netServiceBrowser_didRemoveService_moreComing_(NSNetServiceBrowser aNetServiceBrowser, NSNetService aNetService, boolean moreComing) {
	OS.objc_msgSend(this.id, OS.sel_netServiceBrowser_1didRemoveService_1moreComing_1, aNetServiceBrowser != null ? aNetServiceBrowser.id : 0, aNetService != null ? aNetService.id : 0, moreComing);
}

public void netServiceBrowserDidStopSearch(NSNetServiceBrowser aNetServiceBrowser) {
	OS.objc_msgSend(this.id, OS.sel_netServiceBrowserDidStopSearch_1, aNetServiceBrowser != null ? aNetServiceBrowser.id : 0);
}

public void netServiceBrowserWillSearch(NSNetServiceBrowser aNetServiceBrowser) {
	OS.objc_msgSend(this.id, OS.sel_netServiceBrowserWillSearch_1, aNetServiceBrowser != null ? aNetServiceBrowser.id : 0);
}

public void netServiceDidPublish(NSNetService sender) {
	OS.objc_msgSend(this.id, OS.sel_netServiceDidPublish_1, sender != null ? sender.id : 0);
}

public void netServiceDidResolveAddress(NSNetService sender) {
	OS.objc_msgSend(this.id, OS.sel_netServiceDidResolveAddress_1, sender != null ? sender.id : 0);
}

public void netServiceDidStop(NSNetService sender) {
	OS.objc_msgSend(this.id, OS.sel_netServiceDidStop_1, sender != null ? sender.id : 0);
}

public void netServiceWillPublish(NSNetService sender) {
	OS.objc_msgSend(this.id, OS.sel_netServiceWillPublish_1, sender != null ? sender.id : 0);
}

public void netServiceWillResolve(NSNetService sender) {
	OS.objc_msgSend(this.id, OS.sel_netServiceWillResolve_1, sender != null ? sender.id : 0);
}

//public static id new() {
//	int result = OS.objc_msgSend(OS.class_NSObject, OS.sel_new);
//	return result != 0 ? new id(result) : null;
//}

public id newScriptingObjectOfClass(int objectClass, NSString key, id contentsValue, NSDictionary properties) {
	int result = OS.objc_msgSend(this.id, OS.sel_newScriptingObjectOfClass_1forValueForKey_1withContentsValue_1properties_1, objectClass, key != null ? key.id : 0, contentsValue != null ? contentsValue.id : 0, properties != null ? properties.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSScriptObjectSpecifier objectSpecifier() {
	int result = OS.objc_msgSend(this.id, OS.sel_objectSpecifier);
	return result != 0 ? new NSScriptObjectSpecifier(result) : null;
}

public int observationInfo() {
	return OS.objc_msgSend(this.id, OS.sel_observationInfo);
}

public void observeValueForKeyPath(NSString keyPath, id object, NSDictionary change, int context) {
	OS.objc_msgSend(this.id, OS.sel_observeValueForKeyPath_1ofObject_1change_1context_1, keyPath != null ? keyPath.id : 0, object != null ? object.id : 0, change != null ? change.id : 0, context);
}

public void parser_didEndElement_namespaceURI_qualifiedName_(NSXMLParser parser, NSString elementName, NSString namespaceURI, NSString qName) {
	OS.objc_msgSend(this.id, OS.sel_parser_1didEndElement_1namespaceURI_1qualifiedName_1, parser != null ? parser.id : 0, elementName != null ? elementName.id : 0, namespaceURI != null ? namespaceURI.id : 0, qName != null ? qName.id : 0);
}

public void parser_didEndMappingPrefix_(NSXMLParser parser, NSString prefix) {
	OS.objc_msgSend(this.id, OS.sel_parser_1didEndMappingPrefix_1, parser != null ? parser.id : 0, prefix != null ? prefix.id : 0);
}

public void parser_didStartElement_namespaceURI_qualifiedName_attributes_(NSXMLParser parser, NSString elementName, NSString namespaceURI, NSString qName, NSDictionary attributeDict) {
	OS.objc_msgSend(this.id, OS.sel_parser_1didStartElement_1namespaceURI_1qualifiedName_1attributes_1, parser != null ? parser.id : 0, elementName != null ? elementName.id : 0, namespaceURI != null ? namespaceURI.id : 0, qName != null ? qName.id : 0, attributeDict != null ? attributeDict.id : 0);
}

public void parser_didStartMappingPrefix_toURI_(NSXMLParser parser, NSString prefix, NSString namespaceURI) {
	OS.objc_msgSend(this.id, OS.sel_parser_1didStartMappingPrefix_1toURI_1, parser != null ? parser.id : 0, prefix != null ? prefix.id : 0, namespaceURI != null ? namespaceURI.id : 0);
}

public void parser_foundAttributeDeclarationWithName_forElement_type_defaultValue_(NSXMLParser parser, NSString attributeName, NSString elementName, NSString type, NSString defaultValue) {
	OS.objc_msgSend(this.id, OS.sel_parser_1foundAttributeDeclarationWithName_1forElement_1type_1defaultValue_1, parser != null ? parser.id : 0, attributeName != null ? attributeName.id : 0, elementName != null ? elementName.id : 0, type != null ? type.id : 0, defaultValue != null ? defaultValue.id : 0);
}

public void parser_foundCDATA_(NSXMLParser parser, NSData CDATABlock) {
	OS.objc_msgSend(this.id, OS.sel_parser_1foundCDATA_1, parser != null ? parser.id : 0, CDATABlock != null ? CDATABlock.id : 0);
}

public void parser_foundCharacters_(NSXMLParser parser, NSString string) {
	OS.objc_msgSend(this.id, OS.sel_parser_1foundCharacters_1, parser != null ? parser.id : 0, string != null ? string.id : 0);
}

public void parser_foundComment_(NSXMLParser parser, NSString comment) {
	OS.objc_msgSend(this.id, OS.sel_parser_1foundComment_1, parser != null ? parser.id : 0, comment != null ? comment.id : 0);
}

public void parser_foundElementDeclarationWithName_model_(NSXMLParser parser, NSString elementName, NSString model) {
	OS.objc_msgSend(this.id, OS.sel_parser_1foundElementDeclarationWithName_1model_1, parser != null ? parser.id : 0, elementName != null ? elementName.id : 0, model != null ? model.id : 0);
}

public void parser_foundExternalEntityDeclarationWithName_publicID_systemID_(NSXMLParser parser, NSString name, NSString publicID, NSString systemID) {
	OS.objc_msgSend(this.id, OS.sel_parser_1foundExternalEntityDeclarationWithName_1publicID_1systemID_1, parser != null ? parser.id : 0, name != null ? name.id : 0, publicID != null ? publicID.id : 0, systemID != null ? systemID.id : 0);
}

public void parser_foundIgnorableWhitespace_(NSXMLParser parser, NSString whitespaceString) {
	OS.objc_msgSend(this.id, OS.sel_parser_1foundIgnorableWhitespace_1, parser != null ? parser.id : 0, whitespaceString != null ? whitespaceString.id : 0);
}

public void parser_foundInternalEntityDeclarationWithName_value_(NSXMLParser parser, NSString name, NSString value) {
	OS.objc_msgSend(this.id, OS.sel_parser_1foundInternalEntityDeclarationWithName_1value_1, parser != null ? parser.id : 0, name != null ? name.id : 0, value != null ? value.id : 0);
}

public void parser_foundNotationDeclarationWithName_publicID_systemID_(NSXMLParser parser, NSString name, NSString publicID, NSString systemID) {
	OS.objc_msgSend(this.id, OS.sel_parser_1foundNotationDeclarationWithName_1publicID_1systemID_1, parser != null ? parser.id : 0, name != null ? name.id : 0, publicID != null ? publicID.id : 0, systemID != null ? systemID.id : 0);
}

public void parser_foundProcessingInstructionWithTarget_data_(NSXMLParser parser, NSString target, NSString data) {
	OS.objc_msgSend(this.id, OS.sel_parser_1foundProcessingInstructionWithTarget_1data_1, parser != null ? parser.id : 0, target != null ? target.id : 0, data != null ? data.id : 0);
}

public void parser_foundUnparsedEntityDeclarationWithName_publicID_systemID_notationName_(NSXMLParser parser, NSString name, NSString publicID, NSString systemID, NSString notationName) {
	OS.objc_msgSend(this.id, OS.sel_parser_1foundUnparsedEntityDeclarationWithName_1publicID_1systemID_1notationName_1, parser != null ? parser.id : 0, name != null ? name.id : 0, publicID != null ? publicID.id : 0, systemID != null ? systemID.id : 0, notationName != null ? notationName.id : 0);
}

public void parser_parseErrorOccurred_(NSXMLParser parser, NSError parseError) {
	OS.objc_msgSend(this.id, OS.sel_parser_1parseErrorOccurred_1, parser != null ? parser.id : 0, parseError != null ? parseError.id : 0);
}

public NSData parser_resolveExternalEntityName_systemID_(NSXMLParser parser, NSString name, NSString systemID) {
	int result = OS.objc_msgSend(this.id, OS.sel_parser_1resolveExternalEntityName_1systemID_1, parser != null ? parser.id : 0, name != null ? name.id : 0, systemID != null ? systemID.id : 0);
	return result != 0 ? new NSData(result) : null;
}

public void parser_validationErrorOccurred_(NSXMLParser parser, NSError validationError) {
	OS.objc_msgSend(this.id, OS.sel_parser_1validationErrorOccurred_1, parser != null ? parser.id : 0, validationError != null ? validationError.id : 0);
}

public void parserDidEndDocument(NSXMLParser parser) {
	OS.objc_msgSend(this.id, OS.sel_parserDidEndDocument_1, parser != null ? parser.id : 0);
}

public void parserDidStartDocument(NSXMLParser parser) {
	OS.objc_msgSend(this.id, OS.sel_parserDidStartDocument_1, parser != null ? parser.id : 0);
}

public id performSelector_(int aSelector) {
	int result = OS.objc_msgSend(this.id, OS.sel_performSelector_1, aSelector);
	return result != 0 ? new id(result) : null;
}

public void performSelector_onThread_withObject_waitUntilDone_(int aSelector, NSThread thr, id arg, boolean wait) {
	OS.objc_msgSend(this.id, OS.sel_performSelector_1onThread_1withObject_1waitUntilDone_1, aSelector, thr != null ? thr.id : 0, arg != null ? arg.id : 0, wait);
}

public void performSelector_onThread_withObject_waitUntilDone_modes_(int aSelector, NSThread thr, id arg, boolean wait, NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_performSelector_1onThread_1withObject_1waitUntilDone_1modes_1, aSelector, thr != null ? thr.id : 0, arg != null ? arg.id : 0, wait, array != null ? array.id : 0);
}

public id performSelector_withObject_(int aSelector, id object) {
	int result = OS.objc_msgSend(this.id, OS.sel_performSelector_1withObject_1, aSelector, object != null ? object.id : 0);
	return result != 0 ? new id(result) : null;
}

public void performSelector_withObject_afterDelay_(int aSelector, id anArgument, double delay) {
	OS.objc_msgSend(this.id, OS.sel_performSelector_1withObject_1afterDelay_1, aSelector, anArgument != null ? anArgument.id : 0, delay);
}

public void performSelector_withObject_afterDelay_inModes_(int aSelector, id anArgument, double delay, NSArray modes) {
	OS.objc_msgSend(this.id, OS.sel_performSelector_1withObject_1afterDelay_1inModes_1, aSelector, anArgument != null ? anArgument.id : 0, delay, modes != null ? modes.id : 0);
}

public id performSelector_withObject_withObject_(int aSelector, id object1, id object2) {
	int result = OS.objc_msgSend(this.id, OS.sel_performSelector_1withObject_1withObject_1, aSelector, object1 != null ? object1.id : 0, object2 != null ? object2.id : 0);
	return result != 0 ? new id(result) : null;
}

public void performSelectorInBackground(int aSelector, id arg) {
	OS.objc_msgSend(this.id, OS.sel_performSelectorInBackground_1withObject_1, aSelector, arg != null ? arg.id : 0);
}

public void performSelectorOnMainThread_withObject_waitUntilDone_(int aSelector, id arg, boolean wait) {
	OS.objc_msgSend(this.id, OS.sel_performSelectorOnMainThread_1withObject_1waitUntilDone_1, aSelector, arg != null ? arg.id : 0, wait);
}

public void performSelectorOnMainThread_withObject_waitUntilDone_modes_(int aSelector, id arg, boolean wait, NSArray array) {
	OS.objc_msgSend(this.id, OS.sel_performSelectorOnMainThread_1withObject_1waitUntilDone_1modes_1, aSelector, arg != null ? arg.id : 0, wait, array != null ? array.id : 0);
}

public static void poseAsClass(int aClass) {
	OS.objc_msgSend(OS.class_NSObject, OS.sel_poseAsClass_1, aClass);
}

public void release() {
	OS.objc_msgSend(this.id, OS.sel_release);
}

public void removeObserver(NSObject observer, NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_removeObserver_1forKeyPath_1, observer != null ? observer.id : 0, keyPath != null ? keyPath.id : 0);
}

public void removeObserver(NSObject observer) {
	OS.objc_msgSend(this.id, OS.sel_removeObserver_1, observer != null ? observer.id : 0);
}

public void removeValueAtIndex(int index, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_removeValueAtIndex_1fromPropertyWithKey_1, index, key != null ? key.id : 0);
}

public void replaceValueAtIndex(int index, NSString key, id value) {
	OS.objc_msgSend(this.id, OS.sel_replaceValueAtIndex_1inPropertyWithKey_1withValue_1, index, key != null ? key.id : 0, value != null ? value.id : 0);
}

public id replacementObjectForArchiver(NSArchiver archiver) {
	int result = OS.objc_msgSend(this.id, OS.sel_replacementObjectForArchiver_1, archiver != null ? archiver.id : 0);
	return result != 0 ? new id(result) : null;
}

public id replacementObjectForCoder(NSCoder aCoder) {
	int result = OS.objc_msgSend(this.id, OS.sel_replacementObjectForCoder_1, aCoder != null ? aCoder.id : 0);
	return result != 0 ? new id(result) : null;
}

public id replacementObjectForKeyedArchiver(NSKeyedArchiver archiver) {
	int result = OS.objc_msgSend(this.id, OS.sel_replacementObjectForKeyedArchiver_1, archiver != null ? archiver.id : 0);
	return result != 0 ? new id(result) : null;
}

public id replacementObjectForPortCoder(NSPortCoder coder) {
	int result = OS.objc_msgSend(this.id, OS.sel_replacementObjectForPortCoder_1, coder != null ? coder.id : 0);
	return result != 0 ? new id(result) : null;
}

public static boolean resolveClassMethod(int sel) {
	return OS.objc_msgSend(OS.class_NSObject, OS.sel_resolveClassMethod_1, sel) != 0;
}

public static boolean resolveInstanceMethod(int sel) {
	return OS.objc_msgSend(OS.class_NSObject, OS.sel_resolveInstanceMethod_1, sel) != 0;
}

public boolean respondsToSelector(int aSelector) {
	return OS.objc_msgSend(this.id, OS.sel_respondsToSelector_1, aSelector) != 0;
}

public id retain() {
	int result = OS.objc_msgSend(this.id, OS.sel_retain);
	return result != 0 ? new id(result) : null;
}

public int retainCount() {
	return OS.objc_msgSend(this.id, OS.sel_retainCount);
}

public int roundingMode() {
	return OS.objc_msgSend(this.id, OS.sel_roundingMode);
}

public short scale() {
	return (short)OS.objc_msgSend(this.id, OS.sel_scale);
}

public boolean scriptingBeginsWith(id object) {
	return OS.objc_msgSend(this.id, OS.sel_scriptingBeginsWith_1, object != null ? object.id : 0) != 0;
}

public boolean scriptingContains(id object) {
	return OS.objc_msgSend(this.id, OS.sel_scriptingContains_1, object != null ? object.id : 0) != 0;
}

public boolean scriptingEndsWith(id object) {
	return OS.objc_msgSend(this.id, OS.sel_scriptingEndsWith_1, object != null ? object.id : 0) != 0;
}

public boolean scriptingIsEqualTo(id object) {
	return OS.objc_msgSend(this.id, OS.sel_scriptingIsEqualTo_1, object != null ? object.id : 0) != 0;
}

public boolean scriptingIsGreaterThan(id object) {
	return OS.objc_msgSend(this.id, OS.sel_scriptingIsGreaterThan_1, object != null ? object.id : 0) != 0;
}

public boolean scriptingIsGreaterThanOrEqualTo(id object) {
	return OS.objc_msgSend(this.id, OS.sel_scriptingIsGreaterThanOrEqualTo_1, object != null ? object.id : 0) != 0;
}

public boolean scriptingIsLessThan(id object) {
	return OS.objc_msgSend(this.id, OS.sel_scriptingIsLessThan_1, object != null ? object.id : 0) != 0;
}

public boolean scriptingIsLessThanOrEqualTo(id object) {
	return OS.objc_msgSend(this.id, OS.sel_scriptingIsLessThanOrEqualTo_1, object != null ? object.id : 0) != 0;
}

public NSDictionary scriptingProperties() {
	int result = OS.objc_msgSend(this.id, OS.sel_scriptingProperties);
	return result != 0 ? new NSDictionary(result) : null;
}

public id scriptingValueForSpecifier(NSScriptObjectSpecifier objectSpecifier) {
	int result = OS.objc_msgSend(this.id, OS.sel_scriptingValueForSpecifier_1, objectSpecifier != null ? objectSpecifier.id : 0);
	return result != 0 ? new id(result) : null;
}

public id self() {
	int result = OS.objc_msgSend(this.id, OS.sel_self);
	return result != 0 ? new id(result) : null;
}

public static void setKeys(NSArray keys, NSString dependentKey) {
	OS.objc_msgSend(OS.class_NSObject, OS.sel_setKeys_1triggerChangeNotificationsForDependentKey_1, keys != null ? keys.id : 0, dependentKey != null ? dependentKey.id : 0);
}

public void setNilValueForKey(NSString key) {
	OS.objc_msgSend(this.id, OS.sel_setNilValueForKey_1, key != null ? key.id : 0);
}

public void setObservationInfo(int observationInfo) {
	OS.objc_msgSend(this.id, OS.sel_setObservationInfo_1, observationInfo);
}

public void setScriptingProperties(NSDictionary properties) {
	OS.objc_msgSend(this.id, OS.sel_setScriptingProperties_1, properties != null ? properties.id : 0);
}

public void setValue_forKey_(id value, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_setValue_1forKey_1, value != null ? value.id : 0, key != null ? key.id : 0);
}

public void setValue_forKeyPath_(id value, NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_setValue_1forKeyPath_1, value != null ? value.id : 0, keyPath != null ? keyPath.id : 0);
}

public void setValue_forUndefinedKey_(id value, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_setValue_1forUndefinedKey_1, value != null ? value.id : 0, key != null ? key.id : 0);
}

public void setValuesForKeysWithDictionary(NSDictionary keyedValues) {
	OS.objc_msgSend(this.id, OS.sel_setValuesForKeysWithDictionary_1, keyedValues != null ? keyedValues.id : 0);
}

public static void setVersion(int aVersion) {
	OS.objc_msgSend(OS.class_NSObject, OS.sel_setVersion_1, aVersion);
}

public NSRange spellServer_checkGrammarInString_language_details_(NSSpellServer sender, NSString stringToCheck, NSString language, int details) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_spellServer_1checkGrammarInString_1language_1details_1, sender != null ? sender.id : 0, stringToCheck != null ? stringToCheck.id : 0, language != null ? language.id : 0, details);
	return result;
}

public void spellServer_didForgetWord_inLanguage_(NSSpellServer sender, NSString word, NSString language) {
	OS.objc_msgSend(this.id, OS.sel_spellServer_1didForgetWord_1inLanguage_1, sender != null ? sender.id : 0, word != null ? word.id : 0, language != null ? language.id : 0);
}

public void spellServer_didLearnWord_inLanguage_(NSSpellServer sender, NSString word, NSString language) {
	OS.objc_msgSend(this.id, OS.sel_spellServer_1didLearnWord_1inLanguage_1, sender != null ? sender.id : 0, word != null ? word.id : 0, language != null ? language.id : 0);
}

public NSRange spellServer_findMisspelledWordInString_language_wordCount_countOnly_(NSSpellServer sender, NSString stringToCheck, NSString language, int wordCount, boolean countOnly) {
	NSRange result = new NSRange();
	OS.objc_msgSend_stret(result, this.id, OS.sel_spellServer_1findMisspelledWordInString_1language_1wordCount_1countOnly_1, sender != null ? sender.id : 0, stringToCheck != null ? stringToCheck.id : 0, language != null ? language.id : 0, wordCount, countOnly);
	return result;
}

public NSArray spellServer_suggestCompletionsForPartialWordRange_inString_language_(NSSpellServer sender, NSRange range, NSString string, NSString language) {
	int result = OS.objc_msgSend(this.id, OS.sel_spellServer_1suggestCompletionsForPartialWordRange_1inString_1language_1, sender != null ? sender.id : 0, range, string != null ? string.id : 0, language != null ? language.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray spellServer_suggestGuessesForWord_inLanguage_(NSSpellServer sender, NSString word, NSString language) {
	int result = OS.objc_msgSend(this.id, OS.sel_spellServer_1suggestGuessesForWord_1inLanguage_1, sender != null ? sender.id : 0, word != null ? word.id : 0, language != null ? language.id : 0);
	return result != 0 ? new NSArray(result) : null;
}

public id storedValueForKey(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_storedValueForKey_1, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public void stream(NSStream aStream, int eventCode) {
	OS.objc_msgSend(this.id, OS.sel_stream_1handleEvent_1, aStream != null ? aStream.id : 0, eventCode);
}

public static int static_superclass() {
	return OS.objc_msgSend(OS.class_NSObject, OS.sel_superclass);
}

public int superclass() {
	return OS.objc_msgSend(this.id, OS.sel_superclass);
}

public void takeStoredValue(id value, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_takeStoredValue_1forKey_1, value != null ? value.id : 0, key != null ? key.id : 0);
}

public void takeValue_forKey_(id value, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_takeValue_1forKey_1, value != null ? value.id : 0, key != null ? key.id : 0);
}

public void takeValue_forKeyPath_(id value, NSString keyPath) {
	OS.objc_msgSend(this.id, OS.sel_takeValue_1forKeyPath_1, value != null ? value.id : 0, keyPath != null ? keyPath.id : 0);
}

public void takeValuesFromDictionary(NSDictionary properties) {
	OS.objc_msgSend(this.id, OS.sel_takeValuesFromDictionary_1, properties != null ? properties.id : 0);
}

public NSArray toManyRelationshipKeys() {
	int result = OS.objc_msgSend(this.id, OS.sel_toManyRelationshipKeys);
	return result != 0 ? new NSArray(result) : null;
}

public NSArray toOneRelationshipKeys() {
	int result = OS.objc_msgSend(this.id, OS.sel_toOneRelationshipKeys);
	return result != 0 ? new NSArray(result) : null;
}

public void unableToSetNilForKey(NSString key) {
	OS.objc_msgSend(this.id, OS.sel_unableToSetNilForKey_1, key != null ? key.id : 0);
}

public int unarchiver_cannotDecodeObjectOfClassName_originalClasses_(NSKeyedUnarchiver unarchiver, NSString name, NSArray classNames) {
	return OS.objc_msgSend(this.id, OS.sel_unarchiver_1cannotDecodeObjectOfClassName_1originalClasses_1, unarchiver != null ? unarchiver.id : 0, name != null ? name.id : 0, classNames != null ? classNames.id : 0);
}

public id unarchiver_didDecodeObject_(NSKeyedUnarchiver unarchiver, id object) {
	int result = OS.objc_msgSend(this.id, OS.sel_unarchiver_1didDecodeObject_1, unarchiver != null ? unarchiver.id : 0, object != null ? object.id : 0);
	return result != 0 ? new id(result) : null;
}

public void unarchiver_willReplaceObject_withObject_(NSKeyedUnarchiver unarchiver, id object, id newObject) {
	OS.objc_msgSend(this.id, OS.sel_unarchiver_1willReplaceObject_1withObject_1, unarchiver != null ? unarchiver.id : 0, object != null ? object.id : 0, newObject != null ? newObject.id : 0);
}

public void unarchiverDidFinish(NSKeyedUnarchiver unarchiver) {
	OS.objc_msgSend(this.id, OS.sel_unarchiverDidFinish_1, unarchiver != null ? unarchiver.id : 0);
}

public void unarchiverWillFinish(NSKeyedUnarchiver unarchiver) {
	OS.objc_msgSend(this.id, OS.sel_unarchiverWillFinish_1, unarchiver != null ? unarchiver.id : 0);
}

public void unlock() {
	OS.objc_msgSend(this.id, OS.sel_unlock);
}

public void useCredential(NSURLCredential credential, NSURLAuthenticationChallenge challenge) {
	OS.objc_msgSend(this.id, OS.sel_useCredential_1forAuthenticationChallenge_1, credential != null ? credential.id : 0, challenge != null ? challenge.id : 0);
}

public static boolean useStoredAccessor() {
	return OS.objc_msgSend(OS.class_NSObject, OS.sel_useStoredAccessor) != 0;
}

public boolean validateValue_forKey_error_(int ioValue, NSString inKey, int outError) {
	return OS.objc_msgSend(this.id, OS.sel_validateValue_1forKey_1error_1, ioValue, inKey != null ? inKey.id : 0, outError) != 0;
}

public boolean validateValue_forKeyPath_error_(int ioValue, NSString inKeyPath, int outError) {
	return OS.objc_msgSend(this.id, OS.sel_validateValue_1forKeyPath_1error_1, ioValue, inKeyPath != null ? inKeyPath.id : 0, outError) != 0;
}

public id valueAtIndex(int index, NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_valueAtIndex_1inPropertyWithKey_1, index, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public id valueForKey(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_valueForKey_1, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public id valueForKeyPath(NSString keyPath) {
	int result = OS.objc_msgSend(this.id, OS.sel_valueForKeyPath_1, keyPath != null ? keyPath.id : 0);
	return result != 0 ? new id(result) : null;
}

public id valueForUndefinedKey(NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_valueForUndefinedKey_1, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public id valueWithName(NSString name, NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_valueWithName_1inPropertyWithKey_1, name != null ? name.id : 0, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public id valueWithUniqueID(id uniqueID, NSString key) {
	int result = OS.objc_msgSend(this.id, OS.sel_valueWithUniqueID_1inPropertyWithKey_1, uniqueID != null ? uniqueID.id : 0, key != null ? key.id : 0);
	return result != 0 ? new id(result) : null;
}

public NSDictionary valuesForKeys(NSArray keys) {
	int result = OS.objc_msgSend(this.id, OS.sel_valuesForKeys_1, keys != null ? keys.id : 0);
	return result != 0 ? new NSDictionary(result) : null;
}

public static int version() {
	return OS.objc_msgSend(OS.class_NSObject, OS.sel_version);
}

public void willChange(int changeKind, NSIndexSet indexes, NSString key) {
	OS.objc_msgSend(this.id, OS.sel_willChange_1valuesAtIndexes_1forKey_1, changeKind, indexes != null ? indexes.id : 0, key != null ? key.id : 0);
}

public void willChangeValueForKey_(NSString key) {
	OS.objc_msgSend(this.id, OS.sel_willChangeValueForKey_1, key != null ? key.id : 0);
}

public void willChangeValueForKey_withSetMutation_usingObjects_(NSString key, int mutationKind, NSSet objects) {
	OS.objc_msgSend(this.id, OS.sel_willChangeValueForKey_1withSetMutation_1usingObjects_1, key != null ? key.id : 0, mutationKind, objects != null ? objects.id : 0);
}

public int zone() {
	return OS.objc_msgSend(this.id, OS.sel_zone);
}

}
