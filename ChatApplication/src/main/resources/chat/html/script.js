let form = document.getElementById("form");
let elLogin = document.getElementById("login");
let elPassword = document.getElementById("password");
let elMessage = document.getElementById("message");
let btnSubmit = document.getElementById("submit");

// from Java
var jsConnector = {
    showResult: function(result) {
        btnSubmit.setAttribute("visible", false);
        elMessage.innerText = result;
    }
}

function getJsConnector() {
    return jsConnector;
}

// to Java
function sendToJava() {
    let s = elLogin.value;
    javaConnector.toLowerCase(s);
}

// Event Handler
function handleLogin() {
    sendToJava();
}

elLogin.focus();
btnSubmit.addEventListener("click", handleLogin);
