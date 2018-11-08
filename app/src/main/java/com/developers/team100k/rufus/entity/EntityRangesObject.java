package com.developers.team100k.rufus.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Richard Hrmo.
 */
public class EntityRangesObject {
  @Expose
  @SerializedName("offset")
  private int offset;
  @Expose
  @SerializedName("length")
  private int length;
  @Expose
  @SerializedName("key")
  private int key;

  public int getOffset() {
    return offset;
  }

  public int getLength() {
    return length;
  }

  public int getKey() {
    return key;
  }
}
