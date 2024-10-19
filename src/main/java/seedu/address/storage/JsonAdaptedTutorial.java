package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Jackson-friendly version of {@link Tutorial}.
 */
public class JsonAdaptedTutorial {
    private final String name;
    /**
     * Constructs a {@code JsonAdaptedTutorial} with the given tutorial details.
     */
    @JsonCreator
    public JsonAdaptedTutorial(@JsonProperty("name") String name) {
        this.name = name;
    }
    //    TODO
    //    public Tutorial toModelType() throws IllegalValueException {
    //        return new Tutorial(this.name);
    //    }

    @Override
    public String toString() {
        return this.name;
    }
}
