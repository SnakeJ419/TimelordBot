package Preferences;

public class ServerDataStructure {
	public final String serverID;

	//listingOfTimezones
	public boolean listTimezones = true;

	protected ServerDataStructure(String serverID){
		this.serverID = serverID;
	}
}
