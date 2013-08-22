SeleniumDOHRunner
=================

This is a Selenium script written in java to run your Dojo DOH tests. I  made this for my Jenkins environment. I originally made it very specific for my environment but i have been slowly working to make it more generic for everyone else to use as well. My environment is running Jenkins, jscover, selenium hub, and a custom plugin to the doh testharness. However my intent is to remove any dependancy to those. Feel free to contribute and/or submit issues/bug/enhancments.

Syntax
------

	java SeleniumDohRunner [-a] [-c=<jsocverContent>] [-h=<hubUrl>] [-p=<pluginString>] [-b=<browser>]HostURL HostPort RunnerPath TestModule Path

example

	java SeleniumDohRunner -a -c=jscoverage.html -h=http://localhost:4444/wd/hub -p=lmig/patches/doh/DohRunner;lmig/patches/doh/DohRunnerCI -b=firefox localhost 8080 PmDojo/util/doh/runner.html com.lmig.modulename test,com.lmig.modulename

	-a  : Async on. Defualt is false
	-b=<browser : if you have a preference on the browser to run on
	-x : this is used to add a custome firefox profile that turns off the unresponsive script dialog in firefox. In the future id like a way to pass this in a better way.
	-c=<jsocverContent>: indicates that jscover will beused by passing the relative url
	-h=<hubUrl> : indicates that a selenium hub is in by by passing the selenium hub url
	-p=<pluginString> : used to add mokey patches to the url

TODO
-----
- There is currently a dependancy on a plugin that adds a "loaded" class to the DOM to indicate the test harness, tests, andrequred files loaded correctly
- There is currently a dependancy on a plugin that adds a div where we dump the browser console too
- add a option attribute to the CLI that accepts 1...n key value pairs for firefox profile options
- passing in a timeout configuration
- implement non selenium hub. options are in place but really only focused on selenium hub implementation