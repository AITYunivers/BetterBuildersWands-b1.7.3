package io.github.yunivers.betterbuilderswands.wrappers;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class CreativePlayerWrapper extends BasicPlayerWrapper implements IPlayerWrapper
{
    public CreativePlayerWrapper(PlayerEntity player)
    {
        super(player);
    }

    @Override
    public int countItems(ItemStack itemStack)
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean useItem(ItemStack itemStack)
    {
        return true;
    }

    @Override
    public ItemStack getNextItem(Block block, int meta)
    {
        return new ItemStack(block, 1, meta);
    }
}
