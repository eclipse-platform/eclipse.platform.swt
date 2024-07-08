/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;

public class CComboButton extends JButton implements SwingConstants {

  private static final int ARROW_SPACE_WIDTH = 7;
  private static final int ARROW_WIDTH = 5;

  protected MouseInputAdapter mouseHandler = new MouseInputAdapter() {
    public void mouseExited(MouseEvent e) {
      isMouseOver = false;
      isArrowMouseOver = false;
      repaint();
    }
    public void mouseEntered(MouseEvent e) {
      isMouseOver = true;
      processMouseEvent(e);
      repaint();
    }
    public void mouseMoved(MouseEvent e) {
      processMouseEvent(e);
    }
    public void mouseReleased(MouseEvent e) {
      processMouseEvent(e);
    }
    protected void processMouseEvent(MouseEvent e) {
      int right = originalBorder.getBorderInsets(e.getComponent()).right + ARROW_SPACE_WIDTH;
      isArrowMouseOver = e.getX() > getWidth() - right;
      if(isArrowMouseOver) {
        setActionCommand("Arrow");
      } else {
        setActionCommand("");
      }
    }
  };

  protected Border originalBorder = getBorder();
  protected boolean isMouseOver;
  protected boolean isArrowMouseOver;

  public CComboButton() {
    setMargin(new Insets(0, 1, 0, 1));
    setBorder(BorderFactory.createCompoundBorder(originalBorder, BorderFactory.createEmptyBorder(0, 0, 0, ARROW_SPACE_WIDTH + 1)));
    setModel(new DefaultButtonModel() {
      public boolean isPressed() {
        return super.isPressed() && !isArrowMouseOver;
      }
    });
    addMouseListener(mouseHandler);
    addMouseMotionListener(mouseHandler);
    addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        boolean oldIsArrowMouseOver = isArrowMouseOver;
        isArrowMouseOver = false;
        getModel().setPressed(false);
        isArrowMouseOver = oldIsArrowMouseOver;
      }
    });
  }
  
  public static boolean isArrowEvent(ActionEvent e) {
    return "Arrow".equals(e.getActionCommand());
  }
  
  protected void fireActionPerformed(ActionEvent event) {
    if(isArrowMouseOver == isArrowEvent(event)) {
      super.fireActionPerformed(event);
    }
  }
  
  protected static Color shadow = UIManager.getColor("ComboBox.buttonShadow");
  protected static Color darkShadow = UIManager.getColor("ComboBox.buttonDarkShadow");
  protected static Color highlight = UIManager.getColor("ComboBox.buttonHighlight");

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int w = getWidth();
    int h = getHeight();
    Insets borderInsets = originalBorder.getBorderInsets(this);
    int x = w - ARROW_SPACE_WIDTH - borderInsets.right + 1;
    boolean isEnabled = isEnabled();
    if(isMouseOver && isEnabled) {
      g.setColor(shadow);
      g.drawLine(x, borderInsets.top + 1, x, h - borderInsets.bottom - 1);
    }
    Color origColor;
    origColor = g.getColor();
    int size = (ARROW_WIDTH + 1) / 2;
    paintTriangle(g, x - 1 + ARROW_WIDTH, (h - size) / 2, size, isEnabled);
    g.setColor(origColor);
  }

  public Dimension getPreferredSize() {
    Dimension size = super.getPreferredSize();
    size.height = Math.max(size.height, 12);
    return size;
  }

  protected void paintTriangle(java.awt.Graphics g, int x, int y, int size, boolean isEnabled) {
    java.awt.Color oldColor = g.getColor();
    size = Math.max(size, 2);
    int mid = (size / 2) - 1;
    g.translate(x, y);
    if (isEnabled) {
      g.setColor(darkShadow);
//    } else {
//      g.setColor(shadow);
    } else if (!isEnabled) {
      g.translate(1, 1);
      g.setColor(highlight);
      int j=0;
      for (int i = size - 1; i >= 0; i--) {
        g.drawLine(mid - i, j, mid + i, j);
        j++;
      }
      g.translate(-1, -1);
      g.setColor(shadow);
    }
    int j=0;
    for (int i = size - 1; i >= 0; i--) {
      g.drawLine(mid - i, j, mid + i, j);
      j++;
    }
    g.translate(-x, -y);
    g.setColor(oldColor);
  }

}
