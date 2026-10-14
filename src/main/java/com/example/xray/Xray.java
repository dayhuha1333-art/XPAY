package com.example.xray;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod(modid = "xray", name = "Xray", version = "1.0",
        acceptedMinecraftVersions = "[1.12,1.13)", clientSideOnly = true)
public class Xray {

    private static final Set<ResourceLocation> WHITELIST = new HashSet<>();

    static {
        WHITELIST.add(new ResourceLocation("minecraft:coal_ore"));
        WHITELIST.add(new ResourceLocation("minecraft:iron_ore"));
        WHITELIST.add(new ResourceLocation("minecraft:gold_ore"));
        WHITELIST.add(new ResourceLocation("minecraft:diamond_ore"));
        WHITELIST.add(new ResourceLocation("minecraft:redstone_ore"));
        WHITELIST.add(new ResourceLocation("minecraft:lit_redstone_ore"));
        WHITELIST.add(new ResourceLocation("minecraft:lapis_ore"));
        WHITELIST.add(new ResourceLocation("minecraft:emerald_ore"));
        WHITELIST.add(new ResourceLocation("minecraft:quartz_ore"));

        WHITELIST.add(new ResourceLocation("minecraft:chest"));
        WHITELIST.add(new ResourceLocation("minecraft:trapped_chest"));
        WHITELIST.add(new ResourceLocation("minecraft:ender_chest"));

        WHITELIST.add(new ResourceLocation("minecraft:white_shulker_box"));
        WHITELIST.add(new ResourceLocation("minecraft:orange_shulker_box"));
        WHITELIST.add(new ResourceLocation("minecraft:magenta_shulker_box"));
        WHITELIST.add(new ResourceLocation("minecraft:light_blue_shulker_box"));
        WHITELIST.add(new ResourceLocation("minecraft:yellow_shulker_box"));
        WHITELIST.add(new ResourceLocation("minecraft:lime_shulker_box"));
        WHITELIST.add(new ResourceLocation("minecraft:pink_shulker_box"));
        WHITELIST.add(new ResourceLocation("minecraft:gray_shulker_box"));
        WHITELIST.add(new ResourceLocation("minecraft:silver_shulker_box"));
        WHITELIST.add(new ResourceLocation("minecraft:cyan_shulker_box"));
        WHITELIST.add(new ResourceLocation("minecraft:purple_shulker_box"));
        WHITELIST.add(new ResourceLocation("minecraft:blue_shulker_box"));
        WHITELIST.add(new ResourceLocation("minecraft:brown_shulker_box"));
        WHITELIST.add(new ResourceLocation("minecraft:green_shulker_box"));
        WHITELIST.add(new ResourceLocation("minecraft:red_shulker_box"));
        WHITELIST.add(new ResourceLocation("minecraft:black_shulker_box"));

        WHITELIST.add(new ResourceLocation("minecraft:mob_spawner"));
        WHITELIST.add(new ResourceLocation("minecraft:end_portal"));
        WHITELIST.add(new ResourceLocation("minecraft:end_portal_frame"));
        WHITELIST.add(new ResourceLocation("minecraft:portal"));
        WHITELIST.add(new ResourceLocation("minecraft:water"));
        WHITELIST.add(new ResourceLocation("minecraft:lava"));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        for (ModelResourceLocation mrl : event.getModelRegistry().getKeys()) {
            String full = mrl.toString();
            String path = full.split("#")[0];
            ResourceLocation loc = new ResourceLocation(path);
            if (WHITELIST.contains(loc)) continue;

            IBakedModel original = event.getModelRegistry().getObject(mrl);
            if (original == null) continue;

            event.getModelRegistry().putObject(mrl, new InvisibleModel(original));
        }
    }

    @SideOnly(Side.CLIENT)
    private static class InvisibleModel implements IBakedModel {
        private final IBakedModel original;

        InvisibleModel(IBakedModel original) {
            this.original = original;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
            return Collections.emptyList();
        }

        @Override
        public boolean isAmbientOcclusion() {
            return false;
        }

        @Override
        public boolean isGui3d() {
            return false;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return original.getParticleTexture();
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return original.getItemCameraTransforms();
        }

        @Override
        public ItemOverrideList getOverrides() {
            return original.getOverrides();
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
            return original.handlePerspective(type);
        }
    }
}
