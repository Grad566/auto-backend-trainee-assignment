package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ShortUrl {
    private long id;
    private String name;
    private long originUrlId;
    private String createdAt;

    public ShortUrl(Long originUrlId, String name) {
        this.name = name;
        this.originUrlId = originUrlId;
    }
}
