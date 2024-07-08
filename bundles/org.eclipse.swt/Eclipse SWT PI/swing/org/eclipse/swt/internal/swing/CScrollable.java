/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import javax.swing.JScrollBar;

/**
 * The interface shared by all scrollable controls.
 * @version 1.0 2005.03.22
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public interface CScrollable extends CControl {

  public JScrollBar getHorizontalScrollBar();

  public JScrollBar getVerticalScrollBar();

}
