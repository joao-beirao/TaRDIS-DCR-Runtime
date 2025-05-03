package dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import dto.endpoint.data.computation.ComputationExprDTO;
import dto.endpoint.data.types.TypeDTO;
import dto.endpoint.data.values.ValueDTO;
import dto.endpoint.events.EventDTO;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonEncodingTests {

    @Test
    public void givenSerializedVal_ifDeserializeAndThenSerialize_thenSameJson() throws Exception {
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
        ValueDTO deserializedValueDTO =
                new ObjectMapper().readerFor(ValueDTO.class).readValue(testSrc);
        String serializedValueDTO = new ObjectMapper().writerFor(ValueDTO.class)
                .writeValueAsString(deserializedValueDTO);
        {
            ObjectMapper objectMapper = new ObjectMapper();
            assertEquals(objectMapper.readTree(testSrc), objectMapper.readTree(serializedValueDTO));
        }
    }

    @Test
    public void givenSerializedNoValueMarking_ifDeserializeAndThenSerialize_thenSameJson()
            throws Exception {
        var testSrc = """
                { "isPending": false, "isIncluded": true }
                """;
        EventDTO.MarkingDTO deserializedMarkingDTO =
                new ObjectMapper().readerFor(EventDTO.MarkingDTO.class).readValue(testSrc);
        String serializedMarkingDTO = new ObjectMapper().writerFor(EventDTO.MarkingDTO.class)
                .writeValueAsString(deserializedMarkingDTO);
        {
            ObjectMapper objectMapper = new ObjectMapper();
            assertEquals(objectMapper.readTree(testSrc),
                    objectMapper.readTree(serializedMarkingDTO));
        }
    }

    @Test
    public void givenSerializedIntValueMarking_ifDeserializeAndThenSerialize_thenSameJson()
            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());

        var testSrc = """
                 {
                   "isPending": false,
                   "isIncluded": true,
                   "defaultValue": { "intLit": { "value": 3 } }
                 }
                """;
        EventDTO.MarkingDTO deserializedMarkingDTO =
                objectMapper.readValue(testSrc, EventDTO.MarkingDTO.class);
        String serializedMarkingDTO = objectMapper.writeValueAsString(deserializedMarkingDTO);
        {
            assertEquals(objectMapper.readTree(testSrc),
                    objectMapper.readTree(serializedMarkingDTO));
        }
    }

    @Test
    public void givenFlatSerializedTypeExpr_ifDeserializeAndThenSerialize_thenSameJson()
            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        var testSrc = """
                { "valueType": "int" }
                """;
        TypeDTO deserializedTypeDTO = objectMapper.readValue(testSrc, TypeDTO.class);
        String serializedTypeDTO = objectMapper.writeValueAsString(deserializedTypeDTO);
        {
            assertEquals(objectMapper.readTree(testSrc), objectMapper.readTree(serializedTypeDTO));
        }
    }

    @Test
    public void givenNestedSerializedTypeExpr_ifDeserializeAndThenSerialize_thenSameJson()
            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        var testSrc = """
                 {
                   "recordType": {
                     "fields": [
                       {
                         "name": "p3",
                         "type": {
                           "recordType": {
                             "fields": [
                               { "name": "p2", "type": { "valueType": "bool" } },
                               { "name": "p1", "type": { "valueType": "string" } }
                             ]
                           }
                         }
                       },
                       { "name": "pp4", "type": { "eventType": { "label": "E0" } } }
                     ]
                   }
                 }
                """;
        TypeDTO deserializedTypeDTO = objectMapper.readValue(testSrc, TypeDTO.class);
        String serializedTypeDTO = objectMapper.writeValueAsString(deserializedTypeDTO);
        {
            assertEquals(objectMapper.readTree(testSrc), objectMapper.readTree(serializedTypeDTO));
        }
    }

    @Test
    public void givenNestedSerializedExpr_ifDeserializeAndThenSerialize_thenSameJson()
            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        var testSrc = """
                {
                  "record": {
                    "fields": [
                      { "name": "p3", "value": { "boolLit": { "value": true } } },
                      {
                        "name": "p4",
                        "value": {
                          "record": {
                            "fields": [
                              { "name": "p1", "value": { "boolLit": { "value": true } } },
                              { "name": "p2", "value": { "eventRef": { "value": "E0" } } }
                            ]
                          }
                        }
                      },
                      {
                        "name": "p5",
                        "value": {
                          "propDeref": {
                            "propBasedExpr": {
                              "propDeref": {
                                "propBasedExpr": { "eventRef": { "value": "E0" } },
                                "prop": "value"
                              }
                            },
                            "prop": "cid"
                          }
                        }
                      },
                      {
                        "name": "p6",
                        "value": {
                          "binaryOp": {
                            "expr1": { "intLit": { "value": 2 } },
                            "expr2": { "intLit": { "value": 3 } },
                            "op": "intAdd"
                          }
                        }
                      }
                    ]
                  }
                }
                """;
        ComputationExprDTO deserializedExprDTO =
                objectMapper.readValue(testSrc, ComputationExprDTO.class);
        String serializedExprDTO = objectMapper.writeValueAsString(deserializedExprDTO);
        {
            assertEquals(objectMapper.readTree(testSrc), objectMapper.readTree(serializedExprDTO));
        }
    }


}

