package net.gogo.simulatedextra.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class FrequencyCounter extends SavedData {

    private static final String NAME = "global_frequency_counter";
    private int counter = 0;

    public static FrequencyCounter get(Level level) {
        return ((ServerLevel) level).getDataStorage()
                .computeIfAbsent(new SavedData.Factory<>(
                        FrequencyCounter::new,
                        FrequencyCounter::load
                ), NAME);
    }

    public int next() {
        counter++;
        setDirty();
        return counter;
    }

    // serialization
    public static FrequencyCounter load(CompoundTag tag, HolderLookup.Provider registries) {
        FrequencyCounter data = new FrequencyCounter();
        data.counter = tag.getInt("Counter");
        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        tag.putInt("Counter", counter);
        return tag;
    }
}