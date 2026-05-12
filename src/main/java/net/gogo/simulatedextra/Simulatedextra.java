package net.gogo.simulatedextra;

import com.simibubi.create.foundation.data.CreateRegistrate;
import dev.simulated_team.simulated.registrate.SimulatedRegistrate;
import net.gogo.simulatedextra.content.centered_wheel_mount.CenteredWheelMountRenderer;
import net.gogo.simulatedextra.content.chained_centered_wheel_mount.ChainDrivablePartialModels;
import net.gogo.simulatedextra.registers.BlockEntityTypes;
import net.gogo.simulatedextra.registers.BlocksReg;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.gogo.simulatedextra.registers.BlocksReg.CENTERED_WHEEL_MOUNT;

@Mod(Simulatedextra.ID)
public class Simulatedextra {

    public static final String ID = "simulatedextra";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID).defaultCreativeTab((ResourceKey<CreativeModeTab>) null);

    // the logger for our mod
    public static final Logger LOGGER = LogManager.getLogger(ID);


    public Simulatedextra(IEventBus modEventBus) {
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onServerSetup);
        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onCreative);
        REGISTRATE.registerEventListeners(modEventBus);
        BlocksReg.register();
        BlockEntityTypes.register();
        ChainDrivablePartialModels.init();
        SimulatedRegistrate.TAB_ITEMS.add(CENTERED_WHEEL_MOUNT::asItem);
        SimulatedRegistrate.ITEM_TO_SECTION.put(
                ResourceLocation.fromNamespaceAndPath(Simulatedextra.ID, "centered_wheel_mount"),
                ResourceLocation.fromNamespaceAndPath("offroad", "offroad")
        );

    }

    public static ResourceLocation path(String s) {
        return ResourceLocation.fromNamespaceAndPath(ID, s);
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

    }
    private void onCreative(BuildCreativeModeTabContentsEvent event){

    }
}