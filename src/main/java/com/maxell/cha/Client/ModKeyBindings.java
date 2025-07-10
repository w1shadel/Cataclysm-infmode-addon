package com.maxell.cha.Client;

import com.maxell.cha.CHA;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
@OnlyIn(Dist.CLIENT)
public class ModKeyBindings {
    public static final KeyMapping RAGE_MODE = new KeyMapping(
            "key.cha.rage",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_V,
            "key.categories.cha"
    );
    public static final KeyMapping Adrenaline_MODE = new KeyMapping(
            "key.cha.adrenaline",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.categories.cha"
    );
    @Mod.EventBusSubscriber(modid = CHA.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public class ModClientEvents {

        @SubscribeEvent
        public static void onRegisterKeyMappings_rage(RegisterKeyMappingsEvent event) {
            event.register(ModKeyBindings.RAGE_MODE);
        }

        @SubscribeEvent
        public static void onRegisterKeyMappings_adrenaline(RegisterKeyMappingsEvent event) {
            event.register(ModKeyBindings.Adrenaline_MODE);
        }
    }

}
