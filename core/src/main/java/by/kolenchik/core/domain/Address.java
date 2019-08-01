package by.kolenchik.core.domain;

public class Address {

    private Long id;
    private Long contactId;
    private String country;
    private String city;
    private String street;
    private String building;
    private String apartment;
    private String index;

    public Address() {
    }



    public Address(String country, String city, String street, String building, String apartment, String index) {
        this.country = country;
        this.city = city;
        this.street = street;
        this.building = building;
        this.apartment = apartment;
        this.index = index;
    }

    public Address(Long id, String country, String city, String street,
                   String building, String apartment, String index) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.street = street;
        this.building = building;
        this.apartment = apartment;
        this.index = index;
    }

    public Address(Long id, Long contactId, String country, String city, String street, String building, String apartment, String index) {
        this.id = id;
        this.contactId = contactId;
        this.country = country;
        this.city = city;
        this.street = street;
        this.building = building;
        this.apartment = apartment;
        this.index = index;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
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
        return "Address{" +
                "id=" + id +
                ", contactId=" + contactId +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", building='" + building + '\'' +
                ", apartment='" + apartment + '\'' +
                ", index='" + index + '\'' +
                '}';
    }
}
