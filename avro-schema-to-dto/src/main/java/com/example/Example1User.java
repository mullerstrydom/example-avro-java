package com.example;

import com.example.avroschema.UserDto;
import java.io.File;
import java.io.IOException;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.DatumWriter;

public class Example1User {
    public static void main(String[] args) throws IOException {
        createAvro();
    }

    public static void createAvro() throws IOException {
        UserDto user1 = new UserDto();
        user1.setUsername("jane.doe");
        user1.setFirstName("Jane");
        user1.setLastName("Doe");
        user1.setSrc("generate-schema-to-dto");

        UserDto user2 = new UserDto();
        user2.setUsername("john.doe");
        user2.setFirstName("John");
        user2.setLastName("Doe");
        user2.setSrc("generate-schema-to-dto");

        File file = new File("example1-schema.avro");

        DatumWriter<UserDto> datumWriter = new GenericDatumWriter<>(user1.getSchema());
        try(DataFileWriter<UserDto> dataFileWriter = new DataFileWriter<>(datumWriter)){
            dataFileWriter.create(user1.getSchema(), file);
            dataFileWriter.append(user1);
            dataFileWriter.append(user2);
        }
    }
}