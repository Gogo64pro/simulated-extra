package net.gogo.simulatedextra.Imixin;

import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Unique;

public interface IThrottleLeverCustomName {
    public Component simulatedextra$getCustomLabel();
    public void simulatedextra$setCustomLabel(Component customLabel);
}
