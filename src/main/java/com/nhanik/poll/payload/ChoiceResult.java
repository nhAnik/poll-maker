package com.nhanik.poll.payload;

public class ChoiceResult {
    private Long choiceId;
    private String choiceText;
    private long voteCount;

    public ChoiceResult() {}

    public ChoiceResult(Long choiceId, String choiceText, long voteCount) {
        this.choiceId = choiceId;
        this.choiceText = choiceText;
        this.voteCount = voteCount;
    }

    public Long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Long choiceId) {
        this.choiceId = choiceId;
    }

    public String getChoiceText() {
        return choiceText;
    }

    public void setChoiceText(String choiceText) {
        this.choiceText = choiceText;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }
}
