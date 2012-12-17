package denizen.me.engraver;

import org.bukkit.plugin.java.JavaPlugin;

public class BukkitPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		new EngraveCommand().activate().as("ENGRAVE").withOptions("(SET|REMOVE) (PLAYER:player_name)", 0);
	}

	
}
