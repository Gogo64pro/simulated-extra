package net.gogo.simulatedextra.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.simibubi.create.foundation.utility.CreateLang;
import dev.simulated_team.simulated.content.blocks.throttle_lever.ThrottleLeverBlockEntity;
import net.gogo.simulatedextra.Imixin.IThrottleLeverCustomName;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin(value = ThrottleLeverBlockEntity.class, remap = false)
public class ThrottleLeverBlockEntityMixin implements IThrottleLeverCustomName {
    @Shadow
    protected int state;
    @Unique
    private Component simulatedextra$customLabel = null;

    @Unique
    public Component simulatedextra$getCustomLabel() {
        return simulatedextra$customLabel;
    }

    @Unique
    public void simulatedextra$setCustomLabel(Component customLabel) {
        this.simulatedextra$customLabel = customLabel;
        ThrottleLeverBlockEntity be = (ThrottleLeverBlockEntity)(Object)this;

        be.notifyUpdate();
    }

    @Inject(
            method = "write",
            at = @At("TAIL"),
            remap = false
    )
    private void injectWrite(CompoundTag compound, HolderLookup.Provider registries,
                             boolean clientPacket, CallbackInfo ci){
        if(this.simulatedextra$customLabel != null) {
            Tag componentTag = ComponentSerialization.CODEC.encodeStart(
                    NbtOps.INSTANCE,
                    simulatedextra$customLabel
            ).getOrThrow();
            compound.put("CustomName", componentTag);
        } else {
            compound.remove("CustomName");
        }
    }
    @Inject(
            method = "read",
            at = @At("TAIL"),
            remap = false
    )
    private void injectRead(CompoundTag compound, HolderLookup.Provider registries,
                            boolean clientPacket, CallbackInfo ci) {
        if (compound.contains("CustomName")) {
            this.simulatedextra$customLabel = ComponentSerialization.CODEC.parse(
                    NbtOps.INSTANCE,
                    compound.get("CustomName")
            ).getOrThrow();
        } else
            this.simulatedextra$customLabel = null;
    }
    @ModifyArg(
            method = "addToGoggleTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "net/createmod/catnip/lang/LangBuilder.add(Lnet/minecraft/network/chat/MutableComponent;)Lnet/createmod/catnip/lang/LangBuilder;"
            ),
            index = 0
    )
    private MutableComponent replaceAnalogStrengthTooltip(MutableComponent original) {
        if (simulatedextra$customLabel == null) return original;

        MutableComponent label = simulatedextra$customLabel.copy()
                .withStyle(ChatFormatting.ITALIC);

        MutableComponent value = Component.literal(": " + state + "/15")
                .withStyle(style -> style.withItalic(false));

        return label.append(value);
    }
}
