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
 * The following example prints a right aligned page number in 
 * the footer, sets the job name to "Example" and prints line 
 * background colors but no other formatting:
 * 
 * StyledTextPrintOptions options = 
 * 		new StyledTextPrintOptions(null, "\t\t<page>", "Example", ST.LINE_BACKGROUND);
 * Runnable runnable = styledText.print(new Printer(), options);
 * runnable.run();
 *
 * NOTE: This class is experimental API and subject to change.
 * 
 * @since 2.1
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
	 * Print the text foreground color. Default value is <code>false</code>.
	 */
	public boolean printTextForeground = false;
	/**
	 * Print the text background color. Default value is <code>false</code>.
	 */
	public boolean printTextBackground = false;
	/**
	 * Print the font styles. Default value is <code>false</code>.
	 */
	public boolean printTextFontStyle = false;
	/**
	 * Print the line background color. Default value is <code>false</code>.
	 */
	public boolean printLineBackground = false;
}
