package com.developers.team100k.rufus;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * Created by Richard Hrmo.
 */
class ContentBlock {
  @SerializedName("key")
  private String key;
  @SerializedName("text")
  private String text;
  @SerializedName("type")
  private String type;
  @SerializedName("depth")
  private int depth;
  @SerializedName("inlineStyleRanges")
  private ArrayList<InlineStyleRangesObject> inlineStyleRanges;
  @SerializedName("entityRanges")
  private ArrayList<EntityRangesObject> entityRanges;
  @SerializedName("data")
  private DataObject data;
}
