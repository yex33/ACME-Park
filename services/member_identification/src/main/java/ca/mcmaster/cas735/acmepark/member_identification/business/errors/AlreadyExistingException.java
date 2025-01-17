package ca.mcmaster.cas735.acmepark.member_identification.business.errors;

public class AlreadyExistingException extends  Exception {

    public AlreadyExistingException(String type, String id, String key) {
        super(type + " identified by id (" + id + ") for key (" + key + ") already exists");
    }

}
