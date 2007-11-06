package org.eclipse.swt.internal.cocoa;

public class NSMessagePortNameServer extends NSPortNameServer {

public NSMessagePortNameServer() {
	super();
}

public NSMessagePortNameServer(int id) {
	super(id);
}

public NSPort portForName_(NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_portForName_1, name != null ? name.id : 0);
	return result != 0 ? new NSPort(result) : null;
}

public NSPort portForName_host_(NSString name, NSString host) {
	int result = OS.objc_msgSend(this.id, OS.sel_portForName_1host_1, name != null ? name.id : 0, host != null ? host.id : 0);
	return result != 0 ? new NSPort(result) : null;
}

public static id sharedInstance() {
	int result = OS.objc_msgSend(OS.class_NSMessagePortNameServer, OS.sel_sharedInstance);
	return result != 0 ? new id(result) : null;
}

}
