package com.example;

import com.example.avroschema.GroupDto;
import com.example.avroschema.UserAccessDto;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.DatumWriter;

public class Example2UserAccess {

    public static void main(String[] args) throws IOException {
        createAvro();
    }

    public static void createAvro() throws IOException {
        UserAccessDto userAccessDto = new UserAccessDto();
        userAccessDto.setUserName("john.doe");
        userAccessDto.setGroups(new ArrayList<>());

        GroupDto group1 = new GroupDto();
        group1.setId("1");
        group1.setName("READ");
        userAccessDto.getGroups().add(group1);

        GroupDto group2 = new GroupDto();
        group2.setId("2");
        group2.setName("WRITE");
        userAccessDto.getGroups().add(group2);

        File file = new File("example2-schema.avro");

        DatumWriter<UserAccessDto> datumWriter = new GenericDatumWriter<>(userAccessDto.getSchema());
        try(DataFileWriter<UserAccessDto> dataFileWriter = new DataFileWriter<>(datumWriter)){
            dataFileWriter.create(userAccessDto.getSchema(), file);
            dataFileWriter.append(userAccessDto);
        }
    }
}
