package com.goldenegghead.uploadphoto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.goldenegghead.uploadphoto.models.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {
	
}

