package dto.endpoint.participants;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dto.endpoint.data.computation.ComputationExprDTO;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeName("roleExpr")
public record RoleExprDTO(@JsonProperty(value = "roleName", required = true) String roleName,
                          @JsonProperty(value = "params", required = true) List<Param> params)
        implements UserSetExprDTO {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record Param(@JsonProperty(value = "paramName", required = true) String name,
                        @JsonProperty(value = "paramExpr") Optional<ComputationExprDTO> expr) {
        @NotNull
        @Override
        public String toString() {
            return String.format("%s= %s", name, expr.isPresent() ? expr.get().toString() : "*");
        }
    }

    @NotNull
    @Override
    public String toString() {
        return String.format("%s(%s)", roleName(),
                params.stream().map(Param::toString).collect(Collectors.joining(", ")));
    }

}
