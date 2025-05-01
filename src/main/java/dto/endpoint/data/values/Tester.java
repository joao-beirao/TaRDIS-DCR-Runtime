package dto.endpoint.data.values;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dto.endpoint.data.computation.*;
import dto.endpoint.data.types.PrimitiveTypeDTO;
import dto.endpoint.data.types.RecordTypeDTO;
import dto.endpoint.data.types.RefTypeDTO;
import dto.endpoint.data.types.TypeDTO;
import dto.endpoint.events.ComputationEventDTO;
import dto.endpoint.events.EventDTO;

import java.util.List;


public class Tester {

    public static String formatJson(String input) {
        return input.replaceAll("'", "\"");
    }


    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();

        IntValDTO intVal = new IntValDTO(3);

        // Map<String, ValueDTO> fields = new HashMap<>();
        // fields.put("pid", new IntValDTO(3));
        // fields.put("id", new StringValDTO("a_string"));

        List<RecordValDTO.FieldDTO> fields = List.of(new RecordValDTO.FieldDTO("pid",
                new IntValDTO(3))
        );

        RecordValDTO recVal = new RecordValDTO(fields);


        RecordValDTO recordVal;
        {
            var requesterFields = List.of(new RecordValDTO.FieldDTO("id",
                            new StringValDTO("#p4")), new RecordValDTO.FieldDTO("cid",
                            new IntValDTO(5)),
                    new RecordValDTO.FieldDTO("event", new RefValDTO("a")));
            recordVal = new RecordValDTO(
                    List.of(new RecordValDTO.FieldDTO("request_id", new StringValDTO("#00834238")),
                            new RecordValDTO.FieldDTO("kw", new IntValDTO(13)),
                            new RecordValDTO.FieldDTO("requester",
                                    new RecordValDTO(requesterFields))));
        }

        try {
            String jsonRec = objectMapper.writeValueAsString(recVal);

            System.out.println(jsonRec);

            ValueDTO deserializedRecVal = objectMapper.readValue(jsonRec, ValueDTO.class);

            System.out.println("Deserialized valueDTO Class: " + deserializedRecVal);

            String serializedIntval = objectMapper.writeValueAsString(intVal);

            ValueDTO deseriliazedIntVal = objectMapper.readValue(serializedIntval, ValueDTO.class);

            System.out.println(
                    "Deserialized intVal Class: " + deseriliazedIntVal.getClass().getSimpleName());

            // ==== recordVal test =====

            System.out.println("\n==== recordVal test =====\n");

            String serializedRecordVal = objectMapper.writeValueAsString(recordVal);

            System.out.println("Serialized RecordValDTO: " + serializedRecordVal);


            // ==== Marking test =====

            System.out.println("\n==== Marking tests =====\n");

            System.out.println("= No-Value Marking test\n");

            EventDTO.MarkingDTO noValueMarkingDTO = new EventDTO.MarkingDTO(true, true);

            String serializedNoValueMarkingDTO =
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
                            .writeValueAsString(noValueMarkingDTO);

            System.out.println("Serialized no-value MarkingDTO: " + serializedNoValueMarkingDTO);

            EventDTO.MarkingDTO deserializedMarkingNoVal =
                    objectMapper.readValue(serializedNoValueMarkingDTO,
                            EventDTO.MarkingDTO.class);

            System.out.println("Deserialized no-value MarkingDTO: " + deserializedMarkingNoVal);


            System.out.println("\n= Value Marking test\n");

            EventDTO.MarkingDTO intValuedMarkingDTO = new EventDTO.MarkingDTO(true, true, intVal);

            String serializedIntValuedMarkingDTO =
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
                            .writeValueAsString(intValuedMarkingDTO);

            System.out.println("Serialized int-valued MarkingDTO: " + serializedIntValuedMarkingDTO);

            EventDTO.MarkingDTO deserializedIntValuedMarking =
                    objectMapper.readValue(serializedIntValuedMarkingDTO,
                            EventDTO.MarkingDTO.class);

            System.out.println("Deserialized int-value MarkingDTO: " + deserializedIntValuedMarking);


            // ==== Types test =====

            System.out.println("\n==== Types tests =====\n");

            TypeDTO recordTypeDTO;
            {
                var nestedParams = List.of(new RecordTypeDTO.FieldDTO("param3",
                                new PrimitiveTypeDTO(PrimitiveTypeDTO.PrimitiveType.STRING)),
                        new RecordTypeDTO.FieldDTO("param4",
                                new PrimitiveTypeDTO(PrimitiveTypeDTO.PrimitiveType.INT)));
                var params = List.of(new RecordTypeDTO.FieldDTO("param1",
                        new RefTypeDTO("Consume")),
                        new RecordTypeDTO.FieldDTO("param2",
                                new RecordTypeDTO(nestedParams)));
                recordTypeDTO = new RecordTypeDTO(params);
            }

            String serializedRecordTypeDTO =
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
                            .writeValueAsString(recordTypeDTO);

            System.out.println("Serialized primitive TypeDTO:\n" + serializedRecordTypeDTO);

            TypeDTO deserialisedRecordTypeDTO = objectMapper.readValue(serializedRecordTypeDTO,
                    TypeDTO.class);

            System.out.println("Deserialized RecordTypeDTO:\n" + deserialisedRecordTypeDTO);

            // ==== Computation exprs test =====

            System.out.println("\n==== Expressions tests =====\n");

            ComputationExprDTO computationExprDTO;
            {

                var nestedParams = List.of(new RecordExprDTO.FieldDTO("p1",
                                new BoolLiteralDTO(true)),
                        new RecordExprDTO.FieldDTO("p2",
                                new BinaryOpExprDTO(new IntLiteralDTO(3), new IntLiteralDTO(4),
                                        BinaryOpExprDTO.OpTypeDTO.INT_EQ)));
                var params = List.of(new RecordExprDTO.FieldDTO("param1",
                                new BoolLiteralDTO(false)),
                        new RecordExprDTO.FieldDTO("param2",
                                new RecordExprDTO(nestedParams)));

                computationExprDTO = new RecordExprDTO(params);
            }

            String serializedComputationExprDTO =
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
                            .writeValueAsString(computationExprDTO);

            System.out.println("Serialized ComputationExprDTO:\n" + serializedComputationExprDTO);

            ComputationExprDTO deserializedComputationExprDTO =
                    objectMapper.readValue(serializedComputationExprDTO,
                    ComputationExprDTO.class);

            System.out.println("Deserialized ComputationExprDTO:\n" + deserializedComputationExprDTO);


            // ==== Event test =====

            System.out.println("\n==== Event tests =====\n");

            EventDTO computationEventDTO;
            {
                var uid = "e0_0_tx";
                var id = "e0";
                computationEventDTO = new ComputationEventDTO(uid, id, intValuedMarkingDTO,
                        new BoolLiteralDTO(true), new BoolLiteralDTO(true), computationExprDTO);
            }

            String serializedEventDTO =
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
                            .writeValueAsString(computationEventDTO);

            System.out.println("Serialized EventDTO:\n" + serializedEventDTO);

            EventDTO deserializedEventDTO = objectMapper.readValue(serializedEventDTO,
                    EventDTO.class);

            System.out.println("Deserialized EventDTO:\n" + deserializedEventDTO);





            // String jsonLit = """
            //         {
            //           "literalExpr" : "bool"
            //         }
            //         """;
            //
            // ComputationExprDTO jsontest =
            //         objectMapper.readValue(jsonLit,
            //                 ComputationExprDTO.class);
            //
            //
            //
            // System.out.println("Deserialized ComputationExprDTO:\n" + jsontest);


            // ==== json String test =====
            //
            // System.out.println();
            //
            // String jsonTest = """
            //         {
            //           "recordTy" : [ {
            //             "fieldName" : "p1",
            //             "fieldType" : {
            //               "primitiveTy" : "string"
            //             }
            //           }, {
            //             "fieldName" : "p2",
            //             "fieldType" : {
            //               "refType" : "typing"
            //             }
            //           } ]
            //         }
            //         """;
            //
            // TypeDTO deserializedVal = objectMapper.readValue(jsonTest, TypeDTO.class);
            //
            // System.out.println("Deserialized jsonTest ValueDTO: " + deserializedVal);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
