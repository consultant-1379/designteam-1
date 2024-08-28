package com.ericsson.oss.eniq.dataingress.service.outputstream;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoadFileRepo {
	
	private static final Map<String, ILoadFile> fileRepo = new ConcurrentHashMap<>();
	
	private LoadFileRepo() {
		
	}
	
	public static ILoadFile getLoadFile(String outDir, String folderName, int THRESHOLD_NUMBER){
		ILoadFile loadFile = fileRepo.get(folderName);
		if (loadFile == null) {
			loadFile = new LoadFile(outDir, folderName, THRESHOLD_NUMBER);
			fileRepo.put(folderName, loadFile);
		}
		return loadFile;
	}

}
