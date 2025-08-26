import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConferenceRoom extends Room {
    private List<String> equipment;

    // Empty constructor
    public ConferenceRoom(){
        this.equipment = new ArrayList<>();
    }

    public ConferenceRoom(String roomID){
        super(roomID);
    }

    // Primary constructor
    public ConferenceRoom(String roomID, String roomName, int maxCapacity, List<String> equipment) {
        super(roomID, roomName, maxCapacity);
        // Manual defensive copy
        this.equipment = new ArrayList<>();

    }

    // Modification methods
    public void addEquipment(String item) {
        if (equipment == null) {
            equipment = new ArrayList<>();
        }
        if (item != null && !item.trim().isEmpty()) {
            equipment.add(item.trim());
        }
    }

    @Override
    public String getEquipmentString() {
        return String.join(";", equipment);
    }

}