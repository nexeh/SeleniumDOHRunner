public class Context {
	
	private String hostURL = "";
	private String hostPort = "";
	private String runnerPath = "";
	private String testModule = "";
	private boolean isAsync = false;
	private String hubUrl = "";
	private String jsocverContent = "";
	private String pluginString = "";
	
	public String getHostURL() {
		return hostURL;
	}
	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
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
	@Override
	public String toString() {
		return "Context [hostURL=" + hostURL + ", hostPort=" + hostPort
				+ ", runnerPath=" + runnerPath + ", testModule=" + testModule
				+ ", isAsync=" + isAsync + ", hubUrl=" + hubUrl
				+ ", jsocverContent=" + jsocverContent + ", pluginString="
				+ pluginString + "]";
	}
	
	
	
}
