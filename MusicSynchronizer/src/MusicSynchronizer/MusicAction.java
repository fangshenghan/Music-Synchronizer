package MusicSynchronizer;

public class MusicAction {
	
	public String type = "startmucic";
	public long musictime = 0;
	public long executeTime = System.currentTimeMillis();
	
	public MusicAction(String type, long musictime, long executeTime) {
		this.type = type;
		this.musictime = musictime;
		this.executeTime = executeTime;
	}
	
	public MusicAction(String type, long executeTime) {
		this.type = type;
		this.executeTime = executeTime;
	}

}
