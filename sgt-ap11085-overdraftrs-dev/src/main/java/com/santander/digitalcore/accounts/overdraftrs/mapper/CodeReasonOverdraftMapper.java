package com.santander.digitalcore.accounts.overdraftrs.mapper;

import com.santander.digitalcore.accounts.overdraftrs.dto.CodeReasonOverdraftResponse;
import com.santander.digitalcore.accounts.util.lib.db.model.entity.CodeReasonOverdraftEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for mapping CodeReasonOverdraftEntity to CodeReasonOverdraftResponse.
 */
@Mapper(componentModel = "spring")
public interface CodeReasonOverdraftMapper {

  @Mapping(target = "creationUser", source = "auditFields.creationUser")
  @Mapping(target = "creationDateTime", source = "auditFields.creationDateTime")
  @Mapping(target = "lastUpdateUser", source = "auditFields.lastUpdateUser")
  @Mapping(target = "lastUpdateDateTime", source = "auditFields.lastUpdateDateTime")
  @Mapping(target = "cancellationUser", source = "auditFields.cancellationUser")
  @Mapping(target = "cancellationDateTime", source = "auditFields.cancellationDateTime")
  CodeReasonOverdraftResponse mapToCodeReasonOverdraftResponse(CodeReasonOverdraftEntity codeReasonOverdraftEntity);
}
