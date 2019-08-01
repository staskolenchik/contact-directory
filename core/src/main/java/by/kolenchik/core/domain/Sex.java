package by.kolenchik.core.domain;

public enum Sex {
    MALE("MALE"), FEMALE("FEMALE");
    private String sex;
    Sex(String sex) {
        this.sex = sex;
    }

    public String getSexStr() {
        return sex;
    }
}
