package com.google.sps.data;

/** The structure for comments */
public final class Comment {

  private final long id;
  private final String comment;
  private final long timestamp;

  // Constructor for the Comment Class, it takes in a long ID and timestap and a String comment
  public Comment(long id, String comment, long timestamp) {
    this.id = id;
    this.comment = comment;
    this.timestamp = timestamp;
  }
}