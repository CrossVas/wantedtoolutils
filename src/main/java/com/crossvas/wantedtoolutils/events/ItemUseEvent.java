package com.crossvas.wantedtoolutils.events;

import com.crossvas.wantedtoolutils.utils.WantedUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ItemUseEvent {

	@SubscribeEvent
	public void onItemUseEvent(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		ItemStack stack = player.getHeldItem();
		World world = event.world;

		boolean air = event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR;

		if (!world.isRemote) {
			if (air && stack != null && WantedUtils.getStackCondition(stack)) {
				onItemRightClick(stack, world, player);
			}
		}
	}
	
	public void onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		NBTTagCompound tag = stack.getTagCompound();
		if (player.isSneaking() && WantedUtils.getStackCondition(stack)) {
			if (!tag.getBoolean("ex_vein")) {
				tag.setBoolean("ex_vein", true);
				// do chat
				player.addChatMessage(new ChatComponentText(stack.getDisplayName() + ": " + "Extended Vein On"));
			} else {
				tag.setBoolean("ex_vein", false);
				// do chat
				player.addChatMessage(new ChatComponentText(stack.getDisplayName() + ": " + "Extended Vein Off"));
			}
		}
	}
}
