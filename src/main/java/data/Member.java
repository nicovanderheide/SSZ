package data;

import lombok.Data;

@Data
public class Member extends Person {
    private String type;
    @Override
    public String toString() {
        return String.format("%s (%s)", getName(), type);
    }

}
