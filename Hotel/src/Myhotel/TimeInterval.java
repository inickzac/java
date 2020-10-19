package Myhotel;

import java.time.LocalDate;

public class TimeInterval {

    private LocalDate startDay;
    private LocalDate EndDay;

    public TimeInterval(LocalDate startDay,LocalDate EndDay) {
        this.startDay=startDay;
        this.EndDay=EndDay;
    }

    public LocalDate getStartDay() {
        return startDay;
    }
    public void setStartDay(LocalDate startDay) {
        this.startDay = startDay;
    }
    public LocalDate getEndDay() {
        return EndDay;
    }
    public void setEndDay(LocalDate endDay) {
        EndDay = endDay;
    }

}
