package com.crossvas.wantedtoolutils.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.crossvas.wantedtoolutils.config.WantedToolUtilsConfig;

import cpw.mods.fml.common.Loader;
import ic2.api.item.ElectricItem;
import mekanism.api.Coord4D;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class WantedUtils {

	public static WantedToolUtilsConfig config;

	public static boolean hasIC2() {
		return Loader.isModLoaded("IC2");
	}
	
	public static boolean hasRedstoneArsenal() {
		return Loader.isModLoaded("RedstoneArsenal");
	}

	public static boolean hasEnderIO() {
		return Loader.isModLoaded("EnderIO");
	}
	
	public static boolean getStackCondition(ItemStack stack) {
		if (stack == null) {
			return false;
		}
		if (hasRedstoneArsenal() && stack.getItem() instanceof cofh.redstonearsenal.item.tool.ItemPickaxeRF
				&& config.enableRSACompat) {
			return true;
		} else if (hasIC2() && stack.getItem() instanceof ic2.core.item.tool.ItemDrill && config.enableIC2Compat) {
			return true;
		} else if (hasEnderIO() && stack.getItem() instanceof crazypants.enderio.item.darksteel.ItemDarkSteelPickaxe
				&& config.enableEIOCompat) {
			return true;
		}
		return false;
	}

	public static boolean getStackEnergyCondition(ItemStack stack) {
		NBTTagCompound tag = getOrCreateNbtData(stack);
		if (hasIC2() && stack.getItem() instanceof ic2.core.item.tool.ItemDrill
				&& ElectricItem.manager.canUse(stack, 200)) {
			return true;
		} else if (hasRedstoneArsenal() && stack.getItem() instanceof cofh.redstonearsenal.item.tool.ItemPickaxeRF
				&& tag.getInteger("Energy") > 200) {
			return true;
		} else if (hasEnderIO() && stack.getItem() instanceof crazypants.enderio.item.darksteel.ItemDarkSteelPickaxe) {
			return true;
		}
		return false;
	}

	public static boolean hasEnchantment(ItemStack stack, Enchantment enchantment) {
		boolean value = false;
		NBTTagList enchantmentList = stack.getEnchantmentTagList();
		for (int i = 0; i < enchantmentList.tagCount(); i++) {
			int id = enchantmentList.getCompoundTagAt(i).getInteger("id");
			if (id == enchantment.effectId)
				value = true;
		}
		return value;
	}

	public static NBTTagCompound getOrCreateNbtData(ItemStack itemStack) {
		NBTTagCompound tag = itemStack.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
			itemStack.setTagCompound(tag);
		}
		return tag;
	}

	/**
	 * 
	 * I don't own the following code. Thank you for this code to the author of Mekanism - @author aidancbrady. 
	 * 
	 * */
	public static class Finder {
		public World world;

		public ItemStack stack;

		public Coord4D location;

		public Set<Coord4D> found = new HashSet<Coord4D>();

		public static List<Block> redstoneOre = new ArrayList<Block>();
		public static Map<Block, List<Block>> ignoreBlocks = new HashMap<Block, List<Block>>();

		public Finder(World world, ItemStack stack, Coord4D location) {
			this.world = world;
			this.stack = stack;
			this.location = location;
		}

		public void loop(Coord4D pointer) {
			if (found.contains(pointer) || found.size() > 128) {
				return;
			}

			found.add(pointer);

			for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
				Coord4D coord = pointer.getFromSide(side);

				if (coord.exists(world) && checkID(coord.getBlock(world))
						&& (coord.getMetadata(world) == stack.getItemDamage()
								|| (coord.getMetadata(world) % 4 == stack.getItemDamage() % 4))) {
					loop(coord);
				}
			}
		}

		public Set<Coord4D> calc() {
			loop(location);

			return found;
		}

		public boolean checkID(Block b) {
			Block origBlock = location.getBlock(world);
			return (ignoreBlocks.get(origBlock) == null && b == origBlock)
					|| (ignoreBlocks.get(origBlock) != null && ignoreBlocks.get(origBlock).contains(b));
		}

		static {
			redstoneOre.add(Blocks.lit_redstone_ore);
			redstoneOre.add(Blocks.redstone_ore);
			ignoreBlocks.put(Blocks.lit_redstone_ore, redstoneOre);
			ignoreBlocks.put(Blocks.redstone_ore, redstoneOre);
		}
	}
}
