package com.thunder.item;

import com.thunder.util.Utilities;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class DNAPattern extends ItemBionisation {

    public static final String DNA_ARRAY_KEY = Utilities.getModIdString("custom_dna");

    public DNAPattern(){
        super();
        this.setMaxStackSize(1);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        NBTTagCompound tag = stack.getTagCompound();
        if(tag != null && !tag.hasKey(DNA_ARRAY_KEY)){
            int [] dna_array = new int[8];
            tag.setIntArray(DNA_ARRAY_KEY, dna_array);
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey(DNA_ARRAY_KEY)) {
            int [] dna_array = tag.getIntArray(DNA_ARRAY_KEY);
            tooltip.add(I18n.format("tooltip.dna"));
            for (int i = 0; i < dna_array.length; i++) {
                //to do: add text visualization. not numeric
                tooltip.add((dna_array[i] == 0 ? TextFormatting.RED + "<" + I18n.format("tooltip.empty") + ">" : TextFormatting.GREEN + "<" + I18n.format("tooltip.gene." + dna_array[i]) + ">"));
            }
        }
    }
}
