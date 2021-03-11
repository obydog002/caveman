package src.file;

public class CampaignSave
{
	// which campaign it belongs to
	// would be found in maps/campaign folder
	public byte[] marker;
	
	// what level they are on
	public int level;
	
	// name of who owns this campaign
	public String name;
	
	public CampaignSave(byte[] marker, int level, String name)
	{
		this.marker = marker;
		this.level = level;
		this.name = name;
	}
	
	public String toString()
	{
		String res = level + "\n" + name + "\n";
		for (int i = 0; i < marker.length; i++)
		{
			res += (char)marker[i];
		}
		
		return res;
	}
}