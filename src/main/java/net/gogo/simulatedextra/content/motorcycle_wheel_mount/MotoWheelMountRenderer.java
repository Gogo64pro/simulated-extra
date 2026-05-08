package net.gogo.simulatedextra.content.motorcycle_wheel_mount;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import dev.ryanhcode.offroad.content.components.TireLike;
import dev.ryanhcode.offroad.index.OffroadDataComponents;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;


public class MotoWheelMountRenderer extends KineticBlockEntityRenderer<MotoWheelMountBlockEntity> {


    private static final double FORK_FORWARD  = MotoWheelMountBlockEntity.FORK_FORWARD;
    private static final double FORK_DOWN     = MotoWheelMountBlockEntity.FORK_DOWN;
    private static final double FORK_RAKE_RAD = MotoWheelMountBlockEntity.FORK_RAKE_RAD;

    private static final double FORK_REST_LENGTH =
            Math.sqrt(FORK_FORWARD * FORK_FORWARD + FORK_DOWN * FORK_DOWN);


    private static final double UPPER_TUBE_FRACTION = 0.55;

    public MotoWheelMountRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(MotoWheelMountBlockEntity be, float partialTicks,
                              PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

        final BlockState state    = getRenderedBlockState(be);
        final RenderType solidType = getRenderType(be, state);

        renderRotatingBuffer(be, getRotatedModel(be, state), ms, buffer.getBuffer(solidType), light);
        final VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

        final Direction facing = be.getBlockState()
                .getValue(BlockStateProperties.HORIZONTAL_FACING);

        final SuperByteBuffer forkUpper    = CachedBuffers.partial(MotoPartialModels.MOTO_FORK_UPPER,  state);
        final SuperByteBuffer forkLower    = CachedBuffers.partial(MotoPartialModels.MOTO_FORK_LOWER,  state);
        final SuperByteBuffer forkMount    = CachedBuffers.partial(MotoPartialModels.MOTO_FORK_MOUNT,  state);
        final SuperByteBuffer springTop    = CachedBuffers.partial(MotoPartialModels.MOTO_SPRING_TOP,    state);
        final SuperByteBuffer springMid    = CachedBuffers.partial(MotoPartialModels.MOTO_SPRING_MID,    state);
        final SuperByteBuffer springBottom = CachedBuffers.partial(MotoPartialModels.MOTO_SPRING_BOTTOM, state);

        final ItemStack  itemStack = be.getHeldItem();
        final TireLike   tireLike  = itemStack.get(OffroadDataComponents.TIRE);
        final float      radius    = tireLike != null ? tireLike.radius() : 0.0f;

        final double compression = be.getLerpedForkCompression(partialTicks);

        final double forkLength = FORK_REST_LENGTH - compression;

        final double hubFwd  =  FORK_FORWARD - compression * Math.sin(FORK_RAKE_RAD);
        final double hubDown =  FORK_DOWN    - compression * Math.cos(FORK_RAKE_RAD);

        final float steerYaw = (float) be.getLerpedYaw(partialTicks);

        final float spinAngle = be.getLerpedAngle(partialTicks);
        final float spinSign  = (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0f : -1.0f)
                * (facing.getAxis() == Direction.Axis.X ? 1.0f : -1.0f);


        ms.pushPose();

        TransformStack.of(ms)
                .center()
                .rotateYDegrees(AngleHelper.horizontalAngle(facing.getOpposite())) // face wheel outward
                .uncenter();


        ms.pushPose();
        {
            ms.translate(0.5, 0.5, 0.5);
            ms.mulPose(Axis.XP.rotation(-(float) FORK_RAKE_RAD));
            ms.translate(-0.5, -0.5, -0.5);

            forkUpper.light(light).renderInto(ms, vb);
        }
        ms.popPose();


        ms.pushPose();
        {
            ms.translate(0.5, 0.5, 0.5);
            ms.mulPose(Axis.XP.rotation(-(float) FORK_RAKE_RAD));
            ms.translate(0.0, -compression, 0.0);
            ms.translate(-0.5, -0.5, -0.5);

            forkLower.light(light).renderInto(ms, vb);
        }
        ms.popPose();

        ms.pushPose();
        {
            ms.translate(0.5, 0.5, 0.5);
            ms.mulPose(Axis.XP.rotation(-(float) FORK_RAKE_RAD));
            ms.translate(-0.5, -0.5, -0.5);

            final float springExtent     = (float) forkLength;
            final float capHeight        = 4.0f / 16.0f;
            final float springSpan       = Math.max(springExtent - capHeight * 2.0f, 0.0f);
            final float nominalMidHeight = 14.0f / 16.0f;

            springTop.light(light).renderInto(ms, vb);

            springMid.light(light)
                    .translate(0.0f, capHeight, 0.0f)
                    .scale(1.0f, springSpan / nominalMidHeight, 1.0f)
                    .translateBack(0.0f, capHeight, 0.0f)
                    .renderInto(ms, vb);

            springBottom.light(light)
                    .translate(0.0f, capHeight + springSpan, 0.0f)
                    .renderInto(ms, vb);
        }
        ms.popPose();


        ms.pushPose();
        {
            ms.translate(0.5, 0.5 - hubDown, 0.5 + hubFwd);

            ms.mulPose(Axis.YP.rotation(steerYaw));
            ms.translate(-0.5, -0.5 + hubDown, -0.5 - hubFwd);

            forkMount.light(light).renderInto(ms, vb);
        }
        ms.popPose();


        if (tireLike != null) {
            ms.pushPose();
            {
                ms.translate(0.5, 0.5 - hubDown, 0.5 + hubFwd);

                ms.mulPose(Axis.YP.rotation(steerYaw));

                ms.mulPose(Axis.XP.rotation(spinAngle * spinSign));

                final Vec3 rotation = tireLike.rotation();
                ms.mulPose(Axis.XP.rotation((float) Math.toRadians(rotation.x)));
                ms.mulPose(Axis.YP.rotation((float) Math.toRadians(rotation.y)));
                ms.mulPose(Axis.ZP.rotation((float) Math.toRadians(rotation.z)));

                ms.translate(tireLike.offset().x, tireLike.offset().y, tireLike.offset().z);

                if (tireLike.model().isPresent()) {
                    final ResourceLocation model = tireLike.model().get();
                    final SuperByteBuffer wheel = CachedBuffers.partial(PartialModel.of(model), state);
                    wheel.light(light)
                            .translate(-0.5f, 0.0f, -0.5f)
                            .renderInto(ms, vb);
                } else {
                    Minecraft.getInstance().getItemRenderer().renderStatic(
                            itemStack, ItemDisplayContext.NONE,
                            light, overlay, ms, buffer, be.getLevel(), 0);
                }
            }
            ms.popPose();
        }

        ms.popPose();
    }

    @Override
    public int getViewDistance() {
        return 512;
    }

    @Override
    protected BlockState getRenderedBlockState(MotoWheelMountBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }
}