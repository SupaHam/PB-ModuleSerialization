package com.supaham.sertest.modules.framework;

import com.supaham.sertest.SerTestPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * Module framework base class. Upon extending this class, you will need to annotate the extension with {@link ModuleDataClass} to tell the framework
 * how to load this module from the config file. Immediately after implementation, ensure that the class is also registered to the
 * {@link ModuleManager} using {@link ModuleManager#register(Class)}.
 *
 * @see #Module(ModuleData)
 * @see #load()
 * @see #unload()
 */
public abstract class Module implements Listener {

    /**
     * This constructor is used to force creation of this constructor in each Module class. This constructor doesn't actually do anything with the
     * data parameter.
     */
    public Module(@NotNull ModuleData data) {
    }

    /**
     * Called when this module is being loaded, which is when the session is being loaded. At that point in time, only the map has been
     * loaded, and players are not setup. This shouldn't be confused with {@link #enable()}, which is called when the session starts.
     * <p/>
     * <b>Note: This method registers this module as a {@link Listener}</b>
     */
    public void load() {
    }

    /**
     * Called when this module is being unloaded, which is when the session is being unloaded. At that point in time, the map is still loaded, but
     * the players are no longer in the session.
     * <p/>
     * <b>Note: This method unregisters this module as a {@link Listener}</b>
     */
    public void unload() {

    }

    /**
     * Called when this module is being enabled, which is when the session is being enabled. At that point in time, this module has been loaded and
     * will immediately begin to setup online players.
     */
    public void enable() {
        Bukkit.getPluginManager().registerEvents(this, SerTestPlugin.get());
    }

    /**
     * Called when this module is being enabled, which is when the session is being disabled. At that point in time, this module still has not been
     * unloaded.
     */
    public void disable() {
        HandlerList.unregisterAll(this);
    }
}
