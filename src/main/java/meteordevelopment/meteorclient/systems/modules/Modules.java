/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.systems.modules;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.game.GameJoinedEvent;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.meteor.ActiveModulesChangedEvent;
import meteordevelopment.meteorclient.events.meteor.KeyEvent;
import meteordevelopment.meteorclient.events.meteor.ModuleBindChangedEvent;
import meteordevelopment.meteorclient.events.meteor.MouseButtonEvent;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.System;
import meteordevelopment.meteorclient.systems.Systems;
import meteordevelopment.meteorclient.systems.config.Config;
import meteordevelopment.meteorclient.systems.modules.misc.*;
import meteordevelopment.meteorclient.systems.modules.player.*;
import meteordevelopment.meteorclient.systems.modules.render.*;
import meteordevelopment.meteorclient.systems.modules.render.blockesp.BlockESP;
import meteordevelopment.meteorclient.systems.modules.render.marker.Marker;
import meteordevelopment.meteorclient.systems.modules.world.Ambience;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.misc.ValueComparableMap;
import meteordevelopment.meteorclient.utils.misc.input.Input;
import meteordevelopment.meteorclient.utils.misc.input.KeyAction;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class Modules extends System<Modules> {
    private static final List<Category> CATEGORIES = new ArrayList<>();

    private final List<Module> modules = new ArrayList<>();
    private final Map<Class<? extends Module>, Module> moduleInstances = new Reference2ReferenceOpenHashMap<>();
    private final Map<Category, List<Module>> groups = new Reference2ReferenceOpenHashMap<>();

    private final List<Module> active = new ArrayList<>();
    private Module moduleToBind;
    private boolean awaitingKeyRelease = false;

    public Modules() {
        super("modules");
    }

    public static Modules get() {
        return Systems.get(Modules.class);
    }

    @Override
    public void init() {
        initPlayer();
        initRender();
        initMisc();

        blacklist();
    }

    @Override
    public void load(File folder) {
        for (Module module : getAll()) {
            for (SettingGroup group : module.settings) {
                for (Setting<?> setting : group) setting.reset();
            }
        }

        super.load(folder);
    }

    public void sortModules() {
        for (List<Module> modules : groups.values()) {
            modules.sort(Comparator.comparing(o -> o.title));
        }
        modules.sort(Comparator.comparing(o -> o.title));
    }

    public static void registerCategory(Category category) {
        if (!Categories.REGISTERING) throw new RuntimeException("Modules.registerCategory - Cannot register category outside of onRegisterCategories callback.");

        CATEGORIES.add(category);
    }

    public static Iterable<Category> loopCategories() {
        return CATEGORIES;
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T get(Class<T> klass) {
        return (T) moduleInstances.get(klass);
    }

    public Module get(String name) {
        for (Module module : moduleInstances.values()) {
            if (module.name.equalsIgnoreCase(name)) return module;
        }

        return null;
    }

    public boolean isActive(Class<? extends Module> klass) {
        Module module = get(klass);
        return module != null && module.isActive();
    }

    public List<Module> getGroup(Category category) {
        return groups.computeIfAbsent(category, category1 -> new ArrayList<>());
    }

    public Stream<Module> getGroupEnabled(Category category) {
        return getGroup(category).stream().filter(module -> module.isEnabled());
    }

    public boolean groupHasEnabledModules(Category category) {
        return getGroupEnabled(category).count() > 0;
    }

    public Collection<Module> getAll() {
        return moduleInstances.values();
    }

    public Stream<Module> getAllEnabled() {
        return moduleInstances.values().stream().filter(module -> module.isEnabled());
    }

    /**
     * @deprecated Use {@link Modules#getAll()} instead.
     */
    @Deprecated(forRemoval = true)
    public List<Module> getList() {
        return modules;
    }

    public int getCount() {
        return moduleInstances.size();
    }

    public int getCountEnabled() {
        return Math.toIntExact(getAllEnabled().count());
    }

    public List<Module> getActive() {
        synchronized (active) {
            return active;
        }
    }

    public Set<Module> searchTitles(String text) {
        Map<Module, Integer> modules = new ValueComparableMap<>(Comparator.naturalOrder());

        for (Module module : this.moduleInstances.values()) {
            if (!module.isEnabled()) { continue; }
            int score = Utils.searchLevenshteinDefault(module.title, text, false);
            if (Config.get().moduleAliases.get()) {
                for (String alias : module.aliases) {
                    int aliasScore = Utils.searchLevenshteinDefault(alias, text, false);
                    if (aliasScore < score) score = aliasScore;
                }
            }
            modules.put(module, modules.getOrDefault(module, 0) + score);
        }

        return modules.keySet();
    }

    public Set<Module> searchSettingTitles(String text) {
        Map<Module, Integer> modules = new ValueComparableMap<>(Comparator.naturalOrder());

        for (Module module : this.moduleInstances.values()) {
            if (!module.isEnabled()) { continue; }
            int lowest = Integer.MAX_VALUE;
            for (SettingGroup sg : module.settings) {
                for (Setting<?> setting : sg) {
                    int score = Utils.searchLevenshteinDefault(setting.title, text, false);
                    if (score < lowest) lowest = score;
                }
            }
            modules.put(module, modules.getOrDefault(module, 0) + lowest);
        }

        return modules.keySet();
    }

    void addActive(Module module) {
        synchronized (active) {
            if (!active.contains(module)) {
                active.add(module);
                MeteorClient.EVENT_BUS.post(ActiveModulesChangedEvent.get());
            }
        }
    }

    void removeActive(Module module) {
        synchronized (active) {
            if (active.remove(module)) {
                MeteorClient.EVENT_BUS.post(ActiveModulesChangedEvent.get());
            }
        }
    }

    // Binding

    public void setModuleToBind(Module moduleToBind) {
        this.moduleToBind = moduleToBind;
    }

    /***
     * @see meteordevelopment.meteorclient.commands.commands.BindCommand
     * For ensuring we don't instantly bind the module to the enter key.
     */
    public void awaitKeyRelease() {
        this.awaitingKeyRelease = true;
    }

    public boolean isBinding() {
        return moduleToBind != null;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onKeyBinding(KeyEvent event) {
        if (event.action == KeyAction.Release && onBinding(true, event.key, event.modifiers)) event.cancel();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onButtonBinding(MouseButtonEvent event) {
        if (event.action == KeyAction.Release && onBinding(false, event.button, 0)) event.cancel();
    }

    private boolean onBinding(boolean isKey, int value, int modifiers) {
        if (!isBinding()) return false;

        if (awaitingKeyRelease) {
            if (!isKey || (value != GLFW.GLFW_KEY_ENTER && value != GLFW.GLFW_KEY_KP_ENTER)) return false;

            awaitingKeyRelease = false;
            return false;
        }

        if (moduleToBind.keybind.canBindTo(isKey, value, modifiers)) {
            moduleToBind.keybind.set(isKey, value, modifiers);
            moduleToBind.info("Bound to (highlight)%s(default).", moduleToBind.keybind);
        }
        else if (value == GLFW.GLFW_KEY_ESCAPE) {
            moduleToBind.keybind.set(Keybind.none());
            moduleToBind.info("Removed bind.");
        }
        else return false;

        MeteorClient.EVENT_BUS.post(ModuleBindChangedEvent.get(moduleToBind));
        moduleToBind = null;

        return true;
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onKey(KeyEvent event) {
        if (event.action == KeyAction.Repeat) return;
        onAction(true, event.key, event.modifiers, event.action == KeyAction.Press);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void onMouseButton(MouseButtonEvent event) {
        if (event.action == KeyAction.Repeat) return;
        onAction(false, event.button, 0, event.action == KeyAction.Press);
    }

    private void onAction(boolean isKey, int value, int modifiers, boolean isPress) {
        if (mc.currentScreen != null || Input.isKeyPressed(GLFW.GLFW_KEY_F3)) return;

        for (Module module : moduleInstances.values()) {
            if (module.keybind.matches(isKey, value, modifiers) && (isPress || (module.toggleOnBindRelease && module.isActive()))) {
                module.toggle();
                module.sendToggledMsg();
            }
        }
    }

    // End of binding

    @EventHandler(priority = EventPriority.HIGHEST + 1)
    private void onOpenScreen(OpenScreenEvent event) {
        if (!Utils.canUpdate()) return;

        for (Module module : moduleInstances.values()) {
            if (module.toggleOnBindRelease && module.isActive()) {
                module.toggle();
                module.sendToggledMsg();
            }
        }
    }

    @EventHandler
    private void onGameJoined(GameJoinedEvent event) {
        synchronized (active) {
            for (Module module : getAll()) {
                if (module.isActive() && !module.runInMainMenu) {
                    MeteorClient.EVENT_BUS.subscribe(module);
                    module.onActivate();
                }
            }
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        synchronized (active) {
            for (Module module : getAll()) {
                if (module.isActive() && !module.runInMainMenu) {
                    MeteorClient.EVENT_BUS.unsubscribe(module);
                    module.onDeactivate();
                }
            }
        }
    }

    public void disableAll() {
        synchronized (active) {
            for (Module module : getAll()) {
                if (module.isActive()) module.toggle();
            }
        }
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        NbtList modulesTag = new NbtList();
        for (Module module : getAll()) {
            NbtCompound moduleTag = module.toTag();
            if (moduleTag != null) modulesTag.add(moduleTag);
        }
        tag.put("modules", modulesTag);

        return tag;
    }

    @Override
    public Modules fromTag(NbtCompound tag) {
        disableAll();

        NbtList modulesTag = tag.getListOrEmpty("modules");
        for (NbtElement moduleTagI : modulesTag) {
            NbtCompound moduleTag = (NbtCompound) moduleTagI;
            Module module = get(moduleTag.getString("name", ""));
            if (module != null) module.fromTag(moduleTag);
        }

        return this;
    }

    // INIT MODULES

    public void add(Module module) {
        // Check if the module's category is registered
        if (!CATEGORIES.contains(module.category)) {
            throw new RuntimeException("Modules.addModule - Module's category was not registered.");
        }

        // Remove the previous module with the same name
        AtomicReference<Module> removedModule = new AtomicReference<>();
        if (moduleInstances.values().removeIf(module1 -> {
            if (module1.name.equals(module.name)) {
                removedModule.set(module1);
                module1.settings.unregisterColorSettings();

                return true;
            }

            return false;
        })) {
            getGroup(removedModule.get().category).remove(removedModule.get());
        }

        // Add the module
        moduleInstances.put(module.getClass(), module);
        modules.add(module);
        getGroup(module.category).add(module);

        // Register color settings for the module
        module.settings.registerColorSettings(module);
    }

    public void addDisabled(Module module) {
        module.disabled = true;
        add(module);
    }

    public void addCheat(Module module) {
        module.isCheat = true;
        add(module);
    }

    private void initPlayer() {
        add(new AutoReplenish());
        add(new AutoTool());
        add(new FakePlayer());
        add(new NoInteract());
        add(new NoMiningTrace());
        add(new Portals());
        add(new Rotation());
    }

    private void initRender() {
        add(new BetterTab());
        add(new BetterTooltips());
        addCheat(new BlockESP());
        add(new BlockSelection());
        add(new BossStack());
        add(new Breadcrumbs());
        add(new BreakIndicators());
        addCheat(new CameraTweaks());
        addCheat(new Freecam());
        add(new FreeLook());
        add(new Fullbright());
        add(new HandView());
        add(new ItemHighlight());
        add(new LightOverlay());
        add(new Marker());
        add(new NoRender());
        addCheat(new Tracers());
        add(new Trail());
        add(new UnfocusedCPU());
        add(new WaypointsModule());
        addCheat(new Xray());
        add(new Zoom());
        add(new Blur());
        add(new BetterTab());
    }

    private void initMisc() {
        add(new AutoClicker());
        add(new AutoReconnect());
        add(new AutoRespawn());
        add(new BetterChat());
        add(new DiscordPresence());
        add(new SoundBlocker());
        add(new InventoryTweaks());
        add(new Ambience());
    }

    private void blacklist() {
        final File blacklistFile = new File(MeteorClient.FOLDER.getPath() + "/blacklist.txt");
        if (!blacklistFile.exists()) {
            return;
        }
        List<String> lines;
        try {
            lines = Files.readAllLines(blacklistFile.toPath());
        } catch (IOException exception) {
            return;
        }
        lines.forEach(line -> {
            if (line.trim().startsWith("#")) {
                return;
            }

            boolean isCheat = false;
            String name = line;
            if (line.endsWith("*")) {
                isCheat = true;
                name = line.substring(0, line.length() - 2);
            }
            Module module = get(name);
            if (module == null) {
                return;
            }
            if (isCheat) {
                module.isCheat = true;
            } else {
                module.disabled = true;
            }
        });
    }
}
