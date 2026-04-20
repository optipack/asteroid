/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.systems.modules;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.addons.AddonManager;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.settings.Settings;
import meteordevelopment.meteorclient.systems.config.Config;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.misc.ISerializable;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class Module implements ISerializable<Module>, Comparable<Module> {
    protected final Minecraft mc;

    public final Category category;
    public final String name;
    public String title;
    public final String description;
    public final String[] aliases;
    public final Color color;

    public final MeteorAddon addon;
    public final Settings settings = new Settings();

    private boolean active;
    public boolean disabled;
    public boolean isCheat;
    public boolean oneShot = false;

    public boolean serialize = true;
    public boolean runInMainMenu = false;
    public boolean autoSubscribe = true;

    public final Keybind keybind = Keybind.none();
    public boolean toggleOnBindRelease = false;
    public boolean chatFeedback = true;
    public boolean favorite = false;

    public Module(Category category, String name, String description, String... aliases) {
        if (name.contains(" "))
            MeteorClient.LOG.warn("Module '{}' contains invalid characters in its name making it incompatible with Meteor Client commands.", name);

        this.mc = Minecraft.getInstance();
        this.category = category;
        this.name = name;
        this.title = Utils.nameToTitle(name);
        this.description = description;
        this.aliases = aliases;
        this.color = Color.fromHsv(Utils.random(0.0, 360.0), 0.35, 1);

        String classname = this.getClass().getName();
        for (MeteorAddon addon : AddonManager.ADDONS) {
            if (classname.startsWith(addon.getPackage())) {
                this.addon = addon;
                return;
            }
        }

        this.addon = null;
    }

    public Module(Category category, String name, String desc) {
        this(category, name, desc, new String[0]);
    }

    public WWidget getWidget(GuiTheme theme) {
        return null;
    }

    public void onActivate() {
    }

    public void onDeactivate() {
    }

    public void activate() {
        activate(true);
    }

    public void activate(boolean callback) {
        if (!isEnabled()) { return; }
        if (active) { return; }

        if (oneShot) {
            if (callback) { oneshotActivate(); }
        } else {
            active = true;
            Modules.get().addActive(this);

            if (runInMainMenu || Utils.canUpdate()) {
                if (autoSubscribe) MeteorClient.EVENT_BUS.subscribe(this);
                if (callback) { onActivate(); }
            }
        }

        settings.onActivated();
    }

    private void oneshotActivate() {
        if (!runInMainMenu && !Utils.canUpdate()) { return; }

        onActivate();
    }

    public void deactivate() {
        deactivate(true);
    }

    public void deactivate(boolean callback) {
        if (oneShot) { return; }
        if (!active) { return; }

        if (runInMainMenu || Utils.canUpdate()) {
            if (autoSubscribe) MeteorClient.EVENT_BUS.unsubscribe(this);
            if (callback) { onDeactivate(); }
        }

        active = false;
        Modules.get().removeActive(this);
    }

    public void toggle() {
        toggle(true);
    }

    public void toggle(boolean callback) {
        if (!isEnabled()) { return; }

        if (!active) {
            activate(callback);
        } else {
            deactivate(callback);
        }
    }

    public void enable() {
        if (!isActive()) toggle();
    }

    public void disable() {
        if (isActive()) toggle();
    }

    public void sendToggledMsg() {
        if (Config.get().chatFeedback.get() && chatFeedback) {
            ChatUtils.forceNextPrefixClass(getClass());
            ChatUtils.sendMsg(this.hashCode(), ChatFormatting.GRAY, "Toggled (highlight)%s(default) %s(default).", title, isActive() ? ChatFormatting.GREEN + "on" : ChatFormatting.RED + "off");
        }
    }

    public void info(Component message) {
        ChatUtils.forceNextPrefixClass(getClass());
        ChatUtils.sendMsg(title, message);
    }

    public void info(String message, Object... args) {
        ChatUtils.forceNextPrefixClass(getClass());
        ChatUtils.infoPrefix(title, message, args);
    }

    public void warning(String message, Object... args) {
        ChatUtils.forceNextPrefixClass(getClass());
        ChatUtils.warningPrefix(title, message, args);
    }

    public void error(String message, Object... args) {
        ChatUtils.forceNextPrefixClass(getClass());
        ChatUtils.errorPrefix(title, message, args);
    }

    public boolean isActive() {
        if (!isEnabled()) { return false; }
        return active;
    }

    public boolean isEnabled() {
        if (disabled) { return false; }
        if (!isCheat) { return true; }
        return Utils.cheatsAllowed();
    }

    public String getInfoString() {
        return null;
    }

    public String getDescription() {
        if (this.isCheat && !MeteorClient.BYPASS_CHEATS) {
            return "(Cheat) " + this.description;
        }

        return this.description;
    }

    @Override
    public CompoundTag toTag() {
        if (!serialize) return null;
        CompoundTag tag = new CompoundTag();

        tag.putString("name", name);
        tag.put("keybind", keybind.toTag());
        tag.putBoolean("toggleOnKeyRelease", toggleOnBindRelease);
        tag.putBoolean("chatFeedback", chatFeedback);
        tag.putBoolean("favorite", favorite);
        tag.put("settings", settings.toTag());
        tag.putBoolean("active", active);

        return tag;
    }

    @Override
    public Module fromTag(CompoundTag tag) {
        // General
        keybind.fromTag(tag.getCompoundOrEmpty("keybind"));
        toggleOnBindRelease = tag.getBooleanOr("toggleOnKeyRelease", false);
        chatFeedback = !tag.contains("chatFeedback") || tag.getBooleanOr("chatFeedback", false);
        favorite = tag.getBooleanOr("favorite", false);

        // Settings
        Tag settingsTag = tag.get("settings");
        if (settingsTag instanceof CompoundTag compoundTag) settings.fromTag(compoundTag);

        boolean active = tag.getBooleanOr("active", false);
        if (active != isActive()) toggle();

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return Objects.equals(name, module.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(@NotNull Module o) {
        return name.compareTo(o.name);
    }
}
