package org.eclipse.swt.internal.cocoa;

public class NSPortNameServer extends NSObject {

public NSPortNameServer() {
	super();
}

public NSPortNameServer(int id) {
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

public boolean registerPort(NSPort port, NSString name) {
	return OS.objc_msgSend(this.id, OS.sel_registerPort_1name_1, port != null ? port.id : 0, name != null ? name.id : 0) != 0;
}

public boolean removePortForName(NSString name) {
	return OS.objc_msgSend(this.id, OS.sel_removePortForName_1, name != null ? name.id : 0) != 0;
}

public static NSPortNameServer systemDefaultPortNameServer() {
	int result = OS.objc_msgSend(OS.class_NSPortNameServer, OS.sel_systemDefaultPortNameServer);
	return result != 0 ? new NSPortNameServer(result) : null;
}

}
