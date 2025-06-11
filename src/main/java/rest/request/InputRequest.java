package rest.request;

import app.presentation.endpoint.data.values.ValueDTO;

import java.util.Optional;

public record InputRequest(String eventID, Optional<ValueDTO> inputValue) {

}
