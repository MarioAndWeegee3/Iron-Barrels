package marioandweegee3.ironbarrels;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import marioandweegee3.ironbarrels.blocks.Barrels;
import marioandweegee3.ironbarrels.blocks.BigBarrel;
import marioandweegee3.ironbarrels.container.ScrollingContainer;
import marioandweegee3.ironbarrels.container.ScrollingScreen;
import marioandweegee3.ironbarrels.items.Kits;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class IronBarrels implements ModInitializer, ClientModInitializer{
    public static final String modid = "ironbarrels";
    public static final Logger logger = LogManager.getLogger(modid);

    @Override
    public void onInitialize() {
        Barrels.registerBlocks();

        Kits.init();

        ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier("ironbarrels", "scrollbarrel"), ((syncId, identifier, player, buf) -> {
			BlockPos pos = buf.readBlockPos();
			Component containerName = buf.readTextComponent();
			World world = player.getEntityWorld();
			return new ScrollingContainer(syncId, player.inventory, BigBarrel.getInventoryStatic(world.getBlockState(pos), world, pos), containerName);
        }));
    }

    public static void register(Block block, String name, ItemGroup group){
        Registry.BLOCK.add(makeID(name), block);
        register(new BlockItem(block, new Item.Settings().group(group)), name);
    }

    public static void register(Item item, String name){
        Registry.ITEM.add(makeID(name), item);
    }

    public static BigBarrel registerBarrel(Block source, String material, int numRows){
        BigBarrel barrel = new BigBarrel(FabricBlockSettings.copy(source).hardness(4).build(), material, numRows);
        register(barrel, material + "_barrel", ItemGroup.DECORATIONS);
        return barrel;
    }

    public static Identifier makeID(String name){
        return new Identifier(modid, name);
    }

    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(new Identifier("ironbarrels", "scrollbarrel"), ScrollingScreen::createScreen);
    }
}