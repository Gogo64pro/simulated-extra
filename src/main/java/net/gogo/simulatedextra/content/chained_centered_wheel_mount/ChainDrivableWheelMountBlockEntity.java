package net.gogo.simulatedextra.content.chained_centered_wheel_mount;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.actors.roller.RollerBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity;
import dev.simulated_team.simulated.util.extra_kinetics.ExtraBlockPos;
import dev.simulated_team.simulated.util.extra_kinetics.ExtraKinetics;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ChainDrivableWheelMountBlockEntity extends WheelMountBlockEntity implements ExtraKinetics {

    private static final MutableComponent MODE_TITLE = Component.translatable("block.simulatedextra.chain_drivable_wheel_mount.mode");

    private static final int MODE_REDSTONE = 0;
    private static final int MODE_SHAFT    = 1;

    private ModeSelectorBehaviour mode;
    public final SteeringShaftBlockEntity steeringShaft;

    private double shaftYaw     = 0.0;
    private double lastShaftYaw = 0.0;

    public ChainDrivableWheelMountBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.steeringShaft = new SteeringShaftBlockEntity(type, pos, state, this);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(this.mode = new ModeSelectorBehaviour(MODE_TITLE, this, new ModeValueBox()));
        this.mode.value = MODE_REDSTONE;
    }


    public int getOffset(){
        return 7;
        //TODO ADD THE VALUEBOX
    }

    // --- ExtraKinetics ---

    @Override
    public KineticBlockEntity getExtraKinetics() {
        return this.steeringShaft;
    }

    @Override
    public boolean shouldConnectExtraKinetics() {
        return false;
    }

    @Override
    public String getExtraKineticsSaveName() {
        return "SteeringShaft";
    }

    // --- Tick ---

    @Override
    public void tick() {
        super.tick();
        this.steeringShaft.tick();

        if (this.getMode() == MODE_SHAFT) {
            this.lastShaftYaw = this.shaftYaw;
            // RPM → rad/tick
            final double radPerTick = this.steeringShaft.getSpeed() * Math.PI * 2.0 / 60.0 / 20.0;
            this.shaftYaw += radPerTick;
        }
    }

    // --- Yaw overrides ---

    @Override
    protected double getChasingYaw() {
        if (this.getMode() == MODE_SHAFT) {
            return this.shaftYaw;
        }
        return super.getChasingYaw();
    }

    @Override
    protected double getLerpedYaw(double partialTick) {
        if (this.getMode() == MODE_SHAFT) {
            return Mth.lerp(partialTick, this.lastShaftYaw, this.shaftYaw);
        }
        return super.getLerpedYaw(partialTick);
    }

    private int getMode() {
        return this.mode != null ? this.mode.getValue() : MODE_REDSTONE;
    }

    // --- NBT ---

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);
        tag.putDouble("ShaftYaw",     this.shaftYaw);
        tag.putDouble("LastShaftYaw", this.lastShaftYaw);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);
        this.shaftYaw     = tag.getDouble("ShaftYaw");
        this.lastShaftYaw = tag.getDouble("LastShaftYaw");
    }

    // --- Inner steering shaft BE ---

    public static final class SteeringShaftBlockEntity extends KineticBlockEntity implements ExtraKineticsBlockEntity {
        public static final IRotate ROTATION_CONFIG = new IRotate() {
            @Override
            public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
                return face == Direction.UP;
            }

            @Override
            public Direction.Axis getRotationAxis(BlockState state) {
                return Direction.Axis.Y;
            }
        };
        private final ChainDrivableWheelMountBlockEntity parent;

        public SteeringShaftBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state,
                                        ChainDrivableWheelMountBlockEntity parent) {
            super(type, new ExtraBlockPos(pos), state);
            this.parent = parent;
        }

        @Override
        public KineticBlockEntity getParentBlockEntity() {
            return this.parent;
        }

        @Override
        public Component getKey() {
            return Component.translatable("block.simulatedextra.chain_drivable_wheel_mount.steering_shaft");
        }
    }

    // --- Mode scroll behaviour ---

    private static final class ModeSelectorBehaviour extends ScrollValueBehaviour {
        public ModeSelectorBehaviour(Component label, SmartBlockEntity be, ValueBoxTransform slot) {
            super(label, be, slot);
            this.between(MODE_REDSTONE, MODE_SHAFT);
        }

        @Override
        public String formatValue() {
            return Component.translatable(
                    value == MODE_REDSTONE
                            ? "block.simulatedextra.chain_drivable_wheel_mount.mode.redstone"
                            : "block.simulatedextra.chain_drivable_wheel_mount.mode.shaft"
            ).toString();
        }

        @Override
        public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
            return new ValueSettingsBoard(
                    this.label, MODE_SHAFT, 50,
                    ImmutableList.of(
                            label
                    ),
                    new ValueSettingsFormatter(settings -> Component.translatable(
                            settings.value() == MODE_REDSTONE
                                    ? "block.simulatedextra.chain_drivable_wheel_mount.mode.redstone"
                                    : "block.simulatedextra.chain_drivable_wheel_mount.mode.shaft"
                    ))
            );
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
            return VecHelper.rotateCentered(VecHelper.voxelSpace(10, 15.5f, 11), stateAngle, Direction.Axis.Y);
        }
    }
}