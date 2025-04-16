package io.github.yunivers.betterbuilderswands.wrappers;

import io.github.yunivers.betterbuilderswands.util.Point3d;
import net.minecraft.block.Block;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public interface IWorldWrapper
{
    Block getBlock(Point3d point);
    boolean blockIsAir(Point3d point);

    World getWorld();

    boolean copyBlock(Point3d originalBlock, Point3d blockPos);

    void setBlockToAir(Point3d blockPos);

    int getMetadata(Point3d blockPos);

    boolean entitiesInBox(Box box);

    void playPlaceAtBlock(Point3d position, Block blockType);

    boolean setBlock(Point3d position, Block placeBlock, int placeMeta);
}
