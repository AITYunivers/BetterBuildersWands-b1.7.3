package io.github.yunivers.betterbuilderswands.wrappers;

import io.github.yunivers.betterbuilderswands.util.Point3d;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class BasicPlayerWrapper implements IPlayerWrapper
{
    private PlayerEntity player;

    public BasicPlayerWrapper(PlayerEntity player)
    {
        this.player = player;
    }

    @Override
    public int countItems(ItemStack itemStack)
    {
        int total = 0;
        if (itemStack == null || player.inventory == null || player.inventory.main == null)
            return 0;

        for (ItemStack inventoryStack : player.inventory.main)
            if (inventoryStack != null && itemStack.isItemEqual(inventoryStack))
                total += Math.max(0, inventoryStack.count);

        return itemStack.count > 0 ? total / itemStack.count : 0;
    }

    @Override
    public boolean useItem(ItemStack itemStack)
    {
        if (itemStack == null || player.inventory == null || player.inventory.main == null)
            return false;

        // Reverse direction to leave hotbar to last.
        int toUse = itemStack.count;
        for (int i = player.inventory.main.length - 1; i >= 0; i--)
        {
            ItemStack inventoryStack = player.inventory.main[i];
            if (inventoryStack != null && itemStack.isItemEqual(inventoryStack))
            {
                if (inventoryStack.count < toUse)
                {
                    inventoryStack.count = 0;
                    toUse -= inventoryStack.count;
                }
                else
                {
                    inventoryStack.count = inventoryStack.count - toUse;
                    toUse = 0;
                }

                if (inventoryStack.count == 0)
                    player.inventory.setStack(i, null);

                player.currentScreenHandler.sendContentUpdates();
                if (toUse <= 0)
                    return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack getNextItem(Block block, int meta)
    {
        return null;
    }

    @Override
    public Point3d getPlayerPosition()
    {
        return new Point3d((int)player.x, (int)player.y, (int)player.z);
    }

    @Override
    public PlayerEntity getPlayer()
    {
        return player;
    }

    @Override
    public boolean isCreative()
    {
        if (FabricLoader.getInstance().isModLoaded("bhcreative"))
            return player.creative_isCreative();
        return false;
    }
}
