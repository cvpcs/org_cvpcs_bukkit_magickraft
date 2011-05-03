package org.cvpcs.bukkit.magickraft.runeset.runecraft;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.Block;
import org.bukkit.Material;

import org.cvpcs.bukkit.magickraft.Magickraft;
import org.cvpcs.bukkit.magickraft.Rune;
import org.cvpcs.bukkit.magickraft.RuneSet;
import org.cvpcs.bukkit.magickraft.runestruct.IRuneNode;
import org.cvpcs.bukkit.magickraft.runestruct.RNAnything;
import org.cvpcs.bukkit.magickraft.runestruct.RNMaterial;
import org.cvpcs.bukkit.magickraft.runestruct.RuneStructure;
import org.cvpcs.bukkit.magickraft.runestruct.TierUtility;

public class OracleRune extends Rune {

    public static final String NAME = "oracle";

    public OracleRune(Magickraft plugin, RuneSet set) {
        super(plugin, set, new RuneStructure(3, 3)
                .setRuneMap(new IRuneNode[][]{
                        {RNMaterial.getInstance(Material.REDSTONE_WIRE), RNMaterial.getInstance(Material.REDSTONE_WIRE), RNMaterial.getInstance(Material.REDSTONE_WIRE)},
                        {RNMaterial.getInstance(Material.REDSTONE_WIRE), RNAnything.getInstance()                      , RNMaterial.getInstance(Material.REDSTONE_WIRE)},
                        {RNMaterial.getInstance(Material.REDSTONE_WIRE), RNMaterial.getInstance(Material.REDSTONE_WIRE), RNMaterial.getInstance(Material.REDSTONE_WIRE)},
                    })
                .setRuneConsumptionMap(new int[][] {
                        {Material.AIR.getId(), Material.AIR.getId(), Material.AIR.getId()},
                        {Material.AIR.getId(), -1                  , Material.AIR.getId()},
                        {Material.AIR.getId(), Material.AIR.getId(), Material.AIR.getId()},
                    }));
    }

    public String getName() { return NAME; }

    @Override
    public boolean onRuneInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (tryRune(block)) {
            int blockTier = TierUtility.getTier(block);

            if(blockTier > -1) {
                event.getPlayer().sendMessage("This block is tier " + blockTier + ".");
            } else {
                event.getPlayer().sendMessage("This block cannot be used in runes.");
            }

            return true;
        }

        return false;
    }
}
