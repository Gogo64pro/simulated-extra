package dev.ryanhcode.offroad.content.blocks.wheel_mount;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;


public class WheelMountBlockEntity extends KineticBlockEntity {
    public WheelMountBlockEntity(net.minecraft.world.level.block.entity.BlockEntityType<?> type, net.minecraft.core.BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
        super(type, pos, state);
    }


    public static class SuspensionStrengthValueBehaviour {

    }
    protected double getLerpedYaw(double partialTick) {
        return 0;
    }

    protected Block getStressConfigKey() {
        return null;
    }
    protected int getSteeringSignal() {
        return 0;
    }
    public ItemStack getHeldItem(){
        return null;
    }
    public double getLerpedExtension(float pt){return 0;}
    public float getLerpedAngle(float pt){return 0;}
    protected double getChasingYaw(){return -1;}

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
    }
}