package org.eclipse.swt.internal.cocoa;

public class NSSocketPortNameServer extends NSPortNameServer {

public NSSocketPortNameServer() {
	super();
}

public NSSocketPortNameServer(int id) {
	super(id);
}

public short defaultNameServerPortNumber() {
	return (short)OS.objc_msgSend(this.id, OS.sel_defaultNameServerPortNumber);
}

public NSPort portForName_(NSString name) {
	int result = OS.objc_msgSend(this.id, OS.sel_portForName_1, name != null ? name.id : 0);
	return result != 0 ? new NSPort(result) : null;
}

public NSPort portForName_host_(NSString name, NSString host) {
	int result = OS.objc_msgSend(this.id, OS.sel_portForName_1host_1, name != null ? name.id : 0, host != null ? host.id : 0);
	return result != 0 ? new NSPort(result) : null;
}

public NSPort portForName_host_nameServerPortNumber_(NSString name, NSString host, short portNumber) {
	int result = OS.objc_msgSend(this.id, OS.sel_portForName_1host_1nameServerPortNumber_1, name != null ? name.id : 0, host != null ? host.id : 0, portNumber);
	return result != 0 ? new NSPort(result) : null;
}

public boolean registerPort_name_(NSPort port, NSString name) {
	return OS.objc_msgSend(this.id, OS.sel_registerPort_1name_1, port != null ? port.id : 0, name != null ? name.id : 0) != 0;
}

public boolean registerPort_name_nameServerPortNumber_(NSPort port, NSString name, short portNumber) {
	return OS.objc_msgSend(this.id, OS.sel_registerPort_1name_1nameServerPortNumber_1, port != null ? port.id : 0, name != null ? name.id : 0, portNumber) != 0;
}

public boolean removePortForName(NSString name) {
	return OS.objc_msgSend(this.id, OS.sel_removePortForName_1, name != null ? name.id : 0) != 0;
}

public void setDefaultNameServerPortNumber(short portNumber) {
	OS.objc_msgSend(this.id, OS.sel_setDefaultNameServerPortNumber_1, portNumber);
}

public static id sharedInstance() {
	int result = OS.objc_msgSend(OS.class_NSSocketPortNameServer, OS.sel_sharedInstance);
	return result != 0 ? new id(result) : null;
}

}
