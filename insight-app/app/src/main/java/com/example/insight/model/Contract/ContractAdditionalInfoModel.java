package com.example.insight.model.Contract;

import org.json.JSONException;
import org.json.JSONObject;

public class ContractAdditionalInfoModel {
    ContractTermsModel contractTerms;

    public ContractAdditionalInfoModel(JSONObject additionalInfo){
        try{
            contractTerms = new ContractTermsModel(additionalInfo.getJSONObject("contractTerms"));

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public ContractTermsModel getContractTerms() {
        return contractTerms;
    }

    @Override
    public String toString() {
        return "ContractAdditionalInfoModel{" +
                "contractTerms=" + contractTerms +
                '}';
    }
}
