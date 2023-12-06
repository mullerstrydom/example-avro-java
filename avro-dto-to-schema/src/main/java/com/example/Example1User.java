package com.example;

import com.example.dto.UserDto;
import com.example.dto.indexrecord.IndexRecordUserDto;
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
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.DatumWriter;

public class Example1User {
    public static void main(String[] args) throws IOException {
        createAvroSchemaFromClass();
        createAvro();
    }

    public static void createAvro() throws IOException {
        UserDto user1 = new IndexRecordUserDto();
        user1.setSrc("generate-dto-to-schema");
        user1.setUsername("jane.doe");
        user1.setFirstName("Jane");
        user1.setLastName("Doe");

        UserDto user2 = new IndexRecordUserDto();
        user2.setSrc("generate-dto-to-schema");
        user2.setUsername("john.doe");
        user2.setFirstName("John");
        user2.setLastName("Doe");

        File file = new File("example1-dto.avro");

        Path path = Path.of("generated."+ UserDto.class.getSimpleName() + ".avsc");
        Schema schema = new Schema.Parser().parse(path.toFile());

        DatumWriter<UserDto> datumWriter = new GenericDatumWriter<>(schema);
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