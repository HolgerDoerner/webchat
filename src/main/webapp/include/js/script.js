let chatOutput = null;
let chatInput = null;
let userlist = null;
let userList_legend = null;
let smileyPopup = null;

// initializes the values when page is fully loaded.
window.onload = () => {
    chatOutput = document.getElementById('chatOutput');
    chatInput = document.getElementById('chatInput');
    userlist = document.getElementById('userlist');
    userList_legend = document.getElementById('userList-legend');
    smileyPopup = document.getElementById('smileyPopup-window');

    if (window.Notification || window.webkitNotifications || navigator.mozNotification && Notification.permission !== 'granted') {
        Notification.requestPermission();
    }
}

window.onfocus = () => {
    chatInput.focus();
}

// read the cookie to get the username.
let decodedCookie = decodeURIComponent(document.cookie);
let cookie = decodedCookie.split(';');
let nickname = cookie[0].split('=')[1];

// create the websocket-connection to the server-endpoint.
// TODO: change ws-adress bevor deploying to the server !!!!
//let socket = new WebSocket(`wss://10.100.5.15:8443/webchat/chat/${nickname}`); // production work
//let socket = new WebSocket(`wss://10.100.5.15:8446/webchat/chat/${nickname}`); // development work
let socket = new WebSocket(`wss://192.168.178.100:8446/webchat/chat/${nickname}`); // development home

// handler for inkomming messages.
// takes the JSON-string and parses it to an object.
socket.onmessage = event => {
    let message = JSON.parse(event.data);

    switch (message.subject) {
        case "userlist":
            updateUserlist(message.content);
            break;

        default:
            chatOutput.value += '\n' + '[' + (new Date().toLocaleTimeString()) + '] ' + message.from + ': ' + message.content;
            chatOutput.scrollTop = chatOutput.scrollHeight;

            if (Notification.name && (Notification.permission === 'granted') && message.from !== 'server' && message.from !== nickname && !document.hasFocus()) {
                let notification = new Notification(`${message.from}`, { body: message.content.substring(0,21) });
                notification.onclick = event => {
                    //event.preventDefault();
                    //window.focus();
                    parent.focus();
                    event.target.close();
                }

                // notification.onshow = event => {
                //     setTimeout(notification.close(), 4000);
                // }

                // notification.addEventListener('show', event => {
                //     setTimeout(notification.close(), 4000);
                // })

                // notification.addEventListener('click', event => {
                //     parent.focus();
                //     event.target.close();
                // })
            }
            break;
    }
}

// key-eventhandler for chat input box.
// sends message by pressing 'CTRL+ENTER'.
let onKeyDown = event => {

    if (event.ctrlKey && event.keyCode == 13) {
        sendMsg();
    }
}

// converts the message to JSON-string and sends it to the server
// over open WebSocket connection.
let sendMsg = () => {

    let message = {
        subject: '',
        from: '',
        to: '',
        content: ''
    }

    message.subject = '';
    message.from = nickname;
    message.to = '';
    message.content = chatInput.value;

    socket.send(JSON.stringify(message));

    chatInput.value = '';
    chatInput.focus();
}

// updates the userlist.
let updateUserlist = list => {
    
    let users = list.split(';');
    users.pop(); // removes empty element at end of list

    // write new header with updated usercount
    if (userList_legend.hasChildNodes()) {
        userList_legend.removeChild(userList_legend.firstChild);
    }
    
    let node = document.createElement('b');
    let textNode = document.createTextNode('Users (' + users.length + ')');
    node.appendChild(textNode);
    userList_legend.appendChild(node);

    userlist.value = '';

    users.forEach(user => {
        userlist.value += user + '\n';
    });
}

// handler for opening the smiley-popup window.
let openSmileyPopup = () => {
    smileyPopup.classList.toggle('show');
}

// handler for adding the selected smiley to the inputbox.
let addSmiley= (smileyId) => {
    let smiley = document.getElementById(smileyId).innerHTML;
    chatInput.value += smiley;
    chatInput.focus();
}
