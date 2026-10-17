# HiDPI Support in SWT

This document (incompletely) describes certain parts of the HiDPI support in SWT for all platforms (Windows, MacOS, GTK).

## Table of Contents

1. [Autoscale Methods](#autoscale-Methods)

### Autoscale Methods

The `swt.autoScale.method` system property controls the method used to determine how to handle scaling of Images in SWT:

- **`smooth`**

- Uses anti-aliased / bilinear interpolation
- Smooth edges, may appear slightly blurry

- **`nearest`**

- Uses nearest-neighbor interpolation
- Edges appear jagged or blocky


**Example usage:** To configure the smooth scaling, set the system property to one of the above, e.g. *-Dswt.autoScale.method=smooth*

**Default Behavior**: 

- `GTK`: Uses smooth scaling only when deviceZoom is an integer multiple of 100% (100%, 200%, …). Otherwise, SWT falls back to nearest.
- `Windows`: Uses smooth scaling only if monitor-specific DPI scaling is enabled (Per-Monitor DPI awareness). Otherwise, SWT uses nearest.
- `MacOS`: Never uses smooth scaling.
