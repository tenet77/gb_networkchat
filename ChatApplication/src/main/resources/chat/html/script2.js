let user_chatEl = document.getElementById("user_chat");
let user_listEl = document.getElementById("user-list");

function showUserList() {
    console.log("dfdf");
    user_listEl.classList.remove("user-panel-hide");
}

// Main
user_chatEl.addEventListener("click", showUserList);