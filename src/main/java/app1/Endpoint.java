package app1;

import dcr1.common.Record;
import dcr1.common.data.types.Type;
import dcr1.model.GraphModel;

public record Endpoint(Role role, GraphModel graphModel) {
    public record Role(String roleName, Record<Type> params) {}
}
