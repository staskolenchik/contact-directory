package by.kolenchik.core.dto.address;

public class AddressSearchRequestDTO {

    private String country;
    private String city;
    private String street;
    private String building;
    private String apartment;
    private String index;

    public AddressSearchRequestDTO() {
    }

    public AddressSearchRequestDTO(String country, String city, String street,
                                   String building, String apartment, String index) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.building = building;
        this.apartment = apartment;
        this.index = index;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "AddressSearchRequestDTO{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", building='" + building + '\'' +
                ", apartment='" + apartment + '\'' +
                ", index='" + index + '\'' +
                '}';
    }
}
