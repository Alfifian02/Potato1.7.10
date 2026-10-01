package com.potatomod;

import java.io.File;
import net.minecraftforge.common.config.Configuration;

public class Config {

    // ---- Server / common ----
    public static int itemLifespanTicks;
    public static int maxItemsPerWorld;
    public static int maxXpOrbsPerWorld;
    public static int cleanupIntervalTicks;

    // ---- Client ----
    public static boolean applyClientSettings;
    public static int maxRenderDistance;
    public static boolean fastGraphics;
    public static boolean minimalParticles;
    public static int fpsLimit;
    public static int entityRenderDistance;
    public static boolean disableWeather;
    public static boolean flatSky;

    public static void load(File file) {
        Configuration cfg = new Configuration(file);
        cfg.load();

        itemLifespanTicks = cfg.getInt("itemLifespanTicks", "server", 1200, 100, 6000,
                "Lifetime of dropped items in ticks (20 ticks = 1 s). Vanilla is 6000.");
        maxItemsPerWorld = cfg.getInt("maxItemsPerWorld", "server", 150, 10, 5000,
                "Maximum dropped items per dimension. The oldest ones are removed first.");
        maxXpOrbsPerWorld = cfg.getInt("maxXpOrbsPerWorld", "server", 40, 5, 2000,
                "Maximum XP orbs per dimension. Extra orbs are merged (no XP is lost).");
        cleanupIntervalTicks = cfg.getInt("cleanupIntervalTicks", "server", 100, 20, 1200,
                "How often (in ticks) the limits above are enforced.");

        applyClientSettings = cfg.getBoolean("applyClientSettings", "client", true,
                "Apply the low-end settings below once when the game starts.");
        maxRenderDistance = cfg.getInt("maxRenderDistance", "client", 4, 2, 32,
                "Render distance (chunks) used when applyClientSettings is on. Try 3 if still slow.");
        fastGraphics = cfg.getBoolean("fastGraphics", "client", true,
                "Fast graphics, smooth lighting off, clouds off, view bobbing off, Advanced OpenGL off.");
        minimalParticles = cfg.getBoolean("minimalParticles", "client", true,
                "Set particles to Minimal.");
        fpsLimit = cfg.getInt("fpsLimit", "client", 0, 0, 260,
                "FPS cap applied at startup. 0 = leave as is. 30 reduces heat and throttling.");
        entityRenderDistance = cfg.getInt("entityRenderDistance", "client", 32, 0, 256,
                "Mobs farther than about this many blocks are not rendered (big mobs get a bit more). 0 = disabled.");
        disableWeather = cfg.getBoolean("disableWeather", "client", true,
                "Hide rain/snow/thunder rendering (client-side only, gameplay is unchanged).");
        flatSky = cfg.getBoolean("flatSky", "client", true,
                "Skip drawing sun, moon and stars (sky becomes a flat fog color). Saves draw calls.");

        if (cfg.hasChanged()) {
            cfg.save();
        }
    }
}
