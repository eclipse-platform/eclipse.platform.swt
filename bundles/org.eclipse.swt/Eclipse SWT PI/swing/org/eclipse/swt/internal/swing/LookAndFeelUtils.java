/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.eclipse.swt.SWT;

public class LookAndFeelUtils {

  protected LookAndFeelUtils() {}
  
  public static LookAndFeel getLookAndFeel() {
    return UIManager.getLookAndFeel();
  }
  
  public static Font getSystemFont() {
    if(getLookAndFeel() instanceof MetalLookAndFeel) {
      return MetalLookAndFeel.getSystemTextFont();
    }
    return UIManager.getFont("Label.font");
  }
  
  public static Color getFocusColor() {
    try {
      if(getLookAndFeel() instanceof MetalLookAndFeel) {
        return MetalLookAndFeel.getFocusColor();
      }
    } catch(Exception e) {
//      try {
//        // Swing's Look and Feels have properties named ComponentName.focus, and we use JComponents. So let's remove the "J". 
//        String focusName = drawable.getClass().getName();
//        focusName = focusName.substring(focusName.lastIndexOf('.') + 2) + ".focus";
//        // TODO: test this theory.
//        newColor = UIManager.getColor(focusName);
//      } catch(Exception ex) {}
    }
    return null;
  }
  
  public static int getCaretBlinkRate() {
    return UIManager.getInt ("TextArea.caretBlinkRate");
  }
  
  public static Icon getSystemIcon(int iconID) {
    String key;
    switch(iconID) {
    case SWT.ICON_ERROR:
      key = "OptionPane.errorIcon";
      break;
    case SWT.ICON_QUESTION:
      key = "OptionPane.questionIcon";
      break;
    case SWT.ICON_WARNING:
      key = "OptionPane.warningIcon";
      break;
//    case SWT.ICON_WORKING:
//    case SWT.ICON_INFORMATION:
    default:
      key = "OptionPane.informationIcon";
      break;
    }
    Icon icon = UIManager.getIcon(key);
    if(icon == null) {
      icon = new MetalLookAndFeel().getDefaults().getIcon(key);
    }
    return icon;
  }
  
  public static Border getButtonBorder() {
    return UIManager.getBorder("Button.border");
  }

  public static Border getStandardBorder() {
    return UIManager.getBorder("TextField.border");
  }
  
  public static void applyLabelStyle(JComponent component) {
    component.setForeground(UIManager.getColor("Label.foreground"));
    component.setBackground(UIManager.getColor("Label.background"));
    component.setFont(UIManager.getFont("Label.font"));
    component.setBorder(UIManager.getBorder("Label.border"));
  }

  public static void applyTextFieldStyle(JComponent component) {
    component.setForeground(UIManager.getColor("TextField.foreground"));
    component.setBackground(UIManager.getColor("TextField.background"));
    component.setFont(UIManager.getFont("TextField.font"));
    component.setBorder(UIManager.getBorder("TextField.border"));
  }
  
  public static void applyPasswordFieldStyle(JComponent component) {
    component.setForeground(UIManager.getColor("PasswordField.foreground"));
    component.setBackground(UIManager.getColor("PasswordField.background"));
    component.setFont(UIManager.getFont("PasswordField.font"));
    component.setBorder(UIManager.getBorder("PasswordField.border"));
  }
  
  public static Border getDefaultWindowBorder() {
    return BorderFactory.createLineBorder(UIManager.getColor("controlDkShadow"));
  }
  
  public static String getOKButtonText() {
    return UIManager.getString("OptionPane.okButtonText");
  }
  
  public static String getCancelButtonText() {
    return UIManager.getString("OptionPane.cancelButtonText");
  }
  
  public static Color getSystemColor(int id) {
    switch(id) {
    case SWT.COLOR_WIDGET_DARK_SHADOW:
      return UIManager.getColor("controlDkShadow");
    case SWT.COLOR_WIDGET_NORMAL_SHADOW:
      return UIManager.getColor("controlShadow");
    case SWT.COLOR_WIDGET_LIGHT_SHADOW:
      return UIManager.getColor("controlLtHighlight");
    case SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW:
      return UIManager.getColor("controlHighlight");
    case SWT.COLOR_WIDGET_BACKGROUND:
      return UIManager.getColor("control");
    case SWT.COLOR_WIDGET_BORDER:
      return UIManager.getColor("windowBorder");
    case SWT.COLOR_WIDGET_FOREGROUND:
      return UIManager.getColor("controlText");
    case SWT.COLOR_LIST_FOREGROUND:
      return UIManager.getColor("textText");
    case SWT.COLOR_LIST_BACKGROUND:
      return UIManager.getColor("text");
    case SWT.COLOR_LIST_SELECTION:
      return UIManager.getColor("textHighlight");
    case SWT.COLOR_LIST_SELECTION_TEXT:
      return UIManager.getColor("textHighlightText");
    case SWT.COLOR_INFO_FOREGROUND:
      return UIManager.getColor("infoText");
    case SWT.COLOR_INFO_BACKGROUND:
      return UIManager.getColor("info");
    case SWT.COLOR_TITLE_FOREGROUND: {
      Color swingColor = UIManager.getColor("activeCaptionText");
      if(swingColor == null) {
        swingColor = UIManager.getColor("Menu.selectionForeground");
      }
      return swingColor;
    }
    case SWT.COLOR_TITLE_BACKGROUND: {
      Color swingColor = UIManager.getColor("InternalFrame.activeTitleBackground");
      if(swingColor == null) {
        swingColor = UIManager.getColor("activeCaption");
        if(swingColor == null) {
          swingColor = UIManager.getColor("Menu.selectionBackground");
        }
      }
      return swingColor;
    }
    case SWT.COLOR_TITLE_BACKGROUND_GRADIENT: {
      Color swingColor = UIManager.getColor("InternalFrame.activeTitleGradient");
      if(swingColor == null) {
        swingColor = getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
        swingColor = new java.awt.Color(Math.min(swingColor.getRed() + 20, 255), Math.min(swingColor.getGreen() + 20, 255), Math.min(swingColor.getBlue() + 20, 255), swingColor.getAlpha());
      }
      return swingColor;
    }
    case SWT.COLOR_TITLE_INACTIVE_FOREGROUND: {
      Color swingColor = UIManager.getColor("inactiveCaptionText");
      if(swingColor == null) {
        swingColor = UIManager.getColor("Menu.foreground");
      }
      return swingColor;
    }
    case SWT.COLOR_TITLE_INACTIVE_BACKGROUND: {
      Color swingColor = UIManager.getColor("InternalFrame.inactiveTitleBackground");
      if(swingColor == null) {
        swingColor = UIManager.getColor("inactiveCaption");
        if(swingColor == null) {
          swingColor = UIManager.getColor("Menu.background");
        }
      }
      return swingColor;
    }
    case SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT: {
      Color swingColor = UIManager.getColor("InternalFrame.inactiveTitleGradient");
      if(swingColor == null) {
        swingColor = getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
        swingColor = new java.awt.Color(Math.min(swingColor.getRed() + 20, 255), Math.min(swingColor.getGreen() + 20, 255), Math.min(swingColor.getBlue() + 20, 255), swingColor.getAlpha());
      }
      return swingColor;
    }
    }
    return null;
  }
  
}
