package com.rxmuhammadyoussef.socialmediahelper.util;

public class Preconditions {

    public static void checkNotNull(Object o) {
        if (o == null) {
            throw new NullPointerException("Cannot be null");
        }
    }

    public static boolean isNotNull(Object o) {
        return o != null;
    }
}
