package com.potatomod;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * PotatoMod - lightweight optimization mod for Minecraft 1.7.10 (Forge).
 * Works client-only, server-only, or both (acceptableRemoteVersions = "*").
 */
@Mod(modid = PotatoMod.MODID, name = "PotatoMod", version = "1.0.0", acceptableRemoteVersions = "*")
public class PotatoMod {

    public static final String MODID = "potatomod";

    @SidedProxy(clientSide = "com.potatomod.ClientProxy", serverSide = "com.potatomod.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Config.load(event.getSuggestedConfigurationFile());

        CommonHandler handler = new CommonHandler();
        MinecraftForge.EVENT_BUS.register(handler);
        FMLCommonHandler.instance().bus().register(handler);

        proxy.init();
    }
}
