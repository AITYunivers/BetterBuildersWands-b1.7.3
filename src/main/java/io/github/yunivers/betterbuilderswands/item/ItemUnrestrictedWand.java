package io.github.yunivers.betterbuilderswands.item;

import io.github.yunivers.betterbuilderswands.util.EnumLock;
import io.github.yunivers.betterbuilderswands.wands.IWand;
import io.github.yunivers.betterbuilderswands.wands.RestrictedWand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.*;

public class ItemUnrestrictedWand extends ItemBasicWand
{
    protected Set<Integer> subItemMetas = new HashSet<Integer>();

    public ItemUnrestrictedWand(Identifier identifier, IWand wand)
    {
        super(identifier);
        this.wand = wand;
    }

    @Override
    public EnumLock getFaceLock(ItemStack itemStack)
    {
        if (getMode(itemStack) == EnumLock.HORIZONTAL)
            return EnumLock.HORIZONTAL;
        return EnumLock.NOLOCK;
    }

    @Override
    public void nextMode(ItemStack itemStack, PlayerEntity player)
    {
        switch(getMode(itemStack))
        {
            case NORTHSOUTH:
                setMode(itemStack, EnumLock.EASTWEST);
                break;
            case VERTICAL:
                setMode(itemStack, EnumLock.NORTHSOUTH);
                break;
            case VERTICALEASTWEST:
                setMode(itemStack, EnumLock.NOLOCK);
                break;
            case EASTWEST:
                setMode(itemStack, EnumLock.VERTICALNORTHSOUTH);
                break;
            case HORIZONTAL:
                setMode(itemStack, EnumLock.VERTICAL);
                break;
            case VERTICALNORTHSOUTH:
                setMode(itemStack, EnumLock.VERTICALEASTWEST);
                break;
            case NOLOCK:
                setMode(itemStack, EnumLock.HORIZONTAL);
                break;
        }
    }

    public ItemUnrestrictedWand addSubMeta(int meta)
    {
        this.subItemMetas.add(meta);
        return this;
    }

    public void getSubItems(Item item, List<ItemStack> list)
    {
        if (subItemMetas.isEmpty())
            list.add(new ItemStack(item, 1, 0));
        else
        {
            ArrayList<Integer> metas = new ArrayList<Integer>(this.subItemMetas);
            Collections.sort(metas);
            for (Integer meta : metas)
                list.add(new ItemStack(item, 1, meta));
        }
    }
}
