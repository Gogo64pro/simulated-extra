package net.gogo.simulatedextra.content.motorcycle_wheel_mount;

import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlock;
import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity;
import net.gogo.simulatedextra.registers.BlockEntityTypes;
import net.minecraft.world.level.block.entity.BlockEntityType;


public class MotoWheelMountBlock extends WheelMountBlock {

    public MotoWheelMountBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType<? extends WheelMountBlockEntity> getBlockEntityType() {
        return BlockEntityTypes.MOTO_WHEEL_MOUNT.get();
    }

}