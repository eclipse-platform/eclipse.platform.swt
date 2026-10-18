package com.synexia.m3.viewport;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/** Exact-source candidate for non-materializing GTK virtual Tree bulk traversal. */
public final class GtkVirtualTreeSparseBulkCandidate {
    public static final String TARGET =
            "bundles/org.eclipse.swt/Eclipse SWT/gtk/org/eclipse/swt/widgets/Tree.java";
    public static final String PREIMAGE_SHA256 =
            "4759f0e133e0e58799b9ec2c6572af8233d9460ae22264e49c7facbbc9781b25";
    public static final String POSTIMAGE_SHA256 =
            "926b4e3bde0f1dea2e1032ca26baa81663cc49afbdadfbe58d85781f6b3d9b4d";

    static final String BEFORE =
            "TreeItem [] modelChildren (TreeItem parentItem, boolean materialize) {\n"
            + "\t/*\n"
            + "\t * GTK and Win32 currently retain their native structural projection.\n"
            + "\t * Keep the SWT-level bulk API platform-neutral now; their sparse array\n"
            + "\t * backends can replace this materializing fallback independently.\n"
            + "\t */\n"
            + "\treturn parentItem == null ? getItems () : parentItem.getItems ();\n"
            + "}";

    static final String AFTER =
            "TreeItem [] modelChildren (TreeItem parentItem, boolean materialize) {\n"
            + "\tif (virtualTopology == null || materialize) {\n"
            + "\t\treturn parentItem == null ? getItems () : parentItem.getItems ();\n"
            + "\t}\n"
            + "\tint parentId = parentItem == null ? VirtualTreeTopology.ROOT : virtualItemId (parentItem);\n"
            + "\tif (parentId < VirtualTreeTopology.ROOT\n"
            + "\t\t\t|| (parentId != VirtualTreeTopology.ROOT && !virtualTopology.contains (parentId))) {\n"
            + "\t\treturn new TreeItem [0];\n"
            + "\t}\n"
            + "\tint count = 0;\n"
            + "\tfor (int id = virtualTopology.firstMaterializedChildId (parentId);\n"
            + "\t\t\tid >= 0; id = virtualTopology.nextMaterializedSiblingId (id)) {\n"
            + "\t\tif (id < items.length && items [id] != null && !items [id].isDisposed ()) {\n"
            + "\t\t\tcount++;\n"
            + "\t\t}\n"
            + "\t}\n"
            + "\tTreeItem [] result = new TreeItem [count];\n"
            + "\tint index = 0;\n"
            + "\tfor (int id = virtualTopology.firstMaterializedChildId (parentId);\n"
            + "\t\t\tid >= 0; id = virtualTopology.nextMaterializedSiblingId (id)) {\n"
            + "\t\tif (id < items.length) {\n"
            + "\t\t\tTreeItem item = items [id];\n"
            + "\t\t\tif (item != null && !item.isDisposed ()) {\n"
            + "\t\t\t\tresult [index++] = item;\n"
            + "\t\t\t}\n"
            + "\t\t}\n"
            + "\t}\n"
            + "\treturn result;\n"
            + "}";

    private GtkVirtualTreeSparseBulkCandidate() {
    }

    public static String propose(Path sourcePath, String source) {
        return propose(sourcePath, source, true);
    }

    static String propose(Path sourcePath, String source, boolean enforceHash) {
        if (!normalized(sourcePath).endsWith(TARGET)) return source;
        if (source.length() > 512 * 1024) throw new IllegalStateException("M3_GTK_TREE_SOURCE_BUDGET");
        String hash = enforceHash ? sha256(source) : "";
        if (source.contains(AFTER)) {
            if (enforceHash && !POSTIMAGE_SHA256.equals(hash)) {
                throw new IllegalStateException("M3_GTK_TREE_POSTIMAGE_DRIFT:" + sourcePath);
            }
            return source;
        }
        if (enforceHash && !PREIMAGE_SHA256.equals(hash)) {
            throw new IllegalStateException("M3_GTK_TREE_PREIMAGE_DRIFT:" + sourcePath);
        }
        if (!source.contains(BEFORE)) {
            throw new IllegalStateException("M3_GTK_TREE_ATOM_DRIFT:" + sourcePath);
        }
        return source.replace(BEFORE, AFTER);
    }

    static String sha256(String source) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(source.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException impossible) {
            throw new IllegalStateException(impossible);
        }
    }

    private static String normalized(Path path) {
        return path.toString().replace('\\', '/');
    }
}
