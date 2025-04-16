package io.github.yunivers.betterbuilderswands.item;

import io.github.yunivers.betterbuilderswands.BetterBuildersWandsMod;
import io.github.yunivers.betterbuilderswands.util.EnumLock;
import io.github.yunivers.betterbuilderswands.util.Point3d;
import io.github.yunivers.betterbuilderswands.wands.IWand;
import io.github.yunivers.betterbuilderswands.wands.WandWorker;
import io.github.yunivers.betterbuilderswands.wrappers.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.item.StationNBTSetter;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class ItemBasicWand extends TemplateItem implements IWandItem, CustomTooltipProvider
{
    public IWand wand;

    public ItemBasicWand(Identifier identifier)
    {
        super(identifier);
        setMaxCount(1);
    }

    @Override
    public boolean useOnBlock(ItemStack stack, PlayerEntity user, World world, int x, int y, int z, int side)
    {
        if (wand == null)
            return false;

        if (stack == null)
        {
            BetterBuildersWandsMod.LOGGER.error("BasicWand onItemUse itemstack empty");
            return false;
        }

        if (!world.isRemote)
        {
            IPlayerWrapper playerWrapper = new BasicPlayerWrapper(user);
            if (FabricLoader.getInstance().isModLoaded("bhcreative") && user.creative_isCreative())
                playerWrapper = new CreativePlayerWrapper(user);

            IWorldWrapper worldWrapper = new BasicWorldWrapper(world);
            WandWorker worker = new WandWorker(wand, playerWrapper, worldWrapper);
            Point3d clickedPos = new Point3d(x, y, z);
            ItemStack sourceItems = worker.getProperItemStack(worldWrapper, playerWrapper, clickedPos);
            if (sourceItems != null && sourceItems.getItem() instanceof BlockItem)
            {
                int numBlocks = Math.min(wand.getMaxBlocks(stack), playerWrapper.countItems(sourceItems));
                LinkedList<Point3d> blocks = worker.getBlockPositionList(clickedPos, BetterBuildersWandsMod.directionFromSide(side), numBlocks, getMode(stack), getFaceLock(stack));

                ArrayList<Point3d> placedBlocks = worker.placeBlocks(stack, blocks, clickedPos, sourceItems, BetterBuildersWandsMod.directionFromSide(side), x, y, z);
                if (!placedBlocks.isEmpty())
                {
                    int[] placedIntArray = new int[placedBlocks.size() * 3];
                    for (int i = 0; i < placedBlocks.size(); i++)
                    {
                        Point3d currentPoint = placedBlocks.get(i);
                        placedIntArray[i * 3] = currentPoint.x;
                        placedIntArray[i * 3 + 1] = currentPoint.y;
                        placedIntArray[i * 3 + 2] = currentPoint.z;
                    }

                    NbtCompound itemNBT = stack.getStationNbt();
                    if (itemNBT == null)
                        itemNBT = new NbtCompound();
                    NbtCompound bbwCompond = new NbtCompound();

                    if(itemNBT.contains("bbw"))
                        bbwCompond = itemNBT.getCompound("bbw");
                    if (!bbwCompond.contains("mask"))
                        bbwCompond.putShort("mask", (short) this.getDefaultMode().mask);

                    bbwCompond.put("lastPlaced", placedIntArray);
                    bbwCompond.putString("lastBlock", BetterBuildersWandsMod.getModNameForItem(sourceItems.getItem()));
                    bbwCompond.putInt("lastBlockMeta", sourceItems.getDamage());
                    bbwCompond.putInt("lastPerBlock", sourceItems.count);

                    itemNBT.put("bbw", bbwCompond);
                    ((StationNBTSetter)(Object)stack).setStationNbt(itemNBT);
                }
            }
        }
        return true;
    }

    @Override
    public String[] getTooltip(ItemStack stack, String originalTooltip)
    {
        TranslationStorage translation = TranslationStorage.getInstance();
        EnumLock mode = getMode(stack);
        return new String[]
        {
            originalTooltip,
            translation.get(BetterBuildersWandsMod.LANGID + ".hover.mode." + mode.toString().toLowerCase()),
            translation.get(BetterBuildersWandsMod.LANGID + ".hover.maxblocks", wand.getMaxBlocks(stack))
        };
    }

    @Override
    public boolean postMine(ItemStack stack, int blockId, int x, int y, int z, LivingEntity miner)
    {
        stack.damage(2, miner);
        return true;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker)
    {
        stack.damage(2, attacker);
        return true;
    }

    public void setMode(ItemStack item, EnumLock mode)
    {
        NbtCompound itemNBT = item.getStationNbt();
        if (itemNBT == null)
            itemNBT = new NbtCompound();

        NbtCompound bbwCompond = new NbtCompound();
        if (itemNBT.contains("bbw"))
            bbwCompond = itemNBT.getCompound("bbw");

        short shortMask = (short) (mode.mask & 7);
        bbwCompond.putShort("mask", shortMask);

        itemNBT.put("bbw", bbwCompond);
        ((StationNBTSetter)(Object)item).setStationNbt(itemNBT);
    }

    @Override
    public EnumLock getMode(ItemStack itemStack)
    {
        if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof IWandItem)
        {
            NbtCompound itemBaseNBT = itemStack.getStationNbt();
            if (itemBaseNBT != null && itemBaseNBT.contains("bbw"))
            {
                NbtCompound itemNBT = itemBaseNBT.getCompound("bbw");
                int mask = itemNBT.contains("mask") ? itemNBT.getShort("mask") : EnumLock.NOLOCK.mask;
                return EnumLock.fromMask(mask);
            }
        }
        return getDefaultMode();
    }

    @Override
    public IWand getWand()
    {
        return this.wand;
    }

    public EnumLock getDefaultMode()
    {
        return EnumLock.NOLOCK;
    }
}
