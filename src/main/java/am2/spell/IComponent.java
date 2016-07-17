package am2.spell;

import java.util.Random;
import java.util.Set;

import am2.api.affinity.Affinity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IComponent extends ISpellPart{
	/**
	 * Apply the effect to a single block
	 *
	 * @param stack     The item stack that contains the effect data
	 * @param world     The world the effect is in
	 * @param blockPos  The coordinates of the block
	 * @param blockFace The face of the block that was targeted
	 * @param impactX   The x coordinate of the impact
	 * @param impactY   The y coordinate of the impact
	 * @param impactZ   The z coordinate of the impact
	 * @param caster    The caster of the spell
	 * @return True if the effect was successfully applied to the block
	 */
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos blockPos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster);

	/**
	 * Apply the effect to a single entity
	 *
	 * @param stack  The stack representing the spell
	 * @param world  The world the spell was cast in
	 * @param caster The caster of the spell
	 * @param target The current target of the spell
	 * @return True if the effect was applied successfully to the entity
	 */
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target);

	/**
	 * Gets the mana cost of the spell
	 */
	public float manaCost(EntityLivingBase caster);

	/**
	 * Gets any reagents that must be present in the caster's inventory in order
	 * to cast the spell.
	 */
	public ItemStack[] reagents(EntityLivingBase caster);

	/**
	 * Spawn visual effects for the component
	 *
	 * @param colorModifier The color from the color modifier.  -1 if missing.
	 */
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier);

	/**
	 * Gets the affinity of the spell
	 */
	public Set<Affinity> getAffinity();

	/**
	 * Gets the amount (before diminishing returns) that this component, when successfully applied,
	 * shifts the caster's affinity
	 *
	 * @param affinity The affinity being shifted
	 */
	public float getAffinityShift(Affinity affinity);

}
