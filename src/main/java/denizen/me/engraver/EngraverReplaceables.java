package denizen.me.engraver;

import net.aufdemrand.denizen.events.ReplaceableTagEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EngraverReplaceables implements Listener {

	BukkitPlugin plugin;
	
	EngraverReplaceables(BukkitPlugin engraver) {
		plugin = engraver;
	}
	
	@EventHandler
	public void replaceable(ReplaceableTagEvent event) {
	       if (!event.matches("PLAYER") || event.getPlayer() == null) return;

	       // Replaceable type not PLAYER? return. ... No player? return. 
	       if (event.getPlayer() == null) return;
	       
	       // Replace on <PLAYER.ITEM_IN_HAND.OWNER>
	       if (event.getType().equalsIgnoreCase("ITEM_IN_HAND.OWNER"))
	    	   if (EngraverNBT.hasEngraving(event.getPlayer().getItemInHand()))
	    			   event.setReplaced(EngraverNBT.getEngraving(event.getPlayer().getItemInHand()));
	}
	
}
