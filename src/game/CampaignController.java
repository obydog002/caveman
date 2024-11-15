package src.game;

import src.file.Level;
import src.file.FileManager;

public class CampaignController extends Control {
	private GameMain main;
    private Game game;
	private Campaign campaign;
    public CampaignController(GameMain main, Game game, Campaign campaign) {
		this.main = main;
        this.game = game;
		this.campaign = campaign;
    }

	private void loadLevelIntoGame()
	{
		Level level = FileManager.read_cv_level(FileManager.RES_PATH + "maps/campaign/" +
					  campaign.directory + "/", "lev" + campaign.currentLevel + ".CVL");
		game.load_level(level);
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