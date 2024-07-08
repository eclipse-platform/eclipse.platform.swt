/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.MenuComponent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

public class JTracker {

  public static class TrackerEvent {
    
    protected int x;
    protected int y;
    
    TrackerEvent(int x, int y) {
      this.x = x;
      this.y = y;
    }
    
    public int getX() {
      return x;
    }
    
    public int getY() {
      return y;
    }
    
  }
  
  public static interface TrackerListener extends EventListener {
    public void trackerMoved(TrackerEvent e);
    public void trackerResized(TrackerEvent e);
  }
  
  public static final int SINGLE_LINE_APPEARANCE = 0;
  public static final int THICK_BORDER_APPEARANCE = 1;
  
  public static final int UP_MASK = 1 << 0;
  public static final int LEFT_MASK = 1 << 1;
  public static final int DOWN_MASK = 1 << 2;
  public static final int RIGHT_MASK = 1 << 3;
  
  protected Window[][] windowsArray = new Window[0][];
  protected Rectangle[] rectangles;
  
  protected boolean isResizeType;
  protected Point offset;
  protected Rectangle clipBounds;
  
  public JTracker(boolean isResizeType) {
    this(null, isResizeType);
  }
  
  public JTracker(Component component, boolean isResizeType) {
    this.isResizeType = isResizeType;
    if(component != null) {
      offset = component.getLocationOnScreen();
      clipBounds = new Rectangle(component.getSize());
      Graphics g = component.getGraphics();
      if(g != null) {
        Rectangle gClipBounds = g.getClipBounds();
        if(gClipBounds != null) {
          clipBounds = gClipBounds.intersection(clipBounds);
        }
      }
      clipBounds.x += offset.x;
      clipBounds.y += offset.y;
    }
  }
  
  protected Window sharedOwnerWindow;
  
  public void setRectangles(Rectangle[] rectangles) {
    if(rectangles != null) {
      Rectangle[] rectanglesCopy = new Rectangle[rectangles.length];
      for(int i=0; i<rectangles.length; i++) {
        Rectangle rectangle = new Rectangle(rectangles[i]);
        if(offset != null) {
          rectangle.x += offset.x;
          rectangle.y += offset.y;
        }
        rectanglesCopy[i] = rectangle;
      }
      rectangles = rectanglesCopy;
    }
    this.rectangles = rectangles;
    if(rectangles == null) {
      rectangles = new Rectangle[0];
    }
    Window[][] newWindowsArray = new Window[rectangles.length][];
    for(int i=rectangles.length; i<windowsArray.length; i++) {
      Window[] windows = windowsArray[i];
      for(int j=0; j<windows.length; j++) {
        Window window = windows[j];
        if(window != null) {
          window.dispose();
        }
      }
    }
    System.arraycopy(windowsArray, 0, newWindowsArray, 0, Math.min(windowsArray.length, rectangles.length));
    windowsArray = newWindowsArray;
    postAdjustWindows();
  }
  
  public Rectangle[] getRectangles() {
    if(rectangles == null) {
      return null;
    }
    Rectangle[] newRectangles = new Rectangle[rectangles.length];
    for(int i=0; i<rectangles.length; i++) {
      Rectangle rectangle = new Rectangle(rectangles[i]);
      if(offset != null) {
        rectangle.x -= offset.x;
        rectangle.y -= offset.y;
      }
      newRectangles[i] = rectangle;
    }
    return newRectangles;
  }

  protected boolean isCanceled;
  protected boolean isVisible;
  protected Point lastMouseLocation;
  
  protected void processKeyEvent(KeyEvent ke) {
    if(ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
      isCanceled = true;
      hide();
    }
  }
  
  protected void processMouseEvent(MouseEvent me) {
    switch(me.getID()) {
      case MouseEvent.MOUSE_PRESSED:
      case MouseEvent.MOUSE_RELEASED:
        hide();
        break;
      case MouseEvent.MOUSE_MOVED:
      case MouseEvent.MOUSE_DRAGGED:
        Point newLocation = me.getPoint();
        SwingUtilities.convertPointToScreen(newLocation, me.getComponent());
        if(lastMouseLocation != null) {
          int dx = newLocation.x - lastMouseLocation.x;
          int dy = newLocation.y - lastMouseLocation.y;
          lastMouseLocation = newLocation;
          processMouseMove(dx, dy);
        } else {
          lastMouseLocation = newLocation;
        }
        break;
    }
  }
  
  protected int edgeReiszeMask;
  
  protected void processMouseMove(int dx, int dy) {
    if(isResizeType) {
      if(dx != 0 && (edgeReiszeMask & (LEFT_MASK | RIGHT_MASK)) == 0) {
        if(dx > 0) {
          if((constraints & RIGHT_MASK) != 0) {
            edgeReiszeMask |= RIGHT_MASK;
          }
        } else if((constraints & LEFT_MASK) != 0) {
          edgeReiszeMask |= LEFT_MASK;
        }
      }
      if(dy != 0 && (edgeReiszeMask & (UP_MASK | DOWN_MASK)) == 0) {
        if(dy > 0) {
          if((constraints & DOWN_MASK) != 0) {
            edgeReiszeMask |= DOWN_MASK;
          }
        } else if((constraints & UP_MASK) != 0) {
          edgeReiszeMask |= UP_MASK;
        }
      }
    } else {
      if(dx < 0) {
        if((constraints & LEFT_MASK) == 0) {
          dx = 0;
        }
      } else if((constraints & RIGHT_MASK) == 0) {
        dx = 0;
      }
      if(dy < 0) {
        if((constraints & UP_MASK) == 0) {
          dy = 0;
        }
      } else if((constraints & DOWN_MASK) == 0) {
        dy = 0;
      }
    }
    if(dx == 0 && dy == 0 || isResizeType && edgeReiszeMask == 0) {
      return;
    }
    for(int i=0; i<rectangles.length; i++) {
      Rectangle rectangle = rectangles[i];
      if(isResizeType) {
        if((edgeReiszeMask & LEFT_MASK) != 0) {
          rectangle.x += dx;
          rectangle.width -= dx;
        } else if((edgeReiszeMask & RIGHT_MASK) != 0) {
          rectangle.width += dx;
        }
        if((edgeReiszeMask & UP_MASK) != 0) {
          rectangle.y += dy;
          rectangle.height -= dy;
        } else if((edgeReiszeMask & DOWN_MASK) != 0) {
          rectangle.height += dy;
        }
      } else {
        if(dx < 0 && ((constraints & LEFT_MASK) != 0) || dx > 0 && ((constraints & RIGHT_MASK) != 0)) {
          rectangle.x += dx;
        }
        if(dy < 0 && ((constraints & UP_MASK) != 0) || dy > 0 && ((constraints & DOWN_MASK) != 0)) {
          rectangle.y += dy;
        }
      }
    }
    postAdjustWindows();
    Object[] listeners = listenerList.getListenerList();
    TrackerEvent e = null;
    for(int i=listeners.length-2; i>=0; i-=2) {
      if(listeners[i] == TrackerListener.class) {
        if(e == null) {
          e = new TrackerEvent(lastMouseLocation.x, lastMouseLocation.y);
        }
        if(isResizeType) {
          ((TrackerListener)listeners[i + 1]).trackerResized(e);
        } else {
          ((TrackerListener)listeners[i + 1]).trackerMoved(e);
        }
      }
    }
  }
  
  protected int appearance = SINGLE_LINE_APPEARANCE;
  
  public void setAppearance(int appearance) {
    this.appearance = appearance;
    postAdjustWindows();
  }
  
  protected boolean isAdjusmentPending;
  
  protected void postAdjustWindows() {
    if(isAdjusmentPending) {
      return;
    }
    isAdjusmentPending = true;
    new Thread() {
      public void run() {
        try {
          sleep(10);
        } catch(Exception e) {}
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            adjustWindows();
          }
        });
      }
    }.start();
  }
  
  protected void adjustWindows() {
    isAdjusmentPending = false;
//    if(!Compatibility.IS_JAVA_5_OR_GREATER) {
//      return;
//    }
    if(!isVisible || rectangles.length == 0) {
      return;
    }
    int borderWidth = appearance == THICK_BORDER_APPEARANCE? 3: 1;
    for(int i=0; i<rectangles.length; i++) {
      Rectangle rectangle = rectangles[i];
      Rectangle bounds = new Rectangle(rectangle);
      if(bounds.width < 0) {
        bounds.width = -bounds.width;
        bounds.x -= bounds.width;
      }
      if(bounds.height < 0) {
        bounds.height = -bounds.height;
        bounds.y -= bounds.height;
      }
      Window[] windows = windowsArray[i];
      if(windows == null) {
        windows = new Window[4];
        windowsArray[i] = windows;
      }
      for(int j=0; j<4; j++) {
        Window window = windows[j];
        if(window == null) {
          window = createTrackerWindow(sharedOwnerWindow);
          if(Compatibility.IS_JAVA_5_OR_GREATER) {
            window.setAlwaysOnTop(true);
          }
          windows[j] = window;
        }
        Rectangle newBounds = null;
        switch(j) {
          case 0: newBounds = new Rectangle(bounds.x, bounds.y, bounds.width, borderWidth); break;
          case 1: newBounds = new Rectangle(bounds.x, bounds.y, borderWidth, bounds.height); break;
          case 2: newBounds = new Rectangle(bounds.x, bounds.y + bounds.height - borderWidth, bounds.width, borderWidth); break;
          case 3: newBounds = new Rectangle(bounds.x + bounds.width - borderWidth, bounds.y, borderWidth, bounds.height); break;
        }
        if(clipBounds != null) {
          newBounds = newBounds.intersection(clipBounds);
        }
        if(newBounds.width <= 0 || newBounds.height <= 0) {
          window.setVisible(false);
        } else {
          if(!window.getBounds().equals(newBounds)) {
            window.setBounds(newBounds);
          }
          if(window.isVisible() != isVisible) {
            window.setVisible(isVisible);
          }
        }
        sharedOwnerWindow.setCursor(null);
      }
    }
  }
  
  protected class TrackerWindow extends Window {
    public TrackerWindow(Window ownerWindow) {
      super(ownerWindow);
      setBackground(Color.BLACK);
    }
    public void update(Graphics g) {
      paint(g);
    }
    public void paint(Graphics g) {
      super.paint(g);
      Rectangle bounds = getBounds();
      switch(appearance) {
        case THICK_BORDER_APPEARANCE:
          Color background = sharedOwnerWindow.getBackground();
          for(int i=0; i<bounds.width; i++) {
            for(int j=0; j<bounds.height; j++) {
              if((i + j + bounds.x + bounds.y) % 2 == 0) {
                g.setColor(Color.BLACK);
                g.drawLine(i, j, i, j);
              } else {
                g.setColor(background);
                g.drawLine(i, j, i, j);
              }
            }
          }
          break;
        case SINGLE_LINE_APPEARANCE:
          g.setColor(Color.BLACK);
          if(bounds.width == 1) {
            g.drawLine(0, 0, 0, bounds.height - 1);
          } else {
            g.drawLine(0, 0, bounds.width - 1, 0);
          }
          break;
      }
    }
    public boolean getFocusableWindowState() {
      return false;
    }
    public boolean contains(int x, int y) {
      return false;
    }
  }
  
  protected TrackerWindow createTrackerWindow(Window ownerWindow) {
    return new TrackerWindow(ownerWindow);
  }
  
  protected boolean isAdjusting;
  
  /**
   * @return true if the tracked was not canceled.
   */
  public boolean show() {
    isGenericWindow = false;
    sharedOwnerWindow = createSharedOwnerWindow();
    isCanceled = false;
    isVisible = true;
    if(Compatibility.IS_JAVA_5_OR_GREATER) {
      lastMouseLocation = MouseInfo.getPointerInfo().getLocation();
    }
    adjustWindows();
    // We assume we are in the event dispatch thread, so we simulate modality here.
    while(isVisible) {
      try {
        AWTEvent event = Toolkit.getDefaultToolkit().getSystemEventQueue().getNextEvent();
        Object src = event.getSource();
        if (event instanceof ActiveEvent) {
          ((ActiveEvent)event).dispatch();
        } else if (src instanceof Component) {
          boolean dispatch = false;
          if(event instanceof MouseEvent) {
            processMouseEvent((MouseEvent)event);
            dispatch = !isVisible;
          } else if(event instanceof KeyEvent) {
            processKeyEvent((KeyEvent)event);
            dispatch = !isVisible;
          } else {
            dispatch = true;
          }
          if(dispatch) {
            ((Component)src).dispatchEvent(event);
            if(!Compatibility.IS_JAVA_5_OR_GREATER && isGenericWindow) {
              SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                  for(int i=0; i<windowsArray.length; i++) {
                    Window[] windows = windowsArray[i];
                    if(windows != null) {
                      for(int j=0; j<windows.length; j++) {
                        windows[j].toFront();
                      }
                    }
                  }
                }
              });
            }
          }
        } else if (src instanceof MenuComponent) {
          ((MenuComponent)src).dispatchEvent(event);
        }
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
    return !isCanceled;
  }
  
  public void hide() {
    this.isVisible = false;
    lastMouseLocation = null;
    for(int i=0; i<windowsArray.length; i++) {
      Window[] windows = windowsArray[i];
      if(windows != null) {
        for(int j=0; j<windows.length; j++) {
          Window window = windows[j];
          if(window != null) {
            window.dispose();
          }
        }
      }
    }
    releaseSharedOwnerWindow(sharedOwnerWindow);
    sharedOwnerWindow = null;
  }
  
  public Point getLastMouseLocation() {
    return lastMouseLocation;
  }
  
  protected Window getSharedOwnerWindow() {
    return sharedOwnerWindow;
  }
  
  protected boolean isGenericWindow;
  
  protected Window createSharedOwnerWindow() {
    isGenericWindow = true;
    return new JFrame();
  }
  
  protected void releaseSharedOwnerWindow(Window sharedOwnerWindow) {
    sharedOwnerWindow.dispose();
  }
  
  public boolean isVisible() {
    return isVisible;
  }

  protected int constraints = UP_MASK | LEFT_MASK | DOWN_MASK | RIGHT_MASK;
  
  public void setConstraints(int constraints) {
    if(constraints == 0) {
      constraints = UP_MASK | LEFT_MASK | DOWN_MASK | RIGHT_MASK;
    }
    this.constraints = constraints;
  }
  
  protected EventListenerList listenerList = new EventListenerList();

  public void addTrackerListener(TrackerListener listener) {
    listenerList.add(TrackerListener.class, listener);
  }
  
  public void removeTrackerListener(TrackerListener listener) {
    listenerList.remove(TrackerListener.class, listener);
  }
  
  public TrackerListener[] getTrackerListeners() {
    return (TrackerListener[])listenerList.getListeners(TrackerListener.class);
  }
  
}
