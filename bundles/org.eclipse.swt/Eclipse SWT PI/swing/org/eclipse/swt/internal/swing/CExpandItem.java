/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

import org.eclipse.swt.widgets.ExpandItem;

class CExpandItemImplementation extends JPanel implements CExpandItem {

  protected ExpandItem handle;

  public CExpandItemImplementation(ExpandItem expandItem, int style) {
    super(new BorderLayout(0, 0));
    setOpaque(false);
    this.handle = expandItem;
    init(style);
  }

  protected void init(int style) {
  }

  public Component getContent() {
    if(getComponentCount() > 0) {
      return getComponent(0);
    }
    return null;
  }

}

public interface CExpandItem {

  public static class Factory {
    private Factory() {}

    public static CExpandItem newInstance(ExpandItem expandItem, int style) {
      return new CExpandItemImplementation(expandItem, style);
    }
  }

  public Component getContent();

}
