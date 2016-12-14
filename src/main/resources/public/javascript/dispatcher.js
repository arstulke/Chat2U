function Dispatcher() {
	var dispatcher = {
		types: {},
		createType: function(type, handler) {
			this.types[type] = handler;
		},
		runType: function(msg){
			this.types[msg.type](msg);
		}
	}
	return dispatcher;
}