package com.crossvas.wantedtoolutils.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.crossvas.wantedtoolutils.config.WantedToolUtilsConfig;
import com.crossvas.wantedtoolutils.utils.WantedUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import mekanism.api.Coord4D;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;

public class BlockBreakEvent {

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		EntityPlayer player = event.getPlayer();
		ItemStack stack = player.getHeldItem();
		World world = event.world;
		int x = event.x;
		int y = event.y;
		int z = event.z;

		if (!world.isRemote) {
			if (stack != null && WantedToolUtilsConfig.enableVeinMode && WantedUtils.getStackCondition(stack)) {
				onBlockStartBreak(player.getHeldItem(), x, y, z, player);
			}
		}
	}
	
	public boolean onBlockStartBreak(ItemStack itemstack, int x, int y, int z, EntityPlayer player) {
		Block block = player.worldObj.getBlock(x, y, z);
		int meta = player.worldObj.getBlockMetadata(x, y, z);
		int fortune = EnchantmentHelper.getFortuneModifier(player);
		if (block == Blocks.lit_redstone_ore) {
			block = Blocks.redstone_ore;
		}
		ItemStack blockStack = new ItemStack(block, 1, meta);
		Coord4D orig = new Coord4D(x, y, z, player.worldObj.provider.dimensionId);
		List<String> oreNames = new ArrayList<String>();
		int[] oreIDs = OreDictionary.getOreIDs(blockStack);
		for (Integer id : oreIDs) {
			oreNames.add(OreDictionary.getOreName(id));
		}
		boolean isOre = false;
		for (String name : oreNames) {
			if (name.startsWith("ore")) {
				isOre = true;
			}
		}
		boolean extended_vein = false;
		NBTTagCompound tag = itemstack.getTagCompound();
		if (tag.getBoolean("ex_vein")) {
			extended_vein = true;
		}

		if (!player.capabilities.isCreativeMode && (isOre || extended_vein) && !player.isSneaking()) {
			Set<Coord4D> found = new WantedUtils.Finder(player.worldObj, blockStack,
					new Coord4D(x, y, z, player.worldObj.provider.dimensionId)).calc();
			for (Coord4D coord : found) {
				if (coord.equals(orig)) {
					continue;
				}
				if (WantedUtils.getStackEnergyCondition(itemstack)) {
					Block blockVein = coord.getBlock(player.worldObj);
					if (blockVein.removedByPlayer(player.worldObj, player, coord.xCoord, coord.yCoord, coord.zCoord)) {
						blockVein.onBlockDestroyedByPlayer(player.worldObj, coord.xCoord, coord.yCoord, coord.zCoord,
								meta);
					}
					if (WantedUtils.hasEnchantment(itemstack, Enchantment.fortune)) {
						int exp = blockVein.getExpDrop(player.worldObj, meta, fortune);
						blockVein.dropXpOnBlockBreak(player.worldObj, coord.xCoord, coord.yCoord, coord.zCoord, exp);
					}
					blockVein.harvestBlock(player.worldObj, player, coord.xCoord, coord.yCoord, coord.zCoord, meta);
					blockVein.onBlockHarvested(player.worldObj, coord.xCoord, coord.yCoord, coord.zCoord, meta, player);
					player.worldObj.func_147479_m(coord.xCoord, coord.yCoord, coord.zCoord);
					if ((WantedUtils.hasIC2() && itemstack.getItem() instanceof ic2.core.item.tool.ItemDrill)
							|| (WantedUtils.hasEnderIO() && itemstack
									.getItem() instanceof crazypants.enderio.item.darksteel.ItemDarkSteelPickaxe)) {
						itemstack.getItem().onBlockDestroyed(itemstack, player.worldObj, blockVein, coord.xCoord,
								coord.yCoord, coord.zCoord, player);
					}
					itemstack.getItem().onBlockStartBreak(itemstack, coord.xCoord, coord.yCoord, coord.zCoord, player);
				}
			}
		}
		return false;
	}
}
