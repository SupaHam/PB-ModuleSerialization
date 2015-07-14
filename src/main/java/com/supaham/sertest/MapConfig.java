package com.supaham.sertest;

import com.google.common.base.Preconditions;
import com.supaham.sertest.modules.framework.ModuleData;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pluginbase.config.annotation.SerializableAs;
import pluginbase.config.annotation.SerializeWith;
import pluginbase.config.serializers.Serializer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@SerializableAs("MapConfig")
public class MapConfig {

    @SerializeWith(ModuleSerializer.class)
    private Set<ModuleData> modules = new HashSet<>();

    public static final class ModuleSerializer implements Serializer<Collection<ModuleData>> {
        @Nullable
        @Override
        public Object serialize(Collection<ModuleData> moduleDatas) throws IllegalArgumentException {
            return moduleDatas == null ? null : moduleDatas.stream().collect(Collectors.toList());
        }

        @Nullable
        @Override
        public Set<ModuleData> deserialize(@Nullable Object o, @NotNull Class aClass) throws IllegalArgumentException {
            if (o == null) {
                return null;
            }
            Preconditions.checkArgument(o instanceof List, "Serialized object must be a list of modules");
            //noinspection unchecked
            return new HashSet<>((List) o);
        }
    }
}
