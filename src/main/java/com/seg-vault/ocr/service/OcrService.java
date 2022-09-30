/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.seg-vault.ocr.service;
import com.seg-vault.ocr.entity.WorkItem;
import com.seg-vault.ocr.repository.WorkItemRepository;
import com.seg-vault.ocr.storage.StorageService;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.spring.annotations.Recurring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import net.sourceforge.tess4j.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;
import net.sourceforge.tess4j.ITesseract.RenderedFormat;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class OcrService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final StorageService storageService;
    private final WorkItemRepository repository;

    @Autowired
    public OcrService(StorageService storageService, WorkItemRepository repository) {
            this.storageService = storageService;
            this.repository = repository;
    }
    /*
    @Job(name = "The sample job without variable")
    public void execute() {
        execute("Hello world!");
    }*/
    @Recurring(id = "convert-tmp-job", cron = "* * * * *")
    @Job(name = "Convert tmp directory")
    public void execute() {
        try {
            List<Path> paths = storageService.loadAll("tmp").collect(Collectors.toList());
            if(paths.size() == 0){
                logger.info("nothing to convert...");
                return;
            }
            Path permPath = storageService.getPermanentLocation();
            for(Path path : paths){
                List<WorkItem> pathResults = repository.findByPath(path.toString()); 
                if(pathResults.size() == 0){
                    WorkItem item = new WorkItem(path.toString());
                    repository.save(item);
                    if(Files.exists(path)){
                        logger.info("Converting: "+path.toString()+" ...");
                        ITesseract tess = new Tesseract();
                        List<RenderedFormat> format = new ArrayList<RenderedFormat>();
                        format.add(RenderedFormat.PDF);
                        String newName = permPath.toString() + '\\' + path.getFileName();
                        tess.createDocuments(path.toString(), newName, format);//path.toAbsolutePath().toString()
                        logger.info(path.toString()+" ...complete");
                        logger.info("deleting path...");
                        Files.delete(path);
                        logger.info("deleting item.."+item);
                        repository.delete(item);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error while executing job", e);
        } finally {
            logger.info("job has finished...");
        }
    }
    
    private void convertToTiff(String fileName, String location){ //thanks dyllanwli
        try{
            PDDocument doc = PDDocument.load(new File(fileName));
            PDFRenderer renderer = new PDFRenderer(doc);
            int pageCount = doc.getNumberOfPages();
            ByteArrayOutputStream imageBaos = new ByteArrayOutputStream();
            ImageOutputStream output = ImageIO.createImageOutputStream(imageBaos);
            ImageWriter writer = ImageIO.getImageWritersByFormatName("TIFF").next();
            BufferedImage[] images = new BufferedImage[pageCount];
            writer.setOutput(output);
            ImageWriteParam params = writer.getDefaultWriteParam();
            params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            params.setCompressionType("Deflate");//?
            writer.prepareWriteSequence(null);
            for (int page = 0; page < pageCount; page++){ 
                BufferedImage bim = renderer.renderImageWithDPI(page, 300, ImageType.RGB);
                images[page] = bim;
                IIOMetadata metadata = writer.getDefaultImageMetadata(new ImageTypeSpecifier(bim), params);
                writer.writeToSequence(new IIOImage(bim, null, metadata), params);
            }
            logger.info("imageBaos size: " + imageBaos.size());
            // Finished write to output
            /*
            writer.endWriteSequence();
            boolean b;
            PDPageTree pageTree = doc.getDocumentCatalog().getPages();
            Iterator<PDPage> pages = pageTree.iterator();
            while(pages.hasNext()){
                PDPage page = pages.next();
                BufferedImage bim = page.convertToImage(BufferedImage.TYPE_INT_RGB, 300);
            }
            for (int p = 0; p < pages.size(); ++p)
            {
                // RGB image with 300 dpi
                BufferedImage bim = pages.get(p).convertToImage(BufferedImage.TYPE_INT_RGB, 300);

                // save as TIF with dpi in the metadata
                // PDFBox will choose the best compression for you - here: CCITT G4
                // you need to add jai_imageio.jar to your classpath for this to work
                b = ImageIOUtil.writeImage(bim, "bwpage-" + (p+1) + ".tif", 300);
            }*/
            doc.close();
            writer.dispose();
            ByteArrayOutputStream toFile = getOutput(output);
            //need to get file name
            toFile.writeTo();
        }catch(Exception e){
            logger.info("unable to parse PDF document.. "+e);
        }
    }
    private ByteArrayOutputStream getOutput(ImageOutputStream ios) { //thanks dyllanwli
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // long counter = 0;

        try {
            // System.out.println("getStreamPosition()[BEFORE]=" + ios.getStreamPosition());
            ios.seek(0);
            // System.out.println("getStreamPosition()[AFTER]=" + ios.getStreamPosition());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        while (true) {
            try {
                bos.write(ios.readByte());
                // counter++;
            } catch (EOFException e) {
                System.out.println("End of Image Stream");
                break;
            } catch (IOException e) {
                System.out.println("Error processing the Image Stream");
                break;
            }
        }
        // System.out.println("Total bytes read=" + counter);
        return bos;
    }

}