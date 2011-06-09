package drill;

import java.io.File;
import java.util.List;

import org.bukkit.util.config.Configuration;

public class Config {
    private static Drill plugin;
    public Config(Drill instance) {
        plugin = instance;
    }

    public String directory = "plugins" + File.separator + "Drill";
    File file = new File(directory + File.separator + "config.yml");

    public String TypeOneName = "Iron";
    public String TypeTwoName = "Diamond";
    public int TypeOneId =42; 		//IronBlock
    public int TypeTwoId=57;  		//DiamondBlock
    public int FuelId = 353;  		
    public int TypeOneFuelxBlock = 2; //FuelConsumed By TypeOneDrill
    public int TypeTwoFuelxBlock = 1; //FuelConsumed By TypeTwoDrill
    public int MaxDistance = 65;
    
    public void configCheck(){
        new File(directory).mkdir();


        if(!file.exists()){
            try {
                file.createNewFile();
                addDefaults();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {

            loadkeys();
        }
    }
    private void write(String root, Object x){
        Configuration config = load();
        config.setProperty(root, x);
        config.save();
    }
    private Boolean readBoolean(String root){
        Configuration config = load();
        return config.getBoolean(root, true);
    }

    private int readInt(String root){
        Configuration config = load();
        return config.getInt(root, 0);
    }
    private Double readDouble(String root){
        Configuration config = load();
        return config.getDouble(root, 0);
    }
    private List<String> readStringList(String root){
        Configuration config = load();
        return config.getKeys(root);
    }
    private String readString(String root){
        Configuration config = load();
        return config.getString(root);
    }
    private Configuration load(){

        try {
            Configuration config = new Configuration(file);
            config.load();
            return config;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void addDefaults(){
        plugin.log.info("Generating Config File...");
        write("Drill.Names.TypeOne", TypeOneName);
        write("Drill.Names.TypeTwo", TypeTwoName);
        write("Drill.ID.TypeOneId", TypeOneId);
        write("Drill.ID.TypeTwoId", TypeTwoId);
        write("Drill.Fuel.Id", FuelId);
        write("Drill.Fuel.TypeOneFuelxBlock", TypeOneFuelxBlock);
        write("Drill.Fuel.TypeTwoFuelxBlock", TypeTwoFuelxBlock);
        write("Drill.Distance.MaxDistance", MaxDistance);
     loadkeys();
    }
    private void loadkeys(){
        plugin.log.info("Loading Config File...");
        plugin.TypeOneName = readString("Drill.Names.TypeOne");
        plugin.TypeTwoName = readString("Drill.Names.TypeTwo");
        plugin.TypeOneId = readInt("Drill.ID.TypeOneId");
        plugin.TypeTwoId = readInt("Drill.ID.TypeTwoId");
        plugin.FuelId = readInt("Drill.Fuel.Id");
        plugin.TypeOneFuelxBlock = readInt("Drill.Fuel.TypeOneFuelxBlock");
        plugin.TypeTwoFuelxBlock = readInt("Drill.Fuel.TypeTwoFuelxBlock");
        plugin.MaxDistance = readInt("Drill.Distance.MaxDistance");      
    }
}
