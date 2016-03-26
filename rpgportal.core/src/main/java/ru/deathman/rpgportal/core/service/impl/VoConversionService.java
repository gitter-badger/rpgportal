package ru.deathman.rpgportal.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.deathman.rpgportal.core.service.api.ConverterService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Виктор
 */
@Component("voConversionService")
public class VoConversionService extends GenericConversionService implements ConverterService {

    @Autowired(required = false)
    @Qualifier("vo")
    private List<Converter> converters;
    @Autowired(required = false)
    @Qualifier("vo")
    private List<GenericConverter> genericConverters;
    @Autowired(required = false)
    @Qualifier("vo")
    private List<ConverterFactory> converterFactories;

    @PostConstruct
    public void afterPropertiesSet() {
        Set<Object> allConverters = new HashSet<>();
        if (!CollectionUtils.isEmpty(converters)) {
            allConverters.addAll(converters);
        }
        if (!CollectionUtils.isEmpty(genericConverters)) {
            allConverters.addAll(genericConverters);
        }
        if (!CollectionUtils.isEmpty(converterFactories)) {
            allConverters.addAll(converterFactories);
        }
        if (!CollectionUtils.isEmpty(allConverters)) {
            ConversionServiceFactory.registerConverters(allConverters, this);
        }
    }

    @Override
    public <T> List<T> convertList(List<?> source, Class<T> targetType) {
        List<T> result = new ArrayList<>();
        for (Object o : source) {
            result.add(convert(o, targetType));
        }
        return result;
    }
}
