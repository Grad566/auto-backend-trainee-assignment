package model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Url {
    private long id;
    private String name;
    private String createdAt;

    public Url(String name) {
        this.name = name;
    }
}
