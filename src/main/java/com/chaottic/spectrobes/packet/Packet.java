package com.chaottic.spectrobes.packet;

import io.netty.buffer.ByteBuf;

public interface Packet {

    void encode(ByteBuf byteBuf);
}
