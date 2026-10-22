// Copyright 2026 Hitesh Soliwal and contributors. SPDX-License-Identifier: Apache-2.0
package com.synexia.rewrite;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.HexFormat;
import org.junit.jupiter.api.Test;
import org.openrewrite.*;
import org.openrewrite.internal.InMemoryLargeSourceSet;
import org.openrewrite.java.JavaParser;
final class SwtGtk4ListEvents20261022RecipeTest {
 private static final String CRATE="swt-gtk4-list-event-20261022";
 private static final String PATH="bundles/org.eclipse.swt/Eclipse SWT/gtk/org/eclipse/swt/widgets/List.java";
 private static final String BEFORE="59356482f0f312a69bafc237be0496cdc677d6adb22dec75f2c04f657b116632";
 private static final String AFTER="9678795cbf66d61d6ca3ba25ff69ee085f47a85badda6d12885cdef7db04b25d";
 private static String resource(String name) throws Exception {try(var in=SwtGtk4ListEvents20261022RecipeTest.class.getResourceAsStream("/com/synexia/rewrite/hash-pinned-java/"+CRATE+"/"+name)){assertNotNull(in,name);return new String(in.readAllBytes(),StandardCharsets.UTF_8);}}
 private static String sha(String s) throws Exception {return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8)));}
 private static InMemoryExecutionContext ctx(){return new InMemoryExecutionContext(t->{throw new IllegalStateException("OpenRewrite error",t);});}
 private static List<SourceFile> parsed(String src,String name){return JavaParser.fromJavaVersion().build().parseInputs(List.of(Parser.Input.fromString(Path.of(name),src)),null,ctx()).toList();}
 private static List<Result> changes(List<SourceFile> input){return new M3HashPinnedJavaSnapshotRecipe(CRATE).run(new InMemoryLargeSourceSet(input),ctx(),1).getChangeset().getAllResults();}
 @Test void emittedGtk4OwnerOnlyFromActualOpenRewriteResult() throws Exception {
  var before=resource("before.java.txt");var candidate=resource("List.java.txt");
  assertEquals(BEFORE,sha(before));assertEquals(AFTER,sha(candidate));
  assertEquals(List.of(PATH),new M3HashPinnedJavaSnapshotRecipe(CRATE).targetPaths());
  var result=changes(parsed(before,PATH));assertEquals(1,result.size());
  var output=result.get(0).getAfter();assertNotNull(output);
  assertEquals(PATH,output.getSourcePath().toString().replace('\\','/'));
  assertEquals(candidate,output.printAll());assertEquals(AFTER,sha(output.printAll()));
  var dst=Path.of(System.getProperty("m3.receiving.output")).resolve(PATH);
  Files.createDirectories(dst.getParent());Files.writeString(dst,output.printAll(),StandardCharsets.UTF_8,StandardOpenOption.CREATE_NEW);
  System.out.println("OPENREWRITE_SWT_GTK4_LIST_SHA256="+sha(output.printAll()));
 }
 @Test void fixedPoint() throws Exception {assertTrue(changes(parsed(resource("List.java.txt"),PATH)).isEmpty());}
 @Test void rejectsDrift() throws Exception {assertThrows(RuntimeException.class,()->changes(parsed(resource("before.java.txt")+"\n",PATH)));}
 @Test void rejectsMissing(){assertThrows(RuntimeException.class,()->changes(List.of()));}
 @Test void rejectsDuplicate() throws Exception {var two=new ArrayList<>(parsed(resource("before.java.txt"),PATH));two.addAll(parsed(resource("before.java.txt"),PATH));assertThrows(RuntimeException.class,()->changes(two));}
 @Test void rejectsWrongPath() throws Exception {assertThrows(RuntimeException.class,()->changes(parsed(resource("before.java.txt"),"bundles/org.eclipse.swt/Eclipse SWT/gtk/org/eclipse/swt/widgets/Table.java")));}
 @Test void validatesUpstreamEventAnchorsAndEpl() throws Exception {
  String src=resource("List.java.txt");
  assertTrue(src.contains("boolean rowActivated, defaultSelectionPending;"));
  assertTrue(src.contains("defaultSelectionPending = n_press == 2;"));
  assertTrue(src.contains("return super.gtk_gesture_press_event(gesture, n_press, x, y, event);"));
  assertTrue(src.contains("if (defaultSelectionPending) sendTreeDefaultSelection ();"));
  assertTrue(src.contains("case GDK.GDK_KP_Enter:"));
  assertTrue(src.contains("if (isDisposed ()) return true;"));
  assertTrue(src.contains("rowActivated = true;"));
  assertTrue(src.contains("SPDX-License-Identifier: EPL-2.0"));
  assertFalse(src.contains("if (n_press == 1) {"));
 }
}
