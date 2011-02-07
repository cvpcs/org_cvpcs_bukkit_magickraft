package org.cvpcs.bukkit.magickraft.runes;

import org.bukkit.event.block.BlockRightClickEvent;
import org.bukkit.block.Block;
import org.bukkit.Material;

import org.cvpcs.bukkit.magickraft.Magickraft;
import org.cvpcs.bukkit.magickraft.Rune;
import org.cvpcs.bukkit.magickraft.runestruct.IRuneNode;
import org.cvpcs.bukkit.magickraft.runestruct.RNAnything;
import org.cvpcs.bukkit.magickraft.runestruct.RNMaterial;
import org.cvpcs.bukkit.magickraft.runestruct.RNTier;
import org.cvpcs.bukkit.magickraft.runestruct.RuneStructure;

public class OracleRune extends Rune {
	
	public static final String NAME = "oracle";
	
    public OracleRune(Magickraft plugin) {
        super(plugin, new RuneStructure(3, 3, new IRuneNode[][]{
    			{RNMaterial.getInstance(Material.REDSTONE_WIRE), RNMaterial.getInstance(Material.REDSTONE_WIRE), RNMaterial.getInstance(Material.REDSTONE_WIRE)},
    			{RNMaterial.getInstance(Material.REDSTONE_WIRE), RNAnything.getInstance()                      , RNMaterial.getInstance(Material.REDSTONE_WIRE)},
    			{RNMaterial.getInstance(Material.REDSTONE_WIRE), RNMaterial.getInstance(Material.REDSTONE_WIRE), RNMaterial.getInstance(Material.REDSTONE_WIRE)},
    	}, new int[][] {
        		{Material.AIR.getId(), Material.AIR.getId(), Material.AIR.getId()},
        		{Material.AIR.getId(), -1                  , Material.AIR.getId()},
        		{Material.AIR.getId(), Material.AIR.getId(), Material.AIR.getId()},
        }));
    }
    
    @Override
    public boolean runRuneRightClick(BlockRightClickEvent event) {
        Block block = event.getBlock();
        
        if (super.mStructure.isRune(block)) {
        	int blockTier = RNTier.getTier(block);
        	
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