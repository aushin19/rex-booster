package com.bitlab.game.booster.gfx.tool.model;

public class FilesFeedModal {
    public String title, description, extension, link, fileName, gameVersion, type;

    public FilesFeedModal(String title, String description, String extension, String link, String fileName, String gameVersion, String type) {
        this.title = title;
        this.description = description;
        this.extension = extension;
        this.link = link;
        this.fileName = fileName;
        this.gameVersion = gameVersion;
        this.type = type;
    }
}
