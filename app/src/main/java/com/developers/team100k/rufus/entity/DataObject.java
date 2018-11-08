package com.developers.team100k.rufus.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Richard Hrmo.
 */
public class DataObject {
  @Expose
  @SerializedName("url")
  private String url;

  public String getUrl() {
    return url;
  }
}
