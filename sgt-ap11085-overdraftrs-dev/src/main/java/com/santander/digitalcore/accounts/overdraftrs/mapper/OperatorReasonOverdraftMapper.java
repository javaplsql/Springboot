package com.santander.digitalcore.accounts.overdraftrs.mapper;

import com.santander.digitalcore.accounts.overdraftrs.dto.OperatorReasonOverdraftResponse;
import com.santander.digitalcore.accounts.util.lib.db.model.entity.OperatorReasonOverdraftEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for mapping OperatorReasonOverdraftEntity to OperatorReasonOverdraftResponse.
 */
@Mapper(componentModel = "spring")
public interface OperatorReasonOverdraftMapper {

  @Mapping(target = "creationUser", source = "auditFields.creationUser")
  @Mapping(target = "creationDateTime", source = "auditFields.creationDateTime")
  @Mapping(target = "lastUpdateUser", source = "auditFields.lastUpdateUser")
  @Mapping(target = "lastUpdateDateTime", source = "auditFields.lastUpdateDateTime")
  @Mapping(target = "cancellationUser", source = "auditFields.cancellationUser")
  @Mapping(target = "cancellationDateTime", source = "auditFields.cancellationDateTime")
  OperatorReasonOverdraftResponse mapToOperatorReasonOverdraftResponse(OperatorReasonOverdraftEntity codeReasonOverdraftEntity);
}
