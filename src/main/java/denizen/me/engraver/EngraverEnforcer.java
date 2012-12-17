package denizen.me.engraver;

import java.util.HashMap;
import java.util.Map;

import net.aufdemrand.denizen.utilities.debugging.dB;
import net.citizensnpcs.util.Messaging;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

/*
 * Bukkit listener EventHandler methods to handle enforcing of picking up engraved items, and enforcement
 * of despawning engraved items.
 * 
 */

public class EngraverEnforcer implements Listener {

	BukkitPlugin plugin;
	
	EngraverEnforcer(BukkitPlugin engraver) {
		plugin = engraver;
	}
	
	// Map to keep track of notification cooldowns (String playerName, Long timeout)
	Map<String, Long> notifyCooldown = new HashMap<String, Long>();

	/*
	 * Checks the owner of the picked up item, if any. Will not let the Player pick up
	 * the item if they are not the owner of the engraved item.
	 * 
	 * If sneaking, a message will let the Player know that it is engraved.
	 * 
	 */
	
	@EventHandler
	public void checkOwner(PlayerPickupItemEvent event) {
		if (EngraverNBT.hasEngraving(event.getItem().getItemStack()) 
				&& !EngraverNBT.getEngraving(event.getItem().getItemStack()).equalsIgnoreCase(event.getPlayer().getName())) {

			// See why item isn't being picked up if sneaking.
			if (event.getPlayer().isSneaking()) {
				// Check cooldown to avoid spam from multiple items/event firings
				if (!notifyCooldown.containsKey(event.getPlayer().getName())) {
					// If not cooled down, set cool-down and alert Player they cannot pick it up.
					notifyCooldown.put(event.getPlayer().getName(), System.currentTimeMillis() + 10000);
					Messaging.send(event.getPlayer(), "That " + event.getItem().getItemStack().getType() + " does not belong to you!");
				} else {
					// If cooled down, remove the cooldown. 
					if (notifyCooldown.get(event.getPlayer().getName()) < System.currentTimeMillis()) 
						notifyCooldown.remove(event.getPlayer().getName());
				}
			}
			// If engraved, and not the engraved Player, cancel the pickup event.
			event.setCancelled(true);
		}
		// Otherwise, don't interfere.
	}

	
	
	// Map to keep track of despawn delays. (Integer entityID, Long timeout)
	Map<Integer, Long> despawnDelay = new HashMap<Integer, Long>();

	/*
	 * An engraved item will take longer to despawn in the world, allowing a little bit of extra time
	 * for the player to retrieve it.
	 * 
	 */
	@EventHandler
	public void stopDespawn(ItemDespawnEvent event) {
		// Check if the item has an engraving, otherwise carry on.
		if (EngraverNBT.hasEngraving(event.getEntity().getItemStack())) {
			// If in the delay map
			if (despawnDelay.containsKey(event.getEntity().getEntityId())) {
				// If not cooled, cancel despawn.
				if (despawnDelay.get(event.getEntity().getEntityId()) < System.currentTimeMillis())
					event.setCancelled(true);
				else 
					// If cooled, remove from map.
					dB.log("Removed an ENGRAVED '" + event.getEntity().getItemStack().getType().name() + "' which belonged to '" + EngraverNBT.getEngraving(event.getEntity().getItemStack()) + "'.");
					despawnDelay.remove(event.getEntity().getEntityId());
			} else {
				// If not in delay map, add to delay map and cancel despawn.
				event.setCancelled(true);
				dB.log("Stopped despawn of an ENGRAVED '" + event.getEntity().getItemStack().getType().name() + "' which belonged to '" + EngraverNBT.getEngraving(event.getEntity().getItemStack()) + "'. Will remove from world in 10 minutes.");
				despawnDelay.put(event.getEntity().getEntityId(), System.currentTimeMillis() + (1000 * 60 * 10));
			}
		}
	}
	
}
