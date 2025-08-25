package com.skyeshade.mildlyusefuladditions.item.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class Staff extends Item {
    public Staff(Properties properties) {
        super(properties);
    }

    /*@Override
    public ItemStack getDefaultInstance() {
        //return super.getDefaultInstance().
        return null;
    }

    // Armor code that has dyable part
    public static final class Layer {
        private final ResourceLocation assetName;
        private final String suffix;
        private final boolean dyeable;
        private final ResourceLocation innerTexture;
        private final ResourceLocation outerTexture;

        public Layer(ResourceLocation assetName, String suffix, boolean dyeable) {
            this.assetName = assetName;
            this.suffix = suffix;
            this.dyeable = dyeable;
            this.innerTexture = this.resolveTexture(true);
            this.outerTexture = this.resolveTexture(false);
        }

        public Layer(ResourceLocation assetName) {
            this(assetName, "", false);
        }

        private ResourceLocation resolveTexture(boolean innerTexture) {
            return this.assetName
                    .withPath(p_324187_ -> "textures/models/armor/" + this.assetName.getPath() + "_layer_" + (innerTexture ? 2 : 1) + this.suffix + ".png");
        }

        public ResourceLocation texture(boolean innerTexture) {
            return innerTexture ? this.innerTexture : this.outerTexture;
        }

        public boolean dyeable() {
            return this.dyeable;
        }
    }*/
}
