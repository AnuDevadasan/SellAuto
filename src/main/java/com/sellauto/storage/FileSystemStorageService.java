package com.sellauto.storage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.sellauto.exceptions.AllException;
import com.sellauto.exceptions.StorageFileNotFoundException;

@Service
public class FileSystemStorageService implements StorageRepository {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }
    
    //save the image appending the postId to the filename		
    public void store(MultipartFile file,String postId) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
       //append postId to the filename
        filename=filename.substring(0, filename.lastIndexOf("."))+"_"+postId+"."+filename.substring(filename.lastIndexOf(".")+1,filename.length()); 
        try {
        	//check if uploaded file is empty?
            if (file.isEmpty()) {
                throw new AllException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new AllException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            //check the content type of file is image?
            if (!file.getContentType().contains("image")) {
                throw new AllException("Failed to store non-image file " + filename);
            }
            //copy to the destination.
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new AllException("Failed to store file " + filename, e);
        }
    }
    
    @Override
    //load file and return the path
    public Stream<Path> loadAll() {
        try {
        	 return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                   
                    .map(path -> this.rootLocation.relativize(path));
        }
        catch (IOException e) {
            throw new AllException("Failed to read stored files", e);
        }

    }
    
    
    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    //get the filename and return as File
    public Resource loadAsResource(String filename) {
        try {
        
        	Path file = load(filename);
        	Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
    
    @Override
    //delete the image
    public void deleteFile(String post_id) {
       
        try {
         Files.walk(this.rootLocation, 1)
        .filter(path -> path.getFileName().toString().contains(post_id))  //get the file with PostId
        .map(Path::toFile)
        .forEach(File::delete);
        } catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    //create directory
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new AllException("Could not initialize storage", e);
        }
    }
}
