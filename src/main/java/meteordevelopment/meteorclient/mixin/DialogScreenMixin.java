package meteordevelopment.meteorclient.mixin;

import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.Annoyances;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.dialog.DialogScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DialogScreen.class)
public abstract class DialogScreenMixin extends Screen {
    // Credit: adapted from Unjank by TheNuclearNexus (MIT licensed)
    @Shadow
    private Button warningButton;

    protected DialogScreenMixin(Component title) {
        super(title);
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/dialog/DialogScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;"))
    private <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(DialogScreen instance, T element) {
        Annoyances annoyances = Modules.get().get(Annoyances.class);
        if (annoyances.removeDialogWarning() && element == warningButton)
            return element;

        return this.addRenderableWidget(element);
    }

    @Redirect(method = "createTitleWithWarningButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/LinearLayout;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"))
    private <T extends LayoutElement> T addChild(LinearLayout instance, T child) {
        Annoyances annoyances = Modules.get().get(Annoyances.class);
        if (annoyances.removeDialogWarning() && child == this.warningButton) {
            return child;
        }

        return instance.addChild(child);
    }
}
