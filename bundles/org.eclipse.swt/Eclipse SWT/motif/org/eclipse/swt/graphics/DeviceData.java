package org.eclipse.swt.graphics;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

public class DeviceData {
	/*
	* Motif only fields.
	*/
	public String display_name;
	public String application_name;
	public String application_class;

	/*
	* Debug fields - may not be honoured
	* on some SWT platforms.
	*/
	public boolean debug;
	public boolean tracking;
	public Error [] errors;
	public Object [] objects;
}