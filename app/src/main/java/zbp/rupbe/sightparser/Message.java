package zbp.rupbe.sightparser;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tebbe Ubben on 04.07.2017.
 */
public abstract class Message implements Parcelable {

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            try {
                Message message = (Message) Class.forName(in.readString()).newInstance();
                message.readFromParcel(in);
                return message;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getMessageClass().getName());
    }

    public void readFromParcel(Parcel in) {
    }

    public abstract Class getMessageClass();
}
