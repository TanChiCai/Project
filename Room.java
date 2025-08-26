public abstract class Room {
    private String roomID;  
    private String roomName;
    private int maxCapacity;

    public Room(){
    }

    public Room(String roomID){
        this.roomID = roomID;
    }
    
    public Room(String roomID, String roomName, int maxCapacity) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.maxCapacity = maxCapacity;
    }

    // Getters
    public String getRoomID() { 
        return roomID; 
    }
    public String getRoomName() { 
        return roomName; 
    }
    public int getMaxCapacity() { 
        return maxCapacity; 
    }

    // Abstract method for equipment representation
    public abstract String getEquipmentString();
}