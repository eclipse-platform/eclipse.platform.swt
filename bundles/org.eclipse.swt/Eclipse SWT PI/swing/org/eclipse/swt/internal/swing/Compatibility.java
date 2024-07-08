/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 * 
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package org.eclipse.swt.internal.swing;

public class Compatibility {

  protected static final String JAVA_VERSION = System.getProperty("java.version");
  public static final boolean IS_JAVA_6_OR_GREATER = JAVA_VERSION.compareTo("1.6") >= 0;
  public static final boolean IS_JAVA_5_OR_GREATER = IS_JAVA_6_OR_GREATER || JAVA_VERSION.compareTo("1.5") >= 0;

  /**
   * When a class does not exist on a certain version of Java, it would generate a
   * NoClassDefFoundError simply when the surrounding class gets loaded. The fix is to
   * surround the access by an intermediate class. Using this particular ProtectedCode
   * class is not mandatory but allows a better tracking of such compatibility issues.
   * Here is an example:<br>
   * <pre>
   * if(Compatibility.IS_JAVA_5_OR_GREATER) {
   *   new Compatibility.ProtectedCode() {{
   *     // Code accessing classes that only exist in Java 5.0+
   *   }};
   * }
   * </pre>
   * Note that in the example above, the code is invoked in the constructor of this
   * intermediate class.
   */
  public static abstract class ProtectedCode {
  }
  
}
