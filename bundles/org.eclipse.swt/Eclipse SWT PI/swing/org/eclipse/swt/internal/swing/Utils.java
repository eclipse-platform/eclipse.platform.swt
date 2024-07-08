/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Collections;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

/**
 * General util methods.
 * @version 1.0 2005.03.18
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public class Utils {

  protected Utils() {}

  protected static final String LIGHTPOPUPS_PROPERTY = "swt.swing.lightpopups";
  protected static final String LOOK_AND_FEEL_PROPERTY = "swt.swing.laf";
  protected static final String LOOK_AND_FEEL_DECORATED_PROPERTY = "swt.swing.laf.decorated";
  protected static final String DEFAULT_ARROW_BUTTONS_PROPERTY = "swt.swing.defaultarrowbuttons";
  protected static final String APPLEMENUBAR_PROPERTY = "apple.laf.useScreenMenuBar";

  public static final String SWTSwingGraphics2DClientProperty = "SWTSwingGraphics2DClientProperty";

  public static boolean isLightweightPopups() {
    return "true".equals(System.getProperty(LIGHTPOPUPS_PROPERTY));
  }

  public static void initializeProperties() {
    if(Compatibility.IS_JAVA_5_OR_GREATER) {
      // Specific Sun property to prevent heavyweight components from erasing their background.
      // Used to enhance visual appearance of the tracker. On Java 1.4, tracker is not always on top so this helps to see it.
      System.setProperty("sun.awt.noerasebackground", "true");
    }
    if(System.getProperty(APPLEMENUBAR_PROPERTY) == null) {
      System.setProperty(APPLEMENUBAR_PROPERTY, "true");
    }
  }

  protected static String getLookAndFeel() {
    return System.getProperty(LOOK_AND_FEEL_PROPERTY);
  }

  public static void installDefaultLookAndFeel() {
    boolean isLookAndFeelInstalled = false;
    String lafName = getLookAndFeel();
    if(lafName != null) {
      LookAndFeel lookAndFeel = javax.swing.UIManager.getLookAndFeel();
      if(lookAndFeel != null && lafName.equals(lookAndFeel.getClass().getName())) {
        isLookAndFeelInstalled = true;
      } else {
        try {
          javax.swing.UIManager.setLookAndFeel(lafName);
          isLookAndFeelInstalled = true;
          Boolean isLookAndFeelDecorated = isLookAndFeelDecorated();
          if(isLookAndFeelDecorated != null) {
            boolean isLafDecorated = isLookAndFeelDecorated.booleanValue();
            JFrame.setDefaultLookAndFeelDecorated(isLafDecorated);
            JDialog.setDefaultLookAndFeelDecorated(isLafDecorated);
          }
        } catch(Exception e) {e.printStackTrace();}
      }
    }
    // If no look and feel is specified, install one that looks native.
    if(!isLookAndFeelInstalled) {
      try {
        javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
      } catch(Exception e) {}
    }
  }
  
  
  protected static Boolean isLookAndFeelDecorated() {
    String value = System.getProperty(LOOK_AND_FEEL_DECORATED_PROPERTY);
    return value == null? null: new Boolean(value);
  }

  public static Boolean isUsingDefaultArrowButtons() {
    String value = System.getProperty(DEFAULT_ARROW_BUTTONS_PROPERTY);
    return value == null? null: new Boolean(value);
  }
  
  protected static Canvas panel = new Canvas();

  public static Component getDefaultComponent() {
    return panel;
  }

  static void installMouseListener(Component component, final Control control) {
    component.addMouseListener(new MouseListener() {
      public void mousePressed(MouseEvent e) {
        control.processEvent(e);
      }
      public void mouseReleased(MouseEvent e) {
        control.processEvent(e);
      }
      public void mouseClicked(MouseEvent e) {
        control.processEvent(e);
      }
      public void mouseEntered(MouseEvent e) {
        control.processEvent(e);
      }
      public void mouseExited(MouseEvent e) {
        control.processEvent(e);
      }
    });
    component.addMouseMotionListener(new MouseMotionListener() {
      public void mouseDragged(MouseEvent e) {
        control.processEvent(e);
      }
      public void mouseMoved(MouseEvent e) {
        control.processEvent(e);
      }
    });
    component.addMouseWheelListener(new MouseWheelListener() {
      public void mouseWheelMoved(MouseWheelEvent e) {
        control.processEvent(e);
      }
    });
  }

  static void installKeyListener(Component component, final Control control) {
    component.setFocusTraversalKeysEnabled(false);
    component.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
    component.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, Collections.EMPTY_SET);
    component.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        control.processEvent(e);
      }
      public void keyReleased(KeyEvent e) {
        control.processEvent(e);
      }
      public void keyTyped(KeyEvent e) {
        control.processEvent(e);
      }
    });
  }

  static void installFocusListener(Component component, final Control control) {
    component.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent e) {
        control.processEvent(e);
      }
      public void focusLost(FocusEvent e) {
        control.processEvent(e);
      }
    });
  }

  static void installComponentListener(Component component, final Control control) {
    component.addComponentListener(new ComponentListener() {
      public void componentHidden(ComponentEvent e) {
        control.processEvent(e);
      }
      public void componentShown(ComponentEvent e) {
        control.processEvent(e);
      }
      public void componentResized(ComponentEvent e) {
        control.processEvent(e);
      }
      public void componentMoved(ComponentEvent e) {
        control.processEvent(e);
      }
    });
  }

  public static String escapeSwingXML(String s) {
    if(s == null) {
      return s;
    }
    int length = s.length();
    if(length == 0) {
      return s;
    }
    StringBuffer sb = new StringBuffer((int)(length * 1.1));
    for(int i=0; i < length; i++) {
      char c = s.charAt(i);
      switch(c) {
        case '<':
          sb.append("&lt;");
          break;
        case '>':
          sb.append("&gt;");
          break;
        case '&':
          sb.append("&amp;");
          break;
//        case '\'':
//          sb.append("&apos;");
//          break;
        case '\"':
          sb.append("&quot;");
          break;
        default:
          sb.append(c);
        break;
      }
    }
    return sb.toString();
  }

  public static String unescapeXML(String s) {
    if(s == null) {
      return s;
    }
    int length = s.length();
    if(length < 3) {
      return s;
    }
    char[] chars = new char[length];
    int pos = 0;
    for (int i = 0; i < length; i++) {
      char c = s.charAt(i);
      if(c == '&') {
        String right = s.substring(i + 1);
        if(right.startsWith("lt;")) {
          chars[pos] = '<';
          i += 3;
        } else if(right.startsWith("gt;")) {
          chars[pos] = '>';
          i += 3;
        } else if(right.startsWith("amp;")) {
          chars[pos] = '&';
          i += 4;
        } else if(right.startsWith("apos;")) {
          chars[pos] = '\'';
          i += 5;
        } else if(right.startsWith("quot;")) {
          chars[pos] = '\"';
          i += 5;
        } else {
          chars[pos++] = c;
        }
      } else {
        chars[pos++] = c;
      }
    }
    if(pos == chars.length) {
      return s;
    }
    return new String(chars, 0, pos);
  }

  public static String convertStringToHTML(String string) {
    if (string == null) {
      return null;
    }
    StringBuffer sb = new StringBuffer("<html>");
    for(int i=0; i<string.length(); i++) {
      char c = string.charAt(i);
      switch(c) {
      case '\r':
        sb.append("<p>");
        break;
      case '\n':
        sb.append("<p>");
        break;
      default:
        sb.append(Utils.escapeSwingXML(String.valueOf(c)));
        break;
      }
    }
    sb.append("</html>");
    return sb.toString();
  }

  public static int convertDnDActionsToSWT(int actions) {
    if(actions == 0) {
      return 0;
    }
    int swtActions = 0;
    if((actions & DnDConstants.ACTION_COPY) != 0) {
      swtActions |= DND.DROP_COPY;
    }
    if((actions & DnDConstants.ACTION_MOVE) != 0) {
      swtActions |= DND.DROP_MOVE;
    }
    if((actions & DnDConstants.ACTION_LINK) != 0) {
      swtActions |= DND.DROP_LINK;
    }
    return swtActions;
  }

  public static int convertDnDActionsToSwing(int actions) {
    if(actions == 0) {
      return 0;
    }
    int swingActions = 0;
    if((actions & DND.DROP_COPY) != 0) {
      swingActions |= DnDConstants.ACTION_COPY;
    }
    if((actions & DND.DROP_MOVE) != 0) {
      swingActions |= DnDConstants.ACTION_MOVE;
    }
    if((actions & DND.DROP_LINK) != 0) {
      swingActions |= DnDConstants.ACTION_LINK;
    }
    return swingActions;
  }
  
  static long timeStamp = System.currentTimeMillis();

  public static int getCurrentTime () {
    return (int)(System.currentTimeMillis() - timeStamp);
  }

  public static boolean isFlatLayout(Control control) {
    if(!(control instanceof Composite)) {
      return false;
    }
    Layout layout = ((Composite)control).getLayout();
    if(layout == null) {
      return false;
    }
    if(layout instanceof FillLayout || layout instanceof RowLayout || layout instanceof StackLayout) {
      return true;
    }
    return false;
  }

  /**
   * Get the line number for debuggin purposes.
   * @return The line number of the caller, or a negative number if it could not be obtained.
   */
  public static int getLineNumber() {
    StackTraceElement[] stackTraceElements;
    if(Compatibility.IS_JAVA_5_OR_GREATER) {
      stackTraceElements = Thread.currentThread().getStackTrace();
    } else {
      stackTraceElements = new Exception().getStackTrace();
    }
    int i = 0;
    for(; i<stackTraceElements.length; i++) {
      StackTraceElement stackElement = stackTraceElements[i];
      if(stackElement.getClassName().equals("org.eclipse.swt.internal.swing.Utils") && stackElement.getMethodName().equals("getLineNumber")) {
        return stackTraceElements[i + 1].getLineNumber();
      }
    }
    return -1;
  }
  
  /**
   * Indicates that the method is not implemented. It prints the corresponding frame from
   * the stack trace to the standard error if the "swt.swing.debug" property is defined.
   */
  public static void notImplemented() {
    if(System.getProperty("swt.swing.debug") == null) {
      return;
    }
    StackTraceElement[] stackTraceElements;
    if(Compatibility.IS_JAVA_5_OR_GREATER) {
      stackTraceElements = Thread.currentThread().getStackTrace();
    } else {
      stackTraceElements = new Exception().getStackTrace();
    }
    int i = 0;
    for(; i<stackTraceElements.length; i++) {
      StackTraceElement stackElement = stackTraceElements[i];
      if(stackElement.getClassName().equals("org.eclipse.swt.internal.swing.Utils") && stackElement.getMethodName().equals("notImplemented")) {
        System.out.println("Not implemented: " + stackTraceElements[i + 1]);
        break;
      }
    }
    return;
  }

  public static void dumpTree(Control control) {
    Shell shell = control.getShell();
    dumpControlTree(shell, 0, shell == control? null: control);
  }

  protected static void dumpControlTree(Control control, int depth, Control referenceControl) {
    StringBuffer sb = new StringBuffer();
    if(depth > 0) {
      int count = depth;
      if(control == referenceControl) {
        sb.append("> ");
        count--;
      }
      while(count-- > 0) {
        sb.append("  ");
      }
    }
    sb.append(control);
    Rectangle bounds = control.getBounds();
    sb.append(" - [").append(bounds.x).append(", ").append(bounds.y).append(", ").append(bounds.width).append(", ").append(bounds.height).append(']');
    if(control instanceof Composite) {
      Layout layout = ((Composite)control).getLayout();
      if(layout != null) {
        sb.append(" - ").append(layout.getClass().getName());
      }
    }
    System.out.println(sb.toString());
    if(control instanceof Composite) {
      Control[] children = ((Composite)control).getChildren();
      for(int i=0; i<children.length; i++) {
        dumpControlTree(children[i], depth + 1, referenceControl);
      }
    }
  }
  
  public static void paintComponentImmediately(Component component) {
    if(SwingUtilities.isEventDispatchThread()) {
      synchronized(component.getTreeLock()) {
        RepaintManager repaintManager = RepaintManager.currentManager(component);
        repaintManager.validateInvalidComponents();
        repaintManager.paintDirtyRegions();
      }
      return;
    }
    synchronized(component.getTreeLock()) {
      component.paint(component.getGraphics());
    }
  }

//  public static String getSWTSwingUIThreadsNamePrefix() {
//    return "SWTSwing UI - ";
//  }

  public static void paintTiledImage(Component component, Graphics g, ImageIcon backgroundImageIcon) {
    Dimension size = component.getSize();
    paintTiledImage(g, backgroundImageIcon, 0, 0, size.width, size.height);
  }
  
  public static void paintTiledImage(Graphics g, ImageIcon backgroundImageIcon, int x, int y, int width, int height) {
    if(backgroundImageIcon == null) {
      return;
    }
    int iconWidth = backgroundImageIcon.getIconWidth();
    int iconHeight = backgroundImageIcon.getIconHeight();
    if(iconWidth <= 0 || iconHeight <= 0) {
      return;
    }
    int xCount = width / iconWidth + (width % iconWidth == 0? 0: 1);
    int yCount = height / iconHeight + (height % iconHeight == 0? 0: 1);
    Image image = backgroundImageIcon.getImage();
    for(int i=0; i<xCount; i++) {
      for(int j=0; j<yCount; j++) {
        g.drawImage(image, i * iconWidth + x, j * iconHeight + y, null);
      }
    }
  }
  
  public static void throwUncheckedException(Throwable e) {
    if(e == null) {
      return;
    }
    if(e instanceof Error) {
      throw (Error)e;
    } else if(e instanceof RuntimeException) {
      throw (RuntimeException)e;
    } else {
      e.printStackTrace();
    }
  }
  
  public static int previousModifiersEx;
  public static int modifiersEx;

  public static void storeModifiersEx(int modifiersEx) {
    Utils.previousModifiersEx = Utils.modifiersEx;
    Utils.modifiersEx = modifiersEx;
  }
  
  public static boolean isLocalDragAndDropInProgress;
  
  protected static Point mouseLocation;
  protected static Component mouseComponent;
  
  public static void trackMouseProperties(MouseEvent me) {
    Component c = me.getComponent();
    if(c.isShowing()) {
      mouseLocation = me.getPoint();
      SwingUtilities.convertPointToScreen(mouseLocation, c);
      if(me.getID() == MouseEvent.MOUSE_EXITED) {
        if(mouseComponent == c) {
          mouseComponent = null;
        }
      } else {
        mouseComponent = c;
      }
    }
  }
  
  public static Point getTrakedMouseLocation() {
    return mouseLocation == null? new Point(0, 0): mouseLocation;
  }
  
  public static Control getTrakedMouseControl() {
    if(mouseComponent == null) {
      return null;
    }
    for(Component c=mouseComponent; c != null; c = c.getParent()) {
      if(c instanceof CControl) {
        return ((CControl)c).getSWTHandle();
      }
    }
    return null;
  }
  
  public static Control capturedControl;
  
  public static boolean redispatchEvent(Control control, AWTEvent e) {
    if(e instanceof MouseEvent && capturedControl != null && capturedControl != control) {
      Component target = ((CControl)capturedControl.handle).getClientArea();
      MouseEvent me = (MouseEvent)e;
      java.awt.Point point = SwingUtilities.convertPoint((Component)me.getSource(), me.getX(), me.getY(), target);
      if(me.getID() == MouseEvent.MOUSE_WHEEL) {
        MouseWheelEvent mwe = (MouseWheelEvent)me;
        me = new MouseWheelEvent(target, mwe.getID(), mwe.getWhen(), mwe.getModifiers(), point.x, point.y, mwe.getClickCount(), mwe.isPopupTrigger(), mwe.getScrollType(), mwe.getScrollAmount(), mwe.getWheelRotation());
      } else {
        me = new MouseEvent(target, me.getID(), me.getWhen(), me.getModifiers(), point.x, point.y, me.getClickCount(), me.isPopupTrigger());
      }
      target.dispatchEvent(me);
      return true;
    }
    return false;
  }
  
  public static Cursor globalCursor;
  
  public static void setGlobalCursor(Cursor globalCursor) {
    Utils.globalCursor = globalCursor;
  }
  
}
