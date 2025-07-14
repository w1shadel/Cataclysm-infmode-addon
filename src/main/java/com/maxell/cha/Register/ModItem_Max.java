package com.maxell.cha.Register;

import com.maxell.cha.CHA;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItem_Max {
    public static final DeferredRegister<Item> ITEMS;
    public static final RegistryObject<Item> PARRY_CORE;
    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CHA.MODID);
        PARRY_CORE = ITEMS.register("parry_core", () -> new Item(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));
    }
}
