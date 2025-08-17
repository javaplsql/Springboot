package com.santander.digitalcore.accounts.overdraftrs.mapper;

import com.santander.digitalcore.accounts.util.tests.utils.ResultSetMockMapperUtil;
import com.santander.digitalcore.accounts.util.lib.db.model.entity.ReasonOverdraftEntity;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.util.List;

public class ReasonOverdraftRsMockMapper {

    public void mockReasonOverdraftRs(ResultSet rs, ReasonOverdraftEntity reasonOverdraftEntity) {
        mockReasonOverdraftRs(rs, List.of(reasonOverdraftEntity));
    }

    public void mockReasonOverdraftRs(ResultSet rs, List<ReasonOverdraftEntity> reasonOverdraftEntityList) {
        ResultSetMockMapperUtil.mockRsNext(rs, reasonOverdraftEntityList.size());

        ResultSetMockMapperUtil.mockRsGetValues(rs, String.class, "REASON_CODE", reasonOverdraftEntityList.stream()
                .map(entity -> entity.getPk().getReasonCode())
                .toArray(String[]::new));
        ResultSetMockMapperUtil.mockRsGetValues(rs, String.class, "ENTITY", reasonOverdraftEntityList.stream()
                .map(entity -> entity.getPk().getEntity())
                .toArray(String[]::new));
        ResultSetMockMapperUtil.mockRsGetValues(rs, String.class, "BRAND", reasonOverdraftEntityList.stream()
                .map(entity -> entity.getPk().getBrand())
                .toArray(String[]::new));
        ResultSetMockMapperUtil.mockRsGetValues(rs, Timestamp.class, "START_DATE_TIME", reasonOverdraftEntityList.stream()
                .map(entity -> entity.getStartDateTime() != null ?
                        Timestamp.valueOf(entity.getStartDateTime()) : null)
                .toArray(Timestamp[]::new));
        ResultSetMockMapperUtil.mockRsGetValues(rs, Timestamp.class, "END_DATE_TIME", reasonOverdraftEntityList.stream()
                .map(entity -> entity.getEndDateTime() != null ?
                        Timestamp.valueOf(entity.getEndDateTime()) : null)
                .toArray(Timestamp[]::new));
        ResultSetMockMapperUtil.mockRsGetValues(rs, String.class, "DESCDATA", reasonOverdraftEntityList.stream()
                .map(ReasonOverdraftEntity::getDescData)
                .toArray(String[]::new));
        ResultSetMockMapperUtil.mockRsGetValues(rs, String.class, "SETTLEMENT_INDICATOR", reasonOverdraftEntityList.stream()
                .map(ReasonOverdraftEntity::getSettlementIndicator)
                .toArray(String[]::new));
        ResultSetMockMapperUtil.mockRsGetValues(rs, String.class, "MANDATORY", reasonOverdraftEntityList.stream()
                .map(ReasonOverdraftEntity::getMandatory)
                .toArray(String[]::new));
        ResultSetMockMapperUtil.mockRsGetValues(rs, java.sql.Date.class, "DEFAULTDATE", reasonOverdraftEntityList.stream()
                .map(entity -> entity.getDefaultDate() != null ?
                        java.sql.Date.valueOf(entity.getDefaultDate()) : null)
                .toArray(java.sql.Date[]::new));
        ResultSetMockMapperUtil.mockRsGetValues(rs, BigDecimal.class, "MONTHS", reasonOverdraftEntityList.stream()
                .map(ReasonOverdraftEntity::getMonths)
                .toArray(BigDecimal[]::new));
        ResultSetMockMapperUtil.mockRsGetValues(rs, BigDecimal.class, "COUNTER", reasonOverdraftEntityList.stream()
                .map(ReasonOverdraftEntity::getCounter)
                .toArray(BigDecimal[]::new));
        ResultSetMockMapperUtil.mockRsGetValues(rs, String.class, "CONDITION", reasonOverdraftEntityList.stream()
                .map(ReasonOverdraftEntity::getCondition)
                .toArray(String[]::new));
        ResultSetMockMapperUtil.mockRsGetValues(rs, String.class, "CREATION_USER", reasonOverdraftEntityList.stream()
                .map(ReasonOverdraftEntity::getCreationUser)
                .toArray(String[]::new));
        ResultSetMockMapperUtil.mockRsGetValues(rs, Timestamp.class, "CREATION_DATE_TIME", reasonOverdraftEntityList.stream()
                .map(entity -> entity.getCreationDateTime() != null ?
                        Timestamp.valueOf(entity.getCreationDateTime()) : null)
                .toArray(Timestamp[]::new));
        ResultSetMockMapperUtil.mockRsGetValues(rs, String.class, "LAST_UPDATER_USER", reasonOverdraftEntityList.stream()
                .map(ReasonOverdraftEntity::getLastUpdaterUser)
                .toArray(String[]::new));
        ResultSetMockMapperUtil.mockRsGetValues(rs, Timestamp.class, "LAST_UPDATER_DATE_TIME", reasonOverdraftEntityList.stream()
                .map(entity -> entity.getLastUpdaterDateTime() != null ?
                        Timestamp.valueOf(entity.getLastUpdaterDateTime()) : null)
                .toArray(Timestamp[]::new));
    }

}
