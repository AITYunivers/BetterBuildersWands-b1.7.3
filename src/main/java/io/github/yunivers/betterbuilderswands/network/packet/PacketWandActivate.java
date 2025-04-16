package io.github.yunivers.betterbuilderswands.network.packet;

import io.github.yunivers.betterbuilderswands.BetterBuildersWandsMod;
import io.github.yunivers.betterbuilderswands.item.IWandItem;
import io.github.yunivers.betterbuilderswands.wands.IWand;
import lombok.SneakyThrows;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class PacketWandActivate extends Packet
{
    @Override
    public void read(DataInputStream stream)
    {

    }

    @Override
    public void write(DataOutputStream stream)
    {

    }

    @Override
    public void apply(NetworkHandler networkHandler)
    {
        if (networkHandler instanceof ServerPlayNetworkHandler serverNetworkHandler)
        {
            ServerPlayerEntity player = serverNetworkHandler.player;
            ItemStack heldItem = player.inventory.getSelectedItem();
            if (heldItem != null && heldItem.getItem() instanceof IWandItem wandItem)
            {
                TranslationStorage translation = TranslationStorage.getInstance();
                wandItem.nextMode(heldItem, player);
                player.sendMessage(translation.get(BetterBuildersWandsMod.LANGID + ".hover.mode." + wandItem.getMode(heldItem).toString().toLowerCase()));
            }
        }
    }

    @Override
    public int size() {
        return 0;
    }
}
