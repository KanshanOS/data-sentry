package io.github.kanshanos.silent.sense.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class SenseJsonGenerator extends JsonGeneratorDelegate {

    private String fieldName;
    private SerializableString _fieldName;


    public SenseJsonGenerator(JsonGenerator d) {
        super(d);
    }

    @Override
    public void writeFieldName(String name) throws IOException {
        fieldName = name;
        super.writeFieldName(name);
    }

    @Override
    public void writeFieldName(SerializableString name) throws IOException {
        _fieldName = name;
        super.writeFieldName(name);
    }

    @Override
    public void writeString(String text) throws IOException {
        System.out.printf("字段名: %s, 输出值: %s%n", StringUtils.firstNonBlank(fieldName, _fieldName.getValue()), text);
        super.writeString(text);
    }
}
