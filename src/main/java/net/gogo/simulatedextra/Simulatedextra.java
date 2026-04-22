package net.gogo.simulatedextra;

import dev.ryanhcode.offroad.Offroad;
import net.gogo.simulatedextra.content.centered_wheel_mount.CenteredWheelMountRenderer;
import net.gogo.simulatedextra.datagen.Recipe;
import net.gogo.simulatedextra.registers.BlockEntityTypes;
import net.gogo.simulatedextra.registers.BlocksReg;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.gogo.simulatedextra.registers.BlocksReg.CENTERED_WHEEL_MOUNT_ITEM;

@Mod(Simulatedextra.ID)
public class Simulatedextra {

    public static final String ID = "simulatedextra";

    // the logger for our mod
    public static final Logger LOGGER = LogManager.getLogger(ID);


    public Simulatedextra(IEventBus modEventBus) {
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onServerSetup);
        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onDataSetup);
        BlocksReg.BLOCKS.register(modEventBus);
        BlocksReg.ITEMS.register(modEventBus);
        BlockEntityTypes.BLOCK_ENTITY_TYPES.register(modEventBus);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(
                BlockEntityTypes.CENTERED_WHEEL_MOUNT.get(),
                CenteredWheelMountRenderer::new
        );
    }
    private void onServerSetup(FMLDedicatedServerSetupEvent event) {
        LOGGER.log(Level.INFO, "Server starting...");
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        Offroad.getRegistrate().addExtraItem(CENTERED_WHEEL_MOUNT_ITEM.getId());
    }
    private void onDataSetup(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeServer(),
                new Recipe(generator.getPackOutput(), event.getLookupProvider()));
    }
}