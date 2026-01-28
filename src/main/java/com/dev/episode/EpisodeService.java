package com.dev.episode;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dev.cast.CastService;
import com.dev.season.Season;
import com.dev.exception.ResourceNotFoundException;
import com.dev.season.SeasonRepository;
import com.dev.genre.GenreService;
import com.dev.scene.SceneService;
import com.dev.show.Show;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EpisodeService {

    private final EpisodeRepository epRepo;
    private final SeasonRepository seasonRepo;
    private final CastService castService;
    private final GenreService genreService;
    private final SceneService sceneService;
    private final Cloudinary cloudinary;
    private final AmazonS3 storjClient;

    @Value("fetflix-video")
    private String bucketName ;

    public Episode addEpisode(
            Long seasonId,
            EpisodeRequest req,
            MultipartFile video,
            MultipartFile image
    ) throws IOException {

        Season season = seasonRepo.findById(seasonId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Season not found: " + seasonId));

        Episode ep = new Episode();
        ep.setSeason(season);

        mapFields(ep, req, video, image);

        return epRepo.save(ep);
    }


    public List<Episode> getEpisodes(Long seasonId) {
        Season season = seasonRepo.findById(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("Season not found: " + seasonId));

        return epRepo.findAll()
                .stream()
                .filter(e -> e.getSeason().getId().equals(seasonId))
                .collect(Collectors.toList());
    }

    public Episode getEpisodeById(Long episodeId){
        return epRepo.findById(episodeId).orElseThrow(() -> new ResourceNotFoundException("Episode not found: "+episodeId));
    }

    // UPDATE EPISODE
    public Episode updateEpisode(
            Long episodeId,
            EpisodeRequest req,
            MultipartFile video,
            MultipartFile image
    ) throws IOException {

        Episode ep = epRepo.findById(episodeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Episode not found: " + episodeId));

        mapFields(ep, req, video, image);

        return epRepo.save(ep);
    }
    private void mapFields(
            Episode ep,
            EpisodeRequest req,
            MultipartFile file,
            MultipartFile image
    ) throws IOException {

        // ---------- VIDEO UPLOAD ----------
        if (file != null && !file.isEmpty()) {

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = "videos/" + UUID.randomUUID() + extension;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType("video/mp4");

            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName,
                    fileName,
                    file.getInputStream(),
                    metadata
            );

            // ✅ THIS WAS MISSING
            storjClient.putObject(putObjectRequest);

            // ✅ store ONLY the key
            ep.setEpisodeVideo(fileName);
        }

        // ---------- IMAGE UPLOAD ----------
        if (image != null && !image.isEmpty()) {
            Map<?, ?> imageUpload = cloudinary.uploader().upload(
                    image.getBytes(),
                    ObjectUtils.asMap("resource_type", "image")
            );
            ep.setPoster(imageUpload.get("secure_url").toString());
        }

        // ---------- BASIC ----------
        ep.setEpisodeNumber(req.getEpisodeNumber());
        ep.setTitle(req.getTitle());
        ep.setDescription(req.getDescription());
        ep.setReleaseYear(req.getReleaseYear());
        ep.setAgeRating(req.getAgeRating());
        ep.setEnable(req.isEnable());

        // ---------- CAST ----------
        ep.setCastList(req.getCast().stream()
                .map(castService::getOrCreate)
                .collect(Collectors.toSet()));

        // ---------- GENRES ----------
        ep.setGenres(req.getGenres().stream()
                .map(genreService::getOrCreate)
                .collect(Collectors.toSet()));

        // ---------- SCENES ----------
        ep.setScenes(req.getScenes().stream()
                .map(sceneService::getOrCreate)
                .collect(Collectors.toSet()));
    }


    // DELETE EPISODE
    public void deleteEpisode(Long episodeId) {
        Episode ep = epRepo.findById(episodeId)
                .orElseThrow(() -> new ResourceNotFoundException("Episode not found: " + episodeId));

        epRepo.delete(ep);
    }

    public void episodeEnabled(Long id) {
        Episode episode = epRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Episode not found"));

        Season season = episode.getSeason();
        Show show = season.getShow();

        boolean newStatus = !episode.isEnable();

        // 🔒 RULE: cannot ENABLE episode if season or show disabled
        if (newStatus && (!season.isEnabled() || !show.isEnabled() )) {
            throw new IllegalStateException(
                    "Cannot enable episode while season or show is disabled"
            );
        }

        episode.setEnable(newStatus);
        epRepo.save(episode);
    }

}
