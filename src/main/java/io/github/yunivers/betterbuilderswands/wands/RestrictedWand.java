package io.github.yunivers.betterbuilderswands.wands;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class RestrictedWand implements IWand
{
    protected int blocklimit = 0;

    public RestrictedWand(int limit)
    {
        this.blocklimit = limit;
    }

    @Override
    public int getMaxBlocks(ItemStack itemStack)
    {
        return Math.min(itemStack.getMaxDamage() - itemStack.getDamage(), this.blocklimit);
    }

    @Override
    public boolean placeBlock(ItemStack itemStack, LivingEntity livingEntity)
    {
        itemStack.damage(1, livingEntity);
        if (itemStack.count > 0 && itemStack.getDamage() == itemStack.getMaxDamage())
            itemStack.count = 0;
        return true;
    }
}
