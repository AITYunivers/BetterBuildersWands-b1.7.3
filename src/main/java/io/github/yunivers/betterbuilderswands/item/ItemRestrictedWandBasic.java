package io.github.yunivers.betterbuilderswands.item;

import io.github.yunivers.betterbuilderswands.util.EnumLock;
import io.github.yunivers.betterbuilderswands.wands.RestrictedWand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;

public class ItemRestrictedWandBasic extends ItemBasicWand
{
    public ItemRestrictedWandBasic(Identifier identifier, RestrictedWand wand)
    {
        super(identifier);
        setMaxDamage(ToolMaterial.STONE.getDurability());
        this.wand = wand;
    }

    @Override
    public void nextMode(ItemStack itemStack, PlayerEntity player)
    {
        setMode(itemStack, EnumLock.HORIZONTAL);
    }

    @Override
    public EnumLock getFaceLock(ItemStack itemStack)
    {
        return EnumLock.HORIZONTAL;
    }

    @Override
    public EnumLock getDefaultMode()
    {
        return EnumLock.HORIZONTAL;
    }
}
