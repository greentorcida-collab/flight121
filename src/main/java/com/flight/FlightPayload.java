package com.flight;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record FlightPayload() implements CustomPayload {
    public static final CustomPayload.Id<FlightPayload> ID =
        new CustomPayload.Id<>(Identifier.of("flight", "toggle"));

    public static final PacketCodec<RegistryByteBuf, FlightPayload> CODEC =
        PacketCodec.unit(new FlightPayload());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
