package de.luisdeltoro.calorie;

import org.dozer.CustomConverter;
import org.dozer.MappingException;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * A dozer custom converter between {@link LocalDateTime} and {@link Timestamp}
 */
public class LocalDateTimeTimestampConverter implements CustomConverter {

    @Override
    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null) {
            return null;
        }
        if (source instanceof LocalDateTime) {
            // check to see if the object already exists
            LocalDateTime sourceAsLocalDateTime = (LocalDateTime) source;
            return Timestamp.valueOf(sourceAsLocalDateTime);
        } else if (source instanceof java.sql.Timestamp) {
            Timestamp sourceAsTimestamp = (Timestamp) source;
            return sourceAsTimestamp.toLocalDateTime();
        } else {
            throw new MappingException("Converter LocalDateTimeTimestampConverter "
                    + "used incorrectly. Arguments passed in were:"
                    + destination + " and " + source);
        }
    }
}
