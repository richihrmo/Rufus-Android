package com.developers.team100k.rufus.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Richard Hrmo.
 */
class EntityObject {
  @SerializedName("type")
  private String type;
  @SerializedName("mutability")
  private String mutability;
  @SerializedName("data")
  private DataObject data;
}
