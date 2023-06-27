package com.onesys.gfx.tool.game.booster.model;

public class FilesFeedModal {
    public String title, description, extension, link, backupLink, fileName, gameVersion, type;

    public FilesFeedModal(String title, String description, String extension, String link, String backupLink, String fileName, String gameVersion, String type) {
        this.title = title;
        this.description = description;
        this.extension = extension;
        this.link = link;
        this.backupLink = backupLink;
        this.fileName = fileName;
        this.gameVersion = gameVersion;
        this.type = type;
    }
}
