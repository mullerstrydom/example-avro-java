package com.example;

import com.example.dto2.Group;
import com.example.dto2.UserAccess;
import com.example.dto2.indexedrecord.IndexRecordGroup;
import com.example.dto2.indexedrecord.IndexedRecordUserAccess;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.avro.AvroMapper;
import com.fasterxml.jackson.dataformat.avro.AvroSchema;
import com.fasterxml.jackson.dataformat.avro.jsr310.AvroJavaTimeModule;
import com.fasterxml.jackson.dataformat.avro.schema.AvroSchemaGenerator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.reflect.ReflectDatumWriter;

public class Example2UserAccess {

    public static void main(String[] args) throws IOException {
        createAvroSchemaFromClass();
        createAvro();
    }

    private static void createAvroSchemaFromClass() throws IOException {
        AvroMapper avroMapper = AvroMapper.builder()
                .disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
                .addModule(new AvroJavaTimeModule())
                .build();

        AvroSchemaGenerator gen = new AvroSchemaGenerator();

        avroMapper.acceptJsonFormatVisitor(UserAccess.class, gen);
        AvroSchema schemaWrapper = gen.getGeneratedSchema();
        org.apache.avro.Schema avroSchema = schemaWrapper.getAvroSchema();
        String avroSchemaInJSON = avroSchema.toString(true);
        //Write to File
        Path fileName = Path.of("generated."+ UserAccess.class.getSimpleName() + ".avsc");
        Files.writeString(fileName, avroSchemaInJSON);
    }

    public static void createAvro() throws IOException {
        UserAccess userAccessDto = new IndexedRecordUserAccess();
        userAccessDto.setUserName("john.doe");
        userAccessDto.setGroups(new ArrayList<>());

        Group group1 = new IndexRecordGroup();
        group1.setId("1");
        group1.setName("READ");
        userAccessDto.getGroups().add(group1);

        Group group2 = new IndexRecordGroup();
        group2.setId("2");
        group2.setName("WRITE");
        userAccessDto.getGroups().add(group2);

        File file = new File("example2-dto.avro");

        Path path = Path.of("generated."+ UserAccess.class.getSimpleName() + ".avsc");
        Schema schema = new Schema.Parser().parse(path.toFile());

        DatumWriter<UserAccess> datumWriter = new ReflectDatumWriter<>(schema);
        try(DataFileWriter<UserAccess> dataFileWriter = new DataFileWriter<>(datumWriter)){
            dataFileWriter.create(schema, file);
            dataFileWriter.append(userAccessDto);
        }
    }
}
