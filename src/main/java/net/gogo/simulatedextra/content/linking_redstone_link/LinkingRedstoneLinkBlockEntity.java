package net.gogo.simulatedextra.content.linking_redstone_link;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.logistics.packagerLink.LogisticallyLinkedBehaviour;
import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.content.redstone.link.RedstoneLinkBlock;
import com.simibubi.create.content.redstone.link.RedstoneLinkBlockEntity;
import com.simibubi.create.content.redstone.link.RedstoneLinkFrequencySlot;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.*;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.gui.AllIcons;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import org.apache.commons.lang3.tuple.Pair;
import net.gogo.simulatedextra.Imixin.IFrequencyAccess;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;

import java.util.List;
import java.util.UUID;

public class LinkingRedstoneLinkBlockEntity extends RedstoneLinkBlockEntity {

    public static class NOOP extends ValueBoxTransform {
        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            return Vec3.ZERO;
        }

        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {

        }

        @Override
        public boolean shouldRender(LevelAccessor level, BlockPos pos, BlockState state) {
            return false;
        }

        @Override
        public boolean testHit(LevelAccessor level, BlockPos pos, BlockState state, Vec3 localHit) {
            return false;
        }
    }
    public static class ClearBoxTransform extends ValueBoxTransform {

        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            return Vec3.atCenterOf(pos);
        }

        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {

        }
    }

    public LinkingRedstoneLinkBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    LogisticallyLinkedBehaviour linkedBehaviour;
    ClearBehaviour clearBehaviour;

    LogisticallyLinkedBehaviour createLinkingBehaviour(){
        return new LogisticallyLinkedBehaviour(this, true){
            @Override
            public boolean mayInteract(Player player) {
                return true;
            }

            @Override
            public void redstonePowerChanged(int power) {}
        };
    }


    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        behaviours.add(linkedBehaviour = createLinkingBehaviour());
        behaviours.add(clearBehaviour = new ClearBehaviour(this, new ClearBoxTransform()));
        setChanged();
    }

    public void newFrequency() {
        linkedBehaviour.destroy();
        linkedBehaviour = createLinkingBehaviour();
        var access = (IFrequencyAccess) this;
        access.simulatedextra$getLink().setFrequency(true, jamUUIDInStack(linkedBehaviour.freqId));
        setChanged();
    }

    @Override
    protected void createLink() {
        Pair<ValueBoxTransform, ValueBoxTransform> slots =
                Pair.of(new NOOP(), new NOOP());
        var access = (IFrequencyAccess) this;
        access.simulatedextra$setLink(access.simulatedextra$isTransmitter()
                ? LinkBehaviour.transmitter(this, slots, this::getSignal)
                : LinkBehaviour.receiver(this, slots, this::setSignal));
        access.simulatedextra$getLink().setFrequency(true, jamUUIDInStack(linkedBehaviour.freqId));
    }

    ItemStack jamUUIDInStack(UUID uuid) {
        //Bad use of nbt but idgas
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Id", uuid);
        ItemStack stack = new ItemStack(Items.STICK);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }

    @Override
    public void tick() {
        super.tick();

        if (isTransmitterBlock())
            return;
        if (level.isClientSide)
            return;


        BlockState blockState = getBlockState();
        var access = (IFrequencyAccess) this;
        if ((getReceivedSignal() > 0) != blockState.getValue(RedstoneLinkBlock.POWERED)) {
            access.simulatedextra$setReceivedSignalChanged(true);
            level.setBlockAndUpdate(worldPosition, blockState.cycle(RedstoneLinkBlock.POWERED));
        }

        if (access.simulatedextra$getReceivedSignalChanged()) {
            updateSelfAndAttached(blockState);
        }
    }
}
