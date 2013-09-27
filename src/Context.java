public class Context {
	
	private String host = "";
	private String hostPort = "";
	private String runnerPath = "";
	private String testModule = "";
	private boolean isAsync = false;
	private String hubUrl = "";
	private String jsocverContent = "";
	private String pluginString = "";
	private String path = "";
	private String browser = "firefox";
	private boolean customFirefoxProfile = false;
	private boolean usingLoadedPlugin = true;
	private boolean usingConsoleLogPlugin = true;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getHostPort() {
		return hostPort;
	}
	public void setHostPort(String hostPort) {
		this.hostPort = hostPort;
	}
	public String getRunnerPath() {
		return runnerPath;
	}
	public void setRunnerPath(String runnerPath) {
		this.runnerPath = runnerPath;
	}
	public String getTestModule() {
		return testModule;
	}
	public void setTestModule(String testModule) {
		this.testModule = testModule;
	}
	public boolean isAsync() {
		return isAsync;
	}
	public void setAsync(boolean isAsync) {
		this.isAsync = isAsync;
	}
	public String getHubUrl() {
		return hubUrl;
	}
	public void setHubUrl(String hubUrl) {
		this.hubUrl = hubUrl;
	}
	public String getJsocverContent() {
		return jsocverContent;
	}
	public void setJsocverContent(String jsocverContent) {
		this.jsocverContent = jsocverContent;
	}
	public String getPluginString() {
		return pluginString;
	}
	public void setPluginString(String pluginString) {
		this.pluginString = pluginString;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	
	public boolean isCustomFirefoxProfile() {
		return customFirefoxProfile;
	}
	public void setCustomFirefoxProfile(boolean customFirefoxProfile) {
		this.customFirefoxProfile = customFirefoxProfile;
	}
	
	public boolean isUsingLoadedPlugin() {
		return usingLoadedPlugin;
	}
	public void setUsingLoadedPlugin(boolean usingLoadedPlugin) {
		this.usingLoadedPlugin = usingLoadedPlugin;
	}
	public boolean isUsingConsoleLogPlugin() {
		return usingConsoleLogPlugin;
	}
	public void setUsingConsoleLogPlugin(boolean usingConsoleLogPlugin) {
		this.usingConsoleLogPlugin = usingConsoleLogPlugin;
	}

	@Override
	public String toString() {
		return "Context [host=" + host + ", hostPort=" + hostPort
				+ ", runnerPath=" + runnerPath + ", testModule=" + testModule
				+ ", isAsync=" + isAsync + ", hubUrl=" + hubUrl
				+ ", jsocverContent=" + jsocverContent + ", pluginString="
				+ pluginString + ", path=" + path + ", browser=" + browser
				+ ", customFirefoxProfile=" + customFirefoxProfile
				+ ", usingLoadedPlugin=" + usingLoadedPlugin
				+ ", usingConsoleLogPlugin=" + usingConsoleLogPlugin + "]";
	}
	public boolean isUsingSeleniumHub() {
		
		if(!this.hubUrl.isEmpty()) {
			return true;
		}
		
		return false;
	}
	public boolean isUsingJscover() {
		if(!this.jsocverContent.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public boolean isUsingPlugins() {
		if(!this.pluginString.isEmpty()) {
			return true;
		}
		return false;
	}
}
