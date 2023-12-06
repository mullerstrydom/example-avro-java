package com.example.dto2.indexedrecord;

import com.example.dto2.Group;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.avro.Schema;
import org.apache.avro.generic.IndexedRecord;

public class IndexRecordGroup extends Group implements IndexedRecord {

    @Override
    public void put(int i, Object v) {
        switch(i){
            case 0: setId((String) v); break;
            case 1: setName((String) v); break;
            default: throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public Object get(int i) {
        return switch(i) {
            case 0 -> getId();
            case 1 -> getName();
            default -> throw new IndexOutOfBoundsException();
        };
    }

    @Override
    public Schema getSchema() {
        try {
            Path path = Path.of("generated."+ Group.class.getSimpleName() + ".avsc");
            return new Schema.Parser().parse(path.toFile());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
