package com.nhanik.poll.services;

import com.nhanik.poll.exception.ResourceNotFoundException;
import com.nhanik.poll.models.Choice;
import com.nhanik.poll.payload.UpdatedTextRequest;
import com.nhanik.poll.repositories.ChoiceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChoiceService {

    private static final Logger logger = LoggerFactory.getLogger(ChoiceService.class);

    private final ChoiceRepository choiceRepository;

    public Choice getPollChoice(Long cid) {
        return choiceRepository
                .findById(cid)
                .orElseThrow(() -> new ResourceNotFoundException("Choice", cid));
    }

    public Choice createPollChoice(Choice choice) {
        return choiceRepository.save(choice);
    }

    // called from a transactional
    public Choice updatePollChoice(Long cid, UpdatedTextRequest request) {
        Choice choice = getPollChoice(cid);
        choice.setChoiceText(request.getUpdatedText());
        return choice;
    }

    public void deletePollChoice(Long cid) {
        if (!choiceRepository.existsById(cid)) {
            throw new ResourceNotFoundException("Choice", cid);
        }
        choiceRepository.deleteById(cid);
    }

    public void incrementPollVoteCount(Long cid) {
        choiceRepository.incrementVoteCountByChoiceId(cid);
    }
}
