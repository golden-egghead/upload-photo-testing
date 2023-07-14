package com.goldenegghead.uploadphoto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goldenegghead.uploadphoto.models.Photo;
import com.goldenegghead.uploadphoto.repository.PhotoRepository;

@Service
public class PhotoServices {
	private final PhotoRepository photoRepository;

	@Autowired
	public PhotoServices(PhotoRepository photoRepository) {
		this.photoRepository = photoRepository;
	}

	public Photo uploadPhoto(Photo photo) {
		return photoRepository.save(photo);
	}
}