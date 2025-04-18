package io.github.kanshanos.silent.sense.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;
import io.github.kanshanos.silent.sense.chain.data.SenseDataChain;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class SenseJsonGenerator extends JsonGeneratorDelegate {

    private String fieldName;
    private SerializableString _fieldName;
    private final SenseDataChain dataChain;


    public SenseJsonGenerator(JsonGenerator d, SenseDataChain dataChain) {
        super(d);
        this.dataChain = dataChain;
    }

    @Override
    public void writeFieldName(String name) throws IOException {
        this.fieldName = name;
        super.writeFieldName(name);
    }

    @Override
    public void writeFieldName(SerializableString name) throws IOException {
        this._fieldName = name;
        super.writeFieldName(name);
    }

    @Override
    public void writeString(String text) throws IOException {
        String name = StringUtils.firstNonBlank(fieldName, _fieldName.getValue());
        this.dataChain.process(name, text);
        super.writeString(text);
    }
}
