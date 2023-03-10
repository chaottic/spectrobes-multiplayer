package com.chaottic.spectrobes;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;

public final class Window {
    private final long window;

    {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6);
        glfwWindowHint(GLFW_CONTEXT_RELEASE_BEHAVIOR, GLFW_RELEASE_BEHAVIOR_FLUSH);

        var monitor = glfwGetPrimaryMonitor();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            var buffer = stack.callocInt(4);

            getWorkArea(monitor, memAddress(buffer));

            var width = buffer.get(2) / 2;
            var height = buffer.get(3) / 2;

            if ((window = glfwCreateWindow(width, height, "Spectrobes Multiplayer", NULL, NULL)) == NULL) {
                glfwTerminate();
                throw new RuntimeException("Failed to create a new GLFW Window");
            }

            glfwSetWindowPos(window, width / 2, height / 2);
        }

        glfwMakeContextCurrent(window);

        GL.createCapabilities();
    }

    public void draw(SpectrobesClient client) {
        var projection = new Matrix4f();
        var view = new Matrix4f();
        var model = new Matrix4f();

        while (!glfwWindowShouldClose(window)) {

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public void destroy() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }

    private static void getWorkArea(long monitor, long address) {
        nglfwGetMonitorWorkarea(monitor, address, address + 4, address + 8, address + 12);
    }
}
