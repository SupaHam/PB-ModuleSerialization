package com.supaham.sertest.modules.framework;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
import pluginbase.config.SerializationRegistrar;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is a {@link Module} manager, used to handle Module instance creation, etc.
 *
 * @see Module
 */
@Getter
public class ModuleManager {

    private final BiMap<Class<? extends Module>, Class<? extends ModuleData>> dataClasses = HashBiMap.create();

    /**
     * Registers a {@link Module} class to this manager. This method puts the {@code moduleClass} as well as it's {@link ModuleDataClass} in
     * {@link ModuleManager#getDataClasses()}.
     *
     * @param moduleClass module class to register
     * @return whether or not the method registered the class, the only case where it would return false is if the class is already registered
     */
    public boolean register(@Nonnull Class<? extends Module> moduleClass) {
        if (getDataClasses().containsKey(moduleClass)) {
            return false;
        }
        Preconditions.checkNotNull(moduleClass, "module class to register cannot be null.");
        ModuleDataClass annotation = moduleClass.getDeclaredAnnotation(ModuleDataClass.class);
        Preconditions.checkNotNull(annotation, moduleClass.getName() + " must be annotated with @ModuleDataClass.");
        Class<? extends ModuleData> dataClass = annotation.value();
        Preconditions.checkNotNull(dataClass, moduleClass.getName() + " has a null data class value in @ModuleDataClass.");

        dataClasses.put(moduleClass, dataClass);
        SerializationRegistrar.registerClass(dataClass);
        return true;
    }

    /**
     * Gets the owner {@link Module} class of a {@link ModuleData} from this manager assuming it was registered via {@link #register(Class)}.
     *
     * @param dataClass data class to get parent module for
     * @return the module class, null if the class was never registered to this manager
     */
    public Class<? extends Module> getModuleClassByData(@Nonnull Class<? extends ModuleData> dataClass) {
        Preconditions.checkNotNull(dataClass, "data class cannot be null.");
        return dataClasses.inverse().get(dataClass);
    }

    // Dummy test method, this is meant to be handled in a Session class, for example, a Game (or manager) class.
    public Set<Module> createModules(@Nonnull Collection<ModuleData> datas) {
        Preconditions.checkNotNull(datas, "Collection of ModuleData cannot be null.");
        return datas.stream().filter(data -> data != null).map(data -> {
            try {
                //noinspection RedundantCast - The following cast is important in order to compile since newInstance returns an object.
                return ((Module) getModuleClassByData(data.getClass()).getDeclaredConstructor(data.getClass()).newInstance(data));
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(module -> module != null).collect(Collectors.toSet());
    }

}
