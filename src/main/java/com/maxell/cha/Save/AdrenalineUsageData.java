package com.maxell.cha.Save;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AdrenalineUsageData extends SavedData {
    private final Set<UUID> usedPlayers = new HashSet<>();

    public boolean hasUsed(UUID id) {
        return usedPlayers.contains(id);
    }

    public void markUsed(UUID id) {
        usedPlayers.add(id);
        setDirty();
    }
    public static AdrenalineUsageData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                AdrenalineUsageData::load,
                AdrenalineUsageData::new,
                "adrenaline_usage"
        );
    }




    public static AdrenalineUsageData load(CompoundTag tag) {
        AdrenalineUsageData data = new AdrenalineUsageData();
        ListTag list = tag.getList("usedPlayers", Tag.TAG_STRING);
        for (Tag t : list) data.usedPlayers.add(UUID.fromString(t.getAsString()));
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (UUID id : usedPlayers) list.add(StringTag.valueOf(id.toString()));
        tag.put("usedPlayers", list);
        return tag;
    }
}
