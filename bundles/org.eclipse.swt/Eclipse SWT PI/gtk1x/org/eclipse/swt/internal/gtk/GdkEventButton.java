package org.eclipse.swt.internal.gtk;

/*
 * (c) Copyright IBM Corp. 2000, 2001, 2002.
 * All rights reserved.
 *
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 */

public class GdkEventButton extends GdkEvent {
	public int time;
	public long x;
	public long y;
	public long pressure;
	public long xtilt;
	public long ytilt;
	public int state;
	public int button;
	public int source;
	public int deviceid;
	public long x_root;
	public long y_root;
}
