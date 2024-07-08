/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.util.EventObject;

public class BrowserLocationChangedEvent extends EventObject {

  protected String url;

  public BrowserLocationChangedEvent(Object source, String url) {
    super(source);
    this.url = url;
  }

  public String getURL() {
    return url;
  }

}