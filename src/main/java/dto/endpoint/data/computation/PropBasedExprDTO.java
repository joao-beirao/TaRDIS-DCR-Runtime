package dto.endpoint.data.computation;

public sealed interface PropBasedExprDTO extends ComputationExprDTO
        permits PropDerefExprDTO, RecordExprDTO, RefExprDTO {
}
