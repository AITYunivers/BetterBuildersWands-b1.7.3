package io.github.yunivers.betterbuilderswands.wands;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface IWand
{
    int getMaxBlocks(ItemStack itemStack);
    boolean placeBlock(ItemStack itemStack, LivingEntity livingEntity);
}
