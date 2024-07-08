/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.lang.reflect.Method;

import javax.swing.SwingUtilities;

import org.eclipse.swt.widgets.Display;

/**
 * General UI Thread realted utility methods.
 * @author Christopher Deckers (chrriis@nextencia.net)
 */
public class UIThreadUtils {

  protected UIThreadUtils() {}
  
  public static class SwingEventQueue extends EventQueue {
    protected AWTEvent event;
    public boolean sleep() {
      event = null;
      try {
        event = getNextEvent();
      } catch(InterruptedException e) {}
      return event != null;
    }
    public boolean dispatchEvent() {
      if(event != null) {
        AWTEvent theEvent = event;
        event = null;
        try {
          dispatchEvent(theEvent);
        } catch(Throwable t) {
          t.printStackTrace();
        }
      }
      return false;
    }
    public void pop() {
      super.pop();
    }
  }
  
  protected static Throwable exception;
  
  public static void storeException(Throwable exception) {
    UIThreadUtils.exception = exception;
  }
  
  public static void throwStoredException() {
    Throwable e = exception;
    exception = null;
    Utils.throwUncheckedException(e);
  }
  
  protected static Thread mainThread;
  
  public static void setMainThread(Thread mainThread) {
    UIThreadUtils.mainThread = mainThread;
    if(Compatibility.IS_JAVA_5_OR_GREATER) {
      new Compatibility.ProtectedCode() {{
        if(!isRealDispatch()) {
          UIThreadUtils.mainThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable t) {
              t.printStackTrace();
              monitorShutdown();
            }
          });
        }
      }};
    }
  }
  
  public static SwingEventQueue swingEventQueue;

  protected static void pushQueue() {
    if(isRealDispatch()) {
      EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      if(eventQueue != swingEventQueue) {
        UIThreadUtils.exclusiveSectionCount++;
        eventQueue.push(swingEventQueue);
      }
    }
  }

  public static void popQueue() {
    if(isRealDispatch()) {
      EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
      if(eventQueue == swingEventQueue) {
        swingEventQueue.pop();
        swingEventQueue = null;
        UIThreadUtils.exclusiveSectionCount--;
      }
    }
  }

  public static boolean isRealDispatch() {
    return swingEventQueue != null;
  }

  public static void main(final String[] args) {
    swtExec(new Runnable() {
      public void run() {
        try {
          Method method = Class.forName(args[0]).getDeclaredMethod("main", new Class[] {String[].class});
          String[] newArgs = new String[args.length - 1];
          System.arraycopy(args, 1, newArgs, 0, newArgs.length);
          method.invoke(null, new Object[] {newArgs});
        } catch(Throwable t) {
          t.printStackTrace();
        }
      }
    });
  }

  public static void swtExec(Runnable runnable) {
    if(swingEventQueue == null) {
      final Runnable runnable_ = runnable;
      swingEventQueue = new SwingEventQueue();
      runnable = new Runnable() {
        public void run() {
          try {
            runnable_.run();
          } catch(Throwable t) {
            t.printStackTrace();
            monitorShutdown();
          }
        }
      };
    }
    pushQueue();
    SwingUtilities.invokeLater(runnable);
  }

  public static int exclusiveSectionCount = 0;
  public static final Object UI_LOCK = new Object();

  public static void swtSync(Display display, Runnable runnable) {
    try {
      startExclusiveSection(display);
      runnable.run();
    } finally {
      stopExclusiveSection();
    }
  }

  public static void startExclusiveSection(Display display) {
    if(isRealDispatch() || !SwingUtilities.isEventDispatchThread()) {
      exclusiveSectionCount++;
      return;
    }
    while(exception != null || !mainThread.isAlive()) {
      try {
        Thread.sleep(100);
      } catch(Exception e) {}
    }
    synchronized(UI_LOCK) {
      exclusiveSectionCount++;
      if(exclusiveSectionCount == 1) {
        try {
          display.wake();
          UI_LOCK.wait();
        } catch(Exception e) {
        }
      }
    }
  }

  public static void stopExclusiveSection() {
    if(isRealDispatch() || !SwingUtilities.isEventDispatchThread()) {
      exclusiveSectionCount--;
      return;
    }
    synchronized(UI_LOCK) {
      exclusiveSectionCount--;
      if(exclusiveSectionCount == 0) {
        UI_LOCK.notify();
      }
    }
  }

  public static volatile Thread fakeDispatchingEDT;

  public static void wakeUIThread() {
    if(fakeDispatchingEDT != null) {
      fakeDispatchingEDT.interrupt();
      return;
    }
    synchronized(UIThreadUtils.UI_LOCK) {
      UI_LOCK.notify();
    }
  }
  
  protected static void monitorShutdown() {
    while(true) {
      try {
        Thread.sleep(200);
      } catch(Exception e) {}
      exitSystemIfNoThreads();
    }
  }
  
  protected static void exitSystemIfNoThreads() {
    ThreadGroup group;
    for(group = Thread.currentThread().getThreadGroup(); group.getParent() != null; group = group.getParent());
    Thread[] threads = new Thread[group.activeCount() + 10];
    group.enumerate(threads);
    boolean isExit = true;
    // There are 2 VM non daemon threads (AWT-Shutdown and DestroyJavaVM) + the main thread
    int skippedThreadCount = 3;
    for(int i=0; i<threads.length; i++) {
      Thread thread = threads[i];
      if(thread == null) {
        break;
      }
      if(isExit && thread.isAlive() && !thread.isDaemon()) {
        if(skippedThreadCount > 0) {
          skippedThreadCount--;
        } else {
          isExit = false;
        }
      }
    }
    if(isExit) {
      System.exit(0);
    }
  }
  
}
