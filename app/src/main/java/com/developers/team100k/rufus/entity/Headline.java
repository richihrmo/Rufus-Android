package com.developers.team100k.rufus.entity;

/**
 * Created by Richard Hrmo.
 */
public class Headline {

  public String getTitle() {
    return title;
  }

  public String getSubtitle() {
    return subtitle;
  }

  private String title;
  private String subtitle;

  public Headline() {
  }

  public Headline(String title, String subtitle) {
    this.title = title;
    this.subtitle = subtitle;
  }
}
