package app1;

import app1.presentation.endpoint.GraphDTO;
import app1.presentation.endpoint.data.computation.*;
import app1.presentation.endpoint.data.types.EventTypeDTO;
import app1.presentation.endpoint.data.types.RecordTypeDTO;
import app1.presentation.endpoint.data.types.TypeDTO;
import app1.presentation.endpoint.data.types.ValueTypeDTO;
import app1.presentation.endpoint.data.values.*;
import app1.presentation.endpoint.events.ComputationEventDTO;
import app1.presentation.endpoint.events.EventDTO;
import app1.presentation.endpoint.events.InputEventDTO;
import app1.presentation.endpoint.events.ReceiveEventDto;
import app1.presentation.endpoint.events.participants.InitiatorExprDTO;
import app1.presentation.endpoint.events.participants.ReceiverExprDTO;
import app1.presentation.endpoint.events.participants.RoleExprDTO;
import app1.presentation.endpoint.events.participants.UserSetExprDTO;
import app1.presentation.endpoint.relations.RelationDTO;
import dcr1.common.Record;
import dcr1.common.data.computation.*;
import dcr1.common.data.types.*;
import dcr1.common.data.values.*;
import dcr1.common.events.userset.expressions.*;
import dcr1.model.GraphModel;
import dcr1.model.GraphModelBuilder;
import dcr1.model.events.ImmutableMarkingElement;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.stream.Collectors;


public final class EndpointMapper {
    // TODO java-doc entry-point
    public static GraphModel toGraphModel(GraphDTO dto) {
        return mapGraphDTO(dto, new GraphModelBuilder()).build();
    }

    private static GraphModelBuilder mapGraphDTO(GraphDTO graphDTO, GraphModelBuilder builder) {
        for (EventDTO eventDTO : graphDTO.events())
            builder = mapEvent(eventDTO, builder);
        for (RelationDTO relationDTO : graphDTO.relations())
            builder = mapRelation(relationDTO, builder);
        return builder;
    }

    private static Type toType(TypeDTO dto) {
        return switch (dto) {
            case ValueTypeDTO type -> switch (type) {
                case BOOL -> BooleanType.singleton();
                case INT -> IntegerType.singleton();
                case STRING -> StringType.singleton();
                case VOID -> VoidType.singleton();
            };
            case EventTypeDTO type -> new EventType(type.eventType());
            case RecordTypeDTO type -> RecordType.of(Record.ofEntries(type.fields()
                    .stream()
                    .collect(Collectors.toMap(RecordTypeDTO.FieldDTO::name,
                            x -> toType(x.value())))));
        };
    }

    private static Value toValue(ValueDTO dto) {
        return switch (dto) {
            case BoolValDTO v -> BoolVal.of(v.value());
            case IntValDTO v -> IntVal.of(v.value());
            case StringValDTO v -> StringVal.of(v.value());
            case RecordValDTO v -> RecordVal.of(Record.ofEntries(v.fields()
                    .stream()
                    .collect(Collectors.toMap(RecordValDTO.FieldDTO::name,
                            x -> toValue(x.value())))));
        };
    }

    private static ComputationExpression toExpr(ComputationExprDTO dto) {
        return switch (dto) {
            case BoolLiteralDTO expr -> BoolLiteral.of(expr.value());
            case IntLiteralDTO expr -> IntLiteral.of(expr.value());
            case StringLiteralDTO expr -> StringLiteral.of(expr.value());
            case RefExprDTO expr -> RefExpr.of(expr.value());
            case RecordExprDTO expr -> RecordExpr.of(Record.ofEntries(expr.fields()
                    .stream()
                    .collect(Collectors.toMap(RecordExprDTO.FieldDTO::name,
                            x -> toExpr(x.value())))));
            case PropDerefExprDTO expr -> PropDerefExpr.of(toExpr(expr.expr()), expr.prop());
            case BinaryOpExprDTO expr -> {
                var left = toExpr(expr.left());
                var right = toExpr(expr.right());
                yield switch (expr.optType()) {
                    case AND -> BinaryOpExpr.of(left, right, BinaryOpExpr.OpType.AND);
                    case OR -> BinaryOpExpr.of(left, right, BinaryOpExpr.OpType.OR);
                    case EQ -> BinaryOpExpr.of(left, right, BinaryOpExpr.OpType.EQ);
                    case NEQ -> BinaryOpExpr.of(left, right, BinaryOpExpr.OpType.NEQ);
                    case INT_ADD -> BinaryOpExpr.of(left, right, BinaryOpExpr.OpType.INT_ADD);
                    case STR_CONCAT -> BinaryOpExpr.of(left, right, BinaryOpExpr.OpType.STR_CONCAT);
                    case INT_LT -> BinaryOpExpr.of(left, right, BinaryOpExpr.OpType.INT_LT);
                    case INT_GT -> BinaryOpExpr.of(left, right, BinaryOpExpr.OpType.INT_GT);
                    case INT_LEQ -> BinaryOpExpr.of(left, right, BinaryOpExpr.OpType.INT_LEQ);
                    case INT_GEQ -> BinaryOpExpr.of(left, right, BinaryOpExpr.OpType.INT_GEQ);
                };
            }
        };
    }

    // TODO [monitor] under the current assumptions, role will always be parameterised - may change
    private static UserSetExpression toUserSetExpr(UserSetExprDTO dto) {
        return switch (dto) {
            case RoleExprDTO e -> {
                if (e.params().isEmpty()) {
                    yield RoleExpr.of(e.label());
                }
                else {
                    yield RoleExpr.of(e.label(), Record.ofEntries(e.params()
                            .stream()
                            .filter(x -> x.value().isPresent())
                            .collect(Collectors.toMap(RoleExprDTO.ParamDTO::name,
                                    x -> toExpr(x.value().get())))));
                }
            }
            case InitiatorExprDTO e -> InitiatorExpr.of(e.eventId());
            case ReceiverExprDTO e -> ReceiverExpr.of(e.eventId());
            default -> throw new IllegalStateException("Unexpected value: " + dto);
        };
    }

    private static GraphModelBuilder mapEvent(EventDTO dto, GraphModelBuilder builder) {
        var uid = dto.common.uid();
        var id = dto.common.id();
        var eventType = dto.common.label();
        ImmutableMarkingElement marking;
        {
            var isPending = dto.common.marking().isPending();
            var isIncluded = dto.common.marking().isIncluded();
            var value = dto.common.marking().value()
                    .map(EndpointMapper::toValue)
                    .orElse(UndefinedVal.of(toType(dto.common.dataType())));
            marking = new ImmutableMarkingElement(isPending, isIncluded, value);
        }
        var instantiationConstraint =
                dto.common.instantiationConstraint().map(EndpointMapper::toExpr).orElse(BoolLiteral.TRUE);
        var ifcConstraint =
                dto.common.ifcConstraint().map(EndpointMapper::toExpr).orElse(BoolLiteral.TRUE);
        return switch (dto) {
            case ComputationEventDTO e -> {
                var expr = toExpr(e.dataExpr);
                if (e.receivers.isEmpty()) {
                    yield builder.addLocalComputationEvent(uid, id, eventType, expr, marking,
                            (BooleanExpression) instantiationConstraint,
                            (BooleanExpression) ifcConstraint);
                }
                else {
                    var receiversExpr = SetUnionExpr.of(e.receivers.stream()
                            .map(EndpointMapper::toUserSetExpr)
                            .collect(Collectors.toCollection(ArrayList::new)));
                    yield builder.addComputationEvent(uid, id, eventType, expr, receiversExpr,
                            marking, (BooleanExpression) instantiationConstraint,
                            (BooleanExpression) ifcConstraint);
                }
            }
            case InputEventDTO e -> {
                if (e.receivers.isEmpty()) {
                    yield builder.addLocalInputEvent(uid, id, eventType, marking,
                            (BooleanExpression) instantiationConstraint,
                            (BooleanExpression) ifcConstraint);
                }
                else {
                    var receiversExpr = SetUnionExpr.of(e.receivers.stream()
                            .map(EndpointMapper::toUserSetExpr)
                            .collect(Collectors.toCollection(ArrayList::new)));
                    yield builder.addInputEvent(uid, id, eventType, receiversExpr, marking,
                            (BooleanExpression) instantiationConstraint,
                            (BooleanExpression) ifcConstraint);
                }
            }
            case ReceiveEventDto e -> {
                var initiatorsExpr = toUserSetExpr(e.initiators);
                yield builder.addReceiveEvent(uid, id, eventType, initiatorsExpr, marking,
                        (BooleanExpression) instantiationConstraint,
                        (BooleanExpression) ifcConstraint);
            }
        };
    }

    private static GraphModelBuilder mapRelation(RelationDTO dto, GraphModelBuilder builder) {
        throw new NotImplementedException("Not implemented yet");
    }
}

