/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import it.unimi.dsi.fastutil.Stack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import meteordevelopment.meteorclient.mixininterface.IWorldRenderer;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.BlockSelection;
import meteordevelopment.meteorclient.systems.modules.render.Freecam;
import meteordevelopment.meteorclient.systems.modules.render.NoRender;
import net.minecraft.block.BlockState;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Handle;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements IWorldRenderer {
    @Shadow
    protected abstract void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers);

    @Inject(method = "checkEmpty", at = @At("HEAD"), cancellable = true)
    private void onCheckEmpty(MatrixStack matrixStack, CallbackInfo info) {
        info.cancel();
    }

    @Inject(method = "drawBlockOutline", at = @At("HEAD"), cancellable = true)
    private void onDrawHighlightedBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state, int i, CallbackInfo ci) {
        if (Modules.get().isActive(BlockSelection.class)) ci.cancel();
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;setupTerrain(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/Frustum;ZZ)V"), index = 3)
    private boolean renderSetupTerrainModifyArg(boolean spectator) {
        return Modules.get().isActive(Freecam.class) || spectator;
    }

    // No Render

    @WrapWithCondition(method = "method_62216", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WeatherRendering;renderPrecipitation(Lnet/minecraft/world/World;Lnet/minecraft/client/render/VertexConsumerProvider;IFLnet/minecraft/util/math/Vec3d;)V"))
    private boolean shouldRenderPrecipitation(WeatherRendering instance, World world, VertexConsumerProvider vertexConsumers, int ticks, float tickProgress, Vec3d pos) {
        return !Modules.get().get(NoRender.class).noWeather();
    }

    @WrapWithCondition(method = "method_62216", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldBorderRendering;render(Lnet/minecraft/world/border/WorldBorder;Lnet/minecraft/util/math/Vec3d;DD)V"))
    private boolean shouldRenderWorldBorder(WorldBorderRendering instance, WorldBorder border, Vec3d cameraPos, double viewDistanceBlocks, double farPlaneDistance) {
        return !Modules.get().get(NoRender.class).noWorldBorder();
    }

	@Inject(method = "hasBlindnessOrDarkness(Lnet/minecraft/client/render/Camera;)Z", at = @At("HEAD"), cancellable = true)
	private void hasBlindnessOrDarkness(Camera camera, CallbackInfoReturnable<Boolean> info) {
		if (Modules.get().get(NoRender.class).noBlindness() || Modules.get().get(NoRender.class).noDarkness()) info.setReturnValue(null);
	}

    // IWorldRenderer

    @Shadow
    private Framebuffer entityOutlineFramebuffer;

    @Shadow
    @Final
    private DefaultFramebufferSet framebufferSet;

    @Unique
    private Stack<Framebuffer> framebufferStack;

    @Unique
    private Stack<Handle<Framebuffer>> framebufferHandleStack;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init$IWorldRenderer(CallbackInfo info) {
        framebufferStack = new ObjectArrayList<>();
        framebufferHandleStack = new ObjectArrayList<>();
    }

    @Override
    public void meteor$pushEntityOutlineFramebuffer(Framebuffer framebuffer) {
        framebufferStack.push(this.entityOutlineFramebuffer);
        this.entityOutlineFramebuffer = framebuffer;

        framebufferHandleStack.push(this.framebufferSet.entityOutlineFramebuffer);
        this.framebufferSet.entityOutlineFramebuffer = () -> framebuffer;
    }

    @Override
    public void meteor$popEntityOutlineFramebuffer() {
        this.entityOutlineFramebuffer = framebufferStack.pop();
        this.framebufferSet.entityOutlineFramebuffer = framebufferHandleStack.pop();
    }
}
