package data;

import lombok.Data;

import java.util.List;

@Data
public class Person {
    private String name;
    private Stats stats;
    private List<String> abilities;
    private List<String> equipment;
}
