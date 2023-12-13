package com.nhanik.poll.services;

import com.nhanik.poll.exception.ResourceNotFoundException;
import com.nhanik.poll.models.Choice;
import com.nhanik.poll.payload.UpdatedTextRequest;
import com.nhanik.poll.repositories.ChoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class ChoiceService {

    private final ChoiceRepository choiceRepository;

    public ChoiceService(ChoiceRepository choiceRepository) {
        this.choiceRepository = choiceRepository;
    }

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
        choice.setChoiceText(request.updatedText());
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
