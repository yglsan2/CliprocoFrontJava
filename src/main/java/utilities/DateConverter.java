package utilities;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateConverter {
    
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    
    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    
    public static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            LocalDate localDate = LocalDate.parse(dateStr);
            return toDate(localDate);
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date invalide: " + dateStr);
        }
    }
} 