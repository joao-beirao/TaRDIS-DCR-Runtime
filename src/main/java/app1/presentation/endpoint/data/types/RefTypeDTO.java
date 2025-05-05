package app1.presentation.endpoint.data.types;

public sealed interface RefTypeDTO extends TypeDTO
        permits EventTypeDTO, RecordTypeDTO {
}
