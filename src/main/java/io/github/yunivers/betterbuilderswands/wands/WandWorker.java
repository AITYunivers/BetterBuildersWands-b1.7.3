package io.github.yunivers.betterbuilderswands.wands;

import io.github.yunivers.betterbuilderswands.BetterBuildersWandsMod;
import io.github.yunivers.betterbuilderswands.util.EnumLock;
import io.github.yunivers.betterbuilderswands.util.Point3d;
import io.github.yunivers.betterbuilderswands.wrappers.*;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec3i;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class WandWorker
{
    private final IWand wand;
    private final IPlayerWrapper player;
    private final IWorldWrapper world;

    HashSet<Point3d> allCandidates = new HashSet<Point3d>();

    public WandWorker(IWand wand, IPlayerWrapper player, IWorldWrapper world)
    {
        this.wand = wand;
        this.player = player;
        this.world = world;
    }

    public ItemStack getProperItemStack(IWorldWrapper world, IPlayerWrapper player, Point3d blockPos)
    {
        Block block = world.getBlock(blockPos);
        int meta = world.getMetadata(blockPos);
        ItemStack exactItemstack = new ItemStack(block, 1, meta);
        if (player.countItems(exactItemstack) > 0)
            return exactItemstack;
        return getEquivalentItemStack(blockPos);
    }

    public ItemStack getEquivalentItemStack(Point3d blockPos)
    {
        Block block = world.getBlock(blockPos);
        int meta = world.getMetadata(blockPos);
        ItemStack stack = null;
        if (block.id == Block.DOUBLE_SLAB.id)
            stack = new ItemStack(Block.SLAB, 2, meta);
        else
        {
            Item blockItem = Item.BLOCK_ITEMS.get(block);
            if (blockItem != null)
                stack = new ItemStack(blockItem, 1, meta);
        }
        return stack;

        // -- Old Code --

        /*Block block = world.getBlock(blockPos);
        int meta = world.getMetadata(blockPos);
        ItemStack stack = null;
        Item dropped = Item.ITEMS[block.getDroppedItemId(meta, new Random())];
        if (dropped != null)
            stack = new ItemStack(dropped, block.getDroppedItemCount(new Random()), block.getDroppedItemMeta(meta));
        return stack;*/
    }

    private boolean shouldContinue(Point3d currentCandidate, Block targetBlock, int targetMetadata, Direction facing, Block candidateSupportingBlock, int candidateSupportingMeta, Box blockBB)
    {
        //if (!world.blockIsAir(currentCandidate))
        //    return false;
        if (!targetBlock.equals(candidateSupportingBlock))
            return false;
        if (targetMetadata != candidateSupportingMeta)
            return false;
        if (!targetBlock.canPlaceAt(world.getWorld(), currentCandidate.x, currentCandidate.y, currentCandidate.z))
            return false;
        // TODO: Fix this!!!
//      Vec3i hitVec = facing.getVector().add(currentCandidate.x, currentCandidate.y, currentCandidate.z);
//      if (!targetBlock.canReplace(world.getWorld().getBlockState(currentCandidate.toBlockPos()),
//              new ItemPlacementContext(Minecraft.INSTANCE.player, new ItemStack(candidateSupportingBlock, 1, candidateSupportingMeta),
//              new HitResult(currentCandidate.x, currentCandidate.y, currentCandidate.z, facing.ordinal(),
//              Vec3d.create(hitVec.getX(), hitVec.getY(), hitVec.getZ())))))
//          return false;

        return !world.entitiesInBox(blockBB);
    }

    public LinkedList<Point3d> getBlockPositionList(Point3d blockLookedAt, Direction placeDirection, int maxBlocks, EnumLock directionLock, EnumLock faceLock)
    {
        LinkedList<Point3d> candidates = new LinkedList<Point3d>();
        LinkedList<Point3d> toPlace = new LinkedList<Point3d>();

        Block targetBlock = world.getBlock(blockLookedAt);
        int targetMetadata = world.getMetadata(blockLookedAt);
        Point3d startingPoint = blockLookedAt.move(placeDirection);

        int directionMaskInt = directionLock.mask;
        int faceMaskInt = faceLock.mask;

        if (((directionLock != EnumLock.HORIZONTAL && directionLock != EnumLock.VERTICAL) || (placeDirection != Direction.UP && placeDirection != Direction.DOWN))
                && (directionLock != EnumLock.NORTHSOUTH || (placeDirection != Direction.NORTH && placeDirection != Direction.SOUTH))
                && (directionLock != EnumLock.EASTWEST || (placeDirection != Direction.EAST && placeDirection != Direction.WEST)))
            candidates.add(startingPoint);
        while (!candidates.isEmpty() && toPlace.size() < maxBlocks)
        {
            Point3d currentCandidate = candidates.removeFirst();

            Point3d supportingPoint = currentCandidate.move(placeDirection.getOpposite());
            Block candidateSupportingBlock = world.getBlock(supportingPoint);
            int candidateSupportingMeta = world.getMetadata(supportingPoint);
            Box blockBB = targetBlock.getBoundingBox(world.getWorld(), currentCandidate.x, currentCandidate.y, currentCandidate.z);
            if (shouldContinue(currentCandidate, targetBlock, targetMetadata, placeDirection, candidateSupportingBlock, candidateSupportingMeta, blockBB)
                    && allCandidates.add(currentCandidate))
            {
                toPlace.add(currentCandidate);

                switch (placeDirection)
                {
                    case DOWN:
                    case UP:
                        if ((faceMaskInt & EnumLock.UP_DOWN_MASK) > 0)
                        {
                            if ((directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0)
                                candidates.add(currentCandidate.move(Direction.NORTH));
                            if ((directionMaskInt & EnumLock.EAST_WEST_MASK) > 0)
                                candidates.add(currentCandidate.move(Direction.EAST));
                            if ((directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0)
                                candidates.add(currentCandidate.move(Direction.SOUTH));
                            if ((directionMaskInt & EnumLock.EAST_WEST_MASK) > 0)
                                candidates.add(currentCandidate.move(Direction.WEST));
                            if ((directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0 && (directionMaskInt & EnumLock.EAST_WEST_MASK) > 0)
                            {
                                candidates.add(currentCandidate.move(Direction.NORTH).move(Direction.EAST));
                                candidates.add(currentCandidate.move(Direction.NORTH).move(Direction.WEST));
                                candidates.add(currentCandidate.move(Direction.SOUTH).move(Direction.EAST));
                                candidates.add(currentCandidate.move(Direction.SOUTH).move(Direction.WEST));
                            }
                        }
                        break;
                    case NORTH:
                    case SOUTH:
                        if ((faceMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0)
                        {
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0)
                                candidates.add(currentCandidate.move(Direction.UP));
                            if ((directionMaskInt & EnumLock.EAST_WEST_MASK) > 0)
                                candidates.add(currentCandidate.move(Direction.EAST));
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0)
                                candidates.add(currentCandidate.move(Direction.DOWN));
                            if ((directionMaskInt & EnumLock.EAST_WEST_MASK) > 0)
                                candidates.add(currentCandidate.move(Direction.WEST));
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0 && (directionMaskInt & EnumLock.EAST_WEST_MASK) > 0)
                            {
                                candidates.add(currentCandidate.move(Direction.UP).move(Direction.EAST));
                                candidates.add(currentCandidate.move(Direction.UP).move(Direction.WEST));
                                candidates.add(currentCandidate.move(Direction.DOWN).move(Direction.EAST));
                                candidates.add(currentCandidate.move(Direction.DOWN).move(Direction.WEST));
                            }
                        }
                        break;
                    case WEST:
                    case EAST:
                        if ((faceMaskInt & EnumLock.EAST_WEST_MASK) > 0)
                        {
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0)
                                candidates.add(currentCandidate.move(Direction.UP));
                            if ((directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0)
                                candidates.add(currentCandidate.move(Direction.NORTH));
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0)
                                candidates.add(currentCandidate.move(Direction.DOWN));
                            if ((directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0)
                                candidates.add(currentCandidate.move(Direction.SOUTH));
                            if ((directionMaskInt & EnumLock.UP_DOWN_MASK) > 0 && (directionMaskInt & EnumLock.NORTH_SOUTH_MASK) > 0)
                            {
                                candidates.add(currentCandidate.move(Direction.UP).move(Direction.NORTH));
                                candidates.add(currentCandidate.move(Direction.UP).move(Direction.SOUTH));
                                candidates.add(currentCandidate.move(Direction.DOWN).move(Direction.NORTH));
                                candidates.add(currentCandidate.move(Direction.DOWN).move(Direction.SOUTH));
                            }
                        }
                }
            }
        }
        return toPlace;
    }

    public ArrayList<Point3d> placeBlocks(ItemStack wandItem, LinkedList<Point3d> blockPosList, Point3d originalBlock, ItemStack sourceItems, Direction side, float hitX, float hitY, float hitZ)
    {
        ArrayList<Point3d> placedBlocks = new ArrayList<Point3d>();
        for (Point3d blockPos : blockPosList)
        {
            boolean blockPlaceSuccess;
            blockPlaceSuccess = world.copyBlock(originalBlock, blockPos);

            if (blockPlaceSuccess)
            {
                world.playPlaceAtBlock(blockPos, world.getBlock(originalBlock));
                placedBlocks.add(blockPos);
                if (!player.isCreative())
                    wand.placeBlock(wandItem, player.getPlayer());
                boolean takeFromInventory = player.useItem(sourceItems);
                if (!takeFromInventory)
                {
                    BetterBuildersWandsMod.LOGGER.info("BBW takeback: {}", blockPos.toString());
                    world.setBlockToAir(blockPos);
                    placedBlocks.remove(placedBlocks.size() - 1);
                }
            }
        }

        return placedBlocks;
    }
}
