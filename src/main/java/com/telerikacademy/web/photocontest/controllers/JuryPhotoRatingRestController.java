package com.telerikacademy.web.photocontest.controllers;

//import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingInput;
//import com.telerikacademy.web.photocontest.entities.dtos.JuryPhotoRatingOutput;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class JuryPhotoRatingRestController {

//    private final JuryPhotoRatingServiceImpl juryPhotoRatingService;
//
//    @Autowired
//    public JuryPhotoRatingController(JuryPhotoRatingServiceImpl juryPhotoRatingService) {
//        this.juryPhotoRatingService = juryPhotoRatingService;
//    }
//
//    @GetMapping("/photo/{photoId}")
//    public ResponseEntity<List<JuryPhotoRatingOutputDto>> getRatingsForPhoto(@PathVariable UUID photoId) {
//        List<JuryPhotoRatingOutputDto> ratings = juryPhotoRatingService.getAllRatingsForPhoto(photoId);
//        return ResponseEntity.ok(ratings);
//    }
//
//    @PostMapping
//    public ResponseEntity<JuryPhotoRatingOutputDto> addRating(@RequestBody JuryPhotoRatingInputDto dto) {
//        JuryPhotoRatingOutputDto rating = juryPhotoRatingService.addRating(dto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(rating);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteRating(@PathVariable UUID id) {
//        juryPhotoRatingService.softDeleteRating(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<JuryPhotoRatingOutputDto> updateRating(@PathVariable UUID id, @RequestBody JuryPhotoRatingInputDto dto) {
//        JuryPhotoRatingOutputDto updatedRating = juryPhotoRatingService.updateRating(id, dto);
//        return ResponseEntity.ok(updatedRating);
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<JuryPhotoRatingOutputDto>> getRatingsByUser(@PathVariable UUID userId) {
//        List<JuryPhotoRatingOutputDto> ratings = juryPhotoRatingService.getRatingsByUser(userId);
//        return ResponseEntity.ok(ratings);
//    }
//
//
//    @GetMapping("/photo/{photoId}/average-score")
//    public ResponseEntity<Double> getAverageScoreForPhoto(@PathVariable UUID photoId) {
//        double averageScore = juryPhotoRatingService.getAverageScoreForPhoto(photoId);
//        return ResponseEntity.ok(averageScore);
//    }


}
