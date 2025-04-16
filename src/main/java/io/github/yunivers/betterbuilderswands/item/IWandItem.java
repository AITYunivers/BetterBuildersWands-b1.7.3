package io.github.yunivers.betterbuilderswands.item;

import io.github.yunivers.betterbuilderswands.util.EnumLock;
import io.github.yunivers.betterbuilderswands.wands.IWand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IWandItem
{
    EnumLock getMode(ItemStack itemStack);
    void nextMode(ItemStack itemStack, PlayerEntity player);
    IWand getWand();
    EnumLock getFaceLock(ItemStack itemStack);
}
