package com.capstone.pazaak.controller;

import com.capstone.pazaak.model.Match;
import com.capstone.pazaak.service.MatchService;
import com.capstone.pazaak.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api") // means http://localhost:9092/api/
public class MatchController {
    private UserService userService;
    private MatchService matchService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMatchService(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/match/")
    public Match createMatch(@RequestBody Match matchObject) {
        System.out.println("creating new match....");
        return matchService.createMatch(matchObject);
    }

    @GetMapping("/match/")
    public List<Match> getAllMatches() {
        System.out.println("getting all matches...");
        return matchService.getAllMatches();
    }

    @GetMapping("/match/{matchId}")
    public Optional<Match> getMatch(@PathVariable(value = "matchId") Long matchId) {
        System.out.println("getting match with an id of " + matchId);
        return matchService.getMatch(matchId);
    }

    @PutMapping("/match/{matchId}")
    public Match updateMatch(@PathVariable(value = "matchId") Long matchId, @RequestBody Match matchObject) {
        System.out.println("updating match with an id of " + matchId);
        return matchService.updateMatch(matchId, matchObject);
    }

    @DeleteMapping("/match/{matchId}")
    public ResponseEntity<String> deleteMatch(@PathVariable(value = "matchId") Long matchId) {
        System.out.println("deleting match with id " + matchId);
        matchService.deleteMatch(matchId);
        return ResponseEntity.ok().body("Deleting match with id " + matchId);
    }

    @DeleteMapping("/pokemon/")
    public ResponseEntity<String> deleteAllMatches() {
        System.out.println("deleting all matches...");
        matchService.deleteAllMatches();
        return ResponseEntity.ok().body("Deleting all matches...");
    }
}
