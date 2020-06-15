package com.google.sps.data;

/** The structure for comments */
public final class Comment {

  private final long id;
  private final String comment;
  private final long timestamp;

  public Comment(long id, String com, long timestamp) {
    this.id = id;
    this.comment = com;
    this.timestamp = timestamp;
  }
}