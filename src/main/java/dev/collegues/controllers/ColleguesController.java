package dev.collegues.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import dev.collegues.dto.CreerCollegueReqDto;
import dev.collegues.dto.MessageErreurDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.collegues.exception.CollegueNonTrouveException;
import dev.collegues.dto.CollegueUpdateDto;
import dev.collegues.entites.Collegue;
import dev.collegues.repositories.ColleguesRepository;

import static java.util.stream.Collectors.*;

@RestController
@RequestMapping("/collegues")
public class ColleguesController {

    private ColleguesRepository collegueRepository;

    public ColleguesController(ColleguesRepository collegueRepository) {
        this.collegueRepository = collegueRepository;
    }

    /**
     * GET /collegues?nom=XXX // recupere les matricules dont le nom est passe en
     * parametre
     */
    @GetMapping(
            params = "nom"
    )
    public ResponseEntity<Object> matriculesParNom(@RequestParam String nom) {

        List<String> listeMatricules = collegueRepository.findByNom(nom)
                .stream()
                .map(Collegue::getMatricule)
                .collect(toList());
        return ResponseEntity.status(200).body(listeMatricules);
    }

    /* GET /collegues */
    @GetMapping
    public ResponseEntity<List<Collegue>> collegues() {
        return ResponseEntity.status(200).body(collegueRepository.findAll());
    }

    /**
     * GET : collegues/matricule ------- Recuperation d'un collegue par //
     * son matricule
     */
    @GetMapping("{matricule}")
    public ResponseEntity<Object> collegueParMatricule(@PathVariable @Valid @NotBlank String matricule)
            throws CollegueNonTrouveException {
        Optional<Collegue> collegue = collegueRepository.findByMatricule(matricule);
        if (collegue.isPresent())
            return ResponseEntity.status(200).body(collegue.get());
        else
            return ResponseEntity.status(404).body(new MessageErreurDto("Collegue non trouve"));
    }

    /**
     * POST : collegues/ ------ creation d'un collegueDto
     */
    @PostMapping
    public ResponseEntity<Object> creationClient(@Valid @RequestBody CreerCollegueReqDto collegueDto) {
        Collegue collegue = collegueDto.toCollegue();
        collegue.setMatricule(UUID.randomUUID().toString());
        collegueRepository.save(collegue);
        return ResponseEntity.ok(collegue);
    }

    /**
     * met a jour les donnees d'un client en fonction de son matricule
     */
    @PatchMapping("{matricule}")
    public ResponseEntity<Object> updateClient(@RequestBody @Valid CollegueUpdateDto collegueUpdate, @PathVariable String matricule) {
        Optional<Collegue> collegueOpt = collegueRepository.findByMatricule(matricule);
        if (collegueOpt.isPresent()) {
            Collegue collegue = collegueOpt.get();
            if (collegueUpdate.getPhotoUrl() != null)
                collegue.setPhotoUrl(collegueUpdate.getPhotoUrl());
            if (collegueUpdate.getEmail() != null)
                collegue.setEmail(collegueUpdate.getEmail());
            collegueRepository.save(collegue);
            return ResponseEntity.status(200).body(collegue);
        } else {
            return ResponseEntity.status(404).body(new MessageErreurDto("Erreur : le collegueOpt a mettre a jour n'a pas ete trouve"));
        }
    }

}
