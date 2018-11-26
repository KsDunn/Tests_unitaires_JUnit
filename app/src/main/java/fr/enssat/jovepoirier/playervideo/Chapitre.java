package fr.enssat.jovepoirier.playervideo;

import android.graphics.Color;

public class Chapitre {
    private String titre;
    private Integer position;
    private String url;

    public Chapitre() {
        this.titre = "Chapitre 1";
        this.position = 0;
        this.url = "https://en.wikipedia.org/wiki/Big_Buck_Bunny";
    }

    public Chapitre(String titre, Integer position, String url) {
        this.titre = titre;
        this.position = position;
        this.url = url;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

