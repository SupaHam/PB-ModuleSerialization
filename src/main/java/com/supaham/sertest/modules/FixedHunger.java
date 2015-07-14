package com.supaham.sertest.modules;

import com.supaham.sertest.modules.FixedHunger.FixedHungerData;
import com.supaham.sertest.modules.framework.Module;
import com.supaham.sertest.modules.framework.ModuleData;
import com.supaham.sertest.modules.framework.ModuleDataClass;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import pluginbase.config.annotation.SerializableAs;

/**
 * This is an demo module showing the functionality of this game module framework allowing for extensive configurations to gameplay.
 */
@ModuleDataClass(FixedHungerData.class)
public class FixedHunger extends Module implements Listener {

    private final int hunger;

    // This constructor is used to read the module's data class, in this case FixedHungerData.
    // The constructor is called via reflection from ModuleManager#createModules(Collection).
    public FixedHunger(FixedHungerData data) {
        super(data);
        this.hunger = data.hunger;
    }

    @EventHandler public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setFoodLevel(this.hunger);
    }

    @SerializableAs("FixedHunger")
    public static final class FixedHungerData implements ModuleData {
        private int hunger = 20; // default value of 20.

        public static FixedHungerData from(FixedHunger fixedHunger) {
            FixedHungerData data = new FixedHungerData();
            data.hunger = fixedHunger.hunger;
            return data;
        }
    }

}
