package io.github.yunivers.betterbuilderswands.item;

import io.github.yunivers.betterbuilderswands.util.EnumLock;
import io.github.yunivers.betterbuilderswands.wands.RestrictedWand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Objects;

public class ItemRestrictedWandAdvanced extends ItemBasicWand
{
    public ItemRestrictedWandAdvanced(Identifier identifier, RestrictedWand wand)
    {
        super(identifier);
        setMaxDamage(ToolMaterial.IRON.getDurability());
        this.wand = wand;
    }

    @Override
    public void nextMode(ItemStack itemStack, PlayerEntity player)
    {
        if (getMode(itemStack) == EnumLock.HORIZONTAL)
            setMode(itemStack, EnumLock.VERTICAL);
        else
            setMode(itemStack, EnumLock.HORIZONTAL);
    }

    @Override
    public EnumLock getFaceLock(ItemStack itemStack)
    {
        return EnumLock.NOLOCK;
    }

    @Override
    public EnumLock getDefaultMode()
    {
        return EnumLock.HORIZONTAL;
    }
}
