package drill;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.block.Action;



public class DrillPlayerListener extends PlayerListener{
	
	
    public static Drill plugin;
    
    
    public DrillPlayerListener(Drill instance) {
            plugin = instance;
    }
    
    //Drill Blocks Stuff
	public int base = 61; //Furnace block id
	/*public int topiron = plugin.TypeOneId;  //Iron id
	public int topdiamond = plugin.TypeTwoId;  //Diamond block id*/
	
    
    Logger log = Logger.getLogger("Minecraft");//Define your logger
    
    public boolean isOwner(PlayerInteractEvent event){
    	return true;
    }
    
    public boolean isIron(Block clicked){
    	World world = clicked.getWorld();
		Block tblock = world.getBlockAt(clicked.getX(), clicked.getY()+1, clicked.getZ());
		if(tblock.getTypeId() == plugin.TypeOneId){
			return true;
		}
    	return false;
    }
    
    public boolean isDiamond(Block clicked){
    	World world = clicked.getWorld();
		Block tblock = world.getBlockAt(clicked.getX(), clicked.getY()+1, clicked.getZ());
		if(tblock.getTypeId() == plugin.TypeTwoId){
			return true;
		}
    	return false;
    }
    public int isFurnaceBase(Block clicked){
    	World world = clicked.getWorld();
		Block tblock = world.getBlockAt(clicked.getX(), clicked.getY()-1, clicked.getZ());
		Block bblock = world.getBlockAt(clicked.getX(), clicked.getY()+1, clicked.getZ());
		Block Wblock = world.getBlockAt(clicked.getX()+1, clicked.getY(), clicked.getZ());
		Block Eblock = world.getBlockAt(clicked.getX()-1, clicked.getY(), clicked.getZ());
		Block Nblock = world.getBlockAt(clicked.getX(), clicked.getY(), clicked.getZ()-1);
		Block Sblock = world.getBlockAt(clicked.getX(), clicked.getY(), clicked.getZ()+1);
		if(tblock.getTypeId() == base){
			return 1;
		}
		if(bblock.getTypeId() == base){
			return 2;
		}
		if(Eblock.getTypeId() == base){
			return 3;
		}
		if(Wblock.getTypeId() == base){
			return 4;
		}
		if(Nblock.getTypeId() == base){
			return 5;
		}
		if(Sblock.getTypeId() == base){
			return 6;
		}
    	return 0;
    }
    
    
    public Block getFurnace(Block clicked , int direction){
    	World world = clicked.getWorld();
    	if(direction == 1){
    		Block tblock = world.getBlockAt(clicked.getX(), clicked.getY()-1, clicked.getZ());
    		return tblock;
    	}
    	if(direction == 2){
    		Block bblock = world.getBlockAt(clicked.getX(), clicked.getY()+1, clicked.getZ());
    		return bblock;
    	}
    	if(direction == 3){
    		Block Eblock = world.getBlockAt(clicked.getX()-1, clicked.getY(), clicked.getZ());
    		return Eblock;
    	}
    	if(direction == 4){
    		Block Wblock = world.getBlockAt(clicked.getX()+1, clicked.getY(), clicked.getZ());
    		return Wblock;
    	}
    	if(direction == 5){
    		Block Nblock = world.getBlockAt(clicked.getX(), clicked.getY(), clicked.getZ()-1);
    		return Nblock;
    	}
    	if(direction == 6){
    		Block Sblock = world.getBlockAt(clicked.getX(), clicked.getY(), clicked.getZ()+1);
    		return Sblock;
    	}
    	return clicked;
    }
 
    
    public void onPlayerInteract(PlayerInteractEvent event){
    	Action act = event.getAction();
    	if(act.toString() == "LEFT_CLICK_BLOCK"){
        	Block clicked = event.getClickedBlock();
        	if(clicked.getTypeId()== plugin.TypeOneId){
        		int direction = 0;
        		direction = isFurnaceBase(clicked);
        		if (direction != 0 ){
        			Block furnaceBlock = getFurnace(clicked, direction);
        			DrillToDB check = new DrillToDB();
        			if(check.existsXYZ(furnaceBlock.getX(), furnaceBlock.getY(), furnaceBlock.getZ()) == false){
		        		//permission check
	        			if (Drill.permissionHandler.has(event.getPlayer(), "Drill.Iron") == true){	
	        				event.getPlayer().sendMessage(ChatColor.GRAY + plugin.TypeOneName.toString() + " Drill is SetUp!");
	        				DrillToDB drillobj = new DrillToDB(furnaceBlock.getX(), furnaceBlock.getY(), furnaceBlock.getZ());
	        				drillobj.setObjType(1);
	        				drillobj.setObjOwner(event.getPlayer().getName());
	        				drillobj.setObjDirection(direction);
	        				drillobj.toRecord(drillobj);
	        				return;
	        			}
	        			else{
	        				event.getPlayer().sendMessage(ChatColor.RED + "You don't have permission to use this!");
	        				return;
		        		}
	        		}
        		}
        	}
        	if(clicked.getTypeId()== plugin.TypeTwoId){
        		int direction = 0;
        		direction = isFurnaceBase(clicked);
        		if (direction != 0 ){
        			Block furnaceBlock = getFurnace(clicked, direction);
        			DrillToDB check = new DrillToDB();
        			if(check.existsXYZ(furnaceBlock.getX(), furnaceBlock.getY(), furnaceBlock.getZ()) == false){
	        		//permission check
	        			if (Drill.permissionHandler.has(event.getPlayer(), "Drill.Diamond") == true){
	        				event.getPlayer().sendMessage(ChatColor.AQUA + plugin.TypeTwoName.toString() + " Drill is SetUp!");
	        				DrillToDB drillobj = new DrillToDB(furnaceBlock.getX(), furnaceBlock.getY(), furnaceBlock.getZ());
	        				drillobj.setObjType(2);
	        				drillobj.setObjOwner(event.getPlayer().getName());
	        				drillobj.setObjDirection(direction);
	        				drillobj.toRecord(drillobj);
	        				return;
	        			}
	        			else{
	        				event.getPlayer().sendMessage(ChatColor.RED + "You don't have permission to use this!");
	        				return;
		        		}
	        		}
        		}
        	}
    	return;
    	}
    }
 
}
    
