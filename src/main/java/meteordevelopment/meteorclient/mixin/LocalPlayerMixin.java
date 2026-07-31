/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.entity.DropItemsEvent;
import meteordevelopment.meteorclient.events.entity.player.PlayerTickMovementEvent;
import meteordevelopment.meteorclient.events.entity.player.SendMovementPacketsEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.player.NoMiningTrace;
import meteordevelopment.meteorclient.systems.modules.player.Portals;
import meteordevelopment.meteorclient.utils.entity.fakeplayer.FakePlayerEntity;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    @Shadow
    public ClientInput input;

    public LocalPlayerMixin(ClientLevel world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    private void onDrop(boolean all, CallbackInfoReturnable<Boolean> cir) {
        if (MeteorClient.EVENT_BUS.post(DropItemsEvent.get(getMainHandItem())).isCancelled())
            cir.setReturnValue(false);
    }

    @ModifyExpressionValue(method = "handlePortalTransitionEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;screen()Lnet/minecraft/client/gui/screens/Screen;", ordinal = 0))
    private Screen modifyPortalTransitionEffect(Screen original) {
        if (Modules.get().isActive(Portals.class)) return null;
        return original;
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void preAiStep(CallbackInfo ci) {
        MeteorClient.EVENT_BUS.post(PlayerTickMovementEvent.get());
    }

    @ModifyReturnValue(method = "pick", at = @At("RETURN"))
    private static HitResult onUpdateTargetedEntity(HitResult original, @Local(name = "blockHitResult") HitResult blockHitResult) {
        if (original instanceof EntityHitResult ehr) {
            if (Modules.get().get(NoMiningTrace.class).canWork(ehr.getEntity()) && blockHitResult.getType() == HitResult.Type.BLOCK) {
                return blockHitResult;
            } else if (ehr.getEntity() instanceof FakePlayerEntity fakePlayer && fakePlayer.noHit) {
                return blockHitResult;
            }
        }

        return original;
    }

    // Rotations

    @Inject(method = "sendPosition", at = @At("HEAD"))
    private void onSendPositionHead(CallbackInfo ci) {
        MeteorClient.EVENT_BUS.post(SendMovementPacketsEvent.Pre.get());
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V", ordinal = 1))
    private void onTickHasVehicleBeforeSendPackets(CallbackInfo ci) {
        MeteorClient.EVENT_BUS.post(SendMovementPacketsEvent.Pre.get());
    }

    @Inject(method = "sendPosition", at = @At("TAIL"))
    private void onSendPositionTail(CallbackInfo ci) {
        MeteorClient.EVENT_BUS.post(SendMovementPacketsEvent.Post.get());
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;send(Lnet/minecraft/network/protocol/Packet;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void onTickHasVehicleAfterSendPackets(CallbackInfo ci) {
        MeteorClient.EVENT_BUS.post(SendMovementPacketsEvent.Post.get());
    }
}
