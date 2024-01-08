package org.national.transfer.backoffice.service.utils;

public class Constants {

    public static final String TRANSFER_AMOUNT = "transferAmount";
    public static final String IDENTITY_NUMBER = "identityNumber";
    public static final String CODE_AGENT = "codeAgent";
    public static final String TOTAL_OPERATION_AMOUNT = "totalTransferAmount";
    public static final String LAST_UPDATE = "lastModifiedDate";
    public static final String CREATION_DATE = "creationDate";
    public static final String FEE_AMOUNT = "feeTransferAmount";
    public static final String WITH_NOTIFICATION = "notification";
    public static final String BENEFICIARY_EMAIL = "beneficiaryEmail";
    public static final String CLIENT_FULL_NAME = "clientFullName";
    public static final String CLIENT_EMAIL = "clientEmail";
    public static final String BENEFICIARY_LASTNAME = "beneficiaryLastName";
    public static final String BENEFICIARY_FIRSTNAME = "beneficiaryFirstName";
    public static final String WALLET_EMISSION_TYPE = "byWallet";
    public static final String CASH_EMISSION_TYPE = "bycash";
    public static final String M_WALLET_EMISSION_TYPE = "byM-Wallet";
    public static final String CANAL_ID = "canalId";
    public static final String REVERSE_MOTIF = "motif";
    //Exception
    public static final String EX_OTP_NOT_FOUND = "OTP_NOT_FOUND";
    public static final String EX_OTP_INCORRECT = "OTP_INCORRECT";
    public static final String EX_AMOUNT_IS_HIGH = "AMOUNT_IS_HIGH";
    public static final String EX_TRANSFER_NOT_FOUND = "TRANSFER_NOT_FOUND";

    //Fee Mode
    public static final String ON_PUBLISHER = "ONLY_ME";
    public static final String ON_SUBSCRIBER = "ONLY_HIM";

    //Transaction Status
    public static final String TO_SERVE = "to serve";
    public static final String SERVED = "served";
    public static final String BLOCKED = "blocked";
    public static final String RETURNED = "returned";
    public static final String REVERSE = "reversed";
    public static final String DORMANT = "dormant";
    public static final String UNLOCK_TO_SERVE = "unlock to serve";
}
