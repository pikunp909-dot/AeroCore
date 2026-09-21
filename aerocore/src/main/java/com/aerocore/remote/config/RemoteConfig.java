package com.aerocore.remote.config;

public class RemoteConfig {
    private boolean daemonServiceEnabled = true;
    private boolean hideRootEnabled = true;
    private boolean hideXposedEnabled = true;
    private String customMessage = "";

    public boolean isDaemonServiceEnabled() { return daemonServiceEnabled; }
    public void setDaemonServiceEnabled(boolean val) { this.daemonServiceEnabled = val; }

    public boolean isHideRootEnabled() { return hideRootEnabled; }
    public void setHideRootEnabled(boolean val) { this.hideRootEnabled = val; }

    public boolean isHideXposedEnabled() { return hideXposedEnabled; }
    public void setHideXposedEnabled(boolean val) { this.hideXposedEnabled = val; }

    public String getCustomMessage() { return customMessage; }
    public void setCustomMessage(String msg) { this.customMessage = msg; }
}
