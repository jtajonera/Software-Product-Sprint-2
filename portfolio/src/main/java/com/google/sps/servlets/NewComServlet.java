// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Posts new comments to server*/
@WebServlet("/new-comment")
public class NewComServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String com = request.getParameter("com");
    long timestamp = System.currentTimeMillis();
    float sentimentScore = getSentiment(com);
    Entity comEntity = new Entity("comEntity");
    comEntity.setProperty("comment", com);
    comEntity.setProperty("timestamp", timestamp);
    comEntity.setProperty("sentiment", sentimentScore);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(comEntity);
    response.sendRedirect("/index.html");
  }

  // Returns the sentiment for a given message
  public float getSentiment(String message) throws IOException {
    Document doc = Document.newBuilder().setContent(message).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();
    languageService.close();
    return score;
  }
}
