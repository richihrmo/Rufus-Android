package com.developers.team100k.rufus.entity;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Richard Hrmo.
 */
public class Article {
  @SerializedName("entityMap")
  private Map<Long, EntityObject> mEntityMap;
  @SerializedName("blocks")
  private ArrayList<ContentBlock> mBlockArray;
}
