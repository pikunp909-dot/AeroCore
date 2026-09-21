package com.aerocore.license;

public interface LicenseManager {
    void setLicenseKey(String key);
    boolean validate();
    boolean validateAsync(Callback callback);
    long daysRemaining();
    boolean isExpired();
    String licenseId();
    void clear();

    interface Callback {
        void onResult(boolean success);
    }
}
