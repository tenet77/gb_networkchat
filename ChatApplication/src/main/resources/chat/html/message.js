// from Java
var jsConnector = {
    showResult: function(result) {
    },
    showMessage: function(msg) {
        addMessage(msg);
    }
}

function addMessage(msg) {
    let container = document.createElement("div");
    container.classList.add("container");
    let p = document.createElement("p");
    p.innerHTML = msg;
    p.classList.add("text-left");

    let s = document.createElement("span");
    let currDate = new Date();
    s.innerHTML = currDate.getHours().toString() + " : " + currDate.getMinutes().toString();
    s.classList.add("time-right");
    container.appendChild(p);
    container.appendChild(s);

    document.body.appendChild(container);

    console.log(document.body.offsetHeight);
    console.log(document.body.clientHeight);

    if (document.body.offsetHeight > window.innerHeight) {
        window.scrollTo(document.body.scrollTop, document.body.offsetHeight); 
    }
}

function getJsConnector() {
    return jsConnector;
}

// to Java
function sendToJava() {
}

// Event Handler
function handleLogin() {
    sendToJava();
}

function loadPage() {
}
