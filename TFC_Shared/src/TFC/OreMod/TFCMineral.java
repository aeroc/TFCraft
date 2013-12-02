package TFC.OreMod;

import java.util.ArrayList;

import TFC.TFCBlocks;
import TFC.TerraFirmaCraft;

public class TFCMineral {
	public int blockID;
	public int meta;
	public ArrayList<TFCRock> occurrenceList = new ArrayList<TFCRock>();
	public static enum GenerationType { VEIN, BED, PIPE };
	public GenerationType type;
	
	//IGIN TFCRock.granite, TFCRock.diorite, TFCRock.gabbro
	//IGEX TFCRock.rhyolite, TFCRock.basalt, TFCRock.andesite, TFCRock.dacite
	//SED  TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk
	//MM   TFCRock.quartzite, TFCRock.slate, TFCRock.phyllite, TFCRock.schist, TFCRock.gneiss, TFCRock.marble
	
	//test copper (in all layers)
	//public static TFCMineral nativeCopper = new TFCMineral( TFCBlocks.Ore.blockID, 0, new TFCRock[]{ TFCRock.granite, TFCRock.diorite, TFCRock.gabbro, TFCRock.rhyolite, TFCRock.basalt, TFCRock.andesite, TFCRock.dacite, TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk, TFCRock.quartzite, TFCRock.slate, TFCRock.phyllite, TFCRock.schist, TFCRock.gneiss, TFCRock.marble } ); //all groups for testing purposes
	
	public static TFCMineral nativeCopper = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore.blockID, 0, new TFCRock[]{ TFCRock.rhyolite, TFCRock.basalt, TFCRock.andesite, TFCRock.dacite } );
	public static TFCMineral nativeGold = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore.blockID, 1, new TFCRock[]{ TFCRock.rhyolite, TFCRock.basalt, TFCRock.andesite, TFCRock.dacite, TFCRock.granite, TFCRock.diorite, TFCRock.gabbro } );
	public static TFCMineral nativePlatinum = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore.blockID, 2, new TFCRock[]{ TFCRock.granite, TFCRock.diorite, TFCRock.gabbro } );
	public static TFCMineral hematite = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore.blockID, 3, new TFCRock[]{ TFCRock.rhyolite, TFCRock.basalt, TFCRock.andesite, TFCRock.dacite } );
	public static TFCMineral nativeSilver = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore.blockID, 4, new TFCRock[]{ TFCRock.granite, TFCRock.gneiss } );
	public static TFCMineral cassiterite = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore.blockID, 5, new TFCRock[]{ TFCRock.granite, TFCRock.rhyolite, TFCRock.basalt, TFCRock.andesite, TFCRock.dacite } );
	public static TFCMineral galena = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore.blockID, 6, new TFCRock[]{ TFCRock.rhyolite, TFCRock.basalt, TFCRock.andesite, TFCRock.dacite, TFCRock.quartzite, TFCRock.slate, TFCRock.phyllite, TFCRock.schist, TFCRock.gneiss, TFCRock.marble, TFCRock.granite, TFCRock.limestone } );
	public static TFCMineral bismuthinite = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore.blockID, 7, new TFCRock[]{ TFCRock.granite, TFCRock.diorite, TFCRock.gabbro, TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk } );
	public static TFCMineral garnierite = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore.blockID, 8, new TFCRock[]{ TFCRock.gabbro } );
	public static TFCMineral malachite = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore.blockID, 9, new TFCRock[]{ TFCRock.limestone, TFCRock.marble } );
	public static TFCMineral magnetite = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore.blockID, 10, new TFCRock[]{ TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk } );
	public static TFCMineral limonite = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore.blockID, 11, new TFCRock[]{ TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk } );
	public static TFCMineral sphalerite = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore.blockID, 12, new TFCRock[]{ TFCRock.quartzite, TFCRock.slate, TFCRock.phyllite, TFCRock.schist, TFCRock.gneiss, TFCRock.marble } );
	public static TFCMineral tetrahedrite = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore.blockID, 13, new TFCRock[]{ TFCRock.granite, TFCRock.diorite, TFCRock.gabbro, TFCRock.rhyolite, TFCRock.basalt, TFCRock.andesite, TFCRock.dacite, TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk, TFCRock.quartzite, TFCRock.slate, TFCRock.phyllite, TFCRock.schist, TFCRock.gneiss, TFCRock.marble } );
	public static TFCMineral bituminousCoal = new TFCMineral( TFCMineral.GenerationType.BED, TFCBlocks.Ore.blockID, 14, new TFCRock[]{ TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk } );
	public static TFCMineral lignite = new TFCMineral( TFCMineral.GenerationType.BED, TFCBlocks.Ore.blockID, 15, new TFCRock[]{ TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk } );
	
	public static TFCMineral kaolinite = new TFCMineral( TFCMineral.GenerationType.BED, TFCBlocks.Ore2.blockID, 0, new TFCRock[]{ TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk } );
	public static TFCMineral gypsum = new TFCMineral( TFCMineral.GenerationType.BED, TFCBlocks.Ore2.blockID, 1, new TFCRock[]{ TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk } );
	public static TFCMineral satinspar = new TFCMineral( TFCMineral.GenerationType.BED, TFCBlocks.Ore2.blockID, 2, new TFCRock[]{ TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk } );
	public static TFCMineral selenite = new TFCMineral( TFCMineral.GenerationType.BED, TFCBlocks.Ore2.blockID, 3, new TFCRock[]{ TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk } );
	public static TFCMineral graphite = new TFCMineral( TFCMineral.GenerationType.BED, TFCBlocks.Ore2.blockID, 4, new TFCRock[]{ TFCRock.gneiss, TFCRock.quartzite, TFCRock.marble, TFCRock.schist } );
	public static TFCMineral kimberlite = new TFCMineral( TFCMineral.GenerationType.PIPE, TFCBlocks.Ore2.blockID, 5, new TFCRock[]{ TFCRock.gabbro } );
	public static TFCMineral petrifiedWood = new TFCMineral( TFCMineral.GenerationType.BED, TFCBlocks.Ore2.blockID, 6, new TFCRock[]{ TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk } );
	public static TFCMineral sulfur = new TFCMineral( TFCMineral.GenerationType.BED, TFCBlocks.Ore2.blockID, 7, new TFCRock[]{ TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk } );
	public static TFCMineral jet = new TFCMineral( TFCMineral.GenerationType.BED, TFCBlocks.Ore2.blockID, 8, new TFCRock[]{ TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk } );
	public static TFCMineral microcline = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore2.blockID, 9, new TFCRock[]{ TFCRock.granite } );
	public static TFCMineral pitchblende = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore2.blockID, 10, new TFCRock[]{ TFCRock.granite } );
	public static TFCMineral cinnabar = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore2.blockID, 11, new TFCRock[]{ TFCRock.rhyolite, TFCRock.basalt, TFCRock.andesite, TFCRock.dacite, TFCRock.shale, TFCRock.quartzite } ); 
	public static TFCMineral cryolite = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore2.blockID, 12, new TFCRock[]{ TFCRock.granite } );
	public static TFCMineral saltpeter = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore2.blockID, 13, new TFCRock[]{ TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk } );
	public static TFCMineral serpentine = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore2.blockID, 14, new TFCRock[]{ TFCRock.granite, TFCRock.diorite, TFCRock.gabbro } );
	public static TFCMineral sylvite = new TFCMineral( TFCMineral.GenerationType.BED, TFCBlocks.Ore2.blockID, 15, new TFCRock[]{ TFCRock.rockSalt } );

	public static TFCMineral borax = new TFCMineral( TFCMineral.GenerationType.BED, TFCBlocks.Ore3.blockID, 0, new TFCRock[]{ TFCRock.rockSalt } );
	public static TFCMineral lapisLazuli = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore3.blockID, 1, new TFCRock[]{ TFCRock.marble } );	
	public static TFCMineral olivine = new TFCMineral( TFCMineral.GenerationType.VEIN, TFCBlocks.Ore3.blockID, 2, new TFCRock[]{ TFCRock.gabbro } );
	
	public static TFCMineral anthracite = new TFCMineral( TFCMineral.GenerationType.BED, TerraFirmaCraft.anthracite.blockID, 0, new TFCRock[]{ TFCRock.siltstone, TFCRock.mudstone, TFCRock.shale, TFCRock.claystone, TFCRock.rockSalt, TFCRock.limestone, TFCRock.conglomerate, TFCRock.dolomite, TFCRock.chert, TFCRock.chalk } );
	
	public TFCMineral( GenerationType type, int blockID, int meta, TFCRock[] occurrences ){
		this.type = type;
		this.blockID = blockID;
		this.meta = meta;
		for( TFCRock rock : occurrences ){
			this.occurrenceList.add( rock );
		}
	}
	public boolean canOccurIn( int blockID, int meta ){
		for( TFCRock rock : this.occurrenceList ){
			if( rock.blockID == blockID && rock.meta == meta ){
				return true;
			}
		}
		return false;
	}
	public int[] getLayersInTFCFormat(){
		int[] layers = new int[this.occurrenceList.size() * 2];
		for( int i = 0; i < this.occurrenceList.size(); i++ ){
			layers[i*2] = this.occurrenceList.get( i ).blockID;
			layers[i*2+1] = this.occurrenceList.get( i ).meta;
		}
		return layers;
	}
	public String getName(){
		if( this.blockID == TFCBlocks.Ore.blockID ){
			switch( this.meta ){
				case 0:		return "copper";
				case 1:		return "gold";
				case 2:		return "platinum";
				case 3:		return "hematite";
				case 4:		return "silver";
				case 5:		return "cassiterite";
				case 6:		return "galena";
				case 7:		return "bismuthinite";
				case 8:		return "garnierite";
				case 9:		return "malachite";
				case 10:	return "magnetite";
				case 11:	return "limonite";
				case 12:	return "sphalerite";
				case 13:	return "tetrahedrite";
				case 14:	return "bit. coal";
				case 15:	return "lignite";
			}
		}
		else if( this.blockID == TFCBlocks.Ore2.blockID ){
			switch( this.meta ){
				case 0:		return "kaolinite";
				case 1:		return "gypsum";
				case 2:		return "satinspar";
				case 3:		return "selenite";
				case 4:		return "graphite";
				case 5:		return "kimberlite";
				case 6:		return "petrified wood";
				case 7:		return "sulfur";
				case 8:		return "jet";
				case 9:		return "microcline";
				case 10:	return "pitchblende";
				case 11:	return "cinnabar";
				case 12:	return "cryolite";
				case 13:	return "saltpeter";
				case 14:	return "serpentine";
				case 15:	return "sylvite";
			}
		}
		else if( this.blockID == TFCBlocks.Ore3.blockID ){
			switch( this.meta){
				case 0:		return "borax";
				case 1:		return "lapis lazuli";
				case 2:		return "olivine";
			}
		}
		else if( this.blockID == 980 ){
			return "anthracite";
		}
		return "unknown";
	}
}
