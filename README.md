SeleniumDOHRunner
=================

This is a Selenium script written in java to run your Dojo DOH tests. I  made this for my Jenkins environment.

Syntax
------
	java SeleniumDohRunner [-a] [-c=<jsocverContent>] [-h=<hubUrl>] [-p=<pluginString>] HostURL HostPort RunnerPath TestModule Path
example
	java SeleniumDohRunner -a -c=/jscoverage.html -h=http://localhost:4444/wd/hub -p=lmig/patches/doh/DohRunner;lmig/patches/doh/DohRunnerCI localhost 8080 /PmDojo/util/doh/runner.html com.lmig.modulename test,com.lmig.modulename

-a  : Async on. Defualt is false
-c=<jsocverContent>: indicates that jscover will beused by passing the relative url
-h=<hubUrl> : indicates that a selenium hub is in by by passing the selenium hub url
-p=<pluginString> : used to add mokey patches to the url
