package io.github.yunivers.betterbuilderswands.events.init;

import io.github.yunivers.betterbuilderswands.BetterBuildersWandsMod;
import io.github.yunivers.betterbuilderswands.item.IWandItem;
import io.github.yunivers.betterbuilderswands.network.packet.PacketWandActivate;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.MultiplayerClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import org.lwjgl.input.Keyboard;

public class InitKeybinds
{
    // Keybinds
    public static KeyBinding cycleMode;

    @EventListener
    public void registerKeybinds(KeyBindingRegisterEvent event)
    {
        cycleMode = new KeyBinding("bbw.key.mode", Keyboard.KEY_M);

        event.keyBindings.add(cycleMode);
    }

    // Keybind Data
    private static boolean cycleModePressed;

    @EventListener
    public void handle(KeyStateChangedEvent event)
    {
        if (Keyboard.getEventKeyState())
        {
            if (Keyboard.isKeyDown(cycleMode.code))
            {
                if (event.environment == KeyStateChangedEvent.Environment.IN_GAME)
                {
                    if (Minecraft.INSTANCE.world.isRemote)
                    {
                        PacketWandActivate packet = new PacketWandActivate();
                        ((MultiplayerClientPlayerEntity)Minecraft.INSTANCE.player).networkHandler.sendPacket(packet);
                    }
                    else
                    {
                        ClientPlayerEntity player = Minecraft.INSTANCE.player;
                        ItemStack heldItem = player.inventory.getSelectedItem();
                        if (heldItem != null && heldItem.getItem() instanceof IWandItem wandItem)
                        {
                            TranslationStorage translation = TranslationStorage.getInstance();
                            wandItem.nextMode(heldItem, player);
                            player.sendMessage(translation.get(BetterBuildersWandsMod.LANGID + ".hover.mode." + wandItem.getMode(heldItem).toString().toLowerCase()));
                        }
                    }
                }
            }
        }
    }
}
