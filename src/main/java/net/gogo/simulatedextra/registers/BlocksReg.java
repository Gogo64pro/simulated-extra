package net.gogo.simulatedextra.registers;

import com.simibubi.create.infrastructure.config.CStress;
import dev.ryanhcode.offroad.Offroad;
import net.gogo.simulatedextra.Simulatedextra;
import net.gogo.simulatedextra.content.centered_wheel_mount.CenteredWheelMountBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class BlocksReg {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(BuiltInRegistries.BLOCK, Simulatedextra.ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, Simulatedextra.ID);

    public static final DeferredHolder<Block, CenteredWheelMountBlock> CENTERED_WHEEL_MOUNT =
            BLOCKS.register("centered_wheel_mount",
                    () -> new CenteredWheelMountBlock(BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_GRAY)
                            .noOcclusion()
                            .isRedstoneConductor((state, level, pos) -> false)
                            .strength(3.5f, 3.5f)
                            .sound(SoundType.STONE)
                            .requiresCorrectToolForDrops()
                    ));

    public static final DeferredHolder<Item, BlockItem> CENTERED_WHEEL_MOUNT_ITEM =
            ITEMS.register("centered_wheel_mount",
                    () -> new BlockItem(CENTERED_WHEEL_MOUNT.get(), new Item.Properties()));


    public static void init() {}
}
