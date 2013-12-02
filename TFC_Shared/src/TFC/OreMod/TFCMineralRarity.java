package TFC.OreMod;

public class TFCMineralRarity {

	private TFCMineral mineral;
	private int rarity;
	
	public TFCMineralRarity( TFCMineral mineral, int rarity ){
		this.mineral = mineral;
		this.rarity = rarity;
	}
	public TFCMineral getMineral(){
		return this.mineral;
	}
	public int getRarity(){
		return this.rarity;
	}
	public void setRarity( int rarity ){
		this.rarity = rarity;
	}
}
