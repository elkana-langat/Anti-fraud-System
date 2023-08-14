package antifraud.model;

public class Stolencard {

    private String number;

    public Stolencard() {
    }

    public Stolencard(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
