package TFC.OreMod;

import TFC.TFCBlocks;
import TFC.API.Constant.TFCBlockID;

public class TFCRock {
	public int blockID;
	public int meta;
	
	public static TFCRock granite = new TFCRock( TFCBlocks.StoneIgIn.blockID, 0 );
	public static TFCRock diorite = new TFCRock( TFCBlocks.StoneIgIn.blockID, 1 );
	public static TFCRock gabbro = new TFCRock( TFCBlocks.StoneIgIn.blockID, 2 );
	
	public static TFCRock rhyolite = new TFCRock( TFCBlocks.StoneIgEx.blockID, 0 );
	public static TFCRock basalt = new TFCRock( TFCBlocks.StoneIgEx.blockID, 1 );
	public static TFCRock andesite = new TFCRock( TFCBlocks.StoneIgEx.blockID, 2 );
	public static TFCRock dacite = new TFCRock( TFCBlocks.StoneIgEx.blockID, 3 );
	
	public static TFCRock siltstone = new TFCRock( TFCBlocks.StoneSed.blockID, 0 );
	public static TFCRock mudstone = new TFCRock( TFCBlocks.StoneSed.blockID, 1 );
	public static TFCRock shale = new TFCRock( TFCBlocks.StoneSed.blockID, 2 );
	public static TFCRock claystone = new TFCRock( TFCBlocks.StoneSed.blockID, 3 );
	public static TFCRock rockSalt = new TFCRock( TFCBlocks.StoneSed.blockID, 4 );
	public static TFCRock limestone = new TFCRock( TFCBlocks.StoneSed.blockID, 5 );
	public static TFCRock conglomerate = new TFCRock( TFCBlocks.StoneSed.blockID, 6 );
	public static TFCRock dolomite = new TFCRock( TFCBlocks.StoneSed.blockID, 7 );
	public static TFCRock chert = new TFCRock( TFCBlocks.StoneSed.blockID, 8 );
	public static TFCRock chalk = new TFCRock( TFCBlocks.StoneSed.blockID, 9 );
	
	public static TFCRock quartzite = new TFCRock( TFCBlocks.StoneMM.blockID, 0 );
	public static TFCRock slate = new TFCRock( TFCBlocks.StoneMM.blockID, 1 );
	public static TFCRock phyllite = new TFCRock( TFCBlocks.StoneMM.blockID, 2 );
	public static TFCRock schist = new TFCRock( TFCBlocks.StoneMM.blockID, 3 );
	public static TFCRock gneiss = new TFCRock( TFCBlocks.StoneMM.blockID, 4 );
	public static TFCRock marble = new TFCRock( TFCBlocks.StoneMM.blockID, 5 );
	
	public TFCRock( int blockID, int meta ){
		this.blockID = blockID;
		this.meta = meta;
	}
}
