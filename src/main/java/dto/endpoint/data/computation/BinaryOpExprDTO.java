package dto.endpoint.data.computation;

import com.fasterxml.jackson.annotation.*;
import org.jetbrains.annotations.NotNull;

@JsonTypeName("binaryOpExpr")
public record BinaryOpExprDTO(
        @JsonProperty(value = "expr1", required = true) ComputationExprDTO left,
        @JsonProperty(value = "expr2", required = true) ComputationExprDTO right,
        @JsonProperty(value = "op", required = true) OpTypeDTO optType)
        implements ComputationExprDTO {


    public enum OpTypeDTO {
        AND("and"), OR("or"), BOOL_EQ("bool_equals"), INT_EQ("int_equals"), STR_EQ("str_equals");

        @JsonProperty(value = "op")
        private final String op;

        @JsonCreator
        OpTypeDTO(@JsonProperty(value = "op", required = true) String op) {this.op = op;}

        @JsonValue
        public String getOp() {return op;}

        @NotNull
        @Override
        public String toString() {
            return switch (this) {
                case AND -> "&&";
                case OR -> "||";
                case BOOL_EQ, INT_EQ, STR_EQ -> "==";
            };
        }
    }

    @NotNull
    @Override
    public String toString() {
        return String.format("(%s %s %s)", left, optType, right);
    }
}
