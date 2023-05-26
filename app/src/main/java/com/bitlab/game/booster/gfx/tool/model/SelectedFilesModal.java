package com.bitlab.game.booster.gfx.tool.model;

import com.bitlab.game.booster.gfx.tool.adapters.FeedAdapter;

public class SelectedFilesModal {
    public String FileName, Title;
    public FeedAdapter.FeedViewHolder Holder;

    public SelectedFilesModal(String fileName, String title, FeedAdapter.FeedViewHolder holder) {
        FileName = fileName;
        Title = title;
        Holder = holder;
    }

}
