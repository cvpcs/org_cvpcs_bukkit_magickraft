package org.cvpcs.bukkit.magickraft;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class MagickraftConfig extends YamlConfiguration {

    private static final String CONFIG_FILE = "config.yaml";

    // rune key format
    private static final String RUNESETS_RUNES_KEY_FORMAT = "runesets.%s.runes.%s.%s";

    // keys global to all runes
    public static final String RUNESETS_RUNES_ENABLED_KEY = "enabled";
    
    private File m_ConfigFile;

    public MagickraftConfig(Magickraft plugin) {
        // make sure we have a configuration directory
        plugin.getDataFolder().mkdirs();

        // create the config file
    	m_ConfigFile = new File(plugin.getDataFolder(), CONFIG_FILE);

        // construct our configuration object
        load();
    }

    public boolean getRuneSetsRunesBoolean(String runeset, String rune, String key) {
        String prop = String.format(RUNESETS_RUNES_KEY_FORMAT, runeset, rune, key);
        if(!isSet(prop)) {
            // property not found, set default and save
            set(prop, true);
            save();
        }

        return getBoolean(prop, true);
    }
    
    private void load() {
    	try {
    		load(m_ConfigFile);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private void save() {
    	try {
    		save(m_ConfigFile);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}
