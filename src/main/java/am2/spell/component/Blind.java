package am2.spell.component;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.ArsMagica2;
import am2.api.affinity.Affinity;
import am2.buffs.BuffEffectAstralDistortion;
import am2.defs.ItemDefs;
import am2.defs.PotionEffectsDefs;
import am2.multiblock.MultiblockStructureDefinition;
import am2.particles.AMParticle;
import am2.particles.ParticleOrbitEntity;
import am2.rituals.IRitualInteraction;
import am2.rituals.RitualShapeHelper;
import am2.spell.IComponent;
import am2.spell.SpellModifiers;
import am2.utils.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Blind implements IComponent, IRitualInteraction{

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (target instanceof EntityLivingBase) {
			int duration = SpellUtils.getModifiedInt_Mul(PotionEffectsDefs.default_buff_duration, stack, caster, target, world, SpellModifiers.DURATION);
			//duration = SpellUtils.modifyDurationBasedOnArmor(caster, duration);

			if (RitualShapeHelper.instance.matchesRitual(this, world, target.getPosition())){
				duration += (3600 * (SpellUtils.countModifiers(SpellModifiers.BUFF_POWER, stack) + 1));
				RitualShapeHelper.instance.consumeReagents(this, world, target.getPosition());
			}
			
			if (!world.isRemote)
				((EntityLivingBase)target).addPotionEffect(new BuffEffectAstralDistortion(duration, SpellUtils.countModifiers(SpellModifiers.BUFF_POWER, stack)));

			return true;
		}
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 80;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 15; ++i){
			AMParticle particle = (AMParticle)ArsMagica2.proxy.particleManager.spawn(world, "lens_flare", x, y, z);
			if (particle != null){
				particle.AddParticleController(new ParticleOrbitEntity(particle, target, 0.1f, 1, false).SetTargetDistance(rand.nextDouble() + 0.5));
				particle.setMaxAge(25 + rand.nextInt(10));
				particle.setRGBColorF(0, 0, 0);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.ENDER);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.BLACK.getDyeDamage()),
				"P:!0 & !1 & !2 & 3 & 3+6",
				"P:!0 & 1 & 2 & !3 & 2+6"
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.05f;
	}

	@Override
	public MultiblockStructureDefinition getRitualShape(){
		return RitualShapeHelper.instance.hourglass;
	}

	@Override
	public ItemStack[] getReagents(){
		return new ItemStack[]{
				new ItemStack(Items.CARROT),
				new ItemStack(Items.POISONOUS_POTATO)
		};
	}

	@Override
	public int getReagentSearchRadius(){
		return 3;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world,
			BlockPos blockPos, EnumFacing blockFace, double impactX,
			double impactY, double impactZ, EntityLivingBase caster) {
		// TODO Auto-generated method stub
		return false;
	}
}
