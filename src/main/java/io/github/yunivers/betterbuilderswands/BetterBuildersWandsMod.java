package io.github.yunivers.betterbuilderswands;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

public class BetterBuildersWandsMod {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @SuppressWarnings("UnstableApiUsage")
    public static final Namespace NAMESPACE = Namespace.resolve();

    public static final Logger LOGGER = NAMESPACE.getLogger();

    public static final String LANGID = "bbw";

    // Stolen from Always More Items
    public static String getModNameForItem(@Nullable Item item)
    {
        Identifier identifier = ItemRegistry.INSTANCE.getId(item);
        if (identifier == null) {
            LOGGER.error("Item has no identifier?", new NullPointerException());
            return "";
        }
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(identifier.namespace.toString());
        if (modContainer.isEmpty()) {
            LOGGER.error("Mod namespace has no container", new NullPointerException());
            return "";
        }
        return modContainer.get().getMetadata().getName();
    }

    public static Direction directionFromSide(int side)
    {
        return switch (side)
        {
            case 0 -> Direction.DOWN;
            case 1 -> Direction.UP;
            case 2 -> Direction.NORTH;
            case 3 -> Direction.SOUTH;
            case 4 -> Direction.WEST;
            case 5 -> Direction.EAST;
            default -> throw new IllegalStateException("Unexpected value: " + side);
        };
    }
}
