package com.chaottic.spectrobes;

import com.chaottic.spectrobes.entity.Entity;
import com.chaottic.spectrobes.graphics.drawer.EntityDrawer;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL46.*;
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

        var entityDrawer = new EntityDrawer();
        var entity = new Entity();
        var entities = List.of(entity);

        glClearColor(1.0F, 0.0F, 0.0F, 0.0F);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);

            entityDrawer.draw(entities, projection, view, model);

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
