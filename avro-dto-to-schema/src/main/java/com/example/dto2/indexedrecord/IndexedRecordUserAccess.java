package com.example.dto2.indexedrecord;

import com.example.dto.UserDto;
import com.example.dto2.Group;
import com.example.dto2.UserAccess;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.apache.avro.Schema;
import org.apache.avro.generic.IndexedRecord;

public class IndexedRecordUserAccess extends UserAccess implements IndexedRecord {

    @Override
    public void put(int i, Object v) {
        switch (i) {
            case 0: setUserName((String) v); break;
            case 1: setGroups((List<Group>) v); break;
            default: throw new IndexOutOfBoundsException();
        }

    }

    @Override
    public Object get(int i) {
        return switch (i) {
            case 0 -> getUserName();
            case 1 -> getGroups();
            default -> throw new IndexOutOfBoundsException();
        };
    }

    @Override
    public Schema getSchema() {
        try {
            Path path = Path.of("generated."+ UserAccess.class.getSimpleName() + ".avsc");
            return new Schema.Parser().parse(path.toFile());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
