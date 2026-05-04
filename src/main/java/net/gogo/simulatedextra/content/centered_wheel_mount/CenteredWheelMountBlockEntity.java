package net.gogo.simulatedextra.content.centered_wheel_mount;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.actors.roller.RollerBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import static dev.ryanhcode.offroad.index.OffroadBlocks.WHEEL_MOUNT;

public class CenteredWheelMountBlockEntity extends WheelMountBlockEntity {
    public ScrollValueBehaviour offsetBehaviour;

    public CenteredWheelMountBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public double getLerpedYaw(double partialTick) {
        return super.getLerpedYaw(partialTick);
    }

    public int getOffset() {
        return offsetBehaviour != null ? offsetBehaviour.getValue() : 7;
    }

    @Override
    protected Block getStressConfigKey() {
        return WHEEL_MOUNT.get();
    }

    @Override
    protected int getSteeringSignal() {
        return 0;
    }


    public static final class OffsetValueBox extends ValueBoxTransform {
        private final int hOffset;

        private Direction getFacing(final BlockState state) {
            Direction facing = state.getValue(RollerBlock.FACING);
            if(facing == Direction.WEST || facing == Direction.EAST) {
                facing = facing.getOpposite();
            }
            return facing;
        }

        public OffsetValueBox(final int hOffset) {
            this.hOffset = hOffset;
        }

        @Override
        public void rotate(final LevelAccessor level, final BlockPos pos, final BlockState state, final PoseStack ms) {
            final Direction facing = getFacing(state);
            final float yRot = AngleHelper.horizontalAngle(facing) + 180;
            TransformStack.of(ms)
                    .rotateYDegrees(yRot)
                    .rotateXDegrees(90);
        }

        @Override
        public boolean testHit(final LevelAccessor level, final BlockPos pos, final BlockState state, final Vec3 localHit) {
            final Vec3 offset = this.getLocalOffset(level, pos, state);
            if (offset == null)
                return false;
            return localHit.distanceTo(offset) < this.scale / 3;
        }

        @Override
        public Vec3 getLocalOffset(final LevelAccessor level, final BlockPos pos, final BlockState state) {
            final Direction facing = getFacing(state);
            final float stateAngle = AngleHelper.horizontalAngle(facing) + 180;
            return VecHelper.rotateCentered(VecHelper.voxelSpace(8 + this.hOffset, 15.5f, 11), stateAngle, Direction.Axis.Y);
        }
    }
    public static class WheelOffsetScrollBehaviour extends ScrollValueBehaviour {
        public static final BehaviourType<WheelOffsetScrollBehaviour> TYPE = new BehaviourType<>();

        public WheelOffsetScrollBehaviour(Component label, SmartBlockEntity be, ValueBoxTransform slot) {
            super(label, be, slot);
        }

        @Override
        public BehaviourType<?> getType() {
            return TYPE;
        }

        @Override
        public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
            nbt.putInt("WheelOffsetValue", value);
        }

        @Override
        public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
            value = nbt.getInt("WheelOffsetValue");
        }

        @Override
        public int netId() {
            return 1;
        }
    }
}

