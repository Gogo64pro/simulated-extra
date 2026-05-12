package net.gogo.simulatedextra.mixin;

import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.chainDrive.ChainDriveBlock;
import net.gogo.simulatedextra.content.chained_centered_wheel_mount.IChainDrivable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RotationPropagator.class)
public abstract class RotationPropagatorMixin {
    @Inject(method = "getRotationSpeedModifier", at = @At("RETURN"), cancellable = true, remap = false)
    private static void injectChainLike(KineticBlockEntity from, KineticBlockEntity to,
                                        CallbackInfoReturnable<Float> cir) {
        System.out.println("[]Mixin called");
        if (cir.getReturnValue() != 0) return;
        System.out.println("[]Mixin survived return val being 0");

        BlockState stateFrom = from.getBlockState();
        BlockState stateTo = to.getBlockState();
        Block fromBlock = stateFrom.getBlock();
        Block toBlock = stateTo.getBlock();

        if (!(fromBlock instanceof IChainDrivable fromDef)
                || !(toBlock instanceof IChainDrivable toDef))
            return;
        System.out.println("[]Mixin both implement IChainDriveLike, delegating");

        BlockPos diff = to.getBlockPos().subtract(from.getBlockPos());
        Direction direction = Direction.getNearest(diff.getX(), diff.getY(), diff.getZ());

        boolean connected = IChainDrivable.areBlocksConnected(stateFrom, stateTo, direction, fromDef, toDef);
        if (!connected) return;

        cir.setReturnValue(ChainDriveBlock.getRotationSpeedModifier(from, to));
    }
}
