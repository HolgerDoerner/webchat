let chatOutput;
let chatInput;
let userList;
let userList_legend;
let smileyPopup;
let imagePopup;
let urlPopup;

// for markdown-it provided over CDN
let markdownIt;

// read the cookie to get the username.
let decodedCookie = decodeURIComponent(document.cookie);
let cookie = decodedCookie.split(';');
let nickname = cookie[0].split('=')[1];

// if user is not logged in properly -> redirekt to login page
if (!nickname) window.location.replace('index.jsp');

// create the websocket-connection to the server-endpoint.
// TODO: change ws-adress bevor deploying to the server !!!!
//let socket = new WebSocket(`wss://10.100.5.15:8443/webchat/chat/${nickname}`); // production work
//let socket = new WebSocket(`wss://10.100.5.15:8446/webchat/chat/${nickname}`); // development work
let socket = new WebSocket(`wss://192.168.178.100:8446/webchat/chat/${nickname}`); // development home

// ------------------------------------------------------
// initializes the values when page is fully loaded.
// ------------------------------------------------------
window.onload = () => {

    // making markdown-it available (with plugin(s))
    markdownIt = window.markdownit().use(window.markdownitEmoji);

    chatOutput = document.getElementById('chatOutput');
    chatInput = document.getElementById('chatInput');
    userList = document.getElementById('userList');
    userList_legend = document.getElementById('userList-legend');
    smileyPopup = document.getElementById('smileyPopup-window');
    imagePopup = document.getElementById('imagePopup-window');
    urlPopup = document.getElementById('urlPopup-window');

    // setting initial font-size of the chat-output
    chatOutput.style.fontSize = '14px';
    document.getElementById('showActualFontSize').innerHTML = chatOutput.style.fontSize;

    chatInput.onkeypress = onKeyPress;

    // request permission to display desktop-notifications.
    //
    // TODO: replace this implementation with a propper Service-Worker in the future...
    //
    if (window.Notification || window.webkitNotifications || navigator.mozNotification && Notification.permission !== 'granted') {
        Notification.requestPermission();
    }
}

window.onfocus = () => {
    chatInput.focus();
}

// ------------------------------------------------------
// WebSocket-handler for inkomming messages.
// takes the JSON-string and parses it to an object.
// ------------------------------------------------------
socket.addEventListener('message', event => {
    let message = JSON.parse(event.data);

    switch (message.subject) {
        case 'userlist':
            updateUserlist(message.content);
            break;

        default:
            // process the incomming message and render HTML
            displayMessage(message);

            // auto-scroll for chat window
            chatOutput.scrollTop = chatOutput.scrollHeight;

            // display an desktop-notification on new messages and only if the chat-frontend doesn't has focus
            //
            // TODO: furture replacement with service-worker
            //
            if (Notification.name && (Notification.permission === 'granted') && message.from !== 'server' && message.from !== nickname && !document.hasFocus()) {
                let notification = new Notification(`[${message.timestamp}] ${message.from}`, { body: message.content.substring(0,31) });
                notification.onclick = event => {
                    event.preventDefault();
                    event.target.close();
                    window.focus();
                }

                notification.onshow = event => {
                    setTimeout(() => { notification.close() }, 10000); // currently buggy in Chrome :-/
                }
            }
            break;
    }
})

// ------------------------------------------------------
// WebSocket error-handler
// ------------------------------------------------------
socket.addEventListener('error', event => {

    // TODO: make something usefull here...
    alert('WebSocket connection error!');
})

// ------------------------------------------------------
// key-eventhandler for chat input box.
// sends message by pressing 'CTRL+ENTER'.
// ------------------------------------------------------
let onKeyPress = event => {
    if (event.keyCode == 13 && document.getElementById('selectSendMethod').checked == true) {
        sendMsg(null, null, null, null);
        event.preventDefault();
    }
}

// ------------------------------------------------------
// converts the message to JSON-string and sends it to the server
// over open WebSocket connection.
// ------------------------------------------------------
let sendMsg = (subject, from, to, content) => {

    if (chatInput.value.toLowerCase() === '/clear') {
        chatOutput.innerHTML = '';
        chatInput.value = '';
        //event.preventDefault();
        chatInput.focus();
    }
    else {
        let message = {
            timestamp: '',
            subject: '',
            from: '',
            to: '',
            content: ''
        }
    
        message.subject = subject ? subject : '';
        message.from = from ? from : nickname;
        message.to = to ? to : '';
        message.content = content ? content : chatInput.value;
    
        socket.send(JSON.stringify(message));
    
        chatInput.value = '';
        chatInput.focus();
    }
}

// ------------------------------------------------------
// renders the messsage on the output-area
// ------------------------------------------------------
let displayMessage = (message) => {

    let output = '<hr style="width: 100%; height: 20px; background-color: lightgrey; border: 0px; margin: 0px; padding: 0px">';
    output += `<b style="color: grey">Message from <i style="color: orangered">${message.from}</i> on <i>${message.timestamp}</i> :</b>`;
    output += '<div class="message">';
    
    output += markdownIt.render(message.content); // translate the markdown-syntax to HTML
    output += '</div>';

    chatOutput.innerHTML += output;
}

// ------------------------------------------------------
// updates the userlist.
// ------------------------------------------------------
let updateUserlist = list => {
    
    let users = list.split(';');
    users.pop(); // removes empty element at end of list

    userList_legend.innerHTML = `<img alt="Userlist" title="Userlist" src="include/img/users1-512x512.png" width="30px" height="30px" style="float: left">&nbsp;&nbsp;${users.length}`;

    userList.innerHTML = '';

    users.forEach(user => {
        userList.innerHTML += '<b>' + user + '</b><br>';
    })
}

// ------------------------------------------------------
// toggle display of the options-menu
// ------------------------------------------------------
let toggleOptions = () => {

    document.getElementById('optionsContent').classList.toggle('optionsContent-toggle');
}

// ------------------------------------------------------
// incrasing and decrasing the font-size of the output window
// ------------------------------------------------------
let changeFontSize = value => {

    chatOutput.style.fontSize = Number.parseInt(chatOutput.style.fontSize) + value + 'px';
    document.getElementById('showActualFontSize').innerHTML = chatOutput.style.fontSize;
}

//
// TODO: not needed anymore, will be removed soon
//
// ------------------------------------------------------
// render HTML-output for the viewport
// ------------------------------------------------------
// let textToHTML = text => {
//     let output = ''
//     let textArray = [];
//     textArray = text.split('\n');

//     textArray.forEach(line => {

//         // add HTML linebreaks
//         output += line + '<br>';
//     })

//     return output;
// }