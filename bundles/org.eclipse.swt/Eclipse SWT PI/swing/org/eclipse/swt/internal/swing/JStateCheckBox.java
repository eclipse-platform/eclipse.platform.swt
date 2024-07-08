/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;

public class JStateCheckBox extends JCheckBox {

  public JStateCheckBox() {
    init();
  }

  public JStateCheckBox(Action a) {
    super(a);
    init();
  }

  public JStateCheckBox(Icon icon) {
    super(icon);
    init();
  }

  public JStateCheckBox(Icon icon, boolean selected) {
    super(icon, selected);
    init();
  }

  public JStateCheckBox(String text) {
    super(text);
    init();
  }

  public JStateCheckBox(String text, boolean selected) {
    super(text, selected);
    init();
  }

  public JStateCheckBox(String text, Icon icon) {
    super(text, icon);
    init();
  }

  public JStateCheckBox(String text, Icon icon, boolean selected) {
    super(text, icon, selected);
    init();
  }


  protected void init() {
    setModel(new ToggleButtonModel() {
      public boolean isPressed() {
        return isGrayed || super.isPressed();
      }
      public boolean isArmed() {
        return isGrayed || super.isArmed();
      }
    });
  }

  protected boolean isGrayed;

  public void setGrayed(boolean isGrayed) {
    this.isGrayed = isGrayed;
    repaint();
  }

  public boolean isGrayed() {
    return isGrayed;
  }

}
