/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.border.Border;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.View;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;

class CLinkImplementation extends JEditorPane implements CLink {

  protected Link handle;

  public Container getSwingComponent() {
    return this;
  }

  public Control getSWTHandle() {
    return handle;
  }

  protected UserAttributeHandler userAttributeHandler;
  
  public UserAttributeHandler getUserAttributeHandler() {
    return userAttributeHandler;
  }
  
  public CLinkImplementation(Link link, int style) {
    setFocusable(false);
    this.handle = link;
    userAttributeHandler = new UserAttributeHandler(this) {
      public void setForeground(Color foreground) {
        super.setForeground(foreground);
        adjustStyles();
      }
      public void setFont(Font font) {
        super.setFont(font);
        adjustStyles();
      }
    };
    setContentType("text/html");
    init(style);
  }
  
  protected void init(int style) {
    setEditable(false);
    putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
    setOpaque(false);
    adjustStyles();
    Utils.installMouseListener(this, handle);
    Utils.installKeyListener(this, handle);
    Utils.installFocusListener(this, handle);
    Utils.installComponentListener(this, handle);
    addHyperlinkListener(new HyperlinkListener() {
      public void hyperlinkUpdate(HyperlinkEvent e) {
        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
          handle.processEvent(e);
        }
      }
    });
  }

  public int getPreferredWidth() {
    String htmlText = getText();
    setText(text);
    int width = super.getPreferredSize().width;
    setText(htmlText);
    return width;
  }
  
  protected void adjustStyles() {
    updateUI();
    LookAndFeelUtils.applyLabelStyle(this);
    setMargin(new Insets(0, 0, 0, 0));
    if((handle.getStyle() & SWT.BORDER) != 0) {
      setBorder(LookAndFeelUtils.getStandardBorder());
    }
    reshape(getX(), getY(), getWidth(), getHeight());
  }
  
  public void reshape(int x, int y, int w, int h) {
    super.reshape(x, y, w, h);
    View globalView = getUI().getRootView(this);
    if(globalView != null) {
      Border border = getBorder();
      if(border != null) {
        Insets insets = border.getBorderInsets(this);
        w -= insets.left + insets.right;
        h -= insets.top + insets.bottom;
      }
      globalView.setSize(w, h);
    }
  }

  public Dimension getMaximumSize() {
    return new Dimension(Integer.MAX_VALUE, super.getMaximumSize().height);
  }

  public boolean isOpaque() {
    return backgroundImageIcon == null && (userAttributeHandler != null && userAttributeHandler.background != null || super.isOpaque());
  }
  protected void paintComponent(Graphics g) {
    Utils.paintTiledImage(this, g, backgroundImageIcon);
    super.paintComponent(g);
  }
  
  public Container getClientArea() {
    return this;
  }

  protected String text = getText();

  public void setLinkText(String text) {
    this.text = text;
    super.setText("<html>" + escapeXML(text) + "</html>");
    adjustStyles();
  }

  public static String escapeXML(String s) {
    if(s == null) {
      return s;
    }
    int length = s.length();
    if(length == 0) {
      return s;
    }
    StringBuffer sb = new StringBuffer((int)(length * 1.1));
    int start = -1;
    int lastEndTag = -1;
    for(int i=0; i<length; i++) {
      char c = s.charAt(i);
      switch(c) {
        case '<':
          start = i;
          break;
        case '>':
          if(start >= 0) {
            String tag = s.substring(start, i);
            String lTag = tag.toLowerCase(Locale.ENGLISH);
            if(lTag.equals("</a")) {
              if(lastEndTag >= 0) {
                String content = Utils.escapeSwingXML(s.substring(lastEndTag + 1, i - 3));
                sb.append("<a href=\"" + content + "\">" + content + "</a>");
                lastEndTag = -1;
                start = -1;
                break;
              }
              sb.append("</a>");
              start = -1;
              break;
            } else if(lTag.startsWith("<a ") || lTag.startsWith("<a")) {
              int hrefIndex = tag.indexOf("href=\"");
              if(hrefIndex == -1) {
                lastEndTag = i;
              } else {
                sb.append(s.substring(start, i + 1));
                start = -1;
              }
              break;
            }
          } else {
            sb.append("&gt;");
          }
          break;
        case '&':
          if(start < 0) {
            sb.append("&amp;");
          }
          break;
//        case '\'':
//          if(start < 0) {
//            sb.append("&apos;");
//          }
//          break;
        case '\"':
          if(start < 0) {
            sb.append("&quot;");
          }
          break;
        default:
          if(start < 0) {
            sb.append(c);
          }
        break;
      }
    }
    if(start >= 0) {
      sb.append(Utils.escapeSwingXML(s.substring(start, s.length())));
    }
    return sb.toString();
  }

  public Color getBackground() {
    return userAttributeHandler != null && userAttributeHandler.background != null? userAttributeHandler.background: super.getBackground();
  }
  public Color getForeground() {
    return userAttributeHandler != null && userAttributeHandler.foreground != null? userAttributeHandler.foreground: super.getForeground();
  }
  public Font getFont() {
    return userAttributeHandler != null && userAttributeHandler.font != null? userAttributeHandler.font: super.getFont();
  }
  public Cursor getCursor() {
    if(Utils.globalCursor != null) {
      return Utils.globalCursor;
    }
    return userAttributeHandler != null && userAttributeHandler.cursor != null? userAttributeHandler.cursor: super.getCursor();
  }
  
  protected ImageIcon backgroundImageIcon;

  public void setBackgroundImage(Image backgroundImage) {
    this.backgroundImageIcon = backgroundImage == null? null: new ImageIcon(backgroundImage);
  }

  public void setBackgroundInheritance(int backgroundInheritanceType) {
//    switch(backgroundInheritanceType) {
//    case NO_BACKGROUND_INHERITANCE: setOpaque(true); break;
//    case PREFERRED_BACKGROUND_INHERITANCE:
//    case BACKGROUND_INHERITANCE: setOpaque(false); break;
//    }
  }

}

/**
 * The label equivalent on the Swing side.
 * @version 1.0 2005.08.20
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public interface CLink extends CControl {

  public static class Factory {
    private Factory() {}

    public static CLink newInstance(Link link, int style) {
      return new CLinkImplementation(link, style);
    }

  }

  public int getPreferredWidth();
  
  public void setLinkText(String text);

}
