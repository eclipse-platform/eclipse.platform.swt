/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.metal.MetalScrollButton;

/**
 * An arrow button.
 * @version 1.0 2003.08.03
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public class ArrowButton extends BasicArrowButton {

  protected BasicArrowButton arrowButton;
  
  /**
   * Construct an arrow button using the current look and feel. 
   * @param direction the direction of the arrow.
   */
  public ArrowButton(int direction) {
    super(direction);
    setBorder(LookAndFeelUtils.getButtonBorder());
    createArrowButton();
  }
  
  public void paint(Graphics g) {
    if(arrowButton != null) {
      Dimension size = getSize();
      arrowButton.setSize(size);
      arrowButton.paint(g);
    } else {
      super.paint(g);
    }
  }
  
  /**
   * Set the direction the arrow is pointing to.
   * @param direction The direction of the arrow.
   */
  public void setDirection(int direction) {
    if(direction == getDirection()) {
      return;
    }
    super.setDirection(direction);
    createArrowButton();
    repaint();
  }
  
  protected void createArrowButton() {
    // Scroll bar buttons are not always applicable... so we need an external parameter if the default behavior is not right.
    Boolean isNotCreating = Utils.isUsingDefaultArrowButtons();
    if(isNotCreating != null && isNotCreating.booleanValue()) {
      return;
    }
    arrowButton = null;
    setPreferredSize(null);
    JComponent component = new JScrollBar();
    Component[] components = component.getComponents();
    for(int i=0; i<components.length; i++) {
      Component c = components[i];
      if(c instanceof BasicArrowButton) {
        if(isNotCreating == null && c instanceof MetalScrollButton) {
          return;
        }
        arrowButton = (BasicArrowButton)c;
        arrowButton.setDirection(direction);
        arrowButton.setModel(getModel());
        int width = component.getPreferredSize().width;
        setPreferredSize(new Dimension(width, width));
        return;
      }
    }
  }

}
