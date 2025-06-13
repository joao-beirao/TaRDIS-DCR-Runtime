package rest.request;





import rest.response.ValueDTO;

import java.util.Optional;

public record InputRequest(String eventID, Optional<ValueDTO> inputValue) {

}
