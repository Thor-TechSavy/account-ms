package com.quicktransfer.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CreateAccountDto {

    @Schema(description = "first name", requiredMode = Schema.RequiredMode.REQUIRED, example = "TARS")
    private String firstName;

    @Schema(description = "last name", requiredMode = Schema.RequiredMode.REQUIRED, example = "CASE")
    private String lastName;

    @Schema(description = "date of birth", requiredMode = Schema.RequiredMode.REQUIRED, example = "10-10-2000")
    private String dob;

    @Schema(description = "currency", requiredMode = Schema.RequiredMode.REQUIRED, example = "EUR")
    private String currency;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDob() {
        return dob;
    }

    public String getCurrency() {
        return currency;
    }

}
