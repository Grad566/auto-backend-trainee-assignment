package dto;

import com.sun.tools.javac.Main;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import model.ShortUrl;


@Getter
@Setter
@AllArgsConstructor
public class MainPage {
    private String flash;
    private ShortUrl shortUrl;

    public MainPage(String flash) {
        this.flash = flash;
    }
}
