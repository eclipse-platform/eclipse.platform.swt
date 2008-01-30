package org.eclipse.swt.internal.cocoa;

public class DOMDocument extends NSObject {

public DOMDocument() {
	super();
}

public DOMDocument(int id) {
	super(id);
}

//public NSURL URLWithAttributeString(NSString string) {
//	int result = OS.objc_msgSend(this.id, OS.sel_URLWithAttributeString_1, string != null ? string.id : 0);
//	return result != 0 ? new NSURL(result) : null;
//}

//public DOMNode adoptNode(DOMNode source) {
//	int result = OS.objc_msgSend(this.id, OS.sel_adoptNode_1, source != null ? source.id : 0);
//	return result != 0 ? new DOMNode(result) : null;
//}

//public DOMAttr createAttribute(NSString name) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createAttribute_1, name != null ? name.id : 0);
//	return result != 0 ? new DOMAttr(result) : null;
//}

//public DOMAttr createAttributeNS__(NSString createAttributeNS, NSString ) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createAttributeNS_1_1, createAttributeNS != null ? createAttributeNS.id : 0,  != null ? .id : 0);
//	return result != 0 ? new DOMAttr(result) : null;
//}

//public DOMAttr createAttributeNS_qualifiedName_(NSString namespaceURI, NSString qualifiedName) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createAttributeNS_1qualifiedName_1, namespaceURI != null ? namespaceURI.id : 0, qualifiedName != null ? qualifiedName.id : 0);
//	return result != 0 ? new DOMAttr(result) : null;
//}

//public DOMCDATASection createCDATASection(NSString data) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createCDATASection_1, data != null ? data.id : 0);
//	return result != 0 ? new DOMCDATASection(result) : null;
//}

//public DOMCSSStyleDeclaration createCSSStyleDeclaration() {
//	int result = OS.objc_msgSend(this.id, OS.sel_createCSSStyleDeclaration);
//	return result != 0 ? new DOMCSSStyleDeclaration(result) : null;
//}

//public DOMComment createComment(NSString data) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createComment_1, data != null ? data.id : 0);
//	return result != 0 ? new DOMComment(result) : null;
//}

//public DOMDocumentFragment createDocumentFragment() {
//	int result = OS.objc_msgSend(this.id, OS.sel_createDocumentFragment);
//	return result != 0 ? new DOMDocumentFragment(result) : null;
//}

//public DOMElement createElement(NSString tagName) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createElement_1, tagName != null ? tagName.id : 0);
//	return result != 0 ? new DOMElement(result) : null;
//}

//public DOMElement createElementNS__(NSString createElementNS, NSString ) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createElementNS_1_1, createElementNS != null ? createElementNS.id : 0,  != null ? .id : 0);
//	return result != 0 ? new DOMElement(result) : null;
//}

//public DOMElement createElementNS_qualifiedName_(NSString namespaceURI, NSString qualifiedName) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createElementNS_1qualifiedName_1, namespaceURI != null ? namespaceURI.id : 0, qualifiedName != null ? qualifiedName.id : 0);
//	return result != 0 ? new DOMElement(result) : null;
//}

//public DOMEntityReference createEntityReference(NSString name) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createEntityReference_1, name != null ? name.id : 0);
//	return result != 0 ? new DOMEntityReference(result) : null;
//}

//public DOMEvent createEvent(NSString eventType) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createEvent_1, eventType != null ? eventType.id : 0);
//	return result != 0 ? new DOMEvent(result) : null;
//}

//public DOMXPathExpression createExpression__(NSString createExpression, id  ) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createExpression_1_1, createExpression != null ? createExpression.id : 0,  != null ? .id : 0);
//	return result != 0 ? new DOMXPathExpression(result) : null;
//}

//public DOMXPathExpression createExpression_resolver_(NSString expression, id  resolver) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createExpression_1resolver_1, expression != null ? expression.id : 0, resolver != null ? resolver.id : 0);
//	return result != 0 ? new DOMXPathExpression(result) : null;
//}

//public id  createNSResolver(DOMNode nodeResolver) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createNSResolver_1, nodeResolver != null ? nodeResolver.id : 0);
//	return result != 0 ? new id (result) : null;
//}

//public DOMNodeIterator createNodeIterator____(DOMNode createNodeIterator, int , id  , boolean ) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createNodeIterator_1_1_1_1, createNodeIterator != null ? createNodeIterator.id : 0, ,  != null ? .id : 0, );
//	return result != 0 ? new DOMNodeIterator(result) : null;
//}

//public DOMNodeIterator createNodeIterator_whatToShow_filter_expandEntityReferences_(DOMNode root, int whatToShow, id  filter, boolean expandEntityReferences) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createNodeIterator_1whatToShow_1filter_1expandEntityReferences_1, root != null ? root.id : 0, whatToShow, filter != null ? filter.id : 0, expandEntityReferences);
//	return result != 0 ? new DOMNodeIterator(result) : null;
//}

//public DOMProcessingInstruction createProcessingInstruction__(NSString createProcessingInstruction, NSString ) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createProcessingInstruction_1_1, createProcessingInstruction != null ? createProcessingInstruction.id : 0,  != null ? .id : 0);
//	return result != 0 ? new DOMProcessingInstruction(result) : null;
//}

//public DOMProcessingInstruction createProcessingInstruction_data_(NSString target, NSString data) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createProcessingInstruction_1data_1, target != null ? target.id : 0, data != null ? data.id : 0);
//	return result != 0 ? new DOMProcessingInstruction(result) : null;
//}

//public DOMRange createRange() {
//	int result = OS.objc_msgSend(this.id, OS.sel_createRange);
//	return result != 0 ? new DOMRange(result) : null;
//}

//public DOMText createTextNode(NSString data) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createTextNode_1, data != null ? data.id : 0);
//	return result != 0 ? new DOMText(result) : null;
//}

//public DOMTreeWalker createTreeWalker____(DOMNode createTreeWalker, int , id  , boolean ) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createTreeWalker_1_1_1_1, createTreeWalker != null ? createTreeWalker.id : 0, ,  != null ? .id : 0, );
//	return result != 0 ? new DOMTreeWalker(result) : null;
//}

//public DOMTreeWalker createTreeWalker_whatToShow_filter_expandEntityReferences_(DOMNode root, int whatToShow, id  filter, boolean expandEntityReferences) {
//	int result = OS.objc_msgSend(this.id, OS.sel_createTreeWalker_1whatToShow_1filter_1expandEntityReferences_1, root != null ? root.id : 0, whatToShow, filter != null ? filter.id : 0, expandEntityReferences);
//	return result != 0 ? new DOMTreeWalker(result) : null;
//}

//public DOMXPathResult evaluate_____(NSString evaluate, DOMNode , id  , short , DOMXPathResult ) {
//	int result = OS.objc_msgSend(this.id, OS.sel_evaluate_1_1_1_1_1, evaluate != null ? evaluate.id : 0,  != null ? .id : 0,  != null ? .id : 0, ,  != null ? .id : 0);
//	return result != 0 ? new DOMXPathResult(result) : null;
//}

//public DOMXPathResult evaluate_contextNode_resolver_type_inResult_(NSString expression, DOMNode contextNode, id  resolver, short type, DOMXPathResult inResult) {
//	int result = OS.objc_msgSend(this.id, OS.sel_evaluate_1contextNode_1resolver_1type_1inResult_1, expression != null ? expression.id : 0, contextNode != null ? contextNode.id : 0, resolver != null ? resolver.id : 0, type, inResult != null ? inResult.id : 0);
//	return result != 0 ? new DOMXPathResult(result) : null;
//}

//public DOMCSSStyleDeclaration getComputedStyle__(DOMElement getComputedStyle, NSString ) {
//	int result = OS.objc_msgSend(this.id, OS.sel_getComputedStyle_1_1, getComputedStyle != null ? getComputedStyle.id : 0,  != null ? .id : 0);
//	return result != 0 ? new DOMCSSStyleDeclaration(result) : null;
//}

//public DOMCSSStyleDeclaration getComputedStyle_pseudoElement_(DOMElement element, NSString pseudoElement) {
//	int result = OS.objc_msgSend(this.id, OS.sel_getComputedStyle_1pseudoElement_1, element != null ? element.id : 0, pseudoElement != null ? pseudoElement.id : 0);
//	return result != 0 ? new DOMCSSStyleDeclaration(result) : null;
//}

//public DOMElement getElementById(NSString elementId) {
//	int result = OS.objc_msgSend(this.id, OS.sel_getElementById_1, elementId != null ? elementId.id : 0);
//	return result != 0 ? new DOMElement(result) : null;
//}

//public DOMNodeList getElementsByTagName(NSString tagname) {
//	int result = OS.objc_msgSend(this.id, OS.sel_getElementsByTagName_1, tagname != null ? tagname.id : 0);
//	return result != 0 ? new DOMNodeList(result) : null;
//}

//public DOMNodeList getElementsByTagNameNS__(NSString getElementsByTagNameNS, NSString ) {
//	int result = OS.objc_msgSend(this.id, OS.sel_getElementsByTagNameNS_1_1, getElementsByTagNameNS != null ? getElementsByTagNameNS.id : 0,  != null ? .id : 0);
//	return result != 0 ? new DOMNodeList(result) : null;
//}

//public DOMNodeList getElementsByTagNameNS_localName_(NSString namespaceURI, NSString localName) {
//	int result = OS.objc_msgSend(this.id, OS.sel_getElementsByTagNameNS_1localName_1, namespaceURI != null ? namespaceURI.id : 0, localName != null ? localName.id : 0);
//	return result != 0 ? new DOMNodeList(result) : null;
//}

//public DOMCSSRuleList getMatchedCSSRules_pseudoElement_(DOMElement element, NSString pseudoElement) {
//	int result = OS.objc_msgSend(this.id, OS.sel_getMatchedCSSRules_1pseudoElement_1, element != null ? element.id : 0, pseudoElement != null ? pseudoElement.id : 0);
//	return result != 0 ? new DOMCSSRuleList(result) : null;
//}

//public DOMCSSRuleList getMatchedCSSRules_pseudoElement_authorOnly_(DOMElement element, NSString pseudoElement, boolean authorOnly) {
//	int result = OS.objc_msgSend(this.id, OS.sel_getMatchedCSSRules_1pseudoElement_1authorOnly_1, element != null ? element.id : 0, pseudoElement != null ? pseudoElement.id : 0, authorOnly);
//	return result != 0 ? new DOMCSSRuleList(result) : null;
//}

//public DOMCSSStyleDeclaration getOverrideStyle__(DOMElement getOverrideStyle, NSString ) {
//	int result = OS.objc_msgSend(this.id, OS.sel_getOverrideStyle_1_1, getOverrideStyle != null ? getOverrideStyle.id : 0,  != null ? .id : 0);
//	return result != 0 ? new DOMCSSStyleDeclaration(result) : null;
//}

//public DOMCSSStyleDeclaration getOverrideStyle_pseudoElement_(DOMElement element, NSString pseudoElement) {
//	int result = OS.objc_msgSend(this.id, OS.sel_getOverrideStyle_1pseudoElement_1, element != null ? element.id : 0, pseudoElement != null ? pseudoElement.id : 0);
//	return result != 0 ? new DOMCSSStyleDeclaration(result) : null;
//}

//public DOMNode importNode__(DOMNode importNode, boolean ) {
//	int result = OS.objc_msgSend(this.id, OS.sel_importNode_1_1, importNode != null ? importNode.id : 0, );
//	return result != 0 ? new DOMNode(result) : null;
//}

//public DOMNode importNode_deep_(DOMNode importedNode, boolean deep) {
//	int result = OS.objc_msgSend(this.id, OS.sel_importNode_1deep_1, importedNode != null ? importedNode.id : 0, deep);
//	return result != 0 ? new DOMNode(result) : null;
//}

public WebFrame webFrame() {
	int result = OS.objc_msgSend(this.id, OS.sel_webFrame);
	return result != 0 ? new WebFrame(result) : null;
}

/* DOMEventTarget */

public void addEventListener_listener_useCapture(NSString type, id listener, boolean useCapture) {
	OS.objc_msgSend(this.id, OS.sel_addEventListener_1listener_1useCapture_1, type != null ? type.id : 0, listener != null ? listener.id : 0, useCapture);
}
}
