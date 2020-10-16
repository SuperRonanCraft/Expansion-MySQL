package me.SuperRonanCraft.ExpansionMySQL;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.clip.placeholderapi.PlaceholderAPIPlugin;

public class Config {

	private final Main ex;

	private final PlaceholderAPIPlugin plugin;

	private FileConfiguration config;

	private File file;

	public Config(Main ex) {
		this.ex = ex;
		plugin = ex.getPlaceholderAPI();
		reload();
	}

	public void reload() {
		if (file == null)
			file = new File(
					plugin.getDataFolder() + File.separator + "expansions" + File.separator + ex.getIdentifier(),
					"config.yml");
		if (!file.exists())
			plugin.saveResource("config.yml", false);
		config = YamlConfiguration.loadConfiguration(file);
		config.options().header("");
		if (config.getKeys(false) == null || config.getKeys(false).isEmpty())
			config.set("option", "value");
		save();
	}

	public FileConfiguration load() {
		if (config == null)
			reload();
		return config;
	}

	public void save() {
		if ((config == null) || (file == null))
			return;
		try {
			load().save(file);
		} catch (IOException ex) {
			plugin.getLogger().log(Level.SEVERE, "Could not save to " + file, ex);
		}
	}
}