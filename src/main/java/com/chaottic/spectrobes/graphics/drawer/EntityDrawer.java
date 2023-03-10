package com.chaottic.spectrobes.graphics.drawer;

import com.chaottic.spectrobes.entity.Entity;
import com.chaottic.spectrobes.graphics.ProgramPipeline;
import com.chaottic.spectrobes.graphics.Texture;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.util.List;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.*;

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

            var mesh = memCalloc(88);

            mesh
                    .putFloat(-0.5F).putFloat(-0.5F).putFloat(0.0F).putFloat(1.0F)
                    .putFloat( 0.5F).putFloat(-0.5F).putFloat(0.0F).putFloat(1.0F)
                    .putFloat( 0.5F).putFloat( 0.5F).putFloat(0.0F).putFloat(1.0F)
                    .putFloat(-0.5F).putFloat( 0.5F).putFloat(0.0F).putFloat(1.0F);

            mesh
                    .putInt(0).putInt(1).putInt(2)
                    .putInt(2).putInt(3).putInt(0);

            mesh.flip();

            var vertices = 16 * 4;
            var elements = 6 * 4;

            mesh.limit(vertices);
            glNamedBufferStorage(vbo, mesh,0);
            mesh.position(vertices);

            mesh.limit(vertices + elements);
            glNamedBufferStorage(ebo, mesh, 0);
            mesh.position(0);

            memFree(mesh);

            // Pipeline.
            tuple = ProgramPipeline.createTuple("entity", stack);

            var vertex = tuple.vertex();
            projectionUniformLocation = glGetUniformLocation(vertex, "projection");
            viewUniformLocation = glGetUniformLocation(vertex, "view");
            modelUniformLocationl = glGetUniformLocation(vertex, "model");

            // Texture.
            var paths = List.of(
                    "texture/rallen/rallen_down.png",
                    "texture/rallen/rallen_left.png",
                    "texture/rallen/rallen_right.png",
                    "texture/rallen/rallen_up.png");

            glBindTextureUnit(0, Texture.createTextureArray(paths, stack, 64, 64));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void draw(List<Entity> entities, Matrix4f projection, Matrix4f view, Matrix4f model) {
        glBindProgramPipeline(tuple.pipeline());

        try (MemoryStack stack = MemoryStack.stackPush()) {
            var buffer = stack.callocFloat(16);

            var vertex = tuple.vertex();
            glProgramUniformMatrix4fv(vertex, projectionUniformLocation, false, projection.get(buffer));
            glProgramUniformMatrix4fv(vertex, viewUniformLocation, false, view.get(buffer));
            glProgramUniformMatrix4fv(vertex, modelUniformLocationl, false, model.get(buffer));
        }

        glBindVertexArray(vao);

        glEnableVertexArrayAttrib(vao, 0);

        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

        glDisableVertexArrayAttrib(vao, 0);

        glBindVertexArray(0);

        glBindProgramPipeline(0);
    }

    private void animate() {

    }
}
