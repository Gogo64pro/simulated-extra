package net.gogo.simulatedextra.content.centered_wheel_mount;

import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity;
import net.gogo.simulatedextra.Simulatedextra;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

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

}

