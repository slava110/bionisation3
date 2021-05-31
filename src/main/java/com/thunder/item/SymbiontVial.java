package com.thunder.item;

import com.thunder.bionisation.CommonProxy;
import com.thunder.laboratory.EffectContainer;
import com.thunder.laboratory.IBioSample;
import com.thunder.laboratory.SampleType;
import com.thunder.player.BioPlayerProvider;
import com.thunder.player.IBioPlayer;
import com.thunder.util.Utilities;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class SymbiontVial extends ItemBionisation {

    public static final String SYMBIONT_VIAL_KEY = Utilities.getModIdString("symbiont");

    public SymbiontVial(){
        super();
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItemMainhand();
        if(!worldIn.isRemote) {
            if(stack.hasTagCompound()){
                NBTTagCompound nbt = stack.getTagCompound();
                if(nbt.hasKey(SYMBIONT_VIAL_KEY)) {
                    IBioPlayer cap = playerIn.getCapability(BioPlayerProvider.BIO_PLAYER_CAPABILITY, null);
                    String [] symb = nbt.getString(SYMBIONT_VIAL_KEY).split(":");
                    int id = Integer.parseInt(symb[0]);
                    int power = Integer.parseInt(symb[1]);
                    IBioSample effect = Utilities.getNewEffectCopy(Utilities.findEffectById(id));
                    if(effect != null) {
                        effect.setPower(power);
                        cap.addEffect(effect, playerIn);
                        nbt.removeTag(SYMBIONT_VIAL_KEY);
                    }
                }
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        NBTTagCompound tag = stack.getTagCompound();
        if(tag != null && tag.hasKey(SYMBIONT_VIAL_KEY)){
            String [] symb = tag.getString(SYMBIONT_VIAL_KEY).split(":");
            tooltip.add(I18n.format("tooltip.symbiontvial.symbiont") + " " + TextFormatting.GREEN + I18n.format("tooltip.symbiontvial." + symb[0]));
            tooltip.add(I18n.format("tooltip.creativevial.power") + " " + TextFormatting.GREEN + (Integer.parseInt(symb[1]) + 1));
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(tab == CommonProxy.tabBionisation) {
            for (IBioSample smp : EffectContainer.getInstance().effects) {
                if (smp.getType() == SampleType.SYMBIONT) {
                    ItemStack stack = new ItemStack(this);
                    NBTTagCompound nbt = Utilities.getNbt(stack);
                    nbt.setString(SYMBIONT_VIAL_KEY, smp.getId() + ":" + smp.getPower());
                    items.add(stack);
                }
            }
        }
    }
}
