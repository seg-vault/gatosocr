/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.seg-vault.ocr.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	void store(MultipartFile file, String label);

	Stream<Path> loadAll(String label);
        
        Stream<Path> serveAll(String label);

	Path load(String filename, String label);

	Resource loadAsResource(String filename, String label);

	void deleteAll(String label);
        
        Path getPermanentLocation();
        
        Path getStorageLocation(String label);

}
