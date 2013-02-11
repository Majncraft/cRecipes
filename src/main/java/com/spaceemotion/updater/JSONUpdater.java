package com.spaceemotion.updater;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.json.JSONObject;

public class JSONUpdater {
	public final static String URL = "http://dev.catharos.de/update.php?project=%P%&version=%V%";

	private JavaPlugin plugin;
	private UpdateReader reader;

	private BukkitTask updateTask;

	public JSONUpdater( JavaPlugin plugin ) {
		this.plugin = plugin;

		PluginDescriptionFile descr = this.plugin.getDescription();

		String url = URL;
		url = url.replace( "%P%", descr.getName() );
		url = url.replace( "%V%", Integer.toString( getVersion( descr.getVersion() ) ) );

		this.reader = new UpdateReader( url );

		updateTask = plugin.getServer().getScheduler().runTaskTimerAsynchronously( plugin, new Runnable() {
			public void run() {
				checkForUpdate();
			}
		}, 0, 20 * 60 * plugin.getConfig().getInt( "update-interval", 20 ) );
	}

	public void checkForUpdate() {
		try {
			JSONObject obj = reader.read();

			if (obj.getBoolean( "update" )) {
				String msg = StringEscapeUtils.unescapeJava( obj.getString( "message" ) );
				plugin.getLogger().info( ChatColor.BLUE + msg );
				return;
			}
		} catch (Exception ex) {
		}

		plugin.getLogger().info( ChatColor.BLUE + "No update found!" );
	}

	private int getVersion( String version ) {
		try {
			if (version.contains( "-b" )) {
				return Integer.parseInt( version.substring( version.indexOf( "-b" ) + 1 ) );
			} else {
				return Integer.parseInt( version.replaceAll( ".", "" ) );
			}
		} catch (Exception e) {
			return 0;
		}
	}

	public BukkitTask getUpdaterTask() {
		return updateTask;
	}
}
