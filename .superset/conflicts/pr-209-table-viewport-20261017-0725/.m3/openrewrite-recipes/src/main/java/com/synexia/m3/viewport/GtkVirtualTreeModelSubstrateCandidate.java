package com.synexia.m3.viewport;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/** Exact-source candidate for the dormant GTK virtual Tree logical-model substrate. */
public final class GtkVirtualTreeModelSubstrateCandidate {
    static final String TOPOLOGY_PATH =
            "bundles/org.eclipse.swt/Eclipse SWT/common/org/eclipse/swt/widgets/VirtualTreeTopology.java";
    static final String NATIVE_PATH =
            "bundles/org.eclipse.swt/Eclipse SWT PI/gtk/library/os_custom.c";
    static final String TOPOLOGY_PREIMAGE =
            "d257dbfe16ff6b06f5f22a7d4bb0d3f9fe635272ab2e0cb4391930646cbd81b3";
    static final String NATIVE_PREIMAGE =
            "f49d321ef991514c53c2b0e5da6812c199540e2ea770ed411b8e4b4488c51e28";
    static final String TOPOLOGY_POSTIMAGE =
            "0ae6c0796b39cfe318efb7175b2a2b8981335e409d473d6e06256dd67e0b2eef";
    static final String NATIVE_POSTIMAGE =
            "17916c76157392c7f94c6f3cda3538ec9543419b4dfa4b7572cd2e91bd7f0ee1";

    static final String TOPOLOGY_ANCHOR =
            "\tint childCount (int parentId) {\n"
            + "\t\tif (parentId == ROOT) {\n"
            + "            if (rootChildCount == UNKNOWN_CHILD_COUNT) {\n"
            + "                throw new IllegalStateException(\"root child count unknown\");\n"
            + "            }\n"
            + "\t\t\treturn rootChildCount;\n"
            + "\t\t}\n"
            + "\t\trequirePresent (parentId);\n"
            + "\t\tint count = childCounts [parentId];\n"
            + "        if (count == UNKNOWN_CHILD_COUNT) {\n"
            + "            throw new IllegalStateException(\"child count unknown\");\n"
            + "        }\n"
            + "\t\treturn count;\n"
            + "\t}\n";

    static final String TOPOLOGY_INSERT =
            "\n\t/** Primitive, rebuildable snapshot for a native logical GtkTreeModel. */\n"
            + "\tint [] nativeModelSnapshot () {\n"
            + "\t\tint capacity = parentIds.length;\n"
            + "\t\tint [] snapshot = new int [Math.addExact (2, Math.multiplyExact (capacity, 3))];\n"
            + "\t\tsnapshot [0] = capacity;\n"
            + "\t\tsnapshot [1] = rootChildCount == UNKNOWN_CHILD_COUNT ? 0 : rootChildCount;\n"
            + "\t\tSystem.arraycopy (parentIds, 0, snapshot, 2, capacity);\n"
            + "\t\tSystem.arraycopy (childIndices, 0, snapshot, 2 + capacity, capacity);\n"
            + "\t\tSystem.arraycopy (childCounts, 0, snapshot, 2 + capacity * 2, capacity);\n"
            + "\t\treturn snapshot;\n"
            + "\t}\n";

    static final String NATIVE_MARKER = "typedef struct _SwtVirtualTreeModel {";

    static final String NATIVE_MODEL =
            "\n/*\n"
            + " * Hierarchical logical GtkTreeModel substrate for SWT.VIRTUAL Tree.\n"
            + " *\n"
            + " * The model owns only a rebuildable primitive snapshot of VirtualTreeTopology.\n"
            + " * Cold logical siblings are represented by (parent-id, child-index) in GtkTreeIter;\n"
            + " * they do not require GtkTreeStore nodes. Java remains the semantic owner.\n"
            + " */\n"
            + "#define SWT_VIRTUAL_TREE_ROOT (-1)\n"
            + "#define SWT_VIRTUAL_TREE_ABSENT G_MININT\n"
            + "#define SWT_VIRTUAL_TREE_UNKNOWN_CHILD_COUNT (-1)\n"
            + "\n"
            + "typedef struct _SwtVirtualTreeModel {\n"
            + "\tGObject parent_instance;\n"
            + "\tgint stamp;\n"
            + "\tgint capacity;\n"
            + "\tgint root_count;\n"
            + "\tgint *snapshot;\n"
            + "} SwtVirtualTreeModel;\n"
            + "\n"
            + "typedef struct _SwtVirtualTreeModelClass {\n"
            + "\tGObjectClass parent_class;\n"
            + "} SwtVirtualTreeModelClass;\n"
            + "\n"
            + "static void swt_virtual_tree_model_tree_model_init(GtkTreeModelIface *iface);\n"
            + "\n"
            + "G_DEFINE_TYPE_WITH_CODE(SwtVirtualTreeModel, swt_virtual_tree_model, G_TYPE_OBJECT,\n"
            + "\tG_IMPLEMENT_INTERFACE(GTK_TYPE_TREE_MODEL, swt_virtual_tree_model_tree_model_init))\n"
            + "\n"
            + "enum {\n"
            + "\tSWT_VIRTUAL_TREE_MODEL_PROP_0,\n"
            + "\tSWT_VIRTUAL_TREE_MODEL_PROP_TOPOLOGY,\n"
            + "\tSWT_VIRTUAL_TREE_MODEL_N_PROPERTIES\n"
            + "};\n"
            + "\n"
            + "static GParamSpec *swt_virtual_tree_model_properties[SWT_VIRTUAL_TREE_MODEL_N_PROPERTIES];\n"
            + "\n"
            + "static gint *swt_virtual_tree_model_parents(SwtVirtualTreeModel *self) {\n"
            + "\treturn self->snapshot == NULL ? NULL : self->snapshot + 2;\n"
            + "}\n"
            + "\n"
            + "static gint *swt_virtual_tree_model_indices(SwtVirtualTreeModel *self) {\n"
            + "\treturn self->snapshot == NULL ? NULL : self->snapshot + 2 + self->capacity;\n"
            + "}\n"
            + "\n"
            + "static gint *swt_virtual_tree_model_counts(SwtVirtualTreeModel *self) {\n"
            + "\treturn self->snapshot == NULL ? NULL : self->snapshot + 2 + self->capacity * 2;\n"
            + "}\n"
            + "\n"
            + "static gboolean swt_virtual_tree_model_present(SwtVirtualTreeModel *self, gint id) {\n"
            + "\tgint *parents = swt_virtual_tree_model_parents(self);\n"
            + "\treturn id >= 0 && id < self->capacity && parents != NULL\n"
            + "\t\t&& parents[id] != SWT_VIRTUAL_TREE_ABSENT;\n"
            + "}\n"
            + "\n"
            + "static gint swt_virtual_tree_model_child_count(SwtVirtualTreeModel *self, gint parent_id) {\n"
            + "\tif (parent_id == SWT_VIRTUAL_TREE_ROOT) return MAX(0, self->root_count);\n"
            + "\tif (!swt_virtual_tree_model_present(self, parent_id)) return 0;\n"
            + "\tgint count = swt_virtual_tree_model_counts(self)[parent_id];\n"
            + "\treturn count == SWT_VIRTUAL_TREE_UNKNOWN_CHILD_COUNT ? 0 : MAX(0, count);\n"
            + "}\n"
            + "\n"
            + "static gint swt_virtual_tree_model_lookup_id(\n"
            + "\tSwtVirtualTreeModel *self, gint parent_id, gint child_index) {\n"
            + "\tgint *parents = swt_virtual_tree_model_parents(self);\n"
            + "\tgint *indices = swt_virtual_tree_model_indices(self);\n"
            + "\tif (parents == NULL || indices == NULL) return -1;\n"
            + "\tfor (gint id = 0; id < self->capacity; id++) {\n"
            + "\t\tif (parents[id] == parent_id && indices[id] == child_index) return id;\n"
            + "\t}\n"
            + "\treturn -1;\n"
            + "}\n"
            + "\n"
            + "static void swt_virtual_tree_model_set_iter(\n"
            + "\tSwtVirtualTreeModel *self, GtkTreeIter *iter, gint parent_id, gint child_index) {\n"
            + "\tgint id = swt_virtual_tree_model_lookup_id(self, parent_id, child_index);\n"
            + "\titer->stamp = self->stamp;\n"
            + "\titer->user_data = GINT_TO_POINTER(parent_id + 2);\n"
            + "\titer->user_data2 = GINT_TO_POINTER(child_index + 1);\n"
            + "\titer->user_data3 = id >= 0 ? GINT_TO_POINTER(id + 1) : NULL;\n"
            + "}\n"
            + "\n"
            + "static gboolean swt_virtual_tree_model_iter_valid(\n"
            + "\tSwtVirtualTreeModel *self, GtkTreeIter *iter) {\n"
            + "\tif (iter == NULL || iter->stamp != self->stamp\n"
            + "\t\t\t|| iter->user_data == NULL || iter->user_data2 == NULL) return FALSE;\n"
            + "\tgint parent_id = GPOINTER_TO_INT(iter->user_data) - 2;\n"
            + "\tgint child_index = GPOINTER_TO_INT(iter->user_data2) - 1;\n"
            + "\treturn parent_id >= SWT_VIRTUAL_TREE_ROOT && child_index >= 0\n"
            + "\t\t&& child_index < swt_virtual_tree_model_child_count(self, parent_id);\n"
            + "}\n"
            + "\n"
            + "static gint swt_virtual_tree_model_iter_id(SwtVirtualTreeModel *self, GtkTreeIter *iter) {\n"
            + "\tif (!swt_virtual_tree_model_iter_valid(self, iter)) return -1;\n"
            + "\tgint parent_id = GPOINTER_TO_INT(iter->user_data) - 2;\n"
            + "\tgint child_index = GPOINTER_TO_INT(iter->user_data2) - 1;\n"
            + "\tif (iter->user_data3 != NULL) {\n"
            + "\t\tgint id = GPOINTER_TO_INT(iter->user_data3) - 1;\n"
            + "\t\tif (swt_virtual_tree_model_present(self, id)\n"
            + "\t\t\t\t&& swt_virtual_tree_model_parents(self)[id] == parent_id\n"
            + "\t\t\t\t&& swt_virtual_tree_model_indices(self)[id] == child_index) return id;\n"
            + "\t}\n"
            + "\treturn swt_virtual_tree_model_lookup_id(self, parent_id, child_index);\n"
            + "}\n"
            + "\n"
            + "static GtkTreeModelFlags swt_virtual_tree_model_get_flags(GtkTreeModel *tree_model) {\n"
            + "\treturn 0;\n"
            + "}\n"
            + "\n"
            + "static gint swt_virtual_tree_model_get_n_columns(GtkTreeModel *tree_model) {\n"
            + "\treturn 1;\n"
            + "}\n"
            + "\n"
            + "static GType swt_virtual_tree_model_get_column_type(GtkTreeModel *tree_model, gint index) {\n"
            + "\treturn index == 0 ? G_TYPE_INT : G_TYPE_INVALID;\n"
            + "}\n"
            + "\n"
            + "static gboolean swt_virtual_tree_model_get_iter(\n"
            + "\tGtkTreeModel *tree_model, GtkTreeIter *iter, GtkTreePath *path) {\n"
            + "\tSwtVirtualTreeModel *self = (SwtVirtualTreeModel *)tree_model;\n"
            + "\tgint depth = gtk_tree_path_get_depth(path);\n"
            + "\tgint *indices = gtk_tree_path_get_indices(path);\n"
            + "\tif (depth < 1 || indices == NULL) return FALSE;\n"
            + "\tgint parent_id = SWT_VIRTUAL_TREE_ROOT;\n"
            + "\tfor (gint level = 0; level < depth; level++) {\n"
            + "\t\tgint child_index = indices[level];\n"
            + "\t\tif (child_index < 0\n"
            + "\t\t\t\t|| child_index >= swt_virtual_tree_model_child_count(self, parent_id)) return FALSE;\n"
            + "\t\tif (level == depth - 1) {\n"
            + "\t\t\tswt_virtual_tree_model_set_iter(self, iter, parent_id, child_index);\n"
            + "\t\t\treturn TRUE;\n"
            + "\t\t}\n"
            + "\t\tgint id = swt_virtual_tree_model_lookup_id(self, parent_id, child_index);\n"
            + "\t\tif (id < 0) return FALSE;\n"
            + "\t\tparent_id = id;\n"
            + "\t}\n"
            + "\treturn FALSE;\n"
            + "}\n"
            + "\n"
            + "static GtkTreePath *swt_virtual_tree_model_get_path(\n"
            + "\tGtkTreeModel *tree_model, GtkTreeIter *iter) {\n"
            + "\tSwtVirtualTreeModel *self = (SwtVirtualTreeModel *)tree_model;\n"
            + "\tif (!swt_virtual_tree_model_iter_valid(self, iter)) return NULL;\n"
            + "\tgint parent_id = GPOINTER_TO_INT(iter->user_data) - 2;\n"
            + "\tgint child_index = GPOINTER_TO_INT(iter->user_data2) - 1;\n"
            + "\tGtkTreePath *path = gtk_tree_path_new();\n"
            + "\tgtk_tree_path_prepend_index(path, child_index);\n"
            + "\tfor (gint depth = 0; parent_id != SWT_VIRTUAL_TREE_ROOT; depth++) {\n"
            + "\t\tif (depth > self->capacity || !swt_virtual_tree_model_present(self, parent_id)) {\n"
            + "\t\t\tgtk_tree_path_free(path);\n"
            + "\t\t\treturn NULL;\n"
            + "\t\t}\n"
            + "\t\tgtk_tree_path_prepend_index(path, swt_virtual_tree_model_indices(self)[parent_id]);\n"
            + "\t\tparent_id = swt_virtual_tree_model_parents(self)[parent_id];\n"
            + "\t}\n"
            + "\treturn path;\n"
            + "}\n"
            + "\n"
            + "static void swt_virtual_tree_model_get_value(\n"
            + "\tGtkTreeModel *tree_model, GtkTreeIter *iter, gint column, GValue *value) {\n"
            + "\tSwtVirtualTreeModel *self = (SwtVirtualTreeModel *)tree_model;\n"
            + "\tg_value_init(value, G_TYPE_INT);\n"
            + "\tg_value_set_int(value, column == 0 ? swt_virtual_tree_model_iter_id(self, iter) : -1);\n"
            + "}\n"
            + "\n"
            + "static gboolean swt_virtual_tree_model_iter_next(GtkTreeModel *tree_model, GtkTreeIter *iter) {\n"
            + "\tSwtVirtualTreeModel *self = (SwtVirtualTreeModel *)tree_model;\n"
            + "\tif (!swt_virtual_tree_model_iter_valid(self, iter)) return FALSE;\n"
            + "\tgint parent_id = GPOINTER_TO_INT(iter->user_data) - 2;\n"
            + "\tgint next = GPOINTER_TO_INT(iter->user_data2);\n"
            + "\tif (next >= swt_virtual_tree_model_child_count(self, parent_id)) return FALSE;\n"
            + "\tswt_virtual_tree_model_set_iter(self, iter, parent_id, next);\n"
            + "\treturn TRUE;\n"
            + "}\n"
            + "\n"
            + "static gboolean swt_virtual_tree_model_iter_children(\n"
            + "\tGtkTreeModel *tree_model, GtkTreeIter *iter, GtkTreeIter *parent) {\n"
            + "\tSwtVirtualTreeModel *self = (SwtVirtualTreeModel *)tree_model;\n"
            + "\tif (parent == NULL) {\n"
            + "\t\tif (self->root_count <= 0) return FALSE;\n"
            + "\t\tswt_virtual_tree_model_set_iter(self, iter, SWT_VIRTUAL_TREE_ROOT, 0);\n"
            + "\t\treturn TRUE;\n"
            + "\t}\n"
            + "\tgint id = swt_virtual_tree_model_iter_id(self, parent);\n"
            + "\tif (id < 0 || swt_virtual_tree_model_child_count(self, id) <= 0) return FALSE;\n"
            + "\tswt_virtual_tree_model_set_iter(self, iter, id, 0);\n"
            + "\treturn TRUE;\n"
            + "}\n"
            + "\n"
            + "static gboolean swt_virtual_tree_model_iter_has_child(GtkTreeModel *tree_model, GtkTreeIter *iter) {\n"
            + "\tSwtVirtualTreeModel *self = (SwtVirtualTreeModel *)tree_model;\n"
            + "\tgint id = swt_virtual_tree_model_iter_id(self, iter);\n"
            + "\treturn id >= 0 && swt_virtual_tree_model_child_count(self, id) > 0;\n"
            + "}\n"
            + "\n"
            + "static gint swt_virtual_tree_model_iter_n_children(GtkTreeModel *tree_model, GtkTreeIter *iter) {\n"
            + "\tSwtVirtualTreeModel *self = (SwtVirtualTreeModel *)tree_model;\n"
            + "\tif (iter == NULL) return self->root_count;\n"
            + "\tgint id = swt_virtual_tree_model_iter_id(self, iter);\n"
            + "\treturn id < 0 ? 0 : swt_virtual_tree_model_child_count(self, id);\n"
            + "}\n"
            + "\n"
            + "static gboolean swt_virtual_tree_model_iter_nth_child(\n"
            + "\tGtkTreeModel *tree_model, GtkTreeIter *iter, GtkTreeIter *parent, gint n) {\n"
            + "\tSwtVirtualTreeModel *self = (SwtVirtualTreeModel *)tree_model;\n"
            + "\tgint parent_id = SWT_VIRTUAL_TREE_ROOT;\n"
            + "\tif (parent != NULL) {\n"
            + "\t\tparent_id = swt_virtual_tree_model_iter_id(self, parent);\n"
            + "\t\tif (parent_id < 0) return FALSE;\n"
            + "\t}\n"
            + "\tif (n < 0 || n >= swt_virtual_tree_model_child_count(self, parent_id)) return FALSE;\n"
            + "\tswt_virtual_tree_model_set_iter(self, iter, parent_id, n);\n"
            + "\treturn TRUE;\n"
            + "}\n"
            + "\n"
            + "static gboolean swt_virtual_tree_model_iter_parent(\n"
            + "\tGtkTreeModel *tree_model, GtkTreeIter *iter, GtkTreeIter *child) {\n"
            + "\tSwtVirtualTreeModel *self = (SwtVirtualTreeModel *)tree_model;\n"
            + "\tif (!swt_virtual_tree_model_iter_valid(self, child)) return FALSE;\n"
            + "\tgint parent_id = GPOINTER_TO_INT(child->user_data) - 2;\n"
            + "\tif (parent_id == SWT_VIRTUAL_TREE_ROOT\n"
            + "\t\t\t|| !swt_virtual_tree_model_present(self, parent_id)) return FALSE;\n"
            + "\tswt_virtual_tree_model_set_iter(self, iter,\n"
            + "\t\tswt_virtual_tree_model_parents(self)[parent_id],\n"
            + "\t\tswt_virtual_tree_model_indices(self)[parent_id]);\n"
            + "\treturn TRUE;\n"
            + "}\n"
            + "\n"
            + "static void swt_virtual_tree_model_tree_model_init(GtkTreeModelIface *iface) {\n"
            + "\tiface->get_flags = swt_virtual_tree_model_get_flags;\n"
            + "\tiface->get_n_columns = swt_virtual_tree_model_get_n_columns;\n"
            + "\tiface->get_column_type = swt_virtual_tree_model_get_column_type;\n"
            + "\tiface->get_iter = swt_virtual_tree_model_get_iter;\n"
            + "\tiface->get_path = swt_virtual_tree_model_get_path;\n"
            + "\tiface->get_value = swt_virtual_tree_model_get_value;\n"
            + "\tiface->iter_next = swt_virtual_tree_model_iter_next;\n"
            + "\tiface->iter_children = swt_virtual_tree_model_iter_children;\n"
            + "\tiface->iter_has_child = swt_virtual_tree_model_iter_has_child;\n"
            + "\tiface->iter_n_children = swt_virtual_tree_model_iter_n_children;\n"
            + "\tiface->iter_nth_child = swt_virtual_tree_model_iter_nth_child;\n"
            + "\tiface->iter_parent = swt_virtual_tree_model_iter_parent;\n"
            + "}\n"
            + "\n"
            + "static void swt_virtual_tree_model_set_property(\n"
            + "\tGObject *object, guint property_id, const GValue *value, GParamSpec *pspec) {\n"
            + "\tSwtVirtualTreeModel *self = (SwtVirtualTreeModel *)object;\n"
            + "\tswitch (property_id) {\n"
            + "\t\tcase SWT_VIRTUAL_TREE_MODEL_PROP_TOPOLOGY: {\n"
            + "\t\t\tconst gint *snapshot = (const gint *)g_value_get_pointer(value);\n"
            + "\t\t\tg_free(self->snapshot);\n"
            + "\t\t\tself->snapshot = NULL;\n"
            + "\t\t\tself->capacity = 0;\n"
            + "\t\t\tself->root_count = 0;\n"
            + "\t\t\tif (snapshot != NULL) {\n"
            + "\t\t\t\tgint capacity = snapshot[0];\n"
            + "\t\t\t\tgint root_count = snapshot[1];\n"
            + "\t\t\t\tif (capacity >= 0 && capacity <= (G_MAXINT - 2) / 3 && root_count >= 0) {\n"
            + "\t\t\t\t\tgsize length = (gsize)(2 + capacity * 3);\n"
            + "\t\t\t\t\tself->snapshot = g_new(gint, length);\n"
            + "\t\t\t\t\tmemcpy(self->snapshot, snapshot, length * sizeof(gint));\n"
            + "\t\t\t\t\tself->capacity = capacity;\n"
            + "\t\t\t\t\tself->root_count = root_count;\n"
            + "\t\t\t\t}\n"
            + "\t\t\t}\n"
            + "\t\t\tself->stamp++;\n"
            + "\t\t\tif (self->stamp == 0) self->stamp = 1;\n"
            + "\t\t\tbreak;\n"
            + "\t\t}\n"
            + "\t\tdefault:\n"
            + "\t\t\tG_OBJECT_WARN_INVALID_PROPERTY_ID(object, property_id, pspec);\n"
            + "\t\t\tbreak;\n"
            + "\t}\n"
            + "}\n"
            + "\n"
            + "static void swt_virtual_tree_model_get_property(\n"
            + "\tGObject *object, guint property_id, GValue *value, GParamSpec *pspec) {\n"
            + "\tSwtVirtualTreeModel *self = (SwtVirtualTreeModel *)object;\n"
            + "\tswitch (property_id) {\n"
            + "\t\tcase SWT_VIRTUAL_TREE_MODEL_PROP_TOPOLOGY:\n"
            + "\t\t\tg_value_set_pointer(value, self->snapshot);\n"
            + "\t\t\tbreak;\n"
            + "\t\tdefault:\n"
            + "\t\t\tG_OBJECT_WARN_INVALID_PROPERTY_ID(object, property_id, pspec);\n"
            + "\t\t\tbreak;\n"
            + "\t}\n"
            + "}\n"
            + "\n"
            + "static void swt_virtual_tree_model_finalize(GObject *object) {\n"
            + "\tSwtVirtualTreeModel *self = (SwtVirtualTreeModel *)object;\n"
            + "\tg_free(self->snapshot);\n"
            + "\tself->snapshot = NULL;\n"
            + "\tG_OBJECT_CLASS(swt_virtual_tree_model_parent_class)->finalize(object);\n"
            + "}\n"
            + "\n"
            + "static void swt_virtual_tree_model_class_init(SwtVirtualTreeModelClass *klass) {\n"
            + "\tGObjectClass *object_class = G_OBJECT_CLASS(klass);\n"
            + "\tobject_class->set_property = swt_virtual_tree_model_set_property;\n"
            + "\tobject_class->get_property = swt_virtual_tree_model_get_property;\n"
            + "\tobject_class->finalize = swt_virtual_tree_model_finalize;\n"
            + "\tswt_virtual_tree_model_properties[SWT_VIRTUAL_TREE_MODEL_PROP_TOPOLOGY] =\n"
            + "\t\tg_param_spec_pointer(\"swt-topology\", \"SWT topology\",\n"
            + "\t\t\t\"Primitive SWT virtual Tree topology snapshot\",\n"
            + "\t\t\tG_PARAM_READWRITE | G_PARAM_STATIC_STRINGS);\n"
            + "\tg_object_class_install_properties(object_class, SWT_VIRTUAL_TREE_MODEL_N_PROPERTIES,\n"
            + "\t\tswt_virtual_tree_model_properties);\n"
            + "}\n"
            + "\n"
            + "static void swt_virtual_tree_model_init(SwtVirtualTreeModel *self) {\n"
            + "\tself->stamp = (gint)g_random_int();\n"
            + "\tif (self->stamp == 0) self->stamp = 1;\n"
            + "\tself->capacity = 0;\n"
            + "\tself->root_count = 0;\n"
            + "\tself->snapshot = NULL;\n"
            + "}\n";

    private GtkVirtualTreeModelSubstrateCandidate() {
    }

    static String propose(Path path, String source, boolean enforceHash) {
        String normalized = path.toString().replace('\\', '/');
        if (normalized.endsWith(TOPOLOGY_PATH)) {
            if (source.contains("int [] nativeModelSnapshot ()")) {
                requirePostimage(source, TOPOLOGY_POSTIMAGE, enforceHash, "TOPOLOGY");
                return source;
            }
            requireHash(source, TOPOLOGY_PREIMAGE, enforceHash, "TOPOLOGY");
            if (!source.contains(TOPOLOGY_ANCHOR)) throw new IllegalStateException("M3_TREE_TOPOLOGY_ATOM_DRIFT");
            return source.replace(TOPOLOGY_ANCHOR, TOPOLOGY_ANCHOR + TOPOLOGY_INSERT);
        }
        if (normalized.endsWith(NATIVE_PATH)) {
            if (source.contains(NATIVE_MARKER)) {
                requirePostimage(source, NATIVE_POSTIMAGE, enforceHash, "NATIVE");
                return source;
            }
            requireHash(source, NATIVE_PREIMAGE, enforceHash, "NATIVE");
            String marker = "\n#endif\n\nstatic void *content_providers_copy";
            if (!source.contains(marker)) throw new IllegalStateException("M3_TREE_NATIVE_MODEL_ANCHOR_DRIFT");
            String result = source.replace(marker, NATIVE_MODEL + "\n#endif\n\nstatic void *content_providers_copy");
            String registration =
                    "if (lpname && strcmp(lpname, \"SwtVirtualTableModel\") == 0) {\n"
                    + "\t\trc = swt_virtual_table_model_get_type();\n"
                    + "\t} else";
            String replacement =
                    "if (lpname && strcmp(lpname, \"SwtVirtualTableModel\") == 0) {\n"
                    + "\t\trc = swt_virtual_table_model_get_type();\n"
                    + "\t} else if (lpname && strcmp(lpname, \"SwtVirtualTreeModel\") == 0) {\n"
                    + "\t\trc = swt_virtual_tree_model_get_type();\n"
                    + "\t} else";
            if (!result.contains(registration)) throw new IllegalStateException("M3_TREE_NATIVE_REGISTRATION_DRIFT");
            return result.replace(registration, replacement);
        }
        return source;
    }

    static String propose(Path path, String source) {
        return propose(path, source, true);
    }

    static String sha256(String source) {
        try {
            return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256")
                    .digest(source.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException impossible) {
            throw new IllegalStateException(impossible);
        }
    }

    private static void requireHash(String source, String expected, boolean enforce, String owner) {
        if (source.length() > 2 * 1024 * 1024) throw new IllegalStateException("M3_TREE_" + owner + "_BUDGET");
        if (enforce && !expected.equals(sha256(source))) {
            throw new IllegalStateException("M3_TREE_" + owner + "_PREIMAGE_DRIFT");
        }
    }

    private static void requirePostimage(
            String source, String expected, boolean enforce, String owner) {
        if (source.length() > 2 * 1024 * 1024) throw new IllegalStateException("M3_TREE_" + owner + "_BUDGET");
        if (enforce && !expected.equals(sha256(source))) {
            throw new IllegalStateException("M3_TREE_" + owner + "_POSTIMAGE_DRIFT");
        }
    }
}
