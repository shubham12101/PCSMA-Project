package com.pcsma.ifhtt.locationService;

public class Contact {
	 //private variables
   int _id;
   int _uid;
   String _time;
   String _ap_id;
   String _label;
   String _building;
   String _floor;
   String _wing;
   String _room;
   
   // Empty constructor
   public Contact(){
        
   }
   // constructor
   public Contact(int uid, String time, String ap_id, String label, String building, String floor, String wing, String room){
       this._uid = uid;
       this._time = time;
       this._ap_id = ap_id;
       this._label = label;
       this._building = building;
       this._floor = floor;
       this._wing = wing;
       this._room = room;
   }
   
// getting ID
   public int getID(){
       return this._id;
   }
    
   // setting id
   public void setID(int id){
       this._id = id;
   }
   
   // getting ID
   public int getUID(){
       return this._uid;
   }
    
   // setting id
   public void setUID(int id){
       this._uid = id;
   }
    
   // getting Address
   public String getTime(){
       return this._time;
   }
    
   // setting Address
   public void setTime(String time){
       this._time = time;
   }
   
   // getting Details
   public String getAP_ID(){
       return this._ap_id;
   }
    
   // setting Details
   public void setAP_ID(String AP_ID){
       this._ap_id = AP_ID;
   }
   
   // getting Distance
   public String getLabel(){
       return this._label;
   }
    
   // setting Distance
   public void setLabel(String label){
       this._label = label;
   }
   
   // getting City
   public String getBuilding(){
       return this._building;
   }
    
   // setting city
   public void setBuilding(String building){
       this._building = building;
   }
   
   // getting Pincode
   public String getFloor(){
       return this._floor;
   }
    
   // setting Pincode
   public void setFloor(String floor){
       this._floor = floor;
   }
   
   // getting Location
   public String getWing(){
       return this._wing;
   }
    
   // setting Location
   public void setWing(String wing){
       this._wing = wing;
   }
   
   // getting Tests
   public String getRoom(){
       return this._room;
   }
    
   // setting name
   public void setRoom(String room){
       this._room = room;
   }

}
