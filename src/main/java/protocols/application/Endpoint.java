package protocols.application;

import dcr.common.Record;
import dcr.common.data.types.Type;
import dcr.model.GraphModel;

public record Endpoint(Role role, GraphModel graphModel) {
    public record Role(String roleName, Record<Type> params) {}
}
