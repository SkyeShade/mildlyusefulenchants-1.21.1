package com.skyeshade.mildlyusefuladditions;

import com.skyeshade.mildlyusefuladditions.datacomponents.ModDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Stack;
import java.util.UUID;

public final class ModDamage {
    public static final ResourceKey<DamageType> PIPE_BOMB =
            ResourceKey.create(Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(MildlyUsefulAdditions.MODID, "pipe_bomb"));

    private ModDamage() {}

    public static Holder<DamageType> type(RegistryAccess access) {
        return access.lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(PIPE_BOMB);
    }


    public static DamageSource source(Level level, Entity direct, Entity owner, Vec3 pos) {
        Holder<DamageType> h = type(level.registryAccess());
        if (pos != null) {
            return new DamageSource(h, direct, owner, pos);
        }
        return new DamageSource(h, direct, owner);
    }

}