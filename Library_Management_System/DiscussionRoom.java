public class DiscussionRoom extends Room {
    // Empty constructor
    public DiscussionRoom(){
    }

    public DiscussionRoom(String roomID){
        super(roomID);
    }
    
    public DiscussionRoom(String roomID, String roomName, int maxCapacity) {
        super(roomID, roomName, maxCapacity);
    }

    @Override
    public String getEquipmentString() {
        return "";  // Pass empty string for TXT file
    }
}