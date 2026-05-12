package net.gogo.simulatedextra.content.chained_centered_wheel_mount;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.actors.roller.RollerBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.gogo.simulatedextra.content.centered_wheel_mount.CenteredWheelMountBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ChainDrivableWheelMountBlockEntity extends CenteredWheelMountBlockEntity {

    private static final MutableComponent MODE_TITLE = Component.literal("Turn mode");

    // Mode 0 = redstone (superclass behavior), Mode 1 = shaft driven
    private static final int MODE_REDSTONE = 0;
    private static final int MODE_SHAFT = 1;

    private ModeSelectorBehaviour mode;

    // Shaft-driven yaw accumulator
    private double shaftYaw = 0.0;
    private double lastShaftYaw = 0.0;

    //// Track shaft angle from last tick to compute delta
    //private float lastKineticAngle = Float.NaN;

    public ChainDrivableWheelMountBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(this.mode = new ModeSelectorBehaviour(MODE_TITLE, this, new ModeValueBox()));
        this.mode.value = MODE_REDSTONE;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getMode() == MODE_SHAFT) {
            this.lastShaftYaw = this.shaftYaw;

            final float speed = this.getSpeed();
            final double radPerTick = speed * Math.PI * 2.0 / 60.0 / 20.0;

            this.shaftYaw += radPerTick;
        }
    }

    @Override
    public double getChasingYaw() {
        if (this.getMode() == MODE_SHAFT) {
            return this.shaftYaw;
        }
        return super.getChasingYaw();
    }

    @Override
    public double getLerpedYaw(double partialTick) {
        if (this.getMode() == MODE_SHAFT) {
            return Mth.lerp(partialTick, this.lastShaftYaw, this.shaftYaw);
        }
        return super.getLerpedYaw(partialTick);
    }

    private int getMode() {
        return this.mode != null ? this.mode.getValue() : MODE_REDSTONE;
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putDouble("ShaftYaw", this.shaftYaw);
        tag.putDouble("LastShaftYaw", this.lastShaftYaw);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        this.shaftYaw = tag.getDouble("ShaftYaw");
        this.lastShaftYaw = tag.getDouble("LastShaftYaw");
    }

    private static final class ModeSelectorBehaviour extends ScrollValueBehaviour {
        public ModeSelectorBehaviour(Component label, SmartBlockEntity be, ValueBoxTransform slot) {
            super(label, be, slot);
            this.between(MODE_REDSTONE, MODE_SHAFT);
        }

        @Override
        public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
            return new ValueSettingsBoard(
                    this.label, MODE_SHAFT, 1,
                    ImmutableList.of(
                            Component.literal("Redstone"),
                            Component.literal("Shaft")
                    ),
                    new ValueSettingsFormatter(settings -> Component.literal(
                            settings.value() == MODE_REDSTONE
                                    ? "Redstone"
                                    : "Shaft"
                    ))
            );
        }

        @Override
        public int netId() {
            return 3;
        }
    }
    private static final class ModeValueBox extends ValueBoxTransform {
        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
            final Direction facing = state.getValue(RollerBlock.FACING);
            final float yRot = AngleHelper.horizontalAngle(facing) + 180;
            TransformStack.of(ms)
                    .rotateYDegrees(yRot)
                    .rotateXDegrees(90);
        }

        @Override
        public boolean testHit(LevelAccessor level, BlockPos pos, BlockState state, Vec3 localHit) {
            final Vec3 offset = this.getLocalOffset(level, pos, state);
            if (offset == null) return false;
            return localHit.distanceTo(offset) < this.scale / 3;
        }

        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            final Direction facing = state.getValue(RollerBlock.FACING);
            final float stateAngle = AngleHelper.horizontalAngle(facing) + 180;
            // Offset slightly from the existing suspension strength box (hOffset=2 to avoid overlap)
            return VecHelper.rotateCentered(VecHelper.voxelSpace(10, 15.5f, 11), stateAngle, Direction.Axis.Y);
        }
    }
}