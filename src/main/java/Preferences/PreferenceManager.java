package Preferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

public class PreferenceManager {
	private static ArrayList<ServerDataStructure> serverDataList = null;

	static{
		Gson gson = new Gson();
		Type collectionType = new TypeToken<Collection<ServerDataStructure>>(){}.getType();
		try {
			serverDataList = gson.fromJson(new FileReader(new File("src\\main\\resources\\Preferences.json")), collectionType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static ServerDataStructure getData(String serverID){
		for(ServerDataStructure data : serverDataList){
			if(serverID.equals(data.serverID)){
				return data;
			}
		}

		//if data for server does not exist, make and write data for server
		ServerDataStructure data = new ServerDataStructure(serverID);
		serverDataList.add(data);
		writeData();
		return data;
	}

	public static void writeData(){
		Gson gson = new Gson();
		String serializedData = gson.toJson(serverDataList);
		File file = new File("src\\main\\resources\\Preferences.json");
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(serializedData);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

//	public static void main(String[] args) throws IOException {
//		Gson gson = new Gson();
//		Collection<ServerDataStructure> dataStructures = new ArrayList<>();
//		dataStructures.add(new ServerDataStructure("1"));
//		dataStructures.add(new ServerDataStructure("2"));
//
//		String json = gson.toJson(dataStructures);
//		File file = new File("src\\main\\resources\\Preferences.json");
//		file.createNewFile();
//		FileWriter writer = new FileWriter(file);
//		writer.write(json);
//		writer.close();
//
//		Type collectionType = new TypeToken<Collection<ServerDataStructure>>(){}.getType();
//		Collection<ServerDataStructure> parsedCollection = gson.fromJson(new FileReader(new File("src\\main\\resources\\Preferences.json")), collectionType);
//
//		for(ServerDataStructure data : parsedCollection){
//			System.out.println(Arrays.toString(data.ints));
//		}
//	}

}
