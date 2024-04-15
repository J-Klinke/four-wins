package fourwins.model;

import java.awt.Color;

public record Player(String name, Color color) {

    @Override
    public String toString() {
        return "name='" + name + '\'' + ", color=" + color;
    }
}
