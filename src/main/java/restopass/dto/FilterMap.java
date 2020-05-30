package restopass.dto;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "map_filters")
public class FilterMap {
    private String name;
    private List<String> elements;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }
}
