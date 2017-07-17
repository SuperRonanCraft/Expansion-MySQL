package me.SuperRonanCraft.ExpansionMySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Main extends PlaceholderExpansion {
	Connection connection = null;
	Statement statement = null;
	List<String> identifiers;
	HashMap<String, String> host = new HashMap<String, String>();
	HashMap<String, String> database = new HashMap<String, String>();
	HashMap<String, String> username = new HashMap<String, String>();
	HashMap<String, String> password = new HashMap<String, String>();
	HashMap<String, String> query = new HashMap<String, String>();
	HashMap<String, Integer> port = new HashMap<String, Integer>();

	@Override
	public boolean register() {
		FileConfiguration config = new Config(this).load();
		ConfigurationSection section = config.getConfigurationSection("Query");
		for (String id : section.getKeys(false)) {
			ConfigurationSection sec = section.getConfigurationSection(id);
			identifiers.add(id);
			host.put(id, sec.getString("host"));
			database.put(id, sec.getString("database"));
			username.put(id, sec.getString("username"));
			password.put(id, sec.getString("password"));
			query.put(id, sec.getString("query"));
			port.put(id, sec.getInt("port"));
		}
		Bukkit.broadcastMessage(host.size() + "");
		return super.register();
	}

	@Override
	public String onPlaceholderRequest(Player p, String id) {
		if (identifiers.contains(id)) {
			try {
				openConnection(id);
				ResultSet result = statement.executeQuery(query.get(id).replaceAll("{uuid}", p.getUniqueId().toString())
						.replaceAll("{player}", p.getDisplayName()));
				if (result.next())
					return result.toString();
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;

	}

	/*
	 * @Override public Map<String, Object> getDefaults() { Map<String, Object>
	 * map = new HashMap<String, Object>(); String vt = "playerpoints";
	 * map.put(vt + ".host", "localhost"); map.put(vt + ".port", 3306);
	 * map.put(vt + ".username", "root"); map.put(vt + ".database", "root");
	 * map.put(vt + ".password", "password"); map.put(vt + ".query",
	 * "SELECT points FROM playerpoints WHERE playername = {uuid}"); vt =
	 * "ProVotes"; map.put(vt + ".host", "localhost"); map.put(vt + ".port",
	 * 3306); map.put(vt + ".username", "root"); map.put(vt + ".database",
	 * "root"); map.put(vt + ".password", "password"); map.put(vt + ".query",
	 * "SELECT TotalVotes FROM ProVotes WHERE UUID = {uuid}"); return map; }
	 */

	private boolean openConnection(String id) throws SQLException, ClassNotFoundException {
		if (connection == null || connection.isClosed())
			synchronized (this) {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(
						"jdbc:mysql://" + host.get(id) + ":" + port.get(id) + "/" + database.get(id), username.get(id),
						password.get(id));
				return true;
			}
		return false;
	}

	@Override
	public String getAuthor() {
		return "SuperRonanCraft";
	}

	@Override
	public String getIdentifier() {
		return "MySQL";
	}

	@Override
	public String getPlugin() {
		return null;
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}
}