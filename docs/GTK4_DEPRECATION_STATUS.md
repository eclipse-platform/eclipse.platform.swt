# GTK4 Deprecation Status Report

**Generated:** 2025-10-31 11:26:36 UTC
**Build Command:** `./build_gtk.sh`

## Executive Summary

- **Total Deprecation Warnings:** 159
- **Unique Deprecated Functions:** 149
- **Affected Categories:** 23

### Warnings by Category

| Category | Functions | Warnings | Migration Target |
|----------|-----------|----------|------------------|
| Gtk Tree View | 55 | 55 | GtkColumnView and GtkColumnViewColumn, GtkListView |
| Gtk Tree Store | 9 | 13 | GtkTreeListModel |
| Gtk Combo Box | 11 | 11 | GtkDropDown, GtkDropDown and GtkStringList |
| Gtk List Store | 7 | 11 | GListStore |
| Gtk Tree Model | 10 | 11 | GListModel |
| Gtk Tree Path | 10 | 11 | GListModel |
| Gtk Style Context | 10 | 10 | TBD |
| Gtk Tree Selection | 10 | 10 | TBD |
| Gtk Cell Renderer | 8 | 8 | TBD |
| Gtk Cell Layout | 4 | 4 | TBD |
| Gtk Message Dialog | 2 | 2 | TBD |
| Gtk Widget Get | 2 | 2 | gtk_widget_compute_bounds |
| Gdk Display Put | 1 | 1 | TBD |
| Gdk Surface Create | 1 | 1 | TBD |
| Gtk Cell View | 1 | 1 | TBD |
| ... and 8 more | ... | ... | ... |

## Detailed Analysis by Category

### Gtk Tree View

**Status:** 55 functions deprecated, 55 total warnings

**GTK4 Migration Target(s):** GtkColumnView and GtkColumnViewColumn, GtkListView

<details>
<summary>Show 55 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_tree_view_collapse_row` | 1 | `os.c:8280` |
| `gtk_tree_view_column_add_attribute` | 1 | `os.c:8293` |
| `gtk_tree_view_column_cell_get_position` | 1 | `os.c:8310` |
| `gtk_tree_view_column_cell_get_size` | 1 | `gtk4.c:2418` |
| `gtk_tree_view_column_cell_set_cell_data` | 1 | `os.c:8324` |
| `gtk_tree_view_column_clear` | 1 | `os.c:8334` |
| `gtk_tree_view_column_get_button` | 1 | `os.c:8345` |
| `gtk_tree_view_column_get_fixed_width` | 1 | `os.c:8357` |
| `gtk_tree_view_column_get_reorderable` | 1 | `os.c:8369` |
| `gtk_tree_view_column_get_resizable` | 1 | `os.c:8381` |
| `gtk_tree_view_column_get_visible` | 1 | `os.c:8393` |
| `gtk_tree_view_column_get_width` | 1 | `os.c:8405` |
| `gtk_tree_view_column_new` | 1 | `os.c:8417` |
| `gtk_tree_view_column_pack_end` | 1 | `os.c:8428` |
| `gtk_tree_view_column_pack_start` | 1 | `os.c:8438` |
| `gtk_tree_view_column_set_alignment` | 1 | `os.c:8448` |
| `gtk_tree_view_column_set_cell_data_func` | 1 | `os.c:8458` |
| `gtk_tree_view_column_set_clickable` | 1 | `os.c:8468` |
| `gtk_tree_view_column_set_fixed_width` | 1 | `os.c:8478` |
| `gtk_tree_view_column_set_min_width` | 1 | `os.c:8488` |
| `gtk_tree_view_column_set_reorderable` | 1 | `os.c:8498` |
| `gtk_tree_view_column_set_resizable` | 1 | `os.c:8508` |
| `gtk_tree_view_column_set_sizing` | 1 | `os.c:8518` |
| `gtk_tree_view_column_set_sort_indicator` | 1 | `os.c:8528` |
| `gtk_tree_view_column_set_sort_order` | 1 | `os.c:8538` |
| `gtk_tree_view_column_set_visible` | 1 | `os.c:8548` |
| `gtk_tree_view_column_set_widget` | 1 | `os.c:8558` |
| `gtk_tree_view_convert_bin_window_to_tree_coords` | 1 | `os.c:8572` |
| `gtk_tree_view_convert_bin_window_to_widget_coords` | 1 | `os.c:8589` |
| `gtk_tree_view_create_row_drag_icon` | 1 | `os.c:8603` |
| `gtk_tree_view_expand_row` | 1 | `os.c:8615` |
| `gtk_tree_view_get_background_area` | 1 | `os.c:8628` |
| `gtk_tree_view_get_cell_area` | 1 | `os.c:8642` |
| `gtk_tree_view_get_column` | 1 | `os.c:8655` |
| `gtk_tree_view_get_columns` | 1 | `os.c:8667` |
| `gtk_tree_view_get_cursor` | 1 | `os.c:8682` |
| `gtk_tree_view_get_expander_column` | 1 | `os.c:8696` |
| `gtk_tree_view_get_grid_lines` | 1 | `os.c:8708` |
| `gtk_tree_view_get_headers_visible` | 1 | `os.c:8720` |
| `gtk_tree_view_get_path_at_pos` | 1 | `os.c:8740` |
| `gtk_tree_view_get_selection` | 1 | `os.c:8757` |
| `gtk_tree_view_get_visible_rect` | 1 | `os.c:8770` |
| `gtk_tree_view_insert_column` | 1 | `os.c:8783` |
| `gtk_tree_view_move_column_after` | 1 | `os.c:8794` |
| `gtk_tree_view_new_with_model` | 1 | `os.c:8805` |
| `gtk_tree_view_remove_column` | 1 | `os.c:8816` |
| `gtk_tree_view_row_expanded` | 1 | `os.c:8827` |
| `gtk_tree_view_scroll_to_cell` | 1 | `os.c:8838` |
| `gtk_tree_view_scroll_to_point` | 1 | `os.c:8848` |
| `gtk_tree_view_set_cursor` | 1 | `os.c:8858` |
| `gtk_tree_view_set_drag_dest_row` | 1 | `os.c:8868` |
| `gtk_tree_view_set_grid_lines` | 1 | `os.c:8878` |
| `gtk_tree_view_set_headers_visible` | 1 | `os.c:8888` |
| `gtk_tree_view_set_model` | 1 | `os.c:8898` |
| `gtk_tree_view_set_search_column` | 1 | `os.c:8908` |

</details>

### Gtk Tree Store

**Status:** 9 functions deprecated, 13 total warnings

**GTK4 Migration Target(s):** GtkTreeListModel

<details>
<summary>Show 9 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_tree_store_append` | 1 | `os.c:8136` |
| `gtk_tree_store_clear` | 1 | `os.c:8146` |
| `gtk_tree_store_insert` | 1 | `os.c:8156` |
| `gtk_tree_store_insert_after` | 1 | `os.c:8166` |
| `gtk_tree_store_newv` | 1 | `os.c:8179` |
| `gtk_tree_store_prepend` | 1 | `os.c:8192` |
| `gtk_tree_store_remove` | 1 | `os.c:8202` |
| `gtk_tree_store_set` | 5 | `os.c:8212` |
| `gtk_tree_store_set_value` | 1 | `os.c:8269` |

</details>

### Gtk Combo Box

**Status:** 11 functions deprecated, 11 total warnings

**GTK4 Migration Target(s):** GtkDropDown, GtkDropDown and GtkStringList

<details>
<summary>Show 11 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_combo_box_get_active` | 1 | `os.c:4165` |
| `gtk_combo_box_get_child` | 1 | `gtk4.c:1017` |
| `gtk_combo_box_get_model` | 1 | `os.c:4177` |
| `gtk_combo_box_popdown` | 1 | `os.c:4188` |
| `gtk_combo_box_popup` | 1 | `os.c:4198` |
| `gtk_combo_box_set_active` | 1 | `os.c:4208` |
| `gtk_combo_box_text_insert` | 1 | `os.c:4222` |
| `gtk_combo_box_text_new` | 1 | `os.c:4236` |
| `gtk_combo_box_text_new_with_entry` | 1 | `os.c:4248` |
| `gtk_combo_box_text_remove` | 1 | `os.c:4259` |
| `gtk_combo_box_text_remove_all` | 1 | `os.c:4269` |

</details>

### Gtk List Store

**Status:** 7 functions deprecated, 11 total warnings

**GTK4 Migration Target(s):** GListStore

<details>
<summary>Show 7 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_list_store_append` | 1 | `os.c:5430` |
| `gtk_list_store_clear` | 1 | `os.c:5440` |
| `gtk_list_store_insert` | 1 | `os.c:5450` |
| `gtk_list_store_newv` | 1 | `os.c:5463` |
| `gtk_list_store_remove` | 1 | `os.c:5476` |
| `gtk_list_store_set` | 5 | `os.c:5486` |
| `gtk_list_store_set_value` | 1 | `os.c:5543` |

</details>

### Gtk Tree Model

**Status:** 10 functions deprecated, 11 total warnings

**GTK4 Migration Target(s):** GListModel

<details>
<summary>Show 10 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_tree_model_get` | 2 | `os.c:7752` |
| `gtk_tree_model_get_iter` | 1 | `os.c:7779` |
| `gtk_tree_model_get_iter_first` | 1 | `os.c:7791` |
| `gtk_tree_model_get_n_columns` | 1 | `os.c:7803` |
| `gtk_tree_model_get_path` | 1 | `os.c:7815` |
| `gtk_tree_model_get_value` | 1 | `os.c:7838` |
| `gtk_tree_model_iter_children` | 1 | `os.c:7849` |
| `gtk_tree_model_iter_n_children` | 1 | `os.c:7861` |
| `gtk_tree_model_iter_next` | 1 | `os.c:7873` |
| `gtk_tree_model_iter_nth_child` | 1 | `os.c:7885` |

</details>

### Gtk Tree Path

**Status:** 10 functions deprecated, 11 total warnings

**GTK4 Migration Target(s):** GListModel

<details>
<summary>Show 10 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_tree_path_append_index` | 1 | `os.c:7896` |
| `gtk_tree_path_compare` | 1 | `os.c:7907` |
| `gtk_tree_path_free` | 1 | `os.c:7918` |
| `gtk_tree_path_get_depth` | 1 | `os.c:7929` |
| `gtk_tree_path_get_indices` | 1 | `os.c:7941` |
| `gtk_tree_path_new` | 1 | `os.c:7953` |
| `gtk_tree_path_new_from_string` | 2 | `os.c:7965` |
| `gtk_tree_path_next` | 1 | `os.c:7992` |
| `gtk_tree_path_prev` | 1 | `os.c:8003` |
| `gtk_tree_path_up` | 1 | `os.c:8015` |

</details>

### Gtk Style Context

**Status:** 10 functions deprecated, 10 total warnings

<details>
<summary>Show 10 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_style_context_add_class` | 1 | `os.c:7078` |
| `gtk_style_context_add_provider` | 1 | `os.c:7090` |
| `gtk_style_context_get_border` | 1 | `gtk4.c:2310` |
| `gtk_style_context_get_color` | 1 | `gtk4.c:2324` |
| `gtk_style_context_get_margin` | 1 | `gtk4.c:2338` |
| `gtk_style_context_get_padding` | 1 | `gtk4.c:2352` |
| `gtk_style_context_remove_class` | 1 | `os.c:7102` |
| `gtk_style_context_restore` | 1 | `os.c:7114` |
| `gtk_style_context_save` | 1 | `os.c:7124` |
| `gtk_style_context_set_state` | 1 | `os.c:7134` |

</details>

### Gtk Tree Selection

**Status:** 10 functions deprecated, 10 total warnings

<details>
<summary>Show 10 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_tree_selection_count_selected_rows` | 1 | `os.c:8027` |
| `gtk_tree_selection_get_selected_rows` | 1 | `os.c:8041` |
| `gtk_tree_selection_path_is_selected` | 1 | `os.c:8055` |
| `gtk_tree_selection_select_all` | 1 | `os.c:8066` |
| `gtk_tree_selection_select_iter` | 1 | `os.c:8076` |
| `gtk_tree_selection_set_mode` | 1 | `os.c:8086` |
| `gtk_tree_selection_set_select_function` | 1 | `os.c:8096` |
| `gtk_tree_selection_unselect_all` | 1 | `os.c:8106` |
| `gtk_tree_selection_unselect_iter` | 1 | `os.c:8116` |
| `gtk_tree_selection_unselect_path` | 1 | `os.c:8126` |

</details>

### Gtk Cell Renderer

**Status:** 8 functions deprecated, 8 total warnings

<details>
<summary>Show 8 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_cell_renderer_get_fixed_size` | 1 | `os.c:3932` |
| `gtk_cell_renderer_get_padding` | 1 | `os.c:3949` |
| `gtk_cell_renderer_get_preferred_height_for_width` | 1 | `os.c:3966` |
| `gtk_cell_renderer_get_preferred_size` | 1 | `os.c:3983` |
| `gtk_cell_renderer_pixbuf_new` | 1 | `os.c:3997` |
| `gtk_cell_renderer_set_fixed_size` | 1 | `os.c:4008` |
| `gtk_cell_renderer_text_new` | 1 | `os.c:4019` |
| `gtk_cell_renderer_toggle_new` | 1 | `os.c:4031` |

</details>

### Gtk Cell Layout

**Status:** 4 functions deprecated, 4 total warnings

<details>
<summary>Show 4 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_cell_layout_clear` | 1 | `os.c:3882` |
| `gtk_cell_layout_get_cells` | 1 | `os.c:3893` |
| `gtk_cell_layout_pack_start` | 1 | `os.c:3904` |
| `gtk_cell_layout_set_attributes` | 1 | `os.c:3916` |

</details>

### Gtk Message Dialog

**Status:** 2 functions deprecated, 2 total warnings

<details>
<summary>Show 2 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_message_dialog_format_secondary_text` | 1 | `os.c:5569` |
| `gtk_message_dialog_new` | 1 | `os.c:5587` |

</details>

### Gtk Widget Get

**Status:** 2 functions deprecated, 2 total warnings

**GTK4 Migration Target(s):** gtk_widget_compute_bounds

<details>
<summary>Show 2 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_widget_get_allocation` | 1 | `os.c:9014` |
| `gtk_widget_get_style_context` | 1 | `os.c:9309` |

</details>

### Gdk Display Put

**Status:** 1 functions deprecated, 1 total warnings

<details>
<summary>Show 1 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gdk_display_put_event` | 1 | `os.c:758` |

</details>

### Gdk Surface Create

**Status:** 1 functions deprecated, 1 total warnings

<details>
<summary>Show 1 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gdk_surface_create_similar_surface` | 1 | `os.c:2278` |

</details>

### Gtk Cell View

**Status:** 1 functions deprecated, 1 total warnings

<details>
<summary>Show 1 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_cell_view_set_fit_model` | 1 | `os.c:4042` |

</details>

### Gtk Dialog Add

**Status:** 1 functions deprecated, 1 total warnings

<details>
<summary>Show 1 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_dialog_add_button` | 1 | `os.c:4306` |

</details>

### Gtk Render Background

**Status:** 1 functions deprecated, 1 total warnings

<details>
<summary>Show 1 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_render_background` | 1 | `os.c:6697` |

</details>

### Gtk Render Focus

**Status:** 1 functions deprecated, 1 total warnings

<details>
<summary>Show 1 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_render_focus` | 1 | `os.c:6707` |

</details>

### Gtk Render Frame

**Status:** 1 functions deprecated, 1 total warnings

<details>
<summary>Show 1 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_render_frame` | 1 | `os.c:6717` |

</details>

### Gtk Render Handle

**Status:** 1 functions deprecated, 1 total warnings

<details>
<summary>Show 1 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_render_handle` | 1 | `os.c:6727` |

</details>

### Gtk Css Provider

**Status:** 1 functions deprecated, 1 total warnings

**GTK4 Migration Target(s):** gtk_css_provider_load_from_string

<details>
<summary>Show 1 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_css_provider_load_from_data` | 1 | `gtk4.c:1030` |

</details>

### Gtk Gesture Set

**Status:** 1 functions deprecated, 1 total warnings

<details>
<summary>Show 1 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_gesture_set_sequence_state` | 1 | `gtk4.c:1829` |

</details>

### Gtk Widget Translate

**Status:** 1 functions deprecated, 1 total warnings

**GTK4 Migration Target(s):** gtk_widget_compute_point

<details>
<summary>Show 1 deprecated functions</summary>

| Function | Usage Count | Source Location |
|----------|-------------|-----------------|
| `gtk_widget_translate_coordinates` | 1 | `gtk4.c:2719` |

</details>

## GTK4 Migration Guide References

The following deprecations are documented in the [GTK 3 to GTK 4 Migration Guide](https://docs.gtk.org/gtk4/migrating-3to4.html):

### GtkTreeView, GtkTreeModel and Cell Renderers

**Affected:** 55 functions, 55 warnings

**Migration:** GtkTreeView and related widgets are deprecated. Use GtkListView, GtkColumnView with GListModel.

**Complexity:** High - Major API redesign

**Reference:** [GtkTreeView, GtkTreeModel and Cell Renderers](https://docs.gtk.org/gtk4/migrating-3to4.html#stop-using-gtktreeview)

### GtkComboBox and GtkComboBoxText

**Affected:** 11 functions, 11 warnings

**Migration:** Replace with GtkDropDown and GtkStringList.

**Complexity:** Medium - Different API pattern

**Reference:** [GtkComboBox and GtkComboBoxText](https://docs.gtk.org/gtk4/migrating-3to4.html#stop-using-gtkcombobox)

### Cell Renderers

**Affected:** 13 functions, 13 warnings

**Migration:** Cell renderers are replaced by list item factories in GTK4.

**Complexity:** High - Complete redesign

**Reference:** [Cell Renderers](https://docs.gtk.org/gtk4/migrating-3to4.html#stop-using-gtktreeview)

### List and Tree Models

**Affected:** 7 functions, 11 warnings

**Migration:** Replace with GListModel interface and implementations like GListStore.

**Complexity:** Medium - API differences

**Reference:** [List and Tree Models](https://docs.gtk.org/gtk4/migrating-3to4.html#stop-using-gtktreeview)

### Tree Models

**Affected:** 9 functions, 13 warnings

**Migration:** Replace with GtkTreeListModel for hierarchical data.

**Complexity:** High - Different approach to hierarchy

**Reference:** [Tree Models](https://docs.gtk.org/gtk4/migrating-3to4.html#stop-using-gtktreeview)

### Rendering Functions

**Affected:** 4 functions, 4 warnings

**Migration:** GtkSnapshot replaces context-based rendering.

**Complexity:** High - New rendering model

**Reference:** [Rendering Functions](https://docs.gtk.org/gtk4/migrating-3to4.html#stop-using-gtkstylecontext)

### Style Context

**Affected:** 10 functions, 10 warnings

**Migration:** Many style context functions are deprecated. Use widget-specific APIs.

**Complexity:** Medium - Use widget methods instead

**Reference:** [Style Context](https://docs.gtk.org/gtk4/migrating-3to4.html#stop-using-gtkstylecontext)

## Recommended Migration Plan

This section outlines a strategic approach to addressing GTK4 deprecations, ordered from simplest to most complex.

### Phase 1: Simple Replacements (Low Effort)

These deprecations have direct 1:1 replacements and can be automated or done quickly.

#### 1.1 Direct Function Replacements
**Complexity:** Low | **Automation Potential:** High

Functions with simple name changes or parameter updates:

| Deprecated Function | Replacement | Occurrences |
|---------------------|-------------|-------------|
| `gtk_widget_get_allocation` | `gtk_widget_compute_bounds` | 1 |
| `gtk_css_provider_load_from_data` | `gtk_css_provider_load_from_string` | 1 |
| `gtk_widget_translate_coordinates` | `gtk_widget_compute_point` | 1 |

**Action Items:**
- [ ] Use search & replace with careful review
- [ ] Test each replacement in snippets
- [ ] Can be partially automated with AI-assisted refactoring

#### 1.2 Message Dialogs
**Complexity:** Low | **Automation Potential:** Medium

GtkMessageDialog deprecations can be replaced with GtkAlertDialog.

**Action Items:**
- [ ] Review 2 message dialog functions
- [ ] Create wrapper functions if needed
- [ ] Update all call sites

### Phase 2: Style and Rendering (Medium Effort)

**Complexity:** Medium | **Automation Potential:** Low

#### 2.1 Style Context Functions
**Affected:** 10 functions

Many style context methods are deprecated. Migration strategies:
- Use widget-specific methods when available
- For styling, use CSS through GtkCssProvider
- For measurements, use widget geometry methods

**Action Items:**
- [ ] Audit each style context usage
- [ ] Replace with widget-specific APIs where possible
- [ ] Consider CSS-based alternatives for styling

#### 2.2 Rendering Functions
**Affected:** ~12 functions

GTK4 uses GtkSnapshot instead of cairo context rendering.

**Action Items:**
- [ ] Study GtkSnapshot API
- [ ] Rewrite render functions using snapshot
- [ ] Test rendering in various themes

### Phase 3: ComboBox Migration (Medium-High Effort)

**Complexity:** Medium-High | **Automation Potential:** Low

**Affected:** 11 functions

GtkComboBox → GtkDropDown migration requires API redesign.

**Action Items:**
- [ ] Create abstraction layer for combo box functionality
- [ ] Implement using GtkDropDown + GtkStringList
- [ ] Update all combo box usage in SWT
- [ ] Thorough testing of selection, events, data binding

### Phase 4: TreeView/ListView Migration (High Effort)

**Complexity:** High | **Automation Potential:** Very Low

**Affected:** ~114 functions

This is the largest and most complex migration.

#### 4.1 Data Model Migration
- GtkListStore → GListStore
- GtkTreeStore → GtkTreeListModel

#### 4.2 View Migration
- GtkTreeView → GtkListView / GtkColumnView
- Cell renderers → List item factories
- GtkTreeSelection → Selection models (GtkSingleSelection, GtkMultiSelection)

**Action Items:**
- [ ] Design new list/tree abstraction for SWT
- [ ] Implement GListModel-based data models
- [ ] Create list item factory infrastructure
- [ ] Migrate table widgets
- [ ] Migrate tree widgets
- [ ] Implement selection handling
- [ ] Test all SWT list/tree/table functionality
- [ ] Performance testing with large datasets

## Automation Recommendations

### What Can Be Automated (with AI/Copilot)

1. **Simple Function Replacements** (High Success Rate)
   - Direct 1:1 function name changes
   - Parameter reordering with same semantics
   - Example: `gtk_css_provider_load_from_data` → `gtk_css_provider_load_from_string`

2. **Pattern-Based Replacements** (Medium Success Rate)
   - Converting simple style context usage to widget methods
   - Basic message dialog conversions
   - Requires: Good test coverage to validate

3. **Code Generation Assistance** (Medium Success Rate)
   - Generating boilerplate for new APIs (list item factories)
   - Creating wrapper functions
   - Documentation and migration guides

### What Should Be Manual (Human Required)

1. **Architectural Changes** (Manual Only)
   - TreeView → ListView/ColumnView migration
   - Render function → Snapshot conversion
   - Selection model redesign

2. **Complex Logic** (Manual with AI Assistance)
   - Custom cell renderer logic → Item factory conversion
   - ComboBox with complex models → DropDown migration
   - Event handling changes

3. **Testing and Validation** (Manual Required)
   - Visual regression testing
   - Performance benchmarking
   - Edge case validation

## Suggested AI-Assisted Workflow

1. **Phase 1**: Use Copilot for simple replacements
   - Generate a list of simple 1:1 replacements
   - Review and apply with tests

2. **Phase 2**: AI-assisted pattern migration
   - Provide examples of GTK4 patterns to AI
   - Have AI suggest conversions
   - Manual review and testing

3. **Phase 3-4**: Manual with AI research assistance
   - Use AI to research GTK4 best practices
   - Design architecture manually
   - Use AI for boilerplate generation
   - Thorough manual testing

## Priority Recommendations

**Immediate (Next Sprint):**
- [ ] Phase 1.1: Simple function replacements
- [ ] Set up GTK4 testing environment

**Short Term (1-2 Months):**
- [ ] Phase 1.2: Message dialogs
- [ ] Phase 2.1: Style context (partial)

**Medium Term (3-6 Months):**
- [ ] Phase 2.2: Rendering functions
- [ ] Phase 3: ComboBox migration

**Long Term (6-12 Months):**
- [ ] Phase 4: TreeView/ListView migration (major undertaking)

## Next Steps

1. **Review this document** with the team
2. **Prioritize** which phases to tackle first based on GTK4 timeline
3. **Set up GTK4 CI** to track deprecation warnings over time
4. **Create feature branch** for GTK4 migration work
5. **Start with Phase 1** simple replacements to gain momentum

## Resources

- [GTK 4 Migration Guide](https://docs.gtk.org/gtk4/migrating-3to4.html)
- [GTK 4 API Reference](https://docs.gtk.org/gtk4/)
- [GListModel Tutorial](https://docs.gtk.org/gio/iface.ListModel.html)
- [GTK 4 List Widgets Guide](https://docs.gtk.org/gtk4/section-list-widget.html)

---

*This document was generated automatically by analyzing build output from `./build_gtk.sh` on 2025-10-31.*