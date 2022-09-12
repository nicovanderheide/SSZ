package data;

import lombok.Data;

@Data
public class Commander extends Person {
    @Override
    public String toString() {
        return String.format("CMDR: %s %s", getName(), getAbilities());
    }
}
