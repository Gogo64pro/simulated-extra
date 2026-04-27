package net.gogo.simulatedextra.content.linking_redstone_link;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Stack;

public class IdLinkBehaviour extends LinkBehaviour {
    private int frequencyId;
    public static final ValueBoxTransform NOOP = new ValueBoxTransform() {
        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            return Vec3.ZERO;
        }

        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {}
    };

    protected IdLinkBehaviour(SmartBlockEntity be, int id) {
        super(be, Pair.of(NOOP, NOOP));
        frequencyId = id;
    }


    @Override
    public Couple<RedstoneLinkNetworkHandler.Frequency> getNetworkKey() {
        ItemStack stack = getTaggedStack();
        return Couple.create(RedstoneLinkNetworkHandler.Frequency.of(stack),
                RedstoneLinkNetworkHandler.Frequency.EMPTY);
    }

    protected ItemStack getTaggedStack(){
        ItemStack stack = new ItemStack(Items.STICK);
        CompoundTag tag = new CompoundTag();
        tag.putInt("Id", frequencyId);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }

    @Override
    public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(nbt, registries, clientPacket);
        nbt.putInt("FrequencyId", frequencyId);
    }

    @Override
    public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(nbt, registries, clientPacket);
        frequencyId = nbt.getInt("FrequencyId");
    }

    public void setFrequencyId(int id) {
        frequencyId = id;
    }
}
