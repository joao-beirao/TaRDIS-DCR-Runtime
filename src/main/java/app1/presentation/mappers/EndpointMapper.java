package app1.presentation.mappers;

import app1.Endpoint;
import app1.presentation.endpoint.EndpointDTO;
import app1.presentation.endpoint.GraphDTO;
import app1.presentation.endpoint.RoleDTO;
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
import app1.presentation.endpoint.relations.ControlFlowRelationDTO;
import app1.presentation.endpoint.relations.RelationDTO;
import app1.presentation.endpoint.relations.SpawnRelationDTO;
import dcr1.common.Record;
import dcr1.common.data.computation.*;
import dcr1.common.data.types.*;
import dcr1.common.data.values.*;
import dcr1.common.events.userset.expressions.*;
import dcr1.common.relations.ControlFlowRelation;
import dcr1.common.relations.Relation;
import dcr1.model.GraphModelBuilder;
import dcr1.model.RecursiveGraphModel;
import dcr1.model.events.ImmutableMarkingElement;
import dcr1.model.relations.RelationElements;

import java.util.ArrayList;
import java.util.stream.Collectors;


public final class EndpointMapper {

    // TODO java-doc entry-point
    public static Endpoint mapEndpoint(EndpointDTO endpointDTO) {
        return new Endpoint(mapRole(endpointDTO.role()), mapGraphModel(endpointDTO.graph()));
    }

    private static Endpoint.Role mapRole(RoleDTO dto) {
        var label = dto.label();
        var params = Record.ofEntries(dto.params()
                .stream()
                .map(p -> Record.Field.of(p.name(), mapType(p.type())))
                .collect(Collectors.toMap(Record.Field::name, Record.Field::value)));
        return new Endpoint.Role(label, params);
    }

    private static RecursiveGraphModel mapGraphModel(GraphDTO dto) {
        return mapGraphDTO(dto, new GraphModelBuilder()).build();
    }

    private static RecursiveGraphModel mapGraphModel(String endpointElementUID, GraphDTO dto) {
        return mapGraphDTO(dto, new GraphModelBuilder(endpointElementUID)).build();
    }

    private static GraphModelBuilder mapGraphDTO(GraphDTO graphDTO, GraphModelBuilder builder) {
        for (EventDTO eventDTO : graphDTO.events())
            builder = mapEvent(eventDTO, builder);
        for (RelationDTO relationDTO : graphDTO.relations())
            builder = mapRelation(relationDTO, builder);
        return builder;
    }

    private static Type mapType(TypeDTO dto) {
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
                            x -> mapType(x.value())))));
        };
    }

    private static Value mapValue(ValueDTO dto) {
        return switch (dto) {
            case BoolValDTO v -> BoolVal.of(v.value());
            case IntValDTO v -> IntVal.of(v.value());
            case StringValDTO v -> StringVal.of(v.value());
            case RecordValDTO v -> RecordVal.of(Record.ofEntries(v.fields()
                    .stream()
                    .collect(Collectors.toMap(RecordValDTO.FieldDTO::name,
                            x -> mapValue(x.value())))));
        };
    }

    private static ComputationExpression mapExpr(ComputationExprDTO dto) {
        return switch (dto) {
            case BoolLiteralDTO expr -> BoolLiteral.of(expr.value());
            case IntLiteralDTO expr -> IntLiteral.of(expr.value());
            case StringLiteralDTO expr -> StringLiteral.of(expr.value());
            case RefExprDTO expr -> RefExpr.of(expr.value());
            case RecordExprDTO expr -> RecordExpr.of(Record.ofEntries(expr.fields()
                    .stream()
                    .collect(Collectors.toMap(RecordExprDTO.FieldDTO::name,
                            x -> mapExpr(x.value())))));
            case PropDerefExprDTO expr -> PropDerefExpr.of(mapExpr(expr.expr()), expr.prop());
            case BinaryOpExprDTO expr -> {
                var left = mapExpr(expr.left());
                var right = mapExpr(expr.right());
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
    private static UserSetExpression mapUserSetExpr(UserSetExprDTO dto) {
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
                                    x -> mapExpr(x.value().get())))));
                }
            }
            case InitiatorExprDTO e -> InitiatorExpr.of(e.eventId());
            case ReceiverExprDTO e -> ReceiverExpr.of(e.eventId());
            default -> throw new IllegalStateException("Unexpected value: " + dto);
        };
    }

    private static GraphModelBuilder mapEvent(EventDTO dto, GraphModelBuilder builder) {
        var choreoElementUID = dto.common.choreoElementUID();
        var endpointElementUID = dto.common.endpointElementUID();
        var id = dto.common.id();
        var eventType = dto.common.label();
        ImmutableMarkingElement marking;
        {
            var isPending = dto.common.marking().isPending();
            var isIncluded = dto.common.marking().isIncluded();
            var value = dto.common.marking()
                    .value()
                    .map(EndpointMapper::mapValue)
                    .orElse(UndefinedVal.of(mapType(dto.common.dataType())));
            marking = new ImmutableMarkingElement(isPending, isIncluded, value);
        }
        var instantiationConstraint = dto.common.instantiationConstraint()
                .map(EndpointMapper::mapExpr)
                .orElse(BoolLiteral.TRUE);
        var ifcConstraint =
                dto.common.ifcConstraint().map(EndpointMapper::mapExpr).orElse(BoolLiteral.TRUE);
        return switch (dto) {
            case ComputationEventDTO e -> {
                var expr = mapExpr(e.dataExpr);
                if (e.receivers.isEmpty()) {
                    yield builder.addLocalComputationEvent(choreoElementUID, endpointElementUID, id, eventType, expr
                            , marking,
                            BooleanExpression.of(instantiationConstraint),
                            BooleanExpression.of(ifcConstraint));
                }
                else {
                    var receiversExpr = SetUnionExpr.of(e.receivers.stream()
                            .map(EndpointMapper::mapUserSetExpr)
                            .collect(Collectors.toCollection(ArrayList::new)));
                    yield builder.addComputationEvent(choreoElementUID, endpointElementUID, id, eventType, expr,
                            receiversExpr,
                            marking,  BooleanExpression.of(instantiationConstraint),
                            BooleanExpression.of(ifcConstraint));
                }
            }
            case InputEventDTO e -> {
                if (e.receivers.isEmpty()) {
                    yield builder.addLocalInputEvent(choreoElementUID, endpointElementUID, id, eventType, marking,
                            BooleanExpression.of(instantiationConstraint),
                            BooleanExpression.of(ifcConstraint));
                }
                else {
                    var receiversExpr = SetUnionExpr.of(e.receivers.stream()
                            .map(EndpointMapper::mapUserSetExpr)
                            .collect(Collectors.toCollection(ArrayList::new)));
                    yield builder.addInputEvent(choreoElementUID, endpointElementUID, id, eventType, receiversExpr,
                            marking,
                            BooleanExpression.of(instantiationConstraint),
                            BooleanExpression.of(ifcConstraint));
                }
            }
            case ReceiveEventDto e -> {
                var initiatorsExpr = SetUnionExpr.of(e.initiators.stream()
                        .map(EndpointMapper::mapUserSetExpr)
                        .collect(Collectors.toCollection(ArrayList::new)));
                yield builder.addReceiveEvent(choreoElementUID, endpointElementUID, id, eventType, initiatorsExpr,
                        marking,
                        BooleanExpression.of(instantiationConstraint),
                        BooleanExpression.of(ifcConstraint));
            }
        };
    }

    private static ControlFlowRelation.Type mapRelationType(
            ControlFlowRelationDTO.RelationTypeDTO dto) {
        return switch (dto) {
            case INCLUDE -> ControlFlowRelation.Type.INCLUDE;
            case EXCLUDE -> ControlFlowRelation.Type.EXCLUDE;
            case RESPONSE -> ControlFlowRelation.Type.RESPONSE;
            case CONDITION -> ControlFlowRelation.Type.CONDITION;
            case MILESTONE -> ControlFlowRelation.Type.MILESTONE;
        };
    }

    private static GraphModelBuilder mapRelation(RelationDTO dto, GraphModelBuilder builder) {
        var endpointElementUID = dto.common().endpointElementUID();
        var srcId = dto.common().sourceId();
        var guard =
                dto.common().guard().map(EndpointMapper::mapExpr).orElse(Relation.DEFAULT_GUARD);
        var instantiationConstraint = dto.common()
                .instantiationConstraint()
                .map(EndpointMapper::mapExpr)
                .orElse(Relation.DEFAULT_INSTANTIATION_CONSTRAINT);
        return switch (dto) {
            case ControlFlowRelationDTO r -> builder.addControlFlowRelation(
                    RelationElements.newControlFlowRelation(endpointElementUID, srcId,
                            r.targetId(), guard,
                            mapRelationType(r.relationType()), instantiationConstraint));
            case SpawnRelationDTO r -> builder.addSpawnRelation(
                    RelationElements.newSpawnRelation(endpointElementUID, srcId, r.triggerId(),
                            guard,
                            mapGraphModel(endpointElementUID, r.graph()),
                            instantiationConstraint));
        };
    }

}