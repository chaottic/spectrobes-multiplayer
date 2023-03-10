package com.chaottic.spectrobes.graphics.drawer;

import com.chaottic.spectrobes.entity.Entity;
import com.chaottic.spectrobes.graphics.ProgramPipeline;
import com.chaottic.spectrobes.graphics.Texture;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.util.List;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.memAddress;

public final class EntityDrawer {
    private final ProgramPipeline.Tuple tuple;

    private final int projectionUniformLocation;
    private final int viewUniformLocation;
    private final int modelUniformLocationl;

    private final int vao;
    private final int vbo;
    private final int ebo;

    {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            var buffer = stack.callocInt(3);

            var address = memAddress(buffer);

            nglCreateVertexArrays(1, address);

            nglCreateBuffers(2, address + 4);

            vao = buffer.get(0);
            vbo = buffer.get(1);
            ebo = buffer.get(2);

            glVertexArrayVertexBuffer(vao, 0, vbo, 0, 16);

            glVertexArrayElementBuffer(vao, ebo);

            tuple = ProgramPipeline.createTuple("entity", stack);

            var vertex = tuple.vertex();
            projectionUniformLocation = glGetUniformLocation(vertex, "projection");
            viewUniformLocation = glGetUniformLocation(vertex, "view");
            modelUniformLocationl = glGetUniformLocation(vertex, "model");

            var paths = List.of(
                    "texture/rallen/rallen_down.png",
                    "texture/rallen/rallen_left.png",
                    "texture/rallen/rallen_right.png",
                    "texture/rallen/rallen/up.png");

            glBindTextureUnit(0, Texture.createTextureArray(paths, stack, 64, 64));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(List<Entity> entities, Matrix4f projection, Matrix4f view, Matrix4f model) {

        try (MemoryStack stack = MemoryStack.stackPush()) {
            var buffer = stack.callocFloat(16);

        }

        glBindVertexArray(vao);

        glEnableVertexArrayAttrib(vao, 0);

        glDisableVertexArrayAttrib(vao, 0);

        glBindVertexArray(0);
    }
}
