package com.aerocore.util;

public class AeroException extends Exception {
    public AeroException(String message) {
        super(message);
    }

    public AeroException(String message, Throwable cause) {
        super(message, cause);
    }
}
