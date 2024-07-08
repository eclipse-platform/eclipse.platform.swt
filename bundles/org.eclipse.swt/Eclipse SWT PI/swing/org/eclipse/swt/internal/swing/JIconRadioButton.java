/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JRadioButton;
import javax.swing.plaf.basic.BasicRadioButtonUI;

public class JIconRadioButton extends JRadioButton {

  public JIconRadioButton() {
  }

  public void setIcon(final Icon defaultIcon) {
    if(defaultIcon !=  null) {
      super.setIcon(new Icon() {
        protected Icon icon1 = ((BasicRadioButtonUI)getUI()).getDefaultIcon();
        protected Icon icon2 = defaultIcon;
        public int getIconWidth() {
          return icon1.getIconWidth() + icon2.getIconWidth() + getIconTextGap();
        }
        public int getIconHeight() {
          return Math.max(icon1.getIconHeight(), icon2.getIconHeight());
        }
        public void paintIcon(Component c, Graphics g, int x, int y) {
          // TODO: invert for right to left component orientation?
          int h1 = icon1.getIconHeight();
          int h2 = icon2.getIconHeight();
          int max = Math.max(h1, h2);
          icon1.paintIcon(c, g, x, y + (max - h1) / 2);
          icon2.paintIcon(c, g, x + icon1.getIconWidth() + getIconTextGap(), y + (max - h2) / 2);
        }
      });
    } else {
      super.setIcon(defaultIcon);
    }
  }

}
