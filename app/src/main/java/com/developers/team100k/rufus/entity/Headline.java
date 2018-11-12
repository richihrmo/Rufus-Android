package com.developers.team100k.rufus.entity;

/**
 * Created by Richard Hrmo.
 */
public class Headline {

  public String getAuthor() {
    return author;
  }

  public String getTitle() {
    return title;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public String getCategory() {
    return category;
  }

  public Boolean getFeatured() {
    return featured;
  }

  public Article getArticle() {
    return article;
  }

  private String author;
  private String title;
  private String subtitle;
  private String category;
  private Boolean featured;
  private Article article;

  public Headline(String author, String title, String subtitle, String category, Boolean featured,
      Article article) {
    this.author = author;
    this.title = title;
    this.subtitle = subtitle;
    this.category = category;
    this.featured = featured;
    this.article = article;
  }
}
