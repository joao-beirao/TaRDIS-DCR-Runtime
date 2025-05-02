package dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.endpoint.data.values.ValueDTO;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonEncodingTests {

    @Test
    public void givenSerializedVal_ifDeserializeAndSerialize_thenSameJson() throws Exception {
        var testSrc = """
                {
                 "recordVal": {
                   "fields": [
                     { "name": "p3", "value": { "stringLit": { "value": "a_string" } } },
                     {
                       "name": "p4",
                       "value": {
                         "recordVal": {
                           "fields": [
                             {
                               "name": "p5",
                               "value": {
                                 "recordVal": {
                                   "fields": [
                                     {
                                       "name": "p1",
                                       "value": { "boolLit": { "value": true } }
                                     },
                                     { "name": "p2", "value": { "intLit": { "value": 3 } } }
                                   ]
                                 }
                               }
                             }
                           ]
                         }
                       }
                     }
                   ]
                 }
               }
               """;
        ValueDTO deserializedValueDTO = new ObjectMapper().readerFor(ValueDTO.class).readValue(testSrc,
                ValueDTO.class);
        String serializedValueDTO =
                new ObjectMapper().writerFor(ValueDTO.class).writeValueAsString(deserializedValueDTO);
        {
            ObjectMapper objectMapper = new ObjectMapper();
            assertEquals(objectMapper.readTree(testSrc), objectMapper.readTree(serializedValueDTO));
        }
    }

}
