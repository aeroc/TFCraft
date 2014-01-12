package TFC.Blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import TFC.Reference;
import TFC.TFCItems;
import TFC.API.Constant.Global;
import TFC.Core.Recipes;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLogNatural extends BlockTerra
{
	public BlockLogNatural(int i) 
	{
		super(i, Material.wood);
		this.setTickRandomly(true);
	}
	
	@Override
	public void updateTick(World world, int i, int j, int k, Random rand)
	{
		if (world.isRemote) return;
		
		if (world.isBlockOpaqueCube(i, j-1, k)) return;
		
		if (world.getBlockId(i+1, j, k  ) == blockID || 
			world.getBlockId(i-1, j, k  ) == blockID || 
			world.getBlockId(i  , j, k+1) == blockID || 
			world.getBlockId(i  , j, k-1) == blockID || 
			world.getBlockId(i+1, j, k+1) == blockID || 
			world.getBlockId(i+1, j, k-1) == blockID || 
			world.getBlockId(i-1, j, k+1) == blockID || 
			world.getBlockId(i-1, j, k-1) == blockID) 
			return;
		
		world.setBlock(i, j, k, 0);
	}

	@SideOnly(Side.CLIENT)
	@Override
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List list)
	{
		for (int i = 0; i < Global.WOOD_ALL.length; i++) 
		{
			list.add(new ItemStack(this,1,i));
		}
	}

	@Override
	public float getBlockHardness(World par1World, int par2, int par3, int par4)
	{
		return this.blockHardness;
	}

	private boolean checkOut(World world, int i, int j, int k, int l)
	{
		if (world.getBlockId(i, j, k) == blockID && world.getBlockMetadata(i, j, k) == l) 
			return true;
		return false;
	}

	@Override
	public int damageDropped(int j) {
		return j;
	}	

	@Override
	public Icon getIcon(int i, int j) 
	{
		if (i == 0 || i == 1)
			return innerIcons[j];
		return sideIcons[j];
	}
	
	public static Icon[] sideIcons = new Icon[Global.WOOD_ALL.length];
	public static Icon[] innerIcons = new Icon[Global.WOOD_ALL.length];
	public static Icon[] rotatedSideIcons = new Icon[Global.WOOD_ALL.length];
	
	@Override
	public void registerIcons(IconRegister registerer)
	{
		for (int i = 0; i < Global.WOOD_ALL.length; i++)
		{
			sideIcons[i] = registerer.registerIcon(Reference.ModID + ":" + "wood/trees/" + Global.WOOD_ALL[i] + " Log");
			innerIcons[i] = registerer.registerIcon(Reference.ModID + ":" + "wood/trees/" + Global.WOOD_ALL[i] + " Log Top");
			rotatedSideIcons[i] = registerer.registerIcon(Reference.ModID + ":" + "wood/trees/" + Global.WOOD_ALL[i] + " Log Side");
		}
	}

	static int damage = 0;
	boolean isStone = false;

	@Override
	public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l)
	{		
		if (world.isRemote) return;

		//we need to make sure the player has the correct tool out
		boolean isAxeorSaw = false;
		boolean isHammer = false;
		ItemStack equip = entityplayer.getCurrentEquippedItem();
		
		if (equip==null)
		{
			world.setBlock(i, j, k, blockID, l, 0x2);
			return;
		}
		
		for (int cnt = 0; cnt < Recipes.Axes.length; cnt++)
		{
			if (equip.getItem() == Recipes.Axes[cnt])
			{
				isAxeorSaw = true;
				if (cnt < 4)
					isStone = true;
				break;
			}
		}
		//if (!isAxeorSaw) 
		//{
		//	for(int cnt = 0; cnt < Recipes.Saws.length; cnt++)
		//	{
		//		if(equip.getItem() == Recipes.Saws[cnt])
		//		isAxeorSaw = true;
		//	}
		//}
		if (!isAxeorSaw) {
			for (int cnt = 0; cnt < Recipes.Hammers.length; cnt++)
			{
				if (equip.getItem() == Recipes.Hammers[cnt])
					isHammer = true;
					break;
			}
		}

		if (!isAxeorSaw && !isHammer) return;

		if (isHammer)
		{
			EntityItem item = new EntityItem(world, i+0.5, j+0.5, k+0.5, new ItemStack(Item.stick, 1+world.rand.nextInt(3)));
			world.spawnEntityInWorld(item);
			return;
		}
		
		damage = -1;
		ProcessTree(world, i, j, k, l, equip);	
		
		if(damage + equip.getItemDamage() <= equip.getMaxDamage())
		{
			equip.damageItem(damage, entityplayer);
			return;
		}
		
		int ind = entityplayer.inventory.currentItem;
		entityplayer.inventory.setInventorySlotContents(ind, null);
		world.setBlock(i, j, k, blockID, l, 0x2);
	}
	
	@Override
	public boolean canBlockStay(World par1World, int par2, int par3, int par4)
	{
		return true;
	}

	@Override
	public void onBlockDestroyedByExplosion(World world, int i, int j, int k, Explosion ex) 
	{
		ProcessTree(world, i, j, k, world.getBlockMetadata(i, j, k), null);
	}

	class TreeScan {
		public World world;
		public int i;
		public int j;
		public int k;
		public int l;
		public boolean checkArray[][][];
		public int x;
		public int y;
		public int z;
		public ItemStack stack;
		public int xLength;
		public int zLength;
		public TreeScan(World world, int i, int j, int k, int l, boolean[][][] checkArray,int x, int y, int z, ItemStack stack) {
			this.world = world;
			this.i = i;
			this.j = j;
			this.k = k;
			this.l = l;
			this.checkArray = checkArray;
			this.x = x;
			this.y = y;
			this.z = z;
			this.stack = stack;
			this.xLength = checkArray.length;
			this.zLength = checkArray[0][0].length;
		}
		/**
		 * Recursion helper that creates a new instance with updated coordinates
		 * @return Returns a new scanner with the provided coordinates to help recursion
		 */
		public TreeScan updateCoords(int i, int j, int k, int x, int y, int z) {
			return new TreeScan(this.world, i, j, k, this.l, this.checkArray, x, y, z, this.stack);
		}
	}
	
	private void ProcessTree(World world, int i, int j, int k, int l, ItemStack stack)
	{
		TreeScan ts = new TreeScan(world, i, j, k, l, new boolean[11][50][11], 6, 0, 6, stack);
		int x = i;
		int y = 0;
		int z = k;

		boolean reachedTop = false;
		while (!reachedTop) {
			if (world.getBlockId(x, j+y+1, z) == 0) {
				if (l != 9 && l != 15) {
					reachedTop = true;
				} else {
					if (world.getBlockId(x+1, j+y+1, z  ) != blockID && 
						world.getBlockId(x-1, j+y+1, z  ) != blockID && 
						world.getBlockId(x  , j+y+1, z+1) != blockID && 
						world.getBlockId(x  , j+y+1, z-1) != blockID && 
						world.getBlockId(x-1, j+y+1, z-1) != blockID && 
						world.getBlockId(x-1, j+y+1, z+1) != blockID && 
						world.getBlockId(x+1, j+y+1, z+1) != blockID && 
						world.getBlockId(x+1, j+y+1, z-1) != blockID) {
							reachedTop = true;
					}
				}
			}
			y++;
		}
		while (y >= 0) {
			scanLogs(ts.updateCoords(i,j+y,k,6,y--,6));
		}
	}

	private void scanLogs(TreeScan ts)
	{
		if (ts.y < 0) return;

		ts.checkArray[ts.x][ts.y][ts.z] = true;
		int offsetX = 0;int offsetY = 0;int offsetZ = 0;
		
		for (offsetX = -2; offsetX <= 2; offsetX++)
		{
			if (ts.x+offsetX >= ts.xLength || ts.x+offsetX < 0) continue;

			for (offsetZ = -2; offsetZ <= 2; offsetZ++)
			{
				if (ts.z+offsetZ >= ts.zLength || ts.z+offsetZ < 0) continue;

				if (ts.checkArray[ts.x+offsetX][ts.y][ts.z+offsetZ]) continue;

				if (!checkOut(ts.world, ts.i+offsetX, ts.j, ts.k+offsetZ, ts.l)) continue;

				scanLogs(ts.updateCoords(ts.i+offsetX, ts.j, ts.k+offsetZ, ts.x+offsetX,ts.y,ts.z+offsetZ));
			}
		}
		
		damage++;
		if(ts.stack == null)
		{
			ts.world.setBlockToAir(ts.i, ts.j, ts.k);
			dropBlockAsItem_do(ts.world, ts.i, ts.j, ts.k, new ItemStack(Item.itemsList[TFCItems.Logs.itemID],1,ts.l));
			return;
		}

		if(damage+ts.stack.getItemDamage() <= ts.stack.getMaxDamage())
		{
			ts.world.setBlock(ts.i, ts.j, ts.k, 0, 0, 0x3);
			if((isStone && ts.world.rand.nextInt(10) != 0) || !isStone)
				dropBlockAsItem_do(ts.world, ts.i, ts.j, ts.k, new ItemStack(Item.itemsList[TFCItems.Logs.itemID],1,ts.l));
		}
	}


	@Override
	public int idDropped(int i, Random random, int j)
	{
		return TFCItems.Logs.itemID;
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l)
	{
		boolean check = false;
		
		int ijkMetadata = world.getBlockMetadata(i, j, k);
		
		for (int h = -2; h <= 2; h++)
		{
			for (int g = -2; g <= 2; g++)
			{
				for (int f = -2; f <= 2; f++)
				{
					if (world.getBlockId(i+h, j+g, k+f) == blockID && world.getBlockMetadata(i+h, j+g, k+f) == ijkMetadata)
					{
						check = true;
						break;
					}
				}
			}
		}
		
		if (check) 
			return;
		
		world.setBlock(i, j, k, 0, 0, 0x2);
		dropBlockAsItem_do(world, i, j, k, new ItemStack(Item.itemsList[TFCItems.Logs.itemID],1,l));
	}
}