package net.gogo.simulatedextra.content.linking_redstone_link;

import com.simibubi.create.content.redstone.link.RedstoneLinkBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.apache.commons.lang3.tuple.Pair;
import net.gogo.simulatedextra.Imixin.IFrequencyAccess;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;

public class LinkingRedstoneLinkBlockEntity extends RedstoneLinkBlockEntity {
    private IdLinkBehaviour idLink;
    public LinkingRedstoneLinkBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected void createLink() {
        Pair<ValueBoxTransform, ValueBoxTransform> noSlots =
                Pair.of(new IdValueBoxTransform(getBlockState().getValue(BlockStateProperties.FACING)), IdLinkBehaviour.NOOP);

        IFrequencyAccess access = (IFrequencyAccess) this;
        access.simulatedextra$setLink(access.simulatedextra$isTransmitter()
                ? IdLinkBehaviour.transmitter(this, noSlots, this::getSignal)
                : IdLinkBehaviour.receiver(this, noSlots, this::setSignal));

        applyIdFrequency();
    }
    private void applyIdFrequency() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Id", getSavedId());

        ItemStack carrier = new ItemStack(Items.STICK);
        carrier.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        
        IFrequencyAccess access = (IFrequencyAccess) this;
        access.simulatedextra$getLink().setFrequency(true, ItemStack.EMPTY);
        access.simulatedextra$getLink().setFrequency(false, carrier);
    }

    private int savedId = -1;

    public int getSavedId() { return savedId; }

    public void setId(int id) {
        savedId = id;
        IFrequencyAccess access = (IFrequencyAccess) this;
        if (access.simulatedextra$getLink() != null)
            applyIdFrequency();
        sendData();
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putInt("Id", savedId);
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        savedId = compound.getInt("Id");
        super.read(compound, registries, clientPacket);
        IFrequencyAccess access = (IFrequencyAccess) this;
        if (access.simulatedextra$getLink() != null)
            applyIdFrequency();
    }
    ScrollValueBehaviour scrollValue;

    //@Override
    //public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    //    idLink = new IdLinkBehaviour(this, 0, getBlockState().getValue(BlockStateProperties.FACING));
    //    behaviours.add(idLink);
//
    //    scrollValue = new ScrollValueBehaviour(
    //            Component.literal("Frequency"),
    //            this,
    //            new IdValueBoxTransform(getBlockState().getValue(BlockStateProperties.FACING).getOpposite())
    //    );
    //    scrollValue.between(-1, Integer.MAX_VALUE);
    //    scrollValue.withCallback(id -> {
    //        idLink.setFrequencyId(id);
    //        setId(id);
    //    });
    //    scrollValue.setValue(0);
    //    behaviours.add(scrollValue);
    //}
}
