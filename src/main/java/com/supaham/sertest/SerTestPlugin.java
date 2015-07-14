package com.supaham.sertest;

import com.supaham.commons.bukkit.SimpleCommonPlugin;
import com.supaham.sertest.modules.FixedHunger;
import com.supaham.sertest.modules.framework.ModuleManager;
import lombok.Getter;
import pluginbase.bukkit.config.YamlConfiguration;
import pluginbase.config.SerializationRegistrar;
import pluginbase.messages.PluginBaseException;

import java.io.File;
import java.io.IOException;

@Getter
public class SerTestPlugin extends SimpleCommonPlugin<SerTestPlugin> {

    private static final String COMMAND_PREFIX = "st";
    private static SerTestPlugin instance;

    private ModuleManager moduleManager;

    static {
        SerializationRegistrar.registerClass(MapConfig.class);
    }

    public static SerTestPlugin get() {
        return instance;
    }

    public SerTestPlugin() {
        super(SerTestPlugin.class, SerTestPlugin.COMMAND_PREFIX);
        instance = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();

        this.moduleManager = new ModuleManager();
        this.moduleManager.register(FixedHunger.class);

        this.moduleManager.createModules(createDummyMapConfig().getModules()).forEach(module -> {
            module.load();
            module.enable();
        });
    }

    // This would typically be handled by a game manager loading the world map.yml.
    public MapConfig createDummyMapConfig() {
        final MapConfig defaults = new MapConfig();
        MapConfig config = defaults;
        try {
            File file = new File(getDataFolder(), "map.yml");
            file.getParentFile().mkdirs();
            file.createNewFile();
            YamlConfiguration yaml = YamlConfiguration.loadYamlConfig(file);
            yaml.options().comments(true);

            if (yaml.contains("map")) {
                config = yaml.getToObject("map", defaults);
                if (config == null) { // Should never be true
                    config = defaults;
                }
            } else {
                config = defaults;
            }
            yaml.set("map", config);
            yaml.save(file);
            getLog().fine("Loaded map config file!");
        } catch (PluginBaseException e) { // Catch errors loading the config file and exit out if found.
            getLog().severe("Error loading config file!");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        } catch (IOException e) {
            getLog().severe("There was a problem saving the config file!");
            e.printStackTrace();
        }
        return config;
    }
}
