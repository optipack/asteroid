package meteordevelopment.meteorclient.mixin;

import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.Annoyances;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.FocusableTextWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FocusableTextWidget.class)
public abstract class FocusableTextWidgetMixin {
    // Credit: adapted from Unjank by TheNuclearNexus (MIT licensed)

    @Redirect(method = "extractWidgetRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;outline(IIIII)V"))
    private void outline(GuiGraphicsExtractor instance, int x, int y, int width, int height, int color) {
        Annoyances annoyances = Modules.get().get(Annoyances.class);
        if (annoyances.removeDialogTextFocusBorder()) return;

        instance.outline(x, y, width, height, color);
    }
}
