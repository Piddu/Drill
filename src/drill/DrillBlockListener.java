package drill;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DrillBlockListener extends BlockListener{
	
    //Blocks IDs
	int base = 61;//id OBS
	int topiron = 42; //id IRON
	int topdiamond = 57; // id GOLD
	int bedrock = 7;
	int water = 8;
	int waterbis = 9;
	int lava = 10;
	int lavabis = 11;
	int dungeon = 52;
	int fire = 51;
	
	
	//Fuel
	int sugar = 353;
	
	//Drill distance
	double maxDistance = 65;
	
	
	//Logger
    Logger log = Logger.getLogger("Minecraft");
	
	public static Drill plugin;
        
    public DrillBlockListener(Drill instance) {
            plugin = instance;
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
    //Check whenever a drill is powered
    public void drillPowerOn(World world, int x, int y, int z){
    	Block block = world.getBlockAt(x,y,z);
    	if(block.getBlockPower() > 0){
    		return;
    	}
    	if(block.getTypeId() == base){
	    	//check if is a drill
	    	DrillToDB check = new DrillToDB();
	    	if(check.existsXYZ(x,y,z) == true){
	    		int id = check.getRecordId(x,y,z);
	    		DrillToDB drillobj = new DrillToDB(id);
	    		drillBlocks(world, block, drillobj);
	    	}
    	}
    }
    
    //Drill jobs...
    public void drillBlocks(World  world, Block block, DrillToDB drillobj){
    	Furnace furnace = (Furnace)block.getState();
    	Inventory furnaceInv = furnace.getInventory(); 
    	//Furnace Fuel Slot from 0 to 2
    	//Fuel slot is 1.
    	//Check fuel q. and consume
    	if(furnaceInv.getItem(1).getTypeId() == sugar){
	    	int fuelQ = furnaceInv.getItem(1).getAmount();
	    	int fuelconsumed = consumeFuel(drillobj.getObjType());
	    	int fuelremained = fuelQ - fuelconsumed;
	    	if(fuelremained <= 0){
	    		furnaceInv.remove(sugar);
	    		return;
	    	}	    	   	
	    	int offset = getOffset(world, drillobj.getObjDirection(), drillobj.getObjBlockX(), drillobj.getObjBlockY(), drillobj.getObjBlockZ());
	    	Block targetBlock = getTargetBlock(world, drillobj.getObjDirection(), drillobj.getObjBlockX(), drillobj.getObjBlockY(), drillobj.getObjBlockZ(), offset);
			int targetId = targetBlock.getTypeId();
			if(targetId== 0){
				return;
			}
			if(canBeDrilled(targetId) == true){
				ItemStack sugarStack = new ItemStack(sugar, fuelremained);
		    	furnaceInv.remove(sugar);
		    	furnaceInv.setItem(1,sugarStack);
				destroyDrilledBlock(targetBlock);
				dropDrilledBlock(world, targetId, block);
			}
    		else{
    			return;
    		}
    	}
    	else{
    		return;
    	}
    }
    
    public int getOffset(World world, int direction, int x, int y, int z){
    	int i = 0;
		if(direction == 1){
			for(i=1; i<=65; i++){
				Block next = world.getBlockAt(x,y+1+i,z);
				if(next.getTypeId()!= 0){
					return i;
				}	
			}
		}
    	if(direction == 2){
			for(i=1; i<=65; i++){
				Block next = world.getBlockAt(x,y-1-i,z);
				if(next.getTypeId()!= 0){
					return i;
				}
			}	
        }
    	if(direction == 3){
			for(i=1; i<=65; i++){
				Block next = world.getBlockAt(x+1+i,y,z);
				if(next.getTypeId()!= 0){
					return i;
				}
			}	
        }
    	if(direction == 4){			
    		for(i=1; i<=65; i++){
			Block next = world.getBlockAt(x-1-i,y,z);
			if(next.getTypeId()!= 0){
				return i;
			}
    			
    		}
    	}
    	if(direction == 5){
			for(i=1; i<=65; i++){
				Block next = world.getBlockAt(x,y,z+1+i);
				if(next.getTypeId()!= 0){
					return i;
				}
			}	
        }
    	if(direction == 6){
			for(i=1; i<=65; i++){
				Block next = world.getBlockAt(x,y,z-1-i);
				if(next.getTypeId()!= 0){
					return i;
				}
			}	
        }
    	return 1;
    }
    
    public Block getTargetBlock(World world, int direction,  int x, int y, int z, int offset){
    	if(direction == 1){
    		Block block = world.getBlockAt(x,y+1+offset,z);
    		return block;	
    	}
    	if(direction == 2){
    		Block block = world.getBlockAt(x,y-1-offset,z);
    		return block;	
        }
    	if(direction == 3){
    		Block block = world.getBlockAt(x+1+offset,y,z);
    		return block;	
        }
    	if(direction == 4){
    		Block block = world.getBlockAt(x-1-offset,y,z);
    		return block;	
        }
    	if(direction == 5){
    		Block block = world.getBlockAt(x,y,z+1+offset);
    		return block;	
        }
    	if(direction == 6){
    		Block block = world.getBlockAt(x,y,z-1-offset);
    		return block;	
        }
    	Block block = world.getBlockAt(x,y,z);
    	return block;
    }
    
    public boolean canBeDrilled(int id){
    	if(id!=bedrock && id != water && id != waterbis && id != lava && id != lavabis && id != dungeon && id != fire){
    		return true;
    	}
    	return false;
    }
    //Drop drilled blocks
    public void dropDrilledBlock(World world, int targetId, Block block){
    	Location loc = block.getLocation();
    	//Diamond Ore found
    	if(targetId == 56){
    		ItemStack drop = new ItemStack(264, 1);
        	world.dropItemNaturally(loc, drop);
        	return;
    	}
    	//Stone found
    	if(targetId == 1){
    		ItemStack drop = new ItemStack(4, 1);
        	world.dropItemNaturally(loc, drop);
        	return;
    	}
    	//Carbon Ore found
    	if(targetId == 16){
    		ItemStack drop = new ItemStack(263, 2);
    		world.dropItemNaturally(loc, drop);
    		return;
    	}
    	//Dirt
    	if(targetId == 2){
    		ItemStack drop = new ItemStack(3, 1);
    		world.dropItemNaturally(loc, drop);
    		return;
    	}
    	ItemStack drop = new ItemStack(targetId, 1);
    	world.dropItemNaturally(loc, drop);
    	return;
    }
    
    //Destroy selected block
    public void destroyDrilledBlock(Block block){
    	block.setTypeId(0);
    }
    
    //Return the fuel amount consumed by drill
    public int consumeFuel(int type){
    	if(type == 1){
    		return 2;
    	}
    	if(type == 2){
    		return 1;
    	}
    	return 0;
    }
    
    //EVENTS 
    
    public void onBlockBreak(BlockBreakEvent event){
    	Block block = event.getBlock();
    	//If IronBlock is broken
    	if(block.getTypeId() == topiron){
    		//Check if it was a Drill part
    		int direction = isFurnaceBase(block);
    		if(direction !=0){
	    		Block furnaceblock =  getFurnace(block, direction);
    			//Remove Drill from DB
    			DrillToDB drillobj = new DrillToDB(furnaceblock.getX(), furnaceblock.getY(), furnaceblock.getZ());
    			if(drillobj.existsXYZ(furnaceblock.getX(), furnaceblock.getY(), furnaceblock.getZ())== true){
	    			drillobj.deleteRecord(drillobj);
    			}
	    		return;
	    	}
    	}
    	//If DiamondBlock is broken
    	if(block.getTypeId() == topdiamond){
    		//Check if it was a Drill part
    		int direction = isFurnaceBase(block);
    		if(direction !=0){
	    		Block furnaceblock =  getFurnace(block, direction);
    			//Remove Drill from DB
    			DrillToDB drillobj = new DrillToDB(furnaceblock.getX(), furnaceblock.getY(), furnaceblock.getZ());
    			if(drillobj.existsXYZ(furnaceblock.getX(), furnaceblock.getY(), furnaceblock.getZ())== true){
	    			drillobj.deleteRecord(drillobj);
    			}
	    		return;
	    	}
    	}
    	//If a FurnaceBlock is broken
    	if(block.getTypeId() == base){
    		//Check if was a Drill part - exists in DB
    		DrillToDB drillobj = new DrillToDB(block.getX(), block.getY(), block.getZ());
			if(drillobj.existsXYZ(block.getX(), block.getY(), block.getZ())== true){
    			drillobj.deleteRecord(drillobj);
			}
    	}
    	return;
    }
    
    public void onBlockRedstoneChange(BlockRedstoneEvent event){
    	Block block = event.getBlock();
    	World world = block.getWorld();
    	int x = block.getX();
    	int y = block.getY();
    	int z = block.getZ(); 
		drillPowerOn(world , x , y , z+1);
		drillPowerOn(world , x , y , z-1);
		drillPowerOn(world , x+1 , y , z);
		drillPowerOn(world , x-1 , y , z);
		drillPowerOn(world , x , y+1 , z);
		drillPowerOn(world , x , y-1 , z);	
    }
}
