package net.gogo.simulatedextra.content.chained_centered_wheel_mount;


import com.simibubi.create.content.kinetics.chainDrive.ChainDriveBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.state.BlockState;

public interface IChainDrivable {
    Axis getChainAxis(BlockState state);
    ChainDriveBlock.Part getChainPart(BlockState state);
    boolean isConnectedAlongFirst(BlockState state);
    BlockState setChainPart(BlockState state, ChainDriveBlock.Part part);
    BlockState setConnectedAlongFirst(BlockState state, boolean value);

    static boolean areBlocksConnected(BlockState state, BlockState other, Direction facing,
                                      IChainDrivable selfDef, IChainDrivable otherDef) {
        ChainDriveBlock.Part part = selfDef.getChainPart(state);
        Axis connectionAxis = getConnectionAxis(state, selfDef);
        Axis otherConnectionAxis = getConnectionAxis(other, otherDef);

        if (otherConnectionAxis != connectionAxis) return false;
        if (facing.getAxis() != connectionAxis) return false;
        if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE
                && (part == ChainDriveBlock.Part.MIDDLE || part == ChainDriveBlock.Part.START))
            return true;
        if (facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE
                && (part == ChainDriveBlock.Part.MIDDLE || part == ChainDriveBlock.Part.END))
            return true;
        return false;
    }

    static Axis getConnectionAxis(BlockState state, IChainDrivable def) {
        Axis axis = def.getChainAxis(state);
        boolean alongFirst = def.isConnectedAlongFirst(state);
        return alongFirst
                ? (axis == Axis.X ? Axis.Y : Axis.X)
                : (axis == Axis.Z ? Axis.Y : Axis.Z);
    }

    static BlockState updateShape(BlockState stateIn, Direction face, BlockState neighbour,
                                  IChainDrivable selfDef, IChainDrivable otherDef) {

        ChainDriveBlock.Part part = selfDef.getChainPart(stateIn);
        Axis axis = selfDef.getChainAxis(stateIn);
        boolean connectionAlongFirst = selfDef.isConnectedAlongFirst(stateIn);
        Axis connectionAxis = getConnectionAxis(stateIn, selfDef);

        Axis faceAxis = face.getAxis();
        boolean facingAlongFirst = axis == Axis.X ? faceAxis.isVertical() : faceAxis == Axis.X;
        boolean positive = face.getAxisDirection() == Direction.AxisDirection.POSITIVE;

        if (axis == faceAxis)
            return stateIn;

        if (!(neighbour.getBlock() instanceof IChainDrivable)) {
            if (facingAlongFirst != connectionAlongFirst || part == ChainDriveBlock.Part.NONE)
                return stateIn;
            if (part == ChainDriveBlock.Part.MIDDLE)
                return selfDef.setChainPart(stateIn, positive ? ChainDriveBlock.Part.END : ChainDriveBlock.Part.START);
            if ((part == ChainDriveBlock.Part.START) == positive)
                return selfDef.setChainPart(stateIn, ChainDriveBlock.Part.NONE);
            return stateIn;
        }

        ChainDriveBlock.Part otherPart = otherDef.getChainPart(neighbour);
        Axis otherAxis = otherDef.getChainAxis(neighbour);
        Axis otherConnectionAxis = getConnectionAxis(neighbour, otherDef);

        if (otherAxis == faceAxis)
            return stateIn;
        if (otherPart != ChainDriveBlock.Part.NONE && otherConnectionAxis != faceAxis)
            return stateIn;

        if (part == ChainDriveBlock.Part.NONE) {
            part = positive ? ChainDriveBlock.Part.START : ChainDriveBlock.Part.END;
            connectionAlongFirst = axis == Axis.X ? faceAxis.isVertical() : faceAxis == Axis.X;
        } else if (connectionAxis != faceAxis) {
            return stateIn;
        }

        if ((part == ChainDriveBlock.Part.START) != positive)
            part = ChainDriveBlock.Part.MIDDLE;

        stateIn = selfDef.setChainPart(stateIn, part);
        stateIn = selfDef.setConnectedAlongFirst(stateIn, connectionAlongFirst);
        return stateIn;
    }
}
