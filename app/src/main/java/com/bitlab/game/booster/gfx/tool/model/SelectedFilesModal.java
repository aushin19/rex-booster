package com.bitlab.game.booster.gfx.tool.model;

import com.bitlab.game.booster.gfx.tool.databinding.GfxFilesListBinding;

public class SelectedFilesModal {
    public String FileName, Title;
    public boolean isBackup;
    public GfxFilesListBinding binding;

    public SelectedFilesModal(String fileName, String title, boolean isBackup, GfxFilesListBinding binding) {
        this.FileName = fileName;
        this.Title = title;
        this.isBackup = isBackup;
        this.binding = binding;
    }

}
