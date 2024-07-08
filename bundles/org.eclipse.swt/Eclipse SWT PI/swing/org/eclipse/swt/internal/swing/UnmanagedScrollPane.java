/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Point;

import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeListener;

class UnmanagedScrollPane extends JScrollPane {

  public UnmanagedScrollPane(int vsbPolicy, int hsbPolicy) {
    super(vsbPolicy, hsbPolicy);
  }

  protected static class DisconnectedViewport extends JViewport {
    public void setViewPosition(Point p) {
      // Do nothing to prevent auto scrolling
    }
    public void addChangeListener(ChangeListener l) {
      // Do nothing to prevent auto scrolling
    }
  }
  
  protected final JViewport createViewport() {
    return createDisconnectedViewport();
  }
  
  protected DisconnectedViewport createDisconnectedViewport() {
    return new DisconnectedViewport();
  }

}
