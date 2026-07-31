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

public class NoWarnings extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> experimentalSettingsWarning = sgGeneral.add(new BoolSetting.Builder()
        .name("no-experimental-settings-warning")
        .description("Disable the warning for experimental world settings.")
        .defaultValue(true)
        .build()
    );
    private final Setting<Boolean> dialogWarning = sgGeneral.add(new BoolSetting.Builder()
        .name("no-dialog-warning")
        .description("Disable the warning for custom dialogs.")
        .defaultValue(true)
        .build()
    );
    private final Setting<Boolean> commandRunWarning = sgGeneral.add(new BoolSetting.Builder()
        .name("no-command-run-warning")
        .description("Disable the warning for running a command from a dialog.")
        .defaultValue(true)
        .build()
    );

    public NoWarnings() {
        super(Categories.Misc, "no-warnings", "Disable annoying warning screens.");
    }

    public boolean disableCommandRunWarning() {
        return commandRunWarning.get();
    }
}
