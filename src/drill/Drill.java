package drill;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.alta189.sqlLibrary.SQLite.*;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class Drill  extends JavaPlugin {
	
	//DB Directory Stuff
	public static String mainDirectory = "plugins" + File.separator + "Drill"; 
	public File dir = new File(mainDirectory);
	public static String prefix = "[Drill]";
	public static String dbName = "drillDB";
	static Logger log = Logger.getLogger("Minecraft");//Define your logger
	private static sqlCore dbManage =  new sqlCore(log, prefix, dbName, mainDirectory );
		
	//Create Drills table Query
	public String DBTableQuery ="CREATE TABLE Drills" +
			"(" +
			"B_Id INTEGER PRIMARY KEY, " +
			"BlockX int NOT NULL, " +
			"BlockY int NOT NULL, " +
			"BlockZ int NOT NULL, " +
			"Owner varchar(255) , " +
			"Direction int DEFAULT '0'," +
			"Type int DEFAULT '0'" +
			")" + ";"; 

	//Config File
	public String enabledstartup = "Enabled On Startup";
	public String TypeOneName;
	public String TypeTwoName;
	public int TypeOneId;
	public int TypeTwoId;
	public int FuelId;
	public int TypeOneFuelxBlock;
	public int TypeTwoFuelxBlock;
	public int MaxDistance;
	Config config = new Config(this);
	
    //Listeners
    private final DrillPlayerListener playerListener = new DrillPlayerListener(this);
    private final DrillBlockListener blockListener = new DrillBlockListener(this);
   
    //Permission handler
    public static PermissionHandler permissionHandler;
    //WG
    public static WorldGuardPlugin WGplugin;
    //Plugins
    public static PluginManager pm;
  
    //utils
    public static sqlCore getManager(){
    	return dbManage;
    }
    
    public void createPluginFolder(){
    	if(!this.dir.exists()){
    		log.info("Creating Drill Plugin Folder");
    		dir.mkdir();
    	}
    }
    

    public void setupPermissions() {
        Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

        if (this.permissionHandler == null) {
            if (permissionsPlugin != null) {
                this.permissionHandler = ((Permissions) permissionsPlugin).getHandler();
            } else {
                log.info("Permission system not detected, defaulting to OP");
            }
        }
    }
    
    //WG     
    public void setupWorldGuard() {
        Plugin wgplugin = getServer().getPluginManager().getPlugin("WorldGuard");
     
        // WorldGuard may not be loaded
        if (wgplugin == null || !(wgplugin instanceof WorldGuardPlugin)) {
            return ; // Maybe you want throw an exception instead
        }
     
        WGplugin = (WorldGuardPlugin) wgplugin;
    }
    
    
    public void onDisable() {
    	if(dbManage != null){
    		dbManage.close();
    	}
    	log.info("Drill Plugin DISABLED");
    }

    public void onEnable() {     
    pm = this.getServer().getPluginManager();
    log.info("Drill Plugin ENABLED");
    createPluginFolder();
    config.configCheck();
    dbManage.initialize();
    if(!dbManage.checkTable("Drills")){
    	 dbManage.createTable(DBTableQuery);
    	 //dbManage.close();
    	 //dbManage.initialize();
    	 //Create first row (record) on the table
    	 DrillToDB drillobj = new DrillToDB(0,0,0);
    	 drillobj.toRecord(drillobj);
    	 //dbManage.close();
    }
    //Permissions loading
    setupPermissions();
    //WGloading
    setupWorldGuard();
    //Plugin Commands
	
	//Event listener
    pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
    pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
    pm.registerEvent(Event.Type.REDSTONE_CHANGE, blockListener, Event.Priority.Normal, this);
    }

}
