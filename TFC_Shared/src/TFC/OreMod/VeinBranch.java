package TFC.OreMod;

import java.util.Random;

import TFC.TFCBlocks;

import net.minecraft.world.World;

public class VeinBranch {
	public int vectorX;
	public int vectorY; //cannot be positive
	public int vectorZ;
	public int length = 0;
	public int maxLength;
	public int radius; //Must be 0-3
	public int nextRadiusChange = 5; //Initial radius
	public int density; //1-100, density/100 chance that each of the surrounding blocks (radius) will generate
	public int x;
	public int y;
	public int z;
	public boolean canBranch = false;
	public int ticksInWrongRock = 0; //The number of growTicks since the last time it grew (in case trying to grow in a rock that does not permit this kind of ore)
	public static final int maxTicksInWrongRock = 5; //The maximum number of impossibleRockTicks before the vein finishes growing
	public int ticksInNonRock = 0;
	public static final int maxTicksInNonRock = 12;
	public int nextVectorChange = 70;	//The number vectorChangeCount must surpass before a vector change occurs
	public int vectorChangeCount = 0;
	public static final int vectorChangeIncrement = 18;

	public VeinBranch( int vectorX, int vectorY, int vectorZ, int maxLength, int radius, int density, int x, int y, int z ){
		this.vectorX = vectorX;
		this.vectorY = vectorY;
		this.vectorZ = vectorZ;
		this.maxLength = maxLength;
		this.radius = radius;
		this.density = density;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public boolean isFinishedGrowing(){
		if( this.length >= this.maxLength || this.y <= 8 || this.ticksInWrongRock >= VeinBranch.maxTicksInWrongRock || this.ticksInNonRock >= VeinBranch.maxTicksInNonRock ){
			return true;
		}
		return false;
	}
	public void growTick( World world, Random random, TFCMineral mineral ){
		
		//check for radius change
		if( this.nextRadiusChange == 0 ){

			this.nextRadiusChange = random.nextInt( 7 ) + 2;			//reset nextRadiusChange
			
			if( this.radius == 0 ){
				this.radius = 1;
			}
			else if( this.radius == 1 ){
				int r = random.nextInt( 4 );
				if( r == 0 ){
					this.radius = 0;
				}
				else if( r == 1 ){
					this.radius = 2;
				}
			}
			else if( this.radius == 2 ){
				int r = random.nextInt( 4 );
				if( r == 0 ){
					this.radius = 3;
				}
				else if( r == 1 ){
					this.radius = 1;
				}
			}
			else{
				this.radius = 2;
			}
		}
		else if( this.radius == 0 && random.nextInt( 100 ) > 60 ){ //Added this else if statement afterwards to reduce the amount of iterations with radius 0
			this.nextRadiusChange = random.nextInt( 7 ) + 2;
			this.radius = 1;
		}
		if( this.radius > 0 ){	//Radius can be zero sometimes, which will cause gaps in the contiguousness of the vein
			if( !( this.vectorX == 0 && this.vectorY == 0 && this.vectorZ == 0 ) ){	//If this vector is not zero (should never happen)
				for( int i = -this.radius + 1; i < this.radius; i++ ){
					for( int j = -this.radius + 1; j < this.radius; j++ ){
						for( int k = -this.radius + 1; k < this.radius; k++ ){
							if( Math.abs( i ) + Math.abs( j ) + Math.abs( k ) < this.radius ){	//Prevents blocks from generating too far away from center block (according to radius)
								int targetBlockID = world.getBlockId( this.x + i, this.y + j, this.z + k );
								int targetMeta = world.getBlockMetadata( this.x + i, this.y + j, this.z + k );
								
								if( isRockOrMineral( targetBlockID ) ){
									this.ticksInNonRock = 0;
									//if( mineral.canOccurIn( targetBlockID, targetMeta ) ){
									//removed the above if and the else if below, so that minerals can flow into non-native rock types
										this.ticksInWrongRock = 0;
										int densityCheck = random.nextInt( 100 );
										if( densityCheck <= this.density ){
											world.setBlock( this.x + i, this.y + j, this.z + k, mineral.blockID, mineral.meta, 0x2 );
											//System.out.println( "setblock: " + ( this.x + i ) + ", " + ( this.y + j ) + ", " + ( this.z + k ) );
										}
									//}
//									else if( i == 0 && j == 0 && k == 0 ){
//										this.ticksInWrongRock++;
//									}
								}
								else if( i == 0 && j == 0 && k == 0 ){
									this.ticksInNonRock++;
								}
							}
						}
					}
				}
			}
		}
		//check for vector change
		if( this.vectorChangeCount >= this.nextVectorChange ){
			
			//reset vectorChange variables
			this.vectorChangeCount = 0;
			this.nextVectorChange = random.nextInt( 100 - VeinBranch.vectorChangeIncrement ) + VeinBranch.vectorChangeIncrement;
			
			int xWeight = random.nextInt( 3 ) + 1;
			int zWeight = random.nextInt( 3 ) + 1;
			int yWeight;
			if( this.vectorY == 1 ){ yWeight = random.nextInt( 2 ) + 3; }
			else{ yWeight = random.nextInt( 3 ) + 1; }
			
			int totalWeight = xWeight + zWeight + yWeight;
			
			int planeChange = random.nextInt( totalWeight );
			if( planeChange < xWeight ){
				switch( this.vectorX ){
					case( 1 ): this.vectorX = 0; break;
					case( -1 ): this.vectorX = 0; break;
					case( 0 ): if( random.nextInt( 2 ) == 0 ){ this.vectorX = 1; }else{ this.vectorX = 0; } break;
				}
			}
			if( planeChange < xWeight + zWeight ){
				switch( this.vectorZ ){
					case( 1 ): this.vectorZ = 0; break;
					case( -1 ): this.vectorZ = 0; break;
					case( 0 ): if( random.nextInt( 2 ) == 0 ){ this.vectorZ = 1; }else{ this.vectorZ = 0; } break;
				}
			}
			else{ //y vector changed
				switch( this.vectorY ){
					case( 1 ): this.vectorY = 0; break;
					case( -1 ): this.vectorY = 0; break;
					case( 0 ): 	if( random.nextInt( 10 ) < 7 ){ this.vectorY = -1; }
								else{
									this.vectorY = 1;
								}
								break;
				}
			}
			//make sure the vector isn't 0
			if( this.vectorX == 0 && this.vectorY == 0 && this.vectorZ == 0 ){
				switch( ( this.x + this.y + this.z ) % 4 ){	//If all vectors are 0, change them to some arbitrary downward direction
					case( 0 ): this.vectorX = 1; this.vectorY = -1; this.vectorZ = 1; break;
					case( 1 ): this.vectorX = -1; this.vectorY = -1; this.vectorZ = -1; break;
					case( 2 ): this.vectorX = 1; this.vectorY = -1; this.vectorZ = -1; break;
					case( 3 ): this.vectorX = 1; this.vectorY = -1; this.vectorZ = -1; break;
				}
			}
		}
		//move position along vector
		this.x += this.vectorX;
		this.y += this.vectorY;
		this.z += this.vectorZ;
		this.length++;
		this.vectorChangeCount += VeinBranch.vectorChangeIncrement;
		this.nextRadiusChange--;
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