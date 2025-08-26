import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private String reservationID;
    private UserData user;
    private Room room;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private int noOfPax;
    private String equipments;
    private String status;

    public Reservation(){
    }

    public Reservation(String reservationID, UserData user, Room room, LocalDate reservationDate, LocalTime startTime, LocalTime endTime, int noOfPax, String equipments) {
        this.reservationID = reservationID;
        this.user = user;
        this.room = room;
        this.reservationDate = reservationDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.noOfPax = noOfPax;
        this.equipments = equipments;
        this.status = "Pending"; // Set status to Pending
    }

    public String getReservationID() {
        return reservationID;
    }

    public UserData getUser() {
        return user;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getNoOfPax(){
        return noOfPax;
    }

    public String getEquipments() { 
        return equipments; 
    }

    public String getStatus() {
        return status; 
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString(){
        return reservationID + "," + user.getUserid() + "," + room.getRoomID() + "," + reservationDate.toString() + "," + startTime.toString() + "," + 
               endTime.toString() + "," + noOfPax + "," + equipments + "," + status;
    }
}
