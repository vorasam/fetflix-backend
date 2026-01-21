package com.dev.cast;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CastService {
    private final CastRepository castRepository;

    public List<Cast> getAll(){
        return castRepository.findAll();
    }

    public Cast createCast(String actorName){
        return castRepository.findByActorName(actorName)
                .orElseGet(() -> castRepository.save(new Cast(null,actorName)));
    }

    public Cast getOrCreate(String actorName){
        return createCast(actorName);
    }
}
