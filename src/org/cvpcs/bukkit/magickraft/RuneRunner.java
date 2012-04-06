package org.cvpcs.bukkit.magickraft;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RuneRunner implements Listener {

    private List<Rune> mRunes;

    public RuneRunner() {
        unloadRunes();
    }

    public RuneRunner(Collection<Rune> runes) {
        loadRunes(runes);
    }

    public void loadRunes(Collection<Rune> runes) {
        mRunes = new ArrayList<Rune>(runes);
        Collections.sort(mRunes);
    }

    public void unloadRunes() {
        mRunes = new ArrayList<Rune>();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        // we don't run runes if they're holding a block besides air (we're just like that)
        if(event.getPlayer().getItemInHand().getType().isBlock() &&
                event.getPlayer().getItemInHand().getType() != Material.AIR) {
            return;
        }
        
        for(Rune rune : mRunes) {
            if (rune.getEnabled()) {
                if (rune.onRuneUseInteract(event)) {
                    return;
                }
            }
        }

        for(Rune rune : mRunes) {
            if (rune.getEnabled()) {
                if (rune.onRuneInteract(event)) {
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockDamage(BlockDamageEvent event) {
        for(Rune rune : mRunes) {
            if (rune.getEnabled()) {
                if (rune.onRuneUseDamage(event)) {
                    return;
                }
            }
        }

        for(Rune rune : mRunes) {
            if (rune.getEnabled()) {
                if (rune.onRuneDamage(event)) {
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        for(Rune rune : mRunes) {
            if (rune.getEnabled()) {
                if (rune.onRuneUseBreak(event)) {
                    return;
                }
            }
        }

        for(Rune rune : mRunes) {
            if (rune.getEnabled()) {
                if (rune.onRuneBreak(event)) {
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
        for(Rune rune : mRunes) {
            if (rune.getEnabled()) {
                if (rune.onRuneUseRedstone(event)) {
                    return;
                }
            }
        }

        for(Rune rune : mRunes) {
            if (rune.getEnabled()) {
                if (rune.onRuneRedstone(event)) {
                    return;
                }
            }
        }
    }
}
