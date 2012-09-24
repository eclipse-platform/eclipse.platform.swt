/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.gtk;


public class GdkDragContext {     
   /** @field cast=(GdkDragProtocol) */
	public int protocol;
   /** @field cast=(gboolean) */
	public boolean is_source;
   /** @field cast=(GdkWindow *) */
	public long /*int*/ source_window;
   /** @field cast=(GdkWindow *) */
	public long /*int*/ dest_window;
   /** @field cast=(GList *) */
	public long /*int*/ targets;
   /** @field cast=(GdkDragAction) */
	public int actions;
   /** @field cast=(GdkDragAction) */
	public int suggested_action;
   /** @field cast=(GdkDragAction) */
	public int action; 
   /** @field cast=(guint32) */
	public int start_time;
   public static final int sizeof = OS.GdkDragContext_sizeof();
}
