package io.github.yunivers.betterbuilderswands.wrappers;

import io.github.yunivers.betterbuilderswands.util.Point3d;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.item.ItemUsageContext;

import java.util.List;

public class BasicWorldWrapper implements IWorldWrapper
{
    private World world;

    public BasicWorldWrapper(World world)
    {
        this.world = world;
    }

    @Override
    public Block getBlock(Point3d point)
    {
        if (world != null)
            return Block.BLOCKS[world.getBlockId(point.x, point.y, point.z)];
        return null;
    }

    @Override
    public boolean blockIsAir(Point3d point)
    {
        return world.isAir(point.x, point.y, point.z);
    }

    @Override
    public World getWorld()
    {
        return world;
    }

    @Override
    public boolean copyBlock(Point3d originalBlock, Point3d blockPos)
    {
        int originalBlockId = world.getBlockId(originalBlock.x, originalBlock.y, originalBlock.z);

        // This is required to update the chunk
        world.setBlock(blockPos.x, blockPos.y, blockPos.z, originalBlockId, world.getBlockMeta(originalBlock.x, originalBlock.y, originalBlock.z));
        if (world.getBlockId(blockPos.x, blockPos.y, blockPos.z) == originalBlockId)
        {
            BlockState blockState = world.getBlockState(originalBlock.toBlockPos());
            world.setBlockState(blockPos.toBlockPos(), blockState);
        }
        return true;
    }

    @Override
    public void setBlockToAir(Point3d blockPos)
    {
        world.setBlock(blockPos.x, blockPos.y, blockPos.z, 0);
    }

    @Override
    public int getMetadata(Point3d point)
    {
        if (world != null)
            return world.getBlockMeta(point.x, point.y, point.z);
        return 0;
    }

    @Override
    public boolean entitiesInBox(Box box)
    {
        if (box == null)
            return false;

        return !world.getEntities(null, box).isEmpty();
    }

    @Override
    public void playPlaceAtBlock(Point3d position, Block blockType)
    {
        if (position != null && blockType != null)
            world.playSound(position.x + 0.5D, position.y + 0.5D, position.z + 0.5D, blockType.soundGroup.getSound(), (blockType.soundGroup.getVolume() + 1.0F) / 2.0F, blockType.soundGroup.getPitch() * 0.8F);
    }

    @Override
    public boolean setBlock(Point3d position, Block block, int meta)
    {
        return world.setBlock(position.x, position.y, position.z, block.id, meta);
    }
}
