# org.eclipse.swt.skia

Skia Plugin for the SWT User Interface Library
============================================

This plugin integrates the Skia graphics library into SWT, enabling modern and high-performance 2D graphics rendering.

## Building and Testing Locally

### Prerequisites
- **Eclipse IDE**
- **M2E – Maven Integration for Eclipse** ([m2e](https://eclipse.dev/m2e/))

### Steps
1. Import the following projects into your Eclipse workspace:
   - `org.eclipse.swt`
   - `org.eclipse.swt.skia`
   - The SWT fragment(s) for your platform (e.g., `org.eclipse.swt.gtk.linux.x86_64`)

2. Build the projects using Maven:
   - Right-click the `pom.xml` in `org.eclipse.swt` → `Run As` → `Maven install`
   - Right-click the `pom.xml` in `org.eclipse.swt.skia` → `Run As` → `Maven generate-sources`

   **Note:** The second build may fail. If this happens, close the Skia project, reopen it, and run Maven install again on the Skia project. The build should then succeed.

3. The Skija dependencies will be automatically downloaded into the `lib` directory of the Skia plugin.

4. If you receive error messages stating that the Skija jar files cannot be found, please check that the jar files are present in the `lib` directory and that the native libraries inside are not empty. You can verify this with a text editor.
   If the jar files are empty, you can delete them and execute `Maven generate-sources` again. This is a workaround because Tycho might not be able to download the dependencies yet.

### Notes
- The Skia plugin is currently in an early development stage, and there may be issues with the build process.
- The skija source jars of types and shared also will be downloaded to the `lib` directory. You can add them to your classpath, if you wish to check the source code of Skija.
---

For more information, please refer to the project documentation or contact the maintainers.