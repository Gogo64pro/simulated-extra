package net.gogo.simulatedextra.content.linking_redstone_link;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class IdValueBoxTransform extends ValueBoxTransform {

    private final Direction face;

    public IdValueBoxTransform(Direction face) {
        this.face = face;
    }

    @Override
    public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
        return new Vec3(0.5, 0.7, 0.5)
                .add(Vec3.atLowerCornerOf(face.getNormal()).scale(0.501));
    }

    @Override
    public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
        float yRot = switch (face) {
            case SOUTH -> 180f;
            case EAST  -> 270f;
            case WEST  -> 90f;
            default    -> 0f;
        };
        float xRot = switch (face) {
            case UP   -> -90f;
            case DOWN ->  90f;
            default   ->   0f;
        };

        ms.mulPose(com.mojang.math.Axis.YP.rotationDegrees(yRot));
        if (xRot != 0f)
            ms.mulPose(com.mojang.math.Axis.XP.rotationDegrees(xRot));
    }

    @Override
    public boolean shouldRender(LevelAccessor level, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean testHit(LevelAccessor level, BlockPos pos, BlockState state, Vec3 localHit) {
        return super.testHit(level, pos, state, localHit);
    }

    public Direction getFace() {
        return face;
    }
}