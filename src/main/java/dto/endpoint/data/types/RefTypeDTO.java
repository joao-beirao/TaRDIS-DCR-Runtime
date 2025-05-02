package dto.endpoint.data.types;

import com.fasterxml.jackson.annotation.JsonTypeName;

public sealed interface RefTypeDTO extends TypeDTO
        permits EventRefTypeDTO, RecordTypeDTO {
}
