package com.aerocore;

public class AeroConfig {
    private final String licenseKey;
    private final String remoteUrl;
    private final boolean enableAntiDebug;
    private final boolean enableAntiTamper;
    private final boolean enableIntegrityCheck;
    private final boolean enableTelemetry;
    private final boolean enableVerboseLogging;
    private final boolean hideRootByDefault;
    private final boolean hideXposedByDefault;
    private final boolean enableDaemonService;

    private AeroConfig(Builder builder) {
        this.licenseKey = builder.licenseKey;
        this.remoteUrl = builder.remoteUrl;
        this.enableAntiDebug = builder.enableAntiDebug;
        this.enableAntiTamper = builder.enableAntiTamper;
        this.enableIntegrityCheck = builder.enableIntegrityCheck;
        this.enableTelemetry = builder.enableTelemetry;
        this.enableVerboseLogging = builder.enableVerboseLogging;
        this.hideRootByDefault = builder.hideRootByDefault;
        this.hideXposedByDefault = builder.hideXposedByDefault;
        this.enableDaemonService = builder.enableDaemonService;
    }

    public String getLicenseKey() { return licenseKey; }
    public String getRemoteUrl() { return remoteUrl; }
    public boolean isEnableAntiDebug() { return enableAntiDebug; }
    public boolean isEnableAntiTamper() { return enableAntiTamper; }
    public boolean isEnableIntegrityCheck() { return enableIntegrityCheck; }
    public boolean isEnableTelemetry() { return enableTelemetry; }
    public boolean isEnableVerboseLogging() { return enableVerboseLogging; }
    public boolean isHideRootByDefault() { return hideRootByDefault; }
    public boolean isHideXposedByDefault() { return hideXposedByDefault; }
    public boolean isEnableDaemonService() { return enableDaemonService; }

    public static class Builder {
        private String licenseKey;
        private String remoteUrl;
        private boolean enableAntiDebug = true;
        private boolean enableAntiTamper = true;
        private boolean enableIntegrityCheck = true;
        private boolean enableTelemetry = false;
        private boolean enableVerboseLogging = false;
        private boolean hideRootByDefault = true;
        private boolean hideXposedByDefault = true;
        private boolean enableDaemonService = true;

        public Builder licenseKey(String licenseKey) {
            this.licenseKey = licenseKey;
            return this;
        }

        public Builder remoteUrl(String remoteUrl) {
            this.remoteUrl = remoteUrl;
            return this;
        }

        public Builder enableAntiDebug(boolean val) {
            this.enableAntiDebug = val;
            return this;
        }

        public Builder enableAntiTamper(boolean val) {
            this.enableAntiTamper = val;
            return this;
        }

        public Builder enableIntegrityCheck(boolean val) {
            this.enableIntegrityCheck = val;
            return this;
        }

        public Builder enableTelemetry(boolean val) {
            this.enableTelemetry = val;
            return this;
        }

        public Builder enableVerboseLogging(boolean val) {
            this.enableVerboseLogging = val;
            return this;
        }

        public Builder hideRootByDefault(boolean val) {
            this.hideRootByDefault = val;
            return this;
        }

        public Builder hideXposedByDefault(boolean val) {
            this.hideXposedByDefault = val;
            return this;
        }

        public Builder enableDaemonService(boolean val) {
            this.enableDaemonService = val;
            return this;
        }

        public AeroConfig build() {
            return new AeroConfig(this);
        }
    }
}
