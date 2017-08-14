package zbp.rupbe.sightparser;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by Tebbe Ubben on 30.06.2017.
 */
public class InsightError implements Parcelable {

    private ErrorType errorType;
    private String errorMessage;

    public InsightError(ErrorType errorType, String errorMessage) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(errorType.name());
        dest.writeString(errorMessage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<InsightError> CREATOR = new Creator<InsightError>() {
        @Override
        public InsightError createFromParcel(Parcel in) {
            return new InsightError(ErrorType.valueOf(in.readString()), in.readString());
        }

        @Override
        public InsightError[] newArray(int size) {
            return new InsightError[size];
        }
    };

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
