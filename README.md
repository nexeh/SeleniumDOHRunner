SeleniumDOHRunner
=================

This is a Selenium script written in java to run your Dojo DOH tests. I  made this for my Jenkins environment.

Syntax
------
	java SeleniumDohRunner [-a <value>] [-c <jsocverContent>] [-h <hubContext>] [-p <pluginString>] HostURL HostPort RunnerPath TestModule
	
-a <value> : Async
-c <jsocverContent>: jscover information
-h <hubContext> : selenium hub
-p <pluginString> : used to add mokey patches to the url
