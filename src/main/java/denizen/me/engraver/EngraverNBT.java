package denizen.me.engraver;

import java.lang.reflect.Field;

import net.aufdemrand.denizen.utilities.debugging.dB;
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
        net.minecraft.server.v1_4_5.ItemStack cis =  getItemStackHandle(item);
        if (!cis.hasTag()) return false;
		tag = cis.getTag();
		// if this item has the NBTData for 'owner', there is an engraving.
		return tag.hasKey("owner");
	}

	public static String getEngraving(ItemStack item) {
        net.minecraft.server.v1_4_5.ItemStack cis =  getItemStackHandle(item);
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
		net.minecraft.server.v1_4_5.ItemStack cis =  getItemStackHandle(item);
		NBTTagCompound tag;
		if (!cis.hasTag())
			cis.setTag(new NBTTagCompound());
		tag = cis.getTag();
		// remove 'owner' NBTData
		tag.o("owner");
	}

	public static void addEngraving(ItemStack item, String playerName) {
		net.minecraft.server.v1_4_5.ItemStack cis = getItemStackHandle(item);
		NBTTagCompound tag = null;
		// Do stuff with tag
		if (!cis.hasTag()) {
			dB.log("No tag");
			cis.setTag(new NBTTagCompound());
		}
		dB.log("Get tag");
		tag = cis.getTag();
		// set 'owner' NBTData with the player's name.
		dB.log("setString");
		tag.setString("owner", playerName);
		dB.log("hasTag " + tag.hasKey("owner"));
		dB.log(CraftItemStack.asNMSCopy(item).getTag().getString("owner"));
	}
	
	public static net.minecraft.server.v1_4_5.ItemStack getItemStackHandle(ItemStack item) {
		CraftItemStack cis = (CraftItemStack) item;
		Field f = null;
		try {
			// Use reflection to grant access to CraftItemStack field 'handle'
			// which is not public
			f = cis.getClass().getDeclaredField("handle");
			f.setAccessible(true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		net.minecraft.server.v1_4_5.ItemStack is = null;
		try {
			// Use reflection to get handle
			is = (net.minecraft.server.v1_4_5.ItemStack) f.get(item);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}		
		// Return the itemstack
		return is;
	}
	
}
