package com.developers.team100k.rufus.entity;

/**
 * Created by Richard Hrmo.
 */
public class Page {

  private String title;

  public String getTitle() {
    return title;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public String getArticle() {
    return article;
  }

  private String subtitle;
  private String article;

  public Page(String title, String subtitle, String article) {
    this.title = title;
    this.subtitle = subtitle;
    this.article = article;
  }
}
