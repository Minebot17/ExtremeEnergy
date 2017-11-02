package ru.minebot.extreme_energy.gui.tablet;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class LinkNet extends Link {

    public LinkNet(float posX, float posY, String text, String url, Align align, float width) {
        super(posX, posY, text, url, align, width);
    }

    @Override
    public void action() {
        try {
            URI uri = new URI(url);
            java.awt.Desktop.getDesktop().browse(uri);

        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        }
    }
}
