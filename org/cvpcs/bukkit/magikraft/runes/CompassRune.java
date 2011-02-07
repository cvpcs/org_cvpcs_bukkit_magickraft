/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cvpcs.bukkit.magikraft.runes;
import org.bukkit.event.block.BlockRightClickEvent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;

import org.cvpcs.bukkit.magikraft.Magikraft;
import org.cvpcs.bukkit.magikraft.Rune;
import org.cvpcs.bukkit.magikraft.runestruct.IRuneNode;
import org.cvpcs.bukkit.magikraft.runestruct.RNMaterialGroup;
import org.cvpcs.bukkit.magikraft.runestruct.RuneStructure;
import org.cvpcs.bukkit.magikraft.runestruct.RNMaterial;
import org.cvpcs.bukkit.magikraft.runestruct.RNTier;
import org.cvpcs.bukkit.magikraft.runestruct.RNComplex;

public class CompassRune extends Rune {
	
	public static final String NAME = "compass";
	
    public CompassRune(Magikraft plugin) {
        super(plugin, new RuneStructure(3, 3, new IRuneNode[][]{
        		{
        			RNComplex.getInstance(
        					RNTier.getInstance(1),
        					RNMaterialGroup.getInstance(0)),
        				
        			RNMaterial.getInstance(Material.AIR),
        			
        			RNComplex.getInstance(
        					RNTier.getInstance(1),
        					RNMaterialGroup.getInstance(0)),
        		},
        		{
    				RNMaterial.getInstance(Material.AIR),

        			RNComplex.getInstance(
        					RNTier.getInstance(1),
        					RNMaterialGroup.getInstance(0)),
    			
    				RNMaterial.getInstance(Material.AIR)
        		},
        		{
        			RNComplex.getInstance(
        					RNTier.getInstance(1),
        					RNMaterialGroup.getInstance(0)),
        				
        			RNMaterial.getInstance(Material.AIR),
        			
        			RNComplex.getInstance(
        					RNTier.getInstance(1),
        					RNMaterialGroup.getInstance(0)),
        		},
        }));
    }
    
    @Override
    public boolean runRuneRightClick(BlockRightClickEvent event) {
        Block block = event.getBlock();
        
        if (super.mStructure.isRune(block) &&
        		(event.getItemInHand().getType()==Material.AIR || !event.getItemInHand().getType().isBlock())) {
        	block.getFace(BlockFace.NORTH).setType(block.getType());
            block.setType(Material.AIR);
            block.getFace(BlockFace.EAST).setType(block.getFace(BlockFace.NORTH_EAST).getType());
            block.getFace(BlockFace.NORTH_EAST).setType(Material.AIR);
            block.getFace(BlockFace.WEST).setType(block.getFace(BlockFace.NORTH_WEST).getType());
            block.getFace(BlockFace.NORTH_WEST).setType(Material.AIR);
            return true;
        }
        
        return false;
    }
}
