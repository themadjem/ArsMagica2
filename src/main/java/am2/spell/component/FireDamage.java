package am2.spell.component;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.ArsMagica2;
import am2.affinity.Affinity;
import am2.api.DamageSources;
import am2.defs.ItemDefs;
import am2.defs.SkillDefs;
import am2.items.ItemOre;
import am2.multiblock.MultiblockStructureDefinition;
import am2.particles.AMParticle;
import am2.rituals.IRitualInteraction;
import am2.rituals.RitualShapeHelper;
import am2.spell.IComponent;
import am2.spell.SpellModifiers;
import am2.utils.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FireDamage implements IComponent, IRitualInteraction{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
//		Block block = world.getBlock(blockx, blocky, blockz);
//
//		if (block == BlocksCommonProxy.obelisk){
//			if (RitualShapeHelper.instance.matchesRitual(this, world, pos)){
//				if (!world.isRemote){
//					RitualShapeHelper.instance.consumeReagents(this, world, pos);
//					RitualShapeHelper.instance.consumeShape(this, world, pos);
//					world.setBlock(blockx, blocky, blockz, BlocksCommonProxy.blackAurem);
//					PowerNodeRegistry.For(world).registerPowerNode((IPowerNode)world.getTileEntity(pos));
//				}else{
//
//				}
//
//				return true;
//			}
//		}
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (!(target instanceof EntityLivingBase)) return false;
		float baseDamage = 6;
		double damage = SpellUtils.getModifiedDouble_Add(baseDamage, stack, caster, target, world, SpellModifiers.DAMAGE);
		if (isNetherMob(target))
			return true;
		return SpellUtils.attackTargetSpecial(stack, target, DamageSources.causeFireDamage(caster), SpellUtils.modifyDamage(caster, (float)damage));
	}

	private boolean isNetherMob(Entity target){
		return target instanceof EntityPigZombie ||
				//target instanceof EntityDarkling ||
				//target instanceof EntityFireElemental ||
				target instanceof EntityGhast;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 120;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 5; ++i){
			AMParticle particle = (AMParticle)ArsMagica2.proxy.particleManager.spawn(world, "explosion_2", x, y, z);
			if (particle != null){
				particle.addRandomOffset(1, 0.5, 1);
				particle.addVelocity(rand.nextDouble() * 0.2 - 0.1, rand.nextDouble() * 0.2, rand.nextDouble() * 0.2 - 0.1);
				particle.setAffectedByGravity();
				particle.setDontRequireControllers();
				particle.setMaxAge(5);
				particle.setParticleScale(0.1f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(SkillDefs.FIRE);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.RED.getDyeDamage()),
				Items.FLINT_AND_STEEL,
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_VINTEUM),
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.01f;
	}

	@Override
	public MultiblockStructureDefinition getRitualShape(){
		return RitualShapeHelper.instance.corruption;
	}


	@Override
	public ItemStack[] getReagents(){
		return new ItemStack[]{
				new ItemStack(ItemDefs.mobFocus),
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_SUNSTONE)
		};
	}

	@Override
	public int getReagentSearchRadius(){
		return 3;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {}
}