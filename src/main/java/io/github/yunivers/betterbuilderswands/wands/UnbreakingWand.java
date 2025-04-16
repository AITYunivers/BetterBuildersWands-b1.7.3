package io.github.yunivers.betterbuilderswands.wands;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class UnbreakingWand implements IWand
{
    @Override
    public int getMaxBlocks(ItemStack itemStack)
    {
        if (itemStack.getDamage() <= 0)
            return 4096;
        else
            return 1 << itemStack.getDamage();
    }

    @Override
    public boolean placeBlock(ItemStack itemStack, LivingEntity livingEntity)
    {
        return true;
    }
}
