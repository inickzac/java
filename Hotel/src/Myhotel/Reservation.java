package Myhotel;

import java.time.LocalDate;

public class Reservation {
    private int id;
    private LocalDate startREservation;
    private LocalDate endReservation;
    private User userMakingReservation;
    private int idRoom;
    private int numberOfRoom;

    public Reservation(int id, LocalDate startREservation, LocalDate endReservation, User userMakingReservation, int idRoom, int numberOfRoom) {
        this.id = id;
        this.startREservation = startREservation;
        this.endReservation = endReservation;
        this.userMakingReservation = userMakingReservation;
        this.idRoom = idRoom;
        this.numberOfRoom = numberOfRoom;
    }

    public int getId() {
        return id;
    }

    public LocalDate getStartREservation() {
        return startREservation;
    }

    public LocalDate getEndReservation() {
        return endReservation;
    }

    public User getUserMakingReservation() {
        return userMakingReservation;
    }

    public int getIdRoom() {
        return idRoom;
    }

    public int getNumberOfRoom() {
        return numberOfRoom;
    }


}
