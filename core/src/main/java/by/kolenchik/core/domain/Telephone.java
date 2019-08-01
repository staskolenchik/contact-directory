package by.kolenchik.core.domain;

public class Telephone {

    private Long id;
    private Integer country;
    private Integer operator;
    private Integer number;
    private PhoneType phoneType;
    private String comment;

    public Telephone() {
    }

    public Telephone(Long id, Integer country, Integer operator, Integer number,
                     PhoneType phoneType, String comment) {
        this.id = id;
        this.country = country;
        this.operator = operator;
        this.number = number;
        this.phoneType = phoneType;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCountry() {
        return country;
    }

    public void setCountry(Integer country) {
        this.country = country;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Telephone{" +
                "id=" + id +
                ", country=" + country +
                ", operator=" + operator +
                ", number=" + number +
                ", phoneType=" + phoneType +
                ", comment='" + comment + '\'' +
                '}';
    }
}
