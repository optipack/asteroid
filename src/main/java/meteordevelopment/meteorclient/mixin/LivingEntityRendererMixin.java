/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.Freecam;
import meteordevelopment.meteorclient.utils.player.Rotations;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @Shadow @Nullable
    protected abstract RenderLayer getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline);

    // Freecam

    @ModifyExpressionValue(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getCameraEntity()Lnet/minecraft/entity/Entity;"))
    private Entity hasLabelGetCameraEntityProxy(Entity cameraEntity) {
        return Modules.get().isActive(Freecam.class) ? null : cameraEntity;
    }

    //3rd Person Rotation

    @ModifyVariable(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", ordinal = 2, at = @At(value = "STORE", ordinal = 0))
    public float changeYaw(float oldValue, LivingEntity entity) {
        if (entity.equals(mc.player) && Rotations.rotationTimer < 10) return Rotations.serverYaw;
        return oldValue;
    }

    @ModifyVariable(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", ordinal = 3, at = @At(value = "STORE", ordinal = 0))
    public float changeHeadYaw(float oldValue, LivingEntity entity) {
        if (entity.equals(mc.player) && Rotations.rotationTimer < 10) return Rotations.serverYaw;
        return oldValue;
    }

    @ModifyVariable(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", ordinal = 5, at = @At(value = "STORE", ordinal = 3))
    public float changePitch(float oldValue, LivingEntity entity) {
        if (entity.equals(mc.player) && Rotations.rotationTimer < 10) return Rotations.serverPitch;
        return oldValue;
    }

    // Player model rendering in main menu

    @ModifyExpressionValue(method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getScoreboardTeam()Lnet/minecraft/scoreboard/Team;"))
    private Team hasLabelClientPlayerEntityGetScoreboardTeamProxy(Team team) {
        return (mc.player == null) ? null : team;
    }
}
