package app1.presentation.endpoint.events.participants;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import app1.presentation.endpoint.data.computation.ComputationExprDTO;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeName("roleExpr")
public record RoleExprDTO(@JsonProperty(value = "roleLabel", required = true) String label,
                          @JsonProperty(value = "params", required = true) List<ParamDTO> params)
        implements UserSetExprDTO {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record ParamDTO(@JsonProperty(value = "name", required = true) String name,
                           @JsonProperty(value = "value") Optional<ComputationExprDTO> value) {
        @NotNull
        @Override
        public String toString() {
            return String.format("%s= %s", name, value.isPresent() ? value.get().toString() : "*");
        }
    }

    @NotNull
    @Override
    public String toString() {
        return String.format("%s(%s)", label(),
                params.stream().map(ParamDTO::toString).collect(Collectors.joining(", ")));
    }

}
