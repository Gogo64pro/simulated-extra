package net.gogo.simulatedextra.registers;

import net.gogo.simulatedextra.Simulatedextra;
import net.gogo.simulatedextra.content.centered_wheel_mount.CenteredWheelMountBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Simulatedextra.ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CenteredWheelMountBlockEntity>>
            CENTERED_WHEEL_MOUNT = BLOCK_ENTITY_TYPES.register("centered_wheel_mount", () -> {
        // Use an array trick so the supplier can reference the holder (W ai)
        BlockEntityType<?>[] ref = new BlockEntityType[1];
        ref[0] = BlockEntityType.Builder.of(
                        (pos, state) -> new CenteredWheelMountBlockEntity(
                                ref[0], pos, state),
                        BlocksReg.CENTERED_WHEEL_MOUNT.get())
                .build(null);
        return (BlockEntityType<CenteredWheelMountBlockEntity>) ref[0];
    });

}
