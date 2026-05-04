package net.gogo.simulatedextra.content.centered_wheel_mount;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import dev.ryanhcode.offroad.content.components.TireLike;
import dev.ryanhcode.offroad.index.OffroadDataComponents;
import dev.ryanhcode.offroad.index.OffroadPartialModels;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2d;

public class CenteredWheelMountRenderer extends KineticBlockEntityRenderer<CenteredWheelMountBlockEntity> {


    private static final double HORIZONTAL_WHEEL_POSITION    =  0.0 / 16.0;
    private static final double WHEEL_PIVOT_OFFSET_HOR       = 10.0 / 16.0;
    private static final double SPRING_WHEEL_PIVOT_OFFSET_HOR = 12.0 / 16.0;
    private static final double SPRING_WHEEL_PIVOT_OFFSET_VER = -2.0 / 16.0;

    public CenteredWheelMountRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(CenteredWheelMountBlockEntity be, float partialTicks, PoseStack ms,
                              MultiBufferSource buffer, int light, int overlay) {
        final BlockState state = this.getRenderedBlockState(be);
        final RenderType type = this.getRenderType(be, state);
        renderRotatingBuffer(be, this.getRotatedModel(be, state), ms, buffer.getBuffer(type), light);

        FilteringRenderer.renderOnBlockEntity(be, partialTicks, ms, buffer, light, overlay);
        final VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());

        final Direction direction = be.getBlockState()
                .getValue(BlockStateProperties.HORIZONTAL_FACING)
                .getOpposite();
        final BlockState blockState = be.getBlockState();

        //final SuperByteBuffer diodeLeft   = CachedBuffers.partial(OffroadPartialModels.DIODE_LEFT,    blockState);
        //final SuperByteBuffer diodeRight  = CachedBuffers.partial(OffroadPartialModels.DIODE_RIGHT,   blockState);
        final SuperByteBuffer teleOuter   = CachedBuffers.partial(OffroadPartialModels.TELE_OUTER,    blockState);
        final SuperByteBuffer teleInner   = CachedBuffers.partial(OffroadPartialModels.TELE_INNER,    blockState);
        final SuperByteBuffer teleMount   = CachedBuffers.partial(OffroadPartialModels.TELE_MOUNT,    blockState);
        final SuperByteBuffer springTop    = CachedBuffers.partial(OffroadPartialModels.SPRING_UPPER,  blockState);
        final SuperByteBuffer springBottom = CachedBuffers.partial(OffroadPartialModels.SPRING_LOWER,  blockState);
        final SuperByteBuffer springMiddle = CachedBuffers.partial(OffroadPartialModels.SPRING_MIDDLE, blockState);

        final double wheelPivotOffsetHor       = WHEEL_PIVOT_OFFSET_HOR;
        final double springWheelPivotOffsetHor = SPRING_WHEEL_PIVOT_OFFSET_HOR;
        final double springWheelPivotOffsetVer = SPRING_WHEEL_PIVOT_OFFSET_VER;

        final double horizontalWheelPosition = HORIZONTAL_WHEEL_POSITION;

        final ItemStack itemStack = be.getHeldItem();
        final TireLike tireLike = itemStack.get(OffroadDataComponents.TIRE);
        final float radius = tireLike != null ? tireLike.radius() : 0.0f;

        final double verticalWheelPosition = -(be.getLerpedExtension(partialTicks) - be.getOffset() / 16.0) - radius;

        final double teleMountHor = 0.0 / 16.0;
        final double teleMountVer = -6.0 / 16.0 + be.getOffset() / 16.0;

        final double springMountHor = 7.0 / 16.0;
        final double springMountVer = 7.0 / 16.0;

        final double teleAngle    = Math.atan2(verticalWheelPosition - teleMountVer,
                horizontalWheelPosition - wheelPivotOffsetHor - teleMountHor);
        final double teleDistance = new Vector2d(verticalWheelPosition - teleMountVer,
                horizontalWheelPosition - wheelPivotOffsetHor - teleMountHor).length();

        final double springAngle    = Math.atan2(
                verticalWheelPosition - springWheelPivotOffsetVer - springMountVer,
                horizontalWheelPosition - springWheelPivotOffsetHor - springMountHor);
        final double springDistance = new Vector2d(
                verticalWheelPosition - springWheelPivotOffsetVer - springMountVer,
                horizontalWheelPosition - springWheelPivotOffsetHor - springMountHor).length();

        ms.pushPose();
        TransformStack.of(ms)
                .center()
                .rotateYDegrees(AngleHelper.horizontalAngle(direction))
                .rotateXDegrees(AngleHelper.verticalAngle(direction))
                .uncenter();

        ms.pushPose();

        ms.pushPose();
        ms.translate(0.0, -6.0 / 16.0 + be.getOffset() / 16.0, 0.0);
        ms.translate(0.5, 0.5, 0.5);
        ms.mulPose(Axis.XP.rotation((float) teleAngle));
        ms.translate(-0.5, -0.5, -0.5);
        teleOuter.light(light).renderInto(ms, vb);
        ms.translate(0.0, 0.0, -(teleDistance - 1.0));
        teleInner.light(light).renderInto(ms, vb);
        ms.popPose();

        ms.pushPose();
        ms.translate(0.0, verticalWheelPosition, 26.0 / 16.0 - horizontalWheelPosition);

        ms.translate(0.5, 0.5, 0.5);
        ms.rotateAround(Axis.YP.rotation((float) be.getLerpedYaw(partialTicks)),
                0.0F, 0.0F, (float) (-horizontalWheelPosition + 6.0 / 16.0));
        ms.translate(-0.5, -0.5, -0.5);

        teleMount.light(light).renderInto(ms, vb);

        ms.translate(0.5, 0.5, 0.5);
        ms.translate(0.0, 0.0, -26.0 / 16.0f);

        final double signMultiplier = -be.getLerpedAngle(partialTicks)
                * (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0 : -1.0)
                * (direction.getAxis() == Direction.Axis.X ? 1.0 : -1.0);
        ms.mulPose(Axis.ZP.rotation((float) signMultiplier));


        if (tireLike != null) {
            final Vec3 rotation = tireLike.rotation();
            ms.mulPose(Axis.XP.rotation((float) Math.toRadians(rotation.x)));
            ms.mulPose(Axis.YP.rotation((float) Math.toRadians(rotation.y)));
            ms.mulPose(Axis.ZP.rotation((float) Math.toRadians(rotation.z)));

            if (tireLike.model().isPresent()) {
                final ResourceLocation model = tireLike.model().get();
                ms.translate(tireLike.offset().x, tireLike.offset().y, tireLike.offset().z);
                final SuperByteBuffer wheel = CachedBuffers.partial(PartialModel.of(model), state);
                wheel.light(light)
                        .translate(-0.5f, 0.0f, -0.5f)
                        .renderInto(ms, vb);
            } else {
                ms.translate(tireLike.offset().x, tireLike.offset().y, tireLike.offset().z);
                Minecraft.getInstance().getItemRenderer().renderStatic(
                        itemStack, ItemDisplayContext.NONE,
                        light, overlay, ms, buffer, be.getLevel(), 0);
            }
        }

        ms.popPose();
        ms.popPose();

        // spring
        ms.pushPose();
        ms.translate(0.5, 0.5 + springMountVer, 0.5 - springMountHor);
        ms.mulPose(Axis.XP.rotation((float) springAngle + Mth.PI / 2.0f));
        ms.translate(-0.5, -0.5 - springMountVer, -0.5 + springMountHor);

        final float springExtension = (float) springDistance;
        final float springSpan = springExtension - 4.0f / 16.0f;

        springTop.light(light).renderInto(ms, vb);
        springMiddle.light(light)
                .translate(0.0f, 13.0f / 16.0f, 0.0f)
                .scale(1.0f, springSpan / (14.0f / 16.0f), 1.0f)
                .translateBack(0.0f, 13.0f / 16.0f, 0.0f)
                .renderInto(ms, vb);
        springBottom.light(light)
                .translate(0.0, -(springSpan + -14.0 / 16.0), 0.0)
                .renderInto(ms, vb);
        ms.popPose();

        //diodeLeft.light(light)
        //        .color(SimColors.redstone(be.getClientSteeringSignalLeft() / 15.0f))
        //        .renderInto(ms, vb);
        //diodeRight.light(light)
        //        .color(SimColors.redstone(be.getClientSteeringSignalRight() / 15.0f))
        //        .renderInto(ms, vb);

        ms.popPose();
    }

    @Override
    public int getViewDistance() {
        return 512;
    }

    @Override
    protected BlockState getRenderedBlockState(final CenteredWheelMountBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }
}
