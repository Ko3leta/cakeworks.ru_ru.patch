package com.teamharmony.cakeworks.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class IcingParticle extends TextureSheetParticle {
    public IcingParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.setSize(0.02F, 0.02F);
        this.gravity = 0.01F;
        this.friction = 0.8F;
        this.xd = motionX;
        this.yd = motionY;
        this.zd = motionZ;
        this.quadSize *= 0.5F + this.random.nextFloat() * 0.4F;
        this.lifetime = 60 + this.random.nextInt(20);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        } else {
            this.yd -= (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);

            this.xd *= (double)this.friction;
            this.yd *= 0.95D;
            this.zd *= (double)this.friction;

            if (this.onGround) {
                this.xd *= 0.1D;
                this.zd *= 0.1D;
                if (this.lifetime > 20) {
                    this.lifetime = 20;
                }
            }
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            IcingParticle icingParticle = new IcingParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            icingParticle.pickSprite(this.spriteSet);
            return icingParticle;
        }
    }
}