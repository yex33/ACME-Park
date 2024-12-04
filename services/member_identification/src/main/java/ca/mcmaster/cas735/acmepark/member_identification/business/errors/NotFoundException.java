package ca.mcmaster.cas735.acmepark.member_identification.business.errors;

public class NotFoundException extends Exception {

    public NotFoundException(String type, String id, String key) {
        super("No " + type + " identified by id (" + id + ") for key (" + key + ")");
    }

}