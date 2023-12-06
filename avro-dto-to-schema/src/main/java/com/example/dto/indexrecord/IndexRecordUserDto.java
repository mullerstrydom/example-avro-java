package com.example.dto.indexrecord;

import com.example.dto.UserDto;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.avro.Schema;
import org.apache.avro.generic.IndexedRecord;

public class IndexRecordUserDto extends UserDto implements IndexedRecord {

    @Override
    public void put(int i, Object v) {
        switch(i) {
            case 0: setSrc((String) v); break;
            case 1: setUsername((String) v); break;
            case 2: setFirstName((String) v); break;
            case 3: setLastName((String) v); break;
            default: throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public Object get(int i) {
        return switch (i) {
            case 0 -> getSrc();
            case 1 -> getUsername();
            case 2 -> getFirstName();
            case 3 -> getLastName();
            default -> throw new IndexOutOfBoundsException();
        };
    }

    @Override
    public Schema getSchema() {
        try {
            Path path = Path.of("generated."+ UserDto.class.getSimpleName() + ".avsc");
            return new Schema.Parser().parse(path.toFile());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
