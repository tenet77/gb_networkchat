const msgerChat = get(".msger-chat");

// Add message
function appendMessage(name, side, text) {
    //   Simple solution for small apps
    const msgHTML = `
      <div class="msg ${side}-msg">
        <div class="msg-bubble">
          <div class="msg-info">
            <div class="msg-info-name">${name}</div>
            <div class="msg-info-time">${formatDate(new Date())}</div>
          </div>
  
          <div class="msg-text">${text}</div>
        </div>
      </div>
    `;
  
    msgerChat.insertAdjacentHTML("beforeend", msgHTML);
    msgerChat.scrollTop += 500;
  }
  
  // Utils
function get(selector, root = document) {
    return root.querySelector(selector);
}

function formatDate(date) {
    const h = "0" + date.getHours();
    const m = "0" + date.getMinutes();
  
    return `${h.slice(-2)}:${m.slice(-2)}`;
  }

// Java
function getJsConnector() {
    return jsConnector;
}

var jsConnector = {
    showResult: function(result) {
    },
    showMessage: function(name, side, text) {
        appendMessage(name, side, text);
    }
}

function sendToJava() {
}

// Event Handler
function handleLogin() {
    sendToJava();
}

function loadPage() {
}
