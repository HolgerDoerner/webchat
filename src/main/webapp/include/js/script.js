let outputView = document.getElementById("chatOutput");
let inputField = document.getElementById("chatInput");
let submitButton = document.getElementById("submit");

let decodedCookie = decodeURIComponent(document.cookie);
let cookie = decodedCookie.split(';');
let nickname = cookie[0].split('=')[1];

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
