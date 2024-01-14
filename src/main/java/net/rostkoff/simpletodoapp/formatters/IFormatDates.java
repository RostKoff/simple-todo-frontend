package net.rostkoff.simpletodoapp.formatters;

import java.util.Map;

public interface IFormatDates<T> {
    // Extract dates from the Object's fields, format them and return as Map where key is field name and value is formatted date.
    public Map<String, String> getFormattedDates(T t);
}
