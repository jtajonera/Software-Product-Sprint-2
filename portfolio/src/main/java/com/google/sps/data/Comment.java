package com.google.sps.data;

/** The structure for comments */
public final class Comment {

  private final long id;
  private final String comment;
  private final long timestamp;
  private final Double sentiment;

  // Constructor for the Comment Class, assigns variables to the object
  public Comment(long id, String comment, long timestamp, Double sentiment) {
    this.id = id;
    this.comment = comment;
    this.timestamp = timestamp;
    this.sentiment = sentiment;
  }
}