package data;

import lombok.Data;

import java.util.List;

@Data
public class Crew {
    private String name;
    private String type;
    private String ship;
    private String agenda;
    private String edge;

    private Commander commander;
    private List<Member> members;
}
