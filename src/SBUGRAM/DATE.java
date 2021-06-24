package SBUGRAM;

import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DATE implements Comparable, Serializable {
    @Serial
    private static final long serialVersionUID = 8929685098267757690L;

    private int year, month, day;
    private String date = null;

    public DATE() {
        this.date = Tools.getTime();
    }

    public DATE(int year, int month, int day) {
        if (year == -1 || month == -1 || day == -1) {
            int y = Integer.parseInt("");
        }
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) return 1;
        return this.date.compareTo(((DATE) o).date);
    }

    @Override
    public String toString() {
        if (date == null) {
            return year + "/" + month + "/" +  day;
        }
        return date;
    }
}

