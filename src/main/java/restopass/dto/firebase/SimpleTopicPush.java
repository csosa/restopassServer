package restopass.dto.firebase;

public class SimpleTopicPush<T> {
    private String to;
    private T data;

    public String getTo() {
        return to;
    }


    public void setTo(String to) {
        this.to = "/topics/" + to;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

}
