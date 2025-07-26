package src.game;

import java.io.File;

import src.file.FileManager;
import src.file.Level;

public class CampaignController extends Control {
	private CavemanMain main;
	private AbstractInput input;
    private Game game;
	private Campaign campaign;
    public CampaignController(CavemanMain main, AbstractInput input, Campaign campaign) {
		this.main = main;
		this.input = input;
		this.campaign = campaign;
    }

	private void loadLevelIntoGame()
	{
		File cavemanLevelFile = new File(
			FileManager.RES_PATH + "maps/campaign/" +
					  campaign.directory + "/", "lev" + campaign.currentLevel + ".caveman");
		Level level = FileManager.readCVLevel(cavemanLevelFile);
		
		game = new Game(input, main, level);
	}

    public void tick() {
		System.err.println("??");
		loadLevelIntoGame();
		game.setReady();
		campaign.currentLevel++;
		main.add_process(game);
    }

    // sets the campaign info to go
	/*private void set_campaign()
	{
		
		max_level = FileManager.get_campaign_max_level(FileManager.RES_PATH + "maps/campaign/", marker);
		
		// if its -1 it could not be read properly. 
		// so just throw a run time error to crash
		if (max_level == -1)
			throw new RuntimeException("Could not read campaign file!");
	}*/
}