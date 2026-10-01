package com.potatomod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

/** Server-side limits (also runs inside singleplayer's integrated server). */
public class CommonHandler {

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {
        if (event.world.isRemote) return;
        if (event.entity instanceof EntityItem) {
            EntityItem item = (EntityItem) event.entity;
            if (item.lifespan > Config.itemLifespanTicks) {
                item.lifespan = Config.itemLifespanTicks;
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.side != Side.SERVER) return;
        if (event.world.getTotalWorldTime() % Config.cleanupIntervalTicks != 0) return;

        List<EntityItem> items = new ArrayList<EntityItem>();
        List<EntityXPOrb> orbs = new ArrayList<EntityXPOrb>();

        for (Object o : new ArrayList(event.world.loadedEntityList)) {
            Entity e = (Entity) o;
            if (e.isDead) continue;
            if (e instanceof EntityItem) {
                items.add((EntityItem) e);
            } else if (e instanceof EntityXPOrb) {
                orbs.add((EntityXPOrb) e);
            }
        }

        // Remove the oldest dropped items beyond the cap.
        if (items.size() > Config.maxItemsPerWorld) {
            Collections.sort(items, new Comparator<EntityItem>() {
                @Override
                public int compare(EntityItem a, EntityItem b) {
                    return b.age - a.age; // oldest first
                }
            });
            int excess = items.size() - Config.maxItemsPerWorld;
            for (int i = 0; i < excess; i++) {
                items.get(i).setDead();
            }
        }

        // Merge excess XP orbs into one so no XP is lost.
        if (orbs.size() > Config.maxXpOrbsPerWorld) {
            EntityXPOrb sink = orbs.get(0);
            for (int i = Config.maxXpOrbsPerWorld; i < orbs.size(); i++) {
                EntityXPOrb orb = orbs.get(i);
                sink.xpValue += orb.xpValue;
                orb.setDead();
            }
        }
    }
}
