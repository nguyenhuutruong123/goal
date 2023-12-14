package com.goal.common.utils;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Slf4j
@Component
public class ProcessorDataMapper {
    private ModelMapper mapper;

    @PostConstruct
    public void initialize() {
        mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public <D> D map(Object source, Class<D> destinationClass) {

        if (source == null) {
            return null;
        } else {
            return mapper.map(source, destinationClass);
        }
    }

    public void map(Object source, Object destination) {
        if (source != null) {
            try {
                mapper.map(source, destination);
            } catch (RuntimeException ex) {
                log.error("DataMapper", ex);
            }
        }
    }

    public <S, D> List<D> mapList(List<S> sourceList, Class<D> destinationClass) {
        if (sourceList == null) {
            return (List<D>) CollectionUtils.emptyCollection();
        }
        return sourceList.stream().map((s) -> map(s, destinationClass)).collect(Collectors.toList());
    }
}
