package com.flight;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class Flight implements ModInitializer {
    // Kecepatan terbang saat ON (Creative biasa = 0.05F). Ubah ke 0.1F untuk dua kali lebih cepat.
    private static final float FLY_SPEED = 0.05F;
    private static final float DEFAULT_FLY_SPEED = 0.05F;

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(FlightPayload.ID, FlightPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(FlightPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            if (player.isCreative() || player.isSpectator()) {
                return;
            }

            PlayerAbilities abilities = player.getAbilities();
            if (!abilities.allowFlying) {
                abilities.allowFlying = true;
                abilities.flying = true;
                abilities.setFlySpeed(FLY_SPEED);
                player.sendAbilitiesUpdate();
                player.sendMessage(Text.literal("Terbang: ON"), true);
            } else {
                boolean inAir = !player.isOnGround();
                abilities.allowFlying = false;
                abilities.flying = false;
                abilities.setFlySpeed(DEFAULT_FLY_SPEED);
                player.fallDistance = 0.0F;
                if (inAir) {
                    player.addStatusEffect(
                        new StatusEffectInstance(StatusEffects.SLOW_FALLING, 200, 0, false, false));
                }
                player.sendAbilitiesUpdate();
                player.sendMessage(Text.literal("Terbang: OFF"), true);
            }
        });
    }
}
