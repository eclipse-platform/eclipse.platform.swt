/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

// -- Code contributed --

//A font chooser that allows users to pick a font by name, size, style, and
//color.  The color selection is provided by a JColorChooser pane.  This
//dialog builds an AttributeSet suitable for use with JTextPane.
//
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class JFontChooser extends JDialog {

  private JColorChooser colorChooser;
  private JComboBox fontNameComboBox;
  private JCheckBox isFontBoldCheckBox;
  private JCheckBox isFontItalicCheckBox;
  private JCheckBox isFontStrikeThroughCheckBox;
  private JCheckBox isFontUnderlineCheckBox;
  private JTextField fontSizeTextField;
  private JLabel previewLabel;
  private SimpleAttributeSet attributes;
  private Font newFont;
  private Color newColor;

  
  public JFontChooser(Dialog parent) {
    super(parent, "Font Chooser", true);
    init();
  }
  
  public JFontChooser(Frame parent) {
    super(parent, "Font Chooser", true);
    init();
  }
  
  protected void init() {
    setSize(484, 494);
    setLocation(270, 137);
    attributes = new SimpleAttributeSet();
    // Make sure that any way the user cancels the window does the right thing
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        closeAndCancel();
      }
    });
    // Start the long process of setting up our interface
    Container c = getContentPane();
    Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    Vector names = new Vector();
    for (int i=0; i<fonts.length; i++) {
      Font font = fonts[i];
      if (!names.contains(font.getFamily())) {
        names.add(font.getFamily());
      }
    }
    JPanel northPanel = new JPanel(new BorderLayout());
    JPanel fontPanel = new JPanel();
    fontNameComboBox = new JComboBox(names);
    Font fontNameComboBoxFont = fontNameComboBox.getFont();
    Map fontNameComboBoxAttributes = fontNameComboBoxFont.getAttributes();
    fontNameComboBox.setSelectedItem(fontNameComboBoxFont.getFamily());
    fontNameComboBox.addActionListener(updateActionListener);
    fontSizeTextField = new JTextField("" + fontNameComboBoxFont.getSize(), 4);
    fontSizeTextField.setHorizontalAlignment(SwingConstants.RIGHT);
    fontSizeTextField.addActionListener(updateActionListener);
    fontSizeTextField.addFocusListener(new FocusAdapter() {
      public void focusLost(FocusEvent e) {
        adjustPreview();
      }
    });
    fontPanel.add(fontNameComboBox);
    fontPanel.add(new JLabel(" Size: "));
    fontPanel.add(fontSizeTextField);
    northPanel.add(fontPanel, BorderLayout.NORTH);
    JPanel optionsPanel = new JPanel();
    isFontBoldCheckBox = new JCheckBox("Bold");
    isFontBoldCheckBox.setSelected(fontNameComboBoxFont.isBold());
    isFontBoldCheckBox.addActionListener(updateActionListener);
    isFontItalicCheckBox = new JCheckBox("Italic");
    isFontItalicCheckBox.addActionListener(updateActionListener);
    isFontItalicCheckBox.setSelected(fontNameComboBoxFont.isItalic());
    isFontStrikeThroughCheckBox = new JCheckBox("Strike Through");
    if(!Compatibility.IS_JAVA_6_OR_GREATER) {
      isFontStrikeThroughCheckBox.setVisible(false);
    }
    isFontStrikeThroughCheckBox.setSelected(fontNameComboBoxAttributes.get(TextAttribute.STRIKETHROUGH) == TextAttribute.STRIKETHROUGH_ON);
    isFontStrikeThroughCheckBox.addActionListener(updateActionListener);
    isFontUnderlineCheckBox = new JCheckBox("Underline");
    if(!Compatibility.IS_JAVA_6_OR_GREATER) {
      isFontUnderlineCheckBox.setVisible(false);
    }
    isFontUnderlineCheckBox.setSelected(fontNameComboBoxAttributes.get(TextAttribute.UNDERLINE) == TextAttribute.UNDERLINE_ON);
    isFontUnderlineCheckBox.addActionListener(updateActionListener);
    optionsPanel.add(isFontBoldCheckBox);
    optionsPanel.add(isFontItalicCheckBox);
    optionsPanel.add(isFontStrikeThroughCheckBox);
    optionsPanel.add(isFontUnderlineCheckBox);
    northPanel.add(optionsPanel, BorderLayout.SOUTH);
    c.add(northPanel, BorderLayout.NORTH);
    // Set up the color chooser panel and attach a change listener so that color
    // updates get reflected in our preview label.
    colorChooser = new JColorChooser(fontNameComboBox.getForeground());
    colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        updatePreviewColor();
      }
    });
    c.add(colorChooser, BorderLayout.CENTER);
    JPanel previewPanel = new JPanel(new BorderLayout());
    previewLabel = new JLabel("The quick brown fox jumps over the lazy red dog.");
    previewLabel.setForeground(colorChooser.getColor());
    previewPanel.add(previewLabel, BorderLayout.CENTER);
    // Add in the Ok and Cancel buttons for our dialog box. Let's reuse internationalized texts...
    JButton okButton = new JButton(LookAndFeelUtils.getOKButtonText());
    okButton.setToolTipText("Select the current font and color and clsoe the dialog");
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        closeAndSave();
      }
    });
    getRootPane().setDefaultButton(okButton);
    JButton cancelButton = new JButton(LookAndFeelUtils.getCancelButtonText());
    cancelButton.setToolTipText("Cancel the changes and close the dialog");
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        closeAndCancel();
      }
    });
    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
    JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 5, 5));
    buttonPanel.add(okButton);
    buttonPanel.add(cancelButton);
    southPanel.add(buttonPanel);
    previewPanel.add(southPanel, BorderLayout.SOUTH);
    // Give the preview label room to grow.
    previewPanel.setMinimumSize(new Dimension(100, 100));
    previewPanel.setPreferredSize(new Dimension(100, 100));

    c.add(previewPanel, BorderLayout.SOUTH);
  }

  protected ActionListener updateActionListener = new ActionListener() {
    public void actionPerformed(ActionEvent ae) {
      adjustPreview();
    }
  };
  
  // Ok, something in the font changed, so figure that out and make a
  // new font for the preview label
  protected void adjustPreview() {
    // Check the name of the font
    if (!StyleConstants.getFontFamily(attributes).equals(fontNameComboBox.getSelectedItem())) {
      StyleConstants.setFontFamily(attributes, (String) fontNameComboBox.getSelectedItem());
    }
    try {
      int fontSize = Integer.parseInt(fontSizeTextField.getText());
      if (StyleConstants.getFontSize(attributes) != fontSize) {
        StyleConstants.setFontSize(attributes, fontSize);
      }
    } catch(Exception e) {
      // do nothing, the user must enter a valid number
    }
    boolean isBold = isFontBoldCheckBox.isSelected();
    if (StyleConstants.isBold(attributes) != isBold) {
      StyleConstants.setBold(attributes, isBold);
    }
    boolean isItalic = isFontItalicCheckBox.isSelected();
    if (StyleConstants.isItalic(attributes) != isItalic) {
      StyleConstants.setItalic(attributes, isItalic);
    }
    boolean isStrikeThrough = isFontStrikeThroughCheckBox.isSelected();
    if (StyleConstants.isStrikeThrough(attributes) != isStrikeThrough) {
      StyleConstants.setStrikeThrough(attributes, isStrikeThrough);
    }
    boolean isUnderline = isFontUnderlineCheckBox.isSelected();
    if (StyleConstants.isUnderline(attributes) != isUnderline) {
      StyleConstants.setUnderline(attributes, isUnderline);
    }
    updatePreviewFont();
  }
  
  // Get the appropriate font from our attributes object and update the preview label
  protected void updatePreviewFont() {
    String name = StyleConstants.getFontFamily(attributes);
    boolean isBold = StyleConstants.isBold(attributes);
    boolean isItalic = StyleConstants.isItalic(attributes);
    boolean isStrikeThrough = StyleConstants.isStrikeThrough(attributes);
    boolean isUnderline = StyleConstants.isUnderline(attributes);
    int size = StyleConstants.getFontSize(attributes);
    // Bold and italic don't work properly in beta 4.
    Font f = new Font(name, (isBold ? Font.BOLD : 0) + (isItalic ? Font.ITALIC : 0), size);
    Map newFontAttributes = f.getAttributes();
    if(isStrikeThrough) {
      newFontAttributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
    }
    if(isUnderline) {
      newFontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
    }
    previewLabel.setFont(new Font(newFontAttributes));
  }

  // Get the appropriate color from our chooser and update previewLabel
  protected void updatePreviewColor() {
    previewLabel.setForeground(colorChooser.getColor());
    previewLabel.repaint();
  }

  public Font getNewFont() {
    return newFont;
  }

  public Color getNewColor() {
    return newColor;
  }

  public AttributeSet getAttributes() {
    return attributes;
  }

  public void closeAndSave() {
    // Save font & color information and then close the window
    newFont = previewLabel.getFont();
    newColor = previewLabel.getForeground();
    setVisible(false);
  }

  public void closeAndCancel() {
    // Erase any font information and then close the window
    newFont = null;
    newColor = null;
    setVisible(false);
  }

  public void setDefaultFont(Font defaultFont) {
    if (defaultFont == null) {
      return;
    }
    newFont = defaultFont;
    fontNameComboBox.setSelectedItem(defaultFont.getFamily());
    fontSizeTextField.setText(String.valueOf(defaultFont.getSize()));
    isFontBoldCheckBox.setSelected(defaultFont.isBold());
    isFontItalicCheckBox.setSelected(defaultFont.isItalic());
    Map currentFontAttributes = defaultFont.getAttributes();
    isFontStrikeThroughCheckBox.setSelected(currentFontAttributes.get(TextAttribute.STRIKETHROUGH) == TextAttribute.STRIKETHROUGH_ON);
    isFontUnderlineCheckBox.setSelected(currentFontAttributes.get(TextAttribute.UNDERLINE) == TextAttribute.UNDERLINE_ON);
    adjustPreview();
  }

  public void setDefaultColor(Color currentColor) {
    if (currentColor == null) {
      return;
    }
    newColor = currentColor;
    colorChooser.setColor(currentColor);
    adjustPreview();
  }

}
