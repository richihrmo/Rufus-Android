package com.developers.team100k.rufus.profile;

import android.net.Uri;

/**
 * Created by Richard Hrmo.
 */
public class UserProfile {

  private String personId;
  private String personName;
  private String personEmail;
  private Uri personPhoto;

  public UserProfile(String personId, String personName, String personEmail,
      Uri personPhoto) {
    this.personId = personId;
    this.personName = personName;
    this.personEmail = personEmail;
    this.personPhoto = personPhoto;
  }

  public String getPersonId() {
    return personId;
  }

  public String getPersonName() {
    return personName;
  }

  public String getPersonEmail() {
    return personEmail;
  }

  public Uri getPersonPhoto() {
    return personPhoto;
  }
}
