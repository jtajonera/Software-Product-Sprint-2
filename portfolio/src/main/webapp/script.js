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

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

/**
 * Adds a random fact to the page.
 */
function addRandomFact() {
  const facts =
      ['I am a rising junior', 
       'I love to cook', 
       'Camping is very fun', 
       'I can play the Bb Clarinet'];

  // Picks fact
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fact;
}

// Loads the comments onto the page on load
function loadComments() {
  fetch('/list-com').then(response => response.json()).then((coms) => {
    const comContainer = document.getElementById('comment-container');
    for(const com of coms) {
      comContainer.appendChild(createList(com));
    }
  });
}

//Creates a list element for each comment
function createList(com) {
  const comLi = document.createElement('li');
  comLi.className = 'comList';

  const comData = document.createElement('span');
  comData.innerText = com.comment + " ";

  const deleteBut = document.createElement('button');
  deleteBut.innerText = 'Delete Comment';
  deleteBut.addEventListener('click', () => {
    deleteComment(com);

    // Remove the task from the DOM.
    comLi.remove();
  });

  comLi.appendChild(comData);
  comLi.appendChild(deleteBut);
  return comLi;
}

// Deletes the comment from the server
function deleteComment(task) {
  const params = new URLSearchParams();
  params.append('id', task.id);
  fetch('/delete-comment', {method: 'POST', body: params});
}



