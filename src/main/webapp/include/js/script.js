let outputView = document.getElementById("chatOutput");
let inputField = document.getElementById("chatInput");
let submitButton = document.getElementById("submit");

let nickname = window.location.href.split('?')[1];
nickname = nickname.split('=')[1];

let sendMsg = () => {
    outputView.value += '\n';
    outputView.value += '[' + new Date().toLocaleTimeString() + `] ${nickname}: ` + inputField.value;
    inputField.value = '';
    outputView.scrollTop = outputView.scrollHeight;
}

inputField.onkeypress = e => {
    if (e.keyCode == 10) sendMsg();
}

submitButton.onclick = sendMsg;
