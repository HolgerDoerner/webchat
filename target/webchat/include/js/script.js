let output = document.getElementById("chatOutput");
let input = document.getElementById("chatInput");
let submit = document.getElementById("submit");

submit.onclick = () => {
    output.value += '\n';
    output.value += '[' + new Date().toLocaleTimeString() + '] Knochen83: ' + input.value;
    input.value = '';
}
