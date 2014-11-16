# Better Audio for Cordova on Android

This is a simple plugin which improves how audio works in Cordova apps on Android. Currently its only purpose is to allow ducking of background music (i.e. lowering its volume so your audio is more audible).

Tested with Cordova 4.0.0

## Installation

	cordova plugin add https://github.com/Menardi/cordova-better-audio.git

## Usage

	window.plugins.betteraudio.play('www/audio/example.org')

The URI you pass to the plugin function should be relative to the root of your app's folder structure. Currently only files located here are supported.