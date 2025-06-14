package rest.response;

import dcr.common.Record;
import dcr.common.data.types.*;
import dcr.common.data.values.*;
import dcr.common.events.Event;
import dcr.runtime.elements.events.ComputationEventInstance;
import dcr.runtime.elements.events.EventInstance;
import dcr.runtime.elements.events.InputEventInstance;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Map;
import java.util.stream.Collectors;

public final class Mappers {

    //
//    private static TypeDTO toTypeDTO(Type type) {
//        return switch (type) {
//            case BooleanType ignored -> ValueTypeDTO.BOOL;
//            case IntegerType ignored -> ValueTypeDTO.INT;
//            case StringType ignored -> ValueTypeDTO.STRING;
//            case VoidType ignored -> ValueTypeDTO.VOID;
//            case EventType eventType -> new EventTypeDTO(eventType.typeAlias());
//            case RecordType recordType -> new RecordTypeDTO(recordType.fields()
//                    .stream()
//                    .map(f -> new RecordTypeDTO.FieldDTO(f.name(), toTypeDTO(f.value())))
//                    .collect(Collectors.toList()));
//        };
//    }
//
//    private static ValueDTO toValueDTO(Value value) {
//        return switch (value) {
//            case BoolVal boolVal -> new BoolValDTO(boolVal.value());
//            case IntVal intVal -> new IntValDTO(intVal.value());
//            case StringVal stringVal -> new StringValDTO(stringVal.value());
//            case RecordVal recordVal -> new RecordValDTO(recordVal.fields()
//                    .stream()
//                    .map(f -> new RecordValDTO.FieldDTO(f.name(), toValueDTO(f.value())))
//                    .toList());
//            // TODO overlooking event vals temporarily - assuming none
//            case EventVal eventVal -> throw new RuntimeException("not implemented yet");
//            case UndefinedVal<?> undefinedVal -> null;
//            case VoidVal voidVal -> null;
//        };
//    }
//    private static ComputationExprDTO toExprDTO(ComputationExpression expr) {
//        return switch (expr) {
//            case BoolLiteral boolLit -> new BoolLiteralDTO(boolLit.value());
//            case IntLiteral intLit -> new IntLiteralDTO(intLit.value());
//            case StringLiteral stringLit -> new StringLiteralDTO(stringLit.value());
//            case RefExpr refExpr -> new RefExprDTO(refExpr.eventId());
//            case RecordExpr recordExpr -> new RecordExprDTO(recordExpr.fields()
//                    .stream()
//                    .map(f -> new RecordExprDTO.FieldDTO(f.name(), toExprDTO(f.value())))
//                    .toList());
//            case PropDerefExpr derefExpr ->
//                    new PropDerefExprDTO((PropBasedExprDTO) toExprDTO(derefExpr.propBasedExpr),
//                            derefExpr.propName);
//            case BooleanExpression booleanExpression -> toExprDTO(booleanExpression.expr());
//            case BinaryOpExpr binaryOpExpr -> {
//                var left = toExprDTO(binaryOpExpr.left());
//                var right = toExprDTO(binaryOpExpr.right());
//                var opType = switch (binaryOpExpr.opType()) {
//                    case AND -> BinaryOpExprDTO.OpTypeDTO.AND;
//                    case OR -> BinaryOpExprDTO.OpTypeDTO.OR;
//                    case EQ -> BinaryOpExprDTO.OpTypeDTO.EQ;
//                    case NEQ -> BinaryOpExprDTO.OpTypeDTO.NEQ;
//                    case INT_ADD -> BinaryOpExprDTO.OpTypeDTO.INT_ADD;
//                    case STR_CONCAT -> BinaryOpExprDTO.OpTypeDTO.STR_CONCAT;
//                    case INT_LT -> BinaryOpExprDTO.OpTypeDTO.INT_LT;
//                    case INT_GT -> BinaryOpExprDTO.OpTypeDTO.INT_GT;
//                    case INT_LEQ -> BinaryOpExprDTO.OpTypeDTO.INT_LEQ;
//                    case INT_GEQ -> BinaryOpExprDTO.OpTypeDTO.INT_GEQ;
//                };
//                yield new BinaryOpExprDTO(left, right, opType);
//            }
//            case NegationExpr negationExpr ->
//                    throw new NotImplementedException("not implemented yet");
//            case IfThenElseExpr ifThenElseExpr ->
//                    throw new NotImplementedException("not implemented yet");
//        };
//    }
//
//    private static List<UserSetExprDTO> toUserSetExprDTO(UserSetExpression userSetExpr) {
//        return switch (userSetExpr) {
//            case InitiatorExpr initiatorExpr ->
//                    List.of(new InitiatorExprDTO(initiatorExpr.eventId()));
//            case ReceiverExpr receiverExpr -> List.of(new ReceiverExprDTO(receiverExpr.eventId()));
//            case RoleExpr roleExpr -> {
//                var paramDTOs = new LinkedList<RoleExprDTO.ParamDTO>();
//                roleExpr.unconstrainedParams()
//                        .forEach(name -> paramDTOs.add(
//                                new RoleExprDTO.ParamDTO(name, Optional.empty())));
//                roleExpr.constrainedParams()
//                        .forEach(param -> paramDTOs.add(new RoleExprDTO.ParamDTO(param.name(),
//                                Optional.of(toExprDTO(param.value())))));
//                new RoleExprDTO(roleExpr.role(), paramDTOs);
//                throw new NotImplementedException("not implemented yet");
//            }
//            case SetDiffExpr ignored -> throw new NotImplementedException("not implemented yet");
//            case SetUnionExpr setUnionExpr -> setUnionExpr.userSetExprs()
//                    .stream()
//                    .map(Mappers::toUserSetExprDTO)
//                    .flatMap(Collection::stream)
//                    .collect(Collectors.toList());
//        };
//    }


    public static TypeDTO fromType(Type type) {
        return switch (type) {
            case VoidType ignored -> new UnitTypeDTO();
            case BooleanType ignored -> new BooleanTypeDTO();
            case IntegerType ignored -> new IntegerTypeDTO();
            case StringType ignored -> new StringTypeDTO();
            case RecordType ty -> new RecordTypeDTO(ty.fields().stream()
                    .collect(Collectors.toMap(Record.Field::name, f -> fromType(f.value()))));
            default -> throw new NotImplementedException("Unsupported type: " + type);
        };
    }

    public static Value toValue(ValueDTO dto) {
        return switch (dto) {
            case UnitDTO ignored -> VoidVal.instance();
            case BooleanDTO v -> BoolVal.of(v.value());
            case IntDTO v -> IntVal.of(v.value());
            case StringDTO v -> StringVal.of(v.value());
            case RecordDTO v ->
                    RecordVal.of(Record.ofEntries(v.value().entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, e -> toValue(e.getValue())))));
        };
    }

    public static ValueDTO fromValue(Value v) {
        return switch (v) {
            case BoolVal val -> new BooleanDTO(val.value());
            case IntVal val -> new IntDTO(val.value());
            case StringVal val -> new StringDTO(val.value());
            case UndefinedVal<?> ignored -> new UnitDTO("");
            case VoidVal ignored -> new UnitDTO("");
            case RecordVal val -> new RecordDTO(val.fields().stream()
                    .collect(Collectors.toMap(Record.Field::name, f -> fromValue(f.value()))));
            case EventVal val ->
                    throw new NotImplementedException("EventVal not implemented");
        };
    }

    public static EventDTO toEventDTO(EventInstance e) {
        MarkingDTO marking = null;
        {
            var hasExecuted = e.marking().hasExecuted();
            var isPending = e.marking().isPending();
            var isIncluded = e.marking().isIncluded();
            var value = fromValue(e.value());
            marking = new MarkingDTO(hasExecuted, isPending, isIncluded, value);
        }
        return switch (e) {
            case ComputationEventInstance e1 -> new EventDTO(e1);
            case InputEventInstance e2 -> new EventDTO(e2);
            default -> throw new IllegalStateException("Unexpected value: " + e);
        };
    }

    public static MarkingDTO fromMarking(Event.Marking marking) {
        return new MarkingDTO(marking.hasExecuted(), marking.isPending(), marking.isIncluded(), fromValue(marking.value()));
    }

}
