package denizen.me.engraver;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;



import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.aufdemrand.denizen.scripts.helpers.ArgumentHelper.ArgumentType;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizen.utilities.debugging.dB.Messages;
import net.aufdemrand.denizen.utilities.nbt.NBTItem;

/**
 * Engraves an item. Engraved items are bound to their engraver and cannot be picked
 * up by other Players.
 * 
 * @author Jeremy Schroeder
 */

public class EngraveCommand extends AbstractCommand implements Listener {

	/* ENGRAVE (REMOVE|ADD)

	/* Arguments: [] - Required, () - Optional 
	 * 
	 * Example Usage:
	 */

	private enum EngraveAction { ADD, REMOVE, REMOVEALL }

	private String playerName;
	private EngraveAction action;
	private ItemStack item;

	@Override
	public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {

		item = null;
		playerName = null;
		if (scriptEntry.getPlayer() != null) {
			playerName = scriptEntry.getPlayer().getName();
			item = scriptEntry.getPlayer().getItemInHand();
		}

		action = EngraveAction.ADD;

		// Parse the arguments
		for (String arg : scriptEntry.getArguments()) {

			if (aH.matchesArg("ADD, REMOVE, SET", arg)) {
				action = EngraveAction.valueOf(arg.toUpperCase());
				dB.echoDebug(Messages.DEBUG_SET_TYPE, arg);
				continue;

			} else if (aH.matchesValueArg("TARGET", arg, ArgumentType.String)) {
				dB.echoDebug(Messages.DEBUG_SET_TYPE, arg);
				playerName = aH.getStringFrom(arg);

			} else throw new InvalidArgumentsException(Messages.ERROR_UNKNOWN_ARGUMENT, arg);
		}

		if (item == null) throw new InvalidArgumentsException(Messages.ERROR_MISSING_OTHER, "ITEM");
		if (playerName == null) throw new InvalidArgumentsException(Messages.ERROR_NO_PLAYER);
	}

	@Override
	public void execute(String commandName) throws CommandExecutionException {

		if (action == EngraveAction.REMOVE) {
			dB.echoDebug("Removing engraving on '" + item.getType() + "'.");			
			NBTItem.removeEngraving(item, playerName);
		} else if (action == EngraveAction.ADD) {
			dB.echoDebug("Engraving '" + item.getType() + "' with an inscription of '" + playerName + "'.");
			NBTItem.addEngraving(item, playerName);
		}
	}

	@Override
	public void onEnable() {
		denizen.getServer().getPluginManager().registerEvents(this, denizen);
	}



}