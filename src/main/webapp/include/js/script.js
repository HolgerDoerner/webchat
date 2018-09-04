let chatOutput = null;
let chatInput = null;

window.onload = () => {
    chatOutput = document.getElementById("chatOutput");
    chatInput = document.getElementById("chatInput");
}

let decodedCookie = decodeURIComponent(document.cookie);
let cookie = decodedCookie.split(';');
let nickname = cookie[0].split('=')[1];

let socket = new WebSocket(`ws://10.100.5.15/webchat/chat/${nickname}`);

socket.onmessage = event => {
    let message = JSON.parse(event.data);
    chatOutput.value += '\n' + '[' + (new Date().toLocaleTimeString()) + '] ' + message.from + ': ' + message.content;
}

let sendMsg = () => {
    let message = {
        from: '',
        to: '',
        content: ''
    }

    message.from = nickname;
    message.content = chatInput.value;

    socket.send(JSON.stringify(message));

    chatInput.value = '';
    chatInput.focus();
}
