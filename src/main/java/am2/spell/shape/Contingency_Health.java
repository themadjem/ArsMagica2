package am2.spell.shape;

import java.util.EnumSet;

import am2.api.affinity.Affinity;
import am2.api.spell.SpellModifiers;
import am2.api.spell.SpellShape;
import am2.defs.BlockDefs;
import am2.defs.ItemDefs;
import am2.extensions.EntityExtension;
import am2.items.ItemOre;
import am2.items.ItemSpellBase;
import am2.spell.ContingencyType;
import am2.spell.SpellCastResult;
import am2.utils.AffinityShiftUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class Contingency_Health extends SpellShape{

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				Items.CLOCK,
				PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.HEALING),
				BlockDefs.tarmaRoot,
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_BLUE_TOPAZ),
				"E:*", 5000,
				AffinityShiftUtils.getEssenceForAffinity(Affinity.LIFE)
		};
	}

	@Override
	public SpellCastResult beginStackStage(ItemSpellBase item, ItemStack stack, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, EnumFacing side, boolean giveXP, int useCount){
		EntityExtension.For(target != null ? target : caster).setContingency(ContingencyType.HEALTH, stack);
		return SpellCastResult.SUCCESS;
	}

	@Override
	public boolean isChanneled(){
		return false;
	}

	@Override
	public float manaCostMultiplier(ItemStack spellStack){
		return 10;
	}

	@Override
	public boolean isTerminusShape(){
		return false;
	}

	@Override
	public boolean isPrincipumShape(){
		return true;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.noneOf(SpellModifiers.class);
	}
	
//
//	@Override
//	public String getSoundForAffinity(Affinity affinity, ItemStack stack, World world){
//		return "arsmagica2:spell.contingency.contingency";
//	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {}
}