package net.gogo.simulatedextra.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "dev.ryanhcode.offroad.content.blocks.wheel_mount.WheelMountBlockEntity$SuspensionStrengthValueBox")
public abstract class SuspensionStrengthValueBoxMixin {

    @ModifyExpressionValue(
            method = "rotate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;",
                    ordinal = 0
            )
    )
    private Comparable<?> modifyFacingInRotate(Comparable<?> original) {
        return simulatedextra$fixFacing((Direction) original);
    }

    @ModifyExpressionValue(
            method = "getLocalOffset",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;",
                    ordinal = 0
            )
    )
    private Comparable<?> modifyFacingInGetLocalOffset(Comparable<?> original) {
        return simulatedextra$fixFacing((Direction) original);
    }

    @Unique
    private Direction simulatedextra$fixFacing(final Direction facing) {
        if (facing == Direction.WEST || facing == Direction.EAST) {
            return facing.getOpposite();
        }
        return facing;
    }
}