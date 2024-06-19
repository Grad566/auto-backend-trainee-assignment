package util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Utils {
    public static String getFormattedDate(Timestamp time) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(time);
    }
}
