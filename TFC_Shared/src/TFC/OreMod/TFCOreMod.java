package TFC.OreMod;

import net.minecraft.block.Block;
import TFC.TFCBlocks;
import cpw.mods.fml.common.IWorldGenerator;

public class TFCOreMod {
	
	//Affected class files:
	//-TFC.TerraFirmaCraft
	//-TFC.WorldGen.Generators.WorldGenMinableTFC //Not sure if this is changed anymore
	//-TFC.Blocks.Terrain.BlockGrass.updateTick() grass off
	//-TFC.Blocks.Vanilla.BlockCustomSapling.updateTick() sapling growth 7 days --> 50 years
	//-TFC.WorldGen.TFCChunkProviderGenerate.replaceBlocksForBiomeHigh
	

	public static IWorldGenerator oreGenerator;
	
	public static void initOreMod(){
		TFCOreMod.oreGenerator = new TFCOreModWorldGenOre();
	}
}