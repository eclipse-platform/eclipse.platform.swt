**Required Java 11**.

1. setup swt development environment

2. To build swt, open console in ../eclipse.platform.swt folder and type:
   ```bash
   mvn clean package -P build-individual-bundles -DskipTests
   ```

   **Note:** This command is only necessary the first time, then it will be enough only with:
   ```bash
   mvn install -P build-individual-bundles -DskipTests
   ```

3. To build native binaries, open console in ../eclipse.platform.swt.binaries and type:
   ```bash
   mvn process-resources -P build-individual-bundles -Dnative=gtk.linux.x86_64 -f bundles/org.eclipse.swt.browser.chromium.gtk.linux.x86_64/pom.xml
   ```

   **Note**: With this command you can build the natives of chromiun, Rust and JNI, 
verify that you have something similar to this output, verifying that the construction of rust and the native jni was successful.  
Rust must be installed, for example: `curl https://sh.rustup.rs -sSf | sh -s -- -y --default-toolchain 1.42.0`

4. To build binaries, In the previous console type:
   ```bash
   mvn package -P build-individual-bundles
   ```

   This command will build the project regardless of compiling the natives of chromium.

**Generic notes:**
- For future constructions, keep in mind to add the "-o" flag to do it offline and avoid unnecessary downloads.
- IF during the construction of the binaries you observed in the output a log containing "Different", it means that it is not taking the new changes for the construction and will build with the old.


5. Finally to run BrowserExample application from eclipse swt example , you need import dependencies in run configurations. Go to run configurations > Dependencies > click in Classpath Entries and add your recent buils jars:
   - ~/.p2/pool/plugins/com.make.chromium.cef.gtk.linux.x86_64_*VERSION*.jar (You need [install CEF](https://www.eclipse.org/swt/faq.php#howusechromium))
   -  *PROJECT-FOLDER*/eclipse.platform.swt.binaries/bundles/org.eclipse.swt.gtk.linux.x86_64/target/org.eclipse.swt.gtk.linux.x86_64-*VERSION*-SNAPSHOT.jar<br><br>

   Configure VM arguments. Go to run configurations > Arguments and paste:
   `-Dorg.eclipse.swt.browser.DefaultType=chromium`

Delete ~/.swt folder to ensure cached binaries are not used.
