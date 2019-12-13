package marioandweegee3.ironbarrels2.client;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import marioandweegee3.ironbarrels2.IronBarrels;
import marioandweegee3.ironbarrels2.client.gui.BigBarrelController;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.container.BlockContext;

public class IronBarrelsClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(IronBarrels.BIG_BARREL_ID,
            (syncId, id, player, buf) -> {
                return new CottonInventoryScreen<BigBarrelController>(
                    new BigBarrelController(syncId, player.inventory, buf.readInt(), buf.readInt(), BlockContext.create(player.world, buf.readBlockPos())),
                    player
                );
            }
        );
    }

}