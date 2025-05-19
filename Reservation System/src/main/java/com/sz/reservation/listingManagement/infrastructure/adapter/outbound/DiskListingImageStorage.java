package com.sz.reservation.listingManagement.infrastructure.adapter.outbound;

import com.sz.reservation.globalConfiguration.exception.DirectoryCreationException;
import com.sz.reservation.globalConfiguration.exception.FileWritingException;
import com.sz.reservation.listingManagement.application.useCase.listing.ListingImageState;
import com.sz.reservation.listingManagement.application.useCase.listing.StorageImageProcessingState;
import com.sz.reservation.listingManagement.domain.port.outbound.ListingImageStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class DiskListingImageStorage implements ListingImageStorage {
    private static final Logger log = LogManager.getLogger(DiskListingImageStorage.class);
    private final int MAX_PHOTO_SIZE = 15728640; // 15 MB
    private final String SUFFIX = "listing_";

    private String basePath;

    public DiskListingImageStorage(@Value("${local_listingPhotos_storage.location}") String basePath) {
        this.basePath = basePath;
    }

    @Override
    public StorageImageProcessingState store(String originalFilename,String hostId,String listingId,String photoId,BufferedInputStream bufferedInputStream) {
        String generatedFilename = generateFilename(photoId,originalFilename);
        String generatedDirectory = generateDirectoryIfNotExists(listingId,hostId);
        String generatedPath = generatedDirectory.concat(File.separator).concat(generatedFilename);

        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(generatedPath));
            bufferedInputStream){
            int fileSize = 0;
            byte data[] = new byte[1024 * 64];
            int bytesRead = bufferedInputStream.read(data);
            while (bytesRead != -1) {
                if (fileSize > MAX_PHOTO_SIZE) {
                    return new StorageImageProcessingState(generatedPath, ListingImageState.DISCARDED);
                }
                outputStream.write(data, 0, bytesRead);
                bytesRead = bufferedInputStream.read(data);
                fileSize += bytesRead;
            }
        } catch (IOException e) {

            throw new FileWritingException("Failure while trying to write listing image to path: "+generatedPath + " hostId: "+hostId + " listingId: "+listingId,e);
        }
        return new StorageImageProcessingState(generatedPath, ListingImageState.CORRECT);
    }

    @Override
    public void delete(String filepath) {
        File fileToDelete = new File(filepath);
        boolean success = fileToDelete.delete();
        if (!success) throw new FileWritingException("Error deleting file with path: "+ filepath);
    }


    private String generateFilename(String photoId,String originalFilename){
        String fileExtension = getExtension(originalFilename);
        String filename = SUFFIX.concat(photoId).concat(".").concat(fileExtension);
        return filename;
    }

    private String getExtension(String filename){
        int index = filename.lastIndexOf("."); // get index of file extension
        String fileExtension = filename.substring(index + 1); // get file extension
        return fileExtension;
    }

    private String generateDirectoryIfNotExists(String listingId, String hostId){
        String path = basePath.concat(hostId).concat(File.separator).concat(listingId);
        File directory = new File(path);
        if (!directory.exists()){
            boolean success = directory.mkdirs();
            if (!success) throw new DirectoryCreationException("Failed to create directory: "+directory.getAbsolutePath());
        }
        return directory.getAbsolutePath();
    }

}
