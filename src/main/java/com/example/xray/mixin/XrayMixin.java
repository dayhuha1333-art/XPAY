package com.example.xray.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class XrayMixin {

    @Inject(method = "isOpaqueCube", at = @At("HEAD"), cancellable = true)
    private void xray$isOpaqueCube(IBlockState state, CallbackInfoReturnable<Boolean> cir) {
        ResourceLocation name = state.getBlock().getRegistryName();
        if (name != null
                && "minecraft".equals(name.getNamespace())
                && "stone".equals(name.getPath())) {
            cir.setReturnValue(false);
        }
    }
}
