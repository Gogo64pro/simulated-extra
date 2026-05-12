package net.gogo.simulatedextra.mixin;

import com.simibubi.create.content.kinetics.chainDrive.ChainDriveBlock;
import net.gogo.simulatedextra.content.chained_centered_wheel_mount.IChainDrivable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChainDriveBlock.class)
public abstract class ChainDriveBlockMixin implements IChainDrivable {

    @Override
    public Direction.Axis getChainAxis(BlockState state) {
        return state.getValue(ChainDriveBlock.AXIS);
    }

    @Override
    public ChainDriveBlock.Part getChainPart(BlockState state) {
        return state.getValue(ChainDriveBlock.PART);
    }

    @Override
    public boolean isConnectedAlongFirst(BlockState state) {
        return state.getValue(ChainDriveBlock.CONNECTED_ALONG_FIRST_COORDINATE);
    }

    @Override
    public BlockState setChainPart(BlockState state, ChainDriveBlock.Part part) {
        return state.setValue(ChainDriveBlock.PART, part);
    }

    @Override
    public BlockState setConnectedAlongFirst(BlockState state, boolean value) {
        return state.setValue(ChainDriveBlock.CONNECTED_ALONG_FIRST_COORDINATE, value);
    }

    @Inject(method = "updateShape", at = @At("HEAD"), cancellable = true, remap = false)
    private void injectUpdateShape(BlockState stateIn, Direction face, BlockState neighbour,
                                   LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos,
                                   CallbackInfoReturnable<BlockState> cir) {

        if (neighbour.getBlock() instanceof IChainDrivable otherDef
                && stateIn.getBlock() instanceof IChainDrivable selfDef) {
            cir.setReturnValue(IChainDrivable.updateShape(stateIn, face, neighbour, selfDef, otherDef));
        }
    }
}