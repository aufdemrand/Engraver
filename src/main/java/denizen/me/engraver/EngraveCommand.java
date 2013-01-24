package denizen.me.engraver;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.aufdemrand.denizen.utilities.arguments.aH;
import net.aufdemrand.denizen.utilities.arguments.aH.ArgumentType;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizen.utilities.debugging.dB.Messages;

/*
 * Denizen dScript ENGRAVE command:
 * 
 * Engraves an item in the player's hand. Engraved items are bound to their engraver and cannot be picked
 * up by other Players.
 * 
 */

public class EngraveCommand extends AbstractCommand implements Listener {

	/* ENGRAVE (REMOVE|ADD) (TARGET:target_name)

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
				
			//
			// If there is an ITEM: argument,
			// use that instead of the item
			// in the players hand
			//
			} else if (aH.matchesItem(arg)) {
				dB.echoDebug(Messages.DEBUG_SET_ITEM, arg);
				item = aH.getItemFrom(arg);

			} else throw new InvalidArgumentsException(Messages.ERROR_UNKNOWN_ARGUMENT, arg);
		}

		if (item == null) throw new InvalidArgumentsException(Messages.ERROR_MISSING_OTHER, "ITEM");
		if (playerName == null) throw new InvalidArgumentsException(Messages.ERROR_NO_PLAYER);
	}

	@Override
	public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {

		if (action == EngraveAction.REMOVE) {
			dB.echoDebug("Removing engraving on '" + item.getType() + "'.");			
			EngraverNBT.removeEngraving(item);
		} else if (action == EngraveAction.ADD) {
			dB.echoDebug("Engraving '" + item.getType() + "' with an inscription of '" + playerName + "'.");
			EngraverNBT.addEngraving(item, playerName);
		}
	}

	@Override
	public void onEnable() {
		// Nothing to do here.
	}

}