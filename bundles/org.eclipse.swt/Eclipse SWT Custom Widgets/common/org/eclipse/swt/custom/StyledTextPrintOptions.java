package org.eclipse.swt.custom;
/*
 * Copyright (c) 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

/**
 * Use StyledTextPrintOptions to specify printing options for the
 * StyledText.print(Printer, StyledTextPrintOptions) API.
 * 
 * This class is experimental API and subject to change.
 */
public class StyledTextPrintOptions {
	public static final String PAGE_TAG = "<page>";
	public static final String SEPARATOR = "\t";
	/**
	 * Formatted text to print in the header of each page.
	 * "left '\t' center '\t' right"
	 * left, center, right = <page> | #CDATA	 * Header and footer are defined as three separate regions 
	 * for arbitrary text or the page number placeholder <page> (PAGE_TAG). 
	 * The three regions are left aligned, centered and right aligned. 
	 * They are separated by a tab character (SEPARATOR).
	 */
	public String header = null;
	public String footer = null;
	/**
	 * Name of the print job.	 */
	public String jobName = null;
	
	/**
	 * 0 or more of the following print options ORed together. 
	 * ST.TEXT_FOREGROUND, ST.TEXT_BACKGROUND, ST.TEXT_FONT_STYLE,
	 * ST.LINE_BACKGROUND 	 */
	public int options = 0;
	
	/**
	 * Creates an object of the receiver initialized with default values. 
	 * By default, none of the text or line styles are printed. 
	 */
	public StyledTextPrintOptions() {
	}
	/**
	 * Creates an object of the receiver initialized with the given print 
	 * options. 	 */
	public StyledTextPrintOptions(String header, String footer, String jobName, int options) {
		this.header = header;
		this.footer = footer;
		this.jobName = jobName;
		this.options = options;
	}	    
}
