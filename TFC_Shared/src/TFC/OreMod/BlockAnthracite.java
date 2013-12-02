package TFC.OreMod;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import TFC.Reference;
import TFC.TFCBlocks;
import TFC.TFCItems;
import TFC.API.Constant.Global;
import TFC.Blocks.Terrain.BlockOre;

public class BlockAnthracite extends BlockOre{

	public BlockAnthracite( int i, Material material ) {
		super( i, material );
	}
	@Override
	public String getLocalizedName(){
		return "Anthracite";
	}
	@Override
	public void addCreativeItems( java.util.ArrayList list ){
		list.add( new ItemStack( this, 1, 0 ) );
    }
	@Override
	public void registerIcons( IconRegister iconRegisterer ){
		icons[0] = iconRegisterer.registerIcon( Reference.ModID + ":" + "ores/anthraciteOre" );
    }
	@Override
    public int damageDropped( int j ){
        return 0;
    }
	@Override
	public void harvestBlock( World world, EntityPlayer entityplayer, int i, int j, int k, int l ){
	    if( entityplayer != null ){
            entityplayer.addStat(StatList.mineBlockStatArray[blockID], 1);
            entityplayer.addExhaustion(0.075F);
        }
		Random random = new Random();

		ItemStack itemstack = new ItemStack( Item.coal, random.nextInt(3) + 1, 1 );

		if( itemstack != null ){
			dropBlockAsItem_do( world, i, j, k, itemstack );
		}
	}
	@Override
	public void onBlockExploded( World par1World, int par2, int par3, int par4, Explosion par5Explosion ){
		Random random = new Random();
		ItemStack itemstack;
		int meta = par1World.getBlockMetadata(par2, par3, par4);
		itemstack  = new ItemStack( Item.coal, random.nextInt(3) + 1, 1 );
		
		if( itemstack != null ){
			dropBlockAsItem_do( par1World, par2, par3, par4, itemstack );
		}
		onBlockDestroyedByExplosion( par1World, par2, par3, par4, par5Explosion );
	}
}
