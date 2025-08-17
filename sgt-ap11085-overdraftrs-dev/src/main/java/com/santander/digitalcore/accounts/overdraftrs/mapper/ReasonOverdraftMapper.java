package com.santander.digitalcore.accounts.overdraftrs.mapper;

import com.santander.digitalcore.accounts.util.lib.db.model.entity.ReasonOverdraftEntity;
import com.santander.digitalcore.accounts.util.lib.db.model.entity.ReasonOverdraftEntityPK;
import com.santander.digitalcore.accounts.overdraftrs.dto.ReasonOverdraftDTO;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Context;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper
public interface ReasonOverdraftMapper {

    @Mapping(target = "pk.entity", source = "entity")
    @Mapping(target = "pk.brand", source = "brand")
    @Mapping(target = "pk.reasonCode", source = "reasonCode")
    @Mapping(target = "startDateTime", expression = "java(mapToDateTime(dto.getStartDateTime()))")
    @Mapping(target = "descData", source = "descData")
    @Mapping(target = "settlementIndicator", source = "settlementIndicator")
    @Mapping(target = "mandatory", source = "mandatory")
    @Mapping(target = "defaultDate", expression = "java(mapToDate(dto.getDefaultDate()))")
    @Mapping(target = "months", source = "months")
    @Mapping(target = "counter", source = "counter")
    @Mapping(target = "condition", source = "condition")
    @Mapping(target = "creationUser", source = "creationUser")
    @Mapping(target = "creationDateTime", expression = "java(mapToDateTime(dto.getCreationDateTime()))")
    @Mapping(target = "lastUpdaterDateTime", expression = "java(mapToDateTime(dto.getLastUpdaterDateTime()))")
    ReasonOverdraftEntity mapToEntity(ReasonOverdraftDTO dto, @Context String updaterUser);

    @Mapping(target = "entity", source = "pk.entity")
    @Mapping(target = "brand", source = "pk.brand")
    @Mapping(target = "reasonCode", source = "pk.reasonCode")
    @Mapping(target = "startDateTime", expression = "java(mapToDateTimeStr(entity.getStartDateTime()))")
    @Mapping(target = "endDateTime", expression = "java(mapToDateTimeStr(entity.getEndDateTime()))")
    @Mapping(target = "descData", source = "descData")
    @Mapping(target = "settlementIndicator", source = "settlementIndicator")
    @Mapping(target = "mandatory", source = "mandatory")
    @Mapping(target = "defaultDate", expression = "java(mapToDateStr(entity.getDefaultDate()))")
    @Mapping(target = "months", source = "months")
    @Mapping(target = "counter", source = "counter")
    @Mapping(target = "condition", source = "condition")
    @Mapping(target = "creationUser", source = "creationUser")
    @Mapping(target = "creationDateTime", expression = "java(mapToDateTimeStr(entity.getCreationDateTime()))")
    @Mapping(target = "lastUpdaterUser", source = "lastUpdaterUser")
    @Mapping(target = "lastUpdaterDateTime", expression = "java(mapToDateTimeStr(entity.getLastUpdaterDateTime()))")
    ReasonOverdraftDTO mapToDto(ReasonOverdraftEntity entity);

    @Mapping(target = "reasonCode", source = "reasonCode")
    @Mapping(target = "entity", source = "entity")
    @Mapping(target = "brand", source = "brand")
    ReasonOverdraftEntityPK mapToEntityPK(String reasonCode, String entity, String brand);

    @AfterMapping
    default void afterMapping(@MappingTarget ReasonOverdraftEntity entity, @Context String updaterUser) {
        if (StringUtils.isEmpty(entity.getCreationUser())) {
            entity.setCreationUser(updaterUser);
        }
        entity.setLastUpdaterUser(updaterUser);
    }

    default LocalDate mapToDate(String date) {
        return (date != null) ? LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
    }

    default LocalDateTime mapToDateTime(String dateTime) {
        return (dateTime != null) ? LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) : null;
    }

    default String mapToDateStr(LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
    }

    default String mapToDateTimeStr(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) : null;
    }

}
