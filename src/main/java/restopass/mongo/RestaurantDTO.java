package restopass.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "restaurants")
public class RestaurantDTO {

    private String name;
    private String address;

    public RestaurantDTO(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
