#include <gtk/gtk.h>

struct SwtWidget
{
	GtkWidget*      widget;
	GtkGestureDrag* dragGesture;
};

SwtWidget g_swtWidgets[2];
GtkTargetEntry g_TextTransfer_Target;
GtkTargetList* g_TextTransfer_Targets;
GdkAtom        g_TextTransfer_Atom     = gdk_atom_intern("UTF8_STRING", FALSE);

// ----------------------------------------------------------------------------
// Debug reporters
// ----------------------------------------------------------------------------

static void report_drag_begin(GtkWidget* widget, GdkDragContext* context, gpointer data)
{
	printf("drag-begin             (SWT -)\n");
}

static void report_drag_data_get(
	GtkWidget* self,
	GdkDragContext* context,
	GtkSelectionData* data,
	guint info,
	guint time,
	gpointer user_data
) {
	printf("drag-data-get          (SWT DragSourceListener.dragSetData)\n");
}

static void report_drag_data_received(
	GtkWidget* self,
	GdkDragContext* context,
	gint x,
	gint y,
	GtkSelectionData* data,
	guint info,
	guint time,
	gpointer user_data
) {
	printf("drag-data-received     (SWT DropTargetListener.drop)\n");
}

static gboolean report_drag_drop(
	GtkWidget* self,
	GdkDragContext* context,
	gint x,
	gint y,
	guint time,
	gpointer user_data
) {
	printf("drag-drop              (SWT DropTargetListener.dropAccept)\n");
	return FALSE;
}

static void report_drag_end(GtkWidget* widget, GdkDragContext* context, gpointer data)
{
	printf("drag-end               (SWT DragSourceListener.dragFinished)\n");
}

static gboolean report_drag_leave(GtkWidget* self, GdkDragContext* context, guint time, gpointer user_data)
{
	printf("drag-leave             (SWT DropTargetListener.dragLeave)\n");
	return FALSE;
}

static gboolean report_drag_motion(GtkWidget* self, GdkDragContext* context, gint x, gint y, guint time, gpointer user_data)
{
	printf("drag-motion            (SWT DropTargetListener.dragEnter / DropTargetListener.dragOver)\n");
	return FALSE;
}

static gboolean report_button_press_event(GtkWidget* self, GdkEventButton* event, SwtWidget* swtWidget)
{
	printf("button-press-event     (SWT MouseDown)\n");
	return FALSE;
}

static gboolean report_button_release_event(GtkWidget* self, GdkEventButton* event, SwtWidget* swtWidget)
{
	printf("button-release-event   (SWT MouseUp)\n");
	return FALSE;
}

void connectDebugReporters(GtkWidget* a_item)
{
	g_signal_connect(a_item, "drag-begin",		     G_CALLBACK(report_drag_begin),		      NULL);
	g_signal_connect(a_item, "drag-data-get",	     G_CALLBACK(report_drag_data_get),	      NULL);
	g_signal_connect(a_item, "drag-data-received",   G_CALLBACK(report_drag_data_received),   NULL);
	g_signal_connect(a_item, "drag-drop",            G_CALLBACK(report_drag_drop),            NULL);
	g_signal_connect(a_item, "drag-end",			 G_CALLBACK(report_drag_end),			  NULL);
	g_signal_connect(a_item, "drag-leave",   	     G_CALLBACK(report_drag_leave),		      NULL);
	g_signal_connect(a_item, "drag-motion",   	     G_CALLBACK(report_drag_motion),		  NULL);
	g_signal_connect(a_item, "button-press-event",   G_CALLBACK(report_button_press_event),   NULL);
	g_signal_connect(a_item, "button-release-event", G_CALLBACK(report_button_release_event), NULL);
}

// ----------------------------------------------------------------------------
// SWT-like handlers
// ----------------------------------------------------------------------------

static void swt_drag_data_received(
	GtkWidget* self,
	GdkDragContext* context,
	gint x,
	gint y,
	GtkSelectionData* data,
	guint info,
	guint time,
	gpointer user_data
) {
	printf("  Got from drag&drop: %s\n", gtk_selection_data_get_text(data));

	// See SWT DropTarget.drag_data_received()
	gtk_drag_finish(context, TRUE, FALSE, time);
}

// See SWT Control.dragDetect()
static gboolean swt_Control_dragDetect(GtkWidget* handle, GtkGestureDrag* dragGesture)
{
	double offsetX;
	double offsetY;
	double startX;
	double startY;
	if (!gtk_gesture_drag_get_start_point(dragGesture, &startX, &startY)) {
		return FALSE;
	}

	gtk_gesture_drag_get_offset(dragGesture, &offsetX, &offsetY);
	if (gtk_drag_check_threshold(handle, (int)startX, (int)startY, (int)startX + (int)offsetX, (int)startY + (int)offsetY)) {
		return TRUE;
	}

	return FALSE;
}

static gboolean swt_motion_notify_event(GtkWidget* a_self, GdkEventMotion* a_event, SwtWidget* a_swtWidget)
{
	// printf("motion-notify-event\n");

	GtkWidget*      handle      = a_swtWidget->widget;
	GtkGestureDrag* dragGesture = a_swtWidget->dragGesture;

	// See SWT Control.gtk_motion_notify_event()
	if (!swt_Control_dragDetect(handle, dragGesture))
		return FALSE;

	const GdkEvent* gdkEvent = (const GdkEvent*)a_event;
	gtk_event_controller_handle_event(GTK_EVENT_CONTROLLER(dragGesture), gdkEvent);
	if (GDK_3BUTTON_PRESS == gdk_event_get_event_type(gdkEvent))
		return FALSE;

	printf("<emulated>             (SWT DragSourceListener.dragStart)\n");

	// See SWT DragSource.drag()
	gtk_drag_begin_with_coordinates(handle, g_TextTransfer_Targets, GDK_ACTION_COPY, 1, 0, -1, -1);

	return FALSE;
}

static gboolean swt_drag_motion(GtkWidget* self, GdkDragContext* context, gint x, gint y, guint time, gpointer user_data)
{
	// See SWT DropTarget.drag_motion()
	gdk_drag_status(context, GDK_ACTION_COPY, time);

	// Accept drag
	return TRUE;
}

static void swt_drag_data_get(
	GtkWidget* self,
	GdkDragContext* context,
	GtkSelectionData* data,
	guint info,
	guint time,
	gpointer user_data
) {
	// See SWT DragSource.dragGetData()
	// See SWT TextTransfer.javaToNative()
	const char* dragData = g_strdup("Test drag string");
	gtk_selection_data_set(data, g_TextTransfer_Atom, 8, (const guchar*)dragData, strlen(dragData));
}

static gboolean swt_drag_drop(
	GtkWidget* self,
	GdkDragContext* context,
	gint x,
	gint y,
	guint time,
	gpointer user_data
) {
	// See SWT DropTarget.drag_drop()
	// ask drag source for dropped data
	gtk_drag_get_data(self, context, g_TextTransfer_Atom, time);
	return TRUE;
}

// ----------------------------------------------------------------------------
// Main
// ----------------------------------------------------------------------------

int main (int argc, char **argv)
{
	gtk_init(&argc, &argv);

	GtkWidget *window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	g_signal_connect(window, "delete_event", gtk_main_quit, NULL);
	gtk_window_set_default_size(GTK_WINDOW(window), 300, 200);

	GtkWidget* box = gtk_box_new (GTK_ORIENTATION_VERTICAL, 0);
	gtk_container_add(GTK_CONTAINER(window), box);

	{
		// See SWT DragSource.setTransfer()
		// See SWT TextTransfer
		const char* targetType = "UTF8_STRING";
		g_TextTransfer_Target = { (char*)targetType, 0, 0 };
		g_TextTransfer_Targets = gtk_target_list_new(&g_TextTransfer_Target, 1);
	}

	GtkWidget* hint = gtk_label_new("Controls below show how GTK drag&drop works:");
	gtk_container_add(GTK_CONTAINER(box), hint);
	{
		GtkWidget* box2 = gtk_box_new (GTK_ORIENTATION_HORIZONTAL, 0);
   		gtk_box_set_spacing(GTK_BOX(box2), 10);
	 	gtk_container_add(GTK_CONTAINER(box), box2);

		for (int iItem = 0; iItem < 2; iItem++)
		{
			GtkWidget* item = gtk_entry_new();
			gtk_widget_set_size_request(item, 170, 35);
			gtk_entry_set_text(GTK_ENTRY(item), "Test control");
			gtk_container_add(GTK_CONTAINER(box2), item);

			connectDebugReporters(item);
		}
	}

	hint = gtk_label_new("\nControls below show how SWT drag&drop works:");
	gtk_container_add(GTK_CONTAINER(box), hint);
	{
		GtkWidget* box2 = gtk_box_new (GTK_ORIENTATION_HORIZONTAL, 0);
		gtk_box_set_spacing(GTK_BOX(box2), 10);
    	gtk_container_add(GTK_CONTAINER(box), box2);

		for (int iItem = 0; iItem < 2; iItem++)
		{
			SwtWidget* swtWidget = &g_swtWidgets[iItem];

			GtkWidget* frame = gtk_frame_new(NULL);
			gtk_container_add(GTK_CONTAINER(box2), frame);
			gtk_frame_set_shadow_type(GTK_FRAME(frame), GTK_SHADOW_ETCHED_IN);

			GtkWidget* item = gtk_event_box_new();
			swtWidget->widget = item;
			gtk_container_add(GTK_CONTAINER(frame), item);

			{
				GtkWidget* label = gtk_label_new("Test control");
				gtk_widget_set_size_request(item, 170, 35);
				gtk_container_add(GTK_CONTAINER(item), label);
			}

			// See SWT Control.setDragGesture()
			GtkGestureDrag* dragGesture = (GtkGestureDrag*)gtk_gesture_drag_new(item);
			swtWidget->dragGesture = dragGesture;
			gtk_event_controller_set_propagation_phase(GTK_EVENT_CONTROLLER(dragGesture), GTK_PHASE_BUBBLE);
			gtk_gesture_single_set_button(GTK_GESTURE_SINGLE(dragGesture), 0);

			connectDebugReporters(item);

			// See SWT DropTarget.setTransfer()
			gtk_drag_dest_set(item, GTK_DEST_DEFAULT_MOTION, &g_TextTransfer_Target, 1, GDK_ACTION_COPY);

			// See SWT DropTarget.DropTarget(), DropTarget.drag_motion()
			g_signal_connect(item, "drag-motion",   	   G_CALLBACK(swt_drag_motion),		    swtWidget);

			// See SWT Control.hookMouseSignals(), Widget.gtk_motion_notify_event()
			g_signal_connect(item, "motion-notify-event",  G_CALLBACK(swt_motion_notify_event), swtWidget);

			// See SWT DropTarget.DropTarget(), DropTarget.drag_data_received()
			g_signal_connect(item, "drag-data-received",   G_CALLBACK(swt_drag_data_received),  swtWidget);

			// See SWT DragSource.DragSource(), DragSource.dragGetData()
			g_signal_connect(item, "drag-data-get",        G_CALLBACK(swt_drag_data_get),	    swtWidget);

			// See SWT DropTarget.DropTarget(), DropTarget.drag_drop()
			g_signal_connect(item, "drag-drop",            G_CALLBACK(swt_drag_drop),            swtWidget);
		}
	}

	gtk_widget_show_all(window);
	gtk_main();

	return 0;
}
