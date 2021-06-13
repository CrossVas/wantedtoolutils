package com.crossvas.wantedtoolutils.events;

import com.crossvas.wantedtoolutils.utils.WantedUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class ToolTipEvent {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void itemToolTipEvent(ItemTooltipEvent event) {
		ItemStack stack = event.entityPlayer.getHeldItem();
		NBTTagCompound tag = stack.getTagCompound();
		if (WantedUtils.getStackCondition(stack)) {
			if (tag.getBoolean("ex_vein")) {
				event.toolTip.add("Test");
			} else {
				return;
			}
		}
	}
}
