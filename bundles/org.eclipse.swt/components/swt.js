/*******************************************************************************
 * Copyright (c) 2014 Neil Rashbrook and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Neil Rashbrook <neil@parkwaycc.co.uk> - Bug 429739
 *******************************************************************************/

Components.utils.import("resource://gre/modules/XPCOMUtils.jsm");

function execute() {
}

execute.prototype.evalInWindow = function(aWindow, aString) {
    aWindow = XPCNativeWrapper.unwrap(aWindow);
    try {
        aWindow.external.QueryInterface(Components.interfaces.External);
    } catch (e) {
        aWindow.external = Components.classes["@eclipse.org/external;1"].createInstance();
    }
    return aWindow.eval(aString);
};

execute.prototype.evalAsChrome = function(aWindow, aString) {
    var window = XPCNativeWrapper.unwrap(aWindow);
    return eval(aString);
};

execute.prototype.evalAsync = function(aWindow, aString) {
    aWindow.location = "javascript:" + unescape(aString) + ";void(0);";
};

execute.prototype.QueryInterface = XPCOMUtils.generateQI([Components.interfaces.Execute]);

execute.prototype.classID = Components.ID("{0aebcff1-f7b3-4522-a64b-1706d65dc232}");

var NSGetFactory = XPCOMUtils.generateNSGetFactory([execute]);
