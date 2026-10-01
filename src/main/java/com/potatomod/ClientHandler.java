package com.potatomod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

@SideOnly(Side.CLIENT)
public class ClientHandler {

    /** Draws nothing: replaces the sky renderer so sun/moon/stars cost no draw calls. */
    private static final IRenderHandler EMPTY_RENDERER = new IRenderHandler() {
        @Override
        public void render(float partialTicks, WorldClient world, Minecraft mc) {
            // intentionally empty
        }
    };

    private boolean applied = false;
    private WorldClient lastWorld = null;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();

        // Apply the low-end settings once, on the first tick (main menu), before any world loads.
        if (!applied) {
            applied = true;
            if (Config.applyClientSettings && mc.gameSettings != null) {
                applySettings(mc.gameSettings);
            }
        }

        WorldClient world = mc.theWorld;
        if (world == null) {
            lastWorld = null;
            return;
        }

        if (Config.disableWeather) {
            world.setRainStrength(0.0F);
            world.setThunderStrength(0.0F);
        }

        if (Config.flatSky && world != lastWorld) {
            lastWorld = world;
            if (world.provider.getSkyRenderer() == null) { // leave modded dimension skies alone
                world.provider.setSkyRenderer(EMPTY_RENDERER);
            }
        }
    }

    private void applySettings(GameSettings gs) {
        if (gs.renderDistanceChunks > Config.maxRenderDistance) {
            gs.renderDistanceChunks = Config.maxRenderDistance;
        }
        if (Config.fastGraphics) {
            gs.fancyGraphics = false;   // also disables entity shadows
            gs.ambientOcclusion = 0;
            gs.clouds = false;
            gs.viewBobbing = false;
            gs.advancedOpengl = false;
            gs.anaglyph = false;
        }
        if (Config.minimalParticles) {
            gs.particleSetting = 2;
        }
        if (Config.fpsLimit > 0) {
            gs.limitFramerate = Config.fpsLimit;
        }
        gs.saveOptions();
    }

    /**
     * Shrinks the render range of living entities. Vanilla culls entities by
     * (average bounding box edge * 64 * renderDistanceWeight), before any drawing happens,
     * so this is cheaper than cancelling the render call afterwards.
     */
    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {
        if (!event.world.isRemote || Config.entityRenderDistance <= 0) return;
        if (!(event.entity instanceof EntityLivingBase) || event.entity instanceof EntityPlayer) return;

        double weight = Config.entityRenderDistance / 64.0D;
        if (event.entity.renderDistanceWeight > weight) {
            event.entity.renderDistanceWeight = weight;
        }
    }
}
