package com.sellauto.storage;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageRepository {
	//create directory
	void init();
	
	//save the image appending the postId to the filename		
	void store(MultipartFile file,String postId);
	
	//fetch all image files
	Stream<Path> loadAll();
			
	Path load(String filename);
	
	//return the filename as a File.
	Resource loadAsResource(String filename);
			
	void deleteAll();
	
	//delete the file having postId
	void deleteFile(String post_id);
		}
