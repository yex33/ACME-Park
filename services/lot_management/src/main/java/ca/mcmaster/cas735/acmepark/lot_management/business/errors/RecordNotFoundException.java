package ca.mcmaster.cas735.acmepark.lot_management.business.errors;

public class RecordNotFoundException extends RuntimeException {
  public RecordNotFoundException(String message) {
    super(message);
  }
}
