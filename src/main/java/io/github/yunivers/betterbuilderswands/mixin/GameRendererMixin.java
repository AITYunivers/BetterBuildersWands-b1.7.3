package io.github.yunivers.betterbuilderswands.mixin;

import io.github.yunivers.betterbuilderswands.BetterBuildersWandsMod;
import io.github.yunivers.betterbuilderswands.item.IWandItem;
import io.github.yunivers.betterbuilderswands.util.Point3d;
import io.github.yunivers.betterbuilderswands.wands.IWand;
import io.github.yunivers.betterbuilderswands.wands.WandWorker;
import io.github.yunivers.betterbuilderswands.wrappers.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResultType;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
    @Inject(
        method = "renderFrame",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/WorldRenderer;renderBlockOutline(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemStack;F)V",
            shift = At.Shift.AFTER
        )
    )
    public void renderBorders(float tickDelta, long time, CallbackInfo ci)
    {
        GameRenderer renderer = (GameRenderer)(Object)this;
        PlayerEntity player = (PlayerEntity)renderer.client.camera;
        HitResult initialResult = renderer.client.crosshairTarget;
        ItemStack heldStack = player.inventory.getSelectedItem();
        if (heldStack != null && heldStack.getItem() instanceof IWandItem wandItem && initialResult.type == HitResultType.BLOCK)
        {
            IPlayerWrapper playerWrapper = new BasicPlayerWrapper(player);
            if (FabricLoader.getInstance().isModLoaded("bhcreative") && player.creative_isCreative())
                playerWrapper = new CreativePlayerWrapper(player);

            IWorldWrapper worldWrapper = new BasicWorldWrapper(player.world);
            IWand wand = wandItem.getWand();
            WandWorker worker = new WandWorker(wand, playerWrapper, worldWrapper);
            Point3d clickedPos = new Point3d(initialResult.blockX, initialResult.blockY, initialResult.blockZ);
            ItemStack sourceItems = worker.getProperItemStack(worldWrapper, playerWrapper, clickedPos);
            if (sourceItems != null && sourceItems.getItem() instanceof BlockItem)
            {
                int numBlocks = Math.min(wand.getMaxBlocks(heldStack), playerWrapper.countItems(sourceItems));
                LinkedList<Point3d> blocks = worker.getBlockPositionList(clickedPos, BetterBuildersWandsMod.directionFromSide(initialResult.side), numBlocks, wandItem.getMode(heldStack), wandItem.getFaceLock(heldStack));
                WorldRenderer worldRenderer = renderer.client.worldRenderer;
                for (Point3d block : blocks)
                {
                    // Copied from WorldRenderer.renderBlockOutline
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.4F);
                    GL11.glLineWidth(2.0F);
                    GL11.glDisable(3553);
                    GL11.glDepthMask(false);
                    float expandBy = 0.002F;
                    // Using DIRT bc fuck you I'm lazy
                    double xDelta = player.lastTickX + (player.x - player.lastTickX) * (double) tickDelta;
                    double yDelta = player.lastTickY + (player.y - player.lastTickY) * (double) tickDelta;
                    double zDelta = player.lastTickZ + (player.z - player.lastTickZ) * (double) tickDelta;
                    worldRenderer.renderOutline(Block.DIRT.getBoundingBox(player.world, block.x, block.y, block.z).expand(expandBy, expandBy, expandBy).offset(-xDelta, -yDelta, -zDelta));

                    GL11.glDepthMask(true);
                    GL11.glEnable(3553);
                    GL11.glDisable(3042);
                }
            }
        }
    }
}
