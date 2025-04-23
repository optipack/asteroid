/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.entity.LivingEntityMoveEvent;
import meteordevelopment.meteorclient.events.entity.player.JumpVelocityMultiplierEvent;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.FreeLook;
import meteordevelopment.meteorclient.systems.modules.render.Freecam;
import meteordevelopment.meteorclient.systems.modules.render.NoRender;
import meteordevelopment.meteorclient.utils.entity.fakeplayer.FakePlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @ModifyArgs(method = "pushAwayFrom(Lnet/minecraft/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    private void onPushAwayFrom(Args args, Entity entity) {
        // FakePlayerEntity
        if (entity instanceof FakePlayerEntity player && player.doNotPush) {
            args.set(0, 0.0);
            args.set(2, 0.0);
        }
    }

    @ModifyReturnValue(method = "getJumpVelocityMultiplier", at = @At("RETURN"))
    private float onGetJumpVelocityMultiplier(float original) {
        if ((Object) this == mc.player) {
            JumpVelocityMultiplierEvent event = MeteorClient.EVENT_BUS.post(JumpVelocityMultiplierEvent.get());
            return (original * event.multiplier);
        }

        return original;
    }

    @Inject(method = "move", at = @At("HEAD"))
    private void onMove(MovementType type, Vec3d movement, CallbackInfo info) {
        if ((Object) this == mc.player) {
            MeteorClient.EVENT_BUS.post(PlayerMoveEvent.get(type, movement));
        }
        else if ((Object) this instanceof LivingEntity) {
            MeteorClient.EVENT_BUS.post(LivingEntityMoveEvent.get((LivingEntity) (Object) this, movement));
        }
    }

    @Inject(method = "isGlowing", at = @At("HEAD"), cancellable = true)
    private void isGlowing(CallbackInfoReturnable<Boolean> info) {
        if (Modules.get().get(NoRender.class).noGlowing()) info.setReturnValue(false);
    }

    @Inject(method = "isInvisibleTo", at = @At("HEAD"), cancellable = true)
    private void onIsInvisibleTo(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
        if (player == null) info.setReturnValue(false);
    }

    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    private void updateChangeLookDirection(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {
        if ((Object) this != mc.player) return;

        Freecam freecam = Modules.get().get(Freecam.class);
        FreeLook freeLook = Modules.get().get(FreeLook.class);

        if (freecam.isActive()) {
            freecam.changeLookDirection(cursorDeltaX * 0.15, cursorDeltaY * 0.15);
            ci.cancel();
        }
        else if (freeLook.cameraMode()) {
            freeLook.cameraYaw += (float) (cursorDeltaX / freeLook.sensitivity.get().floatValue());
            freeLook.cameraPitch += (float) (cursorDeltaY / freeLook.sensitivity.get().floatValue());

            if (Math.abs(freeLook.cameraPitch) > 90.0F) freeLook.cameraPitch = freeLook.cameraPitch > 0.0F ? 90.0F : -90.0F;
            ci.cancel();
        }
    }
}
