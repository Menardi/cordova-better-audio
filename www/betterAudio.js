var exec = require('cordova/exec');

module.exports = {
	play : function (audioId, success, fail) {
		console.log('JS: Playing ' + audioId);
		return cordova.exec(success, fail, "BetterAudio", "play", [audioId]);
	}
};