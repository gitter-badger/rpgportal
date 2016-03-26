package ru.deathman.rpgportal.core.service.api;

import java.util.List;

/**
 * @author Виктор
 */
public interface ConverterService {

    <T> T convert(Object source, Class<T> targetType);
    <T> List<T> convertList(List<?> source, Class<T> targetType);
}
