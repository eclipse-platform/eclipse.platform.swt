# AGENTS.md - Eclipse SWT Guide for AI Tools

## Project Overview

This is the **Eclipse Platform SWT** (Standard Widget Toolkit) repository - a cross-platform GUI library for JVM-based desktop applications. SWT is the foundation for the Eclipse IDE and many other desktop applications.

## Architecture

SWT consists of two main parts:
1. **Java code** - Platform-independent widget implementations and APIs
2. **Native code (C)** - Platform-specific implementations using native windowing systems

### Platform Support
- **Linux**: GTK3 (stable) and GTK4 (experimental)
- **Windows**: Win32 API
- **macOS**: Cocoa

### Key Components
- `bundles/org.eclipse.swt` - Main SWT bundle with Java and native code
- `bundles/org.eclipse.swt.svg` - SVG support
- `bundles/org.eclipse.swt.tools` - Build and development tools
- `binaries/` - Platform-specific binary fragments
- `examples/` - Example code and snippets
- `tests/` - JUnit tests

## Build System

### Technology Stack
- **Build Tool**: Maven with Tycho plugin
- **Java Version**: Java 17 (compiler target), Java 21 (build/runtime in CI)
- **Supported Architectures**: x86_64, aarch64, loongarch64, ppc64le, riscv64

### Build Commands
```bash
# Build the entire project
mvn clean verify

# Build specific platform binary
mvn clean verify -Dnative=gtk.linux.x86_64

# Skip tests
mvn clean verify -DskipTests
```

### Building Natives

**GTK (Linux):**
```bash
cd bundles/org.eclipse.swt/Eclipse SWT PI/gtk/library
./build.sh -gtk-all install    # Build both GTK3 and GTK4
```

**CRITICAL**: Files like `os.c`, `os_stats.c`, `os_stats.h` are **auto-generated**. Never edit them directly!
Instead: modify Java source (e.g., `OS.java`), clean/rebuild the project, then run `./build.sh`.

See `docs/gtk-dev-guide.md` for detailed instructions.

## Coding Standards

### Java Code
- **Indentation**: Tabs (as per Eclipse project conventions)
- **Line Length**: Keep lines reasonably short, no strict limit
- **Naming**: Follow standard Java conventions (camelCase for methods/variables, PascalCase for classes)
- **Comments**: Use JavaDoc for public APIs; inline comments where needed for clarity
- **Assertions**: Use assertions for runtime checks (enabled in tests, disabled in production)

### Native Code (C)
- **Platform-specific**: Code goes in platform folders (gtk/, win32/, cocoa/)
- **JNI**: Communication between Java and native code uses JNI
- **OS.java**: Central file for native method declarations
- Do not commit binaries! They will be build and comitted by the CI.

### Code Organization
- Platform-independent code: `bundles/org.eclipse.swt/Eclipse SWT/common/`
- Platform-specific code: `bundles/org.eclipse.swt/Eclipse SWT/{gtk,win32,cocoa}/`
- JNI layer: `bundles/org.eclipse.swt/Eclipse SWT PI/{gtk,win32,cocoa}/`
- Emulated widgets: `bundles/org.eclipse.swt/Eclipse SWT/emulated/`

### OSGi Bundle Dependencies
SWT is an OSGi bundle. Check `bundles/org.eclipse.swt/META-INF/MANIFEST.MF` before using external classes.
If needed, add packages to `Import-Package` or `Require-Bundle`.

## Testing

### Running Tests
```bash
# Run all tests
mvn clean verify

# Run specific test class
mvn test -Dtest=ClassName
```

### Test Location
- Main tests: `tests/org.eclipse.swt.tests/`
- Tests automatically run with assertions enabled

### Writing Tests
- Follow existing test patterns in the repository
- Platform-specific tests should be in appropriate fragments
- Use JUnit for all tests

## Development Workflow

### Making Changes

1. **Java-only changes**: Can be tested without rebuilding natives
2. **Native changes**: Require rebuilding the platform-specific binary fragment
3. **Cross-platform changes**: Test on all affected platforms

### Pull Request Guidelines
- Sign the Eclipse Foundation Contributor License Agreement (CLA)
- Ensure all tests pass
- Follow the contribution guidelines in CONTRIBUTING.md
- Reference related GitHub issues

### CI/CD
- GitHub Actions runs builds on Linux, Windows, and macOS
- Matrix builds test with Java 21 on all platforms
- All tests must pass before merge

## Important Files and Patterns

### Key Files
- `OS.java` - Central native method declarations for platform integration
- `Display.java` - Main event loop and display management
- `Widget.java` - Base class for all widgets
- `Control.java` - Base class for all controls

### Common Patterns
- **Resource Management**: Always dispose of SWT resources (Display, Shell, Image, etc.)
- **Threading**: UI operations must run on the UI thread (use `Display.asyncExec()` or `Display.syncExec()`)
- **Event Handling**: Use listeners (typed or untyped) for event handling
- **Layout Management**: Use layout managers (GridLayout, FillLayout, etc.) instead of absolute positioning

## Platform-Specific Considerations

### GTK (Linux)
- Communication from Java to C happens through `OS.java`
- GTK3 is stable; GTK4 is experimental
- Can be toggled using `SWT_GTK4` environment variable
- See `docs/gtk-dev-guide.md` for comprehensive GTK development guide

### Windows
- Uses Win32 API
- WebView2 support available (see `Readme.WebView2.md`)
- Platform-specific code in `win32/` directories

### macOS
- Uses Cocoa framework
- Supports both x86_64 and aarch64 (Apple Silicon)
- Platform-specific code in `cocoa/` directories

## Resources

- **Main README**: [`README.md`](../README.md)
- **Contributing Guide**: [`CONTRIBUTING.md`](../CONTRIBUTING.md)
- **GTK Development Guide**: [`docs/gtk-dev-guide.md`](../docs/gtk-dev-guide.md)
- **GitHub Discussions**: Use for questions and general discussions
- **GitHub Issues**: Use for bug reports and feature requests

## Critical Development Rules

### Resource Disposal
**YOU MUST DISPOSE:** `Image`, `GC`, `Cursor`, `Region`, `Path`, `Pattern`
**Auto-disposed (no manual disposal needed):** `Color`, `Font` (as of recent SWT versions)
**DO NOT DISPOSE:** System colors/fonts, `ImageRegistry` images, Display/Shell (framework-managed)

```java
Image image = new Image(display, 100, 100);
try {
    // Use image
} finally {
    image.dispose();
}
```

### Threading
**All SWT code must run on the Display thread!**
```java
display.asyncExec(() -> button.setText("Updated"));
```

### Adding GTK Functions
1. Add native method declaration to `OS.java` with JavaDoc annotations (`@param cast=`, `@method flags=dynamic`)
2. Clean and rebuild `org.eclipse.swt` project (regenerates `os.c`)
3. Rebuild natives: `cd bundles/org.eclipse.swt/Eclipse SWT PI/gtk/library && ./build.sh -gtk3 install`

## Tips for AI Tools

- Always include proper resource disposal in generated code
- Ensure UI operations use `Display.syncExec/asyncExec`
- Platform-specific code goes in `{gtk,win32,cocoa}/` directories
- When adding native methods, declare in `OS.java` first
- Never edit auto-generated files (`os.c`, `os_stats.*`)
- Use snippets in `examples/org.eclipse.swt.snippets/` as reference
- Test cross-platform compatibility
- Check MANIFEST.MF for OSGi dependencies
