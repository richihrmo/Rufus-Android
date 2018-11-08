package com.developers.team100k.rufus.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Richard Hrmo.
 */
public class InlineStyleRangesObject {
  @SerializedName("offset")
  private int offset;
  @SerializedName("length")
  private int length;
  @SerializedName("style")
  private String style;
}
