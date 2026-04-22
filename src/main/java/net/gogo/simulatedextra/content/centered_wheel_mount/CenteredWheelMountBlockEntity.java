package net.gogo.simulatedextra.content.centered_wheel_mount;

import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CenteredWheelMountBlockEntity extends WheelMountBlockEntity {

    public CenteredWheelMountBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public double getLerpedYaw(double partialTick) {
        return super.getLerpedYaw(partialTick);
    }
    public int getClientSteeringSignalLeft() {
        return clientSteeringSignalLeft;
    }
    public int getClientSteeringSignalRight() {
        return clientSteeringSignalRight;
    }
}

