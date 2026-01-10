package com.teamharmony.cakeworks.core.registry;

import com.teamharmony.cakeworks.client.particle.IcingParticle;
import com.teamharmony.cakeworks.core.CakeWorks;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = CakeWorks.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CakeWorksParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, CakeWorks.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> ICING = PARTICLE_TYPES.register("icing", () -> new SimpleParticleType(false));

    @SubscribeEvent
    public static void registerParticleTypes(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ICING.get(), IcingParticle.Provider::new);
    }
}