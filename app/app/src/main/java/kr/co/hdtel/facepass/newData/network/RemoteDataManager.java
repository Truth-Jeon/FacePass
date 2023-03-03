package kr.co.hdtel.facepass.newData.network;

import android.content.Context;

import io.reactivex.Observable;
import kr.co.hdtel.server.linux.data.DeleteRecognitionFace;
import kr.co.hdtel.server.linux.data.GetRecognitionFace;
import kr.co.hdtel.server.linux.data.PutRecognitionFace;
import kr.co.hdtel.server.linux.face.FaceRecognition;

public class RemoteDataManager {

    private static class SingletonHolder {
        private static final RemoteDataManager mInstance = RemoteDataManager.getInstance();
    }

    public static RemoteDataManager getInstance() {
        return SingletonHolder.mInstance;
    }

    public Observable<GetRecognitionFace> getFace(Context context, FaceRecognition.Type faceType, String building, String room) {
        return FaceRecognition.getList(context, faceType, building, room);
    }

    public Observable<PutRecognitionFace> updateFace(Context context, String fid, String requestId, boolean enable) {
        return FaceRecognition.changePermission(context,fid, requestId, enable);
    }

    public Observable<DeleteRecognitionFace> deleteFace(Context context, String fid) {
        return FaceRecognition.unregister(context, fid);
    }
}
