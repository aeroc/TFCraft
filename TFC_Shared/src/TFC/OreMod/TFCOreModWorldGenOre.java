package TFC.OreMod;

import java.util.Random;
import java.util.ArrayList;

import TFC.TFCBlocks;
import TFC.WorldGen.DataLayer;
import TFC.WorldGen.TFCWorldChunkManager;
import TFC.WorldGen.Generators.WorldGenMinableTFC;

import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;

public class TFCOreModWorldGenOre implements IWorldGenerator {
	
	public static ArrayList<Integer> groupIndexes;
	public static ArrayList<RegionMineralGroup> mineralGroups;
	public static long seed;

	/**
     * Generate some world
     *
     * @param random the chunk specific {@link Random}.
     * @param chunkX the chunk X coordinate of this chunk.
     * @param chunkZ the chunk Z coordinate of this chunk.
     * @param world : additionalData[0] The minecraft {@link World} we're generating for.
     * @param chunkGenerator : additionalData[1] The {@link IChunkProvider} that is generating.
     * @param chunkProvider : additionalData[2] {@link IChunkProvider} that is requesting the world generation.
     *
     */
    
	@Override
	public void generate( Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider ){
		int coordX = chunkX * 16;
		int coordZ = chunkZ * 16;
		
		int groupIndex = TFCOreModWorldGenOre.getGroupIndex( world.getSeed(), chunkX, chunkZ );
		RegionMineralGroup regionMineralGroup = TFCOreModWorldGenOre.mineralGroups.get( groupIndex );
		for( TFCMineralRarity mR : regionMineralGroup.mineralList ){
			//System.out.println( "Trying: " + mR.getRarity() + " rarity: " + mR.getMineral().getLayersInTFCFormat()[0] );
			int rarity = mR.getRarity();
			int absCoordZ = Math.abs( coordZ );
			if( absCoordZ > 20000 ){
				rarity *= 0.4f;
			}
			else if( absCoordZ > 19000 ){
				rarity *= 0.44f;
			}
			else if( absCoordZ > 18000 ){
				rarity *= 0.48f;
			}
			else if( absCoordZ > 17000 ){
				rarity *= 0.52f;
			}
			else if( absCoordZ > 16000 ){
				rarity *= 0.56f;
			}
			else if( absCoordZ > 15000 ){
				rarity *= 0.6f;
			}
			else if( absCoordZ > 14000 ){
				rarity *= 0.8f;
			}
			else if( absCoordZ > 13000 ){
				rarity *= 0.9f;
			}
			//15,000-20,000 50%
			//20,000 .4%
			if( this.tryToGenerateInChunk( mR.getMineral(), mR.getMineral().getLayersInTFCFormat(), rarity, world, random, coordX, coordZ) ){
				break; //Don't want to generate multiple veins in the same chunk
			}
		}
		//Test copper vein (6 rarity, all rock types)
		//this.tryToGenerateInChunk( TFCMineral.nativeCopper, new int[]{ TFCBlocks.StoneIgEx.blockID, -1, TFCBlocks.StoneIgIn.blockID, -1, TFCBlocks.StoneMM.blockID, -1, TFCBlocks.StoneSed.blockID, -1 }, 6, world, random, coordX, coordZ );
	}
	private static boolean tryToGenerateInChunk( TFCMineral mineral, int[] layers, int rarity, World world, Random random, int coordX, int coordZ ){
		if( world.getWorldChunkManager() instanceof TFCWorldChunkManager ){	//If this is the TFC overworld (not the nether)
			if( random.nextInt( rarity ) == 0 ){ //Might as well check to see if this chunk will even generate a vein before checking the rockLayers
				//Create a random start position within the chunk
				int startX = coordX + random.nextInt( 16 );
				int startZ = coordZ + random.nextInt( 16 );
				int startY = 0;

				for( int i = 255; i > 0 && startY == 0; i-- ){ //Find the highest rock, to complete the start position for veins
					if( TFCOreModWorldGenOre.isRock( world.getBlockId( startX, i, startZ ) ) ){
						startY = i;
					}
				}
				if( mineral.type == TFCMineral.GenerationType.BED ){	//Randomize the height for beds
					startY = random.nextInt( startY - 10 ) + 10;	//Assign a random y-Level height to the bed (10+)
				}
				if( mineral.type == TFCMineral.GenerationType.PIPE ){ //Special case for kimberlite pipes
					int gabbroTop = 0;
					int gabbroBottom = 0;
					for( int i = startY; i > 10; i-- ){ //Look for gabbro
						if( world.getBlockId( startX, i, startZ ) == TFCRock.gabbro.blockID && 
							world.getBlockMetadata( startX, i, startZ ) == TFCRock.gabbro.meta ){
							if( gabbroTop == 0 ){ //If we haven't set the gabbro top yet, set it now
								gabbroTop = i;
							}
							else{
								gabbroBottom = i;
							}
						}
					}
					if( gabbroTop != 0 ){ //If we found gabbro
						//This part is a little strange. Because we cannot do random.nextInt( 0 ), we must ensure it is at least 1;
						//randomHeight refers more to the range that we supply to random.nextInt (it's a poor choice of a variable name)
						//Further note: this was supposed to randomly make the kimberlite pipe spawn at a random height within the gabbro layer
						//But since it wasn't always generating properly, I scrapped this randomness
//						int randomHeight = 1;
//						if( gabbroTop - gabbroBottom > 0 ){
//							randomHeight = gabbroTop - gabbroBottom;
//						}
//						
//						startY = gabbroBottom + ( random.nextInt( randomHeight ) / 2 );
						//This is what replaced it
						startY = gabbroBottom;
					}
				}
				if( mineral.canOccurIn( world.getBlockId( startX, startY, startZ ), world.getBlockMetadata( startX, startY, startZ ) ) ){
					int randomLength = 48 + random.nextInt( 208 ); //48-255
					int randomDensity = ( 5 + random.nextInt( 6 ) ) * 10; // 50-100
					if( mineral.type == TFCMineral.GenerationType.VEIN ){
						Vein vein = new Vein( startX, startY, startZ, 1, randomDensity, mineral, randomLength ); //x, y, z, initial diameter, density

						while( !vein.isFinishedGrowing() ){
							vein.iterateAllBranches( world, random );
						}
//						ChatMessageComponent veinEnd = new ChatMessageComponent();
//						veinEnd.addText( mineral.getName() + " vein: " + startX + ", " + startY + ", " + startZ + " (" + vein.mainBranch.length + "m)" );
//						MinecraftServer.getServer().getConfigurationManager().sendChatMsg( veinEnd );
					}
					else if( mineral.type == TFCMineral.GenerationType.BED ){
						if( mineral == TFCMineral.anthracite && Math.abs( startZ ) < 19000 ){
							; //Do nothing
						}
						else{
							MineralBed bed = new MineralBed( startX, startY, startZ, random.nextInt( 25 ) + 15, random.nextInt( 2 ) + 1, mineral, randomLength / 2, ( random.nextInt( 2 ) + 1 ) * 10, ( random.nextInt( 3 ) + 1 ) );
	
							while( !bed.isFinishedGrowing() ){
								bed.iterateBed( world, random );
							}
//							ChatMessageComponent bedEnd = new ChatMessageComponent();
//							bedEnd.addText( mineral.getName() + " bed: " + startX + ", " + startY + ", " + startZ + " (" + bed.distanceGenerated + "m, w: " + bed.width + ", h: " + bed.height + ", #: " + bed.numberParallelBeds + ")" );
//							MinecraftServer.getServer().getConfigurationManager().sendChatMsg( bedEnd );
						}
					}
					else if( mineral.type == TFCMineral.GenerationType.PIPE ){
						KimberlitePipe pipe = new KimberlitePipe( startX, startY, startZ );
						
						pipe.generate( world, random );
						
//						ChatMessageComponent pipeEnd = new ChatMessageComponent();
//						pipeEnd.addText( mineral.getName() + " pipe: " + startX + ", " + startY + ", " + startZ );
//						MinecraftServer.getServer().getConfigurationManager().sendChatMsg( pipeEnd );
					}
					return true;
				}
			}
		}
		return false;
	}
	static boolean isRock( int i ){
        if( i == TFCBlocks.StoneIgIn.blockID || 
        	i == TFCBlocks.StoneIgEx.blockID || 
            i == TFCBlocks.StoneMM.blockID || 
            i == TFCBlocks.StoneSed.blockID ){
            return true;
        }
        return false;
    }
	public static int getGroupIndex( long seed, int chunkX, int chunkZ ){
		Random random = new Random( seed );
		
		int regionLength = 96; //height and width in chunks
		int groupQty = 16; //The number of unique ore groupings
		
		int xOffset = random.nextInt( regionLength );
		int zOffset = random.nextInt( regionLength );
		int oreOrder = random.nextInt( regionLength );
		
		if( TFCOreModWorldGenOre.groupIndexes == null || TFCOreModWorldGenOre.seed != seed ){ //Trying to only get this to run once per world
			TFCOreModWorldGenOre.generateNewGroupIndexes( random, groupQty );
			TFCOreModWorldGenOre.populateMineralGroups( random, groupQty );
			TFCOreModWorldGenOre.seed = seed;
		}
		//Do not generate any more random numbers using the Random object constructed in this method from this point on
		//Since the TFCOreModWorldGenOre.oreIndexes uses it only the first time this method is called, then further use will have inconsistent random numbers

		//Region refers to ore region, not anvil format region
		//Take the chunk index, add the offset, then divide by the regionLength
		int regionX = ( chunkX + xOffset ) / regionLength;
		int regionZ = ( chunkZ + zOffset ) / regionLength;
		
		if( regionZ % 2 == 0 ){
			return TFCOreModWorldGenOre.groupIndexes.get( ( oreOrder + Math.abs( regionX ) + Math.abs( regionZ ) ) % groupQty );
		}
		return TFCOreModWorldGenOre.groupIndexes.get( ( oreOrder + oreOrder + Math.abs( regionX ) + Math.abs( regionZ ) ) % groupQty );
	}
	public static void generateNewGroupIndexes( Random random, int groupQty ){
		//Called when we need a new static groupIndexes arraylist
		
		TFCOreModWorldGenOre.groupIndexes = new ArrayList<Integer>();
		while( TFCOreModWorldGenOre.groupIndexes.size() < groupQty ){ //randomly orders numbers 0-15 (or groupQty) into an array (should be same order every time)
			int i = random.nextInt( groupQty );
			boolean alreadyIn = false;
			for( Integer j : TFCOreModWorldGenOre.groupIndexes ){
				if( i == j.intValue() ){
					alreadyIn = true;
				}
			}
			if( !alreadyIn ){
				TFCOreModWorldGenOre.groupIndexes.add( new Integer( i ) );
			}
		}
	}
	public static void populateMineralGroups( Random random, int groupQty ){
		//Just like generateNewGroupIndexes method, called when we need to set the randomized group information
		
		TFCOreModWorldGenOre.mineralGroups = new ArrayList<RegionMineralGroup>();
		for( int i = 0; i < groupQty; i++ ){ //Initialize groupQty groups (probably 16)
			TFCOreModWorldGenOre.mineralGroups.add( new RegionMineralGroup() );
		}
		//native copper, malachite and tetrahedrite; make 3 random groups, then randomly assign 1/5 chance
		int addedCopper = 0;
		while( addedCopper < 3 ){	//first assign coppers to 3 random groups
			int i = random.nextInt( groupQty );
			if( !TFCOreModWorldGenOre.mineralGroups.get( i ).hasMineral( TFCMineral.nativeCopper ) ){ //If does not have nativeCopper (it won't have the other copper ores either)
				TFCOreModWorldGenOre.mineralGroups.get( i ).addMineralRarity( new TFCMineralRarity( TFCMineral.nativeCopper, random.nextInt( 70 ) + 70 ) );
				TFCOreModWorldGenOre.mineralGroups.get( i ).addMineralRarity( new TFCMineralRarity( TFCMineral.malachite, random.nextInt( 70 ) + 110 ) );
				TFCOreModWorldGenOre.mineralGroups.get( i ).addMineralRarity( new TFCMineralRarity( TFCMineral.tetrahedrite, random.nextInt( 70 ) + 90 ) );
				addedCopper++;
			}
		}
		for( int i = 0; i < TFCOreModWorldGenOre.mineralGroups.size(); i++ ){ //Randomly assign copper with 1/5 chance
			if( random.nextInt( 5 ) == 0 && !TFCOreModWorldGenOre.mineralGroups.get( i ).hasMineral( TFCMineral.nativeCopper ) ){
				TFCOreModWorldGenOre.mineralGroups.get( i ).addMineralRarity( new TFCMineralRarity( TFCMineral.nativeCopper, random.nextInt( 70 ) + 70 ) );
				TFCOreModWorldGenOre.mineralGroups.get( i ).addMineralRarity( new TFCMineralRarity( TFCMineral.malachite, random.nextInt( 70 ) + 110 ) );
				TFCOreModWorldGenOre.mineralGroups.get( i ).addMineralRarity( new TFCMineralRarity( TFCMineral.tetrahedrite, random.nextInt( 70 ) + 90 ) );
			}
		}
		//random assignments to a single group
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.nativeGold, random.nextInt( 70 ) + 100 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.nativePlatinum, random.nextInt( 70 ) + 180 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.hematite, random.nextInt( 70 ) + 70 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.nativeSilver, random.nextInt( 70 ) + 80 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.cassiterite, random.nextInt( 70 ) + 70 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.galena, random.nextInt( 70 ) + 90 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.bismuthinite, random.nextInt( 70 ) + 90 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.magnetite, random.nextInt( 70 ) + 150 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.limonite, random.nextInt( 70 ) + 150 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.sphalerite, random.nextInt( 70 ) + 110 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.bituminousCoal, random.nextInt( 70 ) + 90 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.lignite, random.nextInt( 70 ) + 90 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.kaolinite, random.nextInt( 70 ) + 90 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.gypsum, random.nextInt( 70 ) + 90 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.satinspar, random.nextInt( 70 ) + 110 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.selenite, random.nextInt( 70 ) + 110 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.graphite, random.nextInt( 70 ) + 70 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.petrifiedWood, random.nextInt( 70 ) + 70 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.sulfur, random.nextInt( 70 ) + 110 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.microcline, random.nextInt( 70 ) + 90 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.pitchblende, random.nextInt( 70 ) + 90 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.cinnabar, random.nextInt( 70 ) + 70 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.saltpeter, random.nextInt( 70 ) + 110 ) );
		TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.serpentine, random.nextInt( 70 ) + 130 ) );
		
		
		for( int i = 0; i < 12; i++ ){ //Add garnierite with random rarity to first 12 groups
			TFCOreModWorldGenOre.mineralGroups.get( i ).addMineralRarity( new TFCMineralRarity( TFCMineral.garnierite, random.nextInt( 70 ) + 140 ) );
			//Randomizing these minerals that don't tend to appear in very many rock types.
			if( i % 3 == 0 ){
				TFCOreModWorldGenOre.mineralGroups.get( i ).addMineralRarity( new TFCMineralRarity( TFCMineral.olivine, random.nextInt( 70 ) + 90 ) );
			}
			else if( i % 2 == 0 ){
				TFCOreModWorldGenOre.mineralGroups.get( i ).addMineralRarity( new TFCMineralRarity( TFCMineral.borax, random.nextInt( 70 ) + 90 ) );
			}
			else{
				TFCOreModWorldGenOre.mineralGroups.get( i ).addMineralRarity( new TFCMineralRarity( TFCMineral.lapisLazuli, random.nextInt( 70 ) + 130 ) );
			}
			if( i % 2 == 0 && i < 8 ){
				TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.cryolite, random.nextInt( 70 ) + 90 ) );
			}
			else if( i % 2 == 1 && i > 4 ){

				TFCOreModWorldGenOre.mineralGroups.get( random.nextInt( TFCOreModWorldGenOre.mineralGroups.size() ) ).addMineralRarity( new TFCMineralRarity( TFCMineral.sylvite, random.nextInt( 70 ) + 110 ) );
			}
		}
		for( int i = 8; i < TFCOreModWorldGenOre.mineralGroups.size(); i++ ){ //Add kimberlite to groups 8+
			TFCOreModWorldGenOre.mineralGroups.get( i ).addMineralRarity( new TFCMineralRarity( TFCMineral.kimberlite, random.nextInt( 70 ) + 180 ) );
		}
		for( int i = 6; i < 10; i++ ){ //Add anthracite
			TFCOreModWorldGenOre.mineralGroups.get( i ).addMineralRarity( new TFCMineralRarity( TFCMineral.anthracite, random.nextInt( 70 ) + 80 ) );
		}
		//Further manipulation of the richest category to make it even richer
		int richestGroup = -1;
		for( int i = 0; i < TFCOreModWorldGenOre.mineralGroups.size(); i++ ){
			if( TFCOreModWorldGenOre.mineralGroups.get( i ).mineralList.size() > richestGroup ){
				richestGroup = i;
			}
		}
		int mineralCount = TFCOreModWorldGenOre.mineralGroups.get( richestGroup ).mineralList.size(); //Since the following for loop adds minerals to the count of the mineralList, it's necessary to get the count first so that the for loop doesn't iterate on newly added minerals
		for( int i = 0; i < mineralCount; i++ ){
			int currentRarity = TFCOreModWorldGenOre.mineralGroups.get( richestGroup ).mineralList.get( i ).getRarity();
			if( currentRarity > 20 ){
				TFCOreModWorldGenOre.mineralGroups.get( richestGroup ).mineralList.get( i ).setRarity( currentRarity - 20 );
			}
		}
	}
	public static void printMineralGroups(){ //For debugging the mineral groups
		System.out.println( "" );
		for( int i = 0; i < TFCOreModWorldGenOre.mineralGroups.size(); i++ ){
			System.out.print( "Group #" );
			if( i < 10 ){
				System.out.print( " " );
			}
			System.out.print( i + ": " );
			for( int j = 0; j < TFCOreModWorldGenOre.mineralGroups.get( i ).mineralList.size(); j++ ){
				int rarity = TFCOreModWorldGenOre.mineralGroups.get( i ).mineralList.get( j ).getRarity();
				TFCMineral mineral = TFCOreModWorldGenOre.mineralGroups.get( i ).mineralList.get( j ).getMineral();
				System.out.print( "ID" + mineral.blockID + "(" + mineral.meta + "," + rarity + ") " );
			}
		}
	}
}
