package kr.co.hdtel.facepass.newData.repository;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.Single;
import kr.co.hdtel.facepass.newData.network.RemoteDataManager;
import kr.co.hdtel.server.linux.data.DeleteRecognitionFace;
import kr.co.hdtel.server.linux.data.GetRecognitionFace;
import kr.co.hdtel.server.linux.data.PutRecognitionFace;
import kr.co.hdtel.server.linux.face.FaceRecognition;

public class FaceRepository {
    private final RemoteDataManager mRemoteDataManager;

    public FaceRepository() {
        mRemoteDataManager = new RemoteDataManager();
    }

    public Single<GetRecognitionFace> getFace(Context context, FaceRecognition.Type faceType, String building, String room) {
        return Single.fromObservable(mRemoteDataManager.getFace(context, faceType, building, room));
    }

    public Single<PutRecognitionFace> updateFace(Context context, String fid, String requestId, boolean enable) {
        return Single.fromObservable(mRemoteDataManager.updateFace(context, fid, requestId, enable));
    }

    public Single<DeleteRecognitionFace> deleteFace(Context context, String fid) {
        return Single.fromObservable(mRemoteDataManager.deleteFace(context, fid));
    }
}
