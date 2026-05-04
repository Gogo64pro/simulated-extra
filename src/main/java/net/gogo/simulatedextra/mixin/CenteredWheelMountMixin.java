package net.gogo.simulatedextra.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.contraptions.actors.roller.RollerBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.gogo.simulatedextra.content.centered_wheel_mount.CenteredWheelMountBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = WheelMountBlockEntity.class, remap = false)
public abstract class CenteredWheelMountMixin extends SmartBlockEntity {
    private static final class OffsetValueBox extends ValueBoxTransform {
        private final int hOffset;

        public OffsetValueBox(final int hOffset) {
            this.hOffset = hOffset;
        }

        @Override
        public void rotate(final LevelAccessor level, final BlockPos pos, final BlockState state, final PoseStack ms) {
            final Direction facing = state.getValue(RollerBlock.FACING);
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
            final Direction facing = state.getValue(RollerBlock.FACING);
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
    }

    public CenteredWheelMountMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @ModifyConstant(
            method = "addBehaviours",
            constant = @Constant(intValue = 0)
    )
    private int changeZeroToThree(int original) {
        if ((Object) this instanceof CenteredWheelMountBlockEntity) {
            return 3;
        }
        return original;
    }
    @Inject(method = "addBehaviours", at = @At("TAIL"))
    private void onAddBehaviours(List<BlockEntityBehaviour> behaviours, CallbackInfo ci) {
        if ((Object) this instanceof CenteredWheelMountBlockEntity be) {
            var scrollValue = new WheelOffsetScrollBehaviour(Component.literal("Wheel offset"), be, new OffsetValueBox(-3));
            scrollValue.between(2, 8);
            scrollValue.value = 7;
            behaviours.add(scrollValue);
            be.offsetBehaviour = scrollValue;
        }
    }
}