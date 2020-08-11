package de.bentrm.datacat.service.impl;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceUtil {

    @NotNull
    static Runnable throwEntityNotFoundException(String id) {
        return () -> {
            throw new IllegalArgumentException("No Object with id " + id + " found.");
        };
    }

    static List<String> sanitizeTextInputValue(String value) {
        return Arrays.stream(value.split(",")).map(String::trim).collect(Collectors.toList());
    }


}
