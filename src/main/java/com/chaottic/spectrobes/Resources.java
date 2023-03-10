package com.chaottic.spectrobes;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public final class Resources {

    private Resources() {}

    public static InputStream getResource(String path) throws IOException {
        return Objects.requireNonNull(Resources.class.getClassLoader().getResource(path)).openStream();
    }

    public static String readString(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getResource(path)))) {
            var builder = new StringBuilder();

            @Nullable
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            return builder.toString();
        }
    }
}
