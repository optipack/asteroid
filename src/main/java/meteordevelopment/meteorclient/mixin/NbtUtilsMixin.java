/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.BetterChat;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NbtUtils.class)
public final class NbtUtilsMixin {
    @ModifyReturnValue(method = "toPrettyComponent", at = @At("RETURN"))
    private static Component toPrettyComponent(Component original, Tag tag) {
        BetterChat bc = Modules.get().get(BetterChat.class);

        if (bc.isActive() && bc.copyCommandOutput.get()) {
            return original.copy().withStyle(Style.EMPTY.withHoverEvent(
                new HoverEvent.ShowText(
                    Component.translatable("chat.copy.click")
                )
            ).withClickEvent(
                new ClickEvent.CopyToClipboard(tag.toString())
            ));
        }

        return original;
    }
}
