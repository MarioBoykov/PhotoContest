package com.telerikacademy.web.photocontest.services;

import com.telerikacademy.web.photocontest.entities.Contest;
import com.telerikacademy.web.photocontest.entities.ContestParticipation;
import com.telerikacademy.web.photocontest.entities.Photo;
import com.telerikacademy.web.photocontest.entities.User;
import com.telerikacademy.web.photocontest.entities.dtos.*;
import com.telerikacademy.web.photocontest.exceptions.AuthorizationException;
import com.telerikacademy.web.photocontest.exceptions.DuplicateEntityException;
import com.telerikacademy.web.photocontest.helpers.PermissionHelper;
import com.telerikacademy.web.photocontest.repositories.ContestRepository;
import com.telerikacademy.web.photocontest.repositories.PhotoRepository;
import com.telerikacademy.web.photocontest.services.contracts.CloudinaryService;
import com.telerikacademy.web.photocontest.services.contracts.ContestParticipationService;
import com.telerikacademy.web.photocontest.services.contracts.PhotoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final ConversionService conversionService;
    private final CloudinaryService cloudinaryService;
    private final ContestRepository contestRepository;
    private final ContestParticipationService contestParticipationService;

    @Override
    public PhotoOutput getPhotoById(UUID id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
        return conversionService.convert(photo, PhotoOutput.class);
    }

    @Override
    public List<PhotoOutput> getAllPhotosOfUser(User user) {
        List<Photo> photos = new ArrayList<>();
        List<Photo> allPhotos = photoRepository.findAllByIsActiveTrue();
        for (Photo photo : allPhotos) {
            if (photo.getUser().equals(user)) {
                photos.add(photo);
            }
        }
        return photos.stream()
                .map(photo -> conversionService.convert(photo, PhotoOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Photo> getAllPhotosEntityOfUser(User user) {
        List<Photo> photos = new ArrayList<>();
        List<Photo> allPhotos = photoRepository.findAllByIsActiveTrue();
        for (Photo photo : allPhotos) {
            if (photo.getUser().equals(user)) {
                photos.add(photo);
            }
        }
        return photos;
    }

    @Override
    public List<PhotoOutput> getAllPhotosOfContest(Contest contest) {
        List<Photo> photos = new ArrayList<>();
        List<Photo> allPhotos = photoRepository.findAllByIsActiveTrue();
        for (Photo photo : allPhotos) {
            if (photo.getContest().equals(contest)) {
                photos.add(photo);
            }
        }
        return photos.stream()
                .map(photo -> conversionService.convert(photo, PhotoOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Photo> getAllPhotosEntityOfContest(Contest contest) {
        List<Photo> photos = new ArrayList<>();
        List<Photo> allPhotos = photoRepository.findAllByIsActiveTrue();
        for (Photo photo : allPhotos) {
            if (photo.getContest().equals(contest)) {
                photos.add(photo);
            }
        }
        return photos;
    }

    @Override
    public List<PhotoOutput> getAll() {
        List<Photo> photos = photoRepository.findAllByIsActiveTrue();
        return photos.stream()
                .map(photo -> conversionService.convert(photo, PhotoOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    public PhotoOutput getByTitle(String title) {
        Photo photo = photoRepository.findByTitleAndIsActiveTrue(title);
        if (photo == null) {
            throw new EntityNotFoundException("Photo with title " + title + " not found.");
        }

        return conversionService.convert(photo, PhotoOutput.class);
    }

    @Override
    public List<PhotoOutput> searchByTitle(String title) {
        return photoRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(photo -> conversionService.convert(photo, PhotoOutput.class))
                .collect(Collectors.toList());
    }

    @Override
    public PhotoIdOutput createPhoto(PhotoInput photoInput, User user) {
        if (photoInput == null) {
            throw new IllegalArgumentException("Photo input cannot be null");
        }

        if (user == null || !user.getIsActive()) {
            throw new AuthorizationException("User is not authorized or inactive");
        }

        Contest contest = contestRepository.findByContestIdAndIsActiveTrue(UUID.fromString(photoInput.getContestId()));
        List<Photo> photos = getAllPhotosEntityOfContest(contest);
        for (Photo photo1 : photos) {
            if (photoInput.getTitle().equals(photo1.getTitle())) {
                throw new DuplicateEntityException("Duplicate photo: A photo with this title already exists in this content .");
            }
        }

        if (!contest.getPhase().getName().equals("Phase 1")) {
            throw new IllegalArgumentException("You can not upload photo after Phase 1!");
        }

        List<ContestParticipation> contestParticipations = contestParticipationService.getAll();

        PhotoIdOutput photoIdOutput = null;

        for (ContestParticipation contestParticipation : contestParticipations) {
            if (contestParticipation.getContest().equals(contest) && contestParticipation.getUser().equals(user)) {
                if (!contestParticipation.isPhotoUploaded()) {

                    Photo photo = Photo.builder()
                            .title(photoInput.getTitle())
                            .story(photoInput.getStory())
                            .contest(contest)
                            .build();
                    photo.setUser(user);
                    photo = photoRepository.save(photo);

                    System.out.println("Creating photo for contest: " + contest.getTitle() + ", Phase:");

                    photoIdOutput = conversionService.convert(photo, PhotoIdOutput.class);
                } else {
                    throw new IllegalArgumentException("You have already upload a photo!");
                }
            }
        }
        return photoIdOutput;
    }

    @Override
    public void softDeletePhoto(UUID id) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));

        photo.setIsActive(false);

        photoRepository.save(photo);
    }

    @Override
    public void updatePhoto(UUID id, PhotoUpdate photoUpdate, User user) {
        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
        PermissionHelper.isSameUser(user, photo.getUser(), "This use is not photo owner!");

        if (photoRepository.existsByTitleAndIsActiveTrue(photoUpdate.getTitle())) {
            throw new DuplicateEntityException("Photo", "title", photo.getTitle());
        }

        photo.setTitle(photoUpdate.getTitle());
        photo.setStory(photoUpdate.getStory());
        photoRepository.save(photo);
    }

    @Override
    public UploadFileOutput uploadPhoto(UploadFileInput uploadFileInput) throws IOException {

        if (uploadFileInput == null || uploadFileInput.getFile() == null || uploadFileInput.getFile().isEmpty()) {
            throw new IllegalArgumentException("Invalid file input");
        }

        String hash = generateSHA256Hash(uploadFileInput.getFile());

        Photo photo = findPhotoEntityById(UUID.fromString(uploadFileInput.getPhotoId()));
        Contest contest = photo.getContest();
        List<Photo> photos = getAllPhotosEntityOfContest(contest);
        for (Photo photo1 : photos) {
            if (hash.equals(photo1.getHash())) {
                throw new DuplicateEntityException("Duplicate photo: A photo already exists in this content .");
            }
        }

        String photoUrl = cloudinaryService.uploadFile(uploadFileInput.getFile());

        if (photoUrl == null || photoUrl.isEmpty()) {
            throw new IOException("Failed to upload photo to Cloudinary");
        }

        photo.setPhotoUrl(photoUrl);
        photo.setHash(hash);

        photoRepository.save(photo);

        return UploadFileOutput.builder().message("Photo uploaded and URL updated successfully!").build();
    }

    private String generateSHA256Hash(MultipartFile file) throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not initialize SHA-256 algorithm", e);
        }

        byte[] hashBytes = digest.digest(file.getBytes());
        StringBuilder hexString = new StringBuilder(2 * hashBytes.length);
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    @Override
    public Photo findPhotoEntityById(UUID id) {
        return photoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found"));
    }
}
