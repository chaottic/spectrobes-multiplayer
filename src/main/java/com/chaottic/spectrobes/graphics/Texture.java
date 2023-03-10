package com.chaottic.spectrobes.graphics;

import com.chaottic.spectrobes.Resources;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.stb.STBImage.nstbi_image_free;
import static org.lwjgl.stb.STBImage.nstbi_load_from_memory;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memFree;

public final class Texture {

    private Texture() {}

    public static int createTextureArray(List<String> paths, MemoryStack memoryStack, int width, int height) throws IOException {
        var buffer = memoryStack.callocInt(4);

        var address = memAddress(buffer);

        nglCreateTextures(GL_TEXTURE_2D_ARRAY, 1, address);

        var texture = buffer.get(0);

        glTextureParameterf(texture, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTextureParameterf(texture, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);

        var size = paths.size();

        glTextureStorage3D(texture, 1, GL_RGBA8, width, height, size);

        for (int i = 0; i < size; i++) {
            var pixels = Resources.readPixels(paths.get(i));

            var l = loadFromMemory(pixels, address + 4);

            var x = buffer.get(1);
            var y = buffer.get(2);

            glTextureSubImage3D(texture, 0, 0, 0, i, x, y, 1, GL_RGBA, GL_UNSIGNED_BYTE, l);

            nstbi_image_free(l);

            memFree(pixels);
        }

        return texture;
    }

    private static long loadFromMemory(ByteBuffer pixels, long address) {
        return nstbi_load_from_memory(memAddress(pixels), pixels.remaining(), address, address + 4, address + 8, 4);
    }
}
