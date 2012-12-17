package denizen.me.engraver;

import java.util.HashMap;
import java.util.Map;

import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizen.utilities.nbt.NBTItem;
import net.citizensnpcs.util.Messaging;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class EngraverEnforcer implements Listener {

	Map<String, Long> notifyCooldown = new HashMap<String, Long>();

	@EventHandler
	public void checkOwner(PlayerPickupItemEvent event) {
		if (NBTItem.hasEngraving(event.getItem().getItemStack()) 
				&& !NBTItem.getEngraving(event.getItem().getItemStack()).equalsIgnoreCase(event.getPlayer().getName())) {

			// See why item isn't being picked up if sneaking.
			if (event.getPlayer().isSneaking()) {
				// Check cooldown to avoid spam from multiple items/event firings
				if (!notifyCooldown.containsKey(event.getPlayer().getName())) {
					notifyCooldown.put(event.getPlayer().getName(), System.currentTimeMillis() + 10000);
					Messaging.send(event.getPlayer(), "That " + event.getItem().getType() + " does not belong to you!");
				} else {
					if (notifyCooldown.get(event.getPlayer().getName()) < System.currentTimeMillis()) 
						notifyCooldown.remove(event.getPlayer().getName());
				}
			}
			event.setCancelled(true);
		}
	}

	Map<Integer, Long> despawnDelay = new HashMap<Integer, Long>();

	@EventHandler
	public void stopDespawn(ItemDespawnEvent event) {
		if (NBTItem.hasEngraving(event.getEntity().getItemStack())) {
			// If in the delay map
			if (despawnDelay.containsKey(event.getEntity().getEntityId())) {
				// If not cooled, cancel despawn.
				if (despawnDelay.get(event.getEntity().getEntityId()) < System.currentTimeMillis())
					event.setCancelled(true);
				else 
					// If cooled, remove from map.
					despawnDelay.remove(event.getEntity().getEntityId());
			} else {
				// If not in delay map, add to delay map and cancel despawn.
				event.setCancelled(true);
				dB.log("Stopped despawn of ENGRAVED item. Will remove from world in 10 minutes.");
				despawnDelay.put(event.getEntity().getEntityId(), System.currentTimeMillis() + (1000 * 60 * 10));
			}
		}
	}
	
}
