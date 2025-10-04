# SWT Headless Fragment

## Overview

The `org.eclipse.swt.headless` fragment provides a headless implementation of SWT (Standard Widget Toolkit) that allows applications to run without requiring a native windowing system or display. This is similar to Java AWT's headless mode (`java.awt.headless`).

## Purpose

The headless fragment is useful for:

- **Server-side applications** that need to reference UI code but don't display a UI
- **Testing** UI applications without requiring a display
- **Continuous Integration** environments where no display is available
- **Headless rendering** of UI layouts for documentation or screenshots
- **Command-line tools** that use SWT libraries but don't require actual UI display

## Key Features

### Detection

You can check if SWT is running in headless mode:

```java
if (Display.isHeadless()) {
    // Running in headless mode
}
```

### Behavior

In headless mode:

- **Widget creation**: Widgets can be created and configured normally
- **State management**: Widget properties (text, selection, bounds, etc.) are stored and can be retrieved
- **Layout**: Layout managers work normally and calculate sizes
- **Parent-child relationships**: Composite widgets maintain their child lists correctly
- **Events**: Event listeners can be registered (though events are not automatically triggered)
- **No rendering**: No actual native widgets are created, no drawing occurs
- **No user interaction**: Mouse and keyboard events don't occur naturally

## Supported Widgets

The headless implementation provides the following core widgets:

### Containers
- `Display` - The main display object
- `Shell` - Top-level window
- `Composite` - Container for other controls
- `Canvas` - Drawing surface (no actual drawing)
- `Decorations` - Base for decorated containers

### Controls
- `Button` - Push button, checkbox, radio, toggle, arrow
- `Label` - Text or image label
- `Control` - Base class for all controls

### Supporting Classes
- `Widget` - Base class for all widgets
- `Scrollable` - Base for scrollable controls
- `Menu` - Menu bar and popup menus
- `MenuItem` - Individual menu items
- `Item` - Base for items
- `Caret` - Text cursor
- `ScrollBar` - Scrollbar widget

## Limitations

The headless implementation has the following limitations:

### Not Implemented

The following features are **not** currently implemented:

1. **Native rendering**: No actual drawing to screen or images
2. **User input**: No keyboard or mouse events
3. **System integration**: 
   - No system tray
   - No clipboard
   - No drag and drop
   - No native file/color/font dialogs
4. **Advanced widgets**: Many complex widgets are not yet implemented:
   - Table, Tree, List
   - Text, StyledText
   - Browser
   - And many others
5. **Graphics operations**: GC (Graphics Context) drawing operations are no-ops
6. **Images**: Image loading and manipulation is limited
7. **Fonts and colors**: System fonts and colors return defaults

### Default Behavior

- `Display.getDefault()` creates a headless display
- `Display.isHeadless()` returns `true`
- Widget methods that can't be implemented return default values:
  - `computeSize()` returns reasonable defaults (64x64 or based on content)
  - `getBounds()` returns stored bounds
  - System queries return sensible defaults
- Operations that require native resources are no-ops:
  - `redraw()` does nothing
  - `update()` does nothing
  - Event loops don't wait for user input

## Usage Example

```java
// Create a headless display
Display display = new Display();
System.out.println("Headless: " + Display.isHeadless()); // true

// Create a shell
Shell shell = new Shell(display);
shell.setText("Test Shell");
shell.setSize(400, 300);

// Create controls
Composite composite = new Composite(shell, SWT.NONE);
composite.setLayout(new FillLayout());

Button button = new Button(composite, SWT.PUSH);
button.setText("Click Me");

Label label = new Label(composite, SWT.NONE);
label.setText("Hello Headless SWT!");

// Layout works normally
shell.layout();

// Can query widget properties
System.out.println("Button text: " + button.getText());
System.out.println("Label text: " + label.getText());
System.out.println("Shell size: " + shell.getSize());

// Cleanup
shell.dispose();
display.dispose();
```

## Installation

The headless fragment is automatically available when:

1. The `org.eclipse.swt` bundle is present
2. The `org.eclipse.swt.headless` fragment is in the classpath
3. No platform-specific fragment (gtk, cocoa, win32) is available

The OSGi framework will automatically select the headless fragment when no native platform is available.

## Building

The headless fragment is built as part of the normal SWT build process:

```bash
mvn clean install
```

The fragment is located in `binaries/org.eclipse.swt.headless/`.

## Contributing

When extending the headless implementation:

1. **Keep it simple**: Headless implementations should be minimal
2. **Store and return**: Store values set via setters, return them from getters
3. **No-op when necessary**: Operations that require native resources should be no-ops
4. **Parent-child tracking**: Maintain widget hierarchies properly
5. **Follow patterns**: Look at existing headless widgets for consistency

### Adding New Widgets

To add a new widget to the headless implementation:

1. Create the widget class in `bundles/org.eclipse.swt/Eclipse SWT/headless/org/eclipse/swt/widgets/`
2. Implement the public API from the platform-specific versions
3. Store state in private fields
4. Handle parent-child relationships for Composite widgets
5. Return sensible defaults for queries

## Testing

The headless implementation can be tested using JUnit tests that don't require a display:

```java
@Test
public void testHeadlessButton() {
    Display display = new Display();
    assertTrue(Display.isHeadless());
    
    Shell shell = new Shell(display);
    Button button = new Button(shell, SWT.PUSH);
    button.setText("Test");
    
    assertEquals("Test", button.getText());
    assertFalse(button.getSelection());
    
    button.setSelection(true);
    assertTrue(button.getSelection());
    
    shell.dispose();
    display.dispose();
}
```

## See Also

- [Java AWT Headless Mode](https://www.oracle.com/technical-resources/articles/javase/headless.html)
- [SWT API Documentation](https://help.eclipse.org/latest/index.jsp)
- [Eclipse SWT Project](https://www.eclipse.org/swt/)

## License

Eclipse Public License 2.0 (EPL-2.0)
