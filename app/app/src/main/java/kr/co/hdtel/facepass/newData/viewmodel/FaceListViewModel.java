package kr.co.hdtel.facepass.newData.viewmodel;

import android.content.Context;
import android.util.Log;
import androidx.databinding.ObservableArrayList;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import kr.co.hdtel.facepass.newData.FaceItem;
import kr.co.hdtel.facepass.newData.FaceItem2;
import kr.co.hdtel.facepass.newData.repository.FaceRepository;
import kr.co.hdtel.server.linux.data.DeleteRecognitionFace;
import kr.co.hdtel.server.linux.data.GetRecognitionFace;
import kr.co.hdtel.server.linux.data.PutRecognitionFace;
import kr.co.hdtel.server.linux.face.FaceRecognition;

public class FaceListViewModel extends ViewModel {
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private final FaceRepository mFaceRepository = new FaceRepository();
    private final ObservableArrayList<FaceItem> faceItems = new ObservableArrayList<>();
    private final ObservableArrayList<FaceItem2> faceItems2 = new ObservableArrayList<>();
    private MutableLiveData<List<FaceItem>> mLiveDataFace = new MutableLiveData<>();
    private MutableLiveData<List<FaceItem2>> mLiveDataFace2 = new MutableLiveData<>();

    public MutableLiveData<List<FaceItem>> getFaceItems() {
        if(mLiveDataFace == null) {
            mLiveDataFace = new MutableLiveData<>();
        }
        return mLiveDataFace;
    }

    public MutableLiveData<List<FaceItem2>> getFaceItems2() {
        if(mLiveDataFace2 == null) {
            mLiveDataFace2 = new MutableLiveData<>();
        }
        return mLiveDataFace2;
    }

    public void getFace(Context context, FaceRecognition.Type faceType, String building, String room) {
        Disposable disposable = mFaceRepository.getFace(context, faceType, building, room)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetRecognitionFace>() {
                    @Override
                    public void accept(GetRecognitionFace getRecognitionFace) throws Exception {
                        Log.d("뷰 모델", "" +  getRecognitionFace.toString());
                        if(faceType == FaceRecognition.Type.MEMBER) {
                            for (GetRecognitionFace.Face remoteFace :
                                    getRecognitionFace.getMembers()) {
                                FaceItem faceItem = new FaceItem();
                                faceItem.setFaceId(remoteFace.getFaceId());
                                faceItem.setName(remoteFace.getName());
                                long epoch = remoteFace.getCreateDate();
                                Date createDate = new Date(epoch);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd hh:mm");
                                String nowFormatDate = simpleDateFormat.format(createDate);
                                faceItem.setCreateDate(String.valueOf(nowFormatDate));
                                if(remoteFace.isEnabled() == true) {
                                    faceItem.setApproveYN("사용");
                                } else {
                                    faceItem.setApproveYN("사용 안함");
                                }
                                faceItem.setRequestId(remoteFace.getRequestId());
                                faceItems.add(faceItem);
                                mLiveDataFace.setValue(faceItems);
                            }
                        }
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                });
        mCompositeDisposable.add(disposable);
    }

    public void getVisitorFace(Context context, FaceRecognition.Type faceType, String building, String room) {
        Disposable disposable = mFaceRepository.getFace(context, faceType, building, room)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetRecognitionFace>() {
                    @Override
                    public void accept(GetRecognitionFace getRecognitionFace) throws Exception {
                        if (faceType == FaceRecognition.Type.VISITOR) {
                            for(GetRecognitionFace.Face remoteFace :
                                    getRecognitionFace.getMembers()) {
                                FaceItem2 faceItem2 = new FaceItem2();
                                faceItem2.setFaceId(remoteFace.getFaceId());
                                faceItem2.setName(remoteFace.getName());
                                //등록일시
                                long epoch = remoteFace.getCreateDate();
                                Date createDate = new Date(epoch);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy.MM.dd hh:mm");
                                String nowFormatData = simpleDateFormat.format(createDate);
                                faceItem2.setCreateDate(String.valueOf(nowFormatData));
                                //방문 시작일
                                long sdEpoch = remoteFace.getStartDate();
                                Date stDate = new Date(sdEpoch);
                                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yy.MM.dd");
                                String startFormatDate = simpleDateFormat1.format(stDate);
                                //방문 마지막일
                                long edEpoch = remoteFace.getEndDate();
                                Date edDate = new Date(edEpoch);
                                String endFormatDate = simpleDateFormat1.format(edDate);
                                //방문 기간
                                faceItem2.setVisitDays(startFormatDate + " " + "~" + " " + endFormatDate);
                                //사용여부
                                if(remoteFace.isEnabled() == true) {
                                    faceItem2.setApproveYN("사용");
                                } else {
                                    faceItem2.setApproveYN("사용 안함");
                                }
                                //requestId
                                faceItem2.setRequestId(remoteFace.getRequestId());
                                faceItems2.add(faceItem2);
                                mLiveDataFace2.setValue(faceItems2);
                            }
                        }
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                });
        mCompositeDisposable.add(disposable);
    }

    public void updateFace(Context context, String fid, String requestId, boolean enable) {
        Disposable disposable = mFaceRepository.updateFace(context, fid, requestId, enable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<PutRecognitionFace>() {
                    @Override
                    public void accept(PutRecognitionFace putRecognitionFace) throws Exception {
                        putRecognitionFace.getResult();
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                });
        mCompositeDisposable.add(disposable);
    }

    public void deleteFace(Context context, String fid) {
        Disposable disposable = mFaceRepository.deleteFace(context, fid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DeleteRecognitionFace>() {
                    @Override
                    public void accept(DeleteRecognitionFace deleteRecognitionFace) throws Exception {
                        deleteRecognitionFace.getResult();
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mCompositeDisposable.dispose();
    }
}
