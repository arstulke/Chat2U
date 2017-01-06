var hostname, commands, interval, thread, run, messages;
onmessage = function(e) {
    var data = e.data;
    hostname = data.hostname;
    commands = data.commands;
    interval = data.interval;
    thread = data.thread;
    run = data.run;
    logger = data.logger;

    messages = [];


    var websocket = new WebSocket("ws://" + hostname + ":80/chat");
    websocket.onmessage = function(msg) {
        msg = msg.data;
        console.log("DEBUG Websocket[" + run + "." + thread + "] received message: \"" + msg + "\"");
        messages[messages.length] = msg;
    };
    websocket.onclose = function(err) {
        console.log(err);
    };

    sendCommand(commands, 0);
    function sendCommand(commands, index) {
        setTimeout(function() {
            var msg = commands[index];
            msg = msg.replace("${thread}", thread);

            console.log("DEBUG Websocket[" + run + "." + thread + "] sends message: \"" + msg + "\"");
            websocket.send(msg);

            if(index < commands.length - 1) {
                sendCommand(commands, index + 1);
            } else {
                websocket.close();
                self.postMessage({msg: "successfully", thread: thread, status: "OK", messages: messages});
            }
        }, interval);
    }
};