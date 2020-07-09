package restopass.exception;

public class ErrorUploadingImgFirebaseException extends RestoPassException {
    public ErrorUploadingImgFirebaseException(String name){
        super(ErrorCode.BAD_IMG_REQUEST_TO_FIREBASE, name);
    }
}
