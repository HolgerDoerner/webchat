let outputView = document.getElementById("chatOutput");
let inputField = document.getElementById("chatInput");
let submitButton = document.getElementById("submit");

let getNickname = () => {
    let nickname = window.location.href.split('?')[1];
    nickname = nickname.split('=')[1];
    return nickname;
}

let sendMsg = () => {
    outputView.value += '\n';
    outputView.value += '[' + new Date().toLocaleTimeString() + `] ${getNickname()}: ` + inputField.value;
    inputField.value = '';
}

inputField.onkeypress = e => {
    if (e.keyCode == 10) sendMsg();
}

submitButton.onclick = sendMsg;
