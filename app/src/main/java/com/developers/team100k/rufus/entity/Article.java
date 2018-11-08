package com.developers.team100k.rufus.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Richard Hrmo.
 */
public class Article {
  @Expose
  @SerializedName("entityMap")
  private Map<Long, EntityObject> mEntityMap;
  @Expose
  @SerializedName("blocks")
  private ArrayList<ContentBlock> mBlockArray;

  public Map<Long, EntityObject> getEntityMap() {
    return mEntityMap;
  }

  public ArrayList<ContentBlock> getBlockArray() {
    return mBlockArray;
  }
}
