package com.developers.team100k.rufus.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * Created by Richard Hrmo.
 */
public class ContentBlock {
  @Expose
  @SerializedName("key")
  private String key;
  @Expose
  @SerializedName("text")
  private String text;
  @Expose
  @SerializedName("type")
  private String type;
  @Expose
  @SerializedName("depth")
  private int depth;
  @Expose
  @SerializedName("inlineStyleRanges")
  private ArrayList<InlineStyleRangesObject> inlineStyleRanges;
  @Expose
  @SerializedName("entityRanges")
  private ArrayList<EntityRangesObject> entityRanges;
  @Expose
  @SerializedName("data")
  private DataObject data;

  public String getKey() {
    return key;
  }

  public String getText() {
    return text;
  }

  public String getType() {
    return type;
  }

  public int getDepth() {
    return depth;
  }

  public ArrayList<InlineStyleRangesObject> getInlineStyleRanges() {
    return inlineStyleRanges;
  }

  public ArrayList<EntityRangesObject> getEntityRanges() {
    return entityRanges;
  }

  public DataObject getData() {
    return data;
  }
}
