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
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod(modid = "xray", name = "Xray", version = "1.0",
        acceptedMinecraftVersions = "[1.12,1.13)",
        clientSideOnly = true)
public class Xray {

    private static final Logger LOGGER = LogManager.getLogger("xray");

    private static final Set<ResourceLocation> HIDDEN = new HashSet<>();

    static {
        HIDDEN.add(new ResourceLocation("minecraft:stone"));
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("Xray: preInit complete");
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event) {
        int replaced = 0;
        Set<ModelResourceLocation> keys = new HashSet<>(event.getModelRegistry().getKeys());

        for (ModelResourceLocation mrl : keys) {
            String variant = mrl.getVariant();
            if ("inventory".equals(variant)) {
                continue;
            }

            ResourceLocation loc = new ResourceLocation(mrl.getNamespace(), mrl.getPath());
            if (!HIDDEN.contains(loc)) {
                continue;
            }

            IBakedModel original = event.getModelRegistry().getObject(mrl);
            if (original == null) {
                continue;
            }

            event.getModelRegistry().putObject(mrl, new InvisibleModel(original));
            replaced++;
        }

        LOGGER.info("Xray: replaced {} stone models", replaced);
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

        @Override public boolean isAmbientOcclusion() { return false; }
        @Override public boolean isGui3d() { return original.isGui3d(); }
        @Override public boolean isBuiltInRenderer() { return original.isBuiltInRenderer(); }
        @Override public TextureAtlasSprite getParticleTexture() { return original.getParticleTexture(); }
        @Override public ItemCameraTransforms getItemCameraTransforms() { return original.getItemCameraTransforms(); }
        @Override public ItemOverrideList getOverrides() { return original.getOverrides(); }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
            return original.handlePerspective(type);
        }
    }
}
