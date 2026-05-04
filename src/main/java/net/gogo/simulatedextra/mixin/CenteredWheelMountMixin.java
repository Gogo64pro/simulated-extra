package net.gogo.simulatedextra.mixin;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity;
import net.gogo.simulatedextra.content.centered_wheel_mount.CenteredWheelMountBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = WheelMountBlockEntity.class, remap = false)
public abstract class CenteredWheelMountMixin extends SmartBlockEntity {


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
            var scrollValue = new CenteredWheelMountBlockEntity.WheelOffsetScrollBehaviour(Component.literal("Wheel offset"), be, new CenteredWheelMountBlockEntity.OffsetValueBox(-3));
            scrollValue.between(2, 8);
            scrollValue.value = 7;
            behaviours.add(scrollValue);
            be.offsetBehaviour = scrollValue;
        }
    }
}