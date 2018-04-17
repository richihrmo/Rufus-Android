package com.developers.team100k.rufus;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

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
