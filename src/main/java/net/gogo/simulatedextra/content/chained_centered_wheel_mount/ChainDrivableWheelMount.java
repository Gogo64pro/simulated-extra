package net.gogo.simulatedextra.content.chained_centered_wheel_mount;

import com.simibubi.create.content.kinetics.chainDrive.ChainDriveBlock;
import net.createmod.catnip.data.Iterate;
import net.gogo.simulatedextra.content.centered_wheel_mount.CenteredWheelMountBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

import static com.simibubi.create.content.kinetics.chainDrive.ChainDriveBlock.CONNECTED_ALONG_FIRST_COORDINATE;
import static com.simibubi.create.content.kinetics.chainDrive.ChainDriveBlock.PART;

public class ChainDrivableWheelMount extends CenteredWheelMountBlock implements IChainDrivable {
    public ChainDrivableWheelMount(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(PART, ChainDriveBlock.Part.NONE));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ChainDriveBlock.PART, CONNECTED_ALONG_FIRST_COORDINATE);
    }

    @Override
    public Direction.Axis getChainAxis(BlockState state) {
        Direction facing = state.getValue(HORIZONTAL_FACING);
        return facing.getAxis();
    }

    @Override
    public ChainDriveBlock.Part getChainPart(BlockState state) {
        return state.getValue(PART);
    }

    @Override
    public boolean isConnectedAlongFirst(BlockState state) {
        return state.getValue(CONNECTED_ALONG_FIRST_COORDINATE);
    }

    @Override
    public BlockState setChainPart(BlockState state, ChainDriveBlock.Part part) {
        return state.setValue(ChainDriveBlock.PART, part);
    }

    @Override
    public BlockState setConnectedAlongFirst(BlockState state, boolean value) {
        return state.setValue(ChainDriveBlock.CONNECTED_ALONG_FIRST_COORDINATE, value);
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState stateIn, @NotNull Direction face,
                                              @NotNull BlockState neighbour, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {

        BlockState result = super.updateShape(stateIn, face, neighbour, worldIn, currentPos, facingPos);

        IChainDrivable otherDef = neighbour.getBlock() instanceof IChainDrivable c ? c : null;
        result = IChainDrivable.updateShape(result, face, neighbour, this, otherDef);

        return result;
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        for (Direction facing : Iterate.directions) {
            if (facing.getAxis() == getChainAxis(state))
                continue;
            BlockPos pos = context.getClickedPos();
            BlockPos offset = pos.relative(facing);
            BlockState neighbour = context.getLevel().getBlockState(offset);
            if (neighbour.getBlock() instanceof IChainDrivable) {
                state = IChainDrivable.updateShape(state, facing, neighbour, this, (IChainDrivable) neighbour.getBlock());
            }
        }
        return state;
    }
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        for (Direction facing : Iterate.directions) {
            if (facing.getAxis() == getChainAxis(state))
                continue;
            BlockPos offset = pos.relative(facing);
            level.neighborChanged(offset, this, pos);
        }
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return super.hasShaftTowards(world, pos, state, face) || face == Direction.UP;
    }
}
