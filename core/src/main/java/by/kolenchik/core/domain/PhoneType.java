package by.kolenchik.core.domain;

public enum PhoneType {
    HOME("HOME"), MOBILE("MOBILE");
    private String value;

    PhoneType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
