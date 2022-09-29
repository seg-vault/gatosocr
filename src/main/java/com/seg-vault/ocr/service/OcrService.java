/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.seg-vault.ocr.service;
import com.seg-vault.ocr.storage.StorageService;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.spring.annotations.Recurring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import net.sourceforge.tess4j.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.sourceforge.tess4j.ITesseract.RenderedFormat;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class OcrService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final StorageService storageService;

    @Autowired
    public OcrService(StorageService storageService) {
            this.storageService = storageService;
    }
    /*
    @Job(name = "The sample job without variable")
    public void execute() {
        execute("Hello world!");
    }*/
    @Recurring(id = "convert-tmp-job", cron = "* * * * *")
    @Job(name = "Convert tmp directory")
    public void execute() {
        logger.info("Converting tmp Directory");
        try {
            List<Path> paths = storageService.loadAll("tmp").collect(Collectors.toList());
            if(paths.size() == 0){
                logger.info("nothing to convert...");
                return;
            }
            Path permPath = storageService.getPermanentLocation();
            logger.info("Setting perm path to: "+permPath.toString()); //permPath.toAbsolutePath().toString()
            logger.info(paths.toString());
            for(Path path : paths){
                logger.info("Converting: "+path.toString()+" ...");
                ITesseract tess = new Tesseract();
                List<RenderedFormat> format = new ArrayList<RenderedFormat>();
                format.add(RenderedFormat.PDF);
                String newName = permPath.toString() + '\\' + path.getFileName();
                logger.info("New Name: " + newName);
                tess.createDocuments(path.toString(), newName, format);//path.toAbsolutePath().toString()
                logger.info(path.toString()+" ...complete");
                logger.info("deleting path...");
                Files.delete(path);
            }
        } catch (Exception e) {
            logger.error("Error while executing sample job", e);
        } finally {
            logger.info("Sample job has finished...");
        }
    }

}