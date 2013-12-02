package TFC.OreMod;

import java.util.ArrayList;

public class RegionMineralGroup {

	public ArrayList<TFCMineralRarity> mineralList = new ArrayList<TFCMineralRarity>();
	
	public RegionMineralGroup(){
		
	}
	public void addMineralRarity( TFCMineralRarity mineralRarity ){
		this.mineralList.add( mineralRarity );
	}
	public boolean hasMineral( TFCMineral mineral ){
		for( TFCMineralRarity mR : mineralList ){
			if( mR.getMineral() == mineral ){
				return true;
			}
		}
		return false;
	}
}
