import java.util.Objects;

public class TimeOfDay implements Comparable<TimeOfDay> {

    private int hours;   // от 0 до 23
    private int minutes; // от 0 до 59

    public TimeOfDay(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    // Нужно для TreeMap — чтобы он умел сортировать по времени
    @Override
    public int compareTo(TimeOfDay other) {
        int thisTotal = this.hours * 60 + this.minutes;
        int otherTotal = other.hours * 60 + other.minutes;
        return Integer.compare(thisTotal, otherTotal);
    }

    // Нужно для поиска по ключу в TreeMap
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeOfDay)) return false;
        TimeOfDay that = (TimeOfDay) o;
        return hours == that.hours && minutes == that.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, minutes);
    }
}