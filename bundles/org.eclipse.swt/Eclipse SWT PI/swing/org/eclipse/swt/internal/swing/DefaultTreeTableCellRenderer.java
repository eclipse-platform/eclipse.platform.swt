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

import javax.swing.JLabel;
import javax.swing.tree.DefaultTreeCellRenderer;

public class DefaultTreeTableCellRenderer implements TreeTableCellRenderer {

  static interface CellPainter {
    public void paintCell(Graphics g);
  }

  protected class InnerTreeCellRenderer extends DefaultTreeCellRenderer implements CellPainter {
    protected void paintComponent(Graphics g) {
      DefaultTreeTableCellRenderer.this.paintComponent(renderer, g);
    }
    public void paintCell(Graphics g) {
      super.paintComponent(g);
    }
  }
  
  protected InnerTreeCellRenderer renderer = createInnerTreeCellRenderer();

  protected InnerTreeCellRenderer createInnerTreeCellRenderer() {
    return new InnerTreeCellRenderer();
  }
  
  protected void paintComponent(CellPainter c, Graphics g) {
    c.paintCell(g);
  }

  public Component getTreeTableCellRendererComponent(JTreeTable treeTable, Object value, boolean selected, boolean expanded, boolean leaf, int row, int column, boolean hasFocus) {
    if(column == 0) {
//      treeTable.getVModel().getValueAt(row, column);
//      value = treeTable.getInnerTable().getValueAt(row, treeTable.convertColumnIndexToView(0));
    }
    Component component = renderer.getTreeCellRendererComponent(treeTable.getInnerTree(), value, selected, expanded, leaf, row, hasFocus);
    if(column != 0 && component instanceof JLabel) {
      ((JLabel)component).setIcon(null);
    }
    return component;
  }

}
