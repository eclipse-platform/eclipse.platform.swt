/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.event.KeyEvent;
import java.util.EventObject;

public class TextFilterEvent extends EventObject {

  protected String text;
  protected int start;
  protected int end;
  protected KeyEvent keyEvent;

  public TextFilterEvent(Object source, String text, int start, int end, KeyEvent keyEvent) {
    super(source);
    this.text = text;
    this.start = start;
    this.end = end;
    this.keyEvent = keyEvent;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }

  public KeyEvent getKeyEvent() {
    return keyEvent;
  }

}