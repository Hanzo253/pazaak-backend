package com.capstone.pazaak.service;

import com.capstone.pazaak.exceptions.InformationExistsException;
import com.capstone.pazaak.exceptions.InformationNotFoundException;
import com.capstone.pazaak.model.Match;
import com.capstone.pazaak.repository.MatchRepository;
import com.capstone.pazaak.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class MatchService {
    private MatchRepository matchRepository;
    private UserService userService;

    @Autowired
    public void setMatchRepository(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    // create a match
    public Match createMatch(Match matchObject) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Match match = matchRepository.findByIdAndUserId(matchObject.getId(), userDetails.getUser().getId());

        if (match != null) {
            // throw exception if the match already exists
            throw new InformationExistsException("match already exists.");
        } else {
            matchObject.setUser(userDetails.getUser());
            return matchRepository.save(matchObject);
        }
    }

    // returns all the user's matches
    public List<Match> getAllMatches() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Match> matches = matchRepository.findByUserId(userDetails.getUser().getId());

        if (matches.isEmpty()) {
            // throw exception when the match list is empty
            throw new InformationNotFoundException("no match found for user id " + userDetails.getUser().getId());
        } else {
            return matches;
        }
    }

    // returns a user's match based on the match id
    public Optional<Match> getMatch(Long matchId) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Match match = matchRepository.findByIdAndUserId(matchId, userDetails.getUser().getId());
        if (match == null) {
            // throw exception if the pokemon id does not exist
            throw new InformationNotFoundException("match with id " + matchId + " not found.");
        } else {
            return Optional.ofNullable(match);
        }
    }

    // updates the match's information
    public Match updateMatch(Long matchId, @RequestBody Match matchObject) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Match match = matchRepository.findByIdAndUserId(matchId, userDetails.getUser().getId());

        if (match == null) {
            throw new InformationNotFoundException("match with id " + matchId + " not found");
        } else {
            match.setUser(userDetails.getUser());
            match.setResult(matchObject.getResult());
            match.setMatchDate(matchObject.getMatchDate());
            return matchRepository.save(match);
        }
    }

    // delete a user's match based on the match id given
    public void deleteMatch(Long matchId) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Match match = matchRepository.findByIdAndUserId(matchId, userDetails.getUser().getId());
        if (match == null) {
            // throw exception if the match id does not exist
            throw new InformationNotFoundException("match with id " + matchId + " not found.");
        } else {
            matchRepository.deleteById(matchId);
        }
    }

    // delete all the user's match
    public void deleteAllMatches() {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Match> matches = matchRepository.findByUserId(userDetails.getUser().getId());

        if (matches.isEmpty()) {
            // throw exception when the matches list is empty
            throw new InformationNotFoundException("no matches found for user id " + userDetails.getUser().getId());
        } else {
            matchRepository.deleteAll();
        }
    }
}
