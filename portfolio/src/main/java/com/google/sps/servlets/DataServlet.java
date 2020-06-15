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

/** Servlet that returns some example content. TODO: modify this file to handle hardComments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
  private List<String> hardComments;
  private List<String> comments;

  @Override
  public void init() {
    hardComments = new ArrayList<>();
    hardComments.add("Here is a comment");
    hardComments.add("Here is another comment");
    hardComments.add("Here is one last comment");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // response.setContentType("text/html;");
    // response.getWriter().println("Hello Jeremy!");
    // String json = listToJson(hardComments);
    String json = listToJson(comments);
    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
  
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    // String com = request.getParameter("text-input");
    // if(com == null)
    //     com = "test";
    
    // comments.add(com);
    // response.sendRedirect("/index.html");

    String com = request.getParameter("com");
    long timestamp = System.currentTimeMillis();

    Entity comEntity = new Entity("comEntity");
    comEntity.setProperty("comment", com);
    comEntity.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(comEntity);

    response.sendRedirect("/index.html");
  }

  // Converts the arrayList into JSON format using GSON
  private String listToJson(List hardComments) {
    Gson gson = new Gson();
    String json = gson.toJson(hardComments);
    return json;
  }
}
