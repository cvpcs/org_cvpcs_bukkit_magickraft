package org.cvpcs.bukkit.magickraft;

import org.bukkit.Server;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;

import org.cvpcs.bukkit.magickraft.runes.MagickBeaconRune;
import org.cvpcs.bukkit.magickraft.runes.ChronoTriggerRune;
import org.cvpcs.bukkit.magickraft.runes.CompassRune;
import org.cvpcs.bukkit.magickraft.runes.DoorRune;
import org.cvpcs.bukkit.magickraft.runes.MineshaftRune;
import org.cvpcs.bukkit.magickraft.runes.OracleRune;
import org.cvpcs.bukkit.magickraft.runes.WaypointRune;

import java.io.File;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Magickraft extends JavaPlugin {
	// a list of all of our runes
	public static final HashMap<String, Rune> RUNES = new HashMap<String, Rune>();

	public static MagickraftConfig CONFIG = null;

    private RuneRunner mRuneRunner;

    public Magickraft(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);
        CONFIG = new MagickraftConfig(this);

        RUNES.clear();
        RUNES.put(OracleRune.NAME, new OracleRune(this));
        RUNES.put(CompassRune.NAME, new CompassRune(this));
        RUNES.put(ChronoTriggerRune.NAME, new ChronoTriggerRune(this));
        RUNES.put(DoorRune.NAME, new DoorRune(this));
        RUNES.put(MagickBeaconRune.NAME, new MagickBeaconRune(this));
        RUNES.put(WaypointRune.NAME, new WaypointRune(this));
        RUNES.put(MineshaftRune.NAME, new MineshaftRune(this));

        mRuneRunner = new RuneRunner();
    }

    public void onDisable() {
    	mRuneRunner.unloadRunes();

        Log("unloaded!");
    }

    public void onEnable() {
    	for(Map.Entry<String, Rune> entry : RUNES.entrySet()) {
    		entry.getValue().setEnabled(CONFIG.getRuneBoolean(entry.getKey(), MagickraftConfig.RUNE_ENABLED_KEY));
    	}

        getServer().getPluginManager().registerEvent(Event.Type.BLOCK_RIGHTCLICKED, mRuneRunner, Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Event.Type.BLOCK_DAMAGED, mRuneRunner, Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Event.Type.REDSTONE_CHANGE, mRuneRunner, Priority.Normal, this);

        mRuneRunner.loadRunes(RUNES.values());

        Log("loaded!");
    }

    public static void Log(String s) {
    	System.out.println("[Magickraft]: " + s);
    }
}


