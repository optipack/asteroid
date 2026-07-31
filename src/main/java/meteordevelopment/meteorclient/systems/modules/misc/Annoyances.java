/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.systems.modules.misc;

import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;

public class Annoyances extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> experimentalSettingsWarning = sgGeneral.add(new BoolSetting.Builder()
        .name("no-experimental-settings-warning")
        .description("Disable the warning for experimental world settings.")
        .defaultValue(true)
        .build()
    );
    private final Setting<Boolean> dialogWarning = sgGeneral.add(new BoolSetting.Builder()
        .name("remove-dialog-warning")
        .description("Disable the warning for custom dialogs.")
        .defaultValue(true)
        .build()
    );
    private final Setting<Boolean> dialogTextFocusBorder = sgGeneral.add(new BoolSetting.Builder()
        .name("remove-dialog-text-focus-border")
        .description("Remove the ugly focus border for text areas inside custom dialogs.")
        .defaultValue(true)
        .build()
    );
    private final Setting<Boolean> commandRunWarning = sgGeneral.add(new BoolSetting.Builder()
        .name("no-command-run-warning")
        .description("Disable the warning for running a command from a dialog.")
        .defaultValue(true)
        .build()
    );

    public Annoyances() {
        super(Categories.Misc, "annoyances", "Disable annoying warning screens and weird UI.");
    }

    public boolean disableExperimentalSettingsWarning() { return experimentalSettingsWarning.get() && isActive(); }
    public boolean removeDialogWarning() { return dialogWarning.get() && isActive(); }
    public boolean removeDialogTextFocusBorder() { return dialogTextFocusBorder.get() && isActive(); }
    public boolean disableCommandRunWarning() {
        return commandRunWarning.get() && isActive();
    }
}
