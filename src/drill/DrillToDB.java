package drill;

//import java.io.File;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.alta189.sqlLibrary.SQLite.sqlCore;


public class DrillToDB {
	
	private int Id;
	private int BlockX;
	private int  BlockY;
	private int BlockZ;
	private String Owner;
	private int Type;
	private int Direction;
	
	//Logger
    Logger log = Logger.getLogger("Minecraft");
       	
	//DB Directory Stuff
	public String mainDirectory = "plugins" + File.separator + "Drill"; 
	public File dir = new File(mainDirectory);
	public String prefix = "[Drill]";
	public String dbName = "drillDB";
	//public sqlCore dbManage = new sqlCore(this.log, this.prefix, this.dbName, this.mainDirectory );
	String tableName = "Drills";
	
	
	public DrillToDB(){
	}
	
	public DrillToDB(int blockX, int blockY, int blockZ){
		this.Id =0; //Don't care at this now...
		this.BlockX = blockX;
		this.BlockY = blockY;
		this.BlockZ = blockZ;
		this.Owner = "";
		this.Type = 0;
		this.Direction = 0;
	}
	
	//Construct a DrillToDB obj by retrieving info from PFBlocks Table...
	public DrillToDB(int id){
		this.Id = id;
		this.Type = this.getRecordType(id);
		this.Owner = this.getRecordOwner(id);
		this.Direction = this.getRecordDirection(id);
		this.BlockX = this.getRecordBlockX(id);
		this.BlockY = this.getRecordBlockY(id);
		this.BlockZ = this.getRecordBlockZ(id);
	}
	
	//METHODS TO RETRIVE INFO FROM DB Table Drills (QUERIES)...
	//INSERT Query, insert a DrillObj into DB as a Record
	public void toRecord(DrillToDB drillobj){
		String insertDrill = "INSERT INTO "+  tableName + 
		                  " (BlockX, BlockY, BlockZ, Owner, Direction , Type) "+
		 				  "VALUES (" + String.valueOf(drillobj.BlockX)+ "," +
		 				  		  String.valueOf(drillobj.BlockY) + "," +
		 				  		  String.valueOf(drillobj.BlockZ)+ "," +
		 				  		  "'" + drillobj.Owner.toString() + "'" + "," +
		 				  		  String.valueOf(drillobj.Direction)  + "," +
		 				  		  String.valueOf(drillobj.Type)  +")"+ ";";
		
		Drill.getManager().insertQuery(insertDrill);
	}
	
	//DELETE Query, delete a Record from DB by giving coordinates
	public void deleteRecord(DrillToDB drillobj){
		String deletedrill = "DELETE FROM " + tableName  + " WHERE " + 
		  				  "BlockX" + " = " + "'" + String.valueOf(drillobj.BlockX) + "'" + "AND " +
		  				  "BlockY" + " = " + "'" + String.valueOf(drillobj.BlockY) + "'" +  "AND " +
		  				  "BlockZ" + " = " + "'" + String.valueOf(drillobj.BlockZ)+  "'" + ";" ;
		Drill.getManager().deleteQuery(deletedrill); 
	}
	
	//These methods are  SELECT QUERIES to get info by passing blocks coordinates.
	//SELECT Query, SELECT the Record with the given coordinates, return its Id
	public int getRecordId(int blockX, int blockY, int blockZ){
		String selectID = "SELECT B_Id FROM " + tableName  + " WHERE " + 
		  				  "BlockX" + " = " + "'" + String.valueOf(blockX) + "'" + "AND " +
		  				  "BlockY" + " = " + "'" + String.valueOf(blockY) + "'" +  "AND " +
		  				  "BlockZ" + " = " + "'" + String.valueOf(blockZ)+  "'" +";" ;
		  //dbManage.initialize();
          ResultSet rs = Drill.getManager().sqlQuery(selectID);
          int Id = 0;
          try {
			Id = rs.getInt("B_Id");
          } catch (SQLException e) {
			e.printStackTrace();
          }
          //dbManage.close();
          return Id;  
	}
	
	//SELECT Query, SELECT the Record with the given coordinates, return its Owner 
	public String getRecordOwner(int blockX, int blockY, int blockZ){
		String selectID = "SELECT Owner FROM " + tableName  + " WHERE " + 
		  				  "BlockX" + " = " + "'" + String.valueOf(blockX) + "'" + "AND " +
		  				  "BlockY" + " = " + "'" + String.valueOf(blockY) + "'" +  "AND " +
		  				  "BlockZ" + " = " + "'" + String.valueOf(blockZ)+  "'" +";" ;
		//dbManage.initialize();
        ResultSet rs = Drill.getManager().sqlQuery(selectID);
        String Owner ="";
		try {
			Owner = rs.getString("Owner");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        //dbManage.close();
        return Owner;
	}
	
	//SELECT Query, SELECT the Record with the given coordinates, return its Type
	public int getRecordTypeXYZ(int blockX, int blockY, int blockZ){
		String selectID = "SELECT Type FROM " + tableName  + " WHERE " + 
		  				  "BlockX" + " = " + "'" + String.valueOf(blockX) + "'" + "AND " +
		                  "BlockY" + " = " + "'" + String.valueOf(blockY) + "'" +  "AND " +
		                  "BlockZ" + " = " + "'" + String.valueOf(blockZ)+  "'" +";" ;
        //dbManage.initialize();
        ResultSet rs = Drill.getManager().sqlQuery(selectID);
        int Type =0;
		try {
			Type = rs.getInt("Type");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        //dbManage.close();
        return Type;
	}
	//SELECT Query, SELECT the Record with the given coordinates, return its Direction
	public int getRecordDirectionXYZ(int blockX, int blockY, int blockZ){
		String selectID = "SELECT Direction FROM " + tableName  + " WHERE " + 
		  				  "BlockX" + " = " + "'" + String.valueOf(blockX) + "'" + "AND " +
		                  "BlockY" + " = " + "'" + String.valueOf(blockY) + "'" +  "AND " +
		                  "BlockZ" + " = " + "'" + String.valueOf(blockZ)+  "'" +";" ;
        //dbManage.initialize();
        ResultSet rs = Drill.getManager().sqlQuery(selectID);
        int Direction =0;
		try {
			Direction = rs.getInt("Direction");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        //dbManage.close();
        return Direction;
	}
	
	//These methods are SELECT Queries to get info by Id
	
	//SELECT Query, SELECT the Record with the given id, return its Type
	public int getRecordType(int id2){
		String selectID = "SELECT Type FROM " + tableName  + " WHERE " + 
						  "B_Id " + " = " + "'" + String.valueOf(id2) + "'" + ";" ;
        //dbManage.initialize();
        ResultSet rs = Drill.getManager().sqlQuery(selectID);
        int Type =0;
		try {
			Type = rs.getInt("Type");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        //dbManage.close();
        return Type;
	}
	//SELECT Query, SELECT the Record with the given id, return its Type
	public int getRecordDirection(int id){
		String selectID = "SELECT Direction FROM " + tableName  + " WHERE " + 
						  "B_Id " + " = " + "'" + String.valueOf(id) + "'" + ";" ;
        //dbManage.initialize();
        ResultSet rs = Drill.getManager().sqlQuery(selectID);
        int Direction =0;
		try {
			Direction = rs.getInt("Direction");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        //dbManage.close();
        return Direction;
	}
	
	
	//SELECT Query, SELECT the Record with the given id, return its Owner
	public String getRecordOwner(int id){
		String selectID = "SELECT Owner FROM " + tableName  + " WHERE " + 
			              "B_Id " + " = " + "'" + String.valueOf(id) + "'"  + ";" ;
		//dbManage.initialize();
        ResultSet rs = Drill.getManager().sqlQuery(selectID);
        String Owner ="";
		try {
			Owner = rs.getString("Owner");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        //dbManage.close();
        return Owner;
	}
	
	//SELECT Query, SELECT the Record with the given id, return its BlockX
	public int getRecordBlockX(int id){
		String selectID = "SELECT BlockX FROM " + tableName  + " WHERE " + 
						  "B_Id " + " = " + "'" + String.valueOf(id) + "'" + ";" ;
        //dbManage.initialize();
        ResultSet rs = Drill.getManager().sqlQuery(selectID);
        int BlockX =0;
		try {
			BlockX = rs.getInt("BlockX");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        //dbManage.close();
        return BlockX;
	}
	//SELECT Query, SELECT the Record with the given id, return its BlockY
	public int getRecordBlockY(int id){
		String selectID = "SELECT BlockY FROM " + tableName  + " WHERE " + 
						  "B_Id " + " = " + "'" + String.valueOf(id) + "'" + ";" ;
        //dbManage.initialize();
        ResultSet rs = Drill.getManager().sqlQuery(selectID);
        int BlockY =0;
		try {
			BlockY = rs.getInt("BlockY");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        //dbManage.close();
        return BlockY;
	}
	
	//SELECT Query, SELECT the Record with the given id, return its BlockZ
	public int getRecordBlockZ(int id){
		String selectID = "SELECT BlockZ FROM " + tableName  + " WHERE " + 
						  "B_Id " + " = " + "'" + String.valueOf(id) + "'" + ";" ;
        //dbManage.initialize();
        ResultSet rs = Drill.getManager().sqlQuery(selectID);
        int BlockZ =0;
		try {
			BlockZ = rs.getInt("BlockZ");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        //dbManage.close();
        return BlockZ;
	}
	
	//UPDATE QUERIES
	//UPDATE Type Query
	public void updateRecordType(int id, int type){
		String updateLink = "UPDATE " + tableName + " SET Type " + " = " +
							"'" + String.valueOf(type) + "'" +
							"WHERE B_Id" +
		                    " = " + "'" + String.valueOf(id) + "'" + ";";
		//dbManage.initialize();
		Drill.getManager().updateQuery(updateLink);
		//dbManage.close();
	}
	
	//UPDATE Direction Query
	public void updateRecordDirection(int id, int direction){
		String updateLink = "UPDATE " + tableName + " SET Type " + " = " +
							"'" + String.valueOf(direction) + "'" +
							"WHERE B_Id" +
		                    " = " + "'" + String.valueOf(id) + "'" + ";";
		//dbManage.initialize();
		Drill.getManager().updateQuery(updateLink);
		//dbManage.close();
	}
	
	//SELECT COUNT QUERY
	public int exists(int id){
		String selectcountID = "SELECT COUNT(B_Id) AS result FROM " + tableName + " WHERE B_Id = " + "'" + id + "'" + ";"; 
        //dbManage.initialize();
        ResultSet rs = Drill.getManager().sqlQuery(selectcountID);
        int count = 1;
		try {
			count = rs.getInt("result");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        //dbManage.close();
        return count;
	}
	
	public boolean existsXYZ(int blockX, int blockY, int blockZ){
		String selectcountID = "SELECT COUNT(B_Id) AS result FROM " + tableName +" WHERE " +
		  					   "BlockX" + " = " + "'" + String.valueOf(blockX) + "'" + " AND " +
		  					   "BlockY" + " = " + "'" + String.valueOf(blockY) + "'" +  " AND " +
		  					   "BlockZ" + " = " + "'" + String.valueOf(blockZ)+  "'" +";" ;
        //dbManage.initialize();
        ResultSet rs = Drill.getManager().sqlQuery(selectcountID);
        int count = 0;
		try {
			count = rs.getInt("result");
		} catch (SQLException e) {
			e.printStackTrace();
		}
        //dbManage.close();
        if(count == 0){
        	return false;
        }
        else{
        	return true;
        }
	}
	
	//Checks if the player is the owner of the PF
	public boolean isOwner(String owner, String playername){
		if(owner.equals(playername)){
			return true;
		}
		return false;
	}
	
	
	//Methods for the object	
	public void setObjId(int id){
		this.Id = id;
	}
	
	public void setObjOwner(String owner){
		this.Owner = owner;
	}
	public void setObjType(int type){
		this.Type = type;
	}
	
	public void setObjDirection(int direction){
		this.Direction = direction;
	}
	
	public int getObjId(){
		return this.Id;
	}
	
	public String getObjOwner(){
		return this.Owner;
	}
	
	public int getObjType(){
		return this.Type;
	}
	
	public int getObjDirection(){
		return this.Direction;
	}
	
	public int getObjBlockX(){
		return this.BlockX;
	}
	
	public int getObjBlockY(){
		return this.BlockY;
	}
	
	public int getObjBlockZ(){
		return this.BlockZ;
	}

}
