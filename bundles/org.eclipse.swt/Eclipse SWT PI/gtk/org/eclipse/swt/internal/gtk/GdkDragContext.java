/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.gtk;


public class GdkDragContext {     
   public int protocol;
   public boolean is_source;
   public int source_window;
   public int dest_window;
   public int targets;
   public int actions;
   public int suggested_action;
   public int action; 
   public int start_time;
   public static final int sizeof = 34;
}
