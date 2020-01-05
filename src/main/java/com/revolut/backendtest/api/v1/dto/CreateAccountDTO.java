package com.revolut.backendtest.api.v1.dto;

public class CreateAccountDTO {
    private boolean credit;

    public CreateAccountDTO() {
    }

    public CreateAccountDTO(boolean credit) {
        this.credit = credit;
    }


    public boolean isCredit() {
        return credit;
    }

    public void setCredit(boolean credit) {
        this.credit = credit;
    }
}
