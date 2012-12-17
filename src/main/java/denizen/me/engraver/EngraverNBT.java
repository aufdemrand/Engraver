package denizen.me.engraver;

import net.minecraft.server.v1_4_5.NBTTagCompound;

import org.bukkit.craftbukkit.v1_4_5.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/*
 * Some static methods for dealing with Minecraft NBT data, which is used to store
 * the engravings. Each item can have one owner.
 * 
 */

public class EngraverNBT {

	public static boolean hasEngraving(ItemStack item) {
        NBTTagCompound tag;
        net.minecraft.server.v1_4_5.ItemStack cis =  CraftItemStack.asNMSCopy(item);
        if (!cis.hasTag()) return false;
		tag = cis.getTag();
		// if this item has the NBTData for 'owner', there is an engraving.
		return tag.hasKey("owner");
	}

	public static String getEngraving(ItemStack item) {
        net.minecraft.server.v1_4_5.ItemStack cis =  CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag;
        if (!cis.hasTag())
			cis.setTag(new NBTTagCompound());
		tag = cis.getTag();
		// if this item has the NBTData for 'owner', return the value, which is the playername of the 'owner'.
		if (tag.hasKey("owner")) return tag.getString("owner");
		// if not, return "".. no owner.
		return "";
	}

	public static void removeEngraving(ItemStack item) {
		net.minecraft.server.v1_4_5.ItemStack cis =  CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag;
		if (!cis.hasTag())
			cis.setTag(new NBTTagCompound());
		tag = cis.getTag();
		// remove 'owner' NBTData
		tag.o("owner");
	}

	public static void addEngraving(ItemStack item, String playerName) {
		NBTTagCompound tag;
		net.minecraft.server.v1_4_5.ItemStack cis =  CraftItemStack.asNMSCopy(item);
		if (!cis.hasTag())
			cis.setTag(new NBTTagCompound());
		tag = cis.getTag();
		// set 'owner' NBTData with the player's name.
		tag.setString("owner", playerName);
	}
	
}
