package TFC.OreMod;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.world.World;

public class Vein {
	public VeinBranch mainBranch;
	public ArrayList<VeinBranch> activeBranches = new ArrayList<VeinBranch>(); //Secondary branches, if any
	public static final int minLengthBetweenBranches = 5;
	public static final int chanceToSpawnNewBranch = 8; //0-100, chanceToSpawnNewBranch/100 chance
	public int lastBranchSpawned = 0;
	public TFCMineral primaryMineral;
	
	public Vein( int x, int y, int z, int radius, int density, TFCMineral primaryMineral, int mainBranchLength ){
		this.mainBranch = new VeinBranch( 0, -1, 0, mainBranchLength, radius, density, x, y, z ); //Initial vector is y -1 only (down) 
		this.mainBranch.canBranch = true;
		this.primaryMineral = primaryMineral;
	}
	public boolean isFinishedGrowing(){

		if( !this.mainBranch.isFinishedGrowing() ){
			return false;
		}
		for( VeinBranch branch : this.activeBranches ){
			if( !branch.isFinishedGrowing() ){
				return false;
			}
		}
		return true;
	}
	public void iterateAllBranches( World world, Random random ){
		this.lastBranchSpawned++;
		if( !this.mainBranch.isFinishedGrowing() ){
			this.mainBranch.growTick( world, random, this.primaryMineral );
		}
		for( VeinBranch branch : this.activeBranches ){
			if( !branch.isFinishedGrowing() ){
				branch.growTick( world, random, this.primaryMineral );
			}
			else{ //If the branch is finished growing, remove it
				//this.activeBranches.remove( branch );
				//The above code crashes, so instead just leave the branches in the arraylist
			}
		}
		if( !this.mainBranch.isFinishedGrowing() && lastBranchSpawned >= Vein.minLengthBetweenBranches ){
			if( random.nextInt( 100 ) < Vein.chanceToSpawnNewBranch ){
				this.newBranch();
				this.lastBranchSpawned = 0;
			}
		}
	}
	public void newBranch(){
		//Determine a new vector dissimilar to the mainBranch's current vector
		int newXVector = 0;
		int newYVector = 0;
		int newZVector = 0;
		switch( this.mainBranch.vectorX ){
			case( -1 ): newXVector = 1; break;
			case( 0 ): if( this.mainBranch.x % 2 == 0 ){ newXVector = 1; }else{ this.mainBranch.vectorX = -1; } break;
			case( 1 ): newXVector = -1; break;
		}
		switch( this.mainBranch.vectorY ){
			case( -1 ): newYVector = 1; break;
			case( 0 ): if( this.mainBranch.y % 2 == 0 ){ newYVector = 1; }else{ this.mainBranch.vectorY = -1; } break;
			case( 1 ): newYVector = -1; break;
		}
		switch( this.mainBranch.vectorZ ){
			case( -1 ): newZVector = 1; break;
			case( 0 ): if( this.mainBranch.z % 2 == 0 ){ newZVector = 1; }else{ this.mainBranch.vectorZ = -1; } break;
			case( 1 ): newZVector = -1; break;
		}
		int randomLength = 16 + ( ( this.mainBranch.x + this.mainBranch.y + this.mainBranch.z ) % 16 ); //(16-31) length
		int randomDensity = ( 5 + ( ( this.mainBranch.x + this.mainBranch.y + this.mainBranch.z ) % 6 ) ) * 10; //50-100 random density 
		VeinBranch newBranch = new VeinBranch( newXVector, newYVector, newZVector, randomLength, this.mainBranch.radius, randomDensity, this.mainBranch.x + newXVector, this.mainBranch.y + newYVector, this.mainBranch.z + newZVector );
		this.activeBranches.add( newBranch );
	}
}
