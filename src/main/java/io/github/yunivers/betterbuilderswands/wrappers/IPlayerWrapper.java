package io.github.yunivers.betterbuilderswands.wrappers;

import io.github.yunivers.betterbuilderswands.util.Point3d;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IPlayerWrapper
{
    int countItems(ItemStack itemStack);
    boolean useItem(ItemStack itemStack);
    ItemStack getNextItem(Block block, int meta);
    Point3d getPlayerPosition();
    PlayerEntity getPlayer();

    boolean isCreative();
}
