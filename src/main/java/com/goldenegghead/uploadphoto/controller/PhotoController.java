package com.goldenegghead.uploadphoto.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.goldenegghead.uploadphoto.models.Photo;
import com.goldenegghead.uploadphoto.models.ResponseObject;
import com.goldenegghead.uploadphoto.services.PhotoServices;

@RestController
@RequestMapping(path = "/api/photos")
public class PhotoController {
	private final PhotoServices photoServices;

	public PhotoController(PhotoServices photoServices) {
		this.photoServices = photoServices;
	}

	@PostMapping("")
	public ResponseEntity<ResponseObject> uploadPhoto(
			@RequestParam("file") MultipartFile file) {
		try {
			if (file == null || file.isEmpty()) {
				return ResponseEntity.badRequest().body(
						new ResponseObject("ERROR", "Invalid input data", null));
			}

			String fileNameExtension = getFileExtension(file.getOriginalFilename());
			String fileName = UUID.randomUUID().toString() + "." + fileNameExtension;

			String destinationDir = "src" + File.separator + "main" + File.separator + "resources"
					+ File.separator + "static" + File.separator + "photos" + File.separator;

			String filePathString = destinationDir + fileName;
			Path filePath = Paths.get(filePathString);
			Photo photo = new Photo();
			photo.setCreatedDate(LocalDateTime.now());
			photo.setPhoto(fileName);

			Photo savePhoto = photoServices.uploadPhoto(photo);

			Files.createDirectories(filePath.getParent());
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			return ResponseEntity.status(HttpStatus.CREATED).body(
					new ResponseObject("OK", "Article created successfully", savePhoto));
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new ResponseObject("ERROR", "Failed to save article", null));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					new ResponseObject("ERROR", "Internal server error", null));
		}
	}

	private String getFileExtension(String filename) {
		int dotIndex = filename.lastIndexOf('.');
		if (dotIndex > 0 && dotIndex < filename.length() - 1) {
			return filename.substring(dotIndex + 1);
		}
		return "";
	}
}
