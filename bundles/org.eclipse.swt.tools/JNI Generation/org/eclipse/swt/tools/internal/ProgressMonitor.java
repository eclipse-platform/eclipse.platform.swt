package org.eclipse.swt.tools.internal;

public interface ProgressMonitor {

public void setTotal(int total);
public void setMessage(String message);
public void step();

}
