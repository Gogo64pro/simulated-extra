package net.gogo.simulatedextra.content.motorcycle_wheel_mount;

import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MotoWheelMountBlockEntity extends WheelMountBlockEntity {


    public static final double FORK_FORWARD = 10.0 / 16.0;
    public static final double FORK_DOWN    = 14.0 / 16.0;
    public static final double FORK_RAKE_RAD = Math.toRadians(25.0);


    private double forkCompression     = 0.0;
    private double lastForkCompression = 0.0;

    public MotoWheelMountBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public double getForkForwardOffset() { return FORK_FORWARD; }
    public double getForkDownOffset()    { return FORK_DOWN;    }
    public double getForkAngleRad()      { return FORK_RAKE_RAD; }


    public double getLerpedForkCompression(float partialTick) {
        return Mth.lerp(partialTick, lastForkCompression, forkCompression);
    }

    // ------------------------------------------------------------------

    @Override
    public void tick() {
        super.tick();

        lastForkCompression = forkCompression;

        if (!this.level.isClientSide) return;

        final double verticalExtension = getLerpedExtension(1.0f);

//        final double forkRestLength = Math.sqrt(FORK_FORWARD * FORK_FORWARD + FORK_DOWN * FORK_DOWN);
        final double compressionRaw = (FORK_DOWN - verticalExtension) * Math.cos(FORK_RAKE_RAD);
        forkCompression = Mth.clamp(compressionRaw, -0.1, 0.25);
    }

    @Override
    public double getLerpedYaw(double partialTick) {
        return super.getLerpedYaw(partialTick);
    }
}