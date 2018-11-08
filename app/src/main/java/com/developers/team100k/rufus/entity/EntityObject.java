package com.developers.team100k.rufus.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Richard Hrmo.
 */
public class EntityObject {
  @Expose
  @SerializedName("type")
  private String type;
  @Expose
  @SerializedName("mutability")
  private String mutability;
  @Expose
  @SerializedName("data")
  private DataObject data;

  public String getType() {
    return type;
  }

  public String getMutability() {
    return mutability;
  }

  public DataObject getData() {
    return data;
  }
}
