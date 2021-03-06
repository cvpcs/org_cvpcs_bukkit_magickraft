package org.cvpcs.bukkit.magickraft.runeset.runecraft;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.Location;

import org.cvpcs.bukkit.magickraft.Magickraft;
import org.cvpcs.bukkit.magickraft.RedstoneUtility;
import org.cvpcs.bukkit.magickraft.Rune;
import org.cvpcs.bukkit.magickraft.RuneSet;
import org.cvpcs.bukkit.magickraft.runestruct.IRuneNode;
import org.cvpcs.bukkit.magickraft.runestruct.RNAnything;
import org.cvpcs.bukkit.magickraft.runestruct.RNComplexAnd;
import org.cvpcs.bukkit.magickraft.runestruct.RNMaterial;
import org.cvpcs.bukkit.magickraft.runestruct.RNMaterialGroup;
import org.cvpcs.bukkit.magickraft.runestruct.RNTier;
import org.cvpcs.bukkit.magickraft.runestruct.RuneStructure;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;

public class DoorRune extends Rune {

    public static final String NAME = "door";

    public DoorRune(Magickraft plugin, RuneSet set) {
        super(plugin, set, new RuneStructure(3, 1, 3)
                .setClickHeight(2)
                .setRuneMap(new IRuneNode[][][]{
                        {
                            {
                                RNComplexAnd.getInstance(
                                        RNTier.getInstance(),
                                        RNMaterialGroup.getInstance(0)),

                                RNMaterial.getInstance(Material.IRON_ORE),

                                RNComplexAnd.getInstance(
                                        RNTier.getInstance(),
                                        RNMaterialGroup.getInstance(0)),
                            },
                        },
                        {
                            {
                                RNMaterial.getInstance(Material.IRON_ORE),

                                RNAnything.getInstance(),

                                RNMaterial.getInstance(Material.IRON_ORE),
                            },
                        },
                        {
                            {
                                RNComplexAnd.getInstance(
                                        RNTier.getInstance(),
                                        RNMaterialGroup.getInstance(0)),

                                RNComplexAnd.getInstance(
                                        RNTier.getInstance(),
                                        RNMaterialGroup.getInstance(0)),

                                RNComplexAnd.getInstance(
                                        RNTier.getInstance(),
                                        RNMaterialGroup.getInstance(0)),
                            },
                        }
                       })
                .setRuneConsumptionMap(new int[][][]{
                        {
                            {Material.AIR.getId(), -1, Material.AIR.getId()},
                        },
                        {
                            {Material.AIR.getId(), -1, Material.AIR.getId()},
                        },
                        {
                            {Material.AIR.getId(), -1, Material.AIR.getId()},
                        }
                    }));
    }

    public String getName() { return NAME; }

    @Override
    public boolean onRuneInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        // look for a door
        Door door = getDoor(block.getLocation());

        if(door == null) {
            // no door, is the rune valid?
            if (tryRune(block)) {

                // time to create a door!
                door = new Door();
                door.key = block.getRelative(BlockFace.DOWN, 1).getTypeId();
                if(door.key <= 0) {
                    // invalidate the key for air
                    door.key = -1;
                }
                door.loc = block.getLocation();
                // SAVE!
                saveDoor(door);

                // now we reset the door
                block.getRelative(BlockFace.DOWN, 1).setType(block.getType());
                block.getRelative(BlockFace.DOWN, 2).setType(block.getType());

                event.getPlayer().sendMessage("Door accepted, fill in the side holes to conceal");

                return true;
            }
        }

        return false;
    }

    @Override
    public boolean onRuneUseInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        // look for a door
        Door door = getDoor(block.getLocation());

        if(door != null) {
            if(block.getRelative(BlockFace.DOWN, 1).getTypeId() == Material.AIR.getId()) {
                // door is showing, hide that bitch!
                block.getRelative(BlockFace.DOWN, 1).setType(block.getType());
                block.getRelative(BlockFace.DOWN, 2).setType(block.getType());
            } else {
                // door is hiding, show that bitch!
                if(door.key > 0) {
                    // we have a key, make sure the key is present
                    boolean keyFound = false;
                    Block curBlock;
                    if((curBlock = block.getRelative(BlockFace.DOWN, 2).getRelative(BlockFace.NORTH)).getTypeId() == door.key) {
                        curBlock.setType(Material.AIR);
                        keyFound = true;
                    } else if((curBlock = block.getRelative(BlockFace.DOWN, 2).getRelative(BlockFace.EAST)).getTypeId() == door.key) {
                        curBlock.setType(Material.AIR);
                        keyFound = true;
                    } else if((curBlock = block.getRelative(BlockFace.DOWN, 2).getRelative(BlockFace.SOUTH)).getTypeId() == door.key) {
                        curBlock.setType(Material.AIR);
                        keyFound = true;
                    } else if((curBlock = block.getRelative(BlockFace.DOWN, 2).getRelative(BlockFace.WEST)).getTypeId() == door.key) {
                        curBlock.setType(Material.AIR);
                        keyFound = true;
                    }

                    if(!keyFound) {
                        return false;
                    }
                }

                block.getRelative(BlockFace.DOWN, 1).setType(Material.AIR);
                block.getRelative(BlockFace.DOWN, 2).setType(Material.AIR);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean onRuneUseRedstone(BlockRedstoneEvent event) {
        for(Block block : RedstoneUtility.getAffectedBlocks(event)) {
            Door door = getDoor(block.getLocation());

            // only use redstone if the door exists and doesn't have a key
            if(door != null && door.key < 0) {
                // we have a door! change its status based on redstone event
                if(event.getNewCurrent() > 0) {
                    // powered, open the door
                    block.getRelative(BlockFace.DOWN, 1).setType(Material.AIR);
                    block.getRelative(BlockFace.DOWN, 2).setType(Material.AIR);
                } else {
                    // unpowered, close the door
                    block.getRelative(BlockFace.DOWN, 1).setType(block.getType());
                    block.getRelative(BlockFace.DOWN, 2).setType(block.getType());
                }
            }
        }

        // never report if we used redstone, as someone else might be using this
        // redstone event, since we are technically watching the blocks AROUND
        // the redstone, not the redstone itself
        return false;
    }

    @Override
    public boolean onRuneBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Door door = null;

        for(int i = 0; i < 3 && door == null; i++) {
            block = event.getBlock().getRelative(BlockFace.UP, i);
            door = getDoor(block.getLocation());
        }

        if(door != null) {
            deleteDoor(door);
            event.getPlayer().sendMessage("Door rune destroyed");

            // players don't get minerals back for this
            event.setCancelled(true);

            // wipe out the door
            block.setType(Material.AIR);
            block.getRelative(BlockFace.DOWN, 1).setType(Material.AIR);
            block.getRelative(BlockFace.DOWN, 2).setType(Material.AIR);
            return true;
        }

        return false;
    }

    private class Door {
        public Location loc;
        public int key;
    }

    private Door getDoor(Location loc) {
        Connection sqlConn = null;
        Door d = null;
        File dbfile = new File(getRuneSet().getDataFolder(), NAME + ".db");
        try {
            sqlConn = DriverManager.getConnection("jdbc:sqlite:" + dbfile.getAbsolutePath());

            PreparedStatement stmt = sqlConn.prepareStatement(
                    "select * from doors where w = ? and x = ? and y = ? and z = ?");
            stmt.setString(1, loc.getWorld().getUID().toString());
            stmt.setInt(2, loc.getBlockX());
            stmt.setInt(3, loc.getBlockY());
            stmt.setInt(4, loc.getBlockZ());

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                d = new Door();
                d.loc = loc;
                d.key = rs.getInt("key");
            }
            rs.close();
        } catch(Exception e) {
            d = null;
        } finally {
            if(sqlConn != null) {
                try {
                    sqlConn.close();
                } catch(Exception e) { }
            }
        }

        return d;
    }

    private void saveDoor(Door d) {
        Connection sqlConn = null;
        File dbfile = new File(getRuneSet().getDataFolder(), NAME + ".db");
        try {
            sqlConn = DriverManager.getConnection("jdbc:sqlite:" + dbfile.getAbsolutePath());

            Statement stmt = sqlConn.createStatement();
            stmt.executeUpdate("create table if not exists doors ("
                    + "w TEXT NOT NULL, "
                    + "x INTEGER NOT NULL, "
                    + "y INTEGER NOT NULL, "
                    + "z INTEGER NOT NULL, "
                    + "key INTEGER, "
                    + "PRIMARY KEY (w, x, y, z));");

            PreparedStatement pstmt = sqlConn.prepareStatement(
                    "insert into doors values (?, ?, ?, ?, ?);");
            pstmt.setString(1, d.loc.getWorld().getUID().toString());
            pstmt.setInt(2, d.loc.getBlockX());
            pstmt.setInt(3, d.loc.getBlockY());
            pstmt.setInt(4, d.loc.getBlockZ());
            pstmt.setInt(5, d.key);
            pstmt.executeUpdate();
        } catch(Exception e) {
        } finally {
            if(sqlConn != null) {
                try {
                    sqlConn.close();
                } catch(Exception e) { }
            }
        }
    }

    private void deleteDoor(Door d) {
        Connection sqlConn = null;
        File dbfile = new File(getRuneSet().getDataFolder(), NAME + ".db");
        try {
            sqlConn = DriverManager.getConnection("jdbc:sqlite:" + dbfile.getAbsolutePath());

            PreparedStatement pstmt = sqlConn.prepareStatement(
                    "delete from doors where w = ? and x = ? and y = ? and z = ?");
            pstmt.setString(1, d.loc.getWorld().getUID().toString());
            pstmt.setInt(2, d.loc.getBlockX());
            pstmt.setInt(3, d.loc.getBlockY());
            pstmt.setInt(4, d.loc.getBlockZ());
            pstmt.executeUpdate();
        } catch(Exception e) {
        } finally {
            if(sqlConn != null) {
                try {
                    sqlConn.close();
                } catch(Exception e) { }
            }
        }
    }
}
