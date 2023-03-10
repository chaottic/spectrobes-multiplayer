package com.chaottic.spectrobes.graphics;

import com.chaottic.spectrobes.Resources;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.system.MemoryUtil.memAddress;

public final class ProgramPipeline {

    private ProgramPipeline() {
    }

    public static Tuple createTuple(String path, MemoryStack memoryStack) throws IOException {
        var buffer = memoryStack.callocInt(1);

        nglCreateProgramPipelines(1, memAddress(buffer));

        var pipeline = buffer.get(0);

        var fragment = glCreateShaderProgramv(GL_FRAGMENT_SHADER, Resources.readString("shader/%s/fragment.glsl".formatted(path)));
        var vertex = glCreateShaderProgramv(GL_VERTEX_SHADER, Resources.readString("shader/%s/vertex.glsl".formatted(path)));

        glUseProgramStages(pipeline, GL_FRAGMENT_SHADER_BIT, fragment);
        glUseProgramStages(pipeline, GL_VERTEX_SHADER_BIT, vertex);

        glValidateProgramPipeline(pipeline);

        return new Tuple(pipeline, fragment, vertex);
    }

    public record Tuple(int pipeline, int fragment, int vertex) {

    }
}
