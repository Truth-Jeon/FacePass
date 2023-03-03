package kr.co.hdtel.facepass.newData;

public class FaceItem {
    String faceId;
    String name;
    String createDate;
    String approveYN;
    String requestId;

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getApproveYN() {
        return approveYN;
    }

    public void setApproveYN(String approveYN) {
        this.approveYN = approveYN;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
