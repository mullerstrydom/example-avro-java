package com.example;

import com.example.dto.UserDto;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.fasterxml.jackson.dataformat.avro.jsr310.AvroJavaTimeModule;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.reflect.ReflectDatumWriter;

/**
 * Using ReflectDataWriter we can use normal Pojo's and don't need IndexRecords
 */
public class Example3User {
    public static void main(String[] args) throws IOException {
        createAvroSchemaFromClass();
        createAvro();
    }

    public static void createAvro() throws IOException {
        UserDto user1 = new UserDto();
        user1.setSrc("generate-from-dto");
        user1.setUsername("jane.doe");
        user1.setFirstName("Jane");
        user1.setLastName("Doe");

        UserDto user2 = new UserDto();
        user2.setSrc("generate-from-dto");
        user2.setUsername("john.doe");
        user2.setFirstName("John");
        user2.setLastName("Doe");

        File file = new File("example3-dto.avro");

        Path path = Path.of("generated."+ UserDto.class.getSimpleName() + ".avsc");
        Schema schema = new Schema.Parser().parse(path.toFile());

        DatumWriter<UserDto> datumWriter = new ReflectDatumWriter<>(schema);
        try(DataFileWriter<UserDto> dataFileWriter = new DataFileWriter<>(datumWriter)){
            dataFileWriter.create(schema, file);
            dataFileWriter.append(user1);
            dataFileWriter.append(user2);
        }
    }

    private static void createAvroSchemaFromClass() throws IOException {
        AvroMapper avroMapper = AvroMapper.builder()
                .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                .addModule(new AvroJavaTimeModule())
                .build();

        AvroSchemaGenerator gen = new AvroSchemaGenerator();

        avroMapper.acceptJsonFormatVisitor(UserDto.class, gen);
        AvroSchema schemaWrapper = gen.getGeneratedSchema();
        Schema avroSchema = schemaWrapper.getAvroSchema();
        String avroSchemaInJSON = avroSchema.toString(true);
        //Write to File
        Path fileName = Path.of("generated."+ UserDto.class.getSimpleName() + ".avsc");
        Files.writeString(fileName, avroSchemaInJSON);
    }
}