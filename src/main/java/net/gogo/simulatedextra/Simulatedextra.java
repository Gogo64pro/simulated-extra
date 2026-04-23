package net.gogo.simulatedextra;

import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CStress;
import dev.ryanhcode.offroad.Offroad;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.createmod.catnip.lang.FontHelper;
import net.gogo.simulatedextra.content.centered_wheel_mount.CenteredWheelMountRenderer;
import net.gogo.simulatedextra.datagen.Recipe;
import net.gogo.simulatedextra.registers.BlockEntityTypes;
import net.gogo.simulatedextra.registers.BlocksReg;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
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
        NeoForge.EVENT_BUS.addListener(this::onTooltip);
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
    public void onTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();

        if (item == BlocksReg.CENTERED_WHEEL_MOUNT_ITEM.get()) {
            TooltipModifier modifier = new ItemDescription
                    .Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item)));

            modifier.modify(event);
        }
    }}