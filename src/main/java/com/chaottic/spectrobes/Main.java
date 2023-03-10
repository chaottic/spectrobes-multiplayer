package com.chaottic.spectrobes;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {

        if (!glfwInit()) {
            throw new RuntimeException("Failed to initialise GLFW");
        }

        var host = "localhost";
        var port = 8080;

        new Thread(() -> {
            var server = new SpectrobesServer();
            server.host(host, port);
        }).start();

        var client = new SpectrobesClient();
        client.connect(host, port);

        var window = new Window();
        window.draw(client);
        window.destroy();

        glfwTerminate();
    }
}
