package TFC.OreMod;

import java.util.Random;

import TFC.TFCBlocks;

import net.minecraft.world.World;

public class MineralBed {
	
	public int x, y, z;
	public int width, height, length;
	public int distanceGenerated = 0;
	TFCMineral mineral;
	public int vectorX, vectorY;
	public int ticksInWrongRock = 0;
	public int ticksInNonRock = 0;
	public static int maxTicksInWrongRock = 5;
	public static int maxTicksInNonRock = 12;
	public int yChangeChance;
	public int numberParallelBeds = 1;

	public MineralBed( int x, int y, int z, int width, int height, TFCMineral mineral, int length, int yChangeChance, int numberParallelBeds ){
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.height = height;
		this.mineral = mineral;
		this.length = length;
		this.yChangeChance = yChangeChance;
		Random random = new Random();
		this.vectorX = random.nextInt( 3 ) - 1; //-1 to 1
		this.vectorY = random.nextInt( 3 ) - 1; //-1 to 1
		this.numberParallelBeds = numberParallelBeds;
	}
	
	public boolean isFinishedGrowing(){
		if( this.distanceGenerated >= this.length ||
			this.ticksInWrongRock >= MineralBed.maxTicksInWrongRock ||
			this.ticksInNonRock >= MineralBed.maxTicksInNonRock ||
			this.y <= 8 ){
			
			return true;
		}
		return false;
	}
	
	public void iterateBed( World world, Random random ){
		boolean didGenerate = false;
		boolean didFindOneRock = false;
		for( int i = 0; i < width; i++ ){
			for( int j = 0; j < height; j++ ){
				for( int bedLayer = 0; bedLayer < this.numberParallelBeds; bedLayer++ ){
					int blockID = world.getBlockId( x + i, y - j - ( bedLayer * ( 4 + this.height ) ), z );
					int blockMeta = world.getBlockMetadata( x + i, y - j - ( bedLayer * ( 4 + this.height ) ), z );
					
					if( isRockOrMineral( blockID ) ){
						didFindOneRock = true;
						if( this.mineral.canOccurIn( blockID, blockMeta ) ){
							if( this.mineral.getName().equals( "petrified wood" ) && random.nextInt( 100 ) >= 10 ){ //Limit how much petrified wood can be found
								; //Do nothing
							}
							else{
								world.setBlock( this.x + i, this.y - j - ( bedLayer * ( 4 + this.height ) ), this.z, mineral.blockID, mineral.meta, 0x2 );
							}
							didGenerate = true;
						}
					}
				}
			}
		}
		if( !didFindOneRock ){
			this.ticksInNonRock++;
		}
		if( didFindOneRock && !didGenerate ){
			this.ticksInWrongRock++;
		}
		if( random.nextInt( 100 ) < this.yChangeChance ){	//Random chance to raise or lower in y level
			switch( random.nextInt( 2 ) ){
				case 0: this.y--; break;
				case 1: this.y++; break;
			}
		}
		//Vector iteration
		this.x += this.vectorX;
		this.y += this.vectorY;
		this.z++;
		
		//Check for vector change
		if( random.nextInt( 100 ) < 25 ){ 
			if( random.nextInt( 2 ) == 0 ){//vectorX change
				switch( this.vectorX ){
					case -1: this.vectorX = 0;
					case 0: if( random.nextInt( 2 ) == 0 ){ this.vectorX = -1; } else{ this.vectorX = 1; } break;
					case 1: this.vectorX = 0;
				}
			}
			else{ //vectorZ change
				switch( this.vectorY ){
					case -1: this.vectorY = 0;
					case 0: if( random.nextInt( 2 ) == 0 ){ this.vectorY = -1; } else{ this.vectorY = 1; } break;
					case 1: this.vectorY = 0;
				}
			}
		}
		this.distanceGenerated++;
	}
	private boolean isRockOrMineral( int i ){
        if( i == TFCBlocks.StoneIgIn.blockID || i == TFCBlocks.StoneIgEx.blockID || 
            i == TFCBlocks.StoneMM.blockID || i == TFCBlocks.StoneSed.blockID ||
            i == TFCBlocks.Ore.blockID || i == TFCBlocks.Ore2.blockID || i == TFCBlocks.Ore3.blockID ){
            return true;
        }
        return false;
    }
}
